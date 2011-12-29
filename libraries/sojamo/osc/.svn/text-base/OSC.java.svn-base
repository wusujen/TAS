/**
 * sojamo.osc is a processing and java library for the open sound control protocol,
 * OSC. 
 * This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version. This library is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Andreas Schlegel (http://www.sojamo.de)
 */

package sojamo.osc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.IOException;
import java.util.Vector;

/**
 * sojamo.osc is an osc implementation for the programming environment
 * processing. osc is the acronym for open sound control, a network protocol
 * developed at cnmat, uc berkeley. open sound control is a protocol for
 * communication among computers, sound synthesizers, and other multimedia
 * devices that is optimized for modern networking technology and has been used
 * in many application areas. for further specifications and application
 * implementations please visit the official osc site.
 * 
 * @usage Application
 * @example oscSendReceive
 * @related OscProperties
 * @related OscMessage
 * @related OscBundle
 */

public class OSC {

    protected ArrayList _myOscPlugList = new ArrayList();

    protected Vector listeners;

    protected final Object parent;

    private Class _myParentClass;

    private Method _myEventMethod;

    private Class _myEventClass = OSCMessage.class;

    private boolean isEventMethod;

    private String _myEventMethodName = "oscEvent";

    /**
     * @invisible
     */
    public static final String VERSION = "0.1.1";
    
    // implement bonjour service
    // http://www.vidvox.com/phpBB2/viewtopic.php?t=3157
    
    // implement tuio protocol
    // implement wii osc protocol, double check glovepie
    //
    // other osc implementations
    // http://www.sciss.de/swingOSC/
    // 
    // have tempaltes to match address spaces.
    // http://www.osculator.net/wp/
    /**
     * @param theParent
     *                Object
     * @param theProperties
     *                OscProperties
     * @usage Application
     */
    public OSC(final Object theParent) {
	welcome();
	parent = theParent;
	isEventMethod = checkEventMethod();
	listeners = new Vector();
    }

    private void welcome() {
	System.out.println("sojamo.osc " + VERSION + " "
		+ "infos, comments, questions at http://www.sojamo.de/libraries/osc\n\n");
    }

    /**
     * check which eventMethod exists in the Object sojamo.osc was started from.
     * this is necessary for backwards compatibility for sojamo.osc because the
     * previous parameterType for the eventMethod was OscIn and is now
     * OscMessage.
     * 
     * @return boolean
     * @invisible
     */
    private boolean checkEventMethod() {
	_myParentClass = parent.getClass();
	try {
	    Method[] myMethods = _myParentClass.getDeclaredMethods();
	    for (int i = 0; i < myMethods.length; i++) {
		if (myMethods[i].getName().indexOf(_myEventMethodName) != -1) {
		    Class[] myClasses = myMethods[i].getParameterTypes();
		    if (myClasses.length == 1) {
			_myEventClass = myClasses[0];
			break;
		    }
		}
	    }

	} catch (Throwable e) {
	    System.err.println(e);
	}

	String tMethod = _myEventMethodName;
	if (tMethod != null) {
	    try {
		Class[] tClass = { _myEventClass };
		_myEventMethod = _myParentClass.getDeclaredMethod(tMethod,
			tClass);
		_myEventMethod.setAccessible(true);
		return true;
	    } catch (SecurityException e1) {
		// e1.printStackTrace();
		printWarning("OSC.plug",
			"### security issues in OSC.checkEventMethod(). "
				+ "(this occures when running in applet mode)");
	    } catch (NoSuchMethodException e1) {
	    }
	}
	// online fix, since an applet throws a security exception when calling
	// setAccessible(true);
	if (_myEventMethod != null) {
	    return true;
	}
	return false;
    }

    /**
     * get the current version of sojamo.osc.
     * 
     * @return String
     */
    public String version() {
	return VERSION;
    }

    /**
     * @invisible
     */
    public void dispose() {
    }

    public void addListener(OSCEventListener theListener) {
	listeners.add(theListener);
    }

    public void removeListener(OSCEventListener theListener) {
	listeners.remove(theListener);
    }

    public Vector listeners() {
	return listeners;
    }

