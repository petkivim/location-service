/**
 * This file is part of Location Service :: Admin.
 * Copyright (C) 2014 Petteri Kivimäki
 *
 * Location Service :: Admin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Location Service :: Admin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Location Service :: Admin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.admin.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.util.LocationJSONDeserializerHelper;
import java.io.IOException;

/**
 * Custom Deserializer for Library objects.
 * 
 * @author Petteri Kivimäki
 */
public class LibraryJSONDeserializer extends JsonDeserializer<Library> {

    @Override
    public Library deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        // Create new Library object and set values that have parsed
        Library library = new Library();
        // Deserialize name, locationCode, floor, staffNote1, staffNote2, 
        // map and image variables
        LocationJSONDeserializerHelper.deserializeBasicGroup1(library, node);
        // Deserialize descriptions and notes variables
        LocationJSONDeserializerHelper.deserializeDescriptionsAndNotes(library, node);
        // Deserialize areas variable
        LocationJSONDeserializerHelper.deserializeAreas(library, node);
        
        // Return the library
        return library;
    }
}
