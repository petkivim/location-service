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
package com.pkrete.locationservice.admin.editor;

import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.service.SubjectMattersService;
import java.beans.PropertyEditorSupport;

/**
 * The {@link SubjectMatterEditor SubjectMatterEditor} converts SubjectMatter
 * objects expressed as strings to SubjectMatter objects. The string presentation
 * of a SubjectMatter object is the object's id number in the database. This
 * class is used to convert the values of html drop down menus to corresponding
 * objects.
 *
 * @author Petteri Kivimäki
 */
public class SubjectMatterEditor extends PropertyEditorSupport {

    private SubjectMattersService subjectMattersService;
    private ConverterService converterService;

    /**
     * Changes the value of converterService instance variable
     * @param converterService new value to be set
     */
    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    /**
     * Changes the value of subjectMattersService instance variable
     * @param subjectMattersService new value to be set
     */
    public void setSubjectMattersService(SubjectMattersService subjectMattersService) {
        this.subjectMattersService = subjectMattersService;
    }

    @Override
    /**
     * Converts a string containing an id number of a SubjectMatter object to
     * the corresponding object in the database.
     * @param text string that contains the id number of the object to be converted
     */
    public void setAsText(String text) {
        int id = 0;
        if (text != null && text.length() > 0) {
            id = this.converterService.strToInt(text);
            if (id == -1) {
                setValue(null);
                return;
            }
            setValue(subjectMattersService.getSubjectMatter(id));
            return;
        }
        setValue(null);
    }

    @Override
    /**
     * Returns a string presentation of a SubjectMatter object.
     * @return id number of a SubjectMatter object as a string
     */
    public String getAsText() {
        SubjectMatter subjectMatter = (SubjectMatter) getValue();
        if (subjectMatter == null) {
            return "-1";
        }
        return Integer.toString(subjectMatter.getId());

    }
}
