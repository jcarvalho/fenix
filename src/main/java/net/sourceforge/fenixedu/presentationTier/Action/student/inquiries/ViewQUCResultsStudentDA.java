package net.sourceforge.fenixedu.presentationTier.Action.student.inquiries;

import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.inquiries.ViewQUCResultsPedagogicalCouncilDA;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/viewQucResults", module = "student", functionality = QUCStudentAuditorDA.class)
public class ViewQUCResultsStudentDA extends ViewQUCResultsPedagogicalCouncilDA {
}
