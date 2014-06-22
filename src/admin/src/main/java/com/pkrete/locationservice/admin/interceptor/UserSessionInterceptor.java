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
package com.pkrete.locationservice.admin.interceptor;

import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.service.UsersService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This interceptor checks that current user is stored in the HTTP session
 * before processing the given request. If the user isn't stored in the
 * session, the User object is fetched from the database and it's added
 * to the session. If fetching the user from the database fails, the
 * handler execution chain is stopped and the user is forwarded to the login
 * page. 
 * 
 * This interceptor also validates that the current User and remote user are 
 * the same. If the users don't match, the handler execution chain is stopped 
 * and the user is logged out.
 * 
 * @author Petteri Kivimäki
 */
public class UserSessionInterceptor extends HandlerInterceptorAdapter {

    private final static Logger logger = Logger.getLogger(UserSessionInterceptor.class.getName());
    private UsersService usersService;

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        // Get current HTTP session
        HttpSession session = request.getSession();
        // Get User object from the session
        User user = (User) session.getAttribute("user");
        // Get remote user from the request
        String remoteUser = request.getRemoteUser();
        // If User is null and remote user is not null,
        // remote user must be added to the session
        if (user == null && remoteUser != null) {
            // Get User object by remote user from DB
            user = usersService.getUser(request.getRemoteUser());
            // If User is still null, stop the handler exceution chain
            if (user == null) {
                logger.warn("Unable to find User object matching the remote user \"" + remoteUser + "\". Stop handler execution chain.");
                // Logout the current user by forwarding to logout controller
                request.getRequestDispatcher("/logout.htm").forward(request, response);
                // Returning false stops the handler execution chain
                return false;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Added User object to the session.");
            }
            // Add User object to the session
            session.setAttribute("user", user);
        } else if (user != null && remoteUser != null) {
            // Check that User and remote user are the same
            if (!user.getUsername().equals(remoteUser)) {
                logger.warn("Current user \"" + user.getUsername() + "\" and remote user \"" + remoteUser + "\" don't match. Stop handler execution chain.");
                // Logout the current user by forwarding to logout controller
                request.getRequestDispatcher("/logout.htm").forward(request, response);
                // Stop handler execution chain if they don't match
                return false;
            }
        }
        // Continue handler execution chain
        return true;
    }
}
