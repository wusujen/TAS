package sojamo.geo;

import java.util.GregorianCalendar;

public class SignalStrength {

	private GregorianCalendar _myTime;

	private float _myStrength;

	public SignalStrength(GregorianCalendar theTime, float theStrength) {
		_myTime = theTime;
		_myStrength = theStrength;
	}

	public GregorianCalendar time() {
		return _myTime;
	}

	public float strength() {
		return _myStrength;
	}
}
