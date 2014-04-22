package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.graduatedPerson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/caseHandlingDegreeCandidacyForGraduatedPersonIndividualProcess", module = "coordinator",
        formBeanClass = FenixActionForm.class, functionality = DegreeCoordinatorIndex.class)
@Forwards({
        @Forward(name = "intro",
                path = "/coordinator/caseHandlingDegreeCandidacyForGraduatedPersonProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "introduce-candidacy-result",
                path = "/coordinator/candidacy/graduatedPerson/introduceCandidacyResult.jsp"),
        @Forward(name = "list-allowed-activities",
                path = "/coordinator/candidacy/graduatedPerson/listIndividualCandidacyActivities.jsp") })
public class DegreeCandidacyForGraduatedPersonIndividualProcessDA
        extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final String degreeCurricularPlanOID = DegreeCoordinatorIndex.findDegreeCurricularPlanID(request);
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanOID);
        request.setAttribute("seriesGrade", getProcess(request).getCandidacy()
                .getDegreeCandidacyForGraduatedPersonSeriesGadeForDegree(degreeCurricularPlan.getDegree()));
        return super.listProcessAllowedActivities(mapping, form, request, response);
    }

    @Override
    public ActionForward prepareExecuteIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final String degreeCurricularPlanOID = DegreeCoordinatorIndex.findDegreeCurricularPlanID(request);
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanOID);
        request.setAttribute("individualCandidacyResultBean", new DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean(
                getProcess(request), degreeCurricularPlan.getDegree()));
        return mapping.findForward("introduce-candidacy-result");
    }

}
