package guiMenu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import menu.AbstractMenu;
import menu.DataFiled;
import menu.ScrollBar;

public class SettingsMenu extends AbstractMenu{
	
	private userInterface.MainFrame frame;
	
	public static SettingsMenu set;
	
	public static Font font;
	
	private LogViewer startUpLog;
	private LogViewer errLog;
	private LogViewer normalLog;
	
	public SettingsMenu(userInterface.MainFrame f){
		frame = f;
		
		try
        {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/FreeMono.ttf"));
            font = font.deriveFont(14f);
        }
        catch(Exception e)
        {
    		font = new Font(Font.MONOSPACED, Font.BOLD, 14);
            e.printStackTrace();
        }
		
		startUpLog = new LogViewer(30, 100, 100, 30);
		errLog = new LogViewer(30, 250, 150, 50);
		normalLog = new LogViewer(30, 450, 200, 100);
		add(startUpLog.scroll);
		add(errLog.scroll);
		add(normalLog.scroll);
		add(startUpLog.data);
		add(errLog.data);
		add(normalLog.data);
		normalLog.setData("Rover Log");
		errLog.data.setColor(Color.orange);
		errLog.data.setTextColor(Color.black);
		
		set = this;
	}
	
	public void clear(int a){
		if(a == 2){
			errLog.clear();
		}else if(a == 1){
			startUpLog.clear();
		}else{
			normalLog.clear();
		}
	}
	
	public void addText(String s, int a){
		if(a == 2){
			errLog.addText(s);
		}else if(a == 1){
			startUpLog.addText(s);
		}else{
			normalLog.addText(s);
			if(s.length()>2){
				debug.Debug.println("* ROVER: "+s.substring(1), debug.Debug.REMOTE);
				debug.Debug.print("[",debug.Debug.REMOTE);
				debug.Debug.print(LogViewer.getStr(s.charAt(0)), LogViewer.getInt(s.charAt(0)));
				debug.Debug.print("]",debug.Debug.REMOTE);
			}
		}
	}
	
	public void addSettings(String s, int a){
		if(a == 2){
			errLog.setData("FAULT-Log "+s);
		}else if(a == 1){
			startUpLog.setData("StartUp-Log "+s);
		}else{
			normalLog.setData("Rover-Log "+s);
		}
	}

	@Override
	protected void uppdateIntern() {
		
	}

	@Override
	protected void paintIntern(Graphics g) {
		errLog.paintYou(g);
		startUpLog.paintYou(g);
		normalLog.paintYou(g);
	}

}

class LogViewer{
	
	public ScrollBar scroll;
	
	public final int size;
	public static final int textSize = 16;
	public final int lines;
	
	public int xPos;
	public int yPos;
	
	public BufferedImage ima;
	public DataFiled data;
	
	public LogViewer(int x, int y, int s, int l){
		lines = l;
		size = s;
		ima = new BufferedImage(500, lines*textSize, BufferedImage.TYPE_INT_RGB);
		
		xPos = x;
		yPos = y;
		
		data = new DataFiled(x,y-20,520,20,Color.blue) {
			
			@Override
			protected void uppdate() {
			}
			
			@Override
			protected void isClicked() {
			}
		};
		data.setTextColor(Color.white);
		scroll = new ScrollBar(x+500, y, size, size/textSize, lines-(size/textSize));
	}
	
	public void setData(String s){
		data.setText(s);
	}
	
	public void addText(String s){
		BufferedImage i = new BufferedImage(500, lines*textSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)i.getGraphics();
		g.drawImage(ima, 0, textSize, null);
		ima = i;
		if(s.length()<2)return;
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g.setFont(SettingsMenu.font);
		g.setColor(Color.white);
		g.drawString("[", 2, 14);
		g.setColor(getCol(s.charAt(0)));
		g.drawString(getStr(s.charAt(0)), 10, 14);
		g.setColor(Color.white);
		if(s.length()>3){
			if(s.charAt(1)=='#'){
				g.drawString("] ", 50, 14);
				g.setColor(Color.red);
				g.drawString(s.substring(1), 60, 14);
			}else{
				g.drawString("] "+s.substring(1), 50, 14);
			}
		}else{
			g.drawString("] "+s.substring(1), 50, 14);
		}
	}
	
	public void paintYou(Graphics g){
		int i = scroll.getScrolled()*textSize;
		if(i>ima.getHeight()-size-1)i = ima.getHeight()-size-1;
		try {
			g.drawImage(ima.getSubimage(0, i, 499, size), xPos, yPos, null);
		} catch (Exception e) {
			debug.Debug.println("* Settings.Scrolling:"+e.toString(), debug.Debug.ERROR);
		}
		g.setColor(Color.gray);
		g.drawRect(xPos, yPos, 500, size);
	}
	
	public void clear(){
		ima = new BufferedImage(400, lines*textSize, BufferedImage.TYPE_INT_RGB);
	}
	
	public static Color getCol(char c){
		switch (c) {
		case 'W':
			return Color.yellow;
		case 'A':
			return Color.red;
		case 'I':
			return new Color(50,50,255);
		case 'C':
			return Color.green;

		default:
			return new Color(250,100,0);
		}
	}
	public static int getInt(char c){
		switch (c) {
		case 'W':
			return debug.Debug.WARN;
		case 'A':
			return debug.Debug.ERROR;
		case 'I':
			return debug.Debug.TEXT;
		case 'C':
			return debug.Debug.MASSAGE;

		default:
			return debug.Debug.SUBWARN;
		}
	}
	public static String getStr(char c){
		switch (c) {
		case 'W':
			return "WARN ";
		case 'A':
			return "ERROR";
		case 'C':
			return " OK  ";
		case 'I':
			return "INFO ";

		default:
			return "*****";
		}
	}
}
