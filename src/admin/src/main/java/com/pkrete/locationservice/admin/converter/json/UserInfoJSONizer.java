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
package com.pkrete.locationservice.admin.converter.json;

import com.pkrete.locationservice.admin.model.user.UserInfo;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import java.util.List;

/**
 * This class converts UserInfo objects to JSON.
 *
 * @author Petteri Kivimäki
 */
public class UserInfoJSONizer implements JSONizerService<UserInfo> {

    /**
     * Converts a single UserInfo object to JSON string. All the variables of
     * the UserInfo object are included.
     *
     * @param source UserInfo object to be converted
     * @return JSON string
     */
    @Override
    public String jsonize(UserInfo source) {
        return this.jsonize(source, false);
    }

    /**
     * Converts a single UserInfo object to JSON string. All the variables of
     * the UserInfo object are included.
     *
     * @param source UserInfo object to be converted
     * @param logEntry, parameter ignored
     * @return JSON string
     */
    @Override
    public String jsonize(UserInfo source, boolean logEntry) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"username\":\"").append(source.getUser().getUsername()).append("\"");
        builder.append(",\"owner_id\":").append(source.getUser().getOwner().getId());
        builder.append(",\"group\":\"").append(source.getGroup()).append("\"");
        builder.append("}");
        return builder.toString();
    }

    /**
     * Converts a list of UserInfo objects to JSON string. Only selected
     * variables are included.
     *
     * @param sources UserInfo objects to be converted
     * @return JSON string
     */
    @Override
    public String jsonize(List<UserInfo> sources) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < sources.size(); i++) {
            UserInfo temp = sources.get(i);
            builder.append("{\"username\":\"").append(temp.getUser().getUsername()).append("\"");
            builder.append(",\"group\":\"").append(temp.getGroup()).append("\"");
            builder.append("}");
            if (i < sources.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
