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
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.template.JS;
import com.pkrete.locationservice.admin.io.JSService;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.util.Settings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * This class provides REST API to JavaScript files. This class implements
 * create, read, update, delete and list all js file functions.
 *
 * NB! Must use "file" postfix after {filename}, because otherwise dots in the
 * URL cause problems. The file extension problem only exists if the parameter
 * is in the last part of the URL.
 *
 * INDEX /scripts [GET] CREATE /scripts [POST] READ /scripts/{filename}/file
 * [GET] UPDATE /scripts/{filename}/file [PUT] DELETE /scripts/{filename}/file
 * [DELETE] RENAME /scripts/{filename}/rename [PUT] INDEX /scripts/jquery [GET]
 * INDEX /scripts/plugins [GET]
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/scripts")
public class ScriptsRestController extends RestController {

    private final static Logger logger = LoggerFactory.getLogger(ScriptsRestController.class.getName());
    @Autowired
    @Qualifier("jsMapService")
    private ObjectMapService mapConverter;
    @Autowired
    @Qualifier("jsService")
    private JSService service;
    @Autowired
    @Qualifier("jsValidator")
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List index(HttpServletRequest request) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get list of js files related to the owner
        List<String> files = this.service.getList(owner);
        // List for results
        List<JS> result = new ArrayList<JS>();
        // Loop through files and create corresponding JS objects
        for (String file : files) {
            // Add new JS object to the results
            result.add(new JS(0, file, owner));
        }
        // Return result
        return this.mapConverter.convert(result);
    }

    @RequestMapping(value = "/{filename}/file", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map get(HttpServletRequest request, @PathVariable String filename) throws ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Check that the js file exists
        if (!this.service.exists(filename, owner)) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Read the js files
        String contents = this.service.read(filename, owner);

        // Return result
        return this.mapConverter.convert(new JS(0, filename, contents, owner));
    }

    @RequestMapping(value = "/{filename}/file", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map delete(HttpServletRequest request, HttpServletResponse response, @PathVariable String filename) throws ResourceNotFoundException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Check that the js file exists
        if (!this.service.exists(filename, owner)) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }

        // Try to delete the js file
        if (!this.service.delete(filename, owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "js");
        // Add id
        result.put("resource_id", filename);
        // Return result
        return result;
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map create(HttpServletRequest request, HttpServletResponse response,
            @RequestBody JS js, BindingResult results) throws OperationFailedException, ValidationException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Set owner
        js.setOwner(owner);

        // Validate JS object
        this.validator.validate(js, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.create.failed"), results);
        }

        // Try to create a new js file
        if (!this.service.create(js.getFilename(), js.getContents(), owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.create.failed"));
        }

        // Return the css file
        return this.mapConverter.convert(js);
    }

    @RequestMapping(value = "/{filename}/file", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map update(HttpServletRequest request, HttpServletResponse response,
            @RequestBody JS js, BindingResult results, @PathVariable String filename) throws OperationFailedException, ValidationException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Set owner
        js.setOwner(owner);
        // css is an update, so id cannot be 0
        js.setId(1);

        // If message body filename and url parameter filename do not match,
        // msg body filename is current and url parameter filename is old
        // name
        if (!js.getFilename().equalsIgnoreCase(filename)) {
            js.setFilenameOld(filename);
        }

        // Validate JS object
        this.validator.validate(js, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // If old filename is not empty, the css file must be renamed before updating
        if (!js.getFilenameOld().isEmpty()) {
            // Try to rename the js file
            if (!this.service.rename(js.getFilenameOld(), js.getFilename(), owner)) {
                // Throw exception
                throw new ValidationException(getMessage("rest.update.failed"), results);
            }
        }

        // Try to update the js file
        if (!this.service.update(js.getFilename(), js.getContents(), owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Return the js file
        return this.mapConverter.convert(js);
    }

    @RequestMapping(value = "/{filename}/rename", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map rename(HttpServletRequest request, HttpServletResponse response,
            @RequestBody JS js, BindingResult results, @PathVariable String filename) throws OperationFailedException, ValidationException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Set owner
        js.setOwner(owner);
        // This is an update, so id cannot be 0
        js.setId(1);

        // If message body filename and url parameter filename do not match,
        // msg body filename is current and url parameter filename is old
        // name
        if (!js.getFilename().equalsIgnoreCase(filename)) {
            js.setFilenameOld(filename);
        }

        // Contents must be set for the validation
        js.setContents("");

        // Validate CSS object
        this.validator.validate(js, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // If old filename is empty, the js file cannot be renamed
        if (js.getFilenameOld().isEmpty()) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Try to rename the js file
        if (!this.service.rename(js.getFilenameOld(), js.getFilename(), owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Create Map containing the response
        Map result = new LinkedHashMap();
        // Add filename
        result.put("filename", js.getFilename());
        // Add old filename
        result.put("filename_old", js.getFilenameOld());

        // Return result
        return result;
    }

    @RequestMapping(value = "/jquery", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List jQuery(HttpServletRequest request) {
        // Get shared jQuery files
        List<String> files = this.service.getList();
        // Get relative path
        String path = Settings.getInstance().getScriptsSysPathRel();
        // Get URL
        String url = Settings.getInstance().getWebpath();
        // List of jQuery files
        List result = new ArrayList();
        // Loop through the files
        for (String file : files) {
            // New object for a script
            Map script = new LinkedHashMap();
            // Add filename name to the map object
            script.put("filename", file);
            // Add relative path to the map object
            script.put("rel_path", path + file);
            // Add URL to the map object
            script.put("url", url + path + file);
            // Add map object to the results list
            result.add(script);
        }
        // Return result
        return result;
    }

    @RequestMapping(value = "/plugins", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List plugins(HttpServletRequest request) {
        // Get list of shared plugins
        List<String> files = this.service.getPluginsList();
        // Get plugins relative path
        String path = Settings.getInstance().getScriptsPluginsPathRel();
        // Get plugins URL
        String url = Settings.getInstance().getWebpath();
        // List of plugins
        List result = new ArrayList();
        // Loop through the plugin files
        for (String file : files) {
            // New object for a plugin
            Map plugin = new LinkedHashMap();
            // Add filename name to the map object
            plugin.put("filename", file);
            // Add relative path to the map object
            plugin.put("rel_path", path + file);
            // Add URL to the map object
            plugin.put("url", url + path + file);
            // Add map object to the results list
            result.add(plugin);
        }
        // Return result
        return result;
    }
}
