package sojamo.chat.irc;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.net.Socket;

class IRCSocket implements Runnable {

    private IRCChat _myIRCChat;

    private Socket _mySocket;

    private String _myHost;

    private int _myPort;

    private Thread _myThread;

    private PrintWriter _myOutput = null;

    private BufferedReader _myInput = null;

    protected boolean isConnected;

    protected IRCSocket(final IRCChat theIRCChat, final String theHost,
	    final int thePort) {
	_myIRCChat = theIRCChat;
	_myHost = theHost;
	_myPort = thePort;
	startSocket();
    }

    private void startSocket() {
	try {
	    _mySocket = new Socket(_myHost, _myPort);
	    init();
	} catch (IOException e) {
	    System.out.println("ERROR @ IRCSocket. "
		    + "IOException while trying to create a new socket."
		    + "no connection has been established.");
	}
    }

    private void init() {
	_myThread = new Thread(this);
	_myThread.start();
	isConnected = true;
    }

    public void run() {
	while (Thread.currentThread() == _myThread) {
	    readline();
	}
    }

    private void readline() {
	try {
	    _myOutput = new PrintWriter(_mySocket.getOutputStream(), true);
	    _myInput = new BufferedReader(new InputStreamReader(_mySocket
		    .getInputStream()));

	    String inputLine;
	    while ((inputLine = _myInput.readLine()) != null) {
		_myIRCChat.process(inputLine);
	    }

	} catch (IOException e) {
	    System.out.println("ERROR @ IRCSocket.readline()."
		    + " connection has been terminated.");
	    // handleStatus(NetworkStatus.CONNECTION_TERMINATED);
	    // handleStatus(NetworkStatus.SERVER_CLOSED);
	    isConnected = false;

	}
    }

    protected void send(String theString) {
	_myOutput.println(theString);
    }

    public void dispose() {
	try {
	    // do io streams need to be closed first?
	    if (_myInput != null) {
		_myInput.close();
	    }
	    if (_myOutput != null) {
		_myOutput.close();
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}

	_myInput = null;
	_myOutput = null;

	try {
	    if (_mySocket != null) {
		_mySocket.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

	if (_myThread == null) {
	    return;
	}

	_mySocket = null;
	_myThread = null;
	// handleStatus(NetworkStatus.CONNECTION_CLOSED);
	isConnected = false;
	System.out.println("IRCSocket.dispose. IRCSocket closed.");

    }

}
