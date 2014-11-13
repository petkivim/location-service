/**
 * This file is part of Location Service :: Endpoint. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.search;

import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.model.owner.Owner;
import com.pkrete.locationservice.endpoint.model.search.LocationType;
import com.pkrete.locationservice.endpoint.model.search.Position;
import com.pkrete.locationservice.endpoint.model.search.SearchType;
import com.pkrete.locationservice.endpoint.solr.model.LocationDocument;
import com.pkrete.locationservice.endpoint.solr.service.LocationIndexService;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class extends the abstract {@link Search Search} class and implements
 * the abstract search method. This class implements the search by
 * systematically enumerating all the possible locations and checking if they
 * match the given conditions. The search is done against Solr and only the
 * locations matching the conditions are fully loaded from the db.
 *
 * @author Petteri Kivimäki
 */
public class SolrBruteForceSearch extends Search {

    private static final Logger logger = LoggerFactory.getLogger(SolrBruteForceSearch.class.getName());
    private LocationIndexService locationIndexService;

    /**
     * Sets the location index service variable value.
     *
     * @param locationIndexService new value
     */
    public void setLocationIndexService(LocationIndexService locationIndexService) {
        this.locationIndexService = locationIndexService;
    }

    /**
     * Searches locations matching the given conditions. The search is
     * implemented by systematically enumerating all the possible locations and
     * checking if they match the given conditions.
     *
     * @param search search string
     * @param position position of the search string in the target field
     * indicated by the type
     * @param type type of the search - target field
     * @param owner owner of the locations
     * @param children children if true, all the sub locations are included
     * @return list of matching locations
     */
    @Override
    public List<Location> search(String search, Position position, SearchType type, Owner owner, boolean children) {
        if (logger.isDebugEnabled()) {
            StringBuilder builder = new StringBuilder("Exporter search - search string : \"");
            builder.append(search).append("\", type : \"").append(type);
            builder.append("\", owner : \"").append(owner.getCode());
            builder.append("\", position : ").append(position.toString());
            builder.append("\", children : ").append(children);
            logger.debug(builder.toString());
        }

        // Variable for results
        List<Location> results;

        // If search type is all, no need to continue
        // Just return all the locations related to the owner
        if (type == SearchType.ALL) {
            results = (List) this.dbService.getAllLocations(owner.getCode());
            logger.debug("Found {} locations matching the conditions.", results.size());
            return results;
        }

        // Pattern for escaping regex special characters
        Pattern pattern = Pattern.compile("([\\\\*+\\[\\](){}\\$.?\\^|])");
        search = escapeRegex(search, pattern);

        logger.debug("Search string after escaping regex special characters: \"{}\"", search);

        //
        // Get all the location documents related to the given owner.
        List<LocationDocument> locations = (List) this.locationIndexService.getAllLocations(owner.getCode());
        // List for temp results
        List tempResults = new ArrayList<LocationDocument>();

        logger.debug("Fetched {} LocationDocuments from Solr. Start searching.", locations.size());

        // Do search according to the search type
        if (type == SearchType.CALLNO) {
            tempResults = getResultsByCallNo(locations, buildSearchStr(search, position));
        } else if (type == SearchType.CODE) {
            tempResults = getResultsByLocationCode(locations, buildSearchStr(search, position));
        } else if (type == SearchType.ID) {
            tempResults = getResultsByLocationId(locations, search);
        } else if (type == SearchType.DESC) {
            tempResults = getResultsByDesc(locations, buildSearchStr(search, position));
        } else if (type == SearchType.NOTE) {
            tempResults = getResultsByNote(locations, buildSearchStr(search, position));
        } else if (type == SearchType.SUBJECT) {
            tempResults = getResultsBySubjectMatter(locations, buildSearchStr(search, position));
        } else if (type == SearchType.STAFFNOTE1) {
            tempResults = getResultsByStaffNotePri(locations, buildSearchStr(search, position));
        } else if (type == SearchType.STAFFNOTE2) {
            tempResults = getResultsByStaffNoteSec(locations, buildSearchStr(search, position));
        } else if (type == SearchType.FLOOR) {
            tempResults = getResultsByFloor(locations, buildSearchStr(search, position));
        } else if (type == SearchType.SHELF) {
            tempResults = getResultsByShelfNumber(locations, buildSearchStr(search, position));
        }

        logger.debug("Search completed. Found {} LocationDocuments matching the conditions.", tempResults.size());
        logger.debug("Fetch full Location objects from the database.");

        // Get library, collection and shelf ids
        List<Integer> libraryIds = this.getIds(tempResults, LocationType.LIBRARY);
        List<Integer> collectionIds = this.getIds(tempResults, LocationType.COLLECTION);
        List<Integer> schelfIds = this.getIds(tempResults, LocationType.SHELF);
        // Get full Location objects from the db
        results = this.dbService.getLocations(libraryIds, collectionIds, schelfIds, children);

        logger.debug("Fetched {} full Location objects from the database.", results.size());

        return results;
    }

