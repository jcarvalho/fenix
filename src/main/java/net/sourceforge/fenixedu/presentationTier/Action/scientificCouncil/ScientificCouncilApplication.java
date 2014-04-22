package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.commons.FacesEntryPoint;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = ScientificCouncilApplication.BUNDLE, path = "scientific-council", titleKey = "scientificCouncil",
        accessGroup = ScientificCouncilApplication.ACCESS_GROUP, hint = ScientificCouncilApplication.HINT)
@Mapping(path = "/index", module = "scientificCouncil", parameter = "/scientificCouncil/firstPage.jsp")
public class ScientificCouncilApplication extends ForwardAction {

    static final String HINT = "Scientific Council";
    static final String ACCESS_GROUP = "role(SCIENTIFIC_COUNCIL)";
    static final String BUNDLE = "ScientificCouncilResources";

    @StrutsApplication(bundle = BUNDLE, path = "bolonha-process", titleKey = "bolonha.process", accessGroup = ACCESS_GROUP,
            hint = HINT)
    public static class ScientificBolonhaProcessApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "dissertations", titleKey = "scientificCouncil.thesis.process",
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ScientificDisserationsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "credits", titleKey = "label.credits.navigation.header",
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ScientificCreditsApp {
    }

    @StrutsApplication(bundle = "ResearcherResources", path = "messaging", titleKey = "title.unit.communication.section",
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ScientificCommunicationApp {
    }

    @StrutsApplication(bundle = "CandidateResources", path = "applications", titleKey = "title.applications",
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ScientificApplicationsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "teachers", titleKey = "title.teachers", accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ScientificTeachersApp {
    }

    // Faces Entry Points

    @StrutsFunctionality(app = ScientificBolonhaProcessApp.class, path = "competence-courses",
            titleKey = "navigation.competenceCoursesManagement")
    @Mapping(path = "/competenceCourses/competenceCoursesManagement", module = "scientificCouncil")
    public static class ScientificCompetenceCoursesManagement extends FacesEntryPoint {
    }

    @StrutsFunctionality(app = ScientificBolonhaProcessApp.class, path = "curricular-plans",
            titleKey = "navigation.curricularPlansManagement")
    @Mapping(path = "/curricularPlans/curricularPlansManagement", module = "scientificCouncil")
    public static class ScientificCurricularPlansManagement extends FacesEntryPoint {
    }

}
