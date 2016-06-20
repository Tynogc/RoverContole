package comunication;


public class PWCoding {
	
	private char[] emptyList;
	
	private final char[] listA =
			new char[]{'a','b','c','d','k','t','f','g','h','i','j','e','l','m','n','o','p','q',
			'r','s','u','v','w','x','z','y','1','0','2','3','5','4','6','7','8','9','+','-'};
	
	private final char[] listB =
		new char[]{'n','0','2','3','a','b','c','d','k','t','f','g','h','i','j','e','l','q',
		'r','v','w','x','z','y','1','5','4','6','s','u','7','8','m','o','p','9','+','-'};
	
	private final char[] listC =
		new char[]{'l','m','n','o','a','f','g','5','4','w','6','9','+','-','h','i','j','e','p','q',
		'b','c','d','k','t','r','7','8','s','u','x','z','v','y','1','0','2','3'};
	
	private final char[] listD =
			new char[]{'l','d','k','i','t','r','m','n','o','+','h','j','e','p','q',
			'b','c','s','u','v','4','w','6','7','x','a','f','g','5','8','9','z','y','1','-','0','2','3'};
	
	
	private int[][] numRotors;
	
	private static final int[][] theNumRotors = new int[][]{
		/*{1,3,-6,44,2,15,3,2,-4,10},
		{-14,29,-33,2,8,66,1,15,-58,-2},
		{84,8,32,-84,72,-36,9,14,-55,-8},
		{74,-8,-33,-7,15,-4,1,-15,9,75},
		{2,-33,47,68,-57,3,22,1,5,44},
		{24,-67,-5,-92,62,7,-6,87,33,-7},
		{58,-7,-31,+3,-8,6,-8,45,71,5},
		{5,39,56,4,-4,82,-65,-7,3,1}*/
		{9,0,6,4,3,5,12,2,1,14},
		{1,3,11,13,7,5,0,2,12,10},
		{6,9,3,12,0,7,11,1,8,14},
		{4,14,12,3,10,7,6,9,8,5},
		{10,4,7,9,11,2,8,5,0,14},
		{1,3,13,11,5,9,0,4,12,8},
		{14,10,9,13,11,4,0,2,7,3},
		{9,5,14,13,12,0,8,11,6,10},
		{8,9,6,3,0,4,14,5,1,7},
		{3,12,1,10,7,13,2,4,8,9},
		{11,7,4,1,3,5,10,8,6,12},
		{11,2,0,7,8,14,10,12,6,3},
		{3,5,12,11,7,1,0,8,13,9}
	}; 
	
	private int k1 = 1783;
	private int k2 = 223;
	private int keyTPM = 0;
	
	public int indicator = 0;
	public String pws = "";
	
	public PWCoding(){
		setList(0);
		numRotors = new int[4][10];
		numRotors[0] = theNumRotors[1];
		numRotors[1] = theNumRotors[2];
		numRotors[2] = theNumRotors[0];
		numRotors[3] = theNumRotors[3];
	}
	
	public void setList(int i){
		char[] list = listA;
		if(i%4 == 0)list = listA;
		else if(i%4 == 1)list = listB;
		else if(i%4 == 2)list = listC;
		else if(i%4 == 3)list = listD;
		else System.err.println("ERROR");
		emptyList = new char[list.length];
		for (int j = 0; j < list.length; j++) {
			emptyList[j] = list[j];
		}
		
		i = i/4;
		
		if(i == 0)return;
		if(i == 1){
			for (int j = 0; j < emptyList.length-4; j+=4) {
				char c = emptyList[j];
				emptyList[j] = emptyList[j+3];
				emptyList[j+3] = c;
			}
			return;
		}
		if(i == 2){
			for (int j = 0; j < emptyList.length-4; j+=4) {
				char c = emptyList[j+1];
				emptyList[j+1] = emptyList[j+2];
				emptyList[j+2] = c;
			}
			return;
		}
		if(i == 3){
			for (int j = 0; j < emptyList.length-2; j+=2) {
				char c = emptyList[j+1];
				emptyList[j+1] = emptyList[j];
				emptyList[j] = c;
			}
			return;
		}
	}
	
	public String code(String s){
		int key = keyTPM;
		String ret = "";
		for (int i = 0; i < s.length(); i++) {
			int mod = findInteger(s.charAt(i));
			if(mod == 100)mod = findInteger('-');
			ret += findLetter(mod+getANumberFromeAKey(key));
			if(mod%2 == 0){
				key += k1;
			}else{
				key += k2;
			}
			if(key > 10000)key-= 10000;
		}
		return ret;
	}
	
	public String decode(String s){
		int key = keyTPM;
		String ret = "";
		for (int i = 0; i <s.length(); i++) {
			int mod = findInteger(s.charAt(i));
			mod-=getANumberFromeAKey(key);
			ret += findLetter(mod);
			if(mod%2 == 0){
				key += k1;
			}else{
				key += k2;
			}
			if(key > 10000)key-= 10000;
		}
		return ret;
	}
	
	private int findInteger(char a){
		for (int i = 0; i < emptyList.length; i++) {
			if(a == emptyList[i]) return i;
		}
		return 100;
	}
	
	private char findLetter(int a){
		while (a<0) {
			a+=emptyList.length;
		}
		return emptyList[a%emptyList.length];
	}
	
