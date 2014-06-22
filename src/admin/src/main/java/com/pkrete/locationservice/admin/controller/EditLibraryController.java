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

import com.pkrete.locationservice.admin.exception.ObjectNotFoundException;
import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.util.Settings;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * The {@link EditLibraryController EditLibraryController} handles the requests
 * that are related to editing a library object and then saving it to the database.
 * This class extends abstract {@link HandleLocationController HandleLocationController} class.
 *
 * @author Petteri Kivimäki
 */
public class EditLibraryController extends HandleLocationController {

    public EditLibraryController() {
        setCommandClass(Library.class);
        setCommandName("library");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Library library = (Library) command;

        if (library.getOwner() == null) {
            library.setOwner(getOwner(request));
        }

        library.setAreas(parseAreas(request, library));
        library.setUpdater(getUser(request).getUsername());
        if (!locationsService.update(library)) {
            throw new Exception("Updating library failed.");
        }
        return new ModelAndView("redirect:locations.htm?select_library=" + request.getParameter("select_library"));
    }

    @Override
    protected java.util.Map referenceData(HttpServletRequest request) throws Exception {
        ModelMap modelMap = new ModelMap();
        Owner owner = getOwner(request);
        modelMap.put("images", imagesService.get(owner));
        modelMap.put("maps", mapsService.get(owner));

        if (request.getParameter("used_areas") == null) {
            String id = request.getParameter("select_library");
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
        String id = request.getParameter("select_library");
        Owner owner = getOwner(request);
        Library lib = locationsService.getLibrary(this.converterService.strToInt(id), owner);
        if (lib == null) {
            throw new ObjectNotFoundException(new StringBuilder("Library not found. {\"id\":").append(id).append(",\"owner\":\"").append(owner.getCode()).append("\"}").toString());
        }
        lib.updateLists(owner.getLanguages());
        return lib;
    }
}
