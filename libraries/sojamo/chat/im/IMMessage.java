/**
 *  ChatP5 is a processing and java library that implements
 *  different chat protocols like AIM, IRC, Jabber.
 *
 *  2006 by Andreas Schlegel
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

import sojamo.chat.ChatClient;
import sojamo.chat.ChatMessage;
import sojamo.chat.ChatNumerics;

/**
 * AIMMessage contains the content of a message received.
 */
public class IMMessage implements ChatMessage, ChatNumerics {
    private String _myFrom = "";

    private String _myMessage = "";

    private int _myType = DEFAULT;

    private String _myChannel = "";

    private final IMChat _myConnection;

    private boolean isPrivate = true;

    private boolean isPublic = false;

    protected IMMessage(final IMChat theAIM, final String theChannel,
	    final String theFrom, final String theMessage) {
	_myConnection = theAIM;
	_myFrom = theFrom;
	_myMessage = theMessage;
	_myChannel = theChannel;
    }

    protected void setType(final int theType) {
	_myType = theType;
    }

    protected void setChannel(final String theChannel) {
	_myChannel = theChannel;
    }

    /**
     * returns the origin of the message. this is useful if you have differenr
     * instances of type AIM, IRC running. with origin() you can identify the
     * origin of this message. e.g.<br />
     * if(myAimInstance.origin()==ChatP5.AIM) {<br /> // do something;<br /> }<br />
     * 
     * @return int
     * @shortdesc returns the origin of the message.
     */
    public int origin() {
	return AIM;
    }

    /**
     * get the chat channel from which this message was sent from.
     * 
     * @return String
     */
    public String channel() {
	return _myChannel;
    }

    /**
     * get the name of the buddy this message has been sent from.
     * 
     * @return String
     */
    public String from() {
	return _myFrom;
    }

    /**
     * get the content of this message.
     * 
     * @return String
     */
    public String message() {
	return _myMessage;
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
     * @return ChatClient
     */
    public ChatClient connection() {
	return _myConnection;
    }

    /**
     * returns true or false if the message is a private message.
     * 
     * @return boolean
     */
    public boolean isPrivate() {
	return isPrivate;
    }

    /**
     * returns true or false if this message is a public message.
     * 
     * @return boolean
     */
    public boolean isPublic() {
	return isPublic;
    }

}
