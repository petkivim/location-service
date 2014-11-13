/**
 * This file is part of Location Service :: Endpoint. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.comparator;

import com.pkrete.locationservice.endpoint.model.location.Location;
import java.util.Comparator;

/**
 * A comparison function, which imposes a total ordering on a collection of
 * Location objects. The ordering is based on the call numbers of the objects.
 *
 * @author Petteri Kivimäki
 */
public class LocationComparator implements Comparator {

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
        Location l1 = (Location) o1;
        Location l2 = (Location) o2;
        return l1.getCallNo().compareTo(l2.getCallNo());
    }
}
