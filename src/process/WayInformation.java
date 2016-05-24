package process;

public class WayInformation {
	
	public static final double ERR = Double.NEGATIVE_INFINITY;
	
	public double prograde(){
		return 89;
	}
	
	public double via(){
		return 104;
	}
	
	public double dir(){
		return 92;
	}
	
	public double target(){
		return ERR;
	}
	
	public double corr(){
		if(prograde() == ERR)return ERR;
		double l = prograde()-dir();
		if((l<0&&l>-180)||l>180)return (prograde()+90)%360;
		l = prograde()-90;
		if(l<0)l+=360;
		return l%360;
	}
	
	public int distanceToWay(){
		return (int)(System.currentTimeMillis()/1000)%30;
	}
	
	public double roverOrientation(){
		return (double)(System.currentTimeMillis()/300)%360;
		//return prograde();
	}
	
	public boolean onPrograde(){
		if(prograde()==ERR)return true;
		return Math.abs(prograde()-roverOrientation())<1;
	}

}
