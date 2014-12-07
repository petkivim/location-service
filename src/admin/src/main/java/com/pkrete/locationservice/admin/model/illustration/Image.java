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
package com.pkrete.locationservice.admin.model.illustration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pkrete.locationservice.admin.deserializers.ImageJSONDeserializer;
import org.springframework.web.multipart.MultipartFile;

/**
 * The <code>Image</code> class extends the {@link Illustration Illustration}
 * class.
 *
 * An image represents a picture that is related to a location. One image can be
 * related to one or more locations.
 *
 * @author Petteri Kivimäki
 */
@JsonDeserialize(using = ImageJSONDeserializer.class)
public class Image extends Illustration {

    /**
     * The x coordinate of the point where the image was taken.
     */
    private int x;
    /**
     * The y coordinate of the point where the image was taken.
     */
    private int y;
    /**
     * This variable is used when user uploads an image to the server. This
     * variable is not saved to the db and therefore it's not mapped.
     */
    private MultipartFile file;

    /**
     * Constructs and initializes an Image.
     */
    public Image() {
    }

    /**
     * Constructs and initializes an Image with the given path.
     *
     * @param path the path of the image
     * @param the description of the image
     */
    public Image(String path, String description) {
        super(path, description, false);
    }

    /**
     * Constructs and initializes an Image with the given values.
     *
     * @param id unique id of this image
     * @param url URL of the image
     * @param filePath path of the file that this image represents
     * @param the description of the image
     */
    public Image(int id, String url, String filePath, String description) {
        super(url, filePath, description);
        this.id = id;
    }

    /**
     * Constructs and initializes an image with the given path and isExternal
     * value.
     *
     * @param path the location of the image
     * @param the description of the image
     * @param isExternal the value that tells if the image is located on the
     * same server as the Location Service
     */
    public Image(String path, String description, boolean isExternal) {
        super(path, description, isExternal);
    }

    /**
     * Returns the x coordinate of the point where the image was taken.
     *
     * @return the x coordinate of the point where the image was taken
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the y coordinate of the point where the image was taken.
     *
     * @return the y coordinate of the point where the image was taken
     */
    public int getY() {
        return this.y;
    }

    /**
     * Changes the x coordinate of the point where the image was taken.
     *
     * @param x1 the x coordinate of the new point
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Changes the y coordinate of the point where the image was taken.
     *
     * @param y1 the y coordinate of the new point
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns the file related to this image.
     *
     * @return file related to this image
     */
    public MultipartFile getFile() {
        return file;
    }

    /**
     * Changes the file related to this image.
     *
     * @param file new file
     */
    public void setFile(MultipartFile file) {
        this.file = file;
    }

    /**
     * Returns true if the point where the image was taken is set, otherwise
     * returns false. The point is set if x and y are not zero.
     */
    public boolean hasCoordinates() {
        if (x != 0 && y != 0) {
            return true;
        }
        return false;
    }
}
