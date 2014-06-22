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
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * The {@link AddSubjectMatterController AddSubjectMatterController} handles the requests
 * that are related to adding a new subject matter to the database. This class extends
 * abstract {@link BaseController BaseController} class.
 *
 * @author Petteri Kivimäki
 */
public class AddSubjectMatterController extends BaseController {

    public AddSubjectMatterController() {
        setCommandClass(SubjectMatter.class);
        setCommandName("subject");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        SubjectMatter subjectMatter = (SubjectMatter) command;

        subjectMatter.setOwner(getOwner(request));
        subjectMatter.setCreator(getUser(request).getUsername());
        if(!subjectMattersService.create(subjectMatter)) {
            throw new Exception("Creating subject matter failed.");
        }
        return new ModelAndView("redirect:subjectmatters.htm");
    }

    @Override
    protected java.util.Map referenceData(HttpServletRequest request) throws Exception {
        ModelMap modelMap = new ModelMap();
        modelMap.put("languages", getOwner(request).getLanguages());
        return modelMap;
    }

    @Override
    /* Initialize the command object */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Owner owner = getOwner(request);
        SubjectMatter subject = new SubjectMatter();
        subject.setOwner(owner);
        return subject;
    }
}
