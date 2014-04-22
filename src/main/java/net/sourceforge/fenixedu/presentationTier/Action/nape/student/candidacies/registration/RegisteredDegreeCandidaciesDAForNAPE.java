package net.sourceforge.fenixedu.presentationTier.Action.nape.student.candidacies.registration;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.candidacy.registrations.RegisteredDegreeCandidaciesDA;
import net.sourceforge.fenixedu.presentationTier.Action.nape.NapeApplication.NapeRegisteredCandidaciesApp;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = NapeRegisteredCandidaciesApp.class, path = "candidacies",
        titleKey = "label.registeredDegreeCandidacies.first.time.list")
@Mapping(path = "/registeredDegreeCandidacies", module = "nape")
@Forwards({ @Forward(name = "viewRegisteredDegreeCandidacies",
        path = "/nape/student/candidacies/registration/viewRegisteredDegreeCandidacies.jsp") })
public class RegisteredDegreeCandidaciesDAForNAPE extends RegisteredDegreeCandidaciesDA {

    @Override
    protected Set<Degree> getDegreesToSearch() {
        return Bennu.getInstance().getDegreesSet();
    }

}
