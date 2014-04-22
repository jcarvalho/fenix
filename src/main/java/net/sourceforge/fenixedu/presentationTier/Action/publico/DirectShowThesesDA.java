package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.domain.thesis.ThesisSite;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/theses/thesis", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showThesisDetails", path = "showThesisDetails") })
public class DirectShowThesesDA extends PublicShowThesesDA {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        FunctionalityContext context = FunctionalityContext.getCurrentContext(request);
        if (context.getSelectedContainer() instanceof ThesisSite) {
            ThesisSite site = (ThesisSite) context.getSelectedContainer();
            request.setAttribute("thesis", site.getThesis());
        }
        return mapping.findForward("showThesisDetails");
    }
}
