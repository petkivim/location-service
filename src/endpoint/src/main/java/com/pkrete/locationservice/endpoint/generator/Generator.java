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
package com.pkrete.locationservice.endpoint.generator;

import com.pkrete.locationservice.endpoint.model.language.Language;
import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import java.util.List;

/**
 * An interface that defines the methods for generating output that is 
 * returned to the user. All the classes implementing this interface must 
 * implement all the methods defined in this interface.
 *
 * @author Petteri Kivimäki
 */
public interface Generator {

    /**
     * Generates the output that is returned to the user.
     * @param library the Library object thats information is shown to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return output that is returned to the user
     */
    public String generateOutput(Library library, String lang, String callno);

    /**
     * Generates the output that is returned to the user.
     * @param collection the LibraryCollection object thats information is shown to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return output that is returned to the user
     */
    public String generateOutput(LibraryCollection collection, String lang, String callno);

    /**
     * Generates the output that is returned to the user.
     * @param shelf the Shelf object thats information is shown to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return output that is returned to the user
     */
    public String generateOutput(Shelf shelf, String lang, String callno);

    /**
     * Generates output that is returned to the user when the given 
     * location doesn't exist in the database.
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @param owner owner code of the library
     * @return output that is returned to the user
     */
    public String generateOutputNotFound(String lang, String callno, String owner);

    /**
     * Generates the output that is returned to the user when the located 
     * item is not available.
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @param owner owner code of the library
     * @return output that is returned to the user
     */
    public String generateOutputNotAvailable(String lang, String callno, String owner);

    /**
     * Generates output containing all the given locations.
     * @param locations locations to be presented in XML
     * @param languages list of available languages in the system
     * @return output containing all the given locations
     */
    public String generateBatchOutput(List locations, List<Language> languages, boolean children);
    
    /**
     * Generates output containing the given error code and error message.
     * @param errorCode error code
     * @param errorMsg error message
     * @return String containing the given error code and error message
     */
    public String generateError(String errorCode, String errorMsg);
}
