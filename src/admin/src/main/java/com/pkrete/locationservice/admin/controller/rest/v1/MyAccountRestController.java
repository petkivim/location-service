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
import com.pkrete.locationservice.admin.model.user.UserInfo;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.service.UsersService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.Validator;

/**
 * This class provides REST API to users for accessing their own account. Users
 * can read and update their account.
 *
 * READ /myaccount [GET] UPDATE /myaccount [PUT]
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/myaccount")
public class MyAccountRestController extends RestController {

    private final static Logger logger = LoggerFactory.getLogger(UsersRestController.class.getName());
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
    public Map index(HttpServletRequest request) throws ResourceNotFoundException {
        // Get operator id
        String operator = (String) request.getAttribute("operator");
        // Get UserInfo matching the given operator
        UserInfo user = this.service.getUserInfoByUsername(operator);
        // Check for null value
        if (user == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Return the userwww
        return this.mapConverter.convert(user);
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map update(HttpServletRequest request, HttpServletResponse response,
            @RequestBody UserInfo user, BindingResult results) throws ValidationException, OperationFailedException, ResourceNotFoundException {
        // Get operator id
        String operator = (String) request.getAttribute("operator");
        // Get UserInfo matching the given username
        UserInfo oldUser = this.service.getUserInfoByUsername(operator);

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
        // Set owner
        user.getUser().setOwner(oldUser.getUser().getOwner());
        // Set group
        user.setGroup(oldUser.getGroup());
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

        // Try to update User
        if (!this.service.update(user.getUser())) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Return the updated subject matter in JSON
        return this.mapConverter.convert(user);
    }
}
