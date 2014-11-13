/**
 * This file is part of Location Service :: Endpoint. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.templateparser;

import com.pkrete.locationservice.endpoint.loader.Loader;
import com.pkrete.locationservice.endpoint.model.location.Area;
import com.pkrete.locationservice.endpoint.model.location.Description;
import com.pkrete.locationservice.endpoint.model.illustration.Image;
import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.model.illustration.Map;
import com.pkrete.locationservice.endpoint.model.location.Note;
import com.pkrete.locationservice.endpoint.util.Settings;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import com.pkrete.locationservice.endpoint.model.subjectmatter.SubjectMatter;
import java.io.*;
import java.net.URLEncoder;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>OptimizedTagParser</code> class implements the
 * {@link TemplateParser TemplateParser} interface.
 *
 * This class implements the methods defined in the TemplateParser interface.
 * This class offers the functionality for generating the HTML page that is
 * returned to the user. Templates can contain standard HTML code and Location
 * Service's own tag markup. This class defines the tags that can be used in the
 * templates.
 *
 * This class is optimized version of {@link BasicTagParser BasicTagParser}
 * class. Both classes do lot of String concatenation, and BasciTagParser uses
 * String objects and this class StringBuilder objects. This class also contains
 * some minor refactoring.
 *
 * @author Petteri Kivimäki
 */
public class OptimizedTagParser implements TemplateParser {

    private final static Logger logger = LoggerFactory.getLogger(OptimizedTagParser.class.getName());

