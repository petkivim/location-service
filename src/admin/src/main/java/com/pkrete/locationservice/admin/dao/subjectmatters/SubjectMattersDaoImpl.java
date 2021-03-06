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
package com.pkrete.locationservice.admin.dao.subjectmatters;

import com.pkrete.locationservice.admin.dao.SubjectMattersDao;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import java.util.List;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements {@link SubjectMattersDao SubjectMattersDao} interface
 * that defines data access layer for SubjectMatter objects.
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
public class SubjectMattersDaoImpl extends HibernateDaoSupport implements SubjectMattersDao {

    private final static Logger localLogger = LoggerFactory.getLogger(SubjectMattersDaoImpl.class.getName());

    /**
     * Returns a list of all the subject matters in the database that are
     * related to the given owner object.
     *
     * @param owner the owner of the objects
     * @return all the subject matters in the database
     */
    @Override
    public List<SubjectMatter> getSubjectMatters(Owner owner) {
        List result = getHibernateTemplate().findByNamedParam("from SubjectMatter as subjectMatter "
                + "join fetch subjectMatter.owner as owner "
                + "where owner.code like :owner "
                + "order by subjectMatter.language.name, subjectMatter.indexTerm ASC", "owner", owner.getCode());
        return result;
    }

    /**
     * Returns a list of all the subject matters in the database with the
     * language object initialized.
     *
     * @param owner the owner of the objects
     * @return all the subject matters in the database
     */
    @Override
    public List<SubjectMatter> getSubjectMattersWithLanguage(Owner owner) {
        List result = getHibernateTemplate().findByNamedParam("from SubjectMatter subjectMatter "
                + "join fetch subjectMatter.owner as owner "
                + "inner join fetch subjectMatter.language "
                + "where owner.code like :owner "
                + "order by subjectMatter.language.name, subjectMatter.indexTerm ASC", "owner", owner.getCode());
        return result;
    }

    /**
     * Returns the subject matter which id matches with the given id number.
     *
     * @param id the id that is used for searching
     * @return the subject matter with the given id
     */
    @Override
    public SubjectMatter getSubjectMatter(int id) {
        List<SubjectMatter> list = (List<SubjectMatter>) getHibernateTemplate().findByNamedParam(
                "from SubjectMatter as subjectMatter "
                + "join fetch subjectMatter.owner "
                + "where subjectMatter.id =:id", "id", id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns the subject matter which id matches with the given id number.
     *
     * @param id the id that is used for searching
     * @param owner the owner of the object
     * @return the subject matter with the given id
     */
    @Override
    public SubjectMatter getSubjectMatter(int id, Owner owner) {
        List<SubjectMatter> list = (List<SubjectMatter>) getHibernateTemplate().findByNamedParam(
                "from SubjectMatter as subjectMatter "
                + "join fetch subjectMatter.owner as owner "
                + "join fetch subjectMatter.language "
                + "left join fetch subjectMatter.locations "
                + "where owner.code like :owner "
                + "and subjectMatter.id = :id", new String[]{"owner", "id"}, new Object[]{owner.getCode(), id});
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns the subject matter which id matches with the given id number.
     * Initializes locations related to the given subject matter object, so it's
     * easy to check whether the object can be deleted or not. The object can't
     * be deleted if there are locations related to it.
     *
     * @param id the id that is used for searching
     * @param owner the owner of the object
     * @return the subject matter with the given id
     */
    @Override
    public SubjectMatter getSubjectMatterToBeDeleted(int id, Owner owner) {
        List<SubjectMatter> list = (List<SubjectMatter>) getHibernateTemplate().findByNamedParam(
                "from SubjectMatter as subjectMatter "
                + "join fetch subjectMatter.owner as owner "
                + "left join fetch subjectMatter.locations "
                + "where owner.code like :owner "
                + "and subjectMatter.id = :id", new String[]{"owner", "id"}, new Object[]{owner.getCode(), id});
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns a list of SubjectMatter ids related to the given Owner.
     *
     * @param owner Owner object
     * @return list of SubjectMatter ids related to the given Owner
     */
    @Override
    public List<Integer> getIds(Owner owner) {
        List<Integer> list = (List<Integer>) getHibernateTemplate().findByNamedParam(
                "select id from SubjectMatter "
                + "where owner.id = :id", "id", owner.getId());
        return list;
    }

    /**
     * Saves the given subject matter object to the database.
     *
     * @param subjectMatter the subject matter to be saved
     */
    @Override
    public boolean create(SubjectMatter subjectMatter) {
        try {
            getHibernateTemplate().save(subjectMatter);
        } catch (Exception e) {
            localLogger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Updates the given subject matter object to the database.
     *
     * @param subjectMatter the subject matter to be updated
     */
    @Override
    public boolean update(SubjectMatter subjectMatter) {
        try {
            getHibernateTemplate().update(subjectMatter);
        } catch (Exception e) {
            localLogger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Deletes the given subject matter object from the database.
     *
     * @param subjectMatter the subject matter to be deleted
     */
    @Override
    public boolean delete(SubjectMatter subjectMatter) {
        try {
            getHibernateTemplate().delete(subjectMatter);
        } catch (Exception e) {
            localLogger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }
}
