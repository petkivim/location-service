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
package com.pkrete.locationservice.admin.solr.service.impl;

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.solr.model.DocumentType;
import com.pkrete.locationservice.admin.solr.model.OwnerDocument;
import com.pkrete.locationservice.admin.solr.model.builder.OwnerDocumentBuilder;
import com.pkrete.locationservice.admin.solr.repository.OwnerDocumentRepository;
import com.pkrete.locationservice.admin.solr.service.OwnerIndexService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

/**
 * This service class offers methods for adding, updating, deleting and
 * searching Owners from external search index.
 *
 * @author Petteri Kivimäki
 */
public class OwnerIndexServiceImpl implements OwnerIndexService {

    private final static Logger logger = LoggerFactory.getLogger(LocationIndexServiceImpl.class.getName());
    private OwnerDocumentRepository repository;

    /**
     * Sets the value of the repository variable.
     *
     * @param repository new value
     */
    public void setRepository(OwnerDocumentRepository repository) {
        this.repository = repository;
    }

    /**
     * Adds or updates the given Owner object to the search index. Returns true
     * if and only if the Owner was successfully added or updated; otherwise
     * false.
     *
     * @param owner Owner to be added
     * @return true if and only if the Owner was successfully added or updated;
     * otherwise false
     */
    @Override
    public boolean save(Owner owner) {
        try {
            // Get a OwnerDocument representing the given Owner
            OwnerDocument document = OwnerDocumentBuilder.build(owner);
            // Save the OwnerDocument to the index
            this.repository.save(document);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Deletes the Owner with the given id from the search index. Returns true
     * if and only if the Owner was successfully deleted; otherwise false
     *
     * @param id ownerId of the Owner to be deleted
     * @return true if and only if the Owner was successfully deleted; otherwise
     * false
     */
    @Override
    public boolean delete(Integer id) {
        try {
            this.repository.delete(OwnerDocumentBuilder.getId(id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Deletes all the index entries. Returns true if and only if all the
     * entries were successfully deleted; otherwise false.
     *
     * @return true if and only if all the entries were successfully deleted;
     * otherwise false
     */
    @Override
    public boolean deleteAll() {
        try {
            List<OwnerDocument> list = this.find();
            for (OwnerDocument document : list) {
                this.repository.delete(document);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Returns all the OwnerDocuments sorted by name
     *
     * @return list of OwnerDocuments sorted by name
     */
    @Override
    public List<OwnerDocument> find() {
        return this.repository.findByDocumentType(DocumentType.OWNER, new Sort(Sort.Direction.ASC, "name"));
    }

    /**
     * Returns the owner with the given id.
     *
     * @param id the id that is used for searching
     * @return owner with the given id
     */
    @Override
    public OwnerDocument find(int id) {
        return this.repository.findOne(OwnerDocumentBuilder.getId(id));
    }

    /**
     * Returns a list of owners with the given owner code. The list should
     * contain only one owner as ids are unique.
     *
     * @param ownerCode owner code that is used for searching
     * @return list of owners with the given owner code
     */
    @Override
    public List<OwnerDocument> find(String ownerCode) {
        return this.repository.findByOwnerCodeAndDocumentType(ownerCode, DocumentType.OWNER);
    }
}
