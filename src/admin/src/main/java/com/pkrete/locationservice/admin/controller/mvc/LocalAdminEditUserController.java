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

import com.pkrete.locationservice.admin.exception.ObjectNotFoundException;
import com.pkrete.locationservice.admin.exception.OperationFailedException;
import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.model.user.UserInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * The {@link LocalAdminEditUserController LocalAdminEditUserController} handles 
 * the requests that are related to editing an existing user. This class extends
 * BaseController class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/ladmedituser.htm")
@SessionAttributes("userInfo")
public class LocalAdminEditUserController extends BaseController {

    @Autowired
    @Qualifier("userInfoValidator")
    private Validator validator;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("userInfo") UserInfo userInfo,
            BindingResult result) throws Exception {

        User currentUser = getUser(request);     
        // Set owner - must match with the updater
        userInfo.getUser().setOwner(currentUser.getOwner());
        // Set user group - must be "user"
        userInfo.setGroup(UserGroup.USER);

        validator.validate(userInfo, result);

        if (result.hasErrors()) {
            return new ModelAndView("edit_user_ladm", new ModelMap());
        }

        // Set updater
        userInfo.getUser().setUpdater(currentUser.getUsername());
        // Update User, no need to update UserInfo, because local admin
        // can not update user group
        if (!usersService.update(userInfo.getUser())) {
            throw new Exception("Updating user failed.");
        }
        String userId = request.getParameter("select_user");
        // No need to update the User object in the Session, because
        // local admin can not update his own profile through this
        // controller

        return new ModelAndView("redirect:ladmuser.htm?select_user=" + userId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) throws Exception {
        User currentUser = getUser(request);
        String id = request.getParameter("select_user");
        UserInfo userInfo = usersService.getUserInfoByUsername(id, currentUser.getOwner());
        if (userInfo == null) {
            throw new ObjectNotFoundException(new StringBuilder("User not found. {\"id\":").append(id).append("}").toString());
        }
        if (!userInfo.getGroup().equals(UserGroup.USER)) {
            throw new OperationFailedException("Editing user forbidden - insufficient rights : wrong user group.");
        }
        model.put("userInfo", userInfo);
        return "edit_user_ladm";
    }
}
