package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/editOldMarkSheet", module = "academicAdministration", formBean = "markSheetManagementForm",
        input = "/markSheetManagement.do?method=prepareSearchMarkSheet", functionality = OldMarkSheetSearchDispatchAction.class)
@Forwards({
        @Forward(name = "editMarkSheet", path = "/academicAdministration/gradeSubmission/oldMarkSheets/editMarkSheet.jsp"),
        @Forward(name = "searchMarkSheetFilled",
                path = "/academicAdministration/oldMarkSheetManagement.do?method=prepareSearchMarkSheetFilled") })
public class OldMarkSheetEditDispatchAction extends MarkSheetEditDispatchAction {

    @Override
    protected void checkIfTeacherIsResponsibleOrCoordinator(CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, String teacherId, Teacher teacher, HttpServletRequest request,
            MarkSheetType markSheetType, ActionMessages actionMessages) {

    }

    @Override
    protected void checkIfEvaluationDateIsInExamsPeriod(DegreeCurricularPlan degreeCurricularPlan,
            ExecutionSemester executionSemester, Date evaluationDate, MarkSheetType markSheetType, HttpServletRequest request,
            ActionMessages actionMessages) {
    }

}
