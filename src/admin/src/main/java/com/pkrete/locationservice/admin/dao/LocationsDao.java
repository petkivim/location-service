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
package com.pkrete.locationservice.admin.dao;

import com.pkrete.locationservice.admin.model.location.Area;
import com.pkrete.locationservice.admin.model.location.Description;
import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.location.Note;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.search.SearchIndex;
import com.pkrete.locationservice.admin.model.location.Shelf;
import java.util.List;

/**
 * This interface defines data access layer for Library, LibraryCollection and
 * Shelf objects. All the classes implementing this interface must implement 
 * all the methods defined here.
 * 
 * @author Petteri Kivimäki
 */
public interface LocationsDao {

    public Library getLibrary(int libraryId);

    public Library getLibrary(int libraryId, Owner owner);

    public Library getLibraryToBeDeleted(int libraryId, Owner owner);

    public LibraryCollection getCollection(int collectionId);

    public LibraryCollection getCollection(int collectionId, Owner owner);

    public LibraryCollection getCollection(int collectionId, int libraryId, Owner owner);

    public LibraryCollection getCollectionToBeDeleted(int collectionId, Owner owner);

    public LibraryCollection getCollectionToBeDeleted(int collectionId, int libraryId, Owner owner);

    public List<LibraryCollection> getCollectionsByLibraryId(int libraryId, Owner owner);

    public List<LibraryCollection> getCollections(Owner owner);

    public Shelf getShelf(int shelfId, Owner owner);

    public Shelf getShelf(int shelfId, int collectionId, int libraryId, Owner owner);

    public List<Shelf> getShelvesByCollectionId(int collectionId, Owner owner);

    public List<Shelf> getShelvesByCollectionId(int libraryId, int collectionId, Owner owner);

    public Shelf getShelfToBeDeleted(int shelfId, Owner owner);

    public Shelf getShelfToBeDeleted(int shelfId, int collectionId, int libraryId, Owner owner);

    public Location getLocation(int locationId, Owner owner);

    public List<Library> getAllLocations(Owner owner);

    public List<Area> getAreasByLocationId(int locationId);

    public boolean create(Library library);

    public boolean update(Library library);

    public boolean create(LibraryCollection collection);

    public boolean update(LibraryCollection collection);

    public boolean create(Shelf shelf);

    public boolean update(Shelf shelf);

    public boolean delete(Library library);

    public boolean delete(LibraryCollection collection);

    public boolean delete(Shelf shelf);

    public boolean deleteDescriptions(List<Integer> ids);

    public boolean deleteNotes(List<Integer> ids);

    public boolean deleteAreas(List<Integer> ids);

    public boolean save(SearchIndex index);

    public List<SearchIndex> findSearchIndexes(int locationId);

    public List<LibraryCollection> getCollectionsForIndexUpdate(int locationId);

    public List<Shelf> getShelvesForIndexUpdate(int locationId);

    public boolean clearSearchIndex();

    public List<Library> getAllLibraries();

    public List<Location> getAllLocationsWithDependecies(Owner owner);

    public int getLibraryId(int collectionId);

    public int getCollectionId(int shelfId);

    public List<Integer> getAreaIds(Location location);

    public List<Integer> getDescriptionIds(Location location);

    public List<Integer> getNoteIds(Location location);
}
