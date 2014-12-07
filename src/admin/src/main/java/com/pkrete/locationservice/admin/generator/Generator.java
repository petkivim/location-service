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
package com.pkrete.locationservice.admin.generator;

import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import java.util.List;

/**
 * An interface that defines the methods for generating output that is returned
 * to the user. All the classes implementing this interface must implement all
 * the methods defined in this interface.
 *
 * @author Petteri Kivimäki
 */
public interface Generator {

    String generate(List locations);

    String generate(SimpleLocation location);
}
