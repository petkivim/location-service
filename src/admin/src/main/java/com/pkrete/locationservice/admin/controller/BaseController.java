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

import com.pkrete.locationservice.admin.editor.ImageEditor;
import com.pkrete.locationservice.admin.editor.LanguageEditor;
import com.pkrete.locationservice.admin.editor.MapEditor;
import com.pkrete.locationservice.admin.editor.OwnerEditor;
import com.pkrete.locationservice.admin.editor.SubjectMatterEditor;
import com.pkrete.locationservice.admin.model.illustration.Image;
import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.illustration.Map;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.service.ImagesService;
import com.pkrete.locationservice.admin.service.LanguagesService;
import com.pkrete.locationservice.admin.service.LocationsService;
import com.pkrete.locationservice.admin.service.MapsService;
import com.pkrete.locationservice.admin.service.SubjectMattersService;
import com.pkrete.locationservice.admin.service.UsersService;
import com.pkrete.locationservice.admin.util.UsersUtil;
import org.springframework.web.servlet.mvc.SimpleFormController;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;

/**
 * The {@link BaseController BaseController} class implements
 * some basic functionality that is shared by all the controllers used for
 * form processing. This is an abstract base class that all the controllers of the
 * application can extend. This class doesn't have any abstract methods
 * though.
 *
 * @author Petteri Kivimäki
 */
public abstract class BaseController extends SimpleFormController {

    protected LocationsService locationsService;
    protected ImagesService imagesService;
    protected MapsService mapsService;
    protected SubjectMattersService subjectMattersService;
    protected UsersService usersService;
    protected LanguagesService languagesService;
    protected ConverterService converterService;
    private ImageEditor imageEditor;
    private MapEditor mapEditor;
    private LanguageEditor languageEditor;
    private SubjectMatterEditor subjectMatterEditor;
    private OwnerEditor ownerEditor;

    public void setLocationsService(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    public void setImagesService(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    public void setMapsService(MapsService mapsService) {
        this.mapsService = mapsService;
    }

    public void setSubjectMattersService(SubjectMattersService subjectMattersService) {
        this.subjectMattersService = subjectMattersService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public void setLanguagesService(LanguagesService languagesService) {
        this.languagesService = languagesService;
    }

    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    public void setImageEditor(ImageEditor imageEditor) {
        this.imageEditor = imageEditor;
    }

    public void setMapEditor(MapEditor mapEditor) {
        this.mapEditor = mapEditor;
    }

    public void setLanguageEditor(LanguageEditor languageEditor) {
        this.languageEditor = languageEditor;
    }

    public void setSubjectMatterEditor(SubjectMatterEditor subjectMatterEditor) {
        this.subjectMatterEditor = subjectMatterEditor;
    }

    public void setOwnerEditor(OwnerEditor ownerEditor) {
        this.ownerEditor = ownerEditor;
    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Image.class, imageEditor);
        binder.registerCustomEditor(Map.class, mapEditor);
        binder.registerCustomEditor(Language.class, languageEditor);
        binder.registerCustomEditor(SubjectMatter.class, subjectMatterEditor);
        binder.registerCustomEditor(Owner.class, ownerEditor);
    }

    protected User getUser(HttpServletRequest request) {
        return UsersUtil.getUser(request, usersService);
    }

    protected Owner getOwner(HttpServletRequest request) {
        return UsersUtil.getUser(request, usersService).getOwner();
    }
}
