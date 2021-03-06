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
package io.milton.cloud.server.apps.forums;

import io.milton.cloud.server.db.*;
import io.milton.cloud.server.manager.CurrentRootFolderService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.milton.vfs.db.Organisation;
import io.milton.cloud.server.web.*;
import io.milton.cloud.server.web.templating.Formatter;
import io.milton.cloud.server.web.templating.HtmlTemplater;
import io.milton.cloud.server.web.templating.MenuItem;
import io.milton.resource.AccessControlledResource.Priviledge;
import io.milton.http.Auth;
import io.milton.http.FileItem;
import io.milton.http.Range;
import io.milton.principal.Principal;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.ConflictException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.http.exceptions.NotFoundException;
import io.milton.resource.GetableResource;
import io.milton.resource.PostableResource;

import static io.milton.context.RequestContext._;
import io.milton.http.HttpManager;
import io.milton.http.Request;
import io.milton.http.http11.auth.CookieAuthenticationHandler;
import io.milton.vfs.db.utils.SessionManager;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author brad
 */
public class ManagePostsPage extends AbstractResource implements GetableResource, PostableResource {

    private static final Logger log = LoggerFactory.getLogger(ManagePostsPage.class);
    private final String name;
    private final CommonCollectionResource parent;
    private final Organisation organisation;
    private JsonResult jsonResult;

    public ManagePostsPage(String name, Organisation organisation, CommonCollectionResource parent) {
        this.organisation = organisation;
        this.parent = parent;
        this.name = name;
    }

    @Override
    public String processForm(Map<String, String> parameters, Map<String, FileItem> files) throws BadRequestException, NotAuthorizedException, ConflictException {
        Session session = SessionManager.session();
        Transaction tx = session.beginTransaction();
        if (parameters.containsKey("deleteId")) {
            Long deletePostId = Long.parseLong(parameters.get("deleteId"));
            Post p = (Post) session.get(Post.class, deletePostId);
            p.delete(session);
            tx.commit();
            jsonResult = new JsonResult(true);
        } else if( parameters.containsKey("editId")) {
            Long editPostId = Long.parseLong(parameters.get("editId"));
            String newNotes = parameters.get("newText");
            Post p = (Post) session.get(Post.class, editPostId);
            p.setNotes(newNotes);
            session.save(p);
            tx.commit();
            jsonResult = new JsonResult(true);
            
        }
        return null;
    }

    @Override
    public Priviledge getRequiredPostPriviledge(Request request) {
        return Priviledge.WRITE_CONTENT;
    }    
    
    @Override
    public void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType) throws IOException, NotAuthorizedException, BadRequestException, NotFoundException {
        if (jsonResult != null) {
            jsonResult.write(out);
        } else {
            MenuItem.setActiveIds("menuTalk", "menuManageForums", "menuManagePosts");
            _(HtmlTemplater.class).writePage("admin", "forums/managePosts", this, params, out);
        }
    }

    public List<RecentPostBean> getLatestPosts() {
        List<RecentPostBean> beans = new ArrayList<>();
        for (Post p : Post.findByOrg(getOrganisation(), 100, SessionManager.session())) {
            beans.add(toBean(p));
        }
        return beans;
    }

    public String getTitle() {
        return "Manage posts";
    }

    @Override
    public boolean isDir() {
        return false;
    }

    @Override
    public CommonCollectionResource getParent() {
        return parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Date getModifiedDate() {
        return null;
    }

    @Override
    public Date getCreateDate() {
        return null;
    }

    @Override
    public Map<Principal, List<Priviledge>> getAccessControlList() {
        return null;
    }

    @Override
    public Long getMaxAgeSeconds(Auth auth) {
        return null;
    }

    @Override
    public String getContentType(String accepts) {
        return "text/html";
    }

    @Override
    public Long getContentLength() {
        return null;
    }

    @Override
    public Organisation getOrganisation() {
        return organisation;
    }

    @Override
    public boolean is(String type) {
        if (type.equals("manageRewards")) {
            return true;
        }
        return super.is(type);
    }

    public String getCookieAuthParams() {
        CookieAuthenticationHandler cookieAuth = _(CookieAuthenticationHandler.class);
        String url = cookieAuth.getCookieNameUserUrl() + "=" + cookieAuth.getUserUrlFromRequest(HttpManager.request());
        url += "&";
        url += cookieAuth.getCookieNameUserUrlHash() + "=" + cookieAuth.getHashFromRequest(HttpManager.request());
        return url;        
    }
    
    private RecentPostBean toBean(Post p) {
        final RecentPostBean b = new RecentPostBean();
        b.setId(p.getId());
        b.setNotes(p.getNotes());
        b.setDate(p.getPostDate());
        b.setUser(ProfileBean.toBean(p.getPoster()));
        String web = p.getWebsite().getDomainName();
        if( web == null ) {
            web = p.getWebsite().getName() + "." + _(CurrentRootFolderService.class).getPrimaryDomain();
        }
        web = web + _(Formatter.class).getPortString();
        b.setContentDomain(web);


        PostVisitor visitor = new PostVisitor() {

            @Override
            public void visit(Comment c) {
                b.setContentHref(c.getContentHref());
                b.setContentTitle(c.getContentTitle());
                b.setType("c"); // comment
            }

            @Override
            public void visit(ForumPost p) {
                b.setForumTitle(p.getForum().getTitle());
                b.setContentHref(ForumsApp.toHref(p));
                b.setType("q"); // question
            }

            @Override
            public void visit(ForumReply r) {
                b.setForumTitle(r.getPost().getForum().getTitle());
                b.setContentHref(ForumsApp.toHref(r.getPost()));
                b.setType("r"); // reply
            }
        };
        p.accept(visitor);

        return b;
    }

    public class RecentPostBean {

        private long id;
        private ProfileBean user;
        private String notes;
        private String forumTitle;
        private String contentDomain;
        private String contentTitle;
        private String contentHref;
        private Date date;
        private String type;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getForumTitle() {
            return forumTitle;
        }

        public void setForumTitle(String forumTitle) {
            this.forumTitle = forumTitle;
        }

        public String getContentDomain() {
            return contentDomain;
        }

        public void setContentDomain(String contentDomain) {
            this.contentDomain = contentDomain;
        }       
        
        public String getContentHref() {
            return contentHref;
        }

        public void setContentHref(String contentHref) {
            this.contentHref = contentHref;
        }

        public String getContentTitle() {
            return contentTitle;
        }

        public void setContentTitle(String contentTitle) {
            this.contentTitle = contentTitle;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public ProfileBean getUser() {
            return user;
        }

        public void setUser(ProfileBean user) {
            this.user = user;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
