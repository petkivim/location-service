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
package com.pkrete.locationservice.admin.service.subjectmatters;

import com.pkrete.locationservice.admin.dao.SubjectMattersDao;
import com.pkrete.locationservice.admin.service.SubjectMattersService;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Petteri Kivimäki
 */
public class SubjectMattersServiceImpl implements SubjectMattersService {

    private final static Logger logger = LoggerFactory.getLogger(SubjectMattersServiceImpl.class.getName());
    private SubjectMattersDao dao;
    private JSONizerService jsonizer;

    /**
     * Sets the data access object.
     *
     * @param dao new value
     */
    public void setDao(SubjectMattersDao dao) {
        this.dao = dao;
    }

    /**
     * Sets the JSON converter object.
     *
     * @param dao new value
     */
    public void setJsonizer(JSONizerService jsonizer) {
        this.jsonizer = jsonizer;
    }

    /**
     * Returns a list of all the subject matters in the database that are
     * related to the given owner object.
     *
     * @param owner the owner of the objects
     * @return all the subject matters in the database
     */
    @Override
    public List<SubjectMatter> getSubjectMatters(Owner owner) {
        return dao.getSubjectMatters(owner);
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
        return dao.getSubjectMattersWithLanguage(owner);
    }

    /**
     * Returns the subject matter which id matches with the given id number.
     * This method is only for editor classes. All the other classes must give
     * also the owner parameter.
     *
     * @param id the id that is used for searching
     * @return the subject matter with the given id
     */
    @Override
    public SubjectMatter getSubjectMatter(int id) {
        return dao.getSubjectMatter(id);
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
        return dao.getSubjectMatter(id, owner);
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
        return dao.getSubjectMatterToBeDeleted(id, owner);
    }

    /**
     * Returns a set of SubjectMatter ids related to the given Owner.
     *
     * @param owner Owner object
     * @return set of SubjectMatter ids related to the given Owner
     */
    @Override
    public Set<Integer> getIds(Owner owner) {
        List<Integer> list = this.dao.getIds(owner);
        return new HashSet<Integer>(list);
    }

    /**
     * Adds the given subject matter object to the database.
     *
     * @param subjectMatter the subject matter to be added
     * @return true if and only id the object was created; otherwise false
     */
    @Override
    public boolean create(SubjectMatter subjectMatter) {
        subjectMatter.setCreated(new Date());
        if (dao.create(subjectMatter)) {
            logger.info("Subject matter created : {}", this.jsonizer.jsonize(subjectMatter, true));
            this.addToIndex(subjectMatter);
            return true;
        }
        logger.warn("Failed to create subject matter : {}", this.jsonizer.jsonize(subjectMatter, true));
        return false;
    }

    /**
     * Updates the given subject matter object to the database.
     *
     * @param subjectMatter the subject matter to be updated
     * @return true if and only id the object was updated; otherwise false
     */
    @Override
    public boolean update(SubjectMatter subjectMatter) {
        subjectMatter.setUpdated(new Date());
        String json = this.jsonizer.jsonize(subjectMatter, true);
        if (dao.update(subjectMatter)) {
            logger.info("Subject matter updated : {}", json);
            this.updateToIndex(subjectMatter);
            return true;
        }
        logger.warn("Failed to update subject matter : {}", json);
        return false;
    }

    /**
     * Deletes the given subject matter object from the database.
     *
     * @param subjectMatter the subject matter to be deleted
     * @return true if and only id the object was deleted; otherwise false
     */
    @Override
    public boolean delete(SubjectMatter subjectMatter) {
        // If subject has any Locations, it can't be deleted
        if (subjectMatter.getLocations() != null && !subjectMatter.getLocations().isEmpty()) {
            logger.info("Failed to delete a subject matter, because it has locations attached to it. Id : {}", subjectMatter.getId());
            return false;
        }
        String json = this.jsonizer.jsonize(subjectMatter, true);
        if (dao.delete(subjectMatter)) {
            logger.info("Subject matter deleted : {}", json);
            this.deleteFromIndex(subjectMatter);
            return true;
        }
        logger.warn("Failed to delete subject matter : {}", json);
        return false;
    }

    /**
     * Can be used for adding SubjectMatters to external index. Not implemented.
     *
     * @param subject SubjectMatter to be added
     * @return always true, not implemented
     */
    protected boolean addToIndex(SubjectMatter subject) {
        return true;
    }

    /**
     * Can be used for updating SubjectMatters to external index. Not
     * implemented.
     *
     * @param subject SubjectMatter to be updated
     * @return always true, not implemented
     */
    protected boolean updateToIndex(SubjectMatter subject) {
        return true;
    }

    /**
     * Can be used for deleting SubjectMatters from external index. Not
     * implemented.
     *
     * @param subject SubjectMatter to be deleted
     * @return always true, not implemented
     */
    protected boolean deleteFromIndex(SubjectMatter subject) {
        return true;
    }

    /**
     * Recreates the search index if it exists. Not supported.
     */
    public void recreateSearchIndex() {
        logger.info("Search index not supported. Nothing to do.");
    }
}
