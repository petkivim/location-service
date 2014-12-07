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
package com.pkrete.locationservice.admin.interceptor;

import com.pkrete.locationservice.admin.exception.AuthenticationException;
import com.pkrete.locationservice.admin.model.user.UserFull;
import com.pkrete.locationservice.admin.converter.EncryptionService;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.service.UsersService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This interceptor checks that the credentials that are provided as request
 * parameters are valid.
 *
 * @author Petteri Kivimäki
 */
public class RestAuthenticationInterceptor extends HandlerInterceptorAdapter {

    private final static Logger logger = LoggerFactory.getLogger(RestAuthenticationInterceptor.class.getName());
    private UsersService usersService;
    private EncryptionService encryptionService;
    private MessageSource messageSource;
    private Map allowedGroups;

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setAllowedGroups(Map allowedGroups) {
        this.allowedGroups = allowedGroups;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        // Get username and password parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Check for null values
        if (username != null && password != null) {
            // Get full user with password
            UserFull user = this.usersService.getFullUser(username);
            // If user is null, throw an exception
            if (user == null) {
                logger.warn("Authenticating user \"{}\" failed! Reason : unknown user.", username);
                throw new AuthenticationException(this.messageSource.getMessage("rest.invalid.authentication", null, null));
            }
            // Validate password
            if (user.getPassword().equals(this.encryptionService.encrypt(password))) {
                if (logger.isInfoEnabled()) {
                    logger.info("User \"{}\" was authenticated succesfully.", username);
                }
                // Add Owner object to the request
                request.setAttribute("owner", user.getOwner());
                // Add operator attribute to the request
                request.setAttribute("operator", user.getUsername());
                // Check the user's group, if allowedGroups is set
                if (this.allowedGroups != null) {
                    // Get user's user group
                    UserGroup group = this.usersService.getUserGroup(username);
                    // Is the user's group in the allowed groups list?
                    if (group == null || !this.allowedGroups.containsKey(group.toString())) {
                        logger.warn("Access denied from user \"{}\"! Reason : wrong user group.", username);
                        throw new AuthenticationException(this.messageSource.getMessage("rest.invalid.authentication.denied", null, null));
                    }
                    // Add group attribute to the request
                    request.setAttribute("group", group);
                }
                // Match -> continue handler execution chain
                return true;
            }
            logger.warn("Authenticating user \"{}\" failed! Reason : invalid password.", username);

        } else {
            logger.warn("Authenticating user failed! Reason : username and/or password missing.");
        }
        throw new AuthenticationException(this.messageSource.getMessage("rest.invalid.authentication", null, null));
    }
}
