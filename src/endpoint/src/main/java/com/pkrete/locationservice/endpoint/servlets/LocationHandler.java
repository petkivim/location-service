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
package com.pkrete.locationservice.endpoint.servlets;

import com.pkrete.locationservice.endpoint.generator.Generator;
import com.pkrete.locationservice.endpoint.resolver.OutputFormat;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.pkrete.locationservice.endpoint.statistics.SearchEvent;
import com.pkrete.locationservice.endpoint.statistics.SearchEventStatisticsQueue;
import com.pkrete.locationservice.endpoint.statistics.SearchEventType;
import com.pkrete.locationservice.endpoint.resolver.Resolver;
import com.pkrete.locationservice.endpoint.converter.ConverterService;
import com.pkrete.locationservice.endpoint.util.ApplicationContextUtils;
import com.pkrete.locationservice.endpoint.util.PropertiesUtil;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * The LocationHandler servlet class receives the call number and the language 
 * of the UI via <code>GET</code> or <code>POST</code> method and forwards 
 * them to the {@link LocationResolver LocationResolver}
 * class. LocationResolver generates output string and returns it to the 
 * LocationHandler that returns it to the user. All the parameters are
 * validated by the LocationHandlerRequestValidator class, so no validation 
 * is needed in LocationHandler.
 *
 * @author Petteri Kivimäki
 */
public class LocationHandler extends HttpServlet {

    private final static Logger logger = Logger.getLogger(LocationHandler.class.getName());
    private static final String errorMessage = PropertiesUtil.getProperty("error.locationhandler.400.message");

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get request processing starting time
        long start = System.currentTimeMillis();
        // Get converter service from application context
        ConverterService converter = (ConverterService) ApplicationContextUtils.getApplicationContext().getBean("converter");
        // Get output format
        OutputFormat format = (OutputFormat) converter.convert(request.getParameter("output"), OutputFormat.class, OutputFormat.HTML);

        // Set response content type according to the output format
        if (format == OutputFormat.XML) {
            response.setContentType("text/xml;charset=UTF-8");
        } else if (format == OutputFormat.JSON) {
            response.setContentType("application/json;charset=UTF-8");
        } else {
            response.setContentType("text/html;charset=UTF-8");
        }

        // Get request parameters
        // Call number
        String callno = request.getParameter("callno");
        // Collection code
        String collection = request.getParameter("collection");
        // Language code
        String lang = request.getParameter("lang");
        // Owner code
        String owner = request.getParameter("owner");
        // Status code - available <> not available
        String statusStr = request.getParameter("status");
        boolean status = (statusStr.compareTo("0") == 0 ? true : false);
        // Location id
        String id = request.getParameter("id");

        // LocationHandlerValidationFilter has created "validRequest" attribute
        // that tells if the request contains all the required parameters.
        // If a required parameter is missing, LocationHandlerRequestValidator
        // sets the value to "false".
        boolean valid = (Boolean) request.getAttribute("validRequest");

        // Create writer for writing the output
        PrintWriter out = response.getWriter();
        // Initialize variable for the output string
        String output = "";

        // Fetch "resolver" bean from application context
        Resolver resolver = (Resolver) ApplicationContextUtils.getApplicationContext().getBean("resolver");

        // If the request is not valid, return an error message
        if (valid) {
            // Find Location object matching the given parameters
            output = resolver.resolve(callno, lang, status, owner, collection, format, id);
        } else {
            // Invalid request -> set response status to 400 (Bad request)
            response.setStatus(400);
            // Fetch output generators from application context
            Map<OutputFormat, Generator> generators = (Map<OutputFormat, Generator>) ApplicationContextUtils.getApplicationContext().getBean("generators");
            // Get generator according to the output format
            Generator generator = generators.get(format);
            // If generator exists, generate error message, otherwise an
            // empty string is returned
            if (generator != null) {
                if (errorMessage == null) {
                    // If errorMessage is null, use default message
                    output = generator.generateError("400", "Bad request. Invalid or missing parameters.");
                    logger.warn("\"config.properties\" file is missing \"error.400.message\" property.");
                } else {
                    output = generator.generateError("400", errorMessage);
                }
            } else {
                logger.warn("Failed to generate error message, because no \"" + format + "\" generator is configured. Processing aborted.");
            }
        }
        try {
            // Write output
            out.print(output);
            // Flush stream
            out.flush();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            // Close stream
            out.close();
            // Add SearchEvent to DB
            SearchEventStatisticsQueue.getInstance().put(
                    new SearchEvent(
                    callno,
                    collection,
                    lang,
                    statusStr,
                    getIpAddress(request),
                    SearchEventType.LOCATION_HANDLER,
                    owner,
                    System.currentTimeMillis() - start));
        }
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (request.getHeader("x-forwarded-for") != null) {
            try {
                ip = InetAddress.getByName(request.getHeader("x-forwarded-for")).getHostAddress();
            } catch (UnknownHostException uhe) {
                try {
                    InetAddress[] ips = InetAddress.getAllByName(request.getHeader("x-forwarded-for"));
                    ip = (ips != null && ips.length > 0) ? ips[0].getHostAddress() : "";
                } catch (UnknownHostException uheAll) {
                    logger.error(uhe);
                    logger.error(uheAll);
                    return "";
                }
            }
        }
        return ip;
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
