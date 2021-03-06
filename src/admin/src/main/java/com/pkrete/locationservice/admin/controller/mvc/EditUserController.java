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
package com.pkrete.locationservice.admin.controller.mvc;

import com.pkrete.locationservice.admin.editor.OwnerEditor;
import com.pkrete.locationservice.admin.editor.UserGroupEditor;
import com.pkrete.locationservice.admin.exception.ObjectNotFoundException;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.model.user.UserInfo;
import com.pkrete.locationservice.admin.service.OwnersService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * The {@link EditUserController EditUserController} handles the requests that
 * are related to adding a new user to the database. This class extends
 * BaseController class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/edituser.htm")
@SessionAttributes("userInfo")
public class EditUserController extends BaseController {

    @Autowired
    @Qualifier("ownersService")
    private OwnersService ownersService;
    @Autowired
    @Qualifier("ownerEditor")
    private OwnerEditor ownerEditor;
    @Autowired
    @Qualifier("userGroupEditor")
    private UserGroupEditor userGroupEditor;
    @Autowired
    @Qualifier("userInfoValidator")
    private Validator validator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(UserGroup.class, userGroupEditor);
        binder.registerCustomEditor(Owner.class, ownerEditor);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("userInfo") UserInfo userInfo,
            BindingResult result) throws Exception {
        validator.validate(userInfo, result);

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            this.setReferenceData(request, model);
            return new ModelAndView("edit_user", model);
        }
        String userId = request.getParameter("select_user");

        userInfo.getUser().setUpdater(getUser(request).getUsername());
        // Updates only User
        if (!usersService.update(userInfo.getUser())) {
            throw new Exception("Updating user failed.");
        }
        // Updates only UserInfor
        if (!usersService.update(userInfo)) {
            throw new Exception("Updating user info failed.");
        }

        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.setAttribute("user", usersService.getUser(request.getRemoteUser()));

        return new ModelAndView("redirect:userowner.htm?select_user=" + userId);
    }

    protected void setReferenceData(HttpServletRequest request, ModelMap modelMap) {
        modelMap.put("owners", ownersService.getOwners());
        modelMap.put("groups", UserGroup.values());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) throws Exception {
        String id = request.getParameter("select_user");
        UserInfo user = usersService.getUserInfoByUsername(id);
        if (user == null) {
            throw new ObjectNotFoundException(new StringBuilder("User not found. {\"id\":").append(id).append("}").toString());
        }
        model.put("userInfo", user);
        this.setReferenceData(request, model);
        return "edit_user";
    }
}
