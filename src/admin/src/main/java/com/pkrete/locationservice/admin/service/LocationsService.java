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
package com.pkrete.locationservice.admin.service;

import com.pkrete.locationservice.admin.model.location.Area;
import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.search.SearchIndex;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.location.Shelf;
import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import java.util.List;
import java.util.Set;

/**
 * This interface defines service layer for Library, LibraryCollection and Shelf
 * objects. All the classes implementing this interface must implement all the
 * methods defined here.
 *
 * @author Petteri Kivimäki
 */
public interface LocationsService {

    Library getLibrary(int libraryId);

    Library getLibrary(int libraryId, Owner owner);

    Library getLibraryToBeDeleted(int libraryId, Owner owner);

    LibraryCollection getCollection(int collectionId);

    LibraryCollection getCollection(int collectionId, Owner owner);

    LibraryCollection getCollection(int collectionId, int libraryId, Owner owner);

    LibraryCollection getCollectionToBeDeleted(int collectionId, Owner owner);

    LibraryCollection getCollectionToBeDeleted(int collectionId, int libraryId, Owner owner);

    List<SimpleLocation> getCollectionsByLibraryId(int libraryId, Owner owner);

    List<SimpleLocation> getCollections(Owner owner);

    Shelf getShelf(int shelfId, Owner owner);

    Shelf getShelf(int shelfId, int collectionId, int libraryId, Owner owner);

    List<SimpleLocation> getShelvesByCollectionId(int collectionId, Owner owner);

    List<SimpleLocation> getShelvesByCollectionId(int libraryId, int collectionId, Owner owner);

    Shelf getShelfToBeDeleted(int shelfId, Owner owner);

    Shelf getShelfToBeDeleted(int shelfId, int collectionId, int libraryId, Owner owner);

    Location getLocation(int locationId, Owner owner);

    List<SimpleLocation> getlLibraries(Owner owner);

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

    void recreateSearchIndex();

    SearchIndex getIndexEntry(int locationId);

    List<SimpleLocation> getLocations(Owner owner);

    int getLibraryId(String collectionId);

    int getCollectionId(String shelfId);

    Set<Integer> getAreaIdsSet(Location location);

    Set<Integer> getDescriptionIdsSet(Location location);

    Set<Integer> getNoteIdsSet(Location location);
}
