package sojamo.geo;

import java.util.Vector;

public abstract class GeoUnit {
	protected Object _myMetaData;
	public GeoTrackPoint centerPoint;
	protected GeoBox _myBoundingBox;
	protected Vector trackPoints;
	
	GeoUnit() {
		centerPoint = new GeoTrackPoint();
		_myBoundingBox = new GeoBox();
	}
	public void setMetaData(Object theMetaData) {
		_myMetaData = theMetaData;
	}
	
	public Object metaData() {
		return _myMetaData;
	}
	
	public GeoTrackPoint centerPoint() {
		return centerPoint;
	}
	
	public GeoBox boundingBox() {
		return _myBoundingBox;
	}
}
