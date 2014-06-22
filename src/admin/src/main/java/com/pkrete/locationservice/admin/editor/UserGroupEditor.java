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

import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import java.beans.PropertyEditorSupport;

/**
 * The {@link UserGroupEditor UserGroupEditor} converts UserGroup
 * objects expressed as strings to UserGroup objects. This
 * class is used to convert the values of html drop down menus to corresponding
 * objects.
 *
 * @author Petteri Kivimäki
 */
public class UserGroupEditor extends PropertyEditorSupport {

    private ConverterService converterService;

    /**
     * Changes the value of converterService instance variable
     * @param usersService new value to be set
     */
    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    @Override
    /**
     * Converts a string containing an id number of an UserGroup object to
     * the corresponding object in the database.
     * @param text string that contains the id number of the object to be converted
     */
    public void setAsText(String text) {
        if (text != null && text.length() > 0) {
            if (text.equals("-1")) {
                setValue(null);
                return;
            }
            setValue((UserGroup) converterService.convert(text, UserGroup.class, null));
            return;
        }
        setValue(null);
    }

    @Override
    /**
     * Returns a string presentation of an UserGroup object.
     * @return id number of a UserGroup object as a string
     */
    public String getAsText() {
        UserGroup group = (UserGroup) getValue();
        if (group == null) {
            return "-1";
        }
        return group.toString();
    }
}
