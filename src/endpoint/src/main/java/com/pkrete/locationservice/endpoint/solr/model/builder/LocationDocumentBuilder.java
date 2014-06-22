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
package com.pkrete.locationservice.endpoint.solr.model.builder;

import com.pkrete.locationservice.endpoint.model.location.Description;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.model.location.Note;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import com.pkrete.locationservice.endpoint.model.search.LocationType;
import com.pkrete.locationservice.endpoint.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.endpoint.solr.model.LocationDocument;
import com.pkrete.locationservice.endpoint.solr.repository.RepositoryConstants;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a helper class that generates LocationDocument objects representing
 * the given Locations.
 * 
 * @author Petteri Kivimäki
 */
public class LocationDocumentBuilder {

    /**
     * Creates a new LocationDocument that represents the given Location.
     * @param location Location object
     * @return LocationDocument that represents the given Location
     */
    public static LocationDocument build(Location location) {
        // Create a new LocationDocument object
        LocationDocument document = new LocationDocument();
        // Set location id
        document.setLocationId(location.getLocationId());
        // Set name
        document.setName(location.getName());
        // Set location type
        document.setLocationType(location.getLocationType());
        // Set some location type specific values
        if (location.getLocationType() == LocationType.COLLECTION) {
            // Set collection code
            document.setCollectionCode(((LibraryCollection) location).getCollectionCode());
            // Set isSubstring
            document.setIsSubstring(location.getIsSubstring());
            // Set parentId
            document.setParentId(((LibraryCollection) location).getLibrary().getLocationId());
            // Set shelf number
            document.setShelf(((LibraryCollection) location).getShelfNumber());
        } else if (location.getLocationType() == LocationType.SHELF) {
            // Set collection code of the parent collection
            document.setCollectionCode(((Shelf) location).getCollection().getCollectionCode());
            // Set parentId
            document.setParentId(((Shelf) location).getCollection().getLocationId());
            // Set grandparentId
            document.setGrandparentId(((Shelf) location).getCollection().getLibrary().getLocationId());
            // Set shelf number
            document.setShelf(((Shelf) location).getShelfNumber());
        }
        // Set location code
        document.setLocationCode(location.getLocationCode());
        // Set call number
        document.setCallNo(location.getCallNo());
        // Set owner id
        document.setOwnerId(location.getOwner().getId());
        // Set owner code
        document.setOwnerCode(location.getOwner().getCode());
        // Set floor
        document.setFloor(location.getFloor());
        // Set notes
        document.setNotes(LocationDocumentBuilder.notesToList(location.getNotes()));
        // Set descriptions
        document.setDescriptions(LocationDocumentBuilder.descriptionsToList(location.getDescriptions()));
        // Set staff notes 1
        document.setStaffNotePri(location.getStaffNotePri());
        // Set staff notes 2
        document.setStaffNoteSec(location.getStaffNoteSec());
        // Set subject matters
        document.setSubjectMatters(LocationDocumentBuilder.subjectsToList(location.getSubjectMatters()));
        // Set subject matter idss
        document.setSubjectMatterIds(LocationDocumentBuilder.subjectIdsToList(location.getSubjectMatters()));
        // Return the document
        return document;
    }

    /**
     * Converts the given int id to a string by adding 'loc-' prefix to the id. 
     * @param id locationId of the Location
     * @return 'loc-' prefix + locationId
     */
    public static String getId(int id) {
        return RepositoryConstants.PREFIX_LOCATION + Integer.toString(id);
    }

    /**
     * Converts the given string id to an int by removing 'loc-' prefix from
     * the id.
     * @param id id of the LocationDocument
     * @return id without 'loc-' prefix, which is the locationId
     */
    public static int getId(String id) {
        id = id.replaceAll(RepositoryConstants.PREFIX_LOCATION, "");
        return Integer.parseInt(id);
    }

    /**
     * Converts the given notes list into list of strings that contains only
     * the value of each note.
     * @param notes list of notes to be converted
     * @return list of strings containing the value of each note
     */
    private static List<String> notesToList(List<Note> notes) {
        List<String> result = new ArrayList<String>();
        if (notes == null) {
            return result;
        }
        for (Note note : notes) {
            result.add(note.getNote());
        }
        return result;
    }

    /**
     * Converts the given descriptions list into list of strings that contains 
     * only the value of each description.
     * @param descriptions list of descriptions to be converted
     * @return list of strings containing the value of each description
     */
    private static List<String> descriptionsToList(List<Description> descriptions) {
        List<String> result = new ArrayList<String>();
        if (descriptions == null) {
            return result;
        }
        for (Description desc : descriptions) {
            result.add(desc.getDescription());
        }
        return result;
    }

    /**
     * Converts the given subject matters list into list of strings that contains 
     * only the value of each subject matter.
     * @param subjects list of subject matters to be converted
     * @return list of strings containing the value of each subject matter
     */
    private static List<String> subjectsToList(List<SubjectMatter> subjects) {
        List<String> result = new ArrayList<String>();
        if (subjects == null) {
            return result;
        }
        for (SubjectMatter subject : subjects) {
            result.add(subject.getIndexTerm());
        }
        return result;
    }

    /**
     * Converts the given subject matters list into list of strings that contains 
     * only the value of each subject matter.
     * @param subjects list of subject matters to be converted
     * @return list of strings containing the value of each subject matter
     */
    private static List<Integer> subjectIdsToList(List<SubjectMatter> subjects) {
        List<Integer> result = new ArrayList<Integer>();
        if (subjects == null) {
            return result;
        }
        for (SubjectMatter subject : subjects) {
            result.add(subject.getId());
        }
        return result;
    }
}
