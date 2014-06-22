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
package com.pkrete.locationservice.admin.controller;

import com.pkrete.locationservice.admin.model.location.Area;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.util.Settings;
import java.util.ArrayList;
import java.util.List;
import org.springframework.ui.ModelMap;
import javax.servlet.http.HttpServletRequest;

/** 
 * The {@link HandleLocationController HandleLocationController} class extends
 * abstract {@link BaseController BaseController} class. This class implements
 * some basic functionality that all the controllers handling Location objects
 * are sharing. This is an abstract base class that controllers handling
 * Location objects can extend. This class doesn't have any abstract methods
 * though.
 *
 * @author Petteri Kivimäki
 */
public abstract class HandleLocationController extends BaseController {

    @Override
    protected java.util.Map referenceData(HttpServletRequest request) throws Exception {
        ModelMap modelMap = new ModelMap();

        Owner owner = getOwner(request);
        modelMap.put("images", imagesService.get(owner));
        modelMap.put("maps", mapsService.get(owner));
        modelMap.put("subjects", subjectMattersService.getSubjectMatters(owner));

        List areas = parseAreas(request);
        if (areas != null) {
            modelMap.put("areas", areas);
        }

        String path = Settings.getInstance().getImagesWebPath(getOwner(request).getCode());
        modelMap.put("imagesPath", path);
        return modelMap;
    }

    /**
     * Parses Area object from the Request object.
     * @param request request with the necessary parameters
     * @return list of Area objects parsed from the request
     */
    protected List<Area> parseAreas(HttpServletRequest request) {
        return parseAreas(request, null);
    }

    /**
     * Parses Area object from the Request object.
     * @param request request with the necessary parameters
     * @param location Location that owns the Areas
     * @return list of Area objects parsed from the request
     */
    protected List<Area> parseAreas(HttpServletRequest request, Location location) {
        List<Area> areas = new ArrayList<Area>();
        if (request.getParameter("used_areas") != null) {
            if (request.getParameter("used_areas").length() > 0) {
                String[] arr = request.getParameter("used_areas").split("\\|");
                for (int i = 0; i < arr.length; i++) {
                    int id = this.converterService.strToInt(request.getParameter("areaId_" + arr[i]));
                    int x1 = this.converterService.strToInt(request.getParameter("x1_" + arr[i]));
                    int y1 = this.converterService.strToInt(request.getParameter("y1_" + arr[i]));
                    int x2 = this.converterService.strToInt(request.getParameter("x2_" + arr[i]));
                    int y2 = this.converterService.strToInt(request.getParameter("y2_" + arr[i]));
                    int angle = this.converterService.strToInt(request.getParameter("angle_" + arr[i]));
                    if (location == null) {
                        areas.add(new Area(id, x1, y1, x2, y2, angle));
                    } else {
                        areas.add(new Area(id, x1, y1, x2, y2, angle, location));
                    }
                }
            }
        }
        return areas;
    }
}
