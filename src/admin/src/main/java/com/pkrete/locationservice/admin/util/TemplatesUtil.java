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

import com.pkrete.locationservice.admin.model.search.LocationType;
import com.pkrete.locationservice.admin.model.owner.Owner;

/**
 * This utility class contains helper methods related to templates.
 * 
 * @author Petteri Kivimäki
 */
public class TemplatesUtil {

    /**
     * Checks if the format of the given template name is valid. If the
     * name is valid returns true, otherwise returns false.
     * @param name template name to be checked
     * @return if name is valid returns true, otherwise returns false
     */
    public static boolean isTemplateNameValid(String name) {
        // Check for null value
        if (name == null) {
            return false;
        }
        // If name contains whitespaces, it's not valid
        if (name.matches(".*\\s+.*")) {
            return false;
        }
        // If name contains two consecutive dots, it's not valid
        if (name.matches(".*\\.{2}.*")) {
            return false;
        }
        // If name contains slashes or backslashes, it's not valid
        if (name.matches(".*[/\\\\].*")) {
            return false;
        }        
        if (name.matches("^template_(library|collection|shelf)(\\.txt|_.*\\.txt)$")) {
            return true;
        }
        if (name.matches("^template_other_.+\\.txt$")) {
            return true;
        }
        if (name.matches("^template_not_(available|found)\\.txt$")) {
            return true;
        }
        return false;
    }

    /**
     * Returns a TemplateType enum corresponding the given string presentation.
     * If no matching enum value is found, null is returned.
     * @param type string presentation of template type
     * @return TemplateType enum or null
     */
    public static TemplateType getTemplateType(String type) {
        // Check that the type is not null
        if (type == null) {
            return null;
        }
        if (type.equalsIgnoreCase(TemplateType.LIBRARY.toString())) {
            return TemplateType.LIBRARY;
        } else if (type.equalsIgnoreCase(TemplateType.COLLECTION.toString())) {
            return TemplateType.COLLECTION;
        } else if (type.equalsIgnoreCase(TemplateType.SHELF.toString())) {
            return TemplateType.SHELF;
        } else if (type.equalsIgnoreCase(TemplateType.NOT_AVAILABLE.toString())) {
            return TemplateType.NOT_AVAILABLE;
        } else if (type.equalsIgnoreCase(TemplateType.NOT_FOUND.toString())) {
            return TemplateType.NOT_FOUND;
        } else if (type.equalsIgnoreCase(TemplateType.OTHER.toString())) {
            return TemplateType.OTHER;
        }
        return null;
    }

    /**
     * Builds the absolute path of the given file based on filename, language
     * and owner information. However, it's not tested whether the file exists
     * or not.
     * @param filename name of the template
     * @param lang language of the template
     * @param owner owner of the template
     * @return absolute template path
     */
    public static String buildFilePath(String filename, String lang, Owner owner) {
        // Get templates path for the given Owner
        String path = Settings.getInstance().getTemplatesPath(owner.getCode());
        // Build file path according to the given filename and language
        StringBuilder filePath = new StringBuilder();
        filePath.append(path);
        if (lang != null && !lang.isEmpty()) {
            filePath.append(lang).append("/");
        }
        filePath.append(filename);
        return filePath.toString();
    }
    
    /**
     * Validates the given template type - location type combination, and
     * returns true if and only if the given combination is valid.
     * @param templateType template type
     * @param locationType location type
     * @return returns true if and only if the given combination is valid; 
     * otherwise false
     */
    public static boolean validate(TemplateType templateType, LocationType locationType) {
        // Location type can be null, because some templates are not related
        // to any specific location
        if(locationType == null) {
            return true;
        }
        // Template type can't be null
        if(templateType == null) {
            return false;
        }
        // If template type is LIBRARY, location type must be LIBRARY
        if(templateType == TemplateType.LIBRARY && locationType != LocationType.LIBRARY) {
            return false;
        } else if(templateType != TemplateType.SHELF && locationType == LocationType.SHELF) {
            // If location type is SHELF, template type can't be anything else
            // than SHELF
            return false;
        }
        // Other combinations are OK
        return true;
    }
}
