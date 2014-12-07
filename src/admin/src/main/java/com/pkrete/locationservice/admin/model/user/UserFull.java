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
package com.pkrete.locationservice.admin.model.user;

/**
 * This class extends the {@link User User} class. This class contains the
 * password of the user object. The password is needed only when the user logs
 * in to the service.
 *
 * @author Petteri Kivimäki
 */
public class UserFull extends User {
    /* Password of the user that is fetched from the db. */

    private String password;
    /* Password that is used in UI. */
    /* This variable is not saved to DB. */
    /* It's used when user is changing the password. */
    private String passwordUi;
    /* Control password of the user. */
    /* This variable is not saved to DB. */
    /* It's used when user is changing the password. */
    private String passwordControl;

    /**
     * Constructs and initializes a new UserFull object.
     */
    public UserFull() {
        password = "";
    }

    /**
     * Changes the password of the user.
     *
     * @param password new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the password of the user.
     *
     * @return password of the user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Changes the UI password of the user.
     *
     * @param password new UI password.
     */
    public void setPasswordUi(String password) {
        this.passwordUi = password;
    }

    /**
     * Returns the UI password of the user.
     *
     * @return UI password of the user
     */
    public String getPasswordUi() {
        return this.passwordUi;
    }

    /**
     * Changes the control password of the user.
     *
     * @param password new control password.
     */
    public void setPasswordControl(String password) {
        this.passwordControl = password;
    }

    /**
     * Returns the control password of the user.
     *
     * @return control password of the user
     */
    public String getPasswordControl() {
        return this.passwordControl;
    }
}
