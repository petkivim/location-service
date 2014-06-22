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
package com.pkrete.locationservice.endpoint.loader;

import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.model.location.Shelf;

/**
 * An interface that defines the methods for loading the template files that 
 * are used for generating the html page that is returned to user. All the 
 * classes implementing this interface must implement all the methods defined 
 * in this interface.
 *
 * @author Petteri Kivimäki
 */
public interface Loader {

    /**
     * Loads the template file related to a Shelf object.
     * @param lang the language of the UI
     * @param shelf the Shelf object thats information is shown to the user
     * @param callno the call number received from the UI
     * @return the context of the template file
     */
    public String loadTemplate(String lang, Shelf shelf, String callno);

    /**
     * Loads the template file related to a LibraryCollection object.
     * @param lang the language of the UI
     * @param collection the LibraryCollection object thats information is shown to the user
     * @param callno the call number received from the UI
     * @return the context of the template file
     */
    public String loadTemplate(String lang, LibraryCollection collection, String callno);

    /**
     * Loads the template file related to a Library object.
     * @param lang the language of the UI
     * @param library the Library object thats information is shown to the user
     * @param callno the call number that was received from the UI
     * @return the context of the template file
     */
    public String loadTemplate(String lang, Library library, String callno);

    /**
     * Loads the template that is shown to the user when the
     * location doesn't exist in the database.
     * @param lang the language of the UI
     * @param callno the call number received from the UI
     * @param owner owner code of the library
     * @return the context of the template file
     */
    public String loadTemplateNotFound(String lang, String callno, String owner);

    /**
     * Loads the template that is shown to the user when the
     * located item is not available.
     * @param lang the language of the UI
     * @param callno the call number received from the UI
     * @param owner owner code of the library
     * @return the context of the template file
     */
    public String loadTemplateNotAvailable(String lang, String callno, String owner);

    /**
     * Loads the template of the given name, language and status. This method
     * is used when the given call number doesn't match to any Location
     * object in the database or the given item is not available.
     * @param lang language of the template
     * @param template name of the template
     * @param status status of the item
     * @param callno call number of the item
     * @param owner owner code of the library
     * @return contents of the template
     */
    public String loadTemplate(String lang, String template, String status, String callno, String owner);

    /**
     * Loads the template of the given name and language, and populates it with 
     * the data related to the given shelf.
     * @param lang language of the template
     * @param shelf shelf object
     * @param callno call number of the shelf
     * @param template name of the template
     * @return contents of the template
     */
    public String loadTemplate(String lang, Shelf shelf, String callno, String template);

    /**
     * Loads the template of the given name and language, and populates it with 
     * the data related to the given collection.
     * @param lang language of the template
     * @param collection collection object
     * @param callno call number of the collection
     * @param template name of the template
     * @return contents of the template
     */
    public String loadTemplate(String lang, LibraryCollection collection, String callno, String template);
    
    /**
     * Loads the template of the given name and language, and populates it with 
     * the data related to the given library.
     * @param lang language of the template
     * @param library library object
     * @param callno call number of the library
     * @param template name of the template
     * @return contents of the template
     */
    public String loadTemplate(String lang, Library library, String callno, String template);

    /**
     * Returns the absolute path of the template (type other) with the given 
     * name. If the  given template doesn't exist, null is returned instead.
     * @param template name of the template
     * @param lang language of the UI
     * @param owner owner code of the library
     * @return absolute path of the file or null if the file does not exist
     */
    public String getTemplateOtherPath(String template, String lang, String owner);
}
