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


import java.util.HashMap;
import sojamo.chat.ChatChannel;
import sojamo.chat.ChatNumerics;


/**
 * An AIMChannel is similar to a chat channel. multiple aim buddies
 * can chat together in an AIMChannel.
 */
public class IMChannel
    implements ChatChannel {

  private int _myStatus = ChatNumerics.DEFAULT;

  private final String _myChannelId;

  private final String _myChannelName;

  private final IMChat _myConnection;

  private boolean hasJoined = false;

  private String _myHelloMessage;

  private HashMap _myChatBuddies = new HashMap();

  protected IMChannel(IMChat theConnection, String theChannelName,
                       String theRoomId, String theInviter, String theMessage) {
    _myConnection = theConnection;
    _myChannelName = theChannelName;
    _myChannelId = theRoomId;
    _myHelloMessage = _myConnection.chatHelloMessage();
    if (!_myChannelName.equals("ERROR")) {
      if (theInviter.length() > 0) {
        _myConnection.event(ChatNumerics.CHAT_INVITE, theInviter,
                            ChatNumerics.CHAT_INVITE,
                            "### you received a chatinvitation from " + theInviter
                            + " @ " + _myChannelName + "/" + _myChannelId
                            + " :" + theMessage);
        accept();
      }
    }
    else {
      _myStatus = ChatNumerics.ERROR;
    }
  }



  private void accept() {
    _myConnection.deliver(IMChat.DATA, "toc_chat_accept "
                          + IMChat.normalize(_myChannelId));
  }



  protected void updateChatBuddy(String[] theString, boolean theStatus) {
    if (!hasJoined) {
      _myConnection.deliver(IMChat.DATA, "toc_chat_send "
                            + IMChat.normalize(_myChannelId) + " \""
                            + IMChat.encode(_myHelloMessage) + "\"");
      hasJoined = true;

    }
    for (int i = 0; i < theString.length; i++) {
      if (!theString[i].equals(_myConnection.username())) {
        if (!IMChat.isContained(_myChatBuddies, theString[i])) {
          if (!IMChat
              .isContained(_myConnection.buddymap(), theString[i])) {
            _myConnection.mapBuddy("encounters", theString[i], 'b');
          }
          _myChatBuddies.put(theString[i], _myConnection
                             .buddy(theString[i]));
        }
        else {
          if (theStatus == false) {
            _myChatBuddies.remove(theString[i]);
          }
        }
      }
    }
  }



  public void invite(String theBuddyName, String theInviteMessage) {
    _myConnection.invite(theBuddyName, this, theInviteMessage);
  }



  public void evil(String theBuddyname, int theType) {
    _myConnection.evil(name(), theBuddyname, theType);
  }



  protected void handleMessage(final String theName,
                               final String theMode,
                               final String theMessage) {
    _myConnection.message(new IMMessage(_myConnection,
                                         name(),
                                         theName,
                                         theMessage));
  }


  /**
   * send a message to the chat channel. everyone can read the message.
   * @param theMessage String
   */
  public void sendMessage(final String theMessage) {
    _myConnection.deliver(IMChat.DATA, "toc_chat_send "
                          + IMChat.normalize(_myChannelId) + " \""
                          + IMChat.encode(theMessage) + "\"");
  }


  /**
   * send a message to a specific buddy in the chat channel.
   * @param theBuddy String
   * @param theMessage String
   */
  public void whisper(String theBuddy, String theMessage) {
    _myConnection.deliver(IMChat.DATA, "toc_chat_whisper "
                          + IMChat.normalize(_myChannelId)
                          + IMChat.normalize(theBuddy) + " \""
                          + IMChat.encode(theMessage) + "\"");
  }



  public int status() {
    return _myStatus;
  }


  /**
   * leave the chat channel.
   */
  public void leave() {
    _myConnection.leaveChat(this);
  }


  /**
   * returns the name of this chat channel.
   * @return String
   */
  public String name() {
    return _myChannelName;
  }


  /**
   *
   * @return String
   * @invisible
   */
  public String toString() {
    String s = "ChatChannel: " + _myChannelName + " with "
        + _myChatBuddies.size() + " users\n";
    String[] ss = new String[_myChatBuddies.size()];
    _myChatBuddies.keySet().toArray(ss);
    for (int i = 0; i < ss.length; i++) {
      s += (i + " " + ( (IMBuddy) _myChatBuddies.get(ss[i])));
    }
    return s;
  }


  /**
   * get a list of current buddies in the chat channel.
   * @return AIMBuddy[]
   */
  public IMBuddy[] users() {
    IMBuddy[] myUsers = new IMBuddy[_myChatBuddies.size()];
    String[] s = new String[_myChatBuddies.size()];
    _myChatBuddies.keySet().toArray(s);
    for (int i = 0; i < myUsers.length; i++) {
      myUsers[i] = ( (IMBuddy) _myChatBuddies.get(s[i]));
    }
    return myUsers;
  }


  /**
   * returns the id of this chat channel.
   * @return String
   */
  public String id() {
    return _myChannelId;
  }



  protected void dispose() {
    /**
     * @todo clean up buddies
     */
  }
}
