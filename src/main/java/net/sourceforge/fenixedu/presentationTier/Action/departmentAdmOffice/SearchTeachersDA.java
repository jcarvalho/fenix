package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.io.OutputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.DepartmentAdmOfficeApp.DepartmentAdmOfficeTeachersApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = DepartmentAdmOfficeTeachersApp.class, path = "search", titleKey = "link.teachers.search")
@Mapping(module = "departmentAdmOffice", path = "/searchTeachers")
public class SearchTeachersDA extends FenixAction {

    private Department getDepartment(final HttpServletRequest request) {
        return getUserView(request).getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=teachers.xls");

        final Department department = getDepartment(request);

        final Spreadsheet spreadsheet = getSpreadsheet();
        for (final Teacher teacher : department.getAllCurrentTeachers()) {
            final Person person = teacher.getPerson();

            final Row row = spreadsheet.addRow();
            row.setCell(teacher.getPerson().getUsername());
            row.setCell(person.getName());
            row.setCell(person.getEmail());
        }

        final OutputStream outputStream = response.getOutputStream();
        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.close();
        return null;
    }

    private Spreadsheet getSpreadsheet() {
        final ResourceBundle enumResourceBundle =
                ResourceBundle.getBundle("resources.ApplicationResources", new Locale("pt", "PT"));
        final Spreadsheet spreadsheet = new Spreadsheet("Teachers");
        spreadsheet.setHeader("Identificação");
        spreadsheet.setHeader(enumResourceBundle.getString("label.person.name"));
        spreadsheet.setHeader(enumResourceBundle.getString("label.person.email"));
        return spreadsheet;
    }

}
