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

import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.editor.LanguageEditor;
import com.pkrete.locationservice.admin.exception.ObjectNotFoundException;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.service.SubjectMattersService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * The {@link EditSubjectMatterController EditSubjectMatterController} handles
 * the requests that are related to editing a subject object and then saving it
 * to the database. This class extends abstract
 * {@link BaseController BaseController} class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/editsubjectmatter.htm")
@SessionAttributes("subject")
public class EditSubjectMatterController extends BaseController {

    @Autowired
    @Qualifier("subjectMatterValidator")
    private Validator validator;
    @Autowired
    @Qualifier("languageEditor")
    private LanguageEditor languageEditor;
    @Autowired
    @Qualifier("subjectMattersService")
    private SubjectMattersService subjectMattersService;
    @Autowired
    @Qualifier("converterService")
    private ConverterService converterService;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Language.class, languageEditor);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("subject") SubjectMatter subjectMatter,
            BindingResult result) throws Exception {

        validator.validate(subjectMatter, result);

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            model.put("languages", getOwner(request).getLanguages());
            return new ModelAndView("edit_subjectmatter", model);
        }

        subjectMatter.setUpdater(getUser(request).getUsername());
        if (!subjectMattersService.update(subjectMatter)) {
            throw new Exception("Updating subject matter failed.");
        }
        return new ModelAndView("redirect:subjectmatters.htm?select_subject=" + request.getParameter("select_subject"));
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) throws Exception {
        model.addAttribute("languages", getOwner(request).getLanguages());
        String id = request.getParameter("select_subject");
        Owner owner = getOwner(request);
        SubjectMatter subject = subjectMattersService.getSubjectMatter(this.converterService.strToInt(id), owner);
        if (subject == null) {
            throw new ObjectNotFoundException(new StringBuilder("Subject matter not found. {\"id\":").append(id).append(",\"owner\":\"").append(owner.getCode()).append("\"}").toString());
        }
        model.put("subject", subject);
        return "edit_subjectmatter";
    }
}
