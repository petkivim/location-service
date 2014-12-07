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
package com.pkrete.locationservice.admin.controller.mvc;

import com.pkrete.locationservice.admin.model.location.Library;
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
 * The {@link AddLibraryController AddLibraryController} handles the requests
 * that are related to adding a new library to the database. This class extends
 * abstract {@link HandleLocationController HandleLocationController} class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/addlibrary.htm")
@SessionAttributes("library")
public class AddLibraryController extends HandleLocationController {

    @Autowired
    @Qualifier("libraryValidatorMVC")
    private Validator validator;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("library") Library library,
            BindingResult result) throws Exception {

        library.setAreas(parseAreas(request, library));
        validator.validate(library, result);

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            super.setReferenceData(request, model);
            return new ModelAndView("add_library", model);
        }

        library.setCreator(getUser(request).getUsername());
        if (!locationsService.create(library)) {
            throw new Exception("Creating library failed.");
        }

        return new ModelAndView("redirect:locations.htm");
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) {
        Owner owner = getOwner(request);
        Library lib = new Library();
        lib.updateLists(owner.getLanguages());
        lib.setOwner(owner);
        model.put("library", lib);
        super.setReferenceData(request, model);
        return "add_library";
    }
}
