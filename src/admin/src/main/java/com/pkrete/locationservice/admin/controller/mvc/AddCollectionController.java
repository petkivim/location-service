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

import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.model.owner.Owner;
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
 * The {@link AddCollectionController AddCollectionController} handles the requests
 * that are related to adding a new collection to the database. This class extends
 * abstract {@link HandleLocationController HandleLocationController} class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/addcollection.htm")
@SessionAttributes("collection")
public class AddCollectionController extends HandleLocationController {

    @Autowired
    @Qualifier("collectionValidatorMVC")
    private Validator validator;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("collection") LibraryCollection collection,
            BindingResult result) throws Exception {

        collection.setAreas(parseAreas(request, collection));
        super.parseSubjectMatters(request, collection);
        validator.validate(collection, result);

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            super.setReferenceData(request, model);
            return new ModelAndView("add_collection", model);
        }

        String libraryId = request.getParameter("select_library");      
        collection.setCreator(getUser(request).getUsername());
        if (!locationsService.create(collection)) {
            throw new Exception("Creating collection failed.");
        }

        return new ModelAndView("redirect:locations.htm?select_library=" + libraryId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) {
        Owner owner = getOwner(request);
        String libraryId = request.getParameter("select_library");
        LibraryCollection collection = new LibraryCollection();
        collection.updateLists(owner.getLanguages());
        collection.setOwner(owner);
        collection.setLibrary(locationsService.getLibrary(this.converterService.strToInt(libraryId), getOwner(request)));
        model.put("collection", collection);
        super.setReferenceData(request, model);
        return "add_collection";
    }
}
