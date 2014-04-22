/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeResultsBean;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlMenu;
import pt.ist.fenixWebFramework.renderers.components.HtmlMenuOption;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryDelegateCoursesResumeRenderer extends InquiryBlocksResumeRenderer {

    @Override
    protected void createHeaderFinalCells(final HtmlTableRow headerRow) {
        final HtmlTableCell fillingStatus = headerRow.createCell(CellType.HEADER);
        fillingStatus.setBody(new HtmlText("Estado do preenchimento"));
        fillingStatus.setClasses("col-fill");

        final HtmlTableCell finalCell = headerRow.createCell(CellType.HEADER);
        finalCell.setClasses("col-actions");
    }

    @Override
    protected void createFinalCells(HtmlTableRow tableRow, BlockResumeResult blockResumeResult) {
        HtmlTableCell fillingStatus = tableRow.createCell();
        CurricularCourseResumeResult courseResumeResult = (CurricularCourseResumeResult) blockResumeResult;
        fillingStatus.setBody(new HtmlText(courseResumeResult.getCompletionState()));
        fillingStatus.setClasses("col-fill");

        HtmlInlineContainer container = new HtmlInlineContainer();
        HtmlTableCell linksCell = tableRow.createCell();
        String fillInParameters = buildFillInParameters(courseResumeResult);
        String resultsParameters = buildParametersForResults(courseResumeResult);

        HtmlLink link = new HtmlLink();
        link.setUrl("/delegateInquiry.do?" + resultsParameters + "&method=viewCourseInquiryResults");
        link.setEscapeAmpersand(false);

        HtmlMenu menu = new HtmlMenu();
        menu.setOnChange("var value=this.options[this.selectedIndex].value; this.selectedIndex=0; if(value!= ''){ window.open(value,'_blank'); }");
        menu.setStyle("width: 150px");
        HtmlMenuOption optionEmpty = menu.createOption("-- Ver resultados --");
        HtmlMenuOption optionUC = menu.createOption("Resultados UC");
        String calculatedUrl = link.calculateUrl();
        optionUC.setValue(calculatedUrl
                + "&_request_checksum_="
                + pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter
                        .calculateChecksum(calculatedUrl));

        for (TeacherShiftTypeResultsBean teacherShiftTypeResultsBean : courseResumeResult.getTeachersResults()) {
            String teacherResultsParameters = buildParametersForTeacherResults(teacherShiftTypeResultsBean);
            HtmlLink teacherLink = new HtmlLink();
            teacherLink.setEscapeAmpersand(false);
            teacherLink.setUrl("/delegateInquiry.do?" + teacherResultsParameters + "&method=viewTeacherShiftTypeInquiryResults");
            calculatedUrl = teacherLink.calculateUrl();

            HtmlMenuOption optionTeacher =
                    menu.createOption(teacherShiftTypeResultsBean.getShiftType().getFullNameTipoAula() + " - "
                            + teacherShiftTypeResultsBean.getProfessorship().getPerson().getName());
            optionTeacher.setValue(calculatedUrl
                    + "&_request_checksum_="
                    + pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter
                            .calculateChecksum(calculatedUrl));
        }

        container.addChild(menu);

        container.addChild(new HtmlText("&nbsp;|&nbsp;", false));

        HtmlLink fillInlink = new HtmlLink();
        fillInlink.setUrl("/delegateInquiry.do?" + fillInParameters + "&method=showFillInquiryPage");
        fillInlink.setText("Preencher");
        container.addChild(fillInlink);

        linksCell.setBody(container);
        linksCell.setClasses("col-actions");
    }

    private String buildParametersForTeacherResults(TeacherShiftTypeResultsBean teacherShiftTypeResultsBean) {
        StringBuilder builder = new StringBuilder();
        builder.append("shiftType=").append(teacherShiftTypeResultsBean.getShiftType().name());
        builder.append("&professorshipOID=").append(teacherShiftTypeResultsBean.getProfessorship().getExternalId());
        return builder.toString();
    }

    private String buildFillInParameters(CurricularCourseResumeResult courseResumeResult) {
        StringBuilder builder = new StringBuilder();
        builder.append("yearDelegateOID=").append(courseResumeResult.getYearDelegate().getExternalId());
        builder.append("&executionDegreeOID=").append(courseResumeResult.getExecutionDegree().getExternalId());
        builder.append("&executionCourseOID=").append(courseResumeResult.getExecutionCourse().getExternalId());
        return builder.toString();
    }

    private String buildParametersForResults(CurricularCourseResumeResult courseResumeResult) {
        StringBuilder builder = new StringBuilder();
        builder.append("degreeCurricularPlanOID=").append(
                courseResumeResult.getExecutionDegree().getDegreeCurricularPlan().getExternalId());
        builder.append("&executionCourseOID=").append(courseResumeResult.getExecutionCourse().getExternalId());
        return builder.toString();
    }
}
