package guiMenu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.CommunicationException;
import javax.swing.ImageIcon;

import debug.Debug;
import userInterface.KeySystem;
import userInterface.MainFrame;
import userInterface.MainThread;
import menu.AbstractMenu;
import menu.Button;
import menu.DataFiled;
import menu.ScrollBar;

public class ComunicationMenu extends AbstractMenu{
	
	private final int arrayLenght = 30;
	
	private String[] upLink;
	private ScrollBar upLiScro;
	private DataFiled upLiDat;
	private long upLiTime;
	private int upLiPos;
	private BufferedImage upLiBuff;
	private Button upLiComm;
	private Button upLiRec;
	private boolean upLiIsRec;
	
	private String[] downLink;
	private ScrollBar doLiScro;
	private DataFiled doLiDat;
	private long doLiTime;
	private int doLiPos;
	private BufferedImage doLiBuff;
	private Button doLiComm;
	private Button doLiRec;
	private boolean doLiIsRec;
	
	private String[] commands;
	private ScrollBar commScro;
	private DataFiled commDat;
	private int commPos;
	private BufferedImage commBuff;
	private Button commSaveToConsol;
	private Button commDel;
	private Button commSaveToFile;
	
	private DataFiled comTextStatus;
	private DataFiled comTextStatus2;
	private DataFiled comTextStatus3;
	
	private Button reconect;
	
	private Font numbers;
	private Font text;
	
	private ImageIcon[] ima;
	
	private String filePath;
	
	private comunication.ConnectionChange changeCon;

	public ComunicationMenu(KeySystem k){
		filePath = "log/com/"+
				new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new java.util.Date (System.currentTimeMillis()));
		
		numbers = new Font(Font.DIALOG,Font.PLAIN, 10);
		text = new Font(Font.DIALOG,Font.PLAIN, 12);
		
		ima = new ImageIcon[]{
				new ImageIcon("res/win/sub/CD1.png"),
				new ImageIcon("res/win/sub/CD2.png"),
				new ImageIcon("res/win/sub/CD3.png"),
				new ImageIcon("res/win/sub/CD4.png")
		};
		
