/**
 * This file is part of Location Service :: Admin. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Admin is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Admin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Admin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.admin.generator.xml;

import com.pkrete.locationservice.admin.generator.Generator;
import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import com.pkrete.locationservice.admin.model.search.LocationType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * This class creates a XML presentation of a given Location object and returns
 * it as a String. This functionality is needed when exporting data from
 * LocationService or returning results to a query received via a search
 * interface.
 *
 * @author Petteri Kivimäki
 */
public class XmlGenerator implements Generator {

    private Map<String, String> filters;

    /**
     * Creates and initializes a new XmlGenerator object.
     */
    public XmlGenerator() {
        this.filters = new LinkedHashMap<String, String>();
        this.filters.put("&", "&amp;");
        this.filters.put("<", "&lt;");
        this.filters.put(">", "&gt;");
        this.filters.put("\"", "&quot;");
        this.filters.put("'", "&apos;");
    }

    /**
     * Sets the filters variable.
     *
     * @param filters new value
     */
    public void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }

    /**
     * Returns XML presentation of the given locations.
     *
     * @param locations locations to be presented in XML
     * @return XML presentation of the given locations
     */
    @Override
    public String generate(List locations) {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

        if (locations.isEmpty()) {
            builder.append("<locations/>\n");
            return builder.toString().trim();
        }

        builder.append("<locations>\n");
        for (SimpleLocation location : (List<SimpleLocation>) locations) {
            builder.append(generate(location));
        }

        builder.append("</locations>\n");
        return builder.toString().trim();
    }

    /**
     * Returns XML presentation of the given location.
     *
     * @param location location to be presented in XML
     * @return XML presentation of the given location
     */
    @Override
    public String generate(SimpleLocation location) {
        StringBuilder builder = new StringBuilder();
        String collectionCode = "";

        if (location.getLocationType() == LocationType.LIBRARY) {
            builder.append("<location type=\"library\">\n");
        } else if (location.getLocationType() == LocationType.COLLECTION) {
            builder.append("<location type=\"collection\">\n");
            if (location.hasCollectionCode()) {
                collectionCode = "<collectioncode>" + decode(location.getCollectionCode()) + "</collectioncode>\n";
            } else {
                collectionCode = "<collectioncode />\n";
            }
        } else if (location.getLocationType() == LocationType.SHELF) {
            builder.append("<location type=\"shelf\">\n");
            if (location.hasCollectionCode()) {
                collectionCode = "<collectioncode>" + decode(location.getCollectionCode()) + "</collectioncode>\n";
            } else {
                collectionCode = "<collectioncode />\n";
            }
        }

        builder.append("<locationid>").append(location.getLocationId()).append("</locationid>\n");

        builder.append("<name>").append(decode(location.getName())).append("</name>\n");

        if (location.getLocationCode().isEmpty()) {
            builder.append("<locationcode />\n");
        } else {
            builder.append("<locationcode>").append(decode(location.getLocationCode())).append("</locationcode>\n");
        }

        builder.append(collectionCode);

        if (location.getCallNo().isEmpty()) {
            builder.append("<callnumber />");
        } else {
            builder.append("<callnumber>").append(decode(location.getCallNo())).append("</callnumber>\n");
        }

        builder.append("</location>\n");

        return builder.toString().trim() + "\n";
    }

    private String decode(String data) {
        // Loop through the filters and replace all the keys with 
        // the given value
        for (String key : this.filters.keySet()) {
            data = data.replaceAll(key, this.filters.get(key));
        }
        return data;
    }
}
