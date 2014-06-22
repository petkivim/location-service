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
package com.pkrete.locationservice.endpoint.callnoparser.simpleparser;

import com.pkrete.locationservice.endpoint.callnoparser.CallNoParser;
import com.pkrete.locationservice.endpoint.callnoparser.LocatingStrategy;
import com.pkrete.locationservice.endpoint.comparator.LocationComparator;
import com.pkrete.locationservice.endpoint.generator.Generator;
import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.util.LocationHelper;
import java.util.Collections;
import java.util.List;

/**
 * The <code>SimpleCallNoParser</code> class extends the {@link CallNoParser CallNoParser} class.
 *
 * The SimpleCallNoParser class offers the functionality for parsing the location information
 * from the given string.
 *
 * @author Petteri Kivimäki
 */
public class SimpleCallNoParser extends CallNoParser {

    /**
     * Contructs and initializes a SimpleCallNoParser object.
     */
    public SimpleCallNoParser() {
        super(LocatingStrategy.SIMPLE);
    }

    /**
     * Contructs and initializes a SimpleCallNoParser object with the given generator
     * @param generator the generator object that generates the html page returned to the user
     */
    public SimpleCallNoParser(Generator generator) {
        super(generator);
    }

    /**
     * Searches the given call number from the database.
     * @param callno the call number to be handled
     * @param lang the language of the UI
     * @param owner owner of the location
     * @return the html page returned to the user
     */
    public String parse(String callno, String lang, String owner) {
        List<Location> list = dbService.getShelves(owner);
        Collections.sort(list, new LocationComparator());
        Collections.reverse(list);
        for (Location location : list) {
            if (LocationHelper.match(location.getCallNo(), callno)) {
                return generator.generateOutput(dbService.getShelf(location.getLocationId()), lang, callno);
            }
        }

        list = dbService.getCollections(owner);
        Collections.sort(list, new LocationComparator());
        Collections.reverse(list);
        for (Location location : list) {
            if (location.getLocationCode().isEmpty()) {
                continue;
            }
            if (LocationHelper.match(location.getCallNo(), callno)) {
                return generator.generateOutput(dbService.getCollection(location.getLocationId()), lang, callno);
            }
        }

        list = dbService.getLibraries(owner);
        Collections.sort(list, new LocationComparator());
        Collections.reverse(list);
        for (Location location : list) {
            if (LocationHelper.match(location.getCallNo(), callno)) {
                return generator.generateOutput(dbService.getLibrary(location.getLocationId()), lang, callno);
            }
        }

        return notFound(callno, lang, owner);
    }
}
