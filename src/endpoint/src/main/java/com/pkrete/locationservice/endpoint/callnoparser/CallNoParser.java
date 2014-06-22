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
package com.pkrete.locationservice.endpoint.callnoparser;

import com.pkrete.locationservice.endpoint.generator.Generator;
import com.pkrete.locationservice.endpoint.modifier.CallnoModification;
import com.pkrete.locationservice.endpoint.service.Service;
import com.pkrete.locationservice.endpoint.model.location.SimpleLocation;
import com.pkrete.locationservice.endpoint.model.search.LocationType;
import com.pkrete.locationservice.endpoint.modifier.Modifier;
import com.pkrete.locationservice.endpoint.util.LocationHelper;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * This abstract class defines the functionality for parsing the location
 * information from the given call number string. The class decides if the 
 * given call number string matches with one of the locations saved in the 
 * database. 
 * 
 * All the subclasses must implement the abstract <i>parse</i> method.
 *
 * @author Petteri Kivimäki
 */
public abstract class CallNoParser {

    private final static Logger logger = Logger.getLogger(CallNoParser.class.getName());
    /**
     * Service object handles the database.
     */
    protected Service dbService;
    /**
     * Generator object is responsible of generating the html code that is returned to the user.
     */
    protected Generator generator;
    /**
     * Modifier is responsible of processing not found redirects.
     */
    protected Modifier modifier;
    /**
     * Parser implements a LocatingStrategy.
     */
    private LocatingStrategy locatingStrategy;

    /**
     * Contructs and initializes a CallNoParser object.
     * @param locatingStrategy locating strategy that this object implements
     */
    protected CallNoParser(LocatingStrategy locatingStrategy) {
        this.locatingStrategy = locatingStrategy;
    }

    /**
     * Contructs and initializes a CallNoParser object with the given generator 
     * object and limit
     * @param generator the generator object that generates the html page 
     * returned to the user
     */
    protected CallNoParser(Generator generator) {
        this.generator = generator;
    }

    /**
     * Searches the given call number from the database.
     * @param callno the call number to be searched
     * @param lang the language of the UI
     * @param owner owner of the location
     * @return the html page returned to the user
     */
    public abstract String parse(String callno, String lang, String owner);

    /**
     * Changes the database service object.
     * @param dbService new object
     */
    public void setDbService(Service dbService) {
        this.dbService = dbService;
    }

    /**
     * Sets the modifier variable.
     * @param modifier new value
     */
    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    /**
     * Sets the generator variable.
     * @param generator new value
     */
    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    /**
     * This method is called when no location matching to the given call
     * number hasn't been found. Before returning not found template
     * this method loads from the db all the CallnoModification objects
     * related to the given owner and checks, if the given call number
     * matches with of them. If a match is found, the call number will
     * be modified according to the CallnoModification object.
     * @param callno call number to be checked
     * @param lang language of the UI
     * @param owner owner of the location
     * @return html page returned to the user
     */
    public String notFound(String callno, String lang, String owner) {
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Unable to find a location matching the given call number: \"").append(callno).append("\""));
        }
        List<CallnoModification> list = dbService.getNotFoundRedirects(owner);
        /* Original call number */
        String orgCallno = callno;
        for (CallnoModification mod : list) {
            if (modifier.canBeModified(callno, mod)) {
                callno = modifier.modify(callno, mod);
                if (logger.isDebugEnabled()) {
                    logger.debug(new StringBuilder("Not found redirect match!").append(" Redirect: \"").append(orgCallno).append("\" -> \"").append(callno).append("\". Restart standard parsing process."));
                }
                return parse(callno, lang, owner);
            }
        }
        return generator.generateOutputNotFound(lang, callno, owner);
    }

    /**
     * Searches the given call number from the database. This method is used
     * when it's already known that the given call number belongs to a collection
     * or a library that has the match beginning check box checked. The given
     * Location object is the parent of the location that the given call number
     * is presenting, so the search can be targeted to the sub locations of
     * the given location. If no matching sub location can be found, the 
     * location given as a paratemer is returned.
     * 
     * Normally the given call number and the Location object's call
     * number must match on word level, but when the match beginning check
     * box is checked it's enough that the beginning of the given call number
     * and Location object's call number are matching.
     * @param callno the call number to be searched
     * @param lang the language of the UI
     * @param owner owner of the location
     * @param the location under which the location being searched belongs to
     * @return the html page returned to the user
     */
    public String parse(String callno, String lang, String owner, SimpleLocation location) {
        List list = null;

        if (location.getLocationType() == LocationType.COLLECTION) {
            // Get list of shelves related to this collection
            list = dbService.getShelvesByCollectionId(location.getLocationId(), owner);
            // Go through the shelves
            for (SimpleLocation loc : (List<SimpleLocation>)list) {
                if (LocationHelper.matchSub(loc.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getShelf(loc.getLocationId()), lang, callno);
                }
            }
            // No matching sub location was found, return the location given as parameter
            return generator.generateOutput(dbService.getCollection(location.getLocationId()), lang, callno);
        } else if (location.getLocationType() == LocationType.LIBRARY) {
            // Get list of collections related to this library
            list = dbService.getCollectionsByLibraryId(location.getLocationId(), owner);
            // Go through the collections
            for (SimpleLocation loc : (List<SimpleLocation>)list) {
                if (LocationHelper.matchSub(loc.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getCollection(loc.getLocationId()), lang, callno);
                }
            }
            // No matching sub location was found, return the location given as parameter
            return generator.generateOutput(dbService.getLibrary(location.getLocationId()), lang, callno);
        }
        return this.parse(callno, lang, owner);
    }

    /**
     * Returns the locating strategy that this CallNoParser implements.
     * @return locating strategy that this CallNoParser implements
     */
    public LocatingStrategy getLocatingStrategy() {
        return locatingStrategy;
    }
}
