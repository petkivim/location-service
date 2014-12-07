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
package com.pkrete.locationservice.admin.solr.model;

import com.pkrete.locationservice.admin.model.owner.LocatingStrategy;
import com.pkrete.locationservice.admin.solr.model.builder.OwnerDocumentBuilder;
import com.pkrete.locationservice.admin.solr.repository.RepositoryConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.solr.client.solrj.beans.Field;

/**
 * This class represents an Owner object that is stored in an external search
 * index. Only information that is used in searches is stored. When an Owner
 * object with all the variables is needed, it's fetched from the DB.
 *
 * @author Petteri Kivimäki
 */
public class OwnerDocument extends RepositoryDocument implements Serializable {

    @Field(RepositoryConstants.FIELD_DOCUMENT_ID)
    private int ownerId;
    @Field(RepositoryConstants.FIELD_OWNER_CODE)
    private String ownerCode;
    @Field(RepositoryConstants.FIELD_LOCATING_STRATEGY)
    private LocatingStrategy locatingStrategy;
    @Field(RepositoryConstants.FIELD_COLOR)
    private String color;
    @Field(RepositoryConstants.FIELD_OPACITY)
    private String opacity;
    @Field(RepositoryConstants.FIELD_EXPORTER_VISIBLE)
    private boolean exporterVisible;
    @Field(RepositoryConstants.FIELD_ALLOWED_IPS)
    private String allowedIPs;
    @Field(RepositoryConstants.FIELD_PREPROCESSING_REDIRECTS)
    private List<String> preprocessingRedirects;
    @Field(RepositoryConstants.FIELD_NOT_FOUND_REDIRECTS)
    private List<String> notFoundRedirects;

    /**
     * Constructs and initializes a new OwnerDocument.
     */
    public OwnerDocument() {
        super(DocumentType.OWNER);
    }

    /**
     * Constructs and initializes a new OwnerDocument with the given values.
     *
     * @param ownerId owner id
     * @param name name of the owner
     * @param ownerCode code of the owner
     */
    public OwnerDocument(int ownerId, String name, String ownerCode) {
        this();
        this.ownerId = ownerId;
        this.id = OwnerDocumentBuilder.getId(ownerId);
        this.name = name;
        this.ownerCode = ownerCode;
        this.preprocessingRedirects = new ArrayList<String>();
    }

    /**
     * Sets the id of this Document.
     *
     * @param id new value
     */
    @Override
    public void setId(String id) {
        this.id = id;
        this.ownerId = OwnerDocumentBuilder.getId(id);
    }

    /**
     * Returns the ownerId of this OwnerDocument.
     *
     * @return ownerId number
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * Sets the ownerId of this OwnerDocument.
     *
     * @param ownerId new value
     */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        this.id = OwnerDocumentBuilder.getId(ownerId);
    }

    /**
     * Returns the owner code of the owner.
     *
     * @return owner code of the owner
     */
    public String getOwnerCode() {
        return ownerCode;
    }

    /**
     * Sets the owner code.
     *
     * @param ownerCode new value
     */
    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    /**
     * Returns the locating strategy that's used for searching call numbers.
     *
     * @return locating strategy that's used for searching call numbers
     */
    public LocatingStrategy getLocatingStrategy() {
        return locatingStrategy;
    }

    /**
     * Sets the locating strategy that's used for searching call numbers.
     *
     * @param locatingStrategy new strategy
     */
    public void setLocatingStrategy(LocatingStrategy locatingStrategy) {
        this.locatingStrategy = locatingStrategy;
    }

    /**
     * Return the default drawing color of this owner.
     *
     * @return default drawing color
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Changes the default drawing color.
     *
     * @param color new drawing color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Returns the opacity of the drawing color.
     *
     * @return opacity of the drawing color
     */
    public String getOpacity() {
        return this.opacity;
    }

    /**
     * Changes the opacity of the drawing color.
     *
     * @param opacity new opacity
     */
    public void setOpacity(String opacity) {
        this.opacity = opacity;
    }

    /**
     * Returns a boolean value that tells if the Exporter interface is open for
     * everyone.
     *
     * @return true if Exported is open for everyone, otherwise false
     */
    public boolean getExporterVisible() {
        return this.exporterVisible;
    }

    /**
     * Changes the boolean value that tells if the Exporter interface is open
     * for everyone.
     *
     * @param visible new value
     */
    public void setExporterVisible(boolean visible) {
        this.exporterVisible = visible;
    }

    /**
     * Returns a string containing the IP addresses that are allowed to access
     * the Exporter interface, even if the interface would be closed.
     *
     * @return IP addresses that are allowed to access the Exporter interface
     */
    public String getAllowedIPs() {
        return this.allowedIPs;
    }

    /**
     * Changes the string containing the IP addresses that are allowed to access
     * the Exporter interface, even if the interface would be closed.
     *
     * @param ips new ips
     */
    public void setAllowedIPs(String ips) {
        this.allowedIPs = ips;
    }

    /**
     * Returns a list of preprocessing redirects.
     *
     * @return list of preprocessing redirects
     */
    public List<String> getPreprocessingRedirects() {
        return preprocessingRedirects;
    }

    /**
     * Sets the value of preprocessing redirects.
     *
     * @param preprocessingRedirects new value
     */
    public void setPreprocessingRedirects(List<String> preprocessingRedirects) {
        this.preprocessingRedirects = preprocessingRedirects;
    }

    /**
     * Returns a list of not found redirects.
     *
     * @return list of not found redirects
     */
    public List<String> getNotFoundRedirects() {
        return notFoundRedirects;
    }

    /**
     * Sets the value of not found redirects.
     *
     * @param notFoundRedirects new value
     */
    public void setNotFoundRedirects(List<String> notFoundRedirects) {
        this.notFoundRedirects = notFoundRedirects;
    }
}
