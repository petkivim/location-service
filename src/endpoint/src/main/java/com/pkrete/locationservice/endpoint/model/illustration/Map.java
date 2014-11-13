/**
 * This file is part of Location Service :: Endpoint. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.model.illustration;

/**
 * The <code>Map</code> class extends the {@link Illustration Illustration}
 * class.
 *
 * A map represents a map that is used for showing a position of a location. Map
 * can be a map of a library or a geographical map. Map can be located on the
 * same server as the Location Service or on some external server or service.
 *
 * @author Petteri Kivimäki
 */
public class Map extends Illustration {

    /**
     * Drawing color that is used for drawing the locations on the map.
     */
    private String color;
    /**
     * Opacity of the drawing color.
     */
    private String opacity;

    /**
     * Constructs and initializes a map with no path and sets isExternal to
     * false.
     */
    public Map() {
        color = "";
        opacity = "";
    }

    /**
     * Constructs and initializes a map with the given path and sets isExternal
     * to false.
     *
     * @param path the location of the map
     */
    public Map(String path) {
        super(path, false);
        color = "";
        opacity = "";
    }

    /**
     * Constructs and initializes a map with the given path and isExternal
     * value.
     *
     * @param path the location of the map
     * @param isExternal the value that tells if the map is located on the same
     * server as the Location Service
     */
    public Map(String path, boolean isExternal) {
        super(path, isExternal);
        color = "";
        opacity = "";
    }

    /**
     * Return the drawing color for this map.
     *
     * @return default drawing color
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Changes the drawing color.
     *
     * @param color new drawing color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Returns the opacity of the drawing color.
     *
     * @return opacity of the drawing color
     */
    public String getOpacity() {
        return this.opacity;
    }

    /**
     * Changes the opacity of the drawing color.
     *
     * @param opacity new opacity
     */
    public void setOpacity(String opacity) {
        this.opacity = opacity;
    }

    /**
     * Tells if this map belongs to the Google Map service. It's important to
     * know, because Google maps are not supposed to be handled by ImageHandler.
     *
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
     * If the map is a Google map, returns an URL that makes it possible to
     * embed the map on the website. If the map isn't a Google map, an empty
     * string is returned.
     *
     * @return URL of the map or an empty string
     */
    public String getGoogleMapEmbedUrl() {
        if (!isGoogleMap()) {
            return "";
        }
        if (this.getPath().contains("output=embed") || this.getPath().contains("/embed")) {
            return this.getPath();
        } else if (this.getPath().contains("/edit") || this.getPath().contains("/viewer")) {
            return this.getPath().replaceAll("(/edit|/viewer)", "/embed");
        }
        return this.getPath() + "&amp;output=embed";
    }

    /**
     * If the map is a Google map, returns a link to the Google maps page. If
     * the map isn't a Google map, an empty string is returned.
     *
     * @return URL of the map or an empty string
     */
    public String getGoogleMapLinkUrl() {
        if (!isGoogleMap()) {
            return "";
        }
        if (this.getPath().matches("^http(s|):\\/\\/www\\.google\\.com\\/maps\\/.+$")) {
            return this.getPath();
        } else if (!this.getPath().contains("output=embed") && !this.getPath().contains("/edit") && !this.getPath().contains("/embed")) {
            return this.getPath();
        } else if (this.getPath().contains("/edit") || this.getPath().contains("/embed")) {
            return this.getPath().replaceAll("(/edit|/embed)", "/viewer");
        }
        return this.getPath().replaceAll("&(amp;|)output=embed", "");
    }
}
