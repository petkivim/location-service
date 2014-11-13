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
package com.pkrete.locationservice.endpoint.generator.json;

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
 * This class returns the output in JSON format.
 *
 * @author Petteri Kivimäki
 */
public class JSONGenerator extends EmptyGenerator {

    private static final Logger logger = LoggerFactory.getLogger(JSONGenerator.class.getName());

    /**
     * Constructs and initializes a new JSONGenerator object.
     */
    public JSONGenerator() {
        this.filters = new LinkedHashMap<String, String>();
        this.filters.put("\"", "\\\\\"");
    }

    /**
     * Generates the JSON that is returned to the user.
     *
     * @param library the Library object thats information is shown to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return JSON that is returned to the user
     */
    @Override
    public String generateOutput(Library library, String lang, String callno) {
        logger.debug("Generate output for library. Id: \"{}\".", library.getLocationId());
        StringBuilder builder = new StringBuilder();
        builder.append("{\"library\":{");
        locationToJSON(library, lang, builder);
        builder.append("}}");

        return builder.toString();
    }

    /**
     * Generates the JSON that is returned to the user.
     *
     * @param collection the LibraryCollection object thats information is shown
     * to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return JSON that is returned to the user
     */
    @Override
    public String generateOutput(LibraryCollection collection, String lang, String callno) {
        logger.debug("Generate output for collection. Id: \"{}\".", collection.getLocationId());
        StringBuilder builder = new StringBuilder();
        builder.append("{\"library\":{");
        locationToJSON(collection.getLibrary(), lang, builder);
        builder.append(",\"collection\":{");
        locationToJSON(collection, lang, builder);
        collectionToJSON(collection, lang, callno, builder);
        builder.append("}}}");

        return builder.toString();
    }

    /**
     * Generates the JSON that is returned to the user.
     *
     * @param shelf the Shelf object thats information is shown to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return JSON that is returned to the user
     */
    @Override
    public String generateOutput(Shelf shelf, String lang, String callno) {
        logger.debug("Generate output for shelf. Id: \"{}\".", shelf.getLocationId());
        StringBuilder builder = new StringBuilder();
        builder.append("{\"library\":{");
        locationToJSON(shelf.getCollection().getLibrary(), lang, builder);
        builder.append(",\"collection\":{");
        locationToJSON(shelf.getCollection(), lang, builder);
        collectionToJSON(shelf.getCollection(), lang, "", builder);
        builder.append(",\"shelf\":{");
        locationToJSON(shelf, lang, builder);
        shelfToJSON(shelf, lang, callno, builder);
        builder.append("}}}}");

        return builder.toString();
    }

    /**
     * Generates the JSON that is returned to the user when the given location
     * doesn't exist in the database.
     *
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return JSON that is returned to the user
     */
    @Override
    public String generateOutputNotFound(String lang, String callno, String owner) {
        logger.debug("Generate output not found.");
        StringBuilder builder = new StringBuilder();
        builder.append("{\"message\":\"Location can not be found.\"}");

        return builder.toString();
    }

    /**
     * Generates the JSON that is returned to the user when the located item is
     * not available.
     *
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return JSON that is returned to the user
     */
    @Override
    public String generateOutputNotAvailable(String lang, String callno, String owner) {
        logger.debug("Generate output not available.");
        StringBuilder builder = new StringBuilder();
        builder.append("{\"message\":\"Item is not available.\"}");

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
        builder.append("{\"error\":");
        builder.append("{\"code\":\"").append(errorCode).append("\",");
        builder.append("\"message\":\"").append(escape(errorMsg)).append("\"}");
        builder.append("}");
        return builder.toString();
    }

