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
package com.pkrete.locationservice.admin.converter.objectmap;

import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class converts LibraryCollection objects to Map.
 *
 * @author Petteri Kivimäki
 */
public class CollectionMapService extends LocationMapService implements ObjectMapService<LibraryCollection> {

    /**
     * Converts a single LibraryCollection object to Map object. All the
     * variables of the LibraryCollection object are included.
     *
     * @param source LibraryCollection object to be converted
     * @return Map object
     */
    @Override
    public Map convert(LibraryCollection source) {
        return this.convert(source, false);
    }

    /**
     * Converts a single LibraryCollection object to Map object. All the
     * variables of the LibraryCollection object are included. If logEntry is
     * true, also owner_id is included.
     *
     * @param source LibraryCollection object to be converted
     * @param logEntry is this for a log entry
     * @return Map object
     */
    @Override
    public Map convert(LibraryCollection source, boolean logEntry) {
        Map collection = new LinkedHashMap();
        super.convertBasicGroup1(collection, source);
        collection.put("library_id", source.getLibrary().getLocationId());
        collection.put("collection_code", source.getCollectionCode());
        collection.put("shelf_number", source.getShelfNumber());
        collection.put("match_beginning", source.getIsSubstring());
        super.convertDescriptionsAndNotes(collection, source);
        super.convertBasicGroup2(collection, source);
        super.convertAreas(collection, source);
        super.convertSubjectMatters(collection, source);
        super.convertBasicGroup3(collection, source, logEntry);

        return collection;
    }

    /**
     * Converts a list of LibraryCollection objects to a list of Map objects.
     * Only selected variables are included.
     *
     * @param sources LibraryCollection objects to be converted
     * @return list of Map objects
     */
    @Override
    public List convert(List<LibraryCollection> sources) {
        List collections = new ArrayList();
        for (LibraryCollection source : sources) {
            Map collection = new LinkedHashMap();
            super.convertForList(collection, source);
            collection.put("collection_code", source.getCollectionCode());
            collections.add(collection);
        }
        return collections;
    }
}
