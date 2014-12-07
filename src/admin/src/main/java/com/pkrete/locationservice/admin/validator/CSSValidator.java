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

import com.pkrete.locationservice.admin.model.template.CSS;
import com.pkrete.locationservice.admin.io.CSSService;
import com.pkrete.locationservice.admin.util.CSSUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * This class validates CSS objects. This class defines the obligatory variables
 * and returns an error message associated with the variable when the value of
 * the variable is not valid.
 *
 * @author Petteri Kivimäki
 */
public class CSSValidator implements Validator {

    private CSSService cssService;

    public void setCssService(CSSService cssService) {
        this.cssService = cssService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CSS.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CSS css = (CSS) target;

        // Validate template's name
        if (!CSSUtil.isCssNameValid(css.getFilename())) {
            errors.rejectValue("filename", "error.css.name.notvalid");
        }

        // Contents cannot be null
        if (css.getContents() == null) {
            errors.rejectValue("contents", "error.css.contents");
        }

        // Owner cannot be null
        if (css.getOwner() == null) {
            errors.rejectValue("owner", "error.css.owner");
        }

        // Does the file exist?
        boolean exists = cssService.exists(css.getFilename(), css.getOwner());

        // If id is 0, css file is new, otherwise it already exists
        if (css.getId() == 0) {
            if (exists) {
                errors.rejectValue("filename", "error.css.exist");
            }
        } else {
            // Css file already exists, so the file must exist
            if (css.getFilenameOld().isEmpty() && !exists) {
                errors.rejectValue("filename", "error.css.not.exist");
            } else if (!css.getFilenameOld().isEmpty()) {
                // File is being renamed, so the new name cannot exist
                // and the old name must exist
                if (!cssService.exists(css.getFilenameOld(), css.getOwner())) {
                    errors.rejectValue("filenameOld", "error.css.not.exist");
                } else if (exists) {
                    errors.rejectValue("filename", "error.css.exist");
                }
            }
        }
    }
}
