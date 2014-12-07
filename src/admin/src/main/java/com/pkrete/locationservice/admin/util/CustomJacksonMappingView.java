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
package com.pkrete.locationservice.admin.util;

import java.util.Map;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * This class will make sure that if there is a single object to transform to
 * JSON, it won't be rendered inside a map.
 *
 * @author Petteri Kivimäki
 */
public class CustomJacksonMappingView extends MappingJackson2JsonView {

    @SuppressWarnings("unchecked")
    @Override
    protected Object filterModel(Map<String, Object> model) {
        Object result = super.filterModel(model);
        if (!(result instanceof Map)) {
            return result;
        }

        Map map = (Map) result;
        if (map.size() == 1) {
            if (map.get("data") == null) {
                map.remove("data");
                return map;
            }
            return map.values().toArray()[0];
        }
        return map;
    }
}
