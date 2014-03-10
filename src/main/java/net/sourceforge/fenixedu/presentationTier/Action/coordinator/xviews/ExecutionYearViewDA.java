package net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/xYear", module = "coordinator", functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "xViewsDisclaimer", path = "/coordinator/xviews/xViewsDisclaimer.jsp"),
        @Forward(name = "xYearEntry", path = "/coordinator/xviews/xYearEntry.jsp"),
        @Forward(name = "xYearDisplay", path = "/coordinator/xviews/xYearDisplay.jsp") })
public class ExecutionYearViewDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @EntryPoint
    public ActionForward showDisclaimer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        return mapping.findForward("xViewsDisclaimer");
    }

    public ActionForward showYearInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        YearViewBean searchFormBean = (YearViewBean) getRenderedObject("searchFormBean");
        RenderUtils.invalidateViewState();

        if (searchFormBean == null || searchFormBean.getExecutionYear() == null) {
            String degreeCurricularPlanID = null;
            DegreeCurricularPlan degreeCurricularPlan = null;
            if (request.getParameter("degreeCurricularPlanID") != null) {
                degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
                request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
                degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
            }

            searchFormBean = new YearViewBean(degreeCurricularPlan);
            request.setAttribute("searchFormBean", searchFormBean);
            return mapping.findForward("xYearEntry");

        }

        request.setAttribute("dcpEId", searchFormBean.getDegreeCurricularPlan().getExternalId());
        request.setAttribute("eyEId", searchFormBean.getExecutionYear().getExternalId());

        request.setAttribute("searchFormBean", searchFormBean);
        return mapping.findForward("xYearDisplay");
    }

}
