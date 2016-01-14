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
package org.fenixedu.academic.domain.person;

import org.fenixedu.academic.domain.accessControl.ActiveStudentsGroup;
import org.fenixedu.academic.domain.accessControl.ActiveTeachersGroup;
import org.fenixedu.academic.domain.accessControl.AllAlumniGroup;
import org.fenixedu.academic.domain.accessControl.AllCoordinatorsGroup;
import org.fenixedu.academic.domain.accessControl.CandidateGroup;
import org.fenixedu.academic.domain.accessControl.ExternalSupervisorGroup;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.LoggedGroup;

@Deprecated
public interface RoleType {

    public static final Group MESSAGING = LoggedGroup.get();

    public static final Group PERSON = LoggedGroup.get();

    public static final Group STUDENT = new ActiveStudentsGroup();

    public static final Group TEACHER = new ActiveTeachersGroup();

    public static final Group RESEARCHER = DynamicGroup.get("researcher");

    public static final Group DEPARTMENT_MEMBER = TEACHER;

    public static final Group RESOURCE_ALLOCATION_MANAGER = DynamicGroup.get("resourceAllocationManager");

    /**
     * @deprecated Use {@link RoleType}.ACADEMIC_ADMINISTRATIVE_OFFICE instead
     */
    @Deprecated
    public static final Group MASTER_DEGREE_ADMINISTRATIVE_OFFICE = DynamicGroup.get("masterDegreeAdmOffice");

    public static final Group COORDINATOR = new AllCoordinatorsGroup();

    public static final Group MANAGER = DynamicGroup.get("managers");

    /**
     * @deprecated Use {@link AcademicAuthorizationGroup#get(AcademicOperationType#MANAGE_DEGREE_CURRICULAR_PLANS)} instead
     */
    @Deprecated
    public static final Group DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER = DynamicGroup.get("degreeAdmOfficeSudo");

    public static final Group SCIENTIFIC_COUNCIL = DynamicGroup.get("scientificCouncil");

    public static final Group OPERATOR = DynamicGroup.get("operator");

    public static final Group GEP = DynamicGroup.get("gep");

    public static final Group DIRECTIVE_COUNCIL = DynamicGroup.get("directiveCouncil");

    public static final Group BOLONHA_MANAGER = DynamicGroup.get("bolonhaManager");

    public static final Group SPACE_MANAGER = DynamicGroup.get("spaceManager");

    public static final Group SPACE_MANAGER_SUPER_USER = DynamicGroup.get("spaceManagerSudo");

    public static final Group ALUMNI = new AllAlumniGroup();

    public static final Group PEDAGOGICAL_COUNCIL = DynamicGroup.get("pedagogicalCouncil");

    public static final Group CANDIDATE = new CandidateGroup();

    public static final Group ACADEMIC_ADMINISTRATIVE_OFFICE = DynamicGroup.get("academicAdmOffice");

    public static final Group LIBRARY = DynamicGroup.get("library");

    public static final Group INTERNATIONAL_RELATION_OFFICE = DynamicGroup.get("internationalRelationsOffice");

    public static final Group EXTERNAL_SUPERVISOR = new ExternalSupervisorGroup();

    public static final Group PUBLIC_RELATIONS_OFFICE = DynamicGroup.get("publicRelationsOffice");

    public static final Group NAPE = DynamicGroup.get("nape");

    public static final Group RESIDENCE_MANAGER = DynamicGroup.get("residenceManager");

    public static final Group RECTORATE = DynamicGroup.get("rectorate");

    public static final Group HTML_CAPABLE_SENDER = DynamicGroup.get("htmlCapableSender");

}
