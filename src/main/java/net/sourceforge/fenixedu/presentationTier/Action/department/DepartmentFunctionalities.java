package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/departmentFunctionalities")
@Forwards({ @Forward(name = "uploadFile", path = "/commons/unitFiles/uploadFile.jsp"),
        @Forward(name = "manageFiles", path = "/commons/unitFiles/manageFiles.jsp"),
        @Forward(name = "editUploaders", path = "/commons/PersistentMemberGroups/configureUploaders.jsp"),
        @Forward(name = "managePersistedGroups", path = "/commons/PersistentMemberGroups/managePersistedGroups.jsp"),
        @Forward(name = "editFile", path = "/commons/unitFiles/editFile.jsp"),
        @Forward(name = "editPersistedGroup", path = "/commons/PersistentMemberGroups/editPersistedGroup.jsp"),
        @Forward(name = "createPersistedGroup", path = "/commons/PersistentMemberGroups/createPersistedGroup.jsp"),
        @Forward(name = "chooseUnit", path = "/departmentMember/chooseUnit.jsp") })
public class DepartmentFunctionalities extends UnitFunctionalities {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute("functionalityAction", "departmentFunctionalities");
        request.setAttribute("module", mapping.getModuleConfig().getPrefix().substring(1));
        return super.execute(mapping, form, request, response);
    }

    @EntryPoint
    public ActionForward chooseUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Unit unit = getUnit(request);
        if (unit != null) {
            return manageFiles(mapping, actionForm, request, response);
        }
        Unit departmentUnit = AccessControl.getPerson().getTeacher().getCurrentWorkingDepartment().getDepartmentUnit();
        Set<Unit> units = new TreeSet<>(Party.COMPARATOR_BY_NAME);
        units.add(departmentUnit);

        for (Unit subUnit : departmentUnit.getAllSubUnits()) {
            if (subUnit.isScientificAreaUnit()) {
                ScientificAreaUnit scientificAreaUnit = (ScientificAreaUnit) subUnit;
                if (scientificAreaUnit.isCurrentUserMemberOfScientificArea()) {
                    units.add(scientificAreaUnit);
                }
            }
        }

        if (units.size() == 1) {
            request.setAttribute("unit", departmentUnit);
            request.setAttribute("unitId", departmentUnit.getExternalId());
            return manageFiles(mapping, actionForm, request, response);
        }

        request.setAttribute("units", units);
        return mapping.findForward("chooseUnit");
    }

}
