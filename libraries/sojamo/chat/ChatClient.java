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

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * @invisible
 */
public class ChatClient {

  /**
   * @invisible
   */
  public final static int MESSAGE = 0;

  /**
   * @invisible
   */
  public final static int EVENT = 1;

  protected Hashtable _myListenerTable = new Hashtable();

  protected boolean DEBUG = false;

  protected String _myUserName = "";

  /**
   * add chtListeners to the listener list.
   * @param theTarget String
   * @param theChatListener String
   */
  public void addListener(final String theTarget,
                          final ChatListener theChatListener) {
    if (_myListenerTable.containsKey(theTarget)) {
      Vector myCollection = (Vector) _myListenerTable.get(theTarget);
      if (!myCollection.contains(theChatListener)) {
        myCollection.add(theChatListener);
        println("### key @ " + theTarget + " exists. adding new value to the list. " + myCollection.size());
      }
    }
    else {
      Vector myCollection = new Vector();
      myCollection.add(theChatListener);
      _myListenerTable.put(theTarget, myCollection);
      println("### new key @ " + theTarget);
    }
  }


  /**
   * remove chatListeners from the listener list.
   * ChatListeners are not removed automatically when a channel is left or a chat user quit.
   * @shortdescr remove listeners.
   * @param theChatListener String
   */
  public void removeListener(final ChatListener theChatListener) {
    Enumeration myEnum = _myListenerTable.keys();
    while (myEnum.hasMoreElements()) {
      String myKey = (String) myEnum.nextElement();
      Vector myValue = (Vector) _myListenerTable.get(myKey);
      while (myValue.contains(theChatListener) == true) {
        myValue.remove(theChatListener);
        println("### removing Listener from " + myKey + ". remaining listeners for " + myKey + ": " + myValue.size());
        if (myValue.size() == 0) {
          _myListenerTable.remove(myKey);
          println("### removed key " + myKey + " from the map, because its container is empty.");
        }
      }
    }
  }


  /**
   * returns a list of currently registered listeners.
   * @return String[]
   */
  public String[] listeners() {
    String[] myListeners = new String[_myListenerTable.keySet().size()];
    _myListenerTable.keySet().toArray(myListeners);
    return myListeners;
  }


  protected boolean forwardToListeners(ChatStatus theStatus) {
    boolean isForward = false;
    if (_myListenerTable.containsKey(theStatus.user())) {
      Vector myListeners = getListeners(theStatus.user());
      Enumeration myEnum = myListeners.elements();
      while (myEnum.hasMoreElements()) {
        ( (ChatListener) myEnum.nextElement()).chatStatus(theStatus);
        isForward = true;
      }
    }
    return isForward;
  }


  protected boolean forwardToListeners(ChatMessage theMessage) {
    boolean isForward = false;
    if (_myListenerTable.containsKey(theMessage.from())) {
      Vector myListeners = getListeners(theMessage.from());
      Enumeration myEnum = myListeners.elements();
      while (myEnum.hasMoreElements()) {
        ( (ChatListener) myEnum.nextElement()).chatEvent(theMessage);
        isForward = true;
      }
    }
    return isForward;
  }


  /**
   *
   * @param theChatElement String
   */
  public void removeListener(final String theChatElement) {
    _myListenerTable.remove(theChatElement);
  }


  protected Vector getListeners(String theKey) {
    return (Vector) _myListenerTable.get(theKey);
  }


  /**
   *
   * @return String
   */
  public String username() {
    return _myUserName;
  }


  protected void println(String theString) {
    if (DEBUG) {
      System.out.println(theString);
    }
  }
}
