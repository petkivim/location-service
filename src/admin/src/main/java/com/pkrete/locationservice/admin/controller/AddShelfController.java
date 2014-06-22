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

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.location.Shelf;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * The {@link AddShelfController AddShelfController} handles the requests
 * that are related to adding a new shelf to the database. This class extends
 * abstract {@link HandleLocationController HandleLocationController} class.
 *
 * @author Petteri Kivimäki
 */
public class AddShelfController extends HandleLocationController {

    public AddShelfController() {
        setCommandClass(Shelf.class);
        setCommandName("shelf");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Shelf shelf = (Shelf) command;
        String libraryId = request.getParameter("select_library");
        String collectionId = request.getParameter("select_collection");
        /* Get the current user. */
        shelf.setCollection(locationsService.getCollection(this.converterService.strToInt(collectionId), getOwner(request)));
        shelf.setOwner(getOwner(request));

        shelf.setAreas(parseAreas(request, shelf));
        shelf.setCreator(getUser(request).getUsername());
        if (!locationsService.create(shelf)) {
            throw new Exception("Creating shelf failed.");
        }

        return new ModelAndView("redirect:locations.htm?"
                + "select_library=" + libraryId
                + "&select_collection=" + collectionId);
    }

    @Override
    /* Initialize the command object */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String libraryId = request.getParameter("select_library");
        String collectionId = request.getParameter("select_collection");
        Owner owner = getOwner(request);
        Shelf shelf = new Shelf();
        shelf.updateLists(owner.getLanguages());
        shelf.setOwner(owner);
        shelf.setCollection(this.locationsService.getCollection(this.converterService.strToInt(collectionId), this.converterService.strToInt(libraryId), owner));
        return shelf;
    }
}
