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

import com.pkrete.locationservice.endpoint.converter.ConverterService;
import com.pkrete.locationservice.endpoint.mailer.EmailMessage;
import com.pkrete.locationservice.endpoint.mailer.EmailMessageFactoryService;
import com.pkrete.locationservice.endpoint.mailer.EmailMessageType;
import com.pkrete.locationservice.endpoint.mailer.EmailService;
import com.pkrete.locationservice.endpoint.util.PropertiesUtil;
import org.apache.log4j.Logger;
import org.apache.commons.mail.*;

/**
 * EmailServiceImpl class implements the {@link EmailService EmailService}
 * interface.
 * 
 * This class is respobsible of sending email messages to defined users.
 * 
 * @author Petteri Kivimäki
 */
public class EmailServiceImpl implements EmailService {

    private final static Logger logger = Logger.getLogger(EmailServiceImpl.class.getName());
    private ConverterService converterService;
    private EmailMessageFactoryService emailMessageFactoryService;

    /**
     * Changes the value of converterService instance variable
     * @param converterService new value to be set
     */
    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    /**
     * Changes the value of emailMessageFactoryService instance variable
     * @param emailMessageFactoryService new value to be set
     */
    public void setEmailMessageFactoryService(EmailMessageFactoryService emailMessageFactoryService) {
        this.emailMessageFactoryService = emailMessageFactoryService;
    }

    /**
     * Sends an email message of the given message type. Returns true if and 
     * only if the message was succesfully sent to all the recipients; 
     * otherwise false.
     * @param messageType type of the email message to be sent
     * @return true if and only if the message was succesfully sent to all
     * the recipients; otherwise false
     */
    public boolean send(EmailMessageType messageType) {
        return this.send(this.emailMessageFactoryService.generate(messageType));
    }

    /**
     * Sends the given EmailMessage to the recipients defined in the message.
     * Returns true if and only if the message was succesfully sent to all
     * the recipients; otherwise false.
     * @param message email message to be sent
     * @return true if and only if the message was succesfully sent to all
     * the recipients; otherwise false
     */
    public boolean send(EmailMessage message) {
        if (logger.isDebugEnabled()) {
            logger.debug("Create new \"" + message.getType().toString() + "\" email message.");
        }
        if (message == null) {
            logger.warn("Message cannot be null.");
            return false;
        }
        if (message.getRecipients().isEmpty()) {
            logger.info("No recipients defined. Nothing to do -> exit.");
            return false;
        }
        Email email = new SimpleEmail();
        email.setHostName(PropertiesUtil.getProperty("mail.host"));
        email.setSmtpPort(this.converterService.strToInt(PropertiesUtil.getProperty("mail.port")));
        email.setAuthenticator(new DefaultAuthenticator(PropertiesUtil.getProperty("mail.user"), PropertiesUtil.getProperty("mail.password")));
        email.setTLS(true);
        try {
            // Set from address
            email.setFrom(message.getFrom());
            // Set subject
            email.setSubject(message.getSubject());

            // Build message body
            StringBuilder body = new StringBuilder();
            if (!message.getHeader().isEmpty()) {
                body.append(message.getHeader()).append("\n\n");
            }
            if (!message.getMessage().isEmpty()) {
                body.append(message.getMessage()).append("\n\n");
            }
            if (!message.getFooter().isEmpty()) {
                body.append(message.getFooter()).append("\n\n");
            }
            if (!message.getSignature().isEmpty()) {
                body.append(message.getSignature()).append("\n\n");
            }

            // Set message contents
            email.setMsg(body.toString());

            // Add message receivers
            for (String recipient : message.getRecipients()) {
                logger.info("Add recipient \"" + recipient + "\".");
                email.addTo(recipient);
            }

            // Send message
            email.send();

            if (logger.isInfoEnabled()) {
                logger.info("Email was succesfully sent to " + message.getRecipients().size() + " recipients.");
            }
        } catch (Exception e) {
            logger.error("Failed to send \"" + message.getType().toString() + "\" email message.");
            logger.error(e);
            return false;
        }
        return true;
    }
}
