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

import com.pkrete.locationservice.admin.comparator.LocationCallnoComparator;
import com.pkrete.locationservice.admin.dao.LocationsDao;
import com.pkrete.locationservice.admin.service.LocationsService;
import com.pkrete.locationservice.admin.model.location.Area;
import com.pkrete.locationservice.admin.model.location.Description;
import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.search.LocationType;
import com.pkrete.locationservice.admin.model.location.Note;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.search.SearchIndex;
import com.pkrete.locationservice.admin.model.location.Shelf;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * This class implements {@link LocationsService LocationsService} interface, 
 * which defines service layer for Location objects.
 * 
 * This class offers methods for adding, editing and removing Location
 * objects. This class doesn't support external index software.
 * 
 * @author Petteri Kivimäki
 */
public class LocationsServiceImpl implements LocationsService {

    private final static Logger logger = Logger.getLogger(LocationsServiceImpl.class.getName());
    private LocationsDao dao;
    private JSONizerService libJsonizer;
    private JSONizerService colJsonizer;
    private JSONizerService shelfJsonizer;

    /**
     * Sets the data access object.
     * @param dao new value
     */
    public void setDao(LocationsDao dao) {
        this.dao = dao;
    }

    /**
     * Sets the library JSON converter object.
     * @param jsonizer new value
     */
    public void setLibJsonizer(JSONizerService jsonizer) {
        this.libJsonizer = jsonizer;
    }

    /**
     * Sets the collection JSON converter object.
     * @param jsonizer new value
     */
    public void setColJsonizer(JSONizerService jsonizer) {
        this.colJsonizer = jsonizer;
    }

    /**
     * Sets the shelf JSON converter object.
     * @param jsonizer new value
     */
    public void setShelfJsonizer(JSONizerService jsonizer) {
        this.shelfJsonizer = jsonizer;
    }


    /**
     * Returns the library which locationId matches with the given libraryId 
     * number. This method is only for editor classes. All the other classes 
     * must give also the owner parameter.
     * @param libraryId locationId that is used for searching
     * @return library with the desired locationId or null if matching
     * library doesn't exist
     */
    public Library getLibrary(int libraryId) {
        return dao.getLibrary(libraryId);
    }

    /**
     * Returns the library which locationId matches with the given libraryId 
     * number. Initializes image, map, areas and collections objects related to
     * the library, so that the library can be edited.
     * @param libraryId locationId that is used for searching
     * @param owner the owner of the object
     * @return library with the desired locationId or null if matching
     * library doesn't exist
     */
    public Library getLibrary(int libraryId, Owner owner) {
        return dao.getLibrary(libraryId, owner);
    }

    /**
     * Returns the library which locationId matches with the given libraryId 
     * number. Initializes areas, collections and shelves related to the given
     * library object, so that the library and the objects related to it can
     * be deleted.
     * @param libraryId locationId that is used for searching
     * @param owner the owner of the object
     * @return library with the desired locationId or null if matching
     * library doesn't exist
     */
    public Library getLibraryToBeDeleted(int libraryId, Owner owner) {
        return dao.getLibraryToBeDeleted(libraryId, owner);
    }

    /**
     * Returns the collection which locationId matches with the given 
     * collectionId number. This method is only for editor classes. All the 
     * other classes must give also the owner parameter.
     * @param collectionId locationId that is used for searching
     * @return collection with the desired locationId or null if matching
     * collection doesn't exist
     */
    public LibraryCollection getCollection(int collectionId) {
        return dao.getCollection(collectionId);
    }

    /**
     * Returns the collection which locationId matches with the given 
     * collectionId number. Initializes image, map, areas, subject matters 
     * and shelves objects related to the collection, so that the collection 
     * can be edited.
     * @param collectionId locationId that is used for searching
     * @param owner the owner of the object
     * @return collection with the desired locationId or null if matching
     * collection doesn't exist
     */
    public LibraryCollection getCollection(int collectionId, Owner owner) {
        return dao.getCollection(collectionId, owner);
    }

    /**
     * Returns the collection which locationId matches with the given 
     * collectionId number and that belongs to the given library. Initializes 
     * image, map, areas, subject matters and shelves objects related to the 
     * collection, so that the collection can be edited.
     * @param collectionId locationId that is used for searching
     * @param libraryId locationId of the library that holds the collection
     * @param owner owner of the collection
     * @return collection with the desired locationId or null if matching
     * collection doesn't exist
     */
    public LibraryCollection getCollection(int collectionId, int libraryId, Owner owner) {
        return dao.getCollection(collectionId, libraryId, owner);
    }

