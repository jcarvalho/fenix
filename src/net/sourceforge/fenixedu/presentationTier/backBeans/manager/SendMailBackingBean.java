package net.sourceforge.fenixedu.presentationTier.backBeans.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.EMail;
import net.sourceforge.fenixedu.util.PeriodState;

public class SendMailBackingBean extends FenixBackingBean {

    private static final int MAX_MAIL_RECIPIENTS = 50;

    private static final List<String> EMPTY_LIST = new ArrayList<String>();

    private String from = null;
    private String to = null;
    private String ccs = null;
    private String bccs = null;
    private String subject = null;
    private String message = null;

    private Boolean teachers = null;
    private Boolean employees = null;
    private Boolean degreeStudents = null;
    private Boolean masterDegreeStudents = null;
    private Boolean executionCourseResponsibles = null;
    private Boolean masterDegreeCoordinators = null;
    private Boolean degreeCoordinators = null;

    public void send() throws FenixFilterException, FenixServiceException {
        final List<String> toList = getToList();
        final List<String> ccList = getCCList();
        final List<String> bccList = getBCCList();

        if (!toList.isEmpty() || !ccList.isEmpty()) {
            EMail.send(mailServer(), getFrom(), getFrom(), getSubject(), getToList(), getCCList(), EMPTY_LIST, getMessage());
        }

        if (!bccList.isEmpty()) {
            for (int i = 0; i < bccList.size(); i = i + MAX_MAIL_RECIPIENTS) {
                final List<String> subList = bccList.subList(i, Math.min(bccList.size(), i + MAX_MAIL_RECIPIENTS));
                EMail.send(mailServer(), getFrom(), getFrom(), getSubject(), EMPTY_LIST, EMPTY_LIST, subList, getMessage());
            }
        }
    }

    private List<String> getToList() {
        final List<String> emails = new ArrayList<String>();

        final String to = getTo();
        if (to != null && to.length() > 0) {
            for (final String email : to.split(",")) {
                emails.add(email);
            }
        }
        
        return emails;
    }

    private List<String> getCCList() {
        final List<String> emails = new ArrayList<String>();

        final String ccs = getCcs();
        if (ccs != null && ccs.length() > 0) {
            for (final String email : ccs.split(",")) {
                emails.add(email);
            }
        }
        
        return emails;
    }

