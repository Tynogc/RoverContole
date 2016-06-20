package userInterface;

import guiMenu.PerformanceMenu;
import guiMenu.PicLoader;
import guiMenu.SettingsMenu;
import guiMenu.WarnMenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.util.Properties;

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
	
	private StartAnimation startAnim;
	
	private boolean startUpBoolean;
	
	private BufferedImage[] buffers;
	private int bufferInUse;
	
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
		
		frame = new JFrame("Seypris");
		if(fullScreen){
			frame.setUndecorated(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			per = 0;
			qPer = 0;
			
		}
		String os = System.getProperty("os.name");
		if(os != null){
			if(os.contains("nux")){
			debug.Debug.println("OS: Linux");
			per = 50;
			}
		}
		frameX = dim.width-qPer;
		frameY = dim.height-per;
		if(frameX > sizeX)frameX = sizeX;
		if(frameY > sizeY)frameY = sizeY;
		
		frame.setBounds(10,10, frameX+10, frameY+70);
		
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
		
		if(playIntro == 0){
			startAnim = new StartAnimation(true);
		}else if(playIntro == 1){
			startAnim = new StartAnimation(false);
		}
		
		buffers = new BufferedImage[]{
				new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB),
				new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB)
		};
		bufferInUse = 0;
		
		if(standartStartUp){
			debug.Debug.bootMsg("Starting Thread", 0);
			thread = new MainThread(this);
			thread.start();
			
			setVisible(true);
			frame.setVisible(true);
			debug.Debug.bootMsg("Frame Open", 0);
			debug.Debug.panel.setVisible(!hideDebugFrame);
		}
		
		GuiControle.warnMenu = new guiMenu.WarnMenu();
		
		comunication = new comunication.ComunicationControl();
		process = new ProcessControl();
		
		gui = new GuiControle(mouse, key, dbm);
		GuiControle.settingsMenu = new SettingsMenu(this);
		
		comunication.setComMenue(gui.comMenu);
		
		new process.SoundPlayer();
		
		startUpBoolean = false;
		debug.Debug.bootMsg("Window open - Boot DONE", 0);
		
		if(!standartStartUp){
			debug.Debug.bootMsg("Starting Thread", 0);
			thread = new MainThread(this);
			thread.start();
			
			setVisible(true);
			frame.setVisible(true);
			debug.Debug.bootMsg("Frame Open", 0);
		}
		
		debug.Debug.panel.setVisible(false);
		
		WarnMenu.warn.setWarn(true, WarnMenu.TYPE_SYSTEM, "Warns at Boot!");
	}
	
	public void loop(int fps, int secFps, int thiFps, int triFps){
		PerformanceMenu.startTime();
		
		Graphics g = buffers[bufferInUse].getGraphics();
		g.setColor(Color.black);
		g.fillRect(0,0, sizeX, sizeY);
		PerformanceMenu.markTime(PerformanceMenu.CreateBuffer);
		
		
		if(!startUpBoolean){
			comunication.process();
			PerformanceMenu.markTime(PerformanceMenu.Communication);
			process.loop();
			PerformanceMenu.markTime(PerformanceMenu.Process);
			gui.uppdate();
			PerformanceMenu.markTime(PerformanceMenu.UpdateGui);
			gui.paint(g);
			PerformanceMenu.markTime(PerformanceMenu.PaintGui);
		}else{
			dbm.paintYou(g);
		}
		
		if(startAnim != null){
			boolean bcl = startAnim.uppdate();
			startAnim.paint(g, (frameX-370)/2, (frameY-300)/2);
			if(bcl && !startUpBoolean){
				startAnim = null;
			}
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
		g.drawString("FPS: "+fps, 1300, 14);
		g.drawString("T: ["+thiFps+"] ["+triFps+"]", 1300, 30);
		if(secFps<0)g.setColor(Color.red);
		secFps = -secFps+50;
		double fpsDpe = (double)secFps/50.0;
		secFps = (int)(fpsDpe*100.0);
		g.drawString("Load:"+secFps/100+""+(secFps/10)%10+""+secFps%10+"%", 1350, 14);
		
		buffer = buffers[bufferInUse];
		bufferInUse++;
		if(bufferInUse>= buffers.length)bufferInUse = 0;
		paint(getGraphics());
		PerformanceMenu.markTime(PerformanceMenu.PaintBack);
	}
	
	public void saveLoop(){
		debug.Debug.panel.setVisible(true);
		
		Graphics g = buffers[bufferInUse].getGraphics();
		g.setColor(Color.black);
		g.fillRect(0,0, sizeX, sizeY);
		
		gui.paint(g);
		
		g.setColor(Color.red);
		g.setFont(menu.Button.plainFont);
		g.drawString("ERROR MODE", 1300, 14);
		buffer = buffers[bufferInUse];
		bufferInUse++;
		if(bufferInUse>= buffers.length)bufferInUse = 0;
		paint(getGraphics());
		PerformanceMenu.markTime(PerformanceMenu.PaintBack);
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
		frame.setSize(x+10,y+70);
		setSize(x,y);
	}
	
	public static void main(String[] a){
		new MainFrame();
	}
}
