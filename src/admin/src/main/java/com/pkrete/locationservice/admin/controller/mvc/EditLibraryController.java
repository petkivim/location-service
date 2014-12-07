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

import com.pkrete.locationservice.admin.exception.ObjectNotFoundException;
import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.util.Settings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * The {@link EditLibraryController EditLibraryController} handles the requests
 * that are related to editing a library object and then saving it to the
 * database. This class extends abstract
 * {@link HandleLocationController HandleLocationController} class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/editlibrary.htm")
@SessionAttributes("library")
public class EditLibraryController extends HandleLocationController {

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
            this.setReferenceData(request, model);
            return new ModelAndView("edit_library", model);
        }

        library.setUpdater(getUser(request).getUsername());
        if (!locationsService.update(library)) {
            throw new Exception("Updating library failed.");
        }
        return new ModelAndView("redirect:locations.htm?select_library=" + request.getParameter("select_library"));
    }

    @Override
    protected void setReferenceData(HttpServletRequest request, ModelMap modelMap) {
        Owner owner = getOwner(request);
        modelMap.put("images", imagesService.get(owner));
        modelMap.put("maps", mapsService.get(owner));
        String path = Settings.getInstance().getImagesWebPath(getOwner(request).getCode());
        modelMap.put("imagesPath", path);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) throws Exception {
        String id = request.getParameter("select_library");
        Owner owner = getOwner(request);
        Library lib = locationsService.getLibrary(this.converterService.strToInt(id), owner);
        if (lib == null) {
            throw new ObjectNotFoundException(new StringBuilder("Library not found. {\"id\":").append(id).append(",\"owner\":\"").append(owner.getCode()).append("\"}").toString());
        }
        lib.updateLists(owner.getLanguages());
        model.put("library", lib);
        this.setReferenceData(request, model);
        return "edit_library";
    }
}
