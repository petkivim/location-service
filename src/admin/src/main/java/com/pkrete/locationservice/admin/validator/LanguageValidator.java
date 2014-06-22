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

import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.service.LanguagesService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * The {@link LanguageValidator LanguageValidator} class validates the values
 * inserted via addLanguage and editLanguage forms. This class defines the
 * obligatory fields and returns an error message associated with the field
 * when the value of the field is not valid.
 *
 * @author Petteri Kivimäki
 */
public class LanguageValidator implements Validator {

    private LanguagesService languagesService;

    public void setLanguagesService(LanguagesService languagesService) {
        this.languagesService = languagesService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Language.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Language lang = (Language) target;
        ValidationUtils.rejectIfEmpty(errors, "code", "error.language.code");
        ValidationUtils.rejectIfEmpty(errors, "name", "error.language.name");

        if(lang.getCode().length() > 20) {
            errors.rejectValue("code", "error.language.code.length");
        }

        if(lang.getName().length() > 50) {
            errors.rejectValue("name", "error.language.name.length");
        }

        if (!lang.getCode().isEmpty() && !lang.getCode().equals(lang.getCodePrevious())) {
            if(languagesService.exists(lang)) {
                errors.rejectValue("code", "error.language.exists");
            }
        }
    }
}
