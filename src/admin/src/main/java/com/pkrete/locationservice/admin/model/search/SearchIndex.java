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
package com.pkrete.locationservice.admin.model.search;

import com.pkrete.locationservice.admin.model.owner.Owner;
import java.io.Serializable;

/**
 * The <code>SearchIndex</code> class represents a single entry in the search
 * index.
 *
 * @author Petteri Kivimäki
 */
public class SearchIndex implements Serializable {

    private int id;
    private int locationId;
    private LocationType locationType;
    private String callNumber;
    private String locationCode;
    private String collectionCode;
    private Owner owner;

    /**
     * Constructs and initializes a new SearchIndex object.
     */
    public SearchIndex() {
    }

    /**
     * Constructs and initializes a new SearchIndex object with the given
     * values.
     *
     * @param locationId id of the Location object to which this SearchIndex is
     * pointing
     * @param locationType type of the Location object to which this SearchIndex
     * is pointing
     * @param callNo call number of the location
     * @param locationCode location code of the location
     * @param owner owner of this SearchIndex
     */
    public SearchIndex(int locationId, LocationType locationType, String callNo, String locationCode, Owner owner) {
        this.locationId = locationId;
        this.locationType = locationType;
        this.callNumber = callNo;
        this.locationCode = locationCode;
        this.owner = owner;
    }

    /**
     * Constructs and initializes a new SearchIndex object with the given
     * values.
     *
     * @param locationId id of the Location object to which this SearchIndex is
     * pointing
     * @param locationType type of the Location object to which this SearchIndex
     * is pointing
     * @param callNo call number of the location
     * @param locationCode location code of the location
     * @param owner owner of this SearchIndex
     * @param collectionCode collection code of the collection
     */
    public SearchIndex(int locationId, LocationType locationType, String callNo, String locationCode, Owner owner, String collectionCode) {
        this(locationId, locationType, callNo, locationCode, owner);
        this.collectionCode = collectionCode;
    }

    /**
     * Returns the id of this SearchIndex.
     *
     * @return id number
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of this SearchIndex.
     *
     * @param id new callNumber
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the id of the Location object to which this SearchIndex is
     * pointing.
     *
     * @return location id of the Location object to which this SearchIndex is
     * pointing
     */
    public int getLocationId() {
        return locationId;
    }

    /**
     * Sets the id of the Location object to which this SearchIndex is pointing.
     *
     * @param locationId new callNumber
     */
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    /**
     * Returns the type of the Location object to which this SearchIndex is
     * pointing. Type is LIBRARY, COLLECTION or SHELF.
     *
     * @return type of the Location object to which this SearchIndex is pointing
     */
    public LocationType getLocationType() {
        return locationType;
    }

    /**
     * Sets the location type of the Location object to which this SearchIndex
     * is pointing. Type is LIBRARY, COLLECTION or SHELF.
     *
     * @param locationType new callNumber
     */
    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    /**
     * Returns the callNumber that is indexed.
     *
     * @return indexed callNumber
     */
    public String getValue() {
        return getCallNumber();
    }

    /**
     * Sets the indexed callNumber.
     *
     * @param callNumber new callNumber
     */
    public void setValue(String value) {
        this.setCallNumber(value);
    }

    /**
     * Returns the owner of this SearchIndex
     *
     * @return owner of this SearchIndex
     */
    public Owner getOwner() {
        return this.owner;
    }

    /**
     * Changes the owner of this SearchIndex.
     *
     * @param owner new owner
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
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
    public String getCallNumber() {
        return callNumber;
    }

    /**
     * Sets the call number.
     *
     * @param callNumber new value
     */
    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    /**
     * Returns a boolean value that indicates if this SearchIndex has a
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
}
