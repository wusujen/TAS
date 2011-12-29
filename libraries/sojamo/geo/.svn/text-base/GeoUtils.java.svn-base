package sojamo.geo;

import java.util.GregorianCalendar;

public class GeoUtils {
	//
	// Vincenty formula for distance between two Latitude/Longitude points
	// http://www.movable-type.co.uk/scripts/latlong-vincenty.html
	// 
	// Extension to Google's Web Toolkit (GWT) framework 
	// http://sourceforge.net/projects/gwt
	//
	// 
	/**
	 * 
	 * @param theLocation
	 *            String
	 * @return float
	 */
	public static float getLocationFromString(
	        String theLocation) {
		return Float.parseFloat(theLocation);
	}

	// GeoTrackList doesnt have a centerPoint yet, implement!
	public static void getBoundingBox(
	        GeoUnit theUnit) {
		for (int i = 0; i < theUnit.trackPoints.size(); i++) {
			checkBoundingBox(theUnit, ((GeoTrackPoint) theUnit.trackPoints.get(i)).longitude,
			        ((GeoTrackPoint) theUnit.trackPoints.get(i)).latitude);
		}
		theUnit.boundingBox().width = (float) Math.abs(theUnit.boundingBox().x2 - theUnit.boundingBox().x1);
		theUnit.boundingBox().height = (float) Math.abs(theUnit.boundingBox().y2 - theUnit.boundingBox().y1);
		theUnit.centerPoint = new GeoTrackPoint();
		theUnit.centerPoint.setCoordinates(theUnit.boundingBox().x1
		        + Math.abs(theUnit.boundingBox().x2 - theUnit.boundingBox().x1) / 2, theUnit.boundingBox().y1
		        + Math.abs(theUnit.boundingBox().y2 - theUnit.boundingBox().y1) / 2);
	}

	public static void checkBoundingBox(
	        GeoUnit theUnit,
	        double theLon,
	        double theLat) {
		if (theUnit.boundingBox().isInitialized == false) {
			theUnit.boundingBox().isInitialized = true;
			theUnit.boundingBox().x1 = theLon;
			theUnit.boundingBox().x2 = theLon;
			theUnit.boundingBox().y1 = theLat;
			theUnit.boundingBox().y2 = theLat;
		}
		if (theLon < theUnit.boundingBox().x1) {
			theUnit.boundingBox().x1 = theLon;
		} else if (theLon > theUnit.boundingBox().x2) {
			theUnit.boundingBox().x2 = theLon;
		}

		if (theLat < theUnit.boundingBox().y1) {
			theUnit.boundingBox().y1 = theLat;
		} else if (theLat > theUnit.boundingBox().y2) {
			theUnit.boundingBox().y2 = theLat;
		}
	}

	/**
	 * 
	 * @param theTime
	 *            String
	 * @return GregorianCalendar
	 */
	public static GregorianCalendar getTimeFromString(
	        String theTime) {
		GregorianCalendar myTime = new GregorianCalendar();
		int myHour = Integer.parseInt(theTime.substring(0, 1));
		int myMinute = Integer.parseInt(theTime.substring(2, 3));
		int mySecond = Integer.parseInt(theTime.substring(4, 5));
		myTime.set(GregorianCalendar.HOUR, myHour);
		myTime.set(GregorianCalendar.MINUTE, myMinute);
		myTime.set(GregorianCalendar.SECOND, mySecond);
		return myTime;
	}

	/**
	 * 
	 * @param myTime
	 *            GregorianCalendar
	 * @return String
	 */
	public static String getStringFromTime(
	        GregorianCalendar myTime) {
		String myYear = String.valueOf(myTime.get(GregorianCalendar.YEAR));
		String myMonth = String.valueOf(myTime.get(GregorianCalendar.MONTH));
		String myDate = String.valueOf(myTime.get(GregorianCalendar.DATE));
		String myHour = String.valueOf(myTime.get(GregorianCalendar.HOUR_OF_DAY));
		String myMinute = String.valueOf(myTime.get(GregorianCalendar.MINUTE));
		String mySecond = String.valueOf(myTime.get(GregorianCalendar.SECOND));
		return myYear + ":" + myMonth + ":" + myDate + ":" + myHour + ":" + myMinute + ":" + mySecond;
	}

