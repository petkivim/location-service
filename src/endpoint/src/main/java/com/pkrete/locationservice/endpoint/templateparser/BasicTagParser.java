/**
 * This file is part of Location Service :: Endpoint.
 * Copyright (C) 2014 Petteri Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
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
import org.apache.log4j.Logger;

/**
 * The <code>BasicTagParser</code> class implements the 
 * {@link TemplateParser TemplateParser} interface.
 *
 * This class implements the methods defined in the TemplateParser interface.  
 * This class offers the functionality for generating the html page that is 
 * returned to the user. Templates can contain standard html code and
 * Location Service's own tag markup. This class defines the tags that can be 
 * used in the templates. 
 *
 * @author Petteri Kivimäki
 */
public class BasicTagParser implements TemplateParser {

    private final static Logger logger = Logger.getLogger(BasicTagParser.class.getName());

    /**
     * Parses the given string and replaces all the markup codes with 
     * corresponding information.
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
                String url = "LocationHandler?callno=" + encode(callno) + "&status=0&lang=" + m.group(1) + "&owner=" + shelf.getOwner().getCode() + "&id=" + shelf.getLocationId();
                if (shelf.getCollection().hasCollectionCode()) {
                    url += "&collection=" + shelf.getCollection().getCollectionCode();
                }
                return line.replaceAll("\\$LINK\\(.+\\)", "<a href=\"" + url + "\">" + m.group(2) + "</a>\n");
            }
        }

        if (line.matches(".*\\$LINK_COLLECTION.*")) {
            String url = "LocationHandler?callno=" + encode(shelf.getCollection().getCallNo()) + "&status=0&lang=" + lang + "&owner=" + shelf.getOwner().getCode();
            if (shelf.getCollection().hasCollectionCode()) {
                url += "&collection=" + shelf.getCollection().getCollectionCode();
            }
            url += "&id=" + shelf.getCollection().getLocationId();
            String label = shelf.getCollection().getLocationCode();
            // $LINK_COLLECTION('Kokoelma:')
            Pattern regex = Pattern.compile(".*\\$LINK_COLLECTION\\(\\s*[\"\'](.+)[\"\']\\s*\\)");
            Matcher m = regex.matcher(line);
            if (m.find()) {
                label = m.group(1);
            }
            return line.replaceAll("\\$LINK_COLLECTION(\\(.+\\)|)", "<a href=\"" + url + "\">" + label + "</a>\n");
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
            if (map.isGoogleMap()) {
                /* $SHELF_MAP("Link label") */
                Pattern regex = Pattern.compile(".*\\$SHELF_MAP\\(\\s*[\"\'](.+)[\"\']\\s*\\)");
                String label;
                String link = "";
                Matcher m = regex.matcher(line);
                if (m.find()) {
                    label = m.group(1);
                    link = "<div class=\"google_map_link_container\"><a class=\"google_map_link\" href=\"" + map.getGoogleMapLinkUrl() + "\" target=\"new\">" + label + "</a></div>";
                }
                return line.replaceAll("\\$SHELF_MAP(\\(.+\\)|)", "<iframe class=\"google_map\" id=\"map_shelf\" src=\"" + map.getGoogleMapEmbedUrl() + "\"></iframe>" + link);
            }
            return line.replaceAll("\\$SHELF_MAP(\\(.+\\)|)", "<img class=\"map\" id=\"map_shelf\" src=\"ImageCreator?owner=" + shelf.getOwner().getCode() + "&locationId=" + shelf.getLocationId() + "&lang=" + lang + "\" usemap=\"#shelf_coords\" />\n" + buildCoordinatesMap(shelf));
        }

        if (line.matches(".*\\$SHELF_IMAGE.*")) {
            Image img = shelf.getImage();
            if (img == null) {
                return "";
            }
            if (img.getIsExternal()) {
                return line.replace("$SHELF_IMAGE", "<img class=\"image\" id=\"img_shelf\" src=\"" + img.getPath() + "\" />");
            }
            return line.replace("$SHELF_IMAGE", "<img class=\"image\" id=\"img_shelf\" src=\"" + Settings.getInstance().getImagesPath(shelf.getOwner().getCode()) + img.getPath() + "\" />");
        }

        return parse(lineOrg, lang, shelf.getCollection(), callno, loader);
    }

    /**
     * Parses the given string and replaces all the markup codes with 
     * corresponding information.
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
                String url = "LocationHandler?callno=" + encode(callno) + "&status=0&lang=" + m.group(1) + "&owner=" + collection.getOwner().getCode() + "&id=" + collection.getLocationId();
                if (collection.hasCollectionCode()) {
                    url += "&collection=" + collection.getCollectionCode();
                }
                return line.replaceAll("\\$LINK\\(.+\\)", "<a href=\"" + url + "\">" + m.group(2) + "</a>\n");
            }
        }

        if (line.matches(".*\\$LINK_LIBRARY.*")) {
            String url = "LocationHandler?callno=" + encode(collection.getLibrary().getCallNo()) + "&status=0&lang=" + lang + "&owner=" + collection.getOwner().getCode() + "&id=" + collection.getLibrary().getLocationId();
            String label = collection.getLibrary().getLocationCode();
            // $LINK_LIBRARY('Kirjasto:')
            Pattern regex = Pattern.compile(".*\\$LINK_LIBRARY\\(\\s*[\"\'](.+)[\"\']\\s*\\)");
            Matcher m = regex.matcher(line);
            if (m.find()) {
                label = m.group(1);
            }
            return line.replaceAll("\\$LINK_LIBRARY(\\(.+\\)|)", "<a href=\"" + url + "\">" + label + "</a>\n");
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
            if (map.isGoogleMap()) {
                /* $COLLECTION_MAP("Link label") */
                Pattern regex = Pattern.compile(".*\\$COLLECTION_MAP\\(\\s*[\"\'](.+)[\"\']\\s*\\)");
                String label;
                String link = "";
                Matcher m = regex.matcher(line);
                if (m.find()) {
                    label = m.group(1);
                    link = "<div class=\"google_map_link_container\"><a class=\"google_map_link\" href=\"" + map.getGoogleMapLinkUrl() + "\" target=\"new\">" + label + "</a></div>";
                }
                return line.replaceAll("\\$COLLECTION_MAP(\\(.+\\)|)", "<iframe class=\"google_map\" id=\"map_collection\" src=\"" + map.getGoogleMapEmbedUrl() + "\"></iframe>" + link);
            }
            return line.replaceAll("\\$COLLECTION_MAP(\\(.+\\)|)", "<img class=\"map\" id=\"map_collection\" src=\"ImageCreator?owner=" + collection.getOwner().getCode() + "&locationId=" + collection.getLocationId() + "&lang=" + lang + "\" usemap=\"#collection_coords\" />\n" + buildCoordinatesMap(collection));
        }

        if (line.matches(".*\\$COLLECTION_IMAGE.*")) {
            Image img = collection.getImage();
            if (img == null) {
                return "";
            }
            if (img.getIsExternal()) {
                return line.replace("$COLLECTION_IMAGE", "<img class=\"image\" id=\"img_collection\" src=\"" + img.getPath() + "\" />");
            }
            return line.replace("$COLLECTION_IMAGE", "<img class=\"image\" id=\"img_collection\" src=\"" + Settings.getInstance().getImagesPath(collection.getOwner().getCode()) + img.getPath() + "\" />");
        }
        return parse(lineOrg, lang, collection.getLibrary(), callno, loader);
    }

    /**
     * Parses the given string and replaces all the markup codes with 
     * corresponding information.
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
                String url = "LocationHandler?callno=" + encode(callno) + "&status=0&lang=" + m.group(1) + "&owner=" + library.getOwner().getCode() + "&id=" + library.getLocationId();
                return line.replaceAll("\\$LINK\\(.+\\)", "<a href=\"" + url + "\">" + m.group(2) + "</a>\n");
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
            if (map.isGoogleMap()) {
                /* $LIBRARY_MAP("Link label") */
                Pattern regex = Pattern.compile(".*\\$LIBRARY_MAP\\(\\s*[\"\'](.+)[\"\']\\s*\\)");
                String label;
                String link = "";
                Matcher m = regex.matcher(line);
                if (m.find()) {
                    label = m.group(1);
                    link = "<div class=\"google_map_link_container\"><a class=\"google_map_link\" href=\"" + map.getGoogleMapLinkUrl() + "\" target=\"new\">" + label + "</a></div>";
                }
                return line.replaceAll("\\$LIBRARY_MAP(\\(.+\\)|)", "<iframe class=\"google_map\" id=\"map_library\" src=\"" + map.getGoogleMapEmbedUrl() + "\"></iframe>" + link);
            }
            return line.replaceAll("\\$LIBRARY_MAP(\\(.+\\)|)", "<img class=\"map\" id=\"map_library\" src=\"ImageCreator?owner=" + library.getOwner().getCode() + "&locationId=" + library.getLocationId() + "&lang=" + lang + "\" usemap=\"#library_coords\" />\n" + buildCoordinatesMap(library));
        }

        if (line.matches(".*\\$LIBRARY_IMAGE.*")) {
            Image img = library.getImage();
            if (img == null) {
                return "";
            }
            if (img.getIsExternal()) {
                return line.replace("$LIBRARY_IMAGE", "<img class=\"image\" id=\"img_library\" src=\"" + img.getPath() + "\" />");
            }
            return line.replace("$LIBRARY_IMAGE", "<img class=\"image\" id=\"img_library\" src=\"" + Settings.getInstance().getImagesPath(library.getOwner().getCode()) + img.getPath() + "\" />");
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
                String url = "LocationHandler?callno=" + encode(callno) + "&status=" + status + "&lang=" + m.group(1) + "&owner=" + owner;
                return line.replaceAll("\\$LINK\\(.+\\)", "<a href=\"" + url + "\">" + m.group(2) + "</a>\n");
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
     * Returns the name of the file which is included into another
     * file. If a filename can not be found, null is returned instead.
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
            logger.error(ex);
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

        Pattern regex = Pattern.compile(pattern0);
        Matcher m = regex.matcher(line);

        if (m.find()) {
            String url = "http://maps.google.com/?q=" + m.group(1) + "&z=" + m.group(2) + "&hl=" + m.group(4);
            String link = "<div class=\"google_map_link_container\"><a class=\"google_map_link\" href=\"" + url + "\" target=\"new\">" + m.group(3) + "</a></div>";
            return line.replaceAll(pattern0, "<iframe class=\"google_map\" id=\"map\" src=\"" + url + "&output=embed\"></iframe>" + link);
        }

        regex = Pattern.compile(pattern1);
        m = regex.matcher(line);
        if (m.find()) {
            String url = "http://maps.google.com/?q=" + m.group(1) + "&z=" + m.group(2);
            String link = "<div class=\"google_map_link_container\"><a class=\"google_map_link\" href=\"" + url + "\" target=\"new\">" + m.group(3) + "</a></div>";
            return line.replaceAll(pattern1, "<iframe class=\"google_map\" id=\"map\" src=\"" + url + "&output=embed\"></iframe>" + link);
        }

        regex = Pattern.compile(pattern2);
        m = regex.matcher(line);
        if (m.find()) {
            return line.replaceAll(pattern2, "<iframe class=\"google_map\" id=\"map\" src=\"http://maps.google.com/?q=" + m.group(1) + "&z=" + m.group(2) + "&hl=" + m.group(3) + "&output=embed\"></iframe>");
        }

        regex = Pattern.compile(pattern3);
        m = regex.matcher(line);
        if (m.find()) {
            return line.replaceAll(pattern3, "<iframe class=\"google_map\" id=\"map\" src=\"http://maps.google.com/?q=" + m.group(1) + "&z=" + m.group(2) + "&output=embed\"></iframe>");
        }

        regex = Pattern.compile(pattern4);
        m = regex.matcher(line);
        if (m.find()) {
            return line.replaceAll(pattern4, "<iframe class=\"google_map\" id=\"map\" src=\"http://maps.google.com/?q=" + m.group(1) + "&hl=" + m.group(2) + "&output=embed\"></iframe>");
        }

        regex = Pattern.compile(pattern5);
        m = regex.matcher(line);
        if (m.find()) {
            String url = "http://maps.google.com/?q=" + m.group(1);
            String link = "<div class=\"google_map_link_container\"><a class=\"google_map_link\" href=\"" + url + "\" target=\"new\">" + m.group(2) + "</a></div>";
            return line.replaceAll(pattern5, "<iframe class=\"google_map\" id=\"map\" src=\"" + url + "&output=embed\"></iframe>" + link);
        }

        regex = Pattern.compile(pattern6);
        m = regex.matcher(line);
        if (m.find()) {
            return line.replaceAll(pattern6, "<iframe class=\"google_map\" id=\"map\" src=\"http://maps.google.com/?q=" + m.group(1) + "&output=embed\"></iframe>");
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
