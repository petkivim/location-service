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
package com.pkrete.locationservice.admin.servlets;

import com.pkrete.locationservice.admin.generator.Generator;
import com.pkrete.locationservice.admin.model.search.SearchLevel;
import com.pkrete.locationservice.admin.search.Searcher;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import com.pkrete.locationservice.admin.util.ApplicationContextUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * This servlet offers an interface for searching and retrieving information
 * from LocationService database so that it's possible to fetch data
 * without connecting the database directly. The results are returned
 * in XML format.
 *
 * @author Petteri Kivimäki
 */
public class Search extends HttpServlet {

    private final static Logger logger = Logger.getLogger(Search.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Fetch Spring's converterService bean 
        ConverterService converterService = (ConverterService) ApplicationContextUtils.getApplicationContext().getBean("converterService");

        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String ownerCode = request.getParameter("owner");
        String searchStr = request.getParameter("search");
        SearchLevel level = (SearchLevel)converterService.convert(request.getParameter("level"), SearchLevel.class, SearchLevel.LIBRARY);

        try {
            // Fetch Spring's searcher bean 
            Searcher searcher = (Searcher) ApplicationContextUtils.getApplicationContext().getBean("searcher");
            // Fetch Spring's generator bean 
            Generator generator = (Generator) ApplicationContextUtils.getApplicationContext().getBean("xmlGenerator");
            // Get search results
            List<SimpleLocation> results = searcher.search(searchStr, ownerCode, level);
            // Generate output
            String output = generator.generate(results);
            // Write output
            out.println(output);

        } catch (Exception e) {
            logger.error(e);
        } finally {
            out.close();
        }
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
