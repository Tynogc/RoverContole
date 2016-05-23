package coding;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import userInterface.KeySystem;
import userInterface.MainThread;
import menu.AbstractMenu;
import menu.Button;
import menu.DataFiled;

public class CodeMenu extends AbstractMenu{

	private String[] str;
	private int pos;
	private static final int arrayLenght = 2000;
	private static final int linesTop = 20;
	private int fontSize = 15;
	
	private String input;
	private DataFiled inputField;
	
	private KeySystem key;
	
	private BufferedImage text;
	
	private Button load;
	private Button tNew;
	private Button compile;
	private Button verify;
	private Button simulate;
	private Button execute;
	private Button pause;
	private Button abort;
	
	public CodeMenu(KeySystem k) {
		key = k;
		str = new String[arrayLenght];
		pos = 0;
		uppdateText(0);
		
		tNew = new Button(260,400,"res/win/gui/spb/NEW") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				
			}
		};
		add(tNew);
		
		inputField = new DataFiled(50,670,300,30,new Color(0,0,0,0)) {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isClicked() {
				key.deletInput();
			}
		};
		inputField.setText("");
		inputField.setTextColor(Color.white);
		add(inputField);
		
		Button d100 = new Button(370,418,"res/win/gui/cli/Gsk") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				uppdateText(pos+100);
			}
		};
		add(d100);
		d100.setText("+100");
		Button d10 = new Button(370,398,"res/win/gui/cli/Gsk") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				uppdateText(pos+10);
			}
		};
		add(d10);
		d10.setText("+10");
		Button d1 = new Button(370,378,"res/win/gui/cli/Gsk") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				uppdateText(pos+1);
			}
		};
		add(d1);
		d1.setText("+1");
		
		Button u100 = new Button(370,298,"res/win/gui/cli/Gsk") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				uppdateText(pos-100);
			}
		};
		add(u100);
		u100.setText("-100");
		Button u10 = new Button(370,318,"res/win/gui/cli/Gsk") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				uppdateText(pos-10);
			}
		};
		add(u10);
		u10.setText("-10");
		Button u1 = new Button(370,338,"res/win/gui/cli/Gsk") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				uppdateText(pos-1);
			}
		};
		add(u1);
		u1.setText("-1");
		DataFiled posDat = new DataFiled(370,358,100,20,Color.black) {
			@Override
			protected void uppdate() {
				setText("At: "+pos);
			}
			@Override
			protected void isClicked() {}
		};
		add(posDat);
		posDat.setTextColor(Color.white);
		
		input = "";
	}
	
	private void uppdateText(int value){
		BufferedImage buffer = new BufferedImage(300, 600, BufferedImage.TYPE_INT_RGB);
		Graphics g = buffer.getGraphics();
		int min = 10000;
		int max = -10000;
		
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
		for (int i = value-linesTop; i < value+linesTop; i++) {
			if(i>=min&&i<=max)continue;
			if(i<0)continue;
			if(i>=arrayLenght)break;
			drawString(30, (i-value+linesTop)*fontSize, g, str[i]);
		}
		
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, fontSize-4));
		g.setColor(Color.gray);
		for (int i = value-linesTop; i < value+linesTop; i++) {
			if(i>=min&&i<=max)continue;
			if(i<0)continue;
			if(i>=arrayLenght)break;
			if(i<10)g.drawString("000"+i,2, (i-value+linesTop)*fontSize);
			else if(i<100)g.drawString("00"+i,2, (i-value+linesTop)*fontSize);
			else if(i<1000)g.drawString("0"+i,2, (i-value+linesTop)*fontSize);
			else g.drawString(""+i,2, (i-value+linesTop)*fontSize);
		}
		
		pos = value;
		text = null;
		text = buffer;
	}
	
	private StringTransmission drawString(int atX, int atY, Graphics g, String s){
		if(s == null)return null;
		if(s.length() == 0)return null;
		StringTransmission st = new StringTransmission();
		boolean endFalse = s.charAt(s.length()-1) == '|' && s.length()>2;
		if(endFalse) s = s.substring(0, s.length()-1);
		st.setString(s);
		st.process();
		
		int i = 0;
		for (; i < s.length(); i++) {
			g.setColor(StringTransmission.colorPatch(st.getIntAtPois(i)));
			g.drawString(""+s.charAt(i), atX+i*9, atY);
		}
		if(endFalse)
			g.drawString("|", atX+i*9, atY);
		
		return st;
	}
	
	@Override
	protected void uppdateIntern() {
		if(inputField.wasLastClicked()){
			input = key.getKeyChain();
			if(key.isEnter()){
				if(pos>=0&&pos<str.length)
					str[pos]=input;
				input = "";
				uppdateText(pos+1);
				key.deletInput();
			}
		}
	}
	
	@Override
	public void scrolled(int i) {
		uppdateText(pos+i);
	};

	@Override
	protected void paintIntern(Graphics g) {
		if(text != null)
			g.drawImage(text, 50, 70, null);
		g.setColor(Color.gray);
		g.drawRect(50, 70, 300, 600);
		g.drawRect(50, 358, 300, 15);
		
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
		StringTransmission st;
		if(inputField.wasLastClicked()&&MainThread.currentTime%1000<=500){
			st = drawString(50, 690, g, (input+"|"));
		}else{
			st = drawString(50, 690, g, input);
		}
		if(st != null)
		g.drawString(st.getCompiled(), 50, 710);
	}

}
