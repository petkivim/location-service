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
package com.pkrete.locationservice.admin.converter.objectmap;

import com.pkrete.locationservice.admin.model.illustration.Map;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.io.FileService;
import com.pkrete.locationservice.admin.service.LanguagesService;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.util.DateTimeUtil;
import com.pkrete.locationservice.admin.util.Settings;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class converts Image objects to Map.
 *
 * @author Petteri Kivimäki
 */
public class MapMapService implements ObjectMapService<Map> {

    private LanguagesService languagesService;
    private FileService fileService;

    public void setLanguagesService(LanguagesService languagesService) {
        this.languagesService = languagesService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Converts a single Map object to java.util.Map object. All the variables
     * of the Map object are included.
     *
     * @param source Map object to be converted
     * @return java.util.Map object
     */
    @Override
    public java.util.Map convert(Map source) {
        return this.convert(source, false);
    }

    /**
     * Converts a single Map object to java.util.Map object. All the variables
     * of the Map object are included. If logEntry is true, also owner_id is
     * included.
     *
     * @param source Map object to be converted
     * @param logEntry is this for a log entry
     * @return java.util.Map object
     */
    @Override
    public java.util.Map convert(Map source, boolean logEntry) {
        List<Language> languages = null;
        if (source != null) {
            languages = this.languagesService.getLanguages(source.getOwner());
        }
        final String url = Settings.getInstance().getMapsWebPath(source.getOwner().getCode());
        java.util.Map map = new LinkedHashMap();
        map.put("id", source.getId());
        map.put("description", source.getDescription());
        map.put("color", source.getColor());
        map.put("opacity", source.getOpacity());
        map.put("is_external", source.getIsExternal());
        if (source.getIsExternal()) {
            map.put("url", source.getPath());
        } else {
            // Get path of the maps dir
            String path = Settings.getInstance().getMapsPath(source.getOwner().getCode());
            List urls = new ArrayList();
            for (Language lang : languages) {
                java.util.Map info = new LinkedHashMap();
                info.put("lang_id", lang.getId());
                if (this.fileService.exists(path + lang.getCode() + "/" + source.getPath())) {
                    info.put("url", url + lang.getCode() + "/" + source.getPath());
                } else {
                    info.put("url", "");
                }
                urls.add(info);
            }
            map.put("urls", urls);
        }
        if (logEntry) {
            map.put("owner_id", source.getOwner().getId());
        }
        map.put("created_at", DateTimeUtil.dateToString(source.getCreated()));
        map.put("create_operator", source.getCreator());
        map.put("updated_at", (source.getUpdated() == null ? "" : DateTimeUtil.dateToString(source.getUpdated())));
        map.put("update_operator", (source.getUpdater() == null ? "" : source.getUpdater()));
        return map;
    }

    /**
     * Converts a list of Image objects to a list of Map objects. Only selected
     * variables are included.
     *
     * @param sources Map objects to be converted
     * @return list of Map objects
     */
    @Override
    public List convert(List<Map> sources) {
        String url = null;
        List<Language> languages = null;
        if (!sources.isEmpty()) {
            url = Settings.getInstance().getMapsWebPath(sources.get(0).getOwner().getCode());
            languages = this.languagesService.getLanguages(sources.get(0).getOwner());
        }
        List maps = new ArrayList();
        for (Map source : sources) {
            java.util.Map map = new LinkedHashMap();
            map.put("id", source.getId());
            map.put("description", source.getDescription());
            map.put("is_external", source.getIsExternal());
            if (source.getIsExternal()) {
                map.put("url", source.getPath());
            } else {
                // Get path of the maps dir
                String path = Settings.getInstance().getMapsPath(source.getOwner().getCode());
                List urls = new ArrayList();
                for (Language lang : languages) {
                    java.util.Map info = new LinkedHashMap();
                    info.put("lang_id", lang.getId());
                    if (this.fileService.exists(path + lang.getCode() + "/" + source.getPath())) {
                        info.put("url", url + lang.getCode() + "/" + source.getPath());
                    } else {
                        info.put("url", "");
                    }
                    urls.add(info);
                }
                map.put("urls", urls);
            }
            maps.add(map);
        }
        return maps;
    }
}
