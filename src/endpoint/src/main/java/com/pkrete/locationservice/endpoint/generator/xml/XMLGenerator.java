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
package com.pkrete.locationservice.endpoint.generator.xml;

import com.pkrete.locationservice.endpoint.generator.EmptyGenerator;
import com.pkrete.locationservice.endpoint.model.location.Area;
import com.pkrete.locationservice.endpoint.model.location.Description;
import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.model.location.Note;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import com.pkrete.locationservice.endpoint.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.endpoint.util.Settings;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>HTMLGenerator</code> class extends the abstract
 * {@link EmptyGenerator EmptyGenerator} class.
 *
 * This class returns the output in XML format.
 *
 * @author Petteri Kivimäki
 */
public class XMLGenerator extends EmptyGenerator {

    private static final Logger logger = LoggerFactory.getLogger(XMLGenerator.class.getName());

    /**
     * Constructs and initializes a new XMLGenerator object.
     */
    public XMLGenerator() {
        this.filters = new LinkedHashMap<String, String>();
        this.filters.put("&", "&amp;");
        this.filters.put("<", "&lt;");
        this.filters.put(">", "&gt;");
        this.filters.put("\"", "&quot;");
        this.filters.put("'", "&apos;");
    }

    /**
     * Generates the XML that is returned to the user.
     *
     * @param library the Library object thats information is shown to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return XML that is returned to the user
     */
    @Override
    public String generateOutput(Library library, String lang, String callno) {
        logger.debug("Generate output for library. Id: \"{}\".", library.getLocationId());
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        builder.append("<library>\n");
        locationToXml(library, lang, builder);
        builder.append("</library>\n");

        return builder.toString();
    }

    /**
     * Generates the XML that is returned to the user.
     *
     * @param collection the LibraryCollection object thats information is shown
     * to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return XML that is returned to the user
     */
    @Override
    public String generateOutput(LibraryCollection collection, String lang, String callno) {
        logger.debug("Generate output for collection. Id: \"{}\".", collection.getLocationId());
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        builder.append("<library>\n");
        locationToXml(collection.getLibrary(), lang, builder);
        builder.append("<collection>\n");
        locationToXml(collection, lang, builder);
        collectionToXml(collection, lang, callno, builder);
        builder.append("</collection>\n");
        builder.append("</library>\n");

        return builder.toString();
    }

    /**
     * Generates the XML that is returned to the user.
     *
     * @param shelf the Shelf object thats information is shown to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return XML that is returned to the user
     */
    @Override
    public String generateOutput(Shelf shelf, String lang, String callno) {
        logger.debug("Generate output for shelf. Id: \"{}\".", shelf.getLocationId());
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        builder.append("<library>\n");
        locationToXml(shelf.getCollection().getLibrary(), lang, builder);
        builder.append("<collection>\n");
        locationToXml(shelf.getCollection(), lang, builder);
        collectionToXml(shelf.getCollection(), lang, "", builder);
        builder.append("<shelf>\n");
        locationToXml(shelf, lang, builder);
        shelfToXml(shelf, lang, callno, builder);
        builder.append("</shelf>\n");
        builder.append("</collection>\n");
        builder.append("</library>\n");

        return builder.toString();
    }

