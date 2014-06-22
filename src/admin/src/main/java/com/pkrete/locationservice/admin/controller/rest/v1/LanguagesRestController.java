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
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.service.LanguagesService;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.io.TemplatesService;
import com.pkrete.locationservice.admin.validator.LanguageValidator;
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

/**
 * This class provides REST API to Language objects. This class implements
 * create, read, update, delete and list all languages functions.
 * 
 * INDEX    /languages      [GET]   
 * CREATE   /languages      [POST]      ?templates=true|false   OPTIONAL
 * READ     /languages/{id} [GET]
 * UPDATE   /languages/{id} [PUT]
 * DELETE   /languages/{id} [DELETE]
 * 
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/languages")
public class LanguagesRestController extends RestController {

    private final static Logger logger = Logger.getLogger(LanguagesRestController.class.getName());
    @Autowired
    @Qualifier("languageMapService")
    private ObjectMapService mapConverter;
    @Autowired
    @Qualifier("languagesService")
    private LanguagesService languagesService;
    @Autowired
    @Qualifier("templatesService")
    private TemplatesService templatesService;
    @Autowired
    @Qualifier("languageValidator")
    private LanguageValidator languageValidator;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List index(HttpServletRequest request) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get list of laguages related to the Owner
        List languages = this.languagesService.getLanguages(owner);
        // Return the languages
        return this.mapConverter.convert(languages);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map get(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get language matching the given id and Owner
        Language lang = this.languagesService.getLanguageById(id, owner);
        // Check for null value
        if (lang == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Return the language
        return this.mapConverter.convert(lang);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map delete(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws ResourceNotFoundException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get language matching the given id and Owner
        Language lang = this.languagesService.getLanguageById(id, owner);
        // Check for null value
        if (lang == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Try to delete the language
        if (!this.languagesService.delete(lang)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "language");
        // Add id
        result.put("resource_id", id);
        // Return result
        return result;
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map create(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Language language, BindingResult results) throws OperationFailedException, ValidationException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Set owner to the Language
        language.setOwner(owner);
        // Set create operator
        language.setCreator(operator);

        // Validate Language object
        this.languageValidator.validate(language, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.create.failed"), results);
        }

        // Try to create a new language
        if (!this.languagesService.create(language)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.create.failed"));
        }

        // Does user want to create default templates?
        if (request.getParameter("templates") != null && request.getParameter("templates").equals("true")) {
            // Create default templates.
            templatesService.createDefaults(language.getCode(), owner);
        }

        // Return the language
        return this.mapConverter.convert(language);
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map update(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Language language, BindingResult results, @PathVariable int id) throws ResourceNotFoundException, ValidationException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Get language matching the given id and Owner
        Language lang = this.languagesService.getLanguageById(id, owner);
        // Check for null value
        if (lang == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Set owner to the Language
        language.setOwner(owner);
        // Set update operator
        language.setUpdater(operator);
        // Set id
        language.setId(id);
        // Set the previous code value
        language.setCodePrevious(lang.getCode());
        // Set creator
        language.setCreator(lang.getCreator());
        // Set created at
        language.setCreated(lang.getCreated());

        // Validate Language object
        this.languageValidator.validate(language, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // Try to update the language
        if (!this.languagesService.update(language)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Return the language
        return this.mapConverter.convert(language);
    }
}
