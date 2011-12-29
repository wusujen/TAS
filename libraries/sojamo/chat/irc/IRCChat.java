/**
 *  ChatP5 is a processing and java library that implements
 *  different chat protocols like AIM, IRC, Jabber.
 *
 *  2006 by Andreas Schlegel
 *
 *  ChatP5.irc.IRC adopted from
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

/*
 http://www.irchelp.org/irchelp/ircd/numerics.html

 http://deoxy.org/chat/unreal.htm
 http://www.mirc.net/raws
 http://www.eggfaq.com/docs/raw.html
 http://www.leonini.net/lolstoolz/numerics.html

 protocol:
 http://www.irchelp.org/irchelp/rfc/rfc2812.txt
 */

package sojamo.chat.irc;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Vector;

import sojamo.chat.CStringUtils;
import sojamo.chat.ConnectionListener;
import sojamo.chat.ChatMessage;
import sojamo.chat.ChatNumerics;
import sojamo.chat.ChatStatus;
import sojamo.chat.irc.IRCServ.ChanServ;
import sojamo.chat.irc.IRCServ.NickServ;

/**
 * IRC
 */
public class IRCChat extends IRCHandler implements ChatNumerics {

    private final String _myServerAddress;

    private int _myServerPort = 6667;

    private String _myRealName;

    private String _myPassword;

    private String _myHostName;

    private String _myMOTD = "";

    /**
     * @invisible
     */
    public final static HashMap map = new HashMap();

    /**
     * @invisible
     */
    public final static int START = 0;

    /**
     * @invisible
     */
    public final static int END = 1;

    private final HashMap _myChannelMap;

    private boolean isConnected = false;

    private final IRCServ _myServ;

    private final ConnectionListener _myChatListener;

    private IRCSocket _mySocket;
    
    private Vector _myChannels;

    /**
     * tech_docs at http://www.irc.org/tech_docs/rfc1459.html
     * 
     * @param theChatListener
     *                ChatListener
     * @param theServerAddress
     *                String
     * @param thePort
     *                int
     * @param theName
     *                String
     * @param thePassword
     *                String
     * @invisible
     */
    public IRCChat(final ConnectionListener theChatListener,
	    final String theServerAddress, final int thePort,
	    final String theName, final String thePassword) {
	_myChatListener = theChatListener;
	_myUserName = theName;
	_myRealName = theName;
	_myPassword = thePassword;
	_myServerPort = thePort;
	_myServerAddress = theServerAddress;
	_myServ = new IRCServ(this);
	_myChannels = new Vector();
	_myChannelMap = new HashMap();
	connect();
    }

    /**
     * 
     * @param theChatListener
     *                ChatListener
     * @param theServerAddress
     *                String
     * @param thePort
     *                int
     * @param theName
     *                String
     * @param thePassword
     *                String
     * @param theRealName
     *                String
     * @invisible
     */
    public IRCChat(final ConnectionListener theChatListener,
	    final String theServerAddress, final int thePort,
	    final String theName, final String thePassword,
	    final String theRealName) {
	_myChatListener = theChatListener;
	_myUserName = theName;
	_myRealName = theRealName;
	_myPassword = thePassword;
	_myServerAddress = theServerAddress;
	_myServerPort = thePort;
	_myChannels = new Vector();
	_myChannelMap = new HashMap();
	_myServ = new IRCServ(this);
	connect();
    }

    protected void connect() {
	close();
	_mySocket = new IRCSocket(this, _myServerAddress, _myServerPort);
	try {
	    Thread.sleep(100);
	} catch(Exception e) {
	    
	}
	sendCommand("PASS " + _myPassword);
	sendCommand("NICK " + _myUserName);
	sendCommand("USER " + _myUserName + " " + _myHostName + " "
		+ _myServerAddress + " :" + _myRealName);
    }

    public boolean isConnected() {
	return _mySocket.isConnected;
    }

    /**
     * try to reconnect to the given server. reconnect() can be used when you
     * get disconnected or the login for failed.
     */
    public void reconnect() {
	connect();
    }

