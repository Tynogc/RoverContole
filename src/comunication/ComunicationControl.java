package comunication;

import guiMenu.WarnMenu;

public class ComunicationControl {
	
	private Linker linker;
	
	public static int timeToWait = 10;
	
	private guiMenu.ComunicationMenu comMenu;
	
	private SendString sendString;
	
	public static final int TIME_TO_PING = 3000;
	public static final int TIME_DELAY_WARN = 1000;
	public static final int TIME_DELAY_ERR = 3000;
	
	public static int lostPacks;
	public static int lpTerminater;
	
	private static boolean quitRestarting = false;
	private static boolean sendTelemetry = true;
	
	private static boolean isRestarting = false;
	
	private static boolean LostPackageWarning = false;
	
	//public static String connStatus = "";
	public static boolean connError = false;
	
	public static int deltaAbs = 0;
	public static int deltaLast = 0;
	
	public static double ecessTime = 0;
	
	private static final int WARN_TYPE = guiMenu.WarnMenu.TYPE_CONNECTION;
	
	public static ComunicationControl com;
	
	private boolean interuptSend;
	
	private CommAction action;
	
	public ComunicationControl(){
		action = new CommAction();
		
		interuptSend = false;
		
		linker = new Linker();
		
		debug.Debug.println("Waiting for response - ");
		for (int i = 0; i < timeToWait; i++) {
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(linker.conectionStatus()) break;
			printWait(i);
		}
		debug.Debug.println(linker.connectionPwAsked);
		if(linker.conectionStatus()){
			debug.Debug.println("Response: "+linker.getResponse());
			debug.Debug.bootMsg("Server response!", 0);
			//connStatus = "Connected!";
			guiMenu.WarnMenu.warn.setAlarm(false, WARN_TYPE, "Connected");
			connError = false;
		}else{
			debug.Debug.println("No response after "+timeToWait/2+"s");
			debug.Debug.println(linker.getResponse(),debug.Debug.ERROR);
			debug.Debug.bootMsg("Server response!", 1);
			//quitRestarting = true;
			guiMenu.WarnMenu.warn.setAlarm(true, WARN_TYPE, "Re-Connecting");
			connError = true;
		}
		lastPingSend = System.currentTimeMillis();
		
		com = this;
	}
	
	private void printWait(int i){
		i = i%4;
		debug.Debug.remove(3);
		switch (i) {
		case 3:
			debug.Debug.print(" - ");
			break;
		case 2:
			debug.Debug.print(" / ");
			break;
		case 1:
			debug.Debug.print(" | ");
			break;
		case 0:
			debug.Debug.print(" \\ ");
			break;

		default:
			break;
		}
	}
	
	private int pingId = 1;
	private long lastPingSend;
	private boolean pendingPing;
	
	private int xlp1 = 0;
	
	public void process(){
		if(!linker.conectionStatus()){
			if(!quitRestarting){
				quitRestarting = true;
				isRestarting = true;
				guiMenu.WarnMenu.warn.setAlarm(true, WARN_TYPE, "Re-Connecting");
				debug.Debug.println("***RESTARTING COMUNICATION***", debug.Debug.MASSAGE);
				Thread th = new Thread("restart"){
					public void run(){
						restart();
						debug.Debug.println("Thread to restart: Stoped!");
						isRestarting = false;
					}
				};
				th.start();
				debug.Debug.println("Thread to restart: Started!");
			}
			if(!connError){
				//connStatus = "NO CONNECTION!";
				guiMenu.WarnMenu.warn.setAlarm(true, WARN_TYPE, "NO CONNECTION");
				connError = true;
			}
			if(!linker.isRunning()){
				if(!quitRestarting){
					//connStatus = "RE-CONNECTING";
					guiMenu.WarnMenu.warn.setAlarm(true, WARN_TYPE, "Re-Connecting");
				}
			}
			return;
		}
		String[] k = linker.recive();
		if(k != null){
			if(k.length != 0){
				if(comMenu!=null){
					comMenu.setDownLink(k);
				}
				for (int i = 0; i < k.length; i++) {
					reciveMsg(k[i]);
				}
			}
		}
		long time = System.currentTimeMillis();
		if(!pendingPing){
			if(time-lastPingSend >= TIME_TO_PING){
				lastPingSend = time;
				sendPing();
			}
		}
		
		SendString s = sendString;
		long delta = 0;
		deltaAbs = 0;
		String max = "";
		while(s != null){
			delta = time-s.time;
			if(delta>deltaAbs){
				deltaAbs = (int)delta;
				max = s.text;
			}
			s=s.next;
		}
		if(deltaAbs>TIME_DELAY_ERR){
			handleLostPackage(max);
		}else if(deltaAbs>TIME_DELAY_WARN && xlp1<=0){
			debug.Debug.println("* Warning long Com-Delay ("+deltaAbs+"ms)",debug.Debug.WARN);
			xlp1=200;
		}
		xlp1--;
		if(xlp1<0)xlp1 = 0;
	}
	
