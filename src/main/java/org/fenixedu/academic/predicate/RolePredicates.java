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
package org.fenixedu.academic.predicate;

import org.fenixedu.academic.domain.AcademicGroups;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.bennu.core.groups.Group;

public class RolePredicates {

    public static final AccessControlPredicate<Object> ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE =
            new AccessControlPredicate<Object>() {
                @Override
                public boolean evaluate(Object domainObject) {
                    return hasRole(AcademicGroups.ACADEMIC_ADMINISTRATIVE_OFFICE);
                };
            };

    public static final AccessControlPredicate<Object> BOLONHA_MANAGER_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(AcademicGroups.BOLONHA_MANAGER);
        };
    };

    public static final AccessControlPredicate<Object> DIRECTIVE_COUNCIL_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object object) {
            return hasRole(AcademicGroups.DIRECTIVE_COUNCIL);

        };
    };

    public static final AccessControlPredicate<Object> GEP_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(AcademicGroups.GEP);
        };
    };

    public static final AccessControlPredicate<Object> MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE =
            new AccessControlPredicate<Object>() {
                @Override
                public boolean evaluate(Object domainObject) {
                    return MANAGER_PREDICATE.evaluate(domainObject)
                            || ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE.evaluate(domainObject);
                };
            };

    public static final AccessControlPredicate<Object> MANAGER_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(AcademicGroups.MANAGER);
        };
    };

    public static final AccessControlPredicate<Object> MANAGER_OR_OPERATOR_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return MANAGER_PREDICATE.evaluate(domainObject) || OPERATOR_PREDICATE.evaluate(domainObject);
        };
    };

    public static final AccessControlPredicate<Object> OPERATOR_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(AcademicGroups.OPERATOR);
        };
    };

    public static final AccessControlPredicate<Object> RESOURCE_ALLOCATION_MANAGER_PREDICATE =
            new AccessControlPredicate<Object>() {
                @Override
                public boolean evaluate(Object domainObject) {
                    return hasRole(AcademicGroups.RESOURCE_ALLOCATION_MANAGER);
                };
            };

    public static final AccessControlPredicate<Object> SCIENTIFIC_COUNCIL_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(AcademicGroups.SCIENTIFIC_COUNCIL);
        };
    };

    public static final AccessControlPredicate<Object> STUDENT_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(AcademicGroups.STUDENT);
        };
    };

    public static final AccessControlPredicate<Object> TEACHER_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return isTeacher();
        };
    };

    public static final AccessControlPredicate<Object> STUDENT_AND_TEACHER_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return isTeacher() || hasRole(AcademicGroups.STUDENT);
        };
    };

    private static boolean hasRole(final Group roleType) {
        final Person person = AccessControl.getPerson();
        return person != null && roleType.isMember(person.getUser());
    }

    private static boolean isTeacher() {
        return hasRole(AcademicGroups.TEACHER) || hasActiveProfessorship();
    }

    private static boolean hasActiveProfessorship() {
        final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        final Person person = AccessControl.getPerson();
        for (final Professorship professorship : person.getProfessorshipsSet()) {
            if (professorship.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                return true;
            }
        }
        return false;
    }

}
