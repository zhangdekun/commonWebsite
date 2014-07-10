package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import util.Config;
import vo.AreaItem;
import vo.AxisItem;
import vo.Bar;
import vo.BarData;
import vo.BarItem;
import vo.Column;
import vo.ColumnData;
import vo.ColumnItem;
import vo.Pie;
import vo.PieData;
import vo.PieItem;
import vo.PointItem;
import vo.ScatterData;
import vo.ScatterMetaData;
import dao.localDomain.LocalPTDAO;
import dao.localDomain.model.AreaRelate;
import dao.localDomain.model.PtsvDimSub;
import dao.localDomain.model.ScatterEntry;
import dao.localDomain.model.ScatterMeta;
import dao.remoteDomain.RemotePTDAO;
import dao.remoteDomain.model.PtsvDim;
import dao.remoteDomain.model.PtsvInfo;

@Service
public class PTService {
	@Autowired
	private LocalPTDAO localPt;
	@Autowired
	private RemotePTDAO remotePt;

	public List<ScatterMeta> getScatterMetaList() {
		return localPt.getScatterMetaList();
	}

	public List<PtsvDim> getRootSubs(Map<String, Object> map) {
		return remotePt.getRootSubs(map);
	}

	public List<PtsvDim> getNormSubs(Map<String, Object> map) {
		return remotePt.getNormSubs(map);
	}

	public PtsvDim getPtsv(String code) {
		return remotePt.getOne(code);
	}

	/**
	 * bar 图数据
	 * 
	 * @param map
	 * @return
	 */
	public BarData getBarResults(Map<String, Object> map) {
		BarData barData = new BarData();

		String code = (String) map.get("ptsv_code");
		String period_id = (String) map.get("period_id");
		Integer period_idInt = Integer.parseInt(period_id);
		// ptsv code 筛选的指标
		List<PtsvDimSub> psList = this.localPt.getPtsvDims(code);
		List<String> norm_codes = new ArrayList<String>();
		norm_codes.add("-10000");
		if (psList != null) {
			for (PtsvDimSub sub : psList) {
				norm_codes.add(sub.getNorm_code());
			}
		}
		map.put("norm_codes", norm_codes);
		Integer realPeriodId = this.realPeriodId(period_idInt, map);
		// 真正的时间
		map.put("period_id", realPeriodId);
		List<PtsvInfo> ptinfList = remotePt.getPtsvInfo(map);
		// 上一年的数据
		Integer realPreYearPeriodId = this.realPreYearPeriodId(period_idInt,
				map);
		map.put("period_id", realPreYearPeriodId);
		List<PtsvInfo> perYptinfList = remotePt.getPtsvInfo(map);

		Map<String, Map<String, Map<String, List<PtsvInfo>>>> nowMap = generateMap(ptinfList);
		Map<String, Map<String, Map<String, List<PtsvInfo>>>> preYMap = generateMap(perYptinfList);
		List<Bar> barList = barData.getBarList();
		for (Map.Entry<String, Map<String, Map<String, List<PtsvInfo>>>> entry : nowMap
				.entrySet()) {
			Bar bar = new Bar();
			bar.setTitle(entry.getKey());// 年累，月累。。。
			Map<String, Map<String, List<PtsvInfo>>> spMap = entry.getValue();
			List<BarItem> barItemList = bar.getBarItems();
			for (Map.Entry<String, Map<String, List<PtsvInfo>>> entry2 : spMap
					.entrySet()) {
				BarItem barItem = new BarItem();
				String keyary[] = entry2.getKey().split("__");
				barItem.setDim_name(keyary[1]);// 指标
				barItem.setDim_code(keyary[0]);
				Map<String, List<PtsvInfo>> svMap = entry2.getValue();
				List<PtsvInfo> pt1List = svMap.get(Config.getExpect());// 目标值
				List<PtsvInfo> pt2List = svMap.get(Config.getReal());// 实际值
				PtsvInfo so1 = (pt1List == null || pt1List.isEmpty()) ? null
						: pt1List.get(0);
				PtsvInfo so3 = (pt2List == null || pt2List.isEmpty()) ? null
						: pt2List.get(0);// 实际值
				Double so1Value = so1 == null ? 0 : so1.getAcnt_value_c();
				Double so3Value = so3 == null ? 0 : so3.getAcnt_value_c();
				barItem.setDim_value(so3Value);
				Double preYearValue = getPreYearRealValue(preYMap,
						entry.getKey(), entry2.getKey());
				barItem.setAmplitude(preYearValue == 0d ? 0f
						: (float) ((so3Value - preYearValue) / preYearValue));
				barItem.setPc(so1Value == 0 ? 0f
						: (float) (so3Value / so1Value));
				barItemList.add(barItem);
			}
			barList.add(bar);
		}

		return barData;
	}

