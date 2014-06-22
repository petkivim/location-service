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
package com.pkrete.locationservice.admin.controller.rest.v1;

import com.pkrete.locationservice.admin.exception.OperationFailedException;
import com.pkrete.locationservice.admin.exception.ResourceNotFoundException;
import com.pkrete.locationservice.admin.exception.ValidationException;
import com.pkrete.locationservice.admin.model.location.Library;
import com.pkrete.locationservice.admin.model.location.LibraryCollection;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.service.LocationsService;
import com.pkrete.locationservice.admin.converter.ObjectMapService;
import com.pkrete.locationservice.admin.model.location.Shelf;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.Validator;

/**
 * This class provides REST API to Locations objects which include Library,
 * LibraryCollection and Shelf objects. This class implements
 * create, read, update, delete and list all locations functions.
 * 
 * INDEX    /locations/libraries         [GET]   
 * CREATE   /locations/libraries         [POST]
 * READ     /locations/libraries/{libId} [GET]
 * UPDATE   /locations/libraries/{libId} [PUT]
 * DELETE   /locations/libraries/{libId} [DELETE]
 * 
 * INDEX    /locations/libraries/{libId}/collections         [GET]   
 * CREATE   /locations/libraries/{libId}/collections         [POST]
 * READ     /locations/libraries/{libId}/collections/{colId} [GET]
 * UPDATE   /locations/libraries/{libId}/collections/{colId} [PUT]
 * DELETE   /locations/libraries/{libId}/collections/{colId} [DELETE]
 * 
 * INDEX    /locations/libraries/{libId}/collections/{colId}/shelves           [GET]   
 * CREATE   /locations/libraries/{libId}/collections/{colId}/shelves           [POST]
 * READ     /locations/libraries/{libId}/collections/{colId}/shelves/{shelfId} [GET]
 * UPDATE   /locations/libraries/{libId}/collections/{colId}/shelves/{shelfId} [PUT]
 * DELETE   /locations/libraries/{libId}/collections/{colId}/shelves/{shelfId} [DELETE]
 * 
 * @author Petteri Kivimäki
 */
@Controller
@RequestMapping("/locations")
public class LocationsRestController extends RestController {

    private final static Logger logger = Logger.getLogger(LocationsRestController.class.getName());
    @Autowired
    @Qualifier("simpleLocationMapService")
    private ObjectMapService simpleLocationMapConverter;
    @Autowired
    @Qualifier("libraryMapService")
    private ObjectMapService libMapConverter;
    @Autowired
    @Qualifier("collectionMapService")
    private ObjectMapService colMapConverter;
    @Autowired
    @Qualifier("shelfMapService")
    private ObjectMapService shelfMapConverter;
    @Autowired
    @Qualifier("locationsService")
    private LocationsService locationsService;
    @Autowired
    @Qualifier("libraryValidator")
    private Validator libValidator;
    @Autowired
    @Qualifier("collectionValidator")
    private Validator colValidator;
    @Autowired
    @Qualifier("shelfValidator")
    private Validator shelfValidator;

    @RequestMapping(value = "/libraries", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List indexLib(HttpServletRequest request) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get list of librariess related to the Owner
        List libraries = locationsService.getlLibraries(owner);
        // Return the list
        return this.simpleLocationMapConverter.convert(libraries);
    }

