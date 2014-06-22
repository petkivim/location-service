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
package com.pkrete.locationservice.admin.validator;

import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.service.SubjectMattersService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.validation.Errors;

/**
 * This is a base class for Location validators that need to validate
 * subject matters.
 * 
 * @author Petteri Kivimäki
 */
public class LocationSubjectMattersValidator extends LocationValidator {
    
    protected SubjectMattersService subjectMattersService;
    
    public void setSubjectMattersService(SubjectMattersService subjectMattersService) {
        this.subjectMattersService = subjectMattersService;
    }
    
    @Override
    public void validate(Object target, Errors errors) {
        // Use super classes' validator
        super.validate(target, errors);
        // Get Location object
        Location location = (Location) target;
        
        // Validate subject matters if there are any
        if (location.getSubjectMatters() != null && !location.getSubjectMatters().isEmpty()) {
            // Get all the subject matter ids related to the Owner
            Set<Integer> subjectIds = this.subjectMattersService.getIds(location.getOwner());
            // Cache for subject matters that have already been used
            Set<Integer> subjectsCache = new HashSet<Integer>();
            
            for (int i = 0; i < location.getSubjectMatters().size(); i++) {
                int temp = location.getSubjectMatters().get(i).getId();
                if (!subjectIds.contains(temp)) {
                    // Location and subject matter much have the same Owner
                    errors.rejectValue("subjectMatters[" + i + "].id", "error.location.subjectmatter.id");
                } else if (subjectsCache.contains(temp)) {
                    // Each subject matter can be used only once with the same Location
                    errors.rejectValue("subjectMatters[" + i + "].id", "error.location.subjectmatter.multi");
                }
                subjectsCache.add(temp);
            }
        }
    }
}
