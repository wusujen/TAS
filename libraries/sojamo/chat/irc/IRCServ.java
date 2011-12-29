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

/**
 * how to start my own channel
 * http://www.wyldryde.org/a/000576.php
 * http://www.wyldryde.org/commands/chanserv.php
 *
 * Hilfe zum IrCQ-Net
 * http://www.oesterchat.com/hilfe/icq/
 *
 */

package sojamo.chat.irc;


/**
 * @invisible
 */
public class IRCServ {

  public final IRCChat _myConnection;

  private final NickServ _myNickServ;

  private final ChanServ _myChanServ;

  public IRCServ(IRCChat theConnection) {
    _myConnection = theConnection;
    _myNickServ = new NickServ();
    _myChanServ = new ChanServ();
  }



  public NickServ nick() {
    return _myNickServ;
  }



  public ChanServ chan() {
    return _myChanServ;
  }


  /**
   * @invisible
   */
  public class NickServ {

    public void help() {
      _myConnection.sendCommand("PRIVMSG nickserv HELP");
    }



    public void register(String thePassword, String theEmail) {
      _myConnection.sendCommand("PRIVMSG nickserv REGISTER " + thePassword + " "
                        + theEmail);
    }



    public void identify(String thePassword) {
      _myConnection.sendCommand("PRIVMSG nickserv IDENTIFY " + thePassword);
    }



    public void auth(String theCode) {
      _myConnection.sendCommand("PRIVMSG nickserv AUTH " + theCode);
    }



    public void info(String theNick) {
      if (theNick.length() == 0) {
        theNick = _myConnection.name();
      }
      _myConnection.sendCommand("PRIVMSG nickserv INFO " + theNick);
    }



    public void set(String theStringA, String theStringB) {
      _myConnection.sendCommand("PRIVMSG nickserv SET " + theStringA + " "
                        + theStringB);
    }



    public void drop(String thePassword) {
      _myConnection.sendCommand("PRIVMSG nickserv DROP " + thePassword);
    }

  }

  /**
   * @invisible
   */
  public class ChanServ {

    public void help() {
      _myConnection.sendCommand("PRIVMSG chanserv HELP");
    }



    public void register(String theChannel, String thePassword,
                         String theDescription) {
      _myConnection.sendCommand("PRIVMSG chanserv REGISTER " + theChannel + " "
                        + thePassword + " " + theDescription);
    }



    public void identify(String thePassword) {
      _myConnection.sendCommand("PRIVMSG chanserv IDENTIFY " + thePassword);
    }



    public void info(String theChannel) {
      _myConnection.sendCommand("PRIVMSG chanserv INFO " + theChannel);
    }



    /**
     * @todo / msg chanserv set #Raumname Option Parameter Verschiedene
     *       Raumoptionen und Informationen sind einstellbar. (siehe help -
     *       Funktion) / msg chanserv set #Raumname password NeuesPasswort
     *       Setzt ein neues Passwort für den Raum. / msg chanserv set
     *       #Raumname email nick
     * @url.at Fügt zu den Rauminformationen eine E - Mailadresse hinzu. /
     *         msg chanserv set #Raumname url www.url.at Fügt den
     *         Rauminformationen eine URL hinzu. / msg chanserv set
     *         #Raumname entrymsg Text Dadurch erscheint beim Betreten des
     *         Raums die Begrüßungsnachricht Text.
     */
    public void set(String theChannel, String theOption, String theParameter) {
      _myConnection.sendCommand("PRIVMSG chanserv SET " + theChannel);
    }



    public void sop(String theChannel, String theNick) {
      // Fügt den Benutzer in die SuperOP - Liste hinzu mit allen Rechten
      // eines SOP.
      _myConnection.sendCommand("PRIVMSG chanserv sop " + theChannel + " add "
                        + theNick);
    }



    public void aop(String theChannel, String theNick) {
      // Fügt den Benutzer in die AutoOP - Liste hinzu mit allen Rechten
      // eines AOP.
      _myConnection.sendCommand("PRIVMSG chanserv aop " + theChannel + " add "
                        + theNick);
    }



    public void hop(String theChannel, String theNick) {
      // Fügt den Benutzer in die HalbOP - Liste hinzu mit allen Rechten
      // eines HOP.
      _myConnection.sendCommand("PRIVMSG chanserv hop " + theChannel + " add "
                        + theNick);
    }



    public void vop(String theChannel, String theNick) {
      // Fügt den Benutzer in die VoiceOP - Liste hinzu mit allen Rechten
      // eines VOP.
      _myConnection.sendCommand("PRIVMSG chanserv vop " + theChannel + " add "
                        + theNick);
    }



    public void op(String theChannel, String theNick) {
      // Gibt dem Benutzer ein @.
      _myConnection.sendCommand("PRIVMSG chanserv op " + theChannel + " "
                        + theNick);
    }