	public void send(String[] s){
		if(interuptSend){
			return;
		}
		if(comMenu!=null){
			comMenu.setUpLink(s);
		}
		for (int i = 0; i < s.length; i++) {
			if(s[i]==null)
				continue;
			
			linker.send(s[i]);
			
			if(s[i].length()>=1){
				if(s[i].charAt(0)=='>')
					action.securityCodePending();
			}
			
			if(sendString == null){
				sendString = new SendString(s[i], System.currentTimeMillis());
			}else{
				sendString.add(new SendString(s[i], System.currentTimeMillis()));
			}
		}
	}
	
	public void send(String s){
		send(new String[]{s});
	}
	
	public void setComMenue(guiMenu.ComunicationMenu c){
		comMenu = c;
	}
	
	private void sendPing(){
		if(pendingPing){
			debug.Debug.println("* ERROR ComunicationControle01: Can't send Ping while Ping is Pending!", debug.Debug.ERROR);
			return;
		}
		pendingPing = true;
		int i = (int)(Math.random()*7000)+1000;
		if(i == pingId){
			i++;
		}
		pingId = i;
		String pinStr = "PING "+pingId;
		//debug.Debug.println("* Ping send! "+pinStr, debug.Debug.MASSAGE);
		send(pinStr);
	}
	
	private void reciveMsg(String msg){
		if(msg == null)return;
		if(msg.contains("RESPONSE")){
			lpTerminater = 0;
			if(LostPackageWarning){
				LostPackageWarning = false;
				WarnMenu.warn.setAlarm(false, WarnMenu.TYPE_CONNECTION, "Response is OK");
			}
			
			if(msg.length() <= 8){
				debug.Debug.println("* Communication Fault: RESPONSE was send empty!", debug.Debug.ERROR);
			}
			SendString s = sendString;
			msg = msg.substring(0, msg.length()-8);
			if(pendingPing){
				if(msg.contains("PING") && msg.contains(""+pingId)){
					pendingPing = false;
					//debug.Debug.println(" Ping recived! "+msg, debug.Debug.MASSAGE);
				}
			}
			
			while (s != null) {
				if(s.text.compareToIgnoreCase(msg) == 0){
					//TODO quitierung der nachricht best�tigen
					if(lastPingSend < s.time){
						lastPingSend = s.time;
					}
					deltaLast = (int)(System.currentTimeMillis()-s.time);
					ecessTime = (ecessTime*9+(double)(deltaLast))/10;
					sendString = sendString.remove(msg);
					return;
				}
				s = s.next;
			}
			
			debug.Debug.println("* Communication Fault: RESPONSE dosn't match to sendet String!", debug.Debug.WARN);
			debug.Debug.println(" RESPONSE is: "+msg, debug.Debug.SUBWARN);
		}else{
			action.processString(msg);
		}
	}
	
	private void handleLostPackage(String s){
		linker.send(s);
		if(s.contains("PING")){
			lpTerminater++;
			debug.Debug.println("* Error, Comunication broken! Delay "+deltaAbs+"ms",debug.Debug.ERROR);
			debug.Debug.println("Lost count:"+lpTerminater+" Total loses:"+lostPacks, debug.Debug.SUBERR);
		}else{
			debug.Debug.println("* Resending MSG: "+s+"; Delta is"+deltaAbs+"ms",debug.Debug.ERROR);
		}
		lostPacks++;
		
		if(lpTerminater >= 2){
			//linker.terminate();
			if(!LostPackageWarning){
				LostPackageWarning = true;
				WarnMenu.warn.setAlarm(true, WARN_TYPE, "Waiting for Response...");
			}
		}
		SendString st = sendString;
		while(st != null){
			if(st.text.compareToIgnoreCase(s) == 0){
				st.time = System.currentTimeMillis();
				return;
			}
			st = st.next;
		}
		debug.Debug.println("* Process ERROR in ComunicationControle", debug.Debug.ERROR);
	}
	
