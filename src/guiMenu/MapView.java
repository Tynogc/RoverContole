package guiMenu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;

import process.ProcessControl;
import process.WayInformation;

import menu.Button;

public class MapView extends menu.AbstractMenu{
	
	private BufferedImage mapTopo;
	private BufferedImage mapStrat;
	
	private BufferedImage theMap;
	private BufferedImage compass;
	
	private BufferedImage[] points;
	private static final String dir = "res/win/nav/sym/1/";
	private BufferedImage[] ikas;
	
	private Button plus;
	private Button minus;
	private Button newElement;
	private Button addElement;
	private Button loadElement;
	
	public static Color progColor = new Color(50,250,50);
	public static Color corrColor = new Color(200,0,250);
	public static Color dirColor = new Color(20,0,200); 
	public static Color targetColor = new Color(200,150,0); 
	public static Color viaColor = new Color(200,200,10);
	public static Color disabledColor = new Color(185,120,90);
	public static Color darkGreen = new Color(0,151,20,150);
	
	private Font font;
	private Font fontS;
	private Font fontT;
	
	private static final int bufferSize = 420;
	private static final int bufferSizeCom = 200;
	private final int firstCircle = 200;
	private final int secondCircle = 150;
	private final int thirdCircle = 100;
	private final int compassCircle = 100;
	private int symboleSizeCorr;
	private final int symboleSizeCorrTip = 14;
	
	private final int viaArrowA = 14;
	private final int viaArrowB = 20;
	
	private static final int subCircleSize = 70;
	
	public MapView(){
		//debug.Debug.println("Preparing map...");
		font = new Font(Font.MONOSPACED, Font.PLAIN, 18);
		fontT = new Font(Font.MONOSPACED, Font.PLAIN, 15);
		fontS = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		
		points = new BufferedImage[7];
		points[0] = PicLoader.pic.getImage(dir+"Prograde.png");
		symboleSizeCorr = points[0].getHeight()/2;
		points[1] = PicLoader.pic.getImage(dir+"Retrograde.png");
		points[2] = PicLoader.pic.getImage(dir+"Corr.png");
		points[3] = PicLoader.pic.getImage(dir+"AntiCorr.png");
		points[4] = PicLoader.pic.getImage(dir+"Run.png");
		points[5] = PicLoader.pic.getImage(dir+"Revers.png");
		points[6] = PicLoader.pic.getImage(dir+"Tetta.png");
		
		ikas = new BufferedImage[10];
		ikas[0] = PicLoader.pic.getImage("res/win/nav/apy.png");
		ikas[1] = PicLoader.pic.getImage("res/win/nav/apx.png");
		
		theMap = new BufferedImage(bufferSize,bufferSize/2+50,BufferedImage.TYPE_INT_RGB);
		paintCircBack(false, theMap.getGraphics());
		compass = new BufferedImage(bufferSizeCom,bufferSizeCom,BufferedImage.TYPE_INT_ARGB);
		paintCompass(compass.getGraphics());
		
		Graphics g = theMap.getGraphics();
		
		g.setColor(Color.white);
		g.drawLine(bufferSize/2,0,bufferSize/2+10,10);
		g.drawLine(bufferSize/2,0,bufferSize/2-10,10);
		g.drawLine(bufferSize/2,1,bufferSize/2+10,11);
		g.drawLine(bufferSize/2,1,bufferSize/2-10,11);
	}
	
	public void generateMap(){
		
	}
	
	private void paintCompass(Graphics g){
		g.setColor(Color.white);
		int k = 1800;
		for (int i = -k; i < k; i++) {
			double d = Math.toRadians((double)i/10.0-90);
			g.drawRect(bufferSizeCom/2+(int)(Math.cos(d)*compassCircle),bufferSizeCom/2+(int)(Math.sin(d)*compassCircle),0,0);
		}
		g.setColor(Color.gray);
		for (int i = -k; i < k; i++) {
			if((i/30+1)%2==0)continue;
			double d = Math.toRadians((double)i/10.0-90);
			g.drawRect(bufferSizeCom/2+(int)(Math.cos(d)*60),bufferSizeCom/2+(int)(Math.sin(d)*60),0,0);
		}
	}
	
