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
package com.pkrete.locationservice.admin.validator;

import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.service.ImagesService;
import com.pkrete.locationservice.admin.util.WebUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * The {@link SimplifiedImageValidator SimplifiedImageValidator} class 
 * validates Image objects. This class defines the obligatory fields and 
 * returns an error message associated with the field when the value of 
 * the field is not valid.
 *
 * @author Petteri Kivimäki
 */
public class SimplifiedImageValidator implements Validator {

    private ImagesService imagesService;

    public void setImagesService(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Image.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // Regex for validating the file type
        String fileTypeRegex = ".*\\.(jpg|png|gif|bmp)$";

        ValidationUtils.rejectIfEmpty(errors, "description", "error.image.description");

        Image image = (Image) target;

        boolean filePathNullOrEmpty = image.getFilePath() != null && !image.getFilePath().isEmpty() ? false : true;
        boolean urlNullOrEmpty = image.getUrl() != null && !image.getUrl().isEmpty() ? false : true;
        boolean fileNullOrEmpty = image.getFile() != null && image.getFile().getSize() != 0 ? false : true;

        int count = getCount(filePathNullOrEmpty, urlNullOrEmpty, fileNullOrEmpty);

        if (image.getOwner() == null) {
            errors.rejectValue("description", "error.image.owner");
        }

        if (!filePathNullOrEmpty) {
            if (image.getFilePath().length() > 500) {
                errors.rejectValue("path", "error.illustration.path.length");
            }
        }

        if (!urlNullOrEmpty) {
            if (image.getUrl().length() > 500) {
                errors.rejectValue("url", "error.illustration.path.length");
            }
        }

        if (image.getDescription().length() > 100) {
            errors.rejectValue("description", "error.illustration.desc.length");
        }

        // If id is 0 the image is new, and it must have file or url
        if (image.getId() == 0 && count == 0) {
            errors.rejectValue("path", "error.image.one_required");
        } else if (count == 0 && image.getIsExternal()) {
            // External image must have a URL
            errors.rejectValue("url", "error.image.url.required");
        } else if (count > 1) {
            // Image can not have more than one file or url
            errors.rejectValue("path", "error.image.only_one");
        } else if (count == 1) {
            if (!filePathNullOrEmpty) {
                if (!this.imagesService.adminImageExists(image.getFilePath(), image.getOwner())) {
                    errors.rejectValue("file", "error.image.file.bad");
                } else if (!image.getFilePath().toLowerCase().matches(fileTypeRegex)) {
                    errors.rejectValue("file", "error.image.file");
                }
            } else if (!fileNullOrEmpty) {
                if (!image.getFile().getOriginalFilename().toLowerCase().matches(fileTypeRegex)) {
                    errors.rejectValue("file", "error.image.file");
                }
            } else if (!urlNullOrEmpty) {
                if (!WebUtil.validateUrl(image.getUrl())) {
                    errors.rejectValue("url", "error.image.url.bad");
                } else if (!WebUtil.exists(image.getUrl())) {
                    errors.rejectValue("url", "error.image.url.exist");
                }
            }
        }
    }

    private int getCount(boolean filePathNullOrEmpty, boolean urlNullOrEmpty, boolean fileNullOrEmpty) {
        int count = 0;

        if (!filePathNullOrEmpty) {
            count++;
        }
        if (!urlNullOrEmpty) {
            count++;
        }
        if (!fileNullOrEmpty) {
            count++;
        }
        return count;
    }
}
