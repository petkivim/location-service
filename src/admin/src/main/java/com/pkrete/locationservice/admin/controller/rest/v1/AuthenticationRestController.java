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

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This class provides REST API for authenticating users. Authentication is
 * handled by {@link RestAuthenticationInterceptor RestAuthenticationInterceptor}.
 *   
 * /authenticate      [POST]
 * 
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/authenticate")
public class AuthenticationRestController extends RestController {

    @RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map authenticate(HttpServletRequest request) {
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("success", "true");
        // Return the list
        return result;
    }
}
