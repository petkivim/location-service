/**
 * This file is part of Location Service :: Endpoint.
 * Copyright (C) 2014 Petteri Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.model.location;

/**
 * An area specifies a rectangle in a coordinate space.
 * @author Petteri Kivimäki
 */
public class Area {

    /**
     * An id number that identifies the area in the database.
     */
    private int areaId;
    /**
     * The x coordinate of the upper left corner.
     */
    private int x1;
    /**
     * The y coordinate of the upper left corner.
     */
    private int y1;
    /**
     * The x coordinate of the lower right corner.
     */
    private int x2;
    /**
     * The y coordinate of the lower right corner.
     */
    private int y2;
    /**
     * The angle of this area in degrees, if the are is rotated.
     */
    private int angle;

    /**
     * Constructs and initializes an area of size 0.
     */
    public Area() {
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;
        this.angle = 0;
    }

    /**
     * Constructs and initializes an area thats top-left point is defined by 
     * (x1, y1) and low-right point by (x2, y2).
     * @param x1 the x coordinate of the upper left corner.
     * @param y1 the y coordinate of the upper left corner.
     * @param x2 the x coordinate of the lower right corner.
     * @param y2 the y coordinate of the lower right corner.
     */
    public Area(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Constructs and initializes an area thats top-left point is defined by 
     * (x1, y1), low-right point by (x2, y2) and the angle.
     * @param x1 the x coordinate of the upper left corner.
     * @param y1 the y coordinate of the upper left corner.
     * @param x2 the x coordinate of the lower right corner.
     * @param y2 the y coordinate of the lower right corner.
     * @param angle the angle of the area in degrees.
     */
    public Area(int x1, int y1, int x2, int y2, int angle) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.angle = angle;
    }

    /**
     * Returns the areaId of the area in the database.
     * @return the id number of the area in the database
     */
    public int getAreaId() {
        return this.areaId;
    }

    /**
     * Returns the x coordinate of the upper left corner of the area.
     * @return the x coordinate of the upper left corner of the area
     */
    public int getX1() {
        return this.x1;
    }

    /**
     * Returns the y coordinate of the upper left corner of the area.
     * @return the y coordinate of the upper left corner of the area
     */
    public int getY1() {
        return this.y1;
    }

    /**
     * Returns the x coordinate of the lower right corner of the area.
     * @return the x coordinate of the lower right corner of the area
     */
    public int getX2() {
        return this.x2;
    }

    /**
     * Returns the y coordinate of the lower right corner of the area.
     * @return the y coordinate of the lower right corner of the area
     */
    public int getY2() {
        return this.y2;
    }

    /**
     * Changes the areaId of the area.
     * @param areaId the new areaId of the area
     */
    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    /**
     * Changes the x coordinate of the upper left corner to the specified location.
     * @param x1 the x coordinate of the new location
     */
    public void setX1(int x1) {
        this.x1 = x1;
    }

    /**
     * Changes the y coordinate of the upper left corner to the specified location.
     * @param y1 the y coordinate of the new location
     */
    public void setY1(int y1) {
        this.y1 = y1;
    }

    /**
     * Changes the x coordinate of the lower right corner to the specified location.
     * @param x2 the x coordinate of the new location
     */
    public void setX2(int x2) {
        this.x2 = x2;
    }

    /**
     * Changes the y coordinate of the lower right corner to the specified location.
     * @param y2 the y coordinate of the new location
     */
    public void setY2(int y2) {
        this.y2 = y2;
    }

    /**
     * Returns the angle of the area in degrees. If the area is rotated the 
     * angle is greater than zero. 
     * @return angle of the area
     */
    public int getAngle() {
        return angle;
    }

    /**
     * Changes the angle of the area, that must be given in degrees. If the 
     * area is rotated the value is greater than zero.
     * @param angle new value in degrees
     */
    public void setAngle(int angle) {
        this.angle = angle;
    }
}
