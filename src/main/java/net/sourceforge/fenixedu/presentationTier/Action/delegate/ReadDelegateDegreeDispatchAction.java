package net.sourceforge.fenixedu.presentationTier.Action.delegate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "DelegateResources", path = "delegate", titleKey = "label.delegatesPortal",
        accessGroup = "role(DELEGATE)", hint = "Delegate")
@Mapping(module = "delegate", path = "/index")
@Forwards(value = { @Forward(name = "success", path = "/delegate/index.jsp") })
public class ReadDelegateDegreeDispatchAction extends FenixAction {

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException {
        final Person person = getLoggedPerson(request);
        if (person.getStudent() != null) {
            final Registration lastActiveRegistration = person.getStudent().getLastActiveRegistration();
            if (lastActiveRegistration != null) {
                final Degree degree = lastActiveRegistration.getDegree();
                final ExecutionDegree executionDegree = degree.getMostRecentDegreeCurricularPlan().getMostRecentExecutionDegree();
                final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
                request.setAttribute(PresentationConstants.MASTER_DEGREE, infoExecutionDegree);
            }
        }
        return mapping.findForward("success");
    }
}
