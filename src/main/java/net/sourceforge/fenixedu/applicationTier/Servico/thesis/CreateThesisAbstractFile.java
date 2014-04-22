package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import java.io.IOException;
import java.util.Locale;

import net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis.ScientificCouncilOrStudentThesisAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;

import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;

public class CreateThesisAbstractFile extends CreateThesisFile {

    @Override
    protected void removePreviousFile(Thesis thesis) {
        ThesisFile extendedAbstract = thesis.getExtendedAbstract();
        if (extendedAbstract != null) {
            if (Authenticate.getUser().getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
                extendedAbstract.deleteWithoutStateCheck();
            } else {
                extendedAbstract.delete();
            }
        }
    }

    @Override
    protected void updateThesis(Thesis thesis, ThesisFile file, String title, String subTitle, Locale language, String fileName,
            byte[] bytes) throws FenixServiceException, IOException {
        thesis.setExtendedAbstract(file);
    }

    // Service Invokers migrated from Berserk

    private static final CreateThesisAbstractFile serviceInstance = new CreateThesisAbstractFile();

    @Atomic
    public static ThesisFile runCreateThesisAbstractFile(Thesis thesis, byte[] bytes, String fileName, String title,
            String subTitle, Locale language) throws FenixServiceException, IOException {
        ScientificCouncilOrStudentThesisAuthorizationFilter.instance.execute(thesis);
        return serviceInstance.run(thesis, bytes, fileName, title, subTitle, language);
    }
}