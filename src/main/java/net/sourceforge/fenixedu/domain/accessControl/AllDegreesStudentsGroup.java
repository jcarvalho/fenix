/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 13, 2006,4:39:15 PM
 */
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jul 13, 2006,4:39:15 PM
 * 
 */
public class AllDegreesStudentsGroup extends Group {

    private static final long serialVersionUID = 2693414643122716513L;

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();
        final Role role = Role.getRoleByRoleType(RoleType.STUDENT);
        for (final Person person : role.getAssociatedPersons()) {
            for (Registration registration : person.getStudent().getRegistrationsSet()) {
                if (registration.getDegreeType() == DegreeType.DEGREE
                        || registration.getDegreeType() == DegreeType.BOLONHA_DEGREE
                        || registration.getDegreeType() == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
                    if (registration.isActive()) {
                        elements.add(person);
                    }
                }
            }
        }

        return elements;
    }

    @Override
    public boolean isMember(Person person) {
        if (person != null && person.getStudent() != null) {
            for (final Registration registration : person.getStudent().getRegistrationsSet()) {
                if (registration.isActive() && registration.isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[0];
    }

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        return PersistentStudentGroup.getInstance(DegreeType.DEGREE)
                .or(PersistentStudentGroup.getInstance(DegreeType.BOLONHA_DEGREE))
                .or(PersistentStudentGroup.getInstance(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
    }
}
