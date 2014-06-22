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

import com.pkrete.locationservice.admin.model.location.Area;
import com.pkrete.locationservice.admin.model.location.Description;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.location.Note;
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.util.DateTimeUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This abstract class offers methods that can be used when converting
 * Location objects to Map.
 * 
 * @author Petteri Kivimäki
 */
public abstract class LocationMapService {

    /**
     * Adds locationId, name, locationCode, callNumber and floor variables
     * to the given Map.
     * @param map container for the variables
     * @param source Location object
     */
    protected void convertBasicGroup1(Map map, Location source) {
        map.put("id", source.getLocationId());
        map.put("name", source.getName());
        map.put("location_code", source.getLocationCode());
        map.put("call_number", source.getCallNo());
        map.put("floor", source.getFloor());
    }

    /**
     * Adds staffNote1, staffNote2, imageId and mapId variables
     * to the given Map.
     * @param map container for the variables
     * @param source Location object
     */
    protected void convertBasicGroup2(Map map, Location source) {
        map.put("staff_note_1", source.getStaffNotePri());
        map.put("staff_note_2", source.getStaffNoteSec());
        map.put("image_id", source.getImage() == null ? 0 : source.getImage().getId());
        map.put("map_id", source.getMap() == null ? 0 : source.getMap().getId());
    }

    /**
     * Adds ownerId, createdAt, creator, updatedAt and updater variables
     * to the given Map. OwnerId is included if and only if the given logEntry
     * parameter is true.
     * @param map container for the variables
     * @param source Location object
     * @param logEntry if true, include ownerId
     */
    protected void convertBasicGroup3(Map map, Location source, boolean logEntry) {
        if (logEntry) {
            map.put("owner_id", source.getOwner().getId());
        }
        map.put("created_at", DateTimeUtil.dateToString(source.getCreated()));
        map.put("create_operator", source.getCreator());
        map.put("updated_at", (source.getUpdated() == null ? "" : DateTimeUtil.dateToString(source.getUpdated())));
        map.put("update_operator", (source.getUpdater() == null ? "" : source.getUpdater()));
    }

    /**
     * Adds descriptions and notes variables to the given Map.
     * @param map container for the variables
     * @param source Location object
     */
    protected void convertDescriptionsAndNotes(Map map, Location source) {
        // Add descriptions
        List descriptions = new ArrayList();
        for (Description desc : source.getDescriptions()) {
            Map temp = new LinkedHashMap();
            temp.put("id", desc.getId());
            temp.put("lang_id", desc.getLanguage().getId());
            temp.put("value", desc.getDescription());
            descriptions.add(temp);
        }
        map.put("descriptions", descriptions);

        // Add Notes
        List notes = new ArrayList();
        for (Note note : source.getNotes()) {
            Map temp = new LinkedHashMap();
            temp.put("id", note.getId());
            temp.put("lang_id", note.getLanguage().getId());
            temp.put("value", note.getNote());
            notes.add(temp);
        }
        map.put("notes", notes);
    }

    /**
     * Adds subject matter variables to the given Map.
     * @param map container for the variables
     * @param source Location object
     */
    protected void convertSubjectMatters(Map map, Location source) {
        // Add descriptions
        List descriptions = new ArrayList();
        for (SubjectMatter subject : source.getSubjectMatters()) {
            Map temp = new LinkedHashMap();
            temp.put("id", subject.getId());
            temp.put("lang_id", subject.getLanguage().getId());
            temp.put("index_term", subject.getIndexTerm());
            descriptions.add(temp);
        }
        map.put("subject_matters", descriptions);
    }

    /**
     * Adds areas variable to the given Map.
     * @param map container for the variables
     * @param source Location object
     */
    protected void convertAreas(Map map, Location source) {
        // If map not null or Google Map, add areas
        if (source.getMap() != null && !source.getMap().getIsGoogleMap()) {
            List areas = new ArrayList();
            for (Area area : source.getAreas()) {
                Map temp = new LinkedHashMap();
                temp.put("id", area.getAreaId());
                temp.put("x1", area.getX1());
                temp.put("y1", area.getY1());
                temp.put("x2", area.getX2());
                temp.put("y2", area.getY2());
                temp.put("angle", area.getAngle());
                areas.add(temp);
            }
            map.put("areas", areas);
        }
    }

    /**
     * Adds locationId, name, locationCode and callNumber variables
     * to the given Map.
     * @param map container for the variables
     * @param source Location object
     */
    protected void convertForList(Map map, Location source) {
        map.put("id", source.getLocationId());
        map.put("name", source.getName());
        map.put("location_code", source.getLocationCode());
        map.put("call_number", source.getCallNo());
    }
}
