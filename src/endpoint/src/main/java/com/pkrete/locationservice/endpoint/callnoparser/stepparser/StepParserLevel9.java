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
package com.pkrete.locationservice.endpoint.callnoparser.stepparser;

import com.pkrete.locationservice.endpoint.generator.Generator;
import com.pkrete.locationservice.endpoint.service.Service;
import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.util.LocationHelper;
import java.util.List;

/**
 * The <code>StepParserLevel9</code> class extends the
 * {@link StepParser StepParser} class.
 *
 * The StepParserLevel9 class defines the functionality for parsing the location
 * information from the given string and it has no limitations regarding to the
 * size of the string. However, only the first nine words of the string are
 * being parsed.
 *
 * @author Petteri Kivimäki
 */
public class StepParserLevel9 extends StepParser {

    /**
     * Constructs and initializes a StepParserLevel9 object with the given
     * generator
     *
     * @param generator the generator object that generates the v page returned
     * to the user
     * @param dbService the dbService is object provides access to the database
     * @param previous CallNoParser object before this object in processing
     * queue
     */
    public StepParserLevel9(Generator generator, Service dbService, StepParser previous) {
        super(generator, 9, previous);
        this.dbService = dbService;
        super.previous = previous;
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
        String callnoArr[] = callno.split(" ");

        if (callnoArr.length > limit) {
            this.next = new StepParserLevel10(generator, dbService, this);
            return this.next.parse(callno, lang, owner);
        } else if (callnoArr.length < limit) {
            return this.previous.parse(callno, lang, owner);
        }

        List<Location> list = null;

        for (int i = 0; i < limit - 8; i++) {
            list = dbService.getShelf(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4] + " " + callnoArr[i + 5] + " " + callnoArr[i + 6] + " " + callnoArr[i + 7] + " " + callnoArr[i + 8], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getShelf(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 7; i++) {
            list = dbService.getShelf(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4] + " " + callnoArr[i + 5] + " " + callnoArr[i + 6] + " " + callnoArr[i + 7], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getShelf(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 6; i++) {
            list = dbService.getShelf(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4] + " " + callnoArr[i + 5] + " " + callnoArr[i + 6], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getShelf(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 5; i++) {
            list = dbService.getShelf(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4] + " " + callnoArr[i + 5], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getShelf(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 4; i++) {
            list = dbService.getShelf(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getShelf(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 3; i++) {
            list = dbService.getShelf(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getShelf(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 2; i++) {
            list = dbService.getShelf(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getShelf(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 1; i++) {
            list = dbService.getShelf(callnoArr[i] + " " + callnoArr[i + 1], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getShelf(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit; i++) {
            list = dbService.getShelf(callnoArr[8 - i], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getShelf(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 8; i++) {
            list = dbService.getCollection(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4] + " " + callnoArr[i + 5] + " " + callnoArr[i + 6] + " " + callnoArr[i + 7] + " " + callnoArr[i + 8], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getCollection(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 7; i++) {
            list = dbService.getCollection(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4] + " " + callnoArr[i + 5] + " " + callnoArr[i + 6] + " " + callnoArr[i + 7], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getCollection(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 6; i++) {
            list = dbService.getCollection(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4] + " " + callnoArr[i + 5] + " " + callnoArr[i + 6], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getCollection(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 5; i++) {
            list = dbService.getCollection(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4] + " " + callnoArr[i + 5], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getCollection(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 4; i++) {
            list = dbService.getCollection(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getCollection(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 3; i++) {
            list = dbService.getCollection(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getCollection(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 2; i++) {
            list = dbService.getCollection(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getCollection(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 1; i++) {
            list = dbService.getCollection(callnoArr[i] + " " + callnoArr[i + 1], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getCollection(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit; i++) {
            list = dbService.getCollection(callnoArr[8 - i], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getCollection(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 8; i++) {
            list = dbService.getLibrary(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4] + " " + callnoArr[i + 5] + " " + callnoArr[i + 6] + " " + callnoArr[i + 7] + " " + callnoArr[i + 8], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getLibrary(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 7; i++) {
            list = dbService.getLibrary(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4] + " " + callnoArr[i + 5] + " " + callnoArr[i + 6] + " " + callnoArr[i + 7], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getLibrary(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 6; i++) {
            list = dbService.getLibrary(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4] + " " + callnoArr[i + 5] + " " + callnoArr[i + 6], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getLibrary(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 5; i++) {
            list = dbService.getLibrary(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4] + " " + callnoArr[i + 5], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getLibrary(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 4; i++) {
            list = dbService.getLibrary(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3] + " " + callnoArr[i + 4], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getLibrary(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 3; i++) {
            list = dbService.getLibrary(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2] + " " + callnoArr[i + 3], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getLibrary(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 2; i++) {
            list = dbService.getLibrary(callnoArr[i] + " " + callnoArr[i + 1] + " " + callnoArr[i + 2], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getLibrary(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit - 1; i++) {
            list = dbService.getLibrary(callnoArr[i] + " " + callnoArr[i + 1], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getLibrary(location.getLocationId()), lang, callno);
                }
            }
        }

        for (int i = 0; i < limit; i++) {
            list = dbService.getLibrary(callnoArr[8 - i], owner);
            for (Location location : list) {
                if (LocationHelper.match(location.getCallNo(), callno)) {
                    return generator.generateOutput(dbService.getLibrary(location.getLocationId()), lang, callno);
                }
            }
        }

        return notFound(callno, lang, owner);
    }
}
