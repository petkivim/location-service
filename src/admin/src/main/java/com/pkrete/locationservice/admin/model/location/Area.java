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
package com.pkrete.locationservice.admin.model.location;

import java.io.Serializable;

/**
 * An area specifies a rectangle in a coordinate space.
 *
 * @author Petteri Kivimäki
 */
public class Area implements Serializable {

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
     * The location that owns the area
     */
    private Location location;

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
     *
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
        this.angle = 0;
    }

    /**
     * Constructs and initializes an area thats top-left point is defined by
     * (x1, y1) and low-right point by (x2, y2).
     *
     * @param x1 the x coordinate of the upper left corner.
     * @param y1 the y coordinate of the upper left corner.
     * @param x2 the x coordinate of the lower right corner.
     * @param y2 the y coordinate of the lower right corner.
     * @param angle the angle of the are in degrees
     */
    public Area(int x1, int y1, int x2, int y2, int angle) {
        this(x1, y1, x2, y2);
        this.angle = angle;
    }

    /**
     * Constructs and initializes an area thats top-left point is defined by
     * (x1, y1) and low-right point by (x2, y2).
     *
     * @param x1 the x coordinate of the upper left corner.
     * @param y1 the y coordinate of the upper left corner.
     * @param x2 the x coordinate of the lower right corner.
     * @param y2 the y coordinate of the lower right corner.
     * @param location the location that owns the area
     */
    public Area(int x1, int y1, int x2, int y2, Location location) {
        this(x1, y1, x2, y2);
        this.location = location;
    }

    /**
     * Constructs and initializes an area thats top-left point is defined by
     * (x1, y1) and low-right point by (x2, y2).
     *
     * @param id of this object
     * @param x1 the x coordinate of the upper left corner
     * @param y1 the y coordinate of the upper left corner
     * @param x2 the x coordinate of the lower right corner
     * @param y2 the y coordinate of the lower right corner
     * @param angle the angle of the are in degrees
     */
    public Area(int id, int x1, int y1, int x2, int y2, int angle) {
        this(x1, y1, x2, y2);
        this.areaId = id;
        this.angle = angle;
    }

    /**
     * Constructs and initializes an area thats top-left point is defined by
     * (x1, y1) and low-right point by (x2, y2).
     *
     * @param id of this object
     * @param x1 the x coordinate of the upper left corner
     * @param y1 the y coordinate of the upper left corner
     * @param x2 the x coordinate of the lower right corner
     * @param y2 the y coordinate of the lower right corner
     * @param angle the angle of the are in degrees
     * @param location the location that owns the area
     */
    public Area(int id, int x1, int y1, int x2, int y2, int angle, Location location) {
        this(x1, y1, x2, y2, location);
        this.areaId = id;
        this.angle = angle;
    }

    /**
     * Returns the areaId of the area in the database.
     *
     * @return the id number of the area in the database
     */
    public int getAreaId() {
        return this.areaId;
    }

    /**
     * Returns the x coordinate of the upper left corner of the area.
     *
     * @return the x coordinate of the upper left corner of the area
     */
    public int getX1() {
        return this.x1;
    }

    /**
     * Returns the y coordinate of the upper left corner of the area.
     *
     * @return the y coordinate of the upper left corner of the area
     */
    public int getY1() {
        return this.y1;
    }

    /**
     * Returns the x coordinate of the lower right corner of the area.
     *
     * @return the x coordinate of the lower right corner of the area
     */
    public int getX2() {
        return this.x2;
    }

    /**
     * Returns the y coordinate of the lower right corner of the area.
     *
     * @return the y coordinate of the lower right corner of the area
     */
    public int getY2() {
        return this.y2;
    }

    /**
     * Returns the owner of the area.
     *
     * @return the location that owns the area
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Changes the areaId of the area.
     *
     * @param areaId the new areaId of the area
     */
    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    /**
     * Changes the x coordinate of the upper left corner to the specified
     * location.
     *
     * @param x1 the x coordinate of the new location
     */
    public void setX1(int x1) {
        this.x1 = x1;
    }

    /**
     * Changes the y coordinate of the upper left corner to the specified
     * location.
     *
     * @param y1 the y coordinate of the new location
     */
    public void setY1(int y1) {
        this.y1 = y1;
    }

    /**
     * Changes the x coordinate of the lower right corner to the specified
     * location.
     *
     * @param x2 the x coordinate of the new location
     */
    public void setX2(int x2) {
        this.x2 = x2;
    }

    /**
     * Changes the y coordinate of the lower right corner to the specified
     * location.
     *
     * @param y2 the y coordinate of the new location
     */
    public void setY2(int y2) {
        this.y2 = y2;
    }

    /**
     * Returns the angle of the area in degrees. If the area is rotated the
     * angle is greater than zero.
     *
     * @return angle of the area
     */
    public int getAngle() {
        return angle;
    }

    /**
     * Changes the angle of the area, that must be given in degrees. If the area
     * is rotated the value is greater than zero.
     *
     * @param angle new value in degrees
     */
    public void setAngle(int angle) {
        this.angle = angle;
    }

    /**
     * Changes the owner of the area.
     *
     * @param location new location that owns the area
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare
     * @return true if this object is the same as the o argument; false
     * otherwise
     */
    public boolean equals(Object o) {
        if (o instanceof Area) {
            if (areaId != 0 && areaId == ((Area) o).areaId) {
                return true;
            } else if (x1 == ((Area) o).x1
                    && y1 == ((Area) o).y1
                    && x2 == ((Area) o).x2
                    && y2 == ((Area) o).y2
                    && angle == ((Area) o).angle) {
                return true;
            }
        }
        return false;
    }

    @Override
    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    public int hashCode() {
        return this.areaId;
    }

    /**
     * Returns true if and only this Area is empty. Area is considered empty
     * when all the coordinates (x1, y1, x2, y2) are set to 0.
     *
     * @return true if and only if this Area is empty; otherwise false
     */
    public boolean isEmpty() {
        if (this.x1 == 0 && this.y1 == 0 && this.x2 == 0 && this.y2 == 0) {
            return true;
        }
        return false;
    }
}
