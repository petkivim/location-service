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
package com.pkrete.locationservice.admin.service.owners;

import com.pkrete.locationservice.admin.dao.OwnersDao;
import com.pkrete.locationservice.admin.model.owner.CallnoModification;
import com.pkrete.locationservice.admin.model.owner.NotFoundRedirect;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.owner.PreprocessingRedirect;
import com.pkrete.locationservice.admin.io.DirectoryService;
import com.pkrete.locationservice.admin.converter.JSONizerService;
import com.pkrete.locationservice.admin.service.OwnersService;
import com.pkrete.locationservice.admin.util.Settings;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements {@link OwnersService OwnersService} interface, which
 * defines service layer for Owner objects.
 *
 * This class offers methods for adding, editing and removing Owner objects.
 * Operations related to Owner objects require both database and file system
 * operations, and single method may include multiple database and/or file
 * system calls. This class doesn't implement the operations, but it delegates
 * them to other objects.
 *
 * @author Petteri Kivimäki
 */
public class OwnersServiceImpl implements OwnersService {

    private final static Logger logger = LoggerFactory.getLogger(OwnersServiceImpl.class.getName());
    private OwnersDao dao;
    private DirectoryService dirService;
    private JSONizerService jsonizer;
    private Map<String, Boolean> ignoredFiles;

    /**
     * Sets the data access object.
     *
     * @param dao new value
     */
    public void setDao(OwnersDao dao) {
        this.dao = dao;
    }

    /**
     * Sets the directory service object.
     *
     * @param dirService new value
     */
    public void setDirService(DirectoryService dirService) {
        this.dirService = dirService;
    }

    /**
     * Sets the JSON converter object.
     *
     * @param dao new value
     */
    public void setJsonizer(JSONizerService jsonizer) {
        this.jsonizer = jsonizer;
    }

    /**
     * Sets the ignored files list.
     *
     * @param ignoredFiles new list
     */
    public void setIgnoredFiles(Map<String, Boolean> ignoredFiles) {
        this.ignoredFiles = ignoredFiles;
    }

    /**
     * Returns a list of all the owners in the database.
     *
     * @return all the owners in the database
     */
    @Override
    public List<Owner> getOwners() {
        return dao.getOwners();
    }

    /**
     * Returns the owner with the given id.
     *
     * @param id the id that is used for searching
     * @return the owner with the given id
     */
    @Override
    public Owner getOwner(int id) {
        Owner owner = dao.getOwner(id);
        if (owner != null) {
            owner.setCodePrevious(owner.getCode());
        }
        return owner;
    }

    /**
     * Returns the owner with the given id with all the collections related to
     * the owner loaded.
     *
     * @param id the id that is used for searching
     * @return the owner with the given id
     */
    @Override
    public Owner getFullOwner(int id) {
        Owner owner = dao.getFullOwner(id);
        if (owner != null) {
            owner.setCodePrevious(owner.getCode());
        }
        return owner;
    }

    /**
     * Returns the owner with the given code.
     *
     * @param code the code that is used for searching
     * @return the owner with the given code
     */
    @Override
    public Owner getOwnerByCode(String code) {
        Owner owner = dao.getOwnerByCode(code);
        if (owner != null) {
            owner.setCodePrevious(owner.getCode());
        }
        return owner;
    }

    /**
     * Checks if the owner object corresponding the give id can be removed from
     * the database.
     *
     * @param id the id number of the owner to be removed
     * @return true if the owner object can be removed, otherwise returns false
     */
    @Override
    public boolean canBeDeleted(Owner owner) {
        // Get list of all owner specific directories
        List<File> dirs = Settings.getInstance().getOwnerDirs(owner.getCode());

        // Go through the directories and check that they are empty
        for (File dir : dirs) {
            if (!this.dirService.isEmpty(dir.getAbsolutePath(), this.ignoredFiles)) {
                // Directory is not empty, there's no need to continue
                return false;
            }
        }
        return dao.canBeDeleted(owner);
    }

