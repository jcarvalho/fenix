package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.GuiderAlreadyChosenServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis.ChangeMasterDegreeThesisData;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.GuiderAlreadyChosenActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

@Mapping(path = "/changeMasterDegreeThesisLookup", module = "masterDegreeAdministrativeOffice",
        input = "/changeMasterDegreeThesis.do?page=0&method=reloadForm", formBean = "changeMasterDegreeThesisForm")
@Forwards(value = { @Forward(name = "start", path = "/thesis/changeMasterDegreeThesis.jsp"),
        @Forward(name = "success", path = "/thesis/changeSuccessScreen.jsp"),
        @Forward(name = "cancel", path = "/thesis/indexThesis.jsp"),
        @Forward(name = "changeThesisCancel", path = "/thesis/indexChangeThesis.jsp"),
        @Forward(name = "error", path = "/thesis/chooseStudentForMasterDegreeThesisAndProof.jsp") })
@Exceptions(value = {
        @ExceptionHandling(key = "resources.Action.exceptions.NonExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class),
        @ExceptionHandling(key = "resources.Action.exceptions.ExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class),
        @ExceptionHandling(key = "resources.Action.exceptions.RequiredGuidersActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.RequiredGuidersActionException.class),
        @ExceptionHandling(key = "resources.Action.exceptions.GuiderAlreadyChosenActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.GuiderAlreadyChosenActionException.class) })
public class ChangeMasterDegreeThesisLookupDispatchAction extends CreateOrEditMasterDegreeThesisLookupDispatchAction {

    @Override
    protected Map getKeyMethodMap() {
        Map map = super.getKeyMethodMap();
        map.put("button.submit.masterDegree.thesis.changeThesis", "changeMasterDegreeThesis");
        map.put("button.cancel", "cancelChangeMasterDegreeThesis");
        return map;
    }

    public ActionForward cancelChangeMasterDegreeThesis(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return super.cancelMasterDegreeThesis(mapping, form, request, response);
    }

    public ActionForward changeMasterDegreeThesis(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        DynaActionForm createMasterDegreeForm = (DynaActionForm) form;
        User userView = Authenticate.getUser();

        String scpID = (String) createMasterDegreeForm.get("scpID");
        String dissertationTitle = (String) createMasterDegreeForm.get("dissertationTitle");

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();

        try {
            operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
            operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
            operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
                    PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
            operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
                    PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
            operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
                    actionErrors);
        } catch (Exception e1) {
            throw new FenixActionException(e1);
        } finally {
            saveErrors(request, actionErrors);

            if (actionErrors.isEmpty() == false) {
                return mapping.findForward("start");
            }

        }

        try {
            ChangeMasterDegreeThesisData.run(userView, scpID, dissertationTitle,
                    operations.getTeachersNumbers(form, "guidersNumbers"),
                    operations.getTeachersNumbers(form, "assistentGuidersNumbers"),
                    operations.getExternalPersonsIDs(form, "externalGuidersIDs"),
                    operations.getExternalPersonsIDs(form, "externalAssistentGuidersIDs"));
        } catch (GuiderAlreadyChosenServiceException e) {
            throw new GuiderAlreadyChosenActionException(e.getMessage(), mapping.findForward("start"));
        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
        } catch (FenixServiceException e) {
            throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
        }

        return mapping.findForward("success");
    }

}