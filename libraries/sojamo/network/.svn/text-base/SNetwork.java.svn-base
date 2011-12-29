package sojamo.network;

import java.util.Calendar;

// add mina network implementation from http://mina.apache.org/
// mina-core-1.0.8.jar is in the libs
// 
// some networking, opengl, openal action at
// http://today.java.net/pub/a/today/2006/10/10/development-of-3d-multiplayer-racing-game.html
//
// very fast messaging infrastructure
// http://www.zeromq.org/

// nice movie about the development of the internet
// http://www.lonja.de/motion/mo_history_internet.html


public abstract class SNetwork {
    
    public final static String VERSION = "0.1.1";

    static boolean DEBUG = true;

    /**
     * @related setNetworkProtocol ( )
     */
    public final static int UDP = 0;

    /**
     * @related setNetworkProtocol ( )
     */
    public final static int MULTICAST = 1;

    /**
     * @related setNetworkProtocol ( )
     */
    public final static int TCP = 2;
    /**
     * TODO authentification in AbstractTcpServer and AbstractUdpServer.
     * TcpServer.authentificationRequired(true/false);
     * UdpServer.authentificationRequired(true/false);
     */

    public final static int ON = 0;

    public final static int OFF = 1;

    public final static int ERROR = 0;

    public final static int WARNING = 1;

    public final static int PROCESS = 2;

    public final static int INFO = 3;

    public final static int DEBUG_FLAG = 4;

    public final static int ALL = 5;

    public static int[] flags = new int[] { ON, ON, ON, ON, OFF };

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
	if (flags[DEBUG_FLAG] == ON) {
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
}
