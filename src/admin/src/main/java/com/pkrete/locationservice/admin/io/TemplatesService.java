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
package com.pkrete.locationservice.admin.io;

import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.util.TemplateType;
import java.util.List;
import java.util.Map;

/**
 * FileService interface defines service interface for managing template files.
 * All the classes implementing this interface must implement all the methods 
 * defined here.
 * 
 * @author Petteri Kivimäki
 */
public interface TemplatesService {

    public List<String> getList(String lang, Owner owner);

    public List<String> getList(boolean onlyOther, String lang, Owner owner);

    public Map<String, String> getMap(String lang, Owner owner);

    public boolean create(String filename, String lang, Owner owner);
    
    public boolean create(String filename, String contents, String lang, Owner owner);

    public boolean create(int locationId, String otherName, boolean incCollectionCode, TemplateType type, String lang, Owner owner);

    public String read(Owner owner);

    public String read(String filename, String lang, Owner owner);

    public boolean update(String contents, Owner owner);

    public boolean update(String filename, String contents, String lang, Owner owner);

    public boolean delete(String filename, String lang, Owner owner);

    public boolean rename(String oldFilename, String newFilename, String lang, Owner owner);

    public boolean rename(String oldFilename, int newId, String newOtherName, boolean incCollectionCode, TemplateType newType, String lang, Owner owner);

    public boolean exists(String filename, String lang, Owner owner);

    public boolean createDefaults(String lang, Owner owner);

    public String buildTemplateName(int locationId, String otherName, boolean incCollectionCode, TemplateType type, Owner owner);
    
    public Map<String, List<Language>> getTemplatesAndLanguages(Owner owner);
}
