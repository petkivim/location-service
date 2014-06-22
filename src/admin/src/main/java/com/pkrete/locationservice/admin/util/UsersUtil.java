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
package com.pkrete.locationservice.admin.util;

import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.service.UsersService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * This class offers helper methods related to User objects.
 * 
 * @author Petteri Kivimäki
 */
public class UsersUtil {

    private final static Logger logger = Logger.getLogger(UsersUtil.class.getName());

    /**
     * Returns the User object that is stored in the session related
     * to the given HTTP request. If the user cannot be found from the session
     * it's fetched from the DB by using the given service object,
     * @param request http request
     * @param service UsersService service object
     * @return User object stored in the session
     */
    public static User getUser(HttpServletRequest request, UsersService service) {
        User user = null;
        try {
            HttpSession session = request.getSession();
            user = (User) session.getAttribute("user");
            if (user == null) {
                user = service.getUser(request.getRemoteUser());
                session.setAttribute("user", user);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return user;
    }
}
