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
package com.pkrete.locationservice.admin.solr.model.builder;

import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.solr.model.LanguageDocument;
import com.pkrete.locationservice.admin.solr.repository.RepositoryConstants;

/**
 * This is a helper class that generates LanguageDocument objects representing
 * the given Languages.
 *
 * @author Petteri Kivimäki
 */
public class LanguageDocumentBuilder {

    /**
     * Creates a new LanguageDocument that represents the given Language.
     *
     * @param language Language object
     * @return LanguageDocument that represents the given Language
     */
    public static LanguageDocument build(Language language) {
        // Create a new OwnerDocument object
        LanguageDocument document = new LanguageDocument(language.getId(), language.getName(), language.getCode(), language.getOwner().getId());
        // Return the object
        return document;
    }

    /**
     * Creates a new Language that represents the given LanguageDocument.
     *
     * @param document LanguageDocument object
     * @return Language that represents the given LanguageDocument
     */
    public static Language build(LanguageDocument document) {
        // Create a new Language object
        Language language = new Language(document.getLanguageId(), document.getName(), document.getLanguageCode());
        // Return the object
        return language;
    }

    /**
     * Converts the given int id to a string by adding 'lang-' prefix to the id.
     *
     * @param id ownerId of the Language
     * @return 'lang-' prefix + languageId
     */
    public static String getId(int id) {
        return RepositoryConstants.PREFIX_LANGUAGE + Integer.toString(id);
    }

    /**
     * Converts the given string id to an int by removing 'lang-' prefix from
     * the id.
     *
     * @param id id of the LanguageDocument
     * @return id without 'lang-' prefix, which is the languageId
     */
    public static int getId(String id) {
        id = id.replaceAll(RepositoryConstants.PREFIX_LANGUAGE, "");
        return Integer.parseInt(id);
    }
}
