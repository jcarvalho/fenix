package net.sourceforge.fenixedu.domain.phd;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum PhdIndividualProgramCollaborationType {

    CMU,

    UT_AUSTIN,

    MIT,

    EPFL(false, false),

    NONE,

    WITH_SUPERVISION(true),

    ERASMUS_MUNDUS(false),

    OTHER(true);

    private boolean needExtraInformation;
    private boolean generateCandidacyDebt;

    private PhdIndividualProgramCollaborationType(final boolean needExtraInformation, final boolean generateCandidacyDebt) {
        this.needExtraInformation = needExtraInformation;
        this.generateCandidacyDebt = generateCandidacyDebt;
    }

    private PhdIndividualProgramCollaborationType() {
        this(false, true);
    }

    private PhdIndividualProgramCollaborationType(final boolean needExtraInformation) {
        this(needExtraInformation, true);
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    private String getQualifiedName() {
        return PhdIndividualProgramCollaborationType.class.getSimpleName() + "." + name();
    }

    public boolean needExtraInformation() {
        return needExtraInformation;
    }

    public boolean generateCandidacyDebt() {
        return generateCandidacyDebt;
    }

    public static Collection<PhdIndividualProgramCollaborationType> valuesAsList() {
        return Collections.unmodifiableList(Arrays.asList(values()));
    }

}
