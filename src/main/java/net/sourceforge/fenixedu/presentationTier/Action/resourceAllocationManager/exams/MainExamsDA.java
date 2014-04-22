package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.ContextSelectionBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.RAMApplication.RAMEvaluationsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = RAMEvaluationsApp.class, path = "written-evaluations-calendar",
        titleKey = "link.writtenEvaluation.map")
@Mapping(path = "/mainExams", module = "resourceAllocationManager")
@Forwards(@Forward(name = "showForm", path = "/resourceAllocationManager/exams/mainExams_bd.jsp"))
public class MainExamsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ContextSelectionBean contextSelectionBean = getRenderedObject();
        if (contextSelectionBean == null) {
            contextSelectionBean = new ContextSelectionBean();
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("bean", contextSelectionBean);
        final List<ExecutionDegree> executionDegrees =
                new ArrayList<ExecutionDegree>(ExecutionDegree.filterByAcademicInterval(contextSelectionBean
                        .getAcademicInterval()));
        Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
        request.setAttribute("executionDegrees", executionDegrees);
        request.setAttribute("executionInterval",
                ExecutionInterval.getExecutionInterval(contextSelectionBean.getAcademicInterval()));
        return mapping.findForward("showForm");
    }
}
