package sojamo.geo;

public class GeoBox {
	boolean isInitialized = false;
	
	public double x1 = 0, y1 = 0, z1 = 0;

	public double x2 = 0, y2 = 0, z2 = 0;

	public double width = 0; // longitude

	public double height = 0; // latitude

	public double length = 0; // altitude

	public float x1() {
		return (float) x1;
	}

	public float x2() {
		return (float) x2;
	}

	public float y1() {
		return (float) y1;
	}

	public float y2() {
		return (float) y2;
	}

	public float z1() {
		return (float) z1;
	}

	public float z2() {
		return (float) z2;
	}

	public float width() {
		return (float) width;
	}

	public float height() {
		return (float) height;
	}

	public float length() {
		return (float) length;
	}

	public String toString() {
		return (x1 + "," + y1 + " / " + x2 + "," + y2 + " / " + width + "," + height);
	}

}
