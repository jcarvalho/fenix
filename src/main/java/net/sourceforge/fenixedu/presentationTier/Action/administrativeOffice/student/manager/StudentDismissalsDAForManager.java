package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.manager;

import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.StudentsApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = StudentsApp.class, descriptionKey = "label.student.dismissals", path = "student-dismissals",
        titleKey = "label.student.dismissals")
@Mapping(module = "manager", path = "/studentDismissals", attribute = "studentDismissalForm", formBean = "studentDismissalForm",
        scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "visualizeRegistration", path = "/bolonhaStudentEnrolment.do?method=showAllStudentCurricularPlans"),
        @Forward(name = "chooseEquivalents", path = "/manager/bolonha/dismissal/chooseEquivalents.jsp"),
        @Forward(name = "chooseExternalCurricularCourse", path = "studentDismissal.chooseExternalCurricularCourse"),
        @Forward(name = "confirmCreateDismissals", path = "/manager/bolonha/dismissal/confirmCreateDismissals.jsp"),
        @Forward(name = "manage", path = "/manager/bolonha/dismissal/managementDismissals.jsp"),
        @Forward(name = "prepareCreateExternalEnrolment", path = "/manager/bolonha/dismissal/createExternalEnrolment.jsp"),
        @Forward(name = "chooseDismissalEnrolments", path = "/manager/bolonha/dismissal/chooseDismissalEnrolments.jsp") })
public class StudentDismissalsDAForManager extends
        net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.StudentDismissalsDA {
}