    /**
     * Parses the given string and replaces all the markup codes with
     * corresponding information.
     *
     * @param line string to be parsed
     * @param lang language of the UI
     * @param shelf Shelf object to which the string is related
     * @param callno call number of the location
     * @param loader Loader object responsible of loading the template file
     * @return parsed string
     */
    public String parse(String line, String lang, Shelf shelf, String callno, Loader loader) {
        if (!line.matches(".*<!--.*\\$.*-->.*")) {
            return line;
        }

        if (line.matches(".*<!--.*\\$DEBUG.*-->.*")) {
            StringBuilder text = new StringBuilder();
            text.append("<b>DEBUG INFO</b><br /><br />\n");
            for (java.util.Map.Entry<String, String> entry : this.getShelfTags().entrySet()) {
                text.append(entry.getKey()).append(" : ");
                text.append(parse(entry.getValue(), lang, shelf, callno, loader));
                text.append("<br />\n");
            }
            return text.toString();
        }

        String lineOrg = line;

        line = line.replaceAll("(<!--)|(-->)", "");

        if (line.matches(".*\\$LINK\\(.+\\).*")) {
            // $LINK('fi', 'Suomi')
            Pattern regex = Pattern.compile(".*\\$LINK\\(\\s*[\"\'](\\w+)[\"\']\\s*,\\s*[\"\'](.+)[\"\']\\s*\\)");
            Matcher m = regex.matcher(line);
            if (m.find()) {
                StringBuilder url = new StringBuilder();
                url.append("LocationHandler?callno=").append(encode(callno));
                url.append("&status=0&lang=").append(m.group(1));
                url.append("&owner=").append(shelf.getOwner().getCode());
                url.append("&id=").append(shelf.getLocationId());
                if (shelf.getCollection().hasCollectionCode()) {
                    url.append("&collection=").append(shelf.getCollection().getCollectionCode());
                }
                StringBuilder link = new StringBuilder();
                link.append("<a href=\"").append(url).append("\">").append(m.group(2)).append("</a>\n");
                return line.replaceAll("\\$LINK\\(.+\\)", link.toString());
            }
        }

        if (line.matches(".*\\$LINK_COLLECTION.*")) {
            StringBuilder url = new StringBuilder();
            url.append("LocationHandler?callno=").append(encode(shelf.getCollection().getCallNo()));
            url.append("&status=0&lang=").append(lang);
            url.append("&owner=").append(shelf.getOwner().getCode());
            if (shelf.getCollection().hasCollectionCode()) {
                url.append("&collection=").append(shelf.getCollection().getCollectionCode());
            }
            url.append("&id=").append(shelf.getCollection().getLocationId());
            String label = shelf.getCollection().getLocationCode();
            // $LINK_COLLECTION('Collection:')
            Pattern regex = Pattern.compile(".*\\$LINK_COLLECTION\\(\\s*[\"\'](.+)[\"\']\\s*\\)");
            Matcher m = regex.matcher(line);
            if (m.find()) {
                label = m.group(1);
            }
            StringBuilder link = new StringBuilder();
            link.append("<a href=\"").append(url).append("\">").append(label).append("</a>\n");
            return line.replaceAll("\\$LINK_COLLECTION(\\(.+\\)|)", link.toString());
        }

        if (line.matches(".*\\$INCLUDE\\(.+\\).*")) {
            String template = parseIncludeFileName(line, lang);
            template = loader.getTemplateOtherPath(template, lang, shelf.getOwner().getCode());
            if (template != null) {
                return loader.loadTemplate(lang, shelf, callno, template);
            }
            return "";
        }

        if (line.matches(".*\\$CALLNO.*")) {
            return line.replace("$CALLNO", shelf.getCallNo());
        }

        if (line.matches(".*\\$SHELF_LOCATION_CODE.*")) {
            return line.replace("$SHELF_LOCATION_CODE", shelf.getLocationCode());
        }

        if (line.matches(".*\\$MAIN_WORD.*")) {
            String callNo = shelf.getMainWord(callno);
            if (callNo.length() == 0) {
                return "";
            }
            return line.replace("$MAIN_WORD", callNo);
        }

        if (line.matches(".*\\$SHELF_NUMBER.*")) {
            String number = shelf.getShelfNumber();
            if (number.isEmpty()) {
                return "";
            }
            return line.replace("$SHELF_NUMBER", number);
        }

        if (line.matches(".*\\$SHELF_FLOOR.*")) {
            String floor = shelf.getFloor();
            if (floor.isEmpty()) {
                return "";
            }
            return line.replace("$SHELF_FLOOR", floor);
        }

        if (line.matches(".*\\$SHELF_SUBJECT.*")) {
            String delimiter = ", ";
            String end = "";
            // $SHELF_SUBJECT(', ', '.')
            Pattern regex = Pattern.compile(".*\\$SHELF_SUBJECT\\(\\s*[\"\'](.+)[\"\']\\s*,\\s*[\"\'](.+)[\"\']\\s*\\)");
            Matcher m = regex.matcher(line);
            if (m.find()) {
                delimiter = m.group(1);
                end = m.group(2);
            }

            if (!shelf.getSubjectMatters().isEmpty()) {
                StringBuilder subjects = new StringBuilder();
                for (SubjectMatter subject : shelf.getSubjectMatters()) {
                    if (subject.getLanguage().getCode().equals(lang)) {
                        subjects.append("<span class=\"subject_matter_shelf\">");
                        subjects.append(subject.getIndexTerm());
                        subjects.append("</span>");
                        subjects.append(delimiter);
                    }
                }
                if (subjects.length() > 0) {
                    String result = subjects.toString();
                    delimiter += "$";
                    result = result.replaceAll(delimiter, end);
                    return line.replaceAll("\\$SHELF_SUBJECT(\\((.+)\\)|)", result);
                }
            }
            return "";
        }

        if (line.matches(".*\\$SHELF_DESCRIPTION.*")) {
            for (Description desc : shelf.getDescriptions()) {
                if (desc.getLanguage().getCode().equals(lang)) {
                    return line.replace("$SHELF_DESCRIPTION", desc.getDescription());
                }
            }
            return "";
        }

        if (line.matches(".*\\$SHELF_NOTE.*")) {
            for (Note note : shelf.getNotes()) {
                if (note.getLanguage().getCode().equals(lang)) {
                    return line.replace("$SHELF_NOTE", note.getNote());
                }
            }
            return "";
        }

        if (line.matches(".*\\$SHELF_MAP.*")) {
            Map map = shelf.getMap();
            if (map == null) {
                return "";
            }
            StringBuilder mapTag = new StringBuilder();
            if (map.isGoogleMap()) {
                /* $SHELF_MAP("Link label") */
                Pattern regex = Pattern.compile(".*\\$SHELF_MAP\\(\\s*[\"\'](.+)[\"\']\\s*\\)");
                StringBuilder link = new StringBuilder("");
                Matcher m = regex.matcher(line);
                if (m.find()) {
                    link.append("<div class=\"google_map_link_container\"><a class=\"google_map_link\" href=\"");
                    link.append(map.getGoogleMapLinkUrl()).append("\" target=\"new\">");
                    link.append(m.group(1)).append("</a></div>");
                }
                mapTag.append("<iframe class=\"google_map\" id=\"map_shelf\" src=\"").append(map.getGoogleMapEmbedUrl()).append("\"></iframe>").append(link);
                return line.replaceAll("\\$SHELF_MAP(\\(.+\\)|)", mapTag.toString());
            }
            mapTag.append("<img class=\"map\" id=\"map_shelf\" src=\"ImageCreator?");
            mapTag.append("owner=").append(shelf.getOwner().getCode());
            mapTag.append("&locationId=").append(shelf.getLocationId());
            mapTag.append("&lang=").append(lang);
            mapTag.append("\" usemap=\"#shelf_coords\" />\n").append(buildCoordinatesMap(shelf));
            return line.replaceAll("\\$SHELF_MAP(\\(.+\\)|)", mapTag.toString());
        }

        if (line.matches(".*\\$SHELF_IMAGE.*")) {
            Image img = shelf.getImage();
            if (img == null) {
                return "";
            }
            StringBuilder imgTag = new StringBuilder("<img class=\"image\" id=\"img_shelf\" src=\"");
            if (!img.getIsExternal()) {
                imgTag.append(Settings.getInstance().getImagesPath(shelf.getOwner().getCode()));
            }
            imgTag.append(img.getPath()).append("\" />");
            return line.replace("$SHELF_IMAGE", imgTag.toString());
        }

        return parse(lineOrg, lang, shelf.getCollection(), callno, loader);
    }