    /**
     * Returns the collection which locationId matches with the given 
     * collectionId number. Initializes areas, subject matters and shelves 
     * related to the given collection object, so that the collection and the 
     * objects related to it can be deleted.
     * @param collectionId locationId that is used for searching
     * @param owner the owner of the object
     * @return collection with the given locationId or null if matching
     * collection doesn't exist
     */
    public LibraryCollection getCollectionToBeDeleted(int collectionId, Owner owner) {
        return dao.getCollectionToBeDeleted(collectionId, owner);
    }

    /**
     * Returns the collection which locationId matches with the given 
     * collectionId number and that belongs to the given library. Initializes 
     * areas, subject  matters and shelves related to the given collection 
     * object, so that the collection and the objects related to it can be 
     * deleted.
     * @param collectionId locationId that is used for searching
     * @param libraryId locationId of the library that holds the collection
     * @param owner owner of the collection
     * @return collection with the given locationId or null if matching
     * collection doesn't exist
     */
    public LibraryCollection getCollectionToBeDeleted(int collectionId, int libraryId, Owner owner) {
        return this.dao.getCollectionToBeDeleted(collectionId, libraryId, owner);
    }

    /**
     * Returns all the collections that are related to the library which
     * locationId matches with the given libraryId number.
     * @param libraryId locationId of the library that holds the collection
     * @param owner owner of the collection
     * @return list of collections that belong to the given library
     */
    public List<SimpleLocation> getCollectionsByLibraryId(int libraryId, Owner owner) {
        return (List)dao.getCollectionsByLibraryId(libraryId, owner);
    }

    /**
     * Returns all the collections that are owned by the given owner.
     * @param owner the owner of the collections
     * @return list of collections owned by the given owner
     */
    public List<SimpleLocation> getCollections(Owner owner) {
        return (List)dao.getCollections(owner);
    }

    /**
     * Returns the shelf which locationId matches with the given shelfId number.
     * Initializes image, map, areas and subject matters objects related to
     * the shelf, so that the shelf can be edited.
     * @param shelfId locationId that is used for searching
     * @param owner the owner of the object
     * @return the shelf with the desired locationId or null if matching
     * shelf doesn't exist
     */
    public Shelf getShelf(int shelfId, Owner owner) {
        return dao.getShelf(shelfId, owner);
    }

    /**
     * Returns the shelf which locationId matches with the given id number.
     * Initializes image, map, areas and subject matters objects related to
     * the shelf, so that the shelf can be edited.
     * @param shelfId locationId that is used for searching
     * @param collectionId locationId of the collection
     * @param libraryId locationId of the lirary
     * @param owner the owner of the object
     * @return shelf with the desired locationId or null if matching shelf
     * is not found
     */
    public Shelf getShelf(int shelfId, int collectionId, int libraryId, Owner owner) {
        return dao.getShelf(shelfId, collectionId, libraryId, owner);
    }

    /**
     * Returns all the shelves that are related to the collection which
     * locationId matches with the given collectionId number.
     * @param collectionId locationId of the collection that holds the shelves
     * @param owner the owner of the object
     * @return list of shelves that belong to the given collection
     */
    public List<SimpleLocation> getShelvesByCollectionId(int collectionId, Owner owner) {
        return (List)dao.getShelvesByCollectionId(collectionId, owner);
    }

    /**
     * Returns all the shelves that are related to the collection which
     * locationId matches with the given id number.
     * @param libraryId id of the library
     * @param collectionId id of the collection
     * @param owner the owner of the object
     * @return list of shelves that belong to the given collection
     */
    public List<SimpleLocation> getShelvesByCollectionId(int libraryId, int collectionId, Owner owner) {
        return (List)this.dao.getShelvesByCollectionId(libraryId, collectionId, owner);
    }

    /**
     * Returns the shelf which locationId matches with the given shelfId number.
     * Initializes areas and subject matters related to the given
     * shelf object, so that the shelf and the objects related to it can
     * be deleted.
     * @param shelfId the locationId that is used for searching
     * @param owner the owner of the object
     * @return shelf with the desired locationId or null if matching
     * shelf doesn't exist
     */
    public Shelf getShelfToBeDeleted(int shelfId, Owner owner) {
        return dao.getShelfToBeDeleted(shelfId, owner);
    }

