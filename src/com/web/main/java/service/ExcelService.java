package service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import util.Config;
import util.ExcelUtil;
import controller.PTController;
import dao.localDomain.LocalPTDAO;
import dao.localDomain.model.ScatterEntry;
import dao.localDomain.model.ScatterMeta;
import dao.remoteDomain.RemotePTDAO;
import dao.remoteDomain.model.PtsvDim;

@Service
public class ExcelService {
	@Autowired
	private LocalPTDAO localPt;
	@Autowired
	private RemotePTDAO remotePt;

	public boolean excelTodb(Workbook wb , Map<String,Object> map){
		boolean flg = true;
		if(wb ==null){
			flg = false;
		}else{
			try{
				
				Sheet summary = wb.getSheetAt(0);
				Sheet content = wb.getSheetAt(1);
				String code = (String)map.get("ptsv_code");
				PtsvDim ptsv = remotePt.getOne(code);
				Row  periodr = summary.getRow(2);
				Cell periodc = periodr.getCell(1);
				String period_id ="-1"; 
				try{
					period_id=periodc.getStringCellValue();
				}catch(Exception e ){
					period_id = periodc.getNumericCellValue()+"";
				}
				int period_idInt = Integer.parseInt(period_id);
				String org_type = ptsv.getOrg_type();
				String ptsv_type = ptsv.getNotes();
				Map<String,String> nameToCodeMappings = scatterEntryMap(map,ptsv_type);
				String snro_shiji = Config.getReal();
				String snro_yuqi = Config.getExpect();
				int rowIndex = 3;
				int maxNum = content.getLastRowNum();
				List<ScatterEntry> entryList = new ArrayList<ScatterEntry>();
				for(int i=rowIndex;i<=maxNum;i++){
					Row row = content.getRow(i);
					if(row == null){
						row = content.createRow(i);
					}
					//ptsv_code
					String ptsv_code = null;
					String ptsv_name = null;
					String y_type_name_s =null;
					String y_name_s = null;
					String x_name_s = null;
					String y_type_name_y = null;
					String y_name_y =null;
					String x_name_y = null;
					Cell ptsvCell = row.getCell(0);
					if(ptsvCell != null){
						ptsv_code = ptsvCell.getStringCellValue();
					}
					Cell ptsv_nameCell = row.getCell(2);
					if(ptsv_nameCell !=null){
						ptsv_name = ptsv_nameCell.getStringCellValue();
					}
					Cell y_type_name_sCell = row.getCell(3);
					if(y_type_name_sCell !=null){
						y_type_name_s = y_type_name_sCell.getStringCellValue();
					}
					Cell y_name_sCell = row.getCell(4);
					if(y_name_sCell !=null){
						y_name_s = y_name_sCell.getStringCellValue();
					}
					Cell x_name_sCell = row.getCell(5);
					if(x_name_sCell !=null){
						x_name_s = x_name_sCell.getStringCellValue();
					}
					Cell y_type_name_yCell = row.getCell(6);
					if(y_type_name_yCell !=null){
						y_type_name_y =y_type_name_yCell.getStringCellValue();  
					}
					Cell y_name_yCell = row.getCell(7);
					if(y_name_yCell !=null){
						y_name_y = y_name_yCell.getStringCellValue();
					}
					Cell x_name_yCell = row.getCell(8);
					if(x_name_yCell !=null){
						x_name_y = x_name_yCell.getStringCellValue();
					}
					//构造scatter entry
					ScatterEntry shijiEntry = new ScatterEntry();
					shijiEntry.setOrg_type(org_type);
					shijiEntry.setPeriod_id(period_idInt);
					shijiEntry.setPtsv_code(ptsv_code);
					shijiEntry.setPtsv_name(ptsv_name);
					String s_code_x_s = nameToCodeMappings.get(x_name_s);
					String s_code_y_s = nameToCodeMappings.get(y_type_name_s+"_"+y_name_s);
					shijiEntry.setS_code_x(s_code_x_s);
					shijiEntry.setS_code_y(s_code_y_s);
					shijiEntry.setSnro_code(snro_shiji);
					ScatterEntry yuqiEntry = new ScatterEntry();
					yuqiEntry.setOrg_type(org_type);
					yuqiEntry.setPeriod_id(period_idInt);
					yuqiEntry.setPtsv_code(ptsv_code);
					yuqiEntry.setPtsv_name(ptsv_name);
					String s_code_x_y = nameToCodeMappings.get(x_name_y);
					String s_code_y_y = nameToCodeMappings.get(y_type_name_y+"_"+y_name_y);
					yuqiEntry.setS_code_x(s_code_x_y);
					yuqiEntry.setS_code_y(s_code_y_y);
					yuqiEntry.setSnro_code(snro_yuqi);
					entryList.add(shijiEntry);
					entryList.add(yuqiEntry);
				}
				// 数据入仓
				fillToDb(entryList,map);
			}catch(Exception e){
				flg = false;
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return flg;
	}
	private Map<String,String> scatterEntryMap(Map<String,Object> map,String ptsv_type){
		Map<String,String> mappings = new HashMap<String,String>();
		List<ScatterMeta> x_List = localPt.getFirstLevelScatterMetaX(ptsv_type);
		List<ScatterMeta> y_List = localPt.getFirstLevelScatterMetaY(ptsv_type);
		List<ScatterMeta> children = localPt.getScatterMetaChildren(ptsv_type);
		Map<String,String> y_Map = new HashMap<String,String>();
		if(y_List !=null){
			for(ScatterMeta sm : y_List){
				y_Map.put(sm.getS_code(), sm.getName());
			}
		}
		if(x_List !=null){
			for(ScatterMeta sm:x_List){
				mappings.put(sm.getName(), sm.getS_code());
			}
		}
		if(children !=null){
			for(ScatterMeta sm:children){
				String name = sm.getName();
				mappings.put(y_Map.get(sm.getPs_code())+"_"+name, sm.getS_code());
			}
		}
		return mappings;
	}
	@Transactional(value="local",readOnly=false,propagation=Propagation.REQUIRED)
	private void fillToDb(List<ScatterEntry> list,Map<String,Object> map){
		if(list == null || list.size()==0){
			return ;
		}
		List<PtsvDim> pdList = this.getPtsvDimList(map);
		List<String> ptsv_codes = new ArrayList<String>();
		ptsv_codes.add("-10000");// 设置一个不可能存在的code
		if (pdList != null) {
			for (PtsvDim pd : pdList) {
				ptsv_codes.add(pd.getPtsv_code());
			}
		}
		map.put("ptsv_codes", ptsv_codes);
		localPt.deleteByPtsvCodeAndPeriod(map);
		List<ScatterEntry> filterList = new ArrayList<ScatterEntry>();
		for(ScatterEntry en : list){
			if(en.getPtsv_code() !=null && en.getS_code_x() !=null && en.getS_code_y() !=null && en.getSnro_code() !=null){
				filterList.add(en);
			}
		}
		localPt.insertScatterEntrys(filterList);
	}
	public Workbook getScatterExcelTemplate(Map<String, Object> map) {
		Workbook wb = null;
		try {
			String path = PTController.class.getResource("/").getPath();
			InputStream is = new FileInputStream(path
					+ "template/scatter_template.xlsx");
			wb = WorkbookFactory.create(is);
			this.getDictionarySheet(wb, true, map);
			Sheet content = wb.getSheetAt(1);
			// 填充数据
			fillDatas(wb, map);
			int sheetRowNum = content.getLastRowNum();
			int rowNum = sheetRowNum > 500 ? sheetRowNum : 500;
			DataValidation v_shiji = ExcelUtil.setDataValidation(content,
					"y_name", 3, rowNum, 3, 3);
			content.addValidationData(v_shiji);
			DataValidation v_yuqi = ExcelUtil.setDataValidation(content,
					"y_name", 3, rowNum, 6, 6);
			content.addValidationData(v_yuqi);
			for (int i = 3; i <= rowNum; i++) {
				DataValidation v2 = ExcelUtil.setDataValidation(content,
						"INDIRECT($D$" + (i + 1) + ")", i, i, 4, 4);
				content.addValidationData(v2);
				DataValidation v3 = ExcelUtil.setDataValidation(content,
						"INDIRECT($G$" + (i + 1) + ")", i, i, 7, 7);
				content.addValidationData(v3);
			}
			DataValidation v_henghzou1 = ExcelUtil.setDataValidation(content,
					"x_name", 3, rowNum, 5, 5);
			content.addValidationData(v_henghzou1);
			DataValidation v_henghzou2 = ExcelUtil.setDataValidation(content,
					"x_name", 3, rowNum, 8, 8);
			content.addValidationData(v_henghzou2);
		} catch (Exception e) {
			wb = null;
			e.printStackTrace();
		}
		return wb;
	}

	public void fillDatas(Workbook wb, Map<String, Object> map) {
		List<PtsvDim> pdList = null;

		String select_type = (String) map.get("select_type");
		String ptsv_code = (String) map.get("ptsv_code");
		String period_id = (String) map.get("period_id");
		PtsvDim ptsv_dim = remotePt.getOne(ptsv_code);
		map.put("ptsv_code", ptsv_dim.getPtsv_code());
		map.put("org_type", ptsv_dim.getOrg_type());
		if ("root".equals(select_type)) {
			pdList = remotePt.getRootSubs(map);
		} else {
			pdList = remotePt.getNormSubs(map);
		}
		List<String> ptsv_codes = new ArrayList<String>();
		Map<String, String> ptsvName_mappings = new HashMap<String, String>();
		Map<String, String> hrName_mappings = new HashMap<String, String>();
		Map<String, String> scatterMeta_mappings[] = getScatterMetaMap();
		ptsv_codes.add("-10000");// 设置一个不可能存在的code
		if (pdList != null) {
			for (PtsvDim pd : pdList) {
				ptsv_codes.add(pd.getPtsv_code());
				ptsvName_mappings.put(pd.getPtsv_code(), pd.getPtsv_name());
				hrName_mappings.put(pd.getPtsv_code(), pd.getHr_name());
			}
		}
		map.put("ptsv_codes", ptsv_codes);
		List<ScatterEntry> seList = localPt.getScatterDatas(map);
		Map<String,String> exsits = new HashMap<String,String>();
		if(seList !=null){
			for(ScatterEntry sm : seList){
				exsits.put(sm.getPtsv_code(), sm.getPtsv_code());
			}
		}
		for(PtsvDim dim : pdList){
			if(!exsits.containsKey(dim.getPtsv_code())){
				ScatterEntry en =new ScatterEntry();
				en.setPtsv_code(dim.getPtsv_code());
				en.setOrg_type(ptsv_dim.getOrg_type());
				en.setPeriod_id(Integer.parseInt(period_id));
				en.setSnro_code(Config.getReal());
				seList.add(en);
			}
		}
		Sheet summary = wb.getSheetAt(0);
		Row srow = summary.getRow(0);
		Cell scell = srow.getCell(1);
		if (scell == null) {
			scell = srow.createCell(1);
		}
		scell.setCellValue(ptsv_dim.getHr_name());// 填报人
		Row srow2 = summary.getRow(1);

		Cell scell2 = srow2.getCell(1);
		if (scell2 == null) {
			scell2 = srow2.createCell(1);
		}
		scell2.setCellValue(ptsv_dim.getPtsv_code());// 所属平台编码
		Row srow3 = summary.getRow(2);
		Cell scell3 = srow3.getCell(1);
		if(scell3 == null){
			scell3 = srow3.createCell(1);
		}
		scell3.setCellValue(period_id);

		Sheet content = wb.getSheetAt(1);
		int rowIndex = 3;
		if (seList != null) {
			Map<String,ScatterEntry[]> seMappings = new HashMap<String,ScatterEntry[]>();
			for(ScatterEntry se : seList){
				String code = se.getPtsv_code();
				String snro = se.getSnro_code();
				ScatterEntry[] sary = seMappings.get(code);
				if(sary ==null){
					sary = new ScatterEntry[2];
					seMappings.put(code, sary);
				}
				if(snro.equals(Config.getReal())){
					sary[0] = se;
				}else{
					sary[1] = se;
				}
			}
			for (Map.Entry<String, ScatterEntry[]> entry:seMappings.entrySet()) {
				ScatterEntry[] sary = entry.getValue();
				if(sary[0]!=null){
					fillRow(content, sary[0], rowIndex, ptsvName_mappings,
							hrName_mappings, scatterMeta_mappings);
				}
				if(sary[1] !=null){
					fillRow(content, sary[1], rowIndex, ptsvName_mappings,
							hrName_mappings, scatterMeta_mappings);
				}
				rowIndex++;
			}
		}
	}

	private void fillRow(Sheet content, ScatterEntry se, int rowIndex,
			Map<String, String> ptsvName_mappings,
			Map<String, String> hrName_mappings,
			Map<String, String>[] scatterMeta_mappings) {
		String code = se.getPtsv_code();
		String xCode = se.getS_code_x();
		String yCode = se.getS_code_y();
		String snro = se.getSnro_code();
		int codeIndex = 0;
		int nameIndex = 1;
		int hrNameIndex = 2;
		int type_index = 3;
		int y_index = 4;
		int x_index = 5;
		if (snro.equals(Config.getExpect())) {
			type_index = 6;
			y_index = 7;
			x_index = 8;
		}
		Row row = content.getRow(rowIndex);
		if (row == null) {
			row = content.createRow(rowIndex);
		}
		Cell codec = row.getCell(codeIndex);
		if (codec == null) {
			codec = row.createCell(codeIndex);
		}
		codec.setCellValue(code);
		Cell namec = row.getCell(nameIndex);
		if (namec == null) {
			namec = row.createCell(nameIndex);
		}
		namec.setCellValue(ptsvName_mappings.get(code));
		Cell hrNamec = row.getCell(hrNameIndex);
		if (hrNamec == null) {
			hrNamec = row.createCell(hrNameIndex);
		}
		hrNamec.setCellValue(hrName_mappings.get(code));
		Cell typec = row.getCell(type_index);
		if (typec == null) {
			typec = row.createCell(type_index);
		}
		String parent_code = scatterMeta_mappings[1].get(yCode);
		if (parent_code != null) {
			typec.setCellValue(scatterMeta_mappings[0].get(parent_code));
		}
		
		if(typec !=null){
			Cell xc = row.getCell(x_index);
			if (xc == null) {
				xc = row.createCell(x_index);
			}
			xc.setCellValue(scatterMeta_mappings[0].get(xCode));
			Cell yc = row.getCell(y_index);
			if (yc == null) {
				yc = row.createCell(y_index);
			}
			yc.setCellValue(scatterMeta_mappings[0].get(yCode));
		}

	}

	/**
	 * 
	 * @return
	 */
	private Map<String, String>[] getScatterMetaMap() {
		Map<String, String> nameMappings = new HashMap<String, String>();
		Map<String, String> parentMappings = new HashMap<String, String>();
		List<ScatterMeta> listm = localPt.getScatterMetaList();
		if (listm != null) {
			for (ScatterMeta sm : listm) {
				nameMappings.put(sm.getS_code(), sm.getName());
				parentMappings.put(sm.getS_code(), sm.getPs_code());
			}
		}
		Map<String, String>[] ary = new Map[2];
		ary[0] = nameMappings;
		ary[1] = parentMappings;
		return ary;
	}

	/**
	 * 获得ptsv的children
	 * 
	 * @param map
	 * @return
	 */
	private List<PtsvDim> getPtsvDimList(Map<String, Object> map) {
		String select_type = (String) map.get("select_type");
		String ptsv_code = (String) map.get("ptsv_code");
		PtsvDim ptsv_dim = remotePt.getOne(ptsv_code);
		map.put("ptsv_code", ptsv_dim.getPtsv_code());
		map.put("org_type", ptsv_dim.getOrg_type());
		if ("root".equals(select_type)) {
			return remotePt.getRootSubs(map);
		} else {
			return remotePt.getNormSubs(map);
		}
	}

	/**
	 * 
	 * @param wb
	 * @param forceUpdate
	 *            强制新建这个sheet
	 * @param map
	 * @return
	 */
	public Sheet getDictionarySheet(Workbook wb, boolean forceUpdate,
			Map<String, Object> map) {
		String dictionarySheetName = "dictionary";
		Sheet sheet = wb.getSheet(dictionarySheetName);
		if (sheet != null && !forceUpdate) {
			return sheet;
		}
		if (sheet == null) {
			sheet = wb.createSheet(dictionarySheetName);
		} else if (forceUpdate) {
			wb.removeName(dictionarySheetName);
			wb.createSheet(dictionarySheetName);
		}
		// wb.setSheetHidden(wb.getSheetIndex(sheet), true);
		// sheet content add
		int rowIndex = 1;
		String code = (String) map.get("ptsv_code");
		PtsvDim ptsvDim = remotePt.getOne(code);
		String ptsv_type = "01";
		if (ptsvDim != null) {
			ptsv_type = ptsvDim.getNotes();
		}
		List<ScatterMeta> xList = localPt.getFirstLevelScatterMetaX(ptsv_type);
		List<ScatterMeta> yList = localPt.getFirstLevelScatterMetaY(ptsv_type);
		Map<String, List<ScatterMeta>> smMap = this.getAxisChildren(ptsv_type);
		// 名称管理器 内容添加
		int y_type_column_index = 0;
		int y_column_index = 1;
		int x_colum_index = 3;
		if (yList != null) {
			int count = 0;
			int sub_count = 0;
			for (ScatterMeta sm : yList) {
				fillCell(sheet, count + rowIndex, y_type_column_index,
						sm.getName());
				List<ScatterMeta> subs = smMap.get(sm.getS_code());
				if (subs != null) {
					for (ScatterMeta sub : subs) {
						fillCell(sheet, sub_count + rowIndex, y_column_index,
								sub.getName());
						fillCell(sheet, sub_count + rowIndex,
								y_column_index + 1, sub.getS_code());
						sub_count++;
					}
					ExcelUtil.createName(wb, sm.getName(), "dictionary!$B$"
							+ (rowIndex + sub_count - subs.size() + 1) + ":$B$"
							+ (rowIndex + sub_count));
				}
				count++;
			}
			ExcelUtil.createName(wb, "y_name", "dictionary!$A$"
					+ (rowIndex + 1) + ":$A$" + (rowIndex + yList.size()));
		}
		if (xList != null) {
			int count = 0;
			for (ScatterMeta sm : xList) {
				fillCell(sheet, count + rowIndex, x_colum_index, sm.getName());
				fillCell(sheet, count + rowIndex, x_colum_index + 1,
						sm.getS_code());
				count++;
			}
			ExcelUtil.createName(wb, "x_name", "dictionary!$D$"
					+ (rowIndex + 1) + ":$D$" + (rowIndex + xList.size()));
		}
		return sheet;
	}

	private void fillCell(Sheet sheet, int rowIndex, int colIndex, String value) {
		Row subrow = sheet.getRow(rowIndex);
		if (subrow == null) {
			subrow = sheet.createRow(rowIndex);
		}
		Cell subcell = subrow.getCell(colIndex);
		if (subcell == null) {
			subcell = subrow.createCell(colIndex);
		}
		subcell.setCellValue(value);
	}

	/**
	 * 将第一层下的children，以第一层的code为key保存
	 * 
	 * @param ptsv_type
	 * @return
	 */
	private Map<String, List<ScatterMeta>> getAxisChildren(String ptsv_type) {
		Map<String, List<ScatterMeta>> axisMap = new HashMap<String, List<ScatterMeta>>();
		List<ScatterMeta> smList = localPt.getScatterMetaChildren(ptsv_type);
		if (smList != null) {
			for (ScatterMeta sm : smList) {
				List<ScatterMeta> aiList = axisMap.get(sm.getPs_code());
				if (aiList == null) {
					aiList = new ArrayList<ScatterMeta>();
					axisMap.put(sm.getPs_code(), aiList);
				}
				aiList.add(sm);
			}
		}
		return axisMap;
	}

	public void downLoadExcel(Workbook wb, String filename,
			HttpServletRequest req, HttpServletResponse rep) {
		OutputStream os = null;
		try {
			os = rep.getOutputStream();
			rep.reset();
			rep.setHeader("Content-disposition", "attachment;filename="
					+ filename);
			rep.setContentType("application/x-download");
			wb.write(os);
			os.close();
		} catch (Exception e) {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}
}
