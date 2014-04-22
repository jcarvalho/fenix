package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.student;

import net.sourceforge.fenixedu.presentationTier.Action.phd.student.PhdIndividualProgramProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.CommonPhdThesisProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdThesisProcess", module = "student", functionality = PhdIndividualProgramProcessDA.class)
@Forwards({ @Forward(name = "manageThesisDocuments", path = "/phd/thesis/student/manageThesisDocuments.jsp") })
public class PhdThesisProcessDA extends CommonPhdThesisProcessDA {

}
