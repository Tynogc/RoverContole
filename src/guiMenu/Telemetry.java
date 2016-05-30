package guiMenu;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import menu.AbstractMenu;

public class Telemetry extends AbstractMenu{
	
	private MapView mapView;
	private BufferedImage[] motorData;
	
	
	public Telemetry(MapView m) {
		mapView = m;
	}
	
	private void updateMotor(int amper){
		
	}

	@Override
	protected void uppdateIntern() {
		
	}

	@Override
	protected void paintIntern(Graphics g) {
		
	}

}
