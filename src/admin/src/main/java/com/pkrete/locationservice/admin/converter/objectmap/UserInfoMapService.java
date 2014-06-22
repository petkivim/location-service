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
package com.pkrete.locationservice.admin.converter.objectmap;

import com.pkrete.locationservice.admin.model.user.UserInfo;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.util.DateTimeUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class converts UserInfo objects to Map.
 * 
 * @author Petteri Kivimäki
 */
public class UserInfoMapService implements ObjectMapService<UserInfo> {

    /**
     * Converts a single UserInfo object to Map object. All the variables of
     * the UserInfo object are included.
     * @param source UserInfo object to be converted
     * @return Map object
     */
    public Map convert(UserInfo source) {
        return this.convert(source, false);
    }

    /**
     * Converts a single UserInfo object to Map object. All the variables of
     * the UserInfo object are included.
     * @param source UserInfo object to be converted
     * @param logEntry is this for a log entry, ignored
     * @return Map object
     */
    public Map convert(UserInfo source, boolean logEntry) {
        Map user = new LinkedHashMap();
        user.put("username", source.getUser().getUsername());
        user.put("first_name", source.getUser().getFirstName());
        user.put("last_name", source.getUser().getLastName());
        user.put("email", source.getUser().getEmail());
        user.put("owner_id", source.getUser().getOwner().getId());
        user.put("organization", source.getUser().getOrganization());
        user.put("group", source.getGroup());
        user.put("created_at", DateTimeUtil.dateToString(source.getUser().getCreated()));
        user.put("create_operator", source.getUser().getCreator());
        user.put("updated_at", (source.getUser().getUpdated() == null ? "" : DateTimeUtil.dateToString(source.getUser().getUpdated())));
        user.put("update_operator", (source.getUser().getUpdater() == null ? "" : source.getUser().getUpdater()));
        return user;
    }

    /**
     * Converts a list of UserInfo objects to a list of Map objects. Only 
     * selected variables are included.
     * @param sources UserInfo objects to be converted
     * @return list of Map objects
     */
    public List convert(List<UserInfo> sources) {
        List users = new ArrayList();
        for (UserInfo source : sources) {
            Map user = new LinkedHashMap();
            user.put("username", source.getUser().getUsername());
            user.put("first_name", source.getUser().getFirstName());
            user.put("last_name", source.getUser().getLastName());
            user.put("owner_id", source.getUser().getOwner().getId());
            user.put("group", source.getGroup());
            users.add(user);
        }
        return users;
    }
}
