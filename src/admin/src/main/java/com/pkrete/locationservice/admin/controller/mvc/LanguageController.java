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
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.service.LanguagesService;
import com.pkrete.locationservice.admin.service.UsersService;
import com.pkrete.locationservice.admin.util.UsersUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The {
 *
 * @LanguageController LanguageController} class returns the page that contains
 * the list of all the languages in the LocationService and offers the necessary
 * functionality for adding, modifying and deleting them. The function depends
 * on the request parameters. LanguageController returns a model that contains
 * all the objects that are needed in the page that is shown to the user.
 * IllustrationController implements the Controller interface.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/languages.htm")
public class LanguageController {

    @Autowired
    @Qualifier("languagesService")
    private LanguagesService languagesService;
    @Autowired
    @Qualifier("usersService")
    private UsersService usersService;
    @Autowired
    @Qualifier("converterService")
    private ConverterService converterService;
    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception, ServletException, IOException {
        /* Get the current user. */
        Owner owner = UsersUtil.getUser(request, usersService).getOwner();
        /* Model that is returned together with the view */
        Map<String, Object> model = new HashMap<String, Object>();
        String idLanguage = request.getParameter("select_language");

        /**
         * If user is administrator and index parameter is present in the URL,
         * recreate search index.
         */
        if (request.isUserInRole(UserGroup.ADMIN.toString()) && request.getParameter("index") != null) {
            this.languagesService.recreateSearchIndex();
        }

        if (request.getParameter("btn_add_language") != null) {
            return new ModelAndView("redirect:addlanguage.htm");
        } else if (request.getParameter("btn_edit_language") != null && idLanguage != null) {
            return new ModelAndView("redirect:editlanguage.htm?select_language=" + idLanguage);
        } else if (request.getParameter("btn_delete_language") != null) {
            Language lang = languagesService.getLanguageById(this.converterService.strToInt(idLanguage), owner);
            if (languagesService.canBeDeleted(lang)) {
                if (!languagesService.delete(lang)) {
                    throw new Exception("Deleting language failed.");
                }
                lang = null;
                updateUser(request);
                owner = UsersUtil.getUser(request, usersService).getOwner();
            } else {
                model.put("errorMsg", this.messageSource.getMessage("error.language.delete", null, null));
            }
        }

        if (request.isUserInRole(UserGroup.ADMIN.toString())) {
            model.put("isAdmin", "");
        }

        model.put("languages", owner.getLanguages());

        return new ModelAndView("language", "model", model);
    }

    protected void updateUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.setAttribute("user", usersService.getUser(request.getRemoteUser()));
    }
}
