package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadRoomsWithNoExamsInDayAndBeginning;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
@Mapping(path = "/viewEmptyRoomsInDayAndShift", module = "resourceAllocationManager", input = "/viewEmptyRoomsInDayAndShift.jsp")
@Forwards(value = { @Forward(name = "View", path = "/viewEmptyRoomsInDayAndShift.jsp") })
public class ViewEmptyRoomsInDayAndShiftAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = Authenticate.getUser();

        Calendar dateAndTime = (Calendar) request.getAttribute(PresentationConstants.EXAM_DATEANDTIME);

        Calendar date = Calendar.getInstance();
        Calendar time = Calendar.getInstance();

        date.set(Calendar.DAY_OF_MONTH, dateAndTime.get(Calendar.DAY_OF_MONTH));
        date.set(Calendar.MONTH, dateAndTime.get(Calendar.MONTH));
        date.set(Calendar.YEAR, dateAndTime.get(Calendar.YEAR));
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);

        time.set(Calendar.DAY_OF_MONTH, 1);
        time.set(Calendar.MONTH, 1);
        time.set(Calendar.YEAR, 1970);
        time.set(Calendar.HOUR_OF_DAY, dateAndTime.get(Calendar.HOUR_OF_DAY));
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 0);

        // Chamar servico que vai ler salas vazias no dia escolhido

        List infoRoomsList = ReadRoomsWithNoExamsInDayAndBeginning.run(date, time);

        if (infoRoomsList != null && infoRoomsList.isEmpty()) {
            request.removeAttribute(PresentationConstants.INFO_EMPTY_ROOMS_KEY);
        } else {
            request.setAttribute(PresentationConstants.INFO_EMPTY_ROOMS_KEY, infoRoomsList);
            request.setAttribute("dateAndTime", request.getAttribute(PresentationConstants.EXAM_DATEANDTIME_STR));
        }

        return mapping.findForward("View");
    }
}