    /**
     * Parses the given string and replaces all the markup codes with
     * corresponding information.
     *
     * @param line string to be parsed
     * @param lang language of the UI
     * @param collection LibraryCollection object to which the string is related
     * @param callno call number of the location
     * @param loader Loader object responsible of loading the template file
     * @return parsed string
     */
    public String parse(String line, String lang, LibraryCollection collection, String callno, Loader loader) {
        if (!line.matches(".*<!--.*\\$.*-->.*")) {
            return line;
        }

        if (line.matches(".*<!--.*\\$DEBUG.*-->.*")) {
            StringBuilder text = new StringBuilder();
            text.append("<b>DEBUG INFO</b><br /><br />\n");
            for (java.util.Map.Entry<String, String> entry : this.getCollectionTags().entrySet()) {
                text.append(entry.getKey()).append(" : ");
                text.append(parse(entry.getValue(), lang, collection, callno, loader));
                text.append("<br />\n");
            }
            return text.toString();
        }

        String lineOrg = line;

        line = line.replaceAll("(<!--)|(-->)", "");

        if (line.matches(".*\\$LINK\\(.+\\).*")) {
            Pattern regex = Pattern.compile(".*\\$LINK\\(\\s*[\"\'](\\w+)[\"\']\\s*,\\s*[\"\'](.+)[\"\']\\s*\\)");
            Matcher m = regex.matcher(line);
            if (m.find()) {
                StringBuilder url = new StringBuilder();
                url.append("LocationHandler?callno=").append(encode(callno));
                url.append("&status=0&lang=").append(m.group(1));
                url.append("&owner=").append(collection.getOwner().getCode());
                url.append("&id=").append(collection.getLocationId());

                if (collection.hasCollectionCode()) {
                    url.append("&collection=").append(collection.getCollectionCode());
                }
                StringBuilder link = new StringBuilder();
                link.append("<a href=\"").append(url).append("\">").append(m.group(2)).append("</a>\n");
                return line.replaceAll("\\$LINK\\(.+\\)", link.toString());
            }
        }

        if (line.matches(".*\\$LINK_LIBRARY.*")) {
            StringBuilder url = new StringBuilder();
            url.append("LocationHandler?callno=").append(encode(collection.getLibrary().getCallNo()));
            url.append("&status=0&lang=").append(lang);
            url.append("&owner=").append(collection.getOwner().getCode());
            url.append("&id=").append(collection.getLibrary().getLocationId());

            String label = collection.getLibrary().getLocationCode();
            // $LINK_LIBRARY('Kirjasto:')
            Pattern regex = Pattern.compile(".*\\$LINK_LIBRARY\\(\\s*[\"\'](.+)[\"\']\\s*\\)");
            Matcher m = regex.matcher(line);
            if (m.find()) {
                label = m.group(1);
            }
            StringBuilder link = new StringBuilder();
            link.append("<a href=\"").append(url).append("\">").append(label).append("</a>\n");
            return line.replaceAll("\\$LINK_LIBRARY(\\(.+\\)|)", link.toString());
        }

        if (line.matches(".*\\$INCLUDE\\(.+\\).*")) {
            String template = parseIncludeFileName(line, lang);
            template = loader.getTemplateOtherPath(template, lang, collection.getOwner().getCode());
            if (template != null) {
                return loader.loadTemplate(lang, collection, callno, template);
            }
            return "";
        }

        if (line.matches(".*\\$CALLNO.*")) {
            return line.replace("$CALLNO", collection.getCallNo());
        }

        if (line.matches(".*\\$MAIN_WORD.*")) {
            String callNo = collection.getMainWord(callno);
            if (callNo.length() == 0) {
                return "";
            }
            return line.replace("$MAIN_WORD", callNo);
        }

        if (line.matches(".*\\$COLLECTION_LOCATION_CODE.*")) {
            return line.replace("$COLLECTION_LOCATION_CODE", collection.getLocationCode());
        }

        if (line.matches(".*\\$COLLECTION_SHELF_NUMBER.*")) {
            String number = collection.getShelfNumber();
            if (number.isEmpty()) {
                return "";
            }
            return line.replace("$COLLECTION_SHELF_NUMBER", number);
        }

        if (line.matches(".*\\$COLLECTION_FLOOR.*")) {
            String floor = collection.getFloor();
            if (floor.isEmpty()) {
                return "";
            }
            return line.replace("$COLLECTION_FLOOR", floor);
        }

        if (line.matches(".*\\$COLLECTION_SUBJECT.*")) {
            String delimiter = ", ";
            String end = "";
            // $COLLECTION_SUBJECT(', ', '.')
            Pattern regex = Pattern.compile(".*\\$COLLECTION_SUBJECT\\(\\s*[\"\'](.+)[\"\']\\s*,\\s*[\"\'](.+)[\"\']\\s*\\)");
            Matcher m = regex.matcher(line);
            if (m.find()) {
                delimiter = m.group(1);
                end = m.group(2);
            }

            if (!collection.getSubjectMatters().isEmpty()) {
                StringBuilder subjects = new StringBuilder();
                for (SubjectMatter subject : collection.getSubjectMatters()) {
                    if (subject.getLanguage().getCode().equals(lang)) {
                        subjects.append("<span class=\"subject_matter_collection\">");
                        subjects.append(subject.getIndexTerm());
                        subjects.append("</span>");
                        subjects.append(delimiter);
                    }
                }
                if (subjects.length() > 0) {
                    String result = subjects.toString();
                    delimiter += "$";
                    result = result.replaceAll(delimiter, end);
                    return line.replaceAll("\\$COLLECTION_SUBJECT(\\((.+)\\)|)", result);
                }
            }
            return "";
        }

        if (line.matches(".*\\$COLLECTION_DESCRIPTION.*")) {
            for (Description desc : collection.getDescriptions()) {
                if (desc.getLanguage().getCode().equals(lang)) {
                    return line.replace("$COLLECTION_DESCRIPTION", desc.getDescription());
                }
            }
            return "";
        }

        if (line.matches(".*\\$COLLECTION_NOTE.*")) {
            for (Note note : collection.getNotes()) {
                if (note.getLanguage().getCode().equals(lang)) {
                    return line.replace("$COLLECTION_NOTE", note.getNote());
                }
            }
            return "";
        }

        if (line.matches(".*\\$COLLECTION_MAP.*")) {
            Map map = collection.getMap();
            if (map == null) {
                return "";
            }
            StringBuilder mapTag = new StringBuilder();
            if (map.isGoogleMap()) {
                /* $COLLECTION_MAP("Link label") */
                Pattern regex = Pattern.compile(".*\\$COLLECTION_MAP\\(\\s*[\"\'](.+)[\"\']\\s*\\)");
                StringBuilder link = new StringBuilder("");
                Matcher m = regex.matcher(line);
                if (m.find()) {
                    link.append("<div class=\"google_map_link_container\"><a class=\"google_map_link\" href=\"");
                    link.append(map.getGoogleMapLinkUrl()).append("\" target=\"new\">");
                    link.append(m.group(1)).append("</a></div>");
                }
                mapTag.append("<iframe class=\"google_map\" id=\"map_collection\" src=\"").append(map.getGoogleMapEmbedUrl()).append("\"></iframe>").append(link);
                return line.replaceAll("\\$COLLECTION_MAP(\\(.+\\)|)", mapTag.toString());
            }
            mapTag.append("<img class=\"map\" id=\"map_collection\" src=\"ImageCreator?");
            mapTag.append("owner=").append(collection.getOwner().getCode());
            mapTag.append("&locationId=").append(collection.getLocationId());
            mapTag.append("&lang=").append(lang);
            mapTag.append("\" usemap=\"#collection_coords\" />\n").append(buildCoordinatesMap(collection));
            return line.replaceAll("\\$COLLECTION_MAP(\\(.+\\)|)", mapTag.toString());
        }

        if (line.matches(".*\\$COLLECTION_IMAGE.*")) {
            Image img = collection.getImage();
            if (img == null) {
                return "";
            }
            StringBuilder imgTag = new StringBuilder("<img class=\"image\" id=\"img_collection\" src=\"");
            if (!img.getIsExternal()) {
                imgTag.append(Settings.getInstance().getImagesPath(collection.getOwner().getCode()));
            }
            imgTag.append(img.getPath()).append("\" />");
            return line.replace("$COLLECTION_IMAGE", imgTag.toString());
        }
        return parse(lineOrg, lang, collection.getLibrary(), callno, loader);
    }

