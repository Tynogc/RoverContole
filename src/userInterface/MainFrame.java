package userInterface;

import guiMenu.PicLoader;
import guiMenu.SettingsMenu;
import guiMenu.WarnMenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import debug.DebugMenu;

import process.ProcessControl;

public class MainFrame extends Panel{

	private JFrame frame;
	
	public static final int sizeX = 1600;
	public static final int sizeY = 900;
	
	public static int frameX = 720;
	public static int frameY = 480;
	
	private int offX = 0;
	private int offY = 0;
	private boolean nonFullFrame;
	
	private BufferedImage buffer;
	
	private MouseListener mouse;
	private KeySystem key;
	
	private GuiControle gui;
	
	private MainThread thread;
	
	private comunication.ComunicationControl comunication;
	private process.ProcessControl process;
	private DebugMenu dbm;
	
	public static boolean hideDebugFrame = true;
	public static boolean standartStartUp = true;
	public static boolean fullScreen = true;
	public static byte playIntro = 0; //0: Play, 1: Play Simple ELSE: dont play
	
	private boolean startUpBoolean;
	
	private static GraphicsDevice device = 
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	
	public MainFrame(){
		super();
		startUpBoolean = true;
		
		debug.DebugFrame f = new debug.DebugFrame();
		
		debug.Debug.println("*(C) Sven T. Schneider");
		debug.Debug.bootMsg("Starting BOOT", 0);
		//StartUp
		StartUp startUp = new StartUp(f);
		startUp.doStartUp();
		
		Dimension dim = getToolkit().getScreenSize();
		int per = 60;
		int qPer = 15;
		frameX = dim.width-qPer;
		frameY = dim.height-per;
		if(frameX > sizeX)frameX = sizeX;
		if(frameY > sizeY)frameY = sizeY;
		
		frame = new JFrame();
		frame.setBounds(10,10, frameX+10, frameY);
		
		setBounds(0,0,frameX, frameY);
		frame.add(this);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		nonFullFrame = frameX<sizeX||frameY<sizeY;
		setFrameSize(frameX, frameY);
		
		mouse = new MouseListener();
		addMouseMotionListener(mouse);
		addMouseListener(mouse);
		addMouseWheelListener(mouse);
		key = new KeySystem();
		addKeyListener(key);
		
		new PicLoader();
		dbm = new DebugMenu();
		
		if(standartStartUp){
			debug.Debug.bootMsg("Starting Thread", 0);
			thread = new MainThread(this);
			thread.start();
			
			frame.setVisible(true);
			debug.Debug.bootMsg("Frame Open", 0);
			debug.Debug.panel.setVisible(!hideDebugFrame);
			if(fullScreen){
				device.setFullScreenWindow(frame);
			}
		}
		
		GuiControle.warnMenu = new guiMenu.WarnMenu();
		
		comunication = new comunication.ComunicationControl();
		process = new ProcessControl();
		
		gui = new GuiControle(mouse, key, dbm);
		GuiControle.settingsMenu = new SettingsMenu(this);
		
		comunication.setComMenue(gui.comMenu);
		
		//new process.SoundPlayer();
		
		startUpBoolean = false;
		debug.Debug.bootMsg("Window open - Boot DONE", 0);
		
		if(!standartStartUp){
			debug.Debug.bootMsg("Starting Thread", 0);
			thread = new MainThread(this);
			thread.start();
			
			frame.setVisible(true);
			debug.Debug.bootMsg("Frame Open", 0);
			if(fullScreen){
				device.setFullScreenWindow(frame);
			}
		}
		
		debug.Debug.panel.setVisible(false);
	}
	
	public void loop(int fps, int secFps, int thiFps, int triFps){
		BufferedImage b = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
		Graphics g = b.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0,0, sizeX, sizeY);
		
		
		if(!startUpBoolean){
			comunication.process();
			process.loop();
			gui.uppdate();
			gui.paint(g);
		}else{
			dbm.paintYou(g);
		}
		
		if(nonFullFrame){
			int diffX = sizeX-frameX;
			int diffY = sizeY-frameY;
			
			double oX = (double)mouse.x/frameX;
			double oY = (double)mouse.y/frameY;
			offX = (int)(oX*diffX);
			offY = (int)(oY*diffY);
			if(offX+frameX>sizeX)offX = sizeX-frameX;
			if(offY+frameY>sizeY)offY = sizeY-frameY;
			if(offX<0)offX = 0;
			if(offY<0)offY = 0;
		}
		if(!startUpBoolean){
			gui.mouseXAdd = offX;
			gui.mouseYAdd = offY;
		}
		
		g.setColor(Color.green);
		g.setFont(menu.Button.plainFont);
		g.drawString("FPS: "+fps, 1200, 14);
		g.drawString("T: ["+thiFps+"] ["+triFps+"]", 1200, 30);
		if(secFps<0)g.setColor(Color.red);
		secFps = -secFps+50;
		double fpsDpe = (double)secFps/50.0;
		secFps = (int)(fpsDpe*100.0);
		g.drawString("Load:"+secFps/100+""+(secFps/10)%10+""+secFps%10+"%", 1250, 14);
		
		buffer = b;
		paint(getGraphics());
	}
	
	public void paint(Graphics g){
		if(buffer != null && g != null){
			g.drawImage(buffer.getSubimage(offX, offY, frameX, frameY), 0, 0, null);
		}
	}
	
	public void setFrameSize(int x, int y){
		nonFullFrame = frameX<sizeX||frameY<sizeY;
		offX = 0;
		offY = 0;
		frameX = x;
		frameY = y;
		frame.setSize(x+10,y);
		setSize(x,y);
	}
	
	public static void main(String[] a){
		new MainFrame();
	}
}
