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
package com.pkrete.locationservice.admin.io.templates;

import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.location.Location;
import com.pkrete.locationservice.admin.model.search.LocationType;
import com.pkrete.locationservice.admin.model.search.SearchIndex;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.io.DirectoryService;
import com.pkrete.locationservice.admin.io.FileService;
import com.pkrete.locationservice.admin.service.LanguagesService;
import com.pkrete.locationservice.admin.service.LocationsService;
import com.pkrete.locationservice.admin.io.TemplatesService;
import com.pkrete.locationservice.admin.util.Settings;
import com.pkrete.locationservice.admin.util.TemplateType;
import com.pkrete.locationservice.admin.util.TemplatesUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TemplatesServiceImpl class implements the
 * {@link TemplatesService TemplatesService} interface.
 *
 * This class offers methods for creating, reading, updating, deleteting and
 * moving template files.
 *
 * @author Petteri Kivimäki
 */
public class TemplatesServiceImpl implements TemplatesService {

    private final static Logger logger = LoggerFactory.getLogger(TemplatesServiceImpl.class.getName());
    private FileService fileService;
    private LocationsService locationsService;
    private LanguagesService languagesService;
    private DirectoryService dirService;

    /**
     * Sets the file service object.
     *
     * @param fileService new value
     */
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Sets the locations service object.
     *
     * @param locationsService new value
     */
    public void setLocationsService(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    /**
     * Sets the language service object.
     *
     * @param languageService new value
     */
    public void setLanguagesService(LanguagesService languagesService) {
        this.languagesService = languagesService;
    }

    /**
     * Sets the directory service object.
     *
     * @param dirService new value
     */
    public void setDirService(DirectoryService dirService) {
        this.dirService = dirService;
    }

    /**
     * Returns a list of templates related to the given owner located in the
     * given language folder.
     *
     * @param lang language code
     * @param owner owner of the templates
     * @return list of templates
     */
    @Override
    public List<String> getList(String lang, Owner owner) {
        return getList(false, lang, owner);
    }

    /**
     * Returns a list of templates related to the given owner located in the
     * given language folder. If onlyOther is true, only templates which is
     * other are returned. Otherwise all the templates in the directory are
     * returned.
     *
     * @param onlyOther if true, only templates which type is other are returned
     * @param lang language code
     * @param owner owner of the templates
     * @return list of templates
     */
    @Override
    public List<String> getList(boolean onlyOther, String lang, Owner owner) {
        List<String> results = new ArrayList<String>();
        String postfix = "(.*)";
        if (onlyOther) {
            postfix = "other_(.*)";
        }
        String path = Settings.getInstance().getTemplatesPath(owner.getCode());
        File dir = new File(path + lang + "/");
        if (!dir.exists()) {
            logger.warn("The given directory doesn't exist! Path : {}", dir.getAbsolutePath());
            return results;
        }
        if (dir.list() != null) {
            for (String file : dir.list()) {
                if (file.matches("^template_" + postfix)) {
                    results.add(file);
                }
            }
            Collections.sort(results);
        }
        return results;
    }

    /**
     * Returns a map that contains template name - template display name
     * key-value pairs. Template file name is the key and template display name
     * is the value.
     *
     * @param lang language code
     * @param owner owner of the templates
     * @return map of template name - template display name key-value pairs
     */
    @Override
    public Map<String, String> getMap(String lang, Owner owner) {
        List<String> templates = getList(lang, owner);
        Map<String, String> results = new TreeMap<String, String>();
        for (String template : templates) {
            Pattern regex = Pattern.compile("^template_(library|collection|shelf|not_available|not_found)\\.txt$");
            Matcher m = regex.matcher(template);
            if (m.find()) {
                results.put(template, m.group(1).replaceAll("_", " ") + ": all");
            } else {
                regex = Pattern.compile("^template_(library|collection|shelf|other)_(.*)\\.txt$");
                m = regex.matcher(template);
                if (m.find()) {
                    results.put(template, m.group(1) + ": " + m.group(2).replaceAll("_", " "));
                }
            }
        }
        return results;
    }

    /**
     * Creates a new template named after the given filename, language and
     * owner. Before creating the template the given filename is validated and
     * false is returned in case the filename doesn't pass the validation. The
     * contents of the system template are copied to the new template.
     *
     * @param filename name of the template
     * @param lang language of the template
     * @param owner owner of the template
     * @return true if and only if the template was successfully created;
     * otherwise false
     */
    @Override
    public boolean create(String filename, String lang, Owner owner) {
        return create(filename, null, lang, owner);
    }

    /**
     * Creates a new template named after the given filename, contents, language
     * and owner. Before creating the template the given filename is validated
     * and false is returned in case the filename doesn't pass the validation.
     * The contents of the system template are copied to the new template if
     * contents parameter is null. Otherwise the give contents are written to
     * the file.
     *
     * @param filename name of the template
     * @param contents file contents, system template is used if null
     * @param lang language of the template
     * @param owner owner of the template
     * @return true if and only if the template was succesfully created;
     * otherwise false
     */
    @Override
    public boolean create(String filename, String contents, String lang, Owner owner) {
        // Validate lang and owner parameters
        if (!validate(lang, owner)) {
            logger.warn("Creating template failed! Reason : invalid parameters.");
            return false;
        }
        // Check that the name is valid
        if (!TemplatesUtil.isTemplateNameValid(filename)) {
            logger.warn("Creating new template failed! Invalid template name : {}", filename);
            return false;
        }
        // Get the absolute path of the file
        String path = TemplatesUtil.buildFilePath(filename, lang, owner);

        // If contents is null, get default contents from the system template
        if (contents == null) {
            contents = read(owner);
        }

        // Create new template
        if (fileService.add(path, contents)) {
            logger.debug("New template created : {}", path);
            return true;
        }
        logger.warn("Failed to create template : {}", path);
        return false;
    }

    /**
     * Creates a new template for the Location matching the given locationId on
     * a given level. If no Location matching the given id is found, a default
     * template for the given type is created. If type is OTHER, the otherName
     * parameter must contain the name of the template. If type is NOT_AVAILABLE
     * or NOT_FOUND, locationId and other name are ignored.
     *
     * @param locationId id of the Location object
     * @param otherName name of the template, if type is OTHER
     * @param incCollectionCode boolean value that tells if collection code
     * should be included in the template's name. If true, collection code is
     * included only if it exists and is not empty.
     * @param type type of template: LIBRARY, SHELF, COLLECTION, NOT_FOUND,
     * NOT_AVAILABLE, OTHER
     * @param lang language of the template
     * @param owner owner of the template
     * @return true if and only if the template was successfully created;
     * otherwise false
     */
    @Override
    public boolean create(int locationId, String otherName, boolean incCollectionCode, TemplateType type, String lang, Owner owner) {
        // Validate lang and owner parameters
        if (!validate(lang, owner)) {
            logger.warn("Creating template failed! Reason : invalid parameters.");
            return false;
        }
        // Get template name
        String filename = buildTemplateName(locationId, otherName, incCollectionCode, type, owner);
        // Check that the name is not null
        if (filename == null) {
            logger.warn("Creating new template failed! Unable to build valid template name.");
            return false;
        }

        // Check that the name is valid
        if (!TemplatesUtil.isTemplateNameValid(filename)) {
            logger.warn("Creating new template failed! Invalid template name : {}", filename);
            return false;
        }
        // Get the absolute path of the file
        String path = TemplatesUtil.buildFilePath(filename, lang, owner);

        logger.debug("Create new template : {}", path);

        // Get default contents from the system template
        String contents = read(owner);
        // Create new template
        return fileService.add(path, contents);
    }

    /**
     * Reads the system template related to the given Owner.
     *
     * @param owner owner of the template
     * @return contents of the system template
     */
    @Override
    public String read(Owner owner) {
        // Get the absolute path of the template
        String filePath = TemplatesUtil.buildFilePath("../template.txt", null, owner);
        logger.debug("Read system template : {}", filePath);
        // Read file contents
        return fileService.read(filePath);
    }

    /**
     * Reads the template matching the given name and language.
     *
     * @param filename name of the template
     * @param lang language if the template
     * @param owner owner of the template
     * @return contents of the template
     */
    @Override
    public String read(String filename, String lang, Owner owner) {
        // Validate lang and owner parameters
        if (!validate(lang, owner)) {
            logger.warn("Reading template failed! Reason : invalid parameters.");
            return "";
        }
        // Check that the name is valid
        if (!TemplatesUtil.isTemplateNameValid(filename)) {
            logger.warn("Reading template failed! Invalid template name : {}", filename);
            return "";
        }
        // Get the absolute path of the file
        String path = TemplatesUtil.buildFilePath(filename, lang, owner);
        logger.debug("Read template : {}", path);

        // Read file contents
        return fileService.read(path);
    }

    /**
     * Writes the given contents to the system template of the given Owner. If
     * the system template file doesn't exist yet, it's created, otherwise it
     * will be overwritten.
     *
     * @param contents contents of the template
     * @param owner owner of the template
     * @return true if and only if the template is successfully written;
     * otherwise false
     */
    @Override
    public boolean update(String contents, Owner owner) {
        // Get the absolute path of the file
        String filePath = TemplatesUtil.buildFilePath("../template.txt", null, owner);

        // Write contents to the system template
        if (fileService.write(filePath, contents)) {
            logger.debug("System template updated : {}", filePath);
            return true;
        }
        logger.debug("Updating system template failed : {}", filePath);
        return false;
    }

    /**
     * Update the template pointed by the given filename, language and owner.
     *
     * @param filename name of the template
     * @param contents contents of the template
     * @param lang language of the template
     * @param owner owner of the template
     * @return true if and only if the template exists and it's successfully
     * updated; otherwise false
     */
    @Override
    public boolean update(String filename, String contents, String lang, Owner owner) {
        // Validate lang and owner parameters
        if (!validate(lang, owner)) {
            logger.warn("Updating template failed! Reason : invalid parameters.");
            return false;
        }
        // Check that the name is valid
        if (!TemplatesUtil.isTemplateNameValid(filename)) {
            logger.warn("Updating template failed! Invalid template name : {}", filename);
            return false;
        }
        // Get the absolute path of the file
        String path = TemplatesUtil.buildFilePath(filename, lang, owner);
        logger.debug("Update template : {}", path);

        // Update file contents
        if (fileService.update(path, contents)) {
            logger.debug("Template updated : {}", path);
            return true;
        }
        logger.debug("Updating template failed : {}", path);
        return false;

    }

    /**
     * Deletes the template pointed by the given filename, language and owner.
     *
     * @param filename name of the template
     * @param lang language of the template
     * @param owner owner of the template
     * @return true if and only if the template is successfully deleted; false
     * otherwise
     */
    @Override
    public boolean delete(String filename, String lang, Owner owner) {
        // Validate lang and owner parameters
        if (!validate(lang, owner)) {
            logger.warn("Deleting template failed! Reason : invalid parameters.");
            return false;
        }
        // Check that the name is valid
        if (!TemplatesUtil.isTemplateNameValid(filename)) {
            logger.warn("Deleting template failed! Invalid template name : {}", filename);
            return false;
        }
        // Get the absolute path of the file
        String path = TemplatesUtil.buildFilePath(filename, lang, owner);
        logger.info("Delete template : {}", path);

        // Delete the template
        if (fileService.delete(path)) {
            logger.debug("Template deleted : {}", path);
            return true;
        }
        logger.debug("Deleting template failed : {}", path);
        return false;
    }

    /**
     * Renames the given template.
     *
     * @param oldFilename old template name
     * @param newFilename new template name
     * @param lang language of the templates
     * @param owner owner of the template
     * @return true if and only if the template was renamed
     */
    @Override
    public boolean rename(String oldFilename, String newFilename, String lang, Owner owner) {
        // Validate lang and owner parameters
        if (!validate(lang, owner)) {
            logger.warn("Renaming template failed! Reason : invalid parameters.");
            return false;
        }
        // Check that the new name is valid
        if (!TemplatesUtil.isTemplateNameValid(newFilename)) {
            logger.warn("Renaming template failed! New template name is invalid : {}", newFilename);
            return false;
        }
        // Check that the old name is valid
        if (!TemplatesUtil.isTemplateNameValid(oldFilename)) {
            logger.warn("Renaming template failed! Old template name is invalid : {}", oldFilename);
            return false;
        }
        // Get the absolute paths of the templates
        String oldPath = TemplatesUtil.buildFilePath(oldFilename, lang, owner);
        String newPath = TemplatesUtil.buildFilePath(newFilename, lang, owner);

        // Rename template
        if (fileService.rename(oldPath, newPath)) {
            logger.debug("Template renamed.");
            return true;
        }
        logger.warn("Renaming template failed.");
        return false;
    }

    /**
     * Renames the given template.
     *
     * @param oldFilename old template name
     * @param newId id of the Location object according to which the template is
     * renamed
     * @param otherName name of the template, if type is OTHER
     * @param incCollectionCode boolean value that tells if collection code
     * should be included in the template's name. If true, collection code is
     * included only if it exists and is not empty
     * @param newType new type of template: LIBRARY, SHELF, COLLECTION,
     * NOT_FOUND, NOT_AVAILABLE, OTHER
     * @param lang language of the template
     * @param owner owner of the template
     * @return true if and only if the template was renamed
     */
    @Override
    public boolean rename(String oldFilename, int newId, String newOtherName, boolean incCollectionCode, TemplateType newType, String lang, Owner owner) {
        // Validate lang and owner parameters
        if (!validate(lang, owner)) {
            logger.warn("Renaming template failed! Reason : invalid parameters.");
            return false;
        }
        // Get new template name
        String newFilename = buildTemplateName(newId, newOtherName, incCollectionCode, newType, owner);
        // Check that the name is not null
        if (newFilename == null) {
            logger.warn("Renaming template failed! Unable to build valid template name.");
            return false;
        }
        // Check that the new name is valid
        if (!TemplatesUtil.isTemplateNameValid(newFilename)) {
            logger.warn("Renaming template failed! New template name is invalid : {}", newFilename);
            return false;
        }
        // Check that the old name is valid
        if (!TemplatesUtil.isTemplateNameValid(oldFilename)) {
            logger.warn("Renaming template failed! Old template name is invalid : {}", oldFilename);
            return false;
        }
        // Get the absolute paths of the templates
        String oldPath = TemplatesUtil.buildFilePath(oldFilename, lang, owner);
        String newPath = TemplatesUtil.buildFilePath(newFilename, lang, owner);
        logger.debug("Rename template.");

        // Rename template
        if (fileService.rename(oldPath, newPath)) {
            logger.debug("Template renamed.");
            return true;
        }
        logger.warn("Renaming template failed.");
        return false;
    }

    /**
     * Tests whether the template exists.
     *
     * @param filename name of the template
     * @param lang language of the template
     * @param owner owner of the template
     * @return true if and only if the template exists; false otherwise
     */
    @Override
    public boolean exists(String filename, String lang, Owner owner) {
        // Validate lang and owner parameters
        if (!validate(lang, owner)) {
            return false;
        }
        // Get list of templates that exist in the given language dir
        List<String> templates = this.getList(lang, owner);
        // Go through the list
        for (String template : templates) {
            if (template.equals(filename)) {
                // The template exists, return true
                return true;
            }
        }
        // No match was found, return false
        return false;
    }

    /**
     * Creates the default template files that must exist in each language. The
     * system template file is used as a base for the files, which means that
     * the contents of the system template are copied into each file that is
     * created. This method can be used when adding a new language to the
     * system, so that the user doesn't have to create the default templates for
     * the new language.
     *
     * @param lang language of the templates to be created
     */
    @Override
    public boolean createDefaults(String lang, Owner owner) {
        logger.info("Create default templates.");
        // Validate lang and owner parameters
        if (!validate(lang, owner)) {
            logger.warn("Creating default templates failed! Reason : invalid parameters.");
            return false;
        }
        // Get list of default templates
        String[] templates = Settings.getInstance().getDefaultTemplates();
        // Get default contents from the system template
        String contents = read(owner);

        boolean ok = true;
        List<String> created = new ArrayList<String>();

        // Loop through the default templates and try to create them
        for (int i = 0; i < templates.length; i++) {
            // Validate template name
            if (!TemplatesUtil.isTemplateNameValid(templates[i])) {
                logger.warn("Creating new default template failed! Invalid template name : {}", templates[i]);
                ok = false;
                break;
            }
            // Get the absolute path of the file
            String path = TemplatesUtil.buildFilePath(templates[i], lang, owner);
            // Create the template
            if (!fileService.add(path, contents)) {
                ok = false;
                break;
            }
            created.add(path);
        }
        if (!ok) {
            logger.warn("Creating default templates failed -> ROLLBACK.");
            logger.warn("Delete all the previously created templates.");
            // Do rollback if all the templates were not created.
            // Go through all the created templates and try to delete them.
            boolean deleted = true;
            for (String path : created) {
                if (!fileService.delete(path)) {
                    deleted = false;
                }
            }
            if (!deleted) {
                logger.warn("Failed to delete all the previously created template files.");
            } else {
                logger.info("Succesfully deleted all the previously created template files.");
            }
            return false;
        }
        return true;
    }

    /**
     * Builds a new template name for the Location matching the given locationId
     * on a given level. If no Location matching the given id is found, a
     * default template for the given type is created. If type is OTHER, the
     * otherName parameter must contain the name of the template. If type is
     * NOT_AVAILABLE or NOT_FOUND, locationId and other name are ignored.
     *
     * @param locationId id of the Location object
     * @param otherName name of the template, if type is OTHER
     * @param incCollectionCode boolean value that tells if collection code
     * should be included in the template's name. If true, collection code is
     * included only if it exists and is not empty
     * @param type type of template: LIBRARY, SHELF, COLLECTION, NOT_FOUND,
     * NOT_AVAILABLE, OTHER
     * @param owner owner of the template
     * @return name of the template or null, if parameters are invalid
     */
    @Override
    public String buildTemplateName(int locationId, String otherName, boolean incCollectionCode, TemplateType type, Owner owner) {
        StringBuilder builder = new StringBuilder();
        // Get index entry matching the given locationId
        SearchIndex index = locationsService.getIndexEntry(locationId);

        // Get location type, index can be null
        LocationType locationType = (index == null ? null : index.getLocationType());

        // Validate template type - location type combination
        if (!TemplatesUtil.validate(type, locationType)) {
            logger.warn("Building template name failed! Invalid template type - location type combination.");
            return null;
        }

        // Begin building the filename
        builder.append("template_").append(type.toString());

        // If template type is OTHER, otherName can't be null or empty
        if (type == TemplateType.OTHER) {
            if (otherName == null) {
                logger.warn("Building template name failed! Template type is OTHER and \"otherName\" is null.");
                return null;
            }
            if (otherName.isEmpty()) {
                logger.warn("Building template name failed! Template type is OTHER and \"otherName\" is empty.");
                return null;
            }
            builder.append("_").append(otherName);
        } else if (index != null && type != TemplateType.NOT_AVAILABLE && type != TemplateType.NOT_FOUND) {
            // Check that index owner and given owner are the same
            if (index.getOwner().getId() != owner.getId()) {
                logger.warn("Building template name failed! Given owner doesn't match with index entry owner : {} <> {}", index.getOwner().getCode(), owner.getCode());
                return null;
            }

            // Variable for Location object
            Location location = null;

            // Get Location object matching the index entry
            if (index.getLocationType() == LocationType.LIBRARY) {
                location = locationsService.getLibrary(index.getLocationId(), owner);
            } else if (index.getLocationType() == LocationType.COLLECTION) {
                location = locationsService.getCollection(index.getLocationId(), owner);
            } else if (index.getLocationType() == LocationType.SHELF) {
                location = locationsService.getShelf(index.getLocationId(), owner);
            }
            if (location == null) {
                logger.warn("Building template name failed! Unable to find Location matching the given conditions {\"id\":{}\",\"owner\":\"{}\"}", index.getLocationId(), owner.getCode());
                return null;
            }
            // Get call number
            builder.append("_").append(location.getCallNoForTemplateName(incCollectionCode));
        }
        // Add file extension
        builder.append(".txt");
        // Replace all the whitespaces with underscores
        return builder.toString().replaceAll("\\s", "_");
    }

    /**
     * Returns a Map containing all the template files and a list of languages
     * in which they're available.
     *
     * @param owner owner of the template files
     * @return Map containing all the template files and a list of languages in
     * which they're available
     */
    @Override
    public Map<String, List<Language>> getTemplatesAndLanguages(Owner owner) {
        // Map for the results
        java.util.Map maps = new LinkedHashMap();
        // Get languages related to the given owner
        List<Language> languages = languagesService.getLanguages(owner);
        // Get path of the maps admin dir
        String path = Settings.getInstance().getTemplatesPath(owner.getCode());
        // Loop through all the languages
        for (Language lang : languages) {
            // Get list of file names in the current language directory
            List<String> files = this.dirService.getFilesStr(path + lang.getCode() + "/");
            // Go through all the files
            for (String file : files) {
                // If the file exists, add the language
                if (maps.containsKey(file)) {
                    ((List) maps.get(file)).add(lang);
                } else {
                    // If the file doesn't exist yet,
                    // Create a list for the languages
                    List temp = new ArrayList();
                    // Add the current language to the list
                    temp.add(lang);
                    // Add the file and the language list to the results
                    maps.put(file, temp);
                }
            }
        }
        // Return results
        return maps;
    }

    /**
     * Checks that the given language and owner parameters are not null or
     * empty.
     *
     * @param lang language
     * @param owner owner
     * @return true if and only if both parameters are not null and are not
     * empty; otherwise false
     */
    private boolean validate(String lang, Owner owner) {
        // Language can't be null or empty
        if (lang == null || lang.isEmpty()) {
            logger.warn("Language can't be null or empty.");
            return false;
        }
        // Owner can't be null
        if (!validate(owner)) {
            return false;
        }
        return true;
    }

    /**
     * Checks that the owner parameter is not null.
     *
     * @param owner owner
     * @return true if and only if owner is not null; otherwise false
     */
    private boolean validate(Owner owner) {
        if (owner == null) {
            logger.warn("Owner can not be null.");
            return false;
        }
        return true;
    }
}
