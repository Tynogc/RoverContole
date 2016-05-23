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
	
	private Button[] quit;
	private DataFiled[] state;
	private DataFiled[] externState;
	
	public boolean overallAlarm = false;
	public boolean overallWarn = false;
	
	private static final int length = 4;
	
	private static final Color normalCol = new Color(70,70,240);
	private static final Color warnCol = new Color(250,220,40);
	private static final Color alarmCol = new Color(255,30,40);
	
	public static final int TYPE_SYSTEM = 0;
	public static final int TYPE_CONNECTION = 1;
	
	public static WarnMenu warn = null;
	
	private AlarmkButton alarmButton;
	
	public WarnMenu(){
		alarmButton = null;
		
		alarmB = new boolean[length];
		warnB = new boolean[length];
		quitB = new boolean[length];
		
		textB = new String[length];
		
		quit = new Button[length];
		state  = new DataFiled[length];
		externState  = new DataFiled[length];
		
		quit[0] = new Button(400,100,"res/win/gui/cli/Gsk") {
			private final int q = TYPE_SYSTEM;
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				quitB[q] = true;
				uppdateAlarmState();
			}
		};
		quit[1] = new Button(400,100,"res/win/gui/cli/Gsk") {
			private final int q = TYPE_CONNECTION;
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				quitB[q] = true;
				uppdateAlarmState();
			}
		};
		
		for (int i = 0; i < length; i++) {
			textB[i] = "";
			alarmB[i] = false;
			warnB[i] = false;
			quitB[i] = false;
			state[i] = new DataFiled(100,100+(i*40),200,20,normalCol) {
				@Override
				protected void uppdate() {
				}
				@Override
				protected void isClicked() {	
				}
			};
			add(state[i]);
			
			if(quit[i]==null){
				allOk = 2;
				continue;
			}
			quit[i].setyPos(100+(i*40));
			add(quit[i]);
		}
		warn = this;
	}
	
	@Override
	protected void uppdateIntern() {
		
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
			state[type].setText(text);
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
				state[type].setText(text);
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
}
