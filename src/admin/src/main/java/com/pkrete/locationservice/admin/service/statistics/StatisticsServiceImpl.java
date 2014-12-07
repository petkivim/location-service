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
package com.pkrete.locationservice.admin.service.statistics;

import com.pkrete.locationservice.admin.comparator.StatisticComparator;
import com.pkrete.locationservice.admin.dao.StatisticsDao;
import com.pkrete.locationservice.admin.model.statistics.SearchEventType;
import com.pkrete.locationservice.admin.model.statistics.StatisticsGroup;
import com.pkrete.locationservice.admin.model.statistics.StatisticsSearchResult;
import com.pkrete.locationservice.admin.service.StatisticsService;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Petteri Kivimäki
 */
public class StatisticsServiceImpl implements StatisticsService {

    private StatisticsDao dao;

    /**
     * Sets the data access object.
     *
     * @param dao new value
     */
    public void setDao(StatisticsDao dao) {
        this.dao = dao;
    }

    /**
     * Fetches the number of searches and unique IPs grouped by date.
     *
     * @param owner the owner of the object
     * @param group how the results are grouped: by day, by month or by year
     * @return number of searches and unique IPs grouped by date
     */
    @Override
    public List<Object[]> getStatistics(String owner, StatisticsGroup group) {
        return dao.getStatistics(owner, group);
    }

    /**
     * Fetches the number of searches and unique IPs grouped by date.
     *
     * @param owner the owner of the object
     * @param group how the results are grouped: by day, by month or by year
     * @param type type of the search events that are included: location handler
     * or exporter
     * @return number of searches and unique IPs grouped by date
     */
    @Override
    public List<Object[]> getStatistics(String owner, StatisticsGroup group, SearchEventType type) {
        return dao.getStatistics(owner, group, type);
    }

    /**
     * Fetches the number of searches and unique IPs grouped by date.
     *
     * @param owner the owner of the object
     * @param group how the results are grouped: by day, by month or by year
     * @param from begin date of the period
     * @param to end date of the period
     * @return number of searches and unique IPs grouped by date
     */
    @Override
    public List<Object[]> getStatistics(String owner, StatisticsGroup group, String from, String to) {
        return dao.getStatistics(owner, group, from, to);
    }

    /**
     * Fetches the number of searches and unique IPs grouped by date.
     *
     * @param owner the owner of the object
     * @param group how the results are grouped: by day, by month or by year
     * @param type type of the search events that are included: location handler
     * or exporter
     * @param from begin date of the period
     * @param to end date of the period
     * @return number of searches and unique IPs grouped by date
     */
    @Override
    public List<Object[]> getStatistics(String owner, StatisticsGroup group, SearchEventType type, String from, String to) {
        return dao.getStatistics(owner, group, type, from, to);
    }

    /**
     * Fetches the number of searches and unique IPs grouped by date.
     *
     * @param owner the organization which statistics are fetched
     * @param group how the results are grouped: by day, by month or by year
     * @param type type of the search events that are included: location handler
     * or exporter
     * @param from begin date of the period
     * @param to end date of the period
     * @param sortOrder sort order of the results (ASC / DESC)
     * @return number of searches and unique IPs grouped by date
     */
    @Override
    public StatisticsSearchResult getStatistics(String owner, StatisticsGroup group, SearchEventType type, String from, String to, String sortOrder) {
        List<Object[]> list = null;
        if (type.equals(SearchEventType.ALL)) {
            if (from == null && to == null) {
                list = dao.getStatistics(owner, group);
            } else {
                list = dao.getStatistics(owner, group, from, to);
            }
        } else {
            if (from == null && to == null) {
                list = dao.getStatistics(owner, group, type);
            } else {
                list = dao.getStatistics(owner, group, type, from, to);
            }
        }
        // Sort results by using custom comparator
        Collections.sort(list, new StatisticComparator());

        if (sortOrder != null) {
            if (sortOrder.equals("desc")) {
                Collections.reverse(list);
            }
        }
        StatisticsSearchResult results = new StatisticsSearchResult(group, type, from, to, list);

        return results;
    }
}
