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
package com.pkrete.locationservice.admin.editor;

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.service.OwnersService;
import java.beans.PropertyEditorSupport;

/**
 * The {@link OwnerEditor OwnerEditor} converts Owner objects expressed as
 * strings to Owner objects. The string presentation of an Owner object is the
 * object's id number in the database. This class is used to convert the values
 * of HTML drop down menus to corresponding objects.
 *
 * @author Petteri Kivimäki
 */
public class OwnerEditor extends PropertyEditorSupport {

    private OwnersService ownersService;
    private ConverterService converterService;

    /**
     * Changes the value of converterService instance variable
     *
     * @param converterService new value to be set
     */
    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    /**
     * Changes the value of ownersService instance variable
     *
     * @param ownersService new value to be set
     */
    public void setOwnersService(OwnersService ownersService) {
        this.ownersService = ownersService;
    }

    @Override
    /**
     * Converts a string containing an id number of an Owner object to the
     * corresponding object in the database.
     *
     * @param text string that contains the id number of the object to be
     * converted
     */
    public void setAsText(String text) {
        int id = 0;
        if (text != null && text.length() > 0) {
            id = this.converterService.strToInt(text);
            if (id == -1) {
                setValue(null);
                return;
            }
            setValue(ownersService.getOwner(id));
            return;
        }
        setValue(null);
    }

    @Override
    /**
     * Returns a string presentation of an Owner object.
     *
     * @return id number of a Owner object as a string
     */
    public String getAsText() {
        Owner owner = (Owner) getValue();
        if (owner == null) {
            return "-1";
        }
        return Integer.toString(owner.getId());

    }
}
