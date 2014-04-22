package net.sourceforge.fenixedu.presentationTier.Action.operator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.PhotoState;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = OperatorApplication.class, path = "pending-photos", titleKey = "link.operator.photo.pending",
        bundle = "ManagerResources")
@Mapping(module = "operator", path = "/pendingPhotos")
@Forwards(@Forward(name = "list", path = "/operator/photo/listPending.jsp"))
public class PendingPhotosAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        List<Photograph> pending = new ArrayList<Photograph>();
        for (Photograph photo : Bennu.getInstance().getPendingPhotographsSet()) {
            if (photo.getState() == PhotoState.PENDING) {
                pending.add(photo);
            }
        }
        request.setAttribute("pending", pending);
        return mapping.findForward("list");
    }

    public ActionForward accept(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        RenderUtils.invalidateViewState();
        return prepare(mapping, actionForm, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        RenderUtils.invalidateViewState();
        return prepare(mapping, actionForm, request, response);
    }
}
