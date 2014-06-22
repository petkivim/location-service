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

import com.pkrete.locationservice.admin.model.template.JS;
import com.pkrete.locationservice.admin.io.JSService;
import com.pkrete.locationservice.admin.util.JSUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * This class validates JS objects. This class defines the
 * obligatory variables and returns an error message associated with the variable
 * when the value of the variable is not valid.
 *  
 * @author Petteri Kivimäki
 */
public class JSValidator implements Validator {

    private JSService jsService;

    public void setJsService(JSService jsService) {
        this.jsService = jsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JS.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        JS js = (JS) target;

        // Validate template's name
        if (!JSUtil.isJsNameValid(js.getFilename())) {
            errors.rejectValue("filename", "error.js.name.notvalid");
        }

        // Contents cannot be null
        if (js.getContents() == null) {
            errors.rejectValue("contents", "error.js.contents");
        }

        // Owner cannot be null
        if (js.getOwner() == null) {
            errors.rejectValue("owner", "error.js.owner");
        }

        // Does the file exist?
        boolean exists = jsService.exists(js.getFilename(), js.getOwner());

        // If id is 0, css file is new, otherwise it already exists
        if (js.getId() == 0) {
            if (exists) {
                errors.rejectValue("filename", "error.js.exist");
            }
        } else {
            // Js file already exists, so the file must exist
            if (js.getFilenameOld().isEmpty() && !exists) {
                errors.rejectValue("filename", "error.js.not.exist");
            } else if (!js.getFilenameOld().isEmpty()) {
                // File is being renamed, so the new name cannot exist
                // and the old name must exist
                if (!jsService.exists(js.getFilenameOld(), js.getOwner())) {
                    errors.rejectValue("filenameOld", "error.js.not.exist");
                } else if (exists) {
                    errors.rejectValue("filename", "error.js.exist");
                }
            }
        }
    }
}

