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
package com.pkrete.locationservice.endpoint.callnoparser.stepparser;

import com.pkrete.locationservice.endpoint.callnoparser.CallNoParser;
import com.pkrete.locationservice.endpoint.callnoparser.LocatingStrategy;
import com.pkrete.locationservice.endpoint.generator.Generator;

/**
 * The abstract <code>StepParser</code> class extends the
 * {@link CallNoParser CallNoParser} class.
 *
 * StepParser follows the Chain of Responsibility design pattern which means
 * that it delegates the task to the next object in the chain if it's not able
 * to complete the given task. All the subclasses must implement the abstract
 * <i>parse</i> method defined in the CallNoParser base class.
 *
 * @author Petteri Kivimäki
 */
public abstract class StepParser extends CallNoParser {

    /**
     * The maximum amount of words in the location information that the class in
     * question can handle.
     */
    protected int limit;
    /**
     * The next object in the chain.
     */
    protected StepParser next;
    /**
     * The previous object in the chain.
     */
    protected StepParser previous;

    /**
     * Constructs and initializes new StepParser.
     *
     * @param limit the maximum amount of words in the call number that the
     * class can handle
     */
    protected StepParser(int limit) {
        super(LocatingStrategy.BASIC);
        this.limit = limit;
    }

    /**
     * Constructs and initializes new StepParser.
     *
     * @param generator the generator object that generates the HTML page
     * returned to the user
     */
    protected StepParser(Generator generator) {
        super(generator);
    }

    /**
     * Constructs and initializes a StepParser object with the given generator
     * object and limit
     *
     * @param generator the generator object that generates the HTML page
     * returned to the user
     * @param limit the maximum amount of words in the call number that the
     * class can handle
     */
    protected StepParser(Generator generator, int limit) {
        super(generator);
        this.limit = limit;
    }

    /**
     * Constructs and initializes a StepParser object with the given generator
     * object and limit
     *
     * @param generator the generator object that generates the HTML page
     * returned to the user
     * @param limit the maximum amount of words in the call number that the
     * class can handle
     * @param previous CallNoParser object before this object in processing
     * queue
     */
    protected StepParser(Generator generator, int limit, StepParser previous) {
        this(generator, limit);
        this.previous = previous;
    }
}
