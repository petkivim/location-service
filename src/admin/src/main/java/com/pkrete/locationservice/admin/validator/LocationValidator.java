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
package com.pkrete.locationservice.admin.validator;

import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.service.LocationsService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * This is a base class for Location validators.
 *
 * @author Petteri Kivimäki
 */
public class LocationValidator {

    protected boolean validateAreas = true;
    protected LocationsService locationsService;

    public void setLocationsService(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    public void setValidateAreas(boolean validateAreas) {
        this.validateAreas = validateAreas;
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "error.library.name");
        Location location = (Location) target;

        if (location.getLocationCode().length() > 100) {
            errors.rejectValue("locationCode", "error.location.loccode.length");
        }

        if (location.getName().length() > 100) {
            errors.rejectValue("name", "error.location.name.length");
        }

        if (location.getFloor().length() > 100) {
            errors.rejectValue("floor", "error.location.floor.length");
        }

        if (location.getStaffNotePri().length() > 2000) {
            errors.rejectValue("staffNotePri", "error.location.staffnote1.length");
        }

        if (location.getStaffNoteSec().length() > 2000) {
            errors.rejectValue("staffNoteSec", "error.location.staffnote2.length");
        }

        // Load existing Description ids if Location id is not 0
        Set<Integer> ids = null;
        if (location.getLocationId() > 0 && !location.getDescriptions().isEmpty()) {
            ids = this.locationsService.getDescriptionIdsSet(location);
        }

        // Language cache
        Set<Integer> langCache = new HashSet<Integer>();

        // Validate Descriptions
        for (int i = 0; i < location.getDescriptions().size(); i++) {
            if (location.getDescriptions().get(i).getDescription().length() > 1000) {
                errors.rejectValue("descriptions[" + i + "].description", "error.location.desc.length");
            }
            if (location.getDescriptions().get(i).getLanguage() == null) {
                // Language can not be null
                errors.rejectValue("descriptions[" + i + "].language", "error.location.desc.lang.missing");
            } else if (location.getOwner().getId() != location.getDescriptions().get(i).getLanguage().getOwner().getId()) {
                // Description and Language must have the same Owner
                errors.rejectValue("descriptions[" + i + "].language", "error.location.desc.lang.invalid");
            } else if (langCache.contains(location.getDescriptions().get(i).getLanguage().getId())) {
                // Each language can be used only once
                errors.rejectValue("descriptions[" + i + "].language", "error.location.desc.lang.multi");
            } else {
                langCache.add(location.getDescriptions().get(i).getLanguage().getId());
            }
            if (location.getLocationId() == 0 && location.getDescriptions().get(i).getId() != 0) {
                // If Location's id is 0, all the Descriptions' ids must be 0 too
                errors.rejectValue("descriptions[" + i + "].id", "error.location.desc.id");
            } else if (location.getLocationId() > 0 && location.getDescriptions().get(i).getId() > 0 && !ids.contains(location.getDescriptions().get(i).getId())) {
                // If Description id is not 0 and it didn't exist before, the id is not valid
                errors.rejectValue("descriptions[" + i + "].id", "error.location.desc.id");
            }
        }

        // Load existing Note ids if Location id is not 0
        ids = null;
        if (location.getLocationId() > 0 && !location.getNotes().isEmpty()) {
            ids = this.locationsService.getNoteIdsSet(location);
        }
        // Reset language cache
        langCache = new HashSet<Integer>();

        // Validate Notes
        for (int i = 0; i < location.getNotes().size(); i++) {
            if (location.getNotes().get(i).getNote().length() > 2000) {
                errors.rejectValue("notes[" + i + "].note", "error.location.note.length");
            }
            if (location.getNotes().get(i).getLanguage() == null) {
                // Language can not be null
                errors.rejectValue("notes[" + i + "].language", "error.location.note.lang.missing");
            } else if (location.getOwner().getId() != location.getNotes().get(i).getLanguage().getOwner().getId()) {
                // Note and Language must have the same Owner
                errors.rejectValue("notes[" + i + "].language", "error.location.note.lang.invalid");
            } else if (langCache.contains(location.getNotes().get(i).getLanguage().getId())) {
                // Each Language can be used only once
                errors.rejectValue("descriptions[" + i + "].language", "error.location.note.lang.multi");
            } else {
                langCache.add(location.getNotes().get(i).getLanguage().getId());
            }
            if (location.getLocationId() == 0 && location.getNotes().get(i).getId() != 0) {
                // If Location's id is 0, all the Notes' ids must be 0 too
                errors.rejectValue("notes[" + i + "].id", "error.location.note.id");
            } else if (location.getLocationId() > 0 && location.getNotes().get(i).getId() > 0 && !ids.contains(location.getNotes().get(i).getId())) {
                // If Note id is not 0 and it didn't exist before, the id is not valid
                errors.rejectValue("notes[" + i + "].id", "error.location.note.id");
            }
        }

        // Image and Location must have the same Owner
        if (location.getImage() != null && location.getOwner().getId() != location.getImage().getOwner().getId()) {
            errors.rejectValue("image", "error.location.image.invalid");
        }

        // Map and Location must have the same Owner
        if (location.getMap() != null && location.getOwner().getId() != location.getMap().getOwner().getId()) {
            errors.rejectValue("image", "error.location.map.invalid");
        }

        if (this.validateAreas) {
            // Get existing Areas if location id is not zero and Location has current areas
            Set<Integer> areas = null;
            if (location.getLocationId() > 0 && location.getAreas() != null && !location.getAreas().isEmpty()) {
                areas = this.locationsService.getAreaIdsSet(location);
            }

            // Validate Areas
            if (location.getAreas() != null) {
                for (int i = 0; i < location.getAreas().size(); i++) {
                    if (location.getLocationId() == 0 && location.getAreas().get(i).getAreaId() != 0) {
                        // If Location's id is 0, all the Areas' ids must be 0 too
                        errors.rejectValue("areas[" + i + "].areaId", "error.location.area.id");
                    } else if (location.getLocationId() > 0 && location.getAreas().get(i).getAreaId() > 0 && !areas.contains(location.getAreas().get(i).getAreaId())) {
                        // If Area id is not 0 and it didn't exist before, the id is not valid
                        errors.rejectValue("areas[" + i + "].areaId", "error.location.area.id");
                    }
                }
            }

            if (location.getMap() == null) {
                if (location.getAreas() != null && !location.getAreas().isEmpty()) {
                    // Location can not have areas if it doesn't have a map
                    errors.rejectValue("areas", "error.location.area.map.null");
                }
            } else {
                if (location.getMap().isGoogleMap() && location.getAreas() != null && !location.getAreas().isEmpty()) {
                    // Location can not have areas if it has a Google Map
                    errors.rejectValue("areas", "error.location.area.map.google");
                }
            }
        }
    }
}
