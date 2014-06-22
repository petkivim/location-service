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
package com.pkrete.locationservice.admin.converter.objectmap;

import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class converts Library objects to Map.
 * 
 * @author Petteri Kivimäki
 */
public class LibraryMapService extends LocationMapService implements ObjectMapService<Library> {

    /**
     * Converts a single Library object to Map object. All the 
     * variables of the Library object are included.
     * @param source Library object to be converted
     * @return Map object
     */
    public Map convert(Library source) {
        return this.convert(source, false);
    }

    /**
     * Converts a single Library object to Map object. All the 
     * variables of the Library object are included. If logEntry is true, 
     * also owner_id is included.
     * @param source Library object to be converted
     * @param logEntry is this for a log entry
     * @return Map object
     */
    public Map convert(Library source, boolean logEntry) {
        Map lib = new LinkedHashMap();
        super.convertBasicGroup1(lib, source);
        super.convertDescriptionsAndNotes(lib, source);
        super.convertBasicGroup2(lib, source);
        super.convertAreas(lib, source);
        super.convertBasicGroup3(lib, source, logEntry);
        return lib;
    }

    /**
     * Converts a list of Library objects to a list of Map objects. Only 
     * selected variables are included.
     * @param sources Library objects to be converted
     * @return list of Map objects
     */
    public List convert(List<Library> sources) {
        List libraries = new ArrayList();
        for (Library source : sources) {
            Map lib = new LinkedHashMap();
            super.convertForList(lib, source);
            libraries.add(lib);
        }
        return libraries;
    }
}