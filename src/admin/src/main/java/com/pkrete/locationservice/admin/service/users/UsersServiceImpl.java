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
package com.pkrete.locationservice.admin.service.users;

import com.pkrete.locationservice.admin.dao.UsersDao;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.model.user.UserFull;
import com.pkrete.locationservice.admin.model.user.UserInfo;
import com.pkrete.locationservice.admin.converter.EncryptionService;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import com.pkrete.locationservice.admin.mailer.MailerService;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.service.UsersService;
import com.pkrete.locationservice.admin.util.PropertiesUtil;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Petteri Kivimäki
 */
public class UsersServiceImpl implements UsersService {

    private final static Logger logger = Logger.getLogger(UsersServiceImpl.class.getName());
    private UsersDao dao;
    private EncryptionService encryptionService;
    private MailerService mailerService;
    private JSONizerService userJsonizer;
    private JSONizerService userInfoJsonizer;

    /**
     * Sets the data access object.
     * @param dao new value
     */
    public void setDao(UsersDao dao) {
        this.dao = dao;
    }

    /**
     * Sets the encryption service object.
     * @param encryptionService new value
     */
    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    /**
     * Sets the mailer service object.
     * @param mailerService new value
     */
    public void setMailerService(MailerService mailerService) {
        this.mailerService = mailerService;
    }

    /**
     * Sets the JSON converter object.
     * @param dao new value
     */
    public void setUserJsonizer(JSONizerService jsonizer) {
        this.userJsonizer = jsonizer;
    }

    /**
     * Sets the JSON converter object.
     * @param dao new value
     */
    public void setUserInfoJsonizer(JSONizerService jsonizer) {
        this.userInfoJsonizer = jsonizer;
    }

    /**
     * Returns the user with given user name. Password is not included.
     * @param username the user name that is used for searching
     * @return the user with the given user name
     */
    public User getUser(String username) {
        return dao.getUser(username);
    }

    /**
     * Returns all the users in the database. Passwords are not included.
     * @return all the users in the database
     */
    public List<User> getUsers() {
        return dao.getUsers();
    }

    /**
     * Returns all the users belonging to the given user group related to the 
     * given owner. Passwords are not included.
     * @return all the users belonging to the given user group related to the 
     * given owner
     */
    public List<User> getUsers(Owner owner, UserGroup group) {
        return dao.getUsers(owner, group);
    }

    /**
     * Returns the user with given user name. The password of the user
     * is included.
     * @param username the user name that is used for searching
     * @return the user with the given user name
     */
    public UserFull getFullUser(String username) {
        return dao.getFullUser(username);
    }

    /**
     * Returns all the users in the database. The passwords of the users
     * are included.
     * @return all the users in the database
     */
    public List<UserFull> getFullUsers() {
        return dao.getFullUsers();
    }

