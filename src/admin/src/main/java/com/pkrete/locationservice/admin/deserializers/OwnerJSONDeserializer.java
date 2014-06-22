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

import com.pkrete.locationservice.admin.model.owner.CallnoModification;
import com.pkrete.locationservice.admin.model.owner.LocatingStrategy;
import com.pkrete.locationservice.admin.model.owner.NotFoundRedirect;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.owner.PreprocessingRedirect;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.util.ApplicationContextUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Custom Deserializer for Owner objects.
 * 
 * @author Petteri Kivimäki
 */
public class OwnerJSONDeserializer extends JsonDeserializer<Owner> {

    @Override
    public Owner deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {

        LocatingStrategy strategy = null;
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        // Parse id
        int id = node.get("id") == null ? 0 : node.get("id").intValue();
        // Parse code
        String code = node.get("code") == null ? "" : node.get("code").textValue();
        // Parse name
        String name = node.get("name") == null ? "" : node.get("name").textValue();
        // Parse color
        String color = node.get("color") == null ? "" : node.get("color").textValue();
        // Parse opacity
        String opacity = node.get("opacity") == null ? "" : node.get("opacity").textValue();
        // Parse locating strategy
        String strategyStr = node.get("locating_strategy") == null ? null : node.get("locating_strategy").textValue();
        // If strategyStr not null, convert from string to LocatingStrategy object
        if (strategyStr != null) {
            // Get converterService bean from Application Context
            ConverterService converterService = (ConverterService) ApplicationContextUtils.getApplicationContext().getBean("converterService");
            // Convert strategyStr to LocatingStrategy object
            strategy = (LocatingStrategy) converterService.convert(strategyStr, LocatingStrategy.class, LocatingStrategy.INDEX);
        }
        // String for ip addresses
        String ips = null;
        // Does allowed_ips node exist
        if (node.path("allowed_ips") != null) {
            // Parse allowed ip addresses
            Iterator<JsonNode> ite = node.path("allowed_ips").elements();
            // Init ips variable
            ips = "";
            // Iterate ips
            while (ite.hasNext()) {
                // Get next ip node
                JsonNode temp = ite.next();
                // Add ip to the ips string
                ips += temp.textValue();
                // Add line break, if there are more ips to read
                if (ite.hasNext()) {
                    ips += "\n";
                }
            }
        }
        // Variables for redirects
        List<PreprocessingRedirect> preprocessingRedirect = null;
        List<NotFoundRedirect> notFoundRedirect = null;

        // Does redirects node exist
        if (node.path("redirects") != null) {
            preprocessingRedirect = new ArrayList<PreprocessingRedirect>();
            notFoundRedirect = new ArrayList<NotFoundRedirect>();
            // Parse redirects
            Iterator<JsonNode> ite = node.path("redirects").elements();
            // Iterate redirects
            while (ite.hasNext()) {
                // Get next redirect
                JsonNode temp = ite.next();
                // Parse id
                int redirectId = temp.get("id") == null ? 0 : temp.get("id").intValue();
                // Parse type
                String redirectType = temp.get("type") == null ? "" : temp.get("type").textValue();
                // Parse operation
                String condition = temp.get("condition") == null ? "" : temp.get("condition").textValue();
                // Parse operation
                String operation = temp.get("operation") == null ? "" : temp.get("operation").textValue();
                // Is active
                boolean redirectActive = true;
                if (temp.get("is_active") != null) {
                    // Parse is_active
                    redirectActive = temp.get("is_active").asBoolean();
                }
                CallnoModification redirect = null;
                if (redirectType.equalsIgnoreCase("PREPROCESS")) {
                    redirect = new PreprocessingRedirect();
                } else if (redirectType.equalsIgnoreCase("NOTFOUND")) {
                    redirect = new NotFoundRedirect();
                } else {
                    continue;
                }
                redirect.setId(redirectId);
                redirect.setCondition(condition);
                redirect.setOperation(operation);
                redirect.setIsActive(redirectActive);
                if (redirectType.equalsIgnoreCase("PREPROCESS")) {
                    preprocessingRedirect.add((PreprocessingRedirect) redirect);
                } else if (redirectType.equalsIgnoreCase("NOTFOUND")) {
                    notFoundRedirect.add((NotFoundRedirect) redirect);
                }
            }
        }
        // Create new Owner object
        Owner owner = new Owner(code, name);
        // Set values
        owner.setPreprocessingRedirects(preprocessingRedirect);
        owner.setNotFoundRedirects(notFoundRedirect);
        owner.setColor(color);
        owner.setOpacity(opacity);
        owner.setLocatingStrategy(strategy);
        owner.setAllowedIPs(ips);
        if (node.get("exporter_visible") != null && node.get("exporter_visible").booleanValue()) {
            // Parse exporter visible
            owner.setExporterVisible(node.get("exporter_visible").asBoolean());
        }
        // Return Owner
        return owner;
    }
}
