/**
 * This file is part of Location Service :: Endpoint. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.model.location;

import com.pkrete.locationservice.endpoint.model.owner.Owner;
import com.pkrete.locationservice.endpoint.model.illustration.Image;
import com.pkrete.locationservice.endpoint.model.illustration.Map;
import com.pkrete.locationservice.endpoint.model.subjectmatter.SubjectMatter;
import java.io.Serializable;
import java.util.*;

/**
 * This abstract class defines the basic structure for all the objects
 * representing locations like libraries, collections or shelves. All the
 * subclasses must implement the abstract <i>getCallNo</i> method.
 *
 * @author Petteri Kivimäki
 */
public abstract class Location implements SimpleLocation, Serializable {

    /**
     * An id number that identifies the location in the database.
     */
    private int locationId;
    /**
     * Name of the location. For example the full name of the location.
     */
    protected String name;
    /**
     * A string that equals to the location's location code in the ILS's
     * database and OPAC.
     */
    protected String locationCode;
    /**
     * Tells if the location code should be considered as a substring in the
     * beginning of a string, or if it's an entire word.
     */
    protected boolean isSubstring;
    /**
     * Descriptions about the location. It's possible to add one description in
     * each language.
     */
    protected List<Description> descriptions;
    /**
     * Notes related to the location. It's possible to add one note in each
     * language.
     */
    protected List<Note> notes;
    /**
     * A list of area objects that speficy the position of the location on a
     * map.
     */
    protected List<Area> areas;
    /**
     * A map on which the location of the collection is drawn.
     */
    protected Map map;
    /**
     * An image that is related to the location
     */
    protected Image image;
    /**
     * A list of subject matters that describe the location.
     */
    protected List<SubjectMatter> subjectMatters;
    /**
     * Number of the floor where the location is located.
     */
    protected String floor;
    /**
     * The owner of this object.
     */
    protected Owner owner;
    /**
     * A primary note, that's not available for the templates.
     */
    protected String staffNotePri;
    /**
     * A secondary note, that's not available for the templates.
     */
    protected String staffNoteSec;

    /**
     * Returns the call number of the location.
     *
     * @return the call number of the location.
     */
    public abstract String getCallNo();

    /**
     * Returns the call number of the location in a format that is used in
     * templates' names. All the whitespaces in the call number are replaced
     * with underscores.
     *
     * @param incCollectionCode boolean value that tells if collection code
     * should be included in the returned call number. If true, collection code
     * is included only if it exists and is not empty
     * @return the call number of the location
     */
    public abstract String getCallNoForTemplateName(boolean incCollectionCode);

    /**
     * Contructs and initializes a location object with no location code.
     */
    public Location() {
        this.areas = new ArrayList<Area>();
        this.subjectMatters = new ArrayList<SubjectMatter>();
        this.descriptions = new ArrayList<Description>();
        this.notes = new ArrayList<Note>();
        this.isSubstring = false;
    }

    /**
     * Construct and initializes a location with the given location code.
     *
     * @param locationCode the location code of the location
     */
    public Location(String locationCode) {
        this.locationCode = locationCode;
        this.areas = new ArrayList<Area>();
        this.subjectMatters = new ArrayList<SubjectMatter>();
        this.descriptions = new ArrayList<Description>();
        this.notes = new ArrayList<Note>();
        this.isSubstring = false;
    }

    /**
     * Returns the locationId of the location in the database.
     *
     * @return the id number of the location in the database
     */
    public int getLocationId() {
        return this.locationId;
    }

    /**
     * Returns the name of the location.
     *
     * @return the name of the location.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the location code of the location.
     *
     * @return the location code of the location
     */
    public String getLocationCode() {
        return this.locationCode;
    }

    /**
     * Returns the map related to the location.
     *
     * @return the map related to the location
     */
    public Map getMap() {
        return this.map;
    }