		downLink = new String[arrayLenght];
		doLiDat = new DataFiled(100,100,420,20,Color.blue) {
			@Override
			protected void uppdate() {
				setText("DownLink - "+tpSacc(doLiTime));
			}
			@Override
			protected void isClicked() {
			}
		};
		add(doLiDat);
		doLiScro = new ScrollBar(501, 121, 120, 10, arrayLenght);
		add(doLiScro);
		doLiComm = new Button(100,242,"res/win/gui/cli/G") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				analyseArray(downLink);
				commDat.setText("DownLink - At:"+
						new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date (System.currentTimeMillis())));
				commPos = -10;
			}
		};
		add(doLiComm);
		doLiComm.setText("Analyse");
		doLiRec = new Button(250,242,"res/win/gui/cli/Gs") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				doLiIsRec = !doLiIsRec;
				if(doLiIsRec){
					doLiRec.setText("STOP REC");
				}else{
					doLiRec.setText("REC");
				}
			}
		};
		add(doLiRec);
		doLiRec.setText("REC");
		doLiPos = -10;
		
		upLink = new String[arrayLenght];
		upLiDat = new DataFiled(100,300,420,20,Color.blue) {
			@Override
			protected void uppdate() {
				setText("UpLink - "+tpSacc(upLiTime));
			}
			@Override
			protected void isClicked() {
			}
		};
		add(upLiDat);
		upLiScro = new ScrollBar(501, 321, 120, 10, arrayLenght);
		add(upLiScro);
		upLiComm = new Button(100,442,"res/win/gui/cli/G") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				analyseArray(upLink);
				commDat.setText("UpLink - At:"+
						new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date (System.currentTimeMillis())));
				commPos = -10;
			}
		};
		add(upLiComm);
		upLiComm.setText("Analyse");
		upLiRec = new Button(250,442,"res/win/gui/cli/Gs") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				upLiIsRec = !upLiIsRec;
				if(upLiIsRec){
					upLiRec.setText("STOP REC");
				}else{
					upLiRec.setText("REC");
				}
			}
		};
		add(upLiRec);
		upLiRec.setText("REC");
		upLiPos = -10;
		
		commands = new String[arrayLenght];
		commDat = new DataFiled(100,500,420,20,Color.blue) {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isClicked() {
			}
		};
		add(commDat);
		commScro = new ScrollBar(501, 521, 120, 10, arrayLenght);
		add(commScro);
		commScro.setDisabled(true);
		commPos = -10;
		
		commSaveToConsol = new Button(100,642,"res/win/gui/cli/G") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				saveToConsole();
			}
		};
		add(commSaveToConsol);
		commSaveToConsol.setText("Send to Console");
		commSaveToFile = new Button(250,642,"res/win/gui/cli/B") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				saveToFile();
			}
		};
		add(commSaveToFile);
		commSaveToFile.setText("Save To File");
		commSaveToFile.setTextColor(Button.gray);
		
		commDel = new Button(400,642,"res/win/gui/cli/R") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				vanishComm();
			}
		};
		add(commDel);
		commDel.setText("DEL");
		commDel.setTextColor(Button.gray);
		
		comTextStatus = new DataFiled(100,70,200,20,Color.BLUE) {
			private boolean ctc = false;
			@Override
			protected void uppdate() {
//				comTextStatus.setText(comunication.ComunicationControl.connStatus);
//				if(ctc != comunication.ComunicationControl.connError){
//					ctc = !ctc;
//					if(ctc){
//						comTextStatus.setColor(Color.RED);
//						comTextStatus.setTextColor(Color.BLACK);
//					}else{
//						comTextStatus.setColor(Color.BLUE);
//						comTextStatus.setTextColor(Color.WHITE);
//					}
//				}
			}
			@Override
			protected void isClicked() {
				
			}
		};
		add(comTextStatus);
		WarnMenu.warn.setExtern(comTextStatus, WarnMenu.TYPE_CONNECTION);
		comTextStatus2 = new DataFiled(300,70,100,20,Color.GREEN) {
			private byte ctc = 22;
			@Override
			protected void uppdate() {
				int pa = (int)comunication.ComunicationControl.deltaLast;
				int pe = (int)comunication.ComunicationControl.deltaAbs;
				if(pe > pa)pa = pe;
				comTextStatus2.setText("Delay "+pa+"ms");
				byte b = 1;
				if(pa > 1000)b = 2;
				if(comunication.ComunicationControl.lpTerminater >= 1){
					comTextStatus2.setText("Delay >3000ms");
					b = 3;
				}
				if(comunication.ComunicationControl.connError)b = 4;
				
				if(ctc != b){
					switch (b) {
					case 1:
						comTextStatus2.setColor(Color.GREEN);break;
					case 2:
						comTextStatus2.setColor(Color.YELLOW);break;
					case 3:
						comTextStatus2.setColor(Color.RED);break;
					case 4:
						comTextStatus2.setColor(Color.BLACK);break;

					default:
						break;
					}
					ctc = b;
				}
			}
			@Override
			protected void isClicked() {
				
			}
		};
		add(comTextStatus2);
		comTextStatus3 = new DataFiled(400,70,130,20,Color.BLUE) {
			private boolean ctc = false;
			@Override
			protected void uppdate() {
				comTextStatus3.setText("Average "+(int)comunication.ComunicationControl.ecessTime+"ms");
				if(ctc != comunication.ComunicationControl.connError){
					ctc = !ctc;
					if(ctc){
						comTextStatus3.setColor(Color.RED);
						comTextStatus3.setTextColor(Color.BLACK);
					}else{
						comTextStatus3.setColor(Color.BLUE);
						comTextStatus3.setTextColor(Color.WHITE);
					}
				}
			}
			@Override
			protected void isClicked() {
				
			}
		};
		add(comTextStatus3);
		comTextStatus3.setTextColor(Color.WHITE);
		
		reconect = new Button(600,100,"res/win/gui/spb/NEW") {//TODO
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				comunication.ComunicationControl.quitRestarting = false;
			}
		};
		add(reconect);
		
		changeCon = new comunication.ConnectionChange(600, 300, k);
		
		add(changeCon.ip);
		add(changeCon.ipText);
		add(changeCon.port);
		add(changeCon.portText);
		add(changeCon.save);
		//debug.Debug.println(changeCon.save.getxSize()+" "+changeCon.save.getySize());
		
		setUpLink(new String[]{
				"***GPS***C124.234_L1254.43_H340_S10",
				"***TRM***F12_F12_F02_F02_K:L",
				"***FRL***All Good",
				"#P22_234",
				"",
				"[12:19.22] ",
				"***GPS***C124.234_L1254.43_H340_S10",
				"*Q23.789",
				"*END"
		});
		
		setDownLink(new String[]{
				"***ATS***",
				"**GPS**Lat33.43.5422",
				"**GPS**Lon156.41.8434",
				"**GPS**hight322"
		});
		
		doLiTime = System.currentTimeMillis();
		upLiTime = System.currentTimeMillis();
		
		vanishComm();
	}
	
	@Override
	protected void uppdateIntern() {
		if(doLiPos != doLiScro.getScrolled()){
			doLiBuff = paintADataArray(downLink, doLiScro.getScrolled());
			doLiPos = doLiScro.getScrolled();
		}
		if(upLiPos != upLiScro.getScrolled()){
			upLiBuff = paintADataArray(upLink, upLiScro.getScrolled());
			upLiPos = upLiScro.getScrolled();
		}
		if(commPos != commScro.getScrolled()){
			commBuff = paintADataArray(commands, commScro.getScrolled());
			commPos = commScro.getScrolled();
		}
		changeCon.uppdate();
	}

	@Override
	protected void paintIntern(Graphics g) {
		g.drawImage(doLiBuff, 100, 120, null);
		if(doLiIsRec){
			g.drawImage(ima[(int)(MainThread.currentTime%500)/125].getImage(),350,242,null);
		}
		
		g.drawImage(upLiBuff, 100, 320, null);
		if(upLiIsRec){
			g.drawImage(ima[(int)(MainThread.currentTime%500)/125].getImage(),350,442,null);
		}
		
		g.drawImage(commBuff, 100, 520, null);
	}
	
	private BufferedImage paintADataArray(String[] s, int pos){
		BufferedImage buff = new BufferedImage(402,122,BufferedImage.TYPE_INT_ARGB);
		Graphics g = buff.getGraphics();
		g.setColor(DataFiled.l1);
		g.drawRect(0,0,400,120);
		g.setColor(DataFiled.l2);
		g.drawRect(1,1,400,120);
		
		g.setFont(text);
		g.setColor(Color.blue);
		
		for (int i = pos; i < s.length; i++) {
			if(i>pos+10)break;
			if(s[i] == null)continue;
			if(s[i].length()<=10)continue;
			if(s[i].charAt(0) == '['){
				g.setFont(numbers);
				g.setColor(Color.white);
				g.drawString(s[i].substring(0, 10), 2, 10+(i-pos)*12);
				g.setFont(text);
				g.setColor(Color.blue);
			}
			if(s[i].charAt(10) == '#'){
				g.setColor(Color.red);
				g.drawString(s[i].substring(10), 51, 12+(i-pos)*12);
				g.setColor(Color.blue);
			}else if(s[i].charAt(10) == '$'){
				g.setColor(Color.gray);
				g.drawString(s[i].substring(10), 51, 12+(i-pos)*12);
				g.setColor(Color.blue);
			}else{
				g.drawString(s[i].substring(10), 51, 12+(i-pos)*12);
			}
		}
		
		return buff;
	}
	
	private void analyseArray(String[] s){
		vanishComm();
		if(s.length != commands.length){
			debug.Debug.println("* ERROR ComunicationMenu 01:", debug.Debug.ERROR);
			debug.Debug.println(" Length of Arrays aren't the same!", debug.Debug.SUBERR);
		}
		
		for (int i = 0; i < commands.length; i++) {
			if(i>=s.length)break;
			if(s[i]== null)continue;
			boolean f = false;
			for (int j = 0; j < i; j++) {
				if(s[i].length()<=10)break;
				if(s[j].length()<=10)continue;
				if(s[i].substring(10).compareTo(s[j].substring(10)) == 0){
					f = true;
					break;
				}
			}
			if(f){
				commands[i] = s[i].substring(0, 10)+"$"+s[i].substring(10);
			}else{
				commands[i] = s[i];
			}
			commScro.setDisabled(false);
			commDel.setDisabled(false);
			commSaveToConsol.setDisabled(false);
			commSaveToFile.setDisabled(false);
		}
	}
	
	public void setUpLink(String[] s){
		if(s == null)return;
		if(s.length == 0)return;
		for (int i = upLink.length; i >= 0; i--) {
			if(i+s.length>=upLink.length)continue;
			upLink[i+s.length] = upLink[i];
		}
		upLiTime = System.currentTimeMillis();
		
		upLink[0] = "["+new java.text.SimpleDateFormat("mm:ss.").format(new java.util.Date (upLiTime))+
				(System.currentTimeMillis()%1000)/10;
		
		if(upLink[0].length()==8){
			upLink[0] =upLink[0].substring(0, 7)+"0"+upLink[0].charAt(7)+"]"+s[0];
		}else{
			upLink[0] +="]"+s[0];
		}
		
		for (int i = 1; i < s.length; i++) {
			if(i>=upLink.length)break;
			upLink[i] = "          "+s[i];
		}
		upLiPos = -10;
		if(upLiIsRec){
			PrintWriter writer = null; 
			try { 
				writer = new PrintWriter(new FileWriter(filePath+"_UPLINK.txt",true),true); 
				for (int i = 0; i < s.length; i++) {
					if(i>=upLink.length)break;
					writer.println(upLink[i]);
				}
			} catch (IOException ioe) { 
				ioe.printStackTrace(); 
			} finally { 
				if (writer != null){ 
					writer.flush(); 
					writer.close(); 
				} 
			}
		}
		userInterface.ExitThread.setUpLink(s);
	}
	
	public void setDownLink(String[] s){
		if(s == null)return;
		if(s.length == 0)return;
		for (int i = downLink.length; i >= 0; i--) {
			if(i+s.length>=downLink.length)continue;
			downLink[i+s.length] = downLink[i];
		}
		doLiTime = System.currentTimeMillis();
		
		downLink[0] = "["+new java.text.SimpleDateFormat("mm:ss.").format(new java.util.Date (doLiTime))+
				(System.currentTimeMillis()%1000)/10;
		
		if(downLink[0].length()==8){
			downLink[0] =downLink[0].substring(0, 7)+"0"+downLink[0].charAt(7)+"]"+s[0];
		}else{
			downLink[0] +="]"+s[0];
		}
		
		for (int i = 1; i < s.length; i++) {
			if(i>=downLink.length)break;
			downLink[i] = "          "+s[i];
		}
		
		doLiPos = -10;
		if(doLiIsRec){
			PrintWriter writer = null; 
			try { 
				writer = new PrintWriter(new FileWriter(filePath+"_DOWNLINK.txt", true), true); 
				for (int i = 0; i < s.length; i++) {
					if(i>=downLink.length)break;
					writer.println(downLink[i]);
				}
			} catch (IOException ioe) { 
				ioe.printStackTrace(); 
			} finally { 
				if (writer != null){ 
					writer.flush(); 
					writer.close(); 
				} 
			}
		}
		userInterface.ExitThread.setDownLink(s);
	}

	private String tpSacc(long l){
		String str = "Last update:";
		
		l = (MainThread.currentTime-l)/10;
		str += l;
		
		if(l<100)
			return str + "0ms";
		
		return str.substring(0, str.length()-2)+"."+str.substring(str.length()-2)+"s";
	}
	
	private void saveToConsole(){
		Debug.println("* Communication-Data: "+commDat.getText(), Debug.COM);
		for (int i = 0; i < commands.length; i++) {
			if(commands[i] == null)continue;
			Debug.println(" "+commands[i], Debug.SUBCOM);
		}
	}
	
	private void saveToFile(){
		String logFilepath = "log/"+
				new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date (System.currentTimeMillis()))+
				"-COM.txt";
		
		PrintWriter writer = null; 
		try { 
			writer = new PrintWriter(new FileWriter(logFilepath)); 
			writer.println("Saved Communication-Data:");
			writer.println(commDat.getText());
			for (int i = 0; i < commands.length; i++) {
				if(commands[i] == null)continue;
				writer.println(commands[i]);
			}
		} catch (IOException ioe) { 
			ioe.printStackTrace(); 
		} finally { 
			if (writer != null){ 
				writer.flush(); 
				writer.close(); 
			} 
		}
		
		Debug.println("* Saved communication-file to: "+logFilepath, Debug.TEXT);
	}
	
	private void vanishComm(){
		commands = new String[arrayLenght];
		commScro.setDisabled(true);
		commDel.setDisabled(true);
		commSaveToConsol.setDisabled(true);
		commSaveToFile.setDisabled(true);
		
		commPos = -10;
	}
	
	private void doNotUse(){
		String[] s;
		switch ((int)(Math.random()*4)) {
		case 0:
			s = new String[]{
				"*GPS*P200_L333"	
			};
			break;
		case 1:
			s = new String[]{
				"*Local*ET31_Q22_Q33_Q34_E23_T21_R10",
				"*SubLocal*ETE_R45_E22"
			};
			break;
		case 2:
			s = new String[]{
				"#*Ello:31"
			};
			break;

		default:
			s = new String[]{
					"**GPS**Lat33.43.5422",
					"**GPS**Lon156.41.8434",
					"**GPS**hight322"
				};
			break;
		}
		setUpLink(s);
	}
}
