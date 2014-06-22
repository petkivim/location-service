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
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;

/**
 * The {@link EditLanguageController EditLanguageController} handles the requests
 * that are related to editing a language object and then saving it to the database.
 * This class extends abstract {@link BaseController BaseController} class.
 *
 * @author Petteri Kivimäki
 */
public class EditLanguageController extends BaseController {

    public EditLanguageController() {
        setCommandClass(Language.class);
        setCommandName("language");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Language language = (Language) command;

        language.setUpdater(getUser(request).getUsername());
        /* Save language to DB */
        if(!languagesService.update(language)) {
            throw new Exception("Updating language failed.");
        }

        /* Update logged in user */
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.setAttribute("user", usersService.getUser(request.getRemoteUser()));
        /* Return to languages page */
        return new ModelAndView("redirect:languages.htm?select_language=" + request.getParameter("select_language"));
    }

    @Override
    /* Initialize the command object */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String id = request.getParameter("select_language");
        Owner owner = getOwner(request);
        Language lang = languagesService.getLanguageById(this.converterService.strToInt(id), owner);
        if (lang == null) {
            throw new ObjectNotFoundException(new StringBuilder("Language not found. {\"id\":").append(id).append(",\"owner\":\"").append(owner.getCode()).append("\"}").toString());
        }
        return lang;
    }
}
