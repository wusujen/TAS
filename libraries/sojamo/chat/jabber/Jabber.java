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

/*
 jabber server : http://danga.com/djabberd/
 */
package sojamo.chat.jabber;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import sojamo.chat.ChatClient;
import sojamo.chat.ChatNumerics;
import sojamo.chat.SChat;


public class Jabber
    extends ChatClient implements ChatNumerics {

  private final String _myServer;

  private final String _myUsername;

  private final String _myPassword;

  private final SChat _myChatP5;

  private XMPPConnection _myConnection;

  private Presence _myPresence;

  private Roster _myRoster;

  private final HashMap _myChatMap = new HashMap();

  private final HashMap _myGroupChatMap = new HashMap();

  public Jabber(SChat theChatP5, String theUsername, String thePassword,
                String theServer) {

    _myChatP5 = theChatP5;
    _myServer = theServer;
    _myUsername = theUsername;
    _myPassword = thePassword;

    try {
      _myConnection = new XMPPConnection(_myServer);
      _myConnection.login(_myUsername, _myPassword);
      // Create a new presence. Pass in false to indicate we're
      // unavailable.
      _myPresence = new Presence(Presence.Type.AVAILABLE);

      _myRoster = _myConnection.getRoster();

      try {
        Thread.sleep(2000);
      }
      catch (InterruptedException e) {

      }

      for (Iterator i = _myRoster.getEntries(); i.hasNext(); ) {
        RosterEntry myEntry = ( (RosterEntry) i.next());
        System.out.println(myEntry + " " + myEntry.getName() + " "
                           + myEntry.getStatus());
      }

      // Create a packet filter to listen for new messages from a
      // particular
      // channel. We use an AndFilter to combine two other filters.
      PacketFilter filter = new AndFilter(new PacketTypeFilter(
          Message.class), new FromContainsFilter(server()));

      // First, register a packet collector using the filter we created.
      PacketCollector myCollector = connection().createPacketCollector(
          filter);

      // Normally, you'd do something with the collector, like wait for
      // new packets.
      final Jabber myThis = this;
      PacketListener myListener = new PacketListener() {
        public void processPacket(Packet packet) {
          System.out.println(">> " + packet.getFrom() + " "
                             + ( (Message) packet).getBody());
        }
      };
      // Register the listener.
      connection().addPacketListener(myListener, filter);
      ConnectionListener myConnectionListener;

      RosterListener myRosterListener = new RosterListener() {
        public void entriesAdded(Collection collection) {
          System.out.println("entries added");
        }



        public void entriesUpdated(Collection collection) {
          System.out.println("entries updated");
        }



        public void entriesDeleted(Collection collection) {
          System.out.println("entries deleted");
        }



        public void presenceChanged(String theString) {
          System.out.println("Presence Changed for " + theString);
          int myIndex = theString.indexOf('/');
          Presence p;
          if (myIndex != -1) {
            p = presence(theString.substring(0, myIndex));
          }
          else {
            p = presence(theString);
          }
          System.out.println(">> presence " + p.getStatus() + " "
                             + p.getType() + " " + p.getMode() + " "
                             + p.getPriority());
          available("i am here");
        }

      };
      connection().getRoster().addRosterListener(myRosterListener);

    }
    catch (org.jivesoftware.smack.XMPPException e) {
//      Logger.printError("Jabber", "failed to log into " + _myServer + " "
//                        + e);
    }

  }



  public void addGroup(String theGroup) {
    if (_myRoster.getGroup(theGroup) == null) {
      _myRoster.createGroup(theGroup);
    }
  }



  public void addUser(String theUser, String theAlias, String[] theGroups) {
    // if(!_myRoster.contains(theUser)) {
    try {
      _myRoster.createEntry(theUser, theAlias, theGroups);
    }
    catch (org.jivesoftware.smack.XMPPException e) {

    }
    // }
  }



  public Presence presence(String theUser) {
    String myUser = (theUser.indexOf('@') == -1) ? (theUser + "@" + _myServer)
        : theUser;
    Presence p = _myRoster.getPresence(myUser);
    if (p != null) {
      return p;
    }
    else {
      if (_myRoster.contains(myUser)) {
        return new Presence(Presence.Type.UNAVAILABLE);
      }
      else {
        return new Presence(Presence.Type.ERROR);
      }
    }
  }



  protected static boolean isContained(final HashMap theMap,
                                       final String theKey) {
    return theMap.containsKey(theKey);
  }



  public void away(String theAwayMessage) {
    setStatus(Presence.Type.UNAVAILABLE, theAwayMessage);
  }



  public void available(String theAvailableMessage) {
    setStatus(Presence.Type.AVAILABLE, theAvailableMessage);
  }



  public void setStatus(Presence.Type theType, String theStatusMessage) {
    _myPresence.setType(theType);
    _myPresence.setStatus(theStatusMessage);
    _myConnection.sendPacket(_myPresence);
  }



  private JabberChat chat(String theUser, String theServer) {
    if (!isContained(_myChatMap, theUser + "@" + theServer)) {
      JabberChat myJabberChat = new JabberChat(this, theUser, theServer);
      _myChatMap.put(theUser + "@" + theServer, myJabberChat);
      return myJabberChat;
    }
    else {
      return ( (JabberChat) _myChatMap.get(theUser + "@" + theServer));
    }
  }



  public JabberChat chat(String theUser) {
    int myIndex = theUser.indexOf('@');
    if (myIndex == -1 || (myIndex + 1) <= theUser.length()) {
      return chat(theUser, _myServer);
    }
    else {
      return chat(theUser.substring(0, myIndex), theUser.substring(
          myIndex + 1, theUser.length()));
    }
  }



  // public JabberUser user() {
  //
  // }
  //

  public JabberChannelChat join(String theChannel, String theSubnet) {
    if (!isContained(_myGroupChatMap, theChannel + "@" + theSubnet)) {
      JabberChannelChat myJabberGroupChat = new JabberChannelChat(this,
          theChannel, theSubnet);
      _myGroupChatMap
          .put(theChannel + "@" + theSubnet, myJabberGroupChat);
      return myJabberGroupChat;
    }
    else {
      return ( (JabberChannelChat) _myGroupChatMap.get(theChannel + "@"
          + theSubnet));
    }
  }



  public JabberChannelChat channel(String theChannel, String theSubnet) {
    return join(theChannel, theSubnet);
  }



  public String username() {
    return _myUsername;
  }



  public XMPPConnection connection() {
    return _myConnection;
  }



  public String server() {
    return _myServer;
  }



  protected SChat sChat() {
    return _myChatP5;
  }



  protected void remove(int theChatType, String theKey) {
    switch (theChatType) {
      case (PRIVATE):
        _myChatMap.remove(theKey);
        break;
      case (CHAT):
        _myGroupChatMap.remove(theKey);
        break;
    }
  }



  public static void main(String args[]) {
    new Jabber(null, "sojamobot", "omasoj", "jabber.org");
  }
}
