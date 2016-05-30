package menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class OsziDiagramm extends Button{
	
	protected BufferedImage[] buffers;
	protected int bufferInUse;
	
	protected BufferedImage backpanel;
	protected int lastY;
	
	private static final Color g1 = new Color(30,230,70);
	private static final Color g2 = new Color(10,190,50,100);
	private static final Color r1 = new Color(230,30,20);
	private static final Color r2 = new Color(190,30,30,100);

	public OsziDiagramm(int x, int y, BufferedImage back) {
		super(x, y, back.getWidth(), back.getHeight());
		buffers = new BufferedImage[]{
				new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB),
				new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB)
		};
		backpanel = back;
		lastY = ySize;
	}
	
	/**
	 * @param x : should be between 0.0 and 1.0
	 */
	public void logData(double x){
		if(x<0)x = 0;
		boolean red = false;
		if(x>1.0){
			red = true;
			x = 1.0;
		}
		int goToY = ySize-(int)(x*getySize()-2);
		
		bufferInUse++;
		if(bufferInUse>=buffers.length)bufferInUse = 0;
		
		int theOtherBuffer = bufferInUse+1;
		if(theOtherBuffer>=buffers.length)theOtherBuffer = 0;
		
		buffers[bufferInUse] = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffers[bufferInUse].getGraphics();
		g.drawImage(buffers[theOtherBuffer], -2, 0, null);
		
		if(red)g.setColor(r2);
		else g.setColor(g2);
		g.drawLine(xSize-2, lastY+1, xSize-2, ySize-1);
		g.drawLine(xSize-1, goToY+1, xSize-1, ySize-1);
		if(red)g.setColor(r1);
		else g.setColor(g1);
		g.drawLine(xSize-2, lastY+1, xSize-1, goToY+1);
		
		lastY = goToY;
	}
	
	@Override
	public void paintYou(Graphics g) {
		if(next != null)next.paintYou(g);
		g.setColor(Color.gray);
		g.setFont(plainFont);
		g.drawString(text, xPos, yPos-1);
		g.drawImage(backpanel, xPos, yPos, null);
		g.drawImage(buffers[bufferInUse], xPos, yPos, null);
	};

	@Override
	protected void isClicked() {
		
	}

	@Override
	protected void isFocused() {
		
	}

	@Override
	protected void uppdate() {
		
	}

}
