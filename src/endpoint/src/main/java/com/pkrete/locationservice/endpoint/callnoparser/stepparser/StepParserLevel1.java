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
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import com.pkrete.locationservice.endpoint.util.LocationHelper;
import java.util.List;

/**
 * The <code>StepParserLevel1</code> class extends the
 * {@link StepParser StepParser} class.
 *
 * The StepParserLevel1 class defines the functionality for parsing the location
 * information from the given string if the string contains only one word.
 *
 * @author Petteri Kivimäki
 */
public class StepParserLevel1 extends StepParser {

    /**
     * Constructs and initializes a StepParserLevel1 object.
     */
    public StepParserLevel1() {
        super(1);
    }

    /**
     * Constructs and initializes a StepParserLevel1 object with the given
     * generator
     *
     * @param generator the generator object that generates the HTML page
     * returned to the user
     */
    public StepParserLevel1(Generator generator) {
        super(generator, 1);
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
            this.next = new StepParserLevel2(generator, dbService, this);
            return this.next.parse(callno, lang, owner);
        }

        List list = dbService.getShelf(callnoArr[0], owner);
        for (Object object : list) {
            Shelf shelf = (Shelf) object;
            if (LocationHelper.match(shelf.getCallNo(), callno)) {
                return generator.generateOutput(dbService.getShelf(shelf.getLocationId()), lang, callno);
            }
        }

        list = dbService.getCollection(callnoArr[0], owner);
        for (Object object : list) {
            LibraryCollection collection = (LibraryCollection) object;
            if (LocationHelper.match(collection.getCallNo(), callno)) {
                return generator.generateOutput(dbService.getCollection(collection.getLocationId()), lang, callno);
            }
        }

        list = dbService.getLibrary(callnoArr[0], owner);
        for (Object object : list) {
            Library library = (Library) object;
            if (LocationHelper.match(library.getCallNo(), callno)) {
                return generator.generateOutput(dbService.getLibrary(library.getLocationId()), lang, callno);
            }
        }

        return notFound(callno, lang, owner);
    }
}
