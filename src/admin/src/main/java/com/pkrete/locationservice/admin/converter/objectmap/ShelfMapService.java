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

import com.pkrete.locationservice.admin.model.location.Shelf;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class converts Shelf objects to Map.
 * 
 * @author Petteri Kivimäki
 */
public class ShelfMapService extends LocationMapService implements ObjectMapService<Shelf> {

    /**
     * Converts a single Shelf object to Map object. All the 
     * variables of the Shelf object are included.
     * @param source Shelf object to be converted
     * @return Map object
     */
    public Map convert(Shelf source) {
        return this.convert(source, false);
    }

    /**
     * Converts a single Shelf object to Map object. All the 
     * variables of the Shelf object are included. If logEntry is true, 
     * also owner_id is included.
     * @param source Shelf object to be converted
     * @param logEntry is this for a log entry
     * @return Map object
     */
    public Map convert(Shelf source, boolean logEntry) {
        Map shelf = new LinkedHashMap();
        super.convertBasicGroup1(shelf, source);
        shelf.put("library_id", source.getCollection().getLibrary().getLocationId());
        shelf.put("collection_id", source.getCollection().getLocationId());
        shelf.put("shelf_number", source.getShelfNumber());
        super.convertDescriptionsAndNotes(shelf, source);
        super.convertBasicGroup2(shelf, source);
        super.convertAreas(shelf, source);
        super.convertSubjectMatters(shelf, source);
        super.convertBasicGroup3(shelf, source, logEntry);
        
        return shelf;
    }

    /**
     * Converts a list of Shelf objects to a list of Map objects. Only 
     * selected variables are included.
     * @param sources Shelf objects to be converted
     * @return list of Map objects
     */
    public List convert(List<Shelf> sources) {
        List shelves = new ArrayList();
        for (Shelf source : sources) {
            Map shelf = new LinkedHashMap();
            super.convertForList(shelf, source);
            shelves.add(shelf);
        }
        return shelves;
    }
}

