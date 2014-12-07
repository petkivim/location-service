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
package com.pkrete.locationservice.admin.service;

import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.model.owner.Owner;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * This interface defines service layer for Image objects. All the classes
 * implementing this interface must implement all the methods defined here.
 *
 * @author Petteri Kivimäki
 */
public interface ImagesService {

    Image get(int id);

    Image get(int id, Owner owner);

    List<Image> get(Owner owner);

    boolean delete(int id, Owner owner);

    boolean delete(Image image);

    boolean delete(String fileName, Owner owner);

    boolean create(Image image);

    boolean update(Image image);

    boolean canBeDeleted(int imageId);

    List<String> getAdminImages(Owner owner);

    String upload(MultipartFile file, Owner owner);

    boolean adminImageExists(String fileName, Owner owner);
}
