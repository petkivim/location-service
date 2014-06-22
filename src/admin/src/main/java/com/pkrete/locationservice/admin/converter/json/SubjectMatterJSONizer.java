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

import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import com.pkrete.locationservice.admin.util.DateTimeUtil;
import java.util.List;

/**
 * This class converts SubjectMatter objects to JSON.
 * 
 * @author Petteri Kivimäki
 */
public class SubjectMatterJSONizer implements JSONizerService<SubjectMatter> {

    /**
     * Converts a single SubjectMatter object to JSON string. All the 
     * variables of the SubjectMatter object are included.
     * @param source SubjectMatter object to be converted
     * @return JSON string
     */
    public String jsonize(SubjectMatter source) {
        return this.jsonize(source, false);
    }

    /**
     * Converts a single SubjectMatter object to JSON string. All the 
     * variables of the SubjectMatter object are included. If logEntry is true, 
     * also owner_id is included.
     * @param source SubjectMatter object to be converted
     * @param logEntry is this JSON for a log entry
     * @return JSON string
     */
    public String jsonize(SubjectMatter source, boolean logEntry) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":").append(source.getId());
        builder.append(",\"index_term\":\"").append(source.getIndexTerm()).append("\"");
        builder.append(",\"language_id\":").append(source.getLanguage().getId()).append("");
        if (logEntry) {
            builder.append(",\"owner_id\":").append(source.getOwner().getId()).append("");
        }
        builder.append(",\"created_at\":\"");
        builder.append(DateTimeUtil.dateToString(source.getCreated())).append("\"");
        builder.append(",\"create_operator\":\"").append(source.getCreator()).append("\"");
        builder.append(",\"updated_at\":\"");
        builder.append((source.getUpdated() == null ? "" : DateTimeUtil.dateToString(source.getUpdated()))).append("\"");
        builder.append(",\"update_operator\":\"").append((source.getUpdater() == null ? "" : source.getUpdater())).append("\"");
        builder.append("}");
        return builder.toString();
    }

    /**
     * Converts a list of SubjectMatter objects to JSON string. Only selected
     * variables are included.
     * @param sources SubjectMatter objects to be converted
     * @return JSON string
     */
    public String jsonize(List<SubjectMatter> sources) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < sources.size(); i++) {
            SubjectMatter temp = sources.get(i);
            builder.append("{\"id\":").append(temp.getId());
            builder.append(",\"index_term\":\"").append(temp.getIndexTerm()).append("\"");
            builder.append(",\"language_id\":").append(temp.getLanguage().getId()).append("");
            builder.append("}");
            if (i < sources.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
