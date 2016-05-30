package guiMenu;

import java.awt.Color;
import java.awt.Graphics;

import process.SoundPlayer;
import menu.AlarmkButton;
import menu.Button;
import menu.DataFiled;

public class WarnMenu extends menu.AbstractMenu{

	private boolean[] alarmB;
	private boolean[] warnB;
	private boolean[] quitB;
	private String[] textB;
	
	//private Button[] quit;
	private AlarmDataField[] state;
	private DataFiled[] externState;
	
	public boolean overallAlarm = false;
	public boolean overallWarn = false;
	public boolean isThereAlarm = false;
	public boolean isThereWarn = false;
	private int lastWarn;
	private int lastAlarm;
	
	private static final int length = 4;
	
	private static final Color normalCol = new Color(70,70,240);
	private static final Color warnCol = new Color(250,220,40);
	private static final Color warnColB = new Color(150,150,0);
	private static final Color alarmCol = new Color(255,30,40);
	
	public static final int TYPE_SYSTEM = 0;
	public static final int TYPE_CONNECTION = 1;
	public static final int TYPE_SYSTEM_THREADS = 2;
	
	public static WarnMenu warn = null;
	
	private AlarmkButton alarmButton;
	
	private boolean blinkState = true;
	
	private DataFiled looker;
	
