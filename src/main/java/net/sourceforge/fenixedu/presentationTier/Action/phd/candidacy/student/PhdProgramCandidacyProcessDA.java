package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.student;

import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.CommonPhdCandidacyDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.student.PhdIndividualProgramProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdProgramCandidacyProcess", module = "student", functionality = PhdIndividualProgramProcessDA.class)
@Forwards({ @Forward(name = "manageCandidacyDocuments", path = "/phd/candidacy/student/manageCandidacyDocuments.jsp") })
public class PhdProgramCandidacyProcessDA extends CommonPhdCandidacyDA {

}
