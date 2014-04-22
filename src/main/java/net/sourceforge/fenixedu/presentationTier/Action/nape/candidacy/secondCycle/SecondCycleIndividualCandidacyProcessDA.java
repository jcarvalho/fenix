package net.sourceforge.fenixedu.presentationTier.Action.nape.candidacy.secondCycle;

import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingSecondCycleIndividualCandidacyProcess", module = "nape", formBeanClass = FenixActionForm.class,
        functionality = SecondCycleCandidacyProcessDA.class)
@Forwards({
        @Forward(name = "intro", path = "/nape/caseHandlingSecondCycleCandidacyProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities", path = "/nape/candidacy/secondCycle/listIndividualCandidacyActivities.jsp") })
public class SecondCycleIndividualCandidacyProcessDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.secondCycle.SecondCycleIndividualCandidacyProcessDA {

}
