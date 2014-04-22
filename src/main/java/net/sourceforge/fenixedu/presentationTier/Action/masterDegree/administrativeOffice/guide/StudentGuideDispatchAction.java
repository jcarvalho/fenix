package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.InfoPrice;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidInformationInFormActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NoChangeMadeActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
@Mapping(path = "/studentGuideDispatchAction", module = "masterDegreeAdministrativeOffice",
        input = "/guide/chooseStudentGuide.jsp", attribute = "chooseStudentGuideForm", formBean = "chooseStudentGuideForm")
@Forwards(value = { @Forward(name = "CreateStudentGuideReady", path = "/guide/createGuideReady.jsp") })
@Exceptions(
        value = {
                @ExceptionHandling(
                        key = "resources.Action.exceptions.InvalidInformationInFormActionException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidInformationInFormActionException.class),
                @ExceptionHandling(key = "resources.Action.exceptions.NoChangeMadeActionException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NoChangeMadeActionException.class) })
public class StudentGuideDispatchAction extends FenixDispatchAction {

    private CreateGuideBean getCreateGuideBean() {
        return getRenderedObject("createGuideBean");
    }

    public ActionForward createReady(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm studentGuideForm = (DynaActionForm) form;
        final CreateGuideBean createGuideBean = getCreateGuideBean();

        String[] quantityList = request.getParameterValues("quantityList");

        String specializationGratuityQuantityString = (String) studentGuideForm.get("specializationGratuityQuantity");
        String specializationGratuityAmountString = (String) studentGuideForm.get("specializationGratuityAmount");
        if (specializationGratuityAmountString.equals("0.0")) {
            specializationGratuityAmountString = "0";
        }

        String graduationType = (String) request.getAttribute("graduationType");
        if (graduationType == null) {
            graduationType = request.getParameter("graduationType");
        }
        request.setAttribute("graduationType", graduationType);

        String requester = (String) studentGuideForm.get("requester");
        if (requester == null) {
            requester = (String) request.getAttribute(PresentationConstants.REQUESTER_TYPE);
        }
        if (requester == null) {
            requester = request.getParameter(PresentationConstants.REQUESTER_TYPE);
        }
        request.setAttribute(PresentationConstants.REQUESTER_TYPE, requester);

        String othersGratuityAmountString = (String) studentGuideForm.get("othersGratuityAmount");
        Integer othersGratuityAmount = null;

        if (othersGratuityAmountString.equals("0.0")) {
            othersGratuityAmountString = "0";
        }

        if ((othersGratuityAmountString != null) && (othersGratuityAmountString.length() > 0)) {
            try {
                othersGratuityAmount = new Integer(othersGratuityAmountString);
                if (othersGratuityAmount.intValue() < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                throw new InvalidInformationInFormActionException(new Throwable());
            }
        }
        String othersGratuityDescription = (String) studentGuideForm.get("othersGratuityDescription");

        final List<InfoPrice> certificateList = createGuideBean.getInfoPrices();
        Iterator<InfoPrice> iterator = certificateList.iterator();

        int position = 0;
        final InfoGuide infoGuide = createGuideBean.getInfoGuide();

        infoGuide.setInfoGuideEntries(new ArrayList());

        while (iterator.hasNext()) {
            iterator.next();
            Integer quantity = null;

            try {
                quantity = new Integer(quantityList[position]);
                if (quantity.intValue() < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                throw new InvalidInformationInFormActionException(new Throwable());
            }

            if (quantity.intValue() > 0) {
                InfoPrice infoPrice = certificateList.get(position);
                InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
                infoGuideEntry.setDescription(infoPrice.getDescription());
                infoGuideEntry.setDocumentType(infoPrice.getDocumentType());
                infoGuideEntry.setGraduationType(infoPrice.getGraduationType());
                infoGuideEntry.setPrice(infoPrice.getPrice());
                infoGuideEntry.setQuantity(quantity);
                infoGuide.getInfoGuideEntries().add(infoGuideEntry);
            }

            position++;

        }

        if ((specializationGratuityAmountString != null) && (specializationGratuityAmountString.length() != 0)
                && (specializationGratuityQuantityString != null) && (specializationGratuityQuantityString.length() != 0)) {
            InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
            infoGuideEntry.setDescription("Pagamento para Especialização");
            infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE);
            infoGuideEntry.setDocumentType(DocumentType.GRATUITY);
            infoGuideEntry.setPrice(new Double(specializationGratuityAmountString));

            infoGuideEntry.setQuantity(new Integer(specializationGratuityQuantityString));
            createGuideBean.getInfoGuide().getInfoGuideEntries().add(infoGuideEntry);
        }

        if (othersGratuityAmount != null) {
            InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
            infoGuideEntry.setDescription(othersGratuityDescription);
            infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE);
            infoGuideEntry.setDocumentType(DocumentType.GRATUITY);
            infoGuideEntry.setPrice(new Double(othersGratuityAmountString));
            infoGuideEntry.setQuantity(new Integer(1));
            infoGuide.getInfoGuideEntries().add(infoGuideEntry);
        }

        if (createGuideBean.getInfoGuide().getInfoGuideEntries().size() == 0) {
            throw new NoChangeMadeActionException("error.exception.noCertificateChosen");
        }

        generateToken(request);
        saveToken(request);

        request.setAttribute("createGuideBean", createGuideBean);

        return mapping.findForward("CreateStudentGuideReady");
    }

}