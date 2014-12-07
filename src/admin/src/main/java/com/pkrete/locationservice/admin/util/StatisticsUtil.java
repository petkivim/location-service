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

import com.pkrete.locationservice.admin.model.statistics.SearchEventType;
import com.pkrete.locationservice.admin.model.statistics.StatisticsGroup;

/**
 * This class offers helper methods for handling statistics.
 *
 * @author Petteri Kivimäki
 */
public class StatisticsUtil {

    /**
     * Returns the StatisticsGroup object matching the given string. If no
     * matching StatisticsGroup can be found, StatisticsGroup.DAY is returned by
     * default.
     *
     * @param code statistics group code
     * @return StatisticsGroup object matching the given string
     */
    public static StatisticsGroup parseGroup(String code) {
        if (code == null) {
            return StatisticsGroup.DAY;
        }
        if (code.isEmpty()) {
            return StatisticsGroup.DAY;
        }
        if (code.equals("d")) {
            return StatisticsGroup.DAY;
        } else if (code.equals("m")) {
            return StatisticsGroup.MONTH;
        } else if (code.equals("y")) {
            return StatisticsGroup.YEAR;
        }
        return StatisticsGroup.DAY;
    }

    /**
     * Returns the SearchEventType object matching the given string. If no
     * matching SearchEventType can be found, SearchEventType.ALL is returned by
     * default.
     *
     * @param code search event type
     * @return SearchEventType object matching the given string
     */
    public static SearchEventType parseType(String type) {
        if (type == null) {
            return SearchEventType.ALL;
        } else if (type.equals("handler")) {
            return SearchEventType.LOCATION_HANDLER;
        } else if (type.equals("exporter")) {
            return SearchEventType.EXPORTER;
        }
        return SearchEventType.ALL;
    }

    /**
     * Adds a time to the given date string. The date must be presented in
     * "dd.MM.yyyy" format. The date string is a begin date of a date range, so
     * time is set to 00:00:00. The returned date string is in "yyyy-MM-dd
     * HH:mm:ss" format.
     *
     * @param date dateStr string presentation of a date in "dd.MM.yyyy" format
     * @return string presentation of a date in "yyyy-MM-dd HH:mm:ss" format
     */
    public static String parseFromDate(String date) {
        return DateTimeUtil.convertDate(true, date);
    }

    /**
     * Adds a time to the given date string. The date must be presented in
     * "dd.MM.yyyy" format. The date string is an end date of a date range, so
     * time is set to 23:59:59. The returned date string is in "yyyy-MM-dd
     * HH:mm:ss" format.
     *
     * @param date dateStr string presentation of a date in "dd.MM.yyyy" format
     * @return string presentation of a date in "yyyy-MM-dd HH:mm:ss" format
     */
    public static String parseToDate(String date) {
        return DateTimeUtil.convertDate(false, date);
    }
}
