package userInterface;

import java.awt.Font;
import java.awt.Graphics;

import coding.CodeMenu;
import menu.AbstractMenu;
import menu.MenuControle;
import guiMenu.*;

public class GuiControle {
	
	private MouseListener mouse;
	
	private menu.MenuControle firstM;
	private static menu.MenuControle secondM;
	private static menu.MenuControle sideMenu;
	private menu.MenuControle bottMenu;
	private menu.MenuControle logMenu;
	
	public static Font fontNormal = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
	public static Font fontTitel = new Font(Font.SANS_SERIF, Font.BOLD, 14);
	
	public int mouseX;
	public int mouseY;
	public int mouseXAdd;
	public int mouseYAdd;
	
	public static coding.CodeMenu codeMenu;
	public static guiMenu.SettingsMenu settingsMenu;
	public static guiMenu.ComunicationMenu comMenu;
	public static guiMenu.GPSstatus gpsStat;
	public static guiMenu.WarnMenu warnMenu;
	public static guiMenu.MapView mapMenu;
	
	private static guiMenu.SideMenu sideM;
	private static guiMenu.PerformanceMenu sideM2;
	
	public GuiControle(MouseListener m, KeySystem key, debug.DebugMenu dbm){
		debug.Debug.println("Starting to make GUI-Areas...");
		
		firstM = new MenuControle();
		secondM = new MenuControle();
		sideMenu = new MenuControle();
		bottMenu = new MenuControle();
		logMenu = new MenuControle();
		debug.Debug.bootMsg("GUI-Areas Done!", 0);
		mouse  = m;
		debug.Debug.println("Starting to make GUI-Menus...");
		TopLevelMenu tlm = new TopLevelMenu();
		firstM.setActivMenu(tlm);
		debug.Debug.bootMsg("TLM ", tlm.getStatus());
		
		logMenu.setActivMenu(dbm);
		debug.Debug.bootMsg("Debug", dbm.getStatus());
		
		BottomMenu bo  =new BottomMenu();
		bottMenu.setActivMenu(bo);
		debug.Debug.bootMsg("Bottom", bo.getStatus());
		
		sideM = new SideMenu();
		sideMenu.setActivMenu(sideM);
		debug.Debug.bootMsg("SideMenu", sideM.getStatus());
		
		sideM2 = new PerformanceMenu();
		//sideMenu.setActivMenu(sideM);
		debug.Debug.bootMsg("Perdormance", sideM2.getStatus());
		
		if(warnMenu == null)
		warnMenu = new WarnMenu();
		warnMenu.setAlb(tlm.getAlb());
		debug.Debug.bootMsg("Owerview", warnMenu.getStatus());
		
		gpsStat = new guiMenu.GPSstatus();
		debug.Debug.bootMsg("GPS", gpsStat.getStatus());
		
		codeMenu = new CodeMenu(key);
		debug.Debug.bootMsg("Coding", codeMenu.getStatus());
		
		comMenu = new guiMenu.ComunicationMenu(key);
		debug.Debug.bootMsg("ComMenu", comMenu.getStatus());
		
		mapMenu = new guiMenu.MapView();
		debug.Debug.bootMsg("MapMenu", mapMenu.getStatus());
		
		key.setBottomMenu(bo);
		setSecondMenu(comMenu);
		
		debug.Debug.bootMsg("GUI-Frames DONE! ", 0);
	}
	
	public static void setSecondMenu(AbstractMenu m){
		if(secondM != null)
		secondM.setActivMenu(m);
	}
	
	public void uppdate(){
		firstM.mouseState(mouse.x+mouseXAdd, mouse.y+mouseYAdd, mouse.left || mouse.leftClicked, mouse.right);
		secondM.mouseState(mouse.x+mouseXAdd, mouse.y+mouseYAdd, mouse.left || mouse.leftClicked, mouse.right);
		sideMenu.mouseState(mouse.x+mouseXAdd, mouse.y+mouseYAdd, mouse.left || mouse.leftClicked, mouse.right);
		bottMenu.mouseState(mouse.x+mouseXAdd, mouse.y+mouseYAdd, mouse.left || mouse.leftClicked, mouse.right);
		logMenu.mouseState(mouse.x+mouseXAdd, mouse.y+mouseYAdd, mouse.left || mouse.leftClicked, mouse.right);
		
		firstM.scrolled(mouse.rot);
		secondM.scrolled(mouse.rot);
		sideMenu.scrolled(mouse.rot);
		bottMenu.scrolled(mouse.rot);
		logMenu.scrolled(mouse.rot);
		mouse.rot = 0;
		
		mouse.leftClicked = false;
		mouseX = mouse.x;
		mouseY = mouse.y;
	}
	
	public void paint(Graphics g){
		secondM.paintYou(g);
		firstM.paintYou(g);
		sideMenu.paintYou(g);
		bottMenu.paintYou(g);
		logMenu.paintYou(g);
	}
	
	public static void sideMenuNormal(boolean b){
		if(sideMenu == null)return;
		if(b){
			sideMenu.setActivMenu(sideM);
		}else{
			sideMenu.setActivMenu(sideM2);
		}
	}

}
