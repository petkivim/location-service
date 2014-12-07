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

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.io.CSSService;
import com.pkrete.locationservice.admin.util.UsersUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The {@link EditCssController EditCssController} handles the requests that are
 * related to editing CSS style sheet and saving the changes.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/editcss.htm")
public class EditCssController extends BaseController {

    @Autowired
    @Qualifier("cssService")
    private CSSService cssService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception, ServletException, IOException {
        /* Get the current user. */
        Owner owner = UsersUtil.getUser(request, usersService).getOwner();
        /* Model that is returned together with the view */
        Map<String, Object> model = new HashMap<String, Object>();

        String lang = request.getParameter("lang");
        String contents = request.getParameter("contents");
        String isSys = request.getParameter("sys");
        String stylesheet = request.getParameter("stylesheet");

        if (contents == null) {
            if (isSys != null) {
                contents = cssService.read(owner);
                stylesheet = "style.css";
            } else {
                contents = cssService.read(stylesheet, owner);
            }
        }

        if (request.getParameter("btn_save") != null || request.getParameter("btn_save_exit") != null) {
            if (isSys != null) {
                if (!cssService.update(contents, owner)) {
                    throw new Exception("Updating default CSS file failed.");
                }
            } else {
                if (!cssService.update(stylesheet, contents, owner)) {
                    throw new Exception("Updating CSS file failed.");
                }
            }
            if (request.getParameter("btn_save_exit") != null) {
                return new ModelAndView("redirect:templates.htm?select_lang=" + lang + "&stylesheet=" + stylesheet);
            }
        } else if (request.getParameter("btn_back") != null) {
            return new ModelAndView("redirect:templates.htm?select_lang=" + lang + "&stylesheet=" + stylesheet);
        }

        model.put("stylesheet", stylesheet);
        model.put("contents", contents);
        model.put("language", lang);
        return new ModelAndView("edit_css", "model", model);
    }
}