	private void paintArrow(Graphics g, int x, int y){
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.rotate(Math.toRadians(ProcessControl.way.roverOrientation()),x,y);
		g2d.drawImage(ikas[1], x-16,y-100,null);
		//g2d = (Graphics2D)g.create();
		//g2d.rotate(Math.toRadians(ProcessControl.way.roverOrientation()+45),x,y);
		//g2d.drawImage(ikas[0], x,y,null);
	}
	
	private void paintCircBack(boolean all, Graphics g){
		g.setColor(Color.white);
		int k = 1800;
		if(!all)k = subCircleSize*10;
		for (int i = -k; i < k; i++) {
			double d = Math.toRadians((double)i/10.0-90);
			g.drawRect(bufferSize/2+(int)(Math.cos(d)*firstCircle),bufferSize/2+(int)(Math.sin(d)*firstCircle),0,0);
			if((i/20+1)%2==0)
			g.drawRect(bufferSize/2+(int)(Math.cos(d)*secondCircle),bufferSize/2+(int)(Math.sin(d)*secondCircle),0,0);
			if((i/25+1)%2==0)
			g.drawRect(bufferSize/2+(int)(Math.cos(d)*thirdCircle),bufferSize/2+(int)(Math.sin(d)*thirdCircle),0,0);
		}
		g.setColor(Color.GRAY);
		double d = Math.toRadians((double)(subCircleSize-90));
		g.drawLine(bufferSize/2, bufferSize/2, bufferSize/2+(int)(Math.cos(d)*firstCircle), bufferSize/2+(int)(Math.sin(d)*firstCircle));
		d = Math.toRadians((double)(-subCircleSize-90));
		g.drawLine(bufferSize/2, bufferSize/2, bufferSize/2+(int)(Math.cos(d)*firstCircle), bufferSize/2+(int)(Math.sin(d)*firstCircle));
	}
	
	private void paintCircVari(int mapOrientation, boolean all, Graphics g, int x, int y, int radius){
		double prog = ProcessControl.way.prograde();
		double dir = ProcessControl.way.dir();
		double corr = ProcessControl.way.corr();
		double via = ProcessControl.way.via();
		double targ = ProcessControl.way.target();
		
		x-=symboleSizeCorr;
		y-=symboleSizeCorr;
		
		if(targ != WayInformation.ERR){
			picAt(all, mapOrientation+targ, points[6], radius, g, x, y);
			//picAt(all, mapOrientation+targ+180, points[1], radius, g, x, y);
		}
		picAt(all, mapOrientation+dir, points[4], radius, g, x, y);
		picAt(all, mapOrientation+dir+180, points[5], radius, g, x, y);
		if(prog != WayInformation.ERR){
			picAt(all, mapOrientation+prog, points[0], radius, g, x, y);
			picAt(all, mapOrientation+prog+180, points[1], radius, g, x, y);
		}
		if(corr != WayInformation.ERR){
			picAt(all, mapOrientation+corr, points[2], radius, g, x, y);
			picAt(all, mapOrientation+corr+180, points[3], radius, g, x, y);
		}
		
		if(all)radius -= viaArrowA;
		if(withinRange(mapOrientation+via, all) && via != WayInformation.ERR){
			x+=symboleSizeCorr;
			y+=symboleSizeCorr;
			g.setColor(viaColor);
			double d = Math.toRadians(mapOrientation+via);
			double mX = x+bufferSize/2+(int)(Math.cos(d)*(radius+viaArrowA));
			double mY = y+bufferSize/2+(int)(Math.sin(d)*(radius+viaArrowA));
			
			double dk = Math.toRadians(mapOrientation+via+45);
			double dj = Math.toRadians(mapOrientation+via-45);
			g.drawLine((int)mX,(int)mY,(int)(mX-Math.cos(dk)*viaArrowB),(int)(mY-Math.sin(dk)*viaArrowB));
			g.drawLine((int)mX,(int)mY,(int)(mX-Math.cos(dj)*viaArrowB),(int)(mY-Math.sin(dj)*viaArrowB));
		}
	}
	
