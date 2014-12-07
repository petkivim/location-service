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

import com.pkrete.locationservice.admin.editor.CollectionEditor;
import com.pkrete.locationservice.admin.exception.ObjectNotFoundException;
import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.location.Shelf;
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
 * The {@link EditCollectionController EditCollectionController} handles the
 * requests that are related to editing a collection object and then saving it
 * to the database. This class extends abstract
 * {@link HandleLocationController HandleLocationController} class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/editshelf.htm")
@SessionAttributes("shelf")
public class EditShelfController extends HandleLocationController {

    @Autowired
    @Qualifier("collectionEditor")
    private CollectionEditor collectionEditor;
    @Autowired
    @Qualifier("shelfValidatorMVC")
    private Validator validator;

    @InitBinder
    @Override
    protected void initBinder(WebDataBinder binder) {
        super.initBinder(binder);
        binder.registerCustomEditor(LibraryCollection.class, collectionEditor);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("shelf") Shelf shelf,
            BindingResult result) throws Exception {

        shelf.setAreas(parseAreas(request, shelf));
        super.parseSubjectMatters(request, shelf);
        validator.validate(shelf, result);

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            this.setReferenceData(request, model);
            return new ModelAndView("edit_shelf", model);
        }

        String libraryId = request.getParameter("select_library");
        String collectionId = request.getParameter("select_collection");
        String shelfId = request.getParameter("select_shelf");
        shelf.setUpdater(getUser(request).getUsername());
        if (!locationsService.update(shelf)) {
            throw new Exception("Updating shelf failed.");
        }

        return new ModelAndView("redirect:locations.htm?"
                + "select_library=" + libraryId
                + "&select_collection=" + collectionId
                + "&select_shelf=" + shelfId);
    }

    @Override
    protected void setReferenceData(HttpServletRequest request, ModelMap modelMap) {
        Owner owner = getOwner(request);
        modelMap.put("collections", locationsService.getCollections(owner));
        modelMap.put("images", imagesService.get(owner));
        modelMap.put("maps", mapsService.get(owner));
        modelMap.put("subjects", subjectMattersService.getSubjectMatters(owner));
        String path = Settings.getInstance().getImagesWebPath(getOwner(request).getCode());
        modelMap.put("imagesPath", path);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) throws Exception {
        String id = request.getParameter("select_shelf");
        Owner owner = getOwner(request);
        Shelf shelf = locationsService.getShelf(this.converterService.strToInt(id), getOwner(request));
        if (shelf == null) {
            throw new ObjectNotFoundException(new StringBuilder("Shelf not found. {\"id\":").append(id).append(",\"owner\":\"").append(owner.getCode()).append("\"}").toString());
        }
        shelf.updateLists(owner.getLanguages());
        model.put("shelf", shelf);
        this.setReferenceData(request, model);
        return "edit_shelf";
    }
}