    public void deop(String theChannel, String theNick) {
      // Nimmt dem Benutzer das @weg.
      _myConnection.sendCommand("PRIVMSG chanserv deop " + theChannel + " "
                        + theNick);
    }



    public void voice(String theChannel, String theNick) {
      // Gibt dem Benutzer ein + .
      _myConnection.sendCommand("PRIVMSG chanserv voice " + theChannel + " "
                        + theNick);
    }



    public void devoice(String theChannel, String theNick) {
      // Nimmt dem Benutzer das + weg.
      _myConnection.sendCommand("PRIVMSG chanserv devoice " + theChannel + " "
                        + theNick);
    }



    public void drop(String thePassword) {
      // Löscht den Raum beim Server.Alle Informationen gehen verloren.Nur
      // der Founder darf das.
      _myConnection.sendCommand("PRIVMSG chanserv drop " + thePassword);
    }



    public void akick(String theChannel, String theNick) {
      // Fügt den Benutzer in die autmatische Kickban - Liste des Raums
      // hinzu.Man kann auch die Host eines Benutzers kickbannen.
      _myConnection.sendCommand("PRIVMSG chanserv akick " + theChannel + " add "
                        + theNick);
    }

    /*
     * / msg chanserv help access Zeigt die Hilfe zur Access -
     * Verwaltung(Liste der SOPs, AOPs uvm.).
     */

  }
}

/*
 * ChanServ @ user *** ChanServ allows you to register and control various
 * ChanServ @ user *** aspects of channels. ChanServ can often prevent ChanServ @
 * user *** malicious users from "taking over" channels by limiting ChanServ @
 * user *** who is allowed channel operator privileges. Available ChanServ @
 * user *** commands are listed below; to use them, type ChanServ @ user ***
 * /msg ChanServ command . For more information on a ChanServ @ user ***
 * specific command, type /msg ChanServ HELP command . ChanServ @ user ***
 * ChanServ @ user *** REGISTER Register a channel ChanServ @ user *** IDENTIFY
 * Identify yourself with your password ChanServ @ user *** DROP Cancel the
 * registration of a channel ChanServ @ user *** INFO Show channel options and
 * information ChanServ @ user *** SET Set channel options and information
 * ChanServ @ user *** ACCESS Maintain the overall channel access list ChanServ @
 * user *** SOP Maintain the SuperOp list ChanServ @ user *** AOP Maintain the
 * AutoOp list ChanServ @ user *** VOP Maintain the AutoVoice list ChanServ @
 * user *** AKICK Maintain the AutoKick list ChanServ @ user *** ChanServ @ user
 * *** Other commands: UNSET, LIST, INVITE, UNBAN, CLEAR, ChanServ @ user ***
 * LEVELS, OP, DEOP, VOICE, DEVOICE, ChanServ @ user *** HALFOP, DEHALFOP,
 * PROTECT, DEPROTECT, ChanServ @ user *** HOP ChanServ @ user *** ChanServ @
 * user *** Note that any channel which is not used for 14 days ChanServ @ user
 * *** (i.e. which no user on the channel's access list enters ChanServ @ user
 * *** for that period of time) will be automatically dropped.
 *
 */

/*
 * NickServ @ user *** NickServ allows you to "register" a nickname and
 * NickServ @ user *** prevent others from using it. If the nick is not used for
 * NickServ @ user *** 30 days, the registration will expire. The following
 * NickServ @ user *** commands allow for registration and maintenance of
 * NickServ @ user *** nicknames; to use them, type /msg NickServ command .
 * NickServ @ user *** For more information on a specific command, type NickServ @
 * user *** /msg NickServ HELP command . NickServ @ user *** NickServ @ user
 * *** REGISTER Register a nickname NickServ @ user *** IDENTIFY Identify
 * yourself with your password NickServ @ user *** ACCESS Modify the list of
 * authorized addresses NickServ @ user *** LINK Make your nick an alias for
 * another NickServ @ user *** SET Set options, including kill protection
 * NickServ @ user *** DROP Cancel the registration of a nickname NickServ @
 * user *** RECOVER Kill another user who has taken your nick NickServ @ user
 * *** RELEASE Regain custody of your nick after RECOVER NickServ @ user ***
 * NickServ @ user *** Other commands: UNLINK, UNSET, GHOST, INFO, LIST,
 * LISTCHANS, NickServ @ user *** STATUS NickServ @ user *** NickServ @ user ***
 * NOTICE: This service is intended to provide a way for NickServ @ user ***
 * IRC users to ensure their identity is not compromised. NickServ @ user *** It
 * is NOT intended to facilitate "stealing" of NickServ @ user *** nicknames
 * or other malicious actions. Abuse of NickServ NickServ @ user *** will result
 * in, at minimum, loss of the abused NickServ @ user *** nickname(s).
 */
