package sojamo.geo;

import java.util.Calendar;
import java.util.Vector;

public class GeoGPX {
	// http://tinygeocoder.com/create.php
    
	int trackIndex = 0;
	int tracksegmentIndex = 0;
	GeoXMLElement xml;
	int myScale = 6000;
	public Vector allTrackPoints = new Vector();
	public GeoTrackList tracklist;
	public GeoTrackPoint centerPoint;
	public float globalX, gloabalY, globalZ;
	public int lineCount;
	public float myOriginScale, myNextScale;
	public float translateScale;
	public float stretchX;
	public float stretchY;
	public boolean isPlaying;
	public boolean isLoop;
	public float cnt = 0;
	
	int len;
	
	boolean isFirst;
	
	double prevLon = 0;
	
	double prevLat = 0;
	
	public GeoGPX() {
		// myScale = (boundaryWidth>boundaryHeight) ?
		// (int)((float)width/abs(boundaryX2-
		// boundaryX1)) : (int)((float)height/abs(boundaryY2- boundaryY1));
		myOriginScale = myScale;
		myNextScale = myScale;
		translateScale = 1;
		isPlaying = true;
		tracklist = new GeoTrackList();
		isFirst = true;
	}
	
	public void load(String theXMLContent) {
		xml = new GeoXMLElement();
		xml.parseString(theXMLContent);
		int numOfTracks = xml.getChildren().size();

		try {
			for (int i = 0; i < numOfTracks; i++) {
				GeoXMLElement gpxElement = (GeoXMLElement)(xml.getChildren().get(i));
				if (gpxElement.getName().equals("trk")) {
					GeoTrack myTrack = new GeoTrack();
					tracklist.addTrack(myTrack);
					tracksegmentIndex = 0;
					for (int j = 0; j < gpxElement.getChildren().size(); j++) {
						if (((GeoXMLElement)gpxElement.getChildren().get(j)).getName().equals("trkseg")) {
							GeoXMLElement track = (GeoXMLElement)(gpxElement.getChildren().get(j));
							GeoTrackSegment mySegment = new GeoTrackSegment();
							tracklist.track(trackIndex).addTrackSegment(mySegment);
							for (int k = 0; k < track.getChildren().size(); k++) {
								GeoXMLElement trackpoint = ((GeoXMLElement)track.getChildren().get(k));
								GeoTrackPoint tp = new GeoTrackPoint();
								if (k == track.getChildren().size() - 1) {
									tp.setVisible(false);
								}
								
								double myLon = Double.parseDouble(trackpoint.getStringAttribute("lon"));
								double myLat = Double.parseDouble(trackpoint.getStringAttribute("lat")); 
								
								GeoUtils.checkBoundingBox(tracklist, myLon, myLat);
								GeoUtils.checkBoundingBox(myTrack, myLon, myLat);
								GeoUtils.checkBoundingBox(mySegment, myLon, myLat);
								
								tp.setCoordinates(myLon, myLat);
								
								if (isFirst == false) {
									double l1 = Math.abs(prevLon - tp.longitude);
									double l2 = Math.abs(prevLat - tp.latitude);
									if (l1 > 0.5 || l2 > 0.5) {
									}
								}

								prevLon = tp.longitude;
								prevLat = tp.latitude;

								for (int m = 0; m < trackpoint.getChildren().size(); m++) {
									if (((GeoXMLElement)trackpoint.getChildren().get(m)).getName().equals("ele")) {
										tp.setAltitude(((GeoXMLElement)trackpoint.getChildren().get(m)).getContent());
									}
									if (((GeoXMLElement)trackpoint.getChildren().get(m)).getName().equals("time")) {
										tp.setTime(((GeoXMLElement)trackpoint.getChildren().get(m)).getContent());
									}
								}
								tracklist.track(trackIndex).trackSegment(tracksegmentIndex).add(tp);
								tracklist.track(trackIndex).trackPoints().add(tp);
								allTrackPoints.add(tp);
							}
							mySegment.boundingBox().width = (float) Math.abs(mySegment.boundingBox().x2 - mySegment.boundingBox().x1);
							mySegment.boundingBox().height = (float) Math.abs(mySegment.boundingBox().y2 - mySegment.boundingBox().y1);
							mySegment.centerPoint = new GeoTrackPoint();
							mySegment.centerPoint.setCoordinates(mySegment.boundingBox().x1 + Math.abs(mySegment.boundingBox().x2 - mySegment.boundingBox().x1) / 2, mySegment.boundingBox().y1
							        + Math.abs(mySegment.boundingBox().y2 - mySegment.boundingBox().y1) / 2);
							tracksegmentIndex++;
						}
					}
					myTrack.boundingBox().width = (float) Math.abs(myTrack.boundingBox().x2 - myTrack.boundingBox().x1);
					myTrack.boundingBox().height = (float) Math.abs(myTrack.boundingBox().y2 - myTrack.boundingBox().y1);
					myTrack.centerPoint = new GeoTrackPoint();
					myTrack.centerPoint.setCoordinates(myTrack.boundingBox().x1 + Math.abs(myTrack.boundingBox().x2 - myTrack.boundingBox().x1) / 2, myTrack.boundingBox().y1
					        + Math.abs(myTrack.boundingBox().y2 - myTrack.boundingBox().y1) / 2);
					trackIndex++;
				}
			}
			tracklist.boundingBox().width = (float) Math.abs(tracklist.boundingBox().x2 - tracklist.boundingBox().x1);
			tracklist.boundingBox().height = (float) Math.abs(tracklist.boundingBox().y2 - tracklist.boundingBox().y1);
		} catch (Exception e) {
			System.out.println(e);
		}
		trackIndex = 0;
		tracksegmentIndex = 0;
		cnt = 0;
		tracklist.centerPoint = new GeoTrackPoint();
		tracklist.centerPoint.setCoordinates(tracklist.boundingBox().x1 + Math.abs(tracklist.boundingBox().x2 - tracklist.boundingBox().x1) / 2, tracklist.boundingBox().y1
		        + Math.abs(tracklist.boundingBox().y2 - tracklist.boundingBox().y1) / 2);
	}
	
	
	

	
	
