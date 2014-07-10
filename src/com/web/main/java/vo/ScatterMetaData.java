package vo;

import java.util.ArrayList;
import java.util.List;

public class ScatterMetaData {

	private Integer period_id;
	private String ptsv_code;
	List<AxisItem> rowAxis=new ArrayList<AxisItem>();
	List<AxisItem> colAxis = new ArrayList<AxisItem>();
	public List<AxisItem> getRowAxis() {
		return rowAxis;
	}
	public void setRowAxis(List<AxisItem> rowAxis) {
		this.rowAxis = rowAxis;
	}
	public List<AxisItem> getColAxis() {
		return colAxis;
	}
	public void setColAxis(List<AxisItem> colAxis) {
		this.colAxis = colAxis;
	}
	public Integer getPeriod_id() {
		return period_id;
	}
	public void setPeriod_id(Integer period_id) {
		this.period_id = period_id;
	}
	public String getPtsv_code() {
		return ptsv_code;
	}
	public void setPtsv_code(String ptsv_code) {
		this.ptsv_code = ptsv_code;
	}
}
