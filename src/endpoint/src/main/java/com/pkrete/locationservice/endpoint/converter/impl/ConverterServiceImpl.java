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
package com.pkrete.locationservice.endpoint.converter.impl;

import com.pkrete.locationservice.endpoint.converter.ConverterService;

/**
 * ConverterServiceImpl class implements the 
 * {@link ConverterService ConverterService} interface.
 * 
 * Converter service implementation for converting between different types.
 * 
 * @author Petteri Kivimäki
 */
public class ConverterServiceImpl<T extends Enum> implements ConverterService<T> {

    /**
     * Parses the string argument as a signed decimal integer. If parsing
     * of the string fails, zero is returned.
     * @param source a String containing the int representation to be parsed
     * @return the integer value represented by the argument in decimal
     */
    public int strToInt(String source) {
        try {
            return Integer.parseInt(source);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    /**
     * Parses the hex string as signed decimal integer. If parsing
     * of the hex string fails, zero is returned.
     * @param source hex string
     * @return the integer value represented by the argument in decimal
     */
    public int hexToInt(String source) {
        try {
            return Integer.parseInt(source, 16);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    /**
     * Converts the given source string to corresponding Enum. If no matching
     * Enum is found, defaultTarget is returned.
     * @param source Enum as string
     * @param enumType type of the Enum
     * @param defaultTarget default enum
     * @return Enum matching the given string
     */
    public T convert(String source, Class<T> enumType, T defaultTarget) {
        // If source is null or empty, defaultTarget is returned
        if (source == null || source.isEmpty()) {
            return defaultTarget;
        }
        try {
            // Try to get the enum constant with the specified name
            return (T) Enum.valueOf(enumType, source.trim().toUpperCase());
        } catch (IllegalArgumentException iae) {
            // If no matching enum constant is found, an exception is
            // thrown and defaultTarget is returned
            return defaultTarget;
        }
    }
}

