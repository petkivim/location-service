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

import com.pkrete.locationservice.admin.exception.ObjectNotFoundException;
import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.util.Settings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * The {@link EditImageController EditImageController} handles the requests
 * that are related to editing an image object and then saving it to the database.
 * This class extends abstract {@link BaseController BaseLocationController} class.
 *
 * @author Petteri Kivimäki
 */
public class EditImageController extends BaseController {

    public EditImageController() {
        setCommandClass(Image.class);
        setCommandName("image");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Image image = (Image) command;
        String imageId = request.getParameter("imageId");

        image.setUpdater(getUser(request).getUsername());
        if(!imagesService.update(image)) {
            throw new Exception("Updating image failed.");
        }

        return new ModelAndView("redirect:illustrations.htm?select_image=" + imageId);
    }

    @Override
    protected java.util.Map referenceData(HttpServletRequest request) throws Exception {
        ModelMap modelMap = new ModelMap();
        String id = request.getParameter("imageId");
        Image img = imagesService.get(this.converterService.strToInt(id), getOwner(request));
        modelMap.put("external", img.getIsExternal());
        String path = "";
        if (!img.getIsExternal()) {
            path = Settings.getInstance().getImagesWebPath(getOwner(request).getCode());
        }
        modelMap.put("imagesPath", path);
        return modelMap;
    }

    @Override
    /* Initialize the command object */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String id = request.getParameter("imageId");
        Owner owner = getOwner(request);
        Image img = imagesService.get(this.converterService.strToInt(id), owner);
        if (img == null) {
            throw new ObjectNotFoundException(new StringBuilder("Image not found. {\"id\":").append(id).append(",\"owner\":\"").append(owner.getCode()).append("\"}").toString());
        }
        if (img.getIsExternal()) {
            img.setUrl(img.getPath());
        } else {
            //img.setFilePath(img.getPath());
        }
        return img;
    }
}
