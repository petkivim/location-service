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

import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import java.util.List;

/**
 * This class converts Library objects to JSON.
 * 
 * @author Petteri Kivimäki
 */
public class LibraryJSONizer extends LocationJSONizer implements JSONizerService<Library> {

    /**
     * Converts a single Library object to JSON string. All the variables of
     * the Library object are included.
     * @param source Library object to be converted
     * @return JSON string
     */
    public String jsonize(Library source) {
        return this.jsonize(source, false);
    }

    /**
     * Converts a single Library object to JSON string. All the variables of
     * the Library object are included. If logEntry is true, also owner_id
     * is included.
     * @param source Library object to be converted
     * @param logEntry is this JSON for a log entry
     * @return JSON string
     */
    public String jsonize(Library source, boolean logEntry) {
        StringBuilder library = new StringBuilder("{");
        super.convertBasicGroup1(library, source);
        super.convertDescriptionsAndNotes(library, source);
        super.convertBasicGroup2(library, source);
        super.convertAreas(library, source);
        super.convertBasicGroup3(library, source, logEntry);
        library.append("}");
        return library.toString();
    }

    /**
     * Converts a list of Library objects to JSON string. Only selected
     * variables are included.
     * @param sources Library objects to be converted
     * @return JSON string
     */
    public String jsonize(List<Library> sources) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < sources.size(); i++) {
            Library temp = sources.get(i);
            builder.append("{");
            super.convertForList(builder, temp);
            builder.append("}");
            if (i < sources.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
