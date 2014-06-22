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

import com.pkrete.locationservice.admin.model.template.CSS;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.util.Settings;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class converts CSS objects to Map.
 * 
 * @author Petteri Kivimäki
 */
public class CSSMapService implements ObjectMapService<CSS> {

    /**
     * Converts a single CSS object to Map object. All the variables of
     * the Template object are included.
     * @param source CSS object to be converted
     * @return Map object
     */
    public Map convert(CSS source) {
        return this.convert(source, false);
    }

    /**
     * Converts a single CSS object to Map object. All the variables of
     * the CSS object are included. If logEntry is true, also owner_id
     * is included.
     * @param source CSS object to be converted
     * @param logEntry is this for a log entry
     * @return Map object
     */
    public Map convert(CSS source, boolean logEntry) {
        // Get css relative path
        String path = Settings.getInstance().getStylesheetsPathRel(source.getOwner().getCode());
        // Get css URL
        String url = Settings.getInstance().getWebpath();

        Map css = new LinkedHashMap();
        css.put("filename", source.getFilename());
        css.put("rel_path", path + source.getFilename());
        css.put("url", url + path + source.getFilename());
        css.put("contents", source.getContents());
        if (logEntry) {
            css.put("owner_id", source.getOwner().getId());
        }
        return css;
    }

    /**
     * Converts a list of CSS objects to a list of Map objects. Only 
     * selected variables are included.
     * @param sources CSS objects to be converted
     * @return list of Map objects
     */
    public List convert(List<CSS> sources) {
        // Get css relative path
        String path = "";
        if (sources != null && !sources.isEmpty()) {
            path = Settings.getInstance().getStylesheetsPathRel(sources.get(0).getOwner().getCode());
        }
        // Get css URL
        String url = Settings.getInstance().getWebpath();

        List files = new ArrayList();
        for (CSS source : sources) {
            Map css = new LinkedHashMap();
            css.put("filename", source.getFilename());
            css.put("rel_path", path + source.getFilename());
            css.put("url", url + path + source.getFilename());
            files.add(css);
        }
        return files;
    }
}