	private Double getPreYearRealValue(
			Map<String, Map<String, Map<String, List<PtsvInfo>>>> preYMap,
			String v, String dim) {
		Double d = 0d;
		Map<String, Map<String, List<PtsvInfo>>> dimMap = preYMap.get(v);
		if (dimMap != null) {
			Map<String, List<PtsvInfo>> snroMap = dimMap.get(dim);
			if (snroMap != null) {
				List<PtsvInfo> ptList = snroMap.get(Config.getReal());
				if (ptList != null && !ptList.isEmpty()) {
					PtsvInfo info = ptList.get(0);
					if (info != null) {
						d = info.getAcnt_value_c();
					}
				}

			}
		}
		return d;
	}

	/**
	 * 从版本（年累，月累。。）到指标，到snro（预估，实际）到具体数据(按照时间的一个list数据)的map
	 * 
	 * @param ptsvInfoList
	 * @return
	 */
	private Map<String, Map<String, Map<String, List<PtsvInfo>>>> generateMap(
			List<PtsvInfo> ptsvInfoList) {
		Map<String, Map<String, Map<String, List<PtsvInfo>>>> infoMap = new HashMap<String, Map<String, Map<String, List<PtsvInfo>>>>();
		if (ptsvInfoList != null) {
			for (PtsvInfo info : ptsvInfoList) {
				String ver = info.getVers_code();
				String snro = info.getSnro_code();
				String norm = info.getNorm_code();
				String normName = info.getNorm_name();
				String norm_key = norm + "__" + normName;
				Map<String, Map<String, List<PtsvInfo>>> normMap = infoMap
						.get(ver);// 指标为key
				if (normMap == null) {
					normMap = new HashMap<String, Map<String, List<PtsvInfo>>>();
					infoMap.put(ver, normMap);
				}
				Map<String, List<PtsvInfo>> snroMap = normMap.get(norm_key);// 预估，实际值为key
				if (snroMap == null) {
					snroMap = new HashMap<String, List<PtsvInfo>>();
					normMap.put(norm_key, snroMap);
				}
				List<PtsvInfo> ptList = snroMap.get(snro);
				if (ptList == null) {
					ptList = new ArrayList<PtsvInfo>();
					snroMap.put(snro, ptList);
				}
				ptList.add(info);
			}
		}
		return infoMap;
	}

	private List<Integer> getMonthAddPeriods(Map<String, Object> map,
			Integer period_id) {
		List<Integer> periods = new ArrayList<Integer>();
		// 201406
		int i = (period_id / 100) * 100;
		for (; i <= period_id; i = i + 1) {
			periods.add(realPeriodId(i, map));
		}
		return periods;
	}

	private List<Integer> getDayAddPeriods(Map<String, Object> map,
			Integer period_id) {
		List<Integer> periods = new ArrayList<Integer>();
		// 201406
		int i = period_id * 100;
		int max = i + 31;
		for (; i <= max; i = i + 1) {
			periods.add(i);
		}
		return periods;
	}

