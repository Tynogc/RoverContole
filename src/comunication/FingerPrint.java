package comunication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FingerPrint {
	
	public int lenght;
	
	private String[] amcs;
	
	private static char[] pw;
	
	public FingerPrint(){
		if(pw == null)pw = new char[]{};
		
		lenght = 0;
		try{
			FileReader fr = new FileReader("res/keys.txt");
		    BufferedReader br = new BufferedReader(fr);
		    
		    while (br.readLine() != null &&lenght<1000) {
		    	lenght++;
			}
		    
		    br.close();
			}catch (FileNotFoundException e) {
				debug.Debug.println(e.getMessage(), debug.Debug.ERROR);
				return;
			}catch (IOException e) {
				debug.Debug.println(e.getMessage(), debug.Debug.ERROR);
			}catch (NullPointerException e) {
				debug.Debug.println(e.getMessage(), debug.Debug.ERROR);
		}
		
		amcs = new String[lenght];
		try{
			FileReader fr = new FileReader("res/keys.txt");
		    BufferedReader br = new BufferedReader(fr);
		    
		    for (int i = 0; i < amcs.length; i++) {
				amcs[i] = br.readLine();
			}
		    
		    br.close();
			}catch (FileNotFoundException e) {
				debug.Debug.println(e.getMessage(), debug.Debug.ERROR);
				return;
			}catch (IOException e) {
				debug.Debug.println(e.getMessage(), debug.Debug.ERROR);
			}catch (NullPointerException e) {
				debug.Debug.println(e.getMessage(), debug.Debug.ERROR);
		}
	}
	
	public String getFingerprintAt(int i){
		if(i<0||i>=amcs.length)return "";
		
		PWCoding c = new PWCoding();
		c.generateCode(pw, false);
		
		return c.decode(amcs[i]);
	}
	
	public static void setPW(String s){
		if(s == null)return;
		pw = new char[s.length()];
		for (int i = 0; i < s.length(); i++) {
			pw[i] = s.charAt(i);
		}
	}

}
