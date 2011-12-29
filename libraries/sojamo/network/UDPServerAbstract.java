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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @invisible
 */

public abstract class UDPServerAbstract implements Runnable {

    private DatagramSocket _myDatagramSocket = null;

    protected UDPPacketListener _myListener;

    private Thread _myThread = null;

    private int _myPort;

    protected int _myDatagramSize = 1536; // common MTU

    private boolean isRunning = true;

    private boolean isSocket = false;

    /**
     * create a new UdpServer
     * 
     * @invisible
     * @param theListener
     *                UdpPacketListener
     * @param thePort
     *                int
     * @param theBufferSize
     *                int
     */
    public UDPServerAbstract(final UDPPacketListener theListener,final  int thePort,
	    final int theBufferSize) {
	_myDatagramSize = theBufferSize;
	_myPort = thePort;
	_myListener = theListener;
	if (_myListener != null) {
	    start();
	}
    }


    /**
     * get the datagram socket of the UDP server.
     * 
     * @return DatagramSocket
     */
    public DatagramSocket socket() {
	return _myDatagramSocket;
    }

    /**
     * @invisible
     * 
     */
    public void start() {
	_myThread = null;
	_myDatagramSocket = null;
	_myThread = new Thread(this);
	try {
	    Thread.sleep(1000);
	} catch (InterruptedException iex) {
	    SNetwork.printError("UDPServer.start()",
		    "UDPServer sleep interruption " + iex);
	}
	try {
	    _myDatagramSocket = new DatagramSocket(_myPort);
	    SNetwork.printProcess("UDPServer.start()",
		    "new Unicast DatagramSocket created @ port " + _myPort);
	} catch (IOException ioex) {
	    SNetwork.printError("UDPServer.start()",
		    " IOException, couldnt create new DatagramSocket @ port "
			    + _myPort + " " + ioex);
	}

	if (_myDatagramSocket != null) {
	    _myThread.start();
	    isRunning = _myThread.isAlive();
	    isSocket = true;
	} else {
	    isRunning = false;
	}
    }

    /**
     * @invisible
     */
    public void run() {
	if (_myDatagramSocket != null) {
	    if (isRunning) {
		SNetwork.printProcess("UDPServer.run()",
			"UDPServer is running @ " + _myPort);
	    }
	} else {
	    SNetwork.printError("UDPServer.run()",
		    "Socket is null. closing UdpServer.");
	    return;
	}

	while (isRunning) {
	    try {
		byte[] myBuffer = new byte[_myDatagramSize];
		DatagramPacket myPacket = new DatagramPacket(myBuffer,
			_myDatagramSize);
		_myDatagramSocket.receive(myPacket);
		_myListener.process(myPacket, _myPort);
	    } catch (IOException ioex) {
		SNetwork.printProcess("UDPServer.run()", " socket closed.");
		break;
	    } catch (ArrayIndexOutOfBoundsException ex) {
		SNetwork.printError("UdpServer.run()",
			"ArrayIndexOutOfBoundsException:  " + ex);
	    }
	}
	dispose();
    }

    /**
     * stop the UDP server, clean up and delete its reference.
     */
    public void dispose() {
	isRunning = false;
	_myThread = null;
	if (_myDatagramSocket != null) {
	    if (_myDatagramSocket.isConnected()) {
		SNetwork.printDebug("UDPServer.dispose()", "disconnect()");
		_myDatagramSocket.disconnect();
	    }
	    SNetwork.printDebug("UDPServer.dispose()", "close()");
	    _myDatagramSocket.close();
	    _myDatagramSocket = null;
	    SNetwork.printDebug("UDPServer.dispose()",
		    "Closing unicast datagram socket.");
	}
    }
    
    
    /**
     * @invisible
     * @param thePacket
     *                DatagramPacket
     */
    public void send(DatagramPacket thePacket) {
	if (isSocket) {
	    try {
		_myDatagramSocket.send(thePacket);
	    } catch (IOException e) {
		SNetwork.printError("UDPServer.send",
			"ioexception while sending packet.");
	    }
	}
    }

    /**
     * send a byte array to a dedicated remoteAddress.
     * 
     * @param thePacket
     *                OscPacket
     * @param theAddress
     *                String
     * @param thePort
     *                int
     */
    public void send(byte[] theBytes, String theAddress, int thePort) {
	try {
	    InetAddress myInetAddress = InetAddress.getByName(theAddress);
	    send(theBytes, myInetAddress, thePort);
	} catch (UnknownHostException e) {
	    SNetwork.printError("UDPServer.send", "while sending to "
		    + theAddress + " " + e);
	}
    }


    public void send(final byte[] theBytes,final  NetworkAddress theAddress) {
	send(theBytes, theAddress.inetaddress, theAddress.port);
    }

    /**
     * send a byte array to a dedicated remoteAddress.
     * 
     * @param theBytes
     *                byte[]
     * @param theAddress
     *                InetAddress
     * @param thePort
     *                int
     */
    public void send(final byte[] theBytes,final InetAddress theAddress, final int thePort) {
	if (isSocket) {
	    try {
		DatagramPacket myPacket = new DatagramPacket(theBytes,
			theBytes.length, theAddress, thePort);
		send(myPacket);
	    } catch (NullPointerException npe) {
		SNetwork.printError("UDPServer.send",
			"a nullpointer exception occured." + npe);
	    }
	} else {
	    SNetwork.printWarning("UDPServer.send",
		    "DatagramSocket is not running. Packet has not been sent.");
	}
    }

}
