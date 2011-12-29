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


import java.util.Vector;
import sojamo.chat.ChatNumerics;


/**
 * a group of aim buddies.
 */
public class IMGroup {

  private int _myStatus = ChatNumerics.NORMAL;

  private final String _myName;

  private final Vector _myBuddies = new Vector();

  private final IMChat _myParent;

  protected IMGroup(IMChat theParent, final String theName) {
    _myParent = theParent;
    _myName = theName;

    if (_myName.equals("ERROR")) {
      _myStatus = ChatNumerics.ERROR;
    }
  }


  /**
   * add a buddy to this group.
   * @param theBuddy AIMBuddy
   */
  public void addBuddy(final IMBuddy theBuddy) {
    _myBuddies.add(theBuddy);
  }


  /**
   * remove a buddy from this group.
   * @param theBuddy AIMBuddy
   */
  public void removeBuddy(final IMBuddy theBuddy) {
    _myBuddies.remove(theBuddy);
  }


  /**
   * returns a list of buddies in this group.
   * @return AIMBuddy[]
   */
  public IMBuddy[] buddies() {
    IMBuddy[] myBuddies = new IMBuddy[_myBuddies.size()];
    for (int i = 0; i < myBuddies.length; i++) {
      myBuddies[i] = ( (IMBuddy) _myBuddies.get(i));
    }
    return myBuddies;
  }


  /**
   * returns an AIMBuddy Object of a specific buddy in this group.
   * @param theBuddyName String
   * @return AIMBuddy
   */
  public IMBuddy buddy(final String theBuddyName) {
    String myBuddyName = theBuddyName.toLowerCase();
    for (int i = 0; i < _myBuddies.size(); i++) {
      IMBuddy myBuddy = ( (IMBuddy) _myBuddies.get(i));
      if ( (myBuddy.name().toLowerCase()).equals(myBuddyName)) {
        return myBuddy;
      }
    }
    return new IMBuddy(_myParent, "ERROR", this, 'd');
  }



  public int status() {
    return _myStatus;
  }


  /**
   * returns the name of this group.
   * @return String
   */
  public String name() {
    return _myName;
  }



  public String toString() {
    String myString = "";
    myString += "*** GROUP: " + name() + "\n";
    for (int i = 0; i < _myBuddies.size(); i++) {
      myString += ( ( (IMBuddy) _myBuddies.get(i)).toString()) + "\n";
    }
    return myString;
  }

}
