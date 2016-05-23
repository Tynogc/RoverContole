package process;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer extends Thread{
	
	private static SoundPlayer sound;
	
	public SoundPlayer(){
		start();
	}
	
	public void run(){
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inpStream = AudioSystem.getAudioInputStream(
					new File("res/audio/1.wmv"));
			clip.open(inpStream);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
