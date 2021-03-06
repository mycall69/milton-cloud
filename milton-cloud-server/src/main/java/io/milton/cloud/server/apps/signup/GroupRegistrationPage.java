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
package io.milton.cloud.server.apps.signup;

import io.milton.cloud.server.web.GroupInWebsiteFolder;
import io.milton.cloud.common.CurrentDateService;
import io.milton.cloud.server.apps.website.WebsiteRootFolder;
import io.milton.cloud.server.db.GroupMembershipApplication;
import io.milton.cloud.server.db.OptIn;
import io.milton.cloud.server.db.SignupLog;
import io.milton.cloud.server.manager.PasswordManager;
import io.milton.cloud.server.web.*;
import io.milton.cloud.server.web.templating.HtmlTemplater;
import io.milton.cloud.server.web.templating.TitledPage;
import io.milton.http.Auth;
import io.milton.http.FileItem;
import io.milton.http.Range;
import io.milton.http.Request;
import io.milton.http.Request.Method;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.ConflictException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.http.exceptions.NotFoundException;
import io.milton.principal.Principal;
import io.milton.resource.AccessControlledResource.Priviledge;
import io.milton.resource.GetableResource;
import io.milton.resource.PostableResource;
import io.milton.vfs.db.Organisation;
import io.milton.vfs.db.Profile;
import io.milton.vfs.db.utils.SessionManager;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.milton.context.RequestContext._;
import io.milton.http.HttpManager;
import io.milton.vfs.db.Group;
import io.milton.vfs.db.GroupMembership;
import io.milton.vfs.db.NvPair;
import io.milton.vfs.db.NvSet;
import io.milton.vfs.db.OrgType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Manages registration of a user when signing up to a group
 *
 * @author brad
 */
public class GroupRegistrationPage extends AbstractResource implements GetableResource, PostableResource, TitledPage {

    private static final Logger log = LoggerFactory.getLogger(GroupRegistrationPage.class);
    private final String name;
    private final GroupInWebsiteFolder parent;
    private final SignupApp app;
    private JsonResult jsonResult;
    private List<Organisation> searchResults;
    private List<OptIn> optIns;
    private boolean useJson;

    public GroupRegistrationPage(String name, GroupInWebsiteFolder parent, SignupApp app) {
        this.parent = parent;
        this.name = name;
        this.app = app;
    }

