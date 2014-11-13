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
package com.pkrete.locationservice.endpoint.resolver;

import com.pkrete.locationservice.endpoint.generator.Generator;
import com.pkrete.locationservice.endpoint.callnoparser.CallNoParser;
import com.pkrete.locationservice.endpoint.callnoparser.CallNoParserFactory;
import com.pkrete.locationservice.endpoint.callnoparser.LocatingStrategy;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import com.pkrete.locationservice.endpoint.model.location.SimpleLocation;
import com.pkrete.locationservice.endpoint.modifier.Modifier;
import com.pkrete.locationservice.endpoint.service.Service;
import com.pkrete.locationservice.endpoint.util.LocationServiceConstants;
import com.pkrete.locationservice.endpoint.util.ServiceFactory;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class that defines the methods for locating the location object
 * corresponding the given parameters from the database.
 *
 * @author Petteri Kivimäki
 */
public abstract class Resolver {

    private final static Logger logger = LoggerFactory.getLogger(Resolver.class.getName());
    protected Modifier modifier;
    protected CallNoParserFactory callNoParserFactory;
    protected Map<OutputFormat, Generator> generators;
    protected ServiceFactory serviceFactory;
    protected String intervalRegex = LocationServiceConstants.INTERVAL_REGEX;
    protected String language = LocationServiceConstants.LANGUAGE;
    protected String country = LocationServiceConstants.COUNTRY;

    protected abstract String runPreprocessingRedirects(String callno, String owner, Service localService);

    protected abstract SimpleLocation runMatchBeginningCheck(String callno, String owner, Service localService);

    protected abstract SimpleLocation runMatchBeginningByCollectionCodeCheck(String callno, String owner, String collectionCode, Service localService);

    protected abstract Shelf runShelvesByCollectionCodeCheck(String collection, String callno, String owner, Service localService);

    protected abstract LibraryCollection runCollectionsByCollectionCodeCheck(String collection, String callno, String owner, Service localService);

    /**
     * Constructs and initializes a new Resolver object.
     */
    public Resolver() {
    }

    /**
     * Constructs and initializes a new Resolver object
     *
     * @param generators list of Generator objects that are responsible of
     * generating the output shown to the user
     */
    public Resolver(Map<OutputFormat, Generator> generators) {
        this.generators = generators;
    }

    /**
     * Sets the generator variable.
     *
     * @param generator new value
     */
    public void setGenerators(Map<OutputFormat, Generator> generators) {
        this.generators = generators;
    }

