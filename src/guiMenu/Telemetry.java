package guiMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import menu.AbstractMenu;
import menu.Button;
import process.MotorInformation;

public class Telemetry extends AbstractMenu{
	
	private MapView mapView;
	private BufferedImage[] motorData;
	private int[] check;
	
	private BufferedImage[] imas;
	private static BufferedImage[] fonts;
	
	public Telemetry(MapView m) {
		mapView = m;
		imas = new BufferedImage[10];
		imas[0] = PicLoader.pic.getImage("res/win/oasis/m1.png");
		imas[1] = PicLoader.pic.getImage("res/win/oasis/m1b.png");
		
		fonts = new BufferedImage[]{
				PicLoader.pic.getImage("res/win/sub/font0.png"),
				PicLoader.pic.getImage("res/win/sub/font1.png"),
				PicLoader.pic.getImage("res/win/sub/font2.png")
		};
		
		motorData = new BufferedImage[]{
				new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB),
				new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB),
				new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB),
				new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB),
				new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB),
				new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB)
		};
		
		check = new int[6];
	}
	
	private void updateMotor(int amper, int pos){
		if(check[pos]==amper)return;
		check[pos] = amper;
		Graphics g = motorData[pos].getGraphics();
		g.drawImage(imas[0], 0, 0, null);
		double d = (double)amper/(MotorInformation.MAX_AMPS*10);
		d *= 225.0;
		if(d>225)d=225;
		d+=135;
		Color c = Color.green;
		int col = 1;
		if(amper>150){
			c = Color.yellow;
			col = 2;
		}
		if(amper>200){
			c = Color.red;
			col = 3;
		}
		g.setColor(c);
		d = Math.toRadians(d);
		g.drawLine(40,40,(int)(Math.cos(d)*45)+45,(int)(Math.sin(d)*45)+45);
		/*g.setFont(Button.boldFont14);
		String s = "";
		if(amper<100)s = " ";
		s+= ""+amper/10;
		s+="."+amper%10;
		g.drawString(s+"A", 45, 35);*/
		paintTelemetryInts(amper, 2, col, 70, 70, g,1);
	}

	@Override
	protected void uppdateIntern() {
		updateMotor((int)((System.currentTimeMillis()/500)%250), 2);
	}

	@Override
	protected void paintIntern(Graphics g) {
		mapView.paintMapOnMainpage(g);
		g.drawImage(motorData[2], 10, 100, null);
	}
	
	public static void paintTelemetryInts(int i,int fontsize, int color, int x, int y, Graphics g, int komata){
		if(fonts == null)return;
		if(color>4)color = 4;
		if(fontsize<0)fontsize = 0;
		if(fontsize>fonts.length)fontsize = fonts.length;
		int p = 1;
		for (int j = 0; j < komata; j++) {
			p *= 10;
		}
		int w = i/p;
		int qt = 1;
		int j = 1;
		boolean zer= true;
		while (w >= qt) {
			zer = false;
			paintFont((w/qt)%10, fontsize, color, x-(getFontSize(fontsize)*j), y, g);
			j++;
			qt*=10;
		}
		if(zer)
			paintFont(0, fontsize, color, x-(getFontSize(fontsize)), y, g);
		
		paintFont(10, fontsize, color, x, y, g);
		fontsize--;
		if(fontsize<0)fontsize = 0;
		for (j = 0; j < komata; j++) {
			p/=10;
			paintFont((i/p)%10, fontsize, color, x+(getFontSize(fontsize)*j), y, g);
		}
		
	}
	
	public static void paintTelemetryInts(int i,int fontsize, int color, int x, int y, Graphics g){
		paintTelemetryInts(i, fontsize, color, x, y, g, 0);
	}
	
	private static void paintFont(int i, int f, int color, int x, int y, Graphics g){
		int xc = getFontSize(f);
		int yc = fonts[f].getHeight()/5;
		i = i%11;
		color = color%5;
		
		g.drawImage(fonts[f].getSubimage(xc*i, yc*color, xc, yc), x-xc, y-yc, null);
	}
	
	private static int getFontSize(int f){
		return fonts[f].getWidth()/11;
	}

}
