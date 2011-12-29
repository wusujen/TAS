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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import sojamo.chat.ChatClient;
import sojamo.chat.ChatNumerics;
import sojamo.chat.ConnectionListener;
import sojamo.chat.ChatStatus;

/**
 * @invisible
 */
public abstract class IMHandler extends ChatClient {

    protected int _myChatPlatform;

    /* The host address of the TOC server */
    protected String tocICQHost = "iht-d01.icq.com";

    /* The port used to connect to the TOC server */
    protected int tocICQPort = 9898;
    
    protected String tocAIMHost = "toc.oscar.aol.com";

    /* The port used to connect to the TOC server */
    protected int tocAIMPort = 9898;

    /* The OSCAR authentication server */
    protected String authICQHost = "login.icq.com";

    protected String authAIMHost = "login.oscar.aol.com";

    /* The OSCAR authentication server's port */
    protected int authPort = 5190;

    /* What language to use */
    protected String language = "english";

    /* The version of the client */
    protected final String version = "TIC:TOC sojamo.chat.aim";

    /*
     * The string used to "roast" passwords. See the roastPassword method for
     * more info.
     */
    protected final String roastString = "Tic/Toc";

    /* The sequence number used for FLAP packets. */
    protected short sequence;

    /* The connection to the TOC server. */
    protected Socket connection;

    /* An InputStream to the connection */
    protected InputStream is;

    /* An OutputStream to the connection */
    protected OutputStream os;

    /* Screen name of current user */
    protected String id;

    /* The ID number for a SIGNON packet(FLAP) */
    protected static final int SIGNON = 1;

    /* The ID number for a DATA packet (flap) */
    protected static final int DATA = 2;

    protected String _myPassword;

    protected Thread _myThread;

    /* The ChatBuddies object that owns this object. */
    protected ConnectionListener _myChatListener;

