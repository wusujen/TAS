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

package sojamo.chat.irc;


import sojamo.chat.ChatUser;

/**
 * some IRCUser description.
 */
public class IRCUser
    implements ChatUser {
  private String _myName;

  private IRCChannel _myChannel;

  private boolean isAway;

  private final IRCChat _myParent;


  protected IRCUser(IRCChat theIRC, IRCUser theUser) {
    _myParent = theIRC;
    _myName = theUser._myName;
    _myChannel = theUser._myChannel;
  }



  protected IRCUser(IRCChat theIRC, String theName, IRCChannel theChannel) {
    _myParent = theIRC;
    _myName = theName;
    _myChannel = theChannel;
  }



  protected void setName(final String theName) {
    _myName = theName;
  }



  protected void setAway(final boolean theAway) {
    isAway = theAway;
  }


  /**
   * send a message to this user.
   * @param theMessage String
   */
  public void sendMessage(final String theMessage) {
    _myParent.sendCommand("PRIVMSG " + name() + " " + theMessage);
  }


  /**
   *
   * @return String
   */
  public String name() {
    return _myName;
  }


  /**
   * check how this actually works. when sending an away message other users
   * dont get your away and back message.
   * @return boolean
   * @invisible
   */
  public boolean away() {
    return isAway;
  }


  /**
   *
   * @return int
   * @invisible
   */
  public int status() {
    return 0;
  }


  /**
   *
   * @return boolean
   * @invisible
   */
  public boolean online() {
    return true;
  }


  /**
   *
   * @return String
   * @invisible
   */
  public String toString() {
    return ("name: " + _myName + "\tonline: " + online() + "\taway: " + away());
  }

}
