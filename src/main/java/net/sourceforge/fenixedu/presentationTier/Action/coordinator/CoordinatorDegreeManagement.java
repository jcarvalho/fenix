package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.CoordinatorApplication.CoordinatorManagementApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = CoordinatorManagementApp.class, path = "choose-degree", titleKey = "label.degrees")
@Mapping(path = "/index", module = "coordinator")
@Forwards(@Forward(name = "ChooseDegree", path = "/coordinator/chooseDegreePage.jsp"))
public class CoordinatorDegreeManagement extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute("degrees", readCoordinatedDegrees());
        return mapping.findForward("ChooseDegree");
    }

    private Set<DegreeCurricularPlan> readCoordinatedDegrees() throws FenixServiceException {
        final Person person = AccessControl.getPerson();

        final Set<DegreeCurricularPlan> activeDegreeCurricularPlans =
                new TreeSet<DegreeCurricularPlan>(
                        DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);
        for (final Coordinator coordinator : person.getCoordinatorsSet()) {
            final ExecutionDegree executionDegree = coordinator.getExecutionDegree();
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.ACTIVE) {
                activeDegreeCurricularPlans.add(degreeCurricularPlan);
            }
        }

        for (ScientificCommission commission : person.getScientificCommissionsSet()) {
            ExecutionDegree executionDegree = commission.getExecutionDegree();
            DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

            if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.ACTIVE) {
                activeDegreeCurricularPlans.add(degreeCurricularPlan);
            }
        }

        return activeDegreeCurricularPlans;
    }

}
