package net.sourceforge.fenixedu.presentationTier.docs.candidacy.erasmus;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import net.sourceforge.fenixedu.util.FenixStringTools;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class LearningAgreementDocument extends FenixReport {

    MobilityIndividualApplicationProcess process;

    static final protected char END_CHAR = ' ';
    static final protected int LINE_LENGTH = 70;
    static final protected String LINE_BREAK = "\n";

    public LearningAgreementDocument(MobilityIndividualApplicationProcess process) {
        this.process = process;
        fillReport();
    }

    public LearningAgreementDocument(MobilityIndividualApplicationProcess process, Locale locale) {
        super(locale);
        this.process = process;
        fillReport();
    }

    @Override
    protected void fillReport() {
        addParameter("mobilityProgram", process.getMobilityProgram().getName().getContent(MultiLanguageString.en));
        addParameter("academicYear", process.getCandidacyExecutionInterval().getName());
        addParameter("studentName", process.getPersonalDetails().getName());
        addParameter("sendingInstitution", process.getCandidacy().getMobilityStudentData().getSelectedOpening()
                .getMobilityAgreement().getUniversityUnit().getNameI18n().getContent());

        addParameter("desiredEnrollments", getChosenSubjectsInformation());
    }

    private String getChosenSubjectsInformation() {
        StringBuilder result = new StringBuilder();

        for (CurricularCourse course : process.getCandidacy().getCurricularCourses()) {
            result.append(
                    FenixStringTools.multipleLineRightPadWithSuffix(course.getNameI18N().getContent(MultiLanguageString.en), LINE_LENGTH,
                            END_CHAR, course.getEctsCredits().toString())).append(LINE_BREAK);
        }

        return result.toString();
    }

    @Override
    public String getReportFileName() {
        return "learning_agreement_" + process.getCandidacy().getPersonalDetails().getDocumentIdNumber();
    }

}
