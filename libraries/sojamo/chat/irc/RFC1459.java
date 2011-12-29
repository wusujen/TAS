/**
 *  ChatP5 is a processing and java library that implements
 *  different chat protocols like AIM, IRC, Jabber.
 *
 *  2006 by Andreas Schlegel
 *
 *  ChatP5.irc.RFC1459 adopted from
 *  Eteria IRC Client, an RFC 1459 compliant client program written in Java.
 *  Copyright (C) 2000-2001  Javier Kohen <jkohen at tough.com>
 *  http://eirc.sourceforge.net/

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
/*
 Eteria IRC Client, an RFC 1459 compliant client program written in Java.
 Copyright (C) 2000-2001  Javier Kohen <jkohen at tough.com>

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package sojamo.chat.irc;

import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;

/**
 * @invisible
 */
public class RFC1459 {
	private static final String RFC1459_rules = "< '_'< '-'< '`'< 0< 1< 2< 3< 4< 5< 6< 7< 8< 9< a,A< b,B< c,C< d,D< e,E< f,F< g,G< h,H< i,I< j,J< k,K< l,L< m,M< n,N< o,O< p,P< q,Q< r,R< s,S< t,T< u,U< v,V< w,W< x,X< y,Y< z,Z< '{','['< '}',']'< '|','\\'< '^','~'";

	private static final String RFC1459_chars = "_-`0123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ{[}]|\\^~";

	// private static RuleBasedCollator rbc;

	static public Collator getCollator() {
		RuleBasedCollator rbc;

		// The following code speeds up this method a lot, but using a single
		// Collator allows everyone to modify the Collator's Strength.
		// if (null != rbc) {
		// return rbc;
		// }

		try {
			rbc = new RuleBasedCollator(RFC1459_rules);
		} catch (ParseException e) {
			System.err.println(e);
			return null;
		} catch (IllegalArgumentException e) {
			System.err.println(e);
			return null;
		}

		rbc.setStrength(Collator.SECONDARY);

		return rbc;
	}

	static public boolean isDeclaredChar(char ch) {
		return (-1 != RFC1459_chars.indexOf(ch));
	}

	static public String filterString(String str) {
		char text[] = str.toCharArray();
		char new_text[] = new char[text.length];

		int i, j;

		for (i = j = 0; i < text.length; i++) {
			if (isDeclaredChar(text[i])) {
				new_text[j++] = text[i];
			}
		}

		return String.valueOf(new_text);
	}
}
