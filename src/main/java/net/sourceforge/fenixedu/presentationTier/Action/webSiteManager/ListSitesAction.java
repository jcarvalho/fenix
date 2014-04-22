package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "PortalResources", path = "website-manager", titleKey = "portal.webSiteManager.name",
        hint = "Website Manager", accessGroup = "role(WEBSITE_MANAGER)")
@Mapping(module = "webSiteManager", path = "/index")
@Forwards(@Forward(name = "list", path = "/webSiteManager/firstPage.jsp"))
public class ListSitesAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = getLoggedPerson(request);

        SortedSet<UnitSite> sites = new TreeSet<UnitSite>(new Comparator<UnitSite>() {
            @Override
            public int compare(UnitSite o1, UnitSite o2) {
                return o1.getUnit().getName().compareTo(o2.getUnit().getName());
            }
        });
        sites.addAll(person.getUnitSitesSet());
        request.setAttribute("sites", sites);

        return mapping.findForward("list");
    }

}
