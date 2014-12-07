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
package com.pkrete.locationservice.admin.model.search;

/**
 * This enumeration represents the level of the search that is done through the
 * Search servlet.
 *
 * @author Petteri Kivimäki
 */
public enum SearchLevel {

    LIBRARY, COLLECTION, SHELF, ALL;

    /**
     * Converts the given SearchLevel to corresponding LocationType.
     * SearchLevel.ALL cannot be converted and null is returned.
     *
     * @param level SearchLevel to be converted
     * @return corresponding LocationType; SearchLevel.ALL cannot be converted
     * and null is returned
     */
    public static LocationType toLocationType(SearchLevel level) {
        if (level == SearchLevel.LIBRARY) {
            return LocationType.LIBRARY;
        } else if (level == SearchLevel.COLLECTION) {
            return LocationType.COLLECTION;
        } else if (level == SearchLevel.SHELF) {
            return LocationType.SHELF;
        }
        return null;
    }
}
