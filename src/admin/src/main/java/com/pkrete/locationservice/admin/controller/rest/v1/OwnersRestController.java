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
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.service.OwnersService;
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
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This class provides REST API to Owner objects. This class implements
 * create, read, update, delete and list all owners functions.
 * 
 * INDEX    /owners                 [GET]   
 * CREATE   /owners                 [POST]
 * READ     /owners/{id}            [GET]
 * UPDATE   /owners/{id}            [PUT]
 * DELETE   /owners/{id}            [DELETE]
 * READ     /owners/{id}/settings   [GET]
 * UPDATE   /owners/{id}/settings   [PUT]
 * 
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/owners")
public class OwnersRestController extends RestController {

    private final static Logger logger = Logger.getLogger(OwnersRestController.class.getName());
    @Autowired
    @Qualifier("ownerMapService")
    private ObjectMapService mapConverter;
    @Autowired
    @Qualifier("ownerSettingsMapService")
    private ObjectMapService settingsMapConverter;
    @Autowired
    @Qualifier("ownersService")
    private OwnersService ownersService;
    @Autowired
    @Qualifier("ownerValidator")
    private Validator ownerValidator;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List index(HttpServletRequest request) {
        // Get list of owners in the system
        List owners = this.ownersService.getOwners();
        // Return the list
        return this.mapConverter.convert(owners);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map get(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws ResourceNotFoundException {
        // Get owner matching the given id
        Owner owner = this.ownersService.getFullOwner(id);
        // Check for null value
        if (owner == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Return the owner
        return this.mapConverter.convert(owner);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map delete(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws ResourceNotFoundException, OperationFailedException {
        // Get owner matching the given id
        Owner owner = this.ownersService.getFullOwner(id);
        // Check for null value
        if (owner == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Try to delete the owner
        if (!this.ownersService.delete(owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "owner");
        // Add id
        result.put("resource_id", id);
        // Return result
        return result;
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map create(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Owner owner, BindingResult results) throws ValidationException, OperationFailedException {
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // New owner object
        Owner newOwner = new Owner(owner.getCode(), owner.getName());
        // Set create operator
        newOwner.setCreator(operator);

        // Validate Owner object
        this.ownerValidator.validate(newOwner, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.create.failed"), results);
        }

        // Try to create a new Owner
        if (!this.ownersService.create(newOwner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.create.failed"));
        }

        // Return the created Owner
        return this.mapConverter.convert(newOwner);
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map update(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Owner owner, BindingResult results, @PathVariable int id) throws ValidationException, OperationFailedException, ResourceNotFoundException {
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Get Owner matching the given id
        Owner oldOwner = this.ownersService.getFullOwner(id);
        // Check for null value
        if (oldOwner == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Set update operator
        oldOwner.setUpdater(operator);
        // Set name
        oldOwner.setName(owner.getName());
        // Set previous code
        oldOwner.setCodePrevious(oldOwner.getCode());
        // Set current code
        oldOwner.setCode(owner.getCode());

        // Validate Owner object
        this.ownerValidator.validate(oldOwner, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // Try to update the subject matter
        if (!this.ownersService.update(oldOwner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Return the updated subject matter in JSON
        return this.mapConverter.convert(oldOwner);
    }

    @RequestMapping(value = "{id}/settings", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map getSettings(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws ResourceNotFoundException {
        // Get owner matching the given id
        Owner owner = this.ownersService.getFullOwner(id);
        // Check for null value
        if (owner == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Return the owner
        return this.settingsMapConverter.convert(owner);
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "{id}/settings", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map updateSettings(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Owner owner, BindingResult results, @PathVariable int id) throws ValidationException, OperationFailedException, ResourceNotFoundException {
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Get Owner matching the given id
        Owner oldOwner = this.ownersService.getFullOwner(id);
        // Check for null value
        if (oldOwner == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Set update operator
        oldOwner.setUpdater(operator);
        // Set code previous
        oldOwner.setCodePrevious(oldOwner.getCode());
        // Set color
        oldOwner.setColor(owner.getColor());
        // Set opacity
        oldOwner.setOpacity(owner.getOpacity());
        // Set locating strategy
        oldOwner.setLocatingStrategy(owner.getLocatingStrategy());
        // Set exporter visible
        oldOwner.setExporterVisible(owner.getExporterVisible());
        // Set allowed IPs
        oldOwner.setAllowedIPs(owner.getAllowedIPs());
        // Set preprocessing redirects
        oldOwner.setPreprocessingRedirects(owner.getPreprocessingRedirects());
        // Set not found redirects
        oldOwner.setNotFoundRedirects(owner.getNotFoundRedirects());

        // Validate Owner object
        this.ownerValidator.validate(oldOwner, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // Try to update the subject matter
        if (!this.ownersService.update(oldOwner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Return the updated subject matter in JSON
        return this.settingsMapConverter.convert(oldOwner);
    }
}
