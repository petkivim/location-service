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
package com.pkrete.locationservice.endpoint.callnoparser;

import com.pkrete.locationservice.endpoint.callnoparser.simpleparser.SimpleCallNoParser;
import com.pkrete.locationservice.endpoint.generator.Generator;
import com.pkrete.locationservice.endpoint.service.Service;
import com.pkrete.locationservice.endpoint.util.ApplicationContextUtils;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class generates {@link CallNoParser CallNoParser} objects.
 *
 * @author Petteri Kivimäki
 */
public class CallNoParserFactoryImpl implements CallNoParserFactory {

    private static final Logger logger = LoggerFactory.getLogger(CallNoParserFactoryImpl.class.getName());
    private Map<LocatingStrategy, CallNoParser> parsers;

    /**
     * Constructs and initializes a new CallNoParserFactoryImpl.
     *
     * @param parsers map of available parsers
     */
    public CallNoParserFactoryImpl(Map<LocatingStrategy, CallNoParser> parsers) {
        this.parsers = parsers;
        if (logger.isDebugEnabled()) {
            StringBuilder builder = new StringBuilder("Available call number parsers:");
            for (LocatingStrategy strategy : this.parsers.keySet()) {
                builder.append(" ").append(strategy);
            }
            logger.debug(builder.toString());
        }
    }

    /**
     * Creates a new call number parser object.
     *
     * @param generator the generator object that generates the html page
     * returned to the user
     * @param owner owner code of the location that is being resolved
     * @param strategy defines the type of the parser that's being used
     * @return new call number parser
     */
    @Override
    public CallNoParser createParser(Generator generator, String owner, LocatingStrategy strategy) {
        // If strategy is null, use default
        if (strategy == null) {
            logger.warn("Strategy parameter is null -> use \"SIMPLE\" call number parser. Owner : \"{}\".", owner);
            strategy = LocatingStrategy.SIMPLE;
        }
        // Get parser matching the given strategy
        CallNoParser parser = this.parsers.get(strategy);
        // If parser is null, use default
        if (parser == null) {
            parser = new SimpleCallNoParser(generator);
            parser.setDbService((Service) ApplicationContextUtils.getApplicationContext().getBean("dbService"));
            logger.warn("Parser matching the given \"{}\" strategy is not available in the parsers list. Use \"SIMPLE\" strategy.", strategy);
            strategy = LocatingStrategy.SIMPLE;
        }
        // Set generator
        parser.setGenerator(generator);
        logger.debug("Use \"{}\" call number parser. Owner : \"{}\".", strategy, owner);
        // Return the parser
        return parser;
    }
}