	public void generateCode(char[] c, boolean showPWS){
		int tr = 1;
		k1 = 1;
		k2 = 1;
		
		pws = "";
		
		boolean b1 = false;
		boolean b2 = false;
		boolean b3 = false;
		boolean b4 = false;
		boolean b5 = false;
		boolean b6 = false;
		
		for (int i = 0; i < c.length; i++) {
			if(i%2 == 0){
				tr += (int)c[i];
			}
			if(i%3 == 1){
				k1 += (int)c[i]-50;
				if((int)c[i]<60)b1 = true;
				else if((int)c[i]<97)b2 = true;
				else b5 = true;
			}else{
				k2 += (int)c[i]-50;
				if((int)c[i]<60)b3 = true;
				else if((int)c[i]<97)b4 = true;
				else b6 = true;
			}
		}
		indicator = 0;
		if(b1)indicator++;
		if(b2)indicator++;
		if(b3)indicator++;
		if(b4)indicator++;
		if(b5)indicator++;
		if(b6 && indicator < 5)indicator++;
		//indicator = 5;
		//if(c.length < 14) indicator = 4;
		//if(c.length < 10) indicator = 3;
		if(c.length < 8) indicator = 2;
		if(c.length < 6) indicator = 1;
		if(c.length < 4) indicator = 0;
		
		k1 = k1%99;
		k2 = k2%96;
		
		if(k1 < 0){
			k1 = -k1;
		}
		if(k2 < 0){
			k2 = -k2;
		}
		if(k1 == 1){
			if(indicator> 1)indicator = 1;
		}
		if(k2 == 1){
			if(indicator> 1)indicator = 1;
		}
		if(k1 == k2){
			k1 += 11;
			if(indicator!= 0)indicator = 1;
			if(showPWS)pws+=" Avoid doubled leters ";
		}
		
		if(c.length < 4){
			numRotors[0] = theNumRotors[0];
			numRotors[1] = theNumRotors[1];
			numRotors[2] = theNumRotors[0];
			numRotors[3] = theNumRotors[1];
		}else{
			int i0 = (int)(c[0])%13;
			int i1 = (int)(c[1])%13;
			int i2 = (int)(c[2])%13;
			int i3 = (int)(c[3])%13;
			numRotors[0] = theNumRotors[i0];
			numRotors[1] = theNumRotors[i1];
			numRotors[2] = theNumRotors[i2];
			numRotors[3] = theNumRotors[i3];
			int weak = 0;
			if(i0 == i1)weak++;
			if(i0 == i2)weak++;
			if(i0 == i3)weak++;
			if(i2 == i1)weak++;
			if(i3 == i1)weak++;
			if(weak >= 2 && indicator > 1){
				indicator = 1;
			}
			if(showPWS){
				int ppt = getRandom(10);
				pws += "Wheels "+(i0+ppt)%10+" "+(i1+ppt)%10+" "+(i2+ppt)%10+" "+(i3+ppt)%10;
			}
		}
		if(!showPWS)pws = "EMPTY";
		
		keyTPM = tr;
	}
	
	public void setK1(int i){
		k1 = i;
	}
	public void setK2(int i){
		k2 = i;
	}
	public void setKey(int i){
		keyTPM = i;
	}
	public void setVanish(){
		k1 = 4733;
		k2 = 4722;
		keyTPM = 2222;
		numRotors[0] = theNumRotors[1];
		numRotors[1] = theNumRotors[2];
		numRotors[2] = theNumRotors[0];
		numRotors[3] = theNumRotors[3];
		pws = "";
		setList(0);
	}
	public void setVanishOnlyForTest(){
		k1 = 4733;
		k2 = 4722;
		keyTPM = 2222;
	}
	
	private int getANumberFromeAKey(int key){
		int t = key/22;
		t+=numRotors[0][key%10];
		t+=numRotors[1][(key/10)%10];
		t+=numRotors[2][(key/100)%10];
		t+=numRotors[3][(key/1000)%10];
		if(t<0)return -t;
		return t;
	}
	
	private static final int forStringLenght = 7;
	
	public String generateRandomeForestring(){
		char c;
		String s = "";
		for (int i = 0; i < forStringLenght; i++) {
			c = emptyList[getRandom(emptyList.length)];
			s+=c;
		}
		
		return s;
	}
	
	public String deletRandomeForestring(String s){
		int at = 0;
		if(s.length()<=forStringLenght+2)return s;
		return s.substring(forStringLenght);
	}
	
	public static void generateAWheel(){
		String s = "{";
		int[] def = new int[10];
		for (int i = 0; i < def.length; i++) {
			boolean done = false;
			while(!done){
				def[i] = getRandom(15);
				done = true;
				for (int j = 0; j < i; j++) {
					if(def[i]==def[j])done = false;
				}
			}
			
			s += def[i]+",";
		}
		s = s.substring(0,s.length()-1)+"},";
		System.out.println(s);
	}
	
	public static int getRandom(int max){
		int a = (int)(Math.random()*max)+(int)(System.nanoTime()%max);
		return a%max;
	}
	
	public boolean testCurrentList(){
		for (int i = 0; i < listA.length; i++) {
			boolean found = false;
			for (int j = 0; j < emptyList.length; j++) {
				if(emptyList[j] == listA[i]){
					if(found)return false;
					else found = true;
				}
			}
			if(!found)return false;
		}
		return true;
	}

}
