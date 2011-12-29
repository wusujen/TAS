/**
 * sojamo.osc is a processing and java library for the
 * open sound control protocol, OSC.
 *
 *  2006 by Andreas Schlegel
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA
 *
 * @author Andreas Schlegel (http://www.sojamo.de)
 *
 */

package sojamo.osc;


import java.util.ArrayList;


/**
 * OSC Bundles are collections of OSC Messages. use bundles to send multiple
 * OSC messages to one destination. the OscBundle timetag is supported for
 * sending but not for receiving yet.
 * @related OscMessage
 * @related OscManager
 * @example oscBundle
 */
public class OSCBundle extends OSCPacket {

  protected static final int BUNDLE_HEADER_SIZE = 16;

  protected static final byte[] BUNDLE_AS_BYTES = {0x23, 0x62, 0x75, 0x6E,
                                                  0x64, 0x6C, 0x65, 0x00};

  private int _myMessageSize = 0;

  /**
   * instantiate a new OscBundle object.
   */
  public OSCBundle() {
    messages = new ArrayList();
  }


  protected OSCBundle(byte[] theBytes) {
    _myMessageSize = parseBundle(theBytes);
    _myType = BUNDLE;
  }


  /**
   * add an OSC message to the OSC bundle.
   * @param theOscMessage OscMessage
   */
  public void add(OSCMessage theOscMessage) {
    messages.add(new OSCMessage(theOscMessage));
    _myMessageSize = messages.size();
  }


  /**
   * clear and reset the OSC bundle for reusing.
   * @example oscBundle
   */
  public void clear() {
    messages = new ArrayList();
  }


  /**
   * remove an OscMessage from an OscBundle.
   * @param theIndex int
   */
  public void remove(int theIndex) {
    messages.remove(theIndex);
  }


  /**
   *
   * @param theOscMessage OscMessage
   */
  public void remove(OSCMessage theOscMessage) {
    messages.remove(theOscMessage);
  }


  /**
   * request an osc message inside the osc bundle array,
   * @param theIndex int
   * @return OscMessage
   */
  public OSCMessage getMessage(int theIndex) {
    return ((OSCMessage) messages.get(theIndex));
  }


  /**
   * get the size of the osc bundle array which contains the osc messages.
   * @return int
   * @example oscBundle
   */
  public int size() {
    return _myMessageSize;
  }


  /**
   * set the timetag of an osc bundle. timetags are used to synchronize events and
   * execute events at a given time in the future or immediately. timetags can
   * only be set for osc bundles, not for osc messages. sojamo.osc supports receiving
   * timetags, but does not queue messages for execution at a set time.
   * @param theTime long
   * @example oscBundle
   */
  public void setTimetag(long theTime) {
    final long secsSince1900 = theTime / 1000 + TIMETAG_OFFSET;
    final long secsFractional = ((theTime % 1000) << 32) / 1000;
    timetag = (secsSince1900 << 32) | secsFractional;
  }


  /**
   * returns the current time in milliseconds. use with setTimetag.
   * @return long
   */
  public static long now() {
    return System.currentTimeMillis();
  }


  /**
   * returns a timetag as byte array.
   * @return byte[]
   */
  public byte[] timetag() {
    return OSCBytes.toBytes(timetag);
  }


  /**
   * @todo get timetag as Date
   */

  /**
   *
   * @return byte[]
   * @invisible
   */
  public byte[] getBytes() {
    byte[] myBytes = new byte[0];
    myBytes = OSCBytes.append(myBytes, BUNDLE_AS_BYTES);
    myBytes = OSCBytes.append(myBytes, timetag());
    for (int i = 0; i < size(); i++) {
      byte[] tBytes = getMessage(i).getBytes();
      myBytes = OSCBytes.append(myBytes, OSCBytes.toBytes(tBytes.length));
      myBytes = OSCBytes.append(myBytes, tBytes);
    }
    return myBytes;
  }
}
