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

import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * The {@link SubjectMatterValidator SubjectMatterValidator} class validates the values
 * inserted via addSubjectMatter and editSubjectMatter forms. This class defines the
 * obligatory fields and returns an error message associated with the field
 * when the value of the field is not valid.
 *
 * @author Petteri Kivimäki
 */
public class SubjectMatterValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SubjectMatter.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "indexTerm", "error.subjectmatter.index_term");

        SubjectMatter subject = (SubjectMatter) target;

        if (subject.getIndexTerm().length() > 100) {
            errors.rejectValue("indexTerm", "error.subjectmatter.index_term.length");
        }

        if (subject.getLanguage() == null) {
            errors.rejectValue("language", "error.subjectmatter.language");
        } else {
            if (subject.getOwner().getId() != subject.getLanguage().getOwner().getId()) {
                errors.rejectValue("language", "error.subjectmatter.language.owner");
            }
        }
    }
}
