package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/tutorshipSummaryPeriod", module = "pedagogicalCouncil", functionality = TutorshipSummaryDA.class)
@Forwards({ @Forward(name = "createPeriod", path = "/pedagogicalCouncil/tutorship/createPeriod.jsp"),
        @Forward(name = "confirmMessagePeriod", path = "/pedagogicalCouncil/tutorship/confirmCreatePeriod.jsp") })
public class TutorshipSummaryPeriodDA extends FenixDispatchAction {

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        RenderUtils.invalidateViewState();

        TutorshipSummaryPeriodBean bean = new TutorshipSummaryPeriodBean();
        setTutorshipSummaryPeriodBean(request, bean);

        return mapping.findForward("createPeriod");
    }

    public ActionForward prepareCreate2(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        TutorshipSummaryPeriodBean bean = getTutorshipSummaryPeriodBean();

        if (bean == null) {
            return prepareCreate(mapping, actionForm, request, response);
        }

        setTutorshipSummaryPeriodBean(request, bean);

        return mapping.findForward("createPeriod");
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        TutorshipSummaryPeriodBean bean = getTutorshipSummaryPeriodBean();

        if (bean != null && bean.isValid()) {
            bean.save();

            return mapping.findForward("confirmMessagePeriod");
        }

        return prepareCreate2(mapping, actionForm, request, response);
    }

    protected TutorshipSummaryPeriodBean getTutorshipSummaryPeriodBean() {
        return getRenderedObject("periodBean");
    }

    protected void setTutorshipSummaryPeriodBean(HttpServletRequest request, TutorshipSummaryPeriodBean bean) {
        request.setAttribute("periodBean", bean);
    }
}