    /**
     * set a new password to login.
     * 
     * @param thePassword
     *                String
     */
    public void setPassword(String thePassword) {
	_myPassword = thePassword;
    }

    /**
     * set a new login name.
     * 
     * @param theLoginName
     *                String
     */
    public void setUserName(String theUserName) {
	_myUserName = theUserName;
    }

    /**
     * change your real name as seen in the chat.
     * 
     * @param theRealName
     *                String
     */
    public void setRealName(String theRealName) {
	_myRealName = theRealName;
	sendCommand("USER " + _myUserName + " " + _myHostName + " "
		+ _myServerAddress + " :" + _myRealName);
    }

    protected void close() {
	if (_mySocket != null) {
	    _mySocket.dispose();
	    _mySocket = null;
	}
	_myChannelMap.clear();
	isConnected = false;
    }

    /**
     * send a raw command to IRC. e.g. PRIVMSG someuser :here is the text
     * message sending messages this way, needs a special formatting. you should
     * be familiar with the IRC protocol. take a look at e.g.
     * http://www.irchelp.org/irchelp/rfc/rfc2812.txt or your prefered IRC
     * protocol for the appropriate syntax.
     * 
     * @param theString
     *                String
     * @shortdesc send messages in raw IRC protocol format.
     */
    public void sendCommand(String theString) {
	try {
	    theString = (theString.charAt(0) == '/') ? theString.substring(1)
		    : theString;
	    _mySocket.send(theString);
	} catch (NullPointerException e) {
	    System.out.println("an Error occured while trying "
		    + "to send a chat message. are you "
		    + "connected to the internet?");
	}
    }

    /**
     * returns an IRCChannel object of a channel you are present in.
     * 
     * @param theChannel
     *                String
     * @return IRCChannel
     */
    public IRCChannel channel(String theChannel) {
	String myChannel = theChannel.toLowerCase();
	if (_myChannelMap.containsKey(myChannel)) {
	    return (IRCChannel) myChannelMap().get(myChannel);
	}
	return new IRCChannel(this, "ERROR");
    }

    /**
     * @invisible
     */
    public NickServ nickserv() {
	return _myServ.nick();
    }

    /**
     * @invisible
     */
    public ChanServ chanserv() {
	return _myServ.chan();
    }

    /**
     * returns the motto of the day as a string.
     * 
     * @return String
     */
    public String motd() {
	return _myMOTD;
    }

    /**
     * returns a string array containing the names of users currently in your
     * ignorelist.
     * 
     * @return String[]
     */
    public String[] ignorelist() {
	String[] myIgnoreList = new String[_myIgnoreList.size()];
	_myIgnoreList.toArray(myIgnoreList);
	return myIgnoreList;
    }

    /**
     * 
     * INTERNAL MESSAGES
     * 
     */

    protected HashMap myChannelMap() {
	return _myChannelMap;
    }
    
    public String[] channels() {
	String[] myChannels = new String[_myChannels.size()];
	_myChannels.toArray(myChannels);
	return myChannels;
    }

    protected void deliver(IRCMessage theMessage) {
	String[] slices = theMessage.slices();
	for (int i = 0; i < slices.length; i++) {
	    _mySocket.send(slices[i]);
	}
    }

