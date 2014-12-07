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

import com.pkrete.locationservice.admin.solr.model.builder.LocationDocumentBuilder;
import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import com.pkrete.locationservice.admin.model.search.LocationType;
import com.pkrete.locationservice.admin.solr.repository.RepositoryConstants;
import java.io.Serializable;
import java.util.List;
import org.apache.solr.client.solrj.beans.Field;

/**
 * This class represents a Location object that is stored in an external search
 * index. Only information that is used in searches is stored. When a Location
 * object with all the variables is needed, it's fetched from the DB.
 *
 * @author Petteri Kivimäki
 */
public class LocationDocument extends RepositoryDocument implements SimpleLocation, Serializable {

    @Field(RepositoryConstants.FIELD_DOCUMENT_ID)
    private int locationId;
    @Field(RepositoryConstants.FIELD_LOCATION_TYPE)
    private LocationType locationType;
    @Field(RepositoryConstants.FIELD_CALL_NUMBER)
    private String callNo;
    @Field(RepositoryConstants.FIELD_LOCATION_CODE)
    private String locationCode;
    @Field(RepositoryConstants.FIELD_COLLECTION_CODE)
    private String collectionCode;
    @Field(RepositoryConstants.FIELD_OWNER_ID)
    private int ownerId;
    @Field(RepositoryConstants.FIELD_OWNER_CODE)
    private String ownerCode;
    @Field(RepositoryConstants.FIELD_IS_SUBSTRING)
    private boolean isSubstring;
    @Field(RepositoryConstants.FIELD_PARENT_ID)
    private int parentId;
    @Field(RepositoryConstants.FIELD_GRANDPARENT_ID)
    private int grandparentId;
    @Field(RepositoryConstants.FIELD_FLOOR)
    private String floor;
    @Field(RepositoryConstants.FIELD_SHELF_NUMBER)
    private String shelf;
    @Field(RepositoryConstants.FIELD_NOTES)
    private List<String> notes;
    @Field(RepositoryConstants.FIELD_DESCRIPTIONS)
    private List<String> descriptions;
    @Field(RepositoryConstants.FIELD_STAFF_NOTE_PRI)
    private String staffNotePri;
    @Field(RepositoryConstants.FIELD_STAFF_NOTE_SEC)
    private String staffNoteSec;
    @Field(RepositoryConstants.FIELD_SUBJECTS)
    private List<String> subjectMatters;
    @Field(RepositoryConstants.FIELD_SUBJECT_IDS)
    private List<Integer> subjectMatterIds;

    /**
     * Creates and initializes a new LocationDocument object.
     */
    public LocationDocument() {
        super(DocumentType.LOCATION);
        this.isSubstring = false;
    }

    /**
     * Creates and initializes a new LocationDocument object with the given
     * values.
     *
     * @param locationId locationId of the Location object
     * @param name name of the location
     * @param locationType type of the Location object
     * (library/collection/shelf)
     * @param callNo call number of the location
     * @param locationCode location code of the location
     * @param collectionCode collection code (only collections can have one)
     * @param ownerId ownerId of the owner of the location
     */
    public LocationDocument(int locationId, String name, LocationType locationType, String callNumber, String locationCode, String collectionCode, int ownerId) {
        this();
        this.id = LocationDocumentBuilder.getId(locationId);
        this.locationId = locationId;
        this.name = name;
        this.locationType = locationType;
        this.callNo = callNumber;
        this.locationCode = locationCode;
        this.collectionCode = collectionCode;
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
        this.locationId = LocationDocumentBuilder.getId(id);
    }

    /**
     * Returns the locationId of this LocationDocument.
     *
     * @return locationId number
     */
    public int getLocationId() {
        return locationId;
    }

    /**
     * Sets the locationId of this LocationDocument.
     *
     * @param locationId new value
     */
    public void setLocationId(int locationId) {
        this.locationId = locationId;
        this.id = LocationDocumentBuilder.getId(locationId);
    }

    /**
     * Returns the type of the Location object to which this LocationDocument is
     * pointing. Type is LIBRARY, COLLECTION or SHELF.
     *
     * @return type of the Location object to which this LocationDocument is
     * pointing
     */
    public LocationType getLocationType() {
        return locationType;
    }

    /**
     * Sets the location type of the Location object to which this
     * LocationDocument is pointing. Type is LIBRARY, COLLECTION or SHELF.
     *
     * @param locationType new locationType
     */
    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    /**
     * Returns the owner id of this LocationDocument.
     *
     * @return owner id of this LocationDocument
     */
    public int getOwnerId() {
        return this.ownerId;
    }

    /**
     * Changes the owner id of this LocationDocument.
     *
     * @param ownerId new owner id
     */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
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
     * Returns the location code of the location.
     *
     * @return locatioan code of the location
     */
    public String getLocationCode() {
        return locationCode;
    }

    /**
     * Sets the location code.
     *
     * @param locationCode new value
     */
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    /**
     * Returns the code of the collection that's used in the ILS.
     *
     * @return code of the collection that's used in the ILS
     */
    public String getCollectionCode() {
        return this.collectionCode;
    }

    /**
     * Changes the code of the collection that's used in the ILS.
     *
     * @param collectionCode new collection code
     */
    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    /**
     * Returns the call number of the location.
     *
     * @return call number of the location
     */
    public String getCallNo() {
        return callNo;
    }

