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
package com.pkrete.locationservice.endpoint.modifier;

import com.pkrete.locationservice.endpoint.model.owner.Owner;

/**
 * This abstract class represents the rules of a modification operation that can
 * be applied to a call number. The rules consist of a condition that
 * defines the conditions when modification is applied, and the modification
 * that's applied to the call number when the condition is true.
 *
 * The actual modification operation is done by the
 * {@link CallnoModifier CallnoModifier} class.
 *
 * @author Petteri Kivimäki
 */
public abstract class CallnoModification {
    /* Id of this object in the db. */
    private int id;
    /* Condition for the modification. */
    private String condition;
    /* The modification that's applied when the rule is true. */
    private String operation;
    /* Tells if this object is active. */
    private boolean isActive;
    /* The owner of this  object. */
    private Owner owner;

    /**
     * Initializes a CallnoModification object.
     */
    public CallnoModification() {
    }

    /**
     * Initializes a CallnoModification object with the given conditino and
     * operation.
     * @param condition condition for the operation
     * @param operation operation when the condition is true
     */
    public CallnoModification(String condition, String operation) {
        this.condition = condition;
        this.operation = operation;
    }

    /**
     * Returns the database id of this object.
     * @return id number of this object in the db
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the condition that defines the condition for the modification.
     * @return condition for the modification
     */
    public String getCondition() {
        return this.condition;
    }

    /**
     * Returns the modification that's applied when the rule is true.
     * @return modification that's applied when the rule is true
     */
    public String getOperation() {
        return this.operation;
    }

    /**
     * Returns a boolean value that indicates if this object is acitve.
     * @return true if active, otherwise false
     */
    public boolean getIsActive() {
        return this.isActive;
    }

    /**
     * Returns the owner of this object.
     * @return owner of this object
     */
    public Owner getOwner() {
        return this.owner;
    }

    /**
     * Changes the database id of this object.
     * @param id new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Changes the condition that defines the condition for the modification.
     * @param condition new condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Changes the modification that's applied when the rule is true.
     * @param opeartion new operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    /**
     * Changes the boolean value that indicates if this object is acitve.
     * @param active new value
     */
    public void setIsActive(boolean active) {
        this.isActive = active;
    }

    /**
     * Changes the owner of this object
     * @param owner new owner
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
