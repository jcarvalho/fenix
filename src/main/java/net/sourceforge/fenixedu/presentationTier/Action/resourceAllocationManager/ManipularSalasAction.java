package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixSelectedRoomsContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.Util;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Nuno Antão
 * @author João Pereira
 */

@Mapping(path = "/selectRoomToViewForExams", module = "resourceAllocationManager", input = "/listRoomsForExams.jsp",
        attribute = "indexForm", formBean = "indexForm")
@Forwards(value = { @Forward(name = "VerSala", path = "/viewRoomForExams.do") })
@Exceptions(value = { @ExceptionHandling(key = "resources.Action.exceptions.notAuthorizedActionDeleteException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.notAuthorizedActionDeleteException.class) })
public class ManipularSalasAction extends FenixSelectedRoomsContextAction {

    /**
     * Executes the selected action, depending on the pressed button. The action
     * depends on the value of the "operation" parameter. It can be prepare the
     * information about the selected sala, to show or edit it, or delete the
     * selected sala.
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String parameter = request.getParameter("operation");

        if (parameter.equals("Ver Sala")) {
            return prepararVerSala(mapping, form, request, response);
        }
        if (parameter.equals("Editar Sala")) {
            return prepararEditarSala(mapping, form, request, response);
        }

        return (mapping.findForward("Voltar"));
    }

    /**
     * Prepares the right information about the selected sala so that it can be
     * shown to the user. Places a java bean object with information about the
     * selected sala in the attribute "salaFormBean" of the request and fowards
     * to the mapping "VerSala".
     */
    public ActionForward prepararVerSala(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        request.removeAttribute(mapping.getAttribute());

        DynaActionForm posicaoSalaFormBean = (DynaActionForm) form;
        Integer index = (Integer) posicaoSalaFormBean.get("index");

        request.setAttribute("selectedRoomIndex", index);
        request.setAttribute("roomId", index.toString());

        // Reset indexForm value
        DynaActionForm selectRoomIndexForm = (DynaActionForm) form;
        selectRoomIndexForm.set("index", null);

        return mapping.findForward("VerSala");
    }

    /**
     * Prepares the information about the selected sala so that it can be shown
     * to the user. Places a java bean object with information about the
     * selected sala in the attribute "salaFormBean" of the request and forwards
     * to the mapping "EditarSala".
     */
    public ActionForward prepararEditarSala(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.execute(mapping, form, request, response);

        InfoRoom salaBean = getSelectedSala(form, request);

        DynaActionForm posicaoSalaFormBean = (DynaActionForm) form;
        Integer index = (Integer) posicaoSalaFormBean.get("index");
        request.setAttribute(PresentationConstants.SELECTED_ROOM_INDEX, index);

        List edificios = Util.readExistingBuldings(null, null);
        List tipos = Util.readTypesOfRooms(null, null);

        request.setAttribute("publico.buildings", edificios);
        request.setAttribute("publico.types", tipos);

        // create the bean that holds the information about the sala to edit
        DynaActionFormClass cl;
        ModuleConfig moduleConfig = mapping.getModuleConfig();
        FormBeanConfig formBeanConfig = moduleConfig.findFormBeanConfig("roomForm");
        cl = DynaActionFormClass.createDynaActionFormClass(formBeanConfig);
        DynaActionForm criarSalaForm = (DynaActionForm) cl.newInstance();
        criarSalaForm.set("capacityNormal", String.valueOf(salaBean.getCapacidadeNormal()));
        criarSalaForm.set("capacityExame", String.valueOf(salaBean.getCapacidadeExame()));
        request.setAttribute("criarSalaForm", criarSalaForm);

        // Reset indexForm value
        DynaActionForm selectRoomIndexForm = (DynaActionForm) form;
        selectRoomIndexForm.set("index", null);

        return (mapping.findForward("EditarSala"));
    }

    /**
     * @returns the name of the selected sala.
     */
    private InfoRoom getSelectedSala(ActionForm form, HttpServletRequest request) {

        DynaActionForm posicaoSalaFormBean = (DynaActionForm) form;
        Integer salaSelecionada = (Integer) posicaoSalaFormBean.get("index");

        List listaSalasBean = (List) request.getAttribute(PresentationConstants.SELECTED_ROOMS);

        InfoRoom sala = null;
        if (listaSalasBean != null && !listaSalasBean.isEmpty()) {
            Collections.sort(listaSalasBean);
            sala = (InfoRoom) listaSalasBean.get(salaSelecionada.intValue());
        }

        return sala;
    }

}