    /**
     * Sets the serviceFactory variable.
     *
     * @param serviceFactory new value
     */
    public void setServiceFactory(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    /**
     * Sets the callNoParserFactory variable.
     *
     * @param callNoParserFactory new value
     */
    public void setCallNoParserFactory(CallNoParserFactory callNoParserFactory) {
        this.callNoParserFactory = callNoParserFactory;
    }

    /**
     * Sets the modifier variable.
     *
     * @param modifier new value
     */
    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    /**
     * Sets the regex that's used for parsing interval definitions from call
     * numbers.
     *
     * @param regex new regex
     */
    public void setIntervalRegex(String regex) {
        this.intervalRegex = regex;
    }

    /**
     * Sets language of the locale that's used for call number interval
     * matching. Default is "fi".
     *
     * @param language language code (two lower case characters)
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Sets country of the locale that's used for call number interval matching.
     * Default is "FI".
     *
     * @param country country code (two upper case characters)
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Fetches the information related to the given call number. If the id
     * parameter is supplied, first the location is searched by the id and then
     * by the call number. If the id parameter is null, then call number is
     * used. If collection parameter is given, it's used together with the call
     * number.
     *
     * @param callno the call number to be resolved
     * @param lang the language of the UI
     * @param status of the publication, 0 = available, 1 = charged
     * @param owner owner of the location
     * @param collection collection code that's related to the location
     * @param id id number of the Location object to be searched
     * @return the HTML page that is returned to the user
     */
    public String resolve(String callno, String lang, boolean status, String owner, String collection, OutputFormat format, String id) {
        /* Get the output generator defined by the format */
        Generator generator = generators.get(format);
        /* Generator cannot be null */
        if (generator == null) {
            logger.error("Unable to find generator matching the given format \"{}\". Processing aborted.", format);
            return "";
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Use \"{}\" as output format.", format);
        }

        /* If status is false, the item is not available. */
        if (!status) {
            logger.debug("Not available: {\"lang:\"{}\",\"callno\":\"{}\",\"owner\":\"{}\"}", lang, callno, owner);
            return generator.generateOutputNotAvailable(lang, callno, owner);
        }

        // Get locating strategy related to the owner
        LocatingStrategy strategy = this.serviceFactory.get().getLocatingStrategy(owner);
        // Get Service object matching the locating strategy
        Service localService = this.serviceFactory.get(strategy);

        /* If id parameter is not null, try to search by id. */
        if (id != null) {
            /* Get the index entry matching the given id and owner */
            SimpleLocation index = localService.getIndexEntry(id, owner);
            /* If index entry is found, get the related Location object and */
            /* pass it to the Generator object. */
            if (index != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Get location by id parameter : \"{}\"", id);
                }
                switch (index.getLocationType()) {
                    case LIBRARY:
                        return generator.generateOutput(localService.getLibrary(index.getLocationId()), lang, callno);
                    case COLLECTION:
                        return generator.generateOutput(localService.getCollection(index.getLocationId()), lang, callno);
                    case SHELF:
                        return generator.generateOutput(localService.getShelf(index.getLocationId()), lang, callno);
                    default:
                        break;
                }
            }
        }

        logger.debug("Parameters : {\"callno\":\"{}\",\"lang\":\"{}\",\"collection\":\"{}\",\"owner\":\"{}\"}", callno, lang, collection, owner);

        /* Get the CallNoParser that is defined in settings. */
        CallNoParser parser = callNoParserFactory.createParser(generator, owner, strategy);

        /* Run preprocessing redirects. */
        callno = runPreprocessingRedirects(callno, owner, localService);

        /* If the collection code parameter is not empty, look for locations */
        /* with the given collection code. It's possible to have multiple */
        /* locations with the same call number, which is why the collection */
        /* code is needed to recognize them from each other. */
        if (!collection.isEmpty()) {
            /* Run through locations that have Match beginning checkbox checked. */
            SimpleLocation temp = runMatchBeginningByCollectionCodeCheck(callno, owner, collection, localService);
            if (temp != null) {
                logger.debug("Match beginning by collection code check hit! Location id : \"{}\"", temp.getLocationId());
                return parser.parse(callno, lang, owner, temp);
            }

            Shelf shelf = runShelvesByCollectionCodeCheck(collection, callno, owner, localService);
            if (shelf != null) {
                logger.debug("Check shelves by collection code hit! Shelf id : \"{}\"", shelf.getLocationId());
                return generator.generateOutput(shelf, lang, callno);
            }

            /* Then check the collections. */
            LibraryCollection libCollection = runCollectionsByCollectionCodeCheck(collection, callno, owner, localService);
            if (libCollection != null) {
                logger.debug("Check collections by collection code hit! Collection id : \"{}\"", libCollection.getLocationId());
                return generator.generateOutput(libCollection, lang, callno);
            }
            logger.debug("No location matching the given collection code was found! Collection code : \"{}\"", collection);
        }

        /* Run through locations that have Match beginning checkbox checked. */
        SimpleLocation location = runMatchBeginningCheck(callno, owner, localService);
        if (location != null) {
            logger.debug("Match beginning check hit! Location id : \"{}\"", location.getLocationId());
            return parser.parse(callno, lang, owner, location);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Start standard parsing process.");
        }
        /* Begin the standard call number parsing process. */
        return parser.parse(callno, lang, owner);
    }
}
