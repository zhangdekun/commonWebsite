package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class PoiTest {
	public static void main(String[] args) throws IOException {
		InputStream is = new FileInputStream("d:\\scatter4.xls");
		HSSFWorkbook wb = new HSSFWorkbook(is);
		HSSFSheet dictSheet = wb.createSheet("dict");
		int rowIndex = 0;
		HSSFRow row = dictSheet.createRow(rowIndex);
		String[] textlist = { "解约驱动机制", "冒小微机制", "小微自演进"};
		for(int i=0;i<textlist.length;i++){
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(textlist[i]);
		}
		rowIndex ++;
		String[] subs = {"红-①单不明确","红-②无用户体验的网络价值","红-③人单酬不合一","红-④存在中间层/隔热墙组织"};
		for(int i=0;i<subs.length;i++){
			HSSFRow r = dictSheet.getRow(rowIndex+i);
			if(r==null){
				r = dictSheet.createRow(rowIndex+i);
			}
			HSSFCell c = r.createCell(0);
			c.setCellValue(subs[i]);
		}
		ExcelUtil.createName(wb, "dict1","dict!$A$1:$C$1");
		ExcelUtil.createName(wb, "解约驱动机制", "dict!$A$2:$A$5");
		HSSFSheet sheet = wb.getSheetAt(1);
		int rowNum = sheet.getLastRowNum();
		HSSFDataValidation  v1 = ExcelUtil.setDataValidation("dict1", 3, rowNum, 3, 3);
		sheet.addValidationData(v1);
		for(int i=3;i<=rowNum;i++){
			HSSFDataValidation  v2 = ExcelUtil.setDataValidation("INDIRECT($D$"+(i+1)+")", i, i, 4, 4);
			sheet.addValidationData(v2);
		}
		
		FileOutputStream out = new FileOutputStream("d:\\scatter_result.xls");
		wb.write(out);
		out.close();
	}
}
