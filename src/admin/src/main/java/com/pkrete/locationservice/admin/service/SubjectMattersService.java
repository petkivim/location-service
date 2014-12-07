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
package com.pkrete.locationservice.admin.service;

import com.pkrete.locationservice.admin.model.owner.Owner;
import com.pkrete.locationservice.admin.model.subjectmatter.SubjectMatter;
import java.util.List;
import java.util.Set;

/**
 * This interface defines service layer for SubjectMatter objects. All the
 * classes implementing this interface must implement all the methods defined
 * here.
 *
 * @author Petteri Kivimäki
 */
public interface SubjectMattersService {

    List<SubjectMatter> getSubjectMatters(Owner owner);

    List<SubjectMatter> getSubjectMattersWithLanguage(Owner owner);

    SubjectMatter getSubjectMatter(int id);

    SubjectMatter getSubjectMatter(int id, Owner owner);

    SubjectMatter getSubjectMatterToBeDeleted(int id, Owner owner);

    Set<Integer> getIds(Owner owner);

    boolean create(SubjectMatter subjectMatter);

    boolean update(SubjectMatter subjectMatter);

    boolean delete(SubjectMatter subjectMatter);
}
