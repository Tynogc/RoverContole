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
	
	private Color greenB = new Color(0,250,0,70);
	private Color yellowB = new Color(200,200,0,70);
	private Color redB = new Color(250,0,0,70);
	
	public Telemetry(MapView m) {
		mapView = m;
		imas = new BufferedImage[10];
		imas[0] = PicLoader.pic.getImage("res/win/oasis/m1.png");
		imas[1] = PicLoader.pic.getImage("res/win/oasis/m1b.png");
		imas[2] = PicLoader.pic.getImage("res/win/oasis/m2.png");
		
		fonts = new BufferedImage[]{
				PicLoader.pic.getImage("res/win/sub/font0.png"),
				PicLoader.pic.getImage("res/win/sub/font1.png"),
				PicLoader.pic.getImage("res/win/sub/font2.png"),
				PicLoader.pic.getImage("res/win/sub/font3.png"),
				PicLoader.pic.getImage("res/win/sub/font4.png")
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
		
		Button b1 = new Button(800,100,"res/win/gui/cli/B") {
			@Override
			protected void uppdate() {}
			
			@Override
			protected void isFocused() {}
			
			@Override
			protected void isClicked() {
				userInterface.GuiControle.setSecondMenu(userInterface.GuiControle.electric);
			}
		};
		b1.setTextColor(Button.gray);
		b1.setText("Electric");
		add(b1);
	}
	
	private void updateThrottle(int th, int pos){
		if(check[pos]==th)return;
		check[pos] = th;
		
		Graphics g = motorData[pos].getGraphics();
		g.drawImage(imas[2], 0, 0, null);
		
		double d = (double)th/100;
		d*=180;
		
		g.setColor(greenB);
		g.drawString(""+d, 10,10);
		double r;
		for (int i = 2; i < 180; i++) {
			if(i>d)break;
			r = Math.toRadians(i+135);
			g.drawRect((int)(Math.cos(r)*44)+45, (int)(Math.sin(r)*44)+45, 1, 1);
		}
	}
	
	private void updateMotor(int amper, int pos){
		if(check[pos]==amper)return;
		check[pos] = amper;
		Graphics g = motorData[pos].getGraphics();
		if(amper < -10){
			g.drawImage(imas[0], 0, 0, null);
			return;
		}
		
		g.drawImage(imas[0], 0, 0, null);
		double d = (double)amper/(MotorInformation.MAX_AMPS*10);
		d *= 225.0;
		if(d>225)d=225;
		d+=135;
		Color c = Color.green;
		Color c2 = greenB;
		int col = 1;
		if(amper>150){
			c = Color.yellow;
			c2 = yellowB;
			col = 2;
		}
		if(amper>200){
			c = Color.red;
			c2 = redB;
			col = 3;
		}
		g.setColor(c);
		d = Math.toRadians(d);
		int d1 = (int)(Math.cos(d)*45);
		int d2 = (int)(Math.sin(d)*45);
		g.drawLine(45,45,d1+45,d2+45);
		
		g.setColor(c2);
		g.drawLine(44,45,d1+44,d2+45);
		g.drawLine(45,46,d1+45,d2+46);
		g.drawLine(45,44,d1+45,d2+44);
		g.drawLine(46,45,d1+46,d2+45);
		/*g.setFont(Button.boldFont14);
		String s = "";
		if(amper<100)s = " ";
		s+= ""+amper/10;
		s+="."+amper%10;
		g.drawString(s+"A", 45, 35);*/
		paintTelemetryInts(amper, 2, col, 70, 70, g,1);
	}
	
	private void paintThVari(int x, int y, int ist, int soll, Graphics g){
		g.setColor(MapView.dirColor);
		double d = (double)ist/100;
		d*=180;
		double dx = Math.cos(Math.toRadians(d+135));
		d = Math.sin(Math.toRadians(d+135));
		g.drawLine((int)(dx*48)+45+x,(int)(d*48)+44+y,(int)(dx*40)+45+x,(int)(d*40)+44+y);
		g.drawLine((int)(dx*48)+44+x,(int)(d*48)+44+y,(int)(dx*40)+44+x,(int)(d*40)+44+y);
		g.drawLine((int)(dx*48)+44+x,(int)(d*48)+45+y,(int)(dx*40)+44+x,(int)(d*40)+45+y);
		g.drawLine((int)(dx*48)+45+x,(int)(d*48)+45+y,(int)(dx*40)+45+x,(int)(d*40)+45+y);
	}

	@Override
	protected void uppdateIntern() {
		updateThrottle((int)((System.currentTimeMillis()/500)%100), 1);
		updateMotor((int)((System.currentTimeMillis()/500)%250), 2);
	}

	@Override
	protected void paintIntern(Graphics g) {
		mapView.paintMapOnMainpage(g);
		g.drawImage(motorData[1], 10, 100, null);
		g.drawImage(motorData[2], 10, 200, null);
		
		paintThVari(10, 100, (int)((System.currentTimeMillis()/500+10)%100), (int)((System.currentTimeMillis()/500)%100+20), g);
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
