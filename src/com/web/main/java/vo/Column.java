package vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Column {

	private String title;
	private String code;
	private Map<String,List<ColumnItem>> snors = new HashMap<String,List<ColumnItem>>(); 
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Map<String, List<ColumnItem>> getSnors() {
		return snors;
	}
	public void setSnors(Map<String, List<ColumnItem>> snors) {
		this.snors = snors;
	}
}
