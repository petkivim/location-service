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

import java.io.File;
import org.apache.log4j.Logger;

/**
 * The Settings class contains information used by other parts of the program.
 * Settings follows the Singleton design pattern which means that only one
 * instance of Settings is created while the program is running.
 *
 * @author Petteri Kivimäki
 */
public class Settings {

    private final static Logger logger = Logger.getLogger(Settings.class.getName());
    private static Settings ref;
    private String homePath;
    private String webPath;
    private String templatePath;
    private String mapsPath;
    private String imagesPath;

    private Settings() {
        String tomcatPath = System.getProperty("catalina.base") + "/webapps/";

        /* service.path is optional */
        String locationServicePath = PropertiesUtil.getProperty("service.path");
        /* service.name is required */
        String locationServiceName = PropertiesUtil.getProperty("service.name");
        /* service.name.postfix is optional */
        String locationServiceNamePostfix = PropertiesUtil.getProperty("service.name.postfix");

        /* service.webPath is required */
        String url = PropertiesUtil.getProperty("service.webPath");

        /* Check that required settings are defined */
        if (locationServiceName == null || locationServiceName.isEmpty()) {
            locationServiceName = "";
            logger.error("Required property service.name is missing or is empty.");
        }
        if (url == null || url.isEmpty()) {
            url = "";
            logger.error("Required property service.webPath is missing or is empty.");
        }

        this.webPath = url + locationServiceName + "/";

        locationServicePath = (locationServicePath == null || locationServicePath.isEmpty()) ? tomcatPath : locationServicePath;

        if (locationServiceNamePostfix != null && !locationServiceNamePostfix.isEmpty()) {
            locationServiceName += "/" + locationServiceNamePostfix;
        }

        this.homePath = locationServicePath + locationServiceName + "/";

        /* Check that the home path exists */
        if (!new File(this.homePath).exists()) {
            logger.error("Home path doesn't exist! Path: " + this.homePath);
        }

        this.templatePath = homePath + "owners/$DIR/templates/lang/";
        this.mapsPath = homePath + "owners/$DIR/maps/lang/";
        this.imagesPath = "owners/$DIR/images/";

        if (logger.isInfoEnabled()) {
            logger.info("Home path: " + this.homePath);
            logger.info("URL: " + this.webPath);
            logger.info("Templates path: " + this.templatePath);
            logger.info("Maps path: " + this.mapsPath);
            logger.info("Images path: " + this.imagesPath);
        }
    }

    /**
     * Returns an instance of Settings. The instance is created when the method
     * is called for the first time.
     *
     * @return instance of Settings class
     */
    public static Settings getInstance() {
        if (ref == null) {
            ref = new Settings();
        }
        return ref;
    }

    /**
     * Returns the path of the templates directory related to the given owner
     * code.
     *
     * @param ownerCode owner whose template directory's path is requested
     * @return path of the templates directory related to the given owner
     */
    public String getTemplatesPath(String ownerCode) {
        String path = templatePath.replace("$DIR", ownerCode);
        return path;
    }

    /**
     * Returns the path of the maps directory related to the given owner code.
     *
     * @param ownerCode owner whose maps directory's path is requested
     * @return path of the maps directory related to the given owner
     */
    public String getMapsPath(String ownerCode) {
        String path = mapsPath.replace("$DIR", ownerCode);
        return path;
    }

    /**
     * Returns the path of the images directory related to the given owner code.
     *
     * @param ownerCode owner whose images directory's path is requested
     * @return path of the images directory related to the given owner
     */
    public String getImagesPath(String ownerCode) {
        String path = imagesPath.replace("$DIR", ownerCode);
        return path;
    }

    /**
     * Returns the url where LocationService is running.
     *
     * @return url of LocationService
     */
    public String getWebpath() {
        return this.webPath;
    }
}
