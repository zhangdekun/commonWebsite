package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CopyOfPoiTest {
	public static void main(String[] args) throws Exception {
		InputStream is = new FileInputStream("d:\\scatter_template.xlsx");
		Workbook wb = WorkbookFactory.create(is);
		
		Sheet dictSheet = wb.createSheet("dict");
		//wb.setSheetHidden(wb.getSheetIndex(dictSheet), true);
		int rowIndex = 0;
		Row row = dictSheet.createRow(rowIndex);
		String[] textlist = { "解约驱动机制", "冒小微机制", "小微自演进"};
		for(int i=0;i<textlist.length;i++){
			Cell cell = row.createCell(i);
			cell.setCellValue(textlist[i]);
		}
		rowIndex ++;
		String[] subs = {"红-①单不明确","红-②无用户体验的网络价值","红-③人单酬不合一","红-④存在中间层/隔热墙组织"};
		for(int i=0;i<subs.length;i++){
			Row r = dictSheet.getRow(rowIndex+i);
			if(r==null){
				r = dictSheet.createRow(rowIndex+i);
			}
			Cell c = r.createCell(0);
			c.setCellValue(subs[i]);
		}
		ExcelUtil.createName(wb, "dict1","dict!$A$1:$C$1");
		ExcelUtil.createName(wb, "解约驱动机制", "dict!$A$2:$A$5");
		Sheet sheet = wb.getSheetAt(1);
		int rowNum = sheet.getLastRowNum();
		DataValidation  v1 = ExcelUtil.setDataValidation(sheet,"dict1", 3, rowNum, 3, 3);
		sheet.addValidationData(v1);
		for(int i=3;i<=rowNum;i++){
			DataValidation  v2 = ExcelUtil.setDataValidation(sheet,"INDIRECT($D$"+(i+1)+")", i, i, 4, 4);
			sheet.addValidationData(v2);
		}
		
		FileOutputStream out = new FileOutputStream("d:\\scatter_result.xlsx");
		wb.write(out);
		out.close();
	}
}
