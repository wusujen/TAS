/**
 *  ChatP5 is a processing and java library that implements
 *  different chat protocols like AIM, IRC, Jabber.
 *
 *  2006 by Andreas Schlegel
 *
 *  ChatP5.irc.IRCMessage adopted from
 *  Eteria IRC Client, an RFC 1459 compliant client program written in Java.
 *  Copyright (C) 2000-2001  Javier Kohen <jkohen at tough.com>
 *  http://eirc.sourceforge.net/
 *
 *   This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * @author Andreas Schlegel (http://www.sojamo.de)
 *
 */

package sojamo.chat.irc;

import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.Vector;

import sojamo.chat.ChatClient;
import sojamo.chat.ChatMessage;
import sojamo.chat.ChatNumerics;

/**
 * IRCMessage implements ChatMessage. An IRCMessage is forwarded to a chatEvent
 * method. its content is a text message received from an irc network.
 */
public class IRCMessage implements ChatMessage, ChatNumerics {

    protected String _myPrefix = "";

    protected String _myCommand = "";

    protected int _myCommandIndex = -1;

    protected String[] _myParameters = {};

    protected String[] _myFromName = new String[3];

    protected String _myChannel = "";

    protected String _myMsg = "";

    private int _myType = TEXT;

    private IRCChat _myConnection;

    private boolean isPrivate = false;

    private boolean isPublic = true;

    private String _myRawMessage = "";

    protected IRCMessage(final IRCChat theConnection, final String theString)
	    throws ParseException {
	_myConnection = theConnection;
	_myRawMessage = theString;
	if (0 == theString.trim().length()) {
	    throw new ParseException("Empty message", 0);
	}

	StringTokenizer st = new StringTokenizer(theString, " ");
	String t;

	if (!st.hasMoreTokens()) {
	    throw new ParseException("empty message", 0);
	}
	t = st.nextToken();

	// Let's see if there's a prefix
	if (t.charAt(0) == ':') {
	    if (t.length() == 1) {
		throw new ParseException("empty prefix", 1);
	    }
	    _myPrefix = t.substring(1);
	    if (!st.hasMoreTokens()) {
		throw new ParseException("command expected", 0);
	    }
	    t = st.nextToken();
	} else {
	    _myPrefix = "";
	}

	_myFromName = parseName(_myPrefix);
	// Then we should have received the command

	_myCommand = t;
	try {
	    _myCommandIndex = Integer.parseInt(t);
	} catch (NumberFormatException e) {
	    if (IRCChat.map.containsKey(t)) {
		_myCommandIndex = ((Integer) IRCChat.map.get(t)).intValue();
	    } else {
		// System.out.println("Key not available.");
	    }
	}

	if (!st.hasMoreTokens()) {
	    throw new ParseException("parameters expected", 0);
	}

	Vector tmp_params = new Vector();
	st = new StringTokenizer(st.nextToken(""), " ");
	while (st.hasMoreTokens()) {
	    t = st.nextToken();
	    if (t.charAt(0) == ':') {
		// We found the last parameter, it needs special treatment.
		break;
	    }
	    tmp_params.addElement(t);
	}

	// We want to pass the last parameter unparsed, therefore, we can't use
	// the output of StringTokenizer; we have to work with the original
	// message.
	// Start looking one char past the beginning to avoid finding the prefix
	// marker.
	int colon_index = theString.indexOf(':', 1);
	if (-1 != colon_index) {
	    tmp_params.addElement(theString.substring(colon_index + 1));
	}

	_myParameters = new String[tmp_params.size()];
	tmp_params.copyInto(_myParameters);
    }

    protected IRCMessage(final IRCChat theConnection, final String theMessage,
	    final String theFrom, final String theChannel, final int theType) {
	_myConnection = theConnection;
	_myMsg = theMessage;
	_myFromName = new String[] { theFrom, theFrom, theFrom };
	_myChannel = theChannel;
	_myType = theType;

    }

    protected IRCMessage(final IRCChat theConnection, final String command,
	    final String[] parameters) {
	this(theConnection, "", command, parameters);
    }

    protected IRCMessage(final IRCChat theConnection, final String thePrefix,
	    final String theCommand, final String[] theParameters) {
	_myConnection = theConnection;
	_myPrefix = thePrefix;
	_myCommand = theCommand;

	_myParameters = new String[theParameters.length];
	for (int i = 0; i < _myParameters.length; i++) {
	    _myParameters[i] = theParameters[i];
	}
    }

