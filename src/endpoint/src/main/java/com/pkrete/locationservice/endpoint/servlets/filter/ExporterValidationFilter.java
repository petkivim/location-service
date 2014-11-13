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
package com.pkrete.locationservice.endpoint.servlets.filter;

import com.pkrete.locationservice.endpoint.model.owner.Owner;
import com.pkrete.locationservice.endpoint.service.Service;
import com.pkrete.locationservice.endpoint.servlets.request.ExporterRequest;
import com.pkrete.locationservice.endpoint.util.ApplicationContextUtils;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This filter is run every time when Exporter servlet is accessed - all the
 * requests to the Exporter are passing through this filter. This filter checks
 * that the request contains all the required parameters and sets a boolean
 * attribute indicating the validity of the request. The request object is
 * wrapped inside ExporterRequest object that takes care of returning default
 * values to optional parameters, if they're not present in the request.
 *
 * @author Petteri Kivimäki
 */
public class ExporterValidationFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(ExporterValidationFilter.class.getName());

    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
        // Set a boolean value that tells if the request is valid or not.
        req.setAttribute("validRequest", validate(req));
        // Pass the request to the next filter in the chain
        fc.doFilter(new ExporterRequest((HttpServletRequest) req), res);
    }

    @Override
    public void destroy() {
    }

    /**
     * Checks that the given ServletRequest object contains all the required
     * parameters. Returns true if and only if the request is valid, otherwise
     * false is returned
     *
     * @param req ServletRequest object to be checked
     * @return true if and only if all the required parameters are present in
     * the request; otherwise returns false
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
        } else {
            // Fetch Spring's dbService bean 
            Service dbService = (Service) ApplicationContextUtils.getApplicationContext().getBean("dbService");
            // Get owner code
            String ownerCode = req.getParameter("owner");
            // Get the Owner object from the DB
            Owner owner = dbService.getOwnerByCode(ownerCode);
            // If owner is null, the request is not valid
            if (owner == null) {
                logger.warn("Bad request! Reason : owner \"{}\" doesn't exist!", ownerCode);
                // "owner" is required -> invalid request
                return false;
            } else {
                // Set the Owner object as request attribute
                req.setAttribute("owner", owner);
            }
        }
        return true;
    }
}
