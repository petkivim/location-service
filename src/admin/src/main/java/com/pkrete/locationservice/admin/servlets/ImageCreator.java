/**
 * This file is part of Location Service :: Admin. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Admin is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Admin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Admin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.admin.servlets;

import com.pkrete.locationservice.admin.drawer.Drawer;
import com.pkrete.locationservice.admin.model.location.Area;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.location.Location;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.List;
import com.pkrete.locationservice.admin.model.illustration.Map;
import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.service.MapsService;
import com.pkrete.locationservice.admin.util.ApplicationContextUtils;
import java.util.ArrayList;
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

    private final static Logger logger = LoggerFactory.getLogger(ImageCreator.class.getName());

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
        // Fetch Spring's illustrationsService bean 
        MapsService mapsService = (MapsService) ApplicationContextUtils.getApplicationContext().getBean("mapsService");
        // Fetch Spring's converterService bean 
        ConverterService converterService = (ConverterService) ApplicationContextUtils.getApplicationContext().getBean("converterService");

        User user = (User) request.getSession().getAttribute("user");
        response.setContentType("image/jpeg");
        String lang = "";
        String mapId = request.getParameter("mapId");

        List<Language> languages = user.getOwner().getLanguages();
        if (!languages.isEmpty()) {
            lang = languages.get(0).getCode();
        } else {
            logger.error("Owner \"{}\" doesn't have any languages configured.", user.getOwner());
            return;
        }

        // Parse Area objects from request parameters
        List<Area> areas = new ArrayList<Area>();
        if (request.getParameter("used_areas") != null) {
            if (request.getParameter("used_areas").length() > 0) {
                String[] arr = request.getParameter("used_areas").split("\\|");
                for (int i = 0; i < arr.length; i++) {
                    int x1 = converterService.strToInt(request.getParameter("x1_" + arr[i]));
                    int y1 = converterService.strToInt(request.getParameter("y1_" + arr[i]));
                    int x2 = converterService.strToInt(request.getParameter("x2_" + arr[i]));
                    int y2 = converterService.strToInt(request.getParameter("y2_" + arr[i]));
                    int angle = converterService.strToInt(request.getParameter("angle_" + arr[i]));
                    areas.add(new Area(x1, y1, x2, y2, angle));
                }
            }
        }

        if (validateParams(mapId)) {
            Map map = mapsService.get(converterService.strToInt(mapId), user.getOwner());
            if (map == null) {
                logger.warn("Unable to find a map matching the given id-owner combination.");
                return;
            }
            // If map is a Google Map, there's nothing to do
            if (map.isGoogleMap()) {
                logger.debug("The given Map is a Google Map -> there's nothing to do here : { \"mapId\":{} }", map.getId());
                return;
            }
            try {
                /* Fetch Spring's mapDrawer bean */
                Drawer drawer = (Drawer) ApplicationContextUtils.getApplicationContext().getBean("mapDrawer");
                if (drawer == null) {
                    logger.error("\"mapDrawer\" object could not be found! Check applicationContext.xml.");
                    return;
                }

                // Create an image
                BufferedImage bufferedImage = drawer.draw(map, areas, lang);
                if (bufferedImage != null) {
                    //Write the image as a png
                    ImageIO.write(bufferedImage, "png", response.getOutputStream());
                } else {
                    StringBuilder builder = new StringBuilder();
                    builder.append("Drawing image failed! Parameters: ");
                    builder.append("{\"mapId\":").append(mapId);
                    //builder.append(",\"owner\":\"").append(map.getOwner());
                    builder.append("\",\"lang\":\"").append(lang);
                    builder.append("\"}");
                    logger.warn(builder.toString());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private boolean validateParams(String mapId) {
        if (mapId != null && mapId.length() > 0 && !mapId.equals("-1")) {
            try {
                Integer.parseInt(mapId);
            } catch (NumberFormatException ex) {
                logger.error("Invalid mapId: {}", mapId);
                return false;
            }
            return true;
        }
        logger.error("Invalid mapId: {}", mapId);
        return false;
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
