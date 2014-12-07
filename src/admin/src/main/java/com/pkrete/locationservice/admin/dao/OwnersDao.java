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
package com.pkrete.locationservice.admin.dao;

import com.pkrete.locationservice.admin.model.owner.CallnoModification;
import com.pkrete.locationservice.admin.model.owner.Owner;
import java.util.List;

/**
 * This interface defines data access layer for Owner objects. All the classes
 * implementing this interface must implement all the methods defined here.
 *
 * @author Petteri Kivimäki
 */
public interface OwnersDao {

    List<Owner> getOwners();

    Owner getOwner(int id);

    Owner getFullOwner(int id);

    Owner getOwnerByCode(String code);

    boolean canBeDeleted(Owner owner);

    boolean create(Owner owner);

    boolean update(Owner owner);

    boolean delete(Owner owner);

    boolean delete(CallnoModification mod);

    boolean deleteOrphanRedirects();

    List<Integer> getPreprocessingRedirectIds(int id);

    List<Integer> getNotFoundRedirectIds(int id);
}
