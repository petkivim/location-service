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
package com.pkrete.locationservice.admin.controller.mvc;

import com.pkrete.locationservice.admin.model.statistics.SearchEventType;
import com.pkrete.locationservice.admin.model.statistics.StatisticsGroup;
import com.pkrete.locationservice.admin.model.statistics.StatisticsSearchResult;
import com.pkrete.locationservice.admin.service.StatisticsService;
import com.pkrete.locationservice.admin.service.UsersService;
import com.pkrete.locationservice.admin.util.StatisticsUtil;
import com.pkrete.locationservice.admin.util.UsersUtil;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/statistics.htm")
public class StatisticsController extends BaseController {

    @Autowired
    @Qualifier("statisticsService")
    private StatisticsService statisticsService;
    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) throws Exception {
        return "statistics";
    }

    @RequestMapping(method = {RequestMethod.POST})
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        StatisticsGroup group = StatisticsUtil.parseGroup(request.getParameter("group"));
        SearchEventType type = StatisticsUtil.parseType(request.getParameter("type"));
        String from = StatisticsUtil.parseFromDate(request.getParameter("from"));
        String to = StatisticsUtil.parseToDate(request.getParameter("to"));
        String order = request.getParameter("order");
        String output = request.getParameter("output");
        String ownerCode = getOwner(request).getCode();

        StatisticsSearchResult results = statisticsService.getStatistics(ownerCode, group, type, from, to, order);

        if (output != null) {
            if (output.equals("file")) {
                response.setContentType("application/txt");
                response.addHeader("content-disposition", "attachment;filename=statistics.txt");
                return new ModelAndView("statistics_file", "stats", resultsToString(results));
            }
        }
        model.put("stats", results.getData());
        return new ModelAndView("statistics", "model", model);
    }

    private String resultsToString(StatisticsSearchResult results) {
        StringBuilder builder = new StringBuilder();

        if (results.getGroup().equals(StatisticsGroup.MONTH)) {
            builder.append(this.messageSource.getMessage("label.statistics.month", null, null));
        } else if (results.getGroup().equals(StatisticsGroup.YEAR)) {
            builder.append(this.messageSource.getMessage("label.statistics.year", null, null));
        } else {
            builder.append(this.messageSource.getMessage("label.statistics.day", null, null));
        }

        builder.append("\t");
        builder.append(this.messageSource.getMessage("label.statistics.results.column1", null, null));
        builder.append("\t");
        builder.append(this.messageSource.getMessage("label.statistics.results.column2", null, null));
        builder.append("\n");

        for (Object[] item : results.getData()) {
            builder.append(item[2]).append("\t");
            builder.append(item[0]).append("\t");
            builder.append(item[1]).append("\n");
        }
        return builder.toString();
    }
}
