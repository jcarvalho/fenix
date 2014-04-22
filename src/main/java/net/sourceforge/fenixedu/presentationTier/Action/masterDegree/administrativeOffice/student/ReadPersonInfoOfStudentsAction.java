package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.general.ReadAllCountries;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.ReadStudentByNumberAndType;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/editStudentToVisualizeInformations", module = "masterDegreeAdministrativeOffice",
        formBean = "changePersonalInfoForm")
@Forwards(value = { @Forward(name = "Success", path = "/student/editStudentInfo.jsp") })
public class ReadPersonInfoOfStudentsAction extends FenixAction {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer studentNumber = new Integer(getFromRequest("studentNumber", request));
        // String graduationType = getFromRequest("graduationType",
        // request);

        InfoStudent infoStudent = null;

        infoStudent = (InfoStudent) ReadStudentByNumberAndType.run(studentNumber, DegreeType.MASTER_DEGREE);

        if (infoStudent == null) {
            ActionErrors errors = new ActionErrors();
            errors.add("nonExisting", new ActionError("error.exception.noStudents"));
            saveErrors(request, errors);
            return mapping.findForward("NoStudent");
        }
        request.setAttribute("infoStudent", infoStudent);
        request.setAttribute("studentNumber", studentNumber);
        request.setAttribute("infoPerson", infoStudent.getInfoPerson());

        DynaActionForm changeApplicationInfoForm = (DynaActionForm) form;
        populateForm(changeApplicationInfoForm, infoStudent.getInfoPerson());

        // Get List of available Countries
        Object result = null;
        result = ReadAllCountries.run();
        List country = (ArrayList) result;

        // Build List of Countries for the Form
        Iterator iterador = country.iterator();

        List nationalityList = new ArrayList();
        while (iterador.hasNext()) {
            InfoCountry countryTemp = (InfoCountry) iterador.next();
            nationalityList.add(new LabelValueBean(countryTemp.getNationality(), countryTemp.getNationality()));
        }

        request.setAttribute(PresentationConstants.NATIONALITY_LIST_KEY, nationalityList);
        request.setAttribute(PresentationConstants.SEX_LIST_KEY,
                Gender.getSexLabelValues((Locale) request.getAttribute(Globals.LOCALE_KEY)));
        request.setAttribute(PresentationConstants.MONTH_DAYS_KEY, Data.getMonthDays());
        request.setAttribute(PresentationConstants.MONTH_LIST_KEY, Data.getMonths());
        request.setAttribute(PresentationConstants.YEARS_KEY, Data.getYears());
        request.setAttribute(PresentationConstants.EXPIRATION_YEARS_KEY, Data.getExpirationYears());

