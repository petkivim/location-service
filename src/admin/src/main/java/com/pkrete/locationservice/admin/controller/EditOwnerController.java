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
import com.pkrete.locationservice.admin.model.owner.LocatingStrategy;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.service.OwnersService;
import com.pkrete.locationservice.admin.service.UsersService;
import org.springframework.web.servlet.mvc.SimpleFormController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;

/**
 * The {@link EditOwnerController EditOwnerController} handles the requests
 * that are related to editing owner objects. This class extends 
 * SimpleFormController class.
 *
 * @author Petteri Kivimäki
 */
public class EditOwnerController extends SimpleFormController {

    private OwnersService ownersService;
    private UsersService usersService;
    private ConverterService converterService;

    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    public void setOwnersService(OwnersService ownersService) {
        this.ownersService = ownersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public EditOwnerController() {
        setCommandClass(Owner.class);
        setCommandName("owner");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Owner owner = (Owner) command;
        String ownerId = request.getParameter("select_owner");
        owner.setUpdater(getUser(request).getUsername());
        if(!ownersService.update(owner)) {
            throw new Exception("Updating owner failed.");
        }
        return new ModelAndView("redirect:userowner.htm?select_owner=" + ownerId);
    }

    @Override
    /* Initialize the command object */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String id = request.getParameter("select_owner");
        Owner owner = ownersService.getFullOwner(this.converterService.strToInt(id));
        if (owner == null) {
            throw new ObjectNotFoundException(new StringBuilder("Owner not found. {\"id\":").append(id).append("}").toString());
        }
        owner.prepareLists();
        return owner;
    }

    @Override
    protected java.util.Map referenceData(HttpServletRequest request) throws Exception {
        ModelMap modelMap = new ModelMap();
        modelMap.put("strategies", LocatingStrategy.values());
        return modelMap;
    }

    protected User getUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = usersService.getUser(request.getRemoteUser());
            session.setAttribute("user", user);
        }
        return user;
    }
}
