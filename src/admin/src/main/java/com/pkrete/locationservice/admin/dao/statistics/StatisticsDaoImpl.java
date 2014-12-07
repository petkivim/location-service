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
package com.pkrete.locationservice.admin.dao.statistics;

import com.pkrete.locationservice.admin.model.statistics.SearchEventType;
import com.pkrete.locationservice.admin.dao.StatisticsDao;
import com.pkrete.locationservice.admin.model.statistics.StatisticsGroup;
import java.util.List;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

/**
 * This class implements {@link StatisticsDao StatisticsDao} interface that
 * defines data access layer for statistics.
 *
 * This class extends {@link HibernateDaoSupport HibernateDaoSupport} class that
 * is a wrapper over {@link HibernateTemplate HibernateTemplate} class.
 * HibernateTemplate is a convenience class for Hibernate based database access.
 * HibernateDaoSupport creates the HibernateTemplate and subclasses can use the
 * getHibernateTemplate() method to obtain the hibernateTemplate and then
 * perform operations on it. HibernateTemplate takes care of obtaining or
 * releasing sessions and managing exceptions.
 *
 * @author Petteri Kivimäki
 */
public class StatisticsDaoImpl extends HibernateDaoSupport implements StatisticsDao {

    /**
     * Fetches the number of searches and unique IPs grouped by date.
     *
     * @param owner the owner of the object
     * @param group how the results are grouped: by day, by month or by year
     * @return number of searches and unique IPs grouped by date
     */
    @Override
    public List<Object[]> getStatistics(String owner, StatisticsGroup group) {
        String groupHQL = this.groupToHQL(group);
        List<Object[]> list = (List<Object[]>) getHibernateTemplate().findByNamedParam("select count(*), "
                + "count(distinct ipAddress), " + groupHQL + " "
                + "from SearchEvent "
                + "where owner like :owner "
                + "group by col_2_0_", "owner", owner);
        return list;
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
        String groupHQL = this.groupToHQL(group);
        List<Object[]> list = (List<Object[]>) getHibernateTemplate().findByNamedParam("select count(*), "
                + "count(distinct ipAddress), " + groupHQL + " "
                + "from SearchEvent "
                + "where owner like :owner "
                + "and eventType = :type "
                + "group by col_2_0_", new String[]{"owner", "type"}, new Object[]{owner, type});
        return list;
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
        String date = parseDateLimit(from, to);
        String groupHQL = this.groupToHQL(group);
        List<Object[]> list = (List<Object[]>) getHibernateTemplate().findByNamedParam("select count(*), "
                + "count(distinct ipAddress), " + groupHQL + " "
                + "from SearchEvent "
                + "where owner like :owner "
                + date + " "
                + "group by col_2_0_", "owner", owner);
        return list;
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
        String date = parseDateLimit(from, to);
        String groupHQL = this.groupToHQL(group);
        List<Object[]> list = (List<Object[]>) getHibernateTemplate().findByNamedParam("select count(*), "
                + "count(distinct ipAddress), " + groupHQL + " "
                + "from SearchEvent "
                + "where owner like :owner "
                + "and eventType = :type "
                + date + " "
                + "group by col_2_0_", new String[]{"owner", "type"}, new Object[]{owner, type});
        return list;
    }

    private String parseDateLimit(String from, String to) {
        String date = "and date between '" + from + "' and '" + to + "'";
        if (to == null) {
            date = "and date >= '" + from + "'";
        } else if (from == null) {
            date = "and date <= '" + to + "'";
        }
        return date;
    }

    private String groupToHQL(StatisticsGroup group) {
        String day = "DAY(date)||'.'||MONTH(date)||'.'||YEAR(date)";
        if (group == null) {
            return day;
        }
        if (group == StatisticsGroup.DAY) {
            return day;
        } else if (group == StatisticsGroup.MONTH) {
            return "MONTH(date)||'/'||YEAR(date)";
        } else if (group == StatisticsGroup.YEAR) {
            return "YEAR(date)";
        }
        return day;
    }
}
