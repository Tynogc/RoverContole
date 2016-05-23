package process;

public class GPS {
	
	public int[] lat;
	public int[] lon;
	public int alt;
	
	public static final int DEGREE_MINUT_SECOND = 15678;//Secods are *100
	public static final int DEGREE_MINUT = 133458; //Minuts are *10 000
	public static final int DEGREE = 24678; //Degrees are *1 000 000
	
	private int type;
	
	public static final int RADIUS = 6371;
	
	public GPS(int TYPE){
		type = TYPE;
		switch (type) {
		case DEGREE_MINUT_SECOND:
			lat = new int[3];
			lon = new int[3];
			break;
		case DEGREE_MINUT:
			lat = new int[2];
			lon = new int[2];
			break;
		case DEGREE:
			lat = new int[1];
			lon = new int[1];
			break;
			
		default:
			debug.Debug.println("* ERROR GPS02: Type is not valid!", debug.Debug.ERROR);
			type = DEGREE_MINUT_SECOND;
			lat = new int[3];
			lon = new int[3];
			break;
		}
	}
	
	public GPS(int[] la, int[] lo, int TYPE){
		
		int q = 3;
		
		type = TYPE;
		switch (type) {
		case DEGREE_MINUT_SECOND:
			q= 3;
			break;
		case DEGREE_MINUT:
			q = 2;
			break;
		case DEGREE:
			q = 1;
			break;
			
		default:
			debug.Debug.println("* ERROR GPS02: Type is not valid!", debug.Debug.ERROR);
			type = DEGREE_MINUT_SECOND;
			lat = new int[3];
			lon = new int[3];
			return;
		}
		
		if(la.length != q){
			debug.Debug.println("* Error GPS01a: Lat is not the correct Format!",debug.Debug.ERROR);
			debug.Debug.println("Lenght should be "+q+", but is "+la.length,debug.Debug.SUBERR);
			lat = new int[q];
			lon = new int[q];
			return;
		}
		if(lo.length != q){
			debug.Debug.println("* Error GPS01b: Lon is not the correct Format!",debug.Debug.ERROR);
			debug.Debug.println("Lenght should be "+q+", but is "+lo.length,debug.Debug.SUBERR);
			lat = new int[q];
			lon = new int[q];
			return;
		}
		lat = la;
		lon = lo;
	}
	
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		convertTo(type);
	}
	
	private void convertTo(int t){
		if(t == type)return;
		
		if(type == DEGREE_MINUT_SECOND){
			//Konvertieren zu DEGREE_MINUT aufwaerts
			int[] newLat = new int[2];
			int[] newLon = new int[2];
			newLat[0] = lat[0];
			newLon[0] = lon[0];
			double d = lat[2];
			d/=6;
			d*=10;
			newLat[1] = lat[1]*10000+(int)(d);
			d = lon[2];
			d/=6;
			d*=10;
			newLon[1] = lon[1]*10000+(int)(d);
			lat = newLat;
			lon = newLon;
			type = DEGREE_MINUT;
			if(t == DEGREE){
				convertTo(DEGREE);
			}
		}else if(type == DEGREE_MINUT){
			if(t == DEGREE){
				//Konvertieren zu DEGREE aufwaerts
				int[] newLat = new int[1];
				int[] newLon = new int[1];
				double d = lat[1];
				d/=6;
				d*=10;
				newLat[0] = lat[0]*1000000+(int)(d);
				d = lon[1];
				d/=6;
				d*=10;
				newLon[0] = lon[0]*1000000+(int)(d);
				lat = newLat;
				lon = newLon;
				type = DEGREE;
			}else if(t == DEGREE_MINUT_SECOND){
				//Konvertieren zu DEGREE_MINUT_SECOND abwaerts
				int[] newLat = new int[3];
				int[] newLon = new int[3];
				newLat[0] = lat[0];
				newLon[0] = lon[0];
				newLat[1] = lat[1]/10000;
				newLon[1] = lon[1]/10000;
				double d = lat[1]%10000;
				d/=10;
				d*=6;
				newLat[2] = (int)(d);
				d = lon[1]%10000;
				d/=10;
				d*=6;
				newLon[2] = (int)(d);
				lat = newLat;
				lon = newLon;
				type = DEGREE_MINUT_SECOND;
			}
		}
	}

	public String toString(){
		String s = "";
		switch (type) {
		case DEGREE_MINUT_SECOND:
			s = "Lat: "+numDum(lat[0], 100)+"°"+numDum(lat[1], 100)+"'"+numDum(lat[2]/100, 100)+","+numDum(lat[2], 100)+"''";
			s += "\n";
			s += "Lon: "+numDum(lon[0], 1000)+"°"+numDum(lon[1], 100)+"'"+numDum(lon[2]/100, 100)+","+numDum(lon[2], 100)+"''";
			break;
		case DEGREE_MINUT:
			s = "Lat: "+numDum(lat[0], 100)+"°"+numDum(lat[1]/10000, 100)+","+numDum(lat[1], 10000)+"'";
			s += "\n";
			s += "Lon: "+numDum(lon[0], 1000)+"°"+numDum(lon[1]/10000, 100)+","+numDum(lon[1], 10000)+"'";
			break;
		case DEGREE:
			s = "Lat: "+numDum(lat[0]/1000000, 100)+","+numDum(lat[0], 1000000)+"°";
			s += "\n";
			s += "Lon: "+numDum(lon[0]/1000000, 1000)+","+numDum(lon[0], 1000000)+"°";
			break;

		default:
			break;
		}
		
		return s;
	}
	
	private String numDum(int i, int ammount){
		String s = "";
		do{
			i=i%ammount;
			ammount /=10;
			s+= ""+(i/ammount);
		}while(ammount > 1);
		
		return s;
	}
	
	private static double distanceAngle(double lat1, double lon1, double lat2, double lon2) {
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		lon1 = Math.toRadians(lon1);
		lon2 = Math.toRadians(lon2);
		double w = (Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1));
		return Math.acos(w);
	}
	
	public static double distanceBetweenPointsOUT(GPS a1, GPS a2){
		GPS g1 = new GPS(a1.lat, a1.lon, a1.type);
		GPS g2 = new GPS(a2.lat, a2.lon, a2.type);
		g1.setType(DEGREE);
		g2.setType(DEGREE);
		return distanceInKm((double)g1.lat[0]/1000000, (double)g1.lon[0]/1000000,
				(double)g2.lat[0]/1000000,(double)g2.lon[0]/1000000);
	}
	
	public static double distanceBetweenPoints(GPS a1, GPS a2){
		GPS g1 = new GPS(a1.lat, a1.lon, a1.type);
		GPS g2 = new GPS(a2.lat, a2.lon, a2.type);
		g1.setType(DEGREE);
		g2.setType(DEGREE);
		return distanceAngle((double)g1.lat[0]/1000000, (double)g1.lon[0]/1000000,
				(double)g2.lat[0]/1000000,(double)g2.lon[0]/1000000)*RADIUS;
	}

	private static double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
		int radius = 6371;	 
		double lat = Math.toRadians(lat2 - lat1);
		double lon = Math.toRadians(lon2- lon1);
		double a = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lon / 2) * Math.sin(lon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = radius * c;
		return Math.abs(d);
	}
	
	public static double angleBetween(GPS a1, GPS a2){
		GPS g1 = new GPS(a1.lat, a1.lon, a1.type);
		GPS g2 = new GPS(a2.lat, a2.lon, a2.type);
		g1.setType(DEGREE);
		g2.setType(DEGREE);
		return angBW((double)g1.lat[0]/1000000, (double)g1.lon[0]/1000000,
				(double)g2.lat[0]/1000000,(double)g2.lon[0]/1000000);
	}
	public static double angleBetweenComplex(GPS a1, GPS a2){
		GPS g1 = new GPS(a1.lat, a1.lon, a1.type);
		GPS g2 = new GPS(a2.lat, a2.lon, a2.type);
		g1.setType(DEGREE);
		g2.setType(DEGREE);
		return angBWCPX((double)g1.lat[0]/1000000, (double)g1.lon[0]/1000000,
				(double)g2.lat[0]/1000000,(double)g2.lon[0]/1000000);
	}
	
	private static double angBWCPX(double lat1, double lon1, double lat2, double lon2){
		
		double c = distanceAngle(lat1, lon1, lat2, lon2);
		//double d = radius * c;
		//double e = Math.atan(distanceInKm(lat1, lon1, lat2, lon2)/6371);
		double aLat = Math.toRadians(lat1);
		double bLat = Math.toRadians(lat2);
		double w = (Math.sin(bLat)-Math.sin(aLat)*Math.cos(c))/Math.abs(Math.cos(aLat)*Math.sin(c));
		
		return Math.toDegrees(Math.acos(w));
	}
	
	private static double angBW(double lat1, double lon1, double lat2, double lon2){
		
		double c1 = distanceAngle(lat1, lon1, lat2, lon1);
		double c2 = distanceAngle(lat1, lon1, lat2, lon2);
		if(c2 < 0.00001)return 0.0;
		
		return Math.toDegrees(Math.cos(c1/c2));
	}

}
