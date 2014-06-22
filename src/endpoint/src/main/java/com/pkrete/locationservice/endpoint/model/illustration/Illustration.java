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
package com.pkrete.locationservice.endpoint.model.illustration;

/**
 * This abstract class defines the basic structure for all the objects representing images.
 * Images can be related to libraries, collections and shelves. An image can be for example
 * a photograph or a map. This class contains implementations of all the methods and subclasses
 * can override the methods if necessary.
 *
 * @author Petteri Kivimäki
 */
public abstract class Illustration {
    /**
     * An id number that identifies the object in the database.
     */
    private int id;
    /**
     * A string that defines the location of the image that the object represents.
     */
    private String path;

    /**
     * Tells if the object is stored on the same server as the Location Service.
     */
    private boolean isExternal;

   /**
     * Constructs and initializes an object with no path and sets isExternal to false.
     */
    public Illustration()
    {
        isExternal = false;
    }

    /**
     * Constructs and initializes an object with the given path and sets isExternal to false.
     * @param path the location of the image that the object represents
     */
    public Illustration(String path)
    {
        this.path = path;
        this.isExternal = false;
    }

    /**
     * Constructs and initializes an object with the given path and isExternal value.
     * @param path the location of the image that the object represents
     * @param isExternal the value that tells if the object is located on the same server as the Location Service
     */
    public Illustration(String path, boolean isExternal)
    {
        this.path = path;
        this.isExternal = isExternal;
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
     * Returns the path of the image that the object represents. The path is a directory path if the iamge is on the same server.
     * Otherwise the path is an URL address.
     * @return the path of the image that the object represents
     */
    public String getPath()
    {
        return this.path;
    }

    /**
     * Tells if the path is on the same server as the Location Service.
     * @return the value that tells if the map is located on the same server as the Location Service
     */
    public boolean getIsExternal()
    {
        return this.isExternal;
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
     * Changes the path of the image that the object represents.
     * @param path the new path of the image
     */
    public void setPath(String path)
    {
        this.path = path;
    }
    
    /**
     * Changes the isExternal value of the object.
     * @param isExternal the new isExternal value
     */
    public void setIsExternal(boolean isExternal)
    {
        this.isExternal = isExternal;
    }
}
