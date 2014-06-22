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
package com.pkrete.locationservice.admin.service.illustrations;

import com.pkrete.locationservice.admin.service.MapsService;
import com.pkrete.locationservice.admin.dao.MapsDao;
import com.pkrete.locationservice.admin.model.illustration.Map;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.io.DirectoryService;
import com.pkrete.locationservice.admin.io.FileService;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import com.pkrete.locationservice.admin.service.LanguagesService;
import com.pkrete.locationservice.admin.util.DateTimeUtil;
import com.pkrete.locationservice.admin.util.IllustrationsUtil;
import com.pkrete.locationservice.admin.util.Settings;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class implements {@link MapsService MapsService} interface, 
 * which defines service layer for Map objects.
 * 
 * @author Petteri Kivimäki
 */
public class MapsServiceImpl implements MapsService {

    private final static Logger logger = Logger.getLogger(MapsServiceImpl.class.getName());
    private MapsDao dao;
    private FileService fileService;
    private DirectoryService dirService;
    private LanguagesService languagesService;
    private JSONizerService jsonizer;

    /**
     * Sets the data access object.
     * @param dao new value
     */
    public void setDao(MapsDao dao) {
        this.dao = dao;
    }

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
     * Sets the language service object.
     * @param languageService new value
     */
    public void setLanguagesService(LanguagesService languagesService) {
        this.languagesService = languagesService;
    }

    /**
     * Sets the JSON converter object.
     * @param dao new value
     */
    public void setJsonizer(JSONizerService jsonizer) {
        this.jsonizer = jsonizer;
    }

    /**
     * Returns the map with given id.
     * This method is only for editor classes. All the other classes must give
     * also the owner parameter.
     * @param id the id that is used for searching
     * @return the map with the given id
     */
    public Map get(int id) {
        return dao.get(id);
    }

    /**
     * Returns the map with given id.
     * @param id the id that is used for searching
     * @param owner the owner of the object
     * @return the map with the given id
     */
    public Map get(int id, Owner owner) {
        return dao.get(id, owner);
    }

    /**
     * Deletes the map with the given id and owner.
     * @param id id of the map
     * @param owner owner of the map
     * @return true if and only if the map was succesfully deleted;
     * otherwise false
     */
    public boolean delete(int id, Owner owner) {
        Map map = dao.get(id, owner);
        return delete(map);
    }

    /**
     * Deletes the given map object from the database.
     * @param map the map to be deleted
     */
    public boolean delete(Map map) {
        // Check for null
        if (map == null) {
            logger.warn("Deleting map failed! Map can not be null.");
            return false;
        }
        // JSON presentation of the Language object
        String json = this.jsonizer.jsonize(map, true);

        // Make sure that the map can be deleted
        if (!dao.canBeDeleted(map.getId())) {
            logger.warn(new StringBuilder("Deleting map failed! Map has dependencies in the database. Map : ").append(json));
            return false;
        }
        boolean success = true;
        // If the map is not external, map files need to be deleted
        if (!map.getIsExternal()) {
            // Map file must be deleted from each language directory
            success = this.deleteFiles(map);
        }
        // Delete the map from DB, if success == true
        if (success) {
            if (dao.delete(map)) {
                if (logger.isInfoEnabled()) {
                    logger.info(new StringBuilder("Map deleted : ").append(json));
                }
                return true;
            }
        }
        logger.warn(new StringBuilder("Failed to delete map : ").append(json));
        return false;
    }

    /**
     * Returns a list of all the maps in the database that are related
     * to the given owner object.
     * @param owner the owner of the objects
     * @return all the maps in the database
     */
    public List<Map> get(Owner owner) {
        return dao.get(owner);
    }

