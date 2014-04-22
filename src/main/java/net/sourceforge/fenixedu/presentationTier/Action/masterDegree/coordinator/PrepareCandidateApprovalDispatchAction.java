package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate.ReadExecutionDegreeByDegreeCurricularPlanID;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/prepareCandidateApproval", functionality = DegreeCoordinatorIndex.class)
@Forwards(@Forward(name = "ExecutionDegreeChosen",
        path = "/coordinator/displayListToSelectCandidates.do?method=prepareSelectCandidates"))
public class PrepareCandidateApprovalDispatchAction extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(PrepareCandidateApprovalDispatchAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward chooseExecutionDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoExecutionDegree infoExecutionDegree;
        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        try {
            infoExecutionDegree =
                    ReadExecutionDegreeByDegreeCurricularPlanID.runReadExecutionDegreeByDegreeCurricularPlanID(
                            degreeCurricularPlanID, new Integer(1));
        } catch (FenixServiceException e) {
            logger.error(e.getMessage(), e);
            throw new FenixActionException();
        }

        request.setAttribute("degree", infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla());
        request.setAttribute("executionYear", infoExecutionDegree.getInfoExecutionYear().getYear());

        request.setAttribute("executionDegreeID", infoExecutionDegree.getExternalId());

        return mapping.findForward("ExecutionDegreeChosen");
    }

}