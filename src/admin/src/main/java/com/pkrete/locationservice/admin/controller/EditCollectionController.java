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

import com.pkrete.locationservice.admin.editor.LibraryEditor;
import com.pkrete.locationservice.admin.exception.ObjectNotFoundException;
import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.model.owner.Owner;
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
public class EditCollectionController extends HandleLocationController {

    private LibraryEditor libraryEditor;

    public EditCollectionController() {
        setCommandClass(LibraryCollection.class);
        setCommandName("collection");
    }

    public void setLibraryEditor(LibraryEditor libraryEditor) {
        this.libraryEditor = libraryEditor;
    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        super.initBinder(request, binder);
        binder.registerCustomEditor(Library.class, libraryEditor);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        LibraryCollection collection = (LibraryCollection) command;
        String libraryId = request.getParameter("select_library");
        String collectionId = request.getParameter("select_collection");
        
        collection.setAreas(parseAreas(request, collection));
        collection.setUpdater(getUser(request).getUsername());
        if(!locationsService.update(collection)) {
            throw new Exception("Updating collection failed.");
        }
        
        return new ModelAndView("redirect:locations.htm?select_library=" + libraryId + "&select_collection=" + collectionId);
    }

    @Override
    protected java.util.Map referenceData(HttpServletRequest request) throws Exception {
        ModelMap modelMap = new ModelMap();
        Owner owner = getOwner(request);
        modelMap.put("libraries", locationsService.getlLibraries(owner));
        modelMap.put("images", imagesService.get(owner));
        modelMap.put("maps", mapsService.get(owner));
        modelMap.put("subjects", subjectMattersService.getSubjectMatters(owner));
        if (request.getParameter("used_areas") == null) {
            String id = request.getParameter("select_collection");
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
        String id = request.getParameter("select_collection");
        Owner owner = getOwner(request);
        /* Get the current user. */
        LibraryCollection collection = locationsService.getCollection(this.converterService.strToInt(id), owner);
        if (collection == null) {
            throw new ObjectNotFoundException(new StringBuilder("Collection not found. {\"id\":").append(id).append(",\"owner\":\"").append(owner.getCode()).append("\"}").toString());
        }
        collection.updateLists(owner.getLanguages());
        return collection;
    }
}
