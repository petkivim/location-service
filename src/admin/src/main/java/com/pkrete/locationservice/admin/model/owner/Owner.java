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
package com.pkrete.locationservice.admin.model.owner;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pkrete.locationservice.admin.deserializers.OwnerJSONDeserializer;
import com.pkrete.locationservice.admin.model.DateInfo;
import com.pkrete.locationservice.admin.model.language.Language;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In a multi user environment where several organizations are sharing the same
 * database, all the objects in the database need to have an owner. All the
 * location objects and objects related to them have an owner as well as the
 * objects representing the users. Users related to a certain owner object can
 * access and modify only objects related to the same owner. System
 * administrators are the only exception: they can modify all the objects in the
 * database no matter the owner. This class defines the owner object related to
 * the other objects in the database.
 *
 * @author Petteri Kivimäki
 */
@JsonDeserialize(using = OwnerJSONDeserializer.class)
public class Owner extends DateInfo implements Serializable, Comparable {

    /**
     * An id number that identifies the object in the database.
     */
    private int id;
    /**
     * Unique code that identifies the owner among other owners.
     */
    private String code;
    /**
     * Previous code of the language that is used when a language object is
     * being edited. This value is not saved to DB.
     */
    private String codePrevious;
    /**
     * Name of the owner object.
     */
    private String name;
    /**
     * A list of languages related to this owner.
     */
    protected List<Language> languages;
    /**
     * Default drawing color that is used for drawing the locations on the map.
     */
    private String color;
    /**
     * Opacity of the drawing color.
     */
    private String opacity;
    /**
     * Preprocessing redirects related to this owner.
     */
    private List<PreprocessingRedirect> preprocessingRedirects;
    /**
     * Not found redirects related to this owner.
     */
    private List<NotFoundRedirect> notFoundRedirects;
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
     * Constructs and initializes an Owner objects with no values set.
     */
    public Owner() {
        this.languages = new ArrayList<Language>();
        this.color = "dd0000";
        this.opacity = "150";
        this.exporterVisible = false;
        this.allowedIPs = "";
        this.locatingStrategy = LocatingStrategy.INDEX;
        this.preprocessingRedirects = new ArrayList<PreprocessingRedirect>();
        this.notFoundRedirects = new ArrayList<NotFoundRedirect>();
    }

    /**
     * Constructs and initializes an Owner object with the given code and name.
     *
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
     *
     * @return the id number of the object
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the code of the owner.
     *
     * @return the code identifying the owner
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Returns the previous code of this owner object.
     *
     * @return previous code of the owner object
     */
    public String getCodePrevious() {
        return this.codePrevious;
    }

    /**
     * Return the name of the owner.
     *
     * @return the name of the owner
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the default drawing color of this owner.
     *
     * @return default drawing color
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Returns the opacity of the drawing color.
     *
     * @return opacity of the drawing color
     */
    public String getOpacity() {
        return this.opacity;
    }

    /**
     * Returns a list of preprocessing redirects related to this owner.
     *
     * @return list of preprocessing redirects
     */
    public List<PreprocessingRedirect> getPreprocessingRedirects() {
        return this.preprocessingRedirects;
    }

    /**
     * Returns a list of not found redirects related to this owner.
     *
     * @return list of not found redirects
     */
    public List<NotFoundRedirect> getNotFoundRedirects() {
        return this.notFoundRedirects;
    }

    /**
     * Returns a boolean value that tells if the Exporter interface is open for
     * everyone.
     *
     * @return true if Exported is open for everyone, otherwise false
     */
    public boolean getExporterVisible() {
        return this.exporterVisible;
    }

    /**
     * Returns a string containing the IP addresses that are allowed to access
     * the Exporter interface, even if the interface would be closed.
     *
     * @return IP addresses that are allowed to access the Exporter interface
     */
    public String getAllowedIPs() {
        return this.allowedIPs;
    }

    /**
     * Changes the id number of the object.
     *
     * @param id the new id number
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Changes the code of this owner.
     *
     * @param code new code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Changes the previous code of the object.
     *
     * @param code the new previous code of the owner
     */
    public void setCodePrevious(String code) {
        this.codePrevious = code;
    }

