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
package com.pkrete.locationservice.endpoint.dao.locations;

import com.pkrete.locationservice.endpoint.modifier.CallnoModification;
import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.callnoparser.LocatingStrategy;
import com.pkrete.locationservice.endpoint.dao.Dao;
import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.model.owner.Owner;
import com.pkrete.locationservice.endpoint.statistics.SearchEvent;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import com.pkrete.locationservice.endpoint.model.search.LocationType;
import com.pkrete.locationservice.endpoint.model.search.SearchIndex;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * LocationDao class implements the Dao interface implementing all the methods
 * defined in the interface. LocationDao implements database operations
 * by using the Hibernate and Spring frameworks. Classes implementing
 * the Service interface can use LocationDao for database operations.
 * 
 * This class extends {@link HibernateDaoSupport HibernateDaoSupport} class 
 * that is a wrapper over {@link HibernateTemplate HibernateTemplate} class. 
 * HibernateTemplate is a convenience class for Hibernate based database access. 
 * HibernateDaoSupport creates the HibernateTemplate and subclasses can use 
 * the getHibernateTemplate() method to obtain the hibernateTemplate and 
 * then perform operations on it. HibernateTemplate takes care of obtaining or 
 * releasing sessions and managing exceptions. 
 * 
 * @author Petteri Kivimäki
 */
public class LocationsDao extends HibernateDaoSupport implements Dao {

    private final static Logger localLogger = Logger.getLogger(LocationsDao.class.getName());