    /**
     * Returns the shelf which locationId matches with the given shelfId number.
     * Initializes areas and subject matters related to the given
     * shelf object, so that the shelf and the objects related to it can
     * be deleted.
     * @param shelfId the locationId that is used for searching
     * @param collectionId locationId of the collection
     * @param libraryId locationId of the lirary
     * @param owner the owner of the object
     * @return shelf with the desired locationId or null if matching
     * shelf doesn't exist
     */
    public Shelf getShelfToBeDeleted(int shelfId, int collectionId, int libraryId, Owner owner) {
        return this.dao.getShelfToBeDeleted(shelfId, collectionId, libraryId, owner);
    }

    /**
     * Returns the location which id number matches with given id number
     * @param locationId the id number that is used for searching
     * @param owner the owner of the object
     * @return the location with the desired id number
     */
    public Location getLocation(int locationId, Owner owner) {
        return dao.getLocation(locationId, owner);
    }

    /**
     * Returns a list of all the libraries in the database that are related
     * to the given owner object.
     * @param owner the owner of the objects
     * @return all the libraries in the database
     */
    public List<SimpleLocation> getlLibraries(Owner owner) {
        return (List)dao.getAllLocations(owner);
    }

    /**
     * Returns a list of all the areas in the database that are related
     * to the given location id.
     * @param location id of the location that the areas are related to
     * @return all the areas related to the given location
     */
    public List<Area> getAreasByLocationId(int locationId) {
        return dao.getAreasByLocationId(locationId);
    }

    /**
     * Returns a Set that contains Area ids related to the given
     * Location.
     * @param location Location that owns the Descriptions
     * @return Set containing Area ids related to the given Location
     */
    public Set<Integer> getAreaIdsSet(Location location) {
        List<Integer> list = this.dao.getAreaIds(location);
        Set<Integer> ids = new HashSet<Integer>();
        for (Integer id : list) {
            ids.add(id);
        }
        return ids;
    }

    /**
     * Returns a Set that contains Description ids related to the given
     * Location.
     * @param location Location that owns the Descriptions
     * @return Set containing Description ids related to the given Location
     */
    public Set<Integer> getDescriptionIdsSet(Location location) {
        List<Integer> list = this.dao.getDescriptionIds(location);
        Set<Integer> ids = new HashSet<Integer>();
        for (Integer id : list) {
            ids.add(id);
        }
        return ids;
    }

    /**
     * Returns a Set that contains Note ids related to the given
     * Location.
     * @param location Location that owns the Notes
     * @return Set containing Note ids related to the given Location
     */
    public Set<Integer> getNoteIdsSet(Location location) {
        List<Integer> list = this.dao.getNoteIds(location);
        Set<Integer> ids = new HashSet<Integer>();
        for (Integer id : list) {
            ids.add(id);
        }
        return ids;
    }