    /**
     * Parses the given string and replaces all the markup codes with
     * corresponding information.
     *
     * @param line string to be parsed
     * @param lang language of the UI
     * @param library Library object to which the string is related
     * @param callno call number of the location
     * @param loader Loader object responsible of loading the template file
     * @return parsed string
     */
    public String parse(String line, String lang, Library library, String callno, Loader loader) {
        if (!line.matches(".*<!--.*\\$.*-->.*")) {
            return line;
        }

        if (line.matches(".*<!--.*\\$DEBUG.*-->.*")) {
            StringBuilder text = new StringBuilder();
            text.append("<b>DEBUG INFO</b><br /><br />\n");
            for (java.util.Map.Entry<String, String> entry : this.getLibraryTags().entrySet()) {
                text.append(entry.getKey()).append(" : ");
                text.append(parse(entry.getValue(), lang, library, callno, loader));
                text.append("<br />\n");
            }
            return text.toString();
        }

        String lineOrg = line;

        line = line.replaceAll("(<!--)|(-->)", "");

        if (line.matches(".*\\$LINK\\(.+\\).*")) {
            Pattern regex = Pattern.compile(".*\\$LINK\\(\\s*[\"\'](\\w+)[\"\']\\s*,\\s*[\"\'](.+)[\"\']\\s*\\)");
            Matcher m = regex.matcher(line);
            if (m.find()) {
                StringBuilder url = new StringBuilder();
                url.append("LocationHandler?callno=").append(encode(callno));
                url.append("&status=0&lang=").append(m.group(1));
                url.append("&owner=").append(library.getOwner().getCode());
                url.append("&id=").append(library.getLocationId());

                StringBuilder link = new StringBuilder();
                link.append("<a href=\"").append(url).append("\">").append(m.group(2)).append("</a>\n");
                return line.replaceAll("\\$LINK\\(.+\\)", link.toString());
            }
        }

        if (line.matches(".*\\$INCLUDE\\(.+\\).*")) {
            String template = parseIncludeFileName(line, lang);
            template = loader.getTemplateOtherPath(template, lang, library.getOwner().getCode());
            if (template != null) {
                return loader.loadTemplate(lang, library, callno, template);
            }
            return "";
        }

        if (line.matches(".*\\$CALLNO.*")) {
            return line.replace("$CALLNO", library.getCallNo());
        }

        if (line.matches(".*\\$LIBRARY_LOCATION_CODE.*")) {
            return line.replace("$LIBRARY_LOCATION_CODE", library.getLocationCode());
        }

        if (line.matches(".*\\$LIBRARY_DESCRIPTION.*")) {
            for (Description desc : library.getDescriptions()) {
                if (desc.getLanguage().getCode().equals(lang)) {
                    return line.replace("$LIBRARY_DESCRIPTION", desc.getDescription());
                }
            }
            return "";
        }

        if (line.matches(".*\\$LIBRARY_NOTE.*")) {
            for (Note note : library.getNotes()) {
                if (note.getLanguage().getCode().equals(lang)) {
                    return line.replace("$LIBRARY_NOTE", note.getNote());
                }
            }
            return "";
        }

        if (line.matches(".*\\$GOOGLE_MAP.*")) {
            return processGoogleMapTag(line);
        }

        if (line.matches(".*\\$LIBRARY_MAP.*")) {
            Map map = library.getMap();
            if (map == null) {
                return "";
            }
            StringBuilder mapTag = new StringBuilder();
            if (map.isGoogleMap()) {
                /* $LIBRARY_MAP("Link label") */
                Pattern regex = Pattern.compile(".*\\$LIBRARY_MAP\\(\\s*[\"\'](.+)[\"\']\\s*\\)");
                StringBuilder link = new StringBuilder("");
                Matcher m = regex.matcher(line);
                if (m.find()) {
                    link.append("<div class=\"google_map_link_container\"><a class=\"google_map_link\" href=\"");
                    link.append(map.getGoogleMapLinkUrl()).append("\" target=\"new\">");
                    link.append(m.group(1)).append("</a></div>");
                }
                mapTag.append("<iframe class=\"google_map\" id=\"map_library\" src=\"").append(map.getGoogleMapEmbedUrl()).append("\"></iframe>").append(link);
                return line.replaceAll("\\$LIBRARY_MAP(\\(.+\\)|)", mapTag.toString());
            }
            mapTag.append("<img class=\"map\" id=\"map_library\" src=\"ImageCreator?");
            mapTag.append("owner=").append(library.getOwner().getCode());
            mapTag.append("&locationId=").append(library.getLocationId());
            mapTag.append("&lang=").append(lang);
            mapTag.append("\" usemap=\"#library_coords\" />\n").append(buildCoordinatesMap(library));
            return line.replaceAll("\\$LIBRARY_MAP(\\(.+\\)|)", mapTag.toString());
        }

        if (line.matches(".*\\$LIBRARY_IMAGE.*")) {
            Image img = library.getImage();
            if (img == null) {
                return "";
            }
            StringBuilder imgTag = new StringBuilder("<img class=\"image\" id=\"img_library\" src=\"");
            if (!img.getIsExternal()) {
                imgTag.append(Settings.getInstance().getImagesPath(library.getOwner().getCode()));
            }
            imgTag.append(img.getPath()).append("\" />");
            return line.replace("$LIBRARY_IMAGE", imgTag.toString());
        }

        if (line.matches(".*\\$LIBRARY_FLOOR.*")) {
            String floor = library.getFloor();
            if (floor.isEmpty()) {
                return "";
            }
            return line.replace("$LIBRARY_FLOOR", floor);
        }
        return lineOrg;
    }

