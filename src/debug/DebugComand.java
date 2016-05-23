package debug;

public class DebugComand {
	
	private static boolean game;
	private static boolean system;
	private static boolean debug;
	
	private static int trueFalseMark = 0;
	
	private static boolean printDevidedCom = true;
	
	public static void operateComand(String s){
		if(s.length()==0)return;
		if(trueFalseMark != 0){
			operateTrueFalseMark(s);
			return;
		}
		String[][] str = devideComand(s);
		if(s.charAt(0)=='F'){
			return;
		}
		if(s.charAt(0)=='/'){
			return;
		}
		if(s.charAt(0)=='*'){
			inSystem(str);
			return;
		}
		if(s.charAt(0)=='<'&&s.charAt(s.length()-1)=='>'){
			return;
		}
	}
	
	private static void inSystem(String[][] s){
		System.out.println(s);
		if(s[1][0].compareToIgnoreCase("ramDet")== 0){
			Runtime r = Runtime.getRuntime();
			long a = r.freeMemory();
			long b = r.maxMemory();
			long c = r.totalMemory();
			
			Debug.println("* Detailed Ram Information:", Debug.COM);
			Debug.println(" Free:  "+a+" byte", Debug.SUBCOM);
			Debug.println(" MAX:  "+b+" byte", Debug.SUBCOM);
			Debug.println(" Total: "+c+" byte", Debug.SUBCOM);
		}
		if(s[1][0].compareToIgnoreCase("ram")== 0){
			Runtime r = Runtime.getRuntime();
			long a = r.freeMemory();
			long b = r.maxMemory();
			long c = r.totalMemory();
			
			Debug.println(" Maximum Ram:", Debug.COM);
			Debug.println(" ", Debug.COM);
			Debug.printProgressBar(c-a, b, Debug.SUBCOM, true);
			Debug.println(" "+(c-a)/1000000+" Mb of "+b/1000000+" Mb", Debug.SUBCOM);
			Debug.println(" Used Ram:", Debug.COM);
			Debug.println(" ", Debug.COM);
			Debug.printProgressBar(c-a, c, Debug.SUBCOM, true);
			Debug.println(" "+(c-a)/1000000+" Mb of "+c/1000000+" Mb", Debug.SUBCOM);
			return;
		}
		if(s[1][0].compareToIgnoreCase("gc")== 0){
			long time = System.currentTimeMillis();
			Debug.println("Starting Garbage-Colector...", Debug.COM);
			Runtime.getRuntime().gc();
			Debug.println("DONE! Time: "+(System.currentTimeMillis()-time)+"ms", Debug.COM);
			return;
		}
		if(s[1][0].compareToIgnoreCase("stop")== 0){
			Debug.println("Do you want to Stop the Program? [j]/[n]", Debug.COM);
			trueFalseMark = 4567;
			return;
		}
		if(s[1][0].compareToIgnoreCase("info")== 0){
			Debug.println("EMPTY "+Runtime.getRuntime().toString(), Debug.COM);
			return;
		}
		if(s[1][0].compareToIgnoreCase("system")== 0){
			
		}
		if(s[1][0].compareToIgnoreCase("graphic")== 0){
			
		}
		if(s[1][0].compareToIgnoreCase("cursor")== 0){
			DebGraphic.processCursor(s);
		}
	}
	
	private static void operateTrueFalseMark(String s){
		boolean operTF = false;
		if(s.compareTo("j") == 0){
			operTF = true;
		}
		switch (trueFalseMark) {
		case 4567:
			if(operTF){
				Debug.println("Stopping Threads...", Debug.ERROR);
				
			}else{
				Debug.println("Cancled", Debug.COMERR);
			}
			break;

		default:
			break;
		}
		trueFalseMark = 0;
	}
	
	private static String[][] devideComand(String com){
		String[][] s = new String[5][2];
		int pos = 1;
		int i;
		for (int j = 0; j < s.length; j++) {
			s[j][0] = "";
			s[j][1] = "";
		}
		s[0][0] += com.charAt(0);
		for (i = 1; i < com.length(); i++) {
			char c = com.charAt(i);
			if(c =='.'){
				pos++;
				if(pos >= 5){
					Debug.println("- Comand got ignored elements!", Debug.COMERR);
				}
			}
			else if(c ==' '){
				break;
			}else{
				s[pos][0]+=c;
			}
		}
		pos = 0;
		for (i+=1; i < com.length(); i++) {
			char c = com.charAt(i);
			if(c ==' '){
				pos++;
				if(pos >= 5){
					Debug.println("- Comand got ignored elements!", Debug.COMERR);
				}
			}
			else{
				s[pos][1]+=c;
			}
		}
		
		if(printDevidedCom){
			Debug.println("Com: ", Debug.COM);
			for (int j = 0; j < s.length; j++) {
				Debug.print("["+s[j][0]+"] ", Debug.PRICOM);
			}
			Debug.println("Cap: ", Debug.COM);
			for (int j = 0; j < s.length; j++) {
				Debug.print("["+s[j][1]+"] ", Debug.PRICOM);
			}
		}
		
		return s;
	}
	
	public static int stringToInt(String s){
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			Debug.println("Can't convert "+s+"to a number :(", Debug.COMERR);
			return 0;
		}
	}

}
