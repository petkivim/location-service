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
package com.pkrete.locationservice.admin.controller.rest.v1;

import com.pkrete.locationservice.admin.exception.AuthenticationException;
import com.pkrete.locationservice.admin.exception.OperationFailedException;
import com.pkrete.locationservice.admin.exception.ResourceNotFoundException;
import com.pkrete.locationservice.admin.exception.ValidationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 * This class is reponsible of handling errors thrown in REST controllers.
 * 
 * @author Petteri Kivimäki
 */
@ControllerAdvice
public class RestExceptionHandler {

    private final static Logger logger = Logger.getLogger(RestExceptionHandler.class.getName());
    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    protected Map handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request, HttpServletResponse response) {
        // Get supported methods
        StringBuilder methods = new StringBuilder();
        for (int i = 0; i < ex.getSupportedMethods().length; i++) {
            methods.append(ex.getSupportedMethods()[i]);
            if (i < ex.getSupportedMethods().length - 1) {
                methods.append(", ");
            }
        }
        // Set response status
        response.setStatus(405);
        // Set Allow  header
        response.addHeader("Allow", methods.toString());
        // Create Map containing all the fields   
        Map errors = new HashMap<String, String>();
        // Set message arguments
        Object[] args = new Object[]{ex.getMethod(), methods};
        // Get message and set argument values
        String message = messageSource.getMessage("rest.http.request.method.not.supported", args, null);
        // Add error message
        errors.put("error", message);
        // Not authenticated -> return error
        return errors;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    protected Map handleAuthenticationException(AuthenticationException ex, WebRequest request, HttpServletResponse response) {
        // Set response status
        response.setStatus(401);
        // Create Map containing all the fields   
        Map errors = new HashMap<String, String>();
        // Add error message
        errors.put("error", ex.getMessage());
        // Return error
        return errors;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    protected Map handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request, HttpServletResponse response) {
        // Set response status
        response.setStatus(404);
        // Create Map containing all the fields   
        Map errors = new HashMap<String, String>();
        // Add error message
        errors.put("error", ex.getMessage());
        // Return error
        return errors;
    }

    @ExceptionHandler(OperationFailedException.class)
    @ResponseBody
    protected Map handleOperationFailedException(OperationFailedException ex, WebRequest request, HttpServletResponse response) {
        // Set response status
        response.setStatus(500);
        // Create Map containing all the fields   
        Map errors = new HashMap<String, String>();
        // Add error message
        errors.put("error", ex.getMessage());
        // Return error
        return errors;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    protected Map handleValidationException(ValidationException ex, WebRequest request, HttpServletResponse response) {
        // Set response status
        response.setStatus(400);
        // Create Map containing the whole error message   
        Map errors = new HashMap();
        // Add error message
        errors.put("error", ex.getMessage());
        // Go through validation results, if not null
        if (ex.getValidationResults() != null) {
            // List for all the field related errors
            List fields = new ArrayList();
            // Go through all the validation errors
            for (ObjectError err : ex.getValidationResults().getAllErrors()) {
                // Map for info related to this error
                Map fieldErrors = new LinkedHashMap();
                // Add field name
                fieldErrors.put("field", ((FieldError) err).getField());
                // Add error code
                fieldErrors.put("code", err.getCode());
                // Add error message
                fieldErrors.put("message", messageSource.getMessage(err, null));
                // Add to the list
                fields.add(fieldErrors);
            }
            // Add fields list to the error message
            errors.put("fields", fields);
        }
        // Return errors
        return errors;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    protected Map handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, WebRequest request, HttpServletResponse response) {
        // Set response status
        response.setStatus(415);
        // Create Map containing all the fields   
        Map errors = new HashMap<String, String>();
        // Add error message
        errors.put("error", ex.getMessage());
        // Return error
        return errors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    protected Map handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request, HttpServletResponse response) {
        // Set response status
        response.setStatus(400);
        // Create Map containing all the fields   
        Map errors = new HashMap<String, String>();
        // Get message
        String message = messageSource.getMessage("rest.message.not.readable", null, null);
        // Add error message
        errors.put("error", message);
        // Return error
        return errors;
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    protected Map handleTypeMismatchException(TypeMismatchException ex, WebRequest request, HttpServletResponse response) {
        // Set response status
        response.setStatus(400);
        // Create Map containing all the fields   
        Map errors = new HashMap<String, String>();
        // Get message
        String message = messageSource.getMessage("rest.invalid.resource.id", null, null);
        // Add error message
        errors.put("error", message);
        // Return error
        return errors;
    }

    @ExceptionHandler(NoSuchRequestHandlingMethodException.class)
    @ResponseBody
    protected Map handleNoSuchRequestHandlingMethodException(NoSuchRequestHandlingMethodException ex, WebRequest request, HttpServletResponse response) {
        // Set response status
        response.setStatus(404);
        // Create Map containing all the fields   
        Map errors = new HashMap<String, String>();
        // Get message
        String message = messageSource.getMessage("rest.invalid.resource.url", null, null);
        // Add error message
        errors.put("error", message);
        // Return error
        return errors;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected Map handleException(Exception ex, WebRequest request, HttpServletResponse response) {
        // Set response status
        response.setStatus(500);
        // Create Map containing all the fields   
        Map errors = new HashMap<String, String>();
        // Write exception stack trace to String
        StringWriter  errorsStr = new StringWriter();
        ex.printStackTrace(new PrintWriter(errorsStr));
        // Log error
        logger.error(errorsStr.toString());
        // Get message
        String message = messageSource.getMessage("rest.error.general", null, null);
        // Add error message
        errors.put("error", message);
        // Return error
        return errors;
    }
}
