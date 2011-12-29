package sojamo.geo;

import java.util.Vector;

public class GeoTrackSegment extends GeoUnit {
	
	public GeoTrackSegment() {
		super();
		trackPoints = new Vector();
	}
	
	public GeoTrackPoint add() {
		return add(new GeoTrackPoint());
	}
	
	public GeoTrackPoint add(
	        GeoTrackPoint theTrackPoint) {
		trackPoints.add(theTrackPoint);
		return theTrackPoint;
	}
	
	
	public GeoTrackPoint trackpoint(
	        int theIndex) {
		return (GeoTrackPoint)trackPoints.get(theIndex);
	}

	public int size() {
		return trackPoints.size();
	}

}
