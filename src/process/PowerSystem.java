package process;

import guiMenu.WarnMenu;

public class PowerSystem {
	
	public static final int MAX_VOLTS = 420;//TODO richtig zuweisen
	public static final int VOLTS_WARN = 340;//TODO richtig zuweisen
	public static final int ALARM_VOLTS = 320;//TODO richtig zuweisen
	public static final int MIN_VOLTS = 310;//TODO richtig zuweisen
	public static final int DISABLED = -1;

	public boolean wasUpdated = false;
	public boolean wasUpdatedC = false;
	private boolean updIntern = false;
	
	public int[] akku1;
	public int[] akku2;
	public int[] akku3;
	
	//Cellcount
	public static final int ak1Size = 4;
	public static final int ak2Size = 4;
	public static final int ak3Size = 4;
	
	public static int volt1;
	public static int volt2;
	public static int voltBar1;
	public static int voltBar2;
	
	public static byte[] switches;
	
	public PowerSystem(){
		akku1 = new int[ak1Size+1];
		akku2 = new int[ak2Size+1];
		akku3 = new int[ak3Size+1];
		
		volt1 = DISABLED;
		volt2 = DISABLED;
		
		switches = new byte[40];
		
		for (int i = 0; i < akku1.length; i++) {
			akku1[i] = DISABLED;
		}
		for (int i = 0; i < akku2.length; i++) {
			akku2[i] = DISABLED;
		}
		for (int i = 0; i < switches.length; i++) {
			switches[i] = 0;
		}
	}
	
	public void processStringBAT(String s){
		String[] st = s.split("_");
		if(st.length<=2){
			debug.Debug.println("* Problem handling Input: PowerSystem: String not alined! "+s, debug.Debug.WARN);
			return;
		}
		if(st[0].compareTo("BAT")!=0){
			debug.Debug.println("* Problem handling Input: PowerSystem: Wrong string! "+s, debug.Debug.ERROR);
			return;
		}
		if(st[1].compareTo("M1")==0){
			processStringToBat(akku1,st);
		}else if(st[1].compareTo("M2")==0){
			processStringToBat(akku2,st);
		}else if(st[1].compareTo("M3")==0){
			processStringToBat(akku3,st);
		}else{
			debug.Debug.println("* Problem handling Input: PowerSystem: Could not resolve! "+s, debug.Debug.WARN);
			return;
		}
	}
	
	public void processStringESV(String s){
		wasUpdatedC = true;
		String[] st = s.split("_");
		if(st.length<=1){
			debug.Debug.println("* Problem handling Input: PowerSystem: String not alined! "+s, debug.Debug.WARN);
			return;
		}
		if(st[0].compareTo("ESV")!=0){
			debug.Debug.println("* Problem handling Input: PowerSystem: Wrong string! "+s, debug.Debug.ERROR);
			return;
		}
		for (int i = 1; i < st.length-1; i+=2) {
			byte a;
			if(st[i+1].compareToIgnoreCase("X")==0) a = 0;
			else if(st[i+1].compareToIgnoreCase("Y")==0) a = 1;
			else if(st[i+1].compareToIgnoreCase("N")==0) a = 2;
			else if(st[i+1].compareToIgnoreCase("W")==0) a = 3;
			else{
				debug.Debug.println("* Problem handling Input: PowerSystem: Wrong Tag! "+s, debug.Debug.WARN);
				continue;
			}
			int d = 0;
			try {
				d = Integer.parseInt(st[i]);
			} catch (IllegalArgumentException e) {
				debug.Debug.println("* Problem handling Input: PowerSystem: Can't convert to int! "+s, debug.Debug.WARN);
				continue;
			}
			if(d<0 || d>=switches.length){
				debug.Debug.println("* Problem handling Input: PowerSystem: Switch dosn't exixt! "+s, debug.Debug.ERROR);
				continue;
			}
			switches[d] = a;
		}
	}
	
