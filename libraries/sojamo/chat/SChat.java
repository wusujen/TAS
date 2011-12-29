/**
 *  ChatP5 is a processing and java library that implements
 *  different chat protocols like AIM, IRC, Jabber.
 *
 *  2006 by Andreas Schlegel
 *
 *  This program is free software; you can redistribute it and/or
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

package sojamo.chat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sojamo.chat.im.IMChat;
import sojamo.chat.irc.IRCChat;

// import sojamo.chat.jabber.Jabber; /* not yet */

/**
 * ChatP5
 */
public class SChat extends ConnectionListener implements ChatNumerics {

    private final Object _myParent;

    private Class _myParentClass;

    private Method _myEventMethod;

    private final boolean isEventMethod;

    private final String _myEventMethodName = "chatEvent";

    private Method _myStatusMethod;

    private final boolean isStatusMethod;

    private final String _myStatusMethodName = "chatStatus";

    private Method _myConnectedMethod;

    private final boolean isConnectedMethod;

    private final String _myConntectedMethodName = "chatConnected";

    public static final String VERSION = "0.1.1";
    
    
    /**
     * instantiate a new chatP5 object
     * 
     * @param theParent
     *                Object
     */
    public SChat(final Object theParent) {
	welcome();
	_myParent = theParent;
	isEventMethod = eventMethod(ChatMessage.class, _myEventMethodName);
	isStatusMethod = statusMethod(ChatStatus.class, _myStatusMethodName);
	isConnectedMethod = connectedMethod(ChatClient.class,
		_myConntectedMethodName);
    }

    private void welcome() {
	System.out.println("sojamo.chat " + VERSION + " "
		+ "infos, comments, questions at"
		+ " http://www.sojamo.de/chat\n\n");
    }

    /**
     * get the current version of the chatP5 library
     * 
     * @return String
     */
    public String version() {
	return VERSION
		+ " [alpha version. bugs, requests, comments to andi@sojamo.de]";
    }

    /**
     * log into AIM with your loginname and password.
     * 
     * @param theLogin
     *                String
     * @param thePassword
     *                String
     * @return IMChat
     */
    public IMChat loginAIM(final String theLogin, final String thePassword) {
	IMChat myConnection = new IMChat(this, ChatNumerics.AIM, theLogin, thePassword);
	return myConnection;
    }
    
    /**
     * log into AIM with your loginname and password.
     * 
     * @param theLogin
     *                String
     * @param thePassword
     *                String
     * @return IMChat
     */
    public IMChat loginICQ(final String theLogin, final String thePassword) {
	IMChat myConnection = new IMChat(this, ChatNumerics.ICQ, theLogin, thePassword);
	return myConnection;
    }

    /**
     * log into AIM with your loginname and password
     * 
     * @param theLogin
     *                String
     * @param thePassword
     *                String
     * @return AIM
     * @invisible
     * @todo
     * 
     * public Jabber loginJabber(String theLogin, String thePassword, String
     * theServer) { Jabber myConnection = new Jabber(this, theLogin,
     * thePassword, theServer); return myConnection; }
     */

    /**
     * log into an IRC server with your loginname and password. a password is
     * usually not required so you just use "" as password.
     * 
     * @param theServer
     *                String
     * @param theName
     *                String
     * @param thePassword
     *                String
     * @return IRC
     */
    public IRCChat loginIRC(final String theServer, final String theLoginName,
	    final String thePassword) {
	return loginIRC(theServer, 6667, theLoginName, thePassword);
    }

    public IRCChat loginIRC(final String theServer, final int thePort,
	    final String theLoginName, final String thePassword) {
	IRCChat myConnection = new IRCChat(this, theServer, thePort,
		theLoginName, thePassword);
	return myConnection;
    }

    private boolean statusMethod(final Class theClass,
	    final String theMethodName) {
	_myParentClass = _myParent.getClass();
	if (theMethodName != null) {
	    try {
		Class[] tClass = { theClass };
		_myStatusMethod = _myParentClass.getDeclaredMethod(
			theMethodName, tClass);
		_myStatusMethod.setAccessible(true);
		return true;
	    } catch (SecurityException e1) {
		e1.printStackTrace();
	    } catch (NoSuchMethodException e1) {
		System.out
			.println("### NOTE. no chatEvent(ChatEvent theEvent) method available.");
	    }
	}
	return false;
    }

