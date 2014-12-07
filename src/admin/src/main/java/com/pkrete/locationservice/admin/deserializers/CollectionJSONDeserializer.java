/**
 * This file is part of Location Service :: Admin. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Admin is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Admin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Admin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.admin.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.util.LocationJSONDeserializerHelper;
import java.io.IOException;

/**
 * Custom deserializer for LibraryCollection objects.
 *
 * @author Petteri Kivimäki
 */
public class CollectionJSONDeserializer extends JsonDeserializer<LibraryCollection> {

    @Override
    public LibraryCollection deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        // Create new LibraryCollection object
        LibraryCollection collection = new LibraryCollection();
        // Deserialize name, locationCode, floor, staffNote1, staffNote2, 
        // map and image variables
        LocationJSONDeserializerHelper.deserializeBasicGroup1(collection, node);
        // Deserialize descriptions and notes variables
        LocationJSONDeserializerHelper.deserializeDescriptionsAndNotes(collection, node);
        // Deserialize areas variable
        LocationJSONDeserializerHelper.deserializeAreas(collection, node);
        // Deserialize subject matters variable
        LocationJSONDeserializerHelper.deserializeSubjectMatters(collection, node);

        // Deserialize collectionCode
        String collectionCode = node.get("collection_code") == null ? "" : node.get("collection_code").textValue();
        // Deserialize shelfNumber
        String shelfNumber = node.get("shelf_number") == null ? "" : node.get("shelf_number").textValue();
        // Set values
        collection.setCollectionCode(collectionCode);
        collection.setShelfNumber(shelfNumber);

        // Deserialize isSubstring (= match_beginning)
        if (node.get("match_beginning") != null) {
            boolean isSubstring = node.get("match_beginning").asBoolean();
            collection.setIsSubstring(isSubstring);
        }

        // Deserialize library id
        int libraryId = node.get("library_id") == null ? 0 : node.get("library_id").intValue();
        if (libraryId != 0) {
            collection.setLibrary(new Library(libraryId));
        }
        // Return the collection
        return collection;
    }
}
