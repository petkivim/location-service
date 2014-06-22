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

import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.io.TemplatesService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;

/**
 * The {@link AddLanguageController AddLanguageController} handles the requests
 * that are related to adding a new language to the database. This class extends
 * abstract {@link BaseController BaseController} class.
 *
 * @author Petteri Kivimäki
 */
public class AddLanguageController extends BaseController {

    private TemplatesService templatesService;

    public AddLanguageController() {
        setCommandClass(Language.class);
        setCommandName("language");
    }

    public void setTemplatesService(TemplatesService templatesService) {
        this.templatesService = templatesService;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Language language = (Language) command;
        Owner owner = getOwner(request);
        language.setCreator(getUser(request).getUsername());
        /* Save new language to DB */
        if(!languagesService.create(language)) {
            throw new Exception("Creating language failed.");      
        }
        /* Does user want to create default templates */
        if (request.getParameter("templates") != null) {
            /* Create default templates. */
            templatesService.createDefaults(language.getCode(), owner);
        }
        /* Update logged in user */
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.setAttribute("user", usersService.getUser(request.getRemoteUser()));
        /* Return to languages page */
        return new ModelAndView("redirect:languages.htm");
    }

    @Override
    /* Initialize the command object */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Language lang = new Language();
        lang.setOwner(getOwner(request));
        return lang;
    }
}
