package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.marksManagement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement.ReadStudentMarksListByCurricularCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Fernanda Quitério 30/06/2003
 * 
 */
@Mapping(path = "/showMarkDispatchAction", module = "masterDegreeAdministrativeOffice",
        input = "/marksManagement.do?method=chooseCurricularCourses", formBean = "studentNumberForm")
@Forwards(value = { @Forward(name = "displayStudentList", path = "/marksManagement/displayStudentList.jsp"),
        @Forward(name = "NoStudents", path = "/marksManagement.do?method=getStudentMarksList"),
        @Forward(name = "NotAuthorized", path = "/student/notAuthorized.jsp") })
public class ShowMarkDispatchAction extends FenixDispatchAction {

    public ActionForward prepareShowMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();

        String curricularCourseId = MarksManagementDispatchAction.getFromRequest("courseId", request);
        MarksManagementDispatchAction.getFromRequest("objectCode", request);
        MarksManagementDispatchAction.getFromRequest("degreeId", request);
        // Get students List
        User userView = Authenticate.getUser();
        List listEnrolmentEvaluation = null;
        try {
            listEnrolmentEvaluation =
                    ReadStudentMarksListByCurricularCourse.runReadStudentMarksListByCurricularCourse(userView,
                            curricularCourseId, null);
        } catch (NotAuthorizedException e) {
            return mapping.findForward("NotAuthorized");
        } catch (NonExistingServiceException e) {
            errors.add("nonExisting", new ActionError("error.exception.noStudents"));
            saveErrors(request, errors);
            return mapping.findForward("NoStudents");
        }
        if (listEnrolmentEvaluation.size() == 0) {
            errors.add("StudentNotEnroled", new ActionError("error.students.Mark.NotAvailable"));
            saveErrors(request, errors);
            return mapping.findForward("NoStudents");
        }

        request.setAttribute("showMarks", "showMarks");
        request.setAttribute("studentList", listEnrolmentEvaluation);

        return mapping.findForward("displayStudentList");
    }
}