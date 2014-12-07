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
package com.pkrete.locationservice.admin.dao;

import com.pkrete.locationservice.admin.model.location.Area;
import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.search.SearchIndex;
import com.pkrete.locationservice.admin.model.location.Shelf;
import java.util.List;

/**
 * This interface defines data access layer for Library, LibraryCollection and
 * Shelf objects. All the classes implementing this interface must implement all
 * the methods defined here.
 *
 * @author Petteri Kivimäki
 */
public interface LocationsDao {

    Library getLibrary(int libraryId);

    Library getLibrary(int libraryId, Owner owner);

    Library getLibraryToBeDeleted(int libraryId, Owner owner);

    LibraryCollection getCollection(int collectionId);

    LibraryCollection getCollection(int collectionId, Owner owner);

    LibraryCollection getCollection(int collectionId, int libraryId, Owner owner);

    LibraryCollection getCollectionToBeDeleted(int collectionId, Owner owner);

    LibraryCollection getCollectionToBeDeleted(int collectionId, int libraryId, Owner owner);

    List<LibraryCollection> getCollectionsByLibraryId(int libraryId, Owner owner);

    List<LibraryCollection> getCollections(Owner owner);

    Shelf getShelf(int shelfId, Owner owner);

    Shelf getShelf(int shelfId, int collectionId, int libraryId, Owner owner);

    List<Shelf> getShelvesByCollectionId(int collectionId, Owner owner);

    List<Shelf> getShelvesByCollectionId(int libraryId, int collectionId, Owner owner);

    Shelf getShelfToBeDeleted(int shelfId, Owner owner);

    Shelf getShelfToBeDeleted(int shelfId, int collectionId, int libraryId, Owner owner);

    Location getLocation(int locationId, Owner owner);

    List<Library> getAllLocations(Owner owner);

    List<Area> getAreasByLocationId(int locationId);

    boolean create(Library library);

    boolean update(Library library);

    boolean create(LibraryCollection collection);

    boolean update(LibraryCollection collection);

    boolean create(Shelf shelf);

    boolean update(Shelf shelf);

    boolean delete(Library library);

    boolean delete(LibraryCollection collection);

    boolean delete(Shelf shelf);

    boolean deleteDescriptions(List<Integer> ids);

    boolean deleteNotes(List<Integer> ids);

    boolean deleteAreas(List<Integer> ids);

    boolean save(SearchIndex index);

    List<SearchIndex> findSearchIndexes(int locationId);

    List<LibraryCollection> getCollectionsForIndexUpdate(int locationId);

    List<Shelf> getShelvesForIndexUpdate(int locationId);

    boolean clearSearchIndex();

    List<Library> getAllLibraries();

    List<Location> getAllLocationsWithDependecies(Owner owner);

    int getLibraryId(int collectionId);

    int getCollectionId(int shelfId);

    List<Integer> getAreaIds(Location location);

    List<Integer> getDescriptionIds(Location location);

    List<Integer> getNoteIds(Location location);
}
