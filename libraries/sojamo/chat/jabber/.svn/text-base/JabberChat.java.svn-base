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



import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class JabberChat {
	private final Chat _myChat;

	private final Jabber _myParent;

	private final String _myUser;

	private final String _myServer;

	public JabberChat(Jabber theParent, String theUser, String theServer) {
		_myParent = theParent;
		_myUser = theUser;
		_myServer = theServer;
		// Assume we've created an XMPPConnection name "connection".
		_myChat = _myParent.connection().createChat(_myUser + "@" + _myServer);

		// Next, create a packet listener. We use an anonymous inner class for
		// brevity.
		PacketListener myListener = new PacketListener() {
			public void processPacket(Packet packet) {
				JabberMessage myJabberMessage = new JabberMessage(_myParent,
						"", packet);
				_myParent.sChat().callEvent(myJabberMessage);
			}
		};

		_myChat.addMessageListener(myListener);
	}

	public String user() {
		return _myChat.getParticipant();
	}

	public void send(String theMessage) {
		Message myMessage = _myChat.createMessage();
		myMessage.setBody(theMessage);
		send(myMessage);
	}

	public void send(Message theMessage) {
		try {
			_myChat.sendMessage(theMessage);
		} catch (org.jivesoftware.smack.XMPPException e) {
//			Logger.printError("JabberChat.send", "failed to send message to "
//					+ _myUser + " " + e);
		}

	}

	public void leave() {
		_myParent.remove(_myParent.PRIVATE, _myUser + "@" + _myServer);
	}

	public Message message() {
		return _myChat.createMessage();
	}
}