    protected static String[] parseName(String theString) {
	String[] myResult = new String[] { theString, "", "" };
	int myIndex = theString.indexOf('!');
	if (myIndex != -1) {
	    myResult[1] = theString.substring(0, myIndex);
	    if (myIndex > theString.length()) {
		myResult[2] = theString.substring(myIndex + 1);
	    }
	}
	return myResult;
    }

    /*
     * Produce as many 512-char strings as needed to represent the message.
     * PREFIX and COMMAND are included in every fragment.
     */
    protected String[] slices() {
	String pre = "";

	if (0 != _myPrefix.length()) {
	    pre += ":" + _myPrefix + " ";
	}
	pre += _myCommand;

	String params = getParametersString();

	Vector v = new Vector();
	// Length is 510 instead of 512 because of the "\r\n" appended later.
	final int max_slice_len = 510 - pre.length();
	int i = 0;
	do {
	    int slice_len = Math.min(max_slice_len, params.length() - i);
	    String slice = params.substring(i, i + slice_len);
	    v.addElement(pre + slice + "\r\n");
	    i += slice_len;
	} while (i < params.length());

	String[] slices = new String[v.size()];
	v.copyInto(slices);

	return slices;
    }

    /**
     * 
     * @return String
     * @invisible
     */
    public String prefix() {
	return _myPrefix;
    }

    /**
     * 
     * @return String
     * @invisible
     */
    public String getParametersString() {
	StringBuffer buf = new StringBuffer();

	if (_myParameters.length > 0) {
	    for (int i = 0; i < _myParameters.length - 1; i++) {
		buf.append(' ').append(_myParameters[i]);
	    }

	    buf.append(" :").append(_myParameters[_myParameters.length - 1]);
	}

	return buf.toString();
    }

    /**
     * 
     * @param theIndex
     *                int
     * @return String
     * @invisible
     */
    public String getParameter(int theIndex) {
	if (_myParameters.length <= theIndex + 1) {
	    return _myParameters[theIndex];
	} else {
	    return "";
	}
    }

    protected void setMessage(String theMsg) {
	_myMsg = theMsg;
    }

    protected void setChannel(String theChannel) {
	if (theChannel.equals(_myConnection.username())) {
	    isPrivate = true;
	    isPublic = false;
	}
	_myChannel = theChannel;
    }

    protected void setType(int theType) {
	_myType = theType;
    }

    protected void setConnection(IRCChat theConnection) {
	_myConnection = theConnection;
    }

    /**
     * 
     * @return String
     * @invisible
     */
    public String command() {
	return _myCommand;
    }

    /**
     * 
     * @return int
     * @invisible
     */
    public int commandIndex() {
	return _myCommandIndex;
    }

    /**
     * 
     * @return int
     */
    public int origin() {
	return IRC;
    }

    /**
     * returns the channel name the message was sent from.
     * 
     * @return String
     */
    public String channel() {
	return _myChannel;
    }

    /**
     * 
     * @return int
     * @invisible
     */
    public int type() {
	return _myType;
    }

    /**
     * the content of the message.
     * 
     * @return String
     */
    public String message() {
	return _myMsg;
    }

    public String rawMessage() {
	return _myRawMessage;
    }

    /**
     * returns the name of the sender.
     * 
     * @return String
     */
    public String from() {
	return _myFromName[1];
    }

    /**
     * assuming you are using multiple instances of the IRC object, then
     * connection() may be useful to distiguish between those.
     * 
     * @return ChatClient
     */
    public ChatClient connection() {
	return _myConnection;
    }

    /**
     * returns true or false if the message is private.
     * 
     * @return boolean
     */
    public boolean isPrivate() {
	return isPrivate;
    }

    /**
     * returns true or false if the message is private.
     * 
     * @return boolean
     */
    public boolean isPublic() {
	return isPublic;
    }

    /**
     * 
     * @param theIndex
     *                int
     * @return String
     * @invisible
     */
    public String name(int theIndex) {
	return _myFromName[theIndex];
    }

    /**
     * 
     * @return String[]
     * @invisible
     */
    public String[] parameters() {
	return _myParameters;
    }

    /**
     * 
     * @return String
     * @invisible
     */
    public String toString() {
	String pre = "";

	if (0 != _myPrefix.length()) {
	    pre += ":" + _myPrefix + " ";
	}

	return ("PRE:" + _myPrefix + " CMD:" + _myCommand + " PARAMS:"
		+ getParametersString() + "\r\n");
    }

}
