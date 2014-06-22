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
package com.pkrete.locationservice.admin.controller;

import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.io.CSSService;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.io.JSService;
import com.pkrete.locationservice.admin.service.LocationsService;
import com.pkrete.locationservice.admin.io.TemplatesService;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.service.UsersService;
import com.pkrete.locationservice.admin.util.CSSUtil;
import com.pkrete.locationservice.admin.util.JSUtil;
import com.pkrete.locationservice.admin.util.TemplatesUtil;
import com.pkrete.locationservice.admin.util.UsersUtil;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;

/**
 * The {@TemplateController TemplateController} class returns the page that
 * contains the list of all the templates in the LocationService and offers
 * the necessary functionality for adding, modifying and deleteting them.
 * The function depends on the request parameters. TemplateController returns
 * a model that contains all the objects that are needed in the page that
 * is shown to the user.
 *
 * @author Petteri Kivimäki
 */
public class TemplateController implements Controller {

    private UsersService usersService;
    private LocationsService locationsService;
    private TemplatesService templatesService;
    private CSSService cssService;
    private JSService jsService;
    private ConverterService converterService;
    private MessageSource messageSource;

    public TemplateController() {
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public void setLocationsService(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    public void setTemplatesService(TemplatesService templatesService) {
        this.templatesService = templatesService;
    }

    public void setCssService(CSSService cssService) {
        this.cssService = cssService;
    }

    public void setJsService(JSService jsService) {
        this.jsService = jsService;
    }

    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /* Get the current user. */
        Owner owner = UsersUtil.getUser(request, usersService).getOwner();
        /* Model that is returned together with the view */
        Map<String, Object> model = new HashMap<String, Object>();

        // Currently selected language
        String lang = request.getParameter("select_lang");
        // Currently selected template
        String template = request.getParameter("template");
        // Id of the location that's used in template generation
        int locationId = this.converterService.strToInt(request.getParameter("location_id"));
        // Template name, if type is OTHER
        String otherName = request.getParameter("other_name");
        // Include collection code to template's name if collection code exists
        String colCodeStr = request.getParameter("inc_col_code");
        boolean incCollectionCode = (colCodeStr != null && colCodeStr.equalsIgnoreCase("true") ? true : false);
        // Current template name, used when renaming templates
        String templateOld = request.getParameter("template_old");
        // Template type
        String templateType = request.getParameter("template_type");
        // Currently selected stylesheet
        String stylesheet = request.getParameter("stylesheet");
        // New stylesheet's name
        String stylesheetNew = request.getParameter("css_new");
        // Currently selected JavaScript file
        String js = request.getParameter("js");
        // New JavaScript file's name
        String jsNew = request.getParameter("js_new");

        if (request.getParameter("btn_add_template") != null) {
            String templateNew = templatesService.buildTemplateName(locationId, otherName, incCollectionCode, TemplatesUtil.getTemplateType(templateType), owner);
            if (lang != null && !lang.isEmpty()) {
                if (TemplatesUtil.isTemplateNameValid(templateNew)) {
                    if (!templatesService.exists(templateNew, lang, owner)) {
                        if (templateOld.isEmpty()) {
                            if (templatesService.create(templateNew, lang, owner)) {
                                Object[] args = new Object[]{templateNew};
                                model.put("responseMsgTemplate", this.messageSource.getMessage("response.template.added", args, null));
                            } else {
                                model.put("errorMsgTemplate", this.messageSource.getMessage("error.template.name.notvalid", null, null));
                            }
                        } else {
                            if (templatesService.rename(templateOld, templateNew, lang, owner)) {
                                Object[] args = new Object[]{templateOld, templateNew};
                                model.put("responseMsgTemplate", this.messageSource.getMessage("response.template.renamed", args, null));
                            } else {
                                Object[] args = new Object[]{templateOld};
                                model.put("errorMsgTemplate", this.messageSource.getMessage("error.template.rename", args, null));
                            }
                        }
                    } else {
                        model.put("errorMsgTemplate", this.messageSource.getMessage("error.template.exist", null, null));
                    }
                } else {
                    model.put("errorMsgTemplate", this.messageSource.getMessage("error.template.name.notvalid", null, null));
                }
            } else {
                model.put("errorMsgTemplate", this.messageSource.getMessage("error.template.no.lang", null, null));
            }
        } else if (request.getParameter("btn_edit_template") != null) {
            return new ModelAndView("redirect:edittemplate.htm?template=" + URLEncoder.encode(template, "UTF-8") + "&lang=" + lang);
        } else if (request.getParameter("btn_delete_template") != null) {
            if (templatesService.delete(template, lang, owner)) {
                Object[] args = new Object[]{template};
                model.put("responseMsgTemplate", this.messageSource.getMessage("response.template.deleted", args, null));
            } else {
                Object[] args = new Object[]{template};
                model.put("errorMsgTemplate", this.messageSource.getMessage("error.template.delete", args, null));
            }
        } else if (request.getParameter("btn_add_css") != null) {
            stylesheetNew += ".css";
            if (CSSUtil.isCssNameValid(stylesheetNew)) {
                if (!cssService.exists(stylesheetNew, owner)) {
                    if (cssService.create(stylesheetNew, owner)) {
                        Object[] args = new Object[]{stylesheetNew};
                        model.put("responseMsgCss", this.messageSource.getMessage("response.css.added", args, null));
                    } else {
                        model.put("errorMsgCss", this.messageSource.getMessage("error.css.name.notvalid", null, null));
                    }
                } else {
                    model.put("errorMsgCss", this.messageSource.getMessage("error.css.exist", null, null));
                }
            } else {
                model.put("errorMsgCss", this.messageSource.getMessage("error.css.name.notvalid", null, null));
            }
        } else if (request.getParameter("btn_edit_css") != null) {
            return new ModelAndView("redirect:editcss.htm?stylesheet=" + stylesheet + "&lang=" + lang);
        } else if (request.getParameter("btn_delete_css") != null) {
            if (cssService.delete(stylesheet, owner)) {
                Object[] args = new Object[]{stylesheet};
                model.put("responseMsgCss", this.messageSource.getMessage("response.css.deleted", args, null));
            } else {
                Object[] args = new Object[]{stylesheet};
                model.put("errorMsgCss", this.messageSource.getMessage("error.css.delete", args, null));
            }
        } else if (request.getParameter("btn_edit_sys_css") != null) {
            return new ModelAndView("redirect:editcss.htm?lang=" + lang + "&sys=true&stylesheet=style.css");
        } else if (request.getParameter("btn_edit_sys_template") != null) {
            return new ModelAndView("redirect:edittemplate.htm?template=template.txt&sys=true&lang=" + lang);
        } else if (request.getParameter("btn_add_js") != null) {
            jsNew += ".js";
            if (JSUtil.isJsNameValid(jsNew)) {
                if (!jsService.exists(jsNew, owner)) {
                    if (jsService.create(jsNew, owner)) {
                        Object[] args = new Object[]{jsNew};
                        model.put("responseMsgJs", this.messageSource.getMessage("response.js.added", args, null));
                    } else {
                        model.put("errorMsgJs", this.messageSource.getMessage("error.js.name.notvalid", null, null));
                    }
                } else {
                    model.put("errorMsgJs", this.messageSource.getMessage("error.js.exist", null, null));
                }
            } else {
                model.put("errorMsgJs", this.messageSource.getMessage("error.js.name.notvalid", null, null));
            }
        } else if (request.getParameter("btn_edit_js") != null) {
            return new ModelAndView("redirect:editjs.htm?js=" + js + "&lang=" + lang);
        } else if (request.getParameter("btn_delete_js") != null) {
            if (jsService.delete(js, owner)) {
                Object[] args = new Object[]{js};
                model.put("responseMsgJs", this.messageSource.getMessage("response.js.deleted", args, null));
            } else {
                Object[] args = new Object[]{js};
                model.put("errorMsgJs", this.messageSource.getMessage("error.js.delete", args, null));
            }
        }

        List<Language> languages = owner.getLanguages();

        if (lang != null) {
            model.put("templates", templatesService.getMap(lang, owner));
            for (Language temp : languages) {
                if (temp.getCode().equals(lang)) {
                    model.put("language", temp);
                    break;
                }
            }
        } else if (!languages.isEmpty()) {
            model.put("templates", templatesService.getMap(languages.get(0).getCode(), owner));
            model.put("language", languages.get(0));
        }

        if (request.isUserInRole(UserGroup.ADMIN.toString())) {
            model.put("isAdmin", "");
        }
        model.put("owner", owner.getCode());
        model.put("locations", locationsService.getlLibraries(owner));
        model.put("scripts", jsService.getList(owner));
        model.put("stylesheets", cssService.getList(owner));
        model.put("languages", languages);
        return new ModelAndView("template", "model", model);
    }
}