	public static float degreeTimeToDegreeDecimal(
	        String theDegreeTime) {
		float myDecimal = 0;
		int myPos = theDegreeTime.indexOf('.');
		/* if we find a dot in theDegreeTime, then parse theDegreeTime */
		if (myPos > -1) {
			/* parse degree. first myPos-2 digits */
			float myDegree = Float.parseFloat(theDegreeTime.substring(0, myPos - 2));
			/* parse minute.2 digits before the dot */
			float myMinute = Float.parseFloat(theDegreeTime.substring(myPos - 2, myPos));
			/* check for seconds behind the dot */
			myPos++;
			float mySecond = (Float.parseFloat(theDegreeTime.substring(myPos, theDegreeTime.length()))) * 60;
			mySecond /= Math.pow(10, theDegreeTime.length() - myPos);
			/* add degree, minutes, and seconds to get the decimal value */
			myDecimal = myDegree + (myMinute / 60) + (mySecond / 3600);
			// println(int(myDegree)+"° "+int(myMinute)+"' "+mySecond+"'' =
			// "+myDecimal);
		}
		return myDecimal;
	}

	/**
	 * convert gps-nmea position-values from degreeTime to degreeDecimal.
	 * 
	 * reference http://home.debitel.net/user/geocaching/navigation.shtml <br />
	 * a) 51,2345¡ sprich 51,2345 Grad <br />
	 * b) 51¡14,07' sprich 51 Grad, 14,07 Minuten <br />
	 * c) 51¡ 14' 4,2" sprich 51 Grad, 14 Minuten, 4,2 Sekunden dezimalgrad N
	 * 50,02527¡, E 008,22217¡ gradminuten N 50¡ 01' 31", E 008¡ 13' 20"
	 * gradminuten N 50¡ 01,51', E 008¡ 13,33'
	 * 
	 */

	public static float dms2degree(
	        String theDegree,
	        String theMinute,
	        String theSecond) {
		/* degree,minutes,seconds to degree */
		float myDegree = (Float.valueOf(theDegree)).floatValue();
		float myMinute = (Float.valueOf(theMinute)).floatValue();
		float mySecond = (Float.valueOf(theSecond)).floatValue();
		if (myMinute >= 60.0) {
			return -9999.9899999999998f;
		}

		if (mySecond >= 60) {
			return -9999.9899999999998f;
		} else {
			myMinute /= (float) (60.0);
			mySecond /= (float) (3600.0);
			return myDegree + myMinute + mySecond;
		}
	}

	public static float dm2degree(
	        String theDegreeMinute) {
		/* degree, decimalminute to degree */
		String[] mydms = dm2dms(theDegreeMinute);
		return dms2degree(mydms[0], mydms[1], mydms[2]);
	}

	public static String[] dm2dms(
	        String theDegreeMinute) {
		/* degree,decimalminute to degree,minutes,seconds */
		int myPos = theDegreeMinute.indexOf('.');
		/* if we find a dot in theDegreeTime, then parse theDegreeTime */
		if (myPos > -1) {
			/* parse degree. first myPos-2 digits */
			float myDegree = Float.parseFloat(theDegreeMinute.substring(0, myPos - 2));
			/* parse minute.2 digits before the dot */
			float myMinute = Float.parseFloat(theDegreeMinute.substring(myPos - 2, myPos));
			/* check for seconds after the dot */
			myPos++;
			float mySecond = (Float.parseFloat(theDegreeMinute.substring(myPos, theDegreeMinute.length()))) * 60;
			mySecond /= Math.pow(10, theDegreeMinute.length() - myPos);
			/* return result as string array */
			return new String[] { "" + (int) myDegree, "" + (int) myMinute, "" + mySecond };
		}
		return new String[] { "0", "0", "0.0" };
	}

	public static float distance(
	        GeoTrackPoint theA,
	        GeoTrackPoint theB) {
		double R = 6371; // km
		double dLat = toRad(theB.latitude - theA.latitude);
		double dLon = toRad(theB.longitude - theA.longitude);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(toRad(theA.latitude))
		        * Math.cos(toRad(theB.latitude)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return (float) (R * c);
	}

	private static double toRad(
	        double theNumber) { // convert degrees to radians
		return theNumber * Math.PI / 180;
	}

}