    /**
     * Add the given owner object to the database and creates all the owner
     * specific folders.
     *
     * @param owner the owner to be added
     * @return true if and only if all the folders were successfully created and
     * the object saved to the database; otherwise false
     */
    @Override
    public boolean create(Owner owner) {
        // Get list of all owner specific directories
        List<File> dirs = Settings.getInstance().getOwnerDirs(owner.getCode());

        // Create folders for the new owner
        List<String> createdDirs = new ArrayList<String>();
        boolean success = true;
        for (File dir : dirs) {
            if (!this.dirService.add(dir.getAbsolutePath())) {
                success = false;
                break;
            }
            createdDirs.add(dir.getAbsolutePath());
        }
        // Save the owner to the DB if all the directories were
        // succesfully created
        if (success) {
            // Set created date
            owner.setCreated(new Date());
            if (dao.create(owner)) {
                logger.info("Owner created : {}", this.jsonizer.jsonize(owner, true));
                // Add to external index
                this.addToIndex(owner);
                return true;
            }
            logger.warn("Failed to create owner : {}", this.jsonizer.jsonize(owner, true));
        } else {
            logger.warn("Creating new owner directories failed -> ROLLBACK.");
        }
        logger.warn("Delete all the previously created directories.");
        // Do rollback if all the directories were not created.
        // Go through all the dirs and try to delete them.
        boolean deleted = true;
        for (String path : createdDirs) {
            // Directory must be empty
            if (!this.dirService.delete(path, true)) {
                deleted = false;
            }
        }
        if (!deleted) {
            logger.warn("Failed to delete all the previously created owner directories.");
        } else {
            logger.info("Succesfully deleted all the previously created owner directories.");
        }
        return false;
    }

    /**
     * Updates the given owner object to the database. If the owner code has
     * changed, all the owner directory names are updated too.
     *
     * @param owner the owner to be updated
     * @return true if and only the object and the directories were successfully
     * updated; otherwise false
     */
    @Override
    public boolean update(Owner owner) {
        // Get owner code's post modify value
        String codePrev = owner.getCodePrevious();
        // Boolean variable that tells if the owner code has changed
        boolean hasChanged = false;
        // Check if the current and previous value are not equal
        if (!owner.getCode().equals(codePrev)) {
            logger.debug("Owner's code changed -> update directories : \"{}\" -> \"{}\"", codePrev, owner.getCode());
            hasChanged = true;
            // Get current path
            String oldPath = Settings.getInstance().getOwnersPath() + codePrev;
            // Get new path
            String newPath = Settings.getInstance().getOwnersPath() + owner.getCode();
            // Try to rename directory
            if (this.dirService.rename(oldPath, newPath)) {
                // Get current admin path
                String oldPathAdmin = Settings.getInstance().getOwnersPathAdmin() + codePrev;
                // Get new admin path
                String newPathAdmin = Settings.getInstance().getOwnersPathAdmin() + owner.getCode();
                // If renaming admin directory fails, undo earlier change
                if (!this.dirService.rename(oldPathAdmin, newPathAdmin)) {
                    logger.warn("Renaming owner directories failed -> ROLLBACK.");
                    logger.warn("Undo all the changes.");
                    if (this.dirService.rename(newPath, oldPath)) {
                        logger.info("Succesfully restored all the previously renamed owner directories.");
                    } else {
                        logger.warn("Failed to restore all the previously renamed owner directories.");
                    }
                    return false;
                }
            } else {
                logger.warn("Renaming owner directory failed -> EXIT.");
                return false;
            }
        }
        // Remove empty redirects
        this.removeUnusedRedirects(owner);
        // Set updated date
        owner.setUpdated(new Date());
        // Try to update the DB
        if (dao.update(owner)) {
            // Remove orphan redirects
            this.dao.deleteOrphanRedirects();
            logger.info("Owner updated : {}", this.jsonizer.jsonize(owner, true));
            // Update to external index
            this.updateToIndex(owner, hasChanged);
            return true;
        }
        logger.warn("Failed to update owner : {}", this.jsonizer.jsonize(owner, true));
        return false;
    }

