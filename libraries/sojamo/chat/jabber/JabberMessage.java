/**
 *  ChatP5 is a processing and java library that implements
 *  different chat protocols like AIM, IRC, Jabber.
 *
 *  2006 by Andreas Schlegel
 *  chatP5.jabber uses the java jabber api smack from jivesoftware
 *  more information @ http://www.jivesoftware.org/smack/
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

package sojamo.chat.jabber;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import sojamo.chat.ChatClient;
import sojamo.chat.ChatMessage;
import sojamo.chat.ChatNumerics;

public class JabberMessage implements ChatMessage, ChatNumerics {
	private String _myFrom = "";

	private String _myMessage = "";

	private int _myType = DEFAULT;

	private String _myChannel = "";

	private final Jabber _myConnection;

	private final Packet _myJabberPacket;

	private final Message _myJabberMessage;

        private boolean isPrivate = true;

        private boolean isPublic = false;


	public JabberMessage(final Jabber theJabber,
                             final String theChannel,
                             final Packet theJabberPacket) {
		_myConnection = theJabber;
		_myJabberPacket = theJabberPacket;
		_myJabberMessage = (Message) _myJabberPacket;
		_myFrom = _myJabberPacket.getFrom();
		_myMessage = _myJabberMessage.getBody();
		_myChannel = theChannel;
	}

	public void setType(final int theType) {
		_myType = theType;
	}

	public void setChannel(final String theChannel) {
		_myChannel = theChannel;
	}

	public int origin() {
		return JABBER;
	}

	public String channel() {
		return _myChannel;
	}

	public String from() {
		return _myFrom;
	}

	public String message() {
		return _myMessage;
	}

	public int type() {
		return _myType;
	}

	public ChatClient connection() {
		return _myConnection;
	}


        public boolean isPrivate() {
          return isPrivate;
        }


        public boolean isPublic() {
          return isPublic;

        }

	public Message jabbermessage() {
		return _myJabberMessage;
	}

	public Packet jabberpacket() {
		return _myJabberPacket;
	}

}
