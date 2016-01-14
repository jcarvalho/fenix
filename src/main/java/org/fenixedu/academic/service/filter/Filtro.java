/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.service.filter;

import java.util.Collection;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;

abstract public class Filtro {

    /**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection<Group> getNeededRoleTypes() {
        return null;
    }

    protected boolean containsRoleType(User user) {
        final Collection<Group> neededRoleTypes = getNeededRoleTypes();
        if (neededRoleTypes == null || neededRoleTypes.isEmpty()) {
            return true;
        }
        if (user != null) {
            for (Group role : neededRoleTypes) {
                if (role.isMember(user)) {
                    return true;
                }
            }
        }
        return false;
    }

}
