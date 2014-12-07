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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;
import org.apache.commons.validator.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class offers methods for validating URL and email addresses. All the
 * methods in this class are static and they can be used for validating user
 * input.
 *
 * @author Petteri Kivimäki
 */
public class WebUtil {

    private final static Logger logger = LoggerFactory.getLogger(WebUtil.class.getName());
    /* Regexp pattern for valid email address. */
    private static final Pattern rfc2822 = Pattern.compile(
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    /* Regexp pattern for hex colors. */
    private static final Pattern HEX_PATTERN = Pattern.compile(
            "^(#|)([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");

    /**
     * Checks if the given email address is valid.
     *
     * @param email email address to be checked
     * @return if valid returns true, otherwise returns false
     */
    public static boolean validateEmail(String email) {
        return rfc2822.matcher(email).matches();
    }

    /**
     * Checks if the given string is a valid hex color.
     *
     * @param color string presentation of a hex color
     * @return if valid returns true, otherwise returns false
     */
    public static boolean validateHexColor(String color) {
        return HEX_PATTERN.matcher(color).matches();
    }

    /**
     * Checks if the given URL exists.
     *
     * @param url the URL to be checked
     * @return if the URL is exists returns true, otherwise returns false
     */
    public static boolean exists(String url) {
        try {
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection con
                    = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            // HTTP statuses 200 and 302 are accepted
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * Checks the form of the given URL address.
     *
     * @param url the URL to be checked
     * @return if the URL is badly formed returns false, otherwise returns true
     */
    public static boolean validateUrl(String url) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }
}
