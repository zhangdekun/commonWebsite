package vo;

import java.util.ArrayList;
import java.util.List;

public class ScatterData {

	List<AxisItem> rowAxis=new ArrayList<AxisItem>();
	List<AxisItem> colAxis = new ArrayList<AxisItem>();
	List<PointItem> points = new ArrayList<PointItem>();
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
	public List<PointItem> getPoints() {
		return points;
	}
	public void setPoints(List<PointItem> points) {
		this.points = points;
	}
}
