package guiMenu;

import java.awt.Color;
import java.awt.Graphics;

import userInterface.MainFrame;
import menu.AbstractMenu;
import menu.Button;
import menu.OsziDiagramm;

public class PerformanceMenu extends AbstractMenu{

	private static PerformanceMenu per;
	
	private int[] time;
	private double[] primary;
	private double[] secondary;
	private double[] percentage;
	private int[] max;
	
	private double load;
	private double loadN;
	private double nanosec;
	private double nanosecFull;
	
	private long startTime;
	
	private static final int lenght = 7;
	
	public static final int CreateBuffer = 0;
	public static final int Communication = 1;
	public static final int Process = 2;
	public static final int UpdateGui = 3;
	public static final int PaintGui = 4;
	public static final int PaintBack = 5;
	
	private Color[] cols;
	
	private OsziDiagramm digrammLog;
	private OsziDiagramm diagrammLogRam;
	
	public PerformanceMenu() {
		
		time = new int[lenght];
		max = new int[lenght];
		primary = new double[lenght];
		secondary = new double[lenght];
		percentage = new double[lenght];
		
		cols = new Color[]{
				Color.red,
				new Color(234,187,0),
				new Color(102,52,228),
				new Color(0,219,46),
				new Color(0,11,235),
				new Color(0,187,221),
				Color.gray
		};
		
		for (int i = 0; i < lenght; i++) {
			time[i] = 0;
			max[i] = 0;
			primary[i] = 0.0;
			secondary[i] = 0.0;
			percentage[i] = 0.0;
		}
		
		digrammLog = new OsziDiagramm(MainFrame.sizeX-370, 75, PicLoader.pic.getImage("res/win/sub/tsk.png"));
		digrammLog.setText("Load:");
		add(digrammLog);
		diagrammLogRam = new OsziDiagramm(MainFrame.sizeX-250, 75, PicLoader.pic.getImage("res/win/sub/tsk.png"));
		diagrammLogRam.setText("RAM:");
		add(diagrammLogRam);
		
		per = this;
		/**
		 * DEBUG_ONLY section ahead!!!!!!!!!!!!!!!!!!!
		 */
		Button b11 = new Button(MainFrame.sizeX-370,500,"res/win/gui/cli/Gs") {
			private boolean a = false;
			@Override
			protected void uppdate() {}
			
			@Override
			protected void isFocused() {}
			
			@Override
			protected void isClicked() {
				a = !a;
				if(a){
					WarnMenu.warn.setAlarm(a, WarnMenu.TYPE_SYSTEM, "Debug-Alarm 1");
					setText("Stop AL");
				}else{
					WarnMenu.warn.setAlarm(a, WarnMenu.TYPE_SYSTEM, "No Alarm");
					setText("Alarm");
				}
			}
		};
		add(b11);
		Button b12 = new Button(MainFrame.sizeX-260,500,"res/win/gui/cli/Gs") {
			private boolean a = false;
			@Override
			protected void uppdate() {}
			
			@Override
			protected void isFocused() {}
			
			@Override
			protected void isClicked() {
				a = !a;
				if(a){
					WarnMenu.warn.setWarn(a, WarnMenu.TYPE_SYSTEM, "Debug-Warn 1");
					setText("Stop Wa");
				}else{
					WarnMenu.warn.setWarn(a, WarnMenu.TYPE_SYSTEM, "No Warn");
					setText("Warning");
				}
			}
		};
		add(b12);
		Button b21 = new Button(MainFrame.sizeX-370,550,"res/win/gui/cli/Gs") {
			private boolean a = false;
			@Override
			protected void uppdate() {}
			
			@Override
			protected void isFocused() {}
			
			@Override
			protected void isClicked() {
				a = !a;
				if(a){
					WarnMenu.warn.setAlarm(a, WarnMenu.TYPE_SYSTEM_THREADS, "Debug-Alarm 2 Attention!");
					setText("Stop AL");
				}else{
					WarnMenu.warn.setAlarm(a, WarnMenu.TYPE_SYSTEM_THREADS, "System OK");
					setText("Alarm");
				}
			}
		};
		add(b21);
		Button b22 = new Button(MainFrame.sizeX-260,550,"res/win/gui/cli/Gs") {
			private boolean a = false;
			@Override
			protected void uppdate() {}
			
			@Override
			protected void isFocused() {}
			
			@Override
			protected void isClicked() {
				a = !a;
				if(a){
					WarnMenu.warn.setWarn(a, WarnMenu.TYPE_SYSTEM_THREADS, "Warning at OASIS!");
					setText("Stop Wa");
				}else{
					WarnMenu.warn.setWarn(a, WarnMenu.TYPE_SYSTEM_THREADS, "System OK");
					setText("Warning");
				}
			}
		};
		add(b22);
	}
	
