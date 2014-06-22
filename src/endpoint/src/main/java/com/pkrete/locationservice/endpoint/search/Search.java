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
package com.pkrete.locationservice.endpoint.search;

import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.model.owner.Owner;
import com.pkrete.locationservice.endpoint.model.search.Position;
import com.pkrete.locationservice.endpoint.model.search.SearchType;
import com.pkrete.locationservice.endpoint.service.Service;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This abstract class defines an abstract search method that's used
 * for searching locations mathing the given parameters. Th
 * 
 * @author Petteri Kivimäki
 */
public abstract class Search {

    protected Service dbService;

    public abstract List<Location> search(String search, Position position, SearchType type, Owner owner, boolean children);

    /**
     * Sets the dbService variable.
     * @param dbService new value
     */
    public void setDbService(Service dbService) {
        this.dbService = dbService;
    }

    /**
     * Finds all the expressions of the given pattern from the given string
     * and escapes them. 
     * @param inStr string to be modified
     * @param pattern pattern to be searhed
     * @return escaped string
     */
    protected String escapeRegex(String inStr, Pattern pattern) {
        Matcher match = pattern.matcher(inStr);
        return match.replaceAll("\\\\$1");
    }

    /**
     * Builds the search string that is used for performing the search. The
     * given search string is modified according to the given position
     * parameter, and the position information is added to the search
     * string.
     * @param search search string
     * @param position position of the search string in the target field
     * @return modified search string
     */
    protected String buildSearchStr(String search, Position position) {
        search = search.toUpperCase();
        if (position == Position.FIRST) {
            return "^" + search + ".*";
        } else if (position == Position.LAST) {
            return ".*" + search + "$";
        } else if (position == Position.MATCH) {
            return "^" + search + "$";
        }
        return ".*" + search + ".*";
    }
}