        return mapping.findForward("Success");

    }

    public static String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

    public static void populateForm(DynaActionForm changeApplicationInfoForm, InfoPerson infoPerson) {
        changeApplicationInfoForm.set("identificationDocumentNumber", infoPerson.getNumeroDocumentoIdentificacao());
        if (infoPerson.getTipoDocumentoIdentificacao() != null) {
            changeApplicationInfoForm.set("identificationDocumentType", infoPerson.getTipoDocumentoIdentificacao().toString());
        }
        changeApplicationInfoForm.set("identificationDocumentIssuePlace", infoPerson.getLocalEmissaoDocumentoIdentificacao());
        changeApplicationInfoForm.set("name", infoPerson.getNome());

        Calendar birthDate = Calendar.getInstance();
        if (infoPerson.getNascimento() == null) {
            changeApplicationInfoForm.set("birthDay", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("birthMonth", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("birthYear", Data.OPTION_DEFAULT);
        } else {
            birthDate.setTime(infoPerson.getNascimento());
            changeApplicationInfoForm.set("birthDay", new Integer(birthDate.get(Calendar.DAY_OF_MONTH)).toString());
            changeApplicationInfoForm.set("birthMonth", new Integer(birthDate.get(Calendar.MONTH)).toString());
            changeApplicationInfoForm.set("birthYear", new Integer(birthDate.get(Calendar.YEAR)).toString());
        }

        Calendar identificationDocumentIssueDate = Calendar.getInstance();
        if (infoPerson.getDataEmissaoDocumentoIdentificacao() == null) {
            changeApplicationInfoForm.set("idIssueDateDay", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idIssueDateMonth", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idIssueDateYear", Data.OPTION_DEFAULT);
        } else {
            identificationDocumentIssueDate.setTime(infoPerson.getDataEmissaoDocumentoIdentificacao());
            changeApplicationInfoForm.set("idIssueDateDay",
                    new Integer(identificationDocumentIssueDate.get(Calendar.DAY_OF_MONTH)).toString());
            changeApplicationInfoForm.set("idIssueDateMonth",
                    new Integer(identificationDocumentIssueDate.get(Calendar.MONTH)).toString());
            changeApplicationInfoForm.set("idIssueDateYear",
                    new Integer(identificationDocumentIssueDate.get(Calendar.YEAR)).toString());
        }

        Calendar identificationDocumentExpirationDate = Calendar.getInstance();
        if (infoPerson.getDataValidadeDocumentoIdentificacao() == null) {
            changeApplicationInfoForm.set("idExpirationDateDay", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idExpirationDateMonth", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idExpirationDateYear", Data.OPTION_DEFAULT);
        } else {
            identificationDocumentExpirationDate.setTime(infoPerson.getDataValidadeDocumentoIdentificacao());
            changeApplicationInfoForm.set("idExpirationDateDay",
                    new Integer(identificationDocumentExpirationDate.get(Calendar.DAY_OF_MONTH)).toString());
            changeApplicationInfoForm.set("idExpirationDateMonth",
                    new Integer(identificationDocumentExpirationDate.get(Calendar.MONTH)).toString());
            changeApplicationInfoForm.set("idExpirationDateYear",
                    new Integer(identificationDocumentExpirationDate.get(Calendar.YEAR)).toString());
        }

        changeApplicationInfoForm.set("fatherName", infoPerson.getNomePai());
        changeApplicationInfoForm.set("motherName", infoPerson.getNomeMae());
        changeApplicationInfoForm.set("nationality", infoPerson.getInfoPais().getNationality());
        changeApplicationInfoForm.set("birthPlaceParish", infoPerson.getFreguesiaNaturalidade());
        changeApplicationInfoForm.set("birthPlaceMunicipality", infoPerson.getConcelhoNaturalidade());
        changeApplicationInfoForm.set("birthPlaceDistrict", infoPerson.getDistritoNaturalidade());
        changeApplicationInfoForm.set("address", infoPerson.getMorada());
        changeApplicationInfoForm.set("place", infoPerson.getLocalidade());
        changeApplicationInfoForm.set("postCode", infoPerson.getCodigoPostal());
        changeApplicationInfoForm.set("addressParish", infoPerson.getFreguesiaMorada());
        changeApplicationInfoForm.set("addressMunicipality", infoPerson.getConcelhoMorada());
        changeApplicationInfoForm.set("addressDistrict", infoPerson.getDistritoMorada());
        changeApplicationInfoForm.set("telephone", infoPerson.getTelefone());
        changeApplicationInfoForm.set("mobilePhone", infoPerson.getTelemovel());
        changeApplicationInfoForm.set("email", infoPerson.getEmail());
        changeApplicationInfoForm.set("webSite", infoPerson.getEnderecoWeb());
        changeApplicationInfoForm.set("contributorNumber", infoPerson.getNumContribuinte());
        changeApplicationInfoForm.set("occupation", infoPerson.getProfissao());
        changeApplicationInfoForm.set("username", infoPerson.getUsername());
        changeApplicationInfoForm.set("areaOfAreaCode", infoPerson.getLocalidadeCodigoPostal());

        if (infoPerson.getSexo() != null) {
            changeApplicationInfoForm.set("sex", infoPerson.getSexo().toString());
        }
        if (infoPerson.getMaritalStatus() != null) {
            changeApplicationInfoForm.set("maritalStatus", infoPerson.getMaritalStatus().toString());
        }
    }

}