    /**
     * Changes the name of this owner.
     *
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Changes the default drawing color.
     *
     * @param color new drawing color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Changes the opacity of the drawing color.
     *
     * @param opacity new opacity
     */
    public void setOpacity(String opacity) {
        this.opacity = opacity;
    }

    /**
     * Changes the list of preprocessing redirects.
     *
     * @param redirects new list
     */
    public void setPreprocessingRedirects(List<PreprocessingRedirect> redirects) {
        this.preprocessingRedirects = redirects;
    }

    /**
     * Changes the list of not found redirects.
     *
     * @param redirects new list
     */
    public void setNotFoundRedirects(List<NotFoundRedirect> redirects) {
        this.notFoundRedirects = redirects;
    }

    /**
     * Removes the given preprocessing redirect from the list of preprocessing
     * redirects related to this owner.
     *
     * @param redirect preprocessing redirect to be removed
     */
    public void removePreprocessingRedirect(PreprocessingRedirect redirect) {
        this.preprocessingRedirects.remove(redirect);
    }

    /**
     * Removes the given not found redirect from the list of not found redirects
     * related to this owner.
     *
     * @param redirect not found redirect to be removed
     */
    public void removeNotFoundRedirect(NotFoundRedirect redirect) {
        this.notFoundRedirects.remove(redirect);
    }

    /**
     * Returns a list of languages related to this owner.
     *
     * @return list of languages related to this owner
     */
    public List<Language> getLanguages() {
        if (languages != null) {
            Collections.sort(languages);
        }
        return this.languages;
    }

    /**
     * Changes the list of languages related to this owner.
     *
     * @param languages new list of languages
     */
    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    /**
     * Changes the boolean value that tells if the Exporter interface is open
     * for everyone.
     *
     * @param visible new value
     */
    public void setExporterVisible(boolean visible) {
        this.exporterVisible = visible;
    }

    /**
     * Changes the string containing the IP addresses that are allowed to access
     * the Exporter interface, even if the interface would be closed.
     *
     * @param ips new ips
     */
    public void setAllowedIPs(String ips) {
        this.allowedIPs = ips;
    }

    /**
     * Returns the locating strategy that's used for searching call numbers from
     * the database.
     *
     * @return locating strategy that's used for searching call numbers from the
     * database.
     */
    public LocatingStrategy getLocatingStrategy() {
        return locatingStrategy;
    }

    /**
     * Sets the locating strategy that's used for searching call numbers from
     * the database.
     *
     * @param locatingStrategy new strategy
     */
    public void setLocatingStrategy(LocatingStrategy locatingStrategy) {
        this.locatingStrategy = locatingStrategy;
    }

    @Override
    /**
     * Compares this object with the specified object for order.
     *
     * @param o the Object to be compared
     * @return a negative integer, zero, or a positive integer as this object is
     * less than, equal to, or greater than the specified object
     */
    public int compareTo(Object o) {
        return compareTo((Owner) o);
    }

    /**
     * Compares this object with the specified object for order.
     *
     * @param illustartion the Owner object to be compared
     * @return a negative integer, zero, or a positive integer as this object is
     * less than, equal to, or greater than the specified object
     */
    public int compareTo(Owner owner) {
        return this.name.compareTo(owner.name);
    }

    @Override
    /**
     * Indicates whether some other object is "equal to" this Owner.
     *
     * @param o the reference object with which to compare
     * @return true only if the specified object is also an Owner and it imposes
     * the same ordering as this Owner
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
     *
     * @return a hash code value for this object
     */
    public int hashCode() {
        return this.id;
    }

    public void prepareLists() {
        /* Not found redirects list. */
        if (notFoundRedirects == null) {
            notFoundRedirects = new ArrayList<NotFoundRedirect>();
        }
        if (notFoundRedirects.isEmpty()) {
            notFoundRedirects.add(new NotFoundRedirect());
        }

        /* Preprocessing redirects list. */
        if (preprocessingRedirects == null) {
            preprocessingRedirects = new ArrayList<PreprocessingRedirect>();
        }
        if (preprocessingRedirects.isEmpty()) {
            preprocessingRedirects.add(new PreprocessingRedirect());
        }
    }
}
