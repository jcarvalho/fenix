/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package ServidorApresentacao.Action.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ChooseExamsMapContextDA extends DispatchAction {

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(true);
		if (session != null) {
			

			InfoExecutionPeriod infoExecutionPeriod = setExecutionContext(request);

			/* Criar o bean de semestres */
			ArrayList semestres = new ArrayList();
			semestres.add(new LabelValueBean("escolher", ""));
			semestres.add(new LabelValueBean("1 �", "1"));
			semestres.add(new LabelValueBean("2 �", "2"));
			request.setAttribute("semesterList", semestres);

			List curricularYearsList = new ArrayList();
			curricularYearsList.add("1");
			curricularYearsList.add("2");
			curricularYearsList.add("3");
			curricularYearsList.add("4");
			curricularYearsList.add("5");
			request.setAttribute(
				"curricularYearList",
				curricularYearsList);

			/* Cria o form bean com as licenciaturas em execucao.*/
			Object argsLerLicenciaturas[] =
				{ infoExecutionPeriod.getInfoExecutionYear()};

			List executionDegreeList =
				(List) ServiceUtils.executeService(
					null,
					"ReadExecutionDegreesByExecutionYear",
					argsLerLicenciaturas);

			ArrayList licenciaturas = new ArrayList();

			licenciaturas.add(new LabelValueBean("escolher", ""));

			Iterator iterator = executionDegreeList.iterator();

			int index = 0;
			while (iterator.hasNext()) {
				InfoExecutionDegree infoExecutionDegree =
					(InfoExecutionDegree) iterator.next();
				String name =
					infoExecutionDegree
						.getInfoDegreeCurricularPlan()
						.getInfoDegree()
						.getNome();
				name
					+= duplicateInfoDegree(
						executionDegreeList,
						infoExecutionDegree)
					? "-"
						+ infoExecutionDegree
							.getInfoDegreeCurricularPlan()
							.getName()
					: "";

				licenciaturas.add(
					new LabelValueBean(name, String.valueOf(index++)));
			}


			request.setAttribute("degreeList", licenciaturas);
			RequestUtils.setExecutionPeriodToRequest(request,infoExecutionPeriod);
			return mapping.findForward("chooseExamsMapContext");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao

	}

	public ActionForward choose(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		DynaActionForm chooseExamContextoForm = (DynaActionForm) form;

		SessionUtils.removeAttributtes(
			session,
			SessionConstants.CONTEXT_PREFIX);

		if (session != null) {
			InfoExecutionPeriod infoExecutionPeriod = RequestUtils.getExecutionPeriodFromRequest(request);
			
			String[] selectedCurricularYears =
				(String[]) chooseExamContextoForm.get(
					"selectedCurricularYears");

			Boolean selectAllCurricularYears =
				(Boolean) chooseExamContextoForm.get(
					"selectAllCurricularYears");

			if ((selectAllCurricularYears != null)
				&& selectAllCurricularYears.booleanValue()) {
				String[] allCurricularYears = { "1", "2", "3", "4", "5" };
				selectedCurricularYears = allCurricularYears;
			}

			List curricularYears =
				new ArrayList(selectedCurricularYears.length);
			for (int i = 0; i < selectedCurricularYears.length; i++)
				curricularYears.add(new Integer(selectedCurricularYears[i]));

			request.setAttribute(
				"curricularYearList",
				curricularYears);

			int index =
				Integer.parseInt((String) chooseExamContextoForm.get("index"));

			Object argsLerLicenciaturas[] =
							{ infoExecutionPeriod.getInfoExecutionYear()};

			List infoExecutionDegreeList =
							(List) ServiceUtils.executeService(
								null,
								"ReadExecutionDegreesByExecutionYear",
								argsLerLicenciaturas);

			
			InfoExecutionDegree infoExecutionDegree =
				(InfoExecutionDegree) infoExecutionDegreeList.get(index);
			
			RequestUtils.setExecutionPeriodToRequest(request,infoExecutionPeriod);
			if (infoExecutionDegree != null) {
				System.out.println("before:"+infoExecutionDegree);
				RequestUtils.setExecutionDegreeToRequest(request,infoExecutionDegree);
				
			} else {
				return mapping.findForward("Licenciatura execucao inexistente");
			}

		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao

		return mapping.findForward("showExamsMap");

	}

	/**
	 * Method setCurricularYearList.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private List setCurricularYearList(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {

		List curricularYearList =
			(List) request.getAttribute(
				"curricularYearList");

		if (curricularYearList == null) {
			curricularYearList = new ArrayList();
			curricularYearList.add(new LabelValueBean("1�", "1"));
			curricularYearList.add(new LabelValueBean("2�", "2"));
			curricularYearList.add(new LabelValueBean("3�", "3"));
			curricularYearList.add(new LabelValueBean("4�", "4"));
			curricularYearList.add(new LabelValueBean("5�", "5"));
			request.setAttribute(
				"curricularYearList",
				curricularYearList);
		}
		return curricularYearList;
	}
	/**
	 * Method setSemesterList.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private List setSemesterList(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {

		List semesterList =
			(List) request.getAttribute("semesterList");

		if (semesterList == null) {

			semesterList = new ArrayList();
			semesterList.add(new LabelValueBean("1�", "1"));
			semesterList.add(new LabelValueBean("2�", "2"));
			request.getSession(false).setAttribute(
				"semesterList",
				semesterList);
		}
		return semesterList;
	}
	/**
	 * Method setInfoDegreeList.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private List setInfoDegreeList(HttpServletRequest request)
		throws Exception {

		List infoExecutionDegreeList = null;
		InfoExecutionPeriod infoExecutionPeriod = setExecutionContext(request);
		Object args[] = { infoExecutionPeriod.getInfoExecutionYear()};
		infoExecutionDegreeList =
			(List) ServiceUtils.executeService(
				null,
				"ReadExecutionDegreesByExecutionYear",
				args);

		request.setAttribute(
			"degreeList",
			infoExecutionDegreeList);

		return infoExecutionDegreeList;
	}

	/**
	 * Method existencesOfInfoDegree.
	 * @param executionDegreeList
	 * @param infoExecutionDegree
	 * @return int
	 */
	private boolean duplicateInfoDegree(
		List executionDegreeList,
		InfoExecutionDegree infoExecutionDegree) {
		InfoDegree infoDegree =
			infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
		Iterator iterator = executionDegreeList.iterator();

		while (iterator.hasNext()) {
			InfoExecutionDegree infoExecutionDegree2 =
				(InfoExecutionDegree) iterator.next();
			if (infoDegree
				.equals(
					infoExecutionDegree2
						.getInfoDegreeCurricularPlan()
						.getInfoDegree())
				&& !(infoExecutionDegree.equals(infoExecutionDegree2)))
				return true;

		}
		return false;
	}
	/**
	 * Method setExecutionContext.
	 * @param request
	 */
	private InfoExecutionPeriod setExecutionContext(HttpServletRequest request)
		throws Exception {
		IUserView userView = SessionUtils.getUserView(request);
		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) ServiceUtils.executeService(
				userView,
				"ReadActualExecutionPeriod",
				new Object[0]);		

		RequestUtils.setExecutionPeriodToRequest(request,infoExecutionPeriod);
		return infoExecutionPeriod;
	}

}
