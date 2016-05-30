package comunication;

import java.awt.Color;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import userInterface.KeySystem;
import menu.Button;
import menu.DataFiled;

public class ConnectionChange {
	
	public DataFiled ip;
	public DataFiled ipText;
	private static String ipString;
	public DataFiled port;
	public DataFiled portText;
	private static String portString;
	
	private static final String file = "res/set/ipSettings.txt";
	
	public Button save;
	private KeySystem key;
	
	public ConnectionChange(int x, int y, KeySystem k){
		ip = new DataFiled(x+50,y,100,20, Color.black) {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isClicked() {
				key.deletInput();
			}
		};
		ip.setText(ipString);
		ip.setTextColor(new Color(100,100,250));
		ipText = new DataFiled(x,y,45,20, Color.white) {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isClicked() {
				
			}
		};
		ipText.setText("IP");
		
		port = new DataFiled(x+50,y+30,100,20, Color.black) {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isClicked() {
				key.deletInput();
			}
		};
		port.setText(portString);
		port.setTextColor(new Color(100,100,250));
		portText = new DataFiled(x,y+30,45,20, Color.white) {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isClicked() {
			}
		};
		portText.setText("PORT");
		
		save = new Button(x+200,y,"res/win/gui/cli/gsk") {
			@Override
			protected void uppdate() {
			}
			@Override
			protected void isFocused() {
			}
			
			@Override
			protected void isClicked() {
				saveToFile();
				save.setDisabled(true);
			}
		};
		save.setText("Save");
		save.setDisabled(true);
		
		key = k;
	}
	
	public void uppdate(){
		if(ip.wasLastClicked()){
			ipString = key.getKeyChain();
			if(key.getKeyChain().length()>0)save.setDisabled(false);
			if((System.currentTimeMillis()/500)%2==0)ip.setText(ipString+" ");
			else ip.setText(ipString+ "|");
		}
		if(port.wasLastClicked()){
			portString = key.getKeyChain();
			if(key.getKeyChain().length()>0)save.setDisabled(false);
			if((System.currentTimeMillis()/500)%2==0)port.setText(portString+" ");
			else port.setText(portString+ "|");
		}
	}
	
	private void saveToFile(){
		debug.Debug.println(" Saved Connection Info: IP:"+ipString+" PORT:"+portString, debug.Debug.SUBCOM);
		PrintWriter writer = null; 
		try { 
			writer = new PrintWriter(new FileWriter(file)); 
			writer.println(ipString);
			writer.println(portString);
		} catch (IOException ioe) { 
			ioe.printStackTrace(); 
		} finally { 
			if (writer != null){ 
				writer.flush(); 
				writer.close(); 
			} 
		}
	}
	
	private static void loadFromFile(){
		try{
			FileReader fr = new FileReader(file);
		    BufferedReader br = new BufferedReader(fr);
		    
		    ipString = br.readLine();
		    portString = br.readLine();
		    
		    br.close();
			}catch (FileNotFoundException e) {
				debug.Debug.println(e.getMessage(), debug.Debug.ERROR);
				return;
			}catch (IOException e) {
				debug.Debug.println(e.getMessage(), debug.Debug.ERROR);
			}
	}
	
	public static String getIp(){
		loadFromFile();
		if(ipString == null)return "localhost";
		return ipString;
	}
	
	public static int getPort(){
		loadFromFile();
		int i = 0;
		try {
			i = Integer.parseInt(portString);
		} catch (Exception e) {
			debug.Debug.println("Faild to pars Integer! "+portString+" is no valid Port!", debug.Debug.ERROR);
		}
		return i;
	}

}
