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
package com.pkrete.locationservice.admin.controller.mvc;

import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.io.CSSService;
import com.pkrete.locationservice.admin.io.JSService;
import com.pkrete.locationservice.admin.io.TemplatesService;
import com.pkrete.locationservice.admin.util.UsersUtil;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.annotation.Resource;

/**
 * The {@link EditTemplateController EditTemplateController} handles the requests
 * that are related to editing a template file and then saving it to file.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/edittemplate.htm")
public class EditTemplateController extends BaseController {

    @Autowired
    @Qualifier("templatesService")
    private TemplatesService templatesService;
    @Autowired
    @Qualifier("cssService")
    private CSSService cssService;
    @Autowired
    @Qualifier("jsService")
    private JSService jsService;
    @Resource(name = "libraryTags")
    private Map<String, String> libraryTags;
    @Resource(name = "collectionTags")
    private Map<String, String> collectionTags;
    @Resource(name = "shelfTags")
    private Map<String, String> shelfTags;
    @Resource(name = "notFoundNotAvailableTags")
    private Map<String, String> notFoundNotAvailableTags;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception, ServletException, IOException {
        /* Get the current user. */
        Owner owner = UsersUtil.getUser(request, usersService).getOwner();
        /* Model that is returned together with the view */
        Map<String, Object> model = new HashMap<String, Object>();

        String lang = request.getParameter("lang");
        String template = request.getParameter("template");
        String contents = request.getParameter("contents");
        String isSys = request.getParameter("sys");

        if (contents == null) {
            if (isSys != null) {
                contents = templatesService.read(owner);
            } else {
                contents = templatesService.read(template, lang, owner);
            }
        }

        if (request.getParameter("btn_save") != null || request.getParameter("btn_save_exit") != null) {
            if (isSys != null) {
                if (!templatesService.update(contents, owner)) {
                    throw new Exception("Updating default template failed.");
                }
            } else {
                if (!templatesService.update(template, contents, lang, owner)) {
                    throw new Exception("Updating template failed.");
                }
            }
            if (request.getParameter("btn_save_exit") != null) {
                return new ModelAndView("redirect:templates.htm?select_lang=" + lang + "&template=" + URLEncoder.encode(template, "UTF-8"));
            }
        } else if (request.getParameter("btn_back") != null) {
            return new ModelAndView("redirect:templates.htm?select_lang=" + lang + "&template=" + URLEncoder.encode(template, "UTF-8"));
        }

        Map tags = new TreeMap();
        if (isLibrary(template) || isCollection(template) || isShelf(template) || isOther(template)) {
            tags.putAll(libraryTags);
        }

        if (isCollection(template) || isShelf(template) || isOther(template)) {
            tags.putAll(collectionTags);
        }

        if (isShelf(template) || isOther(template)) {
            tags.putAll(shelfTags);
        }

        if (isNotAvailableNotFound(template)) {
            tags.putAll(notFoundNotAvailableTags);
        }

        for (Language temp : owner.getLanguages()) {
            if (temp.getCode().equals(lang)) {
                model.put("language", temp);
                break;
            }
        }
        // Get scripts
        model.put("scripts", jsService.getList(owner));
        // Get jQuery (shared)
        model.put("sysScripts", jsService.getList());
        // Get plugins (shared)
        model.put("plugins", jsService.getPluginsList());
        // Get css files
        model.put("stylesheets", cssService.getList(owner));
        model.put("includes", cleanIncludesList(templatesService.getList(true, lang, owner)));
        model.put("tags", tags);
        model.put("contents", contents);
        model.put("template", template);
        model.put("owner", owner.getCode());
        getTemplateNameAndLevel(template, model);
        return new ModelAndView("edit_template", "model", model);
    }

    private boolean isLibrary(String template) {
        if (template.matches("^template_library.*")) {
            return true;
        }
        return false;
    }

    private boolean isCollection(String template) {
        if (template.matches("^template_collection.*")) {
            return true;
        }
        return false;
    }

    private boolean isShelf(String template) {
        if (template.matches("^template_shelf.*")) {
            return true;
        }
        return false;
    }

    private boolean isOther(String template) {
        if (template.matches("^template_other.*")) {
            return true;
        }
        return false;
    }

    private boolean isNotAvailableNotFound(String template) {
        if (template.matches("^template_not.*")) {
            return true;
        }
        return false;
    }

    private void getTemplateNameAndLevel(String template, Map<String, Object> model) {
        if (template.matches("^template_library\\.txt$")) {
            model.put("templateLevel", "library");
            model.put("templateName", "all libraries");
        } else if (template.matches("^template_collection\\.txt$")) {
            model.put("templateLevel", "collection");
            model.put("templateName", "all collections");
        } else if (template.matches("^template_shelf\\.txt$")) {
            model.put("templateLevel", "shelf");
            model.put("templateName", "all shelves");
        } else if (template.matches("^template_other_.+\\.txt$")) {
            model.put("templateLevel", "all levels");
            Pattern regex = Pattern.compile("^template_other_(.+)\\.txt$");
            Matcher m = regex.matcher(template);
            if (m.find()) {
                model.put("templateName", m.group(1).replaceAll("_", " "));
            }
        } else if (template.matches("^template_not_available\\.txt$")) {
            model.put("templateLevel", "all levels");
            model.put("templateName", "not available");
        } else if (template.matches("^template_not_found\\.txt$")) {
            model.put("templateLevel", "all levels");
            model.put("templateName", "not found");
        } else {
            Pattern regex = Pattern.compile("^template_(library|collection|shelf)_(.*)\\.txt$");
            Matcher m = regex.matcher(template);
            if (m.find()) {
                model.put("templateLevel", m.group(1));
                model.put("templateName", m.group(2).replaceAll("_", " "));
            }
        }
    }

    private List<String> cleanIncludesList(List<String> includes) {
        List<String> results = new ArrayList<String>();
        for (String temp : includes) {
            temp = temp.replaceAll("^template_other_", "");
            temp = temp.replaceAll("\\.txt$", "");
            temp = temp.replaceAll("_", " ");
            results.add(temp);
        }
        return results;
    }
}
