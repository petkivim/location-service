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

import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import com.pkrete.locationservice.admin.model.search.LocationType;
import com.pkrete.locationservice.admin.model.search.SearchLevel;
import com.pkrete.locationservice.admin.solr.model.DocumentType;
import com.pkrete.locationservice.admin.solr.model.LocationDocument;
import com.pkrete.locationservice.admin.solr.model.builder.LocationDocumentBuilder;
import com.pkrete.locationservice.admin.solr.repository.LocationDocumentRepository;
import com.pkrete.locationservice.admin.solr.repository.RepositoryConstants;
import com.pkrete.locationservice.admin.solr.service.LocationIndexService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

/**
 * This service class offers methods for adding, updating, deleting and
 * searching Locations from external search index.
 *
 * @author Petteri Kivimäki
 */
public class LocationIndexServiceImpl implements LocationIndexService {

    private final static Logger logger = LoggerFactory.getLogger(LocationIndexServiceImpl.class.getName());
    private LocationDocumentRepository repository;

    /**
     * Sets the value of the repository variable.
     *
     * @param repository new value
     */
    public void setRepository(LocationDocumentRepository repository) {
        this.repository = repository;
    }

    /**
     * Adds or updates the given Location object to the search index. Returns
     * true if and only if the Location was successfully added or updated;
     * otherwise false.
     *
     * @param location Location to be added
     * @return true if and only if the Location was successfully added or
     * updated; otherwise false
     */
    @Override
    public boolean save(Location location) {
        // Get a LocationDocument representing the given Location
        LocationDocument document = LocationDocumentBuilder.build(location);
        // Save the LocationDocument to the index
        return this.save(document);
    }

