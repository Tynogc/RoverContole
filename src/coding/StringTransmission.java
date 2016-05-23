package coding;

import java.awt.Color;

public class StringTransmission {
	
	private String s;
	
	private String compiled;
	
	private int[] inr;
	
	private static final Color lightBlue = new Color(100,100,255);
	
	public static final int MARK_NORMAL = 0;
	public static final int MARK_PATCH = 1384;
	public static final int MARK_VALUE = 4235;
	public static final int MARK_ERROR = 4576;
	public static final int MARK_COMM = 8211;
	
	public StringTransmission(){
		inr = new int[1];
	}
	
	public void setString(String s){
		this.s = s;
		compiled = "";
		inr = new int[s.length()];
	}
	
	public String getString(){
		return s;
	}
	
	public void process(){
		OperateAll.process(this);
	}
	
	public int getIntAtPois(int pos){
		if(pos<0||pos>=inr.length)return 0;
		return inr[pos];
	}
	
	public void setIntAt(int pos, int i){
		if(pos<0||pos>=inr.length)return;
		inr[pos] = i;
	}
	
	public void setCompiled(String c){
		compiled = c;
	}
	
	public String getCompiled(){
		return compiled;
	}
	
	public static Color colorPatch(int value){
		switch (value) {
		case MARK_NORMAL:
			return Color.WHITE;
		case MARK_PATCH:
			return lightBlue;
		case MARK_VALUE:
			return Color.MAGENTA;
		case MARK_ERROR:
			return Color.RED;
		case MARK_COMM:
			return Color.GREEN;

		default:
			return Color.RED;
		}
	}

}
