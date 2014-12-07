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
package com.pkrete.locationservice.admin.model.template;

import com.pkrete.locationservice.admin.model.DateInfo;
import com.pkrete.locationservice.admin.model.owner.Owner;
import java.io.Serializable;

/**
 * This is a base class for classes representing template, JS and CSS files.
 *
 * @author Petteri Kivimäki
 */
public class BaseFile extends DateInfo implements Serializable, Comparable {

    protected int id;
    protected String filename;
    protected String filenameOld;
    protected String contents;
    protected Owner owner;

    /**
     * Creates and initializes a new BaseFile object.
     */
    public BaseFile() {
        this.contents = "";
        this.filenameOld = "";
    }

    /**
     * Creates and initializes a new BaseFile object with the given id,
     * filename, and owner.
     *
     * @param id id of the base file
     * @param filename name of the base file
     * @param owner owner of the base file
     */
    public BaseFile(int id, String filename, Owner owner) {
        this();
        this.id = id;
        this.filename = filename;
        this.owner = owner;
    }

    /**
     * Creates and initializes a new BaseFile object with the given id,
     * filename, contents and owner.
     *
     * @param id id of the base file
     * @param filename name of the base file
     * @param contents contents of the base file
     * @param owner owner of the base file
     */
    public BaseFile(int id, String filename, String contents, Owner owner) {
        this();
        this.id = id;
        this.filename = filename;
        this.contents = contents;
        this.owner = owner;
    }

    /**
     * Creates and initializes a new BaseFile object with the given id,
     * filename, old filename, contents and owner.
     *
     * @param id id of the base file
     * @param filename name of the base file
     * @param filenameOld old name of the base file
     * @param contents contents of the base file
     * @param owner owner of the base file
     */
    public BaseFile(int id, String filename, String filenameOld, String contents, Owner owner) {
        this();
        this.id = id;
        this.filename = filename;
        this.filenameOld = filenameOld;
        this.contents = contents;
        this.owner = owner;
    }

    /**
     * Returns the id of the base file.
     *
     * @return id of the base file
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the base file
     *
     * @param id new value
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the base file
     *
     * @return name of the base file
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the name of the base file.
     *
     * @param filename new name
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Returns the old name of the base file
     *
     * @return old name of the base file
     */
    public String getFilenameOld() {
        return filenameOld;
    }

    /**
     * Sets the old name of the base file.
     *
     * @param filename new old name
     */
    public void setFilenameOld(String filename) {
        this.filenameOld = filename;
    }

    /**
     * Returns the contents of the base file
     *
     * @return contents of the base file
     */
    public String getContents() {
        return contents;
    }

    /**
     * Sets the contents of the base file.
     *
     * @param contents new contents
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * Returns the owner of the base file.
     *
     * @return owner of the base file
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the tepmlate.
     *
     * @param owner new value
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    /**
     * Compares this object with the specified object for order.
     *
     * @param o the object to be compared
     * @return a negative integer, zero, or a positive integer as this object is
     * less than, equal to, or greater than the specified object
     */
    public int compareTo(Object o) {
        return compareTo((BaseFile) o);
    }

    /**
     * Compares this object with the specified object for order.
     *
     * @param base file the object to be compared
     * @return a negative integer, zero, or a positive integer as this object is
     * less than, equal to, or greater than the specified object
     */
    public int compareTo(BaseFile file) {
        return this.filename.compareTo(file.filename);
    }

    @Override
    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare
     * @return true if this object is the same as the obj argument; false
     * otherwise
     */
    public boolean equals(Object o) {
        if (o instanceof BaseFile && filename.equals(((BaseFile) o).filename)) {
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
        return this.filename.hashCode();
    }
}