    @RequestMapping(value = "/libraries/{libId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map getLib(HttpServletRequest request, HttpServletResponse response, @PathVariable int libId) throws ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get library matching the given id and Owner
        Library lib = this.locationsService.getLibrary(libId, owner);
        // Check for null value
        if (lib == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Return the library
        return this.libMapConverter.convert(lib);
    }

    @RequestMapping(value = "/libraries/{libId}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map deleteLib(HttpServletRequest request, HttpServletResponse response, @PathVariable int libId) throws OperationFailedException, ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get library matching the given id and Owner
        Library lib = this.locationsService.getLibraryToBeDeleted(libId, owner);
        // Check for null value
        if (lib == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Try to delete the library
        if (!this.locationsService.delete(lib)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "library");
        // Add id
        result.put("resource_id", libId);
        // Return result
        return result;
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "/libraries", method = RequestMethod.POST, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map createLib(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Library library, BindingResult results) throws ValidationException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Set owner to the Library
        library.setOwner(owner);
        // Set create operator
        library.setCreator(operator);

        // Validate Library object
        this.libValidator.validate(library, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.create.failed"), results);
        }

        // Try to create a new Library
        if (!this.locationsService.create(library)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.create.failed"));
        }

        // Return the created Library
        return this.libMapConverter.convert(library);
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "/libraries/{libId}", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map updateLib(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Library library, BindingResult results, @PathVariable int libId) throws ValidationException, OperationFailedException, ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Get Library matching the given id and Owner
        Library oldLibrary = this.locationsService.getLibraryToBeDeleted(libId, owner);
        // Check for null value
        if (oldLibrary == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Set owner to the Library
        library.setOwner(owner);
        // Set update operator
        library.setUpdater(operator);
        // Set id
        library.setLocationId(libId);
        // Set creator
        library.setCreator(oldLibrary.getCreator());
        // Set created at
        library.setCreated(oldLibrary.getCreated());

        // Validate Library object
        this.libValidator.validate(library, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // Try to update the Library
        if (!this.locationsService.update(library)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Return the updated Library in JSON
        return this.libMapConverter.convert(library);
    }

    @RequestMapping(value = "/libraries/{libId}/collections", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List indexCol(HttpServletRequest request, @PathVariable int libId) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get list of collections related to the library
        List collections = this.locationsService.getCollectionsByLibraryId(libId, owner);
        // Return the list
        return this.simpleLocationMapConverter.convert(collections);
    }

    @RequestMapping(value = "/libraries/{libId}/collections/{colId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map getCol(HttpServletRequest request, HttpServletResponse response, @PathVariable int libId, @PathVariable int colId) throws ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get collection matching the given id and Owner
        LibraryCollection col = this.locationsService.getCollection(colId, libId, owner);
        // Check for null value
        if (col == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Return the collection
        return this.colMapConverter.convert(col);
    }

    @RequestMapping(value = "/libraries/{libId}/collections/{colId}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map deleteCol(HttpServletRequest request, HttpServletResponse response, @PathVariable int libId, @PathVariable int colId) throws OperationFailedException, ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get collection matching the given id and Owner
        LibraryCollection col = this.locationsService.getCollectionToBeDeleted(colId, libId, owner);
        // Check for null value
        if (col == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Try to delete the collection
        if (!this.locationsService.delete(col)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "collection");
        // Add id
        result.put("resource_id", colId);
        // Return result
        return result;
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "/libraries/{libId}/collections", method = RequestMethod.POST, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map createCol(HttpServletRequest request, HttpServletResponse response,
            @RequestBody LibraryCollection collection, BindingResult results, @PathVariable int libId) throws ValidationException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Get library matching the given id and Owner
        Library lib = this.locationsService.getLibrary(libId, owner);
        // Set Library
        collection.setLibrary(lib);
        // Set owner to the LibraryCollection
        collection.setOwner(owner);
        // Set create operator
        collection.setCreator(operator);

        // Validate LibraryCollection object
        this.colValidator.validate(collection, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.create.failed"), results);
        }

        // Try to create a new LibraryCollection
        if (!this.locationsService.create(collection)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.create.failed"));
        }

        // Return the created LibraryCollection
        return this.colMapConverter.convert(collection);
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "/libraries/{libId}/collections/{colId}", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map updateCol(HttpServletRequest request, HttpServletResponse response,
            @RequestBody LibraryCollection collection, BindingResult results, @PathVariable int libId, @PathVariable int colId) throws ValidationException, OperationFailedException, ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");
        // Get collection matching the given id and Owner
        LibraryCollection oldCollection = this.locationsService.getCollection(colId, libId, owner);
        // Check for null value
        if (oldCollection == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // If Library is set and locationId is not 0, use the locationId of the Library
        if (collection.getLibrary() != null && collection.getLibrary().getLocationId() != 0) {
            // Change library's id
            libId = collection.getLibrary().getLocationId();
        }
        // Get library matching the given id and Owner
        Library lib = this.locationsService.getLibrary(libId, owner);
        // Set Library
        collection.setLibrary(lib);
        // Set owner to the collection
        collection.setOwner(owner);
        // Set update operator
        collection.setUpdater(operator);
        // Set id
        collection.setLocationId(colId);
        // Set creator
        collection.setCreator(oldCollection.getCreator());
        // Set created at
        collection.setCreated(oldCollection.getCreated());

        // Validate LibraryCollection object
        this.colValidator.validate(collection, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // Try to update the LibraryCollection
        if (!this.locationsService.update(collection)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Return the updated LibraryCollection in JSON
        return this.colMapConverter.convert(collection);
    }

    @RequestMapping(value = "/libraries/{libId}/collections/{colId}/shelves", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List indexShelf(HttpServletRequest request, @PathVariable int libId, @PathVariable int colId) {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get list of shelves related to the library and collection
        List shelves = this.locationsService.getShelvesByCollectionId(libId, colId, owner);
        // Return the list
        return this.simpleLocationMapConverter.convert(shelves);
    }

    @RequestMapping(value = "/libraries/{libId}/collections/{colId}/shelves/{shelfId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map getShelf(HttpServletRequest request, HttpServletResponse response, @PathVariable int libId, @PathVariable int colId, @PathVariable int shelfId) throws ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get shelf the given id and Owner
        Shelf shelf = this.locationsService.getShelf(shelfId, colId, libId, owner);
        // Check for null value
        if (shelf == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Return the shelf
        return this.shelfMapConverter.convert(shelf);
    }

    @RequestMapping(value = "/libraries/{libId}/collections/{colId}/shelves/{shelfId}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map deleteShelf(HttpServletRequest request, HttpServletResponse response, @PathVariable int libId, @PathVariable int colId, @PathVariable int shelfId) throws OperationFailedException, ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get shelf the given id and Owner
        Shelf shelf = this.locationsService.getShelfToBeDeleted(shelfId, colId, libId, owner);
        // Check for null value
        if (shelf == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // Try to delete the shelf
        if (!this.locationsService.delete(shelf)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.delete.failed"));
        }
        // Create Map containing the message
        Map result = new HashMap();
        // Add message
        result.put("message", getMessage("rest.delete.success"));
        // Add resource type
        result.put("resource_type", "shelf");
        // Add id
        result.put("resource_id", shelfId);
        // Return result
        return result;
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "/libraries/{libId}/collections/{colId}/shelves", method = RequestMethod.POST, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map createShelf(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Shelf shelf, BindingResult results, @PathVariable int libId, @PathVariable int colId) throws ValidationException, OperationFailedException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");

        // Get collection matching the given id and Owner
        LibraryCollection col = this.locationsService.getCollection(colId, libId, owner);
        // Set LibraryCollection
        shelf.setCollection(col);
        // Set owner to the Shelf
        shelf.setOwner(owner);
        // Set create operator
        shelf.setCreator(operator);

        // Validate Shelf object
        this.shelfValidator.validate(shelf, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.create.failed"), results);
        }

        // Try to create a new Shelf
        if (!this.locationsService.create(shelf)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.create.failed"));
        }

        // Return the created Shelf
        return this.shelfMapConverter.convert(shelf);
    }

    // The request must have the following headers: Content-Type: application/json, Accept: application/json
    @RequestMapping(value = "/libraries/{libId}/collections/{colId}/shelves/{shelfId}", method = RequestMethod.PUT, consumes = "application/json; charset=utf-8", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map updateShelf(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Shelf shelf, BindingResult results, @PathVariable int libId, @PathVariable int colId, @PathVariable int shelfId) throws ValidationException, OperationFailedException, ResourceNotFoundException {
        // Get Owner object related to the user
        Owner owner = (Owner) request.getAttribute("owner");
        // Get operator id
        String operator = (String) request.getAttribute("operator");
        // Get Shelf matching the given id and Owner
        Shelf oldShelf = this.locationsService.getShelf(shelfId, colId, libId, owner);
        // Check for null value
        if (oldShelf == null) {
            // Throw exception
            throw new ResourceNotFoundException(getMessage("rest.resource.not.exist"));
        }
        // If LibraryCollection is set and locationId is not 0, use the locationId of the LibraryCollection
        if (shelf.getCollection() != null && shelf.getCollection().getLocationId() != 0) {
            // Change collection's id
            colId = shelf.getCollection().getLocationId();
        }
        // Get collection matching the given id and Owner
        LibraryCollection col = this.locationsService.getCollection(colId, owner);
        // Set collection
        shelf.setCollection(col);
        // Set owner to the shelf
        shelf.setOwner(owner);
        // Set update operator
        shelf.setUpdater(operator);
        // Set id
        shelf.setLocationId(shelfId);
        // Set creator
        shelf.setCreator(oldShelf.getCreator());
        // Set created at
        shelf.setCreated(oldShelf.getCreated());

        // Validate Shelf object
        this.shelfValidator.validate(shelf, results);

        // Check for errors
        if (results.hasErrors()) {
            // Throw exception
            throw new ValidationException(getMessage("rest.update.failed"), results);
        }

        // Try to update the Shelf
        if (!this.locationsService.update(shelf)) {
            // Throw exception
            throw new OperationFailedException(getMessage("rest.update.failed"));
        }

        // Return the updated Shelf in JSON
        return this.shelfMapConverter.convert(shelf);
    }
}
