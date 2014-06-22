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
package com.pkrete.locationservice.admin.util;

import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.model.illustration.Map;

/**
 * This utility class contains helper methods related to images and maps.
 * 
 * @author Petteri Kivimäki
 */
public class IllustrationsUtil {

    /**
     * Builds the absolute path of the given image file based on filename, 
     * and owner information. However, it's not tested whether the 
     * file exists or not.
     * @param image Image object
     * @return absolute image path; if image is external, null is returned
     */
    public static String buildFilePath(Image image) {
        // Can't build path for an external image
        if (image.getIsExternal()) {
            return null;
        }
        // Get images path
        String path = Settings.getInstance().getImagesPath(image.getOwner().getCode());
        // Build file path according to the name of the image file
        StringBuilder filePath = new StringBuilder();
        filePath.append(path);
        filePath.append(image.getPath());
        return filePath.toString();
    }

    /**
     * Builds the absolute path of the given map file based on filename, language 
     * and owner information. However, it's not tested whether the 
     * file exists or not.
     * @param map Map object
     * @return absolute map path; if map is external, null is returned
     */
    public static String buildFilePath(Map map, String lang) {
        // Can't build path for an external map
        if (map.getIsExternal()) {
            return null;
        }
        // Get images path
        String path = Settings.getInstance().getMapsPath(map.getOwner().getCode());
        // Build file path according to the name of the map file
        StringBuilder filePath = new StringBuilder();
        filePath.append(path);
        if (lang != null && !lang.isEmpty()) {
            filePath.append(lang).append("/");
        }
        filePath.append(map.getPath());
        return filePath.toString();
    }
}
