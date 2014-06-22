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

import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.model.location.Shelf;
import com.pkrete.locationservice.admin.model.user.User;
import com.pkrete.locationservice.admin.converter.ConverterService;
import com.pkrete.locationservice.admin.model.location.SimpleLocation;
import com.pkrete.locationservice.admin.model.user.UserGroup;
import com.pkrete.locationservice.admin.service.LocationsService;
import com.pkrete.locationservice.admin.service.UsersService;
import com.pkrete.locationservice.admin.util.UsersUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The {@LocationController LocationController} class returns the page that 
 * contains the list of all the locations in the LocationService or redirects/forwards
 * the user to another page. The function depends on the request parameters.
 * LocationController returns a model that contains all the objects that are
 * needed in the page that is shown to the user. LocationController implements 
 * the Controller interface.
 *
 * @author Petteri Kivimäki
 */
public class LocationController implements Controller {

    private LocationsService locationsService;
    private UsersService usersService;
    private ConverterService converterService;

    public LocationController() {
    }

    public void setLocationsService(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception, ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = usersService.getUser(request.getRemoteUser());
            session.setAttribute("user", user);
        }

        /**
         * If user is administrator and index parameter is present in the
         * URL, recreate search index.
         */
        if (request.isUserInRole(UserGroup.ADMIN.toString()) && request.getParameter("index") != null) {
            locationsService.recreateSearchIndex();
        }

        /* Id of the selected  library */
        String idLibrary = request.getParameter("select_library");
        /* Id of the selected collection */
        String idCollection = request.getParameter("select_collection");
        /* Id of the selected shelf */
        String idShelf = request.getParameter("select_shelf");

        if (idLibrary == null && idCollection != null) {
            int temp = this.locationsService.getLibraryId(idCollection);
            idLibrary = temp == 0 ? null : Integer.toString(temp);
        }
        if (idShelf != null) {
            if (idCollection == null) {
                int temp = this.locationsService.getCollectionId(idShelf);
                idCollection = temp == 0 ? null : Integer.toString(temp);
            }
            if (idLibrary == null) {
                int temp = this.locationsService.getLibraryId(idCollection);
                idLibrary = temp == 0 ? null : Integer.toString(temp);
            }
        }

        if (request.getParameter("btn_add_library") != null) {
            return new ModelAndView("redirect:addlibrary.htm");
        } else if (request.getParameter("btn_edit_library") != null && idLibrary != null) {
            return new ModelAndView("redirect:editlibrary.htm?select_library=" + idLibrary);
        } else if (request.getParameter("btn_delete_library") != null && idLibrary != null) {
            if (idLibrary != null) {
                Library temp = locationsService.getLibraryToBeDeleted(this.converterService.strToInt(idLibrary), user.getOwner());
                if (!locationsService.delete(temp)) {
                    throw new Exception("Deleting library failed.");
                }
                idLibrary = null;
                idCollection = null;
                idShelf = null;
            }
        } else if (request.getParameter("btn_add_collection") != null && idLibrary != null) {
            return new ModelAndView("redirect:addcollection.htm?select_library=" + idLibrary);
        } else if (request.getParameter("btn_edit_collection") != null && idCollection != null) {
            return new ModelAndView("redirect:editcollection.htm?select_library=" + idLibrary + "&select_collection=" + idCollection);
        } else if (request.getParameter("btn_delete_collection") != null && idCollection != null) {
            if (idCollection != null) {
                LibraryCollection temp = locationsService.getCollectionToBeDeleted(this.converterService.strToInt(idCollection), user.getOwner());
                if (!locationsService.delete(temp)) {
                    throw new Exception("Deleting collection failed.");
                }
                idCollection = null;
                idShelf = null;
            }
        } else if (request.getParameter("btn_add_shelf") != null && idCollection != null) {
            return new ModelAndView("redirect:addshelf.htm?select_library=" + idLibrary + "&select_collection=" + idCollection);
        } else if (request.getParameter("btn_edit_shelf") != null && idShelf != null) {
            return new ModelAndView("redirect:editshelf.htm?select_library=" + idLibrary + "&select_collection=" + idCollection + "&select_shelf=" + idShelf);
        } else if (request.getParameter("btn_delete_shelf") != null && idShelf != null) {
            if (idCollection != null) {
                Shelf temp = locationsService.getShelfToBeDeleted(this.converterService.strToInt(idShelf), user.getOwner());
                if (!locationsService.delete(temp)) {
                    throw new Exception("Deleting shelf failed.");
                }
                idShelf = null;
            }
        }

        /* Model that is returned together with the view */
        Map<String, Object> model = new HashMap<String, Object>();

        /* Load list of all the libraries that the user is allowed to see from DB */
        List<SimpleLocation> libraries = locationsService.getlLibraries(user.getOwner());
        List<SimpleLocation> collections = new ArrayList<SimpleLocation>();
        List<SimpleLocation> shelves = new ArrayList<SimpleLocation>();

        if (!libraries.isEmpty()) {
            /* If no library is selected, select the first library on the list */
            if (idLibrary == null) {
                idLibrary = Integer.toString(libraries.get(0).getLocationId());
            }

            /* Load the collections of the selected library */
            collections = locationsService.getCollectionsByLibraryId(this.converterService.strToInt(idLibrary), user.getOwner());

            if (!collections.isEmpty()) {
                /* If no collection is selected or selected library has changed, */
                /* select the first collection on the list */
                if (idCollection == null || request.getParameter("btn_list_library") != null) {
                    idCollection = Integer.toString(collections.get(0).getLocationId());
                }

                /* Load the shelves of the selected collection */
                shelves = locationsService.getShelvesByCollectionId(this.converterService.strToInt(idCollection), user.getOwner());
            }
        }
        model.put("owner", UsersUtil.getUser(request, usersService).getOwner().getCode());
        model.put("libraries", libraries);
        model.put("collections", collections);
        model.put("shelves", shelves);
        model.put("libraryId", idLibrary);
        model.put("collectionId", idCollection);
        model.put("shelfId", idShelf);

        if (request.isUserInRole(UserGroup.ADMIN.toString())) {
            model.put("isAdmin", "");
        }
        return new ModelAndView("location", "model", model);
    }
}
