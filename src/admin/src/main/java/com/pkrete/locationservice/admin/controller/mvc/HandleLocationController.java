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
package com.pkrete.locationservice.admin.controller.mvc;

import com.pkrete.locationservice.admin.editor.ImageEditor;
import com.pkrete.locationservice.admin.editor.LanguageEditor;
import com.pkrete.locationservice.admin.editor.MapEditor;
import com.pkrete.locationservice.admin.editor.OwnerEditor;
import com.pkrete.locationservice.admin.editor.SubjectMatterEditor;
import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.illustration.Map;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.model.location.Area;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.service.ImagesService;
import com.pkrete.locationservice.admin.service.LanguagesService;
import com.pkrete.locationservice.admin.service.LocationsService;
import com.pkrete.locationservice.admin.service.MapsService;
import com.pkrete.locationservice.admin.service.SubjectMattersService;
import com.pkrete.locationservice.admin.util.Settings;
import java.util.ArrayList;
import java.util.List;
import org.springframework.ui.ModelMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

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
@Controller
public abstract class HandleLocationController extends BaseController {

    @Autowired
    @Qualifier("locationsService")
    protected LocationsService locationsService;
    @Autowired
    @Qualifier("imagesService")
    protected ImagesService imagesService;
    @Autowired
    @Qualifier("mapsService")
    protected MapsService mapsService;
    @Autowired
    @Qualifier("subjectMattersService")
    protected SubjectMattersService subjectMattersService;
    @Autowired
    @Qualifier("languagesService")
    protected LanguagesService languagesService;
    @Autowired
    @Qualifier("converterService")
    protected ConverterService converterService;
    @Autowired
    @Qualifier("imageEditor")
    private ImageEditor imageEditor;
    @Autowired
    @Qualifier("mapEditor")
    private MapEditor mapEditor;
    @Autowired
    @Qualifier("subjectMatterEditor")
    private SubjectMatterEditor subjectMatterEditor;
    @Autowired
    @Qualifier("languageEditor")
    private LanguageEditor languageEditor;
    @Autowired
    @Qualifier("ownerEditor")
    private OwnerEditor ownerEditor;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Image.class, imageEditor);
        binder.registerCustomEditor(Map.class, mapEditor);
        binder.registerCustomEditor(SubjectMatter.class, subjectMatterEditor);
        binder.registerCustomEditor(Language.class, languageEditor);
        binder.registerCustomEditor(Owner.class, ownerEditor);
    }

    protected void setReferenceData(HttpServletRequest request, ModelMap model) {
        Owner owner = getOwner(request);
        model.put("images", imagesService.get(owner));
        model.put("maps", mapsService.get(owner));
        model.put("subjects", subjectMattersService.getSubjectMatters(owner));
        String path = Settings.getInstance().getImagesWebPath(getOwner(request).getCode());
        model.put("imagesPath", path);
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

    /**
     * Parses SubjectMatters objects from the Request object and sets them
     * to the given Location.
     * @param request request with the necessary parameters
     * @param location Location that owns the SubjectMatters
     */
    protected void parseSubjectMatters(HttpServletRequest request, Location location) {
        String[] values = request.getParameterValues("subjectMatters");
        List<SubjectMatter> subjects = new ArrayList<SubjectMatter>();
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                subjects.add(this.subjectMattersService.getSubjectMatter(this.converterService.strToInt(values[i]), location.getOwner()));
            }
        }
        location.setSubjectMatters(subjects);
    }
}
