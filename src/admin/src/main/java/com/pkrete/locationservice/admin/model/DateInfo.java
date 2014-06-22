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
package com.pkrete.locationservice.admin.model;

import java.util.Date;

/**
 * This abstract class represents date and user information that is stored
 * when a new object is created or an existing object is updated.
 *
 * @author Petteri Kivimäki
 */
public abstract class DateInfo {
    /* Creation date. */
    private Date created;
    /* Last update date. */
    private Date updated;
    /* User who created this object. */
    private String creator;
    /* User who last updated this obejct. */
    private String updater;

    /**
     * Change the creation date of this object.
     * @param created new creation nate
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Change the last update date of this object.
     * @param updated new update date
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    /**
     * Change the username of the creator of this object.
     * @param user new username
     */
    public void setCreator(String user) {
        this.creator = user;
    }

    /**
     * Change the username of the last updater of this object.
     * @param user new username
     */
    public void setUpdater(String user) {
        this.updater = user;
    }

    /**
     * Return the creation date of this object.
     * @return creation date of this object
     */
    public Date getCreated() {
        return this.created;
    }

    /**
     * Return the last update date of this object
     * @return update date of thi object
     */
    public Date getUpdated() {
        return this.updated;
    }

    /**
     * Return the username of the user who created this object.
     * @return user who created this object
     */
    public String getCreator() {
        return this.creator;
    }

    /**
     * Return the username of the user who last updated this object.
     * @return user who last updated this object
     */
    public String getUpdater() {
        return this.updater;
    }
}
