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
package com.pkrete.locationservice.endpoint.generator.html;

import com.pkrete.locationservice.endpoint.generator.Generator;
import com.pkrete.locationservice.endpoint.loader.Loader;
import com.pkrete.locationservice.endpoint.model.language.Language;
import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * The <code>HTMLGenerator</code> class implements the 
 * {@link Generator Generator} interface.
 *
 * This class implements the methods defined in the Generator interface.  This 
 * class offers the functionality for generating the html page that is returned 
 * to the user. The creation of the concrete html page is done via 
 * {@link Loader Loader} interface. The contents of the page to be
 * generated are defined in a template file that must first be read and
 * then parsed.
 *
 * @author Petteri Kivimäki
 */
public class HTMLGenerator implements Generator {

    private final static Logger logger = Logger.getLogger(HTMLGenerator.class.getName());
    /**
     * Loads the tempalate files that used for building the html page that is 
     * returned to the user
     */
    private Loader loader;

    /**
     * Contructs and initializes a HTMLGenerator object.
     */
    public HTMLGenerator() {
    }

    /**
     * Contructs and initializes a HTMLGenerator object.
     * @param loader Loader object that is responsible of loading the template
     * files
     */
    public HTMLGenerator(Loader loader) {
        this.loader = loader;
    }

    /**
     * Sets the loader variable.
     * @param loader new value
     */
    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    /**
     * Generates the html page that is returned to the user.
     * @param library the Library object thats information is shown to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return the html code of the page that is returned to the user
     */
    public String generateOutput(Library library, String lang, String callno) {
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Generate output for library. Id: ").append(library.getLocationId()));
        }
        return loader.loadTemplate(lang, library, callno);
    }

    /**
     * Generates the html page that is returned to the user.
     * @param collection the LibraryCollection object thats information is 
     * shown to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return the html code of the page that is returned to the user
     */
    public String generateOutput(LibraryCollection collection, String lang, String callno) {
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Generate output for collection. Id: ").append(collection.getLocationId()));
        }
        return loader.loadTemplate(lang, collection, callno);
    }

    /**
     * Generates the html page that is returned to the user.
     * @param shelf the Shelf object thats information is shown to the user
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return the html code of the page that is returned to the user
     */
    public String generateOutput(Shelf shelf, String lang, String callno) {
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Generate output for shelf. Id: ").append(shelf.getLocationId()));
        }
        return loader.loadTemplate(lang, shelf, callno);
    }

    /**
     * Generates the html page that is returned to the user when the given 
     * location doesn't exist in the database.
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return the html code of the page that is returned to the user
     */
    public String generateOutputNotFound(String lang, String callno, String owner) {
        if (logger.isDebugEnabled()) {
            logger.debug("Generate output not found.");
        }
        return loader.loadTemplateNotFound(lang, callno, owner);
    }

    /**
     * Generates the html page that is returned to the user when the located 
     * item is not available.
     * @param lang the language of the UI
     * @param callno the call number that was received from the UI
     * @return the html code of the page that is returned to the user
     */
    public String generateOutputNotAvailable(String lang, String callno, String owner) {
        if (logger.isDebugEnabled()) {
            logger.debug("Generate output not available.");
        }
        return loader.loadTemplateNotAvailable(lang, callno, owner);
    }

    /**
     * Method not implemented. Returns always an empty string.
     * @param locations locations to be presented in XML
     * @param languages list of available languages in the system
     * @return an empty string
     */
    public String generateBatchOutput(List locations, List<Language> languages, boolean children) {
        return "";
    }

    /**
     * Generates output containing the given error code and error message.
     * @param errorCode error code
     * @param errorMsg error message
     * @return String containing the given error code and error message
     */
    public String generateError(String errorCode, String errorMsg) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n<html>\n<head>\n");
        builder.append("<title>Service Error</title>\n</head>\n<body>\n");
        builder.append("<h1>Service Error</h1>\n");
        builder.append("<p>Error code: ").append(errorCode).append("</p>\n");
        builder.append("<p>Error message: ").append(errorMsg).append("</p>\n");
        builder.append("</body>\n</html>");
        return builder.toString();
    }
}
