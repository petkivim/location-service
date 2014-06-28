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
     * optional parameter with the given name cannot be found or its value
     * is empty, a default value instead of null or an emtpy string is returned.
     * @param name a String specifying the name of the parameter 
     * @return a String representing the single value of the parameter
     */
    @Override
    public String getParameter(String name) {
        HttpServletRequest req = (HttpServletRequest) super.getRequest();
        return setDefault(name, req.getParameter(name));
    }

    /**
     * Sets default values to optional parameters, if their value is null
     * or empty. If the given name doesn't match to optional parameter names,
     * the original value is returned.
     * @param name parameter name
     * @param value parameter value
     * @return validated parameter value
     */
    private String setDefault(String name, String value) {
        // Validate "status" parameter - OPTIONAL
        if (name.equals("status") && value == null) {
            // "status" is optional -> set default value
            value = "0";
            return value;
        }
        // Validate "collection" parameter - OPTIONAL
        if (name.equals("collection") && value == null) {
            // "collection" is optional -> set default value
            value = "";
            return value;
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