    @Override
    public void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType) throws IOException, NotAuthorizedException, BadRequestException, NotFoundException {
        if (jsonResult != null) {
            log.info("sendContent: json");
            jsonResult.write(out);
        } else {
            log.info("sendContent: render page");
            
            String q = params.get("q");
            if( q == null ) {
                q = params.get("jsonQuery");
                if( q != null  ) {
                    useJson = true;
                }
            }
            if (q != null && q.length() > 0) {
                Organisation rootSearchOrg = parent.getGroup().getRootRegoOrg();
                if (rootSearchOrg == null) {
                    rootSearchOrg = getOrganisation();
                }
                OrgType regoOrgType = parent.getGroup().getRegoOrgType();
                searchResults = Organisation.search(q, rootSearchOrg, regoOrgType, SessionManager.session()); // find the given user in this organisation 
                
            }

            if( useJson ) {
                jsonResult = new JsonResult(true);
                List<OrgData> orgs = new ArrayList<>();
                for( Organisation org : searchResults) {
                    OrgData d = new OrgData(org);
                    orgs.add(d);
                }
                jsonResult.setData(orgs);
                jsonResult.write(out);
            } else {
                _(HtmlTemplater.class).writePage("signup/register", this, params, out);
            }
        }
    }
    
    @Override
    public String processForm(Map<String, String> parameters, Map<String, FileItem> files) throws BadRequestException, NotAuthorizedException, ConflictException {
        log.info("processForm");
        // ajax request to signup
        Session session = SessionManager.session();
        Transaction tx = session.beginTransaction();
        try {
            Organisation org = parent.getOrganisation();
            if (parameters.containsKey("orgId")) {
                String orgId = parameters.get("orgId");
                org = getOrganisation().childOrg(orgId, session);
                if (org == null) {
                    jsonResult = JsonResult.fieldError("orgId", "Organisation not found: " + orgId);
                    return null;
                }
                if (!org.isWithin(getOrganisation())) {
                    throw new RuntimeException("Selected org is not contained within this org. selected orgId=" + orgId + " this org: " + getOrganisation().getOrgId());
                }
            }

            String password = parameters.get("password");            
            parameters.remove("password");
            if (password == null || password.length() == 0  ) {
                jsonResult = JsonResult.fieldError("password", "No password was given");
                return null;
            }
            if( password.length() != password.trim().length()) {
                jsonResult = JsonResult.fieldError("password", "The given password begins or ends with spaces. Please ensure there are no spaces at the beginning or end of your password");
                return null;
            }

            // Check that any mandatory extra fields have been provided
            Group group = parent.getGroup();
            NvSet fieldset = group.getFieldset();
            List<ExtraField> extraFields = new ArrayList<>();
            if( fieldset != null && !fieldset.isEmpty()) {
                for( NvPair nvp : fieldset.getNvPairs()) {
                    ExtraField f = ExtraField.parse(nvp.getName(), nvp.getPropValue());
                    extraFields.add(f);
                    if( f.isRequired() ) {
                        String s = WebUtils.getParam(parameters, f.getName());
                        if( s == null ) {
                            jsonResult = JsonResult.fieldError(f.getName(), "No value was provided");
                            return null;
                        } else {
                            if( f.getOptions() != null && !f.getOptions().isEmpty()) {
                                if( !f.getOptions().contains(s)) {
                                    jsonResult = JsonResult.fieldError(f.getName(), "Please select a valid option");
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
            
            // Check if the email is already taken. If so, and if the given password matches,
            // then we assume this is an existing Fuse user who is signing up for another
            // group or site
            String email = WebUtils.getParam(parameters, "email");
            parameters.remove("email");
            Profile p = Profile.find(email, session);
            if (p != null) {
                // Check password matches
                if (p.getCredentials() != null && p.getCredentials().isEmpty()) {
                    if (_(PasswordManager.class).verifyPassword(p, password)) {
                        // Passwords match, all good
                    } else {
                        jsonResult = JsonResult.fieldError("password", "An existing user account was found with that email address. If this is your account please enter the existing password");
                        return null;
                    }
                } else {
                    log.info("Found an account with no password, so permit");
                }
            } else {
                // Not existing, create a new profile
                p = new Profile();
                String nickName = WebUtils.getParam(parameters, "nickName");
                if (nickName != null) {
                    nickName = nickName.trim();
                    if (nickName.length() == 0) {
                        nickName = null;
                    }
                }

                String newName = WebUtils.getParam(parameters, "name");
                if (newName == null || newName.trim().length() == 0) {
                    if (nickName == null) {
                        jsonResult = JsonResult.fieldError("nickName", "Please enter a name or nick name");
                        return null;
                    }
                    newName = Profile.findUniqueName(nickName, session);
                }

                p.setName(newName);
                p.setNickName(nickName);
                p.setEmail(email);
                p.setCreatedDate(new Date());
                p.setModifiedDate(new Date());
                
                // optional
                String phone = WebUtils.getParam(parameters, "phone");
                p.setPhone(phone);
                
                session.save(p);
                _(SpliffySecurityManager.class).getPasswordManager().setPassword(p, password);
            }
            
            // All good so far, lets store the extra fields (if any given) and then associate
            // the fieldset with the membership or application
            NvSet fields = storeExtraFields(extraFields, parameters, session);
            
            String result;
            WebsiteRootFolder wrf = (WebsiteRootFolder) WebUtils.findRootFolder(this);
            if (!Group.REGO_MODE_OPEN.equals(group.getRegistrationMode())) {
                // Not open, just create application
                log.info("Group is not open, so create a membership application");
                GroupMembershipApplication gma = new GroupMembershipApplication();
                gma.setCreatedDate(new Date());
                gma.setModifiedDate(new Date());
                gma.setGroupEntity(group);
                gma.setWebsite(wrf.getWebsite());
                gma.setMember(p);
                gma.setAdminOrg(getOrganisation());
                gma.setWithinOrg(org);
                gma.setFields(fields);
                session.save(gma);
                result = "pending";
            } else {
                // add directly to group
                log.info("Group is open, so create membership immediately");
                GroupMembership gm = p.addToGroup(group, org, session);
                gm.setFields(fields);
                session.save(gm);
                _(SignupApp.class).onNewMembership(p.membership(group), wrf);
                SignupLog.logSignup(wrf.getWebsite(), p, org, group, SessionManager.session());
                result = "created";
            }

            // Now process any opt-ins
            String sOptins = WebUtils.getParam(parameters, "optins");
            if (sOptins != null) {
                List<OptIn> optins = OptIn.findForGroup(group, session);
                for (String s : sOptins.split(",")) {
                    Group optInGroup = getOrganisation().group(s, session);
                    if (optInGroup != null) {
                        OptIn o = OptIn.findOptin(optins, optInGroup);
                        if (o != null) {
                            p.addToGroup(optInGroup, org, session);
                            _(SignupApp.class).onNewMembership(p.membership(optInGroup), wrf);
                            SignupLog.logSignup(wrf.getWebsite(), p, org, optInGroup, SessionManager.session());
                        }
                    }
                }
            }


            tx.commit();

            String nextHref = app.getNextHref(p, wrf.getBranch());
            String userPath = "/users/" + p.getName(); // todo: encoding
            log.info("Created user: " + userPath);
            jsonResult = new JsonResult(true, result, nextHref);
        } catch (Exception e) {
            log.error("Exception creating user", e);
            jsonResult = new JsonResult(false, e.getMessage());
        }
        return null;
    }

    public boolean isHasOrgs() {
        List<Organisation> childOrgs = getOrganisation().getChildOrgs();
        return childOrgs != null && !childOrgs.isEmpty();
    }

    @Override
    public String getContentType(String accepts) {
        Request req = HttpManager.request();
        boolean isJsonQuery = false;
        if( req != null && req.getParams() != null && req.getParams().containsKey("jsonQuery") ) {
            isJsonQuery = true;
        }
        if (jsonResult != null || isJsonQuery ) {
            return "application/x-javascript; charset=utf-8";
        }
        return "text/html";
    }

    @Override
    public boolean authorise(Request request, Method method, Auth auth) {
        return true;
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
    public Long getContentLength() {
        return null;
    }

    @Override
    public Organisation getOrganisation() {
        return parent.getOrganisation();
    }

    @Override
    public boolean isPublic() {
        return true;
    }
    
    public Group getGroup() {
        return parent.getGroup();
    }

    @Override
    public Priviledge getRequiredPostPriviledge(Request request) {
        return Priviledge.READ_CONTENT;
    }

    public List<Organisation> getSearchResults() {
        return searchResults;
    }

    public String getRegoOrgType() {
        OrgType ot = parent.getGroup().getRegoOrgType();
        if (ot == null) {
            return "Business unit";
        } else {
            return ot.getDisplayName();
        }
    }

    public List<OptIn> getOptins() {
        if (optIns == null) {
            optIns = OptIn.findForGroup(parent.getGroup(), SessionManager.session());
        }
        return optIns;
    }

    public boolean hasOptins() {
        return !getOptins().isEmpty();
    }

    @Override
    public String getTitle() {
        return "Register: " + parent.getName();
    }
    
    public List<ExtraField> getExtraFields() {
        NvSet fieldset = getGroup().getFieldset();
        if( fieldset == null || fieldset.isEmpty() ) {
            return Collections.EMPTY_LIST;
        }
        List<ExtraField> list = new ArrayList<>();
        for( NvPair nvp : fieldset.getNvPairs()) {
            list.add( ExtraField.parse(nvp.getName(), nvp.getPropValue()));
        }
        return list;
    }

    private NvSet storeExtraFields(List<ExtraField> extraFields, Map<String, String> parameters, Session session) {
        if( extraFields == null || extraFields.isEmpty()) {
            return null;
        }
        Date now = _(CurrentDateService.class).getNow();
        NvSet fields = new NvSet();
        fields.setCreatedDate(now);
        session.save(fields);
        for( ExtraField f : extraFields) {
            String val = WebUtils.getParam(parameters, f.getName());
            if( val != null ) {
                NvPair nvp = fields.addPair(f.getName(), val);
                session.save(nvp);
            }
        }
        return fields;
    }
}