    private List<String> getBCCList() throws FenixFilterException, FenixServiceException {
        final List<String> emails = new ArrayList<String>();

        final String bccs = getBccs();
        if (bccs != null && bccs.length() > 0) {
            for (final String email : bccs.split(",")) {
                emails.add(email);
            }
        }

        final Boolean teachers = getTeachers();
        if (teachers.booleanValue()) {
            final Object[] args = { RoleType.TEACHER };
            final Role role = (Role) ServiceUtils.executeService(getUserView(), "ReadRoleByRoleType", args);
            for (final Person person : role.getAssociatedPersons()) {
                if (person.getEmail() != null && person.getEmail().length() > 0) {
                    emails.add(person.getEmail());
                }
            }
        }

        final Boolean employees = getEmployees();
        if (employees.booleanValue()) {
            final Object[] args = { RoleType.EMPLOYEE };
            final Role role = (Role) ServiceUtils.executeService(getUserView(), "ReadRoleByRoleType", args);
            for (final Person person : role.getAssociatedPersons()) {
                if (person.getTeacher() == null) {
                    if (person.getEmail() != null && person.getEmail().length() > 0) {
                        emails.add(person.getEmail());
                    }
                }
            }
        }

        final Boolean degreeStudents = getDegreeStudents();
        final Boolean masterDegreeStudents = getMasterDegreeStudents();
        if (degreeStudents.booleanValue() || masterDegreeStudents.booleanValue()) {
            final Object[] args = { RoleType.STUDENT };
            final Role role = (Role) ServiceUtils.executeService(getUserView(), "ReadRoleByRoleType", args);
            for (final Person person : role.getAssociatedPersons()) {
                Student student = null;
                if (degreeStudents.booleanValue()) {
                    student = person.getStudentByType(DegreeType.DEGREE);
                }
                if (student == null && masterDegreeStudents.booleanValue()) {
                    student = person.getStudentByType(DegreeType.MASTER_DEGREE);
                }
                if (student != null && person.getEmail() != null && person.getEmail().length() > 0) {
                    emails.add(person.getEmail());
                }
            }
        }

        final Boolean executionCourseResponsibles = getExecutionCourseResponsibles();
        if (executionCourseResponsibles.booleanValue()) {
            final Object[] args = { ExecutionYear.class };
            final List<ExecutionYear> executionYears = (List<ExecutionYear>) ServiceUtils.executeService(userView, "ReadAllDomainObjects", args);
            for (final ExecutionYear executionYear : executionYears) {
                if (executionYear.getState().equals(PeriodState.CURRENT)) {
                    for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
                        for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCourses()) {
                            for (final Professorship professorship : executionCourse.getProfessorships()) {
                                if (professorship.getResponsibleFor().booleanValue()) {
                                    final Teacher teacher = professorship.getTeacher();
                                    final Person person = teacher.getPerson();
                                    emails.add(person.getEmail());
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }

        final Boolean degreeCoordinators = getDegreeCoordinators();
        if (degreeCoordinators.booleanValue()) {
            addEmailsForDegreeType(emails, DegreeType.DEGREE);
        }

        final Boolean masterDegreeCoordinators = getMasterDegreeCoordinators();
        if (masterDegreeCoordinators.booleanValue()) {
            addEmailsForDegreeType(emails, DegreeType.MASTER_DEGREE);
        }

        return emails;
    }

    private void addEmailsForDegreeType(final List<String> emails, final DegreeType degreeType) throws FenixServiceException, FenixFilterException {
        final Object[] args = { ExecutionYear.class };
        final List<ExecutionYear> executionYears = (List<ExecutionYear>) ServiceUtils.executeService(userView, "ReadAllDomainObjects", args);
        for (final ExecutionYear executionYear : executionYears) {
            if (executionYear.getState().equals(PeriodState.CURRENT)) {
                for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
                    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                    final Degree degree = degreeCurricularPlan.getDegree();
                    if (degree.getTipoCurso() == degreeType) {
                        for (final Coordinator coordinator : executionDegree.getCoordinatorsList()) {
                            final Teacher teacher = coordinator.getTeacher();
                            final Person person = teacher.getPerson();
                            emails.add(person.getEmail());
                        }
                    }
                }
                break;
            }
        }
    }

    public String mailServer() {
        final String server = ResourceBundle.getBundle("SMTPConfiguration").getString("server.url");
        return (server != null) ? server : "mail.adm";
    }

    public String getBccs() {
        return bccs;
    }
    public void setBccs(String bccs) {
        this.bccs = bccs;
    }
    public String getCcs() {
        return ccs;
    }
    public void setCcs(String ccs) {
        this.ccs = ccs;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public Boolean getTeachers() {
        return teachers;
    }
    public void setTeachers(Boolean teachers) {
        this.teachers = teachers;
    }

    public Boolean getDegreeStudents() {
        return degreeStudents;
    }

    public void setDegreeStudents(Boolean degreeStudents) {
        this.degreeStudents = degreeStudents;
    }

    public Boolean getEmployees() {
        return employees;
    }

    public void setEmployees(Boolean employees) {
        this.employees = employees;
    }

    public Boolean getMasterDegreeStudents() {
        return masterDegreeStudents;
    }

    public void setMasterDegreeStudents(Boolean masterDegreeStudents) {
        this.masterDegreeStudents = masterDegreeStudents;
    }

    public Boolean getExecutionCourseResponsibles() {
        return executionCourseResponsibles;
    }

    public void setExecutionCourseResponsibles(Boolean executionCourseResponsibles) {
        this.executionCourseResponsibles = executionCourseResponsibles;
    }

    public Boolean getDegreeCoordinators() {
        return degreeCoordinators;
    }

    public void setDegreeCoordinators(Boolean degreeCoordinators) {
        this.degreeCoordinators = degreeCoordinators;
    }

    public Boolean getMasterDegreeCoordinators() {
        return masterDegreeCoordinators;
    }

    public void setMasterDegreeCoordinators(Boolean masterDegreeCoordinators) {
        this.masterDegreeCoordinators = masterDegreeCoordinators;
    }

}
