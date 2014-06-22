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
package com.pkrete.locationservice.admin.dao;

import com.pkrete.locationservice.admin.model.language.Language;
import com.pkrete.locationservice.admin.model.owner.Owner;
import java.util.List;

/**
 * This interface defines data access layer for Language objects. All the 
 * classes implementing this interface must implement all the methods defined 
 * here.
 * 
 * @author Petteri Kivimäki
 */
public interface LanguagesDao {
    
    public List<Language> getLanguages();

    public List<Language> getLanguages(Owner owner);

    public Language getLanguageById(int id);

    public Language getLanguage(String code, Owner owner);

    public Language getLanguageById(int id, Owner owner);

    public boolean create(Language language);
    
    public boolean update(Language language);

    public boolean delete(Language language);

    public boolean canBeDeleted(Language lang);
}
