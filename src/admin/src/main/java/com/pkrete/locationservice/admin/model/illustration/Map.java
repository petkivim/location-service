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
package com.pkrete.locationservice.admin.model.illustration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pkrete.locationservice.admin.deserializers.MapJSONDeserializer;
import com.pkrete.locationservice.admin.model.language.Language;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.web.multipart.MultipartFile;

/**
 * The <code>Map</code> class extends the {@link Illustration Illustration} class.
 *
 * A map represents a map that is used for showing a position of a location. Map
 * can be a map of a library or a geographical map. Map can be located on the same server
 * as the Location Service or on some external server or service.
 *
 * @author Petteri Kivimäki
 */
@JsonDeserialize(using = MapJSONDeserializer.class)
public class Map extends Illustration {

    /**
     * This variable is used when user uploads maps to the server.
     * This variable is not saved to the db and therefore it's not
     * mapped.
     */
    private java.util.Map<Integer, MultipartFile> files;
    /**
     * Drawing color that is used for drawing
     * the locations on the map.
     */
    private String color;
    /**
     * Opacity of the drawing color.
     */
    private String opacity;
    /**
     * Tells if this map is a google map or not.
     * This value is not saved to database.
     */
    public boolean isGoogleMap;

    /**
     * Constructs and initializes a map with no path and sets isExternal to false.
     */
    public Map() {
        color = "";
        opacity = "";
    }

    /**
     * Constructs and initializes a map with no path and sets isExternal to 
     * false. Files Map is initiated with the given languages.
     * @param languages available Languages in the system
     */
    public Map(List<Language> languages) {
        color = "";
        opacity = "";
        files = new LinkedHashMap<Integer, MultipartFile>();
        for (Language lang : languages) {
            files.put(lang.getId(), null);
        }
    }

    /**
     * Constructs and initializes a map with the given path and sets isExternal to false.
     * @param path the location of the map
     * @param the description of the map
     */
    public Map(String path, String description) {
        super(path, description, false);
        color = "";
        opacity = "";
    }

    /**
     * Constructs and initializes a map with the given path and isExternal value.
     * @param path the location of the map
     * @param the description of the map
     * @param isExternal the value that tells if the map is located on the same server as the Location Service
     */
    public Map(String path, String description, boolean isExternal) {
        super(path, description, isExternal);
        color = "";
        opacity = "";
    }

    /**
     * Constructs and initializes a Map with the given values.
     * @param id unique id of this map
     * @param url URL of the map
     * @param filePath path of the file that this map represents
     * @param the description of the map
     */
    public Map(int id, String url, String filePath, String description) {
        super(url, filePath, description);
        this.id = id;
    }

    /**
     * Changes the files related to this map.
     * @param file new files
     */
    public void setFiles(java.util.Map<Integer, MultipartFile> files) {
        this.files = files;
    }

    /**
     * Returns the files related to this map.
     * @return files related to this map
     */
    public java.util.Map<Integer, MultipartFile> getFiles() {
        return files;
    }

    /**
     * Initializes files map structure with the given Languages.
     * @param languages list of languages
     */
    public void createFilesMap(List<Language> languages) {
        this.files = new LinkedHashMap<Integer, MultipartFile>();
        for (Language lang : languages) {
            this.files.put(lang.getId(), null);
        }
    }

    /**
     * Checks if all the positions of the files map
     * contain a file.
     * @return true if all the positions contain a file, otherwise false
     */
    public boolean hasFiles() {
        if (this.files == null || this.files.isEmpty()) {
            return false;
        }
        for (Entry<Integer, MultipartFile> entry : this.files.entrySet()) {
            if (entry == null || entry.getValue().getSize() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if at least one position of the files map
     * contains a file.
     * @return true if at least one cells contains a file, otherwise returns false
     */
    public boolean hasFile() {
        if (this.files == null || this.files.isEmpty()) {
            return false;
        }
        for (Entry<Integer, MultipartFile> entry : this.files.entrySet()) {
            if (entry != null && entry.getValue().getSize() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the given Language-MultipartFile pair to the files map.
     * @param language Language of the file
     * @param file map file
     */
    public void addFile(Language language, MultipartFile file) {
        if (this.files == null) {
            this.files = new LinkedHashMap<Integer, MultipartFile>();
        }
        this.files.put(language.getId(), file);
    }

    /**
     * Returns a list of MultipartFiles related to this Map.
     * @return list of MultipartFiles related to this Map
     */
    public List<MultipartFile> getFilesList() {
        List<MultipartFile> list = new ArrayList<MultipartFile>();
        if (this.files == null) {
            return list;
        }
        for (Entry<Integer, MultipartFile> entry : this.files.entrySet()) {
            if (entry.getValue() != null && entry.getValue().getSize() > 0) {
                list.add(entry.getValue());
            }
        }
        return list;
    }

    /**
     * Return the drawing color for this map.
     * @return default drawing color
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Changes the drawing color.
     * @param color new drawing color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Returns the opacity of the drawing color.
     * @return opacity of the drawing color
     */
    public String getOpacity() {
        return this.opacity;
    }

    /**
     * Changes the opacity of the drawing color.
     * @param opacity new opacity
     */
    public void setOpacity(String opacity) {
        this.opacity = opacity;
    }

    /**
     * Tells if this map belongs to the Google Map service. It's important
     * to know, because Google maps are not supposed to be handled by
     * ImageHandler.
     * @return returns true if this is a link to Google Maps, otherwise false
     */
    public boolean isGoogleMap() {
        if (!this.getIsExternal()) {
            return false;
        }
        if (this.getPath().matches("^http(s|):\\/\\/maps(engine|)\\.google\\.com.+$")) {
            return true;
        }
        if (this.getPath().matches("^http(s|):\\/\\/www\\.google\\.com\\/maps\\/.+$")) {
            return true;
        }
        return false;
    }

    /**
     * Tells if this map belongs to the Google Map service. This method can
     * be called from jsp pages, because this class has a variable called
     * isGoogleMap.
     * @return returns true if this is a link to Google Maps, otherwise false
     */
    public boolean getIsGoogleMap() {
        return isGoogleMap();
    }
}
