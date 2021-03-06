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
package io.milton.cloud.server.apps.contactus;

import io.milton.cloud.server.apps.AppConfig;
import io.milton.cloud.server.apps.ChildPageApplication;
import io.milton.cloud.server.apps.PortletApplication;
import io.milton.cloud.server.apps.SettingsApplication;
import io.milton.cloud.server.apps.website.WebsiteRootFolder;
import io.milton.cloud.server.web.JsonResult;
import io.milton.cloud.server.web.RootFolder;
import io.milton.cloud.server.web.SpliffyResourceFactory;
import io.milton.cloud.server.web.templating.Formatter;
import io.milton.http.FileItem;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.ConflictException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.resource.Resource;
import io.milton.vfs.db.Branch;
import io.milton.vfs.db.Group;
import io.milton.vfs.db.Organisation;
import io.milton.vfs.db.Profile;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import org.apache.velocity.context.Context;

import static io.milton.context.RequestContext._;

/**
 * An app which gives a website a contact us page
 *
 * @author brad
 */
public class ContactUsApp implements ChildPageApplication, PortletApplication, SettingsApplication{

    public static final String CONTACT_US_ID = "contactUs";

    private AppConfig config;
    
    @Override
    public Resource getPage(Resource parent, String requestedName) {
        if( parent instanceof WebsiteRootFolder ) {
            WebsiteRootFolder wrf = (WebsiteRootFolder) parent;
            if( requestedName.equals("contactus")) {
                return new ContactUsFormPage(wrf, requestedName);
            }
        }
        return null;
    }

    @Override
    public String getInstanceId() {
        return CONTACT_US_ID;
    }

    @Override
    public void init(SpliffyResourceFactory resourceFactory, AppConfig config) throws Exception {
        this.config = config;
    }

    @Override
    public String getTitle(Organisation organisation, Branch websiteBranch) {
        return "Contact Us";
    }

    @Override
    public String getSummary(Organisation organisation, Branch websiteBranch) {
        return "Add a 'Contact Us' page to websites, allowing non-logged in users to email the site administrators";
    }
    
    public String getCssLocation() {
        return "contactus.css";
    }

    @Override
    public void renderPortlets(String portletSection, Profile currentUser, RootFolder rootFolder, Context context, Writer writer) throws IOException {

    }

    @Override
    public void renderSettings(Profile currentUser, Organisation org, Branch websiteBranch, Context context, Writer writer) throws IOException {
        String groupName; // = findSetting("gaAccountNumber", rootFolder);
        if( websiteBranch != null ) {
            groupName = config.get("group", websiteBranch);
        } else {
            groupName = config.get("group", org);
        }
        
        if( groupName == null ) {
            groupName = "";
        }
        writer.write("<label for='groupSelect'>Group to receive emails</label>");        
        writer.write("<select name='group' >");
        Formatter f = _(Formatter.class);
        for( Group g : org.getGroups()) {
            writer.write(f.option(g.getName(), g.getName(), groupName));
        }
        writer.write("</select>");
        writer.flush();
    }

    @Override
    public JsonResult processForm(Map<String, String> parameters, Map<String, FileItem> files, Organisation org, Branch websiteBranch) throws BadRequestException, NotAuthorizedException, ConflictException {
        String groupName = parameters.get("group");
        if( websiteBranch != null ) {
            config.set("group", websiteBranch, groupName);
        } else {
            config.set("group", org, groupName);
        }
        return new JsonResult(true);
    }
    
}
