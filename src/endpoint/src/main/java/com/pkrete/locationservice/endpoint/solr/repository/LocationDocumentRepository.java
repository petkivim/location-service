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
package com.pkrete.locationservice.endpoint.solr.repository;

import com.pkrete.locationservice.endpoint.model.search.LocationType;
import com.pkrete.locationservice.endpoint.solr.model.DocumentType;
import com.pkrete.locationservice.endpoint.solr.model.LocationDocument;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

/**
 * This interfaces defines methods for searching Locations from an external 
 * search index.
 * 
 * Search method implementations are dynamically generated runtime based
 * on the naming conventions and annotations. No implementation of this
 * interface is needed.
 * 
 * @author Petteri Kivimäki
 */
public interface LocationDocumentRepository extends Repository<LocationDocument, String> {

    public LocationDocument findByLocationIdAndOwnerCodeAndDocumentType(Integer locationId, String ownerCode, DocumentType documentType);
    
    public List<LocationDocument> findByDocumentType(DocumentType documentType);
    
    public List<LocationDocument> findByOwnerCodeAndDocumentType(String ownerCode, DocumentType documentType, Sort sort);
    
    public List<LocationDocument> findByOwnerCodeAndLocationType(String ownerCode, LocationType locationType, Sort sort);

    public List<LocationDocument> findByOwnerCodeAndLocationTypeAndIsSubstring(String ownerCode, LocationType locationType, boolean isSubstring, Sort sort);
    
    public List<LocationDocument> findByOwnerCodeAndLocationTypeAndCollectionCode(String ownerCode, LocationType locationType, String collectionCode, Sort sort);
    
    public List<LocationDocument> findByOwnerCodeAndLocationTypeAndParentId(String ownerCode, LocationType locationType, Integer parentId, Sort sort);

    public List<LocationDocument> findByOwnerCodeAndLocationTypeAndIsSubstringAndCollectionCode(String ownerCode, LocationType locationType, boolean isSubstring, String collectionCode, Sort sort);
}
