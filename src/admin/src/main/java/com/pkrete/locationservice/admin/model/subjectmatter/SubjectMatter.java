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
package com.pkrete.locationservice.admin.model.subjectmatter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pkrete.locationservice.admin.deserializers.SubjectMatterJSONDeserializer;
import com.pkrete.locationservice.admin.model.DateInfo;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.owner.Owner;
import java.io.Serializable;
import java.util.List;

/**
 * The {@link SubjectMatter SubjectMatter} class represents a topic or a subject
 * matter that is related to one or more locations. The subject matter object
 * describes the location and it can be shown in the UI together with the
 * location information. Subject matter can be expressed in any language
 * supported by the system. The {@link Language Language} class expresses the
 * language of the SubjectMatter object.
 *
 * @author Petteri Kivimäki
 */
@JsonDeserialize(using = SubjectMatterJSONDeserializer.class)
public class SubjectMatter extends DateInfo implements Serializable, Comparable {

    /**
     * An id number that identifies the object in the database.
     */
    private int id;
    /**
     * Index term that describes the subject matter.
     */
    private String indexTerm;
    /**
     * Language of the subject matter.
     */
    private Language language;
    /**
     * List of all the locations related to this subject matter.
     */
    private List<Location> locations;
    /**
     * The owner of this object.
     */
    private Owner owner;

    /**
     * Constructs and initializes a SubjectMatter object with no values set.
     */
    public SubjectMatter() {
        this.language = null;
    }

    /**
     * Constructs and initializes a SubjectMatter object with the given index
     * term.
     *
     * @param indexTerm index term that describes this SubjectMatter
     */
    public SubjectMatter(String indexTerm) {
        this.indexTerm = indexTerm;
        this.language = null;
    }

    /**
     * Constructs and initializes a SubjectMatter object with the given index
     * term and language.
     *
     * @param indexTerm index term that describes this SubjectMatter
     * @param language language of this SubjectMatter
     */
    public SubjectMatter(String indexTerm, Language language) {
        this(indexTerm);
        this.language = language;
    }

    /**
     * Constructs and initializes a SubjectMatter object with the given id,
     * index term and language.
     *
     * @param id identifier of the object
     * @param indexTerm index term that describes this SubjectMatter
     * @param language language of this SubjectMatter
     */
    public SubjectMatter(int id, String indexTerm, Language language) {
        this(indexTerm, language);
        this.id = id;
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
     * Returns the index term of this SubjectMatter object.
     *
     * @return index term of the SubjectMatter object
     */
    public String getIndexTerm() {
        return this.indexTerm;
    }

    /**
     * Returns the language of this SujectMatter object.
     *
     * @return language of the SubjectMatter object
     */
    public Language getLanguage() {
        return this.language;
    }

    /**
     * Returns the owner of the subject matter.
     *
     * @return owner of the subject matter
     */
    public Owner getOwner() {
        return this.owner;
    }

    /**
     * Returns the locations related to this subject matter.
     *
     * @return list of locations related to this subject matter
     */
    public List<Location> getLocations() {
        return this.locations;
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
     * Changes the index term of this object.
     *
     * @param indexTerm the new inexTerm of this SubjectMatter
     */
    public void setIndexTerm(String indexTerm) {
        this.indexTerm = indexTerm;
    }

    /**
     * Changes the language of this SubjectMatter object.
     *
     * @param language new language of this object
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * Changes the owner of the subject matter.
     *
     * @param owner new owner of the subject matter
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * Changes all the locations related to this subject matter.
     *
     * @param locations new list of locations
     */
    public void setLocations(List<Location> locations) {
        this.locations = locations;
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
        return compareTo((SubjectMatter) o);
    }

    /**
     * Compares this object with the specified object for order.
     *
     * @param subjectMatter the SubjectMatter object to be compared
     * @return a negative integer, zero, or a positive integer as this object is
     * less than, equal to, or greater than the specified object
     */
    public int compareTo(SubjectMatter subjectMatter) {
        return this.indexTerm.compareTo(subjectMatter.indexTerm);
    }

    @Override
    /**
     * Indicates whether some other object is "equal to" this SubjectMatter.
     *
     * @param o the reference object with which to compare
     * @return true only if the specified object is also a SubjectMatter and it
     * imposes the same ordering as this Language
     */
    public boolean equals(Object o) {
        if (o instanceof SubjectMatter && id == ((SubjectMatter) o).id) {
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
}
