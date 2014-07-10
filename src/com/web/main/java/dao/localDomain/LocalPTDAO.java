package dao.localDomain;

import java.util.List;
import java.util.Map;

import vo.AreaItem;
import vo.PieItem;
import dao.localDomain.model.AreaRelate;
import dao.localDomain.model.PtsvDimSub;
import dao.localDomain.model.ScatterEntry;
import dao.localDomain.model.ScatterMeta;

public interface LocalPTDAO {

	public List<ScatterMeta> getScatterMetaList();

	public List<ScatterMeta> getFirstLevelScatterMetaY(String ptsv_type);

	public List<ScatterMeta> getFirstLevelScatterMetaX(String ptsv_type);

	public List<ScatterMeta> getScatterMetaChildren(String ptsv_type);

	public List<AreaItem> getAreas(String ptsv_type);

	public List<ScatterEntry> getScatterDatas(Map<String, Object> map);

	public List<PieItem> getPieDatas(Map<String, Object> map);

	public List<PtsvDimSub> getPtsvDims(String ptsv_code);

	public void deleteByPtsvCodeAndPeriod(Map<String, Object> map);

	public void insertScatterEntrys(List<ScatterEntry> list);

	public void batchInsertScatterMeta(List<ScatterMeta> list);

	public void batchInsertAreaRelate(List<AreaRelate> list);

	public void batchUpdateAreaRelate(List<AreaRelate> list);

	public void batchUpdateScatterMeta(List<ScatterMeta> list);

	public String getScatterMetaMaxId();

	public void deleteScatterMetaByPtsvType(Map<String, Object> map);
}
