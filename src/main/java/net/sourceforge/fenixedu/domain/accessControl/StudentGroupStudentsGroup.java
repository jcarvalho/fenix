package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;

public class StudentGroupStudentsGroup extends DomainBackedGroup<StudentGroup> {

    private static final long serialVersionUID = -7462413677592415379L;

    public StudentGroupStudentsGroup(StudentGroup object) {
        super(object);
    }

    private StudentGroup getStudentGroup() {
        return this.getObject();
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();

        final StudentGroup studentGroup = getStudentGroup();
        if (studentGroup != null) {
            for (Attends attends : getStudentGroup().getAttends()) {
                elements.add(attends.getRegistration().getPerson());
            }
        }

        return super.freezeSet(elements);
    }

    @Override
    public boolean isMember(Person person) {
        if (person != null && person.hasStudent()) {
            for (final Attends attends : getStudentGroup().getAttendsSet()) {
                if (attends.getRegistration().getStudent().getPerson() == person) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            try {
                return new StudentGroupStudentsGroup((StudentGroup) arguments[0]);
            } catch (ClassCastException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.studentGroupStudents.notStudentGroup",
                        arguments[0].toString());
            }
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

        @Override
        public int getMaxArguments() {
            return 1;
        }

    }

    @Override
    public PersistentStudentGroupGroup convert() {
        return PersistentStudentGroupGroup.getInstance(getObject());
    }
}
