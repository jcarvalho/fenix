package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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

@Mapping(path = "/createMasterDegreeThesis", module = "masterDegreeAdministrativeOffice",
        formBean = "createMasterDegreeThesisForm", validate = false)
@Forwards(value = { @Forward(name = "start", path = "/thesis/createMasterDegreeThesis.jsp"),
        @Forward(name = "error", path = "/thesis/chooseStudentForMasterDegreeThesisAndProof.jsp") })
@Exceptions(value = { @ExceptionHandling(key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class) })
public class CreateMasterDegreeThesisDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentForCreateMasterDegreeThesis(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();
        boolean isSuccess = operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        if (isSuccess) {
            return mapping.findForward("start");
        }
        throw new NonExistingActionException("error.exception.masterDegree.nonExistentStudent", mapping.findForward("error"));

    }

    public ActionForward reloadForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();

        try {
            operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
            operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
                    PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
            operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
            operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
                    PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
            operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
                    actionErrors);

        } catch (Exception e1) {
            throw new FenixActionException(e1);
        }

        return mapping.findForward("start");

    }

}