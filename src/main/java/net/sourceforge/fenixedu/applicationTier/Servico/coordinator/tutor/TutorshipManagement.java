package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor;

import java.util.List;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.student.Registration;
import java.util.Locale;

public abstract class TutorshipManagement {

    private boolean verifyIfBelongsToDegree(Registration registration, Degree degree) {
        if (registration == null) {
            return false;
        }

        StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
        if (studentCurricularPlan == null) {
            return false;
        }

        return studentCurricularPlan.getDegreeCurricularPlan().getDegree().equals(degree);
    }

    protected void validateTeacher(Teacher teacher, ExecutionDegree executionDegree) throws FenixServiceException {
        List<Teacher> possibleTutorsForExecutionDegree = executionDegree.getPossibleTutorsFromExecutionDegreeDepartments();

        if (!possibleTutorsForExecutionDegree.contains(teacher)) {
            throw new FenixServiceException("error.tutor.cannotBeTutorOfExecutionDegree");
        }
    }

    protected void validateStudentRegistration(Registration registration, ExecutionDegree executionDegree,
            DegreeCurricularPlan degreeCurricularPlan, Integer studentNumber) throws FenixServiceException {

        if (!verifyIfBelongsToDegree(registration, degreeCurricularPlan.getDegree())) {
            // student doesn't belong to this degree
            ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale());
            String degreeType = bundle.getString(executionDegree.getDegree().getDegreeType().getName());

            throw new FenixServiceException("error.tutor.studentNoDegree", new String[] { studentNumber.toString(), degreeType,
                    executionDegree.getDegree().getNameFor(registration.getStartExecutionYear()).getContent() });
        }
    }

    protected void validateTutorship(Registration registration) throws FenixServiceException {
        if (registration.getActiveTutorship() != null) {
            // student already with tutor
            throw new FenixServiceException("error.tutor.studentAlreadyHasTutor", new String[] { registration.getNumber()
                    .toString() });
        }
    }
}
