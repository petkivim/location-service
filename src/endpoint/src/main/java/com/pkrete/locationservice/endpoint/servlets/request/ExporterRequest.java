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
public class ExporterRequest extends HttpServletRequestWrapper {

    public ExporterRequest(HttpServletRequest request) {
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
        // Validate "type" parameter - REQUIRED
        if (name.equals("type")) {
            if (value == null || value.isEmpty()) {
                // "type" is required -> set default value
                value = "callno";
            }
            return value;
        }
        // Validate "position" parameter - REQUIRED
        if (name.equals("position")) {
            if (value == null || value.isEmpty()) {
                // "position" is required -> set default value
                value = "any";
            }
            return value;
        }
        // Validate "search" parameter - OPTIONAL
        if (name.equals("search") && value == null) {
            // "search" is optional -> set default value
            value = "";
            return value;
        }
        // Validate "children" parameter - OPTIONAL
        if (name.equals("children") && value == null) {
            // "children" is optional -> set default value
            value = "no";
            return value;
        }
        // Validate "version" parameter - OPTIONAL
        if (name.equals("version") && value == null) {
            // "version" is optional -> set default value
            value = "1";
            return value;
        }
        return value;
    }
}
