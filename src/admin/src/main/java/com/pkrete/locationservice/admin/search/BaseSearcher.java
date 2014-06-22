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
package com.pkrete.locationservice.admin.search;

import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.search.SearchLevel;
import com.pkrete.locationservice.admin.service.OwnersService;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * This abstract class implements {@link Searcher Searcher} interface. Does
 * not implement the search itself, but validates all the given parameters
 * and fetches the Owner object matching the given owner code. Subclasses
 * must implement doSearch method and implement the actual search operation.
 * 
 * @author Petteri Kivimäki
 */
public abstract class BaseSearcher implements Searcher {

    private final static Logger logger = Logger.getLogger(BaseSearcher.class.getName());
    protected OwnersService ownersService;
    protected ConverterService converterService;

    protected abstract List<SimpleLocation> doSearch(String searchStr, Owner owner, SearchLevel level);

    public void setOwnersService(OwnersService ownersService) {
        this.ownersService = ownersService;
    }

    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    /**
     * Validates all the given parameters and fetches the Owner object matching
     * the given owner code, but does not implement the search itself. Search
     * is implemented in abstract doSearch function.
     * @param searchStr id of a library or a colletion
     * @param ownerCode owner code of the location's Owner
     * @param level level library, collection or shelf
     * @return list of locations
     */
    public List<SimpleLocation> search(String searchStr, String ownerCode, SearchLevel level) {
        List results = new ArrayList();

        // Search string can not be null if level is other than LIBRARY
        if (searchStr == null && level != SearchLevel.LIBRARY && level != SearchLevel.ALL) {
            logger.warn("\"searchStr\" can not be null.");
            return results;
        }
        // Search string must be a number
        if (level != SearchLevel.LIBRARY && level != SearchLevel.ALL && !searchStr.matches("^[0-9]+$")) {
            logger.warn("\"searchStr\" can contain only numbers. Value: \"" + searchStr + "\"");
            return results;
        }
        // Owner code can not be null
        if (ownerCode == null) {
            logger.warn("\"ownerCode\" can not be null.");
            return results;
        }
        // Level can not be null
        if (level == null) {
            logger.warn("\"level\" can not be null.");
            return results;
        }

        // Get owner object matching the given owner code
        Owner owner = ownersService.getOwnerByCode(ownerCode);
        // Owner object can not be null
        if (owner == null) {
            logger.warn("Unable to find owner matching the given \"ownerCode\". Code: \"" + ownerCode + "\"");
            return results;
        }

        return doSearch(searchStr, owner, level);
    }
}
