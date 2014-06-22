/**
 * This file is part of Location Service :: Admin.
 * Copyright (C) 2014 Petteri Kivimäki
 *
 * Location Service :: Admin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Location Service :: Admin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Location Service :: Admin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.admin.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
    private String servicePath;
    private String webPath;
    private String templatePath;
    private String stylesheetsPath;
    private String stylesheetsPathRel;
    private String scriptsPath;
    private String scriptsPathRel;
    private String scriptsSysPath;
    private String scriptsSysPathRel;
    private String scriptsPluginsPath;
    private String scriptsPluginsPathRel;
    private String mapsPath;
    private String mapsPathAdmin;
    private String imagesPath;
    private String imagesPathAdmin;
    private String ownersPath;
    private String ownersPathAdmin;
    private String homeDirRel;

    private Settings() {
        String tomcatPath = System.getProperty("catalina.base") + "/webapps/";

        /* admin.path is optional */
        String adminPath = PropertiesUtil.getProperty("admin.path");
        /* admin.name is required */
        String adminName = PropertiesUtil.getProperty("admin.name");
        /* admin.name.postfix is optional */
        String adminNamePostfix = PropertiesUtil.getProperty("admin.name.postfix");

        /* service.path is optional */
        String locationServicePath = PropertiesUtil.getProperty("service.path");
        /* service.name is required */
        String locationServiceName = PropertiesUtil.getProperty("service.name");
        /* service.name.postfix is optional */
        String locationServiceNamePostfix = PropertiesUtil.getProperty("service.name.postfix");

        /* service.webPath is required */
        String url = PropertiesUtil.getProperty("service.webPath");

        /* Check that required settings are defined */
        if (adminName == null || adminName.isEmpty()) {
            adminName = "";
            logger.error("Required property admin.name is missing or is empty.");
        }
        if (locationServiceName == null || locationServiceName.isEmpty()) {
            locationServiceName = "";
            logger.error("Required property service.name is missing or is empty.");
        }
        if (url == null || url.isEmpty()) {
            url = "";
            logger.error("Required property service.webPath is missing or is empty.");
        }

        adminPath = (adminPath == null || adminPath.isEmpty()) ? tomcatPath : adminPath;
        locationServicePath = (locationServicePath == null || locationServicePath.isEmpty()) ? tomcatPath : locationServicePath;

        this.webPath = url + locationServiceName + "/";

        if (locationServiceNamePostfix != null && !locationServiceNamePostfix.isEmpty()) {
            locationServiceName += "/" + locationServiceNamePostfix;
        }

        if (adminNamePostfix != null && !adminNamePostfix.isEmpty()) {
            adminName += "/" + adminNamePostfix;
        }

        this.servicePath = locationServicePath + locationServiceName + "/";
        this.ownersPath = servicePath + "owners/";
        this.ownersPathAdmin = adminPath + adminName + "/owners/";

        /* Check that the owners paths exist */
        if (!new File(this.ownersPath).exists()) {
            logger.error("Service owners path doesn't exist! Path: " + this.ownersPath);
        }
        if (!new File(this.ownersPathAdmin).exists()) {
            logger.error("Admin owners path doesn't exist! Path: " + this.ownersPathAdmin);
        }

        this.homeDirRel = "owners/$DIR/";
        this.templatePath = ownersPath + "$DIR/templates/lang/";
        this.stylesheetsPath = ownersPath + "$DIR/stylesheets/";
        this.stylesheetsPathRel = "owners/$DIR/stylesheets/";
        this.scriptsPath = ownersPath + "$DIR/scripts/";
        this.scriptsPathRel = "owners/$DIR/scripts/";
        this.scriptsSysPath = servicePath + "scripts/";
        this.scriptsSysPathRel = "scripts/";
        this.scriptsPluginsPath = scriptsSysPath + "plugins/";
        this.scriptsPluginsPathRel = this.scriptsSysPathRel + "plugins/";
        this.mapsPath = ownersPath + "$DIR/maps/lang/";
        this.imagesPath = ownersPath + "$DIR/images/";
        this.mapsPathAdmin = ownersPathAdmin + "$DIR/maps/lang/";
        this.imagesPathAdmin = ownersPathAdmin + "$DIR/images/";

        if (logger.isInfoEnabled()) {
            logger.info("Service path: " + this.servicePath);
            logger.info("Service URL: " + this.webPath);
            logger.info("Service owners path: " + this.ownersPath);
            logger.info("Relative home path: " + this.homeDirRel);
            logger.info("Templates path: " + this.templatePath);
            logger.info("Maps path: " + this.mapsPath);
            logger.info("Images path: " + this.imagesPath);
            logger.info("Sripts path: " + this.scriptsPath);
            logger.info("Sripts relative path: " + this.scriptsPathRel);
            logger.info("Stylesheets path: " + this.stylesheetsPath);
            logger.info("Stylesheets relative path: " + this.stylesheetsPathRel);
            logger.info("jQuery path: " + this.scriptsSysPath);
            logger.info("jQuery relative path: " + this.scriptsSysPathRel);
            logger.info("Plugins path: " + this.scriptsPluginsPath);
            logger.info("Plugins relative path: " + this.scriptsPluginsPathRel);
            logger.info("Admin owners path: " + this.ownersPathAdmin);
            logger.info("Admin maps path: " + this.mapsPathAdmin);
            logger.info("Admin images path: " + this.imagesPathAdmin);
        }
    }

    /**
     * Returns an instance of Settings. The instance is created when the method
     * is called for the first time.
     * @return instance of Settings class
     */
    public static Settings getInstance() {
        if (ref == null) {
            ref = new Settings();
        }
        return ref;
    }

    /**
     * Returns the path of the directory where all the owner related 
     * directories are located under LocationService.
     * @return root directory of the owner directories under LocationService
     */
    public String getOwnersPath() {
        return this.ownersPath;
    }

    /**
     * Returns the path of the directory where all the owner related
     * directories are located under LocationServiceAdmin.
     * @return root directory of the owner directories under LocationServiceAdmin
     */
    public String getOwnersPathAdmin() {
        return this.ownersPathAdmin;
    }

    /**
     * Returns the relative path of owner's home direcotry.
     * @param ownerCode owner whose home directory's path is requested
     * @return relative path of the owner's home directory
     */
    public String getHomePathRel(String ownerCode) {
        String path = this.homeDirRel.replace("$DIR", ownerCode);
        return path;
    }

    /**
     * Returns the path of the templates directory related to the
     * given owner code.
     * @param ownerCode owner whose template directory's path is requested
     * @return path of the templates directory related to the given owner
     */
    public String getTemplatesPath(String ownerCode) {
        String path = templatePath.replace("$DIR", ownerCode);
        return path;
    }

    /**
     * Returns the path of the stylesheets directory related to the
     * given owner code.
     * @param ownerCode owner whose stylesheet directory's path is requested
     * @return path of the stylesheet directory related to the given owner
     */
    public String getStylesheetsPath(String ownerCode) {
        String path = stylesheetsPath.replace("$DIR", ownerCode);
        return path;
    }

    /**
     * Returns the relative path of the stylesheets directory related to the
     * given owner code. This path can be used in template files when
     * referencing a css file.
     * @param ownerCode owner whose stylesheet directory's path is requested
     * @return relative path of the stylesheet directory related to the given 
     * owner
     */
    public String getStylesheetsPathRel(String ownerCode) {
        String path = stylesheetsPathRel.replace("$DIR", ownerCode);
        return path;
    }

    /**
     * Returns the path of the scripts directory related to the
     * given owner code.
     * @param ownerCode owner whose scripts directory's path is requested
     * @return path of the scripts directory related to the given owner
     */
    public String getScriptsPath(String ownerCode) {
        String path = scriptsPath.replace("$DIR", ownerCode);
        return path;
    }

    /**
     * Returns the relative path of the scripts directory related to the
     * given owner code. This path can be used in template files when
     * referencing a script file.
     * @param ownerCode owner whose scripts directory's path is requested
     * @return relative path of the scripts directory related to the given owner
     */
    public String getScriptsPathRel(String ownerCode) {
        String path = scriptsPathRel.replace("$DIR", ownerCode);
        return path;
    }

    /**
     * Returns the path of the scripts directory shared by all the users.
     * The users cannot write to this folder, only read and link to
     * the files in it.
     * @return path of the scripts directory shared by all the users
     */
    public String getScriptsSysPath() {
        return this.scriptsSysPath;
    }

    /**
     * Returns the relative path of the scripts directory shared by all the users.
     * This path can be used in template files when referencing a script file. 
     * The users cannot write to this folder, only read and link to
     * the files in it.
     * @return relative path of the scripts directory shared by all the users
     */
    public String getScriptsSysPathRel() {
        return this.scriptsSysPathRel;
    }

    /**
     * Returns the path of the plugins directory shared by all the users.
     * The users cannot write to this folder, only read and link to
     * the files in it.
     * @return path of the plugins directory shared by all the users
     */
    public String getScriptsPluginsPath() {
        return this.scriptsPluginsPath;
    }

    /**
     * Returns the relative path of the plugins directory shared by all the users.
     * This path can be used in template files when referencing a script file. 
     * The users cannot write to this folder, only read and link to
     * @return relative path of the plugins directory shared by all the users
     */
    public String getScriptsPluginsPathRel() {
        return this.scriptsPluginsPathRel;
    }

    /**
     * Returns the path of the maps directory related to the
     * given owner code.
     * @param ownerCode owner whose maps directory's path is requested
     * @return path of the maps directory related to the given owner
     */
    public String getMapsPath(String ownerCode) {
        String path = mapsPath.replace("$DIR", ownerCode);
        return path;
    }

    /**
     * Returns the path of the admin module maps directory related to the
     * given owner code.
     * @param ownerCode owner whose maps directory's path is requested
     * @return path of the admin module maps directory related to the given owner
     */
    public String getMapsPathAdmin(String ownerCode) {
        String path = mapsPathAdmin.replace("$DIR", ownerCode);
        return path;
    }

    /**
     * Returns the path of the images directory related to the
     * given owner code.
     * @param ownerCode owner whose images directory's path is requested
     * @return path of the images directory related to the given owner
     */
    public String getImagesPath(String ownerCode) {
        String path = imagesPath.replace("$DIR", ownerCode);
        return path;
    }

    /**
     * Returns the path of the admin module images directory related to the
     * given owner code.
     * @param ownerCode owner whose maps directory's path is requested
     * @return path of the admin module images directory related to the given owner
     */
    public String getImagesPathAdmin(String ownerCode) {
        String path = imagesPathAdmin.replace("$DIR", ownerCode);
        return path;
    }

    /**
     * Returns the url where LocationService is running.
     * @return url of LocationService
     */
    public String getWebpath() {
        return this.webPath;
    }

    /**
     * Returns the URL of the images directory related to the
     * given owner code.
     * @param ownerCode owner whose image directory's URL is requested
     * @return URL of the images directory related to the given owner
     */
    public String getImagesWebPath(String ownerCode) {
        String path = webPath + "owners/" + ownerCode + "/images/";
        return path;
    }

    /**
     * Returns the URL of the maps directory related to the
     * given owner code.
     * @param ownerCode owner whose map directory's URL is requested
     * @return URL of the map directory related to the given owner
     */
    public String getMapsWebPath(String ownerCode) {
        String path = webPath + "owners/" + ownerCode + "/maps/lang/";
        return path;
    }

    /**
     * Returns an array of default template file names, that should
     * exist in each language. Without these files the system may not
     * work correctly in all the situations. The number of default
     * templates is five.
     * @return an array of default template file names
     */
    public String[] getDefaultTemplates() {
        String[] templates = {
            "template_library.txt", "template_collection.txt",
            "template_shelf.txt", "template_not_found.txt", "template_not_available.txt"
        };
        return templates;
    }

    /**
     * Returns a list of directories that contains language specific files.
     * @param ownerCode owner code of the organization
     * @return list of language specific directories
     */
    public List<File> getLanguageSpecificDirs(String ownerCode) {
        List<File> dirs = new ArrayList<File>();
        dirs.add(new File(getTemplatesPath(ownerCode)));
        dirs.add(new File(getMapsPath(ownerCode)));
        dirs.add(new File(getMapsPathAdmin(ownerCode)));
        return dirs;
    }

    /**
     * Returns a list of directories that contains owner specific files.
     * @param ownerCode owner code of the organization
     * @return list of owner specific directories
     */
    public List<File> getOwnerDirs(String ownerCode) {
        List<File> dirs = new ArrayList<File>();
        dirs.add(new File(getImagesPath(ownerCode)));
        dirs.add(new File(getMapsPath(ownerCode)));
        dirs.add(new File(getTemplatesPath(ownerCode)));
        dirs.add(new File(getStylesheetsPath(ownerCode)));
        dirs.add(new File(getScriptsPath(ownerCode)));
        dirs.add(new File(getImagesPathAdmin(ownerCode)));
        dirs.add(new File(getMapsPathAdmin(ownerCode)));
        return dirs;
    }
}
