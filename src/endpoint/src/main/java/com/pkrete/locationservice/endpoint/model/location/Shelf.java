/**
 * This file is part of Location Service :: Endpoint.
 * Copyright (C) 2014 Petteri Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.model.location;

import com.pkrete.locationservice.endpoint.model.owner.Owner;
import com.pkrete.locationservice.endpoint.model.search.LocationType;

/**
 * The <code>Shelf</code> class extends the {@link Location Location} class.
 *
 * The Shelf class represents a shelf that belongs to a collection. A shelf
 * can belong to only one collection at a time.
 *
 * @author Petteri Kivimäki
 */
public class Shelf extends Location {

    /**
     * The collection that owns the shelf.
     */
    private LibraryCollection collection;
    /**
     * The number or the code of physical shelf
     * of this shelf class.
     */
    private String shelfNumber;

    /**
     * Constructs and initializes a shelf with no location code and with no owning collection.
     */
    public Shelf() {
        super("");
    }

    /**
     * Construct and initializes a shelf with the given location code and owning collection.
     * @param locationCode the location code of the collection
     * @param collection the collection that owns the shelf
     */
    public Shelf(String locationCode, LibraryCollection collection) {
        super(locationCode);
        this.collection = collection;
    }

    /**
     * Changes the owning collection of the shelf.
     * @param collection the new owning collection
     */
    public void setCollection(LibraryCollection collection) {
        this.collection = collection;
    }

    /**
     * Returns the owning collection of the shelf.
     * @return the owning collection of the shelf
     */
    public LibraryCollection getCollection() {
        return this.collection;
    }

    @Override
    /**
     * Returns the owner of the shelf.
     * @return owner of the location
     */
    public Owner getOwner() {
        return collection.getLibrary().getOwner();
    }

    /**
     * Returns the shelf number of this shelf object.
     * @return the number of the shelf
     */
    public String getShelfNumber() {
        return this.shelfNumber;
    }

    /**
     * Changes the shelf number of this shelf object.
     * @param shelfNumber new shelf number
     */
    public void setShelfNumber(String shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    /**
     * Return the call number of the shelf. The call number is formed by the
     * location code of the owning library, the location code of the collection and
     * the location code of the shelf.
     * @return the call number of the shelf
     */
    public String getCallNo() {
        String callno = "";
        if (!collection.getLibrary().getLocationCode().isEmpty()) {
            callno = collection.getLibrary().getLocationCode() + " ";
        }
        if (!collection.getLocationCode().isEmpty()) {
            callno = callno + collection.getLocationCode().trim() + " ";
        }
        callno = callno + super.getLocationCode().trim();
        return callno;
    }

    /**
     * Returns the call number of the location in a format that is
     * used in templates' names.All the whitespaces in the call number are 
     * replaced with underscores.
     * @param incCollectionCode boolean value that tells if collection
     * code should be included in the returned call number. If true, collection
     * code is included only if it exists and is not empty
     * @return the call number of the location
     */
    public String getCallNoForTemplateName(boolean incCollectionCode) {
        StringBuilder builder = new StringBuilder();
        builder.append(this.collection.getLibrary().getLocationCode()).append(" ");
        builder.append(this.collection.getLocationCode()).append(" ");
        builder.append(this.locationCode);
        if (incCollectionCode && this.collection.hasCollectionCode()) {
            builder.append(" ").append(this.collection.getCollectionCode());
        }
        return builder.toString().replaceAll(" ", "_");
    }

    /**
     * Returns the main word that is included in the given call number.
     * @param callno the call number that includes the main word
     * @return
     */
    public String getMainWord(String callno) {
        String result = callno;
        if (!this.collection.getIsSubstring()) {
            if (callno.startsWith(getCallNo())) {
                result = callno.replace(getCallNo(), "");
            } else if (callno.startsWith(this.getCollection().getCallNo())) {
                result = callno.replace(this.getCollection().getCallNo(), "");
            } else if (callno.startsWith(this.getCollection().getLibrary().getCallNo())) {
                result = callno.replace(this.getCollection().getLibrary().getCallNo(), "");
            }
            return result.trim();
        }
        result = callno.replace(this.collection.getCallNo(), "");
        return result.trim();
    }

    /**
     * Returns the location type SHELF.
     * @return location type SHELF
     */
    public LocationType getLocationType() {
        return LocationType.SHELF;
    }

    /**
     * Returns the collection code of the parent collection.
     * @return collection code of the parent collection
     */
    public String getCollectionCode() {
        return this.collection.getCollectionCode();
    }

    /**
     * Returns a boolean value that indicates if the parent collection
     * of this shelf has a collection code.
     * @return true if the parent collection has a collection code, 
     * otherwise false
     */
    @Override
    public boolean hasCollectionCode() {
        return this.collection.hasCollectionCode();
    }
}
