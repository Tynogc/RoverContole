package guiMenu;

import java.awt.Color;
import java.awt.Graphics;

import userInterface.GuiControle;
import userInterface.MainFrame;
import menu.AbstractMenu;
import menu.Button;

public class ShutdownMenu extends AbstractMenu{
	
	private Button closehook;
	private boolean stillActiv;
	private Button shut;
	private Button cancel;
	private Button advLog;
	
	private final int x;
	private final int y;
	
	private long timeToShutdown;
	private int tts;
	
	public ShutdownMenu(Button b) {
		closehook = b;
		stillActiv = true;
		
		x = MainFrame.sizeX/2;
		y = MainFrame.sizeY/2;
		
		shut = new Button(x-55,y-16,"res/win/gui/cli/001") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				System.exit(0);
			}
		};
		cancel = new Button(x-55,y+20,"res/win/gui/cli/Gsk") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				stillActiv = false;
			}
		};
		
		advLog = new Button(x+25,y+20,"res/win/gui/cli/Gsk") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				//TODO AdvancedLog schreiben
			}
		};
		advLog.setText("AdvLog");
		cancel.setText("Cancel");
		add(advLog);
		add(cancel);
		add(shut);
		
		timeToShutdown  =System.currentTimeMillis()+30000;
	}
	
	@Override
	protected void uppdateIntern() {
		if(!closehook.wasLastClicked()){
			stillActiv = false;
		}
		tts = (int)(timeToShutdown-System.currentTimeMillis());
		if(timeToShutdown<System.currentTimeMillis()){
			System.exit(0);
		}
	}

	@Override
	protected void paintIntern(Graphics g) {
		g.setColor(Color.white);
		g.setFont(Button.boldFont14);
		g.drawString("Shutdown in "+tts/1000+" s", x, y+7);
	}
	
	public boolean stillActiv(){
		return stillActiv;
	}

}
