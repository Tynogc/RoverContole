package userInterface;

public class MainThread extends Thread{

	public static long currentTime = 0;
	
	private MainFrame frame;
	
	private boolean isRunning = true;
	
	public static final int timeToFrameUppdate = 50;
	
	private int lastFPS;
	private long fpsMarker;
	private int currFps;
	private double frameTimeOVA;
	
	public MainThread(MainFrame f){
		super();
		frameTimeOVA = 50;
		frame = f;
	}
	
	public void run(){
		int sleepTime = 0;
		Runtime.getRuntime().addShutdownHook(new ExitThread());
		currentTime = System.currentTimeMillis();
		while(isRunning){
			currFps++;
			
			currentTime = System.currentTimeMillis();
			frame.loop(lastFPS, sleepTime,(int)frameTimeOVA);
			
			//FPS uberprufen
			if(currentTime-fpsMarker>500){
				fpsMarker = currentTime;
				lastFPS = currFps*2;
				currFps = 0;
			}
			
			int q = (int)(System.currentTimeMillis()-currentTime);
			frameTimeOVA = (frameTimeOVA*99+q)/100;
			
			//Schlafen
			sleepTime = timeToFrameUppdate-(int)(System.currentTimeMillis()-currentTime);
			if(sleepTime > 0 ){
				try {
					sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
}
