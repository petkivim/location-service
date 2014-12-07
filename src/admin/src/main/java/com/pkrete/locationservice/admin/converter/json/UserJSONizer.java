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

import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import com.pkrete.locationservice.admin.util.DateTimeUtil;
import java.util.List;

/**
 * This class converts User objects to JSON.
 *
 * @author Petteri Kivimäki
 */
public class UserJSONizer implements JSONizerService<User> {

    /**
     * Converts a single User object to JSON string. All the variables of the
     * User object are included.
     *
     * @param source User object to be converted
     * @return JSON string
     */
    @Override
    public String jsonize(User source) {
        return this.jsonize(source, false);
    }

    /**
     * Converts a single User object to JSON string. All the variables of the
     * User object are included.
     *
     * @param source User object to be converted
     * @param logEntry, parameter ignored
     * @return JSON string
     */
    @Override
    public String jsonize(User source, boolean logEntry) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"username\":\"").append(source.getUsername()).append("\"");
        builder.append(",\"first_name\":\"").append(source.getFirstName()).append("\"");
        builder.append(",\"last_name\":\"").append(source.getLastName()).append("\"");
        builder.append(",\"email\":\"").append(source.getEmail()).append("\"");
        builder.append(",\"owner_id\":").append(source.getOwner().getId());
        builder.append(",\"organization\":\"").append(source.getOrganization()).append("\"");
        builder.append(",\"created_at\":\"").append(DateTimeUtil.dateToString(source.getCreated())).append("\"");
        builder.append(",\"create_operator\":\"").append(source.getCreator()).append("\"");
        builder.append(",\"updated_at\":\"").append((source.getUpdated() == null ? "" : DateTimeUtil.dateToString(source.getUpdated()))).append("\"");
        builder.append(",\"update_operator\":\"").append((source.getUpdater() == null ? "" : source.getUpdater())).append("\"");
        builder.append("}");
        return builder.toString();
    }

    /**
     * Converts a list of User objects to JSON string. Only selected variables
     * are included.
     *
     * @param sources UserInfo objects to be converted
     * @return JSON string
     */
    @Override
    public String jsonize(List<User> sources) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < sources.size(); i++) {
            User temp = sources.get(i);
            builder.append("{\"username\":\"").append(temp.getUsername()).append("\"");
            builder.append(",\"first_name\":\"").append(temp.getFirstName()).append("\"");
            builder.append(",\"last_name\":\"").append(temp.getLastName()).append("\"");
            builder.append(",\"owner_id\":").append(temp.getOwner().getId());
            builder.append("}");
            if (i < sources.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
