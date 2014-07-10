package dao.remoteDomain.model;

public class PtsvDim {
	private String ptsv_code;
	private String ptsv_name;
	private String pt_code;
	private String org_type;
	private String notes;//区分是平台还是小微 01是平台，02是小微
	
	private String hr_code;
	private String hr_name;
	private String emp_code;
	private String emp_name;
	public String getPtsv_code() {
		return ptsv_code;
	}
	public void setPtsv_code(String ptsv_code) {
		this.ptsv_code = ptsv_code;
	}
	public String getPtsv_name() {
		return ptsv_name;
	}
	public void setPtsv_name(String ptsv_name) {
		this.ptsv_name = ptsv_name;
	}
	public String getPt_code() {
		return pt_code;
	}
	public void setPt_code(String pt_code) {
		this.pt_code = pt_code;
	}
	public String getOrg_type() {
		return org_type;
	}
	public void setOrg_type(String org_type) {
		this.org_type = org_type;
	}
	public String getHr_code() {
		return hr_code;
	}
	public void setHr_code(String hr_code) {
		this.hr_code = hr_code;
	}
	public String getHr_name() {
		return hr_name;
	}
	public void setHr_name(String hr_name) {
		this.hr_name = hr_name;
	}
	public String getEmp_code() {
		return emp_code;
	}
	public void setEmp_code(String emp_code) {
		this.emp_code = emp_code;
	}
	public String getEmp_name() {
		return emp_name;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
