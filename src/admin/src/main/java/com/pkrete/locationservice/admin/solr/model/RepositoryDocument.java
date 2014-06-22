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

import com.pkrete.locationservice.admin.solr.repository.RepositoryConstants;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;

/**
 * This is a base class for all the classes representing Solr index
 * entries.
 * 
 * @author Petteri Kivimäki
 */
public class RepositoryDocument {

    @Id
    @Field(RepositoryConstants.FIELD_ID)
    protected String id;
    @Field(RepositoryConstants.FIELD_DOCUMENT_TYPE)
    protected DocumentType documentType;
    @Field
    protected String name;

    /**
     * Constructs and initializes a new RepositoryDocument.
     * 
     * @param documentType type of the document
     */
    public RepositoryDocument(DocumentType documentType) {
        this.documentType = documentType;
    }

    /**
     * Returns the id of this Document. 
     * @return  id of this Document
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of this Document. 
     * 
     * @param id new value
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the name of the location.
     * @return name of the location
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the location.
     * @param name new value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of this Document.
     * @return document type
     */
    public DocumentType getDocumentType() {
        return documentType;
    }

    /**
     * Sets the type of this document.
     * @param documentType new value
     */
    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
}
