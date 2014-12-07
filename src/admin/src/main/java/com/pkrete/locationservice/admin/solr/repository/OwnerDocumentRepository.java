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

import com.pkrete.locationservice.admin.solr.model.DocumentType;
import com.pkrete.locationservice.admin.solr.model.OwnerDocument;
import java.util.List;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.data.domain.Sort;

/**
 * This interfaces defines methods for adding, updating, deleting and searching
 * Owners from an external search index. Methods for adding, updating and
 * deleting data from index are inherited from the super class.
 *
 * Search method implementations are dynamically generated runtime based on the
 * naming conventions and annotations. No implementation of this interface is
 * needed.
 *
 * @author Petteri Kivimäki
 */
public interface OwnerDocumentRepository extends SolrCrudRepository<OwnerDocument, String> {

    List<OwnerDocument> findByDocumentType(DocumentType documentType, Sort sort);

    List<OwnerDocument> findByOwnerIdAndDocumentType(Integer locationId, DocumentType documentType);

    List<OwnerDocument> findByOwnerCodeAndDocumentType(String ownerCode, DocumentType documentType);
}
