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
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * The {@link EditSubjectMatterController EditSubjectMatterController} handles the requests
 * that are related to editing a subject object and then saving it to the database.
 * This class extends abstract {@link HandleLocationController HandleLocationController} class.
 *
 * @author Petteri Kivimäki
 */
public class EditSubjectMatterController extends HandleLocationController {

    public EditSubjectMatterController() {
        setCommandClass(SubjectMatter.class);
        setCommandName("subject");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        SubjectMatter subject = (SubjectMatter) command;
        subject.setUpdater(getUser(request).getUsername());
        if(!subjectMattersService.update(subject)) {
            throw new Exception("Updating subject matter failed.");
        }
        return new ModelAndView("redirect:subjectmatters.htm?select_subject=" + request.getParameter("select_subject"));
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
        String id = request.getParameter("select_subject");
        Owner owner = getOwner(request);
        SubjectMatter subject = subjectMattersService.getSubjectMatter(this.converterService.strToInt(id), owner);
        if(subject == null) {
            throw new ObjectNotFoundException(new StringBuilder("Subject matter not found. {\"id\":").append(id).append(",\"owner\":\"").append(owner.getCode()).append("\"}").toString());
        }
        return subject; 
    }
}
