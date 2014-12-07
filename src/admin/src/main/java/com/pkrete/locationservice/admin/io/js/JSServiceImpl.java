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
package com.pkrete.locationservice.admin.io.js;

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.io.DirectoryService;
import com.pkrete.locationservice.admin.io.FileService;
import com.pkrete.locationservice.admin.io.JSService;
import com.pkrete.locationservice.admin.util.JSUtil;
import com.pkrete.locationservice.admin.util.Settings;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSServiceImpl class implements the {@link JSService JSService} interface.
 *
 * This class offers methods for creating, reading, updating, deleteting and
 * moving JavaScript files.
 *
 * @author Petteri Kivimäki
 */
public class JSServiceImpl implements JSService {

    private final static Logger logger = LoggerFactory.getLogger(JSServiceImpl.class.getName());
    private FileService fileService;
    private DirectoryService dirService;

    /**
     * Sets the file service object.
     *
     * @param fileService new value
     */
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Sets the directory service object.
     *
     * @param dirService new value
     */
    public void setDirService(DirectoryService dirService) {
        this.dirService = dirService;
    }

    /**
     * Returns a list of JS files located in the scripts directory shared by all
     * the users.
     *
     * @return list of JS files in the scripts directory shared by all the users
     */
    @Override
    public List<String> getList() {
        // Get shared scripts directory path
        String path = Settings.getInstance().getScriptsSysPath();
        // Get list of shared js files
        return dirService.getListStr(path, JSUtil.getJsFilter());
    }

    /**
     * Returns a list of JS files located in the scripts directory of the given
     * owner.
     *
     * @return list of JS files in the scripts directory of the given owner
     */
    @Override
    public List<String> getList(Owner owner) {
        // Get scripts directory path
        String path = Settings.getInstance().getScriptsPath(owner.getCode());
        // Get list of js files related to the given owner
        return dirService.getListStr(path, JSUtil.getJsFilter());
    }

    /**
     * Returns a list of JS files located in the plugins directory shared by all
     * the users.
     *
     * @return list of JS files in the plugins directory shared by all the users
     */
    @Override
    public List<String> getPluginsList() {
        // Get plugins directory path
        String path = Settings.getInstance().getScriptsPluginsPath();
        // Get list of shared js files
        return dirService.getListStr(path, JSUtil.getJsFilter());
    }

    /**
     * Creates an empty JS file named after the given filename. Before creating
     * the JS file the given filename is validated and false is returned in case
     * the filename doesn't pass the validation.
     *
     * @param filename name of the JS file
     * @param owner owner of the JS file
     * @return true if and only if the JS file was successfully created;
     * otherwise false
     */
    @Override
    public boolean create(String filename, Owner owner) {
        return this.create(filename, "", owner);
    }

    /**
     * Creates a new JS file with the given contents named after the given
     * filename. Before creating the JS file the given filename is validated and
     * false is returned in case the filename doesn't pass the validation.
     *
     * @param filename name of the JS file
     * @param contents contents of the JS file
     * @param owner owner of the JS file
     * @return true if and only if the JS file was successfully created;
     * otherwise false
     */
    @Override
    public boolean create(String filename, String contents, Owner owner) {
        // Add the file extension if it's missing
        filename = JSUtil.buildJsName(filename);
        // Check that the name is valid
        if (!JSUtil.isJsNameValid(filename)) {
            logger.warn("Creating new JS file failed! Invalid JS file name : {}", filename);
            return false;
        }
        // Get the absolute path of the file
        String path = JSUtil.buildFilePath(filename, owner);

        // Check for null value
        if (contents == null) {
            contents = "";
        }

        // Create new CSS file
        if (fileService.add(path, contents)) {
            logger.debug("New JS file created : {}", path);
            return true;
        }
        logger.warn("Failed to create JS file : {}", path);
        return false;
    }