	public static void markTime(int pos){
		if(per != null)per.mt(pos);
	}
	
	private void mt(int pos){
		if(pos<0 || pos>=lenght){
			debug.Debug.println("Error PerformanceMenu01: pos OOB:"+pos,debug.Debug.ERROR);
			return;
		}
		time[pos] = (int)(getTime()-startTime);
	}
	
	public static void startTime(){
		if(per != null)per.st();
	}
	private int iSiera;
	private void st(){
		time[lenght-1] = (int)(getTime()-startTime);
		startTime = getTime();
		
		double h = (double)time[lenght-1];
		iSiera++;
		if(iSiera>1000)iSiera = 1000;
		
		double lo = -((double)(time[lenght-1]-time[lenght-2])/(double)time[lenght-1]*100)+100;
		logInDia(lo);
		load = (load*9+(double)lo)/10;
		loadN = (loadN*(iSiera-1)+(double)lo)/iSiera;
		long sum = 0;
		for (int i = 0; i < lenght; i++) {
			if(time[i]==0){
				percentage[i]=0;
				continue;
			}
			time[i]-=sum;
			sum+=time[i];
			double per = (double)time[i]/h;
			percentage[i] = (percentage[i]*9+per)/10;
			secondary[i] = (secondary[i]*(iSiera-1)+per)/(iSiera);
			primary[i] = ((double)time[i]*49+(double)per)/50;
		}
		nanosecFull = (nanosecFull*49+sum)/50;
		nanosec = (nanosec*49+(sum-time[lenght-1]))/50;
	}
	
	private double logData= 0.0;
	private static int logTetta = 0;
	private void logInDia(double lo){
		logTetta++;
		
		logData = (logData*(logTetta-1)+lo)/logTetta;
		
		if(logTetta>20){
			logTetta = 0;
			digrammLog.logData(logData/100.0+0.02);
			logData = 0;
		}
	}
	
	public static void logRam(double r){
		if(per!= null)per.loRam(r);
	}
	private void loRam(double r){
		diagrammLogRam.logData(r);
	}
	
	private long getTime(){
		return System.nanoTime();
	}
	
	@Override
	protected void uppdateIntern() {
		
	}

	private final int atX = MainFrame.sizeX-370; 
	
	@Override
	protected void paintIntern(Graphics g) {
		int x = atX;
		g.setFont(Button.plainFont);
		for (int i = 0; i < lenght; i++) {
			int nx = (int)(percentage[i]*300);
			g.setColor(cols[i]);
			g.fillRect(x, 200, nx, 20);
			x += nx;
			g.drawString(getName(i), atX, 300+i*20);
			g.drawString(""+etsInt((long)(primary[i]))+"ns ("+(int)(percentage[i]*100)+"%)", atX+100, 300+i*20);
		}
		x = atX;
		for (int i = 0; i < lenght; i++) {
			int nx = (int)(secondary[i]*300);
			g.setColor(cols[i]);
			g.fillRect(x, 240, nx, 20);
			x += nx;
		}
		g.setColor(Color.gray);
		g.drawString("Load: "+(int)load+"% (Last Second)", atX, 199);
		g.drawString("Load: "+(int)loadN+"% (Last 20s)", atX, 238);
		g.drawString("Time: "+etsInt((long)nanosec)+"/"+etsInt((long)nanosecFull)+"ns", atX, 440);
	}
	
	private String etsInt(long l){
		String s = "";
		if(l<0)return s+l;
		long k = 10000000;
		for (int i = 0; i < 20; i++) {
			if(k<=l)break;
			k/=10;
			s+="0";
		}
		return s+l;
	}
	
	private String getName(int i){
		switch (i) {
		case CreateBuffer:
			return "Create Buffer:";
		case Communication:
			return "Communication:";
		case Process:
			return "Processes:";
		case PaintGui:
			return "Paint GUI:";
		case UpdateGui:
			return "Update GUI:";
		case PaintBack:
			return "Redraw Back:";
		case lenght-1:
			return "Sleep:";

		default:
			return "???";
		}
	}

}