    /**
     * 
     * @param thePacket
     *                TcpPacket
     * @param thePort
     *                int
     * @invisible
     */
    protected void process(String theMessage) {
	if (theMessage.charAt(0) == ':') {
	    parseMessage(theMessage);
	} else {
	    sendCommand("PONG " + theMessage.substring(5));
	}
    }

    
    private void evaluate(IRCMessage theMessage) {
	int myType = EVENT;
	theMessage.toString();
	switch (theMessage.commandIndex()) {
	case (NOTICE):
	    if (theMessage.from().toLowerCase().indexOf("chanserv") != -1
		    || theMessage.from().toLowerCase().indexOf("nickserv") != -1) {
		theMessage.setMessage(theMessage.toString());
		theMessage.setType(IRCMessage.SERVER);
		break;
	    }
	case (PRIVMSG):
	    theMessage.setChannel(theMessage.parameters()[0]);
	    theMessage.setMessage(theMessage.parameters()[1]);
	    theMessage.setType(IRCMessage.TEXT);
	    myType = MESSAGE;
	    break;
	case (JOIN):
	    if (_myUserName.equals(theMessage.from())) {
		addChannel(theMessage.parameters()[0]);
	    }
	    theMessage.setType(IRCMessage.USER);
	    theMessage.setChannel(theMessage.parameters()[0]);
	    theMessage.setMessage(theMessage.from() + " joins channel "
		    + theMessage.parameters()[0]);
	    addUserToChannel(theMessage.parameters()[0],
		    new String[] { theMessage.from() });
	    break;
	case (INVITE):
	    theMessage.setType(IRCMessage.USER);
	    theMessage.setChannel(theMessage.parameters()[1]);
	    theMessage.setMessage(theMessage.from() + " invites "
		    + theMessage.parameters()[0] + " to join channel "
		    + theMessage.parameters()[1]);
	    break;
	case (MODE):
	    System.out.println(theMessage.from() + " MODE "
		    + theMessage.parameters()[1]);
	    theMessage.setChannel(theMessage.parameters()[0]);
	    theMessage.setMessage(theMessage.from() + " changed mode to "
		    + theMessage.parameters()[1]);
	    theMessage.setType(IRCMessage.USER);
	    break;

	case (NICK):
	    changeUser(theMessage.from(), theMessage.parameters()[0]);
	    theMessage.setMessage(theMessage.from()
		    + " changed his nick and is now known as "
		    + theMessage.parameters()[0]);
	    theMessage.setType(IRCMessage.USER);
	    break;
	case (KICK):

	    /*
	     * kick only works for one user kicked. currently the issue is, that
	     * we dont know when the kick comment starts, because we dont know
	     * which string had the : as prefix. this needs to be added to the
	     * parsing routine of incoming messages at IRCMessage. there is
	     * already a not for special treatment, but i am too tired now.
	     */
	    removeUserFromChannel(theMessage.parameters()[0], theMessage
		    .parameters()[1]);
	    theMessage.setChannel(theMessage.parameters()[0]);
	    theMessage.setMessage(theMessage.from() + " kicked "
		    + theMessage.parameters()[1]);
	    theMessage.setType(IRCMessage.USER);
	    break;
	case (PART):
	    removeUserFromChannel(theMessage.parameters()[0], theMessage.from());
	    if (_myUserName.equals(theMessage.from())) {
		removeChannel(theMessage.parameters()[0]);
	    }
	    theMessage.setChannel(theMessage.parameters()[0]);
	    theMessage.setMessage(theMessage.from() + " left "
		    + theMessage.parameters()[0]);
	    theMessage.setType(IRCMessage.USER);
	    break;
	case (STATS):
	    System.out.println("### STATS " + theMessage.toString());
	    break;
	case (AWAY):
	    theMessage.setMessage("AWAY message from " + theMessage.from()
		    + "  " + theMessage.channel());
	    theMessage.setType(IRCMessage.USER);
	    break;
	case (SQUIT):
	case (QUIT):
	    removeUser(theMessage.from());
	    theMessage.setMessage(theMessage.from() + " quit.");
	    theMessage.setType(IRCMessage.USER);
	    break;
	case (RPL_NAMREPLY):
	    String myChannel = theMessage.parameters()[2];
	    String[] myUsers = CStringUtils.explode(theMessage.parameters()[3]);
	    addUserToChannel(myChannel, myUsers);
	    theMessage.setMessage("name reply " + theMessage.toString());
	    theMessage.setType(IRCMessage.USER);
	    break;
	case (RPL_ENDOFNAMES):
	    theMessage.setMessage("end of namelist " + theMessage.toString());
	    theMessage.setType(IRCMessage.USER);
	    break;
	case (RPL_MOTDSTART):
	    _myMOTD = "";
	case (RPL_MOTD):
	    String myMOTD = (theMessage.getParameter(1)).substring(1);
	    theMessage.setMessage(myMOTD);
	    theMessage.setType(IRCMessage.SERVER);
	    _myMOTD += myMOTD;
	    break;
	case (RPL_ENDOFMOTD):
	case (ERR_NOMOTD):
	    theMessage.setMessage(_myUserName + " logged in @ "
		    + theMessage.name(0));
	    theMessage.setType(IRCMessage.INFO);
	    break;
	case (RPL_CHANURL):
	    theMessage.setMessage("RPL_CHANURL " + theMessage.toString());
	    theMessage.setType(IRCMessage.SERVER);
	    break;
	case (RPL_NOTOPIC):
	    myChannel = theMessage.parameters()[1];
	    if (channel(theMessage.parameters()[1]) != null) {
		ircchannel(theMessage.parameters()[1]).setCopyOfTopic("");
	    }
	    theMessage.setChannel(theMessage.parameters()[1]);
	    theMessage.setMessage("no topic @ " + theMessage.channel() + ".");
	    theMessage.setType(IRCMessage.INFO);
	    break;

	case (RPL_TOPIC):
	    myChannel = theMessage.parameters()[1];
	    if (channel(theMessage.parameters()[1]) != null) {
		ircchannel(theMessage.parameters()[1]).setCopyOfTopic(
			theMessage.parameters()[2]);
	    }
	    theMessage.setChannel(theMessage.parameters()[1]);
	    theMessage.setMessage(theMessage.parameters()[2]);
	    theMessage.setType(IRCMessage.INFO);
	    break;
	case (ERR_ERRONEUSNICKNAME):
	case (ERR_NICKNAMEINUSE):
	    theMessage.setMessage(_myUserName
		    + " is in use. change your name and log in again.");
	    theMessage.setType(IRCMessage.ERROR);
	    break;
	case (ERR_PASSWDMISMATCH):
	    close();
	    theMessage.setMessage("you (" + _myUserName
		    + ") provided the wrong password. try again.");
	    theMessage.setType(IRCMessage.ERROR);
	    break;
	case (ERR_NOSUCHNICK):
	case (ERR_CHANNELISFULL):
	case (ERR_INVITEONLYCHAN):
	case (ERR_BANNEDFROMCHAN):
	case (ERR_BADCHANNELKEY):
	    theMessage.setMessage("### ERROR (" + _myUserName + ") "
		    + theMessage.toString());
	    theMessage.setType(IRCMessage.ERROR);
	    break;
	default:
	    theMessage.setMessage("(" + _myUserName + ") "
		    + theMessage.toString());
	    theMessage.setType(IRCMessage.UNKNOWN);
	    break;
	}
	if (myType == MESSAGE) {
	    boolean isIgnore = false;
	    for (int i = 0; i < _myIgnoreList.size(); i++) {
		if (((String) _myIgnoreList.get(i)).equals(theMessage.from())) {
		    isIgnore = true;
		    break;
		}
	    }
	    if (isIgnore == false && isConnected) {

		if (forwardToListeners(theMessage)) {
		    return;
		}
		callEvent(theMessage);
	    }
	} else {
	    ChatStatus myStatus = new ChatStatus(this, theMessage.type(),
		    theMessage.from(), theMessage.commandIndex(), theMessage
			    .message(), theMessage.rawMessage());

	    if (forwardToListeners(myStatus)) {
		return;
	    }
	    _myChatListener.callStatus(myStatus);
	}
	if (!isConnected) {
	    try {
		Thread.sleep(500);
	    } catch (Exception e) {
		System.out.println("### " + e);
	    }
	    _myChatListener.callConnected(this);
	    isConnected = true;
	}

    }

