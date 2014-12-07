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
package com.pkrete.locationservice.admin.service.subjectmatters;

import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.solr.service.LocationIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class extends
 * {@link SubjectMattersServiceImpl SubjectMattersServiceImpl} class which
 * implements {@link SubjectMattersService SubjectMattersService} interface,
 * that defines service layer for SubjectMatters objects.
 *
 * This class overrides methods that define operations with an external index
 * software and external index software is supported.
 *
 * @author Petteri Kivimäki
 */
public class SubjectMattersServiceWithExtIndexSupportImpl extends SubjectMattersServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(SubjectMattersServiceWithExtIndexSupportImpl.class.getName());
    private LocationIndexService locationIndexService;

    /**
     * Sets the location index service object.
     *
     * @param locationIndexService new value
     */
    public void setLocationIndexService(LocationIndexService locationIndexService) {
        this.locationIndexService = locationIndexService;
    }

    /**
     * Can be used for updating SubjectMatters to external index. SubjectMatters
     * are not indexed, but they're stored in the index with locations. When
     * subject matter changes, all the locations need to be updated.
     *
     * @param subject SubjectMatter to be updated
     * @return true if and only if all the locations related to the given
     * subject were successfully updated; otherwise false
     */
    @Override
    protected boolean updateToIndex(SubjectMatter subject) {
        logger.info("Update locations related to this subject matter. Id : {}", subject.getId());
        if (this.locationIndexService.udpate(subject.getOwner().getId(), subject.getId(), subject.getIndexTerm())) {
            logger.info("Updating locations related to the subject matter succesfully completed.");
            return true;
        } else {
            logger.warn("Failed to update all the location related to the subject matter.");
            return false;
        }
    }
}
