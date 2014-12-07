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
import com.pkrete.locationservice.admin.model.illustration.Map;
import java.io.IOException;

/**
 * Custom deserializer for Map objects.
 *
 * @author Petteri Kivimäki
 */
public class MapJSONDeserializer extends JsonDeserializer<Map> {

    @Override
    public Map deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        // Get id
        int id = node.get("id") == null ? 0 : node.get("id").intValue();
        // Get description
        String description = node.get("description") == null ? "" : node.get("description").textValue();
        // Get url
        String url = node.get("url") == null ? "" : node.get("url").textValue();
        // Get filePath
        String filePath = node.get("file") == null ? "" : node.get("file").textValue();
        // Set filePath to null, if length is 0
        filePath = filePath.isEmpty() ? null : filePath;
        // Get color
        String color = node.get("color") == null ? "" : node.get("color").textValue();
        // Get opacity
        String opacity = node.get("opacity") == null ? "" : node.get("opacity").textValue();
        // Create new Map object
        Map map = new Map(0, url, filePath, description);
        // Set color
        map.setColor(color);
        // Set opacity
        map.setOpacity(opacity);
        // Return new map
        return map;
    }
}
