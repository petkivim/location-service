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
import com.pkrete.locationservice.admin.exception.OperationFailedException;
import com.pkrete.locationservice.admin.model.user.User;
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
 * The {@link LocalAdminEditUserController LocalAdminEditUserController} handles 
 * the requests that are related to editing an existing user. This class extends
 * SimpleFormController class.
 *
 * @author Petteri Kivimäki
 */
public class LocalAdminEditUserController extends SimpleFormController {

    private UsersService usersService;

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public LocalAdminEditUserController() {
        setCommandClass(UserInfo.class);
        setCommandName("user");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        UserInfo user = (UserInfo) command;
        User currentUser = UsersUtil.getUser(request, usersService);
        String userId = request.getParameter("select_user");

        // Set owner - must match with the updater
        user.getUser().setOwner(currentUser.getOwner());
        // Set user group - must be "user"
        user.setGroup(UserGroup.USER);
        // Set updater
        user.getUser().setUpdater(currentUser.getUsername());
        // Update User, no need to update UserInfo, because local admin
        // can not update user group
        if(!usersService.update(user.getUser())) {
            throw new Exception("Updating user failed.");
        }
        
        // No need to update the User object in the Session, because
        // local admin can not update his own profile through this
        // controller

        return new ModelAndView("redirect:ladmuser.htm?select_user=" + userId);
    }

    @Override
    /* Initialize the command object */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        User user = UsersUtil.getUser(request, usersService);
        String id = request.getParameter("select_user");
        UserInfo info = usersService.getUserInfoByUsername(id, user.getOwner());
        if (info == null) {
            throw new ObjectNotFoundException(new StringBuilder("User not found. {\"id\":").append(id).append("}").toString());
        }
        if (!info.getGroup().equals(UserGroup.USER)) {
            throw new OperationFailedException("Editing user forbidden - insufficient rights : wrong user group.");
        }
        return info;
    }
}
