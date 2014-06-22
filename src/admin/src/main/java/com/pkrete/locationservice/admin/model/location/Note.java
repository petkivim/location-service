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
 * This class represents a note related to a location object.
 * Location object can have several notes, one in each language
 * configured in the system. Each note can be related to only
 * one location at a time.
 *
 * @author Petteri Kivimäki
 */
public class Note implements Serializable, Comparable {
    /* An id number that identidies the object in the database. */

    private int id;
    /* Note of the object which this note is related to. */
    private String note;
    /* Language of this note. */
    private Language language;

    /**
     * Initializes and constructs a new Descroption object.
     */
    public Note() {
        this.id = 0;
    }

    /**
     * Initializes and constructs a new Note with the given language.
     * @param lang language of the note
     */
    public Note(Language lang) {
        this.id = 0;
        this.language = lang;
    }

    /**
     * Initializes and constructs a new Note with the given id, language
     * and note value.
     * @param id id number of the note
     * @param lang language of the note
     * @param value value of the note
     */
    public Note(int id, Language lang, String value) {
        this(lang);
        this.id = id;
        this.note = value;
    }

    /**
     * Returns the id number of this note in the database.
     * @return id number of this object
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the note of the object which this
     * note is related to.
     * @return note of the object
     */
    public String getNote() {
        return this.note;
    }

    /**
     * Returns the language of this note.
     * @return language of this note
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
     * Changes the value of the note of the object which
     * this note is related to.
     * @param note new note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Changes the language of this note.
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
        return compareTo((Note) o);
    }

    /**
     * Compares this object with the specified object for order.
     * @param note the object to be compared
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    public int compareTo(Note note) {
        return this.language.getName().compareTo(note.language.getName());
    }

    @Override
    /**
     * Indicates whether some other object is "equal to" this one.
     * @param o the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    public boolean equals(Object o) {
        if (o instanceof Note) {
            if (id != 0 && id == ((Note) o).id) {
                return true;
            } else if (note.equals(((Note) o).note)
                    && language.getId() == ((Note) o).language.getId()) {
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
