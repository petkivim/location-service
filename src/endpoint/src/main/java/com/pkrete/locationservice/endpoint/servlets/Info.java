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

import com.pkrete.locationservice.endpoint.service.Service;
import com.pkrete.locationservice.endpoint.solr.service.LocationIndexService;
import com.pkrete.locationservice.endpoint.util.ApplicationContextUtils;
import com.pkrete.locationservice.endpoint.util.PropertiesUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet is purely for maintenance purposes. It provides information
 * about the state of the system and it can by used for controlling that
 * the system is up and running.
 * 
 * @author Petteri Kivimäki
 */
public class Info extends HttpServlet {

    private static final String version = PropertiesUtil.getProperty("service.version");

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get processing starting time
        long start = System.currentTimeMillis();
        response.setContentType("application/xml;charset=UTF-8");
        PrintWriter out = response.getWriter();
        // Variable for status
        String dbStatus = "OK";
        int dbCode = 200;
        String solrStatus = "OK";
        int solrCode = 200;

        // Get dbService service from application context
        Service service = (Service) ApplicationContextUtils.getApplicationContext().getBean("dbService");

        // Test db connection
        if (!service.testDbConnection()) {
            dbStatus = "Database Error";
            response.setStatus(500);
            dbCode = 500;
        }

        // Get locationIndexService service from application context
        LocationIndexService indexService = (LocationIndexService) ApplicationContextUtils.getApplicationContext().getBean("locationIndexService");
        if (indexService != null && !indexService.testConnection()) {
            solrStatus = "Solr Error";
            response.setStatus(500);
            solrCode = 500;
        }

        try {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<locationservice>");
            out.print("<version>");
            out.print(version);
            out.println("</version>");
            out.print("<dbstatus>");
            out.print(dbStatus);
            out.println("</dbstatus>");
            out.print("<dbcode>");
            out.print(dbCode);
            out.println("</dbcode>");
            if (indexService != null) {
                out.print("<solrstatus>");
                out.print(solrStatus);
                out.println("</solrstatus>");
                out.print("<solrcode>");
                out.print(solrCode);
                out.println("</solrcode>");
            } else {
                out.print("<solrstatus />");
                out.print("<solrcode />");
            }
            out.print("<processingtime>");
            out.print(System.currentTimeMillis() - start);
            out.println("</processingtime>");
            out.println("</locationservice>");
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
