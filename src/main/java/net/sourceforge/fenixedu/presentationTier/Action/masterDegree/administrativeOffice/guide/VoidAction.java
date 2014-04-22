/*
 * LogOffAction.java
 *
 * 
 * Created on 09 de Dezembro de 2002, 16:37
 *
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 */
@Mapping(path = "/printGuidePages", module = "masterDegreeAdministrativeOffice")
@Forwards(value = { @Forward(name = "Success", path = "/guide/printGuidePage.jsp") })
public class VoidAction extends Action {

    /**
     * This Action is used when you want to just forward. This exists so that
     * even when you just forward from a link you check to see if the session is
     * valid
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String graduationType = (String) request.getAttribute("graduationType");
        if (graduationType == null) {
            graduationType = request.getParameter("graduationType");
        }
        request.setAttribute("graduationType", graduationType);

        if (request.getParameter(PresentationConstants.REQUESTER_NUMBER) != null) {
            request.setAttribute(PresentationConstants.REQUESTER_NUMBER,
                    new Integer(request.getParameter(PresentationConstants.REQUESTER_NUMBER)));
        }

        return mapping.findForward("Success");

    }

}