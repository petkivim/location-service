/**
 * This file is part of Location Service :: Admin.
 * Copyright (C) 2014 Petteri Kivimäki
 *
 * Location Service :: Admin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Location Service :: Admin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Location Service :: Admin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.admin.converter.json;

import com.pkrete.locationservice.admin.model.location.Shelf;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import java.util.List;

/**
 * This class converts Shelf objects to JSON.
 * 
 * @author Petteri Kivimäki
 */
public class ShelfJSONizer extends LocationJSONizer implements JSONizerService<Shelf> {

    /**
     * Converts a single Shelf object to JSON string. All the variables of
     * the Shelf object are included.
     * @param source Shelf object to be converted
     * @return JSON string
     */
    public String jsonize(Shelf source) {
        return this.jsonize(source, false);
    }

    /**
     * Converts a single Shelf object to JSON string. All the variables of
     * the Shelf object are included. If logEntry is true, also owner_id
     * is included.
     * @param source Shelf object to be converted
     * @param logEntry is this JSON for a log entry
     * @return JSON string
     */
    public String jsonize(Shelf source, boolean logEntry) {
        StringBuilder collection = new StringBuilder("{");
        super.convertBasicGroup1(collection, source);
        collection.append(",\"library_id\":").append(source.getCollection().getLibrary().getLocationId());
        collection.append(",\"collection_id\":").append(source.getCollection().getLocationId());
        collection.append(",\"shelf_number\":\"").append(source.getShelfNumber()).append("\"");
        super.convertDescriptionsAndNotes(collection, source);
        super.convertBasicGroup2(collection, source);
        super.convertAreas(collection, source);
        super.convertSubjectMatters(collection, source);
        super.convertBasicGroup3(collection, source, logEntry);
        collection.append("}");
        return collection.toString();
    }

    /**
     * Converts a list of LibraryCollection objects to JSON string. Only selected
     * variables are included.
     * @param sources LibraryCollection objects to be converted
     * @return JSON string
     */
    public String jsonize(List<Shelf> sources) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < sources.size(); i++) {
            Shelf temp = sources.get(i);
            builder.append("{");
            super.convertForList(builder, temp);
            builder.append("\"}");
            if (i < sources.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}