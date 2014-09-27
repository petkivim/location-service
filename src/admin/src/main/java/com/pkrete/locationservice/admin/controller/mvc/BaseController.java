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
package com.pkrete.locationservice.admin.controller.mvc;

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.service.UsersService;
import com.pkrete.locationservice.admin.util.UsersUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * The {@link BaseController BaseController} class implements
 * some basic functionality that is shared by all the controllers used for
 * form processing. This is an abstract base class that all the controllers of the
 * application can extend. This class doesn't have any abstract methods
 * though.
 *
 * @author Petteri Kivimäki
 */
public class BaseController {
    @Autowired
    @Qualifier("usersService")
    protected UsersService usersService;
    
    protected User getUser(HttpServletRequest request) {
        return UsersUtil.getUser(request, usersService);
    }

    protected Owner getOwner(HttpServletRequest request) {
        return UsersUtil.getUser(request, usersService).getOwner();
    }
}
