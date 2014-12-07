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
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.service.SubjectMattersService;
import com.pkrete.locationservice.admin.util.UsersUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The {
 *
 * @SubjectMatterController SubjectMatterController} class returns the page that
 * contains the list of all the subject matters or redirects/forwards the user
 * to another page. The function depends on the request parameters.
 * SubjectMatterController returns a model that contains all the objects that
 * are needed in the page that is shown to the user. SubjectMatterController
 * implements the Controller interface.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/subjectmatters.htm")
public class SubjectMatterController extends BaseController {

    @Autowired
    @Qualifier("subjectMattersService")
    private SubjectMattersService subjectMattersService;
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
        /* Id of the selected  subject matter */
        String idSubject = request.getParameter("select_subject");

        if (request.getParameter("btn_add_subject") != null) {
            return new ModelAndView("redirect:addsubjectmatter.htm");
        } else if (request.getParameter("btn_edit_subject") != null && idSubject != null) {
            if (idSubject.matches("^[1-9]{1}[0-9]*$")) {
                return new ModelAndView("redirect:editsubjectmatter.htm?select_subject=" + idSubject);
            }
        } else if (request.getParameter("btn_delete_subject") != null && idSubject != null) {
            if (idSubject != null) {
                if (idSubject.matches("^[1-9]{1}[0-9]*$")) {
                    SubjectMatter temp = subjectMattersService.getSubjectMatterToBeDeleted(this.converterService.strToInt(idSubject), owner);
                    if (temp.getLocations().isEmpty()) {
                        if (!subjectMattersService.delete(temp)) {
                            throw new Exception("Deleting subject matter failed.");
                        }
                        idSubject = null;
                    } else {
                        model.put("errorMsg", this.messageSource.getMessage("error.subjectmatter.delete", null, null));
                    }
                }
            }
        }

        if (request.isUserInRole(UserGroup.ADMIN.toString())) {
            model.put("isAdmin", "");
        }
        /* Load list of all libraries from DB */
        model.put("subjects", subjectMattersService.getSubjectMattersWithLanguage(owner));

        return new ModelAndView("subjectmatter", "model", model);
    }
}
