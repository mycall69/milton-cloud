/*
 * Copyright 2012 McEvoy Software Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.milton.cloud.server.apps.email;

import io.milton.cloud.server.db.EmailAttachment;
import io.milton.cloud.server.db.EmailItem;
import io.milton.common.BufferingOutputStream;
import io.milton.context.Context;
import io.milton.context.Executable;
import io.milton.context.Executable2;
import static io.milton.context.RequestContext._;
import io.milton.mail.Attachment;
import io.milton.mail.InputStreamConsumer;
import io.milton.mail.MailboxAddress;
import io.milton.mail.StandardMessage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.hashsplit4j.api.BlobStore;
import org.hashsplit4j.api.Combiner;
import org.hashsplit4j.api.HashStore;

import io.milton.context.RootContext;
import io.milton.vfs.db.utils.SessionManager;
import java.io.IOException;
import javax.mail.Part;
import org.apache.commons.io.IOUtils;
import org.hashsplit4j.api.Fanout;
import org.hashsplit4j.api.Parser;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author brad
 */
public class EmailItemStandardMessage implements StandardMessage {

    private static final Logger log = LoggerFactory.getLogger(EmailItemStandardMessage.class);
    private final EmailItem emailItem;
    private List<Attachment> attachments = new ArrayList<>();
    private final RootContext rootContext;
    private final HashStore hashStore;
    private final BlobStore blobStore;
    private final SessionManager sessionManager;

    public EmailItemStandardMessage(EmailItem emailItem, HashStore hashStore, BlobStore blobStore, RootContext rootContext, SessionManager sessionManager) {
        this.emailItem = emailItem;
        this.rootContext = rootContext;
        this.hashStore = hashStore;
        this.blobStore = blobStore;
        this.sessionManager = sessionManager;
        if (emailItem.getAttachments() != null) {
            for (EmailAttachment att : emailItem.getAttachments()) {
                Fanout fanout = hashStore.getFileFanout(att.getFileHash());
                MyAttachment a = new MyAttachment(att, (int) fanout.getActualContentLength());
                attachments.add(a);
            }
        } else {
            log.trace("EmailItemStandardMessage - no attachments");
        }
    }

    @Override
    public void addAttachment(String name, String ct, String contentId, InputStream in) {
        if (emailItem.getAttachments() == null) {
            emailItem.setAttachments(new ArrayList<EmailAttachment>());
        }

        Parser parser = new Parser();
        String fileHash = null;
        try {
            fileHash = parser.parse(in, hashStore, blobStore);
        } catch (IOException ex) {
            throw new RuntimeException("Exception adding attchment", ex);
        }
        Session session = SessionManager.session();
        //session.save(emailItem); // must be saved before add attachment
        emailItem.addAttachment(name, fileHash, ct, null, session);

    }

    @Override
    public List<StandardMessage> getAttachedMessages() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void setAttachedMessages(List<StandardMessage> attachedMessages) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSubject() {
        return emailItem.getSubject();
    }

    @Override
    public MailboxAddress getFrom() {
        return MailboxAddress.parse(emailItem.getFromAddress());
    }

    @Override
    public List<Attachment> getAttachments() {
        return attachments;
    }

    @Override
    public void setFrom(MailboxAddress from) {
        emailItem.setFromAddress(from.toPlainAddress());
    }

    @Override
    public MailboxAddress getReplyTo() {
        return MailboxAddress.parse(emailItem.getReplyToAddress());
    }

    @Override
    public void setReplyTo(MailboxAddress replyTo) {
        emailItem.setReplyToAddress(replyTo.toPlainAddress());
    }

    @Override
    public void setSubject(String subject) {
        emailItem.setSubject(subject);
    }

    @Override
    public String getHtml() {
        return emailItem.getHtml();
    }

    @Override
    public void setHtml(String html) {
        emailItem.setHtml(html);
    }

    @Override
    public String getText() {
        return emailItem.getText();
    }

    @Override
    public void setText(String text) {
        emailItem.setText(text);
    }

    @Override
    public int getSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSize(int size) {

    }

    @Override
    public void setDisposition(String disposition) {
        emailItem.setDisposition(disposition);
    }

    @Override
    public String getDisposition() {
        return emailItem.getDisposition();
    }

    @Override
    public void setEncoding(String encoding) {
        emailItem.setEncoding(encoding);
    }

    @Override
    public String getEncoding() {
        return emailItem.getEncoding();
    }

    @Override
    public void setContentLanguage(String contentLanguage) {
        emailItem.setContentLanguage(contentLanguage);
    }

    @Override
    public String getContentLanguage() {
        return emailItem.getContentLanguage();
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        // TODO: would be nice to store these somewhere. Perhaps convert to a string and store in blobstore
    }

    @Override
    public List<MailboxAddress> getTo() {
        if (emailItem.getToList() == null) {
            return Collections.EMPTY_LIST;
        }
        String[] arr = emailItem.getToList().split(",");
        List<MailboxAddress> list = new ArrayList<>();
        for (String s : arr) {
            s = s.trim();
            list.add(MailboxAddress.parse(s));
        }
        return list;
    }

