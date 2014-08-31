/**
 * This file is part of Location Service :: Admin.
 * Copyright (C) 2014 Petteri Kivimäki
 *
 * Location Service :: Admin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Location Service :: Admin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Location Service :: Admin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.admin.dao.locations;

import com.pkrete.locationservice.admin.dao.LocationsDao;
import com.pkrete.locationservice.admin.model.location.Area;
import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.search.SearchIndex;
import com.pkrete.locationservice.admin.model.location.Shelf;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * This class implements {@link LocationsDao LocationsDao} interface that
 * defines data access layer for Library, LibraryCollection and
 * Shelf objects. 
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
public class LocationsDaoImpl extends HibernateDaoSupport implements LocationsDao {

    private final static Logger localLogger = Logger.getLogger(LocationsDaoImpl.class.getName());

    /**
     * Returns the library which locationId matches with the given id number.
     * This method is only for editor classes. All the other classes must give
     * also the owner parameter.
     * @param libraryId locationId that is used for searching
     * @return library with the desired locationId or null if matching
     * library doesn't exist
     */
    @Override
    public Library getLibrary(int libraryId) {
        List<Library> list = (List<Library>) getHibernateTemplate().findByNamedParam("from Library library "
                + "join fetch library.owner as owner "
                + "where library.locationId = :libraryId", "libraryId", libraryId);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns the library which locationId matches with the given id number.
     * Initializes image, map, areas and collections objects related to
     * the library, so that the library can be edited.
     * @param libraryId locationId that is used for searching
     * @param owner the owner of the object
     * @return library with the desired locationId or null if matching
     * library doesn't exist
     */
    @Override
    public Library getLibrary(int libraryId, Owner owner) {
        List<Library> list = (List<Library>) getHibernateTemplate().findByNamedParam("from Library library "
                + "left join fetch library.image "
                + "left join fetch library.map "
                + "left join fetch library.areas "
                + "join fetch library.owner as owner "
                + "where owner.code like :owner "
                + "and library.locationId = :libraryId", new String[]{"owner", "libraryId"}, new Object[]{owner.getCode(), libraryId});
        if (list.isEmpty()) {
            return null;
        }
        Hibernate.initialize(list.get(0).getCollections());
        Hibernate.initialize(list.get(0).getDescriptions());
        Hibernate.initialize(list.get(0).getNotes());
        return list.get(0);
    }

    /**
     * Returns the library which locationId matches with the given id number.
     * Initializes areas, collections and shelves related to the given
     * library object, so that the library and the objects related to it can
     * be deleted.
     * @param libraryId locationId that is used for searching
     * @param owner the owner of the object
     * @return library with the desired locationId or null if matching
     * library doesn't exist
     */
    @Override
    public Library getLibraryToBeDeleted(int libraryId, Owner owner) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Library.class).add(Restrictions.eq("locationId", libraryId)).add(Restrictions.eq("owner", owner)).setFetchMode("areas", FetchMode.JOIN);
        List<Library> list = (List<Library>) getHibernateTemplate().findByCriteria(criteria);
        if (list.isEmpty()) {
            return null;
        }
        Hibernate.initialize(list.get(0).getDescriptions());
        Hibernate.initialize(list.get(0).getNotes());
        Hibernate.initialize(list.get(0).getImage());
        Hibernate.initialize(list.get(0).getMap());
        List<LibraryCollection> collections = list.get(0).getCollections();
        for (LibraryCollection collection : collections) {
            Hibernate.initialize(collection.getShelves());
        }
        return list.get(0);
    }

    /**
     * Returns the collection which locationId matches with the given id number.
     * Initializes image, map, areas, subject matters and shelves objects related to
     * the collection, so that the collection can be edited.
     * @param collectionId locationId that is used for searching
     * @param owner the owner of the object
     * @return the collection with the desired locationId or null if matching
     * collection doesn't exist
     */
    @Override
    public LibraryCollection getCollection(int collectionId, Owner owner) {
        return getCollection(collectionId, 0, owner);
    }

    /**
     * Returns the collection which locationId matches with the given id number
     * and that belongs to the given library. Initializes image, map, areas, 
     * subject matters and shelves objects related to the collection, so that 
     * the collection can be edited.
     * @param collectionId locationId that is used for searching
     * @param libraryId locationId of the library
     * @param owner the owner of the object
     * @return the collection with the desired locationId or null if matching
     * collection doesn't exist
     */
    @Override
    public LibraryCollection getCollection(int collectionId, int libraryId, Owner owner) {
        String libCondition = "";
        if (libraryId != 0) {
            libCondition = " and library.locationId = " + libraryId;
        }
        List<LibraryCollection> list = (List<LibraryCollection>) getHibernateTemplate().findByNamedParam("from LibraryCollection collection "
                + "left join fetch collection.image "
                + "left join fetch collection.map "
                + "left join fetch collection.areas "
                + "join fetch collection.owner as owner "
                + "join fetch collection.library as library "
                + "where owner.code like :owner "
                + "and collection.locationId = :collectionId"
                + libCondition, new String[]{"owner", "collectionId"}, new Object[]{owner.getCode(), collectionId});
        if (list.isEmpty()) {
            return null;
        }
        Hibernate.initialize(list.get(0).getShelves());
        Hibernate.initialize(list.get(0).getSubjectMatters());
        Hibernate.initialize(list.get(0).getDescriptions());
        Hibernate.initialize(list.get(0).getNotes());
        return list.get(0);
    }

    /**
     * Returns the collection which locationId matches with the given id number.
     * This method is only for editor classes. All the other classes must give
     * also the owner parameter.
     * @param collectionId locationId that is used for searching
     * @return collection with the desired locationId or null if matching
     * collection doesn't exist
     */
    @Override
    public LibraryCollection getCollection(int collectionId) {
        List<LibraryCollection> list = (List<LibraryCollection>) getHibernateTemplate().findByNamedParam("from LibraryCollection collection "
                + "join fetch collection.owner as owner "
                + "where collection.locationId = :collectionId", "collectionId", collectionId);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns the collection which locationId matches with the given id number.
     * Initializes areas, subject matters and shelves related to the given
     * collection object, so that the collection and the objects related to it can
     * be deleted.
     * @param collectionId locationId that is used for searching
     * @param owner the owner of the object
     * @return collection with the given locationId or null if matching
     * collection doesn't exist
     */
    @Override
    public LibraryCollection getCollectionToBeDeleted(int collectionId, Owner owner) {
        return this.getCollectionToBeDeleted(collectionId, 0, owner);
    }

    /**
     * Returns the collection which locationId matches with the given id number 
     * and that belongs to the given library. Initializes areas, subject 
     * matters and shelves related to the given collection object, so that the
     * collection and the objects related to it can be deleted.
     * @param collectionId locationId that is used for searching
     * @param libraryId locationId of the library
     * @param owner the owner of the object
     * @return collection with the given locationId or null if matching
     * collection doesn't exist
     */
    @Override
    public LibraryCollection getCollectionToBeDeleted(int collectionId, int libraryId, Owner owner) {
        String libCondition = "";
        if (libraryId != 0) {
            libCondition = " and library.locationId = " + libraryId;
        }
        List<LibraryCollection> list = (List<LibraryCollection>) getHibernateTemplate().findByNamedParam("from LibraryCollection collection "
                + "left join fetch collection.image "
                + "left join fetch collection.map "
                + "left join fetch collection.areas "
                + "join fetch collection.owner as owner "
                + "join fetch collection.library as library "
                + "where owner.code like :owner "
                + "and collection.locationId = :collectionId"
                + libCondition, new String[]{"owner", "collectionId"}, new Object[]{owner.getCode(), collectionId});
        if (list.isEmpty()) {
            return null;
        }
        Hibernate.initialize(list.get(0).getShelves());
        Hibernate.initialize(list.get(0).getSubjectMatters());
        Hibernate.initialize(list.get(0).getDescriptions());
        Hibernate.initialize(list.get(0).getNotes());
        return list.get(0);
    }

    /**
     * Returns all the collections that are related to the library which
     * locationId matches with the given id number.
     * @param libraryId locationId of the library that is used for searching
     * @param owner the owner of the object
     * @return list of collections that belong to the library with the
     * given locationId
     */
    @Override
    public List<LibraryCollection> getCollectionsByLibraryId(int libraryId, Owner owner) {
        List<LibraryCollection> list = (List<LibraryCollection>) getHibernateTemplate().findByNamedParam(
                "from LibraryCollection as collection "
                + "join fetch collection.library as library "
                + "join fetch library.owner as owner "
                + "where owner.code like :owner "
                + "and library.locationId = :libraryId "
                + "order by collection.name ASC", new String[]{"owner", "libraryId"}, new Object[]{owner.getCode(), libraryId});
        return list;
    }

    /**
     * Returns all the collections that are owned by the given owner.
     * @param owner the owner of the collections
     * @return list of collections owned by the given owner
     */
    @Override
    public List<LibraryCollection> getCollections(Owner owner) {
        List<LibraryCollection> list = (List<LibraryCollection>) getHibernateTemplate().findByNamedParam(
                "from LibraryCollection as collection "
                + "where collection.owner.code like :owner "
                + "order by collection.name ASC", "owner", owner.getCode());
        return list;
    }

    /**
     * Returns the shelf which locationId matches with the given id number.
     * Initializes image, map, areas and subject matters objects related to
     * the shelf, so that the shelf can be edited.
     * @param shelfId locationId that is used for searching
     * @param owner the owner of the object
     * @return shelf with the desired locationId or null if matching shelf
     * is not found
     */
    @Override
    public Shelf getShelf(int shelfId, Owner owner) {
        return this.getShelf(shelfId, 0, 0, owner);
    }

    /**
     * Returns the shelf which locationId matches with the given id number.
     * Initializes image, map, areas and subject matters objects related to
     * the shelf, so that the shelf can be edited.
     * @param shelfId locationId that is used for searching
     * @param collectionId locationId of the collection
     * @param libraryId locationId of the library
     * @param owner the owner of the object
     * @return shelf with the desired locationId or null if matching shelf
     * is not found
     */
    @Override
    public Shelf getShelf(int shelfId, int collectionId, int libraryId, Owner owner) {
        String libCondition = "";
        if (libraryId != 0) {
            libCondition = " and collection.library.locationId = " + libraryId;
        }
        String colCondition = "";
        if (collectionId != 0) {
            colCondition = " and collection.locationId = " + collectionId;
        }
        List<Shelf> list = (List<Shelf>) getHibernateTemplate().findByNamedParam("from Shelf shelf "
                + "left join fetch shelf.image "
                + "left join fetch shelf.map "
                + "left join fetch shelf.areas "
                + "join fetch shelf.collection as collection "
                + "join fetch shelf.collection.library as library "
                + "join fetch shelf.owner as owner "
                + "where owner.code like :owner "
                + "and shelf.locationId = :shelfId"
                + colCondition
                + libCondition, new String[]{"owner", "shelfId"}, new Object[]{owner.getCode(), shelfId});
        if (list.isEmpty()) {
            return null;
        }
        Hibernate.initialize(list.get(0).getSubjectMatters());
        Hibernate.initialize(list.get(0).getDescriptions());
        Hibernate.initialize(list.get(0).getNotes());
        return list.get(0);
    }

    /**
     * Returns all the shelves that are related to the collection which
     * locationId matches with the given id number.
     * @param collectionId locationId that is used for searching
     * @param owner the owner of the object
     * @return list of shelves that belong to the given collection
     */
    @Override
    public List<Shelf> getShelvesByCollectionId(int collectionId, Owner owner) {
        List<Shelf> list = (List<Shelf>) getHibernateTemplate().findByNamedParam(
                "from Shelf as shelf "
                + "join fetch shelf.collection as collection "
                + "join fetch collection.library as library "
                + "join fetch library.owner as owner "
                + "where owner.code like :owner "
                + "and collection.locationId = :collectionId "
                + "order by shelf.name ASC", new String[]{"owner", "collectionId"}, new Object[]{owner.getCode(), collectionId});
        return list;
    }

    /**
     * Returns all the shelves that are related to the collection which
     * locationId matches with the given id number.
     * @param libraryId id of the library
     * @param collectionId id of the collection
     * @param owner the owner of the object
     * @return list of shelves that belong to the given collection
     */
    @Override
    public List<Shelf> getShelvesByCollectionId(int libraryId, int collectionId, Owner owner) {
        List<Shelf> list = (List<Shelf>) getHibernateTemplate().findByNamedParam(
                "from Shelf as shelf "
                + "join fetch shelf.collection as collection "
                + "join fetch collection.library as library "
                + "join fetch library.owner as owner "
                + "where owner.code like :owner "
                + "and collection.locationId = :collectionId "
                + "and library.locationId = :libraryId "
                + "order by shelf.name ASC", new String[]{"owner", "collectionId", "libraryId"}, new Object[]{owner.getCode(), collectionId, libraryId});
        return list;
    }

    /**
     * Returns the shelf which locationId matches with the given id number.
     * Initializes areas and subject matters related to the given
     * shelf object, so that the shelf and the objects related to it can
     * be deleted.
     * @param shelfId locationId that is used for searching
     * @param owner the owner of the object
     * @return shelf with the desired locationId
     */
    @Override
    public Shelf getShelfToBeDeleted(int shelfId, Owner owner) {
        return this.getShelf(shelfId, owner);
    }

    /**
     * Returns the shelf which locationId matches with the given id number.
     * Initializes areas and subject matters related to the given
     * shelf object, so that the shelf and the objects related to it can
     * be deleted.
     * @param shelfId locationId that is used for searching
     * @param collectionId locationId of the collection
     * @param libraryId locationId of the library
     * @param owner the owner of the object
     * @return shelf with the desired locationId
     */
    @Override
    public Shelf getShelfToBeDeleted(int shelfId, int collectionId, int libraryId, Owner owner) {
        return this.getShelf(shelfId, collectionId, libraryId, owner);
    }

    /**
     * Returns the location which id number matches with given id number
     * @param locationId the id number that is used for searching
     * @param owner the owner of the object
     * @return the location with the desired id number
     */
    @Override
    public Location getLocation(int locationId, Owner owner) {
        List<Location> result = null;
        try {
            result = (List<Location>) getHibernateTemplate().findByNamedParam("from Shelf as shelf where shelf.owner.code like :owner "
                    + "and shelf.locationId = :locationId", new String[]{"owner", "locationId"}, new Object[]{owner.getCode(), locationId});
            if (result.isEmpty()) {
                result = (List<Location>) getHibernateTemplate().findByNamedParam("from LibraryCollection as collection where collection.owner.code like :owner "
                        + "and collection.locationId = :locationId", new String[]{"owner", "locationId"}, new Object[]{owner.getCode(), locationId});
                if (result.isEmpty()) {
                    result = (List<Location>) getHibernateTemplate().findByNamedParam("from Library as library where library.owner.code like :owner "
                            + "and library.locationId = :locationId", new String[]{"owner", "locationId"}, new Object[]{owner.getCode(), locationId});
                }
            }
        } catch (Exception e) {
            localLogger.error(e);
        }
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    /**
     * Returns a list of all the libraries in the database that are related
     * to the given owner object.
     * @param owner the owner of the objects
     * @return all the libraries in the database
     */
    @Override
    public List<Library> getAllLocations(Owner owner) {
        List result = getHibernateTemplate().findByNamedParam("from Library library "
                + "join fetch library.owner as owner where owner.code like :owner "
                + "order by library.name ASC", "owner", owner.getCode());
        return result;
    }

    /**
     * Returns a list of all the areas in the database that are related
     * to the given location id.
     * @param location id of the location that the areas are related to
     * @return all the areas related to the given location
     */
    @Override
    public List<Area> getAreasByLocationId(int locationId) {
        List<Area> result = (List<Area>) getHibernateTemplate().findByNamedParam("from Area area "
                + "where area.location.locationId = :locationId", "locationId", locationId);
        return result;
    }

    /**
     * Adds the given library object to the database.
     * @param library the library to be created
     * @return true if and only if the library was successfully created;
     * otherwise false
     */
    @Override
    public boolean create(Library library) {
        try {
            getHibernateTemplate().save(library);
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Updates the given library object to the database.
     * @param library the library to be updated
     * @return true if and only if the library was successfully updated;
     * otherwise false
     */
    @Override
    public boolean update(Library library) {
        try {
            getHibernateTemplate().update(library);
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Adds the given collection object to the database.
     * @param collection the collection to be created
     * @return true if and only if the collection was successfully created;
     * otherwise false
     */
    @Override
    public boolean create(LibraryCollection collection) {
        try {
            getHibernateTemplate().save(collection);
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Updates the given collection object to the database.
     * @param collection the collection to be updated
     * @return true if and only if the collection was successfully updated;
     * otherwise false
     */
    @Override
    public boolean update(LibraryCollection collection) {
        try {
            getHibernateTemplate().update(collection);
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Adds the given shelf object to the database.
     * @param shelf the shelf to be created
     * @return true if and only if the shelf was successfully created;
     * otherwise false
     */
    @Override
    public boolean create(Shelf shelf) {
        try {
            getHibernateTemplate().save(shelf);
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Updates the given shelf object to the database.
     * @param shelf the shelf to be updated
     * @return true if and only if the shelf was successfully updated;
     * otherwise false
     */
    @Override
    public boolean update(Shelf shelf) {
        try {
            getHibernateTemplate().update(shelf);
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Deletes the given library object from the database. All the collections and shelves
     * attached to the library will be deleted as well.
     * @param library the library to be deleted
     * @return true if and only if the object was successfully deleted;
     * otherwise false
     */
    @Override
    public boolean delete(Library library) {
        try {
            getHibernateTemplate().delete(library);
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Deletes the given location object from the database. All the shelves
     * attached to the collection will be deleted as well.
     * @param collection the collection to be deleted
     * @return true if and only if the object was successfully deleted;
     * otherwise false
     */
    @Override
    public boolean delete(LibraryCollection collection) {
        try {
            getHibernateTemplate().delete(collection);
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Deletes the given shelf object from the database.
     * @param shelf the shelf to be deleted
     * @return true if and only if the object was successfully deleted;
     * otherwise false
     */
    @Override
    public boolean delete(Shelf shelf) {
        try {
            getHibernateTemplate().delete(shelf);
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Deletes all the Description objects which id is included in the given
     * list.
     * @param ids list of Description ids to be deleted
     * @return returns true if and only if the ids were successfully deleted;
     * otherwise returns false
     */
    @Override
    public boolean deleteDescriptions(List<Integer> ids) {
        try {
            Session sess = getHibernateTemplate().getSessionFactory().getCurrentSession();
            sess.createQuery("delete from Description where id in (" + toString(ids) + ")").executeUpdate();
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Deletes all the Note objects which id is included in the given
     * list.
     * @param ids list of Note ids to be deleted
     * @return returns true if and only if the ids were successfully deleted;
     * otherwise returns false
     */
    @Override
    public boolean deleteNotes(List<Integer> ids) {
        try {
            Session sess = getHibernateTemplate().getSessionFactory().getCurrentSession();
            sess.createQuery("delete from Note where id in (" + toString(ids) + ")").executeUpdate();
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Deletes all the Area objects which id is included in the given
     * list.
     * @param ids list of Area ids to be deleted
     * @return returns true if and only if the ids were successfully deleted;
     * otherwise returns false
     */
    @Override
    public boolean deleteAreas(List<Integer> ids) {
        try {
            Session sess = getHibernateTemplate().getSessionFactory().getCurrentSession();
            sess.createQuery("delete from Area where id in (" + toString(ids) + ")").executeUpdate();
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Saves the given SearchIndex object to the database. Can be used 
     * to add and update indexes.
     * @param index the SearchIndex to be saved
     * @return true if and only if the operation was successfully completed;
     * otherwise false
     */
    @Override
    public boolean save(SearchIndex index) {
        try {
            getHibernateTemplate().saveOrUpdate(index);
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Returns a list of search indexes related to the location with
     * the given id.
     * @param locationId id of the location
     * @return list of search indexes
     */
    @Override
    public List<SearchIndex> findSearchIndexes(int locationId) {
        List<SearchIndex> list = (List<SearchIndex>) getHibernateTemplate().findByNamedParam("from SearchIndex si where si.locationId = :locationId", "locationId", locationId);
        return list;
    }

    /**
     * Returns a list of collections that belong to the library with the
     * given location id. Search indexes and shelves with their search
     * indexes are loaded too.
     * @param locationId id of the library
     * @return list of collections
     */
    @Override
    public List<LibraryCollection> getCollectionsForIndexUpdate(int locationId) {
        List<LibraryCollection> list = (List<LibraryCollection>) getHibernateTemplate().findByNamedParam(
                "from LibraryCollection as collection "
                + "left join fetch collection.searchIndexes si "
                + "join fetch collection.library as library "
                + "where library.locationId = :locationId", "locationId", locationId);
        for (LibraryCollection col : list) {
            for (Shelf shelf : col.getShelves()) {
                Hibernate.initialize(shelf.getSearchIndexes());
            }
        }
        return list;
    }

    /**
     * Returns a list of shelves that belong to the collection with the
     * given location id. Search indexes are loaded too.
     * @param locationId id of the collection
     * @return list of shelves
     */
    @Override
    public List<Shelf> getShelvesForIndexUpdate(int locationId) {
        List<Shelf> list = (List<Shelf>) getHibernateTemplate().findByNamedParam(
                "from Shelf as shelf "
                + "join fetch shelf.collection as collection "
                + "left join fetch shelf.searchIndexes si "
                + "where collection.locationId = :locationId", "locationId", locationId);
        return list;
    }

    /**
     * Deletes all the SearchIndex entries.
     * @return true if and only the search index was successfully cleared;
     * otherwise false
     */
    @Override
    public boolean clearSearchIndex() {
        try {
            Session sess = getHibernateTemplate().getSessionFactory().getCurrentSession();
            sess.createQuery("delete from SearchIndex where id > 0").executeUpdate();
        } catch (Exception e) {
            localLogger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Returns a list of all the libraries in the database. All the collections
     * and shelves are loaded as well.
     * @return list of all the libraries in the database
     */
    @Override
    public List<Library> getAllLibraries() {
        List<Library> list = (List<Library>) getHibernateTemplate().find(
                "select distinct lib from Library lib "
                + "join fetch lib.owner as owner "
                + "left join fetch lib.collections as col");
        return list;
    }

    /**
     * Returns a list of all the locations in the database that are related to
     * the given owner. All the collections and shelves are loaded.
     * @param owner owner of the object
     * @return all the libraries in the database
     */
    @Override
    public List<Location> getAllLocationsWithDependecies(Owner owner) {
        List<Location> list = new ArrayList<Location>();
        List<Library> result = (List<Library>) getHibernateTemplate().findByNamedParam(
                "select distinct lib from Library lib "
                + "where lib.owner.code = :owner", "owner", owner.getCode());
        for (Library lib : result) {
            list.add(lib);
            for (LibraryCollection col : lib.getCollections()) {
                list.add(col);
                for (Shelf shelf : col.getShelves()) {
                    list.add(shelf);
                }
            }
        }
        return list;
    }

    /**
     * Returns the id of the library in which the collection with the given
     * id belongs to.
     * @param collectionId id of the collection
     * @return id of the library or zero if no library is found
     */
    @Override
    public int getLibraryId(int collectionId) {
        List<Integer> list = (List<Integer>) getHibernateTemplate().findByNamedParam(
                "select col.library.locationId from LibraryCollection col "
                + "where col.locationId = :collectionId", "collectionId", collectionId);
        if (list.isEmpty()) {
            return 0;
        }
        return list.get(0);
    }

    /**
     * Returns the id of the collection in which the shelf with the given
     * id belongs to.
     * @param shelfId id of the shelf
     * @return id of the collection or zero if no library is found
     */
    @Override
    public int getCollectionId(int shelfId) {
        List<Integer> list = (List<Integer>) getHibernateTemplate().findByNamedParam(
                "select shelf.collection.locationId from Shelf shelf "
                + "where shelf.locationId = :shelfId", "shelfId", shelfId);
        if (list.isEmpty()) {
            return 0;
        }
        return list.get(0);
    }

    /**
     * Returns a list of Area ids that belong to the given Location.
     * @param location Location that owns the Areas
     * @return list of Area ids that belong to the given Location
     */
    @Override
    public List<Integer> getAreaIds(Location location) {
        List<Integer> list = (List<Integer>) getHibernateTemplate().findByNamedParam(
                "select areaId from Area "
                + "where location.locationId = :id", "id", location.getLocationId());
        return list;
    }

    /**
     * Returns a list of Description ids that belong to the given Location.
     * @param location Location that owns the Descriptions
     * @return list of Description ids that belong to the given Location
     */
    @Override
    public List<Integer> getDescriptionIds(Location location) {
        String type = "Library";
        if (location instanceof LibraryCollection) {
            type = "LibraryCollection";
        } else if (location instanceof Shelf) {
            type = "Shelf";
        }
        List<Integer> list = (List<Integer>) getHibernateTemplate().findByNamedParam(
                "select descs.id from " + type + " loc JOIN loc.descriptions descs "
                + "where loc.locationId = :id", "id", location.getLocationId());
        return list;
    }

    /**
     * Returns a list of Note ids that belong to the given Location.
     * @param location Location that owns the Notes
     * @return list of Note ids that belong to the given Location
     */
    @Override
    public List<Integer> getNoteIds(Location location) {
        String type = "Library";
        if (location instanceof LibraryCollection) {
            type = "LibraryCollection";
        } else if (location instanceof Shelf) {
            type = "Shelf";
        }
        List<Integer> list = (List<Integer>) getHibernateTemplate().findByNamedParam(
                "select notes.id from " + type + " loc JOIN loc.notes notes "
                + "where loc.locationId = :id", "id", location.getLocationId());
        return list;
    }

    /**
     * Converts a list of integers to a comma separated string that can be 
     * used in a HQL query,
     * @param list list of integers
     * @return integers as a comma separated string
     */
    private String toString(List<Integer> list) {
        StringBuilder builder = new StringBuilder();
        for (Integer id : list) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(id);
        }
        return builder.toString();
    }
}
