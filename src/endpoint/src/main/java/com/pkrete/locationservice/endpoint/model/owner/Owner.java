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
package com.pkrete.locationservice.endpoint.model.owner;

import com.pkrete.locationservice.endpoint.model.language.Language;
import com.pkrete.locationservice.endpoint.callnoparser.LocatingStrategy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * In a multi user environment where several organizations are sharing the
 * same database, all the objects in the database need to have an owner.
 * All the location objects and objects related to them have an owner as well
 * as the objects representing the users. Users related to a certain owner
 * object can access and modify only objects related to the same owner.
 * System administrators are the only exception: they can modify all the
 * objects in the datababse no matter the owner. This class defines the owner
 * object related to the other objects in the database.
 *
 * @author Petteri Kivimäki
 */
public class Owner implements Serializable, Comparable {

    /**
     * An id number that identifies the object in the database.
     */
    private int id;
    /**
     * Unique code that identifies the owner among other owners.
     */
    private String code;
    /**
     * Name of the owner object.
     */
    private String name;
    /**
     * A list of languages related to this owner.
     */
    protected List<Language> languages;
    /**
     * Default drawing color that is used for drawing
     * the locations on the map.
     */
    private String color;
    /**
     * Opacity of the drawing color.
     */
    private String opacity;
    /* Tells if the Exporter interface is open for everyone. */
    private boolean exporterVisible;
    /* String that contains ip addresses that are allowed to use */
    /* the Exporter interface */
    private String allowedIPs;
    /*
     * Strategy that's used for searching call numbers from the DB.
     */
    private LocatingStrategy locatingStrategy;

    /**
     * Constructs and initializes an Owner obejcts with no values set.
     */
    public Owner() {
        this.languages = new ArrayList<Language>();
        this.color = "dd0000";
        this.opacity = "150";
        this.exporterVisible = false;
        this.locatingStrategy = LocatingStrategy.BASIC;
    }

    /**
     * Constructs and initializes an Owner obejct with the given code and name.
     * @param code code that identifies the owner object among other owners
     * @param name name of the object
     */
    public Owner(String code, String name) {
        this();
        this.code = code;
        this.name = name;
    }

    /**
     * Returns the id number of the object in the database.
     * @return the id number of the object
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the code of the owner.
     * @return the code identifying the owner
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Return the name of the owner.
     * @return the name of the owner
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the default drawing color of this owner.
     * @return default drawing color
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Returns the opacity of the drawing color.
     * @return opacity of the drawing color
     */
    public String getOpacity() {
        return this.opacity;
    }

    /**
     * Returns a boolean value that tells if the Exporter interface
     * is open for everyone.
     * @return true if Exported is open for everyone, otherwise false
     */
    public boolean getExporterVisible() {
        return this.exporterVisible;
    }

    /**
     * Returns a string containing the IP addresses that are allowed
     * to access the Exporter interface, even if the interface would be closed.
     * @return IP addresses that are allowed to access the Exporter interface
     */
    public String getAllowedIPs() {
        return this.allowedIPs;
    }

    /**
     * Changes the id number of the object.
     * @param id the new id number
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Changes the code of this owner.
     * @param code new code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Changes the name of this owner.
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Changes the default drawing color.
     * @param color new drawing color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Changes the opacity of the drawing color.
     * @param opacity new opacity
     */
    public void setOpacity(String opacity) {
        this.opacity = opacity;
    }

    /**
     * Changes the boolean value that tells if the Exporter interface
     * is open for everyone.
     * @param visible new value
     */
    public void setExporterVisible(boolean visible) {
        this.exporterVisible = visible;
    }

    /**
     * Changes the string containing the IP addresses that are allowed
     * to access the Exporter interface, even if the interface would be closed.
     * @param ips new ips
     */
    public void setAllowedIPs(String ips) {
        this.allowedIPs = ips;
    }

    /**
     * Returns a list of languages related to this owner.
     * @return list of languages related to this owner
     */
    public List<Language> getLanguages() {
        return this.languages;
    }

    /**
     * Changes the list of languages related to this owner.
     * @param languages new list of languages
     */
    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    /**
     * Returns the locating strategy that's used for searching call numbers
     * from the database.
     * @return locating strategy that's used for searching call numbers
     * from the database.
     */
    public LocatingStrategy getLocatingStrategy() {
        return locatingStrategy;
    }

    /**
     * Sets the locating strategy that's used for searching call numbers
     * from the database.
     * @param locatingStrategy new strategy
     */
    public void setLocatingStrategy(LocatingStrategy locatingStrategy) {
        this.locatingStrategy = locatingStrategy;
    }

    @Override
    /**
     * Compares this object with the specified object for order.
     * @param o the Object to be compared
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    public int compareTo(Object o) {
        return compareTo((Owner) o);
    }

    /**
     * Compares this object with the specified object for order.
     * @param illustartion the Owner object to be compared
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    public int compareTo(Owner owner) {
        return this.name.compareTo(owner.name);
    }

    @Override
    /**
     * Indicates whether some other object is "equal to" this Owner.
     * @param o the reference object with which to compare
     * @return true only if the specified object is also an Owner and it
     * imposes the same ordering as this Owner
     */
    public boolean equals(Object o) {
        if (o instanceof Owner && id == ((Owner) o).id) {
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
