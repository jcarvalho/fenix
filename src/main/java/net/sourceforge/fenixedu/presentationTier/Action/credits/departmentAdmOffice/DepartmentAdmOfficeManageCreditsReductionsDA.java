package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentAdmOffice;

import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageCreditsReductionsDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/creditsReductions",
        functionality = DepartmentAdmOfficeViewTeacherCreditsDA.class)
@Forwards(value = { @Forward(name = "editReductionService", path = "/credits/degreeTeachingService/editCreditsReduction.jsp"),
        @Forward(name = "viewAnnualTeachingCredits", path = "/departmentAdmOffice/credits.do?method=viewAnnualTeachingCredits") })
public class DepartmentAdmOfficeManageCreditsReductionsDA extends ManageCreditsReductionsDispatchAction {
}
