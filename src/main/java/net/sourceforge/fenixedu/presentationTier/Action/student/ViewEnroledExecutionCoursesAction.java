package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadEnroledExecutionCourses;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentEnrollApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = StudentEnrollApp.class, path = "groups", titleKey = "link.groupEnrolment")
@Mapping(module = "student", path = "/viewEnroledExecutionCourses")
@Forwards({ @Forward(name = "showEnroledExecutionCourses", path = "/student/viewEnroledExecutionCourses_bd.jsp"),
        @Forward(name = "showActiveRegistrations", path = "/student/viewActiveRegistrations.jsp") })
public class ViewEnroledExecutionCoursesAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final Student student = getLoggedPerson(request).getStudent();
        final List<Registration> registrations = student.getActiveRegistrations();

        if (registrations.size() == 1) {
            request.setAttribute("executionCourses", ReadEnroledExecutionCourses.run(registrations.iterator().next()));
            return mapping.findForward("showEnroledExecutionCourses");

        } else {
            request.setAttribute("registrations", registrations);
            return mapping.findForward("showActiveRegistrations");
        }
    }

    public ActionForward select(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final Registration registration =
                getRegistrationByID(getLoggedPerson(request).getStudent(), getStringFromRequest(request, "registrationId"));
        request.setAttribute("executionCourses", ReadEnroledExecutionCourses.run(registration));
        return mapping.findForward("showEnroledExecutionCourses");
    }

    private Registration getRegistrationByID(final Student student, final String registrationId) {
        for (final Registration registration : student.getActiveRegistrations()) {
            if (registration.getExternalId().equals(registrationId)) {
                return registration;
            }
        }
        return null;
    }
}