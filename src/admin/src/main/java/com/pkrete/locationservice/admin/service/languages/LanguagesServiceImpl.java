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
package com.pkrete.locationservice.admin.service.languages;

import com.pkrete.locationservice.admin.dao.LanguagesDao;
import com.pkrete.locationservice.admin.service.LanguagesService;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.io.DirectoryService;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import com.pkrete.locationservice.admin.util.Settings;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements {@link LanguageService LanguageService} interface,
 * which defines service layer for Language objects.
 *
 * This class offers methods for adding, editing and removing Language objects.
 * Operations related to Language objects require both database and file system
 * operations, and single method may include multiple database and/or file
 * system calls. This class doesn't implement the operations, but it delegates
 * them to other objects.
 *
 * @author Petteri Kivimäki
 */
public class LanguagesServiceImpl implements LanguagesService {

    private final static Logger logger = LoggerFactory.getLogger(LanguagesServiceImpl.class.getName());
    private LanguagesDao dao;
    private DirectoryService dirService;
    private JSONizerService jsonizer;

    /**
     * Sets the data access object.
     *
     * @param dao new value
     */
    public void setDao(LanguagesDao dao) {
        this.dao = dao;
    }

    /**
     * Sets the JSON converter object.
     *
     * @param dao new value
     */
    public void setJsonizer(JSONizerService jsonizer) {
        this.jsonizer = jsonizer;
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
     * Returns a list of all the languages in the database that are related to
     * the given owner.
     *
     * @param owner the owner of the object
     * @return all the languages in the database
     */
    @Override
    public List<Language> getLanguages(Owner owner) {
        return dao.getLanguages(owner);
    }

    /**
     * Adds the given language object to the database and creates all the
     * language specific folders.
     *
     * @param language the language to be added
     * @return true if and only if all the folders were successfully created and
     * the object saved to the database; otherwise false
     */
    @Override
    public boolean create(Language language) {
        // Get list of all language specific directories
        List<File> dirs = Settings.getInstance().getLanguageSpecificDirs(language.getOwner().getCode());

        // Create folders for the new language
        List<String> createdDirs = new ArrayList<String>();
        boolean success = true;
        for (File dir : dirs) {
            String path = dir.getAbsolutePath() + "/" + language.getCode();
            if (!this.dirService.add(path)) {
                success = false;
                break;
            }
            createdDirs.add(path);
        }
        // Save the language to the DB if all the directories were
        // succesfully created
        if (success) {
            // Set created date
            language.setCreated(new Date());
            if (dao.create(language)) {
                logger.info("Language created : {}", this.jsonizer.jsonize(language, true));
                // Add to external index
                this.addToIndex(language);
                return true;
            }
            logger.warn("Failed to create language : {}", this.jsonizer.jsonize(language, true));
        } else {
            logger.warn("Creating new language directories failed -> ROLLBACK.");
        }
        logger.warn("Delete all the previously created directories.");
        // Do rollback if all the directories were not created.
        // Go through all the dirs and try to delete them.
        boolean deleted = true;
        for (String path : createdDirs) {
            // Directory must be empty
            if (!this.dirService.delete(path, true)) {
                deleted = false;
            }
        }
        if (!deleted) {
            logger.warn("Failed to delete all the previously created language directories.");
        } else {
            logger.info("Succesfully deleted all the previously created language directories.");
        }
        return false;
    }

    /**
     * Updates the given language object to the database. If the language code
     * has changed, all the language directory names are updated too.
     *
     * @param language the language to be updated
     * @return true if and only the object and the directories were successfully
     * updated; otherwise false
     */
    @Override
    public boolean update(Language language) {
        // Get language code's post modify value
        String codePrev = language.getCodePrevious();
        // Check if the current and previous value are not equal
        if (!language.getCode().equals(codePrev)) {
            logger.debug("Language's code changed -> update directories : \"{}\" -> \"{}\"", codePrev, language.getCode());
            // Get list of all language specific directories
            List<File> dirs = Settings.getInstance().getLanguageSpecificDirs(language.getOwner().getCode());

            // Rename all the language folders
            Map<String, String> renamedDirs = new HashMap<String, String>();
            boolean success = true;
            for (File dir : dirs) {
                String oldPath = dir.getAbsolutePath() + "/" + codePrev;
                String newPath = dir.getAbsolutePath() + "/" + language.getCode();
                if (!this.dirService.rename(oldPath, newPath)) {
                    success = false;
                    break;
                }
                renamedDirs.put(newPath, oldPath);
            }
            // Rollback if renaming of directories failed
            if (!success) {
                // Set back the old laguage code.
                language.setCode(codePrev);

                logger.warn("Renaming language directories failed -> ROLLBACK.");
                logger.warn("Undo all the changes.");

                // Do rollback if all the directories were not renamed.
                // Go through all the dirs and try to rename them with the
                // original name.
                boolean renamed = true;
                for (String key : renamedDirs.keySet()) {
                    if (!this.dirService.rename(key, renamedDirs.get(key))) {
                        renamed = false;
                    }
                }
                if (!renamed) {
                    logger.warn("Failed to restore all the previously renamed language directories.");
                } else {
                    logger.info("Succesfully restored all the previously renamed language directories.");
                }
                logger.warn("Failed to update language : {}", this.jsonizer.jsonize(language, true));
                return false;
            }
        }
        // Set updated date
        language.setUpdated(new Date());
        // Get JSON presentation
        String json = this.jsonizer.jsonize(language, true);
        // Try to update the DB
        if (dao.update(language)) {
            logger.info("Language updated : {}", json);
            // Update to external index
            this.updateToIndex(language);
            return true;
        }
        logger.warn("Failed to update language : {}", json);
        return false;
    }

    /**
     * Deletes the given language object from the database and deletes all the
     * language related directories.
     *
     * @param language the language to be deleted
     * @return true if and only if the language was deleted; otherwise false
     */
    @Override
    public boolean delete(Language language) {
        // Make sure that the language can be deleted
        if (canBeDeleted(language)) {
            // JSON presentation of the Language object
            String json = this.jsonizer.jsonize(language, true);
            // Get list of all language specific directories
            List<File> dirs = Settings.getInstance().getLanguageSpecificDirs(language.getOwner().getCode());

            // Go through the directories and delete them
            boolean deleted = true;
            for (File dir : dirs) {
                String path = dir.getAbsolutePath() + "/" + language.getCode();
                // Directory must be empty
                if (!this.dirService.delete(path, true)) {
                    deleted = false;
                }
            }
            if (!deleted) {
                logger.warn("Failed to delete all the language related directories.");
                logger.warn("Failed to delete language : {}", json);
                return false;
            } else {
                if (dao.delete(language)) {
                    logger.info("Language deleted : {}", json);
                    // Delete from external index
                    this.deleteFromIndex(language);
                    return true;
                }
                logger.warn("Failed to delete language : {}", json);
                return false;
            }
        }
        return false;
    }

    /**
     * Returns the language object with the given code.
     *
     * @param code code of the language object to be fetched
     * @param owner the owner of the object
     * @return language object with the given code or null
     */
    @Override
    public Language getLanguage(String code, Owner owner) {
        Language lang = dao.getLanguage(code, owner);
        if (lang != null) {
            lang.setCodePrevious(lang.getCode());
        }
        return lang;
    }

    /**
     * Returns the language with given id.
     *
     * @param id the id that is used for searching
     * @param owner the owner of the object
     * @return the language with the given id
     */
    @Override
    public Language getLanguageById(int id, Owner owner) {
        Language lang = dao.getLanguageById(id, owner);
        if (lang != null) {
            lang.setCodePrevious(lang.getCode());
        }
        return lang;
    }

    /**
     * Returns the language with given id. This method is only for editor
     * classes. All the other classes must give also the owner parameter.
     *
     * @param id the id that is used for searching
     * @return the language with the given id
     */
    @Override
    public Language getLanguageById(int id) {
        Language lang = dao.getLanguageById(id);
        if (lang != null) {
            lang.setCodePrevious(lang.getCode());
        }
        return lang;
    }

    /**
     * Checks that the language doesn't have any dependencies that would prevent
     * delete operation. Checks that directories related to this language are
     * empty and that there are no dependencies in the db. If the directories
     * are empty and language doesn't have any dependencies, it can be deleted.
     *
     * @param lang language to be deleted
     * @return true if the language can be deleted, otherwise returns false
     */
    @Override
    public boolean canBeDeleted(Language lang) {
        // Get list of all language specific directories
        List<File> dirs = Settings.getInstance().getLanguageSpecificDirs(lang.getOwner().getCode());

        // Go through the directories and check that they are empty
        for (File dir : dirs) {
            String path = dir.getAbsolutePath() + "/" + lang.getCode();
            if (!this.dirService.isEmpty(path)) {
                // Directory is not empty, there's no need to continue
                return false;
            }
        }
        return dao.canBeDeleted(lang);
    }

    /**
     * Checks if the given language already exists. This method should be used
     * every time before adding a new language or editing existing one.
     *
     * @param language language that's added or edited
     * @return true if and only if the language already exists; otherwise false
     */
    @Override
    public boolean exists(Language language) {
        // Get list of all language specific directories
        List<File> dirs = Settings.getInstance().getLanguageSpecificDirs(language.getOwner().getCode());

        // Go through the directories and check that the new language
        // directory doesn't exist yet
        for (File dir : dirs) {
            String path = dir.getAbsolutePath() + "/" + language.getCode();
            if (this.dirService.exists(path)) {
                // Directory exists, there's no need to continue
                return true;
            }
        }

        // Go through all the languages in the DB
        for (Language temp : this.getLanguages(language.getOwner())) {
            if (temp.getCode().equals(language.getCode())) {
                // Language with the same code exists already, no need to
                // continue
                return true;
            }
        }

        return false;
    }

    /**
     * Can be used for adding Languages to external index. Not implemented.
     *
     * @param language Language to be added
     * @return always true, not implemented
     */
    protected boolean addToIndex(Language language) {
        return true;
    }

    /**
     * Can be used for updating Languages to external index. Not implemented.
     *
     * @param language Language to be updated
     * @return always true, not implemented
     */
    protected boolean updateToIndex(Language language) {
        return true;
    }

    /**
     * Can be used for deleting Languages from external index. Not implemented.
     *
     * @param language Language to be deleted
     * @return always true, not implemented
     */
    protected boolean deleteFromIndex(Language language) {
        return true;
    }

    /**
     * Recreates the search index if it exists. Not supported.
     */
    @Override
    public void recreateSearchIndex() {
        logger.info("Search index not supported. Nothing to do.");
    }

    /**
     * Returns a list of all the languages in the database.
     *
     * @return all the languages in the database
     */
    protected List<Language> getLanguages() {
        return this.dao.getLanguages();
    }
}
