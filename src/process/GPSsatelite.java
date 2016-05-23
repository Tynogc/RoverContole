package process;

public class GPSsatelite {
	
	private int number;
	
	private int sigStrenght;
	private int sigStrenghtLast;
	private int sigStrenghtMax;
	
	public static final int maxStrenght = 100;
	
	private byte state;
	
	public byte getState() {
		return state;
	}
	public void setState(byte state) {
		this.state = state;
	}
	public GPSsatelite(int num){
		number = num;
		state = 1;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public void setSigStr(int str){
		sigStrenghtLast = sigStrenght;
		sigStrenght = str;
		if(str > sigStrenghtMax){
			sigStrenghtMax = str;
		}else if(Math.random() >= 0.9){
			sigStrenghtMax = str;
		}
	}
	public int getSigStrenght() {
		return sigStrenght;
	}
	public int getSigStrenghtLast() {
		return sigStrenghtLast;
	}
	public int getSigStrenghtMax() {
		return sigStrenghtMax;
	}
	
}
