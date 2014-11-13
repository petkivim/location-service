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

import com.pkrete.locationservice.endpoint.model.location.Description;
import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.model.location.Note;
import com.pkrete.locationservice.endpoint.model.owner.Owner;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import com.pkrete.locationservice.endpoint.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.endpoint.model.search.Position;
import com.pkrete.locationservice.endpoint.model.search.SearchType;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class extends the abstract {@link Search Search} class and implements
 * the abstract search method. This class implements the search by
 * systematically enumerating all the possible locations and checking if they
 * match the given conditions.
 *
 * @author Petteri Kivimäki
 */
public class BruteForceSearch extends Search {

    private static final Logger logger = LoggerFactory.getLogger(BruteForceSearch.class.getName());

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
            builder.append("\", position : \"").append(position.toString());
            builder.append("\", children : ").append(children);
            logger.debug(builder.toString());
        }
        // Pattern for escaping regex special characters
        Pattern pattern = Pattern.compile("([\\\\*+\\[\\](){}\\$.?\\^|])");
        search = escapeRegex(search, pattern);

        logger.debug("Search string after escaping regex special characters: \"{}\".", search);

        // Get all the libraries related to the given owner.
        // All the collections and shelves related to the libraries
        // are loaded too.
        List<Library> libraries = dbService.getAllLocations((owner == null ? "" : owner.getCode()));
        List results = new ArrayList<Location>();

        logger.debug("Fetched {} libraries from the database. Start searching.", libraries.size());

        // Do search according to the search type
        if (type == SearchType.CALLNO) {
            results = getResultsByCallNo(libraries, buildSearchStr(search, position));
        } else if (type == SearchType.CODE) {
            results = getResultsByLocationCode(libraries, buildSearchStr(search, position));
        } else if (type == SearchType.ID) {
            results = getResultsByLocationId(libraries, search);
        } else if (type == SearchType.DESC) {
            results = getResultsByDesc(libraries, buildSearchStr(search, position));
        } else if (type == SearchType.NOTE) {
            results = getResultsByNote(libraries, buildSearchStr(search, position));
        } else if (type == SearchType.SUBJECT) {
            results = getResultsBySubjectMatter(libraries, buildSearchStr(search, position));
        } else if (type == SearchType.STAFFNOTE1) {
            results = getResultsByStaffNotePri(libraries, buildSearchStr(search, position));
        } else if (type == SearchType.STAFFNOTE2) {
            results = getResultsByStaffNoteSec(libraries, buildSearchStr(search, position));
        } else if (type == SearchType.FLOOR) {
            results = getResultsByFloor(libraries, buildSearchStr(search, position));
        } else if (type == SearchType.SHELF) {
            results = getResultsByShelfNumber(libraries, buildSearchStr(search, position));
        } else if (type == SearchType.ALL) {
            results = libraries;
        }

        logger.debug("Search completed. Found {} locations matching the conditions.", results.size());

        return results;
    }

    /**
     * Performs a search by call number. Goes through the given list of
     * libraries and their collections and shelves, and returns a list
     * containing all the Location objects which call number matches the given
     * search string.
     *
     * @param libraries list of libraries to be searched
     * @param searchStr search string
     * @return list of Locations which call number matches the search string
     */
    private List getResultsByCallNo(List<Library> libraries, String searchStr) {
        List results = new ArrayList<Location>();
        for (Library lib : libraries) {
            if (lib.getLocationCode().toUpperCase().matches(searchStr)) {
                results.add(lib);
            }
            for (LibraryCollection col : lib.getCollections()) {
                if (col.getCallNo().toUpperCase().matches(searchStr)) {
                    results.add(col);
                }
                for (Shelf shelf : col.getShelves()) {
                    if (shelf.getCallNo().toUpperCase().matches(searchStr)) {
                        results.add(shelf);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Performs a search by location code. Goes through the given list of
     * libraries and their collections and shelves, and returns a list
     * containing all the Location objects which location code matches the given
     * search string.
     *
     * @param libraries list of libraries to be searched
     * @param searchStr search string
     * @return list of Locations which location code matches the search string
     */
    private List getResultsByLocationCode(List<Library> libraries, String searchStr) {
        List results = new ArrayList<Location>();
        for (Library lib : libraries) {
            if (lib.getLocationCode().toUpperCase().matches(searchStr)) {
                results.add(lib);
            }
            for (LibraryCollection col : lib.getCollections()) {
                if (col.getLocationCode().toUpperCase().matches(searchStr)) {
                    results.add(col);
                }
                for (Shelf shelf : col.getShelves()) {
                    if (shelf.getLocationCode().toUpperCase().matches(searchStr)) {
                        results.add(shelf);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Performs a search by description. Goes through the given list of
     * libraries and their collections and shelves, and returns a list
     * containing all the Location objects which description matches the given
     * search string.
     *
     * @param libraries list of libraries to be searched
     * @param searchStr search string
     * @return list of Locations which description matches the search string
     */
    private List getResultsByDesc(List<Library> libraries, String searchStr) {
        List results = new ArrayList<Location>();
        for (Library lib : libraries) {
            if (!lib.getDescriptions().isEmpty()) {
                for (Description desc : lib.getDescriptions()) {
                    if (desc.getDescription().toUpperCase().matches(searchStr)) {
                        results.add(lib);
                    }
                }
            }
            for (LibraryCollection col : lib.getCollections()) {
                if (!col.getDescriptions().isEmpty()) {
                    for (Description desc : col.getDescriptions()) {
                        if (desc.getDescription().toUpperCase().matches(searchStr)) {
                            results.add(col);
                        }
                    }
                }
                for (Shelf shelf : col.getShelves()) {
                    if (!shelf.getDescriptions().isEmpty()) {
                        for (Description desc : shelf.getDescriptions()) {
                            if (desc.getDescription().toUpperCase().matches(searchStr)) {
                                results.add(shelf);
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    /**
     * Performs a search by note. Goes through the given list of libraries and
     * their collections and shelves, and returns a list containing all the
     * Location objects which note matches the given search string.
     *
     * @param libraries list of libraries to be searched
     * @param searchStr search string
     * @return list of Locations which note matches the search string
     */
    private List getResultsByNote(List<Library> libraries, String searchStr) {
        List results = new ArrayList<Location>();
        for (Library lib : libraries) {
            if (!lib.getNotes().isEmpty()) {
                for (Note note : lib.getNotes()) {
                    if (note.getNote().toUpperCase().matches(searchStr)) {
                        results.add(lib);
                    }
                }
            }
            for (LibraryCollection col : lib.getCollections()) {
                if (!col.getNotes().isEmpty()) {
                    for (Note note : col.getNotes()) {
                        if (note.getNote().toUpperCase().matches(searchStr)) {
                            results.add(col);
                        }
                    }
                }
                for (Shelf shelf : col.getShelves()) {
                    if (!shelf.getNotes().isEmpty()) {
                        for (Note note : shelf.getNotes()) {
                            if (note.getNote().toUpperCase().matches(searchStr)) {
                                results.add(shelf);
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    /**
     * Performs a search by subject matters. Goes through the given list of
     * libraries and their collections and shelves, and returns a list
     * containing all the Location objects which subject matters match the given
     * search string.
     *
     * @param libraries list of libraries to be searched
     * @param searchStr search string
     * @return list of Locations which subject matters match the search string
     */
    private List getResultsBySubjectMatter(List<Library> libraries, String searchStr) {
        List results = new ArrayList<Location>();
        for (Library lib : libraries) {
            for (LibraryCollection col : lib.getCollections()) {
                if (!col.getSubjectMatters().isEmpty()) {
                    for (SubjectMatter subject : col.getSubjectMatters()) {
                        if (subject.getIndexTerm().toUpperCase().matches(searchStr)) {
                            results.add(col);
                        }
                    }
                }
                for (Shelf shelf : col.getShelves()) {
                    if (!shelf.getSubjectMatters().isEmpty()) {
                        for (SubjectMatter subject : shelf.getSubjectMatters()) {
                            if (subject.getIndexTerm().toUpperCase().matches(searchStr)) {
                                results.add(shelf);
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    /**
     * Performs a search by primary staff note. Goes through the given list of
     * libraries and their collections and shelves, and returns a list
     * containing all the Location objects which primary staff note matches the
     * given search string.
     *
     * @param libraries list of libraries to be searched
     * @param searchStr search string
     * @return list of Locations which primary staff note matches the search
     * string
     */
    private List getResultsByStaffNotePri(List<Library> libraries, String searchStr) {
        List results = new ArrayList<Location>();
        for (Library lib : libraries) {
            if (lib.getStaffNotePri().toUpperCase().matches(searchStr)) {
                results.add(lib);
            }
            for (LibraryCollection col : lib.getCollections()) {
                if (col.getStaffNotePri().toUpperCase().matches(searchStr)) {
                    results.add(col);
                }
                for (Shelf shelf : col.getShelves()) {
                    if (shelf.getStaffNotePri().toUpperCase().matches(searchStr)) {
                        results.add(shelf);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Performs a search by secondary staff note. Goes through the given list of
     * libraries and their collections and shelves, and returns a list
     * containing all the Location objects which secondary staff note matches
     * the given search string.
     *
     * @param libraries list of libraries to be searched
     * @param searchStr search string
     * @return list of Locations which secondary staff note matches the search
     * string
     */
    private List getResultsByStaffNoteSec(List<Library> libraries, String searchStr) {
        List results = new ArrayList<Location>();
        for (Library lib : libraries) {
            if (lib.getStaffNoteSec().toUpperCase().matches(searchStr)) {
                results.add(lib);
            }
            for (LibraryCollection col : lib.getCollections()) {
                if (col.getStaffNoteSec().toUpperCase().matches(searchStr)) {
                    results.add(col);
                }
                for (Shelf shelf : col.getShelves()) {
                    if (shelf.getStaffNoteSec().toUpperCase().matches(searchStr)) {
                        results.add(shelf);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Performs a search by location id. Goes through the given list of
     * libraries and their collections and shelves, and returns a list
     * containing all the Location objects which location id matches the given
     * search string.
     *
     * @param libraries list of libraries to be searched
     * @param searchStr search string
     * @return list of Locations which location id matches the search string
     */
    private List getResultsByLocationId(List<Library> libraries, String searchStr) {
        List results = new ArrayList<Location>();
        int id = 0;
        try {
            id = Integer.parseInt(searchStr);
        } catch (NumberFormatException nfe) {
            return results;
        }

        for (Library lib : libraries) {
            if (lib.getLocationId() == id) {
                results.add(lib);
                return results;
            }
            for (LibraryCollection col : lib.getCollections()) {
                if (col.getLocationId() == id) {
                    results.add(col);
                    return results;
                }
                for (Shelf shelf : col.getShelves()) {
                    if (shelf.getLocationId() == id) {
                        results.add(shelf);
                        return results;
                    }
                }
            }
        }
        return results;
    }

    /**
     * Performs a search by floor. Goes through the given list of libraries and
     * their collections and shelves, and returns a list containing all the
     * Location objects which floor attribute matches the given search string.
     *
     * @param libraries list of libraries to be searched
     * @param searchStr search string
     * @return list of Locations which floor attribute matches the search string
     */
    private List getResultsByFloor(List<Library> libraries, String searchStr) {
        List results = new ArrayList<Location>();
        for (Library lib : libraries) {
            if (lib.getFloor().toUpperCase().matches(searchStr)) {
                results.add(lib);
            }
            for (LibraryCollection col : lib.getCollections()) {
                if (col.getFloor().toUpperCase().matches(searchStr)) {
                    results.add(col);
                }
                for (Shelf shelf : col.getShelves()) {
                    if (shelf.getFloor().toUpperCase().matches(searchStr)) {
                        results.add(shelf);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Performs a search by shelf number. Goes through the given list of
     * libraries and their collections and shelves, and returns a list
     * containing all the Location objects which floor attribute matches the
     * given search string.
     *
     * @param libraries list of libraries to be searched
     * @param searchStr search string
     * @return list of Locations which shelf number matches the search string
     */
    private List getResultsByShelfNumber(List<Library> libraries, String searchStr) {
        List results = new ArrayList<Location>();
        for (Library lib : libraries) {
            for (LibraryCollection col : lib.getCollections()) {
                if (col.getShelfNumber().toUpperCase().matches(searchStr)) {
                    results.add(col);
                }
                for (Shelf shelf : col.getShelves()) {
                    if (shelf.getShelfNumber().toUpperCase().matches(searchStr)) {
                        results.add(shelf);
                    }
                }
            }
        }
        return results;
    }
}
