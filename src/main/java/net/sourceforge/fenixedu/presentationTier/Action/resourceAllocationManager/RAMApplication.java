package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import net.sourceforge.fenixedu.presentationTier.Action.commons.FacesEntryPoint;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "ResourceAllocationManagerResources", path = "resource-allocation-manager",
        titleKey = "title.resourceAllocationManager.management", hint = "Resource Allocation Manager",
        accessGroup = "role(RESOURCE_ALLOCATION_MANAGER)")
@Mapping(path = "/index", module = "resourceAllocationManager", parameter = "/resourceAllocationManager/mainPage.jsp")
public class RAMApplication extends ForwardAction {

    private static final String BUNDLE = "ResourceAllocationManagerResources";
    private static final String HINT = "Resource Allocation Manager";
    private static final String ACCESS_GROUP = "role(RESOURCE_ALLOCATION_MANAGER)";

    @StrutsApplication(bundle = BUNDLE, path = "periods", titleKey = "link.periods", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class RAMPeriodsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "execution-courses", titleKey = "link.courses.management", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class RAMExecutionCoursesApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "curriculum-historic", titleKey = "label.curriculumHistoric", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class RAMCurriculumHistoricApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "first-year-shifts", titleKey = "label.firstYearShifts", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class RAMFirstYearShiftsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "schedules", titleKey = "link.schedules.management", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class RAMSchedulesApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "evaluations", titleKey = "link.writtenEvaluationManagement", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class RAMEvaluationsApp {
    }

    // Faces Entry Points

    @StrutsFunctionality(app = RAMEvaluationsApp.class, path = "written-evaluations-by-room",
            titleKey = "link.writtenEvaluation.by.room")
    @Mapping(path = "/writtenEvaluations/writtenEvaluationsByRoom", module = "resourceAllocationManager")
    public static class WrittenEvaluationsByRoom extends FacesEntryPoint {
    }

}
