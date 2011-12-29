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

package sojamo.chat.jabber;

import org.jivesoftware.smack.packet.Message;

import sojamo.chat.ChatUser;

public class JabberUser implements ChatUser {
	private String _myName;

	private String _myAlias;

	private String[] _myGroups;

	private boolean isAway;

	private final Jabber _myParent;

	private Presence _myPresence;

	public JabberUser(Jabber theParent, String theName, String theAlias,
			String[] theGroups, Presence thePresence) {
		_myParent = theParent;
		_myName = theName;
		_myAlias = theAlias;
		_myGroups = theGroups;
		_myPresence = thePresence;
	}

	protected void setPresence(Presence thePresence) {
		_myPresence = thePresence;
	}

	protected void setName(String theName) {
		_myName = theName;
	}

	protected void setAlias(String theAlias) {
		_myAlias = theAlias;
	}

	protected void setAway(boolean theAway) {
		isAway = theAway;
	}

	public Presence presence() {
		return _myPresence;
	}

	public void send(String theMessage) {
		_myParent.chat(_myName).send(theMessage);
	}

	public void send(Message theMessage) {
		_myParent.chat(_myName).send(theMessage);
	}

	public JabberChat chat() {
		return _myParent.chat(_myName);
	}

	public String name() {
		return _myName;
	}

	public String alias() {
		return _myName;
	}

	public boolean away() {
		return isAway;
	}

	public int status() {
		return 0;
	}

	public boolean online() {
		return true;
	}

	public String toString() {
		return ("name: " + _myName + " alias: " + _myAlias + "\tonline: "
				+ online() + "\taway: " + away());
	}

}
