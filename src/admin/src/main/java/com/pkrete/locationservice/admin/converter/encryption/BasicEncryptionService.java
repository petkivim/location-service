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
package com.pkrete.locationservice.admin.converter.encryption;

import com.pkrete.locationservice.admin.converter.EncryptionService;
import java.security.MessageDigest;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

/**
 * BasicEncryptionService class implements the 
 * {@link EncryptionService EncryptionService} interface.
 * 
 * This class offers methods for encrypting strings. 
 *
 * @author Petteri Kivimäki
 */
public class BasicEncryptionService implements EncryptionService {

    private static final String ALOGORITHM = "SHA";
    private static final String ENCODING = "UTF-8";
    private final static Logger logger = Logger.getLogger(BasicEncryptionService.class.getName());

    /**
     * Encrypts the given string by using SHA algorithm and converts the
     * result to a corresponding hex string. The string must be
     * UTF-8 encoded.
     * @param plaintext string to be encrypted
     * @return encrypted string
     */
    @Override
    public String encrypt(String plaintext) {
        try {
            // Get new MessageDigest object using SHA algorithm
            MessageDigest digester = MessageDigest.getInstance(ALOGORITHM);

            // Encode the credentials using UTF-8 character encoding
            digester.update(plaintext.getBytes(ENCODING));

            // Digest the credentials and return as hexadecimal
            return (Hex.encodeHexString(digester.digest()));
        } catch (Exception ex) {
            logger.error(ex);
            return plaintext;
        }
    }
}
