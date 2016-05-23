package comunication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Linker extends Thread{
	
	public static String ip = "localhost";
	public static int port = 3141;
	
	private Semaphore sema;
	
	private Socket soket;
	private String response;
	
	private FiFo upstream;
	private FiFo downstream;
	
	private boolean conectionAquiered;
	
	private boolean threadIsRunning;
	
	private Scanner sc;
	
	public Linker(){
		conectionAquiered = false;
		sema = new Semaphore(1);
		try {
			sema.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sema.release();
		
		upstream = new FiFo();
		downstream = new FiFo();
		
		debug.Debug.println("Conect to Rover on IP:"+ip+" Port:"+port);
		try {
			soket = new Socket();
			soket.connect(new InetSocketAddress(ip, port),4000);
		} catch (UnknownHostException e) {
			debug.Debug.println(e.toString(),debug.Debug.ERROR);
		}catch (ConnectException e) {
			debug.Debug.println(e.toString(),debug.Debug.WARN);
		} catch (IOException e) {
			debug.Debug.println(e.toString(),debug.Debug.ERROR);
		}
		if(soket.isConnected()){
			debug.Debug.bootMsg("Conected!", 0);
			threadIsRunning = true;
			start();
		}else{
			debug.Debug.bootMsg("Conected!", 1);
			threadIsRunning = false;
		}
	}
	
	public void run(){
		sc = null;
		PrintWriter out = null;
		try {
			sc = new Scanner(soket.getInputStream());
			out = new PrintWriter( soket.getOutputStream(), true );
			out.println("Hello");
			
			response = sc.nextLine();
		} catch (IOException e) {
			response = e.toString();
		}catch (NoSuchElementException e) {
			response = "No Line Found, Socket closed!";
		}
		try {
			sema.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		conectionAquiered = true;
		sema.release();
		
		Thread th = new Thread(){
			public void run(){
				read();
				debug.Debug.println("* Com-Read Thread Terminated!", debug.Debug.ERROR);
				conectionAquiered = false;
			}
		};
		th.start();
		
		boolean ioe = true;
		while (threadIsRunning) {
			if(upstream.contains()){
				try {
					sema.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String s = upstream.out();
				if(s != null){
					out.println(s);
				}
				
				sema.release();
			}
			if(out.checkError() && ioe){
				ioe = false;
				debug.Debug.println("* OutputStream Error!", debug.Debug.ERROR);
				threadIsRunning = false;
			}
			if(soket.isClosed()){
				debug.Debug.println("* Connection to server closed!", debug.Debug.ERROR);
				threadIsRunning = false;
			}
		}
		debug.Debug.println("* Comunication Thread Terminated!", debug.Debug.ERROR);
		conectionAquiered = false;
	}
	
	private void read(){
		while(threadIsRunning){
			int i = 0;
			while(sc.hasNext()&& i<10){
				try {
					sema.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
				if(!threadIsRunning){
					sema.release();
					return;
				}
				downstream.in(sc.nextLine());
				sema.release();
			}
		}
	}
	
	public boolean conectionStatus(){
		try {
			sema.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean b = conectionAquiered;
		sema.release();
		return b;
	}
	public String getResponse(){
		try {
			sema.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String b = response;
		sema.release();
		return b;
	}
	
	public String[] recive(){
		try {
			sema.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int k = downstream.lenght();
		//Abfrage der laenge
		if(k > 30)
			debug.Debug.println("* Warning: High number of recived Messages: "+k, debug.Debug.WARN);
		
		String[] s = new String[k];
		for (int i = 0; i < k; i++) {
			s[i]=downstream.out();
		}
		sema.release();
		return s;
	}
	public void send(String s){
		try {
			sema.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		upstream.in(s);
		sema.release();
	}
	
	public void terminate(){
		try {
			sema.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadIsRunning = false;
		sema.release();
	}
	
	public boolean isRunning(){
		try {
			sema.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean b = threadIsRunning;
		sema.release();
		return b;
	}

}
