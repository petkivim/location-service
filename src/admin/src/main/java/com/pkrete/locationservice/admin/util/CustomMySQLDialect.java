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
package com.pkrete.locationservice.admin.util;

import org.hibernate.dialect.MySQLDialect;

/**
 * The <code>CustomMySQLDialect</code> class extends the MySQLDialect class.
 *
 * This class overrides the getTableTypeString method of the MySQLDialect class.
 * The purpose of this is to set the character set to utf8 when the tables are
 * created.
 *
 * @author Petteri Kivimäki
 */
public class CustomMySQLDialect extends MySQLDialect {

    /**
     * Returns a string that define the DB engine and character set that are
     * used when new tables are created. Sets DB engine to InnoDB and character
     * set to UTF-8.
     *
     * @return string that defines the DB engine and character set.
     */
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci";
    }
}
