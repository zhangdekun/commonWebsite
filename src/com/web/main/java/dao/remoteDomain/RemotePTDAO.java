package dao.remoteDomain;

import java.util.List;
import java.util.Map;

import dao.remoteDomain.model.PtsvDim;
import dao.remoteDomain.model.PtsvInfo;

public interface RemotePTDAO {

	public List<PtsvDim> getRootSubs(Map<String, Object> map);

	public List<PtsvDim> getNormSubs(Map<String, Object> map);

	public List<PtsvInfo> getPtsvInfo(Map<String, Object> map);

	public List<PtsvInfo> getPtsvInfoByPeriods(Map<String, Object> map);

	public PtsvDim getOne(String code);

	public Integer getPeriodId(Map<String, Object> map);

	public Integer getLastPeriodId(Map<String, Object> map);
}
