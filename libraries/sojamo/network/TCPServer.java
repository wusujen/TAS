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

import java.util.Vector;



/**
 * @author andreas schlegel
 */
public class TCPServer
    extends TCPServerAbstract {

  protected NetworkPlug _myNetPlug;

  protected Object _myParent;

  protected final static int NULL = -1;

  protected final static int LISTENER = 0;

  protected final static int EVENT = 1;

  protected int _myMode = NULL;

  /**
   * @invisible
   * @param thePort int
   */
  public TCPServer(final int thePort) {
    super(thePort, TCPServerAbstract.MODE_READLINE);
  }


  /**
   *
   * @param theObject Object
   * @param thePort int
   */
  public TCPServer(final Object theObject,
                   final int thePort) {
    super(thePort, TCPServerAbstract.MODE_READLINE);
    _myParent = theObject;
    initEvent();
  }


  /**
   *
   * @param theObject Object
   * @param thePort int
   * @param theMode int
   */
  public TCPServer(final Object theObject,
                   final int thePort,
                   final int theMode) {
    super(thePort, theMode);
    _myParent = theObject;
    initEvent();
  }


  /**
   *
   * @param thePort int
   * @param theMode int
   */
  public TCPServer(final int thePort,
                   final int theMode) {
    super(thePort, theMode);
  }


  /**
   * @invisible
   * @param theTcpPacketListener TcpPacketListener
   * @param thePort int
   * @param theMode int
   */
  public TCPServer(final TCPPacketListener theTcpPacketListener,
                   final int thePort,
                   final int theMode) {
    super(theTcpPacketListener, thePort, theMode);
    _myMode = LISTENER;
  }



  private void initEvent() {
    _myMode = EVENT;
    _myNetPlug = new NetworkPlug();
  }


  /**
   * @invisible
   * @param thePacket TcpPacket
   * @param thePort int
   */
  public void handleInput(final TCPPacket thePacket,
                          final int thePort) {
    switch (_myMode) {
      case (EVENT):
        _myNetPlug.process(thePacket, thePort);
        break;
      case (LISTENER):
        break;
      case (NULL):
        System.out.println("received a message : " + thePacket.getString());
        break;
    }
  }


  /**
   * @invisible
   * @param theIndex int
   */
  public void status(final int theIndex) {
    switch (_myMode) {
      case (EVENT):
        _myNetPlug.status(theIndex);
        break;
      case (LISTENER):
      case (NULL):
        System.out.println("### status id : " + theIndex);
        break;
    }
  }
  
  
	
	public void addListener(NetworkListener theListener) {
		_myNetPlug.addListener(theListener);
	}
	
	
	public void removeListener(NetworkListener theListener) {
		_myNetPlug.removeListener(theListener);
	}
	
	public NetworkListener getListener(int theIndex) {
		return _myNetPlug.getListener(theIndex);
	}

	public Vector getListeners() {
		return _myNetPlug.getListeners();
	}
	
	
}