	public GeoTrackPoint getTrackPoint(int theIndex) {
		return ((GeoTrackPoint)allTrackPoints.get(theIndex));
	}
	// check the limit of the counter. stop at the end of the array
	// or start over again. can be set to true or false with variable isLoop.
	public void checkCounter() {
		if (isLoop) {
			if (cnt >= (len - 1)) {
				cnt = 0;
				lineCount = 0;
			}
		} else {
			cnt = (int) Math.min(cnt, len - 1);
		}
		lineCount = (int) Math.min(lineCount, len - 1);
	}

	float timeDifference(
	        Calendar calendar1,
	        Calendar calendar2) {
		long milliseconds1 = calendar1.getTimeInMillis();
		long milliseconds2 = calendar2.getTimeInMillis();
		float diff = milliseconds2 - milliseconds1;
		float diffSeconds = diff / 1000;
		float diffMinutes = diff / (60 * 1000);
		float diffHours = diff / (60 * 60 * 1000);
		float diffDays = diff / (24 * 60 * 60 * 1000);
		return diff;
	}

	Calendar convertTimestamp(
	        String theValue) {
		String[] myTimestamp = theValue.split(" ");
		String[] myDate = myTimestamp[0].split(":");
		String[] myTime = myTimestamp[1].split(":");
		Calendar myCalendar = Calendar.getInstance();
		int a = Integer.parseInt("3");
		myCalendar.set(Integer.parseInt(myDate[0]), Integer.parseInt(myDate[1]),Integer.parseInt(myDate[2]), Integer.parseInt(myTime[0]), Integer.parseInt(myTime[1]),
				Integer.parseInt(myTime[2]));
		return myCalendar;
	}
	
	
	public GeoTrackList mergeTracks() {
		GeoTrackList myTrackList = new GeoTrackList();
		GeoTrack myTrack = myTrackList.addTrack();
		for (int i = 0; i < allTrackPoints.size() - 1; i++) {
			myTrack.trackPoints().add(getTrackPoint(i));
			if (GeoUtils.distance(getTrackPoint(i), getTrackPoint(i + 1)) > 100) {
				myTrack = myTrackList.addTrack();
			}
		}

		for (int i = 0; i < myTrackList.size(); i++) {
			GeoUtils.getBoundingBox(myTrackList.track(i));
		}
		return myTrackList;
	}
}
