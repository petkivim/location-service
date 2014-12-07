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
package com.pkrete.locationservice.admin.service.owners;

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.solr.model.OwnerDocument;
import com.pkrete.locationservice.admin.solr.model.builder.OwnerDocumentBuilder;
import com.pkrete.locationservice.admin.solr.service.LocationIndexService;
import com.pkrete.locationservice.admin.solr.service.OwnerIndexService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class extends {@link OwnersServiceImpl OwnersServiceImpl} class which
 * implements {@link OwnersService OwnersService} interface, that defines
 * service layer for Owner objects.
 *
 * This class offers methods for adding, editing and removing Owner objects.
 * This class overrides methods that define operations with an external index
 * software and external index software is supported.
 *
 * @author Petteri Kivimäki
 */
public class OwnersServiceWithExtIndexSupportImpl extends OwnersServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(OwnersServiceWithExtIndexSupportImpl.class.getName());
    private OwnerIndexService ownerIndexService;
    private LocationIndexService locationIndexService;

    /**
     * Sets the owner index service object.
     *
     * @param ownerIndexService new value
     */
    public void setOwnerIndexService(OwnerIndexService ownerIndexService) {
        this.ownerIndexService = ownerIndexService;
    }

    /**
     * Sets the location index service object.
     *
     * @param locationIndexService new value
     */
    public void setLocationIndexService(LocationIndexService locationIndexService) {
        this.locationIndexService = locationIndexService;
    }

    /**
     * Adds the Owner to external index. Returns true if and only if the Owner
     * was successfully added, otherwise false.
     *
     * @param owner Owner to be added
     * @return true if and only if the Owner was successfully added, otherwise
     * false
     */
    @Override
    protected boolean addToIndex(Owner owner) {
        if (this.ownerIndexService.save(owner)) {
            logger.debug("Adding owner to external index succeeded. Id : {}", owner.getId());
            return true;
        }
        logger.error("Adding owner to external index failed. Id : {}", owner.getId());
        return false;
    }

    /**
     * Updated the Owner to external index. Returns true if and only if the
     * Owner was successfully updated, otherwise false.
     *
     * @param owner Owner to be updated
     * @param hasChanged boolean value that tells if the owner code has changed
     * @return true if and only if the Owner was successfully updated, otherwise
     * false
     */
    @Override
    protected boolean updateToIndex(Owner owner, boolean hasChanged) {
        if (this.ownerIndexService.save(owner)) {
            logger.debug("Updating owner to external index succeeded. Id : {}", owner.getId());
            if (hasChanged) {
                logger.debug("Owner code has changed. Update new owner code to locations related to this owner. Id : {}", owner.getId());
                logger.info("Start updating locations related to the owner.");
                boolean result = this.locationIndexService.update(owner.getId(), owner.getCode());
                if (result) {
                    logger.info("Updating locations related to the owner succesfully completed.");
                } else {
                    logger.warn("Failed to update all the location related to the owner.");
                }
            }
            return true;
        }
        logger.error("Updating owner to external index failed. Id : {}", owner.getId());
        return false;
    }

    /**
     * Deletes the Owner from external index. Returns true if and only if the
     * Owner was successfully deleted, otherwise false.
     *
     * @param owner Owner to be deleted
     * @return true if and only if the Owner was successfully deleted, otherwise
     * false
     */
    @Override
    protected boolean deleteFromIndex(Owner owner) {
        if (this.ownerIndexService.delete(owner.getId())) {
            logger.debug("Deleting owner from external index succeeded. Id : {}", owner.getId());
            return true;
        }
        logger.error("Deleting owner from external index failed. Id : {}", owner.getId());
        return false;
    }

    /**
     * Returns a list of all the owners in the database.
     *
     * @return all the owners in the database
     */
    @Override
    public List<Owner> getOwners() {
        List<OwnerDocument> list = this.ownerIndexService.find();
        List<Owner> owners = new ArrayList<Owner>();
        for (OwnerDocument document : list) {
            owners.add(OwnerDocumentBuilder.build(document));
        }
        return owners;
    }

    /**
     * Returns the owner with the given id.
     *
     * @param id the id that is used for searching
     * @return the owner with the given id
     */
    @Override
    public Owner getOwner(int id) {
        OwnerDocument document = this.ownerIndexService.find(id);
        if (document != null) {
            return OwnerDocumentBuilder.build(document);
        }
        return null;
    }

    /**
     * Returns the owner with the given code.
     *
     * @param code the code that is used for searching
     * @return the owner with the given code
     */
    @Override
    public Owner getOwnerByCode(String code) {
        try {
            List<OwnerDocument> list = this.ownerIndexService.find(code);
            if (list != null && !list.isEmpty()) {
                return OwnerDocumentBuilder.build(list.get(0));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Recreates the search index. First all the current entries are deleted and
     * then regenerated.
     */
    @Override
    public void recreateSearchIndex() {
        logger.info("Recreate owners search index.");
        // Delete all the current index entries
        this.ownerIndexService.deleteAll();
        // Get all the owners from the DB
        List<Owner> owners = super.getOwners();
        // Go through the list and add each owner to index
        for (Owner owner : owners) {
            logger.debug("Create index entry for owner. Id : {}", owner.getId());
            if (!this.ownerIndexService.save(owner)) {
                logger.error("Creating index entry for owner failed! Id : {}", owner.getId());
            }
        }
        logger.info("Indexing owners done.");
    }
}
