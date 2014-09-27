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

import com.pkrete.locationservice.admin.editor.LibraryEditor;
import com.pkrete.locationservice.admin.exception.ObjectNotFoundException;
import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.model.location.LibraryCollection;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * The {@link EditCollectionController EditCollectionController} handles the requests
 * that are related to editing a collection object and then saving it to the database.
 * This class extends abstract {@link HandleLocationController HandleLocationController} class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/editcollection.htm")
@SessionAttributes("collection")
public class EditCollectionController extends HandleLocationController {

    @Autowired
    @Qualifier("libraryEditor")
    private LibraryEditor libraryEditor;
    @Autowired
    @Qualifier("collectionValidatorMVC")
    private Validator validator;

    @InitBinder
    @Override
    protected void initBinder(WebDataBinder binder) {
        super.initBinder(binder);
        binder.registerCustomEditor(Library.class, libraryEditor);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("collection") LibraryCollection collection,
            BindingResult result) throws Exception {
        
        collection.setAreas(parseAreas(request, collection));
        super.parseSubjectMatters(request, collection);
        validator.validate(collection, result);

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            this.setReferenceData(request, model);
            return new ModelAndView("edit_collection", model);
        }

        String libraryId = request.getParameter("select_library");
        String collectionId = request.getParameter("select_collection");     
        collection.setUpdater(getUser(request).getUsername());
        if (!locationsService.update(collection)) {
            throw new Exception("Updating collection failed.");
        }

        return new ModelAndView("redirect:locations.htm?select_library=" + libraryId + "&select_collection=" + collectionId);
    }

    @Override
    protected void setReferenceData(HttpServletRequest request, ModelMap modelMap) {
        Owner owner = getOwner(request);
        modelMap.put("libraries", locationsService.getlLibraries(owner));
        modelMap.put("images", imagesService.get(owner));
        modelMap.put("maps", mapsService.get(owner));
        modelMap.put("subjects", subjectMattersService.getSubjectMatters(owner));
        String path = Settings.getInstance().getImagesWebPath(getOwner(request).getCode());
        modelMap.put("imagesPath", path);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) throws Exception {
        String id = request.getParameter("select_collection");
        Owner owner = getOwner(request);
        /* Get the current user. */
        LibraryCollection collection = locationsService.getCollection(this.converterService.strToInt(id), owner);
        if (collection == null) {
            throw new ObjectNotFoundException(new StringBuilder("Collection not found. {\"id\":").append(id).append(",\"owner\":\"").append(owner.getCode()).append("\"}").toString());
        }
        collection.updateLists(owner.getLanguages());
        model.put("collection", collection);
        this.setReferenceData(request, model);
        return "edit_collection";
    }
}