    /**
     * Adds or updates the given LocationDocument object to the search index.
     * Returns true if and only if the LocationDocument was successfully added
     * or updated; otherwise false.
     *
     * @param document LocationDocument to be added
     * @return true if and only if the LocationDocument was successfully added
     * or updated; otherwise false
     */
    protected boolean save(LocationDocument document) {
        try {
            // Save the LocationDocument to the index
            this.repository.save(document);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Updates all the location documents related to the given owner and subject
     * matter. The value of the subject matter is updated.
     *
     * @param ownerId owner of the locations
     * @param subjectMatterId id of the subject matter
     * @param indexTerm new value of the subject matter
     * @return true if and only if all the locations were successfully updated;
     * otherwise false
     */
    @Override
    public boolean udpate(Integer ownerId, Integer subjectMatterId, String indexTerm) {
        List<LocationDocument> documents = this.repository.findByOwnerIdAndDocumentTypeAndSubjectMatterIds(ownerId, DocumentType.LOCATION, subjectMatterId);
        logger.debug("Found {} locations related to the subject matter. Subject matter id: {}", documents.size(), subjectMatterId);

        boolean success = true;
        for (LocationDocument document : documents) {
            for (int i = 0; i < document.getSubjectMatterIds().size(); i++) {
                if (document.getSubjectMatterIds().get(i) == subjectMatterId) {
                    if (logger.isDebugEnabled()) {
                        StringBuilder builder = new StringBuilder("Current value: \"");
                        builder.append(document.getSubjectMatters().get(i));
                        builder.append("\" - New value: \"").append(indexTerm);
                        builder.append("\"");
                        logger.debug(builder.toString());
                    }
                    document.getSubjectMatters().set(i, indexTerm);
                    if (!this.save(document)) {
                        logger.error("Failed to update the subject matter on a location document. Location id: {}", document.getLocationId());
                        success = false;
                    } else {
                        logger.debug("Subject matter updated on location document. Location id: {}", document.getLocationId());
                    }
                    break;
                }
            }
        }
        return success;
    }

    /**
     * Deletes the given Location from the search index. If the Locations is a
     * Library, all the collections and shelves are deleted too. If the Location
     * is a collection, all the shelves are deleted as well. Returns true if and
     * only if the Location was successfully deleted; otherwise false
     *
     * @param location Location to be deleted
     * @return true if and only if the Location was successfully deleted;
     * otherwise false
     */
    @Override
    public boolean delete(Location location) {
        try {
            logger.info("Start deleting Location from external search index. Id : {}", location.getLocationId());
            if (location.getLocationType() == LocationType.LIBRARY) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Location is a library. Delete shelves and collections before deleting the location itself.");
                }
                // Delete all the shelves that are under this Library
                List<LocationDocument> shelves = this.repository.findByOwnerIdAndGrandparentIdAndLocationType(location.getOwner().getId(), location.getLocationId(), LocationType.SHELF);
                logger.info("Number of shelves found : {}", shelves.size());
                for (LocationDocument shelf : shelves) {
                    this.repository.delete(shelf);
                    logger.debug("Shelf deleted. Id : {}", shelf.getLocationId());
                }
                // Delete all the collections
                List<LocationDocument> collections = this.repository.findByOwnerIdAndParentIdAndLocationType(location.getOwner().getId(), location.getLocationId(), LocationType.COLLECTION);
                logger.info("Number of collections found : {}", collections.size());
                for (LocationDocument collection : collections) {
                    this.repository.delete(collection);
                    logger.debug("Collection deleted. Id : {}", collection.getLocationId());
                }
            } else if (location.getLocationType() == LocationType.COLLECTION) {
                logger.debug("Location is a collection. Delete shelves before deleting the location itself.");
                // Delete all the shelves that belong to this collection
                List<LocationDocument> shelves = this.repository.findByOwnerIdAndParentIdAndLocationType(location.getOwner().getId(), location.getLocationId(), LocationType.SHELF);

                logger.info("Number of shelves found : {}", shelves.size());
                for (LocationDocument shelf : shelves) {
                    this.repository.delete(shelf);
                    logger.debug("Shelf deleted. Id : {}", shelf.getLocationId());
                }
            } else {
                logger.debug("Location is a shelf.");
            }
            // Delete the Location itself
            this.repository.delete(LocationDocumentBuilder.getId(location.getLocationId()));
            logger.debug("Location itself deleted. Id : {}", location.getLocationId());
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
            List<LocationDocument> list = this.repository.findByDocumentType(DocumentType.LOCATION);
            for (LocationDocument document : list) {
                this.repository.delete(document);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Updates the owner code's of all the LocationDocuments related to the
     * given owner. Returns true if and only if all the LocationDocuments
     * related to the given owner were successfully updated; otherwise false.
     *
     * @param ownerId ownerId of the Locations
     * @param ownerCode new owner code
     * @return true if and only if all the LocationDocuments related to the
     * given owner were successfully updated; otherwise false
     */
    @Override
    public boolean update(Integer ownerId, String ownerCode) {
        List<LocationDocument> list = this.repository.findByOwnerIdAndDocumentType(ownerId, DocumentType.LOCATION);
        boolean success = true;
        for (LocationDocument document : list) {
            document.setOwnerCode(ownerCode);
            try {
                // Save the LocationDocument to the index
                this.repository.save(document);
            } catch (Exception e) {
                logger.error("Failed to update location document's owner code. Location id: {}", document.getLocationId());
                logger.error(e.getMessage(), e);
                success = false;
            }
            logger.debug("Location document's owner code updated. Location id: {}", document.getLocationId());
        }
        return success;
    }

    /**
     * Finds the location with the given id and SearchLevel.
     *
     * @param id locationId of the location
     * @param level which location types are included in the search
     * @return location with the given id and search level
     */
    @Override
    public List<SimpleLocation> search(Integer id, SearchLevel level) {
        return (List) this.repository.findByLocationIdAndLocationType(id, SearchLevel.toLocationType(level));
    }

    /**
     * Returns all the locations related to the given owner that include the
     * given subject matter id.
     *
     * @param ownerId ownerId of the Locations
     * @param subjectMatterId id of the subject matter to be searched
     * @return list of matching locations
     */
    @Override
    public List<SimpleLocation> search(Integer ownerId, Integer subjectMatterId) {
        return (List) this.repository.findByOwnerIdAndDocumentTypeAndSubjectMatterIds(ownerId, DocumentType.LOCATION, subjectMatterId);
    }

    /**
     * Finds all the locations with the given ownerId and SearchLevel.
     *
     * @param ownerId ownerId of the Locations
     * @param level which location types are included to the results
     * @param sortDirection sort direction
     * @param sortField field which value is used for sorting
     * @return list of matching locations
     */
    @Override
    public List<SimpleLocation> search(Integer ownerId, SearchLevel level, String sortDirection, String sortField) {
        if (level == SearchLevel.ALL) {
            return (List) this.repository.findByOwnerIdAndDocumentType(ownerId, DocumentType.LOCATION, this.generateSort(sortDirection, sortField));
        } else {
            return (List) this.repository.findByOwnerIdAndLocationType(ownerId, SearchLevel.toLocationType(level), this.generateSort(sortDirection, sortField));
        }
    }

    /**
     * Finds all the collections or shelves with the given ownerId, parentId and
     * SearchLevel.
     *
     * @param ownerId ownerId of the Locations
     * @param parentId id of the location's parent
     * @param level which location types are included to the results
     * @param sortDirection sort direction
     * @param sortField field which value is used for sorting
     * @return list of matching collections or shelves
     */
    @Override
    public List<SimpleLocation> search(Integer ownerId, Integer parentId, SearchLevel level, String sortDirection, String sortField) {
        if (level == SearchLevel.ALL) {
            return new ArrayList<SimpleLocation>();
        } else {
            return (List) this.repository.findByOwnerIdAndParentIdAndLocationType(ownerId, parentId, SearchLevel.toLocationType(level), this.generateSort(sortDirection, sortField));
        }
    }

    /**
     * Finds all the shelves with the given ownerId, parentId and grandparentId.
     *
     * @param ownerId ownerId of the Locations
     * @param parentId id of the collection
     * @param grandparentId id of the library
     * @param sortDirection sort direction
     * @param sortField field which value is used for sorting
     * @return list of matching locations
     */
    @Override
    public List<SimpleLocation> search(Integer ownerId, Integer parentId, Integer grandparentId, String sortDirection, String sortField) {
        return (List) this.repository.findByOwnerIdAndParentIdAndGrandparentIdAndLocationType(ownerId, parentId, grandparentId, LocationType.SHELF, this.generateSort(sortDirection, sortField));
    }

    /**
     * Returns a Sort object that sorts the results in ascending order by call
     * number.
     *
     * @return Sort object that sorts the results in ascending order by call
     * number
     */
    private Sort sortByCallNumber() {
        return new Sort(Sort.Direction.ASC, "call_number");
    }

    /**
     * Returns a Sort object that sorts the results in ascending order by name.
     *
     * @return Sort object that sorts the results in ascending order by name
     */
    private Sort sortByName() {
        return new Sort(Sort.Direction.ASC, "name");
    }

    /**
     * Generates a new Sort object based on the given values.
     *
     * @param sortDirection sort direction
     * @param sortField field which value is used for sorting
     * @return new Sort object
     */
    private Sort generateSort(String sortDirection, String sortField) {
        if (sortDirection.equals(RepositoryConstants.SORT_DESC)) {
            return new Sort(Sort.Direction.DESC, sortField);
        } else {
            return new Sort(Sort.Direction.ASC, sortField);
        }
    }
}
