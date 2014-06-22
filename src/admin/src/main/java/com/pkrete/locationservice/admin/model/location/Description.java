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
package com.pkrete.locationservice.admin.model.location;

import com.pkrete.locationservice.admin.model.language.Language;
import java.io.Serializable;

/**
 * This class represents a description related to a location object.
 * Location object can have several descriptions, one in each language
 * configured in the system. Each description can be related to only
 * one location at a time.
 *
 * @author Petteri Kivimäki
 */
public class Description implements Serializable, Comparable {
    /* An id number that identidies the object in the database. */

    private int id;
    /* Description of the object which this description is related to. */
    private String description;
    /* Language of this description. */
    private Language language;

    /**
     * Initializes and constructs a new Descroption object.
     */
    public Description() {
        this.id = 0;
    }

    /**
     * Initializes and constructs a new Description with the given language.
     * @param lang language of the description
     */
    public Description(Language lang) {
        this.id = 0;
        this.language = lang;
    }

    /**
     * Initializes and constructs a new Description with the given id, language
     * and description value.
     * @param id id number of the description
     * @param lang language of the description
     * @param value value of the description
     */
    public Description(int id, Language lang, String value) {
        this(lang);
        this.id = id;
        this.description = value;
    }

    /**
     * Returns the id number of this description in the database.
     * @return id number of this object
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the description of the object which this
     * description is related to.
     * @return description of the object
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the language of this description.
     * @return language of this description
     */
    public Language getLanguage() {
        return this.language;
    }

    /**
     * Changes the id number of this object.
     * @param id new id number
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Changes the value of the description of the object which
     * this description is related to.
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Changes the language of this description.
     * @param language new language
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    /**
     * Compares this object with the specified object for order.
     * @param o the object to be compared
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    public int compareTo(Object o) {
        return compareTo((Description) o);
    }

    /**
     * Compares this object with the specified object for order.
     * @param description the object to be compared
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    public int compareTo(Description description) {
        return this.language.getName().compareTo(description.language.getName());
    }

    @Override
    /**
     * Indicates whether some other object is "equal to" this one.
     * @param o the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    public boolean equals(Object o) {
        if (o instanceof Description) {
            if (id != 0 && id == ((Description) o).id) {
                return true;
            } else if (description.equals(((Description) o).description)
                    && language.getId() == ((Description) o).language.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    /**
     * Returns a hash code value for the object.
     * @return a hash code value for this object
     */
    public int hashCode() {
        return this.id;
    }
}
