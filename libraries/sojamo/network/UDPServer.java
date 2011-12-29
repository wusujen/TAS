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

import java.net.DatagramPacket;
import java.util.Vector;

/**
 * 
 * @author andreas schlegel
 * 
 */
public class UDPServer extends UDPServerAbstract implements UDPPacketListener {

    // protected Object _myParent;

    protected NetworkPlug _myNetPlug;

    /**
     * new UDP server. by default the buffersize of a udp packet is 1536 bytes.
     * you can set your own individual buffersize with the third parameter int
     * in the constructor.
     * 
     * @param thePort
     *                int
     * @param theBufferSize
     *                int
     */
    public UDPServer(final int thePort, final int theBufferSize) {
	super(null, thePort, theBufferSize);
	_myListener = this;
	_myNetPlug = new NetworkPlug();
	start();
    }

    public UDPServer(final int thePort) {
	super(null, thePort, 1536);
    }

    /**
     * @invisible
     * @param theListener
     * @param thePort
     * @param theBufferSize
     */
    public UDPServer(final UDPPacketListener theListener, final int thePort,
	    final int theBufferSize) {
	super(theListener, thePort, theBufferSize);
    }

    /**
     * @invisible
     * @param thePacket
     *                DatagramPacket
     * @param thePort
     *                int
     */
    public void process(DatagramPacket thePacket, int thePort) {
	_myNetPlug.process(thePacket, thePort);
    }
    
    
    public void addListener(Object theObject) {
	_myNetPlug.checkMethod(theObject);
	
    }
    /**
     * add a listener to the udp server. each incoming packet will be forwarded
     * to the listener.
     * 
     * @param theListener
     * @related NetListener
     */
    public void addListener(NetworkListener theListener) {
	_myNetPlug.addListener(theListener);
    }

    /**
     * 
     * @param theListener
     * @related NetListener
     */
    public void removeListener(NetworkListener theListener) {
	_myNetPlug.removeListener(theListener);
    }

    /**
     * 
     * @param theIndex
     * @related NetListener
     * @return
     */
    public NetworkListener getListener(int theIndex) {
	return _myNetPlug.getListener(theIndex);
    }

    /**
     * @related NetListener
     * @return
     */
    public Vector getListeners() {
	return _myNetPlug.getListeners();
    }
}
