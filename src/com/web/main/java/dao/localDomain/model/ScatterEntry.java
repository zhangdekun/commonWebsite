package dao.localDomain.model;

public class ScatterEntry {

	private Integer id;
	private String ptsv_code;
	private String ptsv_name;
	private String org_type;
	private String snro_code;
	private Integer period_id;
	private String s_code_x;
	private String s_code_y;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPtsv_code() {
		return ptsv_code;
	}
	public String getPtsv_name() {
		return ptsv_name;
	}
	public void setPtsv_name(String ptsv_name) {
		this.ptsv_name = ptsv_name;
	}
	public void setPtsv_code(String ptsv_code) {
		this.ptsv_code = ptsv_code;
	}
	public String getOrg_type() {
		return org_type;
	}
	public void setOrg_type(String org_type) {
		this.org_type = org_type;
	}
	public String getSnro_code() {
		return snro_code;
	}
	public void setSnro_code(String snro_code) {
		this.snro_code = snro_code;
	}
	public Integer getPeriod_id() {
		return period_id;
	}
	public void setPeriod_id(Integer period_id) {
		this.period_id = period_id;
	}
	public String getS_code_x() {
		return s_code_x;
	}
	public void setS_code_x(String s_code_x) {
		this.s_code_x = s_code_x;
	}
	public String getS_code_y() {
		return s_code_y;
	}
	public void setS_code_y(String s_code_y) {
		this.s_code_y = s_code_y;
	}
}
