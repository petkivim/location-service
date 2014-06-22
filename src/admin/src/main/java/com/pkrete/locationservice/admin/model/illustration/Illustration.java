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
package com.pkrete.locationservice.admin.model.illustration;

import com.pkrete.locationservice.admin.model.DateInfo;
import com.pkrete.locationservice.admin.model.owner.Owner;
import java.io.Serializable;

/**
 * This abstract class defines the basic structure for all the objects representing images.
 * Images can be related to libraries, collections and shelves. An image can be for example
 * a photograph or a map. This class contains implementations of all the methods and subclasses
 * can override the methods if necessary.
 *
 * @author Petteri Kivimäki
 */
public abstract class Illustration extends DateInfo implements Serializable, Comparable {

    /**
     * An id number that identifies the object in the database.
     */
    protected int id;
    /**
     * A string that defines the name of the file that the object represents.
     */
    private String path;
    /**
     * Tells if the object is stored on the same server as the Location Service.
     */
    private boolean isExternal;
    /**
     * Short description of the illustration. For example the name of the object.
     */
    private String description;
    /**
     * A string that is used for the url address received from addimage
     * or editimage forms. This variable is not mapped to database.
     */
    private String url;
    /**
     * A string that is used for the image path received from addimage
     * or editimage forms. This variable is not mapped to database.
     */
    private String filePath;
    /**
     * The owner of this object.
     */
    private Owner owner;

    /**
     * Constructs and initializes an object with no path and sets isExternal to false.
     */
    public Illustration() {
        this.description = "";
        this.path = "";
        this.isExternal = false;
    }

    /**
     * Constructs and initializes an object with the given path and sets isExternal to false.
     * @param path the location of the image that the object represents
     */
    public Illustration(String path) {
        this();
        this.path = path;
    }

    /**
     * Constructs and initializes an object with the given path and sets isExternal to false.
     * @param the description of the illustration
     * @param path the location of the image that the object represents
     */
    public Illustration(String path, String description) {
        this(path);
        this.description = description;
    }

        /**
     * Constructs and initializes an object with the given path and sets isExternal to false.
     * @param url URL of the illustration
     * @param filePath name of the file that the illustration represents
     * @param description description of the illustration
     * @param path the location of the image that the object represents
     */
    public Illustration(String url, String filePath, String description) {
        this.url = url;
        this.filePath = filePath;
        this.description = description;
    }
    
    /**
     * Constructs and initializes an object with the given path and isExternal value.
     * @param path the location of the image that the object represents
     * @param the description of the illustration
     * @param isExternal the value that tells if the object is located on the same server as the Location Service
     */
    public Illustration(String path, String description, boolean isExternal) {
        this(path, description);
        this.isExternal = isExternal;
    }

    /**
     * Returns the id number of the object in the database.
     * @return the id number of the object
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the name of the file or URL that the illustration object 
     * represents. If the file is stored in localhost, then file name is
     * returned. Otherwise retuns an URL.
     * @return file name or URL
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Tells if the path is on the same server as the Location Service.
     * @return the value that tells if the map is located on the same server as the Location Service
     */
    public boolean getIsExternal() {
        return this.isExternal;
    }

    /**
     * Returns the description of the illustration.
     * @return the description of the illustration.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the owner of the illustration.
     * @return owner of the location
     */
    public Owner getOwner() {
        return this.owner;
    }

    /**
     * Changes the id number of the object.
     * @param id the new id number
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Changes the name of the illustration file that the object represents or 
     * the URL of the illustration, if the illustration is stored in another 
     * server.
     * @param new file name or URL
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Changes the description of the illustration.
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Changes the owner of the illustration.
     * @param owner new owner of the illustration
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * Changes the isExternal value of the object.
     * @param isExternal the new isExternal value
     */
    public void setIsExternal(boolean isExternal) {
        this.isExternal = isExternal;
    }

    /**
     * Returns the url of the illustration.
     * @return the url of the illustration.
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Returns the filePath of the illustration.
     * @return the filePath of the illustration.
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * Changes the url of the object.
     * @param url the new url value
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Changes the filePath of the object.
     * @param filePath the new filePath value
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    /**
     * Compares this object with the specified object for order.
     * @param o the Object to be compared
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    public int compareTo(Object o) {
        return compareTo((Illustration) o);
    }

    /**
     * Compares this object with the specified object for order.
     * @param illustartion the Illustration object to be compared
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    public int compareTo(Illustration illustration) {
        return this.description.compareTo(illustration.description);
    }

    @Override
    /**
     * Indicates whether some other object is "equal to" this Illustration.
     * @param o the reference object with which to compare
     * @return true only if the specified object is also an Illustration and it
     * imposes the same ordering as this Illustration
     */
    public boolean equals(Object o) {
        if (o instanceof Illustration && id == ((Illustration) o).id) {
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
