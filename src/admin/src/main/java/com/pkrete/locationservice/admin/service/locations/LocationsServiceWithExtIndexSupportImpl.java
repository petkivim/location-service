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
package com.pkrete.locationservice.admin.service.locations;

import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.search.SearchLevel;
import com.pkrete.locationservice.admin.solr.model.LocationDocument;
import com.pkrete.locationservice.admin.solr.repository.RepositoryConstants;
import com.pkrete.locationservice.admin.solr.service.LocationIndexService;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * This class extends {@link LocationsServiceImpl LocationsServiceImpl} class
 * which implements {@link LocationsService LocationsService} interface, 
 * that defines service layer for Location objects.
 * 
 * This class offers methods for adding, editing and removing Location
 * objects. This class overrides methods that define operations with an 
 * external index software and external index software is supported.
 * 
 * @author Petteri Kivimäki
 */
public class LocationsServiceWithExtIndexSupportImpl extends LocationsServiceImpl {

    private final static Logger logger = Logger.getLogger(LocationsServiceWithExtIndexSupportImpl.class.getName());
    private LocationIndexService locationIndexService;

    /**
     * Sets the location index service object.
     * @param locationIndexService new value
     */
    public void setLocationIndexService(LocationIndexService locationIndexService) {
        this.locationIndexService = locationIndexService;
    }

    /**
     * Adds the Location to external index. Returns true if and only if the 
     * Location was succesfully added, otherwise false.
     * @param location Location to be added
     * @return true if and only if the Location was succesfully added, 
     * otherwise false
     */
    @Override
    protected boolean addToIndex(Location location) {
        if (this.locationIndexService.save(location)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Adding location to external index succeeded. Id : " + location.getLocationId());
            }
            return true;
        }
        logger.error("Adding location to external index failed. Id : " + location.getLocationId());
        return false;
    }

    /**
     * Updated the Location to external index. Returns true if and only if the 
     * Location was succesfully updated, otherwise false.
     * @param location Location to be updated
     * @return true if and only if the Location was succesfully updated, 
     * otherwise false
     */
    @Override
    protected boolean updateToIndex(Location location) {
        if (this.locationIndexService.save(location)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Updating location to external index succeeded. Id : " + location.getLocationId());
            }
            return true;
        }
        logger.error("Updating location to external index failed. Id : " + location.getLocationId());
        return false;
    }

    /**
     * Deletes the Location from external index. Returns true if and only if the 
     * Location was succesfully deleted, otherwise false.
     * @param location Location to be deleted
     * @return true if and only if the Location was succesfully deleted, 
     * otherwise false
     */
    @Override
    protected boolean deleteFromIndex(Location location) {
        if (this.locationIndexService.delete(location)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Deleting location from external index succeeded. Id : " + location.getLocationId());
            }
            return true;
        }
        logger.error("Deleting location from external index failed. Id : " + location.getLocationId());
        return false;
    }

    /**
     * Deletes all the Locations from external index.
     * @return true if and only if all the Locations were succesfully deleted, 
     * otherwise false
     */
    @Override
    protected boolean deleteAllFromIndex() {
        if (this.locationIndexService.deleteAll()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Deleting all the locations from external index succeeded. ");
            }
            return true;
        }
        logger.error("Deleting all the locations from external index failed. ");
        return false;
    }

    /**
     * Returns a list of all the libraries in the database that are related
     * to the given owner object.
     * @param owner the owner of the objects
     * @return all the libraries in the database
     */
    @Override
    public List<SimpleLocation> getlLibraries(Owner owner) {
        return this.locationIndexService.search(owner.getId(), SearchLevel.LIBRARY, RepositoryConstants.SORT_ASC, RepositoryConstants.FIELD_NAME);
    }

    /**
     * Returns all the collections that are related to the library which
     * locationId matches with the given libraryId number.
     * @param libraryId locationId of the library that holds the collection
     * @param owner owner of the collection
     * @return list of collections that belong to the given library
     */
    @Override
    public List<SimpleLocation> getCollectionsByLibraryId(int libraryId, Owner owner) {
        return this.locationIndexService.search(owner.getId(), libraryId, SearchLevel.COLLECTION, RepositoryConstants.SORT_ASC, RepositoryConstants.FIELD_NAME);
    }

    /**
     * Returns all the collections that are owned by the given owner.
     * @param owner the owner of the collections
     * @return list of collections owned by the given owner
     */
    @Override
    public List<SimpleLocation> getCollections(Owner owner) {
        return this.locationIndexService.search(owner.getId(), SearchLevel.COLLECTION, RepositoryConstants.SORT_ASC, RepositoryConstants.FIELD_NAME);
    }

    /**
     * Returns all the shelves that are related to the collection which
     * locationId matches with the given collectionId number.
     * @param collectionId locationId of the collection that holds the shelves
     * @param owner the owner of the object
     * @return list of shelves that belong to the given collection
     */
    @Override
    public List<SimpleLocation> getShelvesByCollectionId(int collectionId, Owner owner) {
        return this.locationIndexService.search(owner.getId(), collectionId, SearchLevel.SHELF, RepositoryConstants.SORT_ASC, RepositoryConstants.FIELD_NAME);
    }

    /**
     * Returns all the shelves that are related to the collection which
     * locationId matches with the given id number.
     * @param libraryId id of the library
     * @param collectionId id of the collection
     * @param owner the owner of the object
     * @return list of shelves that belong to the given collection
     */
    @Override
    public List<SimpleLocation> getShelvesByCollectionId(int libraryId, int collectionId, Owner owner) {
        return this.locationIndexService.search(owner.getId(), collectionId, libraryId, RepositoryConstants.SORT_ASC, RepositoryConstants.FIELD_NAME);
    }

    /**
     * Returns a list of all the locations that are related to
     * the given owner. All the collections and shelves are loaded. The results
     * are sorted by call numer.
     * @param owner owner of the object
     * @return all the libraries in the database
     */
    @Override
    public List<SimpleLocation> getLocations(Owner owner) {
        return this.locationIndexService.search(owner.getId(), SearchLevel.ALL, RepositoryConstants.SORT_ASC, RepositoryConstants.FIELD_CALL_NUMBER);
    }

    /**
     * Returns the id of the library in which the collection with the given
     * id belongs to.
     * @param collectionId id of the collection
     * @return id of the library or zero if no library is found
     */
    @Override
    public int getLibraryId(String collectionId) {
        int id = 0;
        try {
            id = Integer.parseInt(collectionId);
        } catch (NumberFormatException nfe) {
            return 0;
        }
        List<SimpleLocation> list = this.locationIndexService.search(id, SearchLevel.COLLECTION);
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return ((LocationDocument)list.get(0)).getParentId();
    }

    /**
     * Returns the id of the collection in which the shelf with the given
     * id belongs to.
     * @param shelfId id of the shelf
     * @return id of the collection or zero if no library is found
     */
    @Override
    public int getCollectionId(String shelfId) {
        int id = 0;
        try {
            id = Integer.parseInt(shelfId);
        } catch (NumberFormatException nfe) {
            return 0;
        }
        List<SimpleLocation> list = this.locationIndexService.search(id, SearchLevel.SHELF);
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return ((LocationDocument)list.get(0)).getParentId();
    }
}
