/*
 * Created on Jan 12, 2005
 *
 */
package DataBeans.projectsManagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import DataBeans.DataTranferObject;
import ServidorAplicacao.IUserView;
import Util.projectsManagement.ExcelStyle;
import Util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoCoordinatorReport extends DataTranferObject {
    private List lines;

    private InfoRubric infoCoordinator;

    public InfoRubric getInfoCoordinator() {
        return infoCoordinator;
    }

    public void setInfoCoordinator(InfoRubric infoCoordinator) {
        this.infoCoordinator = infoCoordinator;
    }

    public List getLines() {
        return lines;
    }

    public void setLines(List lines) {
        this.lines = lines;
    }

    public Integer getLinesSize() {
        return new Integer(lines.size());
    }

    public void getReportToExcel(IUserView userView, HSSFWorkbook wb, ReportType reportType) {
        HSSFSheet sheet = wb.createSheet(infoCoordinator.getCode());
        sheet.setGridsPrinted(false);
        ExcelStyle excelStyle = new ExcelStyle(wb);

        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(reportType.getReportLabel());
        cell.setCellStyle(ExcelStyle.TITLE_STYLE);

        row = sheet.createRow((short) 2);
        cell = row.createCell((short) 0);
        cell.setCellValue("Coordenador:");
        cell.setCellStyle(ExcelStyle.LABEL_STYLE);
        cell = row.createCell((short) 1);
        cell.setCellValue(infoCoordinator.getDescription());
        cell.setCellStyle(ExcelStyle.VALUE_STYLE);
        row = sheet.createRow((short) 3);
        cell = row.createCell((short) 0);
        cell.setCellValue("Data:");
        cell.setCellStyle(ExcelStyle.LABEL_STYLE);
        cell = row.createCell((short) 1);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy '�s' HH:mm");
        cell.setCellValue(formatter.format(new Date()));
        cell.setCellStyle(ExcelStyle.VALUE_STYLE);

        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) ((IReportLine) lines.get(0)).getNumberOfColumns()));
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            sheet.addMergedRegion(new Region(i, (short) 1, i, (short) (((IReportLine) lines.get(0)).getNumberOfColumns())));
        }

        int lastRowNum = sheet.getLastRowNum() + 2;
        if (lines != null && lines.size() > 0) {
            row = sheet.createRow(lastRowNum);
            ((IReportLine) lines.get(0)).getHeaderToExcel(sheet);
            lastRowNum++;
            for (int i = 0; i < lines.size(); i++) {
                ((IReportLine) lines.get(i)).getLineToExcel(sheet);
            }
            ((IReportLine) lines.get(0)).getTotalLineToExcel(sheet);
        }

        row = sheet.createRow((short) sheet.getLastRowNum() + 2);
        row.setHeight((short) 0x349);
        cell = row.createCell((short) 0);
        cell.setCellValue(reportType.getReportNote());
        cell.setCellStyle(ExcelStyle.VALUE_STYLE);
        sheet.addMergedRegion(new Region(sheet.getLastRowNum(), (short) 0, sheet.getLastRowNum(), (short) ((IReportLine) lines.get(0))
                .getNumberOfColumns()));
    }
}
