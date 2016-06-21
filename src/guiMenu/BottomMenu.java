package guiMenu;

import java.awt.Color;
import java.awt.Graphics;

import comunication.ComunicationControl;

import debug.Debug;
import userInterface.GuiControle;
import userInterface.MainFrame;
import menu.Button;
import menu.DataFiled;

public class BottomMenu extends menu.AbstractMenu{
	
	private Button run;
	private Button stop;
	
	private Button com;
	
	private Button map;
	
	private Button tele;
	
	private Button prog;
	
	private Button reset;
	
	private Button jog;
	
	private Button settings;
	
	private DataFiled looker;
	
	private int fKeyMarker;
	private long fKeyMarkerT;
	
	public BottomMenu() {
		
		run = new Button(380,MainFrame.sizeY-120,"res/win/gui/spb/RUN") {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isFocused() {
			}
			@Override
			protected void isClicked() {
				B_runStop();
			}
		};
		add(run);
		run.setSubtext("START Rover Operation");
		stop = new Button(380,MainFrame.sizeY-120,"res/win/gui/spb/STOP") {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isFocused() {
			}
			@Override
			protected void isClicked() {
				B_runStop();
			}
		};
		add(stop);
		stop.setVisible(false);
		stop.setSubtext("STOP Rover Operation");
		
		com = new Button(20,MainFrame.sizeY-120,"res/win/gui/spb/COMM") {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isFocused() {
			}
			@Override
			protected void isClicked() {
				B_CommMenu();
			}
		};
		add(com);
		com.setSubtext("Communication Menu");
		
		map = new Button(140,MainFrame.sizeY-120,"res/win/gui/spb/MAP") {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isFocused() {
			}
			@Override
			protected void isClicked() {
				B_Map();
			}
		};
		add(map);
		map.setSubtext("Map View");
		
		tele = new Button(260,MainFrame.sizeY-120,"res/win/gui/spb/TEL") {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isFocused() {
			}
			@Override
			protected void isClicked() {
				B_Telem();
			}
		};
		add(tele);
		tele.setSubtext("Telemetry");
		
		prog = new Button(500,MainFrame.sizeY-120,"res/win/gui/spb/COMM") {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isFocused() {
			}
			@Override
			protected void isClicked() {
				B_ProgMenu();
			}
		};
		add(prog);
		prog.setSubtext("Programming Menu");
		
		jog = new Button(620,MainFrame.sizeY-120,"res/win/gui/spb/COMM") {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isFocused() {
			}
			@Override
			protected void isClicked() {
				B_Jog();
			}
		};
		add(jog);
		jog.setSubtext("Jog Menu");
		
		settings = new Button(860,MainFrame.sizeY-120,"res/win/gui/spb/settings") {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isFocused() {
			}
			@Override
			protected void isClicked() {
				B_Settings();
			}
		};
		add(settings);
		settings.setSubtext("Settings");
		
		reset = new Button(740,MainFrame.sizeY-120,"res/win/gui/spb/RESET") {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isFocused() {
			}
			@Override
			protected void isClicked() {
				B_Reset();
			}
		};
		add(reset);
		reset.setSubtext("SSK Reset");
		
		looker = new DataFiled(2,MainFrame.sizeY-160,990,20,Color.blue) {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isClicked() {
				GuiControle.setSecondMenu(GuiControle.warnMenu);
			}
		};
		add(looker);
	}
	
	public DataFiled getLooker(){return looker;}

	@Override
	protected void uppdateIntern() {
		
	}

	@Override
	protected void paintIntern(Graphics g) {
		g.setFont(GuiControle.fontNormal);
		g.setColor(Color.white);
		g.drawString("F8", 900, MainFrame.sizeY-5);
		g.drawString("F7", 780, MainFrame.sizeY-5);
		g.drawString("F6", 660, MainFrame.sizeY-5);
		g.drawString("F5", 540, MainFrame.sizeY-5);
		g.drawString("F4", 420, MainFrame.sizeY-5);
		g.drawString("F3", 300, MainFrame.sizeY-5);
		g.drawString("F2", 180, MainFrame.sizeY-5);
		g.drawString("F1", 60, MainFrame.sizeY-5);
		
		if(System.currentTimeMillis()-fKeyMarkerT <600){
			g.setColor(new Color(70,70,255,250-(int)(System.currentTimeMillis()-fKeyMarkerT)/3));
			g.drawRect(19+(120*(fKeyMarker-1)), MainFrame.sizeY-121, 102, 102);
			g.drawRect(18+(120*(fKeyMarker-1)), MainFrame.sizeY-122, 104, 104);
		}
	}
	
	public void fKeyPressed(int key){
		fKeyMarker = key;
		fKeyMarkerT = System.currentTimeMillis();
		switch (key) {
		case 5:
			B_ProgMenu();
			break;
		case 2:
			B_Map();
			break;
		case 3:
			B_Telem();
			break;
		case 1:
			B_CommMenu();
			break;
		case 4:
			B_runStop();
			break;
		case 8:
			B_Settings();
			break;
		case 7:
			B_Reset();
			break;
		case 6:
			B_Jog();
			break;

		default:
			break;
		}
		Debug.println("* F-Key Pressed: F"+key, Debug.COM);
	}
	
	private void B_runStop(){
		if(run.isVisible()){
			run.setVisible(false);
			stop.setVisible(true);
			ComunicationControl.com.send(">RUN");
		}else{
			ComunicationControl.com.send("*STOP");
			run.setVisible(true);
			stop.setVisible(false);
		}
	}
	private void B_CommMenu(){
		GuiControle.setSecondMenu(GuiControle.comMenu);
	}
	private void B_ProgMenu(){
		GuiControle.setSecondMenu(GuiControle.codeMenu);
	}
	private void B_Map(){
		GuiControle.setSecondMenu(GuiControle.mapMenu);
	}
	private void B_Telem(){
		GuiControle.setSecondMenu(GuiControle.telemetry);
	}
	private void B_Settings(){
		GuiControle.setSecondMenu(GuiControle.settingsMenu);
	}
	private void B_Reset(){
		ComunicationControl.com.send("*SSK_RES");
	}
	
	private void B_Jog(){
		
	}



}
