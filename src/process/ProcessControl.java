package process;

public class ProcessControl {
	
	public static GPSControle gps;
	public static WayInformation way;
	public static PowerSystem pow;
	public static SSK ssk;
	
	public ProcessControl(){
		gps = new GPSControle();
		way = new WayInformation();
		pow = new PowerSystem();
		ssk = new SSK();
	}
	
	public void loop(){
		pow.check();
	}
	
}
