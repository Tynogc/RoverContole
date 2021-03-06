package guiMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import org.omg.CORBA.COMM_FAILURE;

import process.PowerSystem;
import process.ProcessControl;
import process.SSK;
import userInterface.MainFrame;
import menu.AbstractMenu;
import menu.Button;
import menu.DataFiled;

public class SideMenu extends AbstractMenu {
	
	private Button homeX;
	private Button homeY;
	private Button sskInt;
	
	private Button setMeter;
	private Button setAbsolut;
	private Button setLatLon;
	
	private BufferedImage buffX;
	private BufferedImage buffY;
	private BufferedImage buffAlt;
	
	private BufferedImage energy;
	
	private long gpsRedraw;
	private BufferedImage gpsStatus;
	
	private BufferedImage[][] bigNums;
	private BufferedImage[][] smalNums;
	
	private BufferedImage[] entitys;
	private BufferedImage[] batt;
	
	private byte navPanelState;
	
	private DataFiled sskGes;
	private DataFiled sskRes;
	private DataFiled sskm1;
	private DataFiled sskm2;
	private DataFiled sskm3;
	private DataFiled sskm4;
	
	private Color brown = new Color(185,122,87);
	
	public SideMenu(){
		homeX  = new Button(MainFrame.sizeX-400,100,"res/win/gui/spb/HOMEX") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				
			}
		};
		add(homeX);
		homeX.setSubtext("Home X");
		homeY  = new Button(MainFrame.sizeX-400,160,"res/win/gui/spb/HOMEY") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				
			}
		};
		add(homeY);
		homeY.setSubtext("Home Y");
		
		sskInt  = new Button(MainFrame.sizeX-400,548,"res/win/gui/spb/SSKINT") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				comunication.ComunicationControl.com.send("*SSK_BREAK");
			}
		};
		add(sskInt);
		sskInt.setSubtext("Interupt SSK");
		
		setMeter = new Button(MainFrame.sizeX-400,70,"res/win/gui/cli/Gsk") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				homeX.setDisabled(false);
				homeY.setDisabled(false);
				navPanelState = 0;
				setNavPanel();
			}
		};
		add(setMeter);
		setMeter.setText("Relativ");
		setAbsolut = new Button(MainFrame.sizeX-320,70,"res/win/gui/cli/Gsk") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				homeX.setDisabled(true);
				homeY.setDisabled(true);
				navPanelState = 1;
				setNavPanel();
			}
		};
		setAbsolut.setText("Absolut");
		setAbsolut.setTextColor(new Color(30,250,30));
		add(setAbsolut);
		setLatLon = new Button(MainFrame.sizeX-240,70,"res/win/gui/cli/Gsk") {
			@Override
			protected void uppdate() {}
			@Override
			protected void isFocused() {}
			@Override
			protected void isClicked() {
				homeX.setDisabled(false);
				homeY.setDisabled(false);
				navPanelState = 2;
				setNavPanel();
			}
		};
		setLatLon.setText("Lat-Lon");
		add(setLatLon);
		
		buffX = new BufferedImage(300,56,BufferedImage.TYPE_INT_RGB);
		buffY = new BufferedImage(300,56,BufferedImage.TYPE_INT_RGB);
		buffAlt = new BufferedImage(300,56,BufferedImage.TYPE_INT_RGB);
		energy = new BufferedImage(300,48,BufferedImage.TYPE_INT_RGB);
		
		bigNums = new BufferedImage[11][3];
		smalNums = new BufferedImage[11][3];
		for (int i = 0; i < bigNums.length; i++) {
			for (int k = 0; k < 3; k++) {
				bigNums[i][k] = generatColoredBuffer(new ImageIcon("res/win/sub/gs"+i+".png"), k);
				smalNums[i][k] = generatColoredBuffer(new ImageIcon("res/win/sub/gc"+i+".png"), k);
			}
		}
		
		sskGes = new DataFiled(MainFrame.sizeX-330, 548, 150, 20, brown) {
			private int i = -1;
			@Override
			protected void uppdate() {
				if(i != ProcessControl.ssk.mc1){
					i = ProcessControl.ssk.mc1;
					setText(SSK.csP[i]);
					if(i == 2){
						setColor(Color.red);
						setTextColor(Color.black);
						setBlinking(false);
					}else if(i == 0){
						setColor(Color.blue);
						setTextColor(Color.white);
						setBlinking(false);
					}else if(i == 3){
						setColor(Color.cyan);
						setTextColor(Color.black);
						setBlinking(false);
					}else if(i == 1){
						setColor(Color.red);
						setTextColor(Color.black);
						setBlinking(true);
					}
				}
			}
			@Override
			protected void isClicked() {}
		};
		add(sskGes);
		sskRes = new DataFiled(MainFrame.sizeX-330+150, 548, 150, 20, brown) {
			private int i = -1;
			@Override
			protected void uppdate() {
				if(i != ProcessControl.ssk.mc2){
					i = ProcessControl.ssk.mc2;
					setText(SSK.csQ[i]);
					if(i == 0){
						setColor(Color.RED);
						setTextColor(Color.black);
						setBlinking(false);
					}else if(i == 2){
						setColor(Color.green);
						setTextColor(Color.black);
						setBlinking(true);
					}else if(i == 1){
						setColor(Color.green);
						setTextColor(Color.black);
						setBlinking(false);
					}else if(i == 3){
						setColor(Color.blue);
						setBlinking(false);
					}
				}
			}
			@Override
			protected void isClicked() {}
		};
		add(sskRes);
		sskm1 = new DataFiled(MainFrame.sizeX-330, 568, 75, 20, brown) {
			private int i = -1;
			@Override
			protected void uppdate() {
				if(i != ProcessControl.ssk.getM1()){
					i = ProcessControl.ssk.getM1();
					setText(SSK.cs1[i]);
					if(i == 0){
						setColor(brown);
					}else if(i == 1){
						setColor(Color.green);
					}else{
						setColor(Color.red);
					}
				}
				setBlinking(ProcessControl.ssk.getActiv(0));
			}
			@Override
			protected void isClicked() {}
		};
		add(sskm1);
		sskm2 = new DataFiled(MainFrame.sizeX-330+75, 568, 75, 20, brown) {
			private int i = -1;
			@Override
			protected void uppdate() {
				if(i != ProcessControl.ssk.getM2()){
					i = ProcessControl.ssk.getM2();
					setText(SSK.cs2[i]);
					if(i == 0){
						setColor(brown);
					}else if(i == 1){
						setColor(Color.green);
					}else{
						setColor(Color.red);
					}
				}
				setBlinking(ProcessControl.ssk.getActiv(1));
			}
			@Override
			protected void isClicked() {}
		};
		add(sskm2);
		sskm3 = new DataFiled(MainFrame.sizeX-330+150, 568, 75, 20, brown) {
			private int i = -1;
			@Override
			protected void uppdate() {
				if(i != ProcessControl.ssk.getM3()){
					i = ProcessControl.ssk.getM3();
					setText(SSK.cs3[i]);
					if(i == 0){
						setColor(brown);
					}else if(i == 1){
						setColor(Color.green);
					}else{
						setColor(Color.red);
					}
				}
				setBlinking(ProcessControl.ssk.getActiv(2));
			}
			@Override
			protected void isClicked() {}
		};
		add(sskm3);
		sskm4 = new DataFiled(MainFrame.sizeX-330+225, 568, 75, 20, brown) {
			private int i = -1;
			@Override
			protected void uppdate() {
				if(i != ProcessControl.ssk.getM4()){
					i = ProcessControl.ssk.getM4();
					setText(SSK.cs4[i]);
					if(i == 0){
						setColor(brown);
					}else if(i == 1){
						setColor(Color.green);
					}else{
						setColor(Color.red);
					}
				}
				setBlinking(ProcessControl.ssk.getActiv(3));
			}
			@Override
			protected void isClicked() {}
		};
		add(sskm4);
		
		entitys = new BufferedImage[]{
				guiMenu.PicLoader.pic.getImage("res/win/sub/aw1.png"),
				guiMenu.PicLoader.pic.getImage("res/win/sub/aw2.png"),
				guiMenu.PicLoader.pic.getImage("res/win/sub/aw3.png"),
				guiMenu.PicLoader.pic.getImage("res/win/sub/aw10.png")
		};
		
		batt = new BufferedImage[]{
				guiMenu.PicLoader.pic.getImage("res/win/sub/b0.png"),
				guiMenu.PicLoader.pic.getImage("res/win/sub/b1.png"),
				guiMenu.PicLoader.pic.getImage("res/win/sub/b2.png"),
				guiMenu.PicLoader.pic.getImage("res/win/sub/b3.png"),
		};
		
		navPanelState = 0;
		
		makeBack(buffX, 0, 4, false);
		makeBack(buffY, 14459, 4, false);
		makeBack(buffAlt, 33025, 0, false);
		
		makeVolts(142, 0, 0, energy, true, true);
	}
	
	private void makeBack(BufferedImage bu, int num, int color,boolean green){
		Graphics g = bu.getGraphics();
		if(!green)g.drawImage(entitys[0], 0, 0, null);
		else g.drawImage(entitys[2], 0, 0, null);
		if(color < 3){
			g.drawImage(bigNums[(num/100000)%10][color], 9, 3, null);
			g.drawImage(bigNums[(num/10000)%10][color], 41, 3, null);
			g.drawImage(bigNums[(num/1000)%10][color], 73, 3, null);
			g.drawImage(bigNums[(num/100)%10][color], 105, 3, null);
			g.drawImage(smalNums[(num/10)%10][color], 146, 13, null);
			g.drawImage(smalNums[num%10][color], 169, 13, null);
		}else{
			g.drawImage(bigNums[10][0], 9, 3, null);
			g.drawImage(bigNums[10][0], 41, 3, null);
			g.drawImage(bigNums[10][0], 73, 3, null);
			g.drawImage(bigNums[10][0], 105, 3, null);
			g.drawImage(smalNums[10][0], 146, 13, null);
			g.drawImage(smalNums[10][0], 169, 13, null);
		}
	}
	
	private void makeBackGPS(BufferedImage bu, int degree,
			int degreeMinuts, int degreeSeconds, int color){
		Graphics g = bu.getGraphics();
		g.drawImage(entitys[1], 0, 0, null);
		
		if(color < 3){
			if(degree>=100)g.drawImage(smalNums[1][color], 15-23, 13, null);
			g.drawImage(smalNums[(degree/10)%10][color], 15, 13, null);
			g.drawImage(smalNums[degree%10][color], 38, 13, null);
			
			g.drawImage(smalNums[(degreeMinuts/10)%10][color], 67, 13, null);
			g.drawImage(smalNums[(degreeMinuts/10)%10][color], 90, 13, null);
			
			g.drawImage(bigNums[(degreeSeconds/1000)%10][color], 120, 3, null);
			g.drawImage(bigNums[(degreeSeconds/100)%10][color], 152, 3, null);
			g.drawImage(bigNums[(degreeSeconds/10)%10][color], 190, 3, null);
			g.drawImage(bigNums[degreeSeconds%10][color], 222, 3, null);
		}else{
			g.drawImage(smalNums[10][0], 15, 13, null);
			g.drawImage(smalNums[10][0], 38, 13, null);
			
			g.drawImage(smalNums[10][0], 67, 13, null);
			g.drawImage(smalNums[10][0], 90, 13, null);
			g.drawImage(bigNums[10][0], 120, 3, null);
			g.drawImage(bigNums[10][0], 152, 3, null);
			g.drawImage(bigNums[10][0], 190, 3, null);
			g.drawImage(bigNums[10][0], 222, 3, null);
		}
	}
	
	private void makeVolts(int volt, int voltStatus, int voltStatus2, BufferedImage bu, boolean disabled, boolean disabled2){
		Graphics g = bu.getGraphics();
		g.drawImage(entitys[3], 0,0,null);
		
		if(disabled){
			g.drawImage(smalNums[10][0], 3, 2, null);
			g.drawImage(smalNums[10][0], 26, 2, null);
			g.drawImage(smalNums[10][0], 52, 2, null);
		}else{
			int kap = 0;
			if(voltStatus == 0)kap = 2;
			g.drawImage(smalNums[(volt/100)%10][kap], 3, 2, null);
			g.drawImage(smalNums[(volt/10)%10][kap], 26, 2, null);
			g.drawImage(smalNums[volt%10][kap], 52, 2, null);
			
			if(voltStatus >= 0){
				if(voltStatus >= batt.length)voltStatus = batt.length-1;
				g.drawImage(batt[voltStatus],101,0,null);
			}
		}
		
		if(!disabled2 && voltStatus2 >= 0){
			if(voltStatus2 >= batt.length)voltStatus2 = batt.length-1;
			g.drawImage(batt[voltStatus2],133,0,null);
		}
	}

	private int batCheckSumm = -100;
	@Override
	protected void uppdateIntern() {
		redrawGPSstatus();
		
		int imu = PowerSystem.volt1*1000+PowerSystem.volt2;
		if(imu != batCheckSumm){
			batCheckSumm = imu;
			makeVolts(PowerSystem.volt1/10, PowerSystem.voltBar1, PowerSystem.voltBar2, energy, 
					PowerSystem.volt1 <= PowerSystem.DISABLED, PowerSystem.volt2 <= PowerSystem.DISABLED);
		}
	}

	@Override
	protected void paintIntern(Graphics g) {
		g.drawImage(buffX, MainFrame.sizeX-330, 100, null);
		g.drawImage(buffY, MainFrame.sizeX-330, 160, null);
		g.drawImage(buffAlt, MainFrame.sizeX-330, 220, null);
		
		g.drawImage(energy, MainFrame.sizeX-330, 500, null);
		g.drawImage(gpsStatus, MainFrame.sizeX-330, 300, null);
	}
	
	private BufferedImage generatColoredBuffer(ImageIcon ima, int i){
		BufferedImage ret = new BufferedImage(ima.getIconWidth(), ima.getIconHeight()
				, BufferedImage.TYPE_INT_ARGB);
		Graphics g = ret.getGraphics();
		g.drawImage(ima.getImage(),0 ,0,null);
		
		Color c0  = null;
		Color c1  = null;
		Color c2  = null;
		
		if(i == 2){
			c0 = new Color(205,10,10);
			c1 = new Color(230,15,15);
			c2 = new Color(255,20,20);
		}else if(i == 1){
			c0 = new Color(80,80,80);
			c1 = new Color(100,100,100);
			c2 = new Color(110,110,110);
		}else{
			c0 = new Color(0xffcdcd99);
			c1 = new Color(0xffe8e8cc);
			c2 = new Color(0xffffffe0);
		}
		
		for (int j = 0; j < ima.getIconWidth(); j++) {
			for (int k = 0; k < ima.getIconHeight(); k++) {
				int o = ret.getRGB(j, k);
				if(o == 0xffcdcdcd){
					g.setColor(c0);
					g.drawRect(j, k, 0, 0);
				}
				if(o == 0xffe8e8e8){
					g.setColor(c1);
					g.drawRect(j, k, 0, 0);
				}
				if(o == 0xffffffff){
					g.setColor(c2);
					g.drawRect(j, k, 0, 0);
				}
				if(o == 0xff7f7f7f){
					g.setColor(new Color(80,80,80));
					g.drawRect(j, k, 0, 0);
				}
			}
		}
		
		return ret;
	}
	
	private void setNavPanel(){
		switch (navPanelState) {
		case 0://Relativ
			makeBack(buffX, 0, 4, false);
			makeBack(buffY, 14459, 0, false);
			makeBack(buffAlt, 33025, 0, false);
			break;
		case 1://Absolut
			makeBack(buffX, 2379, 2, true);
			makeBack(buffY, 14459, 0, true);
			makeBack(buffAlt, 33025, 0, true);
			break;
		case 2://LatLon
			makeBackGPS(buffX, 153,22,6834,1);
			makeBackGPS(buffY, 84,76,9432,0);
			makeBack(buffAlt, 33025, 0, false);
			break;

		default:
			break;
		}
	}
	
	private void redrawGPSstatus(){
		long l = System.currentTimeMillis();
		if(l-gpsRedraw >= 1000){
			gpsRedraw = l;
			process.GPSControle gps = process.ProcessControl.gps;
			
			gpsStatus = new BufferedImage(320, 60, BufferedImage.TYPE_INT_ARGB);
			Graphics g = gpsStatus.getGraphics();
			int q = 0;
			process.GPSsatelite sat;
			for (int i = 0; i < gps.numOfSats; i++) {
				sat = gps.getSat(i);
				if(sat.getNumber() != 0){
					if(q <= 8){
						GPSstatus.paintASatelite(q*40, 0, sat.getNumber(), sat.getState(), g);
					}else{
						GPSstatus.paintASatelite((q-9)*40, 30, sat.getNumber(), sat.getState(), g);
					}
					q++;
				}
			}
		}
	}

}
