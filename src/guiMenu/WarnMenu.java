package guiMenu;

import java.awt.Color;
import java.awt.Graphics;

import menu.AlarmkButton;
import menu.Button;
import menu.DataFiled;

public class WarnMenu extends menu.AbstractMenu{

	private boolean[] alarmB;
	private boolean[] warnB;
	private boolean[] quitB;
	private String[] textB;
	
	//private Button[] quit;
	private DataFiled[] state;
	private DataFiled[] externState;
	
	public boolean overallAlarm = false;
	public boolean overallWarn = false;
	
	private static final int length = 4;
	
	private static final Color normalCol = new Color(70,70,240);
	private static final Color warnCol = new Color(250,220,40);
	private static final Color warnColB = new Color(150,150,0);
	private static final Color alarmCol = new Color(255,30,40);
	
	public static final int TYPE_SYSTEM = 0;
	public static final int TYPE_CONNECTION = 1;
	
	public static WarnMenu warn = null;
	
	private AlarmkButton alarmButton;
	
	private boolean blinkState = true;
	
	public WarnMenu(){
		alarmButton = null;
		
		alarmB = new boolean[length];
		warnB = new boolean[length];
		quitB = new boolean[length];
		
		textB = new String[length];
		
		//quit = new Button[length];
		state  = new DataFiled[length];
		externState  = new DataFiled[length];
		
		state[0] = new AlarmDataField(100, 100, 100, 40, normalCol, TYPE_SYSTEM);
		state[1] = new AlarmDataField(100, 160, 100, 40, normalCol, TYPE_CONNECTION);
		
		for (int i = 0; i < length; i++) {
			textB[i] = "";
			alarmB[i] = false;
			warnB[i] = false;
			quitB[i] = false;
			
			if(state[i]==null){
				allOk = 2;
				state[i] = new DataFiled(100,100+(i*40),200,40,normalCol) {
					@Override
					protected void uppdate() {
					}
					@Override
					protected void isClicked() {	
					}
				};
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
		}
		
		if(alarmB[type] != a){
			alarmB[type] = a;
			quitB[type] = false;
			
			uppdateAlButton(alarmB[type], warnB[type], externState[type]);
			uppdateAlButton(alarmB[type], warnB[type], state[type]);
			
			uppdateAlarmState();
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
			}
		}
		
		if(warnB[type] != a){
			warnB[type] = a;
			quitB[type] = false;
			
			uppdateAlButton(alarmB[type], warnB[type], externState[type]);
			uppdateAlButton(alarmB[type], warnB[type], state[type]);
			
			uppdateAlarmState();
		}
	}
	
	private void uppdateAlarmState(){
		overallAlarm = false;
		overallWarn = false;
		for (int i = 0; i < length; i++) {
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
		uppdateAlarmState();
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
	
}
