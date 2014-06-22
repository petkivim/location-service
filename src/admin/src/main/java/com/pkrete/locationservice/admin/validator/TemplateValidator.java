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

import com.pkrete.locationservice.admin.model.template.Template;
import com.pkrete.locationservice.admin.io.TemplatesService;
import com.pkrete.locationservice.admin.util.TemplatesUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * This class validates Template objects. This class defines the
 * obligatory variables and returns an error message associated with the variable
 * when the value of the variable is not valid.
 *  
 * @author Petteri Kivimäki
 */
public class TemplateValidator implements Validator {

    private TemplatesService templatesService;

    public void setTemplatesService(TemplatesService templatesService) {
        this.templatesService = templatesService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Template.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Template template = (Template) target;
        boolean isLangValid = true;

        // Validate template's name
        if (!TemplatesUtil.isTemplateNameValid(template.getFilename())) {
            errors.rejectValue("filename", "error.template.name.notvalid");
        }

        // Contents cannot be null
        if (template.getContents() == null) {
            errors.rejectValue("contents", "error.template.contents");
        }

        // Owner cannot be null
        if (template.getOwner() == null) {
            errors.rejectValue("owner", "error.template.owner");
        }

        // Language cannot be null
        if (template.getLanguage() == null) {
            errors.rejectValue("language", "error.template.language");
            isLangValid = false;
        } else {
            // Check that template and language belong to the same owner
            if (template.getOwner().getId() != template.getLanguage().getOwner().getId()) {
                errors.rejectValue("language", "error.template.language.owner");
                isLangValid = false;
            }
        }
        boolean exists = false;
        // Get language code
        String langCode = template.getLanguage() == null ? "" : template.getLanguage().getCode();

        if (isLangValid) {
            // Does the file exist?
            exists = templatesService.exists(template.getFilename(), langCode, template.getOwner());
        }

        // If id is 0, template is new, otherwise it already exists
        if (template.getId() == 0) {
            if (exists) {
                errors.rejectValue("filename", "error.template.exist");
            }
        } else if (isLangValid) {
            // Template already exists, so the file must exist
            if (template.getFilenameOld().isEmpty() && !exists) {
                errors.rejectValue("filename", "error.template.not.exist");
            } else if (!template.getFilenameOld().isEmpty()) {
                // File is being renamed, so the current name cannot exist
                // and the old name must exist
                if (!templatesService.exists(template.getFilenameOld(), langCode, template.getOwner())) {
                    errors.rejectValue("filenameOld", "error.template.not.exist");
                } else if (exists) {
                    errors.rejectValue("filename", "error.template.exist");
                }
            }
        }
    }
}
