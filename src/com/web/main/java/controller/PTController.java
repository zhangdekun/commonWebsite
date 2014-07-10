/**
 * @author zhangdekun
 *
 */
package controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import service.ExcelService;
import service.PTService;
import vo.BarData;
import vo.ColumnData;
import vo.PieData;
import vo.ResponseResult;
import vo.ScatterData;
import vo.ScatterMetaData;
import dao.localDomain.model.PtsvDimSub;
import dao.localDomain.model.ScatterMeta;
import dao.remoteDomain.model.PtsvDim;

/**
 * @author zhangdekun
 * 
 */
@Controller
public class PTController {
	@Autowired
	private PTService service;
	@Autowired
	private ExcelService excelService;

	// 获取大平台下的子平台，如：大平台有690和1169
	@ResponseBody
	public Object getRootPTSubList() {

		return null;
	}

	@RequestMapping("/smList")
	@ResponseBody
	public List<ScatterMeta> getScatterMetaList() {

		return service.getScatterMetaList();
	}

	@RequestMapping("/subs")
	@ResponseBody
	public List<PtsvDim> getSubs(@RequestParam Map<String, Object> map) {
		return service.getPtsvDimList(map);
	}
	@RequestMapping("/dims")
	@ResponseBody
	public List<PtsvDimSub> dims(@RequestParam Map<String, Object> map) {
		String ptsv_code = (String)map.get("ptsv_code");
		return service.getSubDims(ptsv_code);
	}
	@RequestMapping("/scatter")
	@ResponseBody
	public ScatterData scatter(@RequestParam Map<String, Object> map) {
		return service.getScatterResults(map);
	}

	@RequestMapping("/pie")
	@ResponseBody
	public PieData pie(@RequestParam Map<String, Object> map) {
		return service.getPieResults(map);
	}

	@RequestMapping("/bar")
	@ResponseBody
	public BarData bar(@RequestParam Map<String, Object> map) {
		return service.getBarResults(map);
	}

	@RequestMapping("/column")
	@ResponseBody
	public ColumnData column(@RequestParam Map<String, Object> map) {
		return service.getColumnResults(map);
	}

	@RequestMapping("/scatterExcel")
	public void scatterExcel(HttpServletRequest request,
			HttpServletResponse response, @RequestParam Map<String, Object> map) {
		try {
			Workbook wb = excelService.getScatterExcelTemplate(map);
			excelService
					.downLoadExcel(wb, "scattertmp.xlsx", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/uploadScatterExcel")
	@ResponseBody
	public Object uploadScatterExcel(
			@RequestParam("scatterFile") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> map) {
		try {
//			map.put("select_type", "root");
//			map.put("period_id", 201406);
			Workbook wb = WorkbookFactory.create(file.getInputStream());
			boolean isSuccess =excelService.excelTodb(wb, map);
			if(isSuccess){
				return ResponseResult.OK();
			}else{
				return ResponseResult.BUSERROR();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.SYSERROR();
		}
	}
	@RequestMapping(value="/saveSM",method=RequestMethod.POST)
	@ResponseBody
	public Object saveScatterMeta(@RequestBody ScatterMetaData smd){
		try{
			boolean isSuccess  =service.saveScatterData(smd);
			if(isSuccess){
				return ResponseResult.OK();
			}else{
				return ResponseResult.BUSERROR();
			}
		}catch(Exception e){
			e.printStackTrace();
			return ResponseResult.SYSERROR();
		}
	}
	
}
