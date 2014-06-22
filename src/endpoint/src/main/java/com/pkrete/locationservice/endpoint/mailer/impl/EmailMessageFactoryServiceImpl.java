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
package com.pkrete.locationservice.endpoint.mailer.impl;

import com.pkrete.locationservice.endpoint.mailer.EmailMessage;
import com.pkrete.locationservice.endpoint.mailer.EmailMessageFactoryService;
import com.pkrete.locationservice.endpoint.mailer.EmailMessageType;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.MessageSource;

/**
 * This class is responsible of generating EmailMessage objects of diffenret
 * types.
 * 
 * @author Petteri Kivimäki
 */
public class EmailMessageFactoryServiceImpl implements EmailMessageFactoryService {

    private MessageSource messageSource;

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Generates a new EmailMessage of the given type.
     * @param messageType type of the EmailMessage to be generated
     * @return EmailMessage object of the given type
     */
    public EmailMessage generate(EmailMessageType messageType) {
        // Get values that are shared between different message types
        String from = this.messageSource.getMessage("mail.from", null, null);
        String header = this.messageSource.getMessage("mail.header", null, null);
        String footer = this.messageSource.getMessage("mail.footer", null, null);
        String signature = this.messageSource.getMessage("mail.signature", null, null);
        // Variables for message type specific values
        String recipientsStr;
        String subject;
        String message;
        List<String> recipients;

        // Generate EmailMessage
        switch (messageType) {
            case LOGGER_ERROR:
                // Email addresses as a comma separated string
                recipientsStr = this.messageSource.getMessage("mail.logger_error.recipients", null, null);
                subject = this.messageSource.getMessage("mail.logger_error.subject", null, null);
                message = this.messageSource.getMessage("mail.logger_error.message", null, null);
                // Convert email addresses from String to List
                recipients = Arrays.asList(recipientsStr.split(","));
                return new EmailMessage(from, subject, header, message, footer, signature, EmailMessageType.LOGGER_ERROR, recipients);
            case LOGGER_ERROR_RECOVERY:
                // Email addresses as a comma separated string
                recipientsStr = this.messageSource.getMessage("mail.logger_error.recipients", null, null);
                subject = this.messageSource.getMessage("mail.logger_error_recovery.subject", null, null);
                message = this.messageSource.getMessage("mail.logger_error_recovery.message", null, null);
                // Convert email addresses from String to List
                recipients = Arrays.asList(recipientsStr.split(","));
                return new EmailMessage(from, subject, header, message, footer, signature, EmailMessageType.LOGGER_ERROR_RECOVERY, recipients);
        }
        return null;
    }
}
