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
package com.pkrete.locationservice.endpoint.solr.service;

import com.pkrete.locationservice.endpoint.model.location.SimpleLocation;
import java.util.List;

/**
 * This interface defines methods for  searching Locations from external 
 * search index.
 * 
 * @author Petteri Kivimäki
 */
public interface LocationIndexService {

    public SimpleLocation search(Integer locationId, String ownerCode);
    
    public List<SimpleLocation> getAllLocations(String ownerCode);

    public List<SimpleLocation> getSubstringLocations(String ownerCode);

    public List<SimpleLocation> getSubstringLocations(String ownerCode, String collectionCode);

    public List<SimpleLocation> getShelvesByCollectionCode(String ownerCode, String collectionCode);

    public List<SimpleLocation> getShelvesByCollectionId(String ownerCode, int collectionId);

    public List<SimpleLocation> getCollectionsByLibraryId(String ownerCode, int libraryId);

    public List<SimpleLocation> getLibrariesFromIndex(String ownerCode);

    public List<SimpleLocation> getCollectionsFromIndex(String ownerCode);

    public List<SimpleLocation> getShelvesFromIndex(String ownerCode);
    
    public boolean testConnection();
}
