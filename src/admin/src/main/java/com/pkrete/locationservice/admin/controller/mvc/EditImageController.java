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
package com.pkrete.locationservice.admin.controller.mvc;

import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.exception.ObjectNotFoundException;
import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.service.ImagesService;
import com.pkrete.locationservice.admin.util.Settings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * The {@link EditImageController EditImageController} handles the requests that
 * are related to editing an image object and then saving it to the database.
 * This class extends abstract {@link BaseController BaseLocationController}
 * class.
 *
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/editimage.htm")
@SessionAttributes("image")
public class EditImageController extends BaseController {

    @Autowired
    @Qualifier("imagesService")
    private ImagesService imagesService;
    @Autowired
    @Qualifier("imageValidator")
    private Validator validator;
    @Autowired
    @Qualifier("converterService")
    private ConverterService converterService;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("image") Image image,
            BindingResult result) throws Exception {

        validator.validate(image, result);

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            model.put("external", image.getIsExternal());
            model.put("imagesPath", image.getIsExternal() ? "" : Settings.getInstance().getImagesWebPath(getOwner(request).getCode()));
            return new ModelAndView("edit_image", model);
        }

        String imageId = request.getParameter("imageId");

        image.setUpdater(getUser(request).getUsername());
        if (!imagesService.update(image)) {
            throw new Exception("Updating image failed.");
        }

        return new ModelAndView("redirect:illustrations.htm?select_image=" + imageId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initializeForm(HttpServletRequest request, ModelMap model) throws Exception {
        // Form backing object
        String id = request.getParameter("imageId");
        Owner owner = getOwner(request);
        Image img = imagesService.get(this.converterService.strToInt(id), owner);
        if (img == null) {
            throw new ObjectNotFoundException(new StringBuilder("Image not found. {\"id\":").append(id).append(",\"owner\":\"").append(owner.getCode()).append("\"}").toString());
        }
        if (img.getIsExternal()) {
            img.setUrl(img.getPath());
        }
        model.put("image", img);
        // Other reference data
        model.put("external", img.getIsExternal());
        model.put("imagesPath", img.getIsExternal() ? "" : Settings.getInstance().getImagesWebPath(getOwner(request).getCode()));
        return "edit_image";
    }
}
