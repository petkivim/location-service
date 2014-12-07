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
package com.pkrete.locationservice.admin.drawer;

import com.pkrete.locationservice.admin.model.location.Area;
import com.pkrete.locationservice.admin.model.illustration.Illustration;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * This interface defines methods that are used for drawing a position of a
 * Location object over an image.
 *
 * @author Petteri Kivimäki
 */
public interface Drawer {

    BufferedImage draw(Illustration illustration, List<Area> areas, String lang);
}
