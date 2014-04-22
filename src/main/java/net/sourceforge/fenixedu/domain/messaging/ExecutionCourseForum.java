package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExecutionCourseForum extends ExecutionCourseForum_Base {

    public ExecutionCourseForum() {
        super();
    }

    public ExecutionCourseForum(MultiLanguageString name, MultiLanguageString description) {
        this();
        init(name, description);
    }

    @Override
    public void setName(MultiLanguageString name) {
        if (this.getExecutionCourse() != null) {
            getExecutionCourse().checkIfCanAddForum(name);
        }

        super.setName(name);
    }

    @Override
    public Group getReadersGroup() {
        return getExecutionCourseMembersGroup();
    }

    @Override
    public Group getWritersGroup() {
        return getExecutionCourseMembersGroup();
    }

    @Override
    public Group getAdminGroup() {
        return new ExecutionCourseTeachersGroup(getExecutionCourse());
    }

    private Group getExecutionCourseMembersGroup() {
        return new GroupUnion(new ExecutionCourseTeachersGroup(getExecutionCourse()), new ExecutionCourseStudentsGroup(
                getExecutionCourse()));
    }

}
