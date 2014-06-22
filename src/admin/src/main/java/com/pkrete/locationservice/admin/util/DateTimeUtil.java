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
package com.pkrete.locationservice.admin.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * This class offers helper methods for date and time processing
 * and conversions.
 *
 * @author Petteri Kivimäki
 */
public class DateTimeUtil {

    private final static Logger logger = Logger.getLogger(DateTimeUtil.class.getName());

    /**
     * Converts the given date string to the right format that it can be
     * used in a database query. If the date string is a begin date, time
     * is set to 00:00:00, and if the date string is an end date, time
     * is set to 23:59:59. This method is mainly meant for modifying dates
     * received from the UI, which contain only date, not time.
     * @param isBegin tells if the date is a begin data
     * @param dateStr string presentation of a date
     * @return dateStr as a Date object
     */
    public static String convertDate(boolean isBegin, String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        String time = "00:00:00";
        if (!isBegin) {
            time = "23:59:59";
        }
        dateStr += " " + time;
        try {
            /* Parse the date value received from UI. */
            DateFormat formatter;
            formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            Date date = formatter.parse(dateStr);

            /* Convert the date to the right format for DB query. */
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(date);
        } catch (ParseException e) {
            logger.error(e);
            return null;
        }
    }

    /**
     * Converts the given date string to a Date object.
     * @param dateStr date string
     * @param format date format
     * @return date string as a Date object
     */
    public static Date stringToDate(String dateStr, String format) {
        try {
            /* Parse the date value received as a parameter. */
            DateFormat formatter;
            formatter = new SimpleDateFormat(format);
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            logger.error(e);
            return null;
        }
    }

    /**
     * Creates a String presentation of the given date in the format
     * "yyyy-MM-dd HH:mm:ss".
     * @param date date to be presented as String
     * @return date string
     */
    public static String dateToString(Date date) {
        String format = "yyyy-MM-dd HH:mm:ss";
        return dateToString(date, format);
    }

    /**
     * Creates a String presentation of the given date according to the
     * given format.
     * @param date date to be presented as String
     * @param format date format
     * @return date string
     */
    public static String dateToString(Date date, String format) {
        /* Format the date according to the given format. */
        DateFormat formatter;
        formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * Returns the current timestamp in the format "yyyyMMdd.HHmmssSSS".
     * @return curren timestamp in the format "yyyyMMdd.HHmmssSSS"
     */
    public static String getTimestamp() {
        Date date = new java.util.Date();
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd.HHmmssSSS");
        return formatter.format(date);
    }
}
