/**
 *  ChatP5 is a processing and java library that implements
 *  different chat protocols like AIM, IRC, Jabber.
 *  *
 *  AIM is based on The JavaTOC framework  by Jeff Heaton http://www.heatonresearch.com.
 *  it is a set of classes used to allow
 *  a Java program to communicate with AOL's TOC2 protocol.
 *
 *  2006 by Andreas Schlegel
 *  adopted from JavaTOC by Jeff Heaton
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

package sojamo.chat.im;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import sojamo.chat.CStringUtils;
import sojamo.chat.ChatChannel;
import sojamo.chat.ConnectionListener;
import sojamo.chat.ChatMessage;
import sojamo.chat.ChatNumerics;

public class IMChat extends IMHandler implements Runnable {

    private HashMap _myGroups = new HashMap();

    private HashMap _myChatChannels = new HashMap();

    private HashMap _myChatChannelsMap = new HashMap();

    private HashMap _myBuddies = new HashMap();

    private IMList _myDenyList;

    private IMList _myPermitList;

    private int _mypdmode = 4;

    private boolean isConnected = false;

    private String _myChatHelloMessage = "hello room.";
    
    /**
     * 
     * @param theChatListener
     *                ChatListener
     * @param theUserName
     *                String
     * @param thePassword
     *                String
     * @invisible
     */
    public IMChat(final ConnectionListener theChatListener,
	    final int theChatPlatform,
	    final String theUserName, final String thePassword) {
	_myUserName = theUserName;
	_myPassword = thePassword;
	_myChatPlatform = theChatPlatform;
	_myChatListener = theChatListener;
	_myDenyList = new IMList(this);
	_myPermitList = new IMList(this);
	_myThread = new Thread(this);
	_myThread.start();
    }

    /**
     * returns the denylist. denylist is of type AIMList. get an AIMBuddy Object
     * by using AIMBuddy myBuddy = denylist.get("theBuddyName");
     * 
     */
    public IMList denyList() {
	return _myDenyList;
    }

    /**
     * returns the permitlist. permitlist is of type AIMList. get an AIMBuddy
     * Object by using AIMBuddy myBuddy = permitlist.get("theBuddyName");
     * 
     */
    public IMList permitList() {
	return _myPermitList;
    }

    /**
     * send a message to a chatroom.
     * 
     * @param theRoom
     *                String
     * @param theReceiver
     *                String
     * @param theMessage
     *                String
     */
    public void sendMessage(final String theRoom, final String theReceiver,
	    final String theMessage) {
	if (theRoom.length() > 0) {
	    ChatChannel myChatChannel = channel(theRoom);
	    if (myChatChannel != null) {
		if (theReceiver.length() == 0) {
		    myChatChannel.sendMessage(theMessage);
		} else {
		    myChatChannel.whisper(theReceiver, theMessage);
		}
	    }
	} else {
	    sendMessage(theReceiver, theMessage);
	}
    }

    /**
     * send a private message to a chatroom.
     * 
     * @param theRoom
     *                String
     * @param theReceiver
     *                String
     * @param theMessage
     *                String
     */
    public void whisper(final String theRoom, final String theReceiver,
	    final String theMessage) {
	sendMessage(theRoom, theReceiver, theMessage);
    }

    /**
     * send a message to another buddy.
     * 
     * @param to
     *                String
     * @param msg
     *                String
     */
    public void sendMessage(final String to, final String theMessage) {
	deliver(DATA, "toc2_send_im " + normalize(to) + " \""
		+ encode(theMessage) + "\"");
    }

    /**
     * tell aim with an awaymessage that you are away.
     * 
     * @param theMessage
     *                String
     */
    public void away(final String theMessage) {
	deliver(DATA, "toc_set_away \"" + encode(theMessage) + "\"");
    }

    /**
     * say you are back and available again.
     * 
     */
    public void back() {
	away("");
    }

    /**
     * set the user's information.
     * 
     * @param theInfo
     *                String
     */
    public void setInfo(final String theInfo) {
	deliver(DATA, "toc_set_info \"" + encode(theInfo) + "\"");
    }

    /**
     * request the info for a buddy.
     * 
     * @param theUsername
     *                String
     */
    public void info(final String theUsername) {
	deliver(DATA, "toc_get_info \"" + normalize(theUsername) + "\"");
    }

    /**
     * evil an aim buddy. theType can be ChatNumerics.NORMAL or
     * ChatNumerics.ANONYMOUS
     * 
     * @shortdescr evil an aim buddy.
     * @param theUsername
     *                String
     * @param theType
     *                int
     */
    public void evil(final String theUsername, final int theType) {
	String myType = "anon";
	if (theType == ChatNumerics.NORMAL) {
	    myType = "norm";
	}
	deliver(DATA, "toc_evil " + normalize(theUsername) + " \"" + myType
		+ "\"");
    }

    /**
     * @shortdescr evil an aim buddy inside a chat channel.
     * @param theChatChannel
     *                String
     * @param theUsername
     *                String
     * @param theType
     *                int
     */
    public void evil(final String theChatChannel, final String theUsername,
	    final int theType) {
	String myType = "anon";
	if (theType == ChatNumerics.NORMAL) {
	    myType = "norm";
	}
	String myChannelID = channel(theChatChannel).id();
	deliver(DATA, "toc_chat_evil " + normalize(myChannelID) + " "
		+ normalize(theUsername) + " \"" + myType + "\"");
    }

    /**
     * @param theBuddyName
     *                String
     * @param theChannel
     *                AIMChannel
     * @param theInviteMessage
     *                String
     * @invisible
     */
    protected void invite(final String theBuddyName,
	    final IMChannel theChannel, final String theInviteMessage) {
	if (theChannel != null) {
	    // deliver(DATA, "toc_chat_invite " + theChannel.id() + " \"" +
	    // encode(theInviteMessage) + "\" \"" +short
	    // normalize(theBuddyName)+"\"");
	    deliver(DATA, "toc_chat_invite " + normalize(theChannel.id())
		    + " \"" + encode(theInviteMessage) + "\" "
		    + normalize(theBuddyName));

	    System.out.println("### invite is not supported yet.");
	} else {
	    System.out.println("### invitation failed. No such channel.");
	}
    }

    /**
     * join a chat channel on the aol network.
     * 
     * @param theChannelName
     *                String
     */
    public void join(final String theChannelName) {
	deliver(DATA, "toc_chat_join " + "4" + " \"" + encode(theChannelName)
		+ "\"");
    }

    /**
     * get a list of buddies.return type is a list of AIMBuddy.
     * 
     * @return AIMBuddy[]
     */
    public IMBuddy[] buddies() {
	IMBuddy[] myBuddies = new IMBuddy[_myBuddies.size()];
	String[] s = new String[_myBuddies.size()];
	_myBuddies.keySet().toArray(s);
	for (int i = 0; i < myBuddies.length; i++) {
	    myBuddies[i] = ((IMBuddy) _myBuddies.get(s[i]));
	}
	return myBuddies;
    }

    /**
     * get a specific buddy from your buddylist.
     * 
     * @param theBuddy
     *                String
     * @return AIMBuddy
     */
    public IMBuddy buddy(final String theBuddy) {
	IMBuddy myBuddy = (IMBuddy) _myBuddies.get(theBuddy);
	if (myBuddy != null) {
	    return myBuddy;
	} else {
	    return new IMBuddy(this, "ERROR", new IMGroup(this, "ERROR"), 'd');
	}
    }

    /**
     * get a list of all groups from your buddylist.
     * 
     * @return AIMGroup[]
     */
    public IMGroup[] groups() {
	IMGroup[] myGroups = new IMGroup[_myGroups.size()];
	String[] s = new String[_myGroups.size()];
	_myGroups.keySet().toArray(s);
	for (int i = 0; i < myGroups.length; i++) {
	    myGroups[i] = ((IMGroup) _myGroups.get(s[i]));
	}
	return myGroups;
    }

    /**
     * get a specific group from the buddylist.
     * 
     * @param theGroup
     *                String
     * @return AIMGroup
     */
    public IMGroup group(final String theGroup) {
	if (isContained(_myGroups, theGroup)) {
	    return ((IMGroup) _myGroups.get(theGroup));
	} else {
	    return new IMGroup(this, "ERROR");
	}
    }

    /**
     * get a chat channel. if the chat channel is not available an empty
     * AIMChannel will be returned.
     * 
     * @param theChatName
     *                String
     * @return AIMChannel
     */
    public IMChannel channel(final String theChatName) {
	String myChatId = ((String) _myChatChannelsMap.get(theChatName));
	if (isContained(_myChatChannels, myChatId)) {
	    return ((IMChannel) _myChatChannels.get(myChatId));
	}
	return new IMChannel(this, "ERROR", "ERROR", "ERROR", "ERROR");
    }

    /**
     * removes a group from your buddylist. handle with care, buddies in this
     * list can also be deleted. make sure the group is empty when deleting.
     * 
     * @param theGroupName
     *                String
     */
    public void removeGroup(final String theGroupName) {
	if (isContained(_myGroups, theGroupName)) {
	    IMGroup myGroup = ((IMGroup) _myGroups.get(theGroupName));
	    IMBuddy[] myBuddies = myGroup.buddies();
	    for (int i = 0; i < myBuddies.length; i++) {
		String myBuddyName = myBuddies[i].name();
		_myBuddies.remove(myBuddyName);
	    }
	    _myGroups.remove(theGroupName);
	    deliver(DATA, "toc2_del_group " + "\"" + theGroupName + "\"");
	} else {
	    System.out.println("### DELETING FAILED for group " + theGroupName
		    + ". group does not exist.");
	}
    }

    /**
     * adds a new group to your buddylist.
     * 
     * @param theGroup
     *                String
     */
    public void addGroup(final String theGroup) {
	deliver(DATA, "toc2_new_group " + "\"" + theGroup + "\"");
	_myGroups.put(theGroup, new IMGroup(this, theGroup));
    }

    /**
     * adds a new buddy to your buddylist.
     * 
     * @param theBuddyName
     *                String
     * @param theBuddyGroup
     *                String
     */
    public void addBuddy(final String theBuddyName, final String theBuddyGroup) {
	addBuddies(new String[] { theBuddyName }, theBuddyGroup);
    }

    /**
     * adds a list of buddies to a specific group in the buddylist.
     * 
     * @param theBuddyNames
     *                String[]
     * @param theBuddyGroup
     *                String
     */
    public void addBuddies(final String[] theBuddyNames, String theBuddyGroup) {
	theBuddyGroup = (theBuddyGroup.length() == 0) ? "Buddies"
		: theBuddyGroup;
	String myString = "g:" + theBuddyGroup + "\n";
	for (int i = 0; i < theBuddyNames.length; i++) {
	    myString += "b:" + normalize(theBuddyNames[i]) + "\n";
	}

	deliver(DATA, "toc2_new_buddies " + "\"" + myString + "\"");
    }

    /**
     * remove a buddy from your buddylist.
     * 
     * @param theBuddyName
     *                String
     */
    public void removeBuddy(final String theBuddyName) {
	try {
	    if (isContained(_myBuddies, theBuddyName)) {
		IMBuddy myBuddy = ((IMBuddy) _myBuddies.get(theBuddyName));
		if (myBuddy.group() != null) {
		    myBuddy.group().removeBuddy(myBuddy);
		}
		_myBuddies.remove(theBuddyName);
		deliver(DATA, "toc2_remove_buddy " + normalize(theBuddyName)
			+ " \"" + myBuddy.group().name() + "\"");
	    }
	} catch (NullPointerException e) {
	    System.out.println("ERROR @ AIMChat.removeBuddy. " + e);
	}
    }

    /**
     * move a buddy from one group to another in your buddylist.
     * 
     * @param theBuddyName
     *                String
     * @param theGroupName
     *                String
     */
    public void moveBuddy(final String theBuddyName, final String theGroupName) {
	if (isContained(_myBuddies, theBuddyName)) {
	    char myStatus = buddy(theBuddyName).statuschar();
	    removeBuddy(theBuddyName);
	    addBuddies(new String[] { theBuddyName }, theGroupName);
	    mapBuddy(theGroupName, theBuddyName, myStatus);
	}
    }

    /**
     * permit a buddy. this is useful when the pdmode is set to other than allow
     * all. see setpdmode(int theValue).
     * 
     * @param theBuddyName
     *                String
     * @related setpdmode ( )
     * @related removePermitBuddy ( )
     */
    public void permitBuddy(final String theBuddyName) {
	setBuddyStatus("toc2_add_permit", theBuddyName, IMBuddy.PERMIT);
	if (_myPermitList.indexOf(theBuddyName) == -1) {
	    _myPermitList.add(theBuddyName);
	}
    }

    /**
     * deny a buddy. this is useful when the pdmode is set to other than allow
     * all. see setpdmode(int theValue).
     * 
     * @param theBuddyName
     *                String
     * @related setpdmode ( )
     */
    public void denyBuddy(final String theBuddyName) {
	setBuddyStatus("toc2_add_deny", theBuddyName, IMBuddy.DENY);
	if (_myDenyList.indexOf(theBuddyName) == -1) {
	    _myDenyList.add(theBuddyName);
	}
    }

    /**
     * remove a permit flag from a buddy.
     * 
     * @param theBuddyName
     *                String
     * @related permitBuddy ( )
     */
    public void removePermitBuddy(final String theBuddyName) {
	setBuddyStatus("toc2_remove_permit", theBuddyName, IMBuddy.NORMAL);
	_myPermitList.remove(theBuddyName);
    }

    /**
     * remove a deny flag from a buddy.
     * 
     * @param theBuddyName
     *                String
     * @related denyBuddy ( )
     */
    public void removeDenyBuddy(final String theBuddyName) {
	setBuddyStatus("toc2_remove_deny", theBuddyName, IMBuddy.NORMAL);
	_myDenyList.remove(theBuddyName);
    }

    /**
     * Values:<br />
     * 1 - Allow all (default)<br />
     * 2 - Block all<br />
     * 3 - Allow "permit group" only<br />
     * 4 - Block "deny group" only <br />
     * 5 - Allow buddylist only
     * 
     * @param theValue
     *                int
     * @related denyBuddy ( )
     * @related permitBuddy ( )
     * @related removeDenyBuddy ( )
     * @related removePermitBuddy ( )
     * @shortdescr set your permit/deny mode.
     */
    public void setpdmode(int theValue) {
	if (theValue > 0 && theValue <= 5) {
	    _mypdmode = theValue;
	    deliver(DATA, "toc2_set_pdmode " + _mypdmode);
	}
    }

    /**
     * when joining a chat channel a hello message is sent as first message.
     * here you can set the content of this hello message.
     * 
     * @param theMessage
     *                String
     */
    public void setChatHelloMessage(String theMessage) {
	_myChatHelloMessage = theMessage;
    }

    /**
     * returns the current chat hello message.
     * 
     * @return String
     */
    public String chatHelloMessage() {
	return _myChatHelloMessage;
    }

    /**
     * Logout of aim and close the socket
     */
    public void logout() {
	try {
	    connection.close();
	    is.close();
	    os.close();
	} catch (IOException e) {

	}
    }

    /**
     * returns your login name.
     * 
     * @return String
     */
    public String username() {
	return _myUserName;
    }

    /**
     * Begin processing all TOC events and sending them to the Chatable class.
     * Usually called from a thread.
     * 
     * @exception java.io.IOException
     */

    protected void processTOCEvents() throws IOException {
	for (;;) {
	    String str = this.getFlap();
	    String theMessage = new String(str);
	    if (str == null) {
		continue;
	    }

	    str = str.toUpperCase();

	    if (str.startsWith("SIGN_ON:")) {
		event(ChatNumerics.SERVER, "", ChatNumerics.ERROR, theMessage);
	    } else if (str.startsWith("CONFIG2:")) {
		handleConfig(theMessage);
		event(ChatNumerics.SERVER, "", ChatNumerics.CONFIG, theMessage);
	    } else if (str.startsWith("ERROR:")) {
		handleError(theMessage);
		event(ChatNumerics.ERROR, "", ChatNumerics.ERROR, theMessage);
	    } else if (str.startsWith("IM_IN2:")) {
		handleMessage(theMessage);
	    } else if (str.startsWith("UPDATE_BUDDY2:")) {
		String myUser = updateBuddy(theMessage);
		event(ChatNumerics.USER, myUser, ChatNumerics.UPDATE_BUDDY,
			theMessage);
	    } else if (str.startsWith("CHAT_IN:")) {
		handleChatIn(theMessage);
	    } else if (str.startsWith("CHAT_UPDATE_BUDDY:")) {
		handleChatUpdateBuddy(theMessage);
		event(ChatNumerics.USER, "", ChatNumerics.CHAT_UPDATE_BUDDY,
			theMessage);
	    } else if (str.startsWith("CHAT_INVITE:")) {
		String myUser = handleChatInvite(theMessage);
		event(ChatNumerics.USER, myUser, ChatNumerics.CHAT_INVITE,
			theMessage);
	    } else if (str.startsWith("NICK:")) {
		if (!isConnected) {
		    _myChatListener.callConnected(this);
		    isConnected = true;
		}
		event(ChatNumerics.USER, "", ChatNumerics.NICK, theMessage);
	    } else if (str.startsWith("EVILED:")) {
		event(ChatNumerics.USER, "", ChatNumerics.EVILED, theMessage);
	    }

	    else if (str.startsWith("CHAT_JOIN:")) {
		handleChatJoin(theMessage);
		event(ChatNumerics.USER, "", ChatNumerics.CHAT_JOIN, theMessage);
	    } else if (str.startsWith("CHAT_LEFT:")) {
		event(ChatNumerics.USER, "", ChatNumerics.CHAT_LEFT, theMessage);
	    } else if (str.startsWith("GOTO_URL:")) {
		event(ChatNumerics.SERVER, "", ChatNumerics.GOTO_URL,
			theMessage);
	    } else if (str.startsWith("DIR_STATUS:")) {
		event(ChatNumerics.INFO, "", ChatNumerics.DIR_STATUS,
			theMessage);
	    } else if (str.startsWith("ADMIN_NICK_STATUS:")) {
		event(ChatNumerics.SERVER, "", ChatNumerics.ADMIN_NICK_STATUS,
			theMessage);
	    } else if (str.startsWith("ADMIN_PASSWD_STATUS:")) {
		event(ChatNumerics.SERVER, "",
			ChatNumerics.ADMIN_PASSWD_STATUS, theMessage);
	    } else if (str.startsWith("PAUSE:")) {
		event(ChatNumerics.USER, "", ChatNumerics.PAUSE, theMessage);
	    } else if (str.startsWith("RVOUS_PROPOSE:")) {
		event(ChatNumerics.SERVER, "", ChatNumerics.RVOUS_PROPOSE,
			theMessage);
		System.out.println("### RVOUS_PROPOSE " + this.getFlap());
	    } else if (str.startsWith("NEW_BUDDY_REPLY2:")) {
	    } else if (str.startsWith("UPDATED2:")) {
		addNewBuddy(theMessage);
	    } else {
		unknown(theMessage);
		event(ChatNumerics.UNKNOWN, "", ChatNumerics.DEFAULT,
			theMessage);
	    }
	}

    }

    private void handleConfig(final String theConfig) {
	final String[] myLines = CStringUtils.explode(theConfig, "\n");
	String myGroupName = "";
	String myBuddyName = "";
	boolean myGroupFlag = false;
	for (int i = 0; i < myLines.length; i++) {
	    if (myLines[i].startsWith("g:")) {
		myGroupFlag = true;
		StringBuffer sb = new StringBuffer(myLines[i]);
		nextElement(sb);
		myGroupName = nextElement(sb);
		if (!isContained(_myGroups, myGroupName)) {
		    _myGroups.put(myGroupName, new IMGroup(this, myGroupName));
		}
	    } else if (myGroupFlag && myLines[i].startsWith("b:")) {
		StringBuffer sb = new StringBuffer(myLines[i]);
		nextElement(sb);
		myBuddyName = nextElement(sb);
		mapBuddy(myGroupName, myBuddyName, myLines[i].charAt(0));
	    } else {
		StringBuffer sb = new StringBuffer(myLines[i]);
		nextElement(sb);
		if (myLines[i].startsWith("p:")) {
		    _myPermitList.add(nextElement(sb));
		} else if (myLines[i].startsWith("d:")) {
		    _myDenyList.add(nextElement(sb));
		}

	    }
	}
	deliver(
		DATA,
		"toc_set_caps 09461343-4C7F-11D1-8222-444553540000 09461348-4C7F-11D1-8222-444553540000");
	deliver(DATA, "toc2_set_pdmode " + _mypdmode);
	deliver(DATA, "toc2_add_permit ");
	deliver(DATA, "toc2_add_deny ");
	deliver(DATA, "toc_init_done");
    }

    private void addNewBuddy(final String theMessage) {
	StringBuffer sb = new StringBuffer(theMessage);
	nextElement(sb);
	String myModeString = nextElement(sb);
	char myMode = myModeString.charAt(0);
	String myBuddyName = nextElement(sb);
	String myBuddyGroup = nextElement(sb);
	if (!isContained(_myGroups, myBuddyGroup)) {
	    _myGroups.put(myBuddyGroup, new IMGroup(this, myBuddyGroup));
	}
	mapBuddy(myBuddyGroup, myBuddyName, myMode);
    }

    protected IMBuddy mapBuddy(final String theGroupName,
	    final String theBuddyName, final char theStatus) {
	if (!isContained(_myGroups, theGroupName)) {
	    _myGroups.put(theGroupName, new IMGroup(this, theGroupName));
	}

	if (!isContained(_myBuddies, theBuddyName)) {
	    IMBuddy myBuddy = new IMBuddy(this, theBuddyName,
		    (IMGroup) _myGroups.get(theGroupName), theStatus);
	    _myBuddies.put(theBuddyName, myBuddy);
	    ((IMGroup) _myGroups.get(theGroupName)).addBuddy(myBuddy);
	    return myBuddy;
	} else {
	    return (IMBuddy) _myBuddies.get(theBuddyName);
	}
    }

    protected HashMap buddymap() {
	return _myBuddies;
    }

    /**
     * Parse an instant message and send it back to the Chatable class
     * 
     * @param str
     */
    protected void handleMessage(final String theString) {
	StringBuffer sb = new StringBuffer(theString);
	nextElement(sb);

	String from = nextElement(sb); // get from
	nextElement(sb); // get a
	nextElement(sb); // get b
	String message = sb.toString(); // get message
	message(new IMMessage(this, "", from, message));
    }

    protected String handleChatInvite(final String theString) {
	StringBuffer sb = new StringBuffer(theString);
	nextElement(sb);
	String myChannelName = nextElement(sb);
	String myChannelId = nextElement(sb);
	String myInviter = nextElement(sb);
	String myInvitationMessage = nextElement(sb);
	if (!isContained(_myChatChannels, myChannelId)) {
	    _myChatChannelsMap.put(myChannelName, myChannelId);
	    _myChatChannels
		    .put(myChannelId, new IMChannel(this, myChannelName,
			    myChannelId, myInviter, myInvitationMessage));
	}
	return myInviter;
    }

    protected void handleChatJoin(final String str) {
	StringBuffer sb = new StringBuffer(str);
	nextElement(sb);
	String myChannelId = nextElement(sb);
	String myChannelName = nextElement(sb);
	if (!isContained(_myChatChannels, myChannelId)) {
	    _myChatChannelsMap.put(myChannelName, myChannelId);
	    _myChatChannels.put(myChannelId, new IMChannel(this,
		    myChannelName, myChannelId, "", ""));
	}
    }

    protected void leaveChat(final IMChannel theChannel) {
	deliver(IMChat.DATA, "toc_chat_leave " + normalize(theChannel.id()));
	_myChatChannelsMap.remove(theChannel.name());
	_myChatChannels.remove(theChannel);
	theChannel.dispose();
    }

    protected void deliver(final int theType, final String theMessage) {
	try {
	    this.sendFlap(theType, theMessage);
	} catch (java.io.IOException e) {
	    System.out.println("### ERROR sending message.");
	}

    }

    protected static boolean isContained(final HashMap theMap,
	    final String theKey) {
	return theMap.containsKey(theKey);
    }

    private void unknown(String str) {
    }

    protected void message(final ChatMessage theMessage) {
	if (forwardToListeners(theMessage)) {
	    return;
	}
	_myChatListener.callEvent(theMessage);
    }

    private void handleChatUpdateBuddy(final String theString) {
	StringBuffer sb = new StringBuffer(theString);
	nextElement(sb);
	String myRoomId = nextElement(sb);
	boolean myStatus = (nextElement(sb).charAt(0) == 'T') ? true : false;
	Vector myChatBuddies = new Vector();
	while (true) {
	    String myNextElement = nextElement(sb);
	    if (myNextElement.length() == 0) {
		break;
	    }
	    myChatBuddies.add(myNextElement);
	}
	String[] s = new String[myChatBuddies.size()];
	myChatBuddies.toArray(s);
	if (isContained(_myChatChannels, myRoomId)) {
	    ((IMChannel) _myChatChannels.get(myRoomId)).updateChatBuddy(s,
		    myStatus);
	}
    }

    private void handleChatIn(final String theString) {
	StringBuffer sb = new StringBuffer(theString);
	nextElement(sb);
	String myRoomId = nextElement(sb);
	String myName = nextElement(sb);
	String myMode = nextElement(sb);
	String myMessage = sb.toString();
	if (isContained(_myChatChannels, myRoomId)) {
	    ((IMChannel) _myChatChannels.get(myRoomId)).handleMessage(myName,
		    myMode, myMessage);
	}
    }

    private String updateBuddy(final String theString) {
	StringBuffer sb = new StringBuffer(theString);
	nextElement(sb);
	String myBuddyName = nextElement(sb);
	String myOnline = nextElement(sb).toUpperCase();
	String myEvil = nextElement(sb);
	String mySignonTime = nextElement(sb);
	String myIdleTime = nextElement(sb);
	String myUC = nextElement(sb);

	IMBuddy myBuddy = buddy(myBuddyName);
	if (myBuddy.status() == ChatNumerics.ERROR) {
	    myBuddy = new IMBuddy(this, myBuddyName, group("Recent Buddies"),
		    'b');
	    mapBuddy("Recent Buddies", myBuddyName, 'b');
	}
	myBuddy.setEvil(Integer.parseInt(myEvil));
	myBuddy.setOnline(myOnline.startsWith("T") ? true : false);
	myBuddy.setSignonTime(Long.parseLong(mySignonTime));
	myBuddy.setIdleTime(Long.parseLong(myIdleTime));
	myBuddy.setUC(myUC);
	return myBuddyName;
    }

    private void setBuddyStatus(final String theTOC2command,
	    final String theBuddyName, final int theStatus) {
	deliver(DATA, theTOC2command + " " + normalize(theBuddyName));
	if (isContained(_myBuddies, theBuddyName)) {
	    ((IMBuddy) _myBuddies.get(theBuddyName)).setStatus(theStatus);
	}
    }

}
