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

import com.pkrete.locationservice.admin.model.owner.LocatingStrategy;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.service.OwnersService;
import com.pkrete.locationservice.admin.service.UsersService;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The {@link AddOwnerController AddOwnerController} handles the requests
 * that are related to adding a new owner to the database. This class extends
 * SimpleFormController class.
 *
 * @author Petteri Kivimäki
 */
public class AddOwnerController extends SimpleFormController {

    private OwnersService ownersService;
    private UsersService usersService;

    public void setOwnersService(OwnersService ownersService) {
        this.ownersService = ownersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public AddOwnerController() {
        setCommandClass(Owner.class);
        setCommandName("owner");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Owner owner = (Owner) command;
        owner.setCreator(getUser(request).getUsername());
        if(!ownersService.create(owner)) {
            throw new Exception("Creating owner failed.");
        }
        return new ModelAndView("redirect:userowner.htm");
    }

    @Override
    /* Initialize the command object */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new Owner();
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