    /**
     * Adds the given user object to the database.
     * @param user the user to be added
     */
    public boolean create(UserFull user) {
        // Encrypt password
        String pass = this.encryptionService.encrypt(user.getPasswordUi());
        // Set encrypted password
        user.setPassword(pass);
        // Set creation date
        user.setCreated(new Date());
        // Try to add user to the DB
        if (dao.create(user)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("User created : ").append(this.userJsonizer.jsonize(user, true)));
            }
            if (PropertiesUtil.getProperty("mail.send.add").equals("true")) {
                if (logger.isInfoEnabled()) {
                    logger.info("Notify user by email.");
                }
                this.mailerService.send(user);
            }
            return true;
        }
        logger.warn(new StringBuilder("Failed to create user : ").append(this.userJsonizer.jsonize(user, true)));
        return false;
    }

    /**
     * Updates the given user object to the database.
     * @param user the user to be updated
     */
    public boolean update(UserFull user) {
        // This variable tells if the user should be notified
        boolean notify = false;

        // Has user set a new password?
        if (user.getPasswordUi() != null && !user.getPasswordUi().isEmpty()) {
            // Encrypt the new password
            String pass = this.encryptionService.encrypt(user.getPasswordUi());
            // Does the new password differ from the old one?
            if (!pass.equals(user.getPassword())) {
                // Set new value
                user.setPassword(pass);
                // Should the user be notified?
                if (PropertiesUtil.getProperty("mail.send.edit").equals("true")) {
                    notify = true;
                }
            }
        }
        // Set updated date
        user.setUpdated(new Date());
        // Get JSON presentation
        String json = this.userJsonizer.jsonize(user, true);
        // Try to update the user to the DB
        if (dao.update(user)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("User updated : ").append(json));
            }
            // Notify the user?
            if (notify) {
                if (logger.isInfoEnabled()) {
                    logger.info("Notify user by email.");
                }
                this.mailerService.send(user);
            }
            return true;
        }
        logger.warn(new StringBuilder("Failed to update user : ").append(json));
        return false;
    }

    /**
     * Deletes the given user object from the database.
     * @param user the user to be deleted
     */
    public boolean delete(UserFull user) {
        // JSON presentation of the Language object
        String json = this.userJsonizer.jsonize(user, true);
        // Try to delete the user
        if (dao.delete(user)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("User deleted : ").append(json));
            }
            return true;
        }
        logger.warn(new StringBuilder("Failed to delete user : ").append(json));
        return false;
    }

    /**
     * Returns a list of all the UserInfo objects.
     * @return list of all the UserInfo objects
     */
    public List<UserInfo> getUserInfos() {
        return this.dao.getUserInfos();
    }

    /**
     * Returns a list of UserInfo objects related to the given owner and
     * belonging to the given user group.
     * @param owner owner to which users are related
     * @param group user group of the users
     * @return list of UserInfo objects with the given user group that are
     * related to the given owner
     */
    public List<UserInfo> getUserInfos(Owner owner, UserGroup group) {
        return this.dao.getUserInfos(owner, group);
    }

    /**
     * Returns the user info with the given username.
     * @param username the username that is used for searching
     * @return the user info with the given username
     */
    public UserInfo getUserInfoByUsername(String username) {
        return dao.getUserInfoByUsername(username);
    }

    /**
     * Returns the user info with the given username and owner.
     * @param username the username that is used for searching
     * @param owner owner to which the user is related
     * @return the user info with the given username
     */
    public UserInfo getUserInfoByUsername(String username, Owner owner) {
        return dao.getUserInfoByUsername(username, owner);
    }

    /**
     * Returns the user info with the given username, user group and owner.
     * If user info matching the given conditions cannot be found, null is
     * returned.
     * @param username the username that is used for searching
     * @param owner owner to which the user is related
     * @param group user group of the user
     * @return the user info with the given username and group or null if
     * user matching the given conditions cannot be found
     */
    public UserInfo getUserInfoByUsername(String username, Owner owner, UserGroup group) {
        UserInfo user = this.dao.getUserInfoByUsername(username, owner);
        // Only users belonging to the given group are allowed
        if (user != null && !user.getGroup().equals(group)) {
            return null;
        }
        return user;
    }

    /**
     * Adds the given user info object to the database. Before creating
     * the UserInfo, new User object is created.
     * @param info the info to be added
     */
    public boolean create(UserInfo info) {
        if (this.create(info.getUser())) {
            if (dao.create(info)) {
                if (logger.isInfoEnabled()) {
                    logger.info(new StringBuilder("User info created : ").append(this.userInfoJsonizer.jsonize(info, true)));
                }
                return true;
            }
            logger.warn(new StringBuilder("Failed to create user info : ").append(this.userInfoJsonizer.jsonize(info, true)));
            return false;
        }
        logger.warn("Creating new user failed -> no user info was created.");
        return false;
    }

    /**
     * Updates the given user info object to the database. User object
     * related to the UserInfo is not updated.
     * @param info the info to be updated
     */
    public boolean update(UserInfo info) {
        // Get JSON presentation
        String json = this.userInfoJsonizer.jsonize(info, true);
        // Try to update the DB
        if (dao.update(info)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("User info updated : ").append(json));
            }
            return true;
        }
        logger.warn(new StringBuilder("Failed to update user info : ").append(json));
        return false;
    }

    /**
     * Deletes the given user info object and the User object related
     * to it from the database.
     * @param info the user info to be deleted
     */
    public boolean delete(UserInfo info) {
        // JSON presentation of the UserInfo object
        String json = this.userInfoJsonizer.jsonize(info, true);
        UserFull user = info.getUser();
        if (dao.delete(info)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("User info deleted : ").append(json));
            }
            if (this.delete(user)) {
                return true;
            } else {
                logger.warn("Failed to delete user. User info deleted succesfully.");
                return false;
            }
        }
        logger.warn(new StringBuilder("Failed to delete user info : ").append(json));
        return false;
    }

    /**
     * Returns the UserGroup of the user that's identified by the given
     * username.
     * @param username user id of the user
     * @return UserGroup of the user or null if user with the given username
     * doesn't exist
     */
    public UserGroup getUserGroup(String username) {
        return dao.getUserGroup(username);
    }
}
