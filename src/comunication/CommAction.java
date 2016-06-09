package comunication;

import process.ProcessControl;

public class CommAction {
	
	public CommAction(){
		
	}
	
	public void processString(String s){
		if(s.charAt(0) == '*'){
			telemetry(s.substring(1));
		}
	}
	
	private void telemetry(String s){
		if(s.length()<4) return;
		String p = s.substring(0, 3);
		if(p.contains("BAT")){
			ProcessControl.pow.processStringBAT(s);
		}
	}

}
