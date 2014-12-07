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
package com.pkrete.locationservice.admin.model.statistics;

import java.util.List;

/**
 * This class represents statistics search results.
 *
 * @author Petteri Kivimäki
 */
public class StatisticsSearchResult {

    private StatisticsGroup group;
    private List<Object[]> data;
    private SearchEventType type;
    private String from;
    private String to;

    /**
     * Constructs and initializes a new StatisticsSearchResult object.
     */
    public StatisticsSearchResult() {

    }

    /**
     * Constructs and initializes a new StatisticsSearchResult object with the
     * given statistics group and data.
     *
     * @param group statistics group
     * @param type search event type
     * @param from begin date of the date range
     * @param to end date of the date range
     * @param data statistics data
     */
    public StatisticsSearchResult(StatisticsGroup group, SearchEventType type, String from, String to, List<Object[]> data) {
        this.group = group;
        this.type = type;
        this.from = from;
        this.to = to;
        this.data = data;

    }

    /**
     * Returns the statistics group of this object.
     *
     * @return statistics group
     */
    public StatisticsGroup getGroup() {
        return group;
    }

    /**
     * Sets the statistics group of this object.
     *
     * @param group new value
     */
    public void setGroup(StatisticsGroup group) {
        this.group = group;
    }

    /**
     * Returns the statistics data of this object.
     *
     * @return statistics data
     */
    public List<Object[]> getData() {
        return data;
    }

    /**
     * Sets the statistics data of this object.
     *
     * @param data new value
     */
    public void setData(List<Object[]> data) {
        this.data = data;
    }

    /**
     * Returns the search event type of this object.
     *
     * @return search event type
     */
    public SearchEventType getType() {
        return type;
    }

    /**
     * Sets the search event type of this object
     *
     * @param type new value
     */
    public void setType(SearchEventType type) {
        this.type = type;
    }

    /**
     * Returns the begin date of the date range.
     *
     * @return begin date of the date range
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the begin date of the date range.
     *
     * @param from new value
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Returns the end date of the date range.
     *
     * @return end date of the date range
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the end date of the date range.
     *
     * @param to new value
     */
    public void setTo(String to) {
        this.to = to;
    }
}
