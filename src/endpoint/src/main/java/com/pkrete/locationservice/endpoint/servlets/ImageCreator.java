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

import com.pkrete.locationservice.endpoint.drawer.Drawer;
import com.pkrete.locationservice.endpoint.model.location.Location;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.image.*;
import javax.imageio.*;
import com.pkrete.locationservice.endpoint.service.Service;
import com.pkrete.locationservice.endpoint.util.ApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ImageCreator servlet class draws the location of the given
 * {@link Location Location} object on the map and returns the map as a png
 * image.
 *
 * @author Petteri Kivimäki
 */
public class ImageCreator extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ImageCreator.class.getName());

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
        /* Fetch Spring's dbService bean */
        Service dbService = (Service) ApplicationContextUtils.getApplicationContext().getBean("dbService");

        // Set response content type
        response.setContentType("image/jpeg");
        // Initialize variables
        String lang = request.getParameter("lang");
        String locationId = request.getParameter("locationId");
        String owner = request.getParameter("owner");
        // ImageCreatorValidationFilter has created "validRequest" attribute
        // that tells if the request contains all the required parameters.
        // If a required parameter is missing, ExporterRequestValidator
        // sets the value to "false".
        boolean valid = (Boolean) request.getAttribute("validRequest");

        // If the request is not valid, stop processing and exit
        if (!valid) {
            return;
        }

        // Get the Location object from the DB
        Location location = dbService.getLocation(locationId, owner);

        try {
            /* Fetch Spring's mapDrawer bean */
            Drawer drawer = (Drawer) ApplicationContextUtils.getApplicationContext().getBean("mapDrawer");
            if (drawer == null) {
                logger.error("\"mapDrawer\" object could not be found! Check applicationContext.xml.");
                return;
            }

            // Create an image
            BufferedImage bufferedImage = drawer.draw(location, lang);

            if (bufferedImage != null) {
                //Write the image as a png
                ImageIO.write(bufferedImage, "png", response.getOutputStream());
            } else {
                logger.warn("Drawing image failed! Parameters: {\"locationId\":{},\"owner\":\"{}\",\"lang\":\"{}\"}", locationId, owner, lang);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
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
