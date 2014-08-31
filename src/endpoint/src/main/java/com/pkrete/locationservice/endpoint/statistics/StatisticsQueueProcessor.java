/**
 * This file is part of Location Service :: Endpoint.
 * Copyright (C) 2014 Petteri Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.statistics;

import com.pkrete.locationservice.endpoint.mailer.EmailMessageType;
import com.pkrete.locationservice.endpoint.mailer.EmailService;
import com.pkrete.locationservice.endpoint.service.Service;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * This class is responsible for monitoring search event queue and
 * saving the objects added to the queue to the database. After
 * the object has been added, it's removed from the queue.
 * 
 * Saving operation is run in a JDK Timer based thread, that is different from the main
 * thread. This implementation makes the actual database operation
 * invisible for the user, as it runs in the backround in another thread. The
 * thread is started when the application starts, and it's making blocking
 * method calls, which means that it keeps on running until the shutdown.
 * 
 * @author Petteri Kivimäki
 */
public class StatisticsQueueProcessor implements Runnable {

    private final static Logger logger = Logger.getLogger(StatisticsQueueProcessor.class.getName());
    private SearchEventStatisticsQueue queue;
    private Service dbService;
    private EmailService mailService;
    private List<SearchEvent> recoveryQueue;
    private int failedAttemptsCount = 0;
    private int failedAttemptsMaxLimit;

    /**
     * Constructs and initializes a new StatisticsQueueProcessor object.
     */
    public StatisticsQueueProcessor() {
        this.queue = SearchEventStatisticsQueue.getInstance();
        this.failedAttemptsMaxLimit = 5;
        this.recoveryQueue = new ArrayList<SearchEvent>();
        logger.info("StatisticsQueueProcessor initiated.");
    }

    /**
     * Changes the dbService object.
     * @param dbService new value
     */
    public void setDbService(Service dbService) {
        this.dbService = dbService;
    }

    /**
     * Sets the value of the mailService instance variable.
     * @param mailService new value
     */
    public void setMailService(EmailService mailService) {
        this.mailService = mailService;
    }

    /**
     * Returns the current value of failed attempts max limit.
     * @return current value of failed attempts max limit
     */
    public int getFailedAttemptsMaxLimit() {
        return failedAttemptsMaxLimit;
    }

    /**
     * Sets the value of failed attempts max limit.
     * @param failedAttemptsMaxLimit new value
     */
    public void setFailedAttemptsMaxLimit(int failedAttemptsMaxLimit) {
        this.failedAttemptsMaxLimit = failedAttemptsMaxLimit;
    }

    /**
     * Monitors the search event statistics queue and saves event to the db
     * when one becomes available.
     */
    @Override
    public void run() {
        logger.info("StatisticsQueueProcessor started. Failed attempts max limit set to " + this.failedAttemptsMaxLimit + ".");
        SearchEvent event;
        while ((event = queue.take()) != null && !Thread.currentThread().isInterrupted()) {
            if (!dbService.save(event)) {
                // Saving the event failed -> increase failedAttemptsCount
                this.failedAttemptsCount++;
                logger.error("Saving the search event to the database failed! Failed attempts count current value: " + this.failedAttemptsCount);
                // Add the event to the recovery queue so that it can be
                // accessed when the db starts working again
                this.recoveryQueue.add(event);
                logger.info("SearchEvent added to the recovery queue. Current queue size: " + this.recoveryQueue.size());
                // If failedAttemptsCount equals to failedAttemptsMaxLimit notify admin
                if (this.failedAttemptsCount == this.failedAttemptsMaxLimit) {
                    logger.error("Failed attempts max limit reached! Notify admin!");
                    if (this.mailService != null) {
                        if (this.mailService.send(EmailMessageType.LOGGER_ERROR)) {
                            logger.info("Admin was succesfully notified!");
                        } else {
                            logger.error("Failed to notify admin!");
                        }
                    }
                }
            } else {
                // Try to recover recovery queue if queue not empty
                if (!this.recoveryQueue.isEmpty()) {
                    logger.info("Trying to save " + this.recoveryQueue.size() + " search events from the recovery queue.");
                    // Create new queue for events that can not be added to the db
                    List<SearchEvent> newQueue = new ArrayList<SearchEvent>();
                    // Loop through the queue
                    for (SearchEvent temp : this.recoveryQueue) {
                        // If adding the event fails, add it to the new queue
                        if (!this.dbService.save(temp)) {
                            logger.error("Saving search event from the recovery queue failed!");
                            newQueue.add(temp);
                        }
                    }
                    // Replace the old queue with the new one
                    this.recoveryQueue = newQueue;
                    logger.info("Handling recovery queue done. Current recovery queue size : " + this.recoveryQueue.size());

                    // If failedAttemptsMaxLimit has been reached, admin has been notified about the problem.
                    // Notify admin about the recovery.
                    if (this.failedAttemptsCount >= this.failedAttemptsMaxLimit) {
                        logger.info("Notify admin about the recovery.");
                        if (this.mailService.send(EmailMessageType.LOGGER_ERROR_RECOVERY)) {
                            logger.info("Admin was succesfully notified!");
                        } else {
                            logger.error("Failed to notify admin!");
                        }
                    }
                }
                // Saving the event succeedeed -> reset failedAttemptsCount
                this.failedAttemptsCount = 0;
            }
        }
    }
}
