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

import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.util.DateTimeUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class converts Language objects to Map.
 *
 * @author Petteri Kivimäki
 */
public class LanguageMapService implements ObjectMapService<Language> {

    /**
     * Converts a single language object to Map object. All the variables of the
     * Language object are included.
     *
     * @param source Language object to be converted
     * @return Map object
     */
    @Override
    public Map convert(Language source) {
        return this.convert(source, false);
    }

    /**
     * Converts a single language object to Map object. All the variables of the
     * Language object are included. If logEntry is true, also owner_id is
     * included.
     *
     * @param source Language object to be converted
     * @param logEntry is this for a log entry
     * @return Map object
     */
    @Override
    public Map convert(Language source, boolean logEntry) {
        Map language = new LinkedHashMap();
        language.put("id", source.getId());
        language.put("code", source.getCode());
        language.put("name", source.getName());
        if (logEntry) {
            language.put("owner_id", source.getOwner().getId());
        }
        language.put("created_at", DateTimeUtil.dateToString(source.getCreated()));
        language.put("create_operator", source.getCreator());
        language.put("updated_at", (source.getUpdated() == null ? "" : DateTimeUtil.dateToString(source.getUpdated())));
        language.put("update_operator", (source.getUpdater() == null ? "" : source.getUpdater()));
        return language;
    }

    /**
     * Converts a list of Language objects to a list of Map objects. Only
     * selected variables are included.
     *
     * @param sources Language objects to be converted
     * @return list of Map objects
     */
    @Override
    public List convert(List<Language> sources) {
        List languages = new ArrayList();
        for (Language source : sources) {
            Map language = new LinkedHashMap();
            language.put("id", source.getId());
            language.put("code", source.getCode());
            language.put("name", source.getName());
            languages.add(language);
        }
        return languages;
    }
}
