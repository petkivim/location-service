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
package com.pkrete.locationservice.admin.dao.languages;

import com.pkrete.locationservice.admin.dao.LanguagesDao;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * This class implements {@link LanguagesDao LanguagesDao} interface that
 * defines data access layer for Language objects.
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
public class LanguagesDaoImpl extends HibernateDaoSupport implements LanguagesDao {

    /**
     * Returns a list of all the languages in the database.
     * @return all the languages in the database
     */
    public List<Language> getLanguages() {
        List result = getHibernateTemplate().find("from Language language "
                + "join fetch language.owner");
        return result;
    }

    /**
     * Returns a list of all the languages in the database that are related
     * to the given owner.
     * @param owner the owner of the objects
     * @return all the languages in the database
     */
    public List<Language> getLanguages(Owner owner) {
        List result = getHibernateTemplate().find("from Language language "
                + "join fetch language.owner as owner where "
                + "owner.code like '" + owner.getCode() + "' "
                + "order by owner.name, language.name ASC");
        return result;
    }

    /**
     * Returns the language with given id. If Language object
     * with the given id can not be found, null is returned.
     * This method is only for editor classes. All the other classes must give
     * also the owner parameter.
     * @param id the id that is used for searching
     * @return the language with the given id or null
     */
    public Language getLanguageById(int id) {
        List<Language> list = getHibernateTemplate().find(
                "from Language language "
                + "join fetch language.owner "
                + "where language.id=?", id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns the language object with the given code. If Language object
     * with the given code and owner can not be found, null is returned.
     * @param code code of the language object to be fetched
     * @param owner the owner of the object
     * @return language object with the given code or null
     */
    public Language getLanguage(String code, Owner owner) {
        List<Language> result = getHibernateTemplate().find("from Language language "
                + "join fetch language.owner as owner "
                + "where owner.code like '" + owner.getCode() + "' and "
                + "language.code = '" + code + "'");
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    /**
     * Returns the language with given id. If Language object
     * with the given id and owner can not be found, null is returned.
     * @param id the id that is used for searching
     * @param owner the owner of the object
     * @return the language with the given id or null
     */
    public Language getLanguageById(int id, Owner owner) {
        List<Language> list = getHibernateTemplate().find(
                "from Language language "
                + "join fetch language.owner as owner "
                + "where owner.code like '" + owner.getCode() + "' "
                + "and language.id=?", id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Adds the given language object to the database.
     * @param language the language to be added
     */
    public boolean create(Language language) {
        try {
            getHibernateTemplate().save(language);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Updates the given language object to the database.
     * @param language the language to be added
     */
    public boolean update(Language language) {
        try {
            getHibernateTemplate().update(language);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Deletes the given language object from the database.
     * @param language the language to be deleted
     */
    public boolean delete(Language language) {
        try {
            getHibernateTemplate().delete(language);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Checks from the database if the given language has any depencies.
     * If the language doesn't have any depencies, it can be deleted.
     * @param lang language to be deleted
     * @return true if the language can be deleted, otherwise returns false
     */
    public boolean canBeDeleted(Language lang) {
        List<SubjectMatter> list = null;
        list = getHibernateTemplate().find("from SubjectMatter as subjectMatter where subjectMatter.language.id =?", lang.getId());
        if (!list.isEmpty()) {
            return false;
        }
        list = getHibernateTemplate().find("from Description as description where description.language.id =?", lang.getId());
        if (!list.isEmpty()) {
            return false;
        }
        return true;
    }
}
