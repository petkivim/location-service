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
package com.pkrete.locationservice.admin.io;

/**
 * FileService interface defines service interface for managing files.
 * It defines the necessary methods for managing files in the file system. 
 * Classes implementing this interface must must implement all the methods 
 * defined here.
 * 
 * @author Petteri Kivimäki
 */
public interface FileService {  

    public boolean add(String path, String contents);
    
    public String read(String path);
    
    public boolean update(String path, String contents);
    
    public boolean write(String path, String contents);
    
    public boolean delete(String path);
    
    public boolean rename(String oldPath, String newPath);  
    
    public boolean replace(String oldPath, String newPath);

    public boolean exists(String path);
}
