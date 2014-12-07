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
import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.model.location.Shelf;
import com.pkrete.locationservice.admin.util.LocationJSONDeserializerHelper;
import java.io.IOException;

/**
 * Custom deserializer for Shelf objects.
 *
 * @author Petteri Kivimäki
 */
public class ShelfJSONDeserializer extends JsonDeserializer<Shelf> {

    @Override
    public Shelf deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        // Create new Shelf object
        Shelf shelf = new Shelf();
        // Deserialize name, locationCode, floor, staffNote1, staffNote2, 
        // map and image variables
        LocationJSONDeserializerHelper.deserializeBasicGroup1(shelf, node);
        // Deserialize descriptions and notes variables
        LocationJSONDeserializerHelper.deserializeDescriptionsAndNotes(shelf, node);
        // Deserialize areas variable
        LocationJSONDeserializerHelper.deserializeAreas(shelf, node);
        // Deserialize subject matters variable
        LocationJSONDeserializerHelper.deserializeSubjectMatters(shelf, node);

        // Deserialize shelfNumber
        String shelfNumber = node.get("shelf_number") == null ? "" : node.get("shelf_number").textValue();
        // Set value
        shelf.setShelfNumber(shelfNumber);

        // Deserialize collection id
        int collectionId = node.get("collection_id") == null ? 0 : node.get("collection_id").intValue();
        if (collectionId != 0) {
            shelf.setCollection(new LibraryCollection(collectionId));
        }
        // Return the collection
        return shelf;
    }
}
