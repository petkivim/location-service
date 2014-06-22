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
package com.pkrete.locationservice.admin.controller.rest.v1;

import com.pkrete.locationservice.admin.exception.OperationFailedException;
import com.pkrete.locationservice.admin.exception.ResourceNotFoundException;
import com.pkrete.locationservice.admin.exception.ValidationException;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.service.SubjectMattersService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.Validator;

/**
 * This class provides REST API to SubjectMatter objects. This class implements
 * create, read, update, delete and list all subject matter functions.
 * 
 * INDEX    /subjectmatters      [GET]   
 * CREATE   /subjectmatters      [POST]
 * READ     /subjectmatters/{id} [GET]
 * UPDATE   /subjectmatters/{id} [PUT]
 * DELETE   /subjectmatters/{id} [DELETE]
 * 
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/subjectmatters")
public class SubjectMattersRestController extends RestController {

    private final static Logger logger = Logger.getLogger(LanguagesRestController.class.getName());
    @Autowired
    @Qualifier("subjectMatterMapService")
    private ObjectMapService mapConverter;
    @Autowired
    @Qualifier("subjectMattersService")
    private SubjectMattersService subjectMattersService;
    @Autowired
    @Qualifier("subjectMatterValidator")
    private Validator subjectMatterValidator;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List index(HttpServletRequest request) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get list of subject matters related to the Owner
        List subjects = this.subjectMattersService.getSubjectMattersWithLanguage(owner);
        // Return the list
        return this.mapConverter.convert(subjects);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map get(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get subject matter matching the given id and Owner
        SubjectMatter subject = this.subjectMattersService.getSubjectMatter(id, owner);
        // Check for null value
        if (subject == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Return the subject matter
        return this.mapConverter.convert(subject);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map delete(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws OperationFailedException, ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get subject matter matching the given id and Owner
        SubjectMatter subject = this.subjectMattersService.getSubjectMatter(id, owner);
        // Check for null value
        if (subject == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Try to delete the subject matter
        if (!this.subjectMattersService.delete(subject)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "subjectmatter");
        // Add id
        result.put("resource_id", id);
        // Return result
        return result;
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map create(HttpServletRequest request, HttpServletResponse response,
            @RequestBody SubjectMatter subject, BindingResult results) throws ValidationException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Set owner to the SubjectMatter
        subject.setOwner(owner);
        // Set create operator
        subject.setCreator(operator);

        // Validate SubjectMatter object
        this.subjectMatterValidator.validate(subject, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.create.failed"), results);
        }

        // Try to create a new subject matter
        if (!this.subjectMattersService.create(subject)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.create.failed"));
        }

        // Return the created subject matter
        return this.mapConverter.convert(subject);
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map update(HttpServletRequest request, HttpServletResponse response,
            @RequestBody SubjectMatter subject, BindingResult results, @PathVariable int id) throws ValidationException, OperationFailedException, ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Get SubjectMatter matching the given id and Owner
        SubjectMatter oldSubject = this.subjectMattersService.getSubjectMatter(id, owner);
        // Check for null value
        if (oldSubject == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Set owner to the SubjectMatter
        subject.setOwner(owner);
        // Set update operator
        subject.setUpdater(operator);
        // Set id
        subject.setId(id);
        // Set creator
        subject.setCreator(oldSubject.getCreator());
        // Set created at
        subject.setCreated(oldSubject.getCreated());
        // Set locations
        subject.setLocations(oldSubject.getLocations());

        // Validate SubjectMatter object
        this.subjectMatterValidator.validate(subject, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // Try to update the subject matter
        if (!this.subjectMattersService.update(subject)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Return the updated subject matter in JSON
        return this.mapConverter.convert(subject);
    }
}
