package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.candidacy.degreeChange;

import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingDegreeChangeIndividualCandidacyProcess", module = "scientificCouncil",
        formBeanClass = FenixActionForm.class, functionality = DegreeChangeCandidacyProcessDA.class)
@Forwards({
        @Forward(name = "intro",
                path = "/scientificCouncil/caseHandlingDegreeChangeCandidacyProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities",
                path = "/scientificCouncil/candidacy/degreeChange/listIndividualCandidacyActivities.jsp") })
public class DegreeChangeIndividualCandidacyProcessDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeChange.DegreeChangeIndividualCandidacyProcessDA {

}
