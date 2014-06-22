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

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.util.DateTimeUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class converts Owner objects to Map.
 * 
 * @author Petteri Kivimäki
 */
public class OwnerMapService implements ObjectMapService<Owner> {

    /**
     * Converts a single Owner object to Map object. All the 
     * settigns variables are excluded.
     * @param source Owner object to be converted
     * @return Map object
     */
    public Map convert(Owner source) {
        return this.convert(source, false);
    }

    /**
     * Converts a single Owner object to Map object. All the 
     * settigns variables are excluded.
     * @param source Owner object to be converted
     * @param logEntry is this for a log entry, ignored
     * @return Map object
     */
    public Map convert(Owner source, boolean logEntry) {
        Map owner = new LinkedHashMap();
        owner.put("id", source.getId());
        owner.put("code", source.getCode());
        owner.put("name", source.getName());        
        owner.put("created_at", (source.getCreated() == null ? "" : DateTimeUtil.dateToString(source.getCreated())));
        owner.put("create_operator", (source.getCreator() == null ? "" : source.getCreator()));
        owner.put("updated_at", (source.getUpdated() == null ? "" : DateTimeUtil.dateToString(source.getUpdated())));
        owner.put("update_operator", (source.getUpdater() == null ? "" : source.getUpdater()));
        return owner;
    }

    /**
     * Converts a list of Owner objects to a list of Map objects. Only 
     * selected variables are included.
     * @param sources Owner objects to be converted
     * @return list of Map objects
     */
    public List convert(List<Owner> sources) {
        List owners = new ArrayList();
        for (Owner source : sources) {
            Map owner = new LinkedHashMap();
            owner.put("id", source.getId());
            owner.put("code", source.getCode());
            owner.put("name", source.getName());
            owners.add(owner);
        }
        return owners;
    }
}
