package net.sourceforge.fenixedu.domain.phd;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum PhdIndividualProgramDocumentType {

    CANDIDACY_FORM,

    REGISTRATION_FORM,

    CANDIDACY_REVIEW(true, false),

    STUDY_PLAN(true, false),

    CV,

    DEGREE_FINALIZATION_CERTIFICATE(false),

    HABILITATION_CERTIFICATE_DOCUMENT(false),

    DISSERTATION_OR_FINAL_WORK_DOCUMENT(false),

    ID_DOCUMENT,

    SOCIAL_SECURITY,

    RECOMMENDATION_LETTER(false, false),

    HEALTH_BULLETIN,

    GUIDER_ACCEPTANCE_LETTER(true, false),

    ASSISTENT_GUIDER_ACCEPTANCE_LETTER(true, false),

    MOTIVATION_LETTER,

    RESEARCH_PLAN,

    CANDIDACY_RATIFICATION(true, false),

    PUBLIC_PRESENTATION_SEMINAR_COMISSION(true, false),

    PUBLIC_PRESENTATION_SEMINAR_REPORT(true, false),

    PROVISIONAL_THESIS(true, false),

    FINAL_THESIS(true, false),

    FINAL_THESIS_RATIFICATION_DOCUMENT(true, false),

    THESIS_REQUIREMENT,

    THESIS_ABSTRACT(true, false),

    JURY_ELEMENTS(true, false),

    JURY_PRESIDENT_ELEMENT(true, false),

    JURY_REPORT_FEEDBACK(true, false),

    CANDIDACY_FEEDBACK_DOCUMENT(true, false),

    JURY_MEETING_MINUTES(true, false),

    CONCLUSION_DOCUMENT(true, false),

    OTHER(false, false),

    FEEDBACK_REPORT(false, false, true),

    GUIDANCE_OTHER(false, false, true),

    PAYMENT_DOCUMENT(true, false, false),

    TOEFL_LINGUISTICS_CERTIFICATE(true, false, true),

    GRE_LINGUISTICS_CERTIFICATE(true, false, true),

    JURY_PRESIDENT_DECLARATION(true, false, false),

    MAXIMUM_GRADE_GUIDER_PROPOSAL(true, false, false);

    private boolean isVersioned;

    private boolean isAutomaticallyVisibleToStudent;

    private boolean isForGuidance;

    private PhdIndividualProgramDocumentType() {
        this(true, true, false);
    }

    private PhdIndividualProgramDocumentType(boolean isVersioned) {
        this(isVersioned, true, false);
    }

    private PhdIndividualProgramDocumentType(boolean isVersioned, boolean isAutomaticallyVisibleToStudent) {
        this(isVersioned, isAutomaticallyVisibleToStudent, false);
    }

    private PhdIndividualProgramDocumentType(boolean isVersioned, boolean isAutomaticallyVisibleToStudent, boolean isForGuidance) {
        this.isVersioned = isVersioned;
        this.isAutomaticallyVisibleToStudent = isAutomaticallyVisibleToStudent;
        this.isForGuidance = isForGuidance;
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
        return PhdIndividualProgramDocumentType.class.getSimpleName() + "." + name();
    }

    public boolean isVersioned() {
        return isVersioned;
    }

    static public Collection<PhdIndividualProgramDocumentType> getDocumentTypesVisibleToStudent() {
        final Collection<PhdIndividualProgramDocumentType> result = new HashSet<PhdIndividualProgramDocumentType>();

        for (final PhdIndividualProgramDocumentType document : values()) {
            if (document.isAutomaticallyVisibleToStudent) {
                result.add(document);
            }
        }

        return result;
    }

    public boolean isForGuidance() {
        return isForGuidance;
    }

}
