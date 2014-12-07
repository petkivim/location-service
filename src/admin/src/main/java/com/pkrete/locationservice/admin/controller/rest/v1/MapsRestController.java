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
package com.pkrete.locationservice.admin.controller.rest.v1;

import com.pkrete.locationservice.admin.exception.OperationFailedException;
import com.pkrete.locationservice.admin.exception.ResourceNotFoundException;
import com.pkrete.locationservice.admin.exception.ValidationException;
import com.pkrete.locationservice.admin.model.illustration.Map;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.service.LanguagesService;
import com.pkrete.locationservice.admin.service.MapsService;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * This class provides REST API to Map objects. This class implements create,
 * read, update, delete and list functions.
 *
 * INDEX /maps [GET] CREATE /maps [POST] READ /maps/{id} [GET] UPDATE /maps/{id}
 * [PUT] DELETE /maps/{id} [DELETE]
 *
 * Below functions are for listing uploaded map files, uploading map files and
 * deleting uploaded map files.
 *
 * INDEX /maps/uploads [GET] CREATE /maps/uploads [POST] ?languageId=id REQUIRED
 * DELETE /maps/uploads [DELETE] ?file=fileName&languageId=id REQUIRED
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/maps")
public class MapsRestController extends RestController {

    private final static Logger logger = LoggerFactory.getLogger(MapsRestController.class.getName());
    @Autowired
    @Qualifier("mapMapService")
    private ObjectMapService mapConverter;
    @Autowired
    @Qualifier("mapsService")
    private MapsService mapsService;
    @Autowired
    @Qualifier("mapValidator")
    private Validator mapValidator;
    @Autowired
    @Qualifier("converterService")
    private ConverterService converterService;
    @Autowired
    @Qualifier("languagesService")
    private LanguagesService languagesService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List index(HttpServletRequest request) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get list of maps related to the Owner
        List maps = this.mapsService.get(owner);
        // Return the maps
        return this.mapConverter.convert(maps);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public java.util.Map get(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get image matching the given id and Owner
        Map map = this.mapsService.get(id, owner);
        // Check for null value
        if (map == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Return the map
        return this.mapConverter.convert(map);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public java.util.Map delete(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws ResourceNotFoundException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get image matching the given id and Owner
        Map map = this.mapsService.get(id, owner);
        // Check for null value
        if (map == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Try to delete the map
        if (!this.mapsService.delete(map)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        java.util.Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "map");
        // Add id
        result.put("resource_id", id);
        // Return result
        return result;
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public java.util.Map create(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Map map, BindingResult results) throws OperationFailedException, ValidationException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Set owner to the Map
        map.setOwner(owner);
        // Set create operator
        map.setCreator(operator);

        // Validate Map object
        this.mapValidator.validate(map, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.create.failed"), results);
        }

        // Try to create a new map
        if (!this.mapsService.create(map)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.create.failed"));
        }

        // Return the map
        return this.mapConverter.convert(map);
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public java.util.Map update(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Map map, BindingResult results, @PathVariable int id) throws ResourceNotFoundException, ValidationException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Get Map matching the given id and Owner
        Map dbMap = this.mapsService.get(id, owner);
        // Check for null value
        if (dbMap == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Set owner to the Image
        map.setOwner(owner);
        // Set update operator
        map.setUpdater(operator);
        // Set id
        map.setId(id);
        // Set creator
        map.setCreator(dbMap.getCreator());
        // Set created at
        map.setCreated(dbMap.getCreated());
        // Set is_external
        map.setIsExternal(dbMap.getIsExternal());
        // Set path
        map.setPath(dbMap.getPath());
        // Validate Map object
        this.mapValidator.validate(map, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // Try to update the map
        if (!this.mapsService.update(map)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Return the map
        return this.mapConverter.convert(map);
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List uploadedFiles(HttpServletRequest request, HttpServletResponse response) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get list of uploaded files
        java.util.Map<String, List<Language>> maps = this.mapsService.getUploadedMaps(owner);
        // List of maps
        List result = new ArrayList();
        // Loop through the files
        for (String key : maps.keySet()) {
            // New object for the file
            java.util.Map file = new HashMap();
            // List for the related languages
            List languages = new ArrayList();
            // Loop through the languages related to the file
            for (Language lang : maps.get(key)) {
                // Add language id to the list
                languages.add(lang.getId());
            }
            // Add file name to the map object
            file.put("file", key);
            // Add language ids to the map object
            file.put("language_ids", languages);
            // Add map object to the results list
            result.add(file);
        }
        // Return result
        return result;
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public java.util.Map uploadedFileDelete(HttpServletRequest request, HttpServletResponse response) throws ResourceNotFoundException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get the name of the file to be deleted
        String file = request.getParameter("file");
        // Get the language of the file to be deleted
        String languageId = request.getParameter("languageId");
        // Convert the id from string to int
        int langId = this.converterService.strToInt(languageId);
        // Get language matching the given id and Owner
        Language lang = this.languagesService.getLanguageById(langId, owner);
        // Check for null value
        if (lang == null) {
            logger.warn("Deleting the uploaded map file failed, because no language matching the given language id and owner was found.");
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Try to delete the map file
        if (!this.mapsService.delete(file, lang, owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        java.util.Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "map_file");
        // Add id
        result.put("resource_id", file);
        // Add id
        result.put("language_id", lang.getId());
        // Return result
        return result;
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.POST, consumes = "multipart/form-data", produces = "application/json; charset=utf-8")
    @ResponseBody
    public java.util.Map upload(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile file) throws OperationFailedException, ValidationException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get the language of the file
        String languageId = request.getParameter("languageId");
        // Convert the id from string to int
        int langId = this.converterService.strToInt(languageId);
        // Get language matching the given id and Owner
        Language lang = this.languagesService.getLanguageById(langId, owner);
        // Check for null value
        if (lang == null) {
            logger.warn("Uploading the map file failed, because no language matching the given language id and owner was found.");
            // Throw exception
            throw new OperationFailedException(getMessage("rest.file.upload.failed"));
        }
        // Create new temporary Map object, only for validation
        Map map = new Map(null, "test");
        // Set file
        map.addFile(lang, file);
        // Set owner
        map.setOwner(owner);
        // Create binding results
        BindingResult results = new BeanPropertyBindingResult(map, "map");
        // Validate Map object
        this.mapValidator.validate(map, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.file.upload.failed"));
        }

        // Try to upload the file
        String fileName = this.mapsService.upload(file, lang, owner);
        // If fileName is null the operation failed
        if (fileName == null) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.file.upload.failed"));
        }
        // Create Map containing the message
        java.util.Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.file.upload.success"));
        // Add file name
        result.put("file_name", fileName);
        // Add the original file name
        result.put("file_name_org", file.getOriginalFilename());
        // Add language id
        result.put("language_id", lang.getId());
        // Return result
        return result;
    }
}
