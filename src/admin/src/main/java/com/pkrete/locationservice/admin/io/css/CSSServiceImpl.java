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
package com.pkrete.locationservice.admin.io.css;

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.io.CSSService;
import com.pkrete.locationservice.admin.io.DirectoryService;
import com.pkrete.locationservice.admin.io.FileService;
import com.pkrete.locationservice.admin.util.CSSUtil;
import com.pkrete.locationservice.admin.util.Settings;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * CSSServiceImpl class implements the 
 * {@link CSSService CSSService} interface.
 * 
 * This class offers methods for creating, reading, updating, deleteting
 * and moving CSS files.
 * 
 * @author Petteri Kivimäki
 */
public class CSSServiceImpl implements CSSService {

    private final static Logger logger = Logger.getLogger(CSSServiceImpl.class.getName());
    private FileService fileService;
    private DirectoryService dirService;

    /**
     * Sets the file service object.
     * @param fileService new value
     */
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Sets the directory service object.
     * @param dirService new value
     */
    public void setDirService(DirectoryService dirService) {
        this.dirService = dirService;
    }

    /**
     * Returns a list of css files located in the stylesheets directory
     * of the given owner.
     * @return list of css files in the stylesheets directory of the given
     * owner
     */
    public List<String> getList(Owner owner) {
        // Get CSS directory path
        String path = Settings.getInstance().getStylesheetsPath(owner.getCode());
        // Get list of CSS files related to the given owner
        return dirService.getListStr(path, CSSUtil.getCssFilter());
    }

    /**
     * Creates an empty css file named after the given filename. 
     * Before creating the css file the given filename is
     * validated and false is returned in case the filename doesn't pass
     * the validation. 
     * @param filename name of the css file
     * @param owner owner of the css file
     * @return true if and only if the css file was succesfully created;
     * otherwise false
     */
    public boolean create(String filename, Owner owner) {
        return this.create(filename, "", owner);
    }

    /**
     * Creates a new css file named after the given filename with the given
     * contents. Before creating the css file the given filename is
     * validated and false is returned in case the filename doesn't pass
     * the validation. 
     * @param filename name of the css file
     * @param owner owner of the css file
     * @param contents contents of the css file
     * @return true if and only if the css file was succesfully created;
     * otherwise false
     */
    public boolean create(String filename, String contents, Owner owner) {
        // Add the file extension if it's missing
        filename = CSSUtil.buildCssName(filename);
        // Check that the name is valid
        if (!CSSUtil.isCssNameValid(filename)) {
            logger.warn("Creating new CSS file failed! Invalid CSS file name : " + filename);
            return false;
        }
        // Get the absolute path of the file
        String path = CSSUtil.buildFilePath(filename, owner);

        // Check for null value
        if (contents == null) {
            contents = "";
        }

        // Create new CSS file
        if (fileService.add(path, contents)) {
            if (logger.isDebugEnabled()) {
                logger.debug("New CSS file created : " + path);
            }
            return true;
        }
        logger.warn("Failed to create CSS file : " + path);
        return false;
    }

    /**
     * Reads the system css related to the given Owner.
     * @param owner owner of the css
     * @return contents of the system css
     */
    public String read(Owner owner) {
        // Get the absolute path of the css
        String filePath = CSSUtil.buildFilePath("../style.css", owner);
        if (logger.isDebugEnabled()) {
            logger.debug("Read system CSS : " + filePath);
        }
        // Read file contents
        return fileService.read(filePath);
    }

    /**
     * Reads the css file matching the given name.
     * @param filename name of the css
     * @param owner owner of the css
     * @return contents of the css
     */
    public String read(String filename, Owner owner) {
        // Check that the name is valid
        if (!CSSUtil.isCssNameValid(filename)) {
            logger.warn("Reading CSS file failed! Invalid CSS file name : " + filename);
            return "";
        }
        // Get the absolute path of the file
        String path = CSSUtil.buildFilePath(filename, owner);
        if (logger.isDebugEnabled()) {
            logger.debug("Read CSS file : " + path);
        }
        // Read file contents
        return fileService.read(path);
    }

