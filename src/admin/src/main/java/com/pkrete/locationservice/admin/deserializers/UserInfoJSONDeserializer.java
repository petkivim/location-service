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
package com.pkrete.locationservice.admin.deserializers;

import com.pkrete.locationservice.admin.model.user.UserInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.user.UserFull;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.service.OwnersService;
import com.pkrete.locationservice.admin.util.ApplicationContextUtils;
import java.io.IOException;

/**
 * Custom deserializer for UserInfo objects.
 *
 * @author Petteri Kivimäki
 */
public class UserInfoJSONDeserializer extends JsonDeserializer<UserInfo> {

    @Override
    public UserInfo deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        // Get username
        String username = node.get("username") == null ? "" : node.get("username").textValue();
        // Get first name
        String firstName = node.get("first_name") == null ? "" : node.get("first_name").textValue();
        // Get last name
        String lastName = node.get("last_name") == null ? "" : node.get("last_name").textValue();
        //  Get email address
        String email = node.get("email") == null ? "" : node.get("email").textValue();
        // Get organization
        String organization = node.get("organization") == null ? "" : node.get("organization").textValue();
        // Get user group
        String groupStr = node.get("group") == null ? "" : node.get("group").textValue();
        // Get owner id
        int ownerId = node.get("owner_id") == null ? 0 : node.get("owner_id").intValue();
        // Get password
        String password = node.get("password") == null ? "" : node.get("password").textValue();
        // Get control password
        String passwordControl = node.get("password_control") == null ? "" : node.get("password_control").textValue();

        // Get usersService bean from Application Context
        ConverterService converterService = (ConverterService) ApplicationContextUtils.getApplicationContext().getBean("converterService");
        // Get UserGroup
        UserGroup group = (UserGroup) converterService.convert(groupStr, UserGroup.class, null);
        // Get usersService bean from Application Context
        OwnersService ownersService = (OwnersService) ApplicationContextUtils.getApplicationContext().getBean("ownersService");
        // Get Owner
        Owner owner = ownersService.getOwner(ownerId);

        // Create new UserFull object
        UserFull user = new UserFull();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setOrganization(organization);
        user.setPasswordUi(password);
        user.setPasswordControl(passwordControl);
        user.setOwner(owner);

        // Create new UserInfo object
        UserInfo info = new UserInfo();
        // Set user group
        info.setGroup(group);
        // Set user
        info.setUser(user);

        return info;
    }
}
