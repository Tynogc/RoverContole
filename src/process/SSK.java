package process;

public class SSK {
	
	private int status;
	
	public int mc1;
	public int mc2;

	private int m1;
	private int m2;
	private int m3;
	private int m4;
	
	private boolean[] active;
	
	public int getStatus() {
		return status;
	}
	public int getM1() {
		return m1;
	}
	public int getM2() {
		return m2;
	}
	public int getM3() {
		return m3;
	}
	public int getM4() {
		return m4;
	}
	
	public static final String[] csP = new String[]{
		"OK","INTERUPT","OFF","RESET"
	};
	public static final String[] csQ = new String[]{
		"Need RES","Need START","START UP",""
	};
	public static final String[] cs0 = new String[]{
		"OFF","RES","STU","OK","INT"
	};
	public static final String[] cs1 = new String[]{
		"NOI","ECS","OMP","QDP"
	};
	public static final String[] cs2 = new String[]{
		"NOI","ALCS","DIS"
	};
	public static final String[] cs3 = new String[]{
		"NOI","OK","CONN","OASIS","ECAM","PING"
	};
	public static final String[] cs4 = new String[]{
		"NOI","PCC","SIMP","TEMP"
	};
	
	public SSK(){
		mc1 = 2;
		mc2 = 0;
		active = new boolean[6];
	}
	
	public void processStringSSK(String s){
		String[] st = s.split("_");
		if(st.length<=1){
			debug.Debug.println("* Problem handling Input: SSK: String not alined! "+s, debug.Debug.WARN);
			return;
		}
		if(st[0].compareTo("SSK")!=0){
			debug.Debug.println("* Problem handling Input: SSK: Wrong string! "+s, debug.Debug.ERROR);
			return;
		}
		//Statusmeldung
		status = searchPos(st[1], cs0);
		switch (status) {
		case 0://OFF
			mc1 = 2;
			mc2 = 0;
			break;
		case 1://RES
			mc1 = 3;
			mc2 = 1;
			break;
		case 2://STU
			mc1 = 0;
			mc2 = 2;
			break;
		case 3://OK
			mc1 = 0;
			mc2 = 3;
			break;
		case 4://INT
			mc1 = 1;
			mc2 = 3;
			break;

		default:
			status = 0;
			debug.Debug.println("* Problem handling Input: SSK: Couldn't resolve! [1] "+s, debug.Debug.WARN);
			break;
		}
		
		if(st.length <= 5)return;
		m1 = searchPos(st[2], cs1);
		if(m1 < 0){
			m1 = 0;
			debug.Debug.println("* Problem handling Input: SSK: Couldn't resolve! [2] "+s, debug.Debug.WARN);
		}
		m2 = searchPos(st[3], cs2);
		if(m2 < 0){
			m2 = 0;
			debug.Debug.println("* Problem handling Input: SSK: Couldn't resolve! [3] "+s, debug.Debug.WARN);
		}
		m3 = searchPos(st[4], cs3);
		if(m3 < 0){
			m3 = 0;
			debug.Debug.println("* Problem handling Input: SSK: Couldn't resolve! [4] "+s, debug.Debug.WARN);
		}
		m4 = searchPos(st[5], cs4);
		if(m4 < 0){
			m4 = 0;
			debug.Debug.println("* Problem handling Input: SSK: Couldn't resolve! [5] "+s, debug.Debug.WARN);
		}
		if(st.length <= 6)return;
		for (int i = 0; i < st[6].length(); i++) {
			if(i>=active.length)return;
			
			active[i] = st[6].charAt(i)=='x';
		}
	}
	
	private int searchPos(String s, String[] d){
		for (int i = 0; i < d.length; i++) {
			if(s.compareToIgnoreCase(d[i])== 0)return i;
		}
		return -1;
	}
	
	public boolean getActiv(int i){
		if(i< 0 || i >= active.length){
			debug.Debug.println("* ERROR SSK10a index oob! "+i, debug.Debug.ERROR);
			return false;
		}
		return active[i];
	}

}