    @Override
    public void setTo(List<MailboxAddress> to) {
        StringBuilder sb = null;
        for (MailboxAddress add : to) {
            if (sb == null) {
                sb = new StringBuilder();
            } else {
                sb.append(",");
            }
            sb.append(add.toPlainAddress());
        }
        if (sb != null) {
            emailItem.setToList(sb.toString());
        }
    }

    @Override
    public List<MailboxAddress> getCc() {
        if (emailItem.getCcList() == null) {
            return Collections.EMPTY_LIST;
        }
        String[] arr = emailItem.getCcList().split(",");
        List<MailboxAddress> list = new ArrayList<>();
        for (String s : arr) {
            s = s.trim();
            list.add(MailboxAddress.parse(s));
        }
        return list;

    }

    @Override
    public void setCc(List<MailboxAddress> cc) {
        StringBuilder sb = null;
        for (MailboxAddress add : cc) {
            if (sb == null) {
                sb = new StringBuilder();
            } else {
                sb.append(",");
            }
            sb.append(add.toPlainAddress());
        }
        if (sb != null) {
            emailItem.setCcList(sb.toString());
        }
    }

    @Override
    public List<MailboxAddress> getBcc() {
        if (emailItem.getBccList() == null) {
            return Collections.EMPTY_LIST;
        }
        String[] arr = emailItem.getBccList().split(",");
        List<MailboxAddress> list = new ArrayList<>();
        for (String s : arr) {
            s = s.trim();
            list.add(MailboxAddress.parse(s));
        }
        return list;
    }

    @Override
    public void setBcc(List<MailboxAddress> bcc) {
        StringBuilder sb = null;
        for (MailboxAddress add : bcc) {
            if (sb == null) {
                sb = new StringBuilder();
            } else {
                sb.append(",");
            }
            sb.append(add.toPlainAddress());
        }
        if (sb != null) {
            emailItem.setBccList(sb.toString());
        }
    }

    @Override
    public StandardMessage instantiateAttachedMessage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public class MyAttachment implements Attachment {

        private final EmailAttachment attachment;
        private int size;

        public MyAttachment(EmailAttachment attachment, int size) {
            this.attachment = attachment;
            this.size = size;
        }

        @Override
        public String getName() {
            return attachment.getFileName();
        }

        @Override
        public void useData(final InputStreamConsumer exec) {
            rootContext.execute(new Executable2() {
                @Override
                public void execute(Context context) {
                    System.out.println("MyAttachment - getInputStream2 - " + attachment.getFileName());
                    sessionManager.open();
                    BufferingOutputStream bout = null;
                    try {
                        Fanout fanout = hashStore.getFileFanout(attachment.getFileHash());
                        Combiner combiner = new Combiner();
                        bout = new BufferingOutputStream(100000);

                        combiner.combine(fanout.getHashes(), _(HashStore.class), _(BlobStore.class), bout);
                    } catch (Throwable ex) {
                        log.error("Exception generating attachment", ex);
                        throw new RuntimeException(ex);
                    } finally {
                        sessionManager.close();
                        IOUtils.closeQuietly(bout);
                    }
                    exec.execute(bout.getInputStream());

                }
            });

            rootContext.execute(new Executable2() {
                @Override
                public void execute(Context context) {
                    Fanout fanout = hashStore.getFileFanout(attachment.getFileHash());
                    Combiner combiner = new Combiner();
                    BufferingOutputStream bout = new BufferingOutputStream(100000);
                    try {
                        combiner.combine(fanout.getHashes(), _(HashStore.class), _(BlobStore.class), bout);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } finally {
                        IOUtils.closeQuietly(bout);
                    }
                    exec.execute(bout.getInputStream());

                }
            });
        }

        @Override
        public InputStream getInputStream() {
            System.out.println("MyAttachment - getInputStream - " + attachment.getFileName());
            return rootContext.execute(new Executable<InputStream>() {
                @Override
                public InputStream execute(Context context) {
                    System.out.println("MyAttachment - getInputStream2 - " + attachment.getFileName());
                    sessionManager.open();
                    BufferingOutputStream bout = null;
                    try {
                        Fanout fanout = hashStore.getFileFanout(attachment.getFileHash());
                        Combiner combiner = new Combiner();
                        bout = new BufferingOutputStream(100000);

                        combiner.combine(fanout.getHashes(), _(HashStore.class), _(BlobStore.class), bout);
                    } catch (Throwable ex) {
                        log.error("Exception generating attachment", ex);
                        throw new RuntimeException(ex);
                    } finally {
                        sessionManager.close();
                        IOUtils.closeQuietly(bout);
                    }
                    System.out.println("EmailItemStandardMessage - getInputStram: " + bout.getSize() + "bytes expected=" + size);
                    return bout.getInputStream();

                }
            });
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public String getContentId() {
            return null; // only return not null for inline!!
        }

        @Override
        public String getContentType() {
            return attachment.getContentType();
        }

        @Override
        public String getDisposition() {
            if (attachment.getDisposition() != null) {
                return attachment.getDisposition();
            } else {
                return Part.ATTACHMENT;
            }
        }
    }
}
