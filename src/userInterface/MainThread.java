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
	private double frameTimeOVAsmal;
	
	public MainThread(MainFrame f){
		super();
		frameTimeOVA = 50;
		frameTimeOVAsmal = 50;
		frame = f;
	}
	
	public void run(){
		int sleepTime = 0;
		Runtime.getRuntime().addShutdownHook(new ExitThread());
		fpsMarker = System.currentTimeMillis();
		while(isRunning){
			currFps++;
			
			currentTime = System.currentTimeMillis();
			frame.loop(lastFPS, sleepTime,(int)frameTimeOVAsmal, (int)frameTimeOVA);
			
			//FPS uberprufen
			if(currentTime-fpsMarker>500){
				fpsMarker += 500;
				if(Math.abs(fpsMarker-currentTime)>1000){
					fpsMarker = currentTime;
					currFps = 0;
					debug.Debug.println("WARNING: FPS criticaly low!", debug.Debug.WARN);
				}
				lastFPS = currFps*2;
				currFps = 0;
			}
			
			int q = (int)(System.currentTimeMillis()-currentTime);
			frameTimeOVA = (frameTimeOVA*499+q)/500;
			frameTimeOVAsmal = (frameTimeOVAsmal*49+q)/50;
			
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
