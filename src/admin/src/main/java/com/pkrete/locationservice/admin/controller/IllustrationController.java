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

import java.io.IOException;
import java.util.HashMap;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.service.ImagesService;
import com.pkrete.locationservice.admin.service.MapsService;
import com.pkrete.locationservice.admin.service.UsersService;
import com.pkrete.locationservice.admin.util.UsersUtil;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;

/**
 * The {@IllustrationController IllustrationController} class returns the page that
 * contains the list of all the maps and images in the LocationService or redirects/forwards
 * the user to another page. The function depends on the request parameters.
 * IllustrationController returns a model that contains all the objects that are
 * needed in the page that is shown to the user. IllustrationController implements
 * the Controller interface.
 *
 * @author Petteri Kivimäki
 */
public class IllustrationController implements Controller {

    protected ImagesService imagesService;
    protected MapsService mapsService;
    private UsersService usersService;
    private ConverterService converterService;
    private MessageSource messageSource;

    public IllustrationController() {
    }

    public void setImagesService(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    public void setMapsService(MapsService mapsService) {
        this.mapsService = mapsService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Owner owner = UsersUtil.getUser(request, usersService).getOwner();
        /* Model that is returned together with the view */
        java.util.Map<String, Object> model = new HashMap<String, Object>();
        String imageId = request.getParameter("select_image");
        String mapId = request.getParameter("select_map");

        if (request.getParameter("btn_add_img") != null) {
            return new ModelAndView("redirect:addimage.htm");
        } else if (request.getParameter("btn_edit_img") != null && imageId != null) {
            return new ModelAndView("redirect:editimage.htm?imageId=" + imageId);
        } else if (request.getParameter("btn_delete_img") != null && imageId != null) {
            if (!imagesService.delete(this.converterService.strToInt(imageId), owner)) {
                model.put("errorMsgImg", this.messageSource.getMessage("error.image.delete", null, null));
            }
        } else if (request.getParameter("btn_add_map") != null) {
            return new ModelAndView("redirect:addmap.htm");
        } else if (request.getParameter("btn_edit_map") != null && mapId != null) {
            return new ModelAndView("redirect:editmap.htm?mapId=" + mapId);
        } else if (request.getParameter("btn_delete_map") != null && mapId != null) {
            if (!mapsService.delete(this.converterService.strToInt(mapId), owner)) {
                model.put("errorMsgMap", this.messageSource.getMessage("error.map.delete", null, null));
            }
        }

        if (request.isUserInRole(UserGroup.ADMIN.toString())) {
            model.put("isAdmin", "");
        }

        model.put("images", imagesService.get(owner));
        model.put("maps", mapsService.get(owner));
        return new ModelAndView("illustration", "model", model);
    }
}
