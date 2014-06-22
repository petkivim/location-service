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
package com.pkrete.locationservice.endpoint.loader;

import com.pkrete.locationservice.endpoint.model.location.Library;
import com.pkrete.locationservice.endpoint.model.location.LibraryCollection;
import com.pkrete.locationservice.endpoint.util.Settings;
import com.pkrete.locationservice.endpoint.model.location.Shelf;
import com.pkrete.locationservice.endpoint.templateparser.TemplateParser;
import java.io.*;
import java.util.Scanner;
import org.apache.log4j.Logger;

/**
 * The <code>OutputLoader</code> class implements the {@link Loader Loader} interface.
 *
 * This class implements the methods defined in the OutputLoader interface.  
 * This class offers the functionality for loading the template files that
 * are used for generating the html page that is returned to user.
 * TemplateParser interface is responsible of the actual parsing and html
 * generation.
 *
 * @author Petteri Kivimäki
 */
public class BasicLoader implements Loader {

    private final static Logger logger = Logger.getLogger(BasicLoader.class.getName());
    private String encoding;
    private TemplateParser parser;

    /**
     * Contructs and initializes an OutputLoader object.
     */
    public BasicLoader() {
        this.encoding = "UTF-8";
    }

    /**
     * Contructs and initializes an OutputLoader object.
     * @param parser template parser that is used for parsing the template
     */
    public BasicLoader(TemplateParser parser) {
        this();
        this.parser = parser;
    }

    /**
     * Sets the parser variable.
     * @param parser new value
     */
    public void setParser(TemplateParser parser) {
        this.parser = parser;
    }

