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
package com.pkrete.locationservice.admin.converter.json;

import com.pkrete.locationservice.admin.model.owner.NotFoundRedirect;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.owner.PreprocessingRedirect;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import com.pkrete.locationservice.admin.util.DateTimeUtil;
import java.util.List;

/**
 * This class converts Owner objects to JSON.
 *
 * @author Petteri Kivimäki
 */
public class OwnerJSONizer implements JSONizerService<Owner> {

    /**
     * Converts a single owner object to JSON string. All the variables of the
     * Owner object are included.
     *
     * @param source Owner object to be converted
     * @return JSON string
     */
    @Override
    public String jsonize(Owner source) {
        return this.jsonize(source, false);
    }

    /**
     * Converts a single owner object to JSON string. All the variables of the
     * Owner object are included. If logEntry is true, also owner_id is
     * included.
     *
     * @param source Owner object to be converted
     * @param logEntry is this JSON for a log entry
     * @return JSON string
     */
    @Override
    public String jsonize(Owner source, boolean logEntry) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":").append(source.getId());
        builder.append(",\"code\":\"").append(source.getCode()).append("\"");
        builder.append(",\"name\":\"").append(source.getName()).append("\"");
        builder.append(",\"color\":\"").append(source.getColor()).append("\"");
        builder.append(",\"opacity\":\"").append(source.getOpacity()).append("\"");
        builder.append(",\"locating_strategy\":\"").append(source.getLocatingStrategy()).append("\"");
        builder.append(",\"exporter_visible\":").append(source.getExporterVisible());
        builder.append(",").append(this.getIpArray(source));
        builder.append(",\"redirects\":[");
        for (int i = 0; i < source.getPreprocessingRedirects().size(); i++) {
            PreprocessingRedirect temp = source.getPreprocessingRedirects().get(i);
            builder.append("{\"id\":").append(temp.getId());
            builder.append(",\"type\":\"PREPROCESS\"");
            builder.append(",\"condition\":\"").append(temp.getCondition()).append("\"");
            builder.append(",\"operation\":\"").append(temp.getOperation()).append("\"");
            builder.append(",\"is_active\":").append(temp.getIsActive()).append("}");
            if (i < source.getPreprocessingRedirects().size() - 1 || !source.getNotFoundRedirects().isEmpty()) {
                builder.append(",");
            }
        }
        for (int i = 0; i < source.getNotFoundRedirects().size(); i++) {
            NotFoundRedirect temp = source.getNotFoundRedirects().get(i);
            builder.append("{\"id\":").append(temp.getId());
            builder.append(",\"type\":\"NOTFOUND\"");
            builder.append(",\"condition\":\"").append(temp.getCondition()).append("\"");
            builder.append(",\"operation\":\"").append(temp.getOperation()).append("\"");
            builder.append(",\"is_active\":").append(temp.getIsActive()).append("}");
            if (i < source.getNotFoundRedirects().size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        builder.append(",\"created_at\":\"");
        builder.append(DateTimeUtil.dateToString(source.getCreated())).append("\"");
        builder.append(",\"create_operator\":\"").append(source.getCreator()).append("\"");
        builder.append(",\"updated_at\":\"");
        builder.append((source.getUpdated() == null ? "" : DateTimeUtil.dateToString(source.getUpdated()))).append("\"");
        builder.append(",\"update_operator\":\"").append((source.getUpdater() == null ? "" : source.getUpdater())).append("\"");
        builder.append("}");
        return builder.toString();
    }

    /**
     * Converts a list of Owner objects to JSON string. Only selected variables
     * are included.
     *
     * @param sources Owner objects to be converted
     * @return JSON string
     */
    @Override
    public String jsonize(List<Owner> sources) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < sources.size(); i++) {
            Owner temp = sources.get(i);
            builder.append("{\"id\":").append(temp.getId());
            builder.append(",\"code\":\"").append(temp.getCode()).append("\"");
            builder.append(",\"name\":\"").append(temp.getName()).append("\"");
            builder.append(",\"color\":\"").append(temp.getColor()).append("\"");
            builder.append(",\"opacity\":\"").append(temp.getOpacity()).append("\"");
            builder.append(",\"locating_strategy\":\"").append(temp.getLocatingStrategy()).append("\"");
            builder.append(",\"exporter_visible\":").append(temp.getExporterVisible());
            builder.append(",").append(this.getIpArray(temp));
            builder.append("}");
            if (i < sources.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private StringBuilder getIpArray(Owner source) {
        StringBuilder builder = new StringBuilder("\"allowed_ips\":[");
        String[] ipsArr = source.getAllowedIPs().split("\n");
        for (int i = 0; i < ipsArr.length; i++) {
            if (!ipsArr[i].trim().isEmpty()) {
                builder.append("\"").append(ipsArr[i].trim()).append("\"");
            }
            if (i < ipsArr.length - 1 && !ipsArr[i].trim().isEmpty() && !ipsArr[i + 1].trim().isEmpty()) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder;
    }
}
