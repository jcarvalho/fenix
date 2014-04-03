package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@CustomGroupOperator("studentSharingDegreeOfExecutionCourse")
public class PersistentStudentSharingDegreeOfExecutionCourseGroup extends
        PersistentStudentSharingDegreeOfExecutionCourseGroup_Base {
    protected PersistentStudentSharingDegreeOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        super();
        init(executionCourse);
    }

    @Override
    public String getPresentationName() {
        return BundleUtil.getString("resources.SiteResources",
                " label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroup", getExecutionCourse()
                        .getNameI18N().getContent());
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        Set<Degree> degrees = new HashSet<>();
        for (CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCoursesSet()) {
            degrees.add(curricularCourse.getDegree());
        }
        for (Degree degree : degrees) {
            for (Registration registration : degree.getActiveRegistrations()) {
                User user = registration.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
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
        Set<Degree> degrees = new HashSet<>();
        for (CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCoursesSet()) {
            degrees.add(curricularCourse.getDegree());
        }
        for (Registration registration : user.getPerson().getStudent().getRegistrationsSet()) {
            if (degrees.contains(registration.getDegree())) {
                return true;
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

    public static PersistentStudentSharingDegreeOfExecutionCourseGroup getInstance(ExecutionCourse executionCourse) {
        PersistentStudentSharingDegreeOfExecutionCourseGroup instance =
                select(PersistentStudentSharingDegreeOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : create(executionCourse);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentStudentSharingDegreeOfExecutionCourseGroup create(ExecutionCourse executionCourse) {
        PersistentStudentSharingDegreeOfExecutionCourseGroup instance =
                select(PersistentStudentSharingDegreeOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : new PersistentStudentSharingDegreeOfExecutionCourseGroup(executionCourse);
    }

}