    /**
     * Returns the image related to the location.
     *
     * @return the image related to the location
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Returns the subject matters related to this location.
     *
     * @return list of subject matters related to this location
     */
    public List<SubjectMatter> getSubjectMatters() {
        return this.subjectMatters;
    }

    /**
     * Returns the descriptions related to this location.
     *
     * @return descriptions related to this location
     */
    public List<Description> getDescriptions() {
        return this.descriptions;
    }

    /**
     * Returns a list of notes related to this location.
     *
     * @return notes related to this location
     */
    public List<Note> getNotes() {
        return this.notes;
    }

    /**
     * Returns the floor where the location is located.
     *
     * @return floor where the location is located
     */
    public String getFloor() {
        return this.floor;
    }

    /**
     * Returns the primary staff note, that's accessbile through the XML export,
     * but not through the templates.
     *
     * @return primary staff note
     */
    public String getStaffNotePri() {
        return this.staffNotePri;
    }

    /**
     * Returns the secondary staff note, that's accessbile through the XML
     * export, but not through the templates.
     *
     * @return secondary staff note
     */
    public String getStaffNoteSec() {
        return this.staffNoteSec;
    }

    /**
     * Returns a boolean value that tells if the location code should be
     * considered as a substring in the beginning of a string, or if it's an
     * entire word.
     *
     * @return true if is a substring, otherwise false
     */
    public boolean getIsSubstring() {
        return this.isSubstring;
    }

    /**
     * Changes the id number of the location.
     *
     * @param locationId the new id number
     */
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    /**
     * Changes the name of the location.
     *
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Changes the location code of the location
     *
     * @param locationCode the new location code
     */
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    /**
     * Changes the map related to the location
     *
     * @param map the new map
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * Changes the image related to the location
     *
     * @param image the new image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Changes all the subject matters related to this location.
     *
     * @param subjectMatters new list of subject matters
     */
    public void setSubjectMatters(List<SubjectMatter> subjectMatters) {
        this.subjectMatters = subjectMatters;
    }

    /**
     * Changes all the descriptions related to this location.
     *
     * @param descriptions new list of descriptions
     */
    public void setDescriptions(List<Description> descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * Changes all the notes related to this location.
     *
     * @param notes new list of notes
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    /**
     * Changes the floor where the location is located.
     *
     * @param floor new floor
     */
    public void setFloor(String floor) {
        this.floor = floor;
    }

    /**
     * Changes the owner of the location.
     *
     * @param owner new owner of the location
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * Returns the owner of the location.
     *
     * @return owner of the location
     */
    public Owner getOwner() {
        return this.owner;
    }

    /**
     * Returns the areas related to the location
     *
     * @return the area related to the location
     */
    public List<Area> getAreas() {
        return this.areas;
    }

    /**
     * Changes all the areas related to the location
     *
     * @param areas the new areas
     */
    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    /**
     * Adds a new area related to the location.
     *
     * @param area the new area
     */
    public void addArea(Area area) {
        this.areas.add(area);
    }

    /**
     * Removes the specified area from the list of areas related to the
     * location.
     *
     * @param area the area to be removed
     */
    public void removeArea(Area area) {
        this.areas.remove(area);
    }

    /**
     * Changes the value of the primary staff note, that's accessbile through
     * the XML export, but not through the templates.
     *
     * @param note new primary staff note
     */
    public void setStaffNotePri(String note) {
        this.staffNotePri = note;
    }

    /**
     * Changes the value of the secondary staff note, that's accessbile through
     * the XML export, but not through the templates.
     *
     * @param new secondary staff note
     */
    public void setStaffNoteSec(String note) {
        this.staffNoteSec = note;
    }

    /**
     * Changes the boolean value that tells if the location code should be
     * considered as a substring in the beginning of a string, or if it's an
     * entire word.
     *
     * @param sub new value
     */
    public void setIsSubstring(boolean sub) {
        this.isSubstring = sub;
    }

    /**
     * Default implementation. False is always returned.
     *
     * @return always false
     */
    public boolean hasCollectionCode() {
        return false;
    }
}
