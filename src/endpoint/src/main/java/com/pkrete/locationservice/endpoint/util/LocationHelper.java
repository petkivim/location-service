/**
 * This file is part of Location Service :: Endpoint.
 * Copyright (C) 2014 Petteri Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.util;

import java.text.RuleBasedCollator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class offers helper methods for handling Location objects.
 * 
 * @author Petteri Kivimäki
 */
public class LocationHelper {

    /**
     * Compares the given call numbers and returns true if and only if the
     * whole callno1 can be found from the beginning of callno2. Whole words 
     * are compared which means that 'Lib Col 001' and 'Lib Col 001 Author' 
     * match, but 'Lib Col 001' and 'Lib Col 0010 Author' do not match.
     * @param callno1 call number to compare
     * @param callno2 call number to compare
     * @return true or false
     */
    public static boolean match(String callno1, String callno2) {
        if (callno2.length() < callno1.length()) {
            return false;
        } else if (callno1.compareTo(callno2.substring(0, callno1.length())) == 0) {
            if (callno1.length() == callno2.length() || callno2.charAt(callno1.length()) == ' ') {
                return true;
            }
        }
        return false;
    }

    /**
     * Compares the given call numbers and returns true if and only if the
     * whole callno1 can be found from the beginning of callno2. Substrings are 
     * compared which means that 'Lib Col 001' and 'Lib Col 001 Author' match, 
     * and 'Lib Col 001' and 'Lib Col 0010 Author' match too. 
     * @param callno1 call number to compare
     * @param callno2 call number to compare
     * @return true or false
     */
    public static boolean matchSub(String callno1, String callno2) {
        if (callno2.length() < callno1.length()) {
            return false;
        } else if (callno1.compareTo(callno2.substring(0, callno1.length())) == 0) {
            return true;
        }
        return false;
    }

    /**
     * Compares the given call numbers and returns true if and only if the
     * callno2 is between the interval defined in callno1. The given pattern
     * defines the syntax of the interval definition.
     * @param callno1 call number to compare
     * @param callno2 call number to compare
     * @param pattern syntax of the interval definition
     * @param collator RuleBasedCollator that's used for call number comparisons
     * @return  true or false
     */
    public static boolean match(String callno1, String callno2, Pattern pattern, RuleBasedCollator collator) {
        // Regex matcher
        Matcher matcher = pattern.matcher(callno1);
        // Temp variables for callno2, begin and end need their own variables
        String callno2Start = callno2;
        String callno2End = callno2;
        // Does callno1 match the given pattern
        if (matcher.find()) {
            // Get base of the call number = library + collection
            String base = callno1.replaceAll(pattern.pattern(), "");
            // Interval begin
            String start = base + matcher.group(1);
            // Interval end
            String end = base + matcher.group(2);
            // Callno1 base must be found from the beginning of callno2
            if(!LocationHelper.match(base.trim(), callno2)) {
                return false;
            }
            // Callno2Begin can not be longer than interval begin
            if (callno2Start.length() > start.length()) {
                callno2Start = new String(callno2Start.substring(0, start.length()));
            }
            // Interval end can't be longer than callno2End
            if (callno2End.length() < end.length()) {
                end = new String(end.substring(0, callno2End.length()));
            }
            // If callno2Start is smaller than interval start, return false
            if (collator.compare(callno2Start, start) < 0) {
                return false;
            }
            // If callno2End is greater than interval end, return false
            if (collator.compare(new String(callno2End.substring(0, end.length())), end) > 0) {
                return false;
            }
            // Callno2 is between interval begin and interval end, return true
            return true;
        }
        // Return false
        return false;
    }
}
