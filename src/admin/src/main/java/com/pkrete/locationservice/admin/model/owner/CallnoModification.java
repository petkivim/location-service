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
package com.pkrete.locationservice.admin.model.owner;

import com.pkrete.locationservice.admin.model.location.Location;
import java.io.Serializable;

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
public abstract class CallnoModification implements Serializable, Comparable {
    /* Id of this object in the db. */

    private int id;
    /* Condition for the modification. */
    private String condition;
    /* The modification that's applied when the rule is true. */
    private String operation;
    /* Tells if this object is active. */
    private boolean isActive;
    /* Tells if the object is empty or not. */
    /* This value is not saved to db, but it's only for the UI*/
    private boolean emtpy;
    /* Owner of the object */
    private Owner owner;

    /**
     * Initializes a CallnoModification object.
     */
    public CallnoModification() {
        this.condition = "";
        this.operation = "";
        this.isActive = true;
    }

    /**
     * Initializes a CallnoModification object with the given conditino and
     * operation.
     * @param condition condition for the operation
     * @param operation operation when the condition is true
     */
    public CallnoModification(String condition, String operation) {
        this.isActive = true;
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
     * Checks if the object is empty or not. Object is empty if both codition
     * and operation are empty. Otherwise object is not empty.
     * @return true if operation is empty, otherwise false
     */
    public boolean isEmpty() {
        if (condition.isEmpty() && operation.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the object is empty or not. Object is empty if both codition
     * and operation are empty. Otherwise object is not empty.
     * @return true if operation is empty, otherwise false
     */
    public boolean getEmpty() {
        return isEmpty();
    }

    /**
     * Returns the owner of this object
     * @return owner of this object
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this object.
     * @param owner owner of this object
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    /**
     * Compares this object with the specified object for order.
     * @param o the object to be compared
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    public int compareTo(Object o) {
        return compareTo((Location) o);
    }

    /**
     * Compares this object with the specified object for order.
     * @param mod the object to be compared
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    public int compareTo(CallnoModification mod) {
        return Integer.toString(this.id).compareTo(Integer.toString(mod.id));
    }

    @Override
    /**
     * Indicates whether some other object is "equal to" this one.
     * @param o the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    public boolean equals(Object o) {
        if (o instanceof CallnoModification) {
            if (id != 0 && id == ((CallnoModification) o).id) {
                return true;
            } else if (id == 0) {
                if (condition.equals(((CallnoModification) o).condition)
                        && operation.equals(((CallnoModification) o).operation)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    /**
     * Returns a hash code value for the object.
     * @return a hash code value for this object
     */
    public int hashCode() {
        return this.id;
    }
}
