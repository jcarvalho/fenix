package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@CustomGroupOperator("studentSharingDegreeOfCompetenceOfExecutionCourse")
public class PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup extends
        PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup_Base {
    protected PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        super();
        init(executionCourse);
    }

    @Override
    public String getPresentationName() {
        return BundleUtil.getString("resources.SiteResources",
                " label.net.sourceforge.fenixedu.domain.accessControl.CompetenceCourseGroup", getExecutionCourse().getNameI18N()
                        .getContent());
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<User>();
        //TODO: optimize
        for (User user : Bennu.getInstance().getUserSet()) {
            if (isMember(user)) {
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null || user.getPerson().getStudent() == null) {
            return false;
        }
        final Set<CompetenceCourse> competenceCourses = getExecutionCourse().getCompetenceCourses();
        for (Registration registration : user.getPerson().getStudent().getRegistrationsSet()) {
            for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                    CompetenceCourse competenceCourse = enrolment.getCurricularCourse().getCompetenceCourse();
                    if (competenceCourses.contains(competenceCourse)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    public static PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup getInstance(ExecutionCourse executionCourse) {
        PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup instance =
                select(PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : create(executionCourse);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup create(ExecutionCourse executionCourse) {
        PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup instance =
                select(PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : new PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup(executionCourse);
    }
}
