package process;

public class ProcessControl {
	
	public static GPSControle gps;
	public static WayInformation way;
	public static PowerSystem pow;
	
	public ProcessControl(){
		gps = new GPSControle();
		way = new WayInformation();
		pow = new PowerSystem();
	}
	
	public void loop(){
		pow.check();
	}
	
}
