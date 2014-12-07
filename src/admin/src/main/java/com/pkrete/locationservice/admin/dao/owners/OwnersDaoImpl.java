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
package com.pkrete.locationservice.admin.dao.owners;

import com.pkrete.locationservice.admin.dao.OwnersDao;
import com.pkrete.locationservice.admin.model.owner.CallnoModification;
import com.pkrete.locationservice.admin.model.owner.Owner;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Hibernate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.hibernate.Session;

/**
 * This class implements {@link OwnersDao OwnersDao} interface that defines data
 * access layer for Owner objects.
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
public class OwnersDaoImpl extends HibernateDaoSupport implements OwnersDao {

    private final static Logger localLogger = LoggerFactory.getLogger(OwnersDaoImpl.class.getName());

    /**
     * Returns a list of all the owners in the database.
     *
     * @return all the owners in the database
     */
    @Override
    public List<Owner> getOwners() {
        List result = getHibernateTemplate().find("from Owner owner order by owner.name ASC");
        return result;
    }

    /**
     * Returns the owner with the given id or null, if the owner cannot be
     * found.
     *
     * @param id the id that is used for searching
     * @return the owner with the given id or null
     */
    @Override
    public Owner getOwner(int id) {
        List<Owner> list = (List<Owner>) getHibernateTemplate().findByNamedParam(
                "from Owner owner left join fetch owner.languages "
                + "where owner.id=:id", "id", id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns the owner with the given id with all the collections related to
     * the owner loaded. If the owner cannot be found, null is returned.
     *
     * @param id the id that is used for searching
     * @return the owner with the given id or null
     */
    @Override
    public Owner getFullOwner(int id) {
        List<Owner> list = (List<Owner>) getHibernateTemplate().findByNamedParam(
                "from Owner owner left join fetch owner.languages "
                + "where owner.id=:id", "id", id);
        if (list.isEmpty()) {
            return null;
        }
        Owner owner = list.get(0);
        Hibernate.initialize(owner.getNotFoundRedirects());
        Hibernate.initialize(owner.getPreprocessingRedirects());
        return list.get(0);
    }

    /**
     * Returns the owner with the given code or null, if the owner cannot be
     * found.
     *
     * @param code the code that is used for searching
     * @return the owner with the given code or null
     */
    @Override
    public Owner getOwnerByCode(String code) {
        List<Owner> list = (List<Owner>) getHibernateTemplate().findByNamedParam(
                "from Owner owner where owner.code = :owner", "owner", code);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Checks if the given owner object can be removed from the database.
     *
     * @param owner owner object to be removed
     * @return true if the owner object can be removed; otherwise false
     */
    @Override
    public boolean canBeDeleted(Owner owner) {
        List result = null;

        result = getHibernateTemplate().findByNamedParam("from Library as library where library.owner.id = :id", "id", owner.getId());
        if (!result.isEmpty()) {
            return false;
        }

        result = getHibernateTemplate().findByNamedParam("from Image as image where image.owner.id = :id", "id", owner.getId());
        if (!result.isEmpty()) {
            return false;
        }

        result = getHibernateTemplate().findByNamedParam("from Map as map where map.owner.id = :id", "id", owner.getId());
        if (!result.isEmpty()) {
            return false;
        }

        result = getHibernateTemplate().findByNamedParam("from SubjectMatter as subject where subject.owner.id = :id", "id", owner.getId());
        if (!result.isEmpty()) {
            return false;
        }

        result = getHibernateTemplate().findByNamedParam("from UserFull as user where user.owner.id = :id", "id", owner.getId());
        if (!result.isEmpty()) {
            return false;
        }

        result = getHibernateTemplate().findByNamedParam("from Language as lang where lang.owner.id = :id", "id", owner.getId());
        if (!result.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Saves the given owner object to the database.
     *
     * @param owner the owner to be created
     * @return true if and only if the object was successfully created;
     * otherwise false
     */
    @Override
    public boolean create(Owner owner) {
        try {
            getHibernateTemplate().save(owner);
        } catch (Exception e) {
            localLogger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Updates the given owner object to the database.
     *
     * @param owner the owner to be updated
     * @return true if and only if the object was successfully updated;
     * otherwise false
     */
    @Override
    public boolean update(Owner owner) {
        try {
            getHibernateTemplate().update(owner);
        } catch (Exception e) {
            localLogger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Deletes the given owner object from the database.
     *
     * @param owner the owner to be deleted
     * @return true if and only if the object was successfully deleted;
     * otherwise false
     */
    @Override
    public boolean delete(Owner owner) {
        try {
            Owner temp = this.getOwner(owner.getId());
            Hibernate.initialize(temp.getLanguages());
            getHibernateTemplate().delete(temp);
        } catch (Exception e) {
            localLogger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Deletes the given call number modification object from the database.
     *
     * @param mod the call number modification to be deleted
     * @return true if and only if the object was successfully deleted;
     * otherwise false
     */
    @Override
    public boolean delete(CallnoModification mod) {
        try {
            getHibernateTemplate().delete(mod);
        } catch (Exception e) {
            localLogger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Deletes all the PreporcessingRedirects and NotFoundRedirects that don't
     * have an Owner.
     *
     * @return true if and only if all the orphans were successfully deleted;
     * otherwise false
     */
    @Override
    public boolean deleteOrphanRedirects() {
        try {
            Session sess = getHibernateTemplate().getSessionFactory().getCurrentSession();
            sess.createQuery("delete from PreprocessingRedirect where owner is null").executeUpdate();
            sess.createQuery("delete from NotFoundRedirect where owner is null").executeUpdate();
        } catch (Exception e) {
            localLogger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Returns a list of PreprocessingRedirect ids related to the owner with the
     * given id.
     *
     * @param ownerId owner id
     * @return list of PreprocessingRedirect ids related to the owner
     */
    @Override
    public List<Integer> getPreprocessingRedirectIds(int ownerId) {
        List<Integer> list = (List<Integer>) getHibernateTemplate().findByNamedParam("select id from PreprocessingRedirect where owner.id = :id", "id", ownerId);
        return list;
    }

    /**
     * Returns a list of NotFoundRedirect ids related to the owner with the
     * given id.
     *
     * @param ownerId owner id
     * @return list of NotFoundRedirect ids related to the owner
     */
    @Override
    public List<Integer> getNotFoundRedirectIds(int ownerId) {
        List<Integer> list = (List<Integer>) getHibernateTemplate().findByNamedParam("select id from NotFoundRedirect where owner.id = :id", "id", ownerId);
        return list;
    }
}
