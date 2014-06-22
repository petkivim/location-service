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

import com.pkrete.locationservice.admin.model.search.SearchLevel;
import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import java.util.List;

/**
 * This interface defines methods for searching Locations according to the
 * given conditions. This interface is used by the {@link Search Search}
 * servlet. All the classes implementing this interface must implements all 
 * the methods defined here.
 * 
 * @author Petteri Kivimäki
 */
public interface Searcher {

    public List<SimpleLocation> search(String searchStr, String ownerCode, SearchLevel level);
}