    /**
     * Parses the given string and replaces all the markup codes with
     * corresponding information.
     *
     * @param line string to be parsed
     * @param lang language of the UI
     * @param status status of the item
     * @param callno call number of the location
     * @param loader Loader object responsible of loading the template file
     * @param owner owner code of the library
     * @return parsed string
     */
    public String parse(String line, String lang, String status, String callno, Loader loader, String owner) {
        if (!line.matches(".*<!--.*\\$.*-->.*")) {
            return line;
        }

        String lineOrg = line;

        line = line.replaceAll("(<!--)|(-->)", "");

        if (line.matches(".*\\$LINK\\(.+\\).*")) {
            Pattern regex = Pattern.compile(".*\\$LINK\\(\\s*[\"\'](\\w+)[\"\']\\s*,\\s*[\"\'](.+)[\"\']\\s*\\)");
            Matcher m = regex.matcher(line);
            if (m.find()) {
                StringBuilder url = new StringBuilder();
                url.append("LocationHandler?callno=").append(encode(callno));
                url.append("&status=").append(status);
                url.append("&lang=").append(m.group(1));
                url.append("&owner=").append(owner);
                return line.replaceAll("\\$LINK\\(.+\\)", "<a href=\"" + url.toString() + "\">" + m.group(2) + "</a>\n");
            }
        }

        if (line.matches(".*\\$INCLUDE\\(.+\\).*")) {
            String template = parseIncludeFileName(line, lang);
            template = loader.getTemplateOtherPath(template, lang, owner);
            if (template != null) {
                return loader.loadTemplate(lang, template, status, callno, owner);
            }
            return "";
        }

        if (line.matches(".*\\$GOOGLE_MAP.*")) {
            return processGoogleMapTag(line);
        }

        return lineOrg;
    }

