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


import org.jivesoftware.smack.GroupChat;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

public class JabberChannelChat {

	private final Jabber _myParent;

	private final String _myChannel;

	private final String _mySubnet;

	private GroupChat _myGroupChat;

	public JabberChannelChat(Jabber theParent, String theChannel,
			String theSubnet) {
		_myParent = theParent;
		_myChannel = theChannel;
		_mySubnet = theSubnet;

		String myChannel = _myChannel + "@" + _mySubnet + "."
				+ _myParent.server();
		try {
			_myGroupChat = _myParent.connection().createGroupChat(myChannel);
			_myGroupChat.join(_myParent.username());

			// Next, create a packet listener. We use an anonymous inner class
			// for brevity.
			PacketListener myMessageListener = new PacketListener() {
				public void processPacket(Packet packet) {
					JabberMessage myJabberMessage = new JabberMessage(
							_myParent, _myChannel, packet);
					_myParent.sChat().callEvent(myJabberMessage);
				}
			};

			PacketListener myUserListener = new PacketListener() {
				public void processPacket(Packet packet) {
					// JabberMessage myJabberMessage = new
					// JabberMessage(_myParent,_myChannel,packet);
					// myJabberMessage.setType(ChatProperties.USER);
					// _myParent.chatP5().callEvent(myJabberMessage);
					System.out.println("Some groupevent happened");
				}
			};

			_myGroupChat.addParticipantListener(myUserListener);
			_myGroupChat.addMessageListener(myMessageListener);

		} catch (org.jivesoftware.smack.XMPPException e) {
//			Logger.printError("Jabber", "failed to log into "
//					+ _myParent.server() + " " + e);
		}
	}

	public void leave() {
		_myParent.remove(_myParent.CHAT, _myChannel + "@" + _mySubnet);
		_myGroupChat.leave();
	}

}
