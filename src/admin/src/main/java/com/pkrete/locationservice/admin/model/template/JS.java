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
import com.pkrete.locationservice.admin.deserializers.JSJSONDeserializer;
import com.pkrete.locationservice.admin.model.owner.Owner;

/**
 * This class represents JavaScript file that users can create for their
 * local scripts.
 * 
 * @author Petteri Kivimäki
 */
@JsonDeserialize(using = JSJSONDeserializer.class)
public class JS extends BaseFile {

    /**
     * Creates and initializes a new JS object.
     */
    public JS() {
        super();
    }

    /**
     * Creates and initializes a new JS object with the given id, filename,
     * language and owner.
     * @param id id of the JS
     * @param filename name of the JS
     * @param language language of the JS
     * @param owner owner of the JS
     */
    public JS(int id, String filename, Owner owner) {
        super(id, filename, owner);
    }

    /**
     * Creates and initializes a new JS object with the given id, filename,
     * language, contents and owner.
     * @param id id of the JS
     * @param filename name of the JS
     * @param language language of the JS
     * @param contents contents of the JS
     * @param owner owner of the JS
     */
    public JS(int id, String filename, String contents, Owner owner) {
        super(id, filename, contents, owner);
    }

    /**
     * Creates and initializes a new JS object with the given id, filename,
     * old filename, language, contents and owner.
     * @param id id of the JS
     * @param filename name of the JS
     * @param filenameOld old name of the JS
     * @param language language of the JS
     * @param contents contents of the JS
     * @param owner owner of the JS
     */
    public JS(int id, String filename, String filenameOld, String contents, Owner owner) {
        super(id, filename, filenameOld, contents, owner);
    }
}
