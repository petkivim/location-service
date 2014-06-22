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

import com.pkrete.locationservice.admin.model.template.Template;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class converts Template objects to Map.
 * 
 * @author Petteri Kivimäki
 */
public class TemplateMapService implements ObjectMapService<Template> {

    /**
     * Converts a single Template object to Map object. All the variables of
     * the Template object are included.
     * @param source Template object to be converted
     * @return Map object
     */
    public Map convert(Template source) {
        return this.convert(source, false);
    }

    /**
     * Converts a single Template object to Map object. All the variables of
     * the Template object are included. If logEntry is true, also owner_id
     * is included.
     * @param source Template object to be converted
     * @param logEntry is this for a log entry
     * @return Map object
     */
    public Map convert(Template source, boolean logEntry) {
        Map template = new LinkedHashMap();
        template.put("filename", source.getFilename());
        template.put("language_id", source.getLanguage().getId());
        template.put("contents", source.getContents());
        if (logEntry) {
            template.put("owner_id", source.getOwner().getId());
        }
        return template;
    }

    /**
     * Converts a list of Template objects to a list of Map objects. Only 
     * selected variables are included.
     * @param sources Template objects to be converted
     * @return list of Map objects
     */
    public List convert(List<Template> sources) {
        List templates = new ArrayList();
        for (Template source : sources) {
            Map template = new LinkedHashMap();
            template.put("filename", source.getFilename());
            template.put("language_id", source.getLanguage().getId());
            templates.add(template);
        }
        return templates;
    }
}
