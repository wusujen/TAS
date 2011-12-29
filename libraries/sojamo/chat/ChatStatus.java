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

/**
 * ChatStatus is a message sent to you by a chat server. the content of this
 * message contains control messages notifying you that e.g. someone went offline,
 * or left/joined a chat channel. these messages come with an id to distinguish
 * between the different functions of a message. ChatStatus messages are forwared
 * to the method chatStatus(ChatStatus theChatStatus).
 *
 */

public class ChatStatus
    implements ChatNumerics {

  private final int _myType;

  private String _myRawMessage;

  private final ChatClient _myConnection;

  private String _myUser = "";

  private int _myId = 0;

  private String _myMessage = "";

  /**
   *
   * @param theConnection ChatClient
   * @param theType int
   * @param theUser String
   * @param theAction int
   * @param theMessage String
   * @invisible
   */
  public ChatStatus(final ChatClient theConnection,
                    final int theType,
                    final String theUser,
                    final int theAction,
                    final String theMessage,
                    final String theRawMessage
      ) {
    _myConnection = theConnection;
    _myUser = theUser;
    _myId = theAction;
    _myType = theType;
    _myRawMessage = theMessage;
    int p = _myRawMessage.indexOf("PARAMS:");
    if (p != -1) {
      _myMessage = _myRawMessage.substring(p + 7);
      _myMessage = _myMessage.trim();
      if (_myMessage.startsWith(_myConnection.username() + " :")) {
        int myIndex = (_myConnection.username() + " :").length();
        _myMessage = _myMessage.substring(myIndex);
      }
      else if (_myMessage.startsWith(_myConnection.username())) {
        int myIndex = (_myConnection.username()).length();
        _myMessage = _myMessage.substring(myIndex);
      }
      _myMessage = _myMessage.trim();
    }
    if (_myMessage.length() < 3) {
      _myMessage = theMessage;
    }
    if (_myMessage.startsWith("=")) {
      int myIndex = _myMessage.indexOf(":");
      if (myIndex > 0) {
        _myMessage = _myMessage.substring(myIndex + 1);
      }
    }
    _myRawMessage = theRawMessage;
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
   *
   * @return String
   */
  public String user() {
    return _myUser;
  }


  /**
   *
   * @return int
   */
  public int id() {
    return _myId;
  }


  /**
   *
   * @return String
   */
  public String message() {
    return _myMessage;
  }


  /**
   *
   * @return String
   */
  public String rawMessage() {
    return _myRawMessage;
  }


  /**
   *
   * @return ChatClient
   */
  public ChatClient connection() {
    return _myConnection;
  }


  /**
   *
   * @return String
   * @invisible
   */
  public String toString() {
    String myString = "### ChatStatus \n";
    myString += "connection() " + _myConnection.toString() + "\n";
    myString += "type() " + _myType + "\n";
    myString += "user() " + _myUser + "\n";
    myString += "id() " + _myId + "\n";
    myString += "message() " + _myMessage + "\n";
    myString += "rawmessage() " + _myRawMessage + "\n";
    myString += "\n";
    return myString;
  }

}