    /**
     * @invisible
     */
    public void run() {
	System.out.println("### IM is running.");
	while (Thread.currentThread() == _myThread) {
	    try {
		login(_myUserName, _myPassword);
		processTOCEvents();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

    /**
     * Called to encode a message. Convert carige returns to <br>
     * 's and put \'s infront of quotes, etc.
     * 
     * @param str
     *                The string to be encoded
     * @return The string encoded
     */
    protected static String encode(final String str) {
	String rtn = "";
	try {
	    for (int i = 0; i < str.length(); i++) {
		switch (str.charAt(i)) {
		case '\r':
		    rtn += "<br>";
		    break;
		case '{':
		case '}':
		case '\\':
		case '"':
		    rtn += "\\";

		default:
		    rtn += str.charAt(i);
		}
	    }
	} catch (NullPointerException e) {
	    System.out.println("encoding failed.");
	}
	return rtn;

    }

    /**
     * Called to normalize a screen name. This removes all spaces and converts
     * the name to lower case.
     * 
     * @param name ,
     *                The screen name
     * @return The normalized screen name
     */

    protected static String normalize(final String name) {
	String rtn = "";
	for (int i = 0; i < name.length(); i++) {
	    if (name.charAt(i) == ' ') {
		continue;
	    }
	    rtn += Character.toLowerCase(name.charAt(i));
	}

	return rtn;

    }

    protected abstract void processTOCEvents() throws IOException;

    private boolean login(final String id, final String password) throws IOException {
    this.id = id;
    String mySocketHost = (_myChatPlatform==ChatNumerics.AIM) ? tocAIMHost:tocICQHost;
    int mySocketPort = (_myChatPlatform==ChatNumerics.AIM) ? tocAIMPort:tocICQPort;
    connection = new Socket(mySocketHost, mySocketPort);
    is = connection.getInputStream();
    os = connection.getOutputStream();
    sendRaw("FLAPON\r\n\r\n");
    getFlap();
    sendFlapSignon();
    String myHost = (_myChatPlatform==ChatNumerics.AIM) ? authAIMHost:authICQHost;
    String command = "toc2_signon " + myHost + " " + authPort + " " + id
        + " " + roastPassword(password) + " " + language + " \""
        + this.version + "\" 160 " + calculateCode(id, password);

    sendFlap(DATA, command);
    String str = getFlap();

    if (str.toUpperCase().startsWith("ERROR:")) {
      handleError(str);
      return false;
    }

    this.sendFlap(DATA, "toc_add_buddy " + this.id);
    return true;
  }

    protected void event(final int theType, final String theUser,
	    int theAction, final String theMessage) {
	ChatStatus myStatus = new ChatStatus(this, theType, theUser, theAction,
		theMessage, ""// @todo pass rawMessage on to the chatStatus
	);
	if (forwardToListeners(myStatus)) {
	    return;
	}
	_myChatListener.callStatus(myStatus);
    }

    /**
     * Extract the next element, bounded by a ':', and return that element. This
     * has the advantage over StringTokenizer, in that at any point you can pull
     * the remaining string, just by using what is left of the StringBuffer.
     * This is good for when there is a fixed number of tokens, yet the
     * remaining String can still use the token. For example:
     * JeffHeatonDotCom:F:F:Please remember to get: this, that and that. The
     * final : is not a token because this string only has 4 fields.
     * 
     * @param str
     *                The string to parse, will be modified
     * @return The next element found
     */
    protected static String nextElement(StringBuffer str) {
	String result = "";
	int i = str.indexOf(":", 0);
	if (i == -1) {
	    result = str.toString();
	    str.setLength(0);
	} else {
	    result = str.substring(0, i).toString();
	    String a = str.substring(i + 1);
	    str.setLength(0);
	    str.append(a);
	}
	return result;
    }

    /**
     * Parse an error packet and send it back to the Chatable class
     * 
     * @param str
     *                The error
     */
    protected void handleError(final String str) {
	StringBuffer sb = new StringBuffer(str);
	String e = nextElement(sb);
	String v = nextElement(sb);
	error(e, v);
    }

    private void error(final String str, final String var) {
	System.out.println("Error:" + str + ":" + var);

    }

    /**
     * Called to roast the password.
     * 
     * Passwords are roasted when sent to the host. This is done so they aren't
     * sent in "clear text" over the wire, although they are still trivial to
     * decode. Roasting is performed by first xoring each byte in the password
     * with the equivalent modulo byte in the roasting string. The result is
     * then converted to ascii hex, and prepended with "0x". So for example the
     * password "password" roasts to "0x2408105c23001130"
     * 
     * @param str
     *                The password to roast
     * @return The password roasted
     */
    protected String roastPassword(final String str) {
	byte xor[] = roastString.getBytes();
	int xorIndex = 0;
	String rtn = "0x";

	for (int i = 0; i < str.length(); i++) {
	    String hex = Integer.toHexString(xor[xorIndex]
		    ^ (int) str.charAt(i));
	    if (hex.length() == 1) {
		hex = "0" + hex;
	    }
	    rtn += hex;
	    xorIndex++;
	    if (xorIndex == xor.length) {
		xorIndex = 0;
	    }
	}
	return rtn;
    }

    /**
     * Calculate a login security code from the user id and password.
     * 
     * @param uid
     *                The user id to encode
     * @param pwd
     *                The password to encoude
     * @return The code, which is used to login
     */
    protected int calculateCode(final String uid, final String pwd) {
	int sn = uid.charAt(0) - 96;
	int pw = pwd.charAt(0) - 96;

	int a = sn * 7696 + 738816;
	int b = sn * 746512;
	int c = pw * a;

	return (c - a + b + 71665152);
    }

    /**
     * Send a string over the socket as raw bytes
     * 
     * @param str
     *                The string to send
     * @exception java.io.IOException
     */
    protected void sendRaw(final String str) throws IOException {
	os.write(str.getBytes());
    }

    /**
     * Write a little endian word
     * 
     * @param word
     *                A word to write
     * @exception java.io.IOException
     */
    protected void writeWord(final short word) throws IOException {
	os.write((byte) ((word >> 8) & 0xff));
	os.write((byte) (word & 0xff));

    }

    /**
     * Send a FLAP signon packet
     * 
     * @exception java.io.IOException
     */
    protected void sendFlapSignon() throws IOException {
	int length = 8 + id.length();
	sequence++;
	os.write((byte) '*');
	os.write((byte) SIGNON);
	writeWord(sequence);
	writeWord((short) length);

	os.write(0);
	os.write(0);
	os.write(0);
	os.write(1);

	os.write(0);
	os.write(1);

	writeWord((short) id.length());
	os.write(id.getBytes());
	os.flush();

    }

    /**
     * Send a FLAP packet
     * 
     * @param type
     *                The type DATA or SIGNON
     * @param str
     *                The string message to send
     * @exception java.io.IOException
     */
    protected void sendFlap(final int type, final String str)
	    throws IOException {
	int length = str.length() + 1;
	sequence++;
	os.write((byte) '*');
	os.write((byte) type);
	writeWord(sequence);
	writeWord((short) length);
	os.write(str.getBytes());
	os.write(0);
	os.flush();
    }

    /**
     * Get a FLAP packet
     * 
     * @return The data as a string
     * @exception java.io.IOException
     */
    protected String getFlap() throws IOException {
	// System.out.println("gotFlap "+is.read());
	char myChar = (char) (is.read());
	if (myChar != '*') {
	    return null;
	}
	myChar = (char) (is.read());
	myChar = (char) (is.read());
	myChar = (char) (is.read());
	myChar = (char) (is.read());
	int length = (myChar * 0x100) + is.read();
	byte b[] = new byte[length];
	is.read(b);
	return new String(b);
    }

}
