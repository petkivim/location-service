/**
 * This file is part of Location Service :: Endpoint. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.mailer;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an email message that is sent to selected
 * administrators when a system error occurs. The message includes header,
 * message, footer and signature (in this order).
 *
 * @author Petteri Kivimäki
 */
public class EmailMessage {

    private List<String> recipients;
    private String from;
    private String subject;
    private String header;
    private String message;
    private String footer;
    private String signature;
    private EmailMessageType type;

    /**
     * Constructs and initializes a new EmailMessage.
     */
    public EmailMessage() {
        this.recipients = new ArrayList<String>();
    }

    /**
     * Constructs and initializes a new EmailMessage. The message includes
     * header, message, footer and signature (in this order).
     *
     * @param from email address of the sender
     * @param subject subject of the message
     * @param header header of the message
     * @param message main message
     * @param footer footer of the message
     * @param signature signature of the message
     * @param type type of the message
     */
    public EmailMessage(String from, String subject, String header, String message, String footer, String signature, EmailMessageType type) {
        this();
        this.from = from;
        this.subject = subject;
        this.header = header;
        this.message = message;
        this.footer = footer;
        this.signature = signature;
        this.type = type;
    }

    /**
     * Constructs and initializes a new EmailMessage. The message includes
     * header, message, footer and signature (in this order).
     *
     * @param from email address of the sender
     * @param subject subject of the message
     * @param header header of the message
     * @param message main message
     * @param footer footer of the message
     * @param signature signature of the message
     * @param type type of the message
     * @param recipients list of recipients' email addresses
     */
    public EmailMessage(String from, String subject, String header, String message, String footer, String signature, EmailMessageType type, List<String> recipients) {
        this(from, subject, header, message, footer, signature, type);
        this.recipients = recipients;
    }

    /**
     * Returns a list of recipients' email addresses.
     *
     * @return list of recipients' email addresses
     */
    public List<String> getRecipients() {
        return recipients;
    }

    /**
     * Sets the list containing recipients' email addresses.
     *
     * @param recipients new value
     */
    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    /**
     * Returns the subject of this EmailMessage.
     *
     * @return subject of the message
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of this EmailMessage.
     *
     * @param subject new value
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns the header of this EmailMessage. The content part of the message
     * includes header, message, footer and signature (in this order).
     *
     * @return header of the message
     */
    public String getHeader() {
        return header;
    }

    /**
     * Sets the header of this EmailMessage. The content part of the message
     * includes header, message, footer and signature (in this order).
     *
     * @param header new value
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Returns the main message of this EmailMessage. The content part of the
     * message includes header, message, footer and signature (in this order).
     *
     * @return main message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the main message of this EmailMessage. The content part of the
     * message includes header, message, footer and signature (in this order).
     *
     * @param message new value
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the footer of this EmailMessage. The content part of the message
     * includes header, message, footer and signature (in this order).
     *
     * @return footer of the message
     */
    public String getFooter() {
        return footer;
    }

    /**
     * Sets the footer of this EmailMessage. The content part of the message
     * includes header, message, footer and signature (in this order).
     *
     * @param footer new value
     */
    public void setFooter(String footer) {
        this.footer = footer;
    }

    /**
     * Returns the signature of this EmailMessage. The content part of the
     * message includes header, message, footer and signature (in this order).
     *
     * @return signature of the message
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the signature of this EmailMessage. The content part of the message
     * includes header, message, footer and signature (in this order).
     *
     * @param signature new value
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Returns the type of this EmailMessage.
     *
     * @return type of the message
     */
    public EmailMessageType getType() {
        return type;
    }

    /**
     * Sets the type of this EmailMessage.
     *
     * @param type new value
     */
    public void setType(EmailMessageType type) {
        this.type = type;
    }

    /**
     * Returns the email address of the sender this EmailMessage.
     *
     * @return email address of the sender
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the email address of the sender of this EmailMessage.
     *
     * @param from new value
     */
    public void setFrom(String from) {
        this.from = from;
    }
}