    protected void callEvent(ChatMessage theMessage) {
	_myChatListener.callEvent(theMessage);
    }

    private void addUserToChannel(String theChannel, String[] theUser) {
	String myChannel = theChannel.toLowerCase();
	IRCChannel myIRCChannel = ircchannel(myChannel);
	if (myIRCChannel != null) {
	    myIRCChannel.addUser(theUser);
	}
    }

    private void removeUserFromChannel(String theChannel, String theUser) {
	String myChannel = theChannel.toLowerCase();
	IRCChannel myIRCChannel = ircchannel(myChannel);
	if (myIRCChannel != null) {
	    myIRCChannel.removeUser(theUser);
	}
    }

    private void removeUser(String theUser) {
	String[] s = new String[_myChannelMap.size()];
	_myChannelMap.keySet().toArray(s);
	for (int i = 0; i < s.length; i++) {
	    removeUserFromChannel(s[i], theUser);
	}
    }

    private void changeUser(String theUserName, String theNewUserName) {
	String[] myChannels = new String[_myChannelMap.size()];
	_myChannelMap.keySet().toArray(myChannels);
	for (int i = 0; i < myChannels.length; i++) {
	    IRCChannel myChannel = (IRCChannel) _myChannelMap
		    .get(myChannels[i]);
	    String[] myUsers = myChannel.userlist();
	    for (int j = 0; j < myUsers.length; j++) {
		if (myUsers[j].equals(theUserName)) {
		    if (myChannel.user(myUsers[j]) != null) {
			myChannel.user(myUsers[j]).setName(theNewUserName);
		    }
		}
	    }
	}
    }

