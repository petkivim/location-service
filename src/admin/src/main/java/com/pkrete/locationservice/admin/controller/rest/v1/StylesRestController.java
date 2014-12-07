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
import com.pkrete.locationservice.admin.model.template.CSS;
import com.pkrete.locationservice.admin.io.CSSService;
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
 * This class provides REST API to css files. This class implements create,
 * read, update, delete and list all css file functions.
 *
 * NB! Must use "file" postfix after {filename}, because otherwise dots in the
 * URL cause problems. The file extension problem only exists if the parameter
 * is in the last part of the URL.
 *
 * INDEX /styles [GET] READ /styles/default [GET] UPDATE /styles/default [PUT]
 * CREATE /styles [POST] READ /styles/{filename}/file [GET] UPDATE
 * /styles/{filename}/file [PUT] DELETE /styles/{filename}/file [DELETE] RENAME
 * /styles/{filename}/rename [PUT]
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/styles")
public class StylesRestController extends RestController {

    private final static Logger logger = LoggerFactory.getLogger(StylesRestController.class.getName());
    @Autowired
    @Qualifier("cssMapService")
    private ObjectMapService mapConverter;
    @Autowired
    @Qualifier("cssService")
    private CSSService service;
    @Autowired
    @Qualifier("cssValidator")
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List index(HttpServletRequest request) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get list of CSS files related to the owner
        List<String> files = this.service.getList(owner);
        // List for results
        List<CSS> result = new ArrayList<CSS>();
        // Loop through files and create corresponding CSS objects
        for (String file : files) {
            // Add new CSS object to the results
            result.add(new CSS(0, file, owner));
        }
        // Return result
        return this.mapConverter.convert(result);
    }

    @RequestMapping(value = "/{filename}/file", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map get(HttpServletRequest request, @PathVariable String filename) throws ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Check that the css file exists
        if (!this.service.exists(filename, owner)) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Read the css files
        String contents = this.service.read(filename, owner);

        // Return result
        return this.mapConverter.convert(new CSS(0, filename, contents, owner));
    }

    @RequestMapping(value = "/{filename}/file", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map delete(HttpServletRequest request, HttpServletResponse response, @PathVariable String filename) throws ResourceNotFoundException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Check that the css file exists
        if (!this.service.exists(filename, owner)) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }

        // Try to delete the css file
        if (!this.service.delete(filename, owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "css");
        // Add id
        result.put("resource_id", filename);
        // Return result
        return result;
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map create(HttpServletRequest request, HttpServletResponse response,
            @RequestBody CSS css, BindingResult results) throws OperationFailedException, ValidationException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Set owner
        css.setOwner(owner);

        // Validate CSS object
        this.validator.validate(css, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.create.failed"), results);
        }

        // Try to create a new css file
        if (!this.service.create(css.getFilename(), css.getContents(), owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.create.failed"));
        }

        // Return the css file
        return this.mapConverter.convert(css);
    }

    @RequestMapping(value = "/{filename}/file", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map update(HttpServletRequest request, HttpServletResponse response,
            @RequestBody CSS css, BindingResult results, @PathVariable String filename) throws OperationFailedException, ValidationException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Set owner
        css.setOwner(owner);
        // css is an update, so id cannot be 0
        css.setId(1);

        // If message body filename and url parameter filename do not match,
        // msg body filename is current and url parameter filename is old
        // name
        if (!css.getFilename().equalsIgnoreCase(filename)) {
            css.setFilenameOld(filename);
        }

        // Validate CSS object
        this.validator.validate(css, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // If old filename is not empty, the css file must be renamed before updating
        if (!css.getFilenameOld().isEmpty()) {
            // Try to rename the css file
            if (!this.service.rename(css.getFilenameOld(), css.getFilename(), owner)) {
                // Throw exception
                throw new ValidationException(getMessage("rest.update.failed"), results);
            }
        }

        // Try to update the css file
        if (!this.service.update(css.getFilename(), css.getContents(), owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Return the css file
        return this.mapConverter.convert(css);
    }

    @RequestMapping(value = "/{filename}/rename", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map rename(HttpServletRequest request, HttpServletResponse response,
            @RequestBody CSS css, BindingResult results, @PathVariable String filename) throws OperationFailedException, ValidationException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Set owner
        css.setOwner(owner);
        // This is an update, so id cannot be 0
        css.setId(1);

        // If message body filename and url parameter filename do not match,
        // msg body filename is current and url parameter filename is old
        // name
        if (!css.getFilename().equalsIgnoreCase(filename)) {
            css.setFilenameOld(filename);
        }
        // Contents must be set for the validation
        css.setContents("");

        // Validate CSS object
        this.validator.validate(css, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // If old filename is empty, the css file cannot be renamed
        if (css.getFilenameOld().isEmpty()) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Try to rename the css file
        if (!this.service.rename(css.getFilenameOld(), css.getFilename(), owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Create Map containing the response
        Map result = new LinkedHashMap();
        // Add filename
        result.put("filename", css.getFilename());
        // Add old filename
        result.put("filename_old", css.getFilenameOld());

        // Return result
        return result;
    }

    @RequestMapping(value = "/default", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map getDefault(HttpServletRequest request) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get relative path
        String path = Settings.getInstance().getHomePathRel(owner.getCode());
        // Get URL
        String url = Settings.getInstance().getWebpath();
        // Name of the file
        String file = "style.css";
        // Read the system css file
        String contents = this.service.read(owner);

        // Create Map containing the response
        Map result = new LinkedHashMap();
        // Add filename
        result.put("filename", file);
        // Add relative path to the map object
        result.put("rel_path", path + file);
        // Add URL to the map object
        result.put("url", url + path + file);
        // Add contents
        result.put("contents", contents);

        // Return result
        return result;
    }

    @RequestMapping(value = "/default", method = RequestMethod.PUT, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map updateDefault(HttpServletRequest request, @RequestBody CSS css) throws OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Try to update the default css
        if (!this.service.update(css.getContents(), owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }
        // Get relative path
        String path = Settings.getInstance().getHomePathRel(owner.getCode());
        // Get URL
        String url = Settings.getInstance().getWebpath();
        // Name of the file
        String file = "style.css";

        // Create Map containing the response
        Map result = new LinkedHashMap();
        // Add filename
        result.put("filename", file);
        // Add relative path to the map object
        result.put("rel_path", path + file);
        // Add URL to the map object
        result.put("url", url + path + file);
        // Add contents
        result.put("contents", css.getContents());

        // Return result
        return result;
    }
}
