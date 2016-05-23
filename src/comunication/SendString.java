package comunication;

public class SendString {

	public SendString next;
	
	public long time;
	public String text;
	
	public SendString(String s, long t){
		text = s;
		time = t;
	}
	
	public void add(SendString s){
		if(next == null)next=s;
		else next.add(s);
	}
	
	public SendString remove(String s){
		if(s.compareToIgnoreCase(text) == 0){
			return next;
		}
		if(next == null){
			debug.Debug.println("* WARN SendString01: Tryed to remove non-Existing Element: "+s, debug.Debug.WARN);
			return this;
		}
		next = next.remove(s);
		return this;
	}
}
