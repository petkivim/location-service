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
package com.pkrete.locationservice.admin.service.illustrations;

import com.pkrete.locationservice.admin.service.ImagesService;
import com.pkrete.locationservice.admin.dao.ImagesDao;
import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.io.DirectoryService;
import com.pkrete.locationservice.admin.io.FileService;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import com.pkrete.locationservice.admin.util.DateTimeUtil;
import com.pkrete.locationservice.admin.util.IllustrationsUtil;
import com.pkrete.locationservice.admin.util.Settings;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class implements {@link ImagesService ImagesService} interface, 
 * which defines service layer for Image objects.
 * 
 * @author Petteri Kivimäki
 */
public class ImagesServiceImpl implements ImagesService {

    private final static Logger logger = Logger.getLogger(ImagesServiceImpl.class.getName());
    private ImagesDao dao;
    private FileService fileService;
    private DirectoryService dirService;
    private JSONizerService jsonizer;

    /**
     * Sets the data access object.
     * @param dao new value
     */
    public void setDao(ImagesDao dao) {
        this.dao = dao;
    }

    /**
     * Sets the file service object.
     * @param fileService new value
     */
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Sets the directory service object.
     * @param dirService new value
     */
    public void setDirService(DirectoryService dirService) {
        this.dirService = dirService;
    }

    /**
     * Sets the JSON converter object.
     * @param dao new value
     */
    public void setJsonizer(JSONizerService jsonizer) {
        this.jsonizer = jsonizer;
    }

    /**
     * Returns the image with given id.
     * This method is only for editor classes. All the other classes must give
     * also the owner parameter.
     * @param id the id that is used for searching
     * @return the image with the given id
     */
    public Image get(int id) {
        return dao.get(id);
    }

    /**
     * Returns the image with given id.
     * @param id the id that is used for searching
     * @param owner the owner of the object
     * @return the image with the given id
     */
    public Image get(int id, Owner owner) {
        return dao.get(id, owner);
    }

    /**
     * Deletes the image with the given id and owner.
     * @param id id of the image
     * @param owner owner of the image
     * @return true if and only if the image was succesfully deleted;
     * otherwise false
     */
    public boolean delete(int id, Owner owner) {
        Image image = dao.get(id, owner);
        return delete(image);
    }

