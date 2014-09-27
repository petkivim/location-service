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

import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.service.ImagesService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;

/**
 * The {@link AddImageController AddImageController} handles the requests
 * that are related to adding a new image to the database. This class extends
 * abstract {@link BaseController BaseLocationController} class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/addimage.htm")
public class AddImageController extends BaseController {

    @Autowired
    @Qualifier("imagesService")
    private ImagesService imagesService;
    @Autowired
    @Qualifier("imageValidator")
    private Validator validator;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("image") Image image,
            BindingResult result) throws Exception {
        Owner owner = getOwner(request);
        // Set creator
        image.setCreator(getUser(request).getUsername());
        // Set owner
        image.setOwner(owner);

        validator.validate(image, result);

        if (result.hasErrors()) {
            ModelMap modelMap = new ModelMap();
            modelMap.put("files", imagesService.getAdminImages(owner));
            return new ModelAndView("add_image", modelMap);
        }
        // Create new image
        if (!imagesService.create(image)) {
            throw new Exception("Creating image failed.");
        }
        return new ModelAndView("redirect:illustrations.htm");
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) {
        Image img = new Image();
        img.setOwner(getOwner(request));
        model.put("image", img);
        model.put("files", imagesService.getAdminImages(getOwner(request)));
        return "add_image";
    }
}
