package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.messaging.BoardSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.MessagingApplication.MessagingAnnouncementsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = MessagingAnnouncementsApp.class, path = "boards", titleKey = "messaging.menu.boards.link")
@Mapping(module = "messaging", path = "/announcements/boards")
@Forwards(@Forward(name = "search", path = "/messaging/announcements/searchBoards.jsp"))
public class BoardsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        BoardSearchBean boardSearchBean = getBoardSearchBean(request);
        RenderUtils.invalidateViewState();
        if (boardSearchBean == null) {
            boardSearchBean = new BoardSearchBean();
        }
        request.setAttribute("boardSearchBean", boardSearchBean);
        request.setAttribute("boards", boardSearchBean.getSearchResult());
        return mapping.findForward("search");
    }

    private BoardSearchBean getBoardSearchBean(HttpServletRequest request) {
        final BoardSearchBean boardSearchBean = getRenderedObject();
        return boardSearchBean == null ? (BoardSearchBean) request.getAttribute("boardSearchBean") : boardSearchBean;
    }

}