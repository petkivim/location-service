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
package com.pkrete.locationservice.admin.search.impl;

import com.pkrete.locationservice.admin.model.search.SearchLevel;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import com.pkrete.locationservice.admin.search.BaseSearcher;
import com.pkrete.locationservice.admin.search.Searcher;
import com.pkrete.locationservice.admin.service.LocationsService;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * This class implements the {@link Searcher Searcher} interface. This class
 * implements the locations search by the given parameters and returns a list
 * of matching Location objects.
 * 
 * @author Petteri Kivimäki
 */
public class SearcherImpl extends BaseSearcher {

    private final static Logger logger = Logger.getLogger(SearcherImpl.class.getName());
    private LocationsService locationsService;

    public void setLocationsService(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    /**
     * Searches locations by given parameters. If level is LIBRARY, returns a 
     * list of all the libraries of the given owner. If level is COLLECTION,
     * returns a list of collections belonging to the library which id
     * equals the searchStr parameter. If level is SHELF, returns a list of 
     * shelvels belonging to the collection which id equals the searchStr 
     * parameter.
     * @param searchStr id of a library or a colletion
     * @param owner Owner of the location
     * @param level library, collection or shelf
     * @return list of locations
     */
    public List<SimpleLocation> doSearch(String searchStr, Owner owner, SearchLevel level) {
        List results = new ArrayList<Location>();

        // Get results by search level
        if (level == SearchLevel.LIBRARY) {
            results = locationsService.getlLibraries(owner);
        } else if (level == SearchLevel.COLLECTION) {
            results = locationsService.getCollectionsByLibraryId(this.converterService.strToInt(searchStr), owner);
        } else if (level == SearchLevel.SHELF) {
            results = locationsService.getShelvesByCollectionId(this.converterService.strToInt(searchStr), owner);
        } else if (level == SearchLevel.ALL) {
            results = locationsService.getLocations(owner);
        }
        // Return results
        return results;
    }
}
