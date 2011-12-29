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
 * a ChatMessage contains the content of a message received from a
 * chat server you are connected to.
 */
public interface ChatMessage {

  /**
   * get the content of a message
   * @return String
   */
  public String message();


  /**
   *
   * @return int
   * @shortdescr returns the origin of the message.
   */
  public int origin();


  /**
   *
   * @return int
   * @invisible
   */
  public int type();


  /**
   * returns the name of the sender.
   * @return String
   */
  public String from();


  /**
   * returns the channel name where this message was sent from.
   * @return String
   */
  public String channel();


  /**
   * with the connection() method you can specify which IRC instance
   * received this message. this could be useful if you have multiple
   * chat instances running.
   * @return ChatClient
   */
  public ChatClient connection();


  /**
   * returns true or false if the message is private.
   * @return boolean
   */
  public boolean isPrivate();


  /**
   * returns true or false if the message is public.
   * @return boolean
   */
  public boolean isPublic();

}
