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
package com.pkrete.locationservice.admin.service;

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.model.user.UserFull;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.model.user.UserInfo;
import java.util.List;

/**
 * This interface defines service layer for User, UserInfo and UserGroup 
 * objects. All the classes implementing this interface must implement all 
 * the methods defined here.
 * 
 * @author Petteri Kivimäki
 */
public interface UsersService {

    public User getUser(String username);

    public List<User> getUsers();
    
    public List<User> getUsers(Owner owner, UserGroup group);

    public UserFull getFullUser(String username);

    public List<UserFull> getFullUsers();

    public boolean create(UserFull user);
    
    public boolean update(UserFull user);

    public boolean delete(UserFull user);
    
    public List<UserInfo> getUserInfos();
    
    public List<UserInfo> getUserInfos(Owner owner, UserGroup group);

    public UserInfo getUserInfoByUsername(String username);
    
    public UserInfo getUserInfoByUsername(String username, Owner owner);
    
    public UserInfo getUserInfoByUsername(String username, Owner owner, UserGroup group);

    public boolean create(UserInfo info);
    
    public boolean update(UserInfo info);

    public boolean delete(UserInfo info);
       
    public UserGroup getUserGroup(String username);
}
