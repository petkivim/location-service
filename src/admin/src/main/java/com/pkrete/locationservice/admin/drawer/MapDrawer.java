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
package com.pkrete.locationservice.admin.drawer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.net.URL;
import com.pkrete.locationservice.admin.model.location.Area;
import com.pkrete.locationservice.admin.model.illustration.Illustration;
import com.pkrete.locationservice.admin.model.illustration.Map;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.util.Settings;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;

/**
 * This class implements {@link Drawer Drawer} interface and implements
 * all the methods defined in it. This class can be used for drawing
 * positions of Area objects over maps.
 * 
 * @author Petteri Kivimäki
 */
public class MapDrawer implements Drawer {

    private final static Logger logger = Logger.getLogger(MapDrawer.class.getName());
    private ConverterService converterService;

    /**
     * Changes the value of converterService instance variable
     * @param converterService new value to be set
     */
    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    /**
     * Creates a BufferedImage that presents the location of the given
     * areas on the given image. The image is expected to be a Map object.
     * If the image is not a Map object, null is returned.
     * @param illustration image that's used as base
     * @param areas area that are drawn over the image
     * @param lang language of the image
     * @return BufferedImage that presents the location of the given
     * given areas on the given image
     */
    public BufferedImage draw(Illustration illustration, List<Area> areas, String lang) {
        // If illustration is null, there's nothing to do
        if (illustration == null) {
            logger.warn("Given Illustration object can not be null.");
            return null;
        }
        // If illustration is not a map, there's nothing to do
        if (!(illustration instanceof Map)) {
            logger.warn("Given Illustration object is not a Map.");
            return null;
        }
        // Cast illustration to Map
        Map map = (Map) illustration;

        // If map is a Google Map, there's nothing to do
        if (map.isGoogleMap()) {
            logger.warn("The given Map is a Google Map.");
            return null;
        }
        // If areas is null, there's nothing to do
        if (areas == null) {
            logger.warn("Areas list can not be null.");
            return null;
        }

        // Create an image
        BufferedImage bufferedImage = null;

        try {
            // Create an image
            if (!map.getIsExternal()) {
                File mapFile = loadMapFile(map, lang);
                bufferedImage = ImageIO.read(mapFile);
            } else {
                bufferedImage = ImageIO.read(new URL(map.getPath()));
            }

            if (bufferedImage != null) {
                // Get drawing color as a HEX string
                String colorStr = getColor(map);
                // Get opacity
                int opacity = getOpacity(map);
                // Parse red value from the HEX string
                int intValue1 = parseColor(colorStr, Color.RED);
                // Parse green value from the HEX string
                int intValue2 = parseColor(colorStr, Color.GREEN);
                // Parse blue value from the HEX string
                int intValue3 = parseColor(colorStr, Color.BLUE);
                // Create new color object
                Color color = new Color(intValue1, intValue2, intValue3, opacity);

                // Draw an oval
                Graphics g = bufferedImage.getGraphics();
                Graphics2D g2d;
                g2d = (Graphics2D) g.create();
                g2d.setColor(color);

                // Draw the areas
                for (Area area : areas) {
                    Rectangle rectangle = new Rectangle(area.getX1(), area.getY1(), (area.getX2() - area.getX1()), (area.getY2() - area.getY1()));
                    AffineTransform transform = new AffineTransform();
                    transform.rotate(Math.toRadians(area.getAngle()), rectangle.getX() + rectangle.width / 2, rectangle.getY() + rectangle.height / 2);
                    g2d.setTransform(transform);
                    g2d.fill(rectangle);
                }

                // Free graphic resources
                g.dispose();
            }

        } catch (Exception e) {
            logger.error(e);
        }
        return bufferedImage;
    }

    /**
     * Loads the file related to the given Map object.
     * @param map Map object
     * @param lang the language of the UI
     * @return file related to the given Map object or null if the file
     * doesn't exist
     */
    private File loadMapFile(Map map, String lang) {
        if (lang == null) {
            logger.error("\"lang\" can not be null.");
            return null;
        }
        StringBuilder path = new StringBuilder();
        path.append(Settings.getInstance().getMapsPath(map.getOwner().getCode()));
        path.append(lang).append("/").append(map.getPath());
        File file = new File(path.toString());
        if (file.exists()) {
            return file;
        }
        logger.error("The image file doesn't exist! Path: \"" + file.getAbsolutePath() + "\"");
        return null;
    }

    /**
     * Parses red, green or blue component value from the given HEX color 
     * string according to the rgb parameter value.
     * @param color color as a HEX string, 3 or 6 characters long
     * @param rgb the value that is parsed from the HEX string
     * @return red, green or blue value
     */
    private int parseColor(String color, Color rgb) {
        switch (color.length()) {
            case 3:
                if (rgb == Color.RED) {
                    return this.converterService.hexToInt(color.substring(0, 1));
                } else if (rgb == Color.GREEN) {
                    return this.converterService.hexToInt(color.substring(1, 2));
                } else if (rgb == Color.BLUE) {
                    return this.converterService.hexToInt(color.substring(2, 3));
                }
                break;
            case 6:
                if (rgb == Color.RED) {
                    return this.converterService.hexToInt(color.substring(0, 2));
                } else if (rgb == Color.GREEN) {
                    return this.converterService.hexToInt(color.substring(2, 4));
                } else if (rgb == Color.BLUE) {
                    return this.converterService.hexToInt(color.substring(4, 6));
                }
                break;
        }
        return 0;
    }

    /**
     * Returns the drawing color that is used with the given Map object.
     * The color can be defined on owner or on map level. If map 
     * level definition is missing, owner level definition is used.
     * @param map Map object
     * @return drawing color as HEX decimal number
     */
    private String getColor(Map map) {
        String color = "dd0000";
        String colorOwner = map.getOwner().getColor();
        String colorMap = map.getColor();

        if (colorMap != null && !colorMap.isEmpty()) {
            return colorMap;
        }

        if (colorOwner != null && !colorOwner.isEmpty()) {
            return colorOwner;
        }
        return color;
    }

    /**
     * Returns the opacity value that is used with the given Map object.
     * The opacity can be defined on owner or on map level. If map 
     * level definition is missing, owner level definition is used.
     * @param map Map object 
     * @return opacity value to be used with the map(0-255) 
     */
    private int getOpacity(Map map) {
        String opacity = "150";
        String opacityOwner = map.getOwner().getOpacity();
        String opacityMap = map.getOpacity();

        if (opacityOwner != null && !opacityOwner.isEmpty()) {
            opacity = opacityOwner;
        }

        if (opacityMap != null && !opacityMap.isEmpty()) {
            opacity = opacityMap;
        }
        try {
            return Integer.parseInt(opacity);
        } catch (NumberFormatException nfe) {
            return 255;
        }
    }
}
