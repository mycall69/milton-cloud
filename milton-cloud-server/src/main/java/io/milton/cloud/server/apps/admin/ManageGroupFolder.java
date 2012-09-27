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
package io.milton.cloud.server.apps.admin;

import io.milton.cloud.server.web.*;
import io.milton.cloud.server.web.templating.HtmlTemplater;
import io.milton.http.Auth;
import io.milton.http.Range;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.http.exceptions.NotFoundException;
import io.milton.principal.Principal;
import io.milton.resource.AccessControlledResource;
import io.milton.resource.GetableResource;
import io.milton.vfs.db.Organisation;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.milton.context.RequestContext._;
import io.milton.http.Request;
import io.milton.http.Response.Status;
import io.milton.http.exceptions.ConflictException;
import io.milton.http.values.ValueAndType;
import io.milton.http.webdav.PropFindResponse.NameAndError;
import io.milton.http.webdav.PropertySourcePatchSetter;
import io.milton.property.BeanPropertyAccess;
import io.milton.property.BeanPropertyResource;
import io.milton.resource.CollectionResource;
import io.milton.resource.DeletableResource;
import io.milton.resource.MoveableResource;
import io.milton.resource.Resource;
import io.milton.vfs.db.*;
import io.milton.vfs.db.utils.SessionManager;
import javax.xml.namespace.QName;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author brad
 */
@BeanPropertyResource(value = "milton", enableByDefault = false)
public class ManageGroupFolder extends AbstractResource implements GetableResource, CollectionResource, DeletableResource, PropertySourcePatchSetter.CommitableResource, MoveableResource {

    private static final Logger log = LoggerFactory.getLogger(ManageGroupFolder.class);
    private final Group group;
    private final CommonCollectionResource parent;
    private ResourceList children;

    public ManageGroupFolder(CommonCollectionResource parent, Group group) {
        this.parent = parent;
        this.group = group;
    }

    @Override
    public List<? extends Resource> getChildren() throws NotAuthorizedException, BadRequestException {
        if (children == null) {
            children = new ResourceList();
        }
        return children;
    }

    @Override
    public void delete() throws NotAuthorizedException, ConflictException, BadRequestException {
        Session session = SessionManager.session();
        Transaction tx = session.beginTransaction();
        group.delete(session);
        tx.commit();
    }

    @Override
    public Priviledge getRequiredPostPriviledge(Request request) {
        return Priviledge.WRITE_CONTENT;
    }

    @Override
    public void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType) throws IOException, NotAuthorizedException, BadRequestException, NotFoundException {
        _(HtmlTemplater.class).writePage("admin", "admin/manageGroup", this, params, out);
    }

    public List<Group> getGroups() {
        return Group.findByOrg(getOrganisation(), SessionManager.session());
    }

    public String getTitle() {
        return "Manage group: " + group.getName();
    }

    @Override
    public boolean isDir() {
        return false;
    }

    @Override
    public CommonCollectionResource getParent() {
        return parent;
    }

    @BeanPropertyAccess(value = true)
    @Override
    public String getName() {
        return group.getName();
    }

    public void setName(String s) {
        group.setName(s);
    }

    @BeanPropertyAccess(value = true)
    public String getRegoMode() {
        return group.getRegistrationMode();
    }

    public void setRegoMode(String s) {
        group.setRegistrationMode(s);
    }

    public String getRegoModeText() {
        if (getRegoMode().equals(Group.REGO_MODE_ADMIN_REVIEW)) {
            return "Administrator review";
        } else if (getRegoMode().equals(Group.REGO_MODE_CLOSED)) {
            return "Closed";
        } else if (getRegoMode().equals(Group.REGO_MODE_OPEN)) {
            return "Open";
        } else {
            return getRegoMode();
        }
    }

    @Override
    public Date getModifiedDate() {
        return group.getModifiedDate();
    }

    @Override
    public Date getCreateDate() {
        return group.getModifiedDate();
    }

    @Override
    public Map<Principal, List<AccessControlledResource.Priviledge>> getAccessControlList() {
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
        return parent.getOrganisation();
    }

    @Override
    public boolean is(String type) {
        if (type.equals("group")) {
            return true;
        }
        return super.is(type);
    }

    @Override
    public Resource child(String childName) throws NotAuthorizedException, BadRequestException {
        return NodeChildUtils.childOf(getChildren(), childName);
    }

    @Override
    public void doCommit(Map<QName, ValueAndType> knownProps, Map<Status, List<NameAndError>> errorProps) throws BadRequestException, NotAuthorizedException {
        Session session = SessionManager.session();
        Transaction tx = session.beginTransaction();
        session.save(group);
        tx.commit();
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public void moveTo(CollectionResource rDest, String name) throws ConflictException, NotAuthorizedException, BadRequestException {
        Session session = SessionManager.session();
        Transaction tx = session.beginTransaction();
        
        if( !(rDest instanceof ManageGroupsFolder)) {
            throw new ConflictException("Parent folder must be manage groups folder. Is a: " + rDest.getClass() + " with name: " + rDest.getName());
        }
        group.setName(name);
        
        session.save(group);
        tx.commit();
    }
    
    
}