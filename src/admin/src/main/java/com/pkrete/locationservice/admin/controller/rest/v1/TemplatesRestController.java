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

import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.exception.OperationFailedException;
import com.pkrete.locationservice.admin.exception.ResourceNotFoundException;
import com.pkrete.locationservice.admin.exception.ValidationException;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.template.Template;
import com.pkrete.locationservice.admin.service.LanguagesService;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.io.TemplatesService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
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
 * This class provides REST API to template files. This class implements create,
 * read, update, delete and list all template functions.
 *
 * NB! Must use "file" postfix after {templateName}, because otherwise dots in
 * the URL cause problems. The file extension problem only exists if the
 * parameter is in the last part of the URL.
 *
 * INDEX /templates [GET] INDEX /templates/{langCode} [GET] READ
 * /templates/system [GET] UPDATE /templates/system [PUT] CREATE /templates/
 * [POST] READ /templates/{templateName}/file [GET] ?languageId={langId}
 * REQUIRED UPDATE /templates/{templateName}/file [PUT] DELETE
 * /templates/{templateName}/file [DELETE] ?languageId={langId} REQUIRED RENAME
 * /templates/{templateName}/rename [PUT] INDEX /templates/tags [GET]
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/templates")
public class TemplatesRestController extends RestController {

    private final static Logger logger = LoggerFactory.getLogger(TemplatesRestController.class.getName());
    @Autowired
    @Qualifier("templateMapService")
    private ObjectMapService mapConverter;
    @Autowired
    @Qualifier("templatesService")
    private TemplatesService service;
    @Autowired
    @Qualifier("languagesService")
    private LanguagesService languagesService;
    @Autowired
    @Qualifier("converterService")
    private ConverterService converterService;
    @Autowired
    @Qualifier("templateValidator")
    private Validator validator;
    @Resource(name = "libraryTags")
    private Map<String, String> libraryTags;
    @Resource(name = "collectionTags")
    private Map<String, String> collectionTags;
    @Resource(name = "shelfTags")
    private Map<String, String> shelfTags;
    @Resource(name = "notFoundNotAvailableTags")
    private Map<String, String> notFoundNotAvailableTags;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List index(HttpServletRequest request) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get Map of templates related to the Owner
        java.util.Map<String, List<Language>> templates = this.service.getTemplatesAndLanguages(owner);
        // List of templates
        List result = new ArrayList();
        // Loop through the templates
        for (String key : templates.keySet()) {
            // New object for the template
            Map template = new LinkedHashMap();
            // List for the related languages
            List languages = new ArrayList();
            // Loop through the languages related to the templates
            for (Language lang : templates.get(key)) {
                // New object for the language
                Map language = new HashMap();
                // Add language id
                language.put("id", lang.getId());
                // Add language code
                language.put("code", lang.getCode());
                // Add language id to the list
                languages.add(language);
            }
            // Add template name to the map object
            template.put("filename", key);
            // Add languages to the map object
            template.put("languages", languages);
            // Add map object to the results list
            result.add(template);
        }
        // Return result
        return result;
    }

    @RequestMapping(value = "/{langCode}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List indexByLanguage(HttpServletRequest request, @PathVariable String langCode) throws ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get language matching the given language code and Owner
        Language lang = this.languagesService.getLanguage(langCode, owner);
        // Check for null value
        if (lang == null) {
            logger.error("Language cannot be null.");
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Get templates matching the given id and Owner
        List<String> templates = this.service.getList(langCode, owner);

        // List of templates
        List<Template> result = new ArrayList<Template>();
        // Go through the list of templates
        for (String template : templates) {
            result.add(new Template(0, template, lang, owner));
        }
        // Return result
        return this.mapConverter.convert(result);
    }

    @RequestMapping(value = "/{templateName}/file", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map get(HttpServletRequest request, @PathVariable String templateName) throws ResourceNotFoundException {
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
            logger.error("Language cannot be null.");
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Check that the template exists in the template folder
        if (!this.service.exists(templateName, lang.getCode(), owner)) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Read the template
        String contents = this.service.read(templateName, lang.getCode(), owner);

        // Return result
        return this.mapConverter.convert(new Template(0, templateName, lang, contents, owner));
    }

    @RequestMapping(value = "/{templateName}/file", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map delete(HttpServletRequest request, HttpServletResponse response, @PathVariable String templateName) throws ResourceNotFoundException, OperationFailedException {
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
            logger.error("Language cannot be null.");
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Check that the template exists in the template folder
        if (!this.service.exists(templateName, lang.getCode(), owner)) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }

        // Try to delete the template
        if (!this.service.delete(templateName, lang.getCode(), owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "template");
        // Add id
        result.put("resource_id", templateName);
        // Return result
        return result;
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map create(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Template template, BindingResult results) throws OperationFailedException, ValidationException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Set owner
        template.setOwner(owner);

        // Validate Template object
        this.validator.validate(template, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.create.failed"), results);
        }

        // Try to create a new template
        if (!this.service.create(template.getFilename(), template.getContents(), template.getLanguage().getCode(), owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.create.failed"));
        }

        // Return the template
        return this.mapConverter.convert(template);
    }

    @RequestMapping(value = "/{templateName}/file", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map update(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Template template, BindingResult results, @PathVariable String templateName) throws OperationFailedException, ValidationException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Set owner
        template.setOwner(owner);
        // This is an update, so id cannot be 0
        template.setId(1);

        // If message body filename and url parameter templateName do not match,
        // msg body filename is current and url parameter templateName is old
        // name
        if (!template.getFilename().equalsIgnoreCase(templateName)) {
            template.setFilenameOld(templateName);
        }

        // Validate Template object
        this.validator.validate(template, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // If old filename is not empty, the template must be renamed before updating
        if (!template.getFilenameOld().isEmpty()) {
            // Try to rename the template
            if (!this.service.rename(template.getFilenameOld(), template.getFilename(), template.getLanguage().getCode(), owner)) {
                // Throw exception
                throw new ValidationException(getMessage("rest.update.failed"), results);
            }
        }

        // Try to update the template
        if (!this.service.update(template.getFilename(), template.getContents(), template.getLanguage().getCode(), owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Return the template
        return this.mapConverter.convert(template);
    }

    @RequestMapping(value = "/{templateName}/rename", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map rename(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Template template, BindingResult results, @PathVariable String templateName) throws OperationFailedException, ValidationException, ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Set owner
        template.setOwner(owner);
        // This is an update, so id cannot be 0
        template.setId(1);

        // If message body filename and url parameter templateName do not match,
        // msg body filename is current and url parameter templateName is old
        // name
        if (!template.getFilename().equalsIgnoreCase(templateName)) {
            template.setFilenameOld(templateName);
        }

        // Validate Template object
        this.validator.validate(template, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // If old filename is empty, the template cannot be renamed
        if (template.getFilenameOld().isEmpty()) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Try to rename the template
        if (!this.service.rename(template.getFilenameOld(), template.getFilename(), template.getLanguage().getCode(), owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Create Map containing the response
        Map result = new LinkedHashMap();
        // Add filename
        result.put("filename", template.getFilename());
        // Add old filename
        result.put("filename_old", template.getFilenameOld());
        // Add language id
        result.put("language_id", template.getLanguage().getId());
        // Return result
        return result;
    }

    @RequestMapping(value = "/system", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map getSystem(HttpServletRequest request) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Read the system template
        String contents = this.service.read(owner);

        // Create Map containing the response
        Map result = new LinkedHashMap();
        // Add filename
        result.put("filename", "template.txt");
        // Add contents
        result.put("contents", contents);

        // Return result
        return result;
    }

    @RequestMapping(value = "/system", method = RequestMethod.PUT, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map updateSystem(HttpServletRequest request, @RequestBody Template template) throws OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");

        // Try to update the system template
        if (!this.service.update(template.getContents(), owner)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Create Map containing the response
        Map result = new LinkedHashMap();
        // Add filename
        result.put("filename", "template.txt");
        // Add contents
        result.put("contents", template.getContents());

        // Return result
        return result;
    }

    @RequestMapping(value = "/tags", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List getTags(HttpServletRequest request) {
        List result = new ArrayList<Map>();

        // Create Map object for the level
        Map tags = new LinkedHashMap();
        // Add library level
        tags.put("level", "library");
        // List for tags
        List list = new ArrayList();
        // Loop through tags
        for (String key : libraryTags.keySet()) {
            Map tag = new LinkedHashMap();
            tag.put("label", key);
            tag.put("tag", libraryTags.get(key));
            // Add tag to the list
            list.add(tag);
        }
        // Add tags
        tags.put("tags", list);
        // Add level to results
        result.add(tags);

        // Create Map object for the level
        tags = new LinkedHashMap();
        // Add collection level
        tags.put("level", "collection");
        // List for tags
        list = new ArrayList();
        // Loop through tags
        for (String key : collectionTags.keySet()) {
            Map tag = new LinkedHashMap();
            tag.put("label", key);
            tag.put("tag", collectionTags.get(key));
            // Add tag to the list
            list.add(tag);
        }
        // Add tags
        tags.put("tags", list);
        // Add level to results
        result.add(tags);

        // Create Map object for the level
        tags = new LinkedHashMap();
        // Add shelf level
        tags.put("level", "shelf");
        // List for tags
        list = new ArrayList();
        // Loop through tags
        for (String key : shelfTags.keySet()) {
            Map tag = new LinkedHashMap();
            tag.put("label", key);
            tag.put("tag", shelfTags.get(key));
            // Add tag to the list
            list.add(tag);
        }
        // Add tags
        tags.put("tags", list);
        // Add level to results
        result.add(tags);

        // Create Map object for the level
        tags = new LinkedHashMap();
        // Add not found and not available level
        tags.put("level", "not_found_not_available");
        // List for tags
        list = new ArrayList();
        // Loop through tags
        for (String key : notFoundNotAvailableTags.keySet()) {
            Map tag = new LinkedHashMap();
            tag.put("label", key);
            tag.put("tag", notFoundNotAvailableTags.get(key));
            // Add tag to the list
            list.add(tag);
        }
        // Add tags
        tags.put("tags", list);
        // Add level to results
        result.add(tags);

        // Return result
        return result;
    }
}
