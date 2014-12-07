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

import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.io.TemplatesService;
import com.pkrete.locationservice.admin.service.LanguagesService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;

/**
 * The {@link AddLanguageController AddLanguageController} handles the requests
 * that are related to adding a new language to the database. This class extends
 * abstract {@link BaseController BaseController} class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/addlanguage.htm")
public class AddLanguageController extends BaseController {

    @Autowired
    @Qualifier("templatesService")
    private TemplatesService templatesService;
    @Autowired
    @Qualifier("languagesService")
    private LanguagesService languagesService;
    @Autowired
    @Qualifier("languageValidator")
    private Validator validator;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("language") Language language,
            BindingResult result) throws Exception {
        Owner owner = getOwner(request);
        language.setOwner(owner);

        validator.validate(language, result);

        if (result.hasErrors()) {
            return new ModelAndView("add_language", new ModelMap());
        }

        language.setCreator(getUser(request).getUsername());
        /* Save new language to DB */
        if (!languagesService.create(language)) {
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

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) {
        Language lang = new Language();
        lang.setOwner(getOwner(request));
        model.put("language", lang);
        return "add_language";
    }
}
