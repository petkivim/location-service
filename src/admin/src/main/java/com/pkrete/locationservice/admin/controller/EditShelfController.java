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

import com.pkrete.locationservice.admin.editor.CollectionEditor;
import com.pkrete.locationservice.admin.exception.ObjectNotFoundException;
import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.location.Shelf;
import com.pkrete.locationservice.admin.util.Settings;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

/**
 * The {@link EditCollectionController EditCollectionController} handles the requests
 * that are related to editing a collection object and then saving it to the database.
 * This class extends abstract {@link HandleLocationController HandleLocationController} class.
 *
 * @author Petteri Kivimäki
 */
public class EditShelfController extends HandleLocationController {

    private CollectionEditor collectionEditor;

    public EditShelfController() {
        setCommandClass(Shelf.class);
        setCommandName("shelf");
    }

    public void setCollectionEditor(CollectionEditor collectionEditor) {
        this.collectionEditor = collectionEditor;
    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        super.initBinder(request, binder);
        binder.registerCustomEditor(LibraryCollection.class, collectionEditor);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Shelf shelf = (Shelf) command;
        String libraryId = request.getParameter("select_library");
        String collectionId = request.getParameter("select_collection");
        String shelfId = request.getParameter("select_shelf");

        shelf.setAreas(parseAreas(request, shelf));
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
    protected java.util.Map referenceData(HttpServletRequest request) throws Exception {
        ModelMap modelMap = new ModelMap();
        Owner owner = getOwner(request);
        modelMap.put("collections", locationsService.getCollections(owner));
        modelMap.put("images", imagesService.get(owner));
        modelMap.put("maps", mapsService.get(owner));
        modelMap.put("subjects", subjectMattersService.getSubjectMatters(owner));
        if (request.getParameter("used_areas") == null) {
            String id = request.getParameter("select_shelf");
            modelMap.put("areas", locationsService.getAreasByLocationId(this.converterService.strToInt(id)));
        } else {
            List areas = parseAreas(request);
            if (areas != null) {
                modelMap.put("areas", areas);
            }
        }
        String path = Settings.getInstance().getImagesWebPath(getOwner(request).getCode());
        modelMap.put("imagesPath", path);
        return modelMap;
    }

    @Override
    /* Initialize the command object */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String id = request.getParameter("select_shelf");
        Owner owner = getOwner(request);
        Shelf shelf = locationsService.getShelf(this.converterService.strToInt(id), getOwner(request));
        if (shelf == null) {
            throw new ObjectNotFoundException(new StringBuilder("Shelf not found. {\"id\":").append(id).append(",\"owner\":\"").append(owner.getCode()).append("\"}").toString());
        }
        shelf.updateLists(owner.getLanguages());
        return shelf;
    }
}