    /**
     * Generates the XML that is returned to the user when the given location
     * doesn't exist in the database.
     *
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return XML that is returned to the user
     */
    @Override
    public String generateOutputNotFound(String lang, String callno, String owner) {
        if (logger.isDebugEnabled()) {
            logger.debug("Generate output not found.");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        builder.append("<message>Location can not be found.</message>\n");

        return builder.toString();
    }

    /**
     * Generates the XML that is returned to the user when the located item is
     * not available.
     *
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return XML that is returned to the user
     */
    @Override
    public String generateOutputNotAvailable(String lang, String callno, String owner) {
        if (logger.isDebugEnabled()) {
            logger.debug("Generate output not available.");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        builder.append("<message>Item is not available.</message>\n");

        return builder.toString();
    }

    /**
     * Generates output containing the given error code and error message.
     *
     * @param errorCode error code
     * @param errorMsg error message
     * @return String containing the given error code and error message
     */
    @Override
    public String generateError(String errorCode, String errorMsg) {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        builder.append("<error>\n");
        builder.append("<code>").append(errorCode).append("</code>\n");
        builder.append("<message>").append(escape(errorMsg)).append("</message>\n");
        builder.append("</error>\n");
        return builder.toString();
    }

    /**
     * Writes XML tags that are shared by all the location types.
     *
     * @param location location object
     * @param lang language of the UI
     * @param builder StringBuilder object
     */
    private void locationToXml(Location location, String lang, StringBuilder builder) {
        String imagesPath = Settings.getInstance().getImagesPath(location.getOwner().getCode());
        String webpath = Settings.getInstance().getWebpath();

        builder.append("<locationid>").append(location.getLocationId()).append("</locationid>\n");

        if (location.getLocationCode().isEmpty()) {
            builder.append("<locationcode />");
        } else {
            builder.append("<locationcode>").append(escape(location.getLocationCode())).append("</locationcode>\n");
        }

        if (location.getCallNo().isEmpty()) {
            builder.append("<callnumber />");
        } else {
            builder.append("<callnumber>").append(escape(location.getCallNo())).append("</callnumber>\n");
        }

        if (location.getFloor().isEmpty()) {
            builder.append("<floor />\n");
        } else {
            builder.append("<floor>").append(escape(location.getFloor())).append("</floor>\n");
        }

        if (location.getDescriptions().isEmpty()) {
            builder.append("<description />\n");
        } else {
            boolean hit = false;
            for (Description desc : location.getDescriptions()) {
                if (desc.getLanguage().getCode().equals(lang)) {
                    builder.append("<description lang=\"").append(escape(desc.getLanguage().getCode())).append("\">");
                    builder.append(escape(desc.getDescription())).append("</description>\n");
                    hit = true;
                    break;
                }
            }
            if (!hit) {
                builder.append("<description />\n");
            }
        }

        if (location.getNotes().isEmpty()) {
            builder.append("<note />\n");
        } else {
            boolean hit = false;
            for (Note note : location.getNotes()) {
                if (note.getLanguage().getCode().equals(lang)) {
                    builder.append("<note lang=\"").append(escape(note.getLanguage().getCode())).append("\">");
                    builder.append(escape(note.getNote())).append("</note>\n");
                    hit = true;
                    break;
                }
            }
            if (!hit) {
                builder.append("<note />\n");
            }
        }

        if (location.getImage() == null) {
            builder.append("<image />\n");
        } else {
            String url = "";
            if (location.getImage().getIsExternal()) {
                url = location.getImage().getPath();
            } else {
                url = webpath + imagesPath + location.getImage().getPath();
            }
            builder.append("<image>").append(escape(url)).append("</image>\n");
        }

        if (location.getMap() == null) {
            builder.append("<map />\n");
        } else {
            if (location.getMap().getIsExternal()) {
                builder.append("<map lang=\"undefined\">").append(escape(location.getMap().getPath())).append("</map>\n");
            } else {
                builder.append("<map lang=\"").append(lang).append("\">");
                builder.append(escape(webpath)).append("ImageCreator?locationId=").append(location.getLocationId());
                builder.append("&amp;lang=").append(escape(lang)).append("&amp;owner=").append(escape(location.getOwner().getCode()));
                builder.append("</map>\n");
            }
        }
        if (location.getAreas().isEmpty()) {
            builder.append("<areas />\n");
        } else {
            builder.append("<areas>\n");
            for (Area area : location.getAreas()) {
                builder.append("<area x1=\"").append(area.getX1()).append("\" ");
                builder.append("y1=\"").append(area.getY1()).append("\" ");
                builder.append("x2=\"").append(area.getX2()).append("\" ");
                builder.append("y2=\"").append(area.getY2()).append("\" ");
                builder.append("angle=\"").append(area.getAngle()).append("\" />\n");
            }
            builder.append("</areas>\n");
        }
    }

    /**
     * Writes XML tags that are collection specific.
     *
     * @param location collection object
     * @param lang language of the UI
     * @param callno the call number that was received from the UI
     * @param builder StringBuilder object
     */
    private void collectionToXml(LibraryCollection location, String lang, String callno, StringBuilder builder) {
        if (location.hasCollectionCode()) {
            builder.append("<collectioncode>").append(escape(location.getCollectionCode())).append("</collectioncode>\n");
        } else {
            builder.append("<collectioncode />\n");
        }

        if (location.getMainWord(callno).isEmpty()) {
            builder.append("<mainword />\n");
        } else {
            builder.append("<mainword>").append(escape(location.getMainWord(callno))).append("</mainword>\n");
        }
        if (location.getShelfNumber().isEmpty()) {
            builder.append("<shelfnumber />\n");
        } else {
            builder.append("<shelfnumber>").append(escape(location.getShelfNumber())).append("</shelfnumber>\n");
        }
        subjectsToXml(location, lang, builder);
    }

    /**
     * Writes XML tags that are shelf specific.
     *
     * @param location shelf object
     * @param lang language of the UI
     * @param callno the call number that was received from the UI
     * @param builder StringBuilder object
     */
    private void shelfToXml(Shelf location, String lang, String callno, StringBuilder builder) {
        if (location.getCollection().hasCollectionCode()) {
            builder.append("<collectioncode>").append(escape(location.getCollection().getCollectionCode())).append("</collectioncode>\n");
        } else {
            builder.append("<collectioncode />\n");
        }

        if (location.getMainWord(callno).isEmpty()) {
            builder.append("<mainword />\n");
        } else {
            builder.append("<mainword>").append(escape(location.getMainWord(callno))).append("</mainword>\n");
        }
        if (location.getShelfNumber().isEmpty()) {
            builder.append("<shelfnumber />\n");
        } else {
            builder.append("<shelfnumber>").append(escape(location.getShelfNumber())).append("</shelfnumber>\n");
        }
        subjectsToXml(location, lang, builder);
    }

    /**
     * Writes subject matters to XML.
     *
     * @param location location object
     * @param lang language of the UI
     * @param builder StringBuilder object
     */
    private void subjectsToXml(Location location, String lang, StringBuilder builder) {
        if (location.getSubjectMatters().isEmpty()) {
            builder.append("<subjects />\n");
        } else {
            boolean hit = false;
            StringBuilder temp = new StringBuilder();
            for (SubjectMatter subject : location.getSubjectMatters()) {
                if (subject.getLanguage().getCode().equals(lang)) {
                    temp.append("<subject lang=\"").append(escape(subject.getLanguage().getCode()));
                    temp.append("\">").append(escape(subject.getIndexTerm())).append("</subject>\n");
                    hit = true;
                }
            }
            if (!hit) {
                builder.append("<subjects />\n");
            } else {
                builder.append("<subjects>\n").append(temp).append("</subjects>\n");
            }
        }
    }

    private String escape(String data) {
        // Loop through the filters and replace all the keys with 
        // the corresponding value
        for (String key : this.filters.keySet()) {
            data = data.replaceAll(key, this.filters.get(key));
        }
        return data;
    }
}
