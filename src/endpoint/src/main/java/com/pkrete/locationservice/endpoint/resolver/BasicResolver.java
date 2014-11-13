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
import com.pkrete.locationservice.endpoint.modifier.CallnoModification;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import com.pkrete.locationservice.endpoint.model.location.SimpleLocation;
import com.pkrete.locationservice.endpoint.service.Service;
import com.pkrete.locationservice.endpoint.util.LocationHelper;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.Collator;
import java.text.RuleBasedCollator;

/**
 * The <code>BasicResolver</code> class extends the abstract {@link Resolver}
 * class.
 *
 * The BasicResolver class is responsible of receiving the parameters from the
 * {@link LocationService LocationService} servlet and delegating all the
 * necessary tasks between other classes. LocationResolver can be seen as a link
 * between the {@link LocationService LocationService} servlet and other classes
 * of the application.
 *
 * @author Petteri Kivimäki
 */
public class BasicResolver extends Resolver {

    private static final Logger logger = LoggerFactory.getLogger(BasicResolver.class.getName());

    /**
     * Constructs and initializes a new BasicResolver object.
     */
    public BasicResolver() {
        super();
    }

    /**
     * Constructs and initializes a new BasicResolver object.
     *
     * @param generator Generator object that's responsible of generating the
     * output shown the user
     */
    public BasicResolver(Map<OutputFormat, Generator> generators) {
        super(generators);
    }

    /**
     * Runs through preprocessing redirects and modifies the given call number
     * accordingly. If there are multiple modifications matching the call
     * number, only first one is processed and all the others are skipped. If
     * there are no redirects matching the call number, the original call number
     * is returned unchanged.
     *
     * @param callno call number to be modified
     * @param owner owner code of the library
     * @param localService service object for data connections
     * @return modified or original call number
     */
    @Override
    protected String runPreprocessingRedirects(String callno, String owner, Service localService) {
        /* Run through preprocessing redirects. */
        List<CallnoModification> redirects = localService.getPreprocessingRedirects(owner);
        /* Original call number */
        String orgCallno = callno;
        for (CallnoModification mod : redirects) {
            if (modifier.canBeModified(callno, mod)) {
                callno = modifier.modify(callno, mod);
                logger.debug("Found reprocessing redirect match! Redirect: \"{}\" -> \"{}\".", orgCallno, callno);
                return callno;
            }
        }
        return callno;
    }

    /**
     * Runs through locations that have Match beginning check box checked and
     * checks if the given call number matches to one of them. If a match is
     * found, the matching Location object is returned. If no match is found,
     * null is returned.
     *
     * @param callno call number to be checked
     * @param owner owner code of the library
     * @param localService service object for data connections
     * @return matching Location object or null
     */
    @Override
    protected SimpleLocation runMatchBeginningCheck(String callno, String owner, Service localService) {
        // Get all the collections that have Match beginning checkbox checked
        List list = localService.getSubstringLocations(owner);
        // Run through the list
        for (SimpleLocation location : (List<SimpleLocation>) list) {
            // If the collection's call number can be found from the beginning
            // of the given callno and the callno is longer than the
            // collection's call number, we know that the location indicated
            // by the callno belongs to this collection.
            if (LocationHelper.match(location.getCallNo(), callno) && callno.length() > location.getCallNo().length()) {
                return location;
            }
        }
        return null;
    }

    /**
     * Runs through locations that have Match beginning check box checked and
     * checks if the given call number matches to one of them, and if the given
     * collection code matches too. If a match is found, the matching Location
     * object is returned. If no match is found, null is returned.
     *
     * @param callno call number to be checked
     * @param owner owner code of the library
     * @param collectionCode collection code of the location
     * @param localService service object for data connections
     * @return matching Location object or null
     */
    @Override
    protected SimpleLocation runMatchBeginningByCollectionCodeCheck(String callno, String owner, String collectionCode, Service localService) {
        // Get all the collections that have Match beginning checkbox checked
        // and that have the given collectionCode
        List list = localService.getSubstringLocations(owner, collectionCode);
        // Run through the list
        for (SimpleLocation location : (List<SimpleLocation>) list) {
            // Get the collection code of the collection
            String collection = location.getCollectionCode();
            // If the collection's collection code equals to collectionCode,
            // we know that the location indicated by the callno belongs to
            // this collection
            if (collection != null && !collection.isEmpty() && collection.equals(collectionCode)) {
                return location;
            }
        }
        return null;
    }

    /**
     * Runs through shelves that belong to collections that have a collection
     * code and checks if the given call number and collection code combination
     * matches to one of them. If a match is found, the matching Shelf object is
     * returned. If no match is found, null is returned.
     *
     * @param collection collection code to be searched
     * @param callno call number to be searched
     * @param owner owner code of the library
     * @param localService service object for data connections
     * @return matching Shelf object or null
     */
    @Override
    protected Shelf runShelvesByCollectionCodeCheck(String collection, String callno, String owner, Service localService) {
        // Compile call number interval regex
        Pattern pattern = Pattern.compile(intervalRegex);
        // Create new Locale for Collator
        Locale locale = new Locale(language, country);
        // Get RuleBasedCollator object for call number comparisons
        RuleBasedCollator collator = (RuleBasedCollator) Collator.getInstance(locale);
        // Using the PRIMARY strength - base is taken into account,
        // accent and case are ignored
        collator.setStrength(Collator.PRIMARY);
        // Get shelves by collection code
        List list = localService.getShelvesByCollectionCode(owner, collection);
        for (SimpleLocation location : (List<SimpleLocation>) list) {
            if (LocationHelper.match(location.getCallNo(), callno) || LocationHelper.match(location.getCallNo(), callno, pattern, collator)) {
                return localService.getShelf(location.getLocationId());
            }
        }
        return null;
    }

    /**
     * Runs through the collections that have a collection code and checks if
     * the given call number and collection code combination matches to one of
     * them. If a match is found, the matching LibraryCollection object is
     * returned. If no match is found, null is returned.
     *
     * @param collection collection code to be searched
     * @param callno call number to be searched
     * @param owner owner code of the library
     * @param localService service object for data connections
     * @return matching LibraryCollection object or null
     */
    @Override
    protected LibraryCollection runCollectionsByCollectionCodeCheck(String collection, String callno, String owner, Service localService) {
        // Compile call number interval regex
        Pattern pattern = Pattern.compile(intervalRegex);
        // Create new Locale for Collator
        Locale locale = new Locale(language, country);
        // Get RuleBasedCollator object for call number comparisons
        RuleBasedCollator collator = (RuleBasedCollator) Collator.getInstance(locale);
        // Using the PRIMARY strength - base is taken into account,
        // accent and case are ignored
        collator.setStrength(Collator.PRIMARY);
        // Get collections by collection code
        LibraryCollection location = localService.getCollectionByCollectionCode(owner, collection);
        if (location != null) {
            if (LocationHelper.match(location.getCallNo(), callno) || LocationHelper.match(location.getCallNo(), callno, pattern, collator)) {
                return location;
            }
        }
        return null;
    }
}
