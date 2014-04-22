package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.person.PersonApplication.PersonalAreaApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@StrutsFunctionality(app = PersonalAreaApp.class, descriptionKey = "label.validate.email", path = "validate-email",
        titleKey = "label.validate.email")
@Mapping(module = "person", path = "/validateEmail", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showForm", path = "/person/validateEmail.jsp", tileProperties = @Tile(
        title = "private.personal.dspace.emailconfirm")) })
public class ValidateEmailDA extends FenixDispatchAction {

    public static class ValidateEmailForm {
        private String validationString;

        public String getValidationString() {
            return validationString;
        }

        public void setValidationString(String validationString) {
            this.validationString = validationString;
        }
    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("showForm");
    }

    public ActionForward redirect(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final ActionForward actionForward = new ActionForward();
        actionForward.setRedirect(true);

        final ValidateEmailForm validateEmailForm = getRenderedObject();
        if (validateEmailForm == null) {
            return prepare(mapping, actionForm, request, response);
        }

        final StringBuilder path = new StringBuilder();
        path.append("https://ciist.ist.utl.pt/servicos/self_service/verify.php?hash=");
        path.append(validateEmailForm.getValidationString());
        actionForward.setPath(path.toString());

        return actionForward;
    }

}
