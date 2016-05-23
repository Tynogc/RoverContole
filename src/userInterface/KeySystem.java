package userInterface;

import java.awt.event.KeyEvent;

public class KeySystem implements java.awt.event.KeyListener{
	
	private String keychain = "";
	private boolean enter = false;
	private guiMenu.BottomMenu fMenus;
	
	public void setBottomMenu(guiMenu.BottomMenu m){
		fMenus = m;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode()>=112){
			if(fMenus != null){
				if(arg0.getKeyCode()<121){
					fMenus.fKeyPressed(arg0.getKeyCode()-111);
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		if(arg0.getKeyChar() == '\n'){
			enter = true;
			return;
		}
		if((int)arg0.getKeyChar() == 8){
			if(keychain.length()>=1)
				keychain = keychain.substring(0, keychain.length()-1);
		}else{
			keychain += arg0.getKeyChar();
		}
	}
	
	public String getKeyChain(){
		String s = "";
		return s+keychain;
	}
	
	public boolean isEnter(){
		return enter;
	}
	
	public void deletInput(){
		keychain = "";
		enter = false;
	}

}
