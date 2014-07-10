package vo;

public class BarItem {

	private Double dim_value;
	private String dim_name;
	private String dim_code;
	public Double getDim_value() {
		return dim_value;
	}
	public void setDim_value(Double dim_value) {
		this.dim_value = dim_value;
	}
	public String getDim_name() {
		return dim_name;
	}
	public void setDim_name(String dim_name) {
		this.dim_name = dim_name;
	}
	public Float getPc() {
		return pc;
	}
	public void setPc(Float pc) {
		this.pc = pc;
	}
	public Float getAmplitude() {
		return amplitude;
	}
	public void setAmplitude(Float amplitude) {
		this.amplitude = amplitude;
	}
	private Float pc;//完成度
	private Float amplitude;//增幅 
	private Integer order =0;
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getDim_code() {
		return dim_code;
	}
	public void setDim_code(String dim_code) {
		this.dim_code = dim_code;
	}
}
