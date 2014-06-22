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
package com.pkrete.locationservice.endpoint.model.language;

import java.io.Serializable;

/**
 * The {@link Language Language} class represents a language of the UI. This
 * class is used to define the language of the attributes of {@link Location
 * Location} object.
 *
 * @author Petteri Kivimäki
 */
public class Language implements Serializable, Comparable {
    /**
     * An id number that identifies the object in the database.
     */
    private int id;
    /**
     * Code of the language.
     */
    private String code;
    /**
     * Name of the language.
     */
    private String name;

   /**
     * Constructs and initializes a Language object with no values set.
     */
    public Language() {

    }

    /**
     * Constructs and initializes a Language object with the given name.
     * @param name name of the language object
     */
    public Language(String name) {
        this.name = name;
    }

    /**
     * Returns the id number of the object in the database.
     * @return the id number of the object
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * Returns the code of this language object.
     * @return code of the language object
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Returns the name of this language object.
     * @return name of the language object
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Changes the id number of the object.
     * @param id the new id number
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Changes the code of the object.
     * @param code the new code of the language
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Changes the name of the object.
     * @param name the new name of the language
     */
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    /**
     * Compares this object with the specified object for order.
     * @param o the Object to be compared
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    public int compareTo(Object o) {
        return compareTo((Language) o);
    }

    /**
     * Compares this object with the specified object for order.
     * @param language the Language object to be compared
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    public int compareTo(Language language) {
        return this.name.compareTo(language.name);
    }

    @Override
    /**
     * Indicates whether some other object is "equal to" this Language.
     * @param o the reference object with which to compare
     * @return true only if the specified object is also an Language and it
     * imposes the same ordering as this Language
     */
    public boolean equals(Object o) {
        if (o instanceof Language && id == ((Language)o).id) {
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
        return this.id;
    }
}

