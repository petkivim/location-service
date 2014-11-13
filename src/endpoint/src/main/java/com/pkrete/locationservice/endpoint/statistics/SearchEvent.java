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
package com.pkrete.locationservice.endpoint.statistics;

import java.io.Serializable;
import java.util.Date;

/**
 * The <code>SearchEvent</code> class represents a single search event that can
 * be received through LocationHandler or Exporter servlets. This class holds
 * the information of all the parameters of a single request plus client's IP
 * address. The purpose of this class is purely to collect basic statistics
 * about the usage of the service.
 *
 * @author Petteri Kivimäki
 */
public class SearchEvent implements Serializable {

    private int id;
    private String callno;
    private String collection;
    private String lang;
    private String status;
    private String searchType;
    private String position;
    private boolean authorized;
    private String ipAddress;
    private SearchEventType eventType;
    private long processingTime;
    private Date date;
    private String owner;

    /**
     * Constructs a SearchEvent object with no values set.
     */
    public SearchEvent() {
        this.processingTime = 0;
        this.date = new Date();
    }

    /**
     * Constructs and initializes a SearchEvent object with the given values.
     *
     * @param callno call number search parameter
     * @param collection collection code
     * @param lang language
     * @param status status of the publication
     * @param ip client's ip address
     * @param eventType type of the event
     * @param owner owner of the location that's being searched
     * @param processingTime processing time of the event
     */
    public SearchEvent(String callno, String collection, String lang, String status, String ipAddress, SearchEventType eventType, String owner, long processingTime) {
        this.processingTime = processingTime;
        this.callno = callno;
        this.collection = collection;
        this.lang = lang;
        this.status = status;
        this.ipAddress = ipAddress;
        this.date = new Date();
        this.eventType = eventType;
        this.owner = owner;
        this.authorized = true;
    }

    /**
     * Constructs and initializes a SearchEvent object with the given values.
     *
     * @param callno call number search parameter
     * @param searchType search type
     * @param position position attribute
     * @param authorized boolean value that tells if the request came from
     * authorized ip address
     * @param ip client's ip address
     * @param eventType type of the event
     * @param owner owner of the location that's being searched
     * @param processingTime processing time of the event
     */
    public SearchEvent(String callno, String searchType, String position, boolean authorized, String ipAddress, SearchEventType eventType, String owner, long processingTime) {
        this.processingTime = processingTime;
        this.callno = callno;
        this.searchType = searchType;
        this.ipAddress = ipAddress;
        this.position = position;
        this.authorized = authorized;
        this.date = new Date();
        this.eventType = eventType;
        this.owner = owner;
    }

    /**
     * Returns the id of this event.
     *
     * @return id of this event
     */
    public int getId() {
        return this.id;
    }

    /**
     * Changes the id of this event.
     *
     * @param id new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the call number related to this event.
     *
     * @return call number related to this event
     */
    public String getCallno() {
        return callno;
    }

    /**
     * Changes the call number related to this event.
     *
     * @param callno new call number
     */
    public void setCallno(String callno) {
        this.callno = callno;
    }

    /**
     * Returns the search type related to this event.
     *
     * @return search type related to this event
     */
    public String getSearchType() {
        return searchType;
    }

    /**
     * Changes the search type related to this event.
     *
     * @param searchType new call searchType
     */
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * Returns the position attribute related to this event.
     *
     * @return position attribute related to this event
     */
    public String getPosition() {
        return position;
    }

    /**
     * Changes the position attribute related to this event.
     *
     * @param position new position attribute
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Changes the boolean value that indicates if the request came from
     * authorized ip address.
     *
     * @param authorized new value
     */
    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    /**
     * Returns a boolean value that indicates if the request came from
     * authorized ip address.
     *
     * @return
     */
    public boolean getAuthorized() {
        return this.authorized;
    }

    /**
     * Returns the collection code related to this event.
     *
     * @return collection code related to this event
     */
    public String getCollection() {
        return collection;
    }

    /**
     * Changes the collection code related to this event.
     *
     * @param new collection code
     */
    public void setCollection(String collection) {
        this.collection = collection;
    }

    /**
     * Returns the language related to this event.
     *
     * @return language related to this event
     */
    public String getLang() {
        return lang;
    }

    /**
     * Changes the language related to this event.
     *
     * @param lang new language
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * Returns the status of the publication related to this event.
     *
     * @return status of the publication related to this event
     */
    public String getStatus() {
        return status;
    }

    /**
     * Changes the status of the publication related to this event.
     *
     * @param status new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the ip address of the client related to this event.
     *
     * @return ip address of the client related to this event
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Changes the ip address of the client related to this event.
     *
     * @param new ip address
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Returns the type of this event.
     *
     * @return type of this event
     */
    public SearchEventType getEventType() {
        return eventType;
    }

    /**
     * Changes the type of this event.
     *
     * @param new type
     */
    public void setEventType(SearchEventType eventType) {
        this.eventType = eventType;
    }

    /**
     * Returns the date nad time of this event.
     *
     * @return date nad time of this event
     */
    public Date getDate() {
        return date;
    }

    /**
     * Changes the date and time of this event
     *
     * @param new date and time
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns the owner code of the owner that's related to this event.
     *
     * @return owner code of the owner that's related to this event
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Changes the owner code of the owner that's related to this event.
     *
     * @param owner new owner code
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Returns the processing time of the event in milliseconds.
     *
     * @return processing time of the event in milliseconds
     */
    public long getProcessingTime() {
        return this.processingTime;
    }

    /**
     * Changes the processing time of the event. Processing time must be given
     * in milliseconds.
     *
     * @param processingTime new value
     */
    public void setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
    }
}
