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
package com.pkrete.locationservice.admin.io.file;

import com.pkrete.locationservice.admin.io.FileService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;
import org.apache.log4j.Logger;

/**
 * FileServiceImpl class implements the 
 * {@link FileService FileService} interface.
 * 
 * This class offers methods for creating, reading, updating, deleteting
 * and moving files.
 * 
 * @author Petteri Kivimäki
 */
public class FileServiceImpl implements FileService {

    private final static Logger logger = Logger.getLogger(FileServiceImpl.class.getName());

    /**
     * Creates the file denoted by the abstract pathname and writes
     * the given contents in it. If the file already exists, nothing is 
     * done and false is returned.
     * @param path abstract path name
     * @param contents contents of the file
     * @return true if and only if the file doesn't exist and it's succesfully 
     * created; otherwise false
     */
    public boolean add(String path, String contents) {
        File file = new File(path);
        if (file.exists()) {
            logger.warn("Creating file failed! File already exists : " + file.getAbsolutePath());
            return false;
        }
        return write(path, contents);
    }

    /**
     * Reads the contents of the file denoted by the abstract path name. If
     * the file doesn't exist, an empty string is returned.
     * @param path abstract path name
     * @return contents of the file
     */
    public String read(String path) {
        File file = new File(path);
        if (!file.exists()) {
            logger.warn("Reading file failed! File doesn't exist : " + file.getAbsolutePath());
            return "";
        }
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(file.getAbsolutePath()), "UTF-8");
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine());
                text.append(NL);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            scanner.close();
        }
        return text.toString().trim();
    }

    /**
     * Updates the contents of file denoted by the abstract pathname.
     * If the file doesn't exist, nothing is done and false is returned.
     * @param path abstract path name
     * @param contents contents of the file
     * @return true if and only if the file exists and it's succesfully 
     * updated; otherwise false
     */
    public boolean update(String path, String contents) {
        File file = new File(path);
        if (!file.exists()) {
            logger.warn("Updating file failed! File doesn't exist :  " + file.getAbsolutePath());
            return false;
        }
        return write(path, contents);
    }

    /**
     * Writes the given contents to the file denoted by the abstract pathname.
     * If the file doesn't exist it's created, existing file will
     * be overwritten.
     * @param path abstract path name
     * @param contents contents of the file
     * @return true if and only if the file is successfully written;
     * otherwise false
     */
    public boolean write(String path, String contents) {
        if(contents == null) {
            logger.warn("Writing file failed! Contents can't be null!");
            return false;
        }
        File file = new File(path);
        boolean fileExists = file.exists();

        Writer out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), "UTF-8");
            out.write(contents.trim());
            out.close();
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        if (logger.isDebugEnabled()) {
            if (!fileExists) {
                logger.debug("File created : " + file.getAbsolutePath());
            } else {
                logger.debug("File updated : " + file.getAbsolutePath());
            }
        }
        return true;
    }

    /**
     * Deletes the file denoted by this abstract pathname.
     * @param path abstract path name
     * @return true if and only if the file is successfully 
     * deleted; false otherwise
     */
    public boolean delete(String path) {
        File file = new File(path);
        if (file.delete()) {
            if (logger.isDebugEnabled()) {
                logger.debug("File deleted! Path : " + file.getAbsolutePath());
            }
            return true;
        } else {
            logger.error("Deleting file failed! Path : " + file.getAbsolutePath());
            return false;
        }
    }

    /**
     * Renames the given file.
     * @param oldPath abstract path to be renamed
     * @param newPath new abstract pathname for the named file    
     * @return true if and only if the file was renamed
     */
    public boolean rename(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        if (!oldFile.exists()) {
            logger.warn("Failed to rename the given file, because the file doesn't exist.");
            logger.warn("Old path: " + oldFile.getAbsolutePath());
            return false;
        }
        File newFile = new File(newPath);
        if (newFile.exists()) {
            logger.warn("Failed to rename the given file, because the target file already exists.");
            logger.warn("New path: " + newFile.getAbsolutePath());
            return false;
        }
        if (oldFile.renameTo(newFile)) {
            if (logger.isDebugEnabled()) {
                logger.debug("File renamed!");
                logger.debug("Old path: " + oldFile.getAbsolutePath());
                logger.debug("New path: " + newFile.getAbsolutePath());
            }
            return true;
        } else {
            logger.error("Renaming file failed!");
            logger.error("Old path: " + oldFile.getAbsolutePath());
            logger.error("New path: " + newFile.getAbsolutePath());
            return false;
        }
    }

    /**
     * Moves the given file from oldPath to newPath. If newPath already exists
     * it'll be overwritten by oldPath.
     * @param oldPath abstract path to be renamed
     * @param newPath new abstract pathname for the named file    
     * @return true if and only if the file was renamed
     */
    public boolean replace(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        if (!oldFile.exists()) {
            logger.warn("Failed to rename the given file, because the file doesn't exist.");
            logger.warn("Old path: " + oldFile.getAbsolutePath());
            return false;
        }
        File newFile = new File(newPath);
        File delFile = new File(newPath + ".delete");
        if (newFile.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Target file exists -> create temp file before deleting");
            }
            if (!newFile.renameTo(delFile)) {
                logger.warn("Creating temp file failed!");
                return false;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Temp file created! Path: " + delFile.getAbsolutePath());
            }
        }
        if (oldFile.renameTo(newFile)) {
            if (logger.isDebugEnabled()) {
                logger.debug("File renamed!");
                logger.debug("Old path: " + oldFile.getAbsolutePath());
                logger.debug("New path: " + newFile.getAbsolutePath());
            }
            if (delFile.exists()) {
                if (delFile.delete()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Temp file deleted succesfully!");
                    }
                } else {
                    logger.warn("Deleting temp file failed!");
                }
            }
            return true;
        } else {
            logger.error("Renaming file failed!");
            logger.error("Old path: " + oldFile.getAbsolutePath());
            logger.error("New path: " + newFile.getAbsolutePath());
            if (delFile.renameTo(newFile)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Temp file recovered succesfully!");
                }
            } else {
                logger.warn("Recovering temp file failed!");
            }
            return false;
        }
    }

    /**
     * Tests whether the file denoted by this abstract pathname exists.
     * @param path abstract pathname
     * @return true if and only if the file denoted by this abstract
     * pathname exists; false otherwise
     */
    public boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }
}
