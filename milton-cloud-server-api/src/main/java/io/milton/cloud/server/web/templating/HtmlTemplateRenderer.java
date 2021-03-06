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
package io.milton.cloud.server.web.templating;

import io.milton.resource.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import io.milton.cloud.server.apps.ApplicationManager;
import io.milton.cloud.server.apps.PortletApplication;
import io.milton.cloud.server.apps.orgs.OrganisationFolder;
import io.milton.cloud.server.web.CommonCollectionResource;
import io.milton.cloud.server.web.CommonResource;
import io.milton.vfs.db.Profile;
import io.milton.cloud.server.web.RootFolder;
import io.milton.cloud.server.web.UserResource;
import io.milton.cloud.server.web.WebUtils;
import io.milton.common.Path;
import io.milton.http.HttpManager;
import io.milton.http.Request;

/**
 *
 * @author brad
 */
public class HtmlTemplateRenderer {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HtmlTemplateRenderer.class);
    public static final String EXT_COMPILE_LESS = ".compile.less";
    public static final String COMBINED_RESOURCE_SEPERATOR = "--";
    private final ApplicationManager applicationManager;
    private final Formatter formatter;

    public HtmlTemplateRenderer(ApplicationManager applicationManager, Formatter formatter) {
        this.applicationManager = applicationManager;
        this.formatter = formatter;
    }

    public void renderHtml(RootFolder rootFolder, Resource page, Map<String, String> params, UserResource user, Template themeTemplate, TemplateHtmlPage themeTemplateTemplateMeta, Template contentTemplate, TemplateHtmlPage bodyTemplateMeta, String themeName, String themePath, OutputStream out) throws IOException {
        Context datamodel = new VelocityContext();
        datamodel.put("rootFolder", rootFolder);
        CommonCollectionResource folder;
        if (page instanceof CommonResource) {
            CommonResource cr = (CommonResource) page;
            folder = cr.getParent();
            datamodel.put("folder", folder);
        }
        datamodel.put("page", page);
        datamodel.put("params", params);
        Profile profile = null;
        if (user != null) {
            datamodel.put("user", user);
            profile = user.getThisUser();
        }
        datamodel.put("userResource", user);
        MenuItem menu = applicationManager.getRootMenuItem(page, profile, rootFolder);
        datamodel.put("menu", menu);
        datamodel.put("formatter", formatter);
        Request request = HttpManager.request();
        if (request != null) {
            datamodel.put("request", request);
        }

        OrganisationFolder orgFolder = WebUtils.findParentOrg(page);
        if (orgFolder != null) {
            datamodel.put("parentOrg", orgFolder);
        }

//        System.out.println("themeTemplateTemplateMeta: " + themeTemplateTemplateMeta.getId());
//        System.out.println("bodyTemplateMeta: " + bodyTemplateMeta.getId());

        PrintWriter pw = new PrintWriter(out);
        pw.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
        pw.write("<html xmlns='http://www.w3.org/1999/xhtml'>\n");
        pw.write("<head>\n");
        pw.write("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n");
        if( contentTemplate.getName().contains(":")) {
            pw.write("<meta name='templateName' value='" + contentTemplate.getName().split(":")[1] + "'/>\n");
        }

        List<String> pageBodyClasses = null;
        HtmlPage htmlPage = null;
        if (page instanceof HtmlPage) {
            htmlPage = (HtmlPage) page;
            pageBodyClasses = htmlPage.getBodyClasses();
            BodyRenderer bodyRenderer = new BodyRenderer(htmlPage);
            datamodel.put("body", bodyRenderer);
        }

        if (page instanceof TitledPage) {
            TitledPage titledPage = (TitledPage) page;
            pw.write("<title>" + titledPage.getTitle() + "</title>");
        }

        String versionId = formatter.getVersionId(rootFolder);
        
        printHeaderWebResources(versionId, themeName, themePath, pw, themeTemplateTemplateMeta, bodyTemplateMeta, htmlPage);

        // HACK: need something where templates want to include the portlet, not where they have hard coded exclusions
        if (!themeTemplate.getName().contains("plain") && !themeTemplate.getName().contains("email")) { // don't render the header for plain pages, these might be used as PDF input or emails
            applicationManager.renderPortlets(PortletApplication.PORTLET_SECTION_HEADER, profile, rootFolder, datamodel, pw);
        }
        pw.write("</head>\n");
        pw.write("<body class=\"");
        List<String> bodyClasses = deDupeBodyClasses(themeTemplateTemplateMeta.getBodyClasses(), bodyTemplateMeta.getBodyClasses(), pageBodyClasses);
        writeBodyClasses(pw, bodyClasses);
        pw.write("\">\n");
        pw.flush();
        // do theme body (then content body)

        if (VelocityContentDirective.getContentTemplate(datamodel) != null) {
            log.error("recurisve content invoication");
            Thread.dumpStack();
            throw new RuntimeException("recursive contetn invocation");
        }

        VelocityContentDirective.setContentTemplate(contentTemplate, datamodel);
        themeTemplate.merge(datamodel, pw);
        VelocityContentDirective.setContentTemplate(null, datamodel);
        printBottomWebResources(themeName, themePath, pw, themeTemplateTemplateMeta, bodyTemplateMeta, htmlPage);
        pw.write("</body>\n");
        pw.write("</html>");
        pw.flush();
    }

    private List<String> deDupeBodyClasses(List<String>... bodyClassLists) {
        Set<String> set = new HashSet<>();
        List<String> orderedList = new ArrayList<>();
        for (List<String> list : bodyClassLists) {
            if (list != null) {
                for (String s : list) {
                    if (!set.contains(s)) {
                        set.add(s);
                        orderedList.add(s);
                    }
                }
            }
        }
        return orderedList;
    }

    private void writeBodyClasses(PrintWriter pw, List<String> bodyClasses) {
        for (String s : bodyClasses) {
            pw.append(s).append(" ");
        }
    }

    private void printHeaderWebResources(String versionId, String themeName, String themePath, PrintWriter pw, HtmlPage... pages) {
        pw.println();
        pw.println("<script type=\"text/javascript\">");
        pw.println("    // Templates should push page init function into this array. It will then be run after outer template init functions. Injected by HtmlTemplateRenderer");
        pw.println("    var pageInitFunctions = new Array();");
        pw.println("</script>");
        pw.println();
        Map<String, List<String>> mapOfCssFilesByMedia = new HashMap<>();        
        for (HtmlPage page : pages) {
            if (page != null) {
                List<WebResource> webResources = page.getWebResources();
                if (webResources != null) {
                    // webPath is just the path of the directory containing the template
                    // this allows us to evaluate relative path of resources on the template to absolute paths
                    Path webPath = page.getPath();
                    if( !webPath.isRoot() ) {                        
                        webPath = webPath.getParent();
                    }
                    for (WebResource wr : webResources) {
                        if (wr.getTag().equals("link") && "stylesheet".equals(wr.getAtts().get("rel"))) {
                            String media = wr.getAtts().get("media");
                            if (media != null && media.length() > 0) {
                                List<String> cssFilesForMedia = mapOfCssFilesByMedia.get(media);
                                if (cssFilesForMedia == null) {
                                    cssFilesForMedia = new ArrayList<>();
                                    mapOfCssFilesByMedia.put(media, cssFilesForMedia);
                                }
                                String href = wr.getAtts().get("href");
                                href = wr.adjustRelativePath("href", href, themeName, webPath);
                                cssFilesForMedia.add(href);
                            } else {
                                String html = wr.toHtml(themeName, webPath);
                                pw.write(html + "\n");
                            }
                        } else {
                            // script tags with the bottom attribute should be at bottom, not in header
                            if (!wr.getTag().equals("script") || !wr.getAtts().containsKey("bottom")) {
                                String html = wr.toHtml(themeName, webPath);
                                pw.write(html + "\n");
                            }
                        }
                    }
                }
            }
        }

        // Now write out the combined css files as a link to a LESS css file
        // This is so that less files (such as for apps) can use the mixins provided
        // by the theme        
        for (String media : mapOfCssFilesByMedia.keySet()) {
            List<String> paths = mapOfCssFilesByMedia.get(media);
            String link = "<link rel='stylesheet' type='text/css'";
            if (media != null) {
                link += " media='" + media + "'";
            }
            link += " href='/theme/";
            String cssName = "";
            for (String path : paths) {
                cssName += path.replace("/", COMBINED_RESOURCE_SEPERATOR) + ",";
            }
            link += cssName + EXT_COMPILE_LESS + "?" + versionId + "' />";
            pw.println(link);
        }
        pw.println();
    }

    private void printBottomWebResources(String themeName, String themePath, PrintWriter pw, HtmlPage... pages) {
        pw.println();
        for (HtmlPage page : pages) {
            if (page != null) {
                List<WebResource> webResources = page.getWebResources();
                if (webResources != null) {
                    Path webPath = page.getPath();
                    if( !webPath.isRoot() ) {
                        webPath = webPath.getParent();
                    }                    
                    for (WebResource wr : webResources) {
                        if (wr.getTag().equals("script") && wr.getAtts().containsKey("bottom")) {
                            String html = wr.toHtml(themeName, webPath);
                            pw.write(html + "\n");
                        }
                    }
                }
            }
        }
        pw.println();
    }

    public class BodyRenderer {

        private final HtmlPage htmlPage;

        public BodyRenderer(HtmlPage htmlPage) {
            this.htmlPage = htmlPage;
        }

        @Override
        public String toString() {
            return htmlPage.getBody();
        }
    }
}
