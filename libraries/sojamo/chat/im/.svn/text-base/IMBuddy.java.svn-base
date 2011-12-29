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


import java.util.Date;

import sojamo.chat.ChatNumerics;
import sojamo.chat.ChatUser;


/**
 * AIMBuddy contains information about a buddy in your buddylist.
 */
public class IMBuddy
    implements ChatUser {

  private final String _myName;

  private boolean isOnline = false;

  private int _myEvil = 0;

  private long _mySignonTime = 0;

  private long _myIdleTime = 0;

  private int _myMode = UC_AVAILABLE;

  private int _myStatus = NORMAL;

  private final char[] _myStatusTypes = new char[] {'b', 'p', 'd'};

  private IMGroup _myGroup;

  private final IMChat _myParent;

  protected IMBuddy(final IMChat theParent,
                     final String theName,
                     final IMGroup theGroup,
                     final char theType) {
    _myParent = theParent;
    _myName = theName;
    _myGroup = theGroup;

    switch (theType) {
      case ('b'):
        _myStatus = NORMAL;
        break;
      case ('p'):
        _myStatus = PERMIT;
        break;
      case ('d'):
        _myStatus = DENY;
        if (_myName.equals("ERROR")) {
          _myStatus = ChatNumerics.ERROR;
        }
        break;
    }
  }



  protected void setOnline(final boolean theFlag) {
    isOnline = theFlag;
  }



  protected void setEvil(final int theEvil) {
    _myEvil = theEvil;
  }



  protected void setSignonTime(final long theTime) {
    _mySignonTime = theTime;
  }



  protected void setIdleTime(final long theTime) {
    _myIdleTime = theTime;
  }



  protected void setGroup(final IMGroup theGroup) {
    _myGroup = theGroup;
  }



  protected void setUC(String theUC) {
    _myMode = UC_AVAILABLE;
    if (theUC.charAt(0) == 'A') {
      _myMode |= UC_AOL;
    }
    switch (theUC.charAt(1)) {
      case 'A':
        _myMode |= UC_ADMIN;
        break;
      case 'U':
        _myMode |= UC_UNCONFIRMED;
        break;
      case 'O':
        _myMode |= UC_NORMAL;
        break;
      default:
        break;
    }
    if (theUC.length() == 3) {
      if (theUC.charAt(2) == 'U') {
        _myMode |= UC_UNAVAILABLE;
      }
    }
  }



  protected void setStatus(final int theType) {
    _myStatus = theType;
  }



  /**
   * send a message to this buddy.
   * @param theMessage String
   */
  public void sendMessage(String theMessage) {
    _myParent.sendMessage(_myName, theMessage);
  }



  /**
   * returns the group this buddy belongs to
   * @return AIMGroup
   */
  public IMGroup group() {
    return _myGroup;
  }



  /**
   *
   * @return int
   */
  public int status() {
    return _myStatus;
  }



  /**
   *
   * @return char
   */
  protected char statuschar() {
    return _myStatusTypes[status()];
  }



  /**
   * remove this buddy from your buddylist.
   */
  public void remove() {
    _myParent.removeBuddy(name());
  }



  /**
   * move this buddy to another group in your buddylist.
   * @param theGroupName String
   */
  public void move(final String theGroupName) {
    if (theGroupName.length() > 0) {
      _myParent.moveBuddy(name(), theGroupName);
    }
  }



  /**
   * set a permit to this buddy. this buddy will be moved to your
   * permitlist.
   */
  public void setPermit() {
    _myParent.permitBuddy(name());
    setStatus(PERMIT);
  }



  /**
   * set a deny to this buddy. this buddy will be moved to your
   * denylist.
   */
  public void setDeny() {
    _myParent.denyBuddy(name());
    setStatus(DENY);
  }



  /**
   * not yet supported.
   */
  public void setEvil() {

  }



  /**
   * remove this buddy from your permitlist.
   */
  public void removePermit() {
    _myParent.removePermitBuddy(name());
    setStatus(NORMAL);
  }



  /**
   * remove this buddy from your denylist.
   */
  public void removeDeny() {
    _myParent.removeDenyBuddy(name());
    setStatus(NORMAL);
  }



  /**
   * returns a long value indicating how long (in minutes)
   * this buddy has been idle.
   * @return long
   */
  public long idle() {
    return _myIdleTime;
  }



  /**
   * returns the name of this buddy.
   * @return String
   */
  public String name() {
    return _myName;
  }



  /**
   * returns false or ture for online status of this buddy.
   * @return boolean
   */
  public boolean online() {
    return isOnline;
  }



  /**
   * returns false or ture for away status of this buddy.
   * @return boolean
   */
  public boolean away() {
    return ( ( (_myMode & UC_UNAVAILABLE) == 1) ? true : false);
  }



  /**
   *
   * @return String
   */
  public String toString() {
    return (" * name: " + _myName + " * group: " + _myGroup.name()
            + "* status:" + status()
            + " * online: " + online() + " * signontime: " + signonTime()
            + " * signondate: " + signonDate() + " * idle: " + idle()
            + " * evil: " + evil() + "%" + " * away: " + ( ( (_myMode & UC_UNAVAILABLE) == 1) ? true
        : false));
  }



  /**
   * returns the signondate of this buddy. return type is Date.
   * @return Date
   */
  public Date signonDate() {
    String logintime = new String(signonTime() + "900");
    long myTime = Long.parseLong(logintime);
    return (new Date(myTime));
  }



  /**
   * returns the signontime of this buddy. return type is long.
   * @return long
   */
  public long signonTime() {
    return _mySignonTime;
  }



  /**
   * returns the evil status of this buddy.
   * @return int
   */
  public int evil() {
    return _myEvil;
  }

}
