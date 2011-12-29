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
 *  MERCHANTABILITY or FITNESS FOR A
 * ICULAR PURPOSE.  See the
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


import java.util.HashMap;

import sojamo.chat.CStringUtils;
import sojamo.chat.ChatChannel;
import sojamo.chat.ChatMessage;
import sojamo.chat.ChatNumerics;

import java.util.Iterator;


/**
 * some description
 */
public class IRCChannel
    implements ChatChannel {

  private final IRCChat _myConnection;

  private String _myTopic = "";

  private boolean isConnected = true;

  private final String _myChannelName;

  private final IRCChat _myParent;

  private HashMap _myUserMap = new HashMap();

  private String _myOperator = "";

  private static HashMap _myCommandMap = new HashMap();

  static {
    _myCommandMap.put("privmsg", new Integer(1));
    _myCommandMap.put("msg", new Integer(2));
    _myCommandMap.put("whisper", new Integer(3));
    _myCommandMap.put("notice", new Integer(4));
    _myCommandMap.put("leave", new Integer(5));
    _myCommandMap.put("part", new Integer(6));
    _myCommandMap.put("invite", new Integer(7));
    _myCommandMap.put("kick", new Integer(8));
    _myCommandMap.put("names", new Integer(9));
    _myCommandMap.put("topic", new Integer(10));
    _myCommandMap.put("ignore", new Integer(11));
    _myCommandMap.put("unignore", new Integer(12));
  }



  protected IRCChannel(final IRCChat theConnection,
                       final String theChannelName) {
    _myConnection = theConnection;
    _myParent = theConnection;
    _myChannelName = theChannelName;
    if (_myChannelName.equals("ERROR")) {
      isConnected = false;
      System.out.println("ERROR @ IRCCHannel. you are not connected to this IRC channel.\n"+
                        "make sure you did not get banned from this channel or you joined the channel incorrectly.");
    } else {
      topic();
    }
  }



  /**
   * send a private message to a user.
   * @param theUser String
   * @param theMessage String
   */

  public void whisper(final String theUser,
                      final String theMessage) {
    privmsg(theUser, theMessage);
  }



  /**
   * sends a message to the channel.
   * @param theMessage String
   */
  public void sendMessage(final String theMessage) {
    privmsg(_myChannelName, theMessage);
    ChatMessage myIRCMessage = new IRCMessage(_myParent,
                                              theMessage,
                                              _myParent.name(),
                                              name(),
                                              ChatNumerics.DEFAULT);

    _myParent.callEvent(myIRCMessage);

  }



  /**
   * the following raw commands in a channel are supported:<br />
   * /privmsg, /msg, /notice<br />
   * for example:<br />
   * /privmsg hi there (-> message is readable by the channel)<br />
   * /privmsg username hi there (-> message goes out to the user with username)<br />
   * <br />
   * /whisper<br />
   * /leave<br />
   * /invite<br />
   * /kick<br />
   * /names<br />
   * /topic<br />
   * @param theString String
   * @shortdesc send raw irc commands to the channel.
   */
  public void sendCommand(final String theString) {
    /* its getting a little bit messy */
    if(isConnected) {
      String myString = theString.toLowerCase();
      myString = (myString.charAt(0) == '/') ? myString.substring(1) : myString;
      String[] myElements = CStringUtils.explode(myString);
      boolean isChannel = false;
      boolean isUser = false;
      if(myElements.length>1) {
        if(myElements[1].length()>0) {
          isChannel = (myElements[1].charAt(0) > 47) ? false:true;
          isUser = (_myUserMap.containsKey(myElements[1])) ? true:false;
        }
      }
      int myIndex = -1;
      if (_myCommandMap.containsKey(myElements[0])) {
        myIndex = ( (Integer) (_myCommandMap.get(myElements[0]))).intValue();
        if (myElements.length > 1) {
          switch (myIndex) {
            /* if first element is a privmsg or msg command */
            case (1):
            case (2):
              /* if second element is not a channel */
              if (!isChannel) {
                /* check if second element is a user or a channel */
                if (isUser) {
                  privmsg(myElements[1], CStringUtils.arrayToString(myElements, 2, myElements.length));
                  return;
                }
                /* if second element is not a user, add this channel's name*/
                else {
                  sendMessage(CStringUtils.arrayToString(myElements, 1, myElements.length));
                  return;
                }
              }
              break;
              /* if first element is whisper, then send a privmsg to second element */
            case (3):
              whisper(myElements[1], CStringUtils.arrayToString(myElements, 2, myElements.length));
              return;
              /* if first element is notice, then same procedure as for privmsg */
            case (4):
              if (!isChannel) {
                if (isUser) {
                  notice(myElements[1], CStringUtils.arrayToString(myElements, 2, myElements.length));
                  return;
                }
                else {
                  notice(_myChannelName, CStringUtils.arrayToString(myElements, 1, myElements.length));
                  return;
                }
              }
              break;
              /* if first element is invite */
            case (7):
              invite(myElements[1]);
              return;
              /* if first element is kick*/
            case (8):
              kick(myElements[1], CStringUtils.arrayToString(myElements, 2, myElements.length));
              return;
              /* if first element is names */
            case (9):
              _myConnection.names(myElements[1]);
              return;
              /* if first element is topic */
            case (10):

              /* if the length of the second element is not 0*/
              if (myElements[1].length() > 0) {
                /* check if second element is not a channel */
                if (!isChannel) {
                  /* if second element is not a channel then set the topic for this channel */
                  _myConnection.setTopic(_myChannelName, CStringUtils.arrayToString(myElements, 1, myElements.length));
                }
                else {
                  /* set the topic for another channel */
                  if (myElements.length != 2) {
                    _myConnection.setTopic(myElements[1], CStringUtils.arrayToString(myElements, 2, myElements.length));
                  }
                  /* get the topic of the channel given in the second element */
                  else {
                    _myConnection.topic(myElements[1]);
                  }
                }
              }
              else {
                /* reset the topic */
                _myConnection.setTopic(_myChannelName, "");
              }
              return;
            case(11):
              _myConnection.ignore(myElements[1]);
              return;
            case(12):
              _myConnection.unignore(myElements[1]);
              return;

            default:
              break;
          }
        }
        else {
          if (myIndex == 5 || myIndex == 6) {
            leave();
            return;
          }
          else if (myIndex == 9) {
            names();
            return;
          }
          else if (myIndex == 10) {
            topic();
            return;
          }
        }
      }
    }
    /*
     if the command in the first element didnt match the previous
     checking, then forward the command to our parent connection.
     */
    _myConnection.sendCommand(theString);
  }


  public void names() {
    _myConnection.names(_myChannelName);
  }


  /**
   * returns the name of this channel.
   * @return String
   */
  public String name() {
    return _myChannelName;
  }



  /**
   * leave this channel.
   */
  public void leave() {
    part();
  }

  public void part() {
    _myParent.part(this.name());
  }



  /**
   * a control method to check if you are connected to this channel.
   * @return boolean
   */
  public boolean isConnected() {
    return isConnected;
  }


  /**
   * returns the name of this channel's operator.
   * @return String
   */
  public String operator() {
    return _myOperator;
  }

  /**
   * set the topic of this channel. setting a topic may be only permitted by operators.
   * @param theTopic String
   */
  public void setTopic(final String theTopic) {
    _myConnection.setTopic(_myChannelName, theTopic);
  }


  protected void setCopyOfTopic(final String theTopic) {
  _myTopic = theTopic;
  }


  /**
   * returns the current topic of this channel.
   */
  public String topic() {
    if(_myTopic.length()>0) {
      return _myTopic;
    } else {
      _myConnection.topic(_myChannelName);
    }
    return "";
  }

  /**
   * invite someone to this channel. inviting may be only permitted by operators.
   * @param theNickname String
   */
  public void invite(final String theNickname) {
    _myConnection.invite(theNickname, _myChannelName);
  }



  /**
   * kick a user from this channel. kicking may be only permitted by operators.
   * @param theNickname String
   * @param theComment String
   */
  public void kick(final String theNickname,
                   final String theComment) {
    _myConnection.kick(_myChannelName, theNickname, theComment);
  }



  /**
   * send a private message to a specific user or chat channel.
   * @param theReceiver String
   * @param theMessage String
   */
  public void privmsg(final String theReceiver,
                      final String theMessage) {
    _myConnection.privmsg(theReceiver,theMessage);
  }



  /**
   * send a message to multiple users or multiple channels.
   * @param theReceivers String[]
   * @param theMessage String
   */
  public void privmsg(final String[] theReceivers,
                      final String theMessage) {
    _myConnection.privmsg(theReceivers,theMessage);
  }


  /**
   * send a notice message to a user or channel.
   * @param theReceiver String
   * @param theMessage String
   */
  public void notice(String theReceiver, String theMessage) {
    _myConnection.notice(theReceiver, theMessage);
  }


  /**
   *returns a list of usernames present in the channel. return type is a string array.
   * @return String[]
   */
  public String[] userlist() {
    String[] s = new String[_myUserMap.size()];
    _myUserMap.keySet().toArray(s);
    return s;
  }



  /**
   *returns the number of users currently present in this channel.
   * @return int
   */
  public int usersize() {
    return _myUserMap.size();
  }



  /**
   * returns an IRCUser Object of a user.
   * @param theUser String
   * @return IRCUser
   */
  public IRCUser user(final String theUser) {
    return (IRCUser) _myUserMap.get(theUser);
  }

  /**
   *
   * @return IRCUser[]
   */
  public IRCUser[] users() {
    IRCUser[] myUsers = new IRCUser[_myUserMap.size()];
    for(int i=0;i<myUsers.length;i++) {
         Iterator myIterator = _myUserMap.keySet().iterator();
      while (myIterator.hasNext()) {
         String myUser = (String)myIterator.next();
          myUsers[i] = (IRCUser)_myUserMap.get(myUser);
      }
    }
    return myUsers;
  }



  protected void addUser(final String[] theUsers) {
    for (int i = 0; i < theUsers.length; i++) {
      if (theUsers[i].length() > 1) {
        if (theUsers[i].charAt(0) == '@') {
          /* @ specifies the operator of the channel */
          theUsers[i] = theUsers[i].substring(1, theUsers[i].length());
          _myOperator = theUsers[i];
        }
        addUser(theUsers[i]);
      }
    }
  }



  protected void addUser(final String theUser) {
    if (theUser.length() > 1 && !_myUserMap.containsKey(theUser)) {
      _myUserMap.put(theUser, new IRCUser(_myParent, theUser, this));
    }

  }



  protected void addUser(final IRCUser theUser) {
    _myUserMap.put(theUser.name(), theUser);
  }



  protected void removeUser(final String theUser) {
    if (_myUserMap.containsKey(theUser)) {
      _myUserMap.remove(theUser);
    }
  }


  /**
   *
   * @return String
   * @invisible
   */
  public String toString() {
    String myList = "### channelinfo for " + name() + "\n";
    String[] myUsers = userlist();
    for (int i = 0; i < myUsers.length; i++) {
      myList += myUsers[i] + "\n";
    }
    myList += "### " + myUsers.length + " users @ " + name();
    return myList;
  }

}
