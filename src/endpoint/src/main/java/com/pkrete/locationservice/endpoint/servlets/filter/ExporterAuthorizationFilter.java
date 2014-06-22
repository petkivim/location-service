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

import com.pkrete.locationservice.endpoint.model.owner.Owner;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 * This filter is run every time when Exporter servlet is
 * accessed - all the requests to the Exporter are passing through this
 * filter. This filter checks that the user is authorized to access the
 * Exporter servlet.
 * 
 * @author Petteri Kivimäki
 */
public class ExporterAuthorizationFilter implements Filter {

    private final static Logger logger = Logger.getLogger(ExporterAuthorizationFilter.class.getName());

    public void init(FilterConfig fc) throws ServletException {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
        // Set a boolean value that tells if the user is autthorized to access
        // the Exporter servlet.
        req.setAttribute("authorized", false);

        // If the request is valid, check that the user is authorized to
        // access the servlet
        if ((Boolean) req.getAttribute("validRequest")) {
            // Get Owner object
            Owner owner = (Owner)req.getAttribute("owner");
            // Check that the user is authorized to access the servlet
            if (isAuthorized(owner, (HttpServletRequest) req)) {
                // If user is authorized, set "authorized" to true
                req.setAttribute("authorized", true);
            } else {
                logger.warn("Unauthorized access attempt registered.");
            }
        }
        fc.doFilter(req, res);
    }

    public void destroy() {
    }

    /**
     * Checks if the requestor is authorized to access the Exporter interface.
     * Requestor is authorized if the IP address mathes an IP address defined
     * in the owner's list of allowed IPs.
     * 
     * Read the remote IP of a servlet request. This may be in one of two
     * places. If we are behind an Apache server with mod_proxy_http, we get the
     * remote IP address from the request header x-forwarded-for. In that case
     * the remote ip of the requestor is always that of the Apache server, since
     * that is the last proxy, as per spec.
     * 
     * If this header is missing, we're probably running locally for testing. In
     * that case we can just use the remote IP from the request object itself.
     * 
     * http://httpd.apache.org/docs/2.2/mod/mod_proxy.html
     * 
     * @param owner owner that defines the list of allowed IPs
     * @request request servlet request
     **/
    private boolean isAuthorized(Owner owner, HttpServletRequest request) {
        if (owner.getExporterVisible()) {
            return true;
        } else {
            String ip = request.getRemoteAddr();
            if (request.getHeader("x-forwarded-for") != null) {
                try {
                    ip = InetAddress.getByName(request.getHeader("x-forwarded-for")).getHostAddress();
                } catch (UnknownHostException uhe) {
                    logger.error("Unable to get the remote address from HttpServletRequest.");
                    logger.error(uhe);
                }
            }
            String[] ips = owner.getAllowedIPs().split("\n");
            for (int i = 0; i < ips.length; i++) {
                if (ip.matches(ips[i].trim())) {
                    return true;
                }
            }
        }
        return false;
    }
}
