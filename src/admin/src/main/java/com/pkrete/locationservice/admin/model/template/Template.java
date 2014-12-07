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
package com.pkrete.locationservice.admin.model.template;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pkrete.locationservice.admin.deserializers.TemplateJSONDeserializer;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;

/**
 * This class represents template file that defines the layout of the public
 * interface.
 *
 * @author Petteri Kivimäki
 */
@JsonDeserialize(using = TemplateJSONDeserializer.class)
public class Template extends BaseFile {

    private Language language;

    /**
     * Creates and initializes a new Template object.
     */
    public Template() {
        super();
    }

    /**
     * Creates and initializes a new Template object with the given id,
     * filename, language and owner.
     *
     * @param id id of the template
     * @param filename name of the template
     * @param language language of the template
     * @param owner owner of the template
     */
    public Template(int id, String filename, Language language, Owner owner) {
        super(id, filename, owner);
        this.language = language;
    }

    /**
     * Creates and initializes a new Template object with the given id,
     * filename, language, contents and owner.
     *
     * @param id id of the template
     * @param filename name of the template
     * @param language language of the template
     * @param contents contents of the template
     * @param owner owner of the template
     */
    public Template(int id, String filename, Language language, String contents, Owner owner) {
        super(id, filename, contents, owner);
        this.language = language;
    }

    /**
     * Creates and initializes a new Template object with the given id,
     * filename, old filename, language, contents and owner.
     *
     * @param id id of the base file
     * @param filename name of the template
     * @param filenameOld old name of the template
     * @param language language of the template
     * @param contents contents of the template
     * @param owner owner of the template
     */
    public Template(int id, String filename, String filenameOld, Language language, String contents, Owner owner) {
        super(id, filename, filenameOld, contents, owner);
        this.language = language;
    }

    /**
     * Returns the language of the base file
     *
     * @return language of the base file
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Sets the language of the base file.
     *
     * @param language new language
     */
    public void setLanguage(Language language) {
        this.language = language;
    }
}
