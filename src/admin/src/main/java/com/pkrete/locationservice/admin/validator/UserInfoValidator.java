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
package com.pkrete.locationservice.admin.validator;

import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.model.user.UserInfo;
import com.pkrete.locationservice.admin.service.UsersService;
import com.pkrete.locationservice.admin.util.WebUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * The {@link UserInfoValidator UserInfoValidator} class validates the values
 * inserted via adduser and edituser forms. This class defines the obligatory
 * fields and returns an error message associated with the field when the value
 * of the field is not valid.
 *
 * @author Petteri Kivimäki
 */
public class UserInfoValidator implements Validator {

    private UsersService usersService;
    private int passwordMinLength = 5;

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public void setPasswordMinLength(int passwordMinLength) {
        this.passwordMinLength = passwordMinLength;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserInfo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserInfo info = (UserInfo) target;
        ValidationUtils.rejectIfEmpty(errors, "user.firstName", "error.user.firstname.required");
        ValidationUtils.rejectIfEmpty(errors, "user.lastName", "error.user.lastname.required");
        ValidationUtils.rejectIfEmpty(errors, "user.username", "error.user.username.required");
        ValidationUtils.rejectIfEmpty(errors, "user.email", "error.user.email.required");
        ValidationUtils.rejectIfEmpty(errors, "user.organization", "error.user.organization.required");

        if (info.getUser().getFirstName().length() > 100) {
            errors.rejectValue("user.firstName", "error.user.firstname.length");
        }

        if (info.getUser().getLastName().length() > 100) {
            errors.rejectValue("user.lastName", "error.user.lastname.length");
        }

        if (info.getUser().getUsername().length() > 15) {
            errors.rejectValue("user.username", "error.user.username.length");
        }

        if (info.getUser().getEmail().length() > 100) {
            errors.rejectValue("user.email", "error.user.email.length");
        }

        if (info.getUser().getOrganization().length() > 100) {
            errors.rejectValue("user.organization", "error.user.organization.length");
        }

        if (info.getUser().getPasswordUi().length() > 100) {
            errors.rejectValue("user.passwordUi", "error.user.password.length");
        }

        if (info.getUser().getPasswordControl().length() > 100) {
            errors.rejectValue("user.passwordControl", "error.user.password.length");
        }

        if (!info.getUser().getEmail().isEmpty()) {
            if (!WebUtil.validateEmail(info.getUser().getEmail())) {
                errors.rejectValue("user.email", "error.user.email.invalid");
            }
        }

        if (info.getUser().getPassword().isEmpty()) {
            if (info.getUser().getPasswordUi().isEmpty() && info.getUser().getPasswordControl().isEmpty()) {
                errors.rejectValue("user.passwordUi", "error.user.password.required");
            }
        }

        if (!info.getUser().getPasswordUi().equals(info.getUser().getPasswordControl())) {
            errors.rejectValue("user.passwordUi", "error.user.password.match");
            if (info.getUser().getPasswordControl().isEmpty()) {
                errors.rejectValue("user.passwordControl", "error.user.password.confirmation.required");
            }
        }

        if (!info.getUser().getPasswordUi().isEmpty() && info.getUser().getPasswordUi().length() < this.passwordMinLength) {
            errors.rejectValue("user.passwordUi", "error.userinfo.password.new.length");
        }

        if (info.getGroup() == null) {
            errors.rejectValue("group", "error.user.usergroup.required");
        }
        if (info.getUser().getOwner() == null) {
            errors.rejectValue("user.owner", "error.user.password.owner.required");
        }
        if (info.getId() == 0) {
            if (usersService.getUser(info.getUser().getUsername()) != null) {
                errors.rejectValue("user.username", "error.user.username.exist");
            }
        } else {
            User temp = usersService.getUser(info.getUser().getUsername());
            if (temp != null) {
                UserInfo tempInfo = usersService.getUserInfoByUsername(temp.getUsername());
                if (!info.equals(tempInfo)) {
                    errors.rejectValue("user.username", "error.user.username.exist");
                }
            }

        }
    }
}
