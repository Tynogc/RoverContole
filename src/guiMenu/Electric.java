package guiMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import menu.AbstractMenu;

public class Electric extends AbstractMenu{

	private BufferedImage[] imas;
	
	private BufferedImage[] acc;
	
	private static final int MAX_VOLTS = 420;//TODO richtig zuweisen
	private static final int VOLTS_WARN = 340;//TODO richtig zuweisen
	private static final int MIN_VOLTS = 310;//TODO richtig zuweisen
	private static final int DISABLED = 0;
	
	private Color cB;
	private Color cR;
	
	public Electric() {
		imas = new BufferedImage[10];
		imas[0] = PicLoader.pic.getImage("res/win/sub/ele/PG.png");
		imas[1] = PicLoader.pic.getImage("res/win/sub/ele/P1.png");
		imas[2] = PicLoader.pic.getImage("res/win/sub/ele/P2.png");
		imas[3] = PicLoader.pic.getImage("res/win/sub/ele/P.png");
		
		acc = new BufferedImage[8];
		for (int i = 0; i < acc.length; i++) {
			acc[i]=new BufferedImage(261, 34, BufferedImage.TYPE_INT_ARGB);
		}
		
		if(imas[1].getWidth()<11 || imas[2].getWidth()<11||imas[1].getHeight()<11 || imas[2].getHeight()<11){
			cB = Color.white;
			cR = Color.white;
		}else{
			cB = new Color(imas[1].getRGB(3, 11));
			cR = new Color(imas[2].getRGB(3, 11));
		}
		
	}
	
	private void paintChargeBar(int x, int y, int volt, Graphics g, int mul){
		g.drawImage(imas[0], x, y, null);
		if(volt <= DISABLED)return;
		int col = 0;
		if(volt/mul<=VOLTS_WARN)col = 3;
		Telemetry.paintTelemetryInts(volt, 4, col, x+241, y+28, g,2);
		double p = (double)((volt/mul)-MIN_VOLTS)/(MAX_VOLTS-MIN_VOLTS)*10;
		col++;
		if(col == 4)col = 2;
		for (int i = 0; i < 10; i++) {
			if(i>=p)break;
			g.drawImage(imas[col], x+164-(i*18), y+1, null);
		}
	}
	
	private void paintChargeBarSmall(int x, int y, int volt, Graphics g){
		g.drawImage(imas[3], x, y, null);
		if(volt <= DISABLED)return;
		int col = 0;
		if(volt<=VOLTS_WARN)col = 3;
		Telemetry.paintTelemetryInts(volt, 2, col, x+210, y+22, g,2);
		double p = (double)(volt-MIN_VOLTS)/(MAX_VOLTS-MIN_VOLTS);
		Color c = cB;
		if(col == 3)c = cR;
		g.setColor(c);
		volt = (int)(p*130);
		if(volt>130)volt = 130;
		if(volt<0)volt = 0;
		g.drawRect(47+x+(130-volt), 14+y, volt, 2);
	}
	
	@Override
	protected void uppdateIntern() {
		
	}

	@Override
	protected void paintIntern(Graphics g) {
		paintChargeBar(40, 100, 1800-((int)(System.currentTimeMillis()/30)%900), g, 4);
		paintChargeBar(40, 160, 440-((int)(System.currentTimeMillis()/100)%200), g, 1);
		paintChargeBarSmall(40, 200, 440-((int)(System.currentTimeMillis()/100)%200), g);
	}
	
	

}
