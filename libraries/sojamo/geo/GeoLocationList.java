package sojamo.geo;

import java.util.Vector;

/**
 *
 */
public class GeoLocationList {

	private Vector _myGeoLocations = new Vector();

        /**
         *
         * @return int
         */
        public int size() {
		return _myGeoLocations.size();
	}

        /**
         *
         * @param theIndex int
         * @return GeoLocation
         */
        public GeoLocation get(int theIndex) {
		return (GeoLocation) _myGeoLocations.get(theIndex);
	}

        /**
         *
         * @param theGeoLocation GeoLocation
         */
        public void add(GeoLocation theGeoLocation) {
		_myGeoLocations.add(theGeoLocation);
	}

        /**
         *
         * @param theGeoLocation GeoLocation
         */
        public void remove(GeoLocation theGeoLocation) {
		_myGeoLocations.remove(theGeoLocation);
	}

}
