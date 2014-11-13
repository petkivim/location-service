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
import com.pkrete.locationservice.endpoint.model.language.Language;
import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.model.location.Note;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import com.pkrete.locationservice.endpoint.model.search.LocationType;
import com.pkrete.locationservice.endpoint.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.endpoint.util.Settings;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class creates a XML presentation of a given Location object and returns
 * it as a String. This functionality is needed when exporting data from
 * LocationService or returning results to a query received via a search
 * interface.
 *
 * @author Petteri Kivimäki
 */
public class XmlBatchGenerator extends EmptyGenerator {

    /**
     * Constructs and initializes a new XmlBatchGenerator object.
     */
    public XmlBatchGenerator() {
        this.filters = new LinkedHashMap<String, String>();
        this.filters.put("&", "&amp;");
        this.filters.put("<", "&lt;");
        this.filters.put(">", "&gt;");
        this.filters.put("\"", "&quot;");
        this.filters.put("'", "&apos;");
    }

    /**
     * Returns XML presentation of the given locations.
     *
     * @param locations locations to be presented in XML
     * @param languages list of available languages in the system
     * @return XML presentation of the given locations
     */
    @Override
    public String generateBatchOutput(List locations, List<Language> languages, boolean children) {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

        if (locations.isEmpty()) {
            builder.append("<locations/>\n");
            return builder.toString().trim();
        }

        builder.append("<locations>\n");
        for (Location location : (List<Location>) locations) {
            builder.append(locationToXml(location, languages, children));
        }

        builder.append("</locations>\n");
        return builder.toString().trim();
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
     * Returns XML presentation of the given location.
     *
     * @param location location to be presented in XML
     * @param languages list of available languages in the system
     * @return XML presentation of the given location
     */
    private String locationToXml(Location location, List<Language> languages, boolean children) {
        StringBuilder builder = new StringBuilder();
        String imagesPath = escape(Settings.getInstance().getImagesPath(location.getOwner().getCode()));
        String webpath = escape(Settings.getInstance().getWebpath());
        LocationType locationType = LocationType.LIBRARY;

        if (location instanceof Library) {
            locationType = LocationType.LIBRARY;
            builder.append("<location type=\"library\">\n");
        } else if (location instanceof LibraryCollection) {
            locationType = LocationType.COLLECTION;
            builder.append("<location type=\"collection\">\n");
        } else if (location instanceof Shelf) {
            locationType = LocationType.SHELF;
            builder.append("<location type=\"shelf\">\n");
        }

        builder.append("<locationid>").append(location.getLocationId()).append("</locationid>\n");

        if (locationType == LocationType.COLLECTION) {
            builder.append("<libraryid>").append(((LibraryCollection) location).getLibrary().getLocationId()).append("</libraryid>\n");
        } else if (locationType == LocationType.SHELF) {
            builder.append("<libraryid>").append(((Shelf) location).getCollection().getLibrary().getLocationId()).append("</libraryid>\n");
            builder.append("<collectionid>").append(((Shelf) location).getCollection().getLocationId()).append("</collectionid>\n");
        }

        builder.append("<name>").append(escape(location.getName())).append("</name>\n");

        if (location.getLocationCode().isEmpty()) {
            builder.append("<locationcode />");
        } else {
            builder.append("<locationcode>").append(escape(location.getLocationCode())).append("</locationcode>\n");
        }

        if (locationType == LocationType.COLLECTION) {
            if (((LibraryCollection) location).hasCollectionCode()) {
                builder.append("<collectioncode>").append(escape(((LibraryCollection) location).getCollectionCode())).append("</collectioncode>\n");
            } else {
                builder.append("<collectioncode />\n");
            }
        } else if (locationType == LocationType.SHELF) {
            if (((Shelf) location).getCollection().hasCollectionCode()) {
                builder.append("<collectioncode>").append(escape(((Shelf) location).getCollection().getCollectionCode())).append("</collectioncode>\n");
            } else {
                builder.append("<collectioncode />\n");
            }
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
            builder.append("<descriptions />\n");
        } else {
            builder.append("<descriptions>\n");
            for (Description desc : location.getDescriptions()) {
                builder.append("<description lang=\"").append(escape(desc.getLanguage().getCode())).append("\">");
                builder.append(escape(desc.getDescription())).append("</description>\n");
            }
            builder.append("</descriptions>\n");
        }

        if (location.getNotes().isEmpty()) {
            builder.append("<notes />\n");
        } else {
            builder.append("<notes>\n");
            for (Note note : location.getNotes()) {
                builder.append("<note lang=\"").append(escape(note.getLanguage().getCode())).append("\">");
                builder.append(escape(note.getNote())).append("</note>\n");
            }
            builder.append("</notes>\n");
        }

        if (location.getStaffNotePri() != null) {
            if (location.getStaffNotePri().isEmpty()) {
                builder.append("<staffNote1 />\n");
            } else {
                builder.append("<staffNote1>").append(escape(location.getStaffNotePri())).append("</staffNote1>\n");
            }
        } else {
            builder.append("<staffNote1 />\n");
        }

        if (location.getStaffNoteSec() != null) {
            if (location.getStaffNoteSec().isEmpty()) {
                builder.append("<staffNote2 />\n");
            } else {
                builder.append("<staffNote2>").append(escape(location.getStaffNoteSec())).append("</staffNote2>\n");
            }
        } else {
            builder.append("<staffNote2 />\n");
        }

        if (location.getImage() == null) {
            builder.append("<image />\n");
        } else {
            StringBuilder url = new StringBuilder();
            if (location.getImage().getIsExternal()) {
                url.append(escape(location.getImage().getPath()));
            } else {
                url.append(webpath).append(imagesPath).append(escape(location.getImage().getPath()));
            }
            builder.append("<image>").append(url).append("</image>\n");
        }

        if (location.getMap() == null) {
            builder.append("<maps />\n");
        } else {
            builder.append("<maps>\n");
            if (location.getMap().getIsExternal()) {
                builder.append("<map lang=\"undefined\">").append(escape(location.getMap().getPath())).append("</map>\n");
            } else {
                for (Language lang : languages) {
                    builder.append("<map lang=\"").append(escape(lang.getCode())).append("\">");
                    builder.append(webpath).append("ImageCreator?locationId=").append(location.getLocationId());
                    builder.append("&amp;lang=").append(escape(lang.getCode())).append("&amp;owner=").append(escape(location.getOwner().getCode()));
                    builder.append("</map>\n");
                }
            }
            builder.append("</maps>\n");
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

        if (locationType != LocationType.LIBRARY) {
            if (location.getSubjectMatters().isEmpty()) {
                builder.append("<subjects />\n");
            } else {
                builder.append("<subjects>\n");
                for (SubjectMatter subject : location.getSubjectMatters()) {
                    builder.append("<subject lang=\"").append(escape(subject.getLanguage().getCode()));
                    builder.append("\">").append(escape(subject.getIndexTerm())).append("</subject>\n");
                }
                builder.append("</subjects>\n");
            }
        }

        switch (locationType) {
            case LIBRARY:
                if (children) {
                    Library lib = (Library) location;
                    if (lib.getCollections().isEmpty()) {
                        builder.append("<collections />\n");
                    } else {
                        builder.append("<collections>\n");
                        for (LibraryCollection col : lib.getCollections()) {
                            builder.append(locationToXml(col, languages, true)).append("\n");
                        }
                        builder.append("</collections>\n");
                    }
                }
                builder.append("</location>\n");
                break;
            case COLLECTION:
                LibraryCollection col = (LibraryCollection) location;
                if (col.getShelfNumber().isEmpty()) {
                    builder.append("<shelfnumber />\n");
                } else {
                    builder.append("<shelfnumber>").append(escape(col.getShelfNumber())).append("</shelfnumber>\n");
                }
                if (children) {
                    if (col.getShelves().isEmpty()) {
                        builder.append("<shelves />\n");
                    } else {
                        builder.append("<shelves>\n");
                        for (Shelf shelf : col.getShelves()) {
                            builder.append(locationToXml(shelf, languages, true)).append("\n");
                        }
                        builder.append("</shelves>\n");
                    }
                }
                builder.append("</location>\n");
                break;
            case SHELF:
                Shelf shelf = (Shelf) location;
                if (shelf.getShelfNumber().isEmpty()) {
                    builder.append("<shelfnumber />\n");
                } else {
                    builder.append("<shelfnumber>").append(escape(shelf.getShelfNumber())).append("</shelfnumber>\n");
                }
                builder.append("</location>\n");
                break;
        }
        return builder.toString().trim();
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
