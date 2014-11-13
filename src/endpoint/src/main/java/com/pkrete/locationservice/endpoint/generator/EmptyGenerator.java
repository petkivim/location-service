/**
 * This file is part of Location Service :: Endpoint. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.generator;

import com.pkrete.locationservice.endpoint.model.language.Language;
import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import java.util.List;
import java.util.Map;

/**
 * The <code>EmptyGenerator</code> class implements the
 * {@link Generator Generator} interface.
 *
 * Implements all the methods of the {@link Generator Generator} interface as
 * empty. This class can be used as a base class for classes implementing only
 * one or some methods of the interface.
 *
 * @author Petteri Kivimäki
 */
public abstract class EmptyGenerator implements Generator {

    protected Map<String, String> filters;

    /**
     * Sets the filters variable.
     *
     * @param filters new value
     */
    public void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }

    /**
     * Method not implemented. Returns always an empty string.
     *
     * @param library the Library object thats information is shown to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return an empty string
     */
    @Override
    public String generateOutput(Library library, String lang, String callno) {
        return "";
    }

    /**
     * Method not implemented. Returns always an empty string.
     *
     * @param collection the LibraryCollection object thats information is shown
     * to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return an empty string
     */
    @Override
    public String generateOutput(LibraryCollection collection, String lang, String callno) {
        return "";
    }

    /**
     * Method not implemented. Returns always an empty string.
     *
     * @param shelf the Shelf object thats information is shown to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return an empty string
     */
    @Override
    public String generateOutput(Shelf shelf, String lang, String callno) {
        return "";
    }

    /**
     * Generates the HTML page that is returned to the user when the given
     * location doesn't exist in the database.
     *
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @param owner owner code of the library
     * @return an empty string
     */
    @Override
    public String generateOutputNotFound(String lang, String callno, String owner) {
        return "";
    }

    /**
     * Generates the HTML page that is returned to the user when the located
     * item is not available.
     *
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @param owner owner code of the library
     * @return an empty string
     */
    @Override
    public String generateOutputNotAvailable(String lang, String callno, String owner) {
        return "";
    }

    /**
     * Method not implemented. Returns always an empty string.
     *
     * @param locations locations to be presented in XML
     * @param languages list of available languages in the system
     * @return an empty string
     */
    @Override
    public String generateBatchOutput(List locations, List<Language> languages, boolean children) {
        return "";
    }

    /**
     * Method not implemented. Returns always an empty string.
     *
     * @param errorCode error code
     * @param errorMsg error message
     * @return an empty string
     */
    @Override
    public String generateError(String errorCode, String errorMsg) {
        return "";
    }

    /**
     * Strips all the HTML tags from the given string.
     *
     * @param content string to be modified
     * @return given string without HTML tags
     */
    public String stripHtml(String content) {
        return content.replaceAll("\\<.*?\\>", "");
    }
}
