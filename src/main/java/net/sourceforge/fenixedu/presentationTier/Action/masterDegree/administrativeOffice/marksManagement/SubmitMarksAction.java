package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.marksManagement;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement.InsertStudentsFinalEvaluation;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement.ReadStudentsAndMarksByCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério 01/07/2003
 * 
 */
@Mapping(path = "/marksSubmission", module = "masterDegreeAdministrativeOffice",
        input = "/marksSubmission.do?method=prepare&page=0", formBean = "marksSubmissionForm")
@Forwards(value = { @Forward(name = "MarksSubmission", path = "/marksManagement/marksSubmission.jsp"),
        @Forward(name = "ShowMarksManagementMenu", path = "/marksManagement.do?method=getStudentMarksList") })
@Exceptions(value = { @ExceptionHandling(key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class) })
public class SubmitMarksAction extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(SubmitMarksAction.class);

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String curricularCourseCode = MarksManagementDispatchAction.getFromRequest("courseId", request);
        MarksManagementDispatchAction.getFromRequest("objectCode", request);
        MarksManagementDispatchAction.getFromRequest("degreeId", request);

        // Get students List

        User userView = Authenticate.getUser();
        InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = null;
        try {
            infoSiteEnrolmentEvaluation = ReadStudentsAndMarksByCurricularCourse.run(curricularCourseCode, null);
        } catch (NonExistingServiceException e) {
            sendErrors(request, "nonExisting", "message.masterDegree.notfound.students");
            return mapping.findForward("ShowMarksManagementMenu");
        } catch (ExistingServiceException e) {
            sendErrors(request, "existing", "message.masterDegree.evaluation.alreadyConfirmed");
            return mapping.findForward("ShowMarksManagementMenu");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(infoSiteEnrolmentEvaluation.getEnrolmentEvaluations(), new BeanComparator(
                "infoEnrolment.infoStudentCurricularPlan.infoStudent.number"));

        setForm(form, infoSiteEnrolmentEvaluation);

        request.setAttribute("infoSiteEnrolmentEvaluation", infoSiteEnrolmentEvaluation);

        return mapping.findForward("MarksSubmission");
    }

    private void setForm(ActionForm form, InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation) {
        DynaValidatorForm submitMarksForm = (DynaValidatorForm) form;
        InfoTeacher infoTeacher = infoSiteEnrolmentEvaluation.getInfoTeacher();
        if (infoTeacher != null && infoTeacher.getTeacherId() != null) {
            // fill in teacher number in case it exists
            submitMarksForm.set("teacherId", infoTeacher.getTeacherId().toString());
        }
        if (infoSiteEnrolmentEvaluation.getLastEvaluationDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setLenient(false);
            calendar.setTime(infoSiteEnrolmentEvaluation.getLastEvaluationDate());

            submitMarksForm.set("day", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            submitMarksForm.set("month", String.valueOf(calendar.get(Calendar.MONTH) + 1));
            submitMarksForm.set("year", String.valueOf(calendar.get(Calendar.YEAR)));
        }
    }

    private void sendErrors(HttpServletRequest request, String arg0, String arg1) {
        ActionErrors errors = new ActionErrors();
        errors.add(arg0, new ActionError(arg1));
        saveErrors(request, errors);
    }

    public ActionForward submit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        MarksManagementDispatchAction.getFromRequest("courseId", request);
        MarksManagementDispatchAction.getFromRequest("objectCode", request);
        MarksManagementDispatchAction.getFromRequest("degreeId", request);

        // transform form into list with student's code and students's grade
        final Collection<InfoEnrolmentEvaluation> infoEnrolmentEvaluations = new HashSet<InfoEnrolmentEvaluation>();
        final Integer sizeList = Integer.valueOf(MarksManagementDispatchAction.getFromRequest("sizeList", request));
        for (int i = 0; i < sizeList.intValue(); i++) {
            final InfoEnrolmentEvaluation infoEnrolmentEvaluation = getFinalEvaluation(request, i);
            if (infoEnrolmentEvaluation != null) {
                infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
            }
        }

        final DynaValidatorForm marksForm = (DynaValidatorForm) form;

        int year = Integer.valueOf((String) marksForm.get("year")).intValue();
        int month = Integer.valueOf((String) marksForm.get("month")).intValue();
        int day = Integer.valueOf((String) marksForm.get("day")).intValue();
        final Date evaluationDate = new YearMonthDay(year, month, day).toDateMidnight().toDate();
        if (evaluationDate.after(Calendar.getInstance().getTime())) {
            sendErrors(request, "nonExisting", "message.masterDegree.evaluation.invalidDate");
            return mapping.findForward("ShowMarksManagementMenu");
        }

        final String teacherId = (String) marksForm.get("teacherId");
        final ActionErrors actionErrors = new ActionErrors();

        for (final InfoEnrolmentEvaluation infoEnrolmentEvaluation : infoEnrolmentEvaluations) {
            try {

                InsertStudentsFinalEvaluation.run(infoEnrolmentEvaluation, teacherId, evaluationDate);

            } catch (DomainException e) {
                actionErrors.add(e.getKey(), new ActionError(e.getKey(), e.getArgs()));

            } catch (FenixServiceException e) {
                logger.error(e.getMessage(), e);
                throw new FenixActionException(e);
            }
        }

        if (!actionErrors.isEmpty()) {
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        return mapping.findForward("ShowMarksManagementMenu");
    }

    private InfoEnrolmentEvaluation getFinalEvaluation(HttpServletRequest request, int index) {
        String studentCode = null;
        String enrolmentCode = null;
        String evaluationId = null;
        String evaluation = request.getParameter("enrolmentEvaluation[" + index + "].grade");

        if (!StringUtils.isEmpty(evaluation) && request.getParameter("enrolmentEvaluation[" + index + "].studentCode") != null) {
            studentCode = request.getParameter("enrolmentEvaluation[" + index + "].studentCode");

            enrolmentCode = request.getParameter("enrolmentEvaluation[" + index + "].enrolmentCode");

            evaluationId = request.getParameter("enrolmentEvaluation[" + index + "].externalId");

        }

        if (studentCode != null) {

            final Enrolment enrolment = (Enrolment) FenixFramework.getDomainObject(enrolmentCode);
            final InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
            infoEnrolmentEvaluation.setInfoEnrolment(InfoEnrolment.newInfoFromDomain(enrolment));

            infoEnrolmentEvaluation.setGradeValue(evaluation);
            infoEnrolmentEvaluation.setExternalId(evaluationId);
            return infoEnrolmentEvaluation;
        }

        return null;
    }

    // private ActionErrors getErrors(final Collection<InfoEnrolmentEvaluation>
    // errors) {
    // final ActionErrors actionErrors = new ActionErrors();
    //
    // for (final InfoEnrolmentEvaluation error : errors) {
    // actionErrors.add("invalidGrade", new ActionError("errors.invalidMark",
    // error.getGradeValue(), String.valueOf(error
    // .getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent().getNumber().intValue())));
    // }
    //
    // return actionErrors;
    // }

}