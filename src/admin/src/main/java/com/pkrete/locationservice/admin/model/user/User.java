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
package com.pkrete.locationservice.admin.model.user;

import com.pkrete.locationservice.admin.model.DateInfo;
import com.pkrete.locationservice.admin.model.owner.Owner;
import java.io.Serializable;

/**
 * All the users  must have a username and password that they can login
 * to the service. This class contains only the username and the password is
 * located in the {@link UserFull UserFull} class that extends this class.
 * After the login all the users are handled as User objects that doesn't
 * offer access to the password. This class represents a single user of the
 * LocationServiceAdmin.
 *
 * @author Petteri Kivimäki
 */
public class User extends DateInfo implements Serializable, Comparable {

    /* First name of the user. */
    private String firstName;
    /* Last name of the user. */
    private String lastName;
    /* Username of the user. */
    private String username;
    /* Email address of the user. */
    private String email;
    /* Owner object related to the user. */
    private Owner owner;
    /* Organization that the user represents. */
    private String organization;

    /**
     * Initializes and constructs a User object with no values set.
     */
    public User() {
    }

    /**
     * Changes the first name of the user.
     * @param firstName new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Changes the last name of the user.
     * @param lastName new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Changes the username of the user.
     * @param username new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Changer the owner object related to the user.
     * @param owner new owner object
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * Returns the first of the user.
     * @return first name of the user
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Returns the last of the user.
     * @return last name of the user
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Returns the username of the user.
     * @return username of the user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns the owner obejct related to the user.
     * @return owner object related to the user
     */
    public Owner getOwner() {
        return this.owner;
    }

    /**
     * Returns the email address of the user.
     * @return email address of the user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Changes the email address of the user
     * @param email new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the organization of the user.
     * @return organization that the user represents
     */
    public String getOrganization() {
        return this.organization;
    }

    /**
     * Changes the organization that the user represents.
     * @param organization new organization
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Override
    /**
     * Compares this object with the specified object for order.
     * @param user the object to be compared
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    public int compareTo(Object o) {
        return compareTo((User) o);
    }

    /**
     * Compares this object with the specified object for order.
     * @param user the object to be compared
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    public int compareTo(User user) {
        return this.username.compareTo(user.username);
    }

    @Override
    /**
     * Indicates whether some other object is "equal to" this one.
     * @param o the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    public boolean equals(Object o) {
        if (o instanceof User && username.equals(((User) o).username)) {
            return true;
        }
        return false;
    }

    @Override
    /**
     * Returns a hash code value for the object.
     * @return a hash code value for this object
     */
    public int hashCode() {
        return this.username.hashCode();
    }
}
