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
package com.pkrete.locationservice.admin.solr.model;

import com.pkrete.locationservice.admin.solr.model.builder.LanguageDocumentBuilder;
import com.pkrete.locationservice.admin.solr.repository.RepositoryConstants;
import java.io.Serializable;
import org.apache.solr.client.solrj.beans.Field;

/** 
 * This class represents a Language object that is stored in an external
 * search index. Only information that is used in searches is
 * stored. When a Language object with all the variables is needed,
 * it's fetched from the DB.
 * 
 * @author Petteri Kivimäki
 */
public class LanguageDocument extends RepositoryDocument implements Serializable {

    @Field(RepositoryConstants.FIELD_DOCUMENT_ID)
    private int languageId;
    @Field(RepositoryConstants.FIELD_LANGUAGE_CODE)
    private String languageCode;
    @Field(RepositoryConstants.FIELD_OWNER_ID)
    private int ownerId;

    /**
     * Constructs and initializes a new LanguageDocument.
     */
    public LanguageDocument() {
        super(DocumentType.LANGUAGE);
    }

    /**
     * Constructs and initializes a new LanguageDocument with the given values.
     * @param id id of the Language
     * @param name name of the Language
     * @param languageCode language code of the Language
     * @param ownerId ownerId of the owner of the Language
     */
    public LanguageDocument(int id, String name, String languageCode, int ownerId) {
        this();
        this.languageId = id;
        this.id = LanguageDocumentBuilder.getId(id);
        this.name = name;
        this.languageCode = languageCode;
        this.ownerId = ownerId;
    }

    /**
     * Sets the id of this Document. 
     * 
     * @param id new value
     */
    @Override
    public void setId(String id) {
        this.id = id;
        this.languageId = LanguageDocumentBuilder.getId(id);
    }

    /**
     * Returns the language id.
     * @return language id
     */
    public int getLanguageId() {
        return languageId;
    }

    /**
     * Sets the language id.
     * @param languageId new value
     */
    public void setLanguageId(int languageId) {
        this.languageId = languageId;
        this.id = LanguageDocumentBuilder.getId(languageId);
    }

    /**
     * Returns the language code.
     * @return language code
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * Sets the language code
     * @param lanuageCode new value
     */
    public void setLanguageCode(String lanuageCode) {
        this.languageCode = lanuageCode;
    }

    /**
     * Returns the owner id of this LanguageDocument.
     * @return owner id of this LanguageDocument
     */
    public int getOwnerId() {
        return this.ownerId;
    }

    /**
     * Changes the owner id of this LanguageDocument.
     * @param ownerId new owner id
     */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
