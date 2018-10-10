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
package com.pkrete.locationservice.endpoint.service;

import com.pkrete.locationservice.endpoint.modifier.CallnoModification;
import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.callnoparser.LocatingStrategy;
import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.model.owner.Owner;
import com.pkrete.locationservice.endpoint.statistics.SearchEvent;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import com.pkrete.locationservice.endpoint.model.location.SimpleLocation;
import java.util.List;

/**
 * Service interface defines methods that are needed by other parts of the
 * application that need to access the data in the database. All the classes
 * implementing this interface must implement all the methods.
 *
 * @author Petteri Kivimäki
 */
public interface Service {

    boolean save(SearchEvent event);

    List getLibrary(String callno, String owner);

    Library getLibrary(int id);

    List getLibraries(String owner);

    List<SimpleLocation> getLibrariesFromIndex(String owner);

    List getCollection(String callno, String owner);

    List getCollections(String owner);

    List<SimpleLocation> getCollectionsFromIndex(String owner);

    List<SimpleLocation> getCollectionsByLibraryId(int id, String owner);

    LibraryCollection getCollection(int id);

    List getShelf(String callno, String owner);

    List getShelves(String owner);

    List<SimpleLocation> getShelvesFromIndex(String owner);

    List<SimpleLocation> getShelvesByCollectionId(int id, String owner);

    Shelf getShelf(int id);

    Location getLocation(String locationId, String owner);

    List<SimpleLocation> getSubstringLocations(String owner);

    List<SimpleLocation> getSubstringLocations(String owner, String collectionCode);

    List<SimpleLocation> getShelvesByCollectionCode(String owner, String collectionCode);

    LibraryCollection getCollectionByCollectionCode(String owner, String collectionCode);

	  List<LibraryCollection> getCollectionsByCollectionCode(String owner, String collectionCode);

    List<Library> getAllLocations(String owner);

    Owner getOwnerByCode(String code);

    LocatingStrategy getLocatingStrategy(String owner);

    List<CallnoModification> getNotFoundRedirects(String owner);

    List<CallnoModification> getPreprocessingRedirects(String owner);

    SimpleLocation getIndexEntry(String locationId, String owner);

    List<Location> getLocations(List<Integer> libraryIds, List<Integer> collectionIds, List<Integer> shelfIds, boolean children);

    boolean testDbConnection();
}