    /**
     * Sets the call number.
     *
     * @param callNo new value
     */
    public void setCallNo(String callNumber) {
        this.callNo = callNumber;
    }

    /**
     * Returns a boolean value that tells if the location code should be
     * considered as a substring in the beginning of a string, or if it's an
     * entire word. This variable is used only when locationType is COLLECTION.
     * If locationType is LIBRARY or SHELF, false is always returned.
     *
     * @return true if is a substring, otherwise false
     */
    public boolean getIsSubstring() {
        return this.isSubstring;
    }

    /**
     * Changes the boolean value that tells if the location code should be
     * considered as a substring in the beginning of a string, or if it's an
     * entire word. This variable is used only when locationType is COLLECTION.
     * If locationType is LIBRARY or SHELF, false is always returned.
     *
     * @param sub new value
     */
    public void setIsSubstring(boolean sub) {
        this.isSubstring = sub;
    }

    /**
     * Returns the parent id of this LocationDocument. If this object represents
     * a Shelf the parent id is the locationId of the collection. If this object
     * represents a collection the parentId is the locationId of the library.
     *
     * @return parent id of this LocationDocument
     */
    public int getParentId() {
        return this.parentId;
    }

    /**
     * Changes the parent id of this LocationDocument. If this object represents
     * a Shelf the parent id is the locationId of the collection. If this object
     * represents a collection the parentId is the locationId of the library.
     *
     * @param parentId new parent id
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    /**
     * Returns the grandparent id of this LocationDocument. If this object
     * represents a Shelf the parent id is the locationId of the collection and
     * the grandparent id is the locationId of the library. If this object
     * represents a collection or a library the grandparentId is 0.
     *
     * @return grandparent id of this LocationDocument
     */
    public int getGrandparentId() {
        return this.grandparentId;
    }

    /**
     * Changes the grandparent id of this LocationDocument. If this object
     * represents a Shelf the parent id is the locationId of the collection and
     * the grandparent id is the locationId of the library. If this object
     * represents a collection or a library the grandparentId is 0.
     *
     * @param grandparentId new grandparent id
     */
    public void setGrandparentId(int grandparentId) {
        this.grandparentId = grandparentId;
    }

    /**
     * Returns a boolean value that indicates if this LocationDocument has a
     * collection code.
     *
     * @return true if this SeachIndex has a collection code, otherwise false
     */
    public boolean hasCollectionCode() {
        if (collectionCode == null) {
            return false;
        }
        if (collectionCode.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Returns the floor of the location.
     *
     * @return floor of the location
     */
    public String getFloor() {
        return floor;
    }

    /**
     * Sets the floor of the location.
     *
     * @param floor new value
     */
    public void setFloor(String floor) {
        this.floor = floor;
    }

    /**
     * Returns the shelf number of the location.
     *
     * @return shelf number of the location
     */
    public String getShelf() {
        return shelf;
    }

    /**
     * Sets the shelf number of the location.
     *
     * @param shelf new value
     */
    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    /**
     * Returns a list of notes related to this location.
     *
     * @return list of notes
     */
    public List<String> getNotes() {
        return notes;
    }

    /**
     * Sets the list of notes related to this location.
     *
     * @param notes new value
     */
    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    /**
     * Returns a list of descriptions related to this location.
     *
     * @return list of descriptions
     */
    public List<String> getDescriptions() {
        return descriptions;
    }

    /**
     * Sets the list of descriptions related to this location.
     *
     * @param descriptions new value
     */
    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * Returns a list of primary staff notes related to this location.
     *
     * @return list of primary staff notes
     */
    public String getStaffNotePri() {
        return staffNotePri;
    }

    /**
     * Sets the list of primary staff notes related to this location.
     *
     * @param staffNotePri new value
     */
    public void setStaffNotePri(String staffNotePri) {
        this.staffNotePri = staffNotePri;
    }

    /**
     * Returns a list of secondary staff notes related to this location.
     *
     * @return list of secondary staff notes
     */
    public String getStaffNoteSec() {
        return staffNoteSec;
    }

    /**
     * Sets the list of secondary staff notes related to this location.
     *
     * @param staffNoteSec new value
     */
    public void setStaffNoteSec(String staffNoteSec) {
        this.staffNoteSec = staffNoteSec;
    }

    /**
     * Returns a list of subject matters related to this location.
     *
     * @return list of subject matters
     */
    public List<String> getSubjectMatters() {
        return subjectMatters;
    }

    /**
     * Sets the list of subject matters related to this location.
     *
     * @param subjectsMatters new value
     */
    public void setSubjectMatters(List<String> subjectMatters) {
        this.subjectMatters = subjectMatters;
    }

    /**
     * Returns a list of subject matter ids related to this location.
     *
     * @return list of subject matter ids
     */
    public List<Integer> getSubjectMatterIds() {
        return subjectMatterIds;
    }

    /**
     * Sets the list of subject matter ids related to this location.
     *
     * @param subjectMatterIds new value
     */
    public void setSubjectMatterIds(List<Integer> subjectMatterIds) {
        this.subjectMatterIds = subjectMatterIds;
    }
}
