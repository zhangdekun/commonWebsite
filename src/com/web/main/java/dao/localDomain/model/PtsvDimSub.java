package dao.localDomain.model;

public class PtsvDimSub {

	private Integer id;
	private String ptsv_code;
	private String norm_code;
	private String norm_name;
	private Integer norm_code_type;
	private Integer code_order;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPtsv_code() {
		return ptsv_code;
	}
	public void setPtsv_code(String ptsv_code) {
		this.ptsv_code = ptsv_code;
	}
	public String getNorm_code() {
		return norm_code;
	}
	public void setNorm_code(String norm_code) {
		this.norm_code = norm_code;
	}
	public Integer getNorm_code_type() {
		return norm_code_type;
	}
	public void setNorm_code_type(Integer norm_code_type) {
		this.norm_code_type = norm_code_type;
	}
	public Integer getCode_order() {
		return code_order;
	}
	public void setCode_order(Integer code_order) {
		this.code_order = code_order;
	}
	public String getNorm_name() {
		return norm_name;
	}
	public void setNorm_name(String norm_name) {
		this.norm_name = norm_name;
	}
}
