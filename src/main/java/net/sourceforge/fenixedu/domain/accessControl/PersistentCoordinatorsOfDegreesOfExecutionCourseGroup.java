package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@CustomGroupOperator("coordinatorOfDegreesOfExecutionCourse")
public class PersistentCoordinatorsOfDegreesOfExecutionCourseGroup extends
        PersistentCoordinatorsOfDegreesOfExecutionCourseGroup_Base {
    protected PersistentCoordinatorsOfDegreesOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        super();
        init(executionCourse);
    }

    @Override
    public String getPresentationName() {
        return BundleUtil.getString("resources.SiteResources",
                " label.net.sourceforge.fenixedu.domain.accessControl.CoordinatorsOfDegreesOfExecutionCourseGroup",
                getExecutionCourse().getNameI18N().getContent());
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (ExecutionDegree executionDegree : getExecutionCourse().getExecutionDegrees()) {
            for (Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                User user = coordinator.getPerson().getUser();
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
        if (user == null) {
            return false;
        }
        if (!user.getPerson().getCoordinatorsSet().isEmpty()) {
            Set<ExecutionDegree> degrees = getExecutionCourse().getExecutionDegrees();
            for (Coordinator coordinator : user.getPerson().getCoordinatorsSet()) {
                if (degrees.contains(coordinator.getExecutionDegree())) {
                    return true;
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

    public static PersistentCoordinatorsOfDegreesOfExecutionCourseGroup getInstance(ExecutionCourse executionCourse) {
        PersistentCoordinatorsOfDegreesOfExecutionCourseGroup instance =
                select(PersistentCoordinatorsOfDegreesOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : create(executionCourse);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentCoordinatorsOfDegreesOfExecutionCourseGroup create(ExecutionCourse executionCourse) {
        PersistentCoordinatorsOfDegreesOfExecutionCourseGroup instance =
                select(PersistentCoordinatorsOfDegreesOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : new PersistentCoordinatorsOfDegreesOfExecutionCourseGroup(executionCourse);
    }
}