	/**
	 * column 图数据
	 * 
	 * @param map
	 * @return
	 */
	public ColumnData getColumnResults(Map<String, Object> map) {
		ColumnData columnData = new ColumnData();
		String code = (String) map.get("ptsv_code");
		String period_id = (String) map.get("period_id");
		String norm_code = (String) map.get("norm_code");
		String vers_code = (String) map.get("vers_code");
		Integer period_idInt = Integer.parseInt(period_id);
		// ptsv code 筛选的指标
		List<PtsvDimSub> psList = this.localPt.getPtsvDims(code);
		// 参数中有指标时,不用筛选,直接使用参数中的指标
		List<String> norm_codes = new ArrayList<String>();
		if (norm_code == null || "".equals(norm_code)) {
			norm_codes.add("-10000");
			if (psList != null) {
				for (PtsvDimSub sub : psList) {
					norm_codes.add(sub.getNorm_code());
				}
			}
		} else {
			norm_codes.add(norm_code);
		}

		map.put("norm_codes", norm_codes);
		// 日累数据
		if (Config.getDayAddType().equals(vers_code)) {
			List<Integer> period_ids = getDayAddPeriods(map, period_idInt);
			map.put("period_ids", period_ids);
		} else {// 月累数据
			List<Integer> period_ids = getMonthAddPeriods(map, period_idInt);
			map.put("period_ids", period_ids);
		}

		List<PtsvInfo> ptinfList = remotePt.getPtsvInfoByPeriods(map);
		Map<String, Map<String, Map<String, List<PtsvInfo>>>> nowMap = generateMap(ptinfList);
		for (Map.Entry<String, Map<String, Map<String, List<PtsvInfo>>>> entry : nowMap
				.entrySet()) {
			String key = entry.getKey();
			if (key.equals(vers_code)) {
				List<Column> colList = columnData.getColumnList();
				Map<String, Map<String, List<PtsvInfo>>> spMap = entry
						.getValue();
				for (Map.Entry<String, Map<String, List<PtsvInfo>>> entry2 : spMap
						.entrySet()) {
					Column column = new Column();
					String key2 = entry2.getKey();
					String keyary[] = key2.split("__");
					column.setTitle(keyary[1]);
					column.setCode(keyary[0]);
					Map<String, List<ColumnItem>> snroMap = column.getSnors();
					Map<String, List<PtsvInfo>> svMap = entry2.getValue();
					for (Map.Entry<String, List<PtsvInfo>> entry3 : svMap
							.entrySet()) {
						String snro = entry3.getKey();
						List<ColumnItem> cItemList = snroMap.get(snro);
						if (cItemList == null) {
							cItemList = new ArrayList<ColumnItem>();
							snroMap.put(snro, cItemList);
						}
						List<PtsvInfo> ptsvList = entry3.getValue();
						if (ptsvList != null) {
							for (PtsvInfo info : ptsvList) {
								ColumnItem item = new ColumnItem();
								item.setC_vaule(info.getAcnt_value_c());
								item.setPeriod_id(info.getPeriod_id());
								// item.setPeriod_name(period_name);
								cItemList.add(item);
							}
						}
					}
					colList.add(column);
				}
			}
		}
		return columnData;
	}

	/**
	 * pie图数据
	 * 
	 * @param map
	 * @return
	 */
	public PieData getPieResults(Map<String, Object> map) {
		PieData pieData = new PieData();
		List<PtsvDim> pdList = this.getPtsvDimList(map);
		List<String> ptsv_codes = new ArrayList<String>();
		ptsv_codes.add("-10000");// 设置一个不可能存在的code
		if (pdList != null) {
			for (PtsvDim pd : pdList) {
				ptsv_codes.add(pd.getPtsv_code());
			}
		}
		map.put("ptsv_codes", ptsv_codes);
		List<PieItem> pieItemList = localPt.getPieDatas(map);
		// 分类：实际值，预估值
		Map<String, List<PieItem>> typeMap = new HashMap<String, List<PieItem>>();
		if (pieItemList != null) {
			for (PieItem item : pieItemList) {
				List<PieItem> temp = typeMap.get(item.getType());
				if (temp == null) {
					temp = new ArrayList<PieItem>();
					typeMap.put(item.getType(), temp);
				}
				temp.add(item);
			}
		}
		List<Pie> pieList = pieData.getPieList();
		for (Map.Entry<String, List<PieItem>> entry : typeMap.entrySet()) {
			Pie pie = new Pie();
			pie.setTitle(entry.getKey());
			pie.setPieItems(entry.getValue());
			pieList.add(pie);
		}
		return pieData;
	}

	/**
	 * 二维点阵需要的数据
	 * 
	 * @param map
	 * @return
	 */
	public ScatterData getScatterResults(Map<String, Object> map) {
		ScatterData scatterData = new ScatterData();
		String code = (String) map.get("ptsv_code");
		PtsvDim ptsvDim = remotePt.getOne(code);

		if (ptsvDim != null) {
			/**
			 * set x and y value
			 */
			String orgType = ptsvDim.getOrg_type();// 组织机构，目前有1169,690
			map.put("org_type", orgType);
			String ptsv_type = ptsvDim.getNotes();// 区分平台还是小微
			List<ScatterMeta> scatterMetasx = localPt
					.getFirstLevelScatterMetaX(ptsv_type);
			List<ScatterMeta> scatterMetasy = localPt
					.getFirstLevelScatterMetaY(ptsv_type);
			Map<String, List<AxisItem>> axisChildrenMap = getAxisChildren(ptsv_type);
			if (scatterMetasx != null) {
				List<AxisItem> aiList = scatterData.getRowAxis();
				for (ScatterMeta sm : scatterMetasx) {
					AxisItem item = new AxisItem();
					item.setAxis_code(sm.getS_code());
					item.setName(sm.getName());
					item.setArea_id(sm.getArea_id());
					item.setChildren(axisChildrenMap.get(sm.getS_code()));
					aiList.add(item);
				}
			}
			if (scatterMetasy != null) {
				List<AxisItem> aiList = scatterData.getColAxis();
				for (ScatterMeta sm : scatterMetasy) {
					AxisItem item = new AxisItem();
					item.setAxis_code(sm.getS_code());
					item.setName(sm.getName());
					item.setArea_id(sm.getArea_id());
					item.setChildren(axisChildrenMap.get(sm.getS_code()));
					aiList.add(item);
				}
			}
			/**
			 * set points
			 */

			List<PtsvDim> pdList = this.getPtsvDimList(map);
			List<String> ptsv_codes = new ArrayList<String>();
			ptsv_codes.add("-10000");// 设置一个不可能存在的code
			if (pdList != null) {
				for (PtsvDim pd : pdList) {
					ptsv_codes.add(pd.getPtsv_code());
				}
			}
			map.put("ptsv_codes", ptsv_codes);
			List<ScatterEntry> seList = localPt.getScatterDatas(map);
			if (seList != null) {
				List<PointItem> pointList = scatterData.getPoints();
				for (ScatterEntry se : seList) {
					PointItem item = new PointItem();
					item.setName(se.getPtsv_name());
					item.setX_code(se.getS_code_x());
					item.setY_code(se.getS_code_y());
					item.setP_type(se.getSnro_code());
					pointList.add(item);
				}
			}
		}
		return scatterData;
	}

