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
package io.milton.cloud.server.manager;

import io.milton.cloud.server.apps.ApplicationManager;
import io.milton.cloud.server.apps.orgs.OrganisationRootFolder;
import io.milton.cloud.server.apps.website.ManageWebsitesFolder;
import io.milton.cloud.server.apps.website.WebsiteRootFolder;
import io.milton.cloud.server.web.RootFolder;
import io.milton.cloud.server.web.Utils;
import io.milton.http.HttpManager;
import io.milton.http.Request;
import io.milton.vfs.db.Branch;
import io.milton.vfs.db.Organisation;
import io.milton.vfs.db.Website;
import io.milton.vfs.db.utils.SessionManager;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Stores the root folder in a request attribute. Is null safe, ie does nothing
 * if there is no request
 *
 * Notes on Domain name resolution:
 *
 * A server instance has a primary domain (PD). This is the domain name setup by
 * the server operator. Customers (ie users) can create websites on other
 * domains, and these websites can also be accessed on subdomains of the primary
 * domain
 *
 * When we look up a resource we first must identify a RootFolder, which can be
 * a Website or an Organisation, and we base this on the host name in the
 * request
 *
 * If any step in this sequence returns null then try the next step
 *
 * Sequence: 1. If the host name ends with the instance PD then use the sub
 * domain (SD) part before the PD to locate a website by name. If the SD begins
 * with admin, then we attempt to locate an organisation with the suffix
 * following "admin" Eg for PD = milton.io a. www.milton.io -> SD does not match
 * anything so falls through b. mysite.milton.io -> SD matches a Website named
 * "mysite" c. admin.myorg.milton.io -> SD matches admin console for "myorg"
 *
 * 2. Attempt to locate a Website with a domain name that exactly matches the
 * given host name Eg a. www.mysite.com -> match website.domainName =
 * "www.mysite.com" b. mysite.com -> match "mysite.com". Note that this might be
 * an alias to www.mysite.com
 *
 * 3. If none of the above match, then fall through to the root org's admin
 * console
 *
 *
 * @author brad
 */
public class DefaultCurrentRootFolderService implements CurrentRootFolderService {

    private static final Logger log = LoggerFactory.getLogger(DefaultCurrentRootFolderService.class);
    
    public static String ROOT_FOLDER_NAME = "_spliffy_root_folder";
    private String primaryDomain = "localhost";

    private ApplicationManager applicationManager;
    
    public DefaultCurrentRootFolderService() {
    }

    
    
    @Override
    public RootFolder getRootFolder() {
        Request req = HttpManager.request();
        if (req == null) {
            return null;
        }
        return getRootFolder(req.getHostHeader(), req, true);
    }

    @Override
    public RootFolder peekRootFolder() {
        Request req = HttpManager.request();
        if (req == null) {
            return null;
        }
        return getRootFolder(req.getHostHeader(), req, false);
    }
    
    

    @Override
    public RootFolder getRootFolder(String host) {
        Request req = HttpManager.request();
        return getRootFolder(host, req, true);
    }

    private RootFolder getRootFolder(String host, Request req, boolean resolve) {
        RootFolder rootFolder = null;
        if( req != null ) {
            rootFolder = (RootFolder) req.getAttributes().get(ROOT_FOLDER_NAME);
        }
        if (rootFolder == null && resolve) {
            rootFolder = resolve(host);
            if( req != null ) {
                req.getAttributes().put(ROOT_FOLDER_NAME, rootFolder);
            }
        }
        return rootFolder;
    }

    @Override
    public String getPrimaryDomain() {
        return primaryDomain;
    }

    public void setPrimaryDomain(String primaryDomain) {
        this.primaryDomain = primaryDomain;
    }

    private RootFolder resolve(String host) {
        if (host.contains(":")) {
            host = host.substring(0, host.indexOf(":"));
        }
        Session session = SessionManager.session();
        String primaryDomainSuffix = "." + primaryDomain;
        if (host.endsWith(primaryDomainSuffix)) {
            String subdomain = Utils.stripSuffix(host, primaryDomainSuffix);
            // If starts with admin. then look for an organisation, will go to admin console
            if (subdomain.startsWith("admin.")) {
                String orgName = Utils.stripPrefix(subdomain, "admin.");
                Organisation org = Organisation.findByAdminDomain(orgName, session);
                if (org != null) {
                    return new OrganisationRootFolder(applicationManager, org);
                }
            }
            // otherwise, look for a website with a name that matches the subdomain
            Website website = Website.findByName(subdomain, session);
            if (website != null) {
                Branch b = website.liveBranch();
                if( b != null) {
                    return new WebsiteRootFolder(applicationManager, website, b);
                }
            }
            
            // not exact match on website, but might have a branch suffix. Eg qa.mysite.somewhere.com
            // which would be QA branch of the "mysite" website, with somewhere.com as primary domain
            //System.out.println("resolve: subdomain=" + subdomain);
            if( subdomain.contains(".")) {
                // split the subdomain
                int pos = subdomain.indexOf(".");
                String first = subdomain.substring(0, pos);
                String otherPart = subdomain.substring(pos+1);
                website = Website.findByName(otherPart, session);
                if( website != null ) {
                    // now look for branch
                    Branch b = website.branch(first);
                    if( b != null ) {
                        return new WebsiteRootFolder(applicationManager, website, b);
                    }
                }
            }
        }

        // Didnt find anything matching primary domain, so look for an exact match on website
        Website website = Website.findByDomainName(host, session);
        if (website != null) {
            if( !website.deleted() ) {
                return new WebsiteRootFolder(applicationManager, website, website.liveBranch());
            } else {
                log.warn("Request for deleted website");
            }
        }

        // Still nothing found, so drop to root org admin console
        Organisation org = Organisation.getRootOrg(SessionManager.session());
        if (org == null) {
            throw new RuntimeException("No root organisation");
        }
        return new OrganisationRootFolder(applicationManager, org);
    }

    
    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public void setApplicationManager(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    
}
