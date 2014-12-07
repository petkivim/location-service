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
package com.pkrete.locationservice.admin.solr.repository;

import com.pkrete.locationservice.admin.model.search.LocationType;
import com.pkrete.locationservice.admin.solr.model.DocumentType;
import com.pkrete.locationservice.admin.solr.model.LocationDocument;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * This interfaces defines methods for adding, updating, deleting and searching
 * Locations from an external search index. Methods for adding, updating and
 * deleting data from index are inherited from the super class.
 *
 * Search method implementations are dynamically generated runtime based on the
 * naming conventions and annotations. No implementation of this interface is
 * needed.
 *
 * @author Petteri Kivimäki
 */
public interface LocationDocumentRepository extends SolrCrudRepository<LocationDocument, String> {

    List<LocationDocument> findByDocumentType(DocumentType documentType);

    List<LocationDocument> findByLocationIdAndLocationType(Integer locationId, LocationType locationType);

    List<LocationDocument> findByOwnerIdAndDocumentType(Integer ownerId, DocumentType documentType);

    List<LocationDocument> findByOwnerIdAndDocumentTypeAndSubjectMatterIds(Integer ownerId, DocumentType documentType, Integer subjectMatterId);

    List<LocationDocument> findByOwnerIdAndDocumentType(Integer ownerId, DocumentType documentType, Sort sort);

    List<LocationDocument> findByOwnerIdAndLocationType(Integer ownerId, LocationType locationType, Sort sort);

    List<LocationDocument> findByOwnerIdAndParentIdAndLocationType(Integer ownerId, Integer parentId, LocationType locationType);

    List<LocationDocument> findByOwnerIdAndParentIdAndLocationType(Integer ownerId, Integer parentId, LocationType locationType, Sort sort);

    List<LocationDocument> findByOwnerIdAndParentIdAndGrandparentIdAndLocationType(Integer ownerId, Integer parentId, Integer grandparentId, LocationType locationType, Sort sort);

    List<LocationDocument> findByOwnerIdAndGrandparentIdAndLocationType(Integer ownerId, Integer grandparentId, LocationType locationType);
}