	/**
	 * 将第一层下的children，以第一层的code为key保存
	 * 
	 * @param ptsv_type
	 * @return
	 */
	private Map<String, List<AxisItem>> getAxisChildren(String ptsv_type) {
		Map<String, List<AxisItem>> axisMap = new HashMap<String, List<AxisItem>>();
		List<ScatterMeta> smList = localPt.getScatterMetaChildren(ptsv_type);
		if (smList != null) {
			for (ScatterMeta sm : smList) {
				List<AxisItem> aiList = axisMap.get(sm.getPs_code());
				if (aiList == null) {
					aiList = new ArrayList<AxisItem>();
					axisMap.put(sm.getPs_code(), aiList);
				}
				AxisItem item = new AxisItem();
				item.setAxis_code(sm.getS_code());
				item.setName(sm.getName());
				item.setArea_id(sm.getArea_id());
				aiList.add(item);
			}
		}
		return axisMap;
	}

	/**
	 * 获得ptsv的children
	 * 
	 * @param map
	 * @return
	 */
	public List<PtsvDim> getPtsvDimList(Map<String, Object> map) {
		String select_type = (String) map.get("select_type");
		String ptsv_code = (String) map.get("ptsv_code");
		PtsvDim ptsv_dim = this.getPtsv(ptsv_code);
		map.put("ptsv_code", ptsv_dim.getPtsv_code());
		map.put("org_type", ptsv_dim.getOrg_type());
		if ("root".equals(select_type)) {
			return this.getRootSubs(map);
		} else {
			return this.getNormSubs(map);
		}
	}

	/**
	 * 查找数据库中存在的时间
	 * 
	 * @param period_id
	 * @param map
	 *            contains ptsv_code , norm_codes
	 * @return
	 */
	private Integer realPeriodId(Integer period_id, Map<String, Object> map) {
		// 20140301
		int start = period_id * 100;
		int end = period_id * 100 + 31;
		map.put("period_start", start);
		map.put("period_end", end);
		Integer periodId = remotePt.getPeriodId(map);
		return periodId == null ? -1 : periodId;
	}

	/**
	 * 上一年
	 * 
	 * @param period_id
	 * @param map
	 * @return
	 */
	private Integer realPreYearPeriodId(Integer period_id,
			Map<String, Object> map) {
		Integer preyear = period_id - 100;
		return realPeriodId(preyear, map);
	}

	/**
	 * 获得一个平台下存在指标的最新日期
	 * 
	 * @param map
	 * @return
	 */
	public Integer getLastPeriodId(Map<String, Object> map) {
		String code = (String) map.get("ptsv_code");
		// ptsv code 筛选的指标
		List<PtsvDimSub> psList = this.localPt.getPtsvDims(code);
		List<String> norm_codes = new ArrayList<String>();
		norm_codes.add("-10000");
		if (psList != null) {
			for (PtsvDimSub sub : psList) {
				norm_codes.add(sub.getNorm_code());
			}
		}
		map.put("norm_codes", norm_codes);
		return remotePt.getLastPeriodId(map);
	}

