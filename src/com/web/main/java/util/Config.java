package util;

import java.util.ResourceBundle;

public class Config {
	public static ResourceBundle config = ResourceBundle.getBundle("config");
	public static String getPtType(){
		return config.getString("ptsv.type.pt");
	}
	public static String getSvType(){
		return config.getString("ptsv.type.sv");
	}
	public static String getYearAddType(){
		return config.getString("ptsv.v04");
	}
	public static String getMonthAddType(){
		return config.getString("ptsv.v03");
	}
	public static String getDayAddType(){
		return config.getString("ptsv.v01");
	}
	/**
	 * 预期值
	 * @return
	 */
	public static String getExpect(){
		return config.getString("ptsv.s01");
	}
	/**
	 * 实际值
	 * @return
	 */
	public static String getReal(){
		return config.getString("ptsv.s03");
	}
}