	public WarnMenu(){
		alarmButton = null;
		
		alarmB = new boolean[length];
		warnB = new boolean[length];
		quitB = new boolean[length];
		
		textB = new String[length];
		
		//quit = new Button[length];
		state  = new AlarmDataField[length];
		externState  = new DataFiled[length];
		
		state[0] = new AlarmDataField(100, 100, 100, 40, normalCol, TYPE_SYSTEM);
		state[1] = new AlarmDataField(100, 160, 100, 40, normalCol, TYPE_CONNECTION);
		state[2] = new AlarmDataField(100, 220, 100, 40, normalCol, TYPE_SYSTEM_THREADS);
		
		for (int i = 0; i < length; i++) {
			textB[i] = "";
			alarmB[i] = false;
			warnB[i] = false;
			quitB[i] = false;
			
			if(state[i]==null){
				allOk = 2;
				state[i] = new AlarmDataField(100,100+(i*40),200,40,normalCol, 0);
				continue;
			}
			state[i].setTextColor(Color.white);
			add(state[i]);
			//quit[i].setyPos(100+(i*40));
			//add(quit[i]);
		}
		warn = this;
		
		Button quitAll = new Button(1000,100,"res/win/gui/cli/G") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				for (int i = 0; i < length; i++) {
					quitB(i);
				}
			}
		};
		quitAll.setText("Quit All");
		add(quitAll);
	}
	
	@Override
	protected void uppdateIntern() {
		boolean b = System.currentTimeMillis()/500%2==0;
		if(b != blinkState){
			blinkState = b;
			boolean bace = false;
			for (int i = 0; i < length; i++) {
				if(quitB[i])continue;
				bace = warnB[i];
				if(alarmB[i]){
					if(blinkState){
						state[i].setColor(Color.black);
						state[i].setTextColor(alarmCol);
					}else{
						state[i].setTextColor(Color.black);
						state[i].setColor(alarmCol);
					}
				}else if(bace){
					if(!blinkState){
						state[i].setColor(warnCol);
					}else{
						state[i].setColor(warnColB);
					}
				}
			}
		}
	}
	
	private boolean blinkStateLooker = false;
	private byte lookerState = 0;
	private int lookerText = -1;
	private boolean needUpdate = false;
	public void uppdateLooker(){
		byte n = 0;
		if(overallAlarm)n = 1;
		else if(isThereAlarm)n = 2;
		else if(overallWarn)n = 3;
		else if(isThereWarn)n = 4;
		else n = 5;
		
		if(n == 1 || n == 3){
			boolean b = System.currentTimeMillis()/500%2==0;
			if(b != blinkStateLooker || n != lookerState || needUpdate){
				blinkStateLooker = b;
				if(n == 1){
					if(blinkStateLooker){
						looker.setColor(Color.black);
						looker.setTextColor(alarmCol);
					}else{
						looker.setTextColor(Color.black);
						looker.setColor(alarmCol);
					}
					inputLookerText(lastAlarm, true, true);
				}
				if(n == 3){
					if(blinkStateLooker){
						looker.setColor(warnCol);
						looker.setTextColor(Color.black);
					}else{
						looker.setTextColor(Color.black);
						looker.setColor(warnColB);
					}
					inputLookerText(lastWarn, false, true);
				}
			}
			lookerState = n;
		}
		
		if(lookerState != n){
			if(n == 2){
				looker.setTextColor(Color.black);
				looker.setColor(alarmCol);
			}
			if(n == 4){
				looker.setTextColor(Color.black);
				looker.setColor(warnCol);
			}
			if(n == 5){
				looker.setTextColor(Color.white);
				looker.setColor(normalCol);
				looker.setText("ALL OK");
			}
		}
		if(needUpdate){
			if(n == 2){
				inputLookerText(lastAlarm, true, false);
			}
			if(n == 4){
				inputLookerText(lastWarn, false, false);
			}
		}
		needUpdate = false;
		lookerState = n;
	}
	
	private void inputLookerText(int i, boolean al, boolean qi){
		if(lookerText == i && !needUpdate)return;
		if(i < 0 || i >= length){
			debug.Debug.println("* Error WarnMenu01z: Type dosn't match Button! Type="+i,debug.Debug.ERROR);
			return;
		}
		if(qi){
			if(quitB[i]){
				i = searchAlternated(i);
				if(al) lastAlarm = i;
				else lastWarn = i;
			}
		}else if(al){
			if(!alarmB[i]){
				i = searchAlternated(i);
				lastAlarm = i;
			}
		}else{
			if(!warnB[i]){
				i = searchAlternated(i);
				lastWarn = i;
			}
		}
		
		lookerText = i;
		looker.setText(getTypeName(i)+": "+textB[i]);
	}
	
	private int searchAlternated(int k){
		for (int i = 0; i < length; i++) {
			if(alarmB[i] && !quitB[i])return i;
		}
		for (int i = 0; i < length; i++) {
			if(alarmB[i])return i;
		}
		for (int i = 0; i < length; i++) {
			if(warnB[i] && !quitB[i])return i;
		}
		for (int i = 0; i < length; i++) {
			if(warnB[i])return i;
		}
		return k;
	}

	@Override
	protected void paintIntern(Graphics g) {
		
	}
	
	public void setAlarm(boolean a, int type, String text){
		if(type < 0 || type >= length){
			debug.Debug.println("* Error WarnMenu01a: Type dosn't match Button! Type="+type,debug.Debug.ERROR);
			return;
		}
		if(textB[type].compareTo(text) != 0){
			if(externState[type]!=null)externState[type].setText(text);
			setTexts(type, text);
			textB[type] = text;
			if(a)
				lastAlarm = type;
			needUpdate = true;
		}
		
		if(alarmB[type] != a){
			if(!alarmB[type]){
				SoundPlayer.playSound(SoundPlayer.SOUND_ALARM);
				warnB[type]=false;
			}
			alarmB[type] = a;
			quitB[type] = false;
			
			uppdateAlButton(alarmB[type], warnB[type], externState[type]);
			uppdateAlButton(alarmB[type], warnB[type], state[type]);
			
			uppdateAlarmState();
			needUpdate = true;
		}
	}
	
	public void setWarn(boolean a, int type, String text){
		if(type < 0 || type >= length){
			debug.Debug.println("* Error WarnMenu01b: Type dosn't match Button! Type="+type,debug.Debug.ERROR);
			return;
		}
		
		if(!alarmB[type]){
			if(textB[type].compareTo(text) != 0){
				if(externState[type]!=null)externState[type].setText(text);
				setTexts(type, text);
				textB[type] = text;
				if(a)
					lastWarn = type;
				needUpdate = true;
			}
		
			if(warnB[type] != a){
				warnB[type] = a;
				quitB[type] = false;
			
				uppdateAlButton(alarmB[type], warnB[type], externState[type]);
				uppdateAlButton(alarmB[type], warnB[type], state[type]);
			
				uppdateAlarmState();
				needUpdate = true;
			}
		}
	}
	
	private void uppdateAlarmState(){
		isThereAlarm = false;
		isThereWarn = false;
		overallAlarm = false;
		overallWarn = false;
		for (int i = 0; i < length; i++) {
			if(alarmB[i])isThereAlarm = true;
			if(warnB[i])isThereWarn = true;
			if(quitB[i])continue;
			if(alarmB[i])overallAlarm = true;
			if(warnB[i])overallWarn = true;
		}
		if(alarmButton == null)return;
		alarmButton.setAlarm(overallAlarm);
		alarmButton.setWarn(overallWarn);
	}
	
	private void uppdateAlButton(boolean al, boolean wa, DataFiled d){
		if(d == null)return;
		
		if(al){
			d.setColor(alarmCol);
			d.setTextColor(Color.BLACK);
		}else if(wa){
			d.setColor(warnCol);
			d.setTextColor(Color.BLACK);
		}else{
			d.setColor(normalCol);
			d.setTextColor(Color.WHITE);
		}
	}
	
	public void setExtern(DataFiled data, int type){
		if(type < 0 || type >= length){
			debug.Debug.println("* Error WarnMenu01a: Type dosn't match Button! Type="+type,debug.Debug.ERROR);
			return;
		}
		if(externState[type]!=null)debug.Debug.println("* Owerwriting Warn-Field", debug.Debug.WARN);
		externState[type] = data;
		data.setText(textB[type]);
		uppdateAlButton(alarmB[type], warnB[type], data);
	}
	
	public void setAlb(menu.AlarmkButton a){
		alarmButton = a;
		alarmButton.setAlarm(overallAlarm);
		alarmButton.setWarn(overallWarn);
	}
	
	private void setTexts(int type, String text){
		String[] s = text.split(" ");
		if(s.length == 1){
			state[type].setText(text);
			state[type].setSecondLine(null);
			return;
		}
		int i = s.length/2;
		String text2 = "";
		text = "";
		int qr = 0;
		if(s.length%2 == 1)qr = 1;
		for (int j = 0; j < i; j++) {
			text+=s[j]+" ";
			text2+=s[i+j+qr]+" ";
		}
		if(qr == 1){
			text+=s[i];
		}
		state[type].setText(text);
		state[type].setSecondLine(text2);
	}
	
	public void quitB(int type){
		if(type < 0 || type >= length){
			debug.Debug.println("* Error WarnMenu01a: Type dosn't match Button! Type="+type,debug.Debug.ERROR);
			return;
		}
		if(alarmB[type]){
			state[type].setTextColor(Color.black);
			state[type].setColor(alarmCol);
		}else if(warnB[type]){
			state[type].setColor(warnCol);
		}
		quitB[type] = true;
		needUpdate = true;
		uppdateAlarmState();
	}
	
	public void setLooker(DataFiled l){
		looker = l;
		if(!overallAlarm && !overallWarn){
			looker.setColor(normalCol);
			looker.setText("ALL OK");
			return;
		}
		for (int i = 0; i < length; i++) {
			if(warnB[i]){
				looker.setColor(warnCol);
				looker.setText(textB[i]);
			}
			if(alarmB[i]){
				looker.setColor(alarmCol);
				looker.setText(textB[i]);
				return;
			}
		}
	}
	
	public static String getTypeName(int a){
		switch (a) {
		case TYPE_SYSTEM:
			return "System";
		case TYPE_CONNECTION:
			return "Connection";
		case TYPE_SYSTEM_THREADS:
			return "Sys Threads";

		default:
			return "???";
		}
	}
}

class AlarmDataField extends DataFiled{
	private int atsa;
	
	public AlarmDataField(int x, int y, int wi, int hi, Color c, int ats) {
		super(x, y, wi, hi, c);
		atsa = ats;
	}
	
	@Override
	protected void uppdate() {}
	@Override
	protected void isFocused() {}

	@Override
	protected void isClicked() {
		WarnMenu.warn.quitB(atsa);
	}
	
	public void paintYou(Graphics g){
		super.paintYou(g);
		g.setColor(Color.gray);
		g.setFont(plainFont);
		g.drawString(WarnMenu.getTypeName(atsa), xPos+1, yPos-3);
	}
	
}
