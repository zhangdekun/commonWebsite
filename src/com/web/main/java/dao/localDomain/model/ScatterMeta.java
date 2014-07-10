package dao.localDomain.model;

public class ScatterMeta {
	private Integer id;
	private Integer area_id;
	private String name;
	private String ps_code ;
	private String s_code;
	private Integer xy_type;
	private Integer xy_order;
	private String ptsv_type;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getPs_code() {
		return ps_code;
	}
	public void setPs_code(String ps_code) {
		this.ps_code = ps_code;
	}
	public String getS_code() {
		return s_code;
	}
	public void setS_code(String s_code) {
		this.s_code = s_code;
	}
	public Integer getXy_type() {
		return xy_type;
	}
	public void setXy_type(Integer xy_type) {
		this.xy_type = xy_type;
	}
	public Integer getXy_order() {
		return xy_order;
	}
	public void setXy_order(Integer xy_order) {
		this.xy_order = xy_order;
	}
	public String getPtsv_type() {
		return ptsv_type;
	}
	public void setPtsv_type(String ptsv_type) {
		this.ptsv_type = ptsv_type;
	}
	public Integer getArea_id() {
		return area_id;
	}
	public void setArea_id(Integer area_id) {
		this.area_id = area_id;
	}
	
}
