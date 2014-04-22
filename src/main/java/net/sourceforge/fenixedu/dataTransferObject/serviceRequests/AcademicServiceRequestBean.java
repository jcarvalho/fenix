package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import java.io.Serializable;
import java.util.Collection;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import java.util.Locale;

public class AcademicServiceRequestBean implements Serializable {

    protected AcademicServiceRequestSituationType academicServiceRequestSituationType;

    private AcademicServiceRequest request;

    private String justification;

    private YearMonthDay situationDate;

    protected Integer serviceRequestYear;

    private Person responsible;

    protected AcademicServiceRequestBean() {
        super();
    }

    public AcademicServiceRequestBean(final AcademicServiceRequest request,
            final AcademicServiceRequestSituationType situationType) {
        this();
        setAcademicServiceRequest(request);
        setAcademicServiceRequestSituationType(situationType);
        setSituationDate(new YearMonthDay());
    }

    public AcademicServiceRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType,
            final Person responsible) {
        this();
        setAcademicServiceRequestSituationType(academicServiceRequestSituationType);
        setResponsible(responsible);
        setSituationDate(new YearMonthDay());
    }

    public AcademicServiceRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType,
            final Person responsible, final Integer serviceRequestYear) {
        this(academicServiceRequestSituationType, responsible);
        setServiceRequestYear(serviceRequestYear);
    }

    public AcademicServiceRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType,
            final Person responsible, final String justification) {
        this(academicServiceRequestSituationType, responsible);
        setJustification(justification);
    }

    public AcademicServiceRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType,
            final Person responsible, final YearMonthDay situationDate, final String justification) {
        this(academicServiceRequestSituationType, responsible, justification);
        setSituationDate(situationDate);
    }

    public AcademicServiceRequestBean(final Person responsible, final String justification) {
        this((AcademicServiceRequestSituationType) null, responsible, justification);
    }

    public AcademicServiceRequestSituationType getAcademicServiceRequestSituationType() {
        return academicServiceRequestSituationType;
    }

    public void setAcademicServiceRequestSituationType(AcademicServiceRequestSituationType academicServiceRequestSituationType) {
        this.academicServiceRequestSituationType = academicServiceRequestSituationType;
    }

    public boolean hasAcademicServiceRequestSituationType() {
        return this.academicServiceRequestSituationType != null;
    }

    DateTime finalSituationDate;

    public DateTime getFinalSituationDate() {
        if (finalSituationDate == null) {
            return getSituationDate().toDateTimeAtCurrentTime();
        }

        return finalSituationDate;
    }

    public void setFinalSituationDate(final DateTime finalSituationDate) {
        this.finalSituationDate = finalSituationDate;
    }

    public YearMonthDay getSituationDate() {
        return situationDate;
    }

    public void setSituationDate(YearMonthDay situationDate) {
        this.situationDate = situationDate;
    }

    public Person getResponsible() {
        return responsible;
    }

    public void setResponsible(Person responsible) {
        this.responsible = responsible;
    }

    private AcademicServiceRequest getAcademicServiceRequest() {
        return request;
    }

    private void setAcademicServiceRequest(final AcademicServiceRequest request) {
        this.request = request;
    }

    public String getJustification() {
        if (StringUtils.isEmpty(justification) && getAcademicServiceRequest().isDocumentRequest()
                && ((DocumentRequest) getAcademicServiceRequest()).isDiploma()) {
            if (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.CONCLUDED) {
                return ResourceBundle.getBundle("resources.AcademicAdminOffice", I18N.getLocale()).getString(
                        "DiplomaRequest.diploma.concluded");
            }
            if (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY) {
                return ResourceBundle.getBundle("resources.AcademicAdminOffice", I18N.getLocale()).getString(
                        "DiplomaRequest.diploma.sent");
            }
            if (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY) {
                return ResourceBundle.getBundle("resources.AcademicAdminOffice", I18N.getLocale()).getString(
                        "DiplomaRequest.diploma.received");
            }
        }

        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public boolean hasJustification() {
        return !StringUtils.isEmpty(this.justification);
    }

    public Integer getServiceRequestYear() {
        return serviceRequestYear;
    }

    public void setServiceRequestYear(Integer serviceRequestYear) {
        this.serviceRequestYear = serviceRequestYear;
    }

    public boolean isNew() {
        return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.NEW;
    }

    public boolean isToProcess() {
        return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.PROCESSING;
    }

    public boolean isToDeliver() {
        return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.DELIVERED;
    }

    public boolean isToCancel() {
        return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.CANCELLED;
    }

    public boolean isToReject() {
        return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.REJECTED;
    }

    public boolean isToConclude() {
        return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.CONCLUDED;
    }

    public boolean isToSendToExternalEntity() {
        return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY;
    }

    public boolean isToReceiveFromExternalUnit() {
        return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY;
    }

    public boolean isToCancelOrReject() {
        return isToCancel() || isToReject();
    }

    public Collection<AcademicServiceRequest> searchAcademicServiceRequests() {
        return AcademicAuthorizationGroup.getAcademicServiceRequests(AccessControl.getPerson(), serviceRequestYear,
                academicServiceRequestSituationType, null);
    }
}
