package coding;

import debug.Debug;

public class OperateRover {
	
	public static final int ROVER = 653;
	
	public static final int MOVE = 1;
	public static final int SET_POS = 2;
	public static final int TURN = 3;
	public static final int TURN_TO = 4;
	public static final int STOP = 5;
	
	public static final String[] OP_POINT = new String[]{
		"null","move","setPos","turn","turnTo","stop"
	};
	
	public static void operatePoint(StringTransmission s, int pos){
		String str = s.getString().substring(pos);
		int type = 0;
		for (int i = 0; i < OP_POINT.length; i++) {
			if(str.length()<OP_POINT[i].length()+1)continue;
			if(str.substring(0, OP_POINT[i].length()+1).compareTo(OP_POINT[i]+"(")==0){
				type = i;
				pos+=OP_POINT[i].length();
				break;
			}
		}
		
		switch (type) {
		case MOVE:
			s.setCompiled("RM ");
			
			break;

		default:
			endWithFalse(s, pos);
			return;
		}
		pos++;
		
		if(s.getString().length()<=pos)return;
		str = s.getString().substring(pos);
		
		//if(str.charAt(str.length()-1) == ')')return;
		
		String[] at = OperateAll.divideBySpace(str);
		
		for (int i = 0; i < at.length; i++) {
			int k = OperateAll.operateInpoint(at[i], s, pos);
			pos+=at[i].length()+1;
			s.setCompiled(s.getCompiled()+OperateAll.operateNumberToCompiledString(k)+" ");
		}
		
		if(str.length()<=1){
			endWithFalse(s, s.getString().length()-2);
		}else if(str.charAt(str.length()-1)==')'||str.charAt(str.length()-2)==')'){
			//TODO set OK
			return;
		}else{
			endWithFalse(s, s.getString().length()-2);
		}
	}
	
	public static void operateSpace(StringTransmission s, int pos){
		
	}
	
	private static void endWithFalse(StringTransmission s, int count){
		for (int i = count; i < s.getString().length(); i++) {
			if(s.getString().charAt(i) != '|')
			s.setIntAt(i, StringTransmission.MARK_ERROR);
		}
	}
}
