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
package com.pkrete.locationservice.endpoint.servlets;

import com.pkrete.locationservice.endpoint.generator.Generator;
import com.pkrete.locationservice.endpoint.model.language.Language;
import com.pkrete.locationservice.endpoint.model.owner.Owner;
import com.pkrete.locationservice.endpoint.statistics.SearchEvent;
import com.pkrete.locationservice.endpoint.statistics.SearchEventStatisticsQueue;
import com.pkrete.locationservice.endpoint.statistics.SearchEventType;
import com.pkrete.locationservice.endpoint.model.search.Position;
import com.pkrete.locationservice.endpoint.search.Search;
import com.pkrete.locationservice.endpoint.model.search.SearchType;
import com.pkrete.locationservice.endpoint.converter.ConverterService;
import com.pkrete.locationservice.endpoint.util.ApplicationContextUtils;
import com.pkrete.locationservice.endpoint.util.PropertiesUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet offers an interface for searching and retrieving information
 * from LocationService so that it's possible to fetch data without connecting
 * the database directly. This interface is secure, because only the owner
 * parameter is used for fetching data from the database.
 *
 * The search is implemented by fetching all the locations of the given owner
 * from the database and then searching programmatically Location objects
 * matching the given conditions. The downside of this implementation is that
 * response times get slower when the size of the database increases.
 *
 * The results are returned in XML format.
 *
 * @author Petteri Kivimäki
 */
public class Exporter extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(Exporter.class.getName());
    private static final String errorMessage = PropertiesUtil.getProperty("error.exporter.400.message");
    private static final String accessDeniedMessage = PropertiesUtil.getProperty("error.exporter.401.message");

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get processing starting time
        long start = System.currentTimeMillis();
        // Set response content type
        response.setContentType("text/xml;charset=UTF-8");
        // Get PrintWriter for writing the output
        PrintWriter out = response.getWriter();

        // Get converter service from application context
        ConverterService converter = (ConverterService) ApplicationContextUtils.getApplicationContext().getBean("converter");

        // Initialize variables
        String version = request.getParameter("version");
        String ownerCode = "";
        boolean children = (request.getParameter("children").equals("yes") ? true : false);
        String searchStr = request.getParameter("search");
        SearchType type = (SearchType) converter.convert(request.getParameter("type"), SearchType.class, SearchType.CALLNO);
        Position position = (Position) converter.convert(request.getParameter("position"), Position.class, Position.ANY);
        // Get attributes set by the filters
        boolean authorized = (Boolean) request.getAttribute("authorized");
        // ExporterValidationFilter has created "validRequest" attribute
        // that tells if the request contains all the required parameters.
        // If a required parameter is missing, ExporterRequestValidator
        // sets the value to "false".
        boolean valid = (Boolean) request.getAttribute("validRequest");
        // Initialize variable for the output
        String output = "";
        // Variable that defines the name of the bean that's responsible of the search operation
        String searchBeanName = "exporterSearch";
        // If version is "2", change searchBean's name
        if (version.equals("2")) {
            searchBeanName = "exporterSearchSolr";
        }

        // Get XML generator from application context
        Generator generator = (Generator) ApplicationContextUtils.getApplicationContext().getBean("xmlBatchGenerator");

        // If the request is not valid, return an error message
        if (!valid) {
            // Invalid request -> set response status to 400 (Bad request)
            response.setStatus(400);
            // The request is not valid
            output = generator.generateError("400", errorMessage);
        } else if (!authorized) {
            // Get owner attribute set by the filters
            Owner owner = (Owner) request.getAttribute("owner");
            // Set owner code
            ownerCode = owner.getCode();
            // Not authorized -> set response status to 401 (Access denied)
            response.setStatus(401);
            // The user is not authorized to access the servlet
            output = generator.generateError("401", accessDeniedMessage);
        } else {
            // The user is authorized to access the interface.
            // Get owner attribute set by the filters
            Owner owner = (Owner) request.getAttribute("owner");
            // Set owner code
            ownerCode = owner.getCode();
            // Get languages related to the current owner
            List<Language> languages = owner.getLanguages();
            // Get Search object from application context
            Search searcher = (Search) ApplicationContextUtils.getApplicationContext().getBean(searchBeanName);
            // Search locations matching the conditions
            List results = searcher.search(searchStr, position, type, owner, children);
            logger.debug("Exporting {} locations. Owner : \"{}\".", results.size(), owner.getCode());
            // Generate output
            output = generator.generateBatchOutput(results, languages, children);
        }

        try {
            // Send the response
            out.println(output);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            out.close();
            // Create new SearchEvent for the statistics
            SearchEventStatisticsQueue.getInstance().put(
                    new SearchEvent(
                            searchStr,
                            type.toString(),
                            position.toString(),
                            authorized,
                            getIpAddress(request),
                            SearchEventType.EXPORTER,
                            ownerCode,
                            System.currentTimeMillis() - start));
        }
    }

    /**
     * Returns the IP address of the user. If the IP address for some reason can
     * not delivered, an empty string is returned.
     *
     * @param request http request object
     * @return IP address of the user or an empty string
     */
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
                    logger.error(uhe.getMessage());
                    logger.error(uheAll.getMessage());
                    return "";
                }
            }
        }
        return ip;
    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
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
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