    /**
     * Deletes the given image object from the database.
     * @param image the image to be deleted
     * @return true if and only if the image was succesfully deleted;
     * otherwise false
     */
    public boolean delete(Image image) {
        // Check for null
        if (image == null) {
            logger.warn("Deleting image failed! Image can not be null.");
            return false;
        }
        // JSON presentation of the Language object
        String json = this.jsonizer.jsonize(image, true);

        // Make sure that the image can be deleted
        if (!dao.canBeDeleted(image.getId())) {
            logger.warn(new StringBuilder("Deleting image failed! Image has dependencies in the database. Image : ").append(json));
            return false;
        }

        // Boolean value that tells if the image was deleted
        boolean success = true;
        // If the image is not external, the image file needs to be deleted
        if (!image.getIsExternal()) {
            // Build absolute path of the image file
            String path = IllustrationsUtil.buildFilePath(image);
            if (path == null) {
                logger.warn(new StringBuilder("Deleting image failed! Unable to build path for the image file. Image : ").append(json));
                return false;
            }
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Delete image file : ").append(path));
            }
            // Try to delete the file, if it exists
            if (fileService.exists(path)) {
                // Delete the file
                success = fileService.delete(path);
            } else {
                if (logger.isInfoEnabled()) {
                    logger.info(new StringBuilder("Image file doesn't exist : ").append(path));
                }
            }
        }
        // Delete the image from DB, if success == true
        if (success) {
            if (dao.delete(image)) {
                if (logger.isInfoEnabled()) {
                    logger.info(new StringBuilder("Image deleted : ").append(json));
                }
                return true;
            }
            logger.warn(new StringBuilder("Failed to delete image : ").append(json));
            return false;
        }
        return false;
    }

    /**
     * Returns a list of all the images in the database that are related
     * to the given owner object.
     * @param owner the owner of the objects
     * @return all the images in the database
     */
    public List<Image> get(Owner owner) {
        return dao.get(owner);
    }

    /**
     * Addsthe given image object to the database and to the filesystem.
     * @param image the image to be added
     * @return true if and only if the image was succesfully added; otherwise
     * false
     */
    public boolean create(Image image) {
        // Set created date
        image.setCreated(new Date());
        // Get path of the images dir
        String path = Settings.getInstance().getImagesPath(image.getOwner().getCode());
        // Get path of the images admin dir
        String adminPath = Settings.getInstance().getImagesPathAdmin(image.getOwner().getCode());

        if (image.getFilePath() != null && image.getFilePath().length() > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("Move uploaded image file from admin dir to service dir.");
            }
            // Image is moved from admin dir to service dir.
            // FilePath variable contains the name of the image file
            String name = image.getFilePath();
            // Name must be unique
            name = this.getUniqueName(path, name);
            // Path variable must be updated
            image.setPath(name);
            // The image is not external
            image.setIsExternal(false);
            // Absolute target path
            path += image.getPath();
            // Absolute source path
            adminPath += image.getFilePath();
            // Check that the file really exists
            if (!this.adminImageExists(image.getFilePath(), image.getOwner())) {
                logger.warn(new StringBuilder("Creating image failed, because the given file doesn't exist! Path : ").append(adminPath));
                return false;
            }
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Move image file : \"").append(adminPath).append("\" -> \"").append(path).append("\""));
            }
            // Try to rename (=move) the file
            if (!fileService.rename(adminPath, path)) {
                logger.warn("Moving image file failed! Creating new image failed!");
                return false;
            }
        } else if (image.getUrl() != null && image.getUrl().length() > 0) {
            // Set path
            image.setPath(image.getUrl());
            // Image is external
            image.setIsExternal(true);
        } else if (image.getFile() != null && image.getFile().getSize() > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("Add uploaded image file.");
            }
            // User has uploaded a file
            MultipartFile file = image.getFile();
            // Get the name of the file
            String name = file.getOriginalFilename();
            // Name must be unique
            name = this.getUniqueName(path, name);
            // Update path variable
            image.setPath(name);
            // Absolute target path
            path += image.getPath();
            // Image is not external
            image.setIsExternal(false);
            // Create a new File object for the uploaded file
            File imageFile = new File(path);
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Write uploaded image file to disk. Path : \"").append(path).append("\""));
            }
            try {
                // Write the uploaded file to disk
                file.transferTo(imageFile);
            } catch (IOException ex) {
                logger.warn("Writing image file to disk failed! Creating image failed!");
                logger.error(ex);
                return false;
            }
            // Check that the file really exists
            if (!fileService.exists(path)) {
                logger.warn(new StringBuilder("Writing image file to disk failed! File doesn't exist. Path : \"").append(path).append("\""));
                return false;
            }
        }
        // Try to save the image to DB
        if (dao.create(image)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Image created : ").append(this.jsonizer.jsonize(image, true)));
            }
            return true;
        }
        logger.warn(new StringBuilder("Failed to create image : ").append(this.jsonizer.jsonize(image, true)));
        return false;
    }

    /**
     * Updates the given image object to the database and to the filesystem.
     * @param image the image to be saved
     * @return true if and only if the image was succesfully added; otherwise
     * false
     */
    public boolean update(Image image) {
        // Get JSON presentation
        String json = this.jsonizer.jsonize(image, true);
        // Get path of the images dir
        String path = Settings.getInstance().getImagesPath(image.getOwner().getCode());
        // Get path of the images admin dir
        String adminPath = Settings.getInstance().getImagesPathAdmin(image.getOwner().getCode());

        if (image.getFilePath() != null && image.getFilePath().length() > 0) {
            // Absolute source path
            String source = adminPath + image.getFilePath();
            if (logger.isDebugEnabled()) {
                logger.debug("Move uploaded image file from admin dir to service dir.");
            }
            // Check that the source file really exists
            if (!this.adminImageExists(image.getFilePath(), image.getOwner())) {
                logger.warn(new StringBuilder("Updating image failed, because the given file doesn't exist! Path : ").append(source));
                return false;
            }
            // Absolute target path
            String target = path + image.getPath();
            // If the image is external, name must be checked
            if (image.getIsExternal()) {
                // Old image was external, name of the image file must set
                String name = image.getFilePath();
                // Name must be unique
                name = this.getUniqueName(path, name);
                // Set target path
                target = path + name;
                // Update path
                image.setPath(name);
            } 
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Move image file : \"").append(source).append("\" -> \"").append(target).append("\""));
            }
            // Try to rename (=move) the file
            if (!fileService.replace(source, target)) {
                logger.warn(new StringBuilder("Moving image file failed!").append("Updating image failed!").append(json));
                return false;
            }
            // The image is not external
            image.setIsExternal(false);
        } else if (image.getUrl() != null && !image.getUrl().isEmpty()) {
            // If image is not external, the file must me deleted
            if (!image.getIsExternal()) {
                // Get path of the current file
                String target = IllustrationsUtil.buildFilePath(image);
                // Remove the file
                if (this.fileService.exists(target) && !this.fileService.delete(target)) {
                    logger.warn(new StringBuilder("Unable to delete the old image file while updating image from internal to external. Path : \"").append(target).append("\""));
                    logger.warn(new StringBuilder("Updating image failed!").append(json));
                    return false;
                }
            }
            // Update path
            image.setPath(image.getUrl());
            // The image is external
            image.setIsExternal(true);
        } else if (image.getFile() != null && image.getFile().getSize() > 0) {
            // Check if user has uploaded a new image file
            if (logger.isDebugEnabled()) {
                logger.debug("Update image file.");
            }
            // Get the uploaded file
            MultipartFile file = image.getFile();
            // Absolute target path
            String target = path + image.getPath();
            // Create a new File object for the uploaded file
            File imageFile = new File(target);
            // Delete the old image file, if file exists
            if (fileService.exists(target) && !fileService.delete(target)) {
                logger.warn(new StringBuilder("Updating image failed! Unable to delete the old image file. Path : \"").append(target).append("\""));
                logger.warn(new StringBuilder("Updating image failed!").append(json));
                return false;
            }
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Write uploaded image file to disk. Path : \"").append(target).append("\""));
            }
            try {
                // Write the uploaded file to disk
                file.transferTo(imageFile);
            } catch (IOException ex) {
                logger.warn("Writing image file to disk failed!");
                logger.error(ex);
                logger.warn(new StringBuilder("Updating image failed!").append(json));
                return false;
            }
            // Check that the file really exists
            if (!fileService.exists(target)) {
                logger.warn(new StringBuilder("Writing image file to disk failed! File doesn't exist. Path : \"").append(target).append("\""));
                logger.warn(new StringBuilder("Updating image failed!").append(json));
                return false;
            }
        }
        // Set updated date
        image.setUpdated(new Date());
        // Try to update the DB
        if (dao.update(image)) {
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuilder("Image updated : ").append(this.jsonizer.jsonize(image, true)));
            }
            return true;
        }
        logger.warn(new StringBuilder("Failed to update image : ").append(json));
        return false;
    }

    /**
     * Checks if the image object corresponding the give id can be removed
     * from the database.
     * @param imageId the id number of the image to be removed
     * @return true if the image object can be removed, otherwise returns false
     */
    public boolean canBeDeleted(int imageId) {
        return dao.canBeDeleted(imageId);
    }

    /**
     * Returns a list of image files that are located in the images admin
     * directory related to the given owner.
     * @param owner owner of the image files
     * @return list of image files
     */
    public List<String> getAdminImages(Owner owner) {
        // Get path of the images admin dir
        String path = Settings.getInstance().getImagesPathAdmin(owner.getCode());
        // Return results
        return dirService.getFilesStr(path);
    }

    /**
     * Saves the given file in the images directory of the Admin app.
     * @param file file to be saved
     * @param owner owner of the file
     * @return name of the file if and only if the file was saved; otherwise
     * null
     */
    public String upload(MultipartFile file, Owner owner) {
        // Check for null values
        if (file == null || owner == null) {
            logger.warn("Writing image file to disk failed! File or owner can not be null.");
            return null;
        }

        // Get path of the images admin dir
        String path = Settings.getInstance().getImagesPathAdmin(owner.getCode());

        // Get the name of the file
        String name = file.getOriginalFilename();
        // Name must be unique
        name = this.getUniqueName(path, name);
        // Absolute target path
        path += name;

        // Create a new File object for the uploaded file
        File imageFile = new File(path);
        if (logger.isInfoEnabled()) {
            logger.info("Write uploaded image file to disk. Path : \"" + path + "\"");
        }
        try {
            // Write the uploaded file to disk
            file.transferTo(imageFile);
        } catch (IOException ex) {
            logger.warn("Writing image file to disk failed!");
            logger.error(ex);
            return null;
        }
        // Check that the file really exists
        if (!fileService.exists(path)) {
            logger.warn("Writing image file to disk failed! File doesn't exist. Path : \"" + path + "\"");
            return null;
        }
        if (logger.isInfoEnabled()) {
            logger.info("Writing uploaded image file to disk done. Path : \"" + path + "\"");
        }
        return name;
    }

    /**
     * Deletes the uploaded image file with given name and owner.
     * @param fileName name of the image file
     * @param owner owner of the image
     * @return true if and only if the file was deleted; otherwise false
     */
    public boolean delete(String fileName, Owner owner) {
        // Check for null values
        if (fileName == null || owner == null) {
            logger.error("Deleting uploaded image file failed! File name or owner can not be null.");
            return false;
        }
        // Get path of the images admin dir
        String path = Settings.getInstance().getImagesPathAdmin(owner.getCode());
        // Absolute target path
        path += fileName;
        // The file doesn't exist -> exit
        if (!this.adminImageExists(fileName, owner)) {
            logger.warn(new StringBuilder("Unable to delete the uploaded image file \"").append(path).append("\", because the file doesn't exist."));
            return false;
        }
        // Try to delete the file
        if (!this.fileService.delete(path)) {
            logger.warn(new StringBuilder("Failed to delete the uploaded image file \"").append(path).append("\"."));
            return false;
        }
        if (logger.isInfoEnabled()) {
            logger.info(new StringBuilder("Uploaded image file \"").append(path).append("\" was succesfully deleted."));
        }
        return true;
    }

    /**
     * Checks that an image file with the given name exists in images
     * admin directory related to the given owner.
     * @param fileName name of the image file
     * @param owner owner of the file
     * @return true if and only if the image exists; otherwise false
     */
    public boolean adminImageExists(String fileName, Owner owner) {
        // Get list of uploaded images related to the given owner
        List<String> images = this.getAdminImages(owner);
        // Go through the list and check that the given file exists
        for (String image : images) {
            if (image.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a file denoted by the given dir and filename exists, and
     * adds a timestamp in the beginning of the filename if another file
     * with the same name already exists.
     * @param dir absolute directory
     * @param filename name of the file
     * @return unique filename
     */
    private String getUniqueName(String dir, String filename) {
        String path = dir + filename;
        while (fileService.exists(path)) {
            filename = DateTimeUtil.getTimestamp() + "." + filename;
            path = dir + filename;
        }
        return filename;
    }
}
