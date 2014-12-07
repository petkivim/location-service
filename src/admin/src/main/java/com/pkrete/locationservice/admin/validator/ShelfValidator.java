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

import com.pkrete.locationservice.admin.model.location.Shelf;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * The {@link ShelfValidator ShelfValidator} class validates Shelf objects. This
 * class defines the obligatory fields and returns an error message associated
 * with the field when the value of the field is not valid.
 *
 * @author Petteri Kivimäki
 */
public class ShelfValidator extends LocationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Shelf.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);

        ValidationUtils.rejectIfEmpty(errors, "locationCode", "error.shelf.location_code");

        Shelf shelf = (Shelf) target;

        if (shelf.getCollection() == null) {
            // Collection can not be null
            errors.rejectValue("collection", "error.location.collection.null");
        } else if (shelf.getCollection().getOwner().getId() != shelf.getOwner().getId()) {
            // Collection and Library must have the same Owner
            errors.rejectValue("collection", "error.location.collection.invalid");
        }
    }
}