    /**
     * Adds the given map object to the database and to the filesystem.
     * @param map the map to be added
     * @return true if and only if the map was succesfully added; otherwise
     * false
     */
    public boolean create(Map map) {
        // Set created date
        map.setCreated(new Date());
        // Get path of the maps dir
        final String path = Settings.getInstance().getMapsPath(map.getOwner().getCode());
        // Get path of the maps admin dir
        final String adminPath = Settings.getInstance().getMapsPathAdmin(map.getOwner().getCode());

        if (map.getFilePath() != null && map.getFilePath().length() > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("Move uploaded maps files from admin dir to service dir.");
            }
            // Map files are moved from admin dir to service dir.
            // FilePath variable contains the name of the map files
            String name = map.getFilePath();
            // Check that the file really exists in all the languages
            if (!this.adminMapExists(map.getFilePath(), map.getOwner())) {
                logger.warn(new StringBuilder("Creating map failed, because the given file doesn't exist in all the language directories! File : ").append(map.getFilePath()));
                return false;
            }
            // Get all the languages related to the Map owner
            List<Language> languages = this.languagesService.getLanguages(map.getOwner());
            // Name must be unique
            name = this.getUniqueName(path, name, languages);
            // Path variable must be updated
            map.setPath(name);
            // The map is not external
            map.setIsExternal(false);
            // Map for files that were succesfully renamed
            java.util.Map<String, String> renamedFiles = new HashMap<String, String>();
            boolean success = true;
            // Move all the language versions
            for (Language lang : languages) {
                // Absolute source path
                String sourcePath = adminPath + lang.getCode() + "/" + map.getFilePath();
                // Absolute target path
                String targetPath = path + lang.getCode() + "/" + map.getPath();
                if (logger.isInfoEnabled()) {
                    logger.info(new StringBuilder("Move map file : \"").append(sourcePath).append("\" -> \"").append(targetPath).append("\""));
                }
                // Try to rename (=move) the file
                if (!fileService.rename(sourcePath, targetPath)) {
                    logger.warn("Moving map file failed!");
                    success = false;
                    break;
                }
                renamedFiles.put(targetPath, sourcePath);
            }
            // Rollback if moving the files failed
            if (!success) {
                logger.warn("Moving map files failed -> ROLLBACK.");
                logger.warn("Undo all the changes.");

                // Do rollback if all the fiels were not renamed.
                // Go through all the files and try to rename them with the
                // original name.
                boolean renamed = true;
                for (String key : renamedFiles.keySet()) {
                    if (!this.fileService.rename(key, renamedFiles.get(key))) {
                        renamed = false;
                    }
                }
                if (!renamed) {
                    logger.warn("Failed to restore all the previously moved map files.");
                } else {
                    logger.info("Succesfully restored all the previously moved map files.");
                }
                logger.warn("Creating new map failed.");
                return false;
            }
        } else if (map.getUrl().length() > 0) {
            // Set path
            map.setPath(map.getUrl());
            // Map is external
            map.setIsExternal(true);
        } else if (map.hasFiles()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Add uploaded map files.");
            }
            // User has uploaded files, one file for each language
            java.util.Map<Integer, MultipartFile> files = map.getFiles();
            // Get the name of the first file
            String name = files.entrySet().iterator().next().getValue().getOriginalFilename();
            // Get all the languages related to the Map owner
            List<Language> languages = this.languagesService.getLanguages(map.getOwner());
            // Get list of language id - language pairs
            java.util.Map<Integer, Language> languagesMap = Language.toMap(languages);
            // Name must be unique
            name = this.getUniqueName(path, name, languages);
            // Update path variable
            map.setPath(name);
            // Map is not external
            map.setIsExternal(false);
            // List for files that were succesfully written to disk
            List<String> writtenFiles = new ArrayList<String>();
            boolean success = true;
            // Write uploaded files to disk
            for (Entry<Integer, MultipartFile> entry : files.entrySet()) {
                // New file object for the target file
                File mapFile = new File(path + languagesMap.get(entry.getKey()).getCode() + "/" + name);
                if (logger.isInfoEnabled()) {
                    logger.info(new StringBuilder("Write uploaded map file to disk. Path : \"").append(mapFile.getAbsolutePath()).append("\""));
                }
                try {
                    // Write the uploaded file to disk
                    entry.getValue().transferTo(mapFile);
                } catch (IOException ex) {
                    logger.warn("Writing map file to disk failed!");
                    logger.error(ex);
                    success = false;
                    break;
                }
                // Check that the file really exists
                if (!fileService.exists(mapFile.getAbsolutePath())) {
                    logger.warn(new StringBuilder("Writing map file to disk failed! File doesn't exist. Path : \"").append(mapFile.getAbsolutePath()).append("\""));
                    success = false;
                    break;
                }
                writtenFiles.add(mapFile.getAbsolutePath());
            }
            // Rollback if writing the files failed
            if (!success) {
                logger.warn("Writing map files failed -> ROLLBACK.");
                logger.warn("Undo all the changes.");

                // Do rollback if all the files were not written.
                // Go through all the files and try to delete them
                boolean deleted = true;
                for (String file : writtenFiles) {
                    if (!this.fileService.delete(file)) {
                        deleted = false;
                    }
                }
                if (!deleted) {
                    logger.warn("Failed to remove all the previously written map files.");
                } else {
                    logger.info("Succesfully removed all the previously written map files.");
                }
                logger.warn("Creating new map failed.");
                return false;
            }
        }
        // Try to save the map to DB
        if (dao.create(map)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Map created : ").append(this.jsonizer.jsonize(map, true)));
            }
            return true;
        }
        logger.warn(new StringBuilder("Failed to create map : ").append(this.jsonizer.jsonize(map, true)));
        return false;
    }

    /**
     * Updates the given map object to the database.
     * @param map the map to be updated
     * @return true if and only if the map was succesfully added; otherwise
     * false
     */
    public boolean update(Map map) {
        // Get JSON presentation
        String json = this.jsonizer.jsonize(map, true);
        // Set updated date
        map.setUpdated(new Date());
        // Get path of the maps dir
        final String path = Settings.getInstance().getMapsPath(map.getOwner().getCode());
        // Get path of the maps admin dir
        final String adminPath = Settings.getInstance().getMapsPathAdmin(map.getOwner().getCode());

        if (map.getFilePath() != null && map.getFilePath().length() > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("Move uploaded maps files from admin dir to service dir.");
            }
            // Get name of the map file
            String name = map.getPath();
            // Get all the languages related to the Map owner
            List<Language> languages = this.languagesService.getLanguages(map.getOwner());
            // Check that the file really exists in one language at least
            if (!this.adminMapExists(map.getFilePath(), languages, map.getOwner())) {
                logger.warn(new StringBuilder("Updating map failed, because the given file doesn't exist in any language! File : ").append(map.getFilePath()));
                return false;
            }
            // If the current map is external, name must be checked
            if (map.getIsExternal()) {
                // Old map was external, name of the map file must set
                name = map.getFilePath();
                // Name must be unique
                name = this.getUniqueName(path, name, languages);
                // Update path
                map.setPath(name);
            }
            // The map is not external
            map.setIsExternal(false);
            // Move all the language versions that exist
            for (Language lang : languages) {
                // Absolute source path
                String sourcePath = adminPath + lang.getCode() + "/" + map.getFilePath();
                // If the source file doesn't exist, jump to next
                if (!this.fileService.exists(sourcePath)) {
                    continue;
                }
                // Absolute target path
                String targetPath = path + lang.getCode() + "/" + map.getPath();
                if (!this.fileService.replace(sourcePath, targetPath)) {
                    logger.warn("Moving map file failed!");
                    return false;
                }
            }
        } else if (map.getUrl() != null && map.getUrl().length() > 0) {
            // If map is not external, the files must me deleted
            if (!map.getIsExternal()) {
                if (!this.deleteFiles(map)) {
                    logger.warn("Updating map failed! Unable to delete all the related files!");
                    return false;
                }
            }
            // Update path
            map.setPath(map.getUrl());
            // The map is external
            map.setIsExternal(true);
        } else if (map.hasFile()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Update map file(s).");
            }
            boolean success = true;
            // Get uploaded files
            java.util.Map<Integer, MultipartFile> files = map.getFiles();
            // Get all the languages related to the Map owner
            List<Language> languages = this.languagesService.getLanguages(map.getOwner());
            // Get list of language id - language pairs
            java.util.Map<Integer, Language> languagesMap = Language.toMap(languages);
            // Loop through the files
            for (Entry<Integer, MultipartFile> entry : files.entrySet()) {
                // If file is empty, jump to next
                if (entry.getValue() == null || entry.getValue().getSize() == 0) {
                    continue;
                }
                // Create File object for the target file
                File mapFile = new File(path + languagesMap.get(entry.getKey()).getCode() + "/" + map.getPath());
                // Create File object for the temp file
                File tempFile = new File(path + languagesMap.get(entry.getKey()).getCode() + "/temp." + map.getPath());
                // Try to rename the old file
                if (this.fileService.exists(mapFile.getAbsolutePath()) && !fileService.rename(mapFile.getAbsolutePath(), tempFile.getAbsolutePath())) {
                    logger.warn(new StringBuilder("Renaming the old map file failed! Path : ").append(mapFile.getAbsolutePath()));
                    continue;
                }
                if (logger.isInfoEnabled()) {
                    logger.info(new StringBuilder("Write uploaded map file to disk. Path : \"").append(mapFile.getAbsolutePath()).append("\""));
                }
                try {
                    // Write the uploaded file to disk
                    entry.getValue().transferTo(mapFile);
                } catch (IOException ex) {
                    logger.warn("Writing map file to disk failed!");
                    logger.error(ex);
                    success = false;
                }
                // Check that the file really exists
                if (!fileService.exists(mapFile.getAbsolutePath())) {
                    logger.warn(new StringBuilder("Writing map file to disk failed! File doesn't exist. Path : \"").append(mapFile.getAbsolutePath()).append("\""));
                    // If temp file exists, try to recover it
                    if (this.fileService.exists(tempFile.getAbsolutePath()) && !fileService.rename(tempFile.getAbsolutePath(), mapFile.getAbsolutePath())) {
                        logger.warn(new StringBuilder("Recovering the old map file failed! Path : ").append(mapFile.getAbsolutePath()));
                    } else if (logger.isDebugEnabled()) {
                        logger.debug("Old map file succesfully recovered from the temp file.");
                    }
                    success = false;
                } else {
                    // If temp file exists, delete it
                    if (fileService.exists(tempFile.getAbsolutePath()) && !fileService.delete(tempFile.getAbsolutePath())) {
                        logger.warn(new StringBuilder("Updating map failed! Unable to delete the temp file. Path : \"").append(tempFile.getAbsolutePath()).append("\""));
                    } else if (logger.isDebugEnabled()) {
                        logger.debug("Temp file succesfully deleted.");
                    }
                }
            }
            if (!success) {
                logger.warn("Only part of the map files was updated.");
            }
        }
        // Try to update the DB
        if (dao.update(map)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Map updated : ").append(this.jsonizer.jsonize(map, true)));
            }
            return true;
        }
        logger.warn(new StringBuilder("Failed to update Map : ").append(json));
        return false;
    }

    /**
     * Returns a list of map files that exist in all the language
     * admin directories related to the given owner. If file is missing
     * from one or more language directory, it's not included in the results.
     * @param owner owner of the map files
     * @return list of files that are present in all the language admin
     * directories
     */
    public List<String> getAdminMaps(Owner owner) {
        // List for the results
        List<String> list = new ArrayList<String>();
        // Get languages related to the given owner
        List<Language> languages = languagesService.getLanguages(owner);
        // Get path of the maps admin dir
        String path = Settings.getInstance().getMapsPathAdmin(owner.getCode());
        // Create File object that represents the first language directory
        File source = new File(path + languages.get(0).getCode() + "/");
        // Get all the files in the directory
        String[] files = source.list();
        // Go through all the files
        for (int i = 0; i < files.length; i++) {
            boolean hit = true;
            // Check that the current file is present in all the language
            // directories
            for (Language lang : languages) {
                if (!fileService.exists(path + lang.getCode() + "/" + files[i])) {
                    // Skip other languages, if file doesn't exist.
                    hit = false;
                    break;
                }
            }
            // If file exists in all the language directories,
            // add it to the results
            if (hit) {
                list.add(files[i]);
            }
        }
        // Return results
        return list;
    }

    /**
     * Checks if the map object corresponding the give id can be removed
     * from the database.
     * @param mapId the id number of the map to be removed
     * @return true if the map object can be removed, otherwise returns false
     */
    public boolean canBeDeleted(int mapId) {
        return dao.canBeDeleted(mapId);
    }

    /**
     * Checks if a file denoted by the given dir and filename exists, and
     * adds a timestamp in the beginning of the filename if another file
     * with the same name already exists.
     * @param dir absolute directory
     * @param filename name of the file
     * @return unique filename
     */
    private String getUniqueName(String dir, String filename) {
        String path = dir + filename;
        while (fileService.exists(path)) {
            filename = DateTimeUtil.getTimestamp() + "." + filename;
            path = dir + filename;
        }
        return filename;
    }

    /**
     * Checks if a file denoted by the given dir and filename exists in the
     * language directories, and adds a timestamp in the beginning of the 
     * filename if another file with the same name already exists. The 
     * @param dir absolute directory
     * @param filename name of the file
     * @param languages list of languages to be checked
     * @return unique filename
     */
    private String getUniqueName(String dir, String filename, List<Language> languages) {
        boolean unique = true;
        for (Language lang : languages) {
            String path = dir + lang.getCode() + "/" + filename;
            if (fileService.exists(path)) {
                unique = false;
                break;
            }
        }
        if (unique) {
            return filename;
        }
        do {
            unique = true;
            filename = DateTimeUtil.getTimestamp() + "." + filename;
            for (Language lang : languages) {
                String path = dir + lang.getCode() + "/" + filename;
                if (fileService.exists(path)) {
                    unique = false;
                    break;
                }
            }
        } while (!unique);
        return filename;
    }

    /**
     * Returns a Map containing all the uploaded map files and a list of
     * languages in which they're available.
     * @param owner owner of the map files
     * @return Map containing all the uploaded map files and a list of
     * languages in which they're available
     */
    public java.util.Map<String, List<Language>> getUploadedMaps(Owner owner) {
        // Map for the results
        java.util.Map maps = new LinkedHashMap();
        // Get languages related to the given owner
        List<Language> languages = languagesService.getLanguages(owner);
        // Get path of the maps admin dir
        String path = Settings.getInstance().getMapsPathAdmin(owner.getCode());
        // Loop through all the languages
        for (Language lang : languages) {
            // Get list of file names in the current language directory
            List<String> files = this.dirService.getFilesStr(path + lang.getCode() + "/");
            // Go through all the files
            for (String file : files) {
                // If the file exists, add the language
                if (maps.containsKey(file)) {
                    ((List) maps.get(file)).add(lang);
                } else {
                    // If the file doesn't exist yet,
                    // Create a list for the languages
                    List temp = new ArrayList();
                    // Add the current language to the list
                    temp.add(lang);
                    // Add the file and the language list to the results
                    maps.put(file, temp);
                }
            }
        }
        // Return results
        return maps;
    }

    /**
     * Deletes the uploaded map file with given name, language and owner.
     * @param fileName name of the map file
     * @param language language of the map file
     * @param owner owner of the map file
     * @return true if and only if the file was deleted; otherwise false
     */
    public boolean delete(String fileName, Language language, Owner owner) {
        // Check for null values
        if (fileName == null || owner == null) {
            logger.error("Deleting uploaded image file failed! File name or owner can not be null.");
            return false;
        }
        // Get path of the maps admin dir
        String path = Settings.getInstance().getMapsPathAdmin(owner.getCode());
        // Append language code
        path += language.getCode() + "/";
        // Absolute target path
        path += fileName;
        // The file doesn't exist -> exit
        if (!this.adminMapExists(fileName, language, owner)) {
            logger.warn(new StringBuilder("Unable to delete the uploaded map file \"").append(path).append("\", because the file doesn't exist."));
            return false;
        }
        // Try to delete the file
        if (!this.fileService.delete(path)) {
            logger.warn(new StringBuilder("Failed to delete the uploaded map file \"").append(path).append("\"."));
            return false;
        }
        if (logger.isInfoEnabled()) {
            logger.info(new StringBuilder("Uploaded map file \"").append(path).append("\" was succesfully deleted."));
        }
        return true;
    }

    /**
     * Returns a list of maps files that are located in the maps admin
     * directory under the given language related to the given owner.
     * @param language language of the map
     * @param owner owner of the map files
     * @return list of map files
     */
    public List<String> getAdminMaps(Language language, Owner owner) {
        // Get path of the maps admin dir
        String path = Settings.getInstance().getMapsPathAdmin(owner.getCode());
        // Return results
        return dirService.getFilesStr(path + language.getCode());
    }

    /**
     * Checks that a map file with the given name exists in all the maps
     * admin directories. Returns true if and only if the given file is 
     * present in all the admin language directories. Otherwise returns false.
     * @param fileName name of the file
     * @param owner owner of the file
     * @return true if and only if the given file is present in all the
     * admin language directories; otherwise false
     */
    public boolean adminMapExists(String fileName, Owner owner) {
        // Get list of files that exist in all the admin language dirs
        List<String> files = this.getAdminMaps(owner);
        // Go through the list
        for (String file : files) {
            if (file.equals(fileName)) {
                // The file exists, return true
                return true;
            }
        }
        // No match was found, return false
        return false;
    }

    /**
     * Checks that a map file with the given name exists in maps
     * admin directory in the given language related to the given owner.
     * @param fileName name of the map file
     * @param language language of the map file
     * @param owner owner of the file
     * @return true if and only if the map exists; otherwise false
     */
    public boolean adminMapExists(String fileName, Language language, Owner owner) {
        // Get list of uploaded maps related to the given owner
        List<String> maps = this.getAdminMaps(language, owner);
        // Go through the list and check that the given file exists
        for (String map : maps) {
            if (map.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks that a map file with the given name exists in maps
     * admin directory at least in one of the languages related to the 
     * given owner.
     * @param fileName name of the map file
     * @param languages languages related to the given owner
     * @param owner owner of the file
     * @return true if and only if the map exists; otherwise false
     */
    public boolean adminMapExists(String fileName, List<Language> languages, Owner owner) {
        if (languages == null) {
            languages = this.languagesService.getLanguages(owner);
        }
        for (Language lang : languages) {
            if (this.adminMapExists(fileName, lang, owner)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Saves the given file in the maps directory of the Admin app under
     * the given language.
     * @param file file to be saved
     * @param language language of the file
     * @param owner owner of the file
     * @return name of the file if and only if the file was saved; otherwise
     * null
     */
    public String upload(MultipartFile file, Language language, Owner owner) {
        // Check for null values
        if (file == null || owner == null || language == null) {
            logger.warn("Writing map file to disk failed! File or owner can not be null.");
            return null;
        }

        // Get path of the maps admin dir
        String path = Settings.getInstance().getMapsPathAdmin(owner.getCode());
        // Add language code to the path
        path += language.getCode() + "/";
        // Get the name of the file
        String name = file.getOriginalFilename();
        // Name must be unique inside the target directory, NOT between
        // all the language directories
        name = this.getUniqueName(path, name);
        // Absolute target path
        path += name;

        // Create a new File object for the uploaded file
        File mapFile = new File(path);
        if (logger.isInfoEnabled()) {
            logger.info(new StringBuilder("Write uploaded map file to disk. Path : \"").append(path).append("\""));
        }
        try {
            // Write the uploaded file to disk
            file.transferTo(mapFile);
        } catch (IOException ex) {
            logger.warn("Writing map file to disk failed!");
            logger.error(ex);
            return null;
        }
        // Check that the file really exists
        if (!fileService.exists(path)) {
            logger.warn(new StringBuilder("Writing map file to disk failed! File doesn't exist. Path : \"").append(path).append("\""));
            return null;
        }
        if (logger.isInfoEnabled()) {
            logger.info(new StringBuilder("Writing uploaded map file to disk done. Path : \"").append(path).append("\""));
        }
        return name;
    }

    /**
     * Removes all the files related to the given Map object.
     * @param map Map object which files are deleted
     * @return true if and only if all the existing files were
     * succesfully deleted; otherwise false
     */
    private boolean deleteFiles(Map map) {
        boolean success = true;
        // Map file must be deleted from each language directory
        for (Language lang : languagesService.getLanguages(map.getOwner())) {
            // Build absolute path of the map file
            String path = IllustrationsUtil.buildFilePath(map, lang.getCode());
            if (path == null) {
                logger.warn(new StringBuilder("Deleting map file failed! Unable to build path for the map file. {\"id\":").append(map.getId()).append(",\"lang\":\"").append(lang.getCode()).append("\"}"));
                // Jump to next language
                continue;
            }
            // Try to delete the file only if it exists.
            if (!fileService.exists(path)) {
                if (logger.isDebugEnabled()) {
                    logger.debug(new StringBuilder("Unable to delete map file, because it doesn't exist. File : ").append(path));
                }
                // Jump to next language
                continue;
            }
            // Delete the file
            if (!fileService.delete(path)) {
                success = false;
            }
        }
        return success;
    }
}
