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

import com.pkrete.locationservice.admin.exception.ObjectNotFoundException;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.service.OwnersService;
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
 * The {@link EditSettingsController EditSettingsController} handles the
 * requests that are related to editing an owner object and then saving it to
 * the database. This class extends BaseController class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/editsettings.htm")
@SessionAttributes("owner")
public class EditSettingsController extends BaseController {

    @Autowired
    @Qualifier("ownersService")
    private OwnersService ownersService;
    @Autowired
    @Qualifier("ownerValidator")
    private Validator validator;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("owner") Owner owner,
            BindingResult result) throws Exception {

        if (getUser(request).getOwner().getId() != owner.getId()) {
            return new ModelAndView("redirect:index.htm");
        }

        validator.validate(owner, result);

        if (result.hasErrors()) {
            return new ModelAndView("edit_settings", new ModelMap());
        }

        owner.setUpdater(getUser(request).getUsername());
        if (!ownersService.update(owner)) {
            throw new Exception("Updating settings failed.");
        }
        return new ModelAndView("redirect:editsettings.htm?success=true");
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) throws Exception {
        int id = getUser(request).getOwner().getId();
        Owner owner = ownersService.getFullOwner(id);
        if (owner == null) {
            throw new ObjectNotFoundException(new StringBuilder("Owner not found. {\"id\":").append(id).append("}").toString());
        }
        owner.prepareLists();
        model.put("owner", owner);

        return "edit_settings";
    }
}
