/**
 *  NetP5 is a processing and java library for tcp and udp ip communication.
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

package sojamo.network;


import java.util.ArrayList;

/**
 * NetAddressList is an arraylist of netaddresses.
 * @author andreas schlegel
 */
public class NetworkAddressList {
  protected ArrayList _myList = new ArrayList();

  /**
   *
   * @param theNetAddress NetAddress
   */
  public void add(NetworkAddress theNetAddress) {
    if (theNetAddress.isValid == true) {
      _myList.add(theNetAddress);
    }
  }



  /**
   *
   * @param theAddress String
   * @param thePort int
   */
  public void add(String theAddress, int thePort) {
    NetworkAddress myOscHost = new NetworkAddress(theAddress, thePort);
    if (myOscHost.isValid == true) {
      _myList.add(myOscHost);
    }
  }



  /**
   *
   * @param theAddress String
   * @param thePort int
   */
  public void remove(String theAddress, int thePort) {
    for (int i = 0; i < _myList.size(); i++) {
      NetworkAddress myHost = ( (NetworkAddress) _myList.get(i));
      if (myHost.hostAddress.equals(theAddress) && myHost.port == thePort) {
        _myList.remove(myHost);
      }
    }
  }



  /**
   *
   * @param theNetAddress NetAddress
   */
  public void remove(NetworkAddress theNetAddress) {
    _myList.remove(theNetAddress);
  }


  public NetworkAddress get(String theIPaddress, int thePort) {
    for (int i = 0; i < _myList.size(); i++) {
      NetworkAddress myHost = ( (NetworkAddress) _myList.get(i));
      if (myHost.hostAddress.equals(theIPaddress) && myHost.port == thePort) {
        return myHost;
      }
    }
    return null;

  }


  /**
   *
   * @param theNetAddress NetAddress
   * @return boolean
   */
  public boolean contains(NetworkAddress theNetAddress) {
       if (_myList.contains(theNetAddress)) {
         return true;
       }
     return false;
  }

  /**
   *
   * @param theIPaddress String
   * @param thePort int
   * @return boolean
   */
  public boolean contains(String theIPaddress, int thePort) {
    for (int i = 0; i < _myList.size(); i++) {
      NetworkAddress myHost = ( (NetworkAddress) _myList.get(i));
      if (myHost.hostAddress.equals(theIPaddress) && myHost.port == thePort) {
        return true;
      }
    }
    return false;
  }


  public int size() {
    return _myList.size();
  }




  /**
   *
   * @param theList NetAddress[]
   */
  public void set(NetworkAddress[] theList) {
    _myList = new ArrayList();
    for (int i = 0; i < theList.length; i++) {
      _myList.add(theList[i]);
    }
  }



  /**
   *
   * @return ArrayList
   */
  public ArrayList list() {
    return _myList;
  }



  /**
   *
   * @param theIndex int
   * @return NetAddress
   */
  public NetworkAddress get(int theIndex) {
    return (NetworkAddress) _myList.get(theIndex);
  }

}
