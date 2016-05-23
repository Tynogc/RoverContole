package coding;

import debug.Debug;

public class OperateAll {
	
	private static int count;
	private static int lastCount;
	
	public static final int CLASS_SYSTEM = 1;
	public static final int CLASS_MEMORY = 2;
	public static final int CLASS_ROVER = 3;
	public static final int CLASS_NAVIGATION = 4;
	
	public static final int NUMBER = 1000000;
	
	public static final String[] CLASSES = new String[]{
		"null", "system","memory","rover","navigation"
	};
	
	
	public static void process(StringTransmission s){
		int upClass = 0;
		
		if(s.getString().length() >= 2)
		if(s.getString().charAt(0) == '/' &&s.getString().charAt(1) == '/'){
			for (int i = 0; i < s.getString().length(); i++) {
				s.setIntAt(i, StringTransmission.MARK_COMM);
			}
			return;
		}
		
		count = 0;
		lastCount = 0;
		
		upClass = getUpClass(s.getString());
		for (int i = 0; i < count; i++) {
			s.setIntAt(i, StringTransmission.MARK_PATCH);
		}
		lastCount = count;
		if(count>=s.getString().length())return;
		if(s.getString().charAt(count) == ' '){
			switch (upClass) {
			case CLASS_ROVER:
				OperateRover.operateSpace(s, count+1);
				return;
				
			case CLASS_MEMORY:
				OperateMemory.operateSpace(s, count+1);
				return;

			default:
				endWithFalse(s);
				return;
			}
		}else if(s.getString().charAt(count) == '.'){
			switch (upClass) {
			case CLASS_ROVER:
				OperateRover.operatePoint(s, count+1);
				return;
				
			case CLASS_MEMORY:
				OperateMemory.operatePoint(s, count+1);
				return;

			default:
				endWithFalse(s);
				return;
			}
		}else{
			endWithFalse(s);
			return;
		}
	}
	
	private static int getUpClass(String s){
		for (int i = 0; i < CLASSES.length; i++) {
			if(s.length()<CLASSES[i].length())continue;
			if(s.substring(0, CLASSES[i].length()).compareTo(CLASSES[i])==0){
				count+=CLASSES[i].length();
				return i;
			}
		}
		return 0;
	}
	
	private static void endWithFalse(StringTransmission s){
		for (int i = count; i < s.getString().length(); i++) {
			if(s.getString().charAt(i) != '|')
			s.setIntAt(i, StringTransmission.MARK_ERROR);
		}
	}
	
	public static String[] divideBySpace(String s){
		int num = 1;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == ',')num++;
		}
		int kap = 0;
		String[] tr = new String[num];
		tr[0] = "";
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
				
			if(c == ','){
				kap++;
				if(kap>=num)break;
				tr[kap] = "";
			}else if(c != ')'){
				tr[kap] += c;
			}else{
				break;
			}
		}
		
		//System.out.println("");
		//System.out.print(num+" ");
		//for (int i = 0; i < tr.length; i++) {
		//	System.out.print(tr[i]+"_");
		//}
		
		
		return tr;
	}
	
	public static int operateInpoint(String s, StringTransmission d, int pos){
		int utp = StringTransmission.MARK_ERROR;
		int tr = -1;
		if(s == null){
			Debug.println("* Error OperateAll01: String is null", Debug.ERROR);
			return -1;
		}
		if(s.length() == 0) return -1;
		
		if(s.length()>= 2){
			if(s.charAt(0) == ' ') s = s.substring(1);
			if(s.charAt(s.length()-1) == ' ') s = s.substring(0,s.length()-2);
		}
		
		if(s.charAt(0) == '['){
			if(s.length()>=3){
				if(s.charAt(s.length()-1) == ']'){
					
					boolean b = true;
					try {
						tr = Integer.parseInt(s.substring(1, s.length()-1));
					} catch (NumberFormatException e) {
						b = false;
					} catch (IllegalArgumentException e) {
						b = false;
					}
					if(b){
						utp = StringTransmission.MARK_VALUE;
					}
				}
			}
		}else{
			for (int i = 0; i < OperateMemory.values.length; i++) {
				if(s.compareTo(OperateMemory.values[i])==0){
					for (int j = 0; j < s.length(); j++) {
						d.setIntAt(pos+j, StringTransmission.MARK_VALUE);
					}
					return OperateMemory.valInts[i];
				}
			}
			
			boolean b = true;
			double tb = 0.0;
			try {
				tb = Double.parseDouble(s);
			} catch (NumberFormatException e) {
				b = false;
			} catch (IllegalArgumentException e) {
				b = false;
			}
			if(b){
				utp = StringTransmission.MARK_NORMAL;
			}
			tr = (int)(tb*100)+NUMBER;
		}
		
		for (int i = 0; i < s.length(); i++) {
			d.setIntAt(pos+i, utp);
		}
		return tr;
	}
	
	public static String operateNumberToCompiledString(int c){
		if(c>= NUMBER){
			return "d"+(c-NUMBER);
		}
		return "r"+c;
	}

}
