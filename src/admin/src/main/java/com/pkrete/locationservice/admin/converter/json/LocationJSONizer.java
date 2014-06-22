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

import com.pkrete.locationservice.admin.model.location.Area;
import com.pkrete.locationservice.admin.model.location.Description;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.location.Note;
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.util.DateTimeUtil;

/**
 * This class offers methods that can be used when converting
 * Location objects to JSON Strings.
 * 
 * @author Petteri Kivimäki
 */
public abstract class LocationJSONizer {

    /**
     * Appends locationId, name, locationCode, callNumber and floor variables
     * to the given StringBuilder object.
     * @param map container for the variables
     * @param source Location object
     */
    protected void convertBasicGroup1(StringBuilder builder, Location source) {
        builder.append("\"id\":").append(source.getLocationId());
        builder.append(",\"name\":\"").append(source.getName()).append("\"");
        builder.append(",\"location_code\":\"").append(source.getLocationCode()).append("\"");
        builder.append(",\"call_number\":\"").append(source.getCallNo()).append("\"");
        builder.append(",\"floor\":\"").append(source.getFloor()).append("\"");
    }

    /**
     * Appends staffNote1, staffNote2, imageId and mapId variables
     * to the given StringBuilder object.
     * @param map container for the variables
     * @param source Location object
     */
    protected void convertBasicGroup2(StringBuilder builder, Location source) {
        builder.append(",\"staff_note_1\":\"").append(source.getStaffNotePri()).append("\"");
        builder.append(",\"staff_note_2\":\"").append(source.getStaffNoteSec()).append("\"");
        builder.append(",\"image_id\":").append(source.getImage() == null ? 0 : source.getImage().getId());
        builder.append(",\"map_id\":").append(source.getMap() == null ? 0 : source.getMap().getId());
    }

    /**
     * Appends ownerId, createdAt, creator, updatedAt and updater variables
     * to the given StringBuilder object. OwnerId is included if and only if 
     * the given logEntry parameter is true.
     * @param map container for the variables
     * @param source Location object
     * @param logEntry if true, include ownerId
     */
    protected void convertBasicGroup3(StringBuilder builder, Location source, boolean logEntry) {
        if (logEntry) {
            builder.append(",\"owner_id\":").append(source.getOwner().getId()).append("");
        }
        builder.append(",\"created_at\":\"");
        builder.append(DateTimeUtil.dateToString(source.getCreated())).append("\"");
        builder.append(",\"create_operator\":\"").append(source.getCreator()).append("\"");
        builder.append(",\"updated_at\":\"");
        builder.append((source.getUpdated() == null ? "" : DateTimeUtil.dateToString(source.getUpdated()))).append("\"");
        builder.append(",\"update_operator\":\"").append((source.getUpdater() == null ? "" : source.getUpdater())).append("\"");
    }

    /**
     * Appends descriptions and notes variales to the given StringBuilder object.
     * @param map container for the variables
     * @param source Location object
     */
    protected void convertDescriptionsAndNotes(StringBuilder builder, Location source) {
        builder.append(",\"descriptions\":[");
        for (int i = 0; i < source.getDescriptions().size(); i++) {
            Description temp = source.getDescriptions().get(i);
            builder.append("{\"id\":").append(temp.getId());
            builder.append(",\"language_id\":").append(temp.getLanguage().getId()).append("");
            builder.append(",\"value\":\"").append(temp.getDescription()).append("\"}");
            if (i < source.getDescriptions().size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        builder.append(",\"notes\":[");
        for (int i = 0; i < source.getNotes().size(); i++) {
            Note temp = source.getNotes().get(i);
            builder.append("{\"id\":").append(temp.getId());
            builder.append(",\"language_id\":").append(temp.getLanguage().getId());
            builder.append(",\"value\":\"").append(temp.getNote()).append("\"}");
            if (i < source.getDescriptions().size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
    }

    /**
     * Appends subject matter variales to the given StringBuilder object.
     * @param map container for the variables
     * @param source Location object
     */
    protected void convertSubjectMatters(StringBuilder builder, Location source) {
        builder.append(",\"subject_matters\":[");
        if (source.getSubjectMatters() != null) {
            for (int i = 0; i < source.getSubjectMatters().size(); i++) {
                SubjectMatter temp = source.getSubjectMatters().get(i);
                builder.append("{\"id\":").append(temp.getId());
                builder.append(",\"language_id\":").append(temp.getLanguage().getId()).append("");
                builder.append(",\"index_term\":\"").append(temp.getIndexTerm()).append("\"}");
                if (i < source.getSubjectMatters().size() - 1) {
                    builder.append(",");
                }
            }
        }
        builder.append("]");
    }

    /**
     * Appends areas variable to the given StringBuilder object.
     * @param map container for the variables
     * @param source Location object
     */
    protected void convertAreas(StringBuilder builder, Location source) {
        builder.append(",\"areas\":[");
        // If map not null or Google Map, add areas
        if (source.getMap() != null && !source.getMap().getIsGoogleMap()) {
            for (int i = 0; i < source.getAreas().size(); i++) {
                Area temp = source.getAreas().get(i);
                builder.append("{\"area_id\":").append(temp.getAreaId());
                builder.append(",\"x1\":").append(temp.getX1());
                builder.append(",\"y1\":").append(temp.getY1());
                builder.append(",\"x2\":").append(temp.getX2());
                builder.append(",\"y2\":").append(temp.getY2());
                builder.append(",\"angle\":").append(temp.getAngle()).append("}");
                if (i < source.getAreas().size() - 1) {
                    builder.append(",");
                }
            }
        }
        builder.append("]");
    }

    /**
     * Appends locationId, name, locationCode and callNumber variables
     * to the given StringBuilder object.
     * @param map container for the variables
     * @param source Location object
     */
    protected void convertForList(StringBuilder builder, Location source) {
        builder.append("\"id\":").append(source.getLocationId());
        builder.append(",\"name\":\"").append(source.getName()).append("\"");
        builder.append(",\"location_code\":\"").append(source.getLocationCode()).append("\"");
        builder.append(",\"call_number\":\"").append(source.getCallNo()).append("\"");
    }
}
