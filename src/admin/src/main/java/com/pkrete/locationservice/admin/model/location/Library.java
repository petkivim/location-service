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
package com.pkrete.locationservice.admin.model.location;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pkrete.locationservice.admin.deserializers.LibraryJSONDeserializer;
import com.pkrete.locationservice.admin.model.search.LocationType;
import java.util.*;

/**
 * The <code>Library</code> class extends the {@link Location Location} class.
 *
 * The Library class represents a library that has collections. The collections
 * are represented by the {@link LibraryCollection LibraryCollection} class.
 *
 * @author Petteri Kivimäki
 */
@JsonDeserialize(using = LibraryJSONDeserializer.class)
public class Library extends Location {

    /**
     * List of the collections that belong to the library.
     */
    private List<LibraryCollection> collections;

    /**
     * Constructs and initializes a library with no location code.
     */
    public Library() {
        super("");
        this.collections = new ArrayList<LibraryCollection>();
        this.areas = new ArrayList<Area>();
        this.descriptions = new ArrayList<Description>();
        this.notes = new ArrayList<Note>();
    }

    /**
     * Construct and initializes a library with the given location id
     *
     * @param locationId id number of the library
     */
    public Library(int locationId) {
        this();
        this.setLocationId(locationId);
    }

    /**
     * Construct and initializes a library with the given location code.
     *
     * @param locationCode the location code of the library
     */
    public Library(String locationCode) {
        this();
        this.locationCode = locationCode;
    }

    /**
     * Construct and initializes a library with the given location code and
     * name.
     *
     * @param name the name of the library
     * @param locationCode the location code of the library
     */
    public Library(String locationCode, String name) {
        this(locationCode);
        this.name = name;
    }

    /**
     * Returns the collections related to the library.
     *
     * @return the collections of the library
     */
    public List<LibraryCollection> getCollections() {
        Collections.sort(this.collections);
        return this.collections;
    }

    /**
     * Changes all the collections related to the library.
     *
     * @param collections the new collections of the library
     */
    public void setCollections(List<LibraryCollection> collections) {
        this.collections = collections;
    }

    /**
     * Adds a new collection to the library.
     *
     * @param collection the new collection
     */
    public void addCollection(LibraryCollection collection) {
        this.collections.add(collection);
    }

    /**
     * Removes the specified collection from the library
     *
     * @param collection the collection to be removed from the library
     */
    public void removeCollection(LibraryCollection collection) {
        this.collections.remove(collection);
    }

    /**
     * Returns the call number of the library. The call number is the location
     * code of the library.
     *
     * @return the call number of the library.
     */
    @Override
    public String getCallNo() {
        return this.locationCode;
    }

    /**
     * Returns the call number of the location in a format that is used in
     * templates names. All the white spaces in the call number are replaced
     * with underscores.
     *
     * @param incCollectionCode Library doesn't have collection code, this
     * parameter is ignored
     * @return the call number of the location
     */
    @Override
    public String getCallNoForTemplateName(boolean incCollectionCode) {
        return this.locationCode.replaceAll(" ", "_");
    }

    /**
     * Returns the location type LIBRARY.
     *
     * @return location type LIBRARY
     */
    @Override
    public LocationType getLocationType() {
        return LocationType.LIBRARY;
    }

    /**
     * Library cannot have a collection code. Empty string is returned.
     *
     * @return empty string
     */
    @Override
    public String getCollectionCode() {
        return "";
    }
}
