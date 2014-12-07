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
import com.pkrete.locationservice.admin.model.template.CSS;
import java.io.IOException;

/**
 * Custom deserializer for CSS objects.
 *
 * @author Petteri Kivimäki
 */
public class CSSJSONDeserializer extends JsonDeserializer<CSS> {

    @Override
    public CSS deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        // Parse id
        int id = node.get("id") == null ? 0 : node.get("id").intValue();
        // Parse filename
        String filename = node.get("filename") == null ? "" : node.get("filename").textValue();
        // Parse contents
        String contents = node.get("contents") == null ? null : node.get("contents").textValue();

        // Return new CSS
        return new CSS(id, filename, "", contents, null);
    }
}
