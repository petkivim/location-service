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

import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.model.user.UserFull;
import com.pkrete.locationservice.admin.converter.EncryptionService;
import com.pkrete.locationservice.admin.util.WebUtil;
import java.util.HashMap;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The {@link UserInfoController UserInfoController} handles the requests that
 * are related to changing user password and updating email address.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/userinfo.htm")
public class UserInfoController extends BaseController {

    @Autowired
    @Qualifier("encryptionService")
    private EncryptionService encryptionService;
    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = usersService.getUser(request.getRemoteUser());
            session.setAttribute("user", user);
        }

        /* Model that is returned together with the view */
        java.util.Map<String, Object> model = new HashMap<String, Object>();

        if (request.getParameter("save") != null) {
            boolean ok = true;
            String passOld = request.getParameter("password_old");
            String passNew = request.getParameter("password_new");
            String passNewControl = request.getParameter("password_new_control");

            if (passOld.isEmpty()) {
                ok = false;
                model.put("errorMsgPwOld", this.messageSource.getMessage("error.userinfo.password.old.missing", null, null));
            }
            if (passNew.isEmpty()) {
                ok = false;
                model.put("errorMsgPwNew", this.messageSource.getMessage("error.userinfo.password.new.missing", null, null));
            } else if (passNew.length() < 5) {
                ok = false;
                model.put("errorMsgPwNew", this.messageSource.getMessage("error.userinfo.password.new.length", null, null));
            } else if (!passNew.matches("\\w+")) {
                ok = false;
                model.put("errorMsgPwNew", this.messageSource.getMessage("error.userinfo.password.new.form", null, null));
            }
            if (passNewControl.isEmpty()) {
                ok = false;
                model.put("errorMsgPwCtrl", this.messageSource.getMessage("error.userinfo.password.control.missing", null, null));
            }
            if (!passNewControl.equals(passNew)) {
                ok = false;
                model.put("errorMsgPwNew", this.messageSource.getMessage("error.userinfo.password.new.match", null, null));
            }

            if (passNew.length() > 100) {
                ok = false;
                model.put("errorMsgPwNew", this.messageSource.getMessage("error.user.password.length", null, null));
            }

            if (passNewControl.length() > 100) {
                ok = false;
                model.put("errorMsgPwCtrl", this.messageSource.getMessage("error.user.password.length", null, null));
            }

            if (ok) {
                UserFull userFull = usersService.getFullUser(user.getUsername());
                String pass = this.encryptionService.encrypt(passOld);
                if (pass.equals(userFull.getPassword())) {
                    String passNewCrypted = this.encryptionService.encrypt(passNew);
                    if (!passNewCrypted.equals(userFull.getPassword())) {
                        userFull.setUpdater(user.getUsername());
                        userFull.setPasswordUi(passNew);
                        usersService.update(userFull);
                        model.put("responseMsg", this.messageSource.getMessage("response.userinfo.password.changed", null, null));
                        user.setUpdated(new Date());
                        user.setUpdater(user.getUsername());
                    } else {
                        model.put("errorMsgPwNew", this.messageSource.getMessage("error.userinfo.password.old.new.same", null, null));
                    }
                } else {
                    model.put("errorMsgPwOld", this.messageSource.getMessage("error.userinfo.password.old.match", null, null));
                }
            }
        } else if (request.getParameter("save_email") != null) {
            boolean ok = true;
            String email = request.getParameter("email");
            if (email.isEmpty()) {
                ok = false;
                model.put("errorMsgEmail", this.messageSource.getMessage("error.userinfo.email.empty", null, null));
            } else if (!WebUtil.validateEmail(email)) {
                ok = false;
                model.put("errorMsgEmail", this.messageSource.getMessage("error.userinfo.email.invalid", null, null));
            } else if (email.length() > 100) {
                ok = false;
                model.put("errorMsgEmail", this.messageSource.getMessage("error.user.email.length", null, null));
            }

            if (ok) {
                UserFull userFull = usersService.getFullUser(user.getUsername());
                userFull.setUpdater(user.getUsername());
                userFull.setEmail(email);
                usersService.update(userFull);
                user.setEmail(email);
                session.removeAttribute("user");
                session.setAttribute("user", user);
                model.put("responseMsg", this.messageSource.getMessage("response.userinfo.email.changed", null, null));
                user.setUpdated(new Date());
                user.setUpdater(user.getUsername());
            }
        }

        model.put("user", user);
        return new ModelAndView("user_info", "model", model);
    }
}
