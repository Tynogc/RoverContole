package process;

public class GPSControle {
	
	private GPSsatelite[] sats;
	public static int numOfSats = 14;
	
	public GPSControle(){
		sats = new GPSsatelite[numOfSats];
		for (int i = 0; i < sats.length; i++) {
			sats[i] = new GPSsatelite((int)(Math.random()*99));
		}
		sats[3].setNumber(0);
		sats[6].setNumber(0);
	}
	
	public GPSsatelite getSat(int num){
		if(num < 0 || num > sats.length){
			debug.Debug.println("* Error GPScontrole01: Requested Satelite OOB: "+num, debug.Debug.ERROR);
			return new GPSsatelite(0);
		}
		return sats[num];
	}

}
