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

import com.pkrete.locationservice.admin.model.illustration.Map;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.service.MapsService;
import com.pkrete.locationservice.admin.util.WebUtil;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

/**
 * The {@link SimplifiedMapValidator SimplifiedMapValidator} class validates Map
 * objects. This class defines the obligatory fields and returns an error
 * message associated with the field when the value of the field is not valid.
 *
 * @author Petteri Kivimäki
 */
public class SimplifiedMapValidator implements Validator {

    private ConverterService converterService;
    private MapsService mapsService;

    public void setMapsService(MapsService mapsService) {
        this.mapsService = mapsService;
    }

    /**
     * Changes the value of converterService instance variable
     *
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
        // Regex for validating the file type
        String fileTypeRegex = ".*\\.(jpg|png|gif|bmp)$";

        ValidationUtils.rejectIfEmpty(errors, "description", "error.map.description");

        Map map = (Map) target;

        boolean filePathNullOrEmpty = map.getFilePath() != null && !map.getFilePath().isEmpty() ? false : true;
        boolean urlNullOrEmpty = map.getUrl() != null && !map.getUrl().isEmpty() ? false : true;
        boolean filesNullOrEmpty = map.getFiles() != null && map.hasFile() ? false : true;

        int count = getCount(filePathNullOrEmpty, urlNullOrEmpty, filesNullOrEmpty);

        if (!filePathNullOrEmpty && map.getFilePath().length() > 500) {
            errors.rejectValue("path", "error.illustration.path.length");
        }

        if (!urlNullOrEmpty && map.getUrl().length() > 500) {
            errors.rejectValue("url", "error.illustration.path.length");
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
            try {
                Integer.parseInt(map.getOpacity());
            } catch (NumberFormatException nfe) {
                errors.rejectValue("opacity", "error.map.opacity.form");
            }
            if (map.getOpacity().length() < 1 && map.getOpacity().length() > 3) {
                errors.rejectValue("opacity", "error.map.opacity.length");
            } else if (this.converterService.strToInt(map.getOpacity()) > 255
                    || this.converterService.strToInt(map.getOpacity()) < 0) {
                errors.rejectValue("opacity", "error.map.opacity.value");
            }
        }

        // If id is 0 the map is new, and it must have files or url
        if (map.getId() == 0 && count == 0) {
            errors.rejectValue("path", "error.map.one_required");
        } else if (count == 0 && map.getIsExternal()) {
            // External map must have a URL
            errors.rejectValue("url", "error.map.url.required");
        } else if (count > 1) {
            // Map can not have more than one file or url
            errors.rejectValue("path", "error.map.only_one");
        } else if (count == 1) {
            if (!filePathNullOrEmpty) {
                if (map.getId() == 0 || map.getIsExternal()) {
                    if (!this.mapsService.adminMapExists(map.getFilePath(), map.getOwner())) {
                        errors.rejectValue("path", "error.map.file.bad.multi");
                    }
                } else {
                    List<Language> list = null;
                    if (!this.mapsService.adminMapExists(map.getFilePath(), list, map.getOwner())) {
                        errors.rejectValue("path", "error.map.file.bad.single");
                    }
                }
                if (!map.getFilePath().toLowerCase().matches(fileTypeRegex)) {
                    errors.rejectValue("path", "error.map.file.format.invalid");
                } else if (map.getId() != 0 && !map.getIsExternal()) {
                    // Get file type of the current file
                    int length = map.getPath().length();
                    String ending = map.getPath().substring(length - 3, length);
                    // Check that the file type matches with the current type
                    if (!ending.isEmpty() && !map.getFilePath().endsWith(ending)) {
                        errors.rejectValue("path", "error.map.file.format.original");
                    }
                }
            } else if (!filesNullOrEmpty) {
                if (map.getId() == 0) {
                    if (!map.hasFiles() && map.hasFile()) {
                        errors.rejectValue("path", "error.map.file.missing");
                    } else {
                        // Check that all the files are of the same type
                        List<MultipartFile> list = map.getFilesList();
                        for (int i = 0; i < list.size() - 1; i++) {
                            String file1 = list.get(i).getOriginalFilename();
                            String file2 = list.get(i + 1).getOriginalFilename();
                            int length = file1.length();
                            if (!file2.endsWith(file1.substring(length - 3, length))) {
                                errors.rejectValue("path", "error.map.file.format.different");
                            }
                        }
                    }
                }
                // Get file type of the current file
                int length = map.getPath() == null ? 0 : map.getPath().length();
                String ending = map.getPath() != null && map.getPath().matches(fileTypeRegex) ? map.getPath().substring(length - 3, length) : "";
                // Go through all the files
                java.util.Map<Integer, MultipartFile> files = map.getFiles();
                for (Entry<Integer, MultipartFile> entry : files.entrySet()) {
                    if (entry.getValue() != null && entry.getValue().getSize() > 0) {
                        String name = entry.getValue().getOriginalFilename().toLowerCase();
                        // Check that the file type is valid
                        if (!name.matches(fileTypeRegex)) {
                            errors.rejectValue("files[" + entry.getKey() + "]", "error.map.file.format.invalid");
                        }
                        if (map.getId() > 0) {
                            // Check that the file type matches with the current type
                            if (!ending.isEmpty() && !entry.getValue().getOriginalFilename().endsWith(ending)) {
                                errors.rejectValue("files[" + entry.getKey() + "]", "error.map.file.format.original");
                            }
                        }
                    }
                }
            } else if (!urlNullOrEmpty) {
                if (!WebUtil.validateUrl(map.getUrl())) {
                    errors.rejectValue("url", "error.map.url.bad");
                } else if (!WebUtil.exists(map.getUrl())) {
                    errors.rejectValue("url", "error.map.url.exist");
                }
            }
        }
    }

    private int getCount(boolean filePathNullOrEmpty, boolean urlNullOrEmpty, boolean filesNullOrEmpty) {
        int count = 0;

        if (!filePathNullOrEmpty) {
            count++;
        }
        if (!urlNullOrEmpty) {
            count++;
        }
        if (!filesNullOrEmpty) {
            count++;
        }
        return count;
    }
}
