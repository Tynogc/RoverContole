package process;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer extends Thread{
	
	private static SoundPlayer sound;
	
	private static int soundToPlay = -1;
	private File[] sounds1;
	private static boolean threadIsRunning;
	
	public static final int SOUND_ALARM = 1;
	
	public SoundPlayer(){
		sounds1 = new File[10];
		sounds1[1] = new File("res/audio/alarm.wav");
		
		start();
	}
	
	public void run(){
		debug.Debug.println(" SoundPlayer Started!");
		threadIsRunning = true;
		while (threadIsRunning) {
			if(soundToPlay == -1){
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				if(soundToPlay >= sounds1.length || soundToPlay < 0){
					debug.Debug.println("* Cant Play sound With id:"+soundToPlay, debug.Debug.WARN);
					soundToPlay = -1;
				}else{
					//playSound(sounds1[soundToPlay]);
					soundToPlay = -1;
				}
			}
			
		}
		debug.Debug.println("* SoundThread TERMINATED!",debug.Debug.ERROR);
	}
	
	private final int BUFFER_SIZE = 128000;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;

    /**
     * @param filename the name of the file that is going to be played
     */
    private void playSound(File soundFile){
    	if(soundFile == null)return;

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
        	debug.Debug.println(e.toString(), debug.Debug.ERROR);
            return;
        }

        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            debug.Debug.println(e.toString(), debug.Debug.ERROR);
            return;
            //System.exit(1);
        } catch (Exception e) {
        	debug.Debug.println(e.toString(), debug.Debug.ERROR);
            return;
            //System.exit(1);
        }

        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();
    }
    
    private void playSound2(File f){
    	try{
    	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f);
    		Clip clip = AudioSystem.getClip();
    		clip.open(audioInputStream);
    		FloatControl gainControl = 
    		    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    		gainControl.setValue(-1.0f); // Reduce volume by 10 decibels.
    		clip.start();
    	}catch(Exception e){
    		debug.Debug.println(e.toString(), debug.Debug.ERROR);
            return;
    	}
    }
    
    public static void playSound(int i){
    	if(debug.Debug.showExtendedBootInfo)
    		debug.Debug.println("* Sound "+i+" is now Being played!", debug.Debug.MASSAGE);
    	soundToPlay = i;
    }

}
