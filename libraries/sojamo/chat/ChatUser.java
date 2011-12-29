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
 * @invisible
 */
public interface ChatUser {

    public final static int IGNORE = 0;

    public final static int UC_AVAILABLE = 0x00;

    public final static int UC_UNAVAILABLE = 0x01;

    public final static int UC_AOL = 0x02;

    public final static int UC_ADMIN = 0x04;

    public final static int UC_UNCONFIRMED = 0x08;

    public final static int UC_NORMAL = 0x10;

    public final static int NORMAL = 0;

    public final static int PERMIT = 1;

    public final static int DENY = 2;

    public String name();

    public boolean online();

    public boolean away();

    public int status();
}