    /**
     * Reads the JS file matching the given name.
     *
     * @param filename name of the JS file
     * @param owner owner of the JS file
     * @return contents of the JS file
     */
    @Override
    public String read(String filename, Owner owner) {
        // Check that the name is valid
        if (!JSUtil.isJsNameValid(filename)) {
            logger.warn("Reading JS file failed! Invalid JS file name : {}", filename);
            return "";
        }
        // Get the absolute path of the file
        String path = JSUtil.buildFilePath(filename, owner);
        logger.debug("Read JS file : {}", path);
        // Read file contents
        return fileService.read(path);
    }

    /**
     * Update the JS file pointed by the given filename.
     *
     * @param filename name of the JS file
     * @param contents contents of the JS file
     * @param owner owner of the JS file
     * @return true if and only if the JS file exists and it's successfully
     * updated; otherwise false
     */
    @Override
    public boolean update(String filename, String contents, Owner owner) {
        // Check that the name is valid
        if (!JSUtil.isJsNameValid(filename)) {
            logger.warn("Updating JS file failed! Invalid JS file name : {}", filename);
            return false;
        }
        // Get the absolute path of the file
        String filePath = JSUtil.buildFilePath(filename, owner);

        // Write contents to the js file
        if (fileService.update(filePath, contents)) {
            logger.debug("JS file updated : {}", filePath);
            return true;
        }
        logger.debug("Updating JS file failed : {}", filePath);
        return false;
    }

    /**
     * Deletes the JS file pointed by the given filename.
     *
     * @param filename name of the JS file
     * @param owner owner of the JS file
     * @return true if and only if the JS file is successfully deleted; false
     * otherwise
     */
    @Override
    public boolean delete(String filename, Owner owner) {
        // Check that the name is valid
        if (!JSUtil.isJsNameValid(filename)) {
            logger.warn("Deleteing JS file failed! Invalid JS file name : {}", filename);
            return false;
        }
        // Get the absolute path of the file
        String path = JSUtil.buildFilePath(filename, owner);

        // Delete the js file
        if (fileService.delete(path)) {
            logger.debug("JS file deleted : {}", path);
            return true;
        }
        logger.debug("Deleting JS file failed : {}", path);
        return false;
    }

    /**
     * Renames the given JS file.
     *
     * @param oldFilename old JS file name
     * @param newFilename new JS file name
     * @param owner owner of the JS files
     * @return true if and only if the JS file was renamed
     */
    @Override
    public boolean rename(String oldFilename, String newFilename, Owner owner) {
        // Build new template name
        newFilename = JSUtil.buildJsName(newFilename);
        // Check that the new name is valid
        if (!JSUtil.isJsNameValid(newFilename)) {
            logger.warn("Renaming JS file failed! New JS name is invalid : {}", newFilename);
            return false;
        }
        // Check that the old name is valid
        if (!JSUtil.isJsNameValid(oldFilename)) {
            logger.warn("Renaming JS file failed! Old JS name is invalid : {}", oldFilename);
            return false;
        }
        // Get the absolute paths of the JS files
        String oldPath = JSUtil.buildFilePath(oldFilename, owner);
        String newPath = JSUtil.buildFilePath(newFilename, owner);

        // Rename js file
        if (fileService.rename(oldPath, newPath)) {
            logger.debug("JS file renamed.");
            return true;
        }
        logger.warn("Renaming JS file failed.");
        return false;
    }

    /**
     * Tests whether the JS file exists.
     *
     * @param filename name of the JS file
     * @param owner owner of the JS file
     * @return true if and only if the JS file exists; false otherwise
     */
    @Override
    public boolean exists(String filename, Owner owner) {
        // Get list of js files that exist 
        List<String> files = this.getList(owner);
        // Go through the list
        for (String js : files) {
            if (js.equalsIgnoreCase(filename)) {
                // The js file exists, return true
                return true;
            }
        }
        // No match was found, return false
        return false;
    }
}
