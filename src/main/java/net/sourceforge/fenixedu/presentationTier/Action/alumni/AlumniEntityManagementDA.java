package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.alumni.RegisterAlumniData;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

public abstract class AlumniEntityManagementDA extends FenixDispatchAction {

    public AlumniEntityManagementDA() {
        super();
    }

    protected Alumni getAlumniFromLoggedPerson(HttpServletRequest request) {
        Student alumniStudent = getLoggedPerson(request).getStudent();
        if (alumniStudent.getAlumni() != null) {
            return alumniStudent.getAlumni();
        } else {
            return RegisterAlumniData.run(alumniStudent);
        }
    }

}