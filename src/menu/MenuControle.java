package menu;

import java.awt.Graphics;

public class MenuControle {
	
	private AbstractMenu menus;
	
	private boolean lef;
	
	public MenuControle(){
		
	}
	
	public void paintYou(Graphics g){
		if(menus != null){
			menus.paintYou(g);
		}
	}
	
	public void setActivMenu(AbstractMenu m){
		menus = m;
		if(menus != null)
		menus.setControle(this);
	}
	
	public boolean isActiv(){
		return menus != null;
	}
	
	public void scrolled(int i){
		if(menus!= null)menus.scrolled(i);
	}
	
	public void mouseState(int x, int y, boolean left, boolean right){
		boolean lPress = !lef && left;
		boolean lReleas = lef && !left;
		lef = left;
		if(menus != null){
			menus.updateMenu();
			menus.getActivButtons().checkMouse(x, y);
			if(lPress)menus.getActivButtons().leftClicked(x, y);
			if(lReleas)menus.getActivButtons().leftReleased(x, y);
		}
	}

}
