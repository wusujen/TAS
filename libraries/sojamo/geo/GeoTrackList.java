package sojamo.geo;

import java.util.Vector;

public class GeoTrackList extends GeoUnit {

	Vector tracks;
	
	public GeoTrackList() {
		super();
		tracks = new Vector();
	}

	public GeoTrack track(
	        int theIndex) {
		return ((GeoTrack) tracks.get(theIndex));
	}

	public GeoTrack addTrack() {
		return addTrack(new GeoTrack());
	}
	
	public GeoTrack addTrack(
	        GeoTrack theTrack) {
		tracks.add(theTrack);
		return theTrack;
	}

	public int size() {
		return tracks.size();
	}
	
}
