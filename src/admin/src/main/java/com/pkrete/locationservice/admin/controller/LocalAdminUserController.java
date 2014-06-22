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

import java.io.IOException;
import java.util.HashMap;
import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.model.user.UserFull;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.model.user.UserInfo;
import com.pkrete.locationservice.admin.service.UsersService;
import com.pkrete.locationservice.admin.util.UsersUtil;
import java.util.List;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;

/**
 * The {@LocalAdminUserController LocalAdminUserController} class returns the 
 * page that contains the list of all the users belonging to the same Owner
 * with the local admin who accesses the page. LocalAdminUserController returns
 * a model that contains all the objects that are needed in the page that is 
 * shown tothe user. LocalAdminUserController implements the Controller interface.
 *
 * @author Petteri Kivimäki
 */
public class LocalAdminUserController implements Controller {

    private UsersService usersService;
    private MessageSource messageSource;

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public LocalAdminUserController() {
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception, ServletException, IOException {
        /* Model that is returned together with the view */
        java.util.Map<String, Object> model = new HashMap<String, Object>();
        String userId = request.getParameter("select_user");
        User currentUser = UsersUtil.getUser(request, usersService);
        
        if (request.getParameter("btn_add_user") != null) {
            return new ModelAndView("redirect:ladmadduser.htm");
        } else if (request.getParameter("btn_edit_user") != null && userId != null) {
            return new ModelAndView("redirect:ladmedituser.htm?select_user=" + userId);
        } else if (request.getParameter("btn_delete_user") != null && userId != null) {          
            UserFull temp = usersService.getFullUser(userId);
            UserInfo tempInfo = usersService.getUserInfoByUsername(userId);
            if (temp != null
                    && !currentUser.getUsername().equals(temp.getUsername())
                    && currentUser.getOwner().getId() == temp.getOwner().getId()) {
                if(!usersService.delete(tempInfo)) {
                    throw new Exception("Deleting user failed.");
                }
                temp = null;
                tempInfo = null;
                userId = null;
            } else {
                model.put("errorMsg", this.messageSource.getMessage("error.user.delete", null, null));
            }
        }
        List<User> users = usersService.getUsers(currentUser.getOwner(), UserGroup.USER);
        model.put("users", users);
        return new ModelAndView("user_ladm", "model", model);
    }
}
