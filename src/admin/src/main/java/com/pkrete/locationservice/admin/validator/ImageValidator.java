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
package com.pkrete.locationservice.admin.validator;

import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.util.WebUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Deprecated
/**
 * This class is deprecated. Use
 * {@link SimplifiedImageValidator SimplifiedImageValidator} instead.
 *
 * The {@link ImageValidator ImageValidator} class validates the values inserted
 * via addimage and editimage forms. This class defines the obligatory fields
 * and returns an error message associated with the field when the value of the
 * field is not valid.
 *
 * @author Petteri Kivimäki
 */
public class ImageValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Image.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "description", "error.image.description");

        Image image = (Image) target;

        if (image.getFilePath() != null) {
            if (image.getFilePath().length() > 500) {
                errors.rejectValue("path", "error.illustration.path.length");
            }
        }

        if (image.getUrl() != null) {
            if (image.getUrl().length() > 500) {
                errors.rejectValue("url", "error.illustration.path.length");
            }
        }

        if (image.getDescription().length() > 100) {
            errors.rejectValue("description", "error.illustration.desc.length");
        }

        boolean filePathNull = false;
        if (image.getFilePath() == null) {
            image.setFilePath("");
            filePathNull = true;
        }
        if (image.getUrl() == null) {
            image.setUrl("");
        }

        if (image.getOwner() == null) {
            errors.rejectValue("description", "error.image.owner");
        }

        if (image.getFile() == null && filePathNull) {
            if (image.getUrl().length() == 0) {
                errors.rejectValue("path", "error.image.url.required");
            } else if (!WebUtil.validateUrl(image.getUrl())) {
                errors.rejectValue("path", "error.image.url.bad");
            } else if (!WebUtil.exists(image.getUrl())) {
                errors.rejectValue("path", "error.image.url.exist");
            }
        } else if (image.getFile() != null && image.getId() == 0) {
            if (image.getFilePath().length() == 0 && image.getUrl().length() == 0 && image.getFile().getSize() == 0) {
                errors.rejectValue("path", "error.image.one_required");
            } else if (image.getFilePath().length() > 0 && image.getUrl().length() > 0 && image.getFile().getSize() > 0) {
                errors.rejectValue("path", "error.image.only_one");
            } else if (image.getFilePath().length() > 0 && image.getFile().getSize() > 0) {
                errors.rejectValue("path", "error.image.only_one");
            } else if (image.getFilePath().length() > 0 && image.getUrl().length() > 0) {
                errors.rejectValue("path", "error.image.only_one");
            } else if (image.getUrl().length() > 0 && image.getFile().getSize() > 0) {
                errors.rejectValue("path", "error.image.only_one");
            } else if (image.getUrl().length() > 0) {
                if (!WebUtil.validateUrl(image.getUrl())) {
                    errors.rejectValue("url", "error.image.url.bad");
                } else if (!WebUtil.exists(image.getUrl())) {
                    errors.rejectValue("url", "error.image.url.exist");
                }
            } else if (image.getFile().getSize() > 0) {
                String name = image.getFile().getOriginalFilename().toLowerCase();
                if (!name.matches(".*\\.(jpg|png|gif|bmp)$")) {
                    errors.rejectValue("file", "error.image.file");
                }
            }
        } else if (image.getFile() != null) {
            if (image.getFile().getSize() > 0) {
                String name = image.getFile().getOriginalFilename().toLowerCase();
                if (!name.matches(".*\\.(jpg|png|gif|bmp)$")) {
                    errors.rejectValue("file", "error.image.file");
                }
            }
        } else if (!image.getFilePath().isEmpty() && !image.getUrl().isEmpty()) {
            errors.rejectValue("path", "error.image.only_one");
        } else if (image.getFilePath().isEmpty() && image.getUrl().isEmpty()) {
            errors.rejectValue("path", "error.image.one_required");
        }
    }
}
