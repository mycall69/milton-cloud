/*
 * Copyright 2012 McEvoy Software Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.milton.cloud.server.apps.orgs;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import io.milton.cloud.server.web.AbstractResource;
import io.milton.cloud.server.web.CommonCollectionResource;
import io.milton.cloud.server.web.JsonResult;
import io.milton.cloud.server.web.WebUtils;
import io.milton.common.Path;
import io.milton.http.Auth;
import io.milton.http.FileItem;
import io.milton.http.HttpManager;
import io.milton.http.Range;
import io.milton.http.Request;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.ConflictException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.http.exceptions.NotFoundException;
import io.milton.resource.AccessControlledResource;
import io.milton.resource.GetableResource;
import io.milton.resource.PostableResource;
import io.milton.vfs.db.OrgType;
import io.milton.vfs.db.Organisation;
import io.milton.vfs.db.utils.SessionManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 * Returns a CSV view of the business units in the parent folder
 *
 * @author brad
 */
public class OrganisationsCsv extends AbstractResource implements GetableResource, PostableResource {

    private static final Logger log = LoggerFactory.getLogger(OrganisationsCsv.class);
    private final String name;
    private final OrganisationsFolder parent;
    private JsonResult jsonResult;
    private List<List<String>> unmatched = new ArrayList<>();
    private int numUpdated;
    private int currentLine;

    public OrganisationsCsv(String name, OrganisationsFolder parent) {
        this.parent = parent;
        this.name = name;
    }

    @Override
    public String processForm(Map<String, String> parameters, Map<String, FileItem> files) throws BadRequestException, NotAuthorizedException, ConflictException {
        log.info("processForm: " + parameters.size() + " files:" + files.size());
        Session session = SessionManager.session();
        Transaction tx = session.beginTransaction();

        if (!files.isEmpty()) {
            try {
                Boolean insertMode = WebUtils.getParamAsBool(parameters, "insertMode");
                insertMode = (insertMode == null ? false : insertMode);
                FileItem file = files.values().iterator().next();
                doUpload(file, insertMode, session);
                jsonResult.setData(new UploadResult());
                tx.commit();

            } catch (Exception ex) {
                log.warn("Exception processing", ex);
                tx.rollback();
                jsonResult = new JsonResult(false);
                jsonResult.setMessages(Arrays.asList("Error processing line: " + currentLine + " - " + ex.getMessage()));
            }
        } else {
        }
        return null;
    }

    @Override
    public AccessControlledResource.Priviledge getRequiredPostPriviledge(Request request) {
        return AccessControlledResource.Priviledge.WRITE_CONTENT;
    }

