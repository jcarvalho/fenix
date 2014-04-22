package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.workflow.FillPersonalDataOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.PrintAllDocumentsOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.PrintRegistrationDeclarationOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.PrintScheduleOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.PrintSystemAccessDataOperation;
import net.sourceforge.fenixedu.domain.candidacy.workflow.RegistrationOperation;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import java.util.Locale;

public class IMDCandidacy extends IMDCandidacy_Base {

    public IMDCandidacy(Person person, ExecutionDegree executionDegree) {
        super();
        init(person, executionDegree);
    }

    public IMDCandidacy(final Person person, final ExecutionDegree executionDegree, final Person creator,
            final Double entryGrade, final String contigent, final Ingression ingression, EntryPhase entryPhase) {
        this(person, executionDegree, creator, entryGrade, contigent, ingression, entryPhase, null);
    }

    public IMDCandidacy(final Person person, final ExecutionDegree executionDegree, final Person creator,
            final Double entryGrade, final String contigent, final Ingression ingression, EntryPhase entryPhase,
            Integer placingOption) {
        super();
        init(person, executionDegree, creator, entryGrade, contigent, ingression, entryPhase, placingOption);
    }

    @Override
    public String getDescription() {
        return ResourceBundle.getBundle("resources.CandidateResources", I18N.getLocale()).getString("label.imdCandidacy")
                + " - " + getExecutionDegree().getDegreeCurricularPlan().getName() + " - "
                + getExecutionDegree().getExecutionYear().getYear();
    }

    @Override
    public Set<Operation> getOperations(CandidacySituation candidacySituation) {
        final Set<Operation> operations = new HashSet<Operation>();
        switch (candidacySituation.getCandidacySituationType()) {
        case STAND_BY:
            operations.add(new FillPersonalDataOperation(Collections.singleton(RoleType.CANDIDATE), this));
            break;
        case ADMITTED:
            operations.add(new RegistrationOperation(Collections.singleton(RoleType.CANDIDATE), this));
            break;
        case REGISTERED:
            operations.add(new PrintScheduleOperation(Collections.singleton(RoleType.STUDENT), this));
            operations.add(new PrintRegistrationDeclarationOperation(Collections.singleton(RoleType.STUDENT), this));
            operations.add(new PrintSystemAccessDataOperation(Collections.singleton(RoleType.STUDENT), this));
            //operations.add(new PrintUnder23TransportsDeclarationOperation(RoleType.STUDENT, this));
            //operations.add(new PrintMeasurementTestDateOperation(RoleType.STUDENT, this));
            operations.add(new PrintAllDocumentsOperation(RoleType.STUDENT, this));
            break;
        }
        return operations;
    }

    @Override
    protected void moveToNextState(final CandidacyOperationType operationType, Person person) {
        switch (getActiveCandidacySituation().getCandidacySituationType()) {

        case STAND_BY:
            if (operationType == CandidacyOperationType.FILL_PERSONAL_DATA) {
                new AdmittedCandidacySituation(this, person);
            }
            break;

        case ADMITTED:
            if (operationType == CandidacyOperationType.REGISTRATION) {
                new RegisteredCandidacySituation(this, person);
            }
            break;

        }
    }

    @Override
    public String getDefaultState() {
        return null;
    }

    @Override
    public Map<String, Set<String>> getStateMapping() {
        return null;
    }

}
