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
package com.pkrete.locationservice.endpoint.service.locations;

import com.pkrete.locationservice.endpoint.dao.Dao;
import com.pkrete.locationservice.endpoint.modifier.CallnoModification;
import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.callnoparser.LocatingStrategy;
import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.model.owner.Owner;
import com.pkrete.locationservice.endpoint.statistics.SearchEvent;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import com.pkrete.locationservice.endpoint.converter.ConverterService;
import com.pkrete.locationservice.endpoint.model.location.SimpleLocation;
import com.pkrete.locationservice.endpoint.service.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * LocationService class implements the Service interface and implements
 * all the methods defined in the interface. The class offers functionality 
 * that other parts of the program use when they need to access the database. 
 * Concrete database operations are executed through {@link Dao Dao} interface. 
 * Single service method can include one or more database operations defined
 * in the Dao interface.
 *
 * @author Petteri Kivimäki
 */
public class LocationsService implements Service {

    protected ConverterService converterService;
    private Dao dao;

    /**
     * Changes the value of dao instance variable.
     * @param dao new value of dao
     */
    public void setDao(Dao dao) {
        this.dao = dao;
    }

    /**
     * Changes the value of converterService instance variable
     * @param converterService new value to be set
     */
    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    /**
     * Saves the given search event to the database. Returns true if and
     * only the the search event was succesfully added, otherwise false.
     * @param event search event to be saved
     * @return true if and only if the event was succesfully added;
     * otherwise false
     */
    public boolean save(SearchEvent event) {
        return dao.save(event);
    }

    /**
     * Returns a list of libraries which call numbers match with the given 
     * call number and that related to the given owner.
     * @param callno the call number that is used for searching
     * @param owner owner of the object
     * @return the libraries with the desired call number
     */
    public List getLibrary(String callno, String owner) {
        return dao.getLibrary(callno, owner);
    }

    /**
     * Returns the library which location id mathes the given id. All
     * the lazy relationships are loaded.
     * @param id location id to be searched
     * @return library matching the given location id
     */
    public Library getLibrary(int id) {
        return dao.getLibrary(id);
    }

    /**
     * Returns a list of libraries that are related to the given owner.
     * @param owner owner of the object
     * @return libraries that are related to the given owner
     */
    public List getLibraries(String owner) {
        return dao.getLibraries(owner);
    }

    /**
     * Returns a list of search index entries related to libraries that 
     * belong to the given owner.
     * @param owner owner of the object
     * @return search index entries that are related to the given owner
     */
    public List<SimpleLocation> getLibrariesFromIndex(String owner) {
        return (List) dao.getLibrariesFromIndex(owner);
    }

    /**
     * Returns a list of collection which call numbers match with the given 
     * call number and that related to the given owner.
     * @param callno the call number that is used for searching
     * @param owner owner of the object
     * @return the collections with the desired call number
     */
    public List getCollection(String callno, String owner) {
        return dao.getCollection(callno, owner);
    }

    /**
     * Returns a list of collections that are related to the given owner.
     * @param owner owner of the object
     * @return the collections related to the given owner
     */
    public List getCollections(String owner) {
        return dao.getCollections(owner);
    }

    /**
     * Returns a list of search index entries related to collections that 
     * belong to the given owner.
     * @param owner owner of the object
     * @return search index entries that are related to the given owner
     */
    public List<SimpleLocation> getCollectionsFromIndex(String owner) {
        return (List) dao.getCollectionsFromIndex(owner);
    }

    /**
     * Returns all the collections that are related to the library which
     * locationId matches with the given id number.
     * @param id the locationId that is used for searching
     * @param owner the owner of the object
     * @return the collection with the desired locationId
     */
    public List<SimpleLocation> getCollectionsByLibraryId(int id, String owner) {
        return (List) dao.getCollectionsByLibraryId(id, owner);
    }

    /**
     * Returns the collection which location id mathes the given id. All
     * the lazy relationships are loaded.
     * @param id location id to be searched
     * @return collection matching the given location id
     */
    public LibraryCollection getCollection(int id) {
        return dao.getCollection(id);
    }

    /**
     * Returns a list of shelves which call numbers match with the given call number
     * and that related to the given owner.
     * @param callno the call number that is used for searching
     * @param owner owner of the object
     * @return the libraries with the desired call number
     */
    public List getShelf(String callno, String owner) {
        return dao.getShelf(callno, owner);
    }

    /**
     * Returns a list of shelves that are related to the given owner.
     * @param owner owner of the object
     * @return shelves that are related to the given owner
     */
    public List getShelves(String owner) {
        return dao.getShelves(owner);
    }

    /**
     * Returns a list of search index entries related to shelves that 
     * belong to the given owner.
     * @param owner owner of the object
     * @return search index entries that are related to the given owner
     */
    public List<SimpleLocation> getShelvesFromIndex(String owner) {
        return (List) dao.getShelvesFromIndex(owner);
    }

    /**
     * Returns all the shelves that are related to the collection which
     * locationId matches with the given id number.
     * @param id the locationId that is used for searching
     * @param owner the owner of the object
     * @return the shelf with the desired locationId
     */
    public List<SimpleLocation> getShelvesByCollectionId(int id, String owner) {
        return (List) dao.getShelvesByCollectionId(id, owner);
    }

    /**
     * Returns the shelf which location id mathes the given id. All
     * the lazy relationships are loaded.
     * @param id location id to be searched
     * @return shelf matching the given location id
     */
    public Shelf getShelf(int id) {
        return dao.getShelf(id);
    }