    /**
     * osc messages can be automatically forwarded to a specific method of an
     * object. the plug method can be used to by-pass parsing raw osc messages -
     * this job is done for you with the plug mechanism. you can also use the
     * following array-types int[], float[], String[]. (but only as on single
     * parameter e.g. somemethod(int[] theArray) {} ).
     * 
     * @param theObject
     *                Object, can be any Object
     * @param theMethodName
     *                String, the method name an osc message should be forwarded
     *                to
     * @param theAddrPattern
     *                String, the address pattern of the osc message
     * @param theTypeTag
     *                String
     * @example oscPlug
     * @usage Application
     */
    public void plug(final Object theObject, final String theMethodName,
	    final String theAddrPattern, final String theTypeTag) {
	final OSCPlug myOscPlug = new OSCPlug();
	myOscPlug.plug(theObject, theMethodName, theAddrPattern, theTypeTag);
	_myOscPlugList.add(myOscPlug);
    }

    /**
     * @param theObject
     *                Object, can be any Object
     * @param theMethodName
     *                String, the method name an osc message should be forwarded
     *                to
     * @param theAddrPattern
     *                String, the address pattern of the osc message
     * @example oscPlug
     * @usage Application
     */
    public void plug(final Object theObject, final String theMethodName,
	    final String theAddrPattern) {
	final Class myClass = theObject.getClass();
	final Method[] myMethods = myClass.getDeclaredMethods();
	Class[] myParams = null;
	for (int i = 0; i < myMethods.length; i++) {
	    String myTypetag = "";
	    try {
		myMethods[i].setAccessible(true);
	    } catch (Exception e) {
	    }
	    if ((myMethods[i].getName()).equals(theMethodName)) {
		myParams = myMethods[i].getParameterTypes();
		OSCPlug myOscPlug = new OSCPlug();
		for (int j = 0; j < myParams.length; j++) {
		    myTypetag += myOscPlug.checkType(myParams[j].getName());
		}

		myOscPlug.plug(theObject, theMethodName, theAddrPattern,
			myTypetag);
		_myOscPlugList.add(myOscPlug);
	    }
	}
    }

    /**
     * 
     * @param theOscMessage
     *                OSCMessage
     */
    private void callMethod(final OSCMessage theOscMessage) {
	// forward the message to all OscEventListeners
	for (int i = listeners().size() - 1; i >= 0; i--) {
	    ((OSCEventListener) listeners().get(i)).oscEvent(theOscMessage);
	}

	/* check if the arguments can be forwarded as array */
	if (theOscMessage.isArray) {
	    for (int i = 0; i < _myOscPlugList.size(); i++) {
		OSCPlug myPlug = ((OSCPlug) _myOscPlugList.get(i));
		if (myPlug.isArray && myPlug.checkMethod(theOscMessage, true)) {
		    invoke(myPlug.getObject(), myPlug.getMethod(),
			    theOscMessage.argsAsArray());
		}
	    }
	}
	/* check if there is a plug method for the current message */
	for (int i = 0; i < _myOscPlugList.size(); i++) {
	    OSCPlug myPlug = ((OSCPlug) _myOscPlugList.get(i));
	    if (!myPlug.isArray && myPlug.checkMethod(theOscMessage, false)) {
		theOscMessage.isPlugged = true;
		invoke(myPlug.getObject(), myPlug.getMethod(), theOscMessage
			.arguments());
	    }
	}
	/* if no plug method was detected, then use the default oscEvent mehtod */
	printDebug("OSC.callMethod ", "" + isEventMethod);
	if (isEventMethod) {
	    try {
		invoke(parent, _myEventMethod, new Object[] { theOscMessage });
		printDebug("OSC.callMethod ", "invoking OSCMessage "
			+ isEventMethod);
	    } catch (ClassCastException e) {
		printError("OscHandler.callMethod",
			" ClassCastException." + e);
	    }
	}
    }

    private void invoke(final Object theObject, final Method theMethod,
	    final Object[] theArgs) {
	try {
	    theMethod.invoke(theObject, theArgs);
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    printError(
		    "OSC",
		    "ERROR. an error occured while forwarding an OscMessage\n "
		    + "to a method in your program. please check your code for any \n"
		    + "possible errors that might occur in the method where incoming\n "
		    + "OscMessages are parsed e.g. check for casting errors, possible\n "
		    + "nullpointers, array overflows ... .\n"
		    + "method in charge : "
		    + theMethod.getName() + "  " + e);
	}	
    }

    
    /**
     * incoming osc messages from an udp socket are parsed, processed and
     * forwarded to the parent.
     * 
     * @invisible
     * @param thePacket
     *                DatagramPacket
     * @param thePort
     *                int
     */
    public final void process(byte[] theBytes) {
	synchronized (this) {
	    OSCPacket p = OSCPacket.parse(theBytes);
	    if (p.isValid()) {
		if (p.type() == OSCPacket.BUNDLE) {
		    for (int i = 0; i < ((OSCBundle) p).size(); i++) {
			callMethod(((OSCBundle) p).getMessage(i));
		    }
		} else {
		    callMethod((OSCMessage) p);
		}
	    }
	    notifyAll();
	}
    }
    
