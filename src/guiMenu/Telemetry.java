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
	
	public Telemetry(MapView m) {
		mapView = m;
		imas = new BufferedImage[10];
		imas[0] = PicLoader.pic.getImage("res/win/oasis/m1.png");
		imas[1] = PicLoader.pic.getImage("res/win/oasis/m1b.png");
		
		motorData = new BufferedImage[]{
				new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB),
				new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB),
				new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB),
				new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB),
				new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB),
				new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB)
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
		Color c = Color.green;
		if(amper>150)c = Color.yellow;
		if(amper>200)c = Color.red;
		g.setColor(c);
		d = Math.toRadians(d);
		g.drawLine(40,40,(int)(Math.cos(d)*40)+40,(int)(Math.sin(d)*40)+40);
		g.setFont(Button.boldFont14);
		String s = "";
		if(amper<100)s = " ";
		s+= ""+amper/10;
		s+="."+amper%10;
		g.drawString(s+"A", 45, 35);
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

}
