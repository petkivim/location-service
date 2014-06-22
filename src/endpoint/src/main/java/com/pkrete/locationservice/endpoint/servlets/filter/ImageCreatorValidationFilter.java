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
package com.pkrete.locationservice.endpoint.servlets.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.log4j.Logger;

/**
 * This filter is run every time when ImageCreator servlet is
 * accessed - all the requests to the ImageCreator are passing through this
 * filter. This filter checks that the request contains all the required 
 * parameters and sets a boolean attribute indicating the validity of the
 * request. The request object is wrapped inside ExporterRequest object that 
 * takes care of returning default values to optional parameters, if they're not
 * present in the request.
 * 
 * @author Petteri Kivimäki
 */
public class ImageCreatorValidationFilter implements Filter {

    private final static Logger logger = Logger.getLogger(ImageCreatorValidationFilter.class.getName());

    public void init(FilterConfig fc) throws ServletException {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
        // Set a boolean value that tells if the request is valid or not.
        req.setAttribute("validRequest", validate(req));
        // Pass the request to the next filter in the chain
        fc.doFilter(req, res);
    }

    public void destroy() {
    }

    /**
     * Checks that the given ServletRequest object contains all the required
     * parameters. Returns true if and only if the request is valid, otherwise
     * false is returned
     * @param req ServletRequest object to be checked
     * @return true if and only if all the required parameters are present
     * in the request; otherwise returns false
     */
    private boolean validate(ServletRequest req) {
        // Validate "owner" parameter - REQUIRED
        if (req.getParameter("owner") == null) {
            logger.warn("Bad request! Reason : \"owner\" parameter missing!");
            // "owner" is required -> invalid request
            return false;
        } else if (req.getParameter("owner").isEmpty()) {
            logger.warn("Bad request! Reason : \"owner\" parameter is empty!");
            // "owner" is required -> invalid request
            return false;
        }
        // Validate "locationId" parameter - REQUIRED
        if (req.getParameter("locationId") == null) {
            logger.warn("Bad request! Reason : \"locationId\" parameter missing!");
            // "locationId" is required -> invalid request
            return false;
        } else if (!req.getParameter("locationId").matches("^\\d+$")) {
            logger.warn("\"locationId\" parameter value is invalid: \"" + req.getParameter("locationId") + "\".");
            // "locationId" must contain numbers only -> invalid request
            return false;
        }
        return true;
    }
}