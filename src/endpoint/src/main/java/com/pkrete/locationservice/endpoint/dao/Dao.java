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
package com.pkrete.locationservice.endpoint.dao;

import com.pkrete.locationservice.endpoint.modifier.CallnoModification;
import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.callnoparser.LocatingStrategy;
import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.model.owner.Owner;
import com.pkrete.locationservice.endpoint.statistics.SearchEvent;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import com.pkrete.locationservice.endpoint.model.search.SearchIndex;
import java.util.List;

/**
 * Dao interface defines the database methods that are needed by the 
 * application. All the classes implementing this interface must implement
 * all the methods.
 * 
 * @author Petteri Kivimäki
 */
public interface Dao {

    public boolean save(SearchEvent event);

    public List getLibrary(String callno, String owner);

    public List getLibraries(String owner);

    public List<SearchIndex> getLibrariesFromIndex(String owner);

    public Library getLibrary(int id);

    public List<Library> getLibraries(List<Integer> ids, boolean children);

    public List getCollection(String callno, String owner);

    public List getCollections(String owner);

    public List<SearchIndex> getCollectionsFromIndex(String owner);

    public List<Location> getCollectionsByLibraryId(int id, String owner);

    public LibraryCollection getCollection(int id);

    public List<LibraryCollection> getCollections(List<Integer> ids, boolean children);

    public List getShelf(String callno, String owner);

    public List getShelves(String owner);

    public List<SearchIndex> getShelvesFromIndex(String owner);

    public List<Location> getShelvesByCollectionId(int id, String owner);

    public Shelf getShelf(int id);

    public List<Shelf> getShelves(List<Integer> ids);

    public Location getLocation(int locationId, String owner);

    public List<Location> getSubstringLocations(String owner);

    public List<Location> getSubstringLocations(String owner, String collectionCode);

    public List<Location> getShelvesByCollectionCode(String owner, String collectionCode);

    public LibraryCollection getCollectionByCollectionCode(String owner, String collectionCode);

    public List<Library> getAllLocations(String owner);

    public Owner getOwnerByCode(String code);

    public LocatingStrategy getLocatingStrategy(String owner);

    public List<CallnoModification> getNotFoundRedirects(String owner);

    public List<CallnoModification> getPreprocessingRedirects(String owner);

    public SearchIndex getIndexEntry(int locationId, String owner);

    public boolean testDbConnection();
}
