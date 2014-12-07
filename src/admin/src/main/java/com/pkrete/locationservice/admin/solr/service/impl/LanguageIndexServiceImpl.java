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
package com.pkrete.locationservice.admin.solr.service.impl;

import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.solr.model.DocumentType;
import com.pkrete.locationservice.admin.solr.model.LanguageDocument;
import com.pkrete.locationservice.admin.solr.model.builder.LanguageDocumentBuilder;
import com.pkrete.locationservice.admin.solr.repository.LanguageDocumentRepository;
import com.pkrete.locationservice.admin.solr.repository.RepositoryConstants;
import com.pkrete.locationservice.admin.solr.service.LanguageIndexService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

/**
 *
 * @author dinky
 */
public class LanguageIndexServiceImpl implements LanguageIndexService {

    private final static Logger logger = LoggerFactory.getLogger(LocationIndexServiceImpl.class.getName());
    private LanguageDocumentRepository repository;

    /**
     * Sets the value of the repository variable.
     *
     * @param repository new value
     */
    public void setRepository(LanguageDocumentRepository repository) {
        this.repository = repository;
    }

    /**
     * Adds or updates the given Language object to the search index. Returns
     * true if and only if the LocLanguageation was successfully added or
     * updated; otherwise false.
     *
     * @param language Language to be added
     * @return true if and only if the Language was successfully added or
     * updated; otherwise false
     */
    @Override
    public boolean save(Language language) {
        // Get a LanguageDocument representing the given Language
        LanguageDocument document = LanguageDocumentBuilder.build(language);
        // Save the LanguageDocument to the index
        return this.save(document);
    }

    /**
     * Adds or updates the given LanguageDocument object to the search index.
     * Returns true if and only if the LanguageDocument was successfully added
     * or updated; otherwise false.
     *
     * @param document LanguageDocument to be added
     * @return true if and only if the LanguageDocument was successfully added
     * or updated; otherwise false
     */
    protected boolean save(LanguageDocument document) {
        try {
            // Save the LanguageDocument to the index
            this.repository.save(document);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Deletes the LanguageDocument with the given id from the search index.
     * Returns true if and only if the LanguageDocument was successfully
     * deleted; otherwise false
     *
     * @param id languageId of the LanguageDocument to be deleted
     * @return true if and only if the LanguageDocument was successfully
     * deleted; otherwise false
     */
    @Override
    public boolean delete(Integer id) {
        try {
            this.repository.delete(LanguageDocumentBuilder.getId(id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Deletes all the index entries. Returns true if and only if all the
     * entries were successfully deleted; otherwise false.
     *
     * @return true if and only if all the entries were successfully deleted;
     * otherwise false
     */
    @Override
    public boolean deleteAll() {
        try {
            List<LanguageDocument> list = this.repository.findByDocumentType(DocumentType.LANGUAGE);
            for (LanguageDocument document : list) {
                this.repository.delete(document);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Returns a list of languages related to the given owner. The list is
     * sorted in ascending order by name.
     *
     * @param ownerId id of the owner of the languages
     * @return list of languages related to the given owner
     */
    @Override
    public List<Language> search(Integer ownerId) {
        List<LanguageDocument> docs = this.repository.findByOwnerIdAndDocumentType(ownerId, DocumentType.LANGUAGE, this.sortByName());
        List<Language> languages = new ArrayList<Language>();
        for (LanguageDocument doc : docs) {
            languages.add(LanguageDocumentBuilder.build(doc));
        }
        return languages;
    }

    /**
     * Returns the language object with the given language code.
     *
     * @param code code of the language object to be fetched
     * @param owner the owner of the object
     * @return language object with the given code or null
     */
    @Override
    public Language search(Integer ownerId, String languageCode) {
        LanguageDocument document = this.repository.findByOwnerIdAndLanguageCodeAndDocumentType(ownerId, languageCode, DocumentType.LANGUAGE);
        if (document != null) {
            return LanguageDocumentBuilder.build(document);
        }
        return null;
    }

    /**
     * Returns a Sort object that sorts the results in ascending order by name.
     *
     * @return Sort object that sorts the results in ascending order by name
     */
    private Sort sortByName() {
        return new Sort(Sort.Direction.ASC, RepositoryConstants.FIELD_NAME);
    }
}
