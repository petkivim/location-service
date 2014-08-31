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
package com.pkrete.locationservice.admin.dao.illustrations;

import com.pkrete.locationservice.admin.dao.ImagesDao;
import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.owner.Owner;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * This interface defines data access layer for Image objects.
 * This class implements {@link IllustrationsDao IllustrationsDao} interface 
 * that defines data access layer for Image objects.
 * 
 * This class extends {@link HibernateDaoSupport HibernateDaoSupport} class 
 * that is a wrapper over {@link HibernateTemplate HibernateTemplate} class. 
 * HibernateTemplate is a convenience class for Hibernate based database access. 
 * HibernateDaoSupport creates the HibernateTemplate and subclasses can use 
 * the getHibernateTemplate() method to obtain the hibernateTemplate and 
 * then perform operations on it. HibernateTemplate takes care of obtaining or 
 * releasing sessions and managing exceptions. 
 * @author Petteri Kivimäki
 */
public class ImagesDaoImpl extends HibernateDaoSupport implements ImagesDao {

    /**
     * Returns the image with given id.
     * @param id the id that is used for searching
     * @return the image with the given id; if no matching image is found,
     * null is returned
     */
    @Override
    public Image get(int id) {
        List<Image> list = (List<Image>) getHibernateTemplate().findByNamedParam(
                "from Image image "
                + "where image.id= :id", "id", id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns the image with given id.
     * @param id the id that is used for searching
     * @param owner the owner of the object
     * @return the image with the given id; if no matching image is found,
     * null is returned
     */
    @Override
    public Image get(int id, Owner owner) {
        List<Image> list = (List<Image>) getHibernateTemplate().findByNamedParam(
                "from Image as image "
                + "join fetch image.owner as owner "
                + "where owner.code like :owner "
                + "and image.id= :id", new String[]{"owner", "id"}, new Object[]{owner.getCode(), id});
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Deletes the given image object from the database.
     * @param image the image to be deleted
     * @return true if and only if the image was deleted; otherwise false
     */
    @Override
    public boolean delete(Image image) {
        try {
            getHibernateTemplate().delete(image);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Returns a list of all the images in the database that are related to
     * the given owner object.
     * @param owner the owner of the objects
     * @return all the images in the database
     */
    @Override
    public List<Image> get(Owner owner) {
        List result = getHibernateTemplate().findByNamedParam("from Image image "
                + "join fetch image.owner as owner "
                + "where owner.code like :owner "
                + "order by image.description ASC", "owner", owner.getCode());
        return result;
    }

    /**
     * Saves the given image object to the database.
     * @param image the image to be saved
     * @return true if and only if the image was created; otherwise false
     */
    @Override
    public boolean create(Image image) {
        try {
            getHibernateTemplate().save(image);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Updates the given image object to the database.
     * @param image the image to be saved
     * @return true if and only if the image was update; otherwise false
     */
    @Override
    public boolean update(Image image) {
        try {
            getHibernateTemplate().update(image);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the image object corresponding the give id can be removed
     * from the database.
     * @param imageId the id number of the image to be removed
     * @return true if the image object can be removed, otherwise returns false
     */
    @Override
    public boolean canBeDeleted(int imageId) {
        List result = null;

        result = getHibernateTemplate().findByNamedParam("from Shelf as shelf where shelf.image.id = :id", "id", imageId);
        if (!result.isEmpty()) {
            return false;
        }

        result = getHibernateTemplate().findByNamedParam("from LibraryCollection as collection where collection.image.id = :id", "id", imageId);
        if (!result.isEmpty()) {
            return false;
        }
        result = getHibernateTemplate().findByNamedParam("from Library as library where library.image.id = :id", "id", imageId);
        if (!result.isEmpty()) {
            return false;
        }
        return true;
    }
}
