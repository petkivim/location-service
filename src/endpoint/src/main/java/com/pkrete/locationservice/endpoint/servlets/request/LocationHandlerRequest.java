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
package com.pkrete.locationservice.endpoint.servlets.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * This class extends HttpServletRequestWrapper class and overrides
 * getParameter method. When the optional parameters of the Exporter servlet
 * are accessed, default values are returned instead of null if the request
 * doesn't contain the parameters.
 * 
 * @author Petteri Kivimäki
 */
public class LocationHandlerRequest extends HttpServletRequestWrapper {

    public LocationHandlerRequest(HttpServletRequest request) {
        super(request);
    }

    /**
     * Returns the value of a request parameter with the given name. If an
     * optional parameter with the given name cannot be found or its value is
     * empty, a default value instead of null or an emtpy string is returned.
     *
     * @param name a String specifying the name of the parameter
     * @return a String representing the single value of the parameter
     */
    @Override
    public String getParameter(String name) {
        HttpServletRequest req = (HttpServletRequest) super.getRequest();
        return setDefault(name, req.getParameter(name));
    }

    /**
     * Sets default values to optional parameters, if their value is null or
     * empty. If the given name doesn't match to optional parameter names, the
     * original value is returned.
     *
     * @param name parameter name
     * @param value parameter value
     * @return validated parameter value
     */
    private String setDefault(String name, String value) {
        // Validate "owner" parameter - REQUIRED
        if (name.equals("owner") && value != null) {
            // The maximum length of the owner parameter is 10. If the length
            // of the value is longer, only first 10 characters count.
            if (value.length() > 10) {
                value = value.substring(0, 10);
            }
            return value;
        }
        // Validate "lang" parameter - REQUIRED
        if (name.equals("lang") && value != null) {
            // The maximum length of the lang parameter is 100. If the length
            // of the value is longer, only first 100 characters count.
            if (value.length() > 100) {
                value = value.substring(0, 100);
            }
            return value;
        }
        // Validate "callno" parameter - REQUIRED
        if (name.equals("callno") && value != null) {
            // The maximum length of the callno parameter is 300. If the length
            // of the value is longer, only first 300 characters count.
            if (value.length() > 300) {
                value = value.substring(0, 300);
            }
            return value;
        }
        // Validate "status" parameter - OPTIONAL
        if (name.equals("status")) {
            if (value == null) {
                // "status" is optional -> set default value
                value = "0";
                return value;
            } else {
                if (value.length() > 1) {
                    value = value.substring(0, 1);
                }
                return value;
            }
        }

        // Validate "collection" parameter - OPTIONAL
        if (name.equals("collection")) {
            if (value == null) {
                // "collection" is optional -> set default value
                value = "";
                return value;
            } else {
                // Max length is 100
                if (value.length() > 100) {
                    value = value.substring(0, 100);
                }
                return value;
            }
        }

        // Validate "id" parameter - OPTIONAL
        // "id" parameter must be a number or null which is why its value
        // is checked only when it's not null. If the value is not valid,
        // null must be returned.
        if (name.equals("id") && value != null) {
            if (value.isEmpty()) {
                // Value is empty -> return null
                return null;
            } else if (!value.matches("^\\d+$")) {
                // Value doesn't contain only numbers -> return null
                return null;
            }
            // Value contains only numbers -> return value
            return value;
        }
        return value;
    }
}