	private void paintLines(int mapOrientation, boolean all, Graphics g, int x, int y){
		int prog = (int)ProcessControl.way.prograde()+mapOrientation;
		double dir = ProcessControl.way.dir()+mapOrientation;
		//int corr = (int)ProcessControl.way.corr()+mapOrientation;
		double dtw = ProcessControl.way.distanceToWay();
		double target = ProcessControl.way.target()+mapOrientation;
		
		if(ProcessControl.way.prograde() == WayInformation.ERR){
			dtw = 0;
			prog = 270;
		}
		
		double d = Math.toRadians(dir);
		double mX = Math.cos(d);
		double mY = Math.sin(d);
		g.setColor(dirColor);
		if(withinRange(dir, all)){
			g.drawLine(x+bufferSize/2+(int)(mX*thirdCircle), y+bufferSize/2+(int)(mY*thirdCircle),
					x+bufferSize/2+(int)(mX*(firstCircle-40)), y+bufferSize/2+(int)(mY*(firstCircle-40)));
		}
		
		if(withinRange(target, all) && target != WayInformation.ERR){
			d = Math.toRadians(target);
			mX = Math.cos(d);
			mY = Math.sin(d);
			g.setColor(targetColor);
			g.drawLine(x+bufferSize/2+(int)(mX*(thirdCircle+30)), y+bufferSize/2+(int)(mY*(thirdCircle+30)),
					x+bufferSize/2+(int)(mX*(firstCircle-20)), y+bufferSize/2+(int)(mY*(firstCircle-20)));
		}
		
		/*double e = Math.toRadians(corr);
		double nX = Math.cos(e);
		double nY = Math.sin(e);
		/*g.setColor(Color.white);
		for (int i = -40; i <= 40; i+=5) {
			if(i%15==0){
				g.drawLine(x+bufferSize/2+(int)(nX*i+mX*25), y+bufferSize/2+(int)(nY*i+mY*25),
						x+bufferSize/2+(int)(nX*i+mX*35), y+bufferSize/2+(int)(nY*i+mY*35));
				g.drawLine(x+bufferSize/2+(int)(nX*i+mX*-25), y+bufferSize/2+(int)(nY*i+mY*-25),
						x+bufferSize/2+(int)(nX*i+mX*-35), y+bufferSize/2+(int)(nY*i+mY*-35));
				continue;
			}
			//g.drawRect(x+bufferSize/2+(int)(nX*i), y+bufferSize/2+(int)(nY*i),0,0);
			g.drawRect(x+bufferSize/2+(int)(nX*i+mX*30), y+bufferSize/2+(int)(nY*i+mY*30),0,0);
			g.drawRect(x+bufferSize/2+(int)(nX*i+mX*-30), y+bufferSize/2+(int)(nY*i+mY*-30),0,0);
		}*/
		Graphics2D g2d = (Graphics2D)g.create();
		boolean bcttc = false;
		if(!ProcessControl.way.onPrograde()){
			bcttc = true;
			//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			//g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.rotate(Math.toRadians((int)prog+90),x+bufferSize/2,y+bufferSize/2);
		}
		g2d.drawImage(ikas[0], x+bufferSize/2-42,y+bufferSize/2-60,null);
		if(bcttc)
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setFont(fontT);
		g2d.setColor(Color.green);
		int t = (int)dtw;
		dtw = Math.sqrt(dtw*2.5)*2;
		dtw-=3;
		
		int q = (int)(ProcessControl.way.corr()-ProcessControl.way.prograde());
		int r = 1;
		if(q<0)q+=360;
		if(q>180){
			r=-1;
			q=360-q;
		}
		
		if(dtw<0)dtw=0;
		if(dtw>40)dtw = 40;
		g2d.drawLine(x+bufferSize/2+(int)(r*dtw),y+bufferSize/2-(thirdCircle-40),x+bufferSize/2+(int)(r*dtw),y+bufferSize/2+35);
		
		g2d.setColor(darkGreen);
		g2d.drawLine(x+bufferSize/2+(int)(r*dtw)-1,y+bufferSize/2-(thirdCircle-40),x+bufferSize/2+(int)(r*dtw)-1,y+bufferSize/2+35);
		g2d.drawLine(x+bufferSize/2+(int)(r*dtw)+1,y+bufferSize/2-(thirdCircle-40),x+bufferSize/2+(int)(r*dtw)+1,y+bufferSize/2+35);
		g2d.drawLine(x+bufferSize/2-1,y+bufferSize/2-(thirdCircle-40),x+bufferSize/2-1,y+bufferSize/2-thirdCircle);
		g2d.drawLine(x+bufferSize/2+1,y+bufferSize/2-(thirdCircle-40),x+bufferSize/2+1,y+bufferSize/2-thirdCircle);
		
		g2d.setColor(Color.black);
		g2d.fillRect(x+bufferSize/2-13+(int)(r*dtw), y+bufferSize/2-50, 26, 14);
		g2d.setColor(progColor);
		g2d.drawRect(x+bufferSize/2-13+(int)(r*dtw), y+bufferSize/2-50, 27, 14);
		g2d.drawString(eecDouble(t), x+bufferSize/2-13+(int)(r*dtw), y+bufferSize/2-39);
		//g.drawLine(x+bufferSize/2+(int)(nX*dtw+mX*(thirdCircle-40)), y+bufferSize/2+(int)(nY*dtw+mY*(thirdCircle-40)),
		//		x+bufferSize/2+(int)(nX*dtw+mX*-35), y+bufferSize/2+(int)(nY*dtw+mY*-35));
		g2d.drawLine(x+bufferSize/2,y+bufferSize/2-(thirdCircle-40),x+bufferSize/2,y+bufferSize/2-thirdCircle);
		//g.drawLine(x+bufferSize/2+(int)(mX*thirdCircle), y+bufferSize/2+(int)(mY*thirdCircle),
				//x+bufferSize/2+(int)(mX*(thirdCircle-40)), y+bufferSize/2+(int)(mY*(thirdCircle-40)));
		if(withinRange(prog, all)){
			for (int i = thirdCircle; i < firstCircle-40; i+=4) {
				g2d.drawRect(x+bufferSize/2, y+bufferSize/2-i,0,0);
			}
			
			g2d.setColor(darkGreen);
			for (int i = thirdCircle; i < firstCircle-40; i+=4) {
				g2d.drawRect(x+bufferSize/2-1, y+bufferSize/2-i-1,2,2);
			}
		}
	}
	
