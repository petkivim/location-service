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
package com.pkrete.locationservice.admin.converter.json;

import com.pkrete.locationservice.admin.model.illustration.Map;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.io.FileService;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import com.pkrete.locationservice.admin.service.LanguagesService;
import com.pkrete.locationservice.admin.util.DateTimeUtil;
import com.pkrete.locationservice.admin.util.Settings;
import java.util.List;

/**
 * This class converts Map objects to JSON.
 *
 * @author Petteri Kivimäki
 */
public class MapJSONizer implements JSONizerService<Map> {

    private LanguagesService languagesService;
    private FileService fileService;

    public void setLanguagesService(LanguagesService languagesService) {
        this.languagesService = languagesService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Converts a single Map object to JSON string. All the variables of the Map
     * object are included.
     *
     * @param source Map object to be converted
     * @return JSON string
     */
    @Override
    public String jsonize(Map source) {
        return this.jsonize(source, false);
    }

    /**
     * Converts a single Map object to JSON string. All the variables of the Map
     * object are included. If logEntry is true, also owner_id is included.
     *
     * @param source Map object to be converted
     * @param logEntry is this JSON for a log entry
     * @return JSON string
     */
    @Override
    public String jsonize(Map source, boolean logEntry) {
        List<Language> languages = null;
        if (source != null) {
            languages = this.languagesService.getLanguages(source.getOwner());
        }
        final String url = Settings.getInstance().getMapsWebPath(source.getOwner().getCode());
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":").append(source.getId());
        builder.append(",\"description\":\"").append(source.getDescription()).append("\"");
        builder.append(",\"color\":\"").append(source.getColor()).append("\"");
        builder.append(",\"opacity\":\"").append(source.getOpacity()).append("\"");
        builder.append(",\"is_external\":").append(source.getIsExternal()).append("");
        if (source.getIsExternal()) {
            builder.append(",\"url\":\"").append(source.getPath()).append("\"");
        } else {
            // Get path of the maps dir
            String path = Settings.getInstance().getMapsPath(source.getOwner().getCode());
            builder.append(",\"urls\":[");
            for (int i = 0; i < languages.size(); i++) {
                builder.append("{\"lang_id\":").append(languages.get(i).getId());
                if (this.fileService.exists(path + languages.get(i).getCode() + "/" + source.getPath())) {
                    builder.append(",\"url\":\"").append(url);
                    builder.append(languages.get(i).getCode()).append("/").append(source.getPath()).append("\"");
                } else {
                    builder.append(",\"url\":\"\"");
                }
                builder.append("}");
                if (i < languages.size() - 1) {
                    builder.append(",");
                }
            }
            builder.append("]");

        }
        if (logEntry) {
            builder.append(",\"owner_id\":").append(source.getOwner().getId()).append("");
        }
        builder.append(",\"created_at\":\"");
        builder.append(DateTimeUtil.dateToString(source.getCreated())).append("\"");
        builder.append(",\"create_operator\":\"").append(source.getCreator()).append("\"");
        builder.append(",\"updated_at\":\"");
        builder.append((source.getUpdated() == null ? "" : DateTimeUtil.dateToString(source.getUpdated()))).append("\"");
        builder.append(",\"update_operator\":\"").append((source.getUpdater() == null ? "" : source.getUpdater())).append("\"");
        builder.append("}");
        return builder.toString();

    }

    /**
     * Converts a list of Map objects to JSON string. Only selected variables
     * are included.
     *
     * @param sources Map objects to be converted
     * @return JSON string
     */
    @Override
    public String jsonize(List<Map> sources) {
        List<Language> languages = null;
        if (sources != null && !sources.isEmpty()) {
            languages = this.languagesService.getLanguages(sources.get(0).getOwner());
        }

        String url = null;
        if (!sources.isEmpty()) {
            url = Settings.getInstance().getMapsWebPath(sources.get(0).getOwner().getCode());
        }
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < sources.size(); i++) {
            Map source = sources.get(i);
            builder.append("{\"id\":").append(source.getId());
            builder.append(",\"description\":\"").append(source.getDescription()).append("\"");
            builder.append(",\"is_external\":").append(source.getIsExternal()).append("");
            if (source.getIsExternal()) {
                builder.append(",\"url\":\"").append(source.getPath()).append("\"");
            } else {
                // Get path of the maps dir
                String path = Settings.getInstance().getMapsPath(source.getOwner().getCode());
                builder.append(",\"urls\":[");
                for (int j = 0; j < languages.size(); j++) {
                    builder.append("{\"lang_id\":").append(languages.get(j).getId());
                    if (this.fileService.exists(path + languages.get(j).getCode() + "/" + source.getPath())) {
                        builder.append(",\"url\":\"").append(url);
                        builder.append(languages.get(j).getCode()).append("/").append(source.getPath()).append("\"");
                    } else {
                        builder.append(",\"url\":\"\"");
                    }
                    builder.append("}");
                    if (j < languages.size() - 1) {
                        builder.append(",");
                    }
                }
                builder.append("]");
            }
            builder.append("}");
            if (i < sources.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
