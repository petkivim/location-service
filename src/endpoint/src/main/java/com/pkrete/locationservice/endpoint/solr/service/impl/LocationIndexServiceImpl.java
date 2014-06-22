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
package com.pkrete.locationservice.endpoint.solr.service.impl;

import com.pkrete.locationservice.endpoint.model.location.SimpleLocation;
import com.pkrete.locationservice.endpoint.model.search.LocationType;
import com.pkrete.locationservice.endpoint.solr.model.DocumentType;
import com.pkrete.locationservice.endpoint.solr.repository.LocationDocumentRepository;
import com.pkrete.locationservice.endpoint.solr.repository.RepositoryConstants;
import com.pkrete.locationservice.endpoint.solr.service.LocationIndexService;
import java.util.List;
import org.springframework.data.domain.Sort;

/**
 * This service class offers methods for searching Locations from external 
 * search index. 
 * 
 * @author Petteri Kivimäki
 */
public class LocationIndexServiceImpl implements LocationIndexService {

    private LocationDocumentRepository repository;

    /**
     * Sets the value of the repository variable.
     * @param repository new value
     */
    public void setRepository(LocationDocumentRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns the location with the given id and owner code.
     * @param locationId id of the location
     * @param ownerCode owner code of the organization that owns the location
     * @return location matching the conditions
     */
    public SimpleLocation search(Integer locationId, String ownerCode) {
        return this.repository.findByLocationIdAndOwnerCodeAndDocumentType(locationId, ownerCode, DocumentType.LOCATION);
    }

    /**
     * Returns a list of all the locations related to the given owner.
     * @param ownerCode owner of the location
     * @return list of all the locations related to the given owner
     */
    public List<SimpleLocation> getAllLocations(String ownerCode) {
        return (List) this.repository.findByOwnerCodeAndDocumentType(ownerCode, DocumentType.LOCATION, this.sortByCallNumberAsc());
    }

    /**
     * Returns a list of collections which shelves' location code is a substring placed
     * in the beginning of a string.
     * @param ownerCode owner of the location
     * @return list of locations matching the condition
     */
    public List<SimpleLocation> getSubstringLocations(String ownerCode) {
        return (List) this.repository.findByOwnerCodeAndLocationTypeAndIsSubstring(ownerCode, LocationType.COLLECTION, true, this.sortByCallNumberDesc());
    }

    /**
     * Returns a list of collections which shelves' location code is a substring placed
     * in the beginning of a string.
     * @param ownerCode owner of the location
     * @param collectionCode collection code of the location
     * @return list of locations matching the condition
     */
    public List<SimpleLocation> getSubstringLocations(String owner, String collectionCode) {
        return (List) this.repository.findByOwnerCodeAndLocationTypeAndIsSubstringAndCollectionCode(owner, LocationType.COLLECTION, true, collectionCode, this.sortByCallNumberDesc());

    }

    /**
     * Returns a list of shelves that belong to the collection with the given
     * collection code.
     * @param ownerCode owner of the location
     * @param collectionCode collection code of the collection in which the shelf belongs
     * @return list of shelves matching the condition
     */
    public List<SimpleLocation> getShelvesByCollectionCode(String ownerCode, String collectionCode) {
        return (List) this.repository.findByOwnerCodeAndLocationTypeAndCollectionCode(ownerCode, LocationType.SHELF, collectionCode, this.sortByLocationCodeDesc());
    }

    /**
     * Returns all the shelves that are related to the collection which
     * locationId matches with the given id number.
     * @param id the locationId that is used for searching
     * @param ownerCode the owner of the object
     * @return the shelf with the desired locationId
     */
    public List<SimpleLocation> getShelvesByCollectionId(String ownerCode, int collectionId) {
        return (List) this.repository.findByOwnerCodeAndLocationTypeAndParentId(ownerCode, LocationType.SHELF, collectionId, this.sortByLocationCodeDesc());
    }

    /**
     * Returns all the collections that are related to the library which
     * locationId matches with the given id number.
     * @param id the locationId that is used for searching
     * @param ownerCode the owner of the object
     * @return the collection with the desired locationId
     */
    public List<SimpleLocation> getCollectionsByLibraryId(String ownerCode, int libraryId) {
        return (List) this.repository.findByOwnerCodeAndLocationTypeAndParentId(ownerCode, LocationType.COLLECTION, libraryId, this.sortByLocationCodeDesc());
    }

    /**
     * Returns a list of search index entries related to libraries that 
     * belong to the given owner.
     * @param ownerCode owner of the object
     * @return search index entries that are related to the given owner
     */
    public List<SimpleLocation> getLibrariesFromIndex(String ownerCode) {
        return (List) this.repository.findByOwnerCodeAndLocationType(ownerCode, LocationType.LIBRARY, this.sortByCallNumberDesc());
    }

    /**
     * Returns a list of search index entries related to libraries that 
     * belong to the given owner.
     * @param ownerCode owner of the object
     * @return search index entries that are related to the given owner
     */
    public List<SimpleLocation> getCollectionsFromIndex(String ownerCode) {
        return (List) this.repository.findByOwnerCodeAndLocationType(ownerCode, LocationType.COLLECTION, this.sortByCallNumberDesc());
    }

    /**
     * Returns a list of search index entries related to shelves that 
     * belong to the given owner.
     * @param ownerCode owner of the object
     * @return search index entries that are related to the given owner
     */
    public List<SimpleLocation> getShelvesFromIndex(String ownerCode) {
        return (List) this.repository.findByOwnerCodeAndLocationType(ownerCode, LocationType.SHELF, this.sortByCallNumberDesc());
    }

    /**
     * Returns a Sort object that sorts the results in ascending order by
     * call number.
     * @return Sort object that sorts the results in ascending order by
     * call number
     */
    private Sort sortByCallNumberAsc() {
        return new Sort(Sort.Direction.ASC, RepositoryConstants.FIELD_CALL_NUMBER);
    }

    /**
     * Returns a Sort object that sorts the results in descending order by
     * call number.
     * @return Sort object that sorts the results in descending order by
     * call number
     */
    private Sort sortByCallNumberDesc() {
        return new Sort(Sort.Direction.DESC, RepositoryConstants.FIELD_CALL_NUMBER);
    }

    /**
     * Returns a Sort object that sorts the results in descending order by
     * location code.
     * @return Sort object that sorts the results in descending order by
     * call number
     */
    private Sort sortByLocationCodeDesc() {
        return new Sort(Sort.Direction.DESC, RepositoryConstants.FIELD_LOCATION_CODE);
    }

    /**
     * Tests that Solr instance is running and that the connection is
     * working.
     * @return true if and only if Solr is running and connection is working;
     * otherwise false
     */
    public boolean testConnection() {
        try {
            List result = this.repository.findByDocumentType(DocumentType.LOCATION);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