    /**
     * Deletes the given owner object from the database and deletes all the
     * owner directories.
     *
     * @param owner the owner to be deleted
     * @return true if and only if the owner was deleted; otherwise false
     */
    @Override
    public boolean delete(Owner owner) {
        if (!canBeDeleted(owner)) {
            logger.warn("Unable to delete the given owner, because the owner has dependencies in the database.");
            return false;
        }
        // Load owner object with all the dependencies
        owner = this.getFullOwner(owner.getId());
        // JSON presentation of the Owner object
        String json = this.jsonizer.jsonize(owner, true);
        boolean deleted = true;
        if (!this.dirService.delete(Settings.getInstance().getOwnersPath() + owner.getCode(), false)) {
            deleted = false;
        }
        if (!this.dirService.delete(Settings.getInstance().getOwnersPathAdmin() + owner.getCode(), false)) {
            deleted = false;
        }
        if (!deleted) {
            logger.warn("Failed to delete all the owner related directories.");
            logger.warn("Failed to delete owner : {}", json);
            return false;
        }
        if (dao.delete(owner)) {
            logger.info("Owner deleted : {}", json);
            // Delete from external index
            this.deleteFromIndex(owner);
            return true;
        }
        logger.warn("Failed to delete owner : {}", json);
        return false;
    }

    /**
     * Deletes the given call number modification object from the database.
     *
     * @param mod the call number modification to be deleted
     */
    @Override
    public void delete(CallnoModification mod) {
        dao.delete(mod);
    }

    /**
     * Returns a map of PreprocessingRedirect ids related to the owner with the
     * given id.
     *
     * @param id owner id
     * @return list of PreprocessingRedirect ids related to the owner
     */
    @Override
    public Map<Integer, Boolean> getPreprocessingRedirectIds(int id) {
        List<Integer> list = this.dao.getPreprocessingRedirectIds(id);
        Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
        for (Integer redirectId : list) {
            map.put(redirectId, true);
        }
        return map;
    }

    /**
     * Returns a map of NotFoundRedirect ids related to the owner with the given
     * id.
     *
     * @param id owner id
     * @return list of NotFoundRedirect ids related to the owner
     */
    @Override
    public Map<Integer, Boolean> getNotFoundRedirectIds(int id) {
        List<Integer> list = this.dao.getNotFoundRedirectIds(id);
        Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
        for (Integer redirectId : list) {
            map.put(redirectId, true);
        }
        return map;
    }

    /**
     * Goes through all the redirects related to the given owner and deletes all
     * the empty ones from the database.
     *
     * @param owner Owner to be checked
     */
    private void removeUnusedRedirects(Owner owner) {
        /* Handle not found redirects list. */
        List<CallnoModification> delList = new ArrayList<CallnoModification>();
        for (CallnoModification mod : owner.getNotFoundRedirects()) {
            if (mod.isEmpty()) {
                delList.add(mod);
            }
        }
        for (CallnoModification mod : delList) {
            owner.removeNotFoundRedirect((NotFoundRedirect) mod);
            if (mod.getId() > 0) {
                this.delete(mod);
            }
        }

        /* Handle preprocessing redirects list. */
        delList = new ArrayList<CallnoModification>();
        for (CallnoModification mod : owner.getPreprocessingRedirects()) {
            if (mod.isEmpty()) {
                delList.add(mod);
            }
        }
        for (CallnoModification mod : delList) {
            owner.removePreprocessingRedirect((PreprocessingRedirect) mod);
            if (mod.getId() > 0) {
                this.delete(mod);
            }
        }
    }

    /**
     * Can be used for adding Owners to external index. Not implemented.
     *
     * @param owner Owner to be added
     * @return always true, not implemented
     */
    protected boolean addToIndex(Owner owner) {
        return true;
    }

    /**
     * Can be used for updating Owners to external index. Not implemented.
     *
     * @param owner Owner to be updated
     * @param hasChanged boolean value that tells if the owner code has changed
     * @return always true, not implemented
     */
    protected boolean updateToIndex(Owner owner, boolean hasChanged) {
        return true;
    }

    /**
     * Can be used for deleting Owners from external index. Not implemented.
     *
     * @param owner Owner to be deleted
     * @return always true, not implemented
     */
    protected boolean deleteFromIndex(Owner owner) {
        return true;
    }

    /**
     * Recreates the search index if it exists. Not supported.
     */
    @Override
    public void recreateSearchIndex() {
        logger.info("Search index not supported. Nothing to do.");
    }
}
