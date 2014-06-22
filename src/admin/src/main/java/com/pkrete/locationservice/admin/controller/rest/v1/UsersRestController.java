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
import com.pkrete.locationservice.admin.model.user.UserInfo;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.service.UsersService;
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
 * This class provides REST API to User objects. This class implements
 * create, read, update, delete and list all user functions.
 * 
 * INDEX    /users      [GET]   
 * CREATE   /users      [POST]
 * READ     /users/{id} [GET]
 * UPDATE   /users/{id} [PUT]
 * DELETE   /users/{id} [DELETE]
 * 
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/users")
public class UsersRestController extends RestController {

    private final static Logger logger = Logger.getLogger(UsersRestController.class.getName());
    @Autowired
    @Qualifier("userInfoMapService")
    private ObjectMapService mapConverter;
    @Autowired
    @Qualifier("usersService")
    private UsersService service;
    @Autowired
    @Qualifier("userInfoValidator")
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List index(HttpServletRequest request) {
        // List of all the UserInfos
        List users = null;
        // Get user's group
        UserGroup group = (UserGroup) request.getAttribute("group");
        // The operation depends on the user's group
        if (group.equals(UserGroup.ADMIN)) {
            // Get list of all the UserInfos
            users = this.service.getUserInfos();
        } else if (group.equals(UserGroup.LOCAL_ADMIN)) {
            // Get Owner object related to the user
            Owner owner = (Owner) request.getAttribute("owner");
            // Get list of all the UserInfos related to the Owner
            users = this.service.getUserInfos(owner, UserGroup.USER);
        }
        // Return the list
        return this.mapConverter.convert(users);
    }

    @RequestMapping(value = "{username}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map get(HttpServletRequest request, HttpServletResponse response, @PathVariable String username) throws ResourceNotFoundException {
        // UserInfo object
        UserInfo user = null;
        // Get user's group
        UserGroup group = (UserGroup) request.getAttribute("group");
        // The operation depends on the user's group
        if (group.equals(UserGroup.ADMIN)) {
            // Get UserInfo matching the given username
            user = this.service.getUserInfoByUsername(username);
        } else if (group.equals(UserGroup.LOCAL_ADMIN)) {
            // Get Owner object related to the user
            Owner owner = (Owner) request.getAttribute("owner");
            // Get UserInfo matching the given username, user group and Owner
            user = this.service.getUserInfoByUsername(username, owner, UserGroup.USER);
        }
        // Check for null value
        if (user == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Return the UserInfo
        return this.mapConverter.convert(user);
    }

    @RequestMapping(value = "{username}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map delete(HttpServletRequest request, HttpServletResponse response, @PathVariable String username) throws OperationFailedException, ResourceNotFoundException {
        // UserInfo object
        UserInfo user = null;
        // Get user's group
        UserGroup group = (UserGroup) request.getAttribute("group");
        // The operation depends on the user's group
        if (group.equals(UserGroup.ADMIN)) {
            // Get UserInfo matching the given username
            user = this.service.getUserInfoByUsername(username);
        } else if (group.equals(UserGroup.LOCAL_ADMIN)) {
            // Get Owner object related to the user
            Owner owner = (Owner) request.getAttribute("owner");
            // Get UserInfo matching the given username, user group and Owner
            user = this.service.getUserInfoByUsername(username, owner, UserGroup.USER);
        }
        // Check for null value
        if (user == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }

        // Try to delete the UserInfo
        if (!this.service.delete(user)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "user");
        // Add id
        result.put("resource_id", username);
        // Return result
        return result;
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map create(HttpServletRequest request, HttpServletResponse response,
            @RequestBody UserInfo user, BindingResult results) throws ValidationException, OperationFailedException {
        // Get operator id
        String operator = (String) request.getAttribute("operator");
        // Get user's group
        UserGroup group = (UserGroup) request.getAttribute("group");

        // Local admins can create users related their own Owner only
        if (group.equals(UserGroup.LOCAL_ADMIN)) {
            // Get Owner object related to the user
            Owner owner = (Owner) request.getAttribute("owner");
            // Set owner to the User
            user.getUser().setOwner(owner);
            // Set group to 'user'
            user.setGroup(UserGroup.USER);
        }

        // Set create operator
        user.getUser().setCreator(operator);

        // Validate UserInfo object
        this.validator.validate(user, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.create.failed"), results);
        }

        // Try to create a new user
        if (!this.service.create(user)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.create.failed"));
        }

        // Return the created user
        return this.mapConverter.convert(user);
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "{username}", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map update(HttpServletRequest request, HttpServletResponse response,
            @RequestBody UserInfo user, BindingResult results, @PathVariable String username) throws ValidationException, OperationFailedException, ResourceNotFoundException {
        // Get operator id
        String operator = (String) request.getAttribute("operator");
        // Get user's group
        UserGroup group = (UserGroup) request.getAttribute("group");
        // Variable for old UserInfo object
        UserInfo oldUser = null;

        // The operation depends on the user's group
        if (group.equals(UserGroup.ADMIN)) {
            // Get UserInfo matching the given username
            oldUser = this.service.getUserInfoByUsername(username);
        } else if (group.equals(UserGroup.LOCAL_ADMIN)) {
            // Get Owner object related to the user
            Owner owner = (Owner) request.getAttribute("owner");
            // Set owner to the User
            user.getUser().setOwner(owner);
            // Set group to 'user'
            user.setGroup(UserGroup.USER);
            // Get UserInfo matching the given username, user group and Owner
            oldUser = this.service.getUserInfoByUsername(username, owner, UserGroup.USER);
        }

        // Check for null value
        if (oldUser == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Set UserInfo id
        user.setId(oldUser.getId());
        // Set password - new password is updated by the service object
        user.getUser().setPassword(oldUser.getUser().getPassword());
        // Set username - it can not be changed after creation
        user.getUser().setUsername(oldUser.getUser().getUsername());
        // Set update operator
        user.getUser().setUpdater(operator);
        // Set creator
        user.getUser().setCreator(oldUser.getUser().getCreator());
        // Set created at
        user.getUser().setCreated(oldUser.getUser().getCreated());

        // Validate UserInfo object
        this.validator.validate(user, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // The operation depends on the user's group
        if (group.equals(UserGroup.ADMIN)) {
            // Try to update User and UserInfo
            if (!this.service.update(user.getUser()) || !this.service.update(user)) {
                // Throw exception
                throw new OperationFailedException(getMessage("rest.update.failed"));
            }
        } else if (group.equals(UserGroup.LOCAL_ADMIN)) {
            // Try to update User
            if (!this.service.update(user.getUser())) {
                // Throw exception
                throw new OperationFailedException(getMessage("rest.update.failed"));
            }
        }

        // Return the updated subject matter in JSON
        return this.mapConverter.convert(user);
    }
}
