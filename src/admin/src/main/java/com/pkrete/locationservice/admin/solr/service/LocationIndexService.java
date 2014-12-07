/**
 * This file is part of Location Service :: Admin. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Admin is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Admin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Admin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.admin.solr.service;

import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import com.pkrete.locationservice.admin.model.search.SearchLevel;
import java.util.List;

/**
 * This interface defines methods for adding, updating, deleting and searching
 * Locations from external search index.
 *
 * @author Petteri Kivimäki
 */
public interface LocationIndexService {

    List<SimpleLocation> search(Integer id, SearchLevel level);

    List<SimpleLocation> search(Integer ownerId, Integer subjectMatterId);

    List<SimpleLocation> search(Integer ownerId, SearchLevel level, String sortDirection, String sortField);

    List<SimpleLocation> search(Integer ownerId, Integer parentId, SearchLevel level, String sortDirection, String sortField);

    List<SimpleLocation> search(Integer ownerId, Integer parentId, Integer grandparentId, String sortDirection, String sortField);

    boolean save(Location document);

    boolean udpate(Integer ownerId, Integer subjectMatterId, String indexTerm);

    boolean delete(Location location);

    boolean deleteAll();

    boolean update(Integer ownerId, String ownerCode);
}
