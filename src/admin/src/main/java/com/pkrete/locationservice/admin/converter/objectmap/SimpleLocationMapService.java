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

import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import com.pkrete.locationservice.admin.model.search.LocationType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class converts SimpleLocation objects to Map.
 * 
 * @author Petteri Kivimäki
 */
public class SimpleLocationMapService implements ObjectMapService<SimpleLocation> {

    /**
     * Converts a single SimpleLocation object to Map object. All the 
     * variables of the SimpleLocation object are included.
     * @param source SimpleLocation object to be converted
     * @return Map object
     */
    public Map convert(SimpleLocation source) {
        return this.convert(source, false);
    }

    /**
     * Converts a single SimpleLocation object to Map object. All the 
     * variables of the SimpleLocation object are included. If logEntry is true, 
     * also owner_id is included.
     * @param source SimpleLocation object to be converted
     * @param logEntry is this for a log entry
     * @return Map object
     */
    public Map convert(SimpleLocation source, boolean logEntry) {
        Map map = new LinkedHashMap();
            map.put("id", source.getLocationId());
            map.put("name", source.getName());
            map.put("location_code", source.getLocationCode());
            map.put("call_number", source.getCallNo());
            if (source.getLocationType() == LocationType.COLLECTION) {
                map.put("collection_code", source.getCollectionCode());
            }
        return map;
    }

    /**
     * Converts a list of SimpleLocation objects to a list of Map objects. Only 
     * selected variables are included.
     * @param sources SimpleLocation objects to be converted
     * @return list of Map objects
     */
    public List convert(List<SimpleLocation> sources) {
        List locations = new ArrayList();
        for (SimpleLocation source : sources) {
            Map map = new LinkedHashMap();
            map.put("id", source.getLocationId());
            map.put("name", source.getName());
            map.put("location_code", source.getLocationCode());
            map.put("call_number", source.getCallNo());
            if (source.getLocationType() == LocationType.COLLECTION) {
                map.put("collection_code", source.getCollectionCode());
            }
            locations.add(map);
        }
        return locations;
    }
}