    /**
     * Writes the given contents to the system css of the given Owner. If
     * the system css file doesn't exist yet, it's created, otherwise it
     * will be overwritten.
     * @param contents contents of the css
     * @param owner owner of the css
     * @return true if and only if the css is successfully written;
     * otherwise false
     */
    public boolean update(String contents, Owner owner) {
        // Get the absolute path of the file
        String filePath = CSSUtil.buildFilePath("../style.css", owner);
        // Write contents to the system css
        if (fileService.write(filePath, contents)) {
            if (logger.isDebugEnabled()) {
                logger.debug("System CSS file updated : " + filePath);
            }
            return true;
        }
        logger.debug("Updating system CSS file failed : " + filePath);
        return false;
    }

    /**
     * Update the css pointed by the given filename.
     * @param filename name of the css
     * @param contents contents of the css
     * @param owner owner of the css
     * @return true if and only if the css exists and it's succesfully 
     * updated; otherwise false
     */
    public boolean update(String filename, String contents, Owner owner) {
        // Check that the name is valid
        if (!CSSUtil.isCssNameValid(filename)) {
            logger.warn("Updating CSS file failed! Invalid CSS file name : " + filename);
            return false;
        }
        // Get the absolute path of the file
        String filePath = CSSUtil.buildFilePath(filename, owner);

        // Write contents to the css
        if (fileService.update(filePath, contents)) {
            if (logger.isDebugEnabled()) {
                logger.debug("CSS file updated : " + filePath);
            }
            return true;
        }
        logger.debug("Updating CSS file failed : " + filePath);
        return false;
    }

    /**
     * Deletes the css pointed by the given filename.
     * @param filename name of the css
     * @param owner owner of the css
     * @return true if and only if the css is successfully 
     * deleted; false otherwise
     */
    public boolean delete(String filename, Owner owner) {
        // Check that the name is valid
        if (!CSSUtil.isCssNameValid(filename)) {
            logger.warn("Deleteing CSS file failed! Invalid CSS file name : " + filename);
            return false;
        }
        // Get the absolute path of the file
        String path = CSSUtil.buildFilePath(filename, owner);

        // Delete the css
        if (fileService.delete(path)) {
            if (logger.isDebugEnabled()) {
                logger.debug("CSS file deleted : " + path);
            }
            return true;
        }
        logger.debug("Deleting CSS file failed : " + path);
        return false;
    }

    /**
     * Renames the given css.
     * @param oldFilename old css name
     * @param newFilename new css name
     * @param owner owner of the css
     * @return true if and only if the css was renamed
     */
    public boolean rename(String oldFilename, String newFilename, Owner owner) {
        // Build new template name
        newFilename = CSSUtil.buildCssName(newFilename);
        // Check that the new name is valid
        if (!CSSUtil.isCssNameValid(newFilename)) {
            logger.warn("Renaming CSS file failed! New CSS name is invalid : " + newFilename);
            return false;
        }
        // Check that the old name is valid
        if (!CSSUtil.isCssNameValid(oldFilename)) {
            logger.warn("Renaming CSS file failed! Old CSS name is invalid : " + oldFilename);
            return false;
        }
        // Get the absolute paths of the CSS files
        String oldPath = CSSUtil.buildFilePath(oldFilename, owner);
        String newPath = CSSUtil.buildFilePath(newFilename, owner);

        // Rename css
        if (fileService.rename(oldPath, newPath)) {
            if (logger.isDebugEnabled()) {
                logger.debug("CSS file renamed.");
            }
            return true;
        }
        logger.warn("Renaming CSS file failed.");
        return false;
    }

    /**
     * Tests whether the css exists.
     * @param filename name of the css
     * @param owner owner of the css
     * @return true if and only if the css exists; false otherwise
     */
    public boolean exists(String filename, Owner owner) {
        // Get list of css files that exist 
        List<String> files = this.getList(owner);
        // Go through the list
        for (String css : files) {
            if (css.equalsIgnoreCase(filename)) {
                // The css file exists, return true
                return true;
            }
        }
        // No match was found, return false
        return false;
    }
}
