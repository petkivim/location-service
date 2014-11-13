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
package com.pkrete.locationservice.endpoint.util;

import com.pkrete.locationservice.endpoint.callnoparser.CallNoParser;
import com.pkrete.locationservice.endpoint.callnoparser.LocatingStrategy;
import com.pkrete.locationservice.endpoint.service.Service;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * This class generates {@link Service Service} objects for
 * {@link CallNoParser CallNoParser} objects. Each parser implements a
 * {@link LocatingStrategy LocatingStrategy} and the implementation of the
 * Service interface may vary between different strategies.
 *
 * @author Petteri Kivimäki
 */
public class ServiceFactoryImpl implements ServiceFactory {

    private final static Logger logger = Logger.getLogger(ServiceFactoryImpl.class.getName());
    protected Service defaultService;
    protected Map<LocatingStrategy, Service> services;

    /**
     * Constructs and initializes a new ServiceFactoryImpl object.
     *
     * @param defaultService service that's used as default
     */
    public ServiceFactoryImpl(Service defaultService) {
        this.defaultService = defaultService;
    }

    /**
     * Sets the services variable.
     *
     * @param services new value
     */
    public void setServices(Map<LocatingStrategy, Service> services) {
        this.services = services;
    }

    /**
     * Return the default Service object.
     *
     * @return default service
     */
    @Override
    public Service get() {
        return this.defaultService;
    }

    /**
     * Returns the Service object matching the given key. If no Service object
     * matching the given key cannot be found, default Service object is
     * returned.
     *
     * @param strategy locating strategy that is used as a key
     * @return Service object matching the given key
     */
    @Override
    public Service get(LocatingStrategy strategy) {
        // If strategy is null, return default
        if (strategy == null) {
            logger.warn("Locating strategy is null, use default service.");
            return this.defaultService;
        }
        // If services is null or empty, return default
        if (this.services == null || this.services.isEmpty()) {
            logger.warn("Services map is null or empty, use default service.");
            return this.defaultService;
        }

        // If services doesn't contain the given key, return default
        if (!this.services.containsKey(strategy)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Services map doesn't contain the given key \"" + strategy + "\", use default service.");
            }
            return this.defaultService;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Services map contains the given key \"" + strategy + "\", use the service matching the key.");
        }
        // Return the service matching the given key
        return this.services.get(strategy);
    }
}