    /**
     * Loads the template that is shown to the user when the
     * location doesn't exist in the database.
     * @param lang the language of the UI
     * @param callno the call number received from the UI
     * @param owner owner code of the library
     * @return contents of the template file
     */
    public String loadTemplateNotFound(String lang, String callno, String owner) {
        StringBuilder builder = new StringBuilder();
        builder.append(getPath(owner)).append(lang).append("/template_not_found.txt");
        File file = new File(builder.toString());
        if (!file.exists()) {
            logger.error(new StringBuilder("Template doesn't exist : ").append(builder).toString());
            return "<html>\n<head>\n</head>\n<body>\n</body>\n</html>\n";
        }
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Template found : ").append(builder));
        }
        return loadTemplate(lang, file.getAbsolutePath(), "0", callno, owner);
    }

    /**
     * Loads the template that is shown to the user when the
     * located item is not available.
     * @param lang the language of the UI
     * @param callno the call number received from the UI
     * @param owner owner code of the library
     * @return contents of the template file
     */
    public String loadTemplateNotAvailable(String lang, String callno, String owner) {
        StringBuilder builder = new StringBuilder();
        builder.append(getPath(owner)).append(lang).append("/template_not_available.txt");
        File file = new File(builder.toString());
        if (!file.exists()) {
            logger.error(new StringBuilder("Template doesn't exist : ").append(builder).toString());
            return "<html>\n<head>\n</head>\n<body>\n</body>\n</html>\n";
        }
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Template found : ").append(builder));
        }
        return loadTemplate(lang, file.getAbsolutePath(), "1", callno, owner);
    }

    /**
     * Loads the template of the given name, language and status. This method
     * is used when the given call number doesn't match to any Location
     * object in the database or the given item is not available.
     * @param lang language of the template
     * @param template absolute path of the template
     * @param status status of the item
     * @param callno call number of the item
     * @param owner owner code of the library
     * @return contents of the template
     */
    public String loadTemplate(String lang, String template, String status, String callno, String owner) {
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(template), encoding);
            while (scanner.hasNextLine()) {
                text.append(parser.parse(scanner.nextLine(), lang, status, callno, this, owner));
                text.append(NL);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            scanner.close();
        }
        return text.toString();
    }

    /**
     * Loads the template file related to the given Shelf object.
     * @param lang the language of the UI
     * @param shelf the Shelf object thats information is shown to the user
     * @param callno the call number received from the UI
     * @return contents of the template file
     */
    public String loadTemplate(String lang, Shelf shelf, String callno) {
        String fileName = getTemplateName(shelf, lang);
        if (!new File(fileName).exists()) {
            logger.error(new StringBuilder("Template doesn't exist : ").append(fileName).toString());
            return "<html>\n<head>\n</head>\n<body>\n</body>\n</html>\n";
        }
        return loadTemplate(lang, shelf, callno, fileName);
    }

    /**
     * Loads the template of the given name and language, and populates it with 
     * the data related to the given shelf.
     * @param lang language of the template
     * @param shelf shelf object
     * @param callno call number of the shelf
     * @param template name of the template
     * @return contents of the template
     */
    public String loadTemplate(String lang, Shelf shelf, String callno, String template) {
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(template), encoding);
            while (scanner.hasNextLine()) {
                text.append(parser.parse(scanner.nextLine(), lang, shelf, callno, this));
                text.append(NL);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            scanner.close();
        }
        return text.toString();
    }

    /**
     * Loads the template file related to the given LibraryCollection object.
     * @param lang the language of the UI
     * @param collection the LibraryCollection object thats information is 
     * shown to the user
     * @param callno the call number received from the UI
     * @return contents of the template file
     */
    public String loadTemplate(String lang, LibraryCollection collection, String callno) {
        String fileName = getTemplateName(collection, lang);
        if (!new File(fileName).exists()) {
            logger.error(new StringBuilder("Template doesn't exist : ").append(fileName).toString());
            return "<html>\n<head>\n</head>\n<body>\n</body>\n</html>\n";
        }
        return loadTemplate(lang, collection, callno, fileName);
    }

    /**
     * Loads the template of the given name and language, and populates it with 
     * the data related to the given collection.
     * @param lang language of the template
     * @param collection collection object
     * @param callno call number of the collection
     * @param template name of the template
     * @return contents of the template
     */
    public String loadTemplate(String lang, LibraryCollection collection, String callno, String template) {
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(template), encoding);
            while (scanner.hasNextLine()) {
                text.append(parser.parse(scanner.nextLine(), lang, collection, callno, this));
                text.append(NL);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            scanner.close();
        }
        return text.toString();
    }

    /**
     * Loads the template file related to the given Library object.
     * @param lang the language of the UI
     * @param library the Library object thats information is shown to the user
     * @param callno the call number that was received from the UI
     * @return contents of the template file
     */
    public String loadTemplate(String lang, Library library, String callno) {
        String fileName = getTemplateName(library, lang);
        if (!new File(fileName).exists()) {
            logger.error(new StringBuilder("Template doesn't exist : ").append(fileName).toString());
            return "<html>\n<head>\n</head>\n<body>\n</body>\n</html>\n";
        }
        return loadTemplate(lang, library, callno, fileName);
    }

    /**
     * Loads the template of the given name and language, and populates it with 
     * the data related to the given library.
     * @param lang language of the template
     * @param library library object
     * @param callno call number of the library
     * @param template name of the template
     * @return contents of the template
     */
    public String loadTemplate(String lang, Library library, String callno, String template) {
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(template), encoding);
            while (scanner.hasNextLine()) {
                text.append(parser.parse(scanner.nextLine(), lang, library, callno, this));
                text.append(NL);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            scanner.close();
        }
        return text.toString();
    }

    private String getTemplateName(Shelf shelf, String lang) {
        StringBuilder builder = new StringBuilder();
        StringBuilder templateBase = new StringBuilder();
        String path = getPath(shelf.getOwner().getCode());
        templateBase.append(path).append(lang).append("/template_shelf_");
        String templateExtension = ".txt";
        File file;

        if (shelf.getCollection().hasCollectionCode()) {
            builder.append(templateBase).append(shelf.getCallNoForTemplateName(true)).append(templateExtension);
            file = new File(builder.toString());
            if (file.exists()) {
                if (logger.isDebugEnabled()) {
                    logger.debug(new StringBuilder("Template found : ").append(builder));
                }
                return file.getAbsolutePath();
            }
        }

        builder.setLength(0);
        builder.append(templateBase).append(shelf.getCallNoForTemplateName(false)).append(templateExtension);
        file = new File(builder.toString());
        if (file.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuilder("Template found : ").append(builder));
            }
            return file.getAbsolutePath();
        }


        if (shelf.getCollection().hasCollectionCode()) {
            builder.setLength(0);
            builder.append(templateBase).append(shelf.getCollection().getCallNoForTemplateName(true)).append(templateExtension);
            file = new File(builder.toString());
            if (file.exists()) {
                if (logger.isDebugEnabled()) {
                    logger.debug(new StringBuilder("Template found : ").append(builder));
                }
                return file.getAbsolutePath();
            }
        }

        builder.setLength(0);
        builder.append(templateBase).append(shelf.getCollection().getCallNoForTemplateName(false)).append(templateExtension);
        file = new File(builder.toString());
        if (file.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuilder("Template found : ").append(builder));
            }
            return file.getAbsolutePath();
        }

        builder.setLength(0);
        builder.append(templateBase).append(shelf.getCollection().getLibrary().getCallNoForTemplateName(true)).append(templateExtension);
        file = new File(builder.toString());
        if (file.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuilder("Template found : ").append(builder));
            }
            return file.getAbsolutePath();
        }

        builder.setLength(0);
        builder.append(path).append(lang).append("/template_shelf.txt");
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Use default template : ").append(builder));
        }
        return builder.toString();
    }

    private String getTemplateName(LibraryCollection collection, String lang) {
        StringBuilder builder = new StringBuilder();
        StringBuilder templateBase = new StringBuilder();
        String path = getPath(collection.getOwner().getCode());
        templateBase.append(path).append(lang).append("/template_collection_");
        String templateExtension = ".txt";
        File file;

        if (collection.hasCollectionCode()) {
            builder.append(templateBase).append(collection.getCallNoForTemplateName(true)).append(templateExtension);
            file = new File(builder.toString());
            if (file.exists()) {
                if (logger.isDebugEnabled()) {
                    logger.debug(new StringBuilder("Template found : ").append(builder));
                }
                return file.getAbsolutePath();
            }
        }

        builder.setLength(0);
        builder.append(templateBase).append(collection.getCallNoForTemplateName(false)).append(templateExtension);
        file = new File(builder.toString());
        if (file.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuilder("Template found : ").append(builder));
            }
            return file.getAbsolutePath();
        }

        builder.setLength(0);
        builder.append(templateBase).append(collection.getLibrary().getCallNoForTemplateName(true)).append(templateExtension);
        file = new File(builder.toString());
        if (file.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuilder("Template found : ").append(builder));
            }
            return file.getAbsolutePath();
        }

        builder.setLength(0);
        builder.append(path).append(lang).append("/template_collection.txt");
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Use default template : ").append(builder));
        }
        return builder.toString();
    }

    private String getTemplateName(Library library, String lang) {
        StringBuilder builder = new StringBuilder();
        String path = getPath(library.getOwner().getCode());

        builder.append(path).append(lang).append("/template_library_").append(library.getCallNoForTemplateName(true)).append(".txt");
        File file = new File(builder.toString());
        if (file.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuilder("Template found : ").append(builder));
            }
            return file.getAbsolutePath();
        }

        builder.setLength(0);
        builder.append(path).append(lang).append("/template_library.txt");
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Use default template : ").append(builder));
        }
        return builder.toString();
    }

    /**
     * Returns the absolute path of the template (type other) with the given 
     * name. If the  given template doesn't exist, null is returned instead.
     * @param template name of the template
     * @param lang language of the UI
     * @param owner owner code of the library
     * @return absolute path of the file or null if the file does not exist
     */
    public String getTemplateOtherPath(String template, String lang, String owner) {
        StringBuilder builder = new StringBuilder();
        builder.append(getPath(owner)).append(lang).append("/template_other_").append(template.replaceAll(" ", "_"));
        File file = new File(builder.toString());
        if (!file.exists()) {
            logger.warn(new StringBuilder("Template not found : ").append(builder));
            return null;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Template found : ").append(builder));
        }
        return file.getAbsolutePath();
    }

    private String getPath(String owner) {
        return Settings.getInstance().getTemplatesPath(owner);
    }
}
