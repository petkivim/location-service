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

import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.model.user.UserFull;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.model.user.UserInfo;
import com.pkrete.locationservice.admin.service.UsersService;
import com.pkrete.locationservice.admin.util.UsersUtil;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The {@link LocalAdminAddUserController LocalAdminAddUserController} handles 
 * the requests that are related to adding a new user to the database. 
 * This class extends SimpleFormController class.
 *
 * @author Petteri Kivimäki
 */
public class LocalAdminAddUserController extends SimpleFormController {

    private UsersService usersService;

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public LocalAdminAddUserController() {
        setCommandClass(UserInfo.class);
        setCommandName("user");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        UserInfo user = (UserInfo) command;
        User currentUser = UsersUtil.getUser(request, usersService);
        user.getUser().setCreator(currentUser.getUsername());
        user.getUser().setOwner(currentUser.getOwner());
        user.setGroup(UserGroup.USER);
        // Creating UserInfo creates both UserInfo and User
        if(!usersService.create(user)) {
            throw new Exception("Creating user failed.");
        }

        return new ModelAndView("redirect:ladmuser.htm");
    }

    @Override
    /* Initialize the command object */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        User currentUser = usersService.getUser(request.getRemoteUser());
        UserInfo user = new UserInfo();
        user.setUser(new UserFull());      
        user.setGroup(UserGroup.USER);
        user.getUser().setOwner(currentUser.getOwner());
        return user;
    }
}
