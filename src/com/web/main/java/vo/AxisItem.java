package vo;

import java.util.List;

public class AxisItem {

	private List<AxisItem> children;
	private String axis_code;
	private String name;
	private String color;
	private Integer area_id;
	public String getAxis_code() {
		return axis_code;
	}
	public void setAxis_code(String axis_code) {
		this.axis_code = axis_code;
	}
	public String getName() {
		if(name !=null){
			name = name.trim();
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<AxisItem> getChildren() {
		return children;
	}
	public void setChildren(List<AxisItem> children) {
		this.children = children;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getArea_id() {
		return area_id;
	}
	public void setArea_id(Integer area_id) {
		this.area_id = area_id;
	}
}
