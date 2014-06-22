/**
 * This file is part of Location Service :: Endpoint.
 * Copyright (C) 2014 Petteri Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.model.location;

import com.pkrete.locationservice.endpoint.model.language.Language;
import java.io.Serializable;

/**
 * This class represents a description related to a location object.
 * Location object can have several descriptions, one in each language
 * configured in the system. Each description can be related to only
 * one location at a time.
 *
 * @author Petteri Kivimäki
 */
public class Description implements Serializable {
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
     * Initializes and constructs a new Descroption with the given language.
     * @param lang language of the description
     */
    public Description(Language lang) {
        this.id = 0;
        this.language = lang;
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
}
