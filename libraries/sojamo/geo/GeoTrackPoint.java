package sojamo.geo;

public class GeoTrackPoint {
	public double latitude, longitude, altitude;
	public String time;
	public boolean isVisible;
	Object _myMetaData;
	
	public GeoTrackPoint() {
		isVisible = true;
	}

	public GeoTrackPoint(
	        String theLongitude,
	        String theLatitude) {
		setCoordinates(theLongitude, theLatitude);
		isVisible = true;
	}
	
	public GeoTrackPoint(
	        float theLongitude,
	        float theLatitude) {
		longitude = (double)theLongitude;
		latitude = (double)theLatitude;
		isVisible = true;
	}

	public void setVisible(
	        boolean theFlag) {
		isVisible = theFlag;
	}

	public void setCoordinates(
	        String theLongitude,
	        String theLatitude) {
		longitude = Double.parseDouble(theLongitude);
		latitude = Double.parseDouble(theLatitude);
	}

	public void setCoordinates(
	        double theLongitude,
	        double theLatitude) {
		longitude = (double) (theLongitude);
		latitude = (double) (theLatitude);
	}

	public void setTime(
	        String theTime) {
		time = convertTime(theTime);
	}

	public void setAltitude(
	        String theAltitude) {
		altitude = Double.parseDouble(theAltitude);
	}

	public String toString() {
		return longitude + "," + latitude + "," + altitude + "," + time;
	}

	public String convertTime(
	        String theTime) {
		theTime = theTime.replace('T', ' ');
		theTime = theTime.replace('-', ':');
		theTime = theTime.substring(0, theTime.length() - 1);
		return theTime;
	}
	
	
	public String time() {
		return time;
	}
	
	public void setMetaData(Object theMetaData) {
		_myMetaData = theMetaData;
	}
	
	public Object metaData() {
		return _myMetaData;
	}
	
}


