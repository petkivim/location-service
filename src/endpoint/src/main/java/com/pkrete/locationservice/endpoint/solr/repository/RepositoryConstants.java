/**
 * This file is part of Location Service :: Endpoint. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.solr.repository;

/**
 * This class defines constants related to Solr repository.
 *
 * @author Petteri Kivimäki
 */
public class RepositoryConstants {

    // Sort order
    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "desc";
    // Prefixes
    public static final String PREFIX_LOCATION = "loc-";
    public static final String PREFIX_OWNER = "own-";
    // Repository fields
    public static final String FIELD_ID = "id";
    public static final String FIELD_DOCUMENT_ID = "document_id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_LOCATION_TYPE = "location_type";
    public static final String FIELD_CALL_NUMBER = "call_number";
    public static final String FIELD_LOCATION_CODE = "location_code";
    public static final String FIELD_COLLECTION_CODE = "collection_code";
    public static final String FIELD_OWNER_ID = "owner_id";
    public static final String FIELD_OWNER_CODE = "owner_code";
    public static final String FIELD_IS_SUBSTRING = "is_substring";
    public static final String FIELD_PARENT_ID = "parent_id";
    public static final String FIELD_GRANDPARENT_ID = "grandparent_id";
    public static final String FIELD_DOCUMENT_TYPE = "document_type";
    public static final String FIELD_LOCATING_STRATEGY = "locating_strategy";
    public static final String FIELD_COLOR = "color";
    public static final String FIELD_OPACITY = "opacity";
    public static final String FIELD_EXPORTER_VISIBLE = "exporter_visible";
    public static final String FIELD_ALLOWED_IPS = "allowed_ips";
    public static final String FIELD_PREPROCESSING_REDIRECTS = "preprocessing_redirects";
    public static final String FIELD_NOT_FOUND_REDIRECTS = "not_found_redirects";
    public static final String FIELD_SHELF_NUMBER = "shelf_number";
    public static final String FIELD_FLOOR = "floor";
    public static final String FIELD_DESCRIPTIONS = "descriptions";
    public static final String FIELD_NOTES = "notes";
    public static final String FIELD_STAFF_NOTE_PRI = "staff_note_pri";
    public static final String FIELD_STAFF_NOTE_SEC = "staff_note_sec";
    public static final String FIELD_SUBJECTS = "subjects";
    public static final String FIELD_SUBJECT_IDS = "subject_ids";
}
