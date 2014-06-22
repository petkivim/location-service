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

import com.pkrete.locationservice.endpoint.callnoparser.LocatingStrategy;
import com.pkrete.locationservice.endpoint.modifier.CallnoModification;
import com.pkrete.locationservice.endpoint.solr.model.DocumentType;
import com.pkrete.locationservice.endpoint.solr.model.OwnerDocument;
import com.pkrete.locationservice.endpoint.solr.model.builder.OwnerDocumentBuilder;
import com.pkrete.locationservice.endpoint.solr.repository.OwnerDocumentRepository;
import com.pkrete.locationservice.endpoint.solr.service.OwnerIndexService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;

/**
 * This service class offers methods for searching Owners from external 
 * search index. 
 * 
 * @author Petteri Kivimäki
 */
public class OwnerIndexServiceImpl implements OwnerIndexService {

    private OwnerDocumentRepository repository;

    /**
     * Sets the value of the repository variable.
     * @param repository new value
     */
    public void setRepository(OwnerDocumentRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns the locating strategy defined for the given owner. If no
     * strategy is found, null is returned.
     * @param ownerCode owner of the object
     * @return locating strategy defined for the given owner or null, if
     * no strategy is found
     */
    public LocatingStrategy getLocatingStrategy(String ownerCode) {
        OwnerDocument owner = this.repository.findByOwnerCodeAndDocumentType(ownerCode, DocumentType.OWNER);
        if (owner == null) {
            return null;
        }
        return owner.getLocatingStrategy();
    }

    /**
     * Returns a list of active PreprocessingRedirects related to the 
     * given owner.
     * @param ownerCode owner code of the redirects' owner
     * @return list of active redirects
     */
    public List<CallnoModification> getPreprocessingRedirects(String ownerCode) {
        List<CallnoModification> result = new ArrayList<CallnoModification>();
        OwnerDocument owner = this.repository.findByOwnerCodeAndDocumentType(ownerCode, DocumentType.OWNER);
        List<CallnoModification> temp = OwnerDocumentBuilder.build(owner.getPreprocessingRedirects(), true);
        for (CallnoModification mod : temp) {
            if (mod.getIsActive()) {
                result.add(mod);
            }
        }
        return result;
    }

    /**
     * Returns a list of active NotFoundRedirects related to the 
     * given owner.
     * @param ownerCode owner code of the redirects' owner
     * @return list of active redirects
     */
    public List<CallnoModification> getNotFoundRedirects(String ownerCode) {
        List<CallnoModification> result = new ArrayList<CallnoModification>();
        OwnerDocument owner = this.repository.findByOwnerCodeAndDocumentType(ownerCode, DocumentType.OWNER);
        List<CallnoModification> temp = OwnerDocumentBuilder.build(owner.getNotFoundRedirects(), false);
        for (CallnoModification mod : temp) {
            if (mod.getIsActive()) {
                result.add(mod);
            }
        }
        return result;
    }
}
