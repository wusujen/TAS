package sojamo.geo;

import java.util.Vector;

public class GeoTrack extends GeoUnit {
	
	Vector trackSegments;
	
	GeoTrack() {
		super();
		trackSegments = new Vector();
		trackPoints = new Vector();
	}

	public GeoTrackSegment trackSegment(
	        int theIndex) {
		return ((GeoTrackSegment) trackSegments.get(theIndex));
	}

	public Vector trackSegments() {
		return trackSegments;
	}
	
	public GeoTrackPoint trackPoint(int theIndex) {
		return (GeoTrackPoint)trackPoints.get(theIndex);
	}
	
	public Vector trackPoints() {
		return trackPoints;
	}
	
	public GeoTrackSegment addTrackSegment() {
		return addTrackSegment(new GeoTrackSegment());
	}
	
	public GeoTrackSegment addTrackSegment(
	        GeoTrackSegment theTrackSegment) {
		trackSegments.add(theTrackSegment);
		return theTrackSegment;
	}

	public int size() {
		return trackSegments.size();
	}
	
}
