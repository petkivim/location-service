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
package com.pkrete.locationservice.endpoint.modifier;

/**
 * This class represents a redirect that takes before the given call number is
 * searched from the database. In this way it's possible to modify call numbers
 * matching certain conditions before any other operations take place. The
 * redirect is used only if the given call number matches the condition related
 * to the redirect object, in that case the modification operation defined in
 * the redirect object is applied to the call number.
 *
 * @author Petteri Kivimäki
 */
public class PreprocessingRedirect extends CallnoModification {

}
