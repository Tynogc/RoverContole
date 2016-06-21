package comunication;

import guiMenu.SettingsMenu;
import process.ProcessControl;

public class CommAction {
	
	private int currentLog;
	private boolean secureComPending;
	
	public CommAction(){
		secureComPending = false;
	}
	
	public void processString(String s){
		if(s==null)return;
		if(s.length()<2)return;
		
		if(s.charAt(0) == '*'){
			telemetry(s.substring(1));
		}else if(s.charAt(0) == '#'){
			//TODO process Error
		}else if(s.charAt(0)=='/'){
			log(s.substring(1));
		}else if(s.charAt(0)=='>'){
			sendFP(s.substring(1));
		}
	}
	
	private void telemetry(String s){
		if(s.length()<4) return;
		String p = s.substring(0, 3);
		if(p.contains("BAT")){
			ProcessControl.pow.processStringBAT(s);
		}else if(p.contains("SSK")){
			ProcessControl.ssk.processStringSSK(s);
		}else if(p.contains("COI")){
			userInterface.GuiControle.comMenu.processCOI(s);
		}
	}
	
	private void log(String s){
		if(s.charAt(0)=='~'){
			if(s.length()>=2){
				if(s.charAt(1)=='~')
					SettingsMenu.set.clear(currentLog);
			}
			if(s.contains("START")){
				currentLog = 1;
			}else if(s.contains("FAULT")){
				currentLog = 2;
			}else if(s.contains("INFO")){
				SettingsMenu.set.addSettings(s, currentLog);
			}else{
				currentLog = 0;
			}
			return;
		}
		SettingsMenu.set.addText(s, currentLog);
	}
	
	private void sendFP(String s){
		if(secureComPending){
			int i = 0;
			try {
				i = Integer.parseInt(s);
			} catch (Exception e) {
				debug.Debug.println("* ERROR CommAction07: cant Convert to int:"+s, debug.Debug.ERROR);
				return;
			}
			
			FingerPrint f = new FingerPrint();
			debug.Debug.println("* A Security Key is Requested: Sending...!", debug.Debug.TEXT);
			ComunicationControl.com.send("<"+f.getFingerprintAt(i));
			secureComPending = false;
		}else{
			debug.Debug.println("* A Security Key is Requested, however no Key should be sent!", debug.Debug.ERROR);
		}
	}

	public void securityCodePending(){
		secureComPending = true;
	}
}