	@SuppressWarnings("static-access")
	private void restart(){
		linker.terminate();
		
		SendString s = sendString;
		long t = System.currentTimeMillis();
		while(s!=null){
			s.time = t+10000;
			s = s.next;
		}
		
		linker = new Linker();
		guiMenu.WarnMenu.warn.setAlarm(true, WARN_TYPE, "Re-Connecting");
		debug.Debug.println("Waiting for response...");
		for (int i = 0; i < timeToWait*3; i++) {
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(linker.conectionStatus()) break;
			//printWait(i);
		}
		debug.Debug.println(linker.connectionPwAsked);
		if(linker.conectionStatus()){
			debug.Debug.println("Response: "+linker.getResponse());
			debug.Debug.bootMsg("Server response!", 0);
			quitRestarting = false;
			//connStatus = "Connected!";
			guiMenu.WarnMenu.warn.setAlarm(false, WARN_TYPE, "Connected");
			connError = false;
		}else{
			debug.Debug.println("No response after "+3*timeToWait/2+"s");
			debug.Debug.println(linker.getResponse(),debug.Debug.ERROR);
			debug.Debug.bootMsg("Server response!", 1);
			//connStatus = "No Connection!";
			guiMenu.WarnMenu.warn.setAlarm(true, WARN_TYPE, "NO CONNECTION");
			if(linker.isRunning()){
				debug.Debug.println("Pipe is good! But Server isn't responding...");
				//connStatus = "Pipe is set, but no Response!";
				guiMenu.WarnMenu.warn.setAlarm(true, WARN_TYPE, "Pipe is set, but no Response!");
				connError = true;
			}
			
			quitRestarting = true;
		}
		
		if(!linker.isRunning()){
			debug.Debug.println(" Pipe is brocken, no actions!");
			return;
		}
		
		lastPingSend = System.currentTimeMillis();
		
		debug.Debug.println("Resending all MSG");
		s = sendString;
		t = System.currentTimeMillis();
		while(s!=null){
			linker.send(s.text);
			s.time = t;
			s = s.next;
		}
	}
	
	public void closeConnection(String s){
		debug.Debug.println("* Connection is going to be closed! Reason:"+s, debug.Debug.WARN);
		//TODO handel stop;
		if(linker != null){
			linker.terminate();
		}
		quitRestarting = true;
	}
	
	public void changeTelemetrySendState(boolean b){
		if(b == sendTelemetry)return;
		sendTelemetry = b;
		if(sendTelemetry){
			debug.Debug.println("* Telemetry will be send again!", debug.Debug.MASSAGE);
			send("E RS_Telem");
		}else{
			debug.Debug.println("* Telemetry will be STOPED!", debug.Debug.WARN);
			send("E STOP_Telem");
		}
	}
	
	public void whipeSendStrings(){
		debug.Debug.println("* Wiping Send Strings...", debug.Debug.WARN);
		
		sendString = null;
		linker.whipe();
		
		pendingPing = false;
	}
	
	public static boolean telemetrySendState(){
		return sendTelemetry;
	}
	
	public static boolean isRestarting(){
		return isRestarting;
	}
	
	public static void restartConnection(){
		if(isRestarting){
			debug.Debug.println("* WARNING! Restart is already happening!", debug.Debug.ERROR);
			debug.Debug.println(" Restart of Linker is done twice, this may lead to malefunctions", debug.Debug.SUBERR);
		}
		
		quitRestarting = false;
	}
	
	public void enterDebugLink(String s){
		debug.Debug.println("* ALARM Debug-Comand entered! "+s, debug.Debug.COMERR);
		action.processString(s);
		if(comMenu!=null){
			comMenu.setDownLink(new String[]{s});
		}
	}
	
	public boolean getInterupt(){
		return interuptSend;
	}
	
	public void setIneruptSend(boolean b){
		interuptSend = b;
		if(!b){
			pendingPing = false;
		}
	}

}
