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

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.io.JSService;
import com.pkrete.locationservice.admin.service.UsersService;
import com.pkrete.locationservice.admin.util.UsersUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The {@link EditJsController EditJsController} handles the requests
 * that are related to editing js scripts and saving the changes.
 *
 * @author Petteri Kivimäki
 */
public class EditJsController implements Controller {

    private UsersService usersService;
    private JSService jsService;

    public EditJsController() {
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public void setJsService(JSService jsService) {
        this.jsService = jsService;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception, ServletException, IOException {
        /* Get the current user. */
        Owner owner = UsersUtil.getUser(request, usersService).getOwner();
        /* Model that is returned together with the view */
        Map<String, Object> model = new HashMap<String, Object>();

        String lang = request.getParameter("lang");
        String contents = request.getParameter("contents");
        String js = request.getParameter("js");

        if (contents == null) {
            contents = jsService.read(js, owner);
        }

        if (request.getParameter("btn_save") != null) {
            if(!jsService.update(js, contents, owner)) {
                throw new Exception("Updating JS file failed.");
            }
        } else if (request.getParameter("btn_save_exit") != null) {
            if(!jsService.update(js, contents, owner)) {
                throw new Exception("Updating JS file failed.");
            }
            return new ModelAndView("redirect:templates.htm?select_lang=" + lang + "&js=" + js);
        } else if (request.getParameter("btn_back") != null) {
            return new ModelAndView("redirect:templates.htm?select_lang=" + lang + "&js=" + js);
        }

        model.put("js", js);
        model.put("contents", contents);
        model.put("language", lang);
        return new ModelAndView("edit_js", "model", model);
    }
}
