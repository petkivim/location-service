/**
 * This file is part of Location Service :: Endpoint. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.model.location;

import com.pkrete.locationservice.endpoint.model.owner.Owner;
import com.pkrete.locationservice.endpoint.model.search.LocationType;
import java.util.*;

/**
 * The <code>LibraryCollection</code> class extends the
 * {@link Location Location} class.
 *
 * The LibraryCollection class represents a collection that belongs to a
 * library. A collection can belong to only one library at a time. A collection
 * can include shelves that are represented by the {@link Shelf Shelf} class.
 *
 * @author Petteri Kivimäki
 */
public class LibraryCollection extends Location {
    /* Code of the collection that's used in the ILS. */

    private String collectionCode;
    /**
     * List of the shelves that belong to the collection.
     */
    private List<Shelf> shelves;
    /**
     * The library that owns the collection.
     */
    private Library library;
    /**
     * The number or the code of physical shelf where this collection is
     * located.
     */
    private String shelfNumber;

    /**
     * Constructs and initializes a collection with no location code and with no
     * owning library.
     */
    public LibraryCollection() {
        super("");
        this.shelves = new ArrayList<Shelf>();
    }

    /**
     * Construct and initializes a collection with the given location code and
     * owning library.
     *
     * @param locationCode the location code of the collection
     * @param library the library that owns the collection
     */
    public LibraryCollection(String locationCode, Library library) {
        super(locationCode);
        this.shelves = new ArrayList<Shelf>();
        this.library = library;
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
     * Returns a boolean value that indicates if this collection has a
     * collection code.
     *
     * @return true if the collection has a collection code, otherwise false
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
     * Returns the shelves that belong to the collection.
     *
     * @return the shelves of the collection
     */
    public List<Shelf> getShelves() {
        return this.shelves;
    }

    /**
     * Changes all the shelves that related to the collection.
     *
     * @param shelves the new shelves
     */
    public void setShelves(List<Shelf> shelves) {
        this.shelves = shelves;
    }

    /**
     * Adds a new shelf to the collection.
     *
     * @param shelf the new shelf
     */
    public void addShelf(Shelf shelf) {
        this.shelves.add(shelf);
    }

    /**
     * Removes the specified shelf from the collection
     *
     * @param shelf the shelf to be removed from the collection
     */
    public void removeShelf(Shelf shelf) {
        this.shelves.remove(shelf);
    }

    /**
     * Changes the owning library of the collection.
     *
     * @param library the new owning library
     */
    public void setLibrary(Library library) {
        this.library = library;
    }

    /**
     * Returns the owning library of the collection.
     *
     * @return the owning library of the collection
     */
    public Library getLibrary() {
        return this.library;
    }

    @Override
    /**
     * Returns the owner of the collection.
     *
     * @return owner of the location
     */
    public Owner getOwner() {
        return library.getOwner();
    }

    /**
     * Returns the shelf number of this collection object.
     *
     * @return the number of the shelf where this collection is located
     */
    public String getShelfNumber() {
        return this.shelfNumber;
    }

    /**
     * Changes the shelf number of this collection object.
     *
     * @param shelfNumber new shelf number
     */
    public void setShelfNumber(String shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    /**
     * Returns the call number of the collection. The call number is formed by
     * the location code of the owning library and the location code of the
     * collection.
     *
     * @return the call number of the collection
     */
    public String getCallNo() {
        String callno = "";
        if (!getLibrary().getLocationCode().isEmpty()) {
            callno = getLibrary().getLocationCode();
        }
        if (!super.getLocationCode().isEmpty()) {
            if (!callno.isEmpty()) {
                callno += " ";
            }
            callno += super.getLocationCode();
        }
        return callno;
    }

    /**
     * Returns the call number of the location in a format that is used in
     * templates' names.All the whitespaces in the call number are replaced with
     * underscores.
     *
     * @param incCollectionCode boolean value that tells if collection code
     * should be included in the returned call number. If true, collection code
     * is included only if it exists and is not empty
     * @return the call number of the location
     */
    public String getCallNoForTemplateName(boolean incCollectionCode) {
        StringBuilder builder = new StringBuilder();
        builder.append(this.library.getLocationCode()).append(" ");
        builder.append(this.locationCode);
        if (incCollectionCode && this.hasCollectionCode()) {
            builder.append(" ").append(this.collectionCode);
        }
        return builder.toString().replaceAll(" ", "_");
    }

    /**
     * Returns the main word that is included in the given call number.
     *
     * @param callno the call number that includes the main word
     * @return
     */
    public String getMainWord(String callno) {
        String result = callno;
        if (!this.library.isSubstring) {
            if (callno.startsWith(getCallNo())) {
                result = callno.replace(getCallNo(), "");
            } else if (callno.startsWith(this.getLibrary().getCallNo())) {
                result = callno.replace(this.getLibrary().getCallNo(), "");
            }
            return result.trim();
        }
        result = callno.replace(this.library.getCallNo(), "");
        return result.trim();
    }

    /**
     * Returns the location type COLLECTION.
     *
     * @return location type COLLECTION
     */
    public LocationType getLocationType() {
        return LocationType.COLLECTION;
    }
}
