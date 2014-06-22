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

import com.pkrete.locationservice.admin.model.owner.Owner;
import java.io.File;
import java.io.FileFilter;

/**
 * This utility class contains helper methods related to JavaScript files.
 * 
 * @author Petteri Kivimäki
 */
public class JSUtil {

    /**
     * Checks that the name has a valid file extension and adds it if
     * necessary.
     * @param name name of the css file
     * @return name with ".js" file extension
     */
    public static String buildJsName(String name) {
        // Check for null
        if (name == null) {
            return "";
        }
        // If name ends with ".js" it's OK
        if (name.endsWith(".js")) {
            return name;
        }
        return name + ".js";
    }

    /**
     * Checks if the format of the given js file name is valid. If the
     * name is valid returns true, otherwise returns false.
     * @param name js file name to be checked
     * @return if name is valid returns true; otherwise returns false
     */
    public static boolean isJsNameValid(String name) {
        // Check for null
        if (name == null) {
            return false;
        }
        // Is name valid?
        if (name.matches("^\\w+\\.js$")) {
            return true;
        }
        return false;
    }

    /**
     * Returns a FileFilter object that can used for getting a list of all
     * the JavaScript files in a directory. All the visible files with ".js"
     * extension are considered as JavaScript files.
     * @return FileFilter object for js files
     */
    public static FileFilter getJsFilter() {
        FileFilter filter = new FileFilter() {

            public boolean accept(File file) {
                if (file.isFile() && !file.isHidden() && file.getName().endsWith(".js")) {
                    return true;
                }
                return false;
            }
        };
        return filter;
    }

    /**
     * Builds the absolute path of the given file based on filename
     * and owner information. However, it's not tested whether the file exists
     * or not.
     * @param filename name of the js file
     * @param owner owner of the js file
     * @return absolute js file path
     */
    public static String buildFilePath(String filename, Owner owner) {
        // Get scripts path for the given Owner
        String path = Settings.getInstance().getScriptsPath(owner.getCode());
        // Build file path according to the given filename and owner
        StringBuilder filePath = new StringBuilder();
        filePath.append(path).append(filename);
        return filePath.toString();
    }
}
