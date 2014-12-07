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
package com.pkrete.locationservice.admin.comparator;

import com.pkrete.locationservice.admin.util.DateTimeUtil;
import java.util.Comparator;
import java.util.Date;

/**
 * A comparison function, which imposes a total ordering on a collection of
 * Object array objects.
 *
 * @author Petteri Kivimäki
 */
public class StatisticComparator implements Comparator {

    /**
     * Compares its two arguments for order. Returns a negative integer, zero,
     * or a positive integer as the first argument is less than, equal to, or
     * greater than the second.
     *
     * @param o1 the first object to be compared
     * @param o2 the second object to be compared
     * @return a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second
     */
    @Override
    public int compare(Object o1, Object o2) {
        Object[] arr1 = (Object[]) o1;
        Object[] arr2 = (Object[]) o2;
        try {
            if (((String) arr1[2]).matches("\\d{1,2}\\.\\d{1,2}\\.\\d{4}")) {
                Date date1 = DateTimeUtil.stringToDate((String) arr1[2], "dd.MM.yyyy");
                Date date2 = DateTimeUtil.stringToDate((String) arr2[2], "dd.MM.yyyy");
                return date1.compareTo(date2);
            } else if (((String) arr1[2]).matches("\\d{1,2}\\/\\d{4}")) {
                Date date1 = DateTimeUtil.stringToDate((String) arr1[2], "MM/yyyy");
                Date date2 = DateTimeUtil.stringToDate((String) arr2[2], "MM/yyyy");
                return date1.compareTo(date2);
            } else if (((String) arr1[2]).matches("\\d{4}")) {
                Date date1 = DateTimeUtil.stringToDate((String) arr1[2], "yyyy");
                Date date2 = DateTimeUtil.stringToDate((String) arr2[2], "yyyy");
                return date1.compareTo(date2);
            }
        } catch (ClassCastException cce) {
            Date date1 = DateTimeUtil.stringToDate(Integer.toString((Integer) arr1[2]), "yyyy");
            Date date2 = DateTimeUtil.stringToDate(Integer.toString((Integer) arr2[2]), "yyyy");
            return date1.compareTo(date2);
        }
        return 0;
    }
}