    /**
     * Returns the name of the file which is included into another file. If a
     * filename can not be found, null is returned instead.
     *
     * @param line single line of a template file that is processed
     * @param lang language of the UI
     * @return name of the file or null if the filename can not be found
     */
    private String parseIncludeFileName(String line, String lang) {
        Pattern regex = Pattern.compile(".*\\$INCLUDE\\(\\s*[\"\'](.+)[\"\']\\s*\\)");
        Matcher m = regex.matcher(line);
        if (m.find()) {
            String template = m.group(1);
            if (!template.endsWith(".txt")) {
                template += ".txt";
            }
            return template;
        }
        return null;
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex.getMessage());
            return value;
        }
    }

    private java.util.Map<String, String> getLibraryTags() {
        java.util.Map<String, String> list = new TreeMap<String, String>();
        list.put("CALLNO", "<!--$CALLNO-->");
        list.put("LIBRARY_LOCATION_CODE", "<!--$LIBRARY_LOCATION_CODE-->");
        list.put("LIBRARY_IMAGE", "<!--$LIBRARY_IMAGE-->");
        list.put("LIBRARY_MAP", "<!--$LIBRARY_MAP-->");
        list.put("LIBRARY_FLOOR", "<!--$LIBRARY_FLOOR-->");
        list.put("LIBRARY_DESCRIPTION", "<!--$LIBRARY_DESCRIPTION-->");
        list.put("LIBRARY_NOTE", "<!--$LIBRARY_NOTE-->");
        return list;
    }

    private java.util.Map<String, String> getCollectionTags() {
        java.util.Map<String, String> list = getLibraryTags();
        list.put("LINK_LIBRARY", "<!--$LINK_LIBRARY('link')-->");
        list.put("MAIN_WORD", "<!--$MAIN_WORD-->");
        list.put("COLLECTION_LOCATION_CODE", "<!--$COLLECTION_LOCATION_CODE-->");
        list.put("COLLECTION_IMAGE", "<!--$COLLECTION_IMAGE-->");
        list.put("COLLECTION_MAP", "<!--$COLLECTION_MAP-->");
        list.put("COLLECTION_SUBJECT", "<!--$COLLECTION_SUBJECT-->");
        list.put("COLLECTION_SHELF_NUMBER", "<!--$COLLECTION_SHELF_NUMBER-->");
        list.put("COLLECTION_FLOOR", "<!--$COLLECTION_FLOOR-->");
        list.put("COLLECTION_DESCRIPTION", "<!--$COLLECTION_DESCRIPTION-->");
        list.put("COLLECTION_NOTE", "<!--$COLLECTION_NOTE-->");
        return list;
    }

    private java.util.Map<String, String> getShelfTags() {
        java.util.Map<String, String> list = getCollectionTags();
        list.put("LINK_COLLECTION", "<!--$LINK_COLLECTION('collection')-->");
        list.put("SHELF_LOCATION_CODE", "<!--$SHELF_LOCATION_CODE-->");
        list.put("SHELF_IMAGE", "<!--$SHELF_IMAGE-->");
        list.put("SHELF_MAP", "<!--$SHELF_MAP-->");
        list.put("SHELF_SUBJECT", "<!--$SHELF_SUBJECT-->");
        list.put("SHELF_NUMBER", "<!--$SHELF_NUMBER-->");
        list.put("SHELF_FLOOR", "<!--$SHELF_FLOOR-->");
        list.put("SHELF_DESCRIPTION", "<!--$SHELF_DESCRIPTION-->");
        list.put("SHELF_NOTE", "<!--$SHELF_NOTE-->");
        return list;
    }

    private String processGoogleMapTag(String line) {
        /* $GOOGLE_MAP('address', '14', 'link label', 'fi') */
        String pattern0 = ".*\\$GOOGLE_MAP\\(\\s*[\"\'](.+)[\"\']\\s*,\\s*[\"\'](\\d{1,2})[\"\']\\s*,\\s*[\"\'](.{3,})[\"\']\\s*,\\s*[\"\']([a-z]{2})[\"\']\\s*\\)";
        /* $GOOGLE_MAP('address', '14', 'link label') */
        String pattern1 = ".*\\$GOOGLE_MAP\\(\\s*[\"\'](.+)[\"\']\\s*,\\s*[\"\'](\\d{1,2})[\"\']\\s*,\\s*[\"\'](.{3,})[\"\']\\s*\\)";
        /* $GOOGLE_MAP('address', '14', 'fi') */
        String pattern2 = ".*\\$GOOGLE_MAP\\(\\s*[\"\'](.+)[\"\']\\s*,\\s*[\"\'](\\d{1,2})[\"\']\\s*,\\s*[\"\']([a-z]{2})[\"\']\\s*\\)";
        /* $GOOGLE_MAP('address', '14') */
        String pattern3 = ".*\\$GOOGLE_MAP\\(\\s*[\"\'](.+)[\"\']\\s*,\\s*[\"\'](\\d{1,2})[\"\']\\s*\\)";
        /* $GOOGLE_MAP('address', 'fi') */
        String pattern4 = ".*\\$GOOGLE_MAP\\(\\s*[\"\'](.+)[\"\']\\s*,\\s*[\"\']([a-z]{2})[\"\']\\s*\\)";
        /* $GOOGLE_MAP('address', 'label') */
        String pattern5 = ".*\\$GOOGLE_MAP\\(\\s*[\"\'](.+)[\"\']\\s*,\\s*[\"\'](.{3,})[\"\']\\s*\\)";
        /* $GOOGLE_MAP('address') */
        String pattern6 = ".*\\$GOOGLE_MAP\\(\\s*[\"\'](.+)[\"\']\\s*\\)";

        StringBuilder iframe = new StringBuilder();
        StringBuilder url = new StringBuilder();
        StringBuilder link = new StringBuilder();
        String baseUrl = "https://maps.google.com/?q=";

        Pattern regex = Pattern.compile(pattern0);
        Matcher m = regex.matcher(line);

        if (m.find()) {
            url.append(baseUrl).append(m.group(1)).append("&z=").append(m.group(2)).append("&hl=").append(m.group(4));
            link.append("<div class=\"google_map_link_container\"><a class=\"google_map_link\" href=\"").append(url).append("\" target=\"new\">").append(m.group(3)).append("</a></div>");
            iframe.append("<iframe class=\"google_map\" id=\"map\" src=\"").append(url).append("&output=embed\"></iframe>").append(link);
            return line.replaceAll(pattern0, iframe.toString());
        }

        regex = Pattern.compile(pattern1);
        m = regex.matcher(line);
        if (m.find()) {
            url.append(baseUrl).append(m.group(1)).append("&z=").append(m.group(2));
            link.append("<div class=\"google_map_link_container\"><a class=\"google_map_link\" href=\"").append(url).append("\" target=\"new\">").append(m.group(3)).append("</a></div>");
            iframe.append("<iframe class=\"google_map\" id=\"map\" src=\"").append(url).append("&output=embed\"></iframe>").append(link);
            return line.replaceAll(pattern1, iframe.toString());
        }

        regex = Pattern.compile(pattern2);
        m = regex.matcher(line);
        if (m.find()) {
            iframe.append("<iframe class=\"google_map\" id=\"map\" src=\"").append(baseUrl).append(m.group(1)).append("&z=").append(m.group(2)).append("&hl=").append(m.group(3)).append("&output=embed\"></iframe>");
            return line.replaceAll(pattern2, iframe.toString());
        }

        regex = Pattern.compile(pattern3);
        m = regex.matcher(line);
        if (m.find()) {
            iframe.append("<iframe class=\"google_map\" id=\"map\" src=\"").append(baseUrl).append(m.group(1)).append("&z=").append(m.group(2)).append("&output=embed\"></iframe>");
            return line.replaceAll(pattern3, iframe.toString());
        }

        regex = Pattern.compile(pattern4);
        m = regex.matcher(line);
        if (m.find()) {
            iframe.append("<iframe class=\"google_map\" id=\"map\" src=\"").append(baseUrl).append(m.group(1)).append("&hl=").append(m.group(2)).append("&output=embed\"></iframe>");
            return line.replaceAll(pattern4, iframe.toString());
        }

        regex = Pattern.compile(pattern5);
        m = regex.matcher(line);
        if (m.find()) {
            url.append(baseUrl).append(m.group(1));
            link.append("<div class=\"google_map_link_container\"><a class=\"google_map_link\" href=\"").append(url).append("\" target=\"new\">").append(m.group(2)).append("</a></div>");
            iframe.append("<iframe class=\"google_map\" id=\"map\" src=\"").append(url).append("&output=embed\"></iframe>").append(link);
            return line.replaceAll(pattern5, iframe.toString());
        }

        regex = Pattern.compile(pattern6);
        m = regex.matcher(line);
        if (m.find()) {
            iframe.append("<iframe class=\"google_map\" id=\"map\" src=\"").append(baseUrl).append(m.group(1)).append("&output=embed\"></iframe>");
            return line.replaceAll(pattern6, iframe.toString());
        }
        return "";
    }

    private String buildCoordinatesMap(Shelf shelf) {
        StringBuilder builder = new StringBuilder();
        builder.append("<map name=\"shelf_coords\">\n");
        return buildCoordinatesMap(shelf, builder);
    }

    private String buildCoordinatesMap(LibraryCollection collection) {
        StringBuilder builder = new StringBuilder();
        builder.append("<map name=\"collection_coords\">\n");
        return buildCoordinatesMap(collection, builder);
    }

    private String buildCoordinatesMap(Library library) {
        StringBuilder builder = new StringBuilder();
        builder.append("<map name=\"library_coords\">\n");
        return buildCoordinatesMap(library, builder);
    }

    private String buildCoordinatesMap(Location location, StringBuilder builder) {
        if (location.getAreas() == null) {
            return "";
        }
        if (location.getAreas().isEmpty()) {
            return "";
        }

        for (Area area : location.getAreas()) {
            builder.append("<area shape=\"rect\" coords=\"");
            builder.append(area.getX1()).append(",");
            builder.append(area.getY1()).append(",");
            builder.append(area.getX2()).append(",");
            builder.append(area.getY2()).append("\"");
            builder.append(" title=\"").append(location.getCallNo()).append("\">\n");
        }
        builder.append("</map>\n");
        return builder.toString();
    }
}
