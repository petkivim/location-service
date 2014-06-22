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

import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.model.owner.Owner;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * The {@link AddCollectionController AddCollectionController} handles the requests
 * that are related to adding a new collection to the database. This class extends
 * abstract {@link HandleLocationController HandleLocationController} class.
 *
 * @author Petteri Kivimäki
 */
public class AddCollectionController extends HandleLocationController {

    public AddCollectionController() {
        setCommandClass(LibraryCollection.class);
        setCommandName("collection");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        LibraryCollection collection = (LibraryCollection) command;
        String libraryId = request.getParameter("select_library");
        collection.setAreas(parseAreas(request, collection));
        collection.setCreator(getUser(request).getUsername());
        if(!locationsService.create(collection)) {
            throw new Exception("Creating collection failed.");
        }

        return new ModelAndView("redirect:locations.htm?select_library=" + libraryId);
    }

    @Override
    /* Initialize the command object */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Owner owner = getOwner(request);
        String libraryId = request.getParameter("select_library");
        LibraryCollection collection = new LibraryCollection();
        collection.updateLists(owner.getLanguages());
        collection.setOwner(owner);     
        collection.setLibrary(locationsService.getLibrary(this.converterService.strToInt(libraryId), getOwner(request)));
        return collection;
    }
}
