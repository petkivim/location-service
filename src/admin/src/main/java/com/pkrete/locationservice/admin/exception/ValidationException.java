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
package com.pkrete.locationservice.admin.exception;

import org.springframework.validation.BindingResult;

/**
 * This class extends the Exception class and it represents an exception that
 * occurs when validation fails.
 *
 * @author Petteri Kivimäki
 */
public class ValidationException extends Exception {

    private BindingResult validationResults;

    /**
     * Constructs and initializes a new ValidationException object with the
     * given error message.
     *
     * @param message error message that's shown
     * @param validationResults validation results
     */
    public ValidationException(String message, BindingResult validationResults) {
        super(message);
        this.validationResults = validationResults;
    }

    /**
     * Returns validation results related to this exception.
     *
     * @return validation results.
     */
    public BindingResult getValidationResults() {
        return validationResults;
    }
}
