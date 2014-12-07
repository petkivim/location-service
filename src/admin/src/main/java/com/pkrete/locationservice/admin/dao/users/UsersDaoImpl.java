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
package com.pkrete.locationservice.admin.dao.users;

import com.pkrete.locationservice.admin.dao.UsersDao;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.model.user.UserFull;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.model.user.UserInfo;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

/**
 * This class implements {@link UsersDao UsersDao} interface that defines data
 * access layer for User and UserInfo objects.
 *
 * This class extends {@link HibernateDaoSupport HibernateDaoSupport} class that
 * is a wrapper over {@link HibernateTemplate HibernateTemplate} class.
 * HibernateTemplate is a convenience class for Hibernate based database access.
 * HibernateDaoSupport creates the HibernateTemplate and subclasses can use the
 * getHibernateTemplate() method to obtain the hibernateTemplate and then
 * perform operations on it. HibernateTemplate takes care of obtaining or
 * releasing sessions and managing exceptions.
 *
 * @author Petteri Kivimäki
 */
public class UsersDaoImpl extends HibernateDaoSupport implements UsersDao {

    private final static Logger localLogger = LoggerFactory.getLogger(UsersDaoImpl.class.getName());

    /**
     * Returns the user with given user name. Password is not included.
     *
     * @param username the user name that is used for searching
     * @return the user with the given user name
     */
    @Override
    public User getUser(String username) {
        List<User> list = (List<User>) getHibernateTemplate().findByNamedParam(
                "from UserFull user join fetch user.owner owner left join fetch "
                + "owner.languages where user.username = :username", "username", username);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns all the users in the database. Passwords are not included.
     *
     * @return all the users in the database
     */
    @Override
    public List<User> getUsers() {
        List<User> list = (List<User>) getHibernateTemplate().find(
                "from UserFull user order by user.username ASC");
        return list;
    }

    /**
     * Returns all the users belonging to the given user group related to the
     * given owner. Passwords are not included.
     *
     * @return all the users belonging to the given user group related to the
     * given owner
     */
    @Override
    public List<User> getUsers(Owner owner, UserGroup group) {
        List<User> list = (List<User>) getHibernateTemplate().findByNamedParam(
                "from UserFull user "
                + "where user.owner.id=:id "
                + "and user.username in "
                + "(select info.user.username from UserInfo info where info.group  = :group) "
                + "order by user.username ASC", new String[]{"id", "group"}, new Object[]{owner.getId(), group});
        return list;
    }

    /**
     * Returns the user with given user name. The password of the user is
     * included.
     *
     * @param username the user name that is used for searching
     * @return the user with the given user name
     */
    @Override
    public UserFull getFullUser(String username) {
        List<UserFull> list = (List<UserFull>) getHibernateTemplate().findByNamedParam(
                "from UserFull user where user.username = :username", "username", username);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns all the users in the database. The passwords of the users are
     * included.
     *
     * @return all the users in the database
     */
    @Override
    public List<UserFull> getFullUsers() {
        List<UserFull> list = (List<UserFull>) getHibernateTemplate().find(
                "from UserFull user order by user.username ASC");
        return list;
    }

    /**
     * Adds the given user object to the database.
     *
     * @param user the user to be added
     * @return true if and only if the user was successfully added; otherwise
     * false
     */
    @Override
    public boolean create(UserFull user) {
        try {
            getHibernateTemplate().save(user);
        } catch (Exception e) {
            localLogger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Updates the given user object to the database.
     *
     * @param user the user to be updated
     * @return true if and only if the user was successfully updated; otherwise
     * false
     */
    @Override
    public boolean update(UserFull user) {
        try {
            getHibernateTemplate().update(user);
        } catch (Exception e) {
            localLogger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Deletes the given user object from the database.
     *
     * @param user the user to be deleted
     * @return true if and only if the user was successfully deleted; otherwise
     * false
     */
    @Override
    public boolean delete(UserFull user) {
        try {
            getHibernateTemplate().delete(user);
        } catch (Exception e) {
            localLogger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Returns a list of all the UserInfo objects.
     *
     * @return list of all the UserInfo objects
     */
    @Override
    public List<UserInfo> getUserInfos() {
        List<UserInfo> list = (List<UserInfo>) getHibernateTemplate().find(
                "from UserInfo info "
                + "join fetch info.user "
                + "order by info.user.username ASC");
        return list;
    }

    /**
     * Returns a list of UserInfo objects related to the given owner and
     * belonging to the given user group.
     *
     * @param owner owner to which users are related
     * @param group user group of the users
     * @return list of UserInfo objects with the given user group that are
     * related to the given owner
     */
    @Override
    public List<UserInfo> getUserInfos(Owner owner, UserGroup group) {
        List<UserInfo> list = (List<UserInfo>) getHibernateTemplate().findByNamedParam(
                "from UserInfo info "
                + "join fetch info.user "
                + "where info.user.owner.id = :id "
                + "and info.group= :group "
                + "order by info.user.username ASC", new String[]{"id", "group"}, new Object[]{owner.getId(), group});
        return list;
    }

    /**
     * Returns the user info with the given username.
     *
     * @param username the username that is used for searching
     * @return the user info with the given username
     */
    @Override
    public UserInfo getUserInfoByUsername(String username) {
        List<UserInfo> list = (List<UserInfo>) getHibernateTemplate().findByNamedParam(
                "from UserInfo info where info.user.username = :username", "username", username);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns the user info with the given username and owner.
     *
     * @param username the username that is used for searching
     * @param owner owner to which the user is related
     * @return the user info with the given username
     */
    @Override
    public UserInfo getUserInfoByUsername(String username, Owner owner) {
        List<UserInfo> list = (List<UserInfo>) getHibernateTemplate().findByNamedParam(
                "from UserInfo info "
                + "where info.user.username = :username "
                + "and info.user.owner.id = :id",
                new String[]{"username", "id"}, new Object[]{username, owner.getId()});
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Adds the given user info object to the database.
     *
     * @param info the info to be created
     * @return true if and only if the user info was successfully created;
     * otherwise false
     */
    @Override
    public boolean create(UserInfo info) {
        try {
            getHibernateTemplate().save(info);
        } catch (Exception e) {
            localLogger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Updates the given user info object to the database.
     *
     * @param info the info to be updated
     * @return true if and only if the user info was successfully updated;
     * otherwise false
     */
    @Override
    public boolean update(UserInfo info) {
        try {
            getHibernateTemplate().update(info);
        } catch (Exception e) {
            localLogger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Deletes the given user info object from the database.
     *
     * @param info the user info to be deleted
     * @return true if and only if the user info was successfully deleted;
     * otherwise false
     */
    @Override
    public boolean delete(UserInfo info) {
        try {
            getHibernateTemplate().delete(info);
        } catch (Exception e) {
            localLogger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Returns the UserGroup of the user that's identified by the given
     * username.
     *
     * @param username user id of the user
     * @return UserGroup of the user or null if user with the given username
     * doesn't exist
     */
    @Override
    public UserGroup getUserGroup(String username) {
        List<UserGroup> list = (List<UserGroup>) getHibernateTemplate().findByNamedParam(
                "select group from UserInfo info "
                + "where info.user.username = :username", "username", username);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
