package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.candidacy.degreeCandidacyForGraduatedPerson;

import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingDegreeCandidacyForGraduatedPersonIndividualProcess", module = "scientificCouncil",
        formBeanClass = FenixActionForm.class, functionality = DegreeCandidacyForGraduatedPersonProcessDA.class)
@Forwards({
        @Forward(
                name = "intro",
                path = "/scientificCouncil/caseHandlingDegreeCandidacyForGraduatedPersonProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities",
                path = "/scientificCouncil/candidacy/graduatedPerson/listIndividualCandidacyActivities.jsp") })
public class DegreeCandidacyForGraduatedPersonIndividualProcessDA
        extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualProcessDA {

}
