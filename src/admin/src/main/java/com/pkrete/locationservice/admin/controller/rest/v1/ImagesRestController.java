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
import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.service.ImagesService;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class provides REST API to Image objects. This class implements
 * create, read, update, delete and list all images functions.
 * 
 * INDEX    /images      [GET]   
 * CREATE   /images      [POST]
 * READ     /images/{id} [GET]
 * UPDATE   /images/{id} [PUT]
 * DELETE   /images/{id} [DELETE]
 * 
 * Below functions are for listing uploaded image files, uploading image files
 * and deleting uploaded image files.
 * 
 * INDEX    /images/uploads     [GET]   
 * CREATE   /images/uploads     [POST]
 * DELETE   /images/uploads     [DELETE]    ?file=fileName  REQUIRED
 * 
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/images")
public class ImagesRestController extends RestController {

    private final static Logger logger = Logger.getLogger(ImagesRestController.class.getName());
    @Autowired
    @Qualifier("imageMapService")
    private ObjectMapService mapConverter;
    @Autowired
    @Qualifier("imagesService")
    private ImagesService imagesService;
    @Autowired
    @Qualifier("imageValidator")
    private Validator imageValidator;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List index(HttpServletRequest request) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get list of images related to the Owner
        List images = this.imagesService.get(owner);
        // Return the images
        return this.mapConverter.convert(images);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map get(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get image matching the given id and Owner
        Image image = this.imagesService.get(id, owner);
        // Check for null value
        if (image == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Return the image
        return this.mapConverter.convert(image);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map delete(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws ResourceNotFoundException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get image matching the given id and Owner
        Image image = this.imagesService.get(id, owner);
        // Check for null value
        if (image == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Try to delete the image
        if (!this.imagesService.delete(image)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "image");
        // Add id
        result.put("resource_id", id);
        // Return result
        return result;
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map create(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Image image, BindingResult results) throws OperationFailedException, ValidationException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Set owner to the Image
        image.setOwner(owner);
        // Set create operator
        image.setCreator(operator);

        // Validate Image object
        this.imageValidator.validate(image, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.create.failed"), results);
        }

        // Try to create a new image
        if (!this.imagesService.create(image)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.create.failed"));
        }

        // Return the image
        return this.mapConverter.convert(image);
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map update(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Image image, BindingResult results, @PathVariable int id) throws ResourceNotFoundException, ValidationException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Get Image matching the given id and Owner
        Image img = this.imagesService.get(id, owner);
        // Check for null value
        if (img == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Set owner to the Image
        image.setOwner(owner);
        // Set update operator
        image.setUpdater(operator);
        // Set id
        image.setId(id);
        // Set creator
        image.setCreator(img.getCreator());
        // Set created at
        image.setCreated(img.getCreated());
        // Set is_external
        image.setIsExternal(img.getIsExternal());
        // Set path
        image.setPath(img.getPath());
        // Validate Image object
        this.imageValidator.validate(image, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // Try to update the image
        if (!this.imagesService.update(image)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Return the image
        return this.mapConverter.convert(image);
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.POST, consumes = "multipart/form-data", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map upload(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile file) throws OperationFailedException, ValidationException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Create new temporary Image object, only for validation
        Image image = new Image(null, "test");
        // Set file
        image.setFile(file);
        // Set owner
        image.setOwner(owner);
        // Create binding results
        BindingResult results = new BeanPropertyBindingResult(image, "image");
        // Validate Image object
        this.imageValidator.validate(image, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.file.upload.failed"));
        }

        // Try to upload the file
        String fileName = this.imagesService.upload(file, owner);
        // If fileName is null the operation failed
        if (fileName == null) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.file.upload.failed"));
        }
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.file.upload.success"));
        // Add file name
        result.put("file_name", fileName);
        // Add the original file name
        result.put("file_name_org", file.getOriginalFilename());
        // Return result
        return result;
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List uploadedFiles(HttpServletRequest request, HttpServletResponse response) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get list of uploaded files
        List<String> images = this.imagesService.getAdminImages(owner);
        // List of images
        List result = new ArrayList();
        // Go through the list of files
        for (String image : images) {
            Map file = new HashMap();
            file.put("file", image);
            result.add(file);
        }
        // Return result
        return result;
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map uploadedFileDelete(HttpServletRequest request, HttpServletResponse response) throws ResourceNotFoundException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get the name of the file to be deleted
        String file = request.getParameter("file");
        // Try to delete the image
        if (!this.imagesService.delete(file, owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "image_file");
        // Add id
        result.put("resource_id", file);
        // Return result
        return result;
    }
}