    /**
     * Adds the given library object to the database and updates search index.
     * @param library the library to be added
     * @return true if and only if the library was succesfully created;
     * otherwise false
     */
    public boolean create(Library library) {
        // Remove empty descriptions and notes
        this.checkDescriptions(library);
        this.checkNotes(library);
        // Set creation date
        library.setCreated(new Date());
        // Add Library to the database
        if (dao.create(library)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Library created : ").append(this.libJsonizer.jsonize(library, true)));
            }
            // Create index entry
            if (!dao.save(new SearchIndex(library.getLocationId(), LocationType.LIBRARY, library.getCallNo(), library.getLocationCode(), library.getOwner()))) {
                logger.error("Creating index entry failed!");
            }
            // Add to external index
            this.addToIndex(library);
            return true;
        }
        return false;
    }

    /**
     * Updatess the given library object to the database and updates search index.
     * @param library the library to be updated
     * @return true if and only if the library was succesfully updated;
     * otherwise false
     */
    public boolean update(Library library) {
        // Remove unused descriptions, notes and areas, and get ids that must be deleted
        List<Integer> descriptionIds = this.checkDescriptions(library);
        List<Integer> noteIds = this.checkNotes(library);
        List<Integer> areaIds = this.checkAreas(library);

        // Set updated date
        library.setUpdated(new Date());
        // Update Library to the database
        if (!dao.update(library)) {
            logger.info(new StringBuilder("Failed to update library : ").append(this.libJsonizer.jsonize(library, true)));
            return false;
        }

        // Delete unused Descriptions, if exist
        if (!descriptionIds.isEmpty() && !this.dao.deleteDescriptions(descriptionIds)) {
            logger.warn("Failed to delete unused Descriptions!");
        }

        // Delete unused Notes, if exist
        if (!noteIds.isEmpty() && !this.dao.deleteNotes(noteIds)) {
            logger.warn("Failed to delete unused Notes!");
        }

        // Delete unused Areas, if exist
        if (!areaIds.isEmpty() && !this.dao.deleteAreas(areaIds)) {
            logger.warn("Failed to delete unused Areas!");
        }

        if (logger.isInfoEnabled()) {
            logger.info(new StringBuilder("Library updated : ").append(this.libJsonizer.jsonize(library, true)));
        }

        // Update to external index
        this.updateToIndex(library);

        // Get the search index related to this library
        List<SearchIndex> indexes = dao.findSearchIndexes(library.getLocationId());
        // Index needs to be updated only if the location code has changed
        if (indexes.size() == 1 && !indexes.get(0).getLocationCode().equals(library.getLocationCode())) {
            if (logger.isDebugEnabled()) {
                logger.debug("Library's location code changed -> update index : \"" + indexes.get(0).getLocationCode() + "\" -> \"" + library.getLocationCode() + "\"");
            }
            SearchIndex index = indexes.get(0);
            // Update location code
            index.setLocationCode(library.getLocationCode());
            // Update call number
            index.setCallNumber(library.getCallNo());
            // Update index
            if (!dao.save(index)) {
                logger.error(new StringBuilder("Updating index entry failed! Index id : ").append(index.getId()));
            }
            // Update collections' indexes, because their call number has
            // changed too
            for (LibraryCollection col : dao.getCollectionsForIndexUpdate(library.getLocationId())) {
                if (col.getSearchIndexes().size() == 1) {
                    SearchIndex temp = col.getSearchIndexes().get(0);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Update collection's call number : \"" + temp.getCallNumber() + "\" -> \"" + col.getCallNo() + "\"");
                    }
                    temp.setCallNumber(col.getCallNo());
                    if (!dao.save(temp)) {
                        logger.error(new StringBuilder("Updating index entry failed! Index id : ").append(temp.getId()));
                    }
                }
                // Update external index
                this.updateToIndex(col);
                // Update shelves' indexes, because their call number has
                // changed too
                for (Shelf shelf : col.getShelves()) {
                    if (shelf.getSearchIndexes().size() == 1) {
                        SearchIndex temp = shelf.getSearchIndexes().get(0);
                        if (logger.isDebugEnabled()) {
                            logger.debug("Update shelf's call number : \"" + temp.getCallNumber() + "\" -> \"" + shelf.getCallNo() + "\"");
                        }
                        temp.setCallNumber(shelf.getCallNo());
                        if (!dao.save(temp)) {
                            logger.error(new StringBuilder("Updating index entry failed! Index id : ").append(temp.getId()));
                        }
                    }
                    // Update external index
                    this.updateToIndex(shelf);
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Done! Index is up-to-date.");
            }
        }
        return true;
    }

    /**
     * Adds the given collection object to the database and updates search 
     * index.
     * @param collection the collection to be added
     * @return true if and only if the collection was succesfully created;
     * otherwise false
     */
    public boolean create(LibraryCollection collection) {
        // Remove empty descriptions and notes
        this.checkDescriptions(collection);
        this.checkNotes(collection);
        // Set creation date
        collection.setCreated(new Date());
        // Add collection to the database
        if (dao.create(collection)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Collection created : ").append(this.colJsonizer.jsonize(collection, true)));
            }
            // Create index entry
            if (!dao.save(new SearchIndex(collection.getLocationId(), LocationType.COLLECTION, collection.getCallNo(), collection.getLocationCode(), collection.getOwner(), collection.getCollectionCode()))) {
                logger.error("Creating index entry failed!");
            }
            // Add to external index
            this.addToIndex(collection);
            return true;
        }
        return false;
    }

    /**
     * Updates the given collection object to the database and updates search 
     * index.
     * @param collection the collection to be updated
     * @return true if and only if the collection was succesfully updated;
     * otherwise false
     */
    public boolean update(LibraryCollection collection) {
        // Remove unused descriptions, notes and areas, and get ids that must be deleted
        List<Integer> descriptionIds = this.checkDescriptions(collection);
        List<Integer> noteIds = this.checkNotes(collection);
        List<Integer> areaIds = this.checkAreas(collection);

        // Set updated date
        collection.setUpdated(new Date());
        // Update collection to the database
        if (!dao.update(collection)) {
            logger.info(new StringBuilder("Failed to update collection : ").append(this.colJsonizer.jsonize(collection, true)));
            return false;
        }

        // Delete unused Descriptions, if exist
        if (!descriptionIds.isEmpty() && !this.dao.deleteDescriptions(descriptionIds)) {
            logger.warn("Failed to delete unused Descriptions!");
        }

        // Delete unused Notes, if exist
        if (!noteIds.isEmpty() && !this.dao.deleteNotes(noteIds)) {
            logger.warn("Failed to delete unused Notes!");
        }

        // Delete unused Areas, if exist
        if (!areaIds.isEmpty() && !this.dao.deleteAreas(areaIds)) {
            logger.warn("Failed to delete unused Areas!");
        }

        if (logger.isInfoEnabled()) {
            logger.info(new StringBuilder("Collection updated : ").append(this.colJsonizer.jsonize(collection, true)));
        }

        // Update external index
        this.updateToIndex(collection);

        // Get the search index related to this collection
        List<SearchIndex> indexes = dao.findSearchIndexes(collection.getLocationId());
        // Index needs to be updated if call number or collection code has changed
        if (indexes.size() == 1 && (!indexes.get(0).getCallNumber().equals(collection.getCallNo()) || !indexes.get(0).getCollectionCode().equals(collection.getCollectionCode()))) {
            // Get index entry
            SearchIndex index = indexes.get(0);
            if (logger.isDebugEnabled()) {
                logger.debug("Collection's location code, collection code and/or call number have changed -> update index.");
                logger.debug("Location code : \"" + indexes.get(0).getLocationCode() + "\" -> \"" + collection.getLocationCode() + "\"");
                logger.debug("Collection code : \"" + indexes.get(0).getCollectionCode() + "\" -> \"" + collection.getCollectionCode() + "\"");
                logger.debug("Call number : \"" + indexes.get(0).getCallNumber() + "\" -> \"" + collection.getCallNo() + "\"");
            }
            // Update location code
            index.setLocationCode(collection.getLocationCode());
            // Update collection code
            index.setCollectionCode(collection.getCollectionCode());
            // Update call number
            index.setCallNumber(collection.getCallNo());
            // Save changes to DB
            if (!dao.save(index)) {
                logger.error(new StringBuilder("Updating index entry failed! Index id : ").append(index.getId()));
            }
            // Update shelves' indexes, because their call number and/or
            // collection code changed too
            for (Shelf shelf : dao.getShelvesForIndexUpdate(collection.getLocationId())) {
                if (shelf.getSearchIndexes().size() == 1) {
                    SearchIndex temp = shelf.getSearchIndexes().get(0);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Update shelf. Call number : \"" + temp.getCallNumber() + "\" -> \"" + shelf.getCallNo() + "\", collection code : \"" + temp.getCollectionCode() + "\" -> \"" + collection.getCollectionCode() + "\"");
                    }
                    temp.setCallNumber(shelf.getCallNo());
                    temp.setCollectionCode(collection.getCollectionCode());
                    if (!dao.save(temp)) {
                        logger.error(new StringBuilder("Updating index entry failed! Index id : ").append(temp.getId()));
                    }
                }
                // Update external index
                this.updateToIndex(shelf);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Done! Index is up-to-date.");
            }
        }
        return true;
    }

    /**
     * Adds the given shelf object to the database and updates search index. 
     * @param shelf the shelf to be added
     */
    public boolean create(Shelf shelf) {
        // Remove empty descriptions and notes
        this.checkDescriptions(shelf);
        this.checkNotes(shelf);
        // Set creation date
        shelf.setCreated(new Date());
        // Add shelf to the database
        if (dao.create(shelf)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Shelf created : ").append(this.shelfJsonizer.jsonize(shelf, true)));
            }
            // Load collection object with all the lazy associations loaded
            shelf.setCollection(dao.getCollection(shelf.getCollection().getLocationId(), shelf.getOwner()));
            // Create index entry
            if (!dao.save(new SearchIndex(shelf.getLocationId(), LocationType.SHELF, shelf.getCallNo(), shelf.getLocationCode(), shelf.getOwner(), shelf.getCollection().getCollectionCode()))) {
                logger.error("Creating index entry failed!");
            }
            // Add to external index
            this.addToIndex(shelf);
            return true;
        }
        return false;
    }

    /**
     * Updates the given shelf object to the database and updates search index. 
     * @param shelf the shelf to be updated
     */
    public boolean update(Shelf shelf) {
        // Remove unused descriptions, notes and areas, and get ids that must be deleted
        List<Integer> descriptionIds = this.checkDescriptions(shelf);
        List<Integer> noteIds = this.checkNotes(shelf);
        List<Integer> areaIds = this.checkAreas(shelf);

        // Set updated date
        shelf.setUpdated(new Date());
        // Update shelf to the database
        if (!dao.update(shelf)) {
            logger.info(new StringBuilder("Failed to update shelf : ").append(this.shelfJsonizer.jsonize(shelf, true)));
            return false;
        }

        // Delete unused Descriptions, if exist
        if (!descriptionIds.isEmpty() && !this.dao.deleteDescriptions(descriptionIds)) {
            logger.warn("Failed to delete unused Descriptions!");
        }

        // Delete unused Notes, if exist
        if (!noteIds.isEmpty() && !this.dao.deleteNotes(noteIds)) {
            logger.warn("Failed to delete unused Notes!");
        }

        // Delete unused Areas, if exist
        if (!areaIds.isEmpty() && !this.dao.deleteAreas(areaIds)) {
            logger.warn("Failed to delete unused Areas!");
        }

        // Get Collection object, it's needed because we need to compare call numbers
        LibraryCollection collection = dao.getCollection(shelf.getCollection().getLocationId(), shelf.getOwner());
        // Set the collection object
        shelf.setCollection(collection);

        if (logger.isInfoEnabled()) {
            logger.info(new StringBuilder("Shelf updated : ").append(this.shelfJsonizer.jsonize(shelf, true)));
        }

        // Update external index
        this.updateToIndex(shelf);

        // Get the search index related to this shelf
        List<SearchIndex> indexes = dao.findSearchIndexes(shelf.getLocationId());
        // Index needs to be updated only if the location code and/or call number have changed
        if (indexes.size() == 1 && !indexes.get(0).getCallNumber().equals(shelf.getCallNo())) {
            // Load collection object with all the lazy associations loaded
            shelf.setCollection(dao.getCollection(shelf.getCollection().getLocationId(), shelf.getOwner()));
            // Get index entry
            SearchIndex index = indexes.get(0);
            if (logger.isDebugEnabled()) {
                logger.debug("Shelf's location code and/or call number have changed -> update index.");
                logger.debug("Location code : \"" + indexes.get(0).getLocationCode() + "\" -> \"" + shelf.getLocationCode() + "\"");
                logger.debug("Call number : \"" + indexes.get(0).getCallNumber() + "\" -> \"" + shelf.getCallNo() + "\"");
            }
            // Update location code
            index.setLocationCode(shelf.getLocationCode());
            // Update call number
            index.setCallNumber(shelf.getCallNo());
            // Save changes to DB
            if (!dao.save(index)) {
                logger.error(new StringBuilder("Updating index entry failed! Index id : ").append(index.getId()));
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Done! Index is up-to-date.");
            }
        }
        return true;
    }

    /**
     * Deletes the given library object from the database. All the collections and shelves
     * attached to the library will be deleted as well.
     * @param library the library to be deleted
     * @return true if and only if the library was succesfully deleted;
     * otherwise false
     */
    public boolean delete(Library library) {
        // JSON presentation of the Library object
        String json = this.libJsonizer.jsonize(library, true);
        if (dao.delete(library)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Library deleted : ").append(json));
            }
            // Delete from external index
            this.deleteFromIndex(library);
            return true;
        }
        logger.warn(new StringBuilder("Failed to delete library : ").append(json));
        return false;
    }

    /**
     * Deletes the given location object from the database. All the shelves
     * attached to the collection will be deleted as well.
     * @param collection the collection to be deleted
     * @return true if and only if the collection was succesfully deleted;
     * otherwise false
     */
    public boolean delete(LibraryCollection collection) {
        // JSON presentation of the LibraryCollection object
        String json = this.colJsonizer.jsonize(collection, true);
        if (dao.delete(collection)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Collection deleted : ").append(json));
            }
            // Delete from external index
            this.deleteFromIndex(collection);
            return true;
        }
        logger.warn(new StringBuilder("Failed to delete collection : ").append(json));
        return false;
    }

    /**
     * Deletes the given shelf object from the database.
     * @param shelf the shelf to be deleted
     */
    public boolean delete(Shelf shelf) {
        // JSON presentation of the Shelf object
        String json = this.shelfJsonizer.jsonize(shelf, true);
        if (dao.delete(shelf)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Shelf deleted : ").append(json));
            }
            // Delete from external index
            this.deleteFromIndex(shelf);
            return true;
        }
        logger.warn(new StringBuilder("Failed to delete shelf : ").append(json));
        return false;
    }

    /**
     * Recreates search index. Removes all the entries first and
     * then regenerates all the search indexes.
     */
    public void recreateSearchIndex() {
        logger.info("Recreate locations search index.");
        dao.clearSearchIndex();
        this.deleteAllFromIndex();
        for (Library library : dao.getAllLibraries()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Create index entry for library. Id : " + library.getLocationId());
            }
            if (!dao.save(new SearchIndex(library.getLocationId(), LocationType.LIBRARY, library.getCallNo(), library.getLocationCode(), library.getOwner()))) {
                logger.error("Creating index entry for library failed! Id : " + library.getLocationId());
            }
            this.addToIndex(library);
            for (LibraryCollection collection : library.getCollections()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Create index entry for collection. Library id : " + library.getLocationId() + ", collection id : " + collection.getLocationId());
                }
                if (!dao.save(new SearchIndex(collection.getLocationId(), LocationType.COLLECTION, collection.getCallNo(), collection.getLocationCode(), library.getOwner(), collection.getCollectionCode()))) {
                    logger.error("Creating index entry for collection failed! Id : " + collection.getLocationId());
                }
                this.addToIndex(collection);
                for (Shelf shelf : collection.getShelves()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Create index entry for shelf. Library id : " + library.getLocationId() + ", collection id : " + collection.getLocationId() + ", shelf id : " + shelf.getLocationId());
                    }
                    if (!dao.save(new SearchIndex(shelf.getLocationId(), LocationType.SHELF, shelf.getCallNo(), shelf.getLocationCode(), library.getOwner(), collection.getCollectionCode()))) {
                        logger.error("Creating index entry for shelf failed! Id : " + shelf.getLocationId());
                    }
                    this.addToIndex(shelf);
                }
            }
        }
        logger.info("Indexing locations done.");
    }

    /**
     * Returns the search index entry of the Location with the given
     * location id. If no matching the given id is found, null is returned.
     * @param locationId id of the location object
     * @return search index entry of the location with the given id or null,
     * if matching entry is not found
     */
    public SearchIndex getIndexEntry(int locationId) {
        List<SearchIndex> indexes = dao.findSearchIndexes(locationId);
        if (indexes.isEmpty()) {
            return null;
        }
        return indexes.get(0);
    }

    /**
     * Returns a list of all the locations in the database that are related to
     * the given owner. All the collections and shelves are loaded. The results
     * are sorted by call numer.
     * @param owner owner of the object
     * @return all the libraries in the database
     */
    public List<SimpleLocation> getLocations(Owner owner) {
        List result = dao.getAllLocationsWithDependecies(owner);
        Collections.sort(result, new LocationCallnoComparator());
        return result;
    }

    /**
     * Returns the id of the library in which the collection with the given
     * id belongs to.
     * @param collectionId id of the collection
     * @return id of the library or zero if no library is found
     */
    public int getLibraryId(String collectionId) {
        int id = 0;
        try {
            id = Integer.parseInt(collectionId);
        } catch (NumberFormatException nfe) {
            return 0;
        }
        return this.dao.getLibraryId(id);
    }

    /**
     * Returns the id of the collection in which the shelf with the given
     * id belongs to.
     * @param shelfId id of the shelf
     * @return id of the collection or zero if no library is found
     */
    public int getCollectionId(String shelfId) {
        int id = 0;
        try {
            id = Integer.parseInt(shelfId);
        } catch (NumberFormatException nfe) {
            return 0;
        }
        return this.dao.getCollectionId(id);
    }

    /**
     * Checks the Descriptions related to the given Location and returns a list
     * of Description ids that must be deleted from the DB. Descriptions can
     * be deleted because they are empty or because the association between
     * the Description and the Location does not exist anymore. Removes the
     * association between new empty Description objects and the Location too.
     * @param location Location object
     * @return list of Description ids that should be deleted from the db
     */
    protected List<Integer> checkDescriptions(Location location) {
        // Temp Set for unique Description ids that should be deleted from the db
        Set<Integer> deleteDb = new HashSet<Integer>();
        // List for Description objects that should be deleted
        List<Description> deleted = new ArrayList<Description>();
        // List of existing Description ids related to the Location
        Set<Integer> ids = null;
        // Existing ids need to be loaded only if the Location is not new
        if (location.getLocationId() > 0) {
            ids = this.getDescriptionIdsSet(location);
        }

        // Go through all the Descriptions and collect all the empty ones.
        for (Description desc : location.getDescriptions()) {
            // Collect all the empty and null values
            if (desc.getDescription() == null || desc.getDescription().isEmpty()) {
                deleted.add(desc);
            }
            // This check must be done only if the Location is not new
            if (location.getLocationId() > 0 && ids.contains(desc.getId())) {
                // Remove the id from the existing ids list. After this loop
                // the list contains only ids that do not exist anymore and
                // can be removed
                ids.remove(desc.getId());
            }
        }

        // Remove all the Descriptions to be deleted from the Location object
        for (Description desc : deleted) {
            location.removeDescription(desc);
            // If the id is greater than zero Description object exists in
            // the database and must be removed.
            if (desc.getId() > 0) {
                deleteDb.add(desc.getId());
            }
        }

        // If the Location is new, no need to continue
        if (location.getLocationId() == 0) {
            return new ArrayList<Integer>();
        }

        // Add all the remaning existing ids to the delete from the db list
        deleteDb.addAll(ids);

        // Return a list of ids that must be deleted
        return new ArrayList<Integer>(deleteDb);
    }

    /**
     * Checks the Notes related to the given Location and returns a list
     * of Note ids that must be deleted from the DB. Notes can
     * be deleted because they are empty or because the association between
     * the Note and the Location does not exist anymore. Removes the
     * association between new empty Note objects and the Location too.
     * @param location Location object
     * @return list of Note ids that should be deleted from the db
     */
    protected List<Integer> checkNotes(Location location) {
        // Temp Set for unique Note ids that should be deleted from the db
        Set<Integer> deleteDb = new HashSet<Integer>();
        // List for Note objects that should be deleted
        List<Note> deleted = new ArrayList<Note>();
        // List of existing Note ids related to the Location
        Set<Integer> ids = null;
        // Existing ids need to be loaded only if the Location is not new
        if (location.getLocationId() > 0) {
            ids = this.getNoteIdsSet(location);
        }

        // Go through all the Notes and collect all the empty ones.
        for (Note note : location.getNotes()) {
            // Collect all the empty and null values
            if (note.getNote() == null || note.getNote().isEmpty()) {
                deleted.add(note);
            }
            // This check must be done only if the Location is not new
            if (location.getLocationId() > 0 && ids.contains(note.getId())) {
                // Remove the id from the existing ids list. After this loop
                // the list contains only ids that do not exist anymore and
                // can be removed
                ids.remove(note.getId());
            }
        }

        // Remove all the Note to be deleted from the Location object
        for (Note note : deleted) {
            location.removeNote(note);
            // If the id is greater than zero Note object exists in
            // the database and must be removed.
            if (note.getId() > 0) {
                deleteDb.add(note.getId());
            }
        }

        // If the Location is new, no need to continue
        if (location.getLocationId() == 0) {
            return new ArrayList<Integer>();
        }

        // Add all the remaning existing ids to the delete from the db list
        deleteDb.addAll(ids);

        // Return a list of ids that must be deleted
        return new ArrayList<Integer>(deleteDb);
    }

    /**
     * Checks the Areas related to the given Location and returns a list
     * of Area ids that must be deleted from the DB. Areas are deleted
     * because the association between the Area and the Location does not 
     * exist anymore. 
     * @param location Location object
     * @return list of Area ids that should be deleted from the db
     */
    protected List<Integer> checkAreas(Location location) {
        // List of existing Area ids related to the Location
        Set<Integer> ids = null;
        // Existing ids need to be loaded only if the Location is not new
        if (location.getLocationId() > 0) {
            ids = this.getAreaIdsSet(location);
        } else {
            return new ArrayList<Integer>();
        }

        // Go through all the Areas
        for (Area area : location.getAreas()) {
            if (ids.contains(area.getAreaId())) {
                // Remove the id from the existing ids list. After this loop
                // the list contains only ids that do not exist anymore and
                // can be removed
                ids.remove(area.getAreaId());
            }
            // Set Location
            area.setLocation(location);
        }
        // Return a list of ids that must be deleted
        return new ArrayList<Integer>(ids);
    }

    /**
     * Can be used for adding Locations to external index. Not implemented.
     * @param location Location to be added
     * @return always true, not implemented
     */
    protected boolean addToIndex(Location location) {
        return true;
    }

    /**
     * Can be used for updating Locations to external index. Not implemented.
     * @param location Location to be updated
     * @return always true, not implemented
     */
    protected boolean updateToIndex(Location location) {
        return true;
    }

    /**
     * Can be used for deleting Locations from external index. Not implemented.
     * @param location Location to be deleted
     * @return always true, not implemented
     */
    protected boolean deleteFromIndex(Location location) {
        return true;
    }

    /**
     * Deletes all the entries from external index. Not implemented
     * @return always true, not implemented
     */
    protected boolean deleteAllFromIndex() {
        return true;
    }
}
