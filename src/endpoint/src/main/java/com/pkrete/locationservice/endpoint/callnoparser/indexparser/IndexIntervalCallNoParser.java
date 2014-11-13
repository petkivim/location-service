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
package com.pkrete.locationservice.endpoint.callnoparser.indexparser;

import com.pkrete.locationservice.endpoint.callnoparser.CallNoParser;
import com.pkrete.locationservice.endpoint.callnoparser.LocatingStrategy;
import com.pkrete.locationservice.endpoint.generator.Generator;
import com.pkrete.locationservice.endpoint.model.location.SimpleLocation;
import com.pkrete.locationservice.endpoint.util.LocationHelper;
import com.pkrete.locationservice.endpoint.util.LocationServiceConstants;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * The <code>IndexIntervalCallNoParser</code> class extends the
 * {@link CallNoParser CallNoParser} class.
 *
 * The IndexIntervalCallNoParser class offers the functionality for parsing the
 * location information from the given string. This class supports interval
 * definitions in call numbers which means that it's possible to match call
 * number intervals to a single Location object.
 *
 * @author Petteri Kivimäki
 */
public class IndexIntervalCallNoParser extends CallNoParser {

    private String regex = LocationServiceConstants.INTERVAL_REGEX;
    private String language = LocationServiceConstants.LANGUAGE;
    private String country = LocationServiceConstants.COUNTRY;

    /**
     * Constructs and initializes a IndexIntervalCallNoParser object.
     */
    public IndexIntervalCallNoParser() {
        super(LocatingStrategy.INDEX_INTERVAL);
    }

    /**
     * Constructs and initializes a IndexIntervalCallNoParser object with the
     * given generator.
     *
     * @param generator the generator object that generates the html page
     * returned to the user
     */
    public IndexIntervalCallNoParser(Generator generator) {
        super(generator);
    }

    /**
     * Sets the regex that's used for parsing interval definitions from call
     * numbers.
     *
     * @param regex new regex
     */
    public void setRegex(String regex) {
        this.regex = regex;
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
     * Searches the given call number from the database.
     *
     * @param callno the call number to be handled
     * @param lang the language of the UI
     * @param owner owner of the location
     * @return the HTML page returned to the user
     */
    @Override
    public String parse(String callno, String lang, String owner) {
        // Compile call number interval regex
        Pattern pattern = Pattern.compile(regex);
        // Create new Locale for Collator
        Locale locale = new Locale(language, country);
        // Get RuleBasedCollator object for call number comparisons
        RuleBasedCollator collator = (RuleBasedCollator) Collator.getInstance(locale);
        // Using the PRIMARY strength - base is taken into account,
        // accent and case are ignored
        collator.setStrength(Collator.PRIMARY);

        // Get list of shelves from the index
        List<SimpleLocation> list = (List) dbService.getShelvesFromIndex(owner);
        // Go through the shelves
        for (SimpleLocation index : list) {
            if (LocationHelper.match(index.getCallNo(), callno) || LocationHelper.match(index.getCallNo(), callno, pattern, collator)) {
                return generator.generateOutput(dbService.getShelf(index.getLocationId()), lang, callno);
            }
        }

        // Get list of collections from the index
        list = (List) dbService.getCollectionsFromIndex(owner);
        // Go through the collections
        for (SimpleLocation index : list) {
            if (index.getLocationCode().isEmpty()) {
                continue;
            }
            if (LocationHelper.match(index.getCallNo(), callno) || LocationHelper.match(index.getCallNo(), callno, pattern, collator)) {
                return generator.generateOutput(dbService.getCollection(index.getLocationId()), lang, callno);
            }
        }

        // Get list of libraries from the index
        list = (List) dbService.getLibrariesFromIndex(owner);
        // Go through the libraries
        for (SimpleLocation index : list) {
            if (LocationHelper.match(index.getCallNo(), callno) || LocationHelper.match(index.getCallNo(), callno, pattern, collator)) {
                return generator.generateOutput(dbService.getLibrary(index.getLocationId()), lang, callno);
            }
        }

        return notFound(callno, lang, owner);
    }
}
