package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;

public abstract class DataFiled extends Button{
	
	private Color col;
	
	public static final Color l1 = new Color(120,120,120);
	public static final Color l2 = new Color(70,70,70);
	private static final Color l3 = new Color(0,0,0,100);
	
	public DataFiled(int x, int y, int wi, int hi, Color c) {
		super(x, y, wi, hi);
		col = c;
		textColor = black;
		text = "EMPTY";
		setBold(false);
		setBig(false);
	}

	public Color getColor() {
		return col;
	}

	public void setColor(Color col) {
		this.col = col;
	}

	@Override
	protected void isFocused() {
		
	}
	
	public void paintYou(Graphics g){
		if(disabled){
			g.setFont(font);
			g.setColor(textColor);
			g.drawString(text, xPos+offsettext+2, yPos+(ySize/2)+5);
			g.setColor(l1);
			g.drawRect(xPos, yPos, xSize, ySize);
		}else{
			g.setColor(col);
			g.fillRect(xPos, yPos, xSize, ySize);
			g.setColor(l1);
			g.drawRect(xPos, yPos, xSize, ySize);
			g.setColor(l2);
			g.drawRect(xPos+1, yPos+1, xSize-2, ySize-2);
			g.setColor(l3);
			g.drawLine(xPos+2, yPos+2, xPos+xSize-2, yPos+2);
			g.drawLine(xPos+xSize-2, yPos+3, xPos+xSize-2, yPos+ySize-2);
			
			g.setFont(font);
			g.setColor(textColor);
			g.drawString(text, xPos+offsettext+2, yPos+(ySize/2)+5);
		}
		
		next.paintYou(g);
	}
	
	public void setData(String s){
		setText(s);
	}

	
}