    /**
     * Saves the given search event to the database. Returns true if and
     * only the the search event was successfully added, otherwise false.
     * @param event search event to be saved
     * @return true if and only if the event was successfully added;
     * otherwise false
     */
    @Override
    public boolean save(SearchEvent event) {
        try {
            getHibernateTemplate().save(event);
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Returns a list of libraries which call numbers match with the given
     * call number and that are related to the given owner.
     * @param callno the call number that is used for searching
     * @param owner owner of the object
     * @return the libraries with the desired call number
     */
    @Override
    public List getLibrary(String callno, String owner) {
        List list = getHibernateTemplate().find(
                "from Library as library where library.owner.code = ? "
                + "and library.locationCode = ?", owner, callno);
        return list;
    }

    /**
     * Returns a list of libraries that are related to the given owner.
     * @param owner owner of the object
     * @return libraries that are related to the given owner
     */
    @Override
    public List getLibraries(String owner) {
        List list = getHibernateTemplate().find(
                "from Library as library where library.owner.code = ?", owner);
        return list;
    }

    /**
     * Returns a list of search index entries related to libraries that 
     * belong to the given owner.
     * @param owner owner of the object
     * @return search index entries that are related to the given owner
     */
    @Override
    public List<SearchIndex> getLibrariesFromIndex(String owner) {
        List<SearchIndex> list = getHibernateTemplate().find(
                "from SearchIndex si where si.locationType = '"
                + LocationType.LIBRARY + "' and si.owner.code = ? "
                + "order by si.callNo desc", owner);
        return list;
    }

    /**
     * Returns the library which location id mathes the given id. All
     * the lazy relationships are loaded.
     * @param id location id to be searched
     * @return library matching the given location id
     */
    @Override
    public Library getLibrary(int id) {
        List<Library> list = getHibernateTemplate().find(
                "from Library as lib "
                + "left join fetch lib.image "
                + "left join fetch lib.map "
                + "left join fetch lib.areas "
                + "where lib.locationId = ?", id);
        if (list.isEmpty()) {
            return null;
        }
        Library lib = list.get(0);
        Hibernate.initialize(lib.getNotes());
        Hibernate.initialize(lib.getDescriptions());
        return lib;
    }

    /**
     * Returns all the libraries which id is in the given list. All
     * the lazy relationships are loaded. If children is true, all the 
     * collections and shelves belonging to the libraries are loaded too.
     * @param ids list of ids
     * @param children if true, all the collections and shelves belonging to 
     * the libraries are loaded
     * @return libraries matching the given ids
     */
    @Override
    public List<Library> getLibraries(List<Integer> ids, boolean children) {
        List<Library> result = getHibernateTemplate().findByNamedParam(
                "select distinct lib from Library as lib "
                + "left join fetch lib.image "
                + "left join fetch lib.map "
                + "left join fetch lib.areas "
                + "where lib.locationId in (:ids) ", "ids", ids);
        for (Library lib : result) {
            Hibernate.initialize(lib.getNotes());
            Hibernate.initialize(lib.getDescriptions());
            if (children) {
                for (LibraryCollection col : lib.getCollections()) {
                    Hibernate.initialize(col.getNotes());
                    Hibernate.initialize(col.getDescriptions());
                    Hibernate.initialize(col.getSubjectMatters());
                    Hibernate.initialize(col.getAreas());
                    Hibernate.initialize(col.getImage());
                    Hibernate.initialize(col.getMap());
                    for (Shelf shelf : col.getShelves()) {
                        Hibernate.initialize(shelf.getNotes());
                        Hibernate.initialize(shelf.getDescriptions());
                        Hibernate.initialize(shelf.getSubjectMatters());
                        Hibernate.initialize(shelf.getAreas());
                        Hibernate.initialize(shelf.getImage());
                        Hibernate.initialize(shelf.getMap());
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns a list of collection which call numbers match with the given
     * call number and that are related to the given owner.
     * @param callno the call number that is used for searching
     * @param owner owner of the object
     * @return the collections with the desired call number
     */
    @Override
    public List getCollection(String callno, String owner) {
        List list = getHibernateTemplate().find(
                "from LibraryCollection as collection where "
                + "collection.owner.code = ? and "
                + "collection.locationCode = ?", owner, callno);
        return list;
    }

    /**
     * Returns a list of collections that are related to the given owner.
     * @param owner owner of the object
     * @return the collections related to the given owner
     */
    @Override
    public List getCollections(String owner) {
        List list = getHibernateTemplate().find(
                "from LibraryCollection as collection where "
                + "collection.owner.code = ?", owner);
        return list;
    }

    /**
     * Returns a list of search index entries related to collections that 
     * belong to the given owner.
     * @param owner owner of the object
     * @return search index entries that are related to the given owner
     */
    @Override
    public List<SearchIndex> getCollectionsFromIndex(String owner) {
        List<SearchIndex> list = getHibernateTemplate().find(
                "from SearchIndex si where si.locationType = '"
                + LocationType.COLLECTION + "' and si.owner.code = ? "
                + "order by si.callNo desc", owner);
        return list;
    }

    /**
     * Returns all the collections that are related to the library which
     * locationId matches with the given id number.
     * @param id the locationId that is used for searching
     * @param owner the owner of the object
     * @return the collection with the desired locationId
     */
    @Override
    public List<Location> getCollectionsByLibraryId(int id, String owner) {
        List<Location> list = getHibernateTemplate().find(
                "from LibraryCollection as collection where "
                + "collection.owner.code = ? and "
                + "and library.locationId = ?", owner, id);
        return list;
    }

    /**
     * Returns the collection which location id mathes the given id. All
     * the lazy relationships are loaded.
     * @param id location id to be searched
     * @return collection matching the given location id
     */
    @Override
    public LibraryCollection getCollection(int id) {
        List<LibraryCollection> list = getHibernateTemplate().find(
                "from LibraryCollection as loc "
                + "left join fetch loc.image "
                + "left join fetch loc.map "
                + "left join fetch loc.areas "
                + "where loc.locationId = ?", id);
        if (list.isEmpty()) {
            return null;
        }
        LibraryCollection col = list.get(0);
        Hibernate.initialize(col.getNotes());
        Hibernate.initialize(col.getDescriptions());
        Hibernate.initialize(col.getSubjectMatters());
        Hibernate.initialize(col.getLibrary().getNotes());
        Hibernate.initialize(col.getLibrary().getDescriptions());
        Hibernate.initialize(col.getLibrary().getAreas());
        Hibernate.initialize(col.getLibrary().getImage());
        Hibernate.initialize(col.getLibrary().getMap());
        return col;
    }

    /**
     * Returns all the collections which id is in the given list. All
     * the lazy relationships are loaded. If chindren is true, all the shelves 
     * belonging to the collections are loaded too.
     * @param ids list of ids
     * @param children if true, all the shelves belonging to the collections
     * are loaded
     * @return collections matching the given ids
     */
    @Override
    public List<LibraryCollection> getCollections(List<Integer> ids, boolean children) {
        List<LibraryCollection> list = getHibernateTemplate().findByNamedParam(
                "select distinct loc from LibraryCollection as loc "
                + "left join fetch loc.image "
                + "left join fetch loc.map "
                + "left join fetch loc.areas "
                + "where loc.locationId in (:ids) ", "ids", ids);
        for (LibraryCollection col : list) {
            Hibernate.initialize(col.getNotes());
            Hibernate.initialize(col.getDescriptions());
            Hibernate.initialize(col.getSubjectMatters());
            Hibernate.initialize(col.getLibrary());
            if (children) {
                for (Shelf shelf : col.getShelves()) {
                    Hibernate.initialize(shelf.getNotes());
                    Hibernate.initialize(shelf.getDescriptions());
                    Hibernate.initialize(shelf.getSubjectMatters());
                    Hibernate.initialize(shelf.getAreas());
                    Hibernate.initialize(shelf.getImage());
                    Hibernate.initialize(shelf.getMap());
                }
            }
        }
        return list;
    }

    /**
     * Returns a list of shelves which call numbers match with the given call number
     * and that are related to the given owner.
     * @param callno the call number that is used for searching
     * @param owner owner of the object
     * @return shelves with the desired call number
     */
    @Override
    public List getShelf(String callno, String owner) {
        List list = getHibernateTemplate().find(
                "from Shelf as shelf where shelf.owner.code = ? "
                + "and shelf.locationCode = ?", owner, callno);
        return list;
    }

    /**
     * Returns a list of shelves that are related to the given owner.
     * @param owner owner of the object
     * @return shelves that are related to the given owner
     */
    @Override
    public List getShelves(String owner) {
        List list = getHibernateTemplate().find(
                "from Shelf as shelf where shelf.owner.code = ?", owner);
        return list;
    }

    /**
     * Returns a list of search index entries related to shelves that 
     * belong to the given owner.
     * @param owner owner of the object
     * @return search index entries that are related to the given owner
     */
    @Override
    public List<SearchIndex> getShelvesFromIndex(String owner) {
        List<SearchIndex> list = getHibernateTemplate().find(
                "from SearchIndex si where si.locationType = '"
                + LocationType.SHELF + "' and si.owner.code = ? "
                + "order by si.callNo desc", owner);
        return list;
    }

    /**
     * Returns all the shelves that are related to the collection which
     * locationId matches with the given id number.
     * @param id the locationId that is used for searching
     * @param owner the owner of the object
     * @return the shelf with the desired locationId
     */
    @Override
    public List<Location> getShelvesByCollectionId(int id, String owner) {
        List<Location> list = getHibernateTemplate().find(
                "from Shelf as shelf "
                + "where shelf.owner.code = ? "
                + "and collection.locationId = ? "
                + "order by shelf.locationCode desc", owner, id);
        return list;
    }

    /**
     * Returns the shelf which location id matches the given id. All
     * the lazy relationships are loaded.
     * @param id location id to be searched
     * @return shelf matching the given location id
     */
    @Override
    public Shelf getShelf(int id) {
        List<Shelf> list = getHibernateTemplate().find(
                "from Shelf as loc "
                + "left join fetch loc.image "
                + "left join fetch loc.map "
                + "left join fetch loc.areas "
                + "where loc.locationId = ?", id);
        if (list.isEmpty()) {
            return null;
        }
        Shelf shelf = list.get(0);
        Hibernate.initialize(shelf.getNotes());
        Hibernate.initialize(shelf.getDescriptions());
        Hibernate.initialize(shelf.getSubjectMatters());
        Hibernate.initialize(shelf.getCollection().getNotes());
        Hibernate.initialize(shelf.getCollection().getDescriptions());
        Hibernate.initialize(shelf.getCollection().getSubjectMatters());
        Hibernate.initialize(shelf.getCollection().getAreas());
        Hibernate.initialize(shelf.getCollection().getImage());
        Hibernate.initialize(shelf.getCollection().getMap());
        Hibernate.initialize(shelf.getCollection().getLibrary().getNotes());
        Hibernate.initialize(shelf.getCollection().getLibrary().getDescriptions());
        Hibernate.initialize(shelf.getCollection().getLibrary().getAreas());
        Hibernate.initialize(shelf.getCollection().getLibrary().getImage());
        Hibernate.initialize(shelf.getCollection().getLibrary().getMap());
        return shelf;
    }

    /**
     * Returns all the shelves which id is in the given list. All
     * the lazy relationships are loaded.
     * @param ids list of ids
     * @return shelves matching the given ids
     */
    @Override
    public List<Shelf> getShelves(List<Integer> ids) {
        List<Shelf> list = getHibernateTemplate().findByNamedParam(
                "select distinct loc from Shelf as loc "
                + "left join fetch loc.image "
                + "left join fetch loc.map "
                + "left join fetch loc.areas "
                + "where loc.locationId in (:ids)", "ids", ids);
        for (Shelf shelf : list) {
            Hibernate.initialize(shelf.getNotes());
            Hibernate.initialize(shelf.getDescriptions());
            Hibernate.initialize(shelf.getSubjectMatters());
            Hibernate.initialize(shelf.getCollection());
            Hibernate.initialize(shelf.getCollection().getLibrary());
        }
        return list;
    }

    /**
     * Returns the location which id number matches with given id number
     * and given owner.
     * @param locationId the id number that is used for searching
     * @param owner owner of the object
     * @return the location with the desired id number
     */
    @Override
    public Location getLocation(int locationId, String owner) {
        List<Location> result = null;
        try {
            result = getHibernateTemplate().find("from Shelf as shelf join fetch shelf.map left join fetch shelf.areas where shelf.owner.code = ? and shelf.locationId =?", owner, locationId);
            if (result.isEmpty()) {
                result = getHibernateTemplate().find("from LibraryCollection as collection join fetch collection.map left join fetch collection.areas where collection.owner.code = ? and collection.locationId =?", owner, locationId);
                if (result.isEmpty()) {
                    result = getHibernateTemplate().find("from Library as library join fetch library.map left join fetch library.areas where library.owner.code = ? and library.locationId =?", owner, locationId);
                }
            }
        } catch (Exception e) {
            localLogger.error(e);
        }
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    /**
     * Returns a list of collections which shelves location code is a substring placed
     * in the beginning of a string.
     * @param owner owner of the location
     * @return list of locations matching the condition
     */
    @Override
    public List<Location> getSubstringLocations(String owner) {
        List<Location> result = getHibernateTemplate().find("from LibraryCollection c "
                + "where c.isSubstring = true and c.owner.code = ?", owner);
        return result;
    }

    /**
     * Returns a list of collections which shelves location code is a substring placed
     * in the beginning of a string.
     * @param owner owner of the location
     * @param collectionCode collection code of the location
     * @return list of locations matching the condition
     */
    @Override
    public List<Location> getSubstringLocations(String owner, String collectionCode) {
        List<Location> result = getHibernateTemplate().find("from LibraryCollection c "
                + "where c.isSubstring = true and c.owner.code = ? and "
                + "c.collectionCode = ?", owner, collectionCode);
        return result;
    }

    /**
     * Returns a list of shelves that belong to the collection with the given
     * collection code.
     * @param owner owner of the location
     * @param collectionCode collection code of the collection in which the shelf belongs
     * @return list of shelves matching the condition
     */
    @Override
    public List<Location> getShelvesByCollectionCode(String owner, String collectionCode) {
        List<Location> result = getHibernateTemplate().find("from Shelf s "
                + "where s.collection.collectionCode = ? "
                + "and s.owner.code = ?", collectionCode, owner);
        return result;
    }

    /**
     * Returns the collection with the given collection code. If the collection
     * with the given code doesn't exist, null is returned.
     * @param owner owner of the location
     * @param collectionCode collection code of the collection to be searched
     * @return collection matching the given code or null
     */
    @Override
    public LibraryCollection getCollectionByCollectionCode(String owner, String collectionCode) {
        List<LibraryCollection> result = getHibernateTemplate().find("from LibraryCollection c "
                + "left join fetch c.image "
                + "left join fetch c.map "
                + "left join fetch c.areas "
                + "where c.collectionCode = ? "
                + "and c.owner.code = ?", collectionCode, owner);
        if (result.isEmpty()) {
            return null;
        }
        LibraryCollection col = result.get(0);
        Hibernate.initialize(col.getNotes());
        Hibernate.initialize(col.getDescriptions());
        Hibernate.initialize(col.getSubjectMatters());
        Hibernate.initialize(col.getLibrary().getNotes());
        Hibernate.initialize(col.getLibrary().getDescriptions());
        Hibernate.initialize(col.getLibrary().getAreas());
        Hibernate.initialize(col.getLibrary().getImage());
        Hibernate.initialize(col.getLibrary().getMap());
        return col;
    }

    /**
     * Returns a list of all the libraries in the database that are related to
     * the given owner. All the lazy relationships are loaded.
     * @param owner owner of the object
     * @return all the libraries in the database
     */
    @Override
    public List<Library> getAllLocations(String owner) {
        List<Library> result = getHibernateTemplate().find(
                "select distinct lib from Library lib "
                + "left join fetch lib.image "
                + "left join fetch lib.map "
                + "left join fetch lib.areas "
                + "where lib.owner.code = ?", owner);
        for (Library lib : result) {
            Hibernate.initialize(lib.getNotes());
            Hibernate.initialize(lib.getDescriptions());
            for (LibraryCollection col : lib.getCollections()) {
                Hibernate.initialize(col.getNotes());
                Hibernate.initialize(col.getDescriptions());
                Hibernate.initialize(col.getSubjectMatters());
                Hibernate.initialize(col.getAreas());
                Hibernate.initialize(col.getImage());
                Hibernate.initialize(col.getMap());
                for (Shelf shelf : col.getShelves()) {
                    Hibernate.initialize(shelf.getNotes());
                    Hibernate.initialize(shelf.getDescriptions());
                    Hibernate.initialize(shelf.getSubjectMatters());
                    Hibernate.initialize(shelf.getAreas());
                    Hibernate.initialize(shelf.getImage());
                    Hibernate.initialize(shelf.getMap());
                }
            }
        }
        return result;
    }

    /**
     * Returns the owner with the given code.
     * @param code the code that is used for searching
     * @return the owner with the given code
     */
    @Override
    public Owner getOwnerByCode(String code) {
        List<Owner> list = getHibernateTemplate().find(
                "from Owner owner where owner.code = ?", code);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns the locating strategy defined for the given owner. If no
     * strategy is found, null is returned.
     * @param owner owner of the object
     * @return locating strategy defined for the given owner or null, if
     * no strategy is found
     */
    @Override
    public LocatingStrategy getLocatingStrategy(String owner) {
        List<LocatingStrategy> strategies = getHibernateTemplate().find(
                "select locatingStrategy from Owner where code = ?", owner);
        if (strategies.isEmpty()) {
            return null;
        }
        return strategies.get(0);
    }

    /**
     * Returns a list of not found redirect objects related to
     * the given owner.
     * @param owner owner of the not found redirects
     * @return list of not found redirects related to the given owner
     */
    @Override
    public List<CallnoModification> getNotFoundRedirects(String owner) {
        List<CallnoModification> list = getHibernateTemplate().find(
                "from NotFoundRedirect redirect "
                + "where redirect.owner.code = ? "
                + "and redirect.isActive = true", owner);
        return list;
    }

    /**
     * Returns a list of preprocessing redirect objects related to
     * the given owner.
     * @param owner owner of the preprocessing redirects
     * @return list of preprocessing redirects related to the given owner
     */
    @Override
    public List<CallnoModification> getPreprocessingRedirects(String owner) {
        List<CallnoModification> list = getHibernateTemplate().find(
                "from PreprocessingRedirect redirect "
                + "where redirect.owner.code = ? "
                + "and redirect.isActive = true", owner);
        return list;
    }

    /**
     * Returns the index entry matching the given location id and owner code. If
     * no matching entry or more than one entries are found, null is returned.
     * @param locationId location id of the Location object
     * @param owner owner of the Location object
     * @return index entry or null
     */
    @Override
    public SearchIndex getIndexEntry(int locationId, String owner) {
        List<SearchIndex> list = getHibernateTemplate().find(
                "from SearchIndex si where si.locationId = ?"
                + " and si.owner.code = ?", locationId, owner);
        if (list.isEmpty() || list.size() != 1) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Tests the DB connection by executing a simple test query. If the test
     * query succeeds the connection is OK, otherwise there's a problem.
     * @return true if and only if the connection works; otherwise false
     */
    @Override
    public boolean testDbConnection() {
        try {
            getHibernateTemplate().find("select count(*) from Library");
        } catch (Exception e) {
            localLogger.error("Testing the DB connection failed!");
            localLogger.error(e);
            return false;
        }
        return true;
    }
}