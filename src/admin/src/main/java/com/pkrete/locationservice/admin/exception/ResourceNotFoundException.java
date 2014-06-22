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
package com.pkrete.locationservice.admin.exception;

/**
 * This class extends the Exception class and it represents an exception
 * that occurs when the requested resource doens't exist.
 * 
 * @author Petteri Kivimäki
 */
public class ResourceNotFoundException extends Exception {
        /**
     * Constructs and initializes a new ResourceNotFoundException object
     * with the given error message.
     * @param message error message that's shown
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
