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
package com.pkrete.locationservice.admin.model.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pkrete.locationservice.admin.deserializers.UserInfoJSONDeserializer;
import java.io.Serializable;

/**
 * All the users must be a member of a user group. This class represents User -
 * UserGroup pairs.
 *
 * @author Petteri Kivimäki
 */
@JsonDeserialize(using = UserInfoJSONDeserializer.class)
public class UserInfo implements Serializable, Comparable {
    /* An id number that identifies the object in the database. */

    private int id;
    /* User obejct related to the pair. */
    private UserFull user;
    /* UserGroup object related to the pair. */
    private UserGroup group;

    /**
     * Changes the id number of the object.
     *
     * @param id the new id number
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Changes the user of this pair.
     *
     * @param user new user
     */
    public void setUser(UserFull user) {
        this.user = user;
    }

    /**
     * Changes the user group of this pair.
     *
     * @param group new group
     */
    public void setGroup(UserGroup group) {
        this.group = group;
    }

    /**
     * Returns the id number of the object in the database.
     *
     * @return the id number of the object
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the user related to this pair.
     *
     * @return user related to this pair
     */
    public UserFull getUser() {
        return this.user;
    }

    /**
     * Returns the user group related to this pair.
     *
     * @return user group related to this pair
     */
    public UserGroup getGroup() {
        return this.group;
    }

    @Override
    /**
     * Compares this object with the specified object for order.
     *
     * @param UserInfo the object to be compared
     * @return a negative integer, zero, or a positive integer as this object is
     * less than, equal to, or greater than the specified object
     */
    public int compareTo(Object o) {
        return compareTo((UserInfo) o);
    }

    /**
     * Compares this object with the specified object for order.
     *
     * @param UserInfo the object to be compared
     * @return a negative integer, zero, or a positive integer as this object is
     * less than, equal to, or greater than the specified object
     */
    public int compareTo(UserInfo UserInfo) {
        return this.user.getUsername().compareTo(UserInfo.user.getUsername());
    }

    @Override
    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare
     * @return true if this object is the same as the o argument; false
     * otherwise
     */
    public boolean equals(Object o) {
        if (o instanceof UserInfo && id == ((UserInfo) o).id) {
            return true;
        }
        return false;
    }

    @Override
    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    public int hashCode() {
        return this.id;
    }
}