    /**
     * @invisible
     * @param theBytes
     */
    public final void networkEvent(byte[] theBytes) {
	process(theBytes);
    }

    /**
     * sojamo.osc has a logging mechanism which prints out processes, warnings
     * and errors into the console window. e.g. turn off the error log with
     * setLogStatus(ERROR, OFF);
     * 
     * @param theIndex
     *                int
     * @param theValue
     *                int
     * @usage Application
     */
    public static void setLogStatus(final int theIndex, final int theValue) {
	set(theIndex, theValue);
    }

    /**
     * @param theValue
     */
    public static void setLogStatus(final int theValue) {
	for (int i = 0; i < ALL; i++) {
	    set(i, theValue);
	}
    }

    public static void set(int theIndex, int theValue) {
	if (theValue > -1 && theValue < 2) {
	    if (theIndex > -1 && theIndex < flags.length) {
		flags[theIndex] = theValue;
		return;
	    } else if (theIndex == ALL) {
		for (int i = 0; i < flags.length; i++) {
		    flags[i] = theValue;
		}
		return;
	    }
	}
    }

    public static void printError(String theLocation, String theMsg) {
	if (flags[ERROR] == ON) {
	    println("### " + getTime() + " ERROR @ " + theLocation + " "
		    + theMsg);
	}
    }

    public static void printProcess(String theLocation, String theMsg) {
	if (flags[PROCESS] == ON) {
	    println("### " + getTime() + " PROCESS @ " + theLocation + " "
		    + theMsg);
	}
    }

    public static void printWarning(String theLocation, String theMsg) {
	if (flags[WARNING] == ON) {
	    println("### " + getTime() + " WARNING @ " + theLocation + " "
		    + theMsg);
	}
    }

    public static void printInfo(String theLocation, String theMsg) {
	if (flags[INFO] == ON) {
	    println("### " + getTime() + " INFO @ " + theLocation + " "
		    + theMsg);
	}
    }

    public static void printDebug(String theLocation, String theMsg) {
	if (flags[DEBUG] == ON) {
	    println("### " + getTime() + " DEBUG @ " + theLocation + " "
		    + theMsg);
	}
    }

    public static void print(String theMsg) {
	System.out.print(theMsg);
    }

    public static void println(String theMsg) {
	System.out.println(theMsg);
    }

    public static void printBytes(byte[] byteArray) {
	for (int i = 0; i < byteArray.length; i++) {
	    print(byteArray[i] + " (" + (char) byteArray[i] + ")  ");
	    if ((i + 1) % 4 == 0) {
		print("\n");
	    }
	}
	print("\n");
    }

    public static String getTime() {
	Calendar cal = Calendar.getInstance();
	return "[" + (cal.get(Calendar.YEAR)) + "/"
		+ (cal.get(Calendar.MONTH) + 1) + "/"
		+ cal.get(Calendar.DAY_OF_MONTH) + " "
		+ cal.get(Calendar.HOUR_OF_DAY) + ":"
		+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND)
		+ "]";
    }

    /**
     * @invisible
     */
    public final static int ON = 0;
    
    /**
     * @invisible
     */
    public final static int OFF = 1;
    
    /**
     * @invisible
     */
    public final static int ERROR = 0;
    
    /**
     * @invisible
     */
    public final static int WARNING = 1;
    
    /**
     * @invisible
     */
    public final static int PROCESS = 2;
    
    /**
     * @invisible
     */
    public final static int INFO = 3;
    
    /**
     * @invisible
     */
    public final static int DEBUG = 4;
    
    /**
     * @invisible
     */
    public final static int ALL = 5;
    
    /**
     * @invisible
     */
    public static int[] flags = new int[] { ON, ON, ON, ON, OFF };
}
