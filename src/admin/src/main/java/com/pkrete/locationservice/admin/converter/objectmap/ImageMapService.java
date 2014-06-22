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

import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.util.DateTimeUtil;
import com.pkrete.locationservice.admin.util.Settings;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class converts Image objects to Map.
 * 
 * @author Petteri Kivimäki
 */
public class ImageMapService implements ObjectMapService<Image> {

    /**
     * Converts a single Image object to Map object. All the variables of
     * the Image object are included.
     * @param source Image object to be converted
     * @return Map object
     */
    public Map convert(Image source) {
        return this.convert(source, false);
    }

    /**
     * Converts a single Image object to Map object. All the variables of
     * the Image object are included. If logEntry is true, also owner_id
     * is included.
     * @param source Image object to be converted
     * @param logEntry is this for a log entry
     * @return Map object
     */
    public Map convert(Image source, boolean logEntry) {
        String url = null;
        if (source.getIsExternal()) {
            url = source.getPath();
        } else {
            url = Settings.getInstance().getImagesWebPath(source.getOwner().getCode());
            url += source.getPath();
        }
        Map image = new LinkedHashMap();
        image.put("id", source.getId());
        image.put("description", source.getDescription());
        image.put("is_external", source.getIsExternal());
        image.put("url", url);
        if (logEntry) {
            image.put("owner_id", source.getOwner().getId());
        }
        image.put("created_at", DateTimeUtil.dateToString(source.getCreated()));
        image.put("create_operator", source.getCreator());
        image.put("updated_at", (source.getUpdated() == null ? "" : DateTimeUtil.dateToString(source.getUpdated())));
        image.put("update_operator", (source.getUpdater() == null ? "" : source.getUpdater()));
        return image;
    }

    /**
     * Converts a list of Image objects to a list of Map objects. Only 
     * selected variables are included.
     * @param sources Image objects to be converted
     * @return list of Map objects
     */
    public List convert(List<Image> sources) {
        String url = null;
        if (!sources.isEmpty()) {
            url = Settings.getInstance().getImagesWebPath(sources.get(0).getOwner().getCode());
        }
        List images = new ArrayList();
        for (Image source : sources) {
            Map image = new LinkedHashMap();
            image.put("id", source.getId());
            image.put("description", source.getDescription());
            image.put("is_external", source.getIsExternal());
            image.put("url", (source.getIsExternal() ? source.getPath() : url + source.getPath()));
            images.add(image);
        }
        return images;
    }
}
