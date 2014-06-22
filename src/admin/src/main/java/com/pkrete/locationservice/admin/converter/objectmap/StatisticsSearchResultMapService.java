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
package com.pkrete.locationservice.admin.converter.objectmap;

import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.model.statistics.StatisticsSearchResult;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class converts StatisticsSearchResult objects to Map.
 * 
 * @author Petteri Kivimäki
 */
public class StatisticsSearchResultMapService implements ObjectMapService<StatisticsSearchResult> {

    /**
     * Converts a single StatisticsSearchResult object to Map object. All the 
     * variables of the StatisticsSearchResult object are included.
     * @param source StatisticsSearchResult object to be converted
     * @return Map object
     */
    public Map convert(StatisticsSearchResult source) {
        return this.convert(source, false);
    }

    /**
     * Converts a single StatisticsSearchResult object to Map object. All the 
     * variables of the StatisticsSearchResult object are included. LogEntry 
     * is ignored.
     * @param source StatisticsSearchResult object to be converted
     * @param logEntry ignored
     * @return Map object
     */
    public Map convert(StatisticsSearchResult source, boolean logEntry) {
        Map statistics = new LinkedHashMap();
        statistics.put("group_by", source.getGroup().toString());
        statistics.put("search_event_type", source.getType().toString());
        statistics.put("date_range_from", source.getFrom() == null ? "" : source.getFrom());
        statistics.put("date_range_to", source.getTo() == null ? "" : source.getTo());
        List data = new ArrayList();
        for (Object[] item : source.getData()) {
            Map entry = new LinkedHashMap();
            entry.put(source.getGroup().toString().toLowerCase(), item[2]);
            entry.put("searches", item[0]);
            entry.put("users", item[1]);
            data.add(entry);
        }
        statistics.put("statistics_data", data);
        return statistics;
    }

    /**
     * Converts a list of StatisticsSearchResult objects to a list of Map 
     * objects. Only selected variables are included.
     * @param sources StatisticsSearchResult objects to be converted
     * @return list of Map objects
     */
    public List convert(List<StatisticsSearchResult> sources) {
        List results = new ArrayList();
        for (StatisticsSearchResult source : sources) {
            Map statistics = new LinkedHashMap();
            statistics.put("statistics_group", source.getGroup().toString());
            statistics.put("search_event_type", source.getType().toString());
            statistics.put("date_range_from", source.getFrom());
            statistics.put("date_range_to", source.getTo());
            List data = new ArrayList();
            for (Object[] item : source.getData()) {
                Map entry = new LinkedHashMap();
                entry.put(source.getGroup().toString().toLowerCase(), item[2]);
                entry.put("searches", item[0]);
                entry.put("users", item[1]);
                data.add(entry);
            }
            statistics.put("statistics_data", data);
            results.add(statistics);
        }
        return results;
    }
}
