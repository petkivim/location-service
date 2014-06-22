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
 * This interface defines service layer for Library, LibraryCollection and
 * Shelf objects. All the classes implementing this interface must implement 
 * all the methods defined here.
 * 
 * @author Petteri Kivimäki
 */
public interface LocationsService {

    public Library getLibrary(int libraryId);

    public Library getLibrary(int libraryId, Owner owner);

    public Library getLibraryToBeDeleted(int libraryId, Owner owner);

    public LibraryCollection getCollection(int collectionId);

    public LibraryCollection getCollection(int collectionId, Owner owner);

    public LibraryCollection getCollection(int collectionId, int libraryId, Owner owner);

    public LibraryCollection getCollectionToBeDeleted(int collectionId, Owner owner);

    public LibraryCollection getCollectionToBeDeleted(int collectionId, int libraryId, Owner owner);

    public List<SimpleLocation> getCollectionsByLibraryId(int libraryId, Owner owner);

    public List<SimpleLocation> getCollections(Owner owner);

    public Shelf getShelf(int shelfId, Owner owner);

    public Shelf getShelf(int shelfId, int collectionId, int libraryId, Owner owner);

    public List<SimpleLocation> getShelvesByCollectionId(int collectionId, Owner owner);

    public List<SimpleLocation> getShelvesByCollectionId(int libraryId, int collectionId, Owner owner);

    public Shelf getShelfToBeDeleted(int shelfId, Owner owner);

    public Shelf getShelfToBeDeleted(int shelfId, int collectionId, int libraryId, Owner owner);

    public Location getLocation(int locationId, Owner owner);

    public List<SimpleLocation> getlLibraries(Owner owner);

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

    public void recreateSearchIndex();

    public SearchIndex getIndexEntry(int locationId);

    public List<SimpleLocation> getLocations(Owner owner);

    public int getLibraryId(String collectionId);

    public int getCollectionId(String shelfId);

    public Set<Integer> getAreaIdsSet(Location location);

    public Set<Integer> getDescriptionIdsSet(Location location);

    public Set<Integer> getNoteIdsSet(Location location);
}