	private void paintNumbers(Graphics g, int x, int y){
		double rovOri = ProcessControl.way.roverOrientation();
		double prograde = ProcessControl.way.prograde();
		double correct = ProcessControl.way.corr();
		double direction = ProcessControl.way.dir();
		double via = ProcessControl.way.via();
		double target = ProcessControl.way.target();
		
		String s = eecDouble(rovOri)+"°";
		g.setFont(fontT);
		g.setColor(dirColor);
		g.drawString(s, x+bufferSize/2-13, y-20);
		
		s = eecDouble(prograde)+"°";
		if(prograde == WayInformation.ERR) s = "---°";
		g.setColor(progColor);
		g.drawString("PROG:"+s, x, y-20);
		
		int q = (int)(prograde-rovOri);
		char r = 'R';
		if(q<0)q+=360;
		if(q>180){
			r='L';
			q=360-q;
		}
		s = eecDouble(q)+r;
		if(prograde == WayInformation.ERR) s = "---";
		g.drawString("TURN:"+s, x, y-10);
		
		s = eecDouble(direction)+"°";
		g.setColor(dirColor);
		g.drawString("DIR: "+s, x+bufferSize-70, y-20);
		
		q = (int)(direction-rovOri);
		r = 'R';
		if(q<0)q+=360;
		if(q>180){
			r='L';
			q=360-q;
		}
		s = eecDouble(q)+r;
		g.drawString("TURN:"+s, x+bufferSize-70, y-10);
		
		s = eecDouble(via)+"°";
		g.setColor(viaColor);
		if(via == WayInformation.ERR) s ="---°";
		g.drawString("VIA: "+s, x+bufferSize-70, y+5);
		
		q = (int)(via-rovOri);
		r = 'R';
		if(q<0)q+=360;
		if(q>180){
			r='L';
			q=360-q;
		}
		s = eecDouble(q)+r;
		if(via == WayInformation.ERR) s ="---";
		g.drawString("TURN:"+s, x+bufferSize-70, y+15);
		
		s = eecDouble(correct)+"°";
		if(correct == WayInformation.ERR) s = "---°";
		g.setColor(corrColor);
		g.drawString("CORR:"+s, x, y+5);
		
		s = eecDouble(target)+"°";
		g.setColor(targetColor);
		if(target == WayInformation.ERR) s ="---°";
		g.drawString("DIR: "+s, x, y+15);
	}
	
