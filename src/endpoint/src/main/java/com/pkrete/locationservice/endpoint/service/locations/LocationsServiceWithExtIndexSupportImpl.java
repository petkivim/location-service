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
package com.pkrete.locationservice.endpoint.service.locations;

import com.pkrete.locationservice.endpoint.callnoparser.LocatingStrategy;
import com.pkrete.locationservice.endpoint.model.location.SimpleLocation;
import com.pkrete.locationservice.endpoint.modifier.CallnoModification;
import com.pkrete.locationservice.endpoint.solr.service.LocationIndexService;
import com.pkrete.locationservice.endpoint.solr.service.OwnerIndexService;
import java.util.List;

/**
 * LocationsServiceWithExtIndexSupportImpl class extends the LocationsService
 * class and overrides some its methods. This class overrides methods that
 * define operations with an external index software and external index software
 * is supported.
 *
 * @author Petteri Kivimäki
 */
public class LocationsServiceWithExtIndexSupportImpl extends LocationsService {

    private LocationIndexService locationIndexService;
    private OwnerIndexService ownerIndexService;

    /**
     * Sets the owner index service object.
     *
     * @param ownerIndexService new value
     */
    public void setOwnerIndexService(OwnerIndexService ownerIndexService) {
        this.ownerIndexService = ownerIndexService;
    }

    /**
     * Sets the location index service variable value.
     *
     * @param locationIndexService new value
     */
    public void setLocationIndexService(LocationIndexService locationIndexService) {
        this.locationIndexService = locationIndexService;
    }

    /**
     * Returns the index entry matching the given location id and owner code. If
     * no matching entry or more than one entries are found, null is returned.
     *
     * @param locationId location id of the Location object
     * @param owner owner of the Location object
     * @return index entry or null
     */
    @Override
    public SimpleLocation getIndexEntry(String locationId, String owner) {
        int id = 0;
        try {
            id = Integer.parseInt(locationId);
        } catch (NumberFormatException nfe) {
            return null;
        }
        return this.locationIndexService.search(id, owner);
    }

    /**
     * Returns a list of active preprocessing redirect objects related to the
     * given owner.
     *
     * @param code owner of the preprocessing redirects
     * @return list of active preprocessing redirects related to the given owner
     */
    @Override
    public List<CallnoModification> getPreprocessingRedirects(String owner) {
        return this.ownerIndexService.getPreprocessingRedirects(owner);
    }

    /**
     * Returns a list of not found redirect objects related to the given owner.
     *
     * @param code owner of the not found redirects
     * @return list of not found redirects related to the given owner
     */
    @Override
    public List<CallnoModification> getNotFoundRedirects(String owner) {
        return this.ownerIndexService.getNotFoundRedirects(owner);
    }

    /**
     * Returns a list of collections which shelves' location code is a substring
     * placed in the beginning of a string.
     *
     * @param owner owner of the location
     * @return list of locations matching the condition
     */
    @Override
    public List<SimpleLocation> getSubstringLocations(String owner) {
        return this.locationIndexService.getSubstringLocations(owner);
    }

    /**
     * Returns a list of collections which shelves' location code is a substring
     * placed in the beginning of a string.
     *
     * @param owner owner of the location
     * @param collectionCode collection code of the location
     * @return list of locations matching the condition
     */
    @Override
    public List<SimpleLocation> getSubstringLocations(String owner, String collectionCode) {
        return this.locationIndexService.getSubstringLocations(owner, collectionCode);
    }

    /**
     * Returns a list of shelves that belong to the collection with the given
     * collection code.
     *
     * @param owner owner of the location
     * @param collectionCode collection code of the collection in which the
     * shelf belongs
     * @return list of shelves matching the condition
     */
    @Override
    public List<SimpleLocation> getShelvesByCollectionCode(String owner, String collectionCode) {
        return this.locationIndexService.getShelvesByCollectionCode(owner, collectionCode);
    }

    /**
     * Returns all the shelves that are related to the collection which
     * locationId matches with the given id number.
     *
     * @param id the locationId that is used for searching
     * @param owner the owner of the object
     * @return the shelf with the desired locationId
     */
    @Override
    public List<SimpleLocation> getShelvesByCollectionId(int id, String owner) {
        return this.locationIndexService.getShelvesByCollectionId(owner, id);
    }

    /**
     * Returns all the collections that are related to the library which
     * locationId matches with the given id number.
     *
     * @param id the locationId that is used for searching
     * @param owner the owner of the object
     * @return the collection with the desired locationId
     */
    @Override
    public List<SimpleLocation> getCollectionsByLibraryId(int id, String owner) {
        return this.locationIndexService.getCollectionsByLibraryId(owner, id);
    }

    /**
     * Returns a list of search index entries related to libraries that belong
     * to the given owner.
     *
     * @param owner owner of the object
     * @return search index entries that are related to the given owner
     */
    @Override
    public List<SimpleLocation> getLibrariesFromIndex(String owner) {
        return this.locationIndexService.getLibrariesFromIndex(owner);
    }

    /**
     * Returns a list of search index entries related to libraries that belong
     * to the given owner.
     *
     * @param owner owner of the object
     * @return search index entries that are related to the given owner
     */
    @Override
    public List<SimpleLocation> getCollectionsFromIndex(String owner) {
        return this.locationIndexService.getCollectionsFromIndex(owner);
    }

    /**
     * Returns a list of search index entries related to shelves that belong to
     * the given owner.
     *
     * @param owner owner of the object
     * @return search index entries that are related to the given owner
     */
    @Override
    public List<SimpleLocation> getShelvesFromIndex(String owner) {
        return this.locationIndexService.getShelvesFromIndex(owner);
    }

    /**
     * Returns the locating strategy defined for the given owner. If no strategy
     * is found, null is returned.
     *
     * @param owner owner of the object
     * @return locating strategy defined for the given owner or null, if no
     * strategy is found
     */
    @Override
    public LocatingStrategy getLocatingStrategy(String owner) {
        return this.ownerIndexService.getLocatingStrategy(owner);
    }
}