    /**
     * Returns the location which id number matches with given id number
     * and given owner.
     * @param locationId the id number that is used for searching
     * @param owner owner of the object
     * @return the location with the desired id number
     */
    public Location getLocation(String locationId, String owner) {
        return dao.getLocation(converterService.strToInt(locationId), owner);
    }

    /**
     * Returns a list of collections which shelves' location code is a substring placed
     * in the beginning of a string.
     * @param owner owner of the location
     * @return list of locations matching the condition
     */
    public List<SimpleLocation> getSubstringLocations(String owner) {
        return (List) dao.getSubstringLocations(owner);
    }

    /**
     * Returns a list of collections which shelves' location code is a substring placed
     * in the beginning of a string.
     * @param owner owner of the location
     * @param collectionCode collection code of the location
     * @return list of locations matching the condition
     */
    public List<SimpleLocation> getSubstringLocations(String owner, String collectionCode) {
        return (List) dao.getSubstringLocations(owner, collectionCode);
    }

    /**
     * Returns a list of shelves that belong to the collection with the given
     * collection code.
     * @param owner owner of the location
     * @param collectionCode collection code of the collection in which the shelf belongs
     * @return list of shelves matching the condition
     */
    public List<SimpleLocation> getShelvesByCollectionCode(String owner, String collectionCode) {
        return (List) dao.getShelvesByCollectionCode(owner, collectionCode);
    }

    /**
     * Returns the collection with the given collection code. If the collection
     * with the given code doesn't exist, null is returned.
     * @param owner owner of the location
     * @param collectionCode collection code of the collection to be searched
     * @return collection mathing the given code or null
     */
    public LibraryCollection getCollectionByCollectionCode(String owner, String collectionCode) {
        return dao.getCollectionByCollectionCode(owner, collectionCode);
    }

    /**
     * Returns a list of all the libraries in the database that are related to
     * the given owner.
     * @param owner owner of the object
     * @return all the libraries in the database
     */
    public List<Library> getAllLocations(String owner) {
        return dao.getAllLocations(owner);
    }

    /**
     * Returns the owner with the given code.
     * @param code the code that is used for searching
     * @return the owner with the given code
     */
    public Owner getOwnerByCode(String code) {
        return dao.getOwnerByCode(code);
    }

    /**
     * Returns the locating strategy defined for the given owner. If no
     * strategy is found, null is returned.
     * @param owner owner of the object
     * @return locating strategy defined for the given owner or null, if
     * no strategy is found
     */
    public LocatingStrategy getLocatingStrategy(String owner) {
        return dao.getLocatingStrategy(owner);
    }

    /**
     * Returns a list of not found redirect objects related to
     * the given owner.
     * @param code owner of the not found redirects
     * @return list of not found redirects related to the given owner
     */
    public List<CallnoModification> getNotFoundRedirects(String owner) {
        return dao.getNotFoundRedirects(owner);
    }

    /**
     * Returns a list of preprocessing redirect objects related to
     * the given owner.
     * @param code owner of the preprocessing redirects
     * @return list of preprocessing redirects related to the given owner
     */
    public List<CallnoModification> getPreprocessingRedirects(String owner) {
        return dao.getPreprocessingRedirects(owner);
    }

    /**
     * Returns the index entry matching the given location id and owner code. If
     * no matching entry or more than one entries are found, null is returned.
     * @param locationId location id of the Location object
     * @param owner owner of the Location object
     * @return index entry or null
     */
    public SimpleLocation getIndexEntry(String locationId, String owner) {
        int id = 0;
        try {
            id = Integer.parseInt(locationId);
        } catch (NumberFormatException nfe) {
            return null;
        }
        return dao.getIndexEntry(id, owner);
    }

    /**
     * Tests that the DB connection is alive.
     * @return true if and only if the connection works; otherwise false
     */
    public boolean testDbConnection() {
        return dao.testDbConnection();
    }

    /**
     * Returns all the libraries which id is in the given list. All
     * the lazy relationships are loaded. If children is true, all the 
     * collections and shelves belonging to the libraries are loaded too.
     * @param ids list of ids
     * @param children if true, all the collections and shelves belonging to 
     * the libraries are loaded
     * @return libraries matching the given ids
     */
    /**
     * Returns all the locations which id is in the given lists. All
     * the lazy relationships are loaded. If children is true, all the 
     * collections and shelves belonging to the libraries are loaded too.
     * @param libraryIds list of library ids
     * @param collectionIds list of collection ods
     * @param shelfIds list of shelf ids
     * @param children children if true, all the collections and shelves 
     * belonging to the libraries and collections are loaded
     * @return locations matching the given ids
     */
    public List<Location> getLocations(List<Integer> libraryIds, List<Integer> collectionIds, List<Integer> shelfIds, boolean children) {
        List<Location> results = new ArrayList<Location>();
        if (libraryIds != null && !libraryIds.isEmpty()) {
            // Get libraries by id
            results.addAll(this.dao.getLibraries(libraryIds, children));
        }
        if (collectionIds != null && !collectionIds.isEmpty()) {
            // Get collections by id
            results.addAll(this.dao.getCollections(collectionIds, children));
        }
        if (shelfIds != null && !shelfIds.isEmpty()) {
            // Get shelves by id
            results.addAll(this.dao.getShelves(shelfIds));
        }
        return results;
    }
}
