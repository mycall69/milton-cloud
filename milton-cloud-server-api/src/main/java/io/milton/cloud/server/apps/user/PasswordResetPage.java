/*
 * Copyright (C) 2012 McEvoy Software Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.milton.cloud.server.apps.user;

import io.milton.cloud.common.CurrentDateService;
import io.milton.cloud.server.apps.website.WebsiteRootFolder;
import io.milton.cloud.server.db.EmailItem;
import io.milton.cloud.server.db.PasswordReset;
import io.milton.cloud.server.web.CommonCollectionResource;
import io.milton.cloud.server.web.JsonResult;
import io.milton.cloud.server.web.RootFolder;
import io.milton.cloud.server.web.TemplatedHtmlPage;
import io.milton.cloud.server.web.WebUtils;
import io.milton.http.FileItem;
import io.milton.http.Range;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.ConflictException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.http.exceptions.NotFoundException;
import io.milton.resource.PostableResource;
import io.milton.vfs.db.Organisation;
import io.milton.vfs.db.Profile;
import io.milton.vfs.db.Website;
import io.milton.vfs.db.utils.SessionManager;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.Transaction;

import static io.milton.context.RequestContext._;

/**
 * 
 *
 * @author brad
 */
public class PasswordResetPage extends TemplatedHtmlPage implements PostableResource {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PasswordResetPage.class);
    
    private JsonResult jsonResult;
    
    private PasswordReset passwordReset;
    
    public PasswordResetPage(String name, CommonCollectionResource parent) {
        super(name, parent, "user/passwordReset", "Password reset");
    }

    @Override
    public String processForm(Map<String, String> parameters, Map<String, FileItem> files) throws BadRequestException, NotAuthorizedException, ConflictException {
        log.info("processform");
        Session session = SessionManager.session();
        Transaction tx = session.beginTransaction();
        if( parameters.containsKey("resetEmail")) {
            try {
                String email = parameters.get("resetEmail");
                if( createReset(email, session) ) {
                    tx.commit();
                    log.info("processform: sent ok");
                    jsonResult = new JsonResult(true);
                } else {
                    log.info("processform: coult not find user");
                    jsonResult = new JsonResult(false, "Could not locate email address");
                }
            } catch (IOException ex) {
                log.error("Exception generating password reset email", ex);
                jsonResult = new JsonResult(false, "There was an error generating the email. Please contact the administrator, or try again later");
            }
        }
        return null;
    }

    @Override
    public void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType) throws IOException, NotAuthorizedException, BadRequestException, NotFoundException {
        if( jsonResult != null ) {
            jsonResult.write(out);
        } else {
            super.sendContent(out, range, params, contentType);
        }
    }

    private boolean createReset(String email, Session session) throws IOException {
        Organisation org = getOrganisation();
        Profile user = Profile.findByEmail(email, session);
        if( user == null ) {
            log.info("user not found: " + email + "  in " + org.getOrgId());
            return false;
        }
        RootFolder rootFolder = WebUtils.findRootFolder(this);
        Website website = null;
        String subject; // subject for the email
        final String fromAddress = rootFolder.getEmailAddress();
        if( rootFolder instanceof WebsiteRootFolder) {
            website = ((WebsiteRootFolder)rootFolder).getWebsite();
            if( website.getDomainName() != null ) {
                subject = "Password reset for " + website.getDomainName();
            } else {
                subject = "Password reset for " + website.getName();
            }
        } else {
            subject = "Password reset for " + org.getOrgId();
        }
        String returnUrl = UserApp.getPasswordResetBase(website);
        Date now = _(CurrentDateService.class).getNow();
        passwordReset = PasswordReset.create(user, now, returnUrl, website, session);
        returnUrl +=  "?token=" + passwordReset.getToken();
        log.info("created reset token: " + passwordReset.getToken());
                        
        EmailItem emailItem = new EmailItem();
        emailItem.setCreatedDate(now);
        emailItem.setFromAddress(fromAddress);
        emailItem.setReplyToAddress(fromAddress);        
        emailItem.setRecipient(user);
        emailItem.setRecipientAddress(email);
        emailItem.setSubject(subject);        
        emailItem.setSendStatusDate(now);
        emailItem.setReadStatus(true); // set readStatus to true so doesnt appear as an unread message
        setEmailContent(emailItem, returnUrl);
        System.out.println("email html:");
        System.out.println(emailItem.getHtml());
        System.out.println("------");
        
        session.save(emailItem);
        log.info("queue email itemxxxx");
        
        return true;
    }

    private void setEmailContent(EmailItem emailItem, String returnUrl) throws IOException {
        String html = "<h1>Reset your password</h1>\n" + 
                "<p>Click on the following link to reset your e-learning password - \n" + 
                "<a href='" + returnUrl + "'>Reset your password</a></p>";
        emailItem.setHtml(html);
     
        String text = "Reset your password\n" + 
                "Click on the following link to reset your e-learning password - \n" + 
                returnUrl;
        emailItem.setText(text);
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    public PasswordReset getPasswordReset() {
        return passwordReset;
    }

    
    

}
