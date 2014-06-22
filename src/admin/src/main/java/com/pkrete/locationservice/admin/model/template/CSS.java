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
package com.pkrete.locationservice.admin.model.template;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pkrete.locationservice.admin.deserializers.CSSJSONDeserializer;
import com.pkrete.locationservice.admin.model.owner.Owner;

/**
 * This class represents CSS file that defines the layout of the template
 * files.
 * 
 * @author Petteri Kivimäki
 */
@JsonDeserialize(using = CSSJSONDeserializer.class)
public class CSS extends BaseFile {

    /**
     * Creates and initializes a new CSS object.
     */
    public CSS() {
        super();
    }

    /**
     * Creates and initializes a new CSS object with the given id, filename,
     * language and owner.
     * @param id id of the CSS
     * @param filename name of the CSS
     * @param language language of the CSS
     * @param owner owner of the CSS
     */
    public CSS(int id, String filename, Owner owner) {
        super(id, filename, owner);
    }

    /**
     * Creates and initializes a new CSS object with the given id, filename,
     * language, contents and owner.
     * @param id id of the CSS
     * @param filename name of the CSS
     * @param language language of the CSS
     * @param contents contents of the CSS
     * @param owner owner of the CSS
     */
    public CSS(int id, String filename, String contents, Owner owner) {
        super(id, filename, contents, owner);
    }

    /**
     * Creates and initializes a new CSS object with the given id, filename,
     * old filename, language, contents and owner.
     * @param id id of the CSS
     * @param filename name of the CSS
     * @param filenameOld old name of the CSS
     * @param language language of the CSS
     * @param contents contents of the CSS
     * @param owner owner of the CSS
     */
    public CSS(int id, String filename, String filenameOld, String contents, Owner owner) {
        super(id, filename, filenameOld, contents, owner);
    }
}