    @Override
    public void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType) throws IOException, NotAuthorizedException, BadRequestException, NotFoundException {
        if (jsonResult != null) {
            jsonResult.write(out);
        } else {
            try (OutputStreamWriter pw = new OutputStreamWriter(out)) {
                CSVWriter writer = new CSVWriter(pw);
                String[] arr = {"OrgID", "OrgType", "Path", "OrgTitle", "Address1", "Address2", "AddressState", "Phone", "Postcode"};
                writer.writeNext(arr);

                toCsv(parent.getOrganisation(), parent.getOrganisation().childOrgs(), writer);
                pw.flush();
            }
        }
    }

    public String getTitle() {
        return "Business units CSV";
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
    public Long getMaxAgeSeconds(Auth auth) {
        return null;
    }

    @Override
    public String getContentType(String accepts) {
        if (jsonResult == null) {
            return "text/csv";
        } else {
            return JsonResult.CONTENT_TYPE;
        }
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
        if (type.equals("csv")) {
            return true;
        }
        return super.is(type);
    }

    private void toCsv(Organisation rootOrg, List<Organisation> orgs, CSVWriter writer) {
        if (orgs == null) {
            return;
        }

        List<String> values;
        String[] arr;
        for (Organisation org : orgs) {
            values = buildLineOfValues(rootOrg, org);
            arr = new String[values.size()];
            values.toArray(arr);
            writer.writeNext(arr);
            toCsv(rootOrg, org.childOrgs(), writer);
        }
    }

    private List<String> buildLineOfValues(Organisation rootOrg, Organisation org) {
        List<String> values = new ArrayList<>();
        Path path = toOrgPath(rootOrg, org);
        if (!path.isRoot()) {
            path = path.getParent();
        }
        String sOrgType = "";
        if (org.getOrgType() != null) {
            sOrgType = org.getOrgType().getName();
        }

        values.add(org.getOrgId()); // unique ID
        values.add(sOrgType);
        values.add(path.toString()); // path to org        
        values.add(org.getTitle()); // user friendly name
        values.add(org.getAddress());
        values.add(org.getAddressLine2());
        values.add(org.getAddressState());
        values.add(org.getPhone());
        values.add(org.getPostcode());
        return values;
    }

    private Path toOrgPath(Organisation rootOrg, Organisation org) {
        if (org == null || rootOrg == org) {
            return Path.root;
        } else {
            return toOrgPath(rootOrg, org.getOrganisation()).child(org.getOrgId());
        }
    }

    private void doUpload(FileItem file, Boolean insertMode, Session session) throws IOException {
        log.info("doUpload: " + insertMode);
        InputStream in = null;
        try {
            in = file.getInputStream();
            Request req = HttpManager.request();
            if (insertMode) {
                fromCsv(in, session);

            } else {
                updateOnlyFromCsv(in, session);
            }
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public void fromCsv(InputStream in, Session session) throws IOException {
        InputStreamReader r = new InputStreamReader(in);
        CSVReader reader = new CSVReader(r);

        String[] lineParts;
        currentLine = 1;
        Organisation rootOrg = getOrganisation();
        long rootOrgId = rootOrg.getId();
        log.info("fromCsv: rootOrg=" + rootOrg.getOrgId());
        reader.readNext(); // skip first row, column headings
        while ((lineParts = reader.readNext()) != null) {
            if (lineParts.length > 0) {
                currentLine++;
                if (log.isTraceEnabled()) {
                    log.trace("process line: " + currentLine + " : " + Arrays.toString(lineParts));
                }
                List<String> lineList = new ArrayList<>();
                lineList.addAll(Arrays.asList(lineParts));
                if (lineList.size() > 0 && lineList.get(0).length() > 0) {
                    doProcess(rootOrg, lineList, currentLine, true, session);
                }
                if (currentLine % 20 == 0) {
                    log.info("Flush and clear session");
                    session.flush();
                    session.clear();
                    rootOrg = Organisation.get(rootOrgId, session);
                }
            }
        }
        // TODO: find all recs not updated and delete them
        jsonResult = new JsonResult(true, "Done insert and updates");
    }

    private void doProcess(Organisation rootOrg, List<String> lineList, int line, boolean allowInserts, Session session) {
        String orgId = lineList.get(0);
        if (orgId == null || orgId.length() == 0) {
            //throw new RuntimeException("Cant save record with an empty name: column" + pos + " line: " + line);
            unmatched.add(lineList);
            return;
        }
        log.info("doProcess: orgId=" + orgId);
        Organisation child = getOrganisation().childOrg(orgId, session);
        if (child == null) {
            log.trace("Create child called: " + orgId);
            if (allowInserts) {
                child = createOrg(rootOrg, session, lineList);
                session.flush();
            } else {
                unmatched.add(lineList);
                return;
            }
            log.info("created new record: " + child.getOrgId());
        } else {
            log.trace("found record to update: " + child.getOrgId());
        }
        updateRecord(child, lineList, line, rootOrg, session);
    }

    private void updateRecord(Organisation child, List<String> lineList, int line, Organisation rootOrg, Session session) {
        numUpdated++;
        String sOrgType = get(lineList, 1);
        if (sOrgType != null) {
            OrgType orgType = rootOrg.orgType(sOrgType, true, session);
            child.setOrgType(orgType);
        }

        String sPath = lineList.get(2);
        Path path = Path.path(sPath);

        checkPath(child, path, rootOrg, session);
        child.setTitle(get(lineList, 3));
        child.setAddress(get(lineList, 4));
        child.setAddressLine2(get(lineList, 5));
        child.setAddressState(get(lineList, 6));
        child.setPhone(get(lineList, 7));
        child.setPostcode(get(lineList, 8));
        session.save(child);
    }

    private void updateOnlyFromCsv(InputStream in, Session session) throws IOException {
        InputStreamReader r = new InputStreamReader(in);
        CSVReader reader = new CSVReader(r);

        String[] lineParts;
        currentLine = 1;
        reader.readNext(); // skip first row, column headings

        while ((lineParts = reader.readNext()) != null) {
            if (lineParts.length > 0) {
                currentLine++;
                if (log.isTraceEnabled()) {
                    log.trace("process line: " + currentLine + " : " + Arrays.toString(lineParts));
                }
                List<String> lineList = new ArrayList<>();
                lineList.addAll(Arrays.asList(lineParts));
                if (lineList.size() > 0 && lineList.get(0).length() > 0) {
                    doProcess(getOrganisation(), lineList, currentLine, false, session);
                }
            }
        }
        jsonResult = new JsonResult(true, "Done updates");
        jsonResult.setData(unmatched);

    }

    /**
     *
     *
     * @param lineList
     * @param i - column index, ie starts at zero
     * @return
     */
    private String get(List<String> lineList, int i) {
        if (i > lineList.size() - 1) {
            return null;
        }
        String s = lineList.get(i);
        if (s != null) {
            s = s.trim();
            if (s.length() == 0) {
                s = null;
            }
        }
        return s;
    }

    private Organisation createOrg(Organisation rootOrg, Session session, List<String> lineList) {
        String orgId = lineList.get(0);
        String sPath = lineList.get(2);
        log.info("Create org: " + sPath);
        Path path = Path.path(sPath);
        path = path.child(orgId);
        Organisation org = rootOrg;
        for (String childName : path.getParts()) {
            Organisation child = org.childOrg(childName, session);
            if (child == null) {
                child = org.createChildOrg(childName, session);
            }
            org = child;
        }
        return org;
    }

    /**
     * Check that the org has the given path, or move it if required
     *
     * @param child
     * @param path - path to the parent org
     */
    private void checkPath(Organisation childToCheck, Path path, Organisation rootOrg, Session session) {
        Organisation org = rootOrg;
        if (!path.isRoot()) {
            for (String childName : path.getParts()) {
                Organisation child = org.childOrg(childName, session);
                if (child == null) {
                    child = org.createChildOrg(childName, session);
                }
                org = child;
            }
        }
        // The org that we're left with needs to be the parent of childToCheck
        if (childToCheck.getOrganisation() != org) {
            log.info("org has moved: " + childToCheck.getOrgId() + " - from: " + childToCheck.getOrganisation().getOrgId() + " to " + org.getOrgId());
            childToCheck.setOrganisation(org, session);
        }

    }

    public class UploadResult {

        private List<List<String>> unmatched = new ArrayList<>();
        private int numUpdated;

        public UploadResult() {
            this.numUpdated = OrganisationsCsv.this.numUpdated;
            this.unmatched = OrganisationsCsv.this.unmatched;
        }

        public int getNumUpdated() {
            return numUpdated;
        }

        public List<List<String>> getUnmatched() {
            return unmatched;
        }
    }
}