	private void processStringToBat(int[] a, String[] s){
		updIntern = true;
		if(s.length >= 4)wasUpdated = true;
		for (int i = 2; i < s.length; i++) {
			if(i-2>= a.length){
				debug.Debug.println("* Problem handling Input: PowerSystem: to many Numbers!", debug.Debug.WARN);
				return;
			}
			int q;
			try {
				q = Integer.parseInt(s[i]);
			} catch (Exception e) {
				debug.Debug.println("* Problem handling Input: PowerSystem: Could not resolve! "+s[i], debug.Debug.WARN);
				q = DISABLED;
			}
			a[i-2] = q;
		}
	}
	
	private boolean thereWasAlBat = false;
	public void check(){
		if(updIntern){
			updIntern = false;
			
			volt1 = akku1[0];
			volt2 = akku2[0];
			
			double d = (double)(akku1[0]-VOLTS_WARN*ak1Size)/((MAX_VOLTS-VOLTS_WARN)*ak1Size);
			voltBar1 = (int)(d*4)+1;
			d = (double)(akku2[0]-VOLTS_WARN*ak2Size)/((MAX_VOLTS-VOLTS_WARN)*ak2Size);
			voltBar2 = (int)(d*4)+1;
			if(volt1 > DISABLED && voltBar1 <0)voltBar1 = 0;
			if(volt2 > DISABLED && voltBar2 <0)voltBar2 = 0;
			if(voltBar1>3)voltBar1 = 3;
			if(voltBar2>3)voltBar2 = 3;
			
			String warn = null;
			String alarm = null;
			for (int i = 1; i < akku2.length; i++) {
				if(akku2[i] == DISABLED){
					if(akku2[0] != DISABLED){
						warn = "No Data: M2."+(i-1);
					}
				}else if(akku2[i] <= MIN_VOLTS){
					alarm = "Cell Voltage Low: M2."+(i-1);
					break;
				}else if(akku2[i] <= VOLTS_WARN){
					warn = "Cell Voltage Low: M2."+(i-1);
				}
			}
			for (int i = 1; i < akku1.length; i++) {
				if(akku1[i] == DISABLED){
					if(akku1[0] != DISABLED && warn == null){
						warn = "No Data: M1."+(i-1);
					}
				}else if(akku1[i] <= MIN_VOLTS){
					alarm = "Cell Voltage Low: M1."+(i-1);
					break;
				}else if(akku1[i] <= VOLTS_WARN){
					warn = "Cell Voltage Low: M1."+(i-1);
				}
			}
			
			if(akku1[0]<ALARM_VOLTS*ak1Size){
				if(akku1[0] == DISABLED){
					warn = "M1 Disabled /NoData!";
				}else{
					alarm = "Voltage criticaly Low! M1";
				}
			}else if(akku2[0]<ALARM_VOLTS*ak2Size){
				if(akku2[0] == DISABLED){
					warn = "M2 Disabled /NoData!";
				}else{
					alarm = "Voltage criticaly Low! M2";
				}
			}
			if(akku1[0]<VOLTS_WARN*ak1Size){
				if(akku1[0] == DISABLED){
					warn = "M1 Disabled /NoData!";
				}else{
					warn = "Voltag LOW: M1";
				}
			}else if(akku2[0]<VOLTS_WARN*ak2Size){
				if(akku2[0] == DISABLED){
					warn = "M2 Disabled /NoData!";
				}else{
					warn = "Voltag LOW: M2";
				}
			}
			
			if(alarm != null){
				WarnMenu.warn.setAlarm(true, WarnMenu.TYPE_ELEC_BATTERY, alarm);
				thereWasAlBat = true;
			}else if(thereWasAlBat){
				thereWasAlBat = false;
				WarnMenu.warn.setAlarm(false, WarnMenu.TYPE_ELEC_BATTERY, "No Alarm");
			}
			if(warn != null){
				WarnMenu.warn.setWarn(true, WarnMenu.TYPE_ELEC_BATTERY, warn);
			}else{
				WarnMenu.warn.setWarn(false, WarnMenu.TYPE_ELEC_BATTERY, "Battery OK");
				//WarnMenu.warn.setAlarm(false, WarnMenu.TYPE_ELEC_BATTERY, "Battery OK");
			}
		}
	}
}
