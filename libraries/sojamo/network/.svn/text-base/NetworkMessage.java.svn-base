package sojamo.network;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * @author andreas schlegel
 */
public class NetworkMessage {

    private InetAddress _myInetAddress;

    private int _myPort;

    private String _myString = "";

    private byte[] _myData = new byte[0];

    private TCPClient _myTcpClient;

    private boolean isDatagramPacket = false;

    private int _myProtocol;

    private DatagramPacket _myDatagramPacket;

    private TCPPacket _myTcpPacket;

    protected NetworkMessage(DatagramPacket theDatagramPacket) {
	_myDatagramPacket = theDatagramPacket;
	_myInetAddress = theDatagramPacket.getAddress();
	_myPort = theDatagramPacket.getPort();
	_myData = theDatagramPacket.getData();
	_myProtocol = SNetwork.UDP;
	isDatagramPacket = true;
    }

    protected NetworkMessage(TCPPacket theTcpPacket) {
	_myTcpPacket = theTcpPacket;
	_myInetAddress = theTcpPacket.getTcpConnection().socket()
		.getInetAddress();
	_myPort = theTcpPacket.getTcpConnection().socket().getPort();
	_myString = theTcpPacket.getTcpConnection().getString();
	_myData = theTcpPacket.getBytes();
	_myProtocol = SNetwork.TCP;
	_myTcpClient = theTcpPacket.getTcpConnection();
    }

    public TCPPacket getTcpPacket() {
	return _myTcpPacket;
    }

    public DatagramPacket getDatagramPacket() {
	return _myDatagramPacket;
    }

    protected void setProtocol(int theType) {
	_myProtocol = theType;
    }

    /**
     * get the data of the message as bytes.
     * 
     * @return
     */
    public byte[] getBytes() {
	return _myData;
    }

    /**
     * get the data the message as string.
     * 
     * @return
     */
    public String getString() {
	if (isDatagramPacket) {
	    return new String(_myData);
	} else {
	    return _myString;
	}
    }

    /**
     * get the protocol type the message was sent over. NetP5.TCP or NetP5.UDP
     * are possible.
     * 
     * @return
     */
    public int protocol() {
	return _myProtocol;
    }

    /**
     * get the port the net message was received at.
     * 
     * @return
     */
    public int port() {
	return _myPort;
    }

    public TCPClient tcpConnection() {
	return _myTcpClient;
    }

    public String address() {
	return _myInetAddress.getHostAddress();
    }

    public InetAddress inetAddress() {
	return _myInetAddress;
    }

}
