package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.CurriculumModuleEnroledWrapperConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import java.util.Locale;

public class SpecialSeasonBolonhaStudentEnrolmentBean extends BolonhaStudentEnrollmentBean {

    private static final long serialVersionUID = -7472651937511355140L;

    public SpecialSeasonBolonhaStudentEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester) {
        super(studentCurricularPlan, executionSemester, new SpecialSeasonStudentCurriculumGroupBean(
                studentCurricularPlan.getRoot(), executionSemester), CurricularRuleLevel.SPECIAL_SEASON_ENROLMENT);
    }

    @Override
    public Converter getDegreeModulesToEvaluateConverter() {
        return new CurriculumModuleEnroledWrapperConverter();
    }

    @Override
    public String getFuncionalityTitle() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", I18N.getLocale());
        return resourceBundle.getString("label.special.season.enrolment");
    }

}
