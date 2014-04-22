package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.DepartmentAdmOfficeApp.DepartmentAdmOfficeTeachersApp;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DepartmentAdmOfficeTeachersApp.class, path = "summaries-management", titleKey = "link.summaries")
@Mapping(path = "/teacherSearchForSummariesManagement", module = "departmentAdmOffice")
@Forwards({ @Forward(name = "search-form", path = "/credits/commons/searchTeacherLayout.jsp"),
        @Forward(name = "list-one", path = "/departmentAdmOffice/showTeacherProfessorshipsForSummariesManagement.do") })
public class TeacherSearchForSummariesManagement extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String teacherId = request.getParameter("teacherId");
        Teacher teacher = getTeacher(teacherId);
        if (teacher != null) {
            request.setAttribute("infoTeacher", new InfoTeacher(teacher));
            return mapping.findForward("list-one");
        }
        return mapping.findForward("search-form");
    }

    private Teacher getTeacher(String teacherId) {
        Person person = Person.readPersonByUsername(teacherId);
        if (person == null) {
            return null;
        }
        Teacher teacher = person.getTeacher();
        String employeeDepartment = AccessControl.getPerson().getEmployee().getCurrentDepartmentWorkingPlace().getName();
        if (teacher == null || !employeeDepartment.equals(teacher.getCurrentWorkingDepartment().getName())) {
            return null;
        } else {
            return teacher;
        }
    }
}