    /**
     * Performs a search by call number. Goes through the given list of location
     * documents and returns a list containing all the LocationDocument objects
     * which call number matches the given search string.
     *
     * @param locations list of locations to be searched
     * @param searchStr search string
     * @return list of LocationDocuments which call number matches the search
     * string
     */
    private List getResultsByCallNo(List<LocationDocument> locations, String searchStr) {
        List results = new ArrayList<LocationDocument>();
        for (LocationDocument doc : locations) {
            if (doc.getCallNo().toUpperCase().matches(searchStr)) {
                results.add(doc);
            }
        }
        return results;
    }

    /**
     * Performs a search by location code. Goes through the given list of
     * location documents and returns a list containing all the LocationDocument
     * objects which location code matches the given search string.
     *
     * @param locations list of locations to be searched
     * @param searchStr search string
     * @return list of LocationDocuments which location code matches the search
     * string
     */
    private List getResultsByLocationCode(List<LocationDocument> locations, String searchStr) {
        List results = new ArrayList<LocationDocument>();
        for (LocationDocument doc : locations) {
            if (doc.getLocationCode().toUpperCase().matches(searchStr)) {
                results.add(doc);
            }
        }
        return results;
    }

    /**
     * Performs a search by description. Goes through the given list of location
     * documents and returns a list containing all the LocationDocument objects
     * which descriptions match the given search string.
     *
     * @param locations list of locations to be searched
     * @param searchStr search string
     * @return list of LocationDocuments which descriptions match the search
     * string
     */
    private List getResultsByDesc(List<LocationDocument> locations, String searchStr) {
        List results = new ArrayList<LocationDocument>();
        for (LocationDocument doc : locations) {
            if (doc.getDescriptions() != null && !doc.getDescriptions().isEmpty()) {
                for (String desc : doc.getDescriptions()) {
                    if (desc.toUpperCase().matches(searchStr)) {
                        results.add(doc);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Performs a search by note. Goes through the given list of location
     * documents and returns a list containing all the LocationDocument objects
     * which notes match the given search string.
     *
     * @param locations list of locations to be searched
     * @param searchStr search string
     * @return list of LocationDocuments which notes match the search string
     */
    private List getResultsByNote(List<LocationDocument> locations, String searchStr) {
        List results = new ArrayList<LocationDocument>();
        for (LocationDocument doc : locations) {
            if (doc.getNotes() != null && !doc.getNotes().isEmpty()) {
                for (String note : doc.getNotes()) {
                    if (note.toUpperCase().matches(searchStr)) {
                        results.add(doc);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Performs a search by subject matters. Goes through the given list of
     * location documents and returns a list containing all the LocationDocument
     * objects which subject matters match the given search string.
     *
     * @param locations list of locations to be searched
     * @param searchStr search string
     * @return list of LocationDocuments which subject matters match the search
     * string
     */
    private List getResultsBySubjectMatter(List<LocationDocument> locations, String searchStr) {
        List results = new ArrayList<LocationDocument>();
        for (LocationDocument doc : locations) {
            if (doc.getSubjectMatters() != null && !doc.getSubjectMatters().isEmpty()) {
                for (String subject : doc.getSubjectMatters()) {
                    if (subject.toUpperCase().matches(searchStr)) {
                        results.add(doc);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Performs a search by primary staff note. Goes through the given list of
     * location documents and returns a list containing all the LocationDocument
     * objects which primary staff note matches the given search string.
     *
     * @param locations list of locations to be searched
     * @param searchStr search string
     * @return list of LocationDocuments which primary staff note matches the
     * search string
     */
    private List getResultsByStaffNotePri(List<LocationDocument> locations, String searchStr) {
        List results = new ArrayList<LocationDocument>();
        for (LocationDocument doc : locations) {
            if (doc.getStaffNotePri() != null && doc.getStaffNotePri().toUpperCase().matches(searchStr)) {
                results.add(doc);
            }
        }
        return results;
    }

    /**
     * Performs a search by secondary staff note. Goes through the given list of
     * location documents and returns a list containing all the LocationDocument
     * objects which secondary staff note matches the given search string.
     *
     * @param locations list of locations to be searched
     * @param searchStr search string
     * @return list of LocationDocuments which secondary staff note matches the
     * search string
     */
    private List getResultsByStaffNoteSec(List<LocationDocument> locations, String searchStr) {
        List results = new ArrayList<LocationDocument>();
        for (LocationDocument doc : locations) {
            if (doc.getStaffNoteSec() != null && doc.getStaffNoteSec().toUpperCase().matches(searchStr)) {
                results.add(doc);
            }
        }
        return results;
    }

    /**
     * Performs a search by location id. Goes through the given list of location
     * documents and returns a list containing all the LocationDocument objects
     * which location id matches the given search string.
     *
     * @param locations list of locations to be searched
     * @param searchStr search string
     * @return list of LocationDocuments which location id matches the search
     * string
     */
    private List getResultsByLocationId(List<LocationDocument> locations, String searchStr) {
        List results = new ArrayList<LocationDocument>();
        int id = 0;
        try {
            id = Integer.parseInt(searchStr);
        } catch (NumberFormatException nfe) {
            return results;
        }

        for (LocationDocument doc : locations) {
            if (doc.getLocationId() == id) {
                results.add(doc);
                return results;
            }
        }
        return results;
    }

    /**
     * Performs a search by floor. Goes through the given list of location
     * documents and returns a list containing all the LocationDocument objects
     * which floor matches the given search string.
     *
     * @param locations list of locations to be searched
     * @param searchStr search string
     * @return list of LocationDocuments which floor attribute matches the
     * search string
     */
    private List getResultsByFloor(List<LocationDocument> locations, String searchStr) {
        List results = new ArrayList<LocationDocument>();
        for (LocationDocument doc : locations) {
            if (doc.getFloor() != null && doc.getFloor().toUpperCase().matches(searchStr)) {
                results.add(doc);
            }
        }
        return results;
    }

    /**
     * Performs a search by shelf number. Goes through the given list of
     * location documents and returns a list containing all the LocationDocument
     * objects which shelf number matches the given search string.
     *
     * @param locations list of locations to be searched
     * @param searchStr search string
     * @return list of LocationDocuments which shelf number matches the search
     * string
     */
    private List getResultsByShelfNumber(List<LocationDocument> locations, String searchStr) {
        List results = new ArrayList<LocationDocument>();
        for (LocationDocument doc : locations) {
            if (doc.getShelf() != null && doc.getShelf().toUpperCase().matches(searchStr)) {
                results.add(doc);
            }
        }
        return results;
    }

    /**
     * Returns a list of ids of locations representing the given location type.
     * Goes through the given list of LocationDocuments and picks the location
     * ids representing the given type.
     *
     * @param documents list of LocationDocuments to be checked
     * @param type LocationType to be searched
     * @return list of ids
     */
    private List<Integer> getIds(List<LocationDocument> documents, LocationType type) {
        List<Integer> result = new ArrayList<Integer>();
        for (LocationDocument doc : documents) {
            if (doc.getLocationType() == type) {
                result.add(doc.getLocationId());
            }
        }
        return result;
    }
}
