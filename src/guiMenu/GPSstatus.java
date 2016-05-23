package guiMenu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GPSstatus extends menu.AbstractMenu{

	private static BufferedImage[] sats;
	private static Font fon;
	
	private long lastUpdate;
	
	private BufferedImage timeLine;
	private BufferedImage timeLineSat;
	private BufferedImage satStatus;
	private BufferedImage virtualHorizon;
	
	private Color g1 = new Color(30,230,70);
	private Color g2 = new Color(10,190,50,100);
	
	public GPSstatus() {
		fon = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		sats = new BufferedImage[]{
			PicLoader.pic.getImage("res/win/sub/gps/s1.png"),
			PicLoader.pic.getImage("res/win/sub/gps/b2.png"),
			PicLoader.pic.getImage("res/win/sub/gps/b1.png"),
			PicLoader.pic.getImage("res/win/sub/gps/b0.png"),
			PicLoader.pic.getImage("res/win/sub/gps/b4.png")
		};
		timeLine = new BufferedImage(240,406,BufferedImage.TYPE_INT_ARGB);
		timeLineSat = new BufferedImage(40,406,BufferedImage.TYPE_INT_ARGB);
	}
	
	@Override
	protected void uppdateIntern() {
		updateTimeLine();
	}

	@Override
	protected void paintIntern(Graphics g) {
		g.drawImage(sats[0], 100, 150, null);
		g.drawImage(timeLine, 140, 150, null);
		g.drawImage(timeLineSat, 100, 150, null);
	}
	
	public static void paintASatelite(int x, int y, int num, byte c, Graphics g){
		if(num == 0){
			g.drawImage(sats[4], x, y, null);
			return;
		}
		if(sats != null){
			switch (c) {
			case 0:
				g.drawImage(sats[1], x, y, null);
				break;
			case 1:
				g.drawImage(sats[2], x, y, null);
				break;
			case 2:
				g.drawImage(sats[3], x, y, null);
				break;

			default:
				break;
			}
			g.setColor(Color.white);
			g.setFont(fon);
			g.drawString(""+num/10+""+num%10, x+14, y+18);
		}
	}
	
	private void updateTimeLine(){
		if(System.currentTimeMillis()-lastUpdate >= 2000){
			lastUpdate+=2000;
			if(System.currentTimeMillis()-lastUpdate >= 2000){
				lastUpdate = System.currentTimeMillis();
			}
			//Move and Flush
			BufferedImage tln = new BufferedImage(240,406,BufferedImage.TYPE_INT_ARGB);
			Graphics g = tln.getGraphics();
			g.drawImage(timeLine, -2, 0, null);
			int w = timeLine.getWidth()-2;
			
			process.GPSControle gps = process.ProcessControl.gps;
			process.GPSsatelite sat;
			
			Graphics gS = timeLineSat.getGraphics();
			
			for (int i = 0; i < gps.numOfSats; i++) {
				sat = gps.getSat(i);
				int y = i*29+28;
				if(sat.getNumber() != 0){
					g.setColor(g2);
					g.drawLine(w, y, w, y-sat.getSigStrenghtLast()/4);
					g.drawLine(w+1, y, w+1, y-sat.getSigStrenght()/4);
					g.setColor(g1);
					g.drawLine(w, y-sat.getSigStrenghtLast()/4, w+1, y-sat.getSigStrenght()/4);
				
				}
				
				paintASatelite(0, y-27, sat.getNumber(), sat.getState(), gS);
			}
			
			timeLine = null;
			timeLine = tln;
			
			dontUse();//TODO entfernen
		}
	}
	
	private void dontUse(){
		process.GPSControle gps = process.ProcessControl.gps;
		process.GPSsatelite sat;
		
		for (int i = 0; i < gps.numOfSats; i++) {
			sat = gps.getSat(i);
			if(sat.getNumber() == 0)continue;
			int q = sat.getSigStrenght()+(int)(Math.random()*40)-20;
			if(q>100)q=100;
			if(q<0)q=0;
			sat.setSigStr(q);
		}
	}

}
