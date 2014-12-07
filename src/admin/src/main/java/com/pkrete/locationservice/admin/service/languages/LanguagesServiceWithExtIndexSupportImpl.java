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

import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.solr.service.LanguageIndexService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class extends {@link LanguagesServiceImpl LanguagesServiceImpl} class
 * which implements {@link LanguagesService LanguagesService} interface, that
 * defines service layer for Language objects.
 *
 * This class offers methods for adding, editing and removing Language objects.
 * This class overrides methods that define operations with an external index
 * software and external index software is supported.
 *
 * @author Petteri Kivimäki
 */
public class LanguagesServiceWithExtIndexSupportImpl extends LanguagesServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(LanguagesServiceWithExtIndexSupportImpl.class.getName());
    private LanguageIndexService languageIndexService;

    /**
     * Sets the language index service object.
     *
     * @param languageIndexService new value
     */
    public void setLanguageIndexService(LanguageIndexService languageIndexService) {
        this.languageIndexService = languageIndexService;
    }

    /**
     * Adds the Language to external index. Returns true if and only if the
     * Language was successfully added, otherwise false.
     *
     * @param language Language to be added
     * @return true if and only if the Language was successfully added,
     * otherwise false
     */
    @Override
    protected boolean addToIndex(Language language) {
        if (this.languageIndexService.save(language)) {
            logger.debug("Adding language to external index succeeded. Id : " + language.getId());
            return true;
        }
        logger.error("Adding language to external index failed. Id : " + language.getId());
        return false;
    }

    /**
     * Updated the Language to external index. Returns true if and only if the
     * Language was successfully updated, otherwise false.
     *
     * @param location Language to be updated
     * @return true if and only if the Language was successfully updated,
     * otherwise false
     */
    @Override
    protected boolean updateToIndex(Language language) {
        if (this.languageIndexService.save(language)) {
            logger.debug("Updating language to external index succeeded. Id : " + language.getId());
            return true;
        }
        logger.error("Updating language to external index failed. Id : " + language.getId());
        return false;
    }

    /**
     * Deletes the Language from external index. Returns true if and only if the
     * Language was successfully deleted, otherwise false.
     *
     * @param location Language to be deleted
     * @return true if and only if the Language was successfully deleted,
     * otherwise false
     */
    @Override
    protected boolean deleteFromIndex(Language language) {
        if (this.languageIndexService.delete(language.getId())) {
            logger.debug("Deleting language from external index succeeded. Id : " + language.getId());
            return true;
        }
        logger.error("Deleting language from external index failed. Id : " + language.getId());
        return false;
    }

    /**
     * Recreates the search index. First all the current entries are deleted and
     * then regenerated.
     */
    @Override
    public void recreateSearchIndex() {
        logger.info("Recreate languages search index.");
        // Delete all the current index entries
        this.languageIndexService.deleteAll();
        // Get all the languages from the DB
        List<Language> languages = super.getLanguages();
        // Go through the list and add each language to index
        for (Language language : languages) {
            logger.debug("Create index entry for language. Id : " + language.getId());
            if (!this.languageIndexService.save(language)) {
                logger.error("Creating index entry for language failed! Id : " + language.getId());
            }
        }
        logger.info("Indexing languages done.");
    }

    /**
     * Returns a list of all the languages that are related to the given owner.
     *
     * @param owner the owner of the object
     * @return all the languages related to the given Owner
     */
    @Override
    public List<Language> getLanguages(Owner owner) {
        List<Language> languages = this.languageIndexService.search(owner.getId());
        for (Language lang : languages) {
            lang.setOwner(owner);
        }
        return languages;
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
        Language lang = this.languageIndexService.search(owner.getId(), code);
        if (lang != null) {
            lang.setCodePrevious(lang.getCode());
        }
        return lang;
    }
}
