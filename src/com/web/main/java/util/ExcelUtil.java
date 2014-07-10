package util;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtil {
	/**
	 * 设置数据有效性（通过名称管理器级联相关）
	 * 
	 * @param name
	 * @param firstRow
	 * @param endRow
	 * @param firstCol
	 * @param endCol
	 * @return
	 */
	public static HSSFDataValidation setDataValidation(String name,
			int firstRow, int endRow, int firstCol, int endCol) {
		// 设置下拉列表的内容
		// 加载下拉列表内容
		DVConstraint constraint = DVConstraint
				.createFormulaListConstraint(name);
		// 设置数据有效性加载在哪个单元格上。
		// 四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(
				(short) firstRow, (short) endRow, (short) firstCol,
				(short) endCol);
		// 数据有效性对象
		HSSFDataValidation data_validation = new HSSFDataValidation(regions,
				constraint);
		return data_validation;
	}

	public static DataValidation setDataValidation(Sheet sheet, String name,
			int firstRow, int endRow, int firstCol, int endCol) {
		// 设置下拉列表的内容
		// 加载下拉列表内容
		DataValidationConstraint dataValidationConstraint = sheet
				.getDataValidationHelper().createFormulaListConstraint(name);
		// 设置数据有效性加载在哪个单元格上。
		// 四个参数分别是：起始行、终止行、起始列、终止列
		@SuppressWarnings("deprecation")
		CellRangeAddressList regions = new CellRangeAddressList(
				(short) firstRow, (short) endRow, (short) firstCol,
				(short) endCol);
		// 数据有效性对象
		DataValidation data_validation = sheet.getDataValidationHelper()
				.createValidation(dataValidationConstraint, regions);
		return data_validation;
	}

	/**
	 * 创建名称
	 * 
	 * @param wb
	 * @param name
	 * @param expression
	 * @return
	 */
	public static HSSFName createName(HSSFWorkbook wb, String name,
			String expression) {
		HSSFName refer = wb.createName();
		refer.setRefersToFormula(expression);
		refer.setNameName(name);
		return refer;
	}
	public static Name createName(Workbook wb, String name,
			String expression) {
		Name refer = wb.createName();
		refer.setRefersToFormula(expression);
		refer.setNameName(name);
		return refer;
	}
}
