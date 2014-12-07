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

import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.util.DateTimeUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class converts SubjectMatter objects to Map.
 *
 * @author Petteri Kivimäki
 */
public class SubjectMatterMapService implements ObjectMapService<SubjectMatter> {

    /**
     * Converts a single SubjectMatter object to Map object. All the variables
     * of the SubjectMatter object are included.
     *
     * @param source SubjectMatter object to be converted
     * @return Map object
     */
    @Override
    public Map convert(SubjectMatter source) {
        return this.convert(source, false);
    }

    /**
     * Converts a single SubjectMatter object to Map object. All the variables
     * of the SubjectMatter object are included. If logEntry is true, also
     * owner_id is included.
     *
     * @param source SubjectMatter object to be converted
     * @param logEntry is this for a log entry
     * @return Map object
     */
    @Override
    public Map convert(SubjectMatter source, boolean logEntry) {
        Map subject = new LinkedHashMap();
        subject.put("id", source.getId());
        subject.put("index_term", source.getIndexTerm());
        subject.put("language_id", source.getLanguage().getId());
        if (logEntry) {
            subject.put("owner_id", source.getOwner().getId());
        }
        subject.put("created_at", DateTimeUtil.dateToString(source.getCreated()));
        subject.put("create_operator", source.getCreator());
        subject.put("updated_at", (source.getUpdated() == null ? "" : DateTimeUtil.dateToString(source.getUpdated())));
        subject.put("update_operator", (source.getUpdater() == null ? "" : source.getUpdater()));
        return subject;
    }

    /**
     * Converts a list of SubjectMatter objects to a list of Map objects. Only
     * selected variables are included.
     *
     * @param sources SubjectMatter objects to be converted
     * @return list of Map objects
     */
    @Override
    public List convert(List<SubjectMatter> sources) {
        List subjects = new ArrayList();
        for (SubjectMatter source : sources) {
            Map subject = new LinkedHashMap();
            subject.put("id", source.getId());
            subject.put("index_term", source.getIndexTerm());
            subject.put("language_id", source.getLanguage().getId());
            subjects.add(subject);
        }
        return subjects;
    }
}
