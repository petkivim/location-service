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
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.user.UserInfo;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.service.OwnersService;
import com.pkrete.locationservice.admin.service.UsersService;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;

/**
 * The {@UserOwnerController UserOwnerController} class returns the page that
 * contains the list of all the users and owner groups in the LocationService
 * or redirects/forwards the user to another page. The function depends
 * on the request parameters. UserOwnerController returns a model that
 * contains all the objects that are needed in the page that is shown to
 * the user. UserOwnerController implements the Controller interface.
 *
 * @author Petteri Kivimäki
 */
public class UserOwnerController implements Controller {

    private OwnersService ownersService;
    private UsersService usersService;
    private ConverterService converterService;
    private MessageSource messageSource;

    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    public void setOwnersService(OwnersService ownersService) {
        this.ownersService = ownersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public UserOwnerController() {
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception, ServletException, IOException {
        /* Model that is returned together with the view */
        java.util.Map<String, Object> model = new HashMap<String, Object>();
        String userId = request.getParameter("select_user");
        String ownerId = request.getParameter("select_owner");

        /**
         * If user is administrator and index parameter is present in the
         * URL, recreate search index.
         */
        if (request.isUserInRole(UserGroup.ADMIN.toString()) && request.getParameter("index") != null) {
            ownersService.recreateSearchIndex();
        }

        if (request.getParameter("btn_add_owner") != null) {
            return new ModelAndView("redirect:addowner.htm");
        } else if (request.getParameter("btn_edit_owner") != null && ownerId != null) {
            return new ModelAndView("redirect:editowner.htm?select_owner=" + ownerId);
        } else if (request.getParameter("btn_delete_owner") != null && ownerId != null) {
            Owner temp = ownersService.getOwner(this.converterService.strToInt(ownerId));
            if (ownersService.canBeDeleted(temp)) {
                if (!ownersService.delete(temp)) {
                    model.put("errorMsg", this.messageSource.getMessage("error.owner.delete", null, null));
                }
            } else {
                model.put("errorMsg", this.messageSource.getMessage("error.owner.delete", null, null));
            }
        } else if (request.getParameter("btn_add_user") != null) {
            return new ModelAndView("redirect:adduser.htm");
        } else if (request.getParameter("btn_edit_user") != null && userId != null) {
            return new ModelAndView("redirect:edituser.htm?select_user=" + userId);
        } else if (request.getParameter("btn_delete_user") != null && userId != null) {
            UserInfo tempInfo = usersService.getUserInfoByUsername(userId);
            if (!usersService.delete(tempInfo)) {
                throw new Exception("Deleting user failed.");
            }
            tempInfo = null;
            userId = null;
        }

        model.put("users", usersService.getUsers());
        model.put("owners", ownersService.getOwners());
        return new ModelAndView("user_owner", "model", model);
    }
}
