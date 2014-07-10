package vo;

import java.util.ArrayList;
import java.util.List;

public class Bar {

	private String title;
	private List<BarItem> barItems = new ArrayList<BarItem>();
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<BarItem> getBarItems() {
		return barItems;
	}
	public void setBarItems(List<BarItem> barItems) {
		this.barItems = barItems;
	}
}
