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

import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * The {@link CollectionValidator CollectionValidator} class validates 
 * LibraryCollection objects. This class defines the
 * obligatory fields and returns an error message associated with the field
 * when the value of the field is not valid.
 *
 * @author Petteri Kivimäki
 */
public class CollectionValidator extends LocationSubjectMattersValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return LibraryCollection.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);

        LibraryCollection collection = (LibraryCollection) target;

        if (collection.getCollectionCode().length() > 50) {
            errors.rejectValue("collectionCode", "error.location.colcode.length");
        }
        
        if(collection.getLibrary() == null) {
            // Library can not be null
            errors.rejectValue("library", "error.location.library.null");
        } else if(collection.getLibrary().getOwner().getId() != collection.getOwner().getId()) {
            // Collection and Library must have the same Owner
            errors.rejectValue("library", "error.location.library.invalid");
        }
    }
}
