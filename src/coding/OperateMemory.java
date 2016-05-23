package coding;

public class OperateMemory {
	
	public static String[] values;
	public static int[] valInts;
	
	static{
		values = new String[]{};
		valInts = new int[]{};
	}
	
	public static void operatePoint(StringTransmission s, int pos){
		
	}
	
	public static void operateSpace(StringTransmission s, int pos){
		boolean step = false;
		int subPos = pos;
		for (int i = pos; i < s.getString().length(); i++) {
			if(s.getString().charAt(i) == '='){
				step = true;
				subPos = i;
				break;
			}
		}
		if(step){
			setValue(s, pos, subPos);
			return;
		}
	}
	
	private static void setValue(StringTransmission s, int x1, int x2){
		String value = "";
		
		for (int i = x1; i < x2; i++) {
			char c = s.getString().charAt(i);
			if(c!=' ')value+=c;
		}
		
		int posInArr = -1;
		
		for (int i = 0; i < values.length; i++) {
			if(values[i].compareTo(value) == 0){
				posInArr = i;
				break;
			}
		}
		
		if(posInArr == -1){
			int[] ai = new int[valInts.length+1];
			String[] as = new String[valInts.length+1];
			for (int i = 0; i < valInts.length; i++) {
				ai[i]=valInts[i];
				as[i]=values[i];
			}
			as[valInts.length] = value;
			posInArr = valInts.length;
			
			valInts = ai;
			values = as;
		}
		
		for (int i = x1; i < x2; i++) {
			s.setIntAt(i, StringTransmission.MARK_VALUE);
		}
		
		for (int i = x1; i < s.getString().length(); i++) {
			char c = s.getString().charAt(i);
			if(c == '[')x1 = i+1;
			if(c == ']')x2 = i;
		}
		int toPars = StringTransmission.MARK_NORMAL;
		int parsed = 0;
		String pp = "";
		for (int i = x1; i < x2; i++) {
			pp+=s.getString().charAt(i);
		}
		try {
			parsed = Integer.parseInt(pp);
		} catch (NumberFormatException e) {
			toPars = StringTransmission.MARK_ERROR;
		}catch (IllegalArgumentException e) {
			toPars = StringTransmission.MARK_ERROR;
		}
		
		valInts[posInArr] = parsed;
		
		for (int i = x1; i <= x2; i++) {
			s.setIntAt(i, toPars);
		}
	}

}