	@Transactional(value="local",readOnly=false,propagation=Propagation.REQUIRED)
	public boolean saveScatterData(ScatterMetaData smd) {
		boolean flg = true;
		try {
			String ptsv_code = smd.getPtsv_code();
			PtsvDim ptsv = remotePt.getOne(ptsv_code);
			String ptsv_type = ptsv.getNotes();
			String F = "F";// 父层
			String C = "C";// 子层
			Integer X = 0;
			Integer Y = 1;
			Map<String, ScatterMeta> scatterMetaMappings = new HashMap<String, ScatterMeta>();
			String maxId = localPt.getScatterMetaMaxId();
			if(maxId == null){
				maxId = "100000";
			}
			List<AxisItem> rItemList = smd.getRowAxis();
			if (rItemList != null) {
				for (int i = 0; i < rItemList.size(); i++) {
					AxisItem item = rItemList.get(i);
					ScatterMeta sm = getOneSmFromAxisItem(item,maxId, null, X, i + 1,
							F, ptsv_type);
					scatterMetaMappings.put(sm.getS_code(), sm);
					List<AxisItem> children = item.getChildren();
					if (children != null && children.size() > 0) {
						for (int j = 0; j < children.size(); j++) {
							AxisItem item2 = children.get(j);
							ScatterMeta sm2 = getOneSmFromAxisItem(item2,maxId,
									sm.getS_code(), X, j + 1, C, ptsv_type);
							scatterMetaMappings.put(sm2.getS_code(), sm2);
						}
					}
				}
			}
			List<AxisItem> cItemList = smd.getColAxis();
			if (cItemList != null) {
				for (int i = 0; i < cItemList.size(); i++) {
					AxisItem item = cItemList.get(i);
					ScatterMeta sm = getOneSmFromAxisItem(item,maxId, null, Y, i + 1,
							F, ptsv_type);
					scatterMetaMappings.put(sm.getS_code(), sm);
					List<AxisItem> children = item.getChildren();
					if (children != null && children.size() > 0) {
						for (int j = 0; j < children.size(); j++) {
							AxisItem item2 = children.get(j);
							ScatterMeta sm2 = getOneSmFromAxisItem(item2,maxId,
									sm.getS_code(), Y, j + 1, C, ptsv_type);
							scatterMetaMappings.put(sm2.getS_code(), sm2);
						}
					}
				}
			}
			List<ScatterMeta> all = localPt.getScatterMetaList();
			Map<String, String> all_s_code_mappings = new HashMap<String, String>();
			if (all != null) {
				for (ScatterMeta sm : all) {
					all_s_code_mappings.put(sm.getS_code(), sm.getS_code());
				}
			}
			List<ScatterMeta> inserts = new ArrayList<ScatterMeta>();
			List<ScatterMeta> updates = new ArrayList<ScatterMeta>();
			List<String> exist_codes = new ArrayList<String>();
			exist_codes.add("-1");
			for (Map.Entry<String, ScatterMeta> entry : scatterMetaMappings
					.entrySet()) {
				if (all_s_code_mappings.containsKey(entry.getKey())) {
					updates.add(entry.getValue());
				} else {
					inserts.add(entry.getValue());
				}
				exist_codes.add(entry.getKey());
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ptsv_type", ptsv_type);
			map.put("s_codes", exist_codes);
			localPt.deleteScatterMetaByPtsvType(map);
			
			if (inserts.size() > 0) {
				localPt.batchInsertScatterMeta(inserts);
			}
			if (updates.size() > 0) {
				localPt.batchUpdateScatterMeta(updates);
			}
		} catch (Exception e) {
			e.printStackTrace();
			flg = false;
			throw new RuntimeException(e);
		}
		return flg;
	}

	private String getS_code(String maxId,String level,Integer xy_type,Integer order) {
		if(maxId ==null){
			maxId = "100000";
		}
		int maxInt = Integer.parseInt(maxId)+1;
		return maxInt+"";
	}

	private ScatterMeta getOneSmFromAxisItem(AxisItem item,String maxId, String ps_code,
			Integer xy_type, Integer order, String level, String ptsv_type) {
		ScatterMeta sm = new ScatterMeta();
		sm.setName(item.getName());
		sm.setPs_code(ps_code);
		sm.setXy_type(xy_type);
		sm.setArea_id(item.getArea_id());
		sm.setXy_order(order);
		String s_code = item.getAxis_code();
		if (StringUtils.isEmpty(s_code)) {
			s_code = getS_code(maxId,level,xy_type,order);
		}
		sm.setS_code(s_code);
		sm.setPtsv_type(ptsv_type);
		return sm;
	}

	/**
	 * 指标列表
	 * 
	 * @param ptsv_code
	 * @return
	 */
	public List<PtsvDimSub> getSubDims(String ptsv_code) {
		return localPt.getPtsvDims(ptsv_code);
	}
}
