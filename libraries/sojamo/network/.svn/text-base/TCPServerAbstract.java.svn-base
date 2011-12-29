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
import java.net.ServerSocket;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @invisible
 */

public abstract class TCPServerAbstract implements Runnable, TCPPacketListener {

	protected ServerSocket _myServerSocket;

	protected static int _myPort;

	protected TCPPacketListener _myTcpPacketListener = null;

	protected Vector _myTCPClients;

	protected Thread _myThread;

	public final static int MODE_READLINE = TCPClient.MODE_READLINE;

	public final static int MODE_TERMINATED = TCPClient.MODE_TERMINATED;

	public final static int MODE_NEWLINE = TCPClient.MODE_NEWLINE;

	public final static int MODE_STREAM = TCPClient.MODE_STREAM;

	protected final int _myMode;

	protected Vector _myBanList;

	/**
	 * @invisible
	 * @param thePort
	 *            int
	 * @param theMode
	 *            int
	 */
	public TCPServerAbstract(
			final int thePort, 
			final int theMode) {
		_myPort = thePort;
		_myMode = theMode;
		_myTcpPacketListener = this;
		init();
	}

	/**
	 * @invisible
	 * @param theTcpPacketListener
	 *            TcpPacketListener
	 * @param thePort
	 *            int
	 * @param theMode
	 *            int
	 */
	public TCPServerAbstract(
			final TCPPacketListener theTcpPacketListener,
			final int thePort, 
			final int theMode) {
		_myPort = thePort;
		_myMode = theMode;
		_myTcpPacketListener = theTcpPacketListener;
		init();
	}

	protected void init() {
		_myBanList = new Vector();
		_myServerSocket = null;
		_myTCPClients = new Vector();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException iex) {
			SNetwork.printError("TcpServer.start()",
					"TcpServer sleep interuption " + iex);
			return;
		}
		try {
			_myServerSocket = new ServerSocket(_myPort);
		} catch (IOException e) {
			SNetwork.printError("TcpServer.start()", "TcpServer io Exception "
					+ e);
			return;
		}

		_myThread = new Thread(this);
		_myThread.start();
		SNetwork.printProcess("TcpServer", "ServerSocket started @ " + _myPort);
	}

	/**
	 * ban an IP address from the server.
	 * @param theIP
	 */
	public void ban(String theIP) {
		_myBanList.add(theIP);
		for (int i = _myTCPClients.size() - 1; i >= 0; i--) {
			if (((TCPClient) _myTCPClients.get(i)).netAddress().address()
					.equals(theIP)) {
				((TCPClient) _myTCPClients.get(i)).dispose();
			}
		}
	}

	/**
	 * remove the ban for an IP address.
	 * @param theIP
	 */
	public void unBan(String theIP) {
		_myBanList.remove(theIP);
	}
	

	private boolean checkBanList(ServerSocket theSocket) {
		try {
			String mySocketAddress = theSocket.getInetAddress()
					.getHostAddress();
			String mySocketName = theSocket.getInetAddress().getHostName();
			for (int i = _myBanList.size() - 1; i >= 0; i--) {
				if (mySocketAddress.equals(_myBanList.get(i))
						|| mySocketName.equals(_myBanList.get(i))) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * get the server socket object. more at java.net.ServerSocket
	 * @return
	 */
	public ServerSocket socket() {
		return _myServerSocket;
	}

	/**
	 * @invisible
	 */
	public void run() {
		threadLoop: while (Thread.currentThread() == _myThread) {
			try {
				/**
				 * @author when synchronized, disconnected clients are only
				 *         removed from _myTcpClients when there is a new
				 *         connection.
				 */
				// synchronized(_myTcpClients) {
				if (checkBanList(_myServerSocket)) {
					TCPClient t = new TCPClient(this, _myServerSocket.accept(),
							_myTcpPacketListener, _myPort, _myMode);
					if (SNetwork.DEBUG) {
						System.out.println("### new Client @ " + t);
					}
					_myTCPClients.addElement(t);
					SNetwork.printProcess("TcpServer.run", _myTCPClients.size()
							+ " currently running.");
				}
			}
			// }
			catch (IOException e) {
				SNetwork.printError("TcpServer", "IOException. Stopping server.");
				break threadLoop;
			}
		}
		dispose();
	}

	/**
	 * send a string to the connected client(s).
	 * @param theString
	 */
	public synchronized void send(final String theString) {
		try {
			Enumeration en = _myTCPClients.elements();
			while (en.hasMoreElements()) {
				((TCPClient) en.nextElement()).send(theString);
			}
		} catch (NullPointerException e) {

		}
	}

	/**
	 * send a byte array to the connected client(s).
	 * @param theBytes
	 */
	public synchronized void send(final byte[] theBytes) {
		try {
			Enumeration en = _myTCPClients.elements();
			while (en.hasMoreElements()) {
				((TCPClient) en.nextElement()).send(theBytes);
			}
		} catch (NullPointerException e) {

		}
	}

	/**
	 * kill the server.
	 */
	public void dispose() {
		try {
			_myThread = null;

			if (_myTCPClients != null) {
				Enumeration en = _myTCPClients.elements();
				while (en.hasMoreElements()) {
					remove((TCPClient) en.nextElement());
				}
				_myTCPClients = null;
			}

			if (_myServerSocket != null) {
				_myServerSocket.close();
				_myServerSocket = null;
			}
		} catch (IOException e) {
			SNetwork.printError("TCPServer.dispose", "IOException " + e);
		}
	}

	/**
	 * get the number of connected clients.
	 * @return
	 */
	public int size() {
		return _myTCPClients.size();
	}

	/**
	 * get a list of all connected clients. an array of type TCPClient[]
	 * will be returned.
	 * @return
	 */
	public TCPClient[] getClients() {
		TCPClient[] s = new TCPClient[_myTCPClients.size()];
		_myTCPClients.toArray(s);
		return s;
	}

	/**
	 * get a client at a specific position the client list.
	 * @param theIndex
	 * @return
	 */
	public TCPClient getClient(final int theIndex) {
		return (TCPClient) _myTCPClients.elementAt(theIndex);
	}

	/**
	 * @invisible
	 * @param thePacket
	 *            TcpPacket
	 * @param thePort
	 *            int
	 */
	public void process(final TCPPacket thePacket, final int thePort) {
		handleInput(thePacket, thePort);
	}

	/**
	 * @invisible
	 * @param thePacket
	 *            TcpPacket
	 * @param thePort
	 *            int
	 */
	public abstract void handleInput(final TCPPacket thePacket,
			final int thePort);

	/**
	 * remove a TcpClient from the server's client list.
	 * @param theTcpClient
	 *            AbstractTcpClient
	 */
	public void remove(TCPClientAbstract theTcpClient) {
		if (_myTcpPacketListener != null && !_myTcpPacketListener.equals(this)) {
			_myTcpPacketListener.remove(theTcpClient);
		}
		theTcpClient.dispose();
		_myTCPClients.removeElement(theTcpClient);
		SNetwork.printProcess("TCPServer", "removing TCPClient.");
	}

}
