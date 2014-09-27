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

import java.io.IOException;
import java.util.HashMap;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.service.ImagesService;
import com.pkrete.locationservice.admin.service.MapsService;
import com.pkrete.locationservice.admin.service.UsersService;
import com.pkrete.locationservice.admin.util.UsersUtil;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
@Controller
@RequestMapping("/illustrations.htm")
public class IllustrationController {

    @Autowired
    @Qualifier("imagesService")
    private ImagesService imagesService;
    @Autowired
    @Qualifier("mapsService")
    private MapsService mapsService;
    @Autowired
    @Qualifier("usersService")
    private UsersService usersService;
    @Autowired
    @Qualifier("converterService")
    private ConverterService converterService;
    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
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
