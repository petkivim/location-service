/**
 * This file is part of Location Service :: Endpoint. Copyright (C) 2014 Petteri
 * Kivimäki
 *
 * Location Service :: Endpoint is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Location Service :: Endpoint is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Location Service :: Endpoint. If not, see <http://www.gnu.org/licenses/>.
 */
package com.pkrete.locationservice.endpoint.modifier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The CallnoModifier class is responsible for modificating call numbers
 * according to the given {@link CallnoModification CallnoModification} that
 * represents rules of the modification operation. The rules define a condition
 * and an operation that's done, if the rule apply. Before doing the
 * modification it's possible to check if the given call number and modification
 * rule are matching.
 *
 * Both the rule and the operation can contain regular expressions. It is
 * possible to use the value of a group within the condition, and it is also
 * possible to use a group value in the operation pattern. Instead of a
 * backslash, the form of the reference is $n where n is a group number starting
 * from 0.
 *
 * @author Petteri Kivimäki
 */
public class CallnoModifier implements Modifier {

    /**
     * Compares the given call number and call number modification rule and
     * tells if the given call number matches the rule.
     *
     * @param callno call number to be checked
     * @param modification modification rule to be checked
     * @return true if the call number and rule match, otherwise false
     */
    @Override
    public boolean canBeModified(String callno, CallnoModification modification) {
        Pattern regex = Pattern.compile(modification.getCondition());
        Matcher m = regex.matcher(callno);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * Modifies the given call number according to the given modification rule,
     * and returns the modified call number.
     *
     * @param callno call number to be modified
     * @param modification modification rule to be checked
     * @return modified call number
     */
    @Override
    public String modify(String callno, CallnoModification modification) {
        Pattern regex = Pattern.compile(modification.getCondition());
        Matcher m = regex.matcher(callno);
        return m.replaceAll(modification.getOperation());
    }
}
