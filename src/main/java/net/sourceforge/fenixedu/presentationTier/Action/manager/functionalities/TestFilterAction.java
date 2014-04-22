package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class TestFilterAction extends FenixDispatchAction {

    public ActionForward index(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return selectBean(mapping.findForward("index"), request, new TestFilterBean());
    }

    public ActionForward results(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TestFilterBean bean = getBean();

        if (bean == null) {
            return index(mapping, actionForm, request, response);
        }

        Person person = bean.getTargetPerson();
        if (person == null) {
            addBeanMessage(request, bean);
            return selectBean(mapping.findForward("index"), request, bean);
        }

        List<TestFilterResultBean> results = new ArrayList<TestFilterResultBean>();
        for (Functionality functionality : Functionality.getFunctionalities()) {
            if (functionality.getMatchPath() == null) {
                continue;
            }

            if (functionality.getMatchPath() == null) {
                continue;
            }

            try {
                results.add(new TestFilterResultBean(functionality, functionality.isAvailable(), bean.getParametersMap()));
            } catch (GroupDynamicExpressionException e) {
                results.add(new TestFilterResultBean(functionality, false, bean.getParametersMap()));
            }
        }

        request.setAttribute("results", results);
        return selectBean(mapping.findForward("results"), request, bean);
    }

    private void addBeanMessage(HttpServletRequest request, TestFilterBean bean) {
        if (bean.getPersonName() != null && bean.getPersonName().length() > 0) {
            addMessage(request, "filterTest.person.select.name.invalid");
        } else if (bean.getPersonId() != null) {
            addMessage(request, "filterTest.person.select.id.invalid");
        }
    }

    private void addMessage(HttpServletRequest request, String string) {
        ActionMessages messages = getMessages(request);
        messages.add("selectBean", new ActionMessage(string));
        saveMessages(request, messages);
    }

    private ActionForward selectBean(ActionForward forward, HttpServletRequest request, TestFilterBean bean) {
        request.setAttribute("bean", bean);
        return forward;
    }

    private TestFilterBean getBean() {
        TestFilterBean bean = (TestFilterBean) RenderUtils.getViewState("select-person").getMetaObject().getObject();
        RenderUtils.invalidateViewState("select-person"); // bean will be
        // changed internly

        return bean;
    }
}