    private boolean eventMethod(final Class theClass, final String theMethodName) {
	_myParentClass = _myParent.getClass();
	if (theMethodName != null) {
	    try {
		Class[] tClass = { theClass };
		_myEventMethod = _myParentClass.getDeclaredMethod(
			theMethodName, tClass);
		_myEventMethod.setAccessible(true);
		return true;
	    } catch (SecurityException e1) {
		e1.printStackTrace();
	    } catch (NoSuchMethodException e1) {
		System.out
			.println("### NOTE. no chatMessage(ChatMessage theMessage) method available.");
	    }
	}
	return false;
    }

    private boolean connectedMethod(final Class theClass,
	    final String theMethodName) {
	_myParentClass = _myParent.getClass();
	if (theMethodName != null) {
	    try {
		Class[] tClass = { theClass };
		_myConnectedMethod = _myParentClass.getDeclaredMethod(
			theMethodName, tClass);
		_myConnectedMethod.setAccessible(true);
		return true;
	    } catch (SecurityException e1) {
		e1.printStackTrace();
	    } catch (NoSuchMethodException e1) {
		System.out.println("### NOTE. "
			+ "no chatConnected(ChatClient theClient)"
			+ " method available.");
	    }
	}
	return false;
    }

    private void invoke(final Object theObject, final Method theMethod,
	    final Object[] theArgs) {
	try {
	    theMethod.invoke(theObject, theArgs);
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    System.out
		    .println("ChatP5 ClassCastException. parsing failed for ChatMessage "
			    + e);
	    e.printStackTrace();
	}
    }

    /**
     * 
     * @param theChatMessage
     *                ChatMessage
     * @invisible
     */
    public void callEvent(final ChatMessage theChatMessage) {
	if (isEventMethod) {
	    try {
		invoke(_myParent, _myEventMethod,
			new Object[] { theChatMessage });
	    } catch (ClassCastException e) {
		System.out.println("ChatP5.callEvent ClassCastException." + e);
	    }
	}
    }

    /**
     * 
     * @param theStatus
     *                ChatStatus
     * @invisible
     */
    public void callStatus(ChatStatus theStatus) {
	if (isStatusMethod) {
	    try {
		invoke(_myParent, _myStatusMethod, new Object[] { theStatus });
	    } catch (ClassCastException e) {
		System.out
			.println("ChatP5.callStatus ClassCastException. " + e);
	    }
	}
    }

    /**
     * 
     * @param theChatClient
     *                ChatClient
     * @invisible
     */
    public void callConnected(ChatClient theChatClient) {
	if (isConnectedMethod) {
	    try {
		invoke(_myParent, _myConnectedMethod,
			new Object[] { theChatClient });
	    } catch (ClassCastException e) {
		System.out.println("ChatP5.callConnected ClassCastException."
			+ e);
	    }
	}

    }

    /**
     * removehtml removes html tags from a string, this is especially useful for
     * AIM messages.
     * 
     * @param theString
     *                String
     * @return String
     */
    public static String removehtml(final String theString) {
	boolean isOpen = (theString.indexOf("<html>") != -1 || theString
		.indexOf("<HTML>") != -1);
	boolean isClose = isOpen;
	String theCleanString = "";
	for (int i = 0; i < theString.length(); i++) {
	    char c = theString.charAt(i);
	    if (c == '<' && !isOpen) {
		isOpen = true;
		isClose = false;
	    }
	    if (!isOpen && !isClose) {
		theCleanString += c;
	    }
	    if (c == '>') {
		isOpen = false;
		isClose = false;
	    }
	}
	theCleanString = theCleanString.replaceAll("&lt;", "<");
	theCleanString = theCleanString.replaceAll("&gt;", ">");
	return theCleanString;

    }

}
