package sojamo.geo;


import java.util.Vector;


public class GeoSatellite {

  private int _myID;

  private Vector _mySignalStrengths;

  public GeoSatellite(int theID, SignalStrength theSignalStrength) {
    _myID = theID;
    _mySignalStrengths = new Vector();
    _mySignalStrengths.add(theSignalStrength);
  }



  public int getID() {
    return _myID;
  }



  public void addSignalStrength(SignalStrength theSignalStrength) {
    _mySignalStrengths.add(theSignalStrength);
  }
}
