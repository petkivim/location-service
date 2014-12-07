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

import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.exception.ObjectNotFoundException;
import com.pkrete.locationservice.admin.model.illustration.Map;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.service.MapsService;
import com.pkrete.locationservice.admin.util.Settings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * The {@link EditMapController EditMapController} handles the requests that are
 * related to editing an map object and then saving it to the database. This
 * class extends abstract {@link BaseController BaseLocationController} class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/editmap.htm")
@SessionAttributes("map")
public class EditMapController extends BaseController {

    @Autowired
    @Qualifier("mapsService")
    private MapsService mapsService;
    @Autowired
    @Qualifier("mapValidator")
    private Validator validator;
    @Autowired
    @Qualifier("converterService")
    private ConverterService converterService;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("map") Map map,
            BindingResult result) throws Exception {

        validator.validate(map, result);

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            model.put("languages", Language.toMap(getOwner(request).getLanguages()));
            model.put("external", map.getIsExternal());
            model.put("mapsPath", map.getIsExternal() ? "" : Settings.getInstance().getMapsWebPath(getOwner(request).getCode()));
            return new ModelAndView("edit_map", model);
        }

        String mapId = request.getParameter("mapId");
        // Set update operator
        map.setUpdater(getUser(request).getUsername());
        // Update map
        if (!mapsService.update(map)) {
            throw new Exception("Updating map failed.");
        }
        return new ModelAndView("redirect:illustrations.htm?select_map=" + mapId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) throws Exception {
        // Form backing object
        String id = request.getParameter("mapId");
        Owner owner = getOwner(request);
        Map map = mapsService.get(this.converterService.strToInt(id), owner);
        if (map == null) {
            throw new ObjectNotFoundException(new StringBuilder("Map not found. {\"id\":").append(id).append(",\"owner\":\"").append(owner.getCode()).append("\"}").toString());
        }
        if (map.getIsExternal()) {
            map.setUrl(map.getPath());
        } else {
            map.createFilesMap(owner.getLanguages());
        }
        map.setOwner(owner);
        model.put("map", map);
        // Other reference data
        model.put("languages", Language.toMap(owner.getLanguages()));
        model.put("external", map.getIsExternal());
        model.put("mapsPath", map.getIsExternal() ? "" : Settings.getInstance().getMapsWebPath(owner.getCode()));
        return "edit_map";
    }
}