	private String eecDouble(double i){
		return ""+(int)i/100+""+(int)(i/10)%10+""+(int)i%10;
	}
	
	private void paintCircOrient(int mapOrientation, boolean all, Graphics g, int x, int y){
		g.setFont(font);
		g.setColor(Color.white);
		asSpace(all, mapOrientation, "N", g, -4+x,5+y);
		asSpace(all, mapOrientation+90, "E", g,-4+x,5+y);
		asSpace(all, mapOrientation+180, "S", g,-4+x,5+y);
		asSpace(all, mapOrientation+270, "W", g,-4+x,5+y);
		
		g.setFont(fontS);
		asSpace(all, mapOrientation+45, "NE", g, -7+x,4+y);
		asSpace(all, mapOrientation+135, "SE", g,-7+x,4+y);
		asSpace(all, mapOrientation+225, "SW", g,-7+x,4+y);
		asSpace(all, mapOrientation+315, "NW", g,-7+x,4+y);
		
		g.setColor(Color.gray);
		asSpace(all, mapOrientation+22.5, "NNE", g, -10+x,4+y);
		asSpace(all, mapOrientation+67.5, "ENE", g, -10+x,4+y);
		asSpace(all, mapOrientation+157.5, "SSE", g,-10+x,4+y);
		asSpace(all, mapOrientation+112.5, "ESE", g,-10+x,4+y);
		asSpace(all, mapOrientation+202.5, "SSW", g,-10+x,4+y);
		asSpace(all, mapOrientation+247.5, "WSW", g,-10+x,4+y);
		asSpace(all, mapOrientation+292.5, "WNW", g,-10+x,4+y);
		asSpace(all, mapOrientation+337.5, "NNW", g,-10+x,4+y);
	}
	
	private void picAt(boolean all, double pos, BufferedImage i, int radius, Graphics g, int x, int y){
		pos = pos%360;
		if(!withinRange(pos, all))return;
		double q = Math.toRadians(pos);
		g.drawImage(i, bufferSize/2+(int)(Math.cos(q)*(radius+symboleSizeCorrTip))+x
				, bufferSize/2+(int)(Math.sin(q)*(radius+symboleSizeCorrTip))+y, null);
	}
	
	private void asSpace(boolean all, double pos, String s, Graphics g, int x, int y){
		pos = pos%360;
		if(!withinRange(pos, all))return;
		double q = Math.toRadians(pos);
		g.drawString(s, bufferSize/2+(int)(Math.cos(q)*(firstCircle-10))+x
				, bufferSize/2+(int)(Math.sin(q)*(firstCircle-10))+y);
	}
	
	private boolean withinRange(double w, boolean all){
		if(all)return true;
		if(w<0)w+=360;
		w = w%360;
		if(w > 270+subCircleSize||w<270-subCircleSize)return false;
		return true;
	}

	@Override
	protected void uppdateIntern() {
		
	}

	@Override
	protected void paintIntern(Graphics g) {
		int x = -20;
		int y = 200;
		
		paintMapOnMainpage(g);
		
		g.drawImage(compass, x+(bufferSize-bufferSizeCom)/2, y+(bufferSize-bufferSizeCom)/2, null);
		paintCircVari(270, true, g, x, y, compassCircle);
		paintArrow(g, x+bufferSize/2, y+bufferSize/2);
	}
	
	public void paintMapOnMainpage(Graphics g){
		final int x = 300;
		final int y = 110;
		g.drawImage(theMap, x, y, null);
		paintCircOrient((int)(-ProcessControl.way.roverOrientation()+630)%360, false, g, x, y);
		paintCircVari((int)(-ProcessControl.way.roverOrientation()+630)%360, false, g, x, y, firstCircle);
		paintLines((int)(-ProcessControl.way.roverOrientation()+630)%360, false, g, x, y);
		paintNumbers(g,x,y);
	}

}
