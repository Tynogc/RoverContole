package menu;

import java.awt.Graphics;

public abstract class AbstractMenu {

	private ButtonInterface buttons;
	
	private MenuControle controle;
	
	protected byte allOk = 0;
	
	public AbstractMenu(){
		buttons = new EndButtonList();
	}
	
	public void paintYou(Graphics g){
		paintIntern(g);
		buttons.paintYou(g);
		
	}
	
	public void setControle(MenuControle c){
		controle = c;
	}
	
	public void add(Button b){
		buttons = buttons.add(b);
	}
	
	public void remove(Button b){
		buttons = buttons.remove(b);
	}
	
	public void closeYou(){
		if(controle != null){
			controle.setActivMenu(null);
		}
	}
	
	public void changeActivMenu(AbstractMenu m){
		if(controle != null){
			controle.setActivMenu(m);
		}
	}
	
	public ButtonInterface getActivButtons(){
		return buttons;
	}
	
	public void updateMenu(){
		uppdateIntern();
	}
	
	public void scrolled(int i){
		
	}
	
	protected abstract void uppdateIntern();
	
	protected abstract void paintIntern(Graphics g);
	
	public byte getStatus(){
		return allOk;
	}
}
