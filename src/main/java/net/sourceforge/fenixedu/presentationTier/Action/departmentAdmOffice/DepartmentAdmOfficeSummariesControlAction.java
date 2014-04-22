package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.DepartmentAdmOfficeApp.DepartmentAdmOfficeViewApp;
import net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.SummariesControlAction;

import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DepartmentAdmOfficeViewApp.class, path = "summaries-control", titleKey = "link.summaries.control",
        bundle = "ApplicationResources")
@Mapping(module = "departmentAdmOffice", path = "/summariesControl")
@Forwards(@Forward(name = "success", path = "/departmentAdmOffice/summariesControl/listTeacherSummariesControl.jsp"))
public class DepartmentAdmOfficeSummariesControlAction extends SummariesControlAction {

    @Override
    protected void readAndSaveAllDepartments(HttpServletRequest request) throws FenixServiceException {

        List<LabelValueBean> departments = new ArrayList<LabelValueBean>();
        final User userView = Authenticate.getUser();
        Person person = userView.getPerson();
        Collection<Department> manageableDepartments = person.getManageableDepartmentCreditsSet();
        for (Department department : manageableDepartments) {
            LabelValueBean bean = new LabelValueBean();
            bean.setLabel(department.getRealName());
            bean.setValue(department.getExternalId().toString());
            departments.add(bean);
        }
        request.setAttribute("departments", departments);
    }

}