    /**
     * Writes fields that are shared by all the location types.
     *
     * @param location location object
     * @param lang language of the UI
     * @param builder StringBuilder object
     */
    private void locationToJSON(Location location, String lang, StringBuilder builder) {
        String imagesPath = Settings.getInstance().getImagesPath(location.getOwner().getCode());
        String webpath = Settings.getInstance().getWebpath();

        builder.append("\"locationid\":").append(location.getLocationId()).append(",");

        builder.append("\"locationcode\":\"").append(escape(location.getLocationCode())).append("\",");

        builder.append("\"callnumber\":\"").append(escape(location.getCallNo())).append("\",");

        builder.append("\"floor\":\"").append(escape(location.getFloor())).append("\"");

        for (Description desc : location.getDescriptions()) {
            if (desc.getLanguage().getCode().equals(lang)) {
                builder.append(",\"description\":{\"lang\":\"").append(escape(desc.getLanguage().getCode())).append("\",\"value\":\"");
                builder.append(escape(desc.getDescription())).append("\"}");
                break;
            }
        }

        for (Note note : location.getNotes()) {
            if (note.getLanguage().getCode().equals(lang)) {
                builder.append(",\"note\":{\"lang\":\"").append(escape(note.getLanguage().getCode())).append("\",\"value\":\"");
                builder.append(escape(note.getNote())).append("\"}");
                break;
            }
        }

        if (location.getImage() != null) {
            String url = "";
            if (location.getImage().getIsExternal()) {
                url = location.getImage().getPath();
            } else {
                url = webpath + imagesPath + location.getImage().getPath();
            }
            builder.append(",\"image\":\"").append(url).append("\"");
        }

        if (location.getMap() != null) {
            if (location.getMap().getIsExternal()) {
                builder.append(",\"map\":{\"lang\":\"undefined\",");
                builder.append("\"url\":\"").append(location.getMap().getPath()).append("\"}");
            } else {
                builder.append(",\"map\":{\"lang\":\"").append(escape(lang)).append("\",\"url\":\"");
                builder.append(webpath).append("ImageCreator?locationId=").append(location.getLocationId());
                builder.append("&lang=").append(lang).append("&owner=").append(escape(location.getOwner().getCode()));
                builder.append("\"}");
            }
        }
        if (!location.getAreas().isEmpty()) {
            builder.append(",\"areas\":[");
            StringBuilder temp = new StringBuilder();
            for (Area area : location.getAreas()) {
                if (temp.length() > 0) {
                    temp.append(",");
                }
                temp.append("{\"x1\":").append(area.getX1()).append(",");
                temp.append("\"y1\":").append(area.getY1()).append(",");
                temp.append("\"x2\":").append(area.getX2()).append(",");
                temp.append("\"y2\":").append(area.getY2()).append(",");
                temp.append("\"angle\":").append(area.getAngle()).append("}");
            }
            builder.append(temp).append("]");
        }
    }

    /**
     * Writes fields that are collection specific.
     *
     * @param location collection object
     * @param lang language of the UI
     * @param callno the call number that was received from the UI
     * @param builder StringBuilder object
     */
    private void collectionToJSON(LibraryCollection location, String lang, String callno, StringBuilder builder) {
        if (location.hasCollectionCode()) {
            builder.append(",\"collectioncode\":\"").append(escape(location.getCollectionCode())).append("\"");
        }

        builder.append(",\"mainword\":\"").append(escape(location.getMainWord(callno))).append("\"");

        if (!location.getShelfNumber().isEmpty()) {
            builder.append(",\"shelfnumber\":\"").append(escape(location.getShelfNumber())).append("\"");
        }
        subjectsToJSON(location, lang, builder);
    }

    /**
     * Writes fields that are shelf specific.
     *
     * @param location shelf object
     * @param lang language of the UI
     * @param callno the call number that was received from the UI
     * @param builder StringBuilder object
     */
    private void shelfToJSON(Shelf location, String lang, String callno, StringBuilder builder) {
        if (location.getCollection().hasCollectionCode()) {
            builder.append(",\"collectioncode\":\"").append(escape(location.getCollection().getCollectionCode())).append("\"");
        }

        builder.append(",\"mainword\":\"").append(escape(location.getMainWord(callno))).append("\"");

        if (!location.getShelfNumber().isEmpty()) {
            builder.append(",\"shelfnumber\":\"").append(escape(location.getShelfNumber())).append("\"");
        }
        subjectsToJSON(location, lang, builder);
    }

    /**
     * Writes subject matters to JSON.
     *
     * @param location location object
     * @param lang language of the UI
     * @param builder StringBuilder object
     */
    private void subjectsToJSON(Location location, String lang, StringBuilder builder) {
        if (!location.getSubjectMatters().isEmpty()) {
            builder.append(",\"subjects\":[");
            StringBuilder temp = new StringBuilder();
            for (SubjectMatter subject : location.getSubjectMatters()) {
                if (subject.getLanguage().getCode().equals(lang)) {
                    if (temp.length() > 0) {
                        temp.append(",");
                    }
                    temp.append("{\"lang\":\"").append(escape(subject.getLanguage().getCode()));
                    temp.append("\",\"value\":\"").append(escape(subject.getIndexTerm())).append("\"}");
                }
            }
            builder.append(temp).append("]");
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
