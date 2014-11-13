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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class presents a queue of {@link SearchEvent SearchEvent} objects that
 * are waiting to be stored in the database. Every search event generates a new
 * event that is stored to the database for statistical purposes.
 *
 * This queue is accessed via BlockingQueue interface and the queue
 * implementation uses LinkedBlockingQueue class. BlockingQueue implementation
 * is thread-safe. All queuing methods achieve their effects atomically using
 * internal locks or other forms of concurrency control.
 *
 * This class implements Singleton design pattern, which means that only one
 * object is created runtime, and it's referenced by all the other objects.
 * Because of this, thread safe implementation is essential.
 *
 * @author Petteri Kivimäki
 */
public class SearchEventStatisticsQueue {

    private BlockingQueue<SearchEvent> queue;
    private static SearchEventStatisticsQueue ref;

    /**
     * The class implements Singleton design pattern, so constructor must be
     * defined as private.
     */
    private SearchEventStatisticsQueue() {
        this.queue = new LinkedBlockingQueue<SearchEvent>();
    }

    /**
     * Returns the SearchEventStatisticsQueue Singleton object. If the object
     * doesn't exist yet, it's created.
     *
     * @return SearchEventStatisticsQueue Singleton object
     */
    public static SearchEventStatisticsQueue getInstance() {
        if (ref == null) {
            ref = new SearchEventStatisticsQueue();
        }
        return ref;
    }

    /**
     * Inserts a new search event to the queue, waiting if necessary for space
     * to become available.
     *
     * @param msg message to be inserted
     */
    public void put(SearchEvent msg) {
        try {
            queue.put(msg);
        } catch (InterruptedException iex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Unexpected interruption");
        }

    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary until
     * an element becomes available.
     *
     * @return head of the queue
     */
    public SearchEvent take() {
        try {
            return queue.take();
        } catch (InterruptedException iex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Unexpected interruption");
        }
    }

    /**
     * Returns true if this queue contains no elements.
     *
     * @return true if this queue contains no elements
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
