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
package com.pkrete.locationservice.admin.controller.mvc;

import com.pkrete.locationservice.admin.editor.LanguageEditor;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.service.SubjectMattersService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * The {@link AddSubjectMatterController AddSubjectMatterController} handles the requests
 * that are related to adding a new subject matter to the database. This class extends
 * abstract {@link BaseController BaseController} class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/addsubjectmatter.htm")
public class AddSubjectMatterController extends BaseController {

    @Autowired
    @Qualifier("subjectMatterValidator")
    private Validator validator;
    @Autowired
    @Qualifier("languageEditor")
    private LanguageEditor languageEditor;
    @Autowired
    @Qualifier("subjectMattersService")
    private SubjectMattersService subjectMattersService;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Language.class, languageEditor);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("subject") SubjectMatter subjectMatter,
            BindingResult result) throws Exception {

        Owner owner = getOwner(request);
        subjectMatter.setOwner(owner);

        validator.validate(subjectMatter, result);

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            model.put("languages", owner.getLanguages());
            return new ModelAndView("add_subjectmatter", model);
        }

        subjectMatter.setCreator(getUser(request).getUsername());
        if (!subjectMattersService.create(subjectMatter)) {
            throw new Exception("Creating subject matter failed.");
        }
        return new ModelAndView("redirect:subjectmatters.htm");
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) {
        model.addAttribute("languages", getOwner(request).getLanguages());
        model.put("subject", new SubjectMatter());
        return "add_subjectmatter";
    }
}
