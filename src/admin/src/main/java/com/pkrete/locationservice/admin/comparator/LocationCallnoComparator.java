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

import com.pkrete.locationservice.admin.model.location.Location;
import java.util.Comparator;

/**
 * This class implements a comparison function, which imposes an ordering of two
 * Location objects by call number.
 *
 * @author Petteri Kivimäki
 */
public class LocationCallnoComparator implements Comparator<Location> {

    /**
     * Compares two Location objects by call number.
     *
     * @param o1 Location object
     * @param o2 Location object
     * @returns the value 0 if o1 is equal to o2; value less than 0 if o1 is
     * less than o2 and value greater than 0 if o1 is greater than o2
     */
    @Override
    public int compare(Location o1, Location o2) {
        return o1.getCallNo().compareTo(o2.getCallNo());
    }
}
