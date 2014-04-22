package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum ExecutedActionType {
    VIEWED_APPROVED_LEARNING_AGREEMENT, SENT_APPROVED_LEARNING_AGREEMENT, VIEWED_ALERT, SENT_EMAIL_ACCEPTED_STUDENT,
    SENT_EMAIL_FOR_MISSING_REQUIRED_DOCUMENTS, SENT_RECEPTION_EMAIL;

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.CandidateResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

}
