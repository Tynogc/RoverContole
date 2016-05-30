package guiMenu;

import java.awt.Color;
import java.awt.Graphics;

import debug.Debug;
import userInterface.GuiControle;
import userInterface.MainFrame;
import menu.Button;
import menu.DataFiled;

public class BottomMenu extends menu.AbstractMenu{
	
	private Button run;
	private Button stop;
	
	private Button com;
	
	private Button prog;
	
	private Button settings;
	
	private DataFiled looker;
	
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
		
		com = new Button(260,MainFrame.sizeY-120,"res/win/gui/spb/COMMENU") {
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
		
		prog = new Button(20,MainFrame.sizeY-120,"res/win/gui/spb/COMMENU") {
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
		prog.setSubtext("Pr0gramming Menu");
		
		settings = new Button(740,MainFrame.sizeY-120,"res/win/gui/spb/settings") {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isFocused() {
			}
			@Override
			protected void isClicked() {
				GuiControle.setSecondMenu(GuiControle.settingsMenu);
			}
		};
		add(settings);
		settings.setSubtext("Settings");
		
		looker = new DataFiled(2,MainFrame.sizeY-160,600,20,Color.blue) {
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
		g.drawString("F4", 420, MainFrame.sizeY-5);
		g.drawString("F3", 300, MainFrame.sizeY-5);
		g.drawString("F2", 180, MainFrame.sizeY-5);
		g.drawString("F1", 60, MainFrame.sizeY-5);
	}
	
	public void fKeyPressed(int key){
		switch (key) {
		case 1:
			B_ProgMenu();
			break;
		case 3:
			B_CommMenu();
			break;
		case 4:
			B_runStop();
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
		}else{
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

}
