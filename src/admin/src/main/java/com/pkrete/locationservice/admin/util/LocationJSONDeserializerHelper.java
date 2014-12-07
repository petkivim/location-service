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
package com.pkrete.locationservice.admin.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.location.Area;
import com.pkrete.locationservice.admin.model.location.Description;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.location.Note;
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.service.ImagesService;
import com.pkrete.locationservice.admin.service.LanguagesService;
import com.pkrete.locationservice.admin.service.MapsService;
import com.pkrete.locationservice.admin.service.SubjectMattersService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class offers static helper methods that can be used when deserializing
 * Locations from JSON.
 *
 * @author Petteri Kivimäki
 */
public class LocationJSONDeserializerHelper {

    /**
     * Deserializes name, locationCode, floor, staffNote1, staffNote2, map and
     * image variables.
     *
     * @param location Location object
     * @param node JSON node that contains the data
     */
    public static void deserializeBasicGroup1(Location location, JsonNode node) {
        //int id = node.get("id") == null ? 0 : node.get("id").intValue();
        String name = node.get("name") == null ? "" : node.get("name").textValue();
        String locationCode = node.get("location_code") == null ? "" : node.get("location_code").textValue();
        String floor = node.get("floor") == null ? "" : node.get("floor").textValue();
        String staffNote1 = node.get("staff_note_1") == null ? "" : node.get("staff_note_1").textValue();
        String staffNote2 = node.get("staff_note_2") == null ? "" : node.get("staff_note_2").textValue();
        int imageId = node.get("image_id") == null ? 0 : node.get("image_id").intValue();
        int mapId = node.get("map_id") == null ? 0 : node.get("map_id").intValue();

        // Set values that have been parsed
        location.setName(name);
        location.setLocationCode(locationCode);
        location.setLocationId(0);
        location.setFloor(floor);
        location.setStaffNotePri(staffNote1);
        location.setStaffNoteSec(staffNote2);

        // Get imagesService bean from Application Context
        ImagesService imagesService = (ImagesService) ApplicationContextUtils.getApplicationContext().getBean("imagesService");
        // Get mapsService bean from Application Context
        MapsService mapsService = (MapsService) ApplicationContextUtils.getApplicationContext().getBean("mapsService");

        // Get Image object
        Image image = imagesService.get(imageId);
        // Get Map object
        com.pkrete.locationservice.admin.model.illustration.Map map = mapsService.get(mapId);

        // Set image and map
        location.setImage(image);
        location.setMap(map);
    }

    /**
     * Deserializes descriptions and notes variables.
     *
     * @param location Location object
     * @param node JSON node that contains the data
     */
    public static void deserializeDescriptionsAndNotes(Location location, JsonNode node) {
        // Get languagesService bean from Application Context
        LanguagesService languagesService = (LanguagesService) ApplicationContextUtils.getApplicationContext().getBean("languagesService");
        // Language cache
        Map<Integer, Language> languages = new HashMap<Integer, Language>();

        List<Description> descriptions = new ArrayList<Description>();

        // Does descriptions node exist
        if (node.path("descriptions") != null) {
            // Parse descriptions
            Iterator<JsonNode> ite = node.path("descriptions").elements();
            // Iterate redirects
            while (ite.hasNext()) {
                // Get next description
                JsonNode temp = ite.next();
                // Parse id
                int descId = temp.get("id") == null ? 0 : temp.get("id").intValue();
                // Parse language id
                int languageId = temp.get("lang_id") == null ? 0 : temp.get("lang_id").intValue();
                // Parse value
                String value = temp.get("value") == null ? "" : temp.get("value").textValue();
                // Check if the language is cached
                if (!languages.containsKey(languageId)) {
                    // Get language from DB and cache it
                    languages.put(languageId, languagesService.getLanguageById(languageId));
                }
                descriptions.add(new Description(descId, languages.get(languageId), value));
            }
        }
        // Set descriptions
        location.setDescriptions(descriptions);

        List<Note> notes = new ArrayList<Note>();

        // Does notes node exist
        if (node.path("notes") != null) {
            // Parse notes
            Iterator<JsonNode> ite = node.path("notes").elements();
            // Iterate notes
            while (ite.hasNext()) {
                // Get next note
                JsonNode temp = ite.next();
                // Parse id
                int noteId = temp.get("id") == null ? 0 : temp.get("id").intValue();
                // Parse language id
                int languageId = temp.get("lang_id") == null ? 0 : temp.get("lang_id").intValue();
                // Parse value
                String value = temp.get("value") == null ? "" : temp.get("value").textValue();
                // Check if the language is cached
                if (!languages.containsKey(languageId)) {
                    // Get language from DB and cache it
                    languages.put(languageId, languagesService.getLanguageById(languageId));
                }
                notes.add(new Note(noteId, languages.get(languageId), value));
            }
        }
        // Set notes
        location.setNotes(notes);
    }

    /**
     * Deserializes areas variable.
     *
     * @param location Location object
     * @param node JSON node that contains the data
     */
    public static void deserializeAreas(Location location, JsonNode node) {
        List<Area> areas = new ArrayList<Area>();

        // Does areas node exist
        if (node.path("areas") != null) {
            // Parse areas
            Iterator<JsonNode> ite = node.path("areas").elements();
            // Iterate notes
            while (ite.hasNext()) {
                // Get next area
                JsonNode temp = ite.next();
                // Parse id
                int areaId = temp.get("id") == null ? 0 : temp.get("id").intValue();
                // Parse x1
                int x1 = temp.get("x1") == null ? 0 : temp.get("x1").intValue();
                // Parse y1
                int y1 = temp.get("y1") == null ? 0 : temp.get("y1").intValue();
                // Parse x2
                int x2 = temp.get("x2") == null ? 0 : temp.get("x2").intValue();
                // Parse y2
                int y2 = temp.get("y2") == null ? 0 : temp.get("y2").intValue();
                // Parse angle
                int angle = temp.get("angle") == null ? 0 : temp.get("angle").intValue();

                areas.add(new Area(areaId, x1, y1, x2, y2, angle, location));
            }
        }
        // Set areas
        location.setAreas(areas);
    }

    /**
     * Deserializes subjectMatters variable.
     *
     * @param location Location object
     * @param node JSON node that contains the data
     */
    public static void deserializeSubjectMatters(Location location, JsonNode node) {
        // Get subjectMattersService bean from Application Context
        SubjectMattersService service = (SubjectMattersService) ApplicationContextUtils.getApplicationContext().getBean("subjectMattersService");
        // SubjectMatters cache
        Map<Integer, SubjectMatter> cache = new HashMap<Integer, SubjectMatter>();

        List<SubjectMatter> subjects = new ArrayList<SubjectMatter>();

        // Does subject_matters node exist
        if (node.path("subject_matters") != null) {
            // Parse descriptions
            Iterator<JsonNode> ite = node.path("subject_matters").elements();
            // Iterate redirects
            while (ite.hasNext()) {
                // Get next description
                JsonNode temp = ite.next();
                // Parse id
                int id = temp.get("id") == null ? 0 : temp.get("id").intValue();
                // Check if the subject is cached
                if (!cache.containsKey(id)) {
                    // Get subject from DB and cache it
                    cache.put(id, service.getSubjectMatter(id));
                }
                if (cache.get(id) != null) {
                    subjects.add(cache.get(id));
                }
            }
        }
        // Set subject matters
        location.setSubjectMatters(subjects);
    }
}
