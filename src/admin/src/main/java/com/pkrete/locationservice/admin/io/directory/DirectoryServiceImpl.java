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
package com.pkrete.locationservice.admin.io.directory;

import com.pkrete.locationservice.admin.io.DirectoryService;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * DirectoryServiceImpl class implements the 
 * {@link DirectoryService DirectoryService} interface.
 * 
 * This class offers methods for getting listings of directory contents, and
 * for adding, renaming and deleting directories.
 * 
 * @author Petteri Kivimäki
 */
public class DirectoryServiceImpl implements DirectoryService {

    private final static Logger logger = Logger.getLogger(DirectoryServiceImpl.class.getName());

    /**
     * Returns a list of directories inside the directory pointed by the
     * given base path. Hidden directories are excluded.
     * @param basePath absolute path of the base directory
     * @return list of directories
     */
    public List<File> getSubDirectories(String basePath) {
        File dir = new File(basePath);
        List<File> list = new ArrayList<File>();
        if (!dir.exists()) {
            return list;
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory() && !files[i].isHidden()) {
                list.add(files[i]);
            }
        }
        return list;
    }

    /**
     * Returns a list of directory names as strings inside the directory 
     * pointed by the given base path. Hidden directories are excluded.
     * @param basePath absolute path of the base directory
     * @return list of directory names as strings
     */
    public List<String> getSubDirectoriesStr(String basePath) {
        List<String> list = new ArrayList<String>();
        for (File f : this.getSubDirectories(basePath)) {
            list.add(f.getName());
        }
        Collections.sort(list);
        return list;
    }

    /**
     * Returns a list of files inside the directory pointed by the
     * given base path. Hidden files are excluded.
     * @param dir absolute path of the directory
     * @return list of files
     */
    public List<File> getFiles(String dir) {
        File path = new File(dir);
        List<File> list = new ArrayList<File>();
        if (!path.exists()) {
            return list;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile() && !files[i].isHidden()) {
                list.add(files[i]);
            }
        }
        return list;
    }

    /**
     * Returns a list of file names as strings inside the directory 
     * pointed by the given base path. Hidden files are excluded.
     * @param dir absolute path of the directory
     * @return list of file names as strings
     */
    public List<String> getFilesStr(String dir) {
        List<String> list = new ArrayList<String>();
        for (File f : this.getFiles(dir)) {
            list.add(f.getName());
        }
        Collections.sort(list);
        return list;
    }

    /**
     * Returns a list of directory entries matching the given filter inside 
     * the directory pointed by the given base path.
     * @param basePath absolute path of the base directory
     * @return list of directory entries matching the filter
     */
    public List<File> getList(String dir, FileFilter filter) {
        File path = new File(dir);
        List<File> list = new ArrayList<File>();
        if (!path.exists()) {
            return list;
        }
        // Get matching dir entries as an array
        File[] files = path.listFiles(filter);
        // Check for null
        if (files != null) {
            // Add all the entries to the list
            list.addAll(Arrays.asList(files));
        }
        return list;
    }

    /**
     * Returns a list of directory entries matching the given filter inside 
     * the directory pointed by the given base path.
     * @param basePath absolute path of the base directory
     * @return list of directory entries matching the filter
     */
    public List<String> getListStr(String dir, FileFilter filter) {
        List<String> list = new ArrayList<String>();
        for (File f : this.getList(dir, filter)) {
            list.add(f.getName());
        }
        Collections.sort(list);
        return list;
    }

    /**
     * Renames the given directory.
     * @param oldDir abstract path to be renamed
     * @param newDir new abstract pathname for the named directory    
     * @return true if and only if the directory was renamed
     */
    public boolean rename(String oldDir, String newDir) {
        File dirNew = new File(newDir);
        if (dirNew.exists()) {
            logger.warn("Failed to rename the given directory, because the target directory already exists.");
            logger.warn("New path: " + dirNew.getAbsolutePath());
            return false;
        }
        File dirOld = new File(oldDir);
        if (!dirOld.exists()) {
            logger.warn("Failed to rename the given directory, because the directory dooesn't exist.");
            logger.warn("Old path: " + dirOld.getAbsolutePath());
            return false;
        }
        if (dirOld.renameTo(dirNew)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Directory renamed!");
                logger.debug("Old path: " + dirOld.getAbsolutePath());
                logger.debug("New path: " + dirNew.getAbsolutePath());
            }
            return true;
        } else {
            logger.error("Renaming directory failed!");
            logger.error("Old path: " + dirOld.getAbsolutePath());
            logger.error("New path: " + dirNew.getAbsolutePath());
            return false;
        }
    }

    /**
     * Creates the directory named by the given abstract path name.
     * @param dir abstract paht name
     * @return true if and only if the directory was created; false otherwise
     */
    public boolean add(String dir) {
        File dirNew = new File(dir);
        if (dirNew.exists()) {
            logger.warn("Failed to create directory, because the directory already exists!");
            logger.warn("Path: " + dirNew.getAbsolutePath());
            return false;
        }
        if (dirNew.mkdirs()) {
            if (logger.isDebugEnabled()) {
                logger.debug("New directory created! Path: " + dirNew.getAbsolutePath());
            }
            return true;
        } else {
            logger.error("Failed to create directory! Path: " + dirNew.getAbsolutePath());
            return false;
        }
    }

    /**
     * Deletes the directory denoted by this abstract pathname. If mustBeEmpty
     * is set to true, then the directory must be empty in order to be deleted.
     * If must be empty is set to false, then all the files and subdirectories
     * and their contents are deleted before deleting the directory.
     * will be deleted before deleting the directory.
     * @param dir abstract path name
     * @param mustBeEmpty must the directory be empty
     * @return true if and only if the file or directory is successfully 
     * deleted; false otherwise
     */
    public boolean delete(String dir, boolean mustBeEmpty) {
        File dirDel = new File(dir);
        // Check that the directory exists
        if (!dirDel.exists()) {
            logger.warn("Failed to delete the given directory, because the directory doesn't exist.");
            logger.warn("Path: " + dirDel.getAbsolutePath());
            return false;
        }
        // Check that the directory is empty
        if (mustBeEmpty && !this.isEmpty(dir)) {
            logger.warn("Failed to delete the given directory, because the directory is not empty.");
            logger.warn("Path: " + dirDel.getAbsolutePath());
            return false;
        }
        if (dirDel.exists()) {
            // Get list of files in the directory
            File[] delFiles = dirDel.listFiles();
            // Delete all the files  and subdirectorie from the directory
            for (int i = 0; i < delFiles.length; i++) {
                // Is this a subdirectory?
                if (delFiles[i].isDirectory()) {
                    // Try to delete the subdirectory and all its contents
                    if (!delete(delFiles[i].getAbsolutePath(), mustBeEmpty)) {
                        return false;
                    } else {
                        // Deleting the subdirectory and its contents succeeded.
                        // Jump to next file/subdirectory,
                        continue;
                    }
                }
                if (delFiles[i].delete()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("File deleted! Path: " + delFiles[i].getAbsolutePath());
                    }
                } else {
                    logger.error("Failed to delete file! Path: " + delFiles[i].getAbsolutePath());
                }
            }
        }
        if (dirDel.delete()) {
            logger.debug("Directory deleted! Path: " + dirDel.getAbsolutePath());
            return true;
        } else {
            logger.error("Failed to delete the given directory!");
            logger.error("Path: " + dirDel.getAbsolutePath());
            return false;
        }
    }

    /**
     * Tests whether the directory denoted by this abstract pathname exists.
     * @param dir abstract pathname
     * @return true if and only if the directory denoted by this abstract
     * pathname exists; false otherwise
     */
    public boolean exists(String dir) {
        File file = new File(dir);
        return file.exists();
    }

    /**
     * Tests whether the directory denoted by this abstract pathname is empty. 
     * Returns true if and only if the directory doesn't contain any files or 
     * subdirectories.
     * @param dir abstract pathname
     * @return true if and only if the directory is empty; false otherwise
     */
    public boolean isEmpty(String dir) {
        File newDir = new File(dir);
        if (newDir.exists()) {
            if (newDir.list().length > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests whether the directory denoted by this abstract pathname contains
     * any files. Returns true if and only if the directory and its 
     * subdirectories don't contain any files that are not present in 
     * the ignored files list.
     * @param dir abstract pathname
     * @param ignoredFiles list of files that are ignore - directory is 
     * cosidered empty it it contains only files that are present in this list
     * @return true if and only if the directory and its subdirectories don't 
     * contain any files that are not present in the ignored files list;
     * otherwise false
     */
    public boolean isEmpty(String dir, Map<String, Boolean> ignoredFiles) {
        // Make sure that ignoredFiles is not null
        if (ignoredFiles == null) {
            ignoredFiles = new HashMap<String, Boolean>();
        }
        File newDir = new File(dir);
        if (newDir.exists() && newDir.isDirectory()) {
            // Get list of all the files and folders in the directory
            String[] children = newDir.list();
            // Loop through the list
            for (int i = 0; i < children.length; i++) {
                // Create a new file object presenting the directory entry
                File file = new File(newDir, children[i]);
                // If entry is a file and it's not in the ignore list, 
                // return false
                if (file.isFile() && !ignoredFiles.containsKey(file.getName())) {
                    return false;
                } else if (file.isDirectory()) {
                    // If entry is a directory, check that it's empty
                    if (!isEmpty(new File(newDir, children[i]).getAbsolutePath(), ignoredFiles)) {
                        // If directory is not empty, return false
                        return false;
                    }
                }
            }
        }
        // The directory is empty, return true
        return true;
    }
}
