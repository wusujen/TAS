package sojamo.chat.im;

import java.util.ArrayList;

/**
 * @invisible
 */
public class IMList extends ArrayList {

	private static final long serialVersionUID = 1L;

	private final IMChat _myParent;

	public IMList(IMChat theParent) {
		super();
		_myParent = theParent;
	}

	public IMBuddy get(String theBuddyName) {
		return _myParent.buddy(theBuddyName);
	}

	public String[] list() {
		String[] s = new String[size()];
		toArray(s);
		return s;
	}

}
