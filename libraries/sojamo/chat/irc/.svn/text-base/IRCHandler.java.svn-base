package sojamo.chat.irc;


import sojamo.chat.CStringUtils;
import sojamo.chat.ChatClient;
import java.util.ArrayList;

/**i
 * @invisible
 */
public abstract class IRCHandler
    extends ChatClient {

  protected String _myLatestChannel;

  protected final ArrayList _myIgnoreList = new ArrayList();

  /**
   * send a raw command to IRC. e.g.
   * PRIVMSG someuser :here is the text message
   * sending messages this way, needs a special formatting.
   * you should be familiar with the IRC protocol.
   * take a look at e.g. http://www.irchelp.org/irchelp/rfc/rfc2812.txt
   * or your prefered IRC protocol for the appropriate syntax.
   * @param theCommand String
   * @shortdesc send messages in raw IRC protocol format.
   */
  public abstract void sendCommand(String theCommand);


  protected abstract void close();


  /**
   *
   * @param theServerAddress String
   * @param theHops int
   * @param theInfo String
   * @invisible
   */
  public void server(String theServerAddress, int theHops, String theInfo) {
    sendCommand("SERVER " + theServerAddress + " " + theHops + " :" + theInfo);
  }



  /**
   *
   * @param theServerAddress String
   * @param theComment String
   * @invisible
   */
  public void squit(final String theServerAddress,
                    final String theComment) {
    sendCommand("SQUIT " + theServerAddress + " :" + theComment);
  }



  /**
   * requests the userhost of the user in question.
   * Usually used by scripts or bots to retrieve
   * userhost information.
   * @param theNickname String
   */
  public void userhost(final String theNickname) {
    sendCommand("USERHOST " + theNickname);
  }



  /**
   * join a channel on the IRC server you are connected to.
   * @param theChannel String
   */
  public void join(final String theChannel) {
    String myChannel = theChannel.toLowerCase();
    _myLatestChannel = myChannel;
    sendCommand("JOIN " + myChannel);
  }



  /**
   * join a channel on the IRC server you are connected to,
   * using a key as password.
   * @param theChannel String
   * @param theKey String
   */
  public void join(final String theChannel,
                   final String theKey) {
    String myChannel = theChannel.toLowerCase();
    _myLatestChannel = myChannel;
    sendCommand("JOIN " + myChannel + " " + theKey);
  }


  /**
   * leave an IRC channel you are currently connected to at.
   * @param theChannel String
   */
  public void part(final String theChannel) {
    String myChannel = theChannel.toLowerCase();
    sendCommand("PART " + myChannel);
  }



  /**
   * quit your IRC session with a goodbye message
   * @param theGoodbyeString String
   */
  public void quit(final String theGoodbyeString) {
    sendCommand("QUIT :" + theGoodbyeString);
    close();
  }



  /**
   * change your loginname.
   * @param theName String
   */
  public void nick(final String theName) {
    _myUserName = RFC1459.filterString(theName);
    sendCommand("NICK " + theName);
  }



  /**
   * return your loginname you are using for your current IRC session.
   * @return String
   */
  public String name() {
    return _myUserName;
  }


  /**
   *
   * @param theUser String
   * @param thePassword String
   */
  public void oper(final String theUser,
                   final String thePassword) {
    sendCommand("OPER " + theUser + " " + thePassword);
  }



  /**
   * The user MODE's are typically changes which affect either how the
   * client is seen by others or what 'extra' messages the client is sent.
   * The available modes are as follows:<br />
   * a - user is flagged as away;<br />
   * i - marks a users as invisible;<br />
   * w - user receives wallops;<br />
   * r - restricted user connection;<br />
   * o - operator flag;<br />
   * O - local operator flag;<br />
   * s - marks a user for receipt of server notices.<br />
   * additional modes may be supported by your server.

   * @param thePrefix char
   * @param theValue char
   * @shortdesc set your user mode.
   */
  public void setMode(final char thePrefix,
                   final char theValue) {
    sendCommand("MODE " + name() + " " + thePrefix + "" + theValue);
  }



  /**
   * The MODE command is provided so that users may query and change the
   * characteristics of a channel.  For more details on available modes
   * and their uses, see "Internet Relay Chat: Channel Management" [IRC-CHAN].
   * setting channel modes may only be permitted by operators.
   * @param theChannelName String
   * @param thePrefix char
   * @param theModes String
   * @param theModeParameter String
   * @shortdesc set channel modes. this may only be permitted by operators.
   */
  public void setChannelmode(final String theChannelName,
                          final char thePrefix,
                          final String theModes,
                          final String theModeParameter) {
    sendCommand("MODE " + theChannelName + " " + thePrefix + "" + theModes + " " + theModeParameter);
  }



  /**
   * set the topic of a channel. this may only be permitted by operators.
   * @param theChannel String
   * @param theTopic String
   */
  public void setTopic(final String theChannel,
                       final String theTopic) {
    sendCommand("TOPIC " + theChannel + " :"+ ( (theTopic.length() > 0) ? theTopic : ""));
  }



  /**
   * requests the topic of a given channel.
   * @param theChannel String
   */
  public void topic(final String theChannel) {
    sendCommand("TOPIC " + theChannel);
  }



  /**
   * requests the names of all connected users in the specified channel.
   * @param theChannel String
   */
  public void names(String theChannel) {
    sendCommand("NAMES " + theChannel);
  }



  /**
   * requests the names of all connected users in the specified channellist.
   * @param theChannels String[]
   */
  public void names(String[] theChannels) {
    sendCommand("NAMES " + CStringUtils.arrayToString(theChannels));
  }



  /**
   * The list command is used to list channels and their topics.
   * If theChannel parameter is used, only the status of that channel is
   * displayed.
   * The listing report shows the channel name, the number of
   * people connected to each channel, and sometimes a
   * comment or description associated with the channel.
   * @shortdesc The list command is used to request a list of channels and their topics.
   */
  public void list() {
    sendCommand("LIST");
  }


  /**
   *
   * @param theChannel String
   */
  public void list(final String theChannel) {
    sendCommand("LIST " + theChannel);
  }


  /**
   *
   * @param theChannels String[]
   */
  public void list(final String[] theChannels) {
    sendCommand("LIST " + CStringUtils.arrayToString(theChannels));
  }



  /**
   * invite someone to a channel. inviting may be only permitted by operators.
   * @param theNickname String
   * @param theChannel String
   */
  public void invite(final String theNickname,
                     final String theChannel) {
    sendCommand("INVITE " + theNickname + " " + theChannel);
  }



  /**
   * kick a user from a channel. kicking may be only permitted by operators.
   * @param theNickname String
   * @param theComment String
   */
  public void kick(final String theChannel,
                   final String theNickname,
                   final String theComment) {
    sendCommand("KICK " + theChannel + " " + theNickname + ( (theComment.length() > 0) ? " :" + theComment : ""));
  }


  /**
   *
   * @param theChannel String
   * @param theNickname String
   */
  public void kick(final String theChannel,
                   final String theNickname) {
    kick(theChannel, theNickname, "");
  }



  /**
   * send a private message to a user.
   * @param theReceiver String
   * @param theMessage String
   */
  public void privmsg(final String theReceiver,
                      final String theMessage) {
    sendCommand("PRIVMSG " + theReceiver + " :" + theMessage);
  }



  /**
   * send a private message to multiple users.
   * @param theReceivers String[]
   * @param theMessage String
   */
  public void privmsg(final String[] theReceivers,
                      final String theMessage) {
    sendCommand("PRIVMSG " + CStringUtils.arrayToString(theReceivers) + " :" + theMessage);
  }



  /**
   * the reason for NOTICE according to RCF1459, is to define
   * a way to send messages that should never generate an automatic
   * reply. The object is to avoid loops of responses between scripts/bots
   * @param theReceiver String
   * @param theMessage String
   * @shortdesc notice defines a way to send messages that should never generate an automatic reply.
   */
  public void notice(String theReceiver, String theMessage) {
    sendCommand("NOTICE " + theReceiver + " :" + theMessage);
  }



  /**
   * Provides Local and Global user information (Such as Current and Maximum user count).
   * e.g. the parameter() returned in a chatStatus Object looks like this:
   * Current Global Users: 4423  Max: 12491
   * The LUSERS command is used to get statistics about the size of the
   * IRC network.  If no parameter is given, the reply will be about the
   * whole net.  If a <mask> is specified, then the reply will only
   * concern the part of the network formed by the servers matching the
   * mask.  Finally, if the <target> parameter is specified, the request
   * is forwarded to that server which will generate the reply.
   * @shortdesc The LUSERS command is used to get statistics about the size of the IRC network.

   */
  public void lusers() {
    sendCommand("LUSERS");
  }



  /**
   * Provides version information of the IRCd software in usage.
   * @param theServer String
   */
  public void version(String theServer) {
    sendCommand("VERSION " + theServer);
  }



  /**
   * request stats of the current server.<br />
   * c - returns a list of servers which the server may connect to or allow connections from<br />
   * h - returns a list of servers which are either forced to be treated as leaves or allowed to act as hubs<br />
   * i - returns a list of hosts which the server allows a client to connect from;<br />
   * k - returns a list of banned username/hostname combinations for that server;<br />
   * l - returns a list of the server's connections, showing how<br />
   * @param theQuery char
   * @shortdesc request stats of the current server.
   */
  public void stats(char theQuery) {
    sendCommand("STATS " + theQuery);
  }


  /**
   *
   * @param theQuery char
   * @param theServer String
   */
  public void stats(char theQuery, String theServer) {
    sendCommand("STATS " + theQuery + " " + theServer);
  }



  /**
   * For channels which are invite only, you can "Knock" on a
   * channel to request an invite.
   * @param theChannelName String
   * @param theMessage String
   */
  public void knock(String theChannelName, String theMessage) {
    sendCommand("KNOCK " + theChannelName + " " + theMessage);
  }


  /**
   * With LINKS, a user can list all servernames, which are known by the
   * server answering the query.  The returned list of servers MUST match
   * the mask, or if no mask is given, the full list is returned.
   * @param theRemoteServer String
   * @shortdesc With LINKS, a user can list all servernames, which are known by the
   * server answering the query.
   */
  public void links(String theRemoteServer) {
    sendCommand("LINKS " + theRemoteServer);
  }


  /**
   *
   * @param theUser String
   */
  public void watch(String theUser) {
    String myUser = "";
    if (theUser.charAt(0) != '-' && theUser.charAt(0) != '+') {
      myUser = " +" + theUser;
    }
    else {
      myUser = " " + theUser;
    }
    sendCommand("WATCH" + myUser);
  }


  /**
   *
   * @param theUsers String[]
   */
  public void watch(String[] theUsers) {
    String myUsers = "";
    for (int i = 0; i < theUsers.length; i++) {
      if (theUsers[i].charAt(0) != '-' && theUsers[i].charAt(0) != '+') {
        myUsers = " +" + theUsers[i];
      }
      else {
        myUsers += " " + theUsers[i];
      }
    }
    sendCommand("WATCH" + myUsers);
  }



  /**
   * get the current time from the server.
   */
  public void time() {
    sendCommand("TIME");
  }



  /**
   * get the current time from a specific the server.
   * @param theServer String
   */
  public void time(String theServer) {
    sendCommand("TIME " + theServer);
  }



  /**
   * tracing a server or a nickname, usually only permitted by operators or admins.
   * @param theSource String
   */
  public void trace(String theSource) {
    sendCommand("TRACE " + theSource);
  }



  /**
   * The admin message is used to find the name of the administrator of the
   * given server, or current server if theServer parameter is omitted. Each
   * server must have the ability to forward ADMIN messages to other servers.
   * @param theServer String
   * @shortdesc find the name of the administrator of the server
   */
  public void admin(String theServer) {
    sendCommand("ADMIN " + theServer);
  }



  /**
   * The INFO command is required to return information which describes the
   * server: its version, when it was compiled, the patchlevel, when it was
   * started, and any other miscellaneous information which may be considered
   * to be relevant. you can also request info from the server if a specific
   * user is connected.
   * @param theServer String
   * @shortdesc reuqests information that describes the server.
   */
  public void requestInfo(String theSource) {
    sendCommand("INFO " + theSource);
  }


  /**
   * The WHO command is used by a client to generate a query which returns
   * a list of information which 'matches' the <mask> parameter given by
   * the client.  In the absence of the <mask> parameter, all visible
   * (users who aren't invisible (user mode +i) and who don't have a
   * common channel with the requesting client) are listed.  The same
   * result can be achieved by using a <mask> of "0" or any wildcard which
   * will end up matching every visible user.
   * The <mask> passed to WHO is matched against users' host, server, real
   * name and nickname if the channel <mask> cannot be found.
   * @param theName String
   * @shortdesc  The WHO command is used by a client to generate a query which returns
   * a list of information which 'matches' the <mask> parameter given by
   * the client.
   */
  public void who(String theMask) {
    sendCommand("WHO " + theMask);
  }


  /**
   * This command is used to query information about particular user.
   * The server will answer this command with several numeric messages
   * indicating different statuses of each user which matches the mask.
   * @param theName String
   * @shortdesc The whois command is used to query information about particular user.
   */

  public void whois(String theName) {
    sendCommand("WHOIS " + theName);
  }


  /**
   *
   * @param theServer String
   * @param theName String
   */
  public void whois(String theServer, String theName) {
    sendCommand("WHOIS " + theServer + " " + theName);
  }


  /**
   *
   * @param theName String
   */
  public void whowas(String theName) {
    sendCommand("WHOWAS " + theName);
  }


  /**
   *  Whowas asks for information about a nickname which no longer exists.
   * This may either be due to a nickname change or the user leaving IRC.
   * In response to this query, the server searches through its nickname
   * history, looking for any nicks which are lexically the same (no wild
   * card matching here).  The history is searched backward, returning the
   * most recent entry first.
   * @param theName String
   * @param theCount int ,number of how many nicknames should max be returned.
   * @shortdesc Whowas asks for information about a nickname which no longer exists.
   */
  public void whowas(String theName, int theCount) {
    sendCommand("WHOWAS " + theName + " " + theCount);
  }



  /**
   *
   * Leave a message explaining that you are not currently
   * paying attention to IRC. Whenever someone sends you a PRIVMSG
   * or does a WHOIS on you, they automatically see whatever
   * message you set. Using AWAY with no parameters marks
   * you as no longer being away.
   * @param theMessage String
   * @shortdesc tell the server you are away.
   */
  public void away(final String theMessage) {
    sendCommand("AWAY" + ( (theMessage.length() > 0) ? " " + theMessage : ""));
  }



  /**
   * tell the server you are back and available.
   */
  public void back() {
    sendCommand("AWAY");
  }


  /**
   *
   * @param theNickname String
   */
  public void ignore(String theNickname) {
    sendCommand("SILENCE " + ( (theNickname.charAt(0) == '+') ? "" : "+") + theNickname);
    _myIgnoreList.add( (theNickname.charAt(0) == '+') ? theNickname.substring(1) : theNickname);
  }



  /**
   * remove a previously added user from the ignorelist.
   * @param theNickname String
   */
  public void unignore(String theNickname) {
    sendCommand("SILENCE " + ( (theNickname.charAt(0) == '-') ? "" : "-") + theNickname);
    _myIgnoreList.remove( (theNickname.charAt(0) == '-') ? theNickname.substring(1) : theNickname);
  }

}
