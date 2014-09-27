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

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.location.Shelf;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * The {@link AddShelfController AddShelfController} handles the requests
 * that are related to adding a new shelf to the database. This class extends
 * abstract {@link HandleLocationController HandleLocationController} class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/addshelf.htm")
@SessionAttributes("shelf")
public class AddShelfController extends HandleLocationController {

    @Autowired
    @Qualifier("shelfValidatorMVC")
    private Validator validator;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("shelf") Shelf shelf,
            BindingResult result) throws Exception {

        shelf.setAreas(parseAreas(request, shelf));
        super.parseSubjectMatters(request, shelf);
        validator.validate(shelf, result);

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            super.setReferenceData(request, model);
            return new ModelAndView("add_shelf", model);
        }

        String libraryId = request.getParameter("select_library");
        String collectionId = request.getParameter("select_collection");
        shelf.setCreator(getUser(request).getUsername());
        if (!locationsService.create(shelf)) {
            throw new Exception("Creating shelf failed.");
        }

        return new ModelAndView("redirect:locations.htm?"
                + "select_library=" + libraryId
                + "&select_collection=" + collectionId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) {
        String libraryId = request.getParameter("select_library");
        String collectionId = request.getParameter("select_collection");
        Owner owner = getOwner(request);
        Shelf shelf = new Shelf();
        shelf.updateLists(owner.getLanguages());
        shelf.setOwner(owner);
        shelf.setCollection(this.locationsService.getCollection(this.converterService.strToInt(collectionId), this.converterService.strToInt(libraryId), owner));
        model.addAttribute("shelf", shelf);
        super.setReferenceData(request, model);
        return "add_shelf";
    }
}
