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
package com.pkrete.locationservice.admin.controller.rest.v1;

import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.statistics.SearchEventType;
import com.pkrete.locationservice.admin.model.statistics.StatisticsGroup;
import com.pkrete.locationservice.admin.model.statistics.StatisticsSearchResult;
import com.pkrete.locationservice.admin.service.StatisticsService;
import com.pkrete.locationservice.admin.util.StatisticsUtil;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This class provides REST API for accessing statistics.
 * 
 * INDEX    /statistics      [GET]
 * 
 * Parameters:
 * 
 * group: d = day, m = month, y =year
 * type: handler = UI, exporter = exporter interface, default is both
 * from: date "dd.MM.yyyy"
 * to: date "dd.MM.yyyy"
 * order: desc = descending, default is ascending
 * 
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsRestController {

    private final static Logger logger = Logger.getLogger(StatisticsRestController.class.getName());
    @Autowired
    @Qualifier("statisticsSearchResultMapService")
    private ObjectMapService mapConverter;
    @Autowired
    @Qualifier("statisticsService")
    private StatisticsService service;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map get(HttpServletRequest request) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Parse StatisticalGroup
        StatisticsGroup group = StatisticsUtil.parseGroup(request.getParameter("group"));
        // Parse SearchEventType
        SearchEventType type = StatisticsUtil.parseType(request.getParameter("type"));
        // Get begin date of the date range
        String from = StatisticsUtil.parseFromDate(request.getParameter("from"));
        // Get end date of the date range
        String to = StatisticsUtil.parseToDate(request.getParameter("to"));
        // Get order of the results (ASC / DESC)
        String order = request.getParameter("order");
        // Get statistics matching the given conditions
        StatisticsSearchResult results = service.getStatistics(owner.getCode(), group, type, from, to, order);
        // Return the statistics
        return this.mapConverter.convert(results);
    }
}
