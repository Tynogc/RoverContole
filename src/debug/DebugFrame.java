package debug;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class DebugFrame extends JFrame{
	
	private DebPanel panel;
	
	private String input = "";
	
	private int a = 0;
	private int b = 10;
	
	private byte canState;
	private boolean checkState;
	
	public static int dfl = -280;
	
	public DebugFrame(){
		setBounds(100,0,Debug.sizeX+110,Debug.sizeY+80+dfl);
		panel = new DebPanel(this);
		add(panel);
		Debug.panel = panel;
		setVisible(true);
		panel.setFocusable(false);
		setFocusable(true);
		canState = 0;
		checkState = false;
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(arg0.getButton() == MouseEvent.BUTTON1){
					Debug.knowMemory();
					Debug.displayMemory(Debug.COM);
				}
				else{
					Debug.remove(3);
					Debug.print(b+"%", Debug.SUBWARN);
					b++;
				}
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c = arg0.getKeyChar();
				if(checkState){
					if(c == '\n') canState = 3;
					if(c == 'y') canState = 1;
					if(c == 'n') canState = 2;
					
					return;
				}
				if(c == '\n'){
					Debug.println(input, Debug.PRICOM);
					DebugComand.operateComand(input);
					input = "";
				}else if((int)arg0.getKeyChar() == 8){
					if(input.length()>=1)
					input = input.substring(0, input.length()-1);
				}else{
					input += c;
				}
				panel.input = input;
				panel.paint(panel.getGraphics());
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}
		});
		setVisible(true);
		
		paint(getGraphics());
	}
	
	public void setCheckState(boolean state){
		checkState = state;
		canState = 0;
	}
	
	public byte canState(){
		return canState;
	}

}
