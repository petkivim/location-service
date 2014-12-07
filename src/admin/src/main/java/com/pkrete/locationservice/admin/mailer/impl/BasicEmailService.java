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
package com.pkrete.locationservice.admin.mailer.impl;

import com.pkrete.locationservice.admin.model.user.UserFull;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.mailer.MailerService;
import com.pkrete.locationservice.admin.util.PropertiesUtil;
import org.apache.commons.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

/**
 * BasicEmailService class implements the {@link MailerService MailerService}
 * interface.
 *
 * This class is responsible for sending an email to the user when a new user is
 * created or the password of the user is modified. All the settings related to
 * the mail server that are being used are defined in the config.properties
 * file. It's also possible to disable this feature by using the
 * config.properties file.
 *
 * @author Petteri Kivimäki
 */
public class BasicEmailService implements MailerService {

    private final static Logger logger = LoggerFactory.getLogger(BasicEmailService.class.getName());
    private ConverterService converterService;
    private MessageSource messageSource;

    /**
     * Changes the value of converterService instance variable
     *
     * @param converterService new value to be set
     */
    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Send an email to the given user when the user is created or the password
     * is modified.
     *
     * @param user the receiver of the email
     */
    @Override
    public void send(UserFull user) {
        logger.info("Create new email message.");
        Email email = new SimpleEmail();
        email.setHostName(PropertiesUtil.getProperty("mail.host"));
        email.setSmtpPort(this.converterService.strToInt(PropertiesUtil.getProperty("mail.port")));
        email.setAuthenticator(new DefaultAuthenticator(PropertiesUtil.getProperty("mail.user"), PropertiesUtil.getProperty("mail.password")));
        email.setTLS(true);
        try {
            // Init variables
            String header = null;
            String msg = null;

            // Set from address
            email.setFrom(this.messageSource.getMessage("mail.from", null, null));

            // Set message arguments
            Object[] args = new Object[]{user.getUsername(), user.getPasswordUi()};

            // Set variables values
            if (user.getUpdated() == null) {
                // This is a new user
                if (logger.isDebugEnabled()) {
                    logger.debug("The message is for a new user.");
                }
                // Set subject
                email.setSubject(this.messageSource.getMessage("mail.title.add", null, null));
                // Set message header
                header = this.messageSource.getMessage("mail.header.add", null, null);
                // Set message content
                msg = this.messageSource.getMessage("mail.message.add", args, null);
            } else {
                // This is an existing user
                logger.debug("The message is for an existing user.");
                // Set subject
                email.setSubject(this.messageSource.getMessage("mail.title.edit", null, null));
                // Get message header
                header = this.messageSource.getMessage("mail.header.edit", null, null);
                // Get message content
                msg = this.messageSource.getMessage("mail.message.edit", args, null);
            }

            // Get note
            String note = this.messageSource.getMessage("mail.note", null, null);
            // Get footer
            String footer = this.messageSource.getMessage("mail.footer", null, null);
            // Get signature
            String signature = this.messageSource.getMessage("mail.signature", null, null);

            // Build message body
            StringBuilder result = new StringBuilder();
            if (!header.isEmpty()) {
                result.append(header).append("\n\n");
            }
            if (!msg.isEmpty()) {
                result.append(msg).append("\n\n");
            }
            if (!note.isEmpty()) {
                result.append(note).append("\n\n");
            }
            if (!footer.isEmpty()) {
                result.append(footer).append("\n\n");
            }
            if (!signature.isEmpty()) {
                result.append(signature).append("\n\n");
            }
            // Set message contents
            email.setMsg(result.toString());
            // Set message receiver
            email.addTo(user.getEmail());
            // Send message
            email.send();
            logger.info("Email was sent to \"{}\".", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send email to \"{}\".", user.getEmail());
            logger.error(e.getMessage(), e);
        }
    }
}
