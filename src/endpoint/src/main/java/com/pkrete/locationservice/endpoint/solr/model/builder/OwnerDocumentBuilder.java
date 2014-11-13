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
package com.pkrete.locationservice.endpoint.solr.model.builder;

import com.pkrete.locationservice.endpoint.model.owner.Owner;
import com.pkrete.locationservice.endpoint.modifier.CallnoModification;
import com.pkrete.locationservice.endpoint.modifier.NotFoundRedirect;
import com.pkrete.locationservice.endpoint.modifier.PreprocessingRedirect;
import com.pkrete.locationservice.endpoint.solr.model.OwnerDocument;
import com.pkrete.locationservice.endpoint.solr.repository.RepositoryConstants;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a helper class that generates OwnerDocument objects representing the
 * given Owners.
 *
 * @author Petteri Kivimäki
 */
public class OwnerDocumentBuilder {

    private static final Logger logger = LoggerFactory.getLogger(OwnerDocumentBuilder.class.getName());

    /**
     * Creates a new OwnerDocument that represents the given Owner.
     *
     * @param owner Owner object
     * @return OwnerDocument that represents the given Owner
     */
    public static OwnerDocument build(Owner owner) {
        // Create a new OwnerDocument object
        OwnerDocument document = new OwnerDocument(owner.getId(), owner.getName(), owner.getCode());
        document.setColor(owner.getColor());
        document.setOpacity(owner.getOpacity());
        document.setLocatingStrategy(owner.getLocatingStrategy());
        document.setExporterVisible(owner.getExporterVisible());
        document.setAllowedIPs(owner.getAllowedIPs());
        // Return the object
        return document;
    }

    /**
     * Creates a new Owner that represents the given OwnerDocument.
     *
     * @param document OwnerDocument object
     * @return Owner that represents the given OwnerDocument
     */
    public static Owner build(OwnerDocument document) {
        // Create a new Owner object
        Owner owner = new Owner(document.getOwnerCode(), document.getName());
        owner.setId(document.getOwnerId());
        owner.setColor(document.getColor());
        owner.setOpacity(document.getOpacity());
        owner.setLocatingStrategy(document.getLocatingStrategy());
        owner.setExporterVisible(document.getExporterVisible());
        owner.setAllowedIPs(document.getAllowedIPs());
        // Return the object
        return owner;
    }

    /**
     * Converts the given int id to a string by adding 'own-' prefix to the id.
     *
     * @param id ownerId of the Owner
     * @return 'own-' prefix + ownerId
     */
    public static String getId(int id) {
        return RepositoryConstants.PREFIX_OWNER + Integer.toString(id);
    }

    /**
     * Converts the given string id to an int by removing 'own-' prefix from the
     * id.
     *
     * @param id id of the OwnerDocument
     * @return id without 'own-' prefix, which is the ownerId
     */
    public static int getId(String id) {
        id = id.replaceAll(RepositoryConstants.PREFIX_OWNER, "");
        return Integer.parseInt(id);
    }

    /**
     * Converts the given CallnoModification list into a list of Strings. Each
     * CallnoModification is serialized as a String.
     *
     * @param list CallnoModifications list
     * @return list of Strings
     */
    public static List<String> build(List<CallnoModification> list) {
        List<String> result = new ArrayList<String>();
        if (list == null) {
            return result;
        }
        for (CallnoModification mod : list) {
            result.add(Integer.toString(mod.getId()));
            result.add(mod.getCondition());
            result.add(mod.getOperation());
            result.add(Boolean.toString(mod.getIsActive()));
        }
        return result;
    }

    /**
     * Converts the given list of Strings into a list of CallnoModifications.
     *
     * @param list list to be converted
     * @param isPreprocessingRedirect if true the strings are converted into
     * PreprocessingRedirects, otherwise they're converted into
     * NotFoundRedirects
     * @return list of CallnoModifications
     */
    public static List<CallnoModification> build(List<String> list, boolean isPreprocessingRedirect) {
        List<CallnoModification> result = new ArrayList<CallnoModification>();
        if (list == null) {
            return result;
        }
        for (int i = 0; i < list.size(); i += 4) {
            CallnoModification mod;
            String type;
            if (isPreprocessingRedirect) {
                mod = new PreprocessingRedirect();
                type = "PREPROCESSING_REDIRECT";
            } else {
                mod = new NotFoundRedirect();
                type = "NOT_FOUND_REDIRECT";
            }
            if ((i + 1) % 4 == 1) {
                mod.setId(Integer.parseInt(list.get(i)));
            }
            if ((i + 2) % 4 == 2) {
                mod.setCondition(list.get(i + 1));
            }
            if ((i + 3) % 4 == 3) {
                mod.setOperation(list.get(i + 2));
            }
            if ((i + 4) % 4 == 0) {
                if (list.get(i + 3).equalsIgnoreCase("true")) {
                    mod.setIsActive(true);
                } else {
                    mod.setIsActive(false);
                }
            }
            result.add(mod);
            if (logger.isTraceEnabled()) {
                StringBuilder builder = new StringBuilder();
                builder.append("{\"id\":").append(mod.getId());
                builder.append(",\"condition\":\"").append(mod.getCondition());
                builder.append("\",\"operation\":\"").append(mod.getOperation());
                builder.append("\",\"isActive:\"").append(mod.getIsActive());
                builder.append(",\"type\":\"").append(type).append("\"}");
                logger.trace(builder.toString());
            }
        }
        return result;
    }
}