    private void addChannel(String theChannel) {
	String myChannel = theChannel.toLowerCase();
	sendCommand("NAMES " + myChannel);
	_myChannelMap.put(myChannel, new IRCChannel(this, myChannel));
    }

    private void removeChannel(String theChannel) {
	String myChannel = theChannel.toLowerCase();
	_myChannelMap.remove(myChannel);
    }

    private void parseMessage(String theString) {
	try {
	    IRCMessage m = new IRCMessage(this, theString);
	    evaluate(m);
	} catch (ParseException e) {

	}
    }

    private IRCChannel ircchannel(String theChannel) {
	String myChannel = theChannel.toLowerCase();
	if (_myChannelMap.containsKey(myChannel)) {
	    return (IRCChannel) myChannelMap().get(myChannel);
	}
	return new IRCChannel(this, "ERROR");
    }

    /**
     * 
     * irc commands on icq http://www.icq.com/ircqnet/help/user-commands.html
     */
    static {
	map.put("PASS", new Integer(0));
	map.put("NICK", new Integer(1));
	map.put("USER", new Integer(2));
	map.put("SERVER", new Integer(3));
	map.put("OPER", new Integer(4));
	map.put("QUIT", new Integer(5));
	map.put("SQUIT", new Integer(6));
	map.put("JOIN", new Integer(7));
	map.put("PART", new Integer(8));
	map.put("MODE", new Integer(9));
	map.put("TOPIC", new Integer(10));
	map.put("NAMES", new Integer(11));
	map.put("LIST", new Integer(12));
	map.put("INVITE", new Integer(13));
	map.put("KICK", new Integer(14));
	map.put("STATS", new Integer(15));
	map.put("LINKS", new Integer(16));
	map.put("TIME", new Integer(17));
	map.put("CONNECT", new Integer(18));
	map.put("TRACE", new Integer(19));
	map.put("ADMIN", new Integer(20));
	map.put("INFO", new Integer(21));
	map.put("PRIVMSG", new Integer(22));
	map.put("NOTICE", new Integer(23));
	map.put("WHO", new Integer(24));
	map.put("WHOIS", new Integer(25));
	map.put("WHOWAS", new Integer(26));
	map.put("KILL", new Integer(27));
	map.put("PING", new Integer(28));
	map.put("PONG", new Integer(29));
	map.put("ERROR", new Integer(30));
	map.put("AWAY", new Integer(31));
	map.put("REHASH", new Integer(32));
	map.put("RESTART", new Integer(33));
	map.put("SUMMON", new Integer(34));
	map.put("USERS", new Integer(35));
	map.put("USERHOST", new Integer(36));
	map.put("ISON", new Integer(37));
    }

}
