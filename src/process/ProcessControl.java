package process;

public class ProcessControl {
	
	public static GPSControle gps;
	public static WayInformation way;
	
	public ProcessControl(){
		gps = new GPSControle();
		way = new WayInformation();
	}
	
	public void loop(){
		
	}
	
}
