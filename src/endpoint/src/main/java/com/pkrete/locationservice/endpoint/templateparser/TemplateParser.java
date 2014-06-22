/**
 * This file is part of Location Service :: Endpoint.
 * Copyright (C) 2014 Petteri Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.templateparser;

import com.pkrete.locationservice.endpoint.loader.Loader;
import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.model.location.Shelf;

/**
 * An interface that defines the methods for parsing the template files that 
 * define the layout of the UI. All the classes implementing this interface 
 * must implement all the methods defined in this interface. Template files
 * can contain markup codes that must be replaced with the corresponding
 * information. Classes implementing this interface can freely define
 * the markup they support.
 * 
 * @author Petteri Kivimäki
 */
public interface TemplateParser {

    /**
     * Parses the given string and replaces all the markup codes with 
     * corresponding information.
     * @param line string to be parsed
     * @param lang language of the UI
     * @param shelf Shelf object to which the string is related
     * @param callno call number of the location
     * @param loader Loader object responsible of loading the template file
     * @return parsed string
     */
    public String parse(String line, String lang, Shelf shelf, String callno, Loader loader);

    /**
     * Parses the given string and replaces all the markup codes with 
     * corresponding information.
     * @param line string to be parsed
     * @param lang language of the UI
     * @param collection LibraryCollection object to which the string is related
     * @param callno call number of the location
     * @param loader Loader object responsible of loading the template file
     * @return parsed string
     */
    public String parse(String line, String lang, LibraryCollection collection, String callno, Loader loader);

    /**
     * Parses the given string and replaces all the markup codes with 
     * corresponding information.
     * @param line string to be parsed
     * @param lang language of the UI
     * @param library Library object to which the string is related
     * @param callno call number of the location
     * @param loader Loader object responsible of loading the template file
     * @return parsed string
     */
    public String parse(String line, String lang, Library library, String callno, Loader loader);

    /**
     * Parses the given string and replaces all the markup codes with 
     * corresponding information.
     * @param line string to be parsed
     * @param lang language of the UI
     * @param status status of the item
     * @param callno call number of the location
     * @param loader Loader object responsible of loading the template file
     * @param owner owner code of the library
     * @return parsed string
     */
    public String parse(String line, String lang, String status, String callno, Loader loader, String owner);
}
