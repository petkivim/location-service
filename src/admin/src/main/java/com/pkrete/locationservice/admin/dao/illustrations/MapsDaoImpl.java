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
package com.pkrete.locationservice.admin.dao.illustrations;

import com.pkrete.locationservice.admin.model.illustration.Map;
import com.pkrete.locationservice.admin.model.owner.Owner;
import java.util.List;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import com.pkrete.locationservice.admin.dao.MapsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interface defines data access layer for Map objects. This class
 * implements {@link IllustrationsDao IllustrationsDao} interface that defines
 * data access layer for Map objects.
 *
 * This class extends {@link HibernateDaoSupport HibernateDaoSupport} class that
 * is a wrapper over {@link HibernateTemplate HibernateTemplate} class.
 * HibernateTemplate is a convenience class for Hibernate based database access.
 * HibernateDaoSupport creates the HibernateTemplate and subclasses can use the
 * getHibernateTemplate() method to obtain the hibernateTemplate and then
 * perform operations on it. HibernateTemplate takes care of obtaining or
 * releasing sessions and managing exceptions.
 *
 * @author Petteri Kivimäki
 */
public class MapsDaoImpl extends HibernateDaoSupport implements MapsDao {

    private final static Logger localLogger = LoggerFactory.getLogger(MapsDaoImpl.class.getName());

    /**
     * Returns the map with given id.
     *
     * @param id the id that is used for searching
     * @return the map with the given id; if no matching map is found, null is
     * returned
     */
    @Override
    public Map get(int id) {
        List<Map> list = (List<Map>) getHibernateTemplate().findByNamedParam(
                "from Map map "
                + "where map.id= :id", "id", id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns the map with given id.
     *
     * @param id the id that is used for searching
     * @param owner the owner of the object
     * @return the map with the given id; if no matching map is found, null is
     * returned
     */
    @Override
    public Map get(int id, Owner owner) {
        List<Map> list = (List<Map>) getHibernateTemplate().findByNamedParam(
                "from Map as map "
                + "join fetch map.owner as owner "
                + "where owner.code like :owner "
                + "and map.id=:id", new String[]{"owner", "id"}, new Object[]{owner.getCode(), id});
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Deletes the given map object from the database.
     *
     * @param map the map to be deleted
     * @return true if and only if the map was deleted; otherwise false
     */
    @Override
    public boolean delete(Map map) {
        try {
            getHibernateTemplate().delete(map);
        } catch (Exception e) {
            localLogger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Returns a list of all the maps in the database that are related to the
     * given owner object.
     *
     * @param owner the owner of the objects
     * @return all the maps in the database
     */
    @Override
    public List<Map> get(Owner owner) {
        List result = getHibernateTemplate().findByNamedParam("from Map map "
                + "join fetch map.owner as owner "
                + "where owner.code like :owner "
                + "order by map.description ASC", "owner", owner.getCode());
        return result;
    }

    /**
     * Saves the given map object to the database.
     *
     * @param map the map to be saved
     * @return true if and only if the map was created; otherwise false
     */
    @Override
    public boolean create(Map map) {
        try {
            getHibernateTemplate().save(map);
        } catch (Exception e) {
            localLogger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Updates the given map object to the database.
     *
     * @param map the map to be saved
     * @return true if and only if the map was updated; otherwise false
     */
    @Override
    public boolean update(Map map) {
        try {
            getHibernateTemplate().update(map);
        } catch (Exception e) {
            localLogger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Checks if the map object corresponding the give id can be removed from
     * the database.
     *
     * @param mapId the id number of the map to be removed
     * @return true if the map object can be removed, otherwise returns false
     */
    @Override
    public boolean canBeDeleted(int mapId) {
        List result = null;

        result = getHibernateTemplate().findByNamedParam("from Shelf as shelf where shelf.map.id = :id", "id", mapId);
        if (!result.isEmpty()) {
            return false;
        }

        result = getHibernateTemplate().findByNamedParam("from LibraryCollection as collection where collection.map.id = :id", "id", mapId);
        if (!result.isEmpty()) {
            return false;
        }
        result = getHibernateTemplate().findByNamedParam("from Library as library where library.map.id = :id", "id", mapId);
        if (!result.isEmpty()) {
            return false;
        }
        return true;
    }
}
