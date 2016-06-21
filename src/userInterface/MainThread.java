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
			try {
				frame.loop(lastFPS, sleepTime,(int)frameTimeOVAsmal, (int)frameTimeOVA);
			} catch (Exception e) {
				debug.Debug.println("* FATAL: Uncatched Error!", debug.Debug.FATAL);
				debug.Debug.printExeption(e);
				isRunning = false;
				ExitThread.restart = true;
			}
			
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
		isRunning = true;
		sleepTime = 10;
		debug.Debug.println("* Restoring to save Mode", debug.Debug.FATAL);
		while (isRunning) {
			frame.saveLoop();
			
			sleepTime--;
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			debug.Debug.print("-");
			if(sleepTime < 0){
				System.exit(-1);
			}
		}
	}
	
	
	
}
