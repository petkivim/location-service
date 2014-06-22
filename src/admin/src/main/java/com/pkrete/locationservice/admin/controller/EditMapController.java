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
import com.pkrete.locationservice.admin.model.illustration.Map;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.util.Settings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * The {@link EditMapController EditMapController} handles the requests
 * that are related to editing an map object and then saving it to the database.
 * This class extends abstract {@link BaseController BaseLocationController} class.
 *
 * @author Petteri Kivimäki
 */
public class EditMapController extends BaseController {

    public EditMapController() {
        setCommandClass(Map.class);
        setCommandName("map");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Map map = (Map) command;
        String mapId = request.getParameter("mapId");
        // Set update operator
        map.setUpdater(getUser(request).getUsername());
        // Update map
        if(!mapsService.update(map)) {
            throw new Exception("Updating map failed.");
        }
        return new ModelAndView("redirect:illustrations.htm?select_map=" + mapId);
    }

    @Override
    protected java.util.Map referenceData(HttpServletRequest request) throws Exception {
        ModelMap modelMap = new ModelMap();
        Owner owner = getOwner(request);
        String id = request.getParameter("mapId");
        Map map = mapsService.get(this.converterService.strToInt(id), owner);
        modelMap.put("external", map.getIsExternal());
        modelMap.put("languages", Language.toMap(owner.getLanguages()));
        String path = "";
        if (!map.getIsExternal()) {
            path = Settings.getInstance().getMapsWebPath(getOwner(request).getCode());
        }
        modelMap.put("mapsPath", path);
        return modelMap;
    }

    @Override
    /* Initialize the command object */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String id = request.getParameter("mapId");
        Owner owner = getOwner(request);
        Map map = mapsService.get(this.converterService.strToInt(id), owner);
        if (map == null) {
            throw new ObjectNotFoundException(new StringBuilder("Map not found. {\"id\":").append(id).append(",\"owner\":\"").append(owner.getCode()).append("\"}").toString());
        }
        if (map.getIsExternal()) {
            map.setUrl(map.getPath());
        } else {
            //map.setFilePath(map.getPath());
            map.createFilesMap(owner.getLanguages());
        }
        map.setOwner(owner);
        return map;
    }
}
