package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.candidacy.registrations;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;

@Mapping(path = "/registeredDegreeCandidacies", module = "academicAdministration")
@Forwards({ @Forward(name = "viewRegisteredDegreeCandidacies",
        path = "/academicAdminOffice/student/candidacies/registration/viewRegisteredDegreeCandidacies.jsp") })
public class RegisteredDegreeCandidaciesDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        RegisteredDegreeCandidaciesSelectionBean registeredDegreeCandidaciesSelectionBean =
                getRegisteredDegreeCandidaciesSelectionBean();
        if (registeredDegreeCandidaciesSelectionBean == null) {
            request.setAttribute("bean", new RegisteredDegreeCandidaciesSelectionBean());
            return mapping.findForward("viewRegisteredDegreeCandidacies");
        }

        List<StudentCandidacy> studentCandidacies = registeredDegreeCandidaciesSelectionBean.search(getDegreesToSearch());
        request.setAttribute("studentCandidacies", studentCandidacies);

        return mapping.findForward("viewRegisteredDegreeCandidacies");
    }

    public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        RegisteredDegreeCandidaciesSelectionBean registeredDegreeCandidaciesSelectionBean =
                getRegisteredDegreeCandidaciesSelectionBean();
        Spreadsheet export = registeredDegreeCandidaciesSelectionBean.export(getDegreesToSearch());

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition",
                "attachment; filename=" + registeredDegreeCandidaciesSelectionBean.getFilename());
        export.exportToXLSSheet(response.getOutputStream());

        return null;
    }

    public ActionForward exportWithApplyForResidence(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        RegisteredDegreeCandidaciesSelectionBean registeredDegreeCandidaciesSelectionBean =
                getRegisteredDegreeCandidaciesSelectionBean();
        RegisteredDegreeCandidaciesWithApplyForResidence forResidence =
                new RegisteredDegreeCandidaciesWithApplyForResidence(registeredDegreeCandidaciesSelectionBean);

        Spreadsheet spreadsheet = forResidence.export(getDegreesToSearch());
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + forResidence.getFilename());
        spreadsheet.exportToXLSSheet(response.getOutputStream());

        return null;
    }

    private RegisteredDegreeCandidaciesSelectionBean getRegisteredDegreeCandidaciesSelectionBean() {
        return getRenderedObject("bean");
    }

    protected Set<Degree> getDegreesToSearch() {
        return AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(),
                AcademicOperationType.MANAGE_REGISTERED_DEGREE_CANDIDACIES);
    }
}
