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

import com.pkrete.locationservice.admin.model.illustration.Map;

import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.util.WebUtil;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Deprecated
/**
 * This class is deprecated. Use 
 * {@link SimplifiedMapValidator SimplifiedMapValidator} instead.
 * 
 * The {@link MapValidator MapValidator} class validates the values
 * inserted via addmap and editmap forms. This class defines the
 * obligatory fields and returns an error message associated with the field
 * when the value of the field is not valid.
 *
 * @author Petteri Kivimäki
 */
public class MapValidator implements Validator {

    private ConverterService converterService;

    /**
     * Changes the value of converterService instance variable
     * @param converterService new value to be set
     */
    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "description", "error.map.description");

        Map map = (Map) target;

        if (map.getFilePath() != null) {
            if (map.getFilePath().length() > 500) {
                errors.rejectValue("path", "error.illustration.path.length");
            }
        }

        if (map.getUrl() != null) {
            if (map.getUrl().length() > 500) {
                errors.rejectValue("url", "error.illustration.path.length");
            }
        }

        if (map.getDescription().length() > 100) {
            errors.rejectValue("description", "error.illustration.desc.length");
        }

        if (map.getColor().length() > 6) {
            errors.rejectValue("color", "error.illustration.color.length");
        }

        if (map.getOpacity().length() > 3) {
            errors.rejectValue("opacity", "error.illustration.opacity.length");
        }

        boolean filePathNull = false;
        if (map.getFilePath() == null) {
            map.setFilePath("");
            filePathNull = true;
        }
        if (map.getUrl() == null) {
            map.setUrl("");
        }

        if (map.getOwner() == null) {
            errors.rejectValue("description", "error.map.owner");
        }

        if (!map.getColor().isEmpty()) {
            if (map.getColor().length() != 6 && map.getColor().length() != 3) {
                errors.rejectValue("color", "error.map.color.length");
            } else if (!WebUtil.validateHexColor(map.getColor())) {
                errors.rejectValue("color", "error.map.color.form");
            }
        }

        if (!map.getOpacity().isEmpty()) {
            String temp = map.getOpacity();
            try {
                int opacityInt = this.converterService.strToInt(temp);
                temp = Integer.toString(opacityInt);
            } catch (NumberFormatException nfe) {
                temp = "255";
            }
            map.setOpacity(temp);
            if (map.getOpacity().length() < 1 && map.getOpacity().length() > 3) {
                errors.rejectValue("opacity", "error.map.opacity.length");
            } else if (!map.getOpacity().matches("^\\d+$")) {
                errors.rejectValue("opacity", "error.map.opacity.form");
            } else if (this.converterService.strToInt(map.getOpacity()) > 255
                    || this.converterService.strToInt(map.getOpacity()) < 0) {
                errors.rejectValue("opacity", "error.map.opacity.value");
            }
        }

        if (map.getFiles() == null && filePathNull) {
            if (map.getUrl().length() == 0) {
                errors.rejectValue("path", "error.map.url.required");
            } else if (!WebUtil.validateUrl(map.getUrl())) {
                errors.rejectValue("path", "error.map.url.bad");
            } else if (!WebUtil.exists(map.getUrl())) {
                errors.rejectValue("path", "error.map.url.exist");
            }
        } else if (map.getFiles() != null && map.getId() == 0) {
            if (map.getFilePath().length() == 0 && map.getUrl().length() == 0 && !map.hasFile()) {
                errors.rejectValue("path", "error.map.one_required");
            } else if (map.getFilePath().length() > 0 && map.getUrl().length() > 0 && map.hasFile()) {
                errors.rejectValue("path", "error.map.only_one");
            } else if (map.getFilePath().length() > 0 && map.hasFile()) {
                errors.rejectValue("path", "error.map.only_one");
            } else if (map.getFilePath().length() > 0 && map.getUrl().length() > 0) {
                errors.rejectValue("path", "error.map.only_one");
            } else if (map.getUrl().length() > 0 && map.hasFile()) {
                errors.rejectValue("path", "error.map.only_one");
            } else if (map.getUrl().length() > 0) {
                if (!WebUtil.validateUrl(map.getUrl())) {
                    errors.rejectValue("url", "error.map.url.bad");
                } else if (!WebUtil.exists(map.getUrl())) {
                    errors.rejectValue("url", "error.map.url.exist");
                }
            } else if (!map.hasFiles() && map.hasFile()) {
                errors.rejectValue("path", "error.map.file.missing");
            } else if (map.hasFiles()) {
                java.util.Map<Integer, MultipartFile> files = map.getFiles();
                for (Entry<Integer, MultipartFile> entry : files.entrySet()) {
                    String name = entry.getValue().getOriginalFilename().toLowerCase();
                    if (!name.matches(".*\\.(jpg|png|gif|bmp)$")) {
                        errors.rejectValue("files[" + entry.getKey() + "]", "error.map.file.format.invalid");
                    }
                }
                if (files.size() > 1) {
                    List<MultipartFile> list = map.getFilesList();
                    for (int i=0; i < list.size() - 1; i++) {
                        String file1 = list.get(i).getOriginalFilename();
                        String file2 = list.get(i + 1).getOriginalFilename();
                        int length = file1.length();
                        if (!file2.endsWith(file1.substring(length - 3, length))) {
                            errors.rejectValue("path", "error.map.file.format.different");
                        }
                    }
                }
            }
        } else if (map.getFiles() != null && map.getId() > 0) {
            java.util.Map<Integer, MultipartFile> files = map.getFiles();
            for (Entry<Integer, MultipartFile> entry : files.entrySet()) {
                String name = entry.getValue().getOriginalFilename().toLowerCase();
                if (name.length() > 0) {
                    if (!name.matches(".*\\.(jpg|png|gif|bmp)$")) {
                        errors.rejectValue("files[" + entry.getKey() + "]", "error.map.file.format.invalid");
                    }
                }
            }

            if (files.size() > 1) {
                String ending = "";
                for (Entry<Integer, MultipartFile> entry : files.entrySet()) {
                    if (entry.getValue().getOriginalFilename().length() > 0) {
                        int length = entry.getValue().getOriginalFilename().length();
                        ending = entry.getValue().getOriginalFilename().substring(length - 3, length);
                        break;
                    }
                }
                for (Entry<Integer, MultipartFile> entry : files.entrySet()) {
                    if (entry.getValue().getOriginalFilename().length() > 0) {
                        if (!entry.getValue().getOriginalFilename().endsWith(ending) || !map.getPath().endsWith(ending)) {
                           errors.rejectValue("files[" + entry.getKey() + "]", "error.map.file.format.different");
                        }
                    }
                }
            }
        }
    }
}
