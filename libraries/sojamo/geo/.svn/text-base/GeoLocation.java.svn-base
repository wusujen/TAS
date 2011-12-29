package sojamo.geo;


import java.util.GregorianCalendar;

/**
 *
 */
public class GeoLocation {

  private GregorianCalendar _myTime;

  private float _myLatitude;

  private float _myLongitude;

  private float _myAltitude = 0;

  private String _myComment;

  /**
   *
   * @param theTime GregorianCalendar
   * @param theLatitude float
   * @param theLongitude float
   * @param theAltitude float
   */
  public GeoLocation(GregorianCalendar theTime,
                     float theLatitude,
                     float theLongitude,
                     float theAltitude) {
    _myTime = theTime;
    _myLongitude = theLongitude;
    _myLatitude = theLatitude;
    _myAltitude = theAltitude;
  }


  /**
   *
   * @param theLatitude float
   * @param theLongitude float
   * @param theAltitude float
   */
  public GeoLocation(float theLatitude,
                     float theLongitude,
                     float theAltitude) {
    _myTime = null;
    _myLongitude = theLongitude;
    _myLatitude = theLatitude;
    _myAltitude = theAltitude;
  }


  /**
   *
   * @return GregorianCalendar
   */
  public GregorianCalendar time() {
    return _myTime;
  }


  /**
   *
   * @return float
   */
  public float latitude() {
    return _myLatitude;
  }


  /**
   *
   * @return float
   */
  public float longitude() {
    return _myLongitude;
  }


  /**
   *
   * @param theComment String
   */
  public void addComment(String theComment) {
    _myComment = theComment;
  }


  /**
   *
   * @param theAltitude float
   */
  public void setAltitude(float theAltitude) {
    _myAltitude = theAltitude;
  }


  /**
   *
   * @return float
   */
  public float altitude() {
    return _myAltitude;
  }


  /**
   *
   * @return String
   */
  public String comment() {
    return _myComment;
  }


  /**
   *
   * @return String
   */
  public String toString() {
    return "Geolocation: \n" + "Latitude:  " + _myLatitude + "\n"
        + "Longitude: " + _myLongitude + "\n" + "Comment:   "
        + _myComment;
  }
}
