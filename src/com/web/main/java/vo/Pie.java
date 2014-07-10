package vo;

import java.util.ArrayList;
import java.util.List;

public class Pie {

	private String title;
	private List<PieItem> pieItems = new ArrayList<PieItem>();
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<PieItem> getPieItems() {
		return pieItems;
	}
	public void setPieItems(List<PieItem> pieItems) {
		this.pieItems = pieItems;
	}
}
