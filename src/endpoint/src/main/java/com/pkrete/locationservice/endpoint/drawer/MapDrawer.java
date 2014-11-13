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
package com.pkrete.locationservice.endpoint.drawer;

import com.pkrete.locationservice.endpoint.model.location.Area;
import com.pkrete.locationservice.endpoint.model.location.Location;
import com.pkrete.locationservice.endpoint.converter.ConverterService;
import com.pkrete.locationservice.endpoint.util.Settings;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements {@link Drawer Drawer} interface and implements all the
 * methods defined in it. This class can be used for drawing a position of a
 * Location object on a map.
 *
 * @author Petteri Kivimäki
 */
public class MapDrawer implements Drawer {

    private static final Logger logger = LoggerFactory.getLogger(MapDrawer.class.getName());
    private ConverterService converterService;

    /**
     * Changes the value of converterService instance variable
     *
     * @param converterService new value to be set
     */
    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    /**
     * Creates a BufferedImage that presents the location of the given Location
     * object on a map.
     *
     * @param location Location object
     * @param lang language of the UI
     * @return BufferedImage that presents the location of the given Location
     * object on a map
     */
    @Override
    public BufferedImage draw(Location location, String lang) {
        // If location is null, there's nothing to do
        if (location == null) {
            logger.warn("\"location\" can not be null.");
            return null;
        }

        // If map is null, there's nothing to do
        if (location.getMap() == null) {
            logger.warn("The given Location doesn't have a map.");
            return null;
        }

        // If map is a Google Map, there's nothing to do
        if (location.getMap().isGoogleMap()) {
            logger.warn("The given Location has a Google Map.");
            return null;
        }

        // Create an image
        BufferedImage bufferedImage = null;

        try {
            if (!location.getMap().getIsExternal()) {
                // Build string that contains the absolute path of the map
                StringBuilder mapsPath = new StringBuilder();
                mapsPath.append(Settings.getInstance().getMapsPath(location.getOwner().getCode()));
                mapsPath.append(lang).append("/").append(location.getMap().getPath());
                // Create a new file
                File file = new File(mapsPath.toString());
                if (file.exists()) {
                    bufferedImage = ImageIO.read(file);
                } else {
                    logger.error("The image file doesn't exist! Path: \"{}\"", file.getAbsolutePath());
                }
            } else {
                // The image is external and accessed via its URL
                bufferedImage = ImageIO.read(new URL(location.getMap().getPath()));
            }

            if (bufferedImage != null) {
                // Get drawing color as a HEX string
                String colorStr = getColor(location);
                // Get opacity
                int opacity = getOpacity(location);
                // Parse red value from the HEX string
                int intValue1 = parseColor(colorStr, Color.RED);
                // Parse green value from the HEX string
                int intValue2 = parseColor(colorStr, Color.GREEN);
                // Parse blue value from the HEX string
                int intValue3 = parseColor(colorStr, Color.BLUE);
                // Create new color object
                Color color = new Color(intValue1, intValue2, intValue3, opacity);

                //Draw an oval
                Graphics g = bufferedImage.getGraphics();
                Graphics2D g2d;
                g2d = (Graphics2D) g.create();
                g2d.setColor(color);

                for (Area area : location.getAreas()) {
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
            logger.error(e.getMessage());
        }
        return bufferedImage;
    }

    /**
     * Parses red, green or blue component value from the given HEX color string
     * according to the rgb parameter value.
     *
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
     * Returns the drawing color that is used with the given Location object.
     * The color can be defined on owner or on map level. If map level
     * definition is missing, owner level definition is used.
     *
     * @param location Location object
     * @return drawing color as HEX decimal number
     */
    private String getColor(Location location) {
        String color = "dd0000";
        String colorStr = location.getOwner().getColor();
        String colorMap = location.getMap().getColor();

        if (colorMap != null) {
            if (!colorMap.isEmpty()) {
                return colorMap;
            }
        }

        if (colorStr != null) {
            if (!colorStr.isEmpty()) {
                return colorStr;
            }
        }
        return color;
    }

    /**
     * Returns the opacity value that is used with the given Location object.
     * The opacity can be defined on owner or on map level. If map level
     * definition is missing, owner level definition is used.
     *
     * @param location Location object
     * @return opacity value to be used with the location (0-255)
     */
    private int getOpacity(Location location) {
        String opacity = "150";
        String opacityLoc = location.getOwner().getOpacity();
        String opacityMap = location.getMap().getOpacity();

        if (opacityLoc != null) {
            if (!opacityLoc.isEmpty()) {
                opacity = opacityLoc;
            }
        }

        if (opacityMap != null) {
            if (!opacityMap.isEmpty()) {
                opacity = opacityMap;
            }
        }
        try {
            return Integer.parseInt(opacity);
        } catch (NumberFormatException nfe) {
            return 255;
        }
    }
}
