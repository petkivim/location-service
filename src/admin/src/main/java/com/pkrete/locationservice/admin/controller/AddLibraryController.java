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

import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.model.owner.Owner;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * The {@link AddLibraryController AddLibraryController} handles the requests
 * that are related to adding a new library to the database. This class extends
 * abstract {@link HandleLocationController HandleLocationController} class.
 *
 * @author Petteri Kivimäki
 */
public class AddLibraryController extends HandleLocationController {

    public AddLibraryController() {
        setCommandClass(Library.class);
        setCommandName("library");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Library library = (Library) command;

        library.setAreas(parseAreas(request, library));

        if (library.getOwner() == null) {
            library.setOwner(getOwner(request));
        }
        library.setCreator(getUser(request).getUsername());
        if(!locationsService.create(library)) {
            throw new Exception("Creating library failed.");
        }
        
        return new ModelAndView("redirect:locations.htm");
    }

    @Override
    /* Initialize the command object */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Owner owner = getOwner(request);
        Library lib = new Library();
        lib.updateLists(owner.getLanguages());
        lib.setOwner(owner);
        return lib;
    }
}
