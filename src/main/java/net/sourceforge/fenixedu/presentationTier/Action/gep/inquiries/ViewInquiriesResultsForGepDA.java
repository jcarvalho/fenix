/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.ViewInquiriesResultPageDTO;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.inquiries.ViewInquiriesResultsDA;
import net.sourceforge.fenixedu.presentationTier.Action.gep.GepApplication.GepInquiriesApp;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@StrutsFunctionality(app = GepInquiriesApp.class, path = "view-results", titleKey = "title.inquiries.results")
@Mapping(path = "/viewInquiriesResults", module = "gep", formBeanClass = ViewInquiriesResultPageDTO.class)
@Forwards({ @Forward(name = "inquiryResults", path = "/coordinator/inquiries/viewInquiriesResults.jsp"),
        @Forward(name = "chooseDegreeCurricularPlan", path = "/pedagogicalCouncil/inquiries/chooseDegreeCurricularPlan.jsp"),
        @Forward(name = "curricularUnitSelection", path = "/coordinator/inquiries/curricularUnitSelection.jsp"),
        @Forward(name = "showFilledTeachingInquiry", path = "/coordinator/inquiries/showFilledTeachingInquiry.jsp"),
        @Forward(name = "showFilledTeachingInquiry_v2", path = "/coordinator/inquiries/showFilledTeachingInquiry_v2.jsp"),
        @Forward(name = "showFilledDelegateInquiry", path = "/coordinator/inquiries/showFilledDelegateInquiry.jsp"),
        @Forward(name = "showCourseInquiryResult", path = "/coordinator/inquiries/showCourseInquiryResult.jsp"),
        @Forward(name = "showTeachingInquiryResult", path = "/coordinator/inquiries/showTeachingInquiryResult.jsp"),
        @Forward(name = "showOthersTeacherCourses", path = "/pedagogicalCouncil/inquiries/showOthersTeacherCourses.jsp") })
public class ViewInquiriesResultsForGepDA extends ViewInquiriesResultsDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("degreeCurricularPlanID", CoordinatedDegreeInfo.findDegreeCurricularPlanID(request));
        return super.execute(mapping, actionForm, request, response);
    }

    @EntryPoint
    public ActionForward chooseDegreeCurricularPlan(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final List<Degree> degrees = new ArrayList<Degree>(rootDomainObject.getDegreesSet());
        Collections.sort(degrees, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

        request.setAttribute("degrees", degrees);

        return actionMapping.findForward("chooseDegreeCurricularPlan");
    }

    @Override
    protected boolean coordinatorCanComment(ExecutionDegree executionDegree, ExecutionSemester executionPeriod) {
        return false;
    }

    public ActionForward showOthersTeacherCourses(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Professorship professorship = FenixFramework.getDomainObject(getStringFromRequest(request, "professorshipID"));
        Set<ExecutionCourse> executionCourses = new HashSet<ExecutionCourse>();
        ExecutionSemester executionSemester = professorship.getExecutionCourse().getExecutionPeriod();
        for (Professorship anotherProfessorship : professorship.getPerson().getProfessorships(executionSemester)) {
            executionCourses.add(anotherProfessorship.getExecutionCourse());
        }
        request.setAttribute("professorship", professorship);
        request.setAttribute("executionCourses", executionCourses);
        return actionMapping.findForward("showOthersTeacherCourses");
    }
}
