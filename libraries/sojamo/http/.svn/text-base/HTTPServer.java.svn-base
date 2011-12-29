/**
 *  NetP5 is a processing and java library for tcp and udp ip communication.
 *
 *  2006 by Andreas Schlegel
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA
 *
 * @author Andreas Schlegel (http://www.sojamo.de)
 *
 */

package sojamo.http;

/**
 * An example of a very simple, multi-threaded HTTP server. Implementation notes
 * are in the comments in the source code.
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Vector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HTTPServer implements HTTPConstants, Runnable {
	
	// TODO
	// implement keep alive threadss
	// http://www.io.com/~maus/HttpKeepAlive.html
	// http://java.sun.com/j2se/1.5.0/docs/guide/net/http-keepalive.html
	//
	// url encoding
	// http://www.permadi.com/tutorial/urlEncoding/
	//
	// HTTP protocols
	// --------------------------------------------
	// JSON
	// http://www.xul.fr/ajax-javascript-json.html
	// http://www.json.org/example.html
	// http://www.json.org/java/
	//
	// birdfeeder
	// http://brdfdr.com/
	//
	//
	// iphone multitouch / javascript
	// http://theclevermonkey.blogspot.com/2009/02/javascript-multi-touch-api-for-safari.html
	// http://www.pillowsopher.com/blog/?p=79
	// http://createdigitalmotion.com/2009/04/20/tuio-multitouch-control-on-the-iphone-now-via-a-browser-hack-since-the-app-was-rejected/#comments
	// http://carpe.ambiprospect.com/slider/archive/v1.3/
	
	
	static PrintStream log = null;

	/*
	 * our server's configuration information is stored in these properties
	 */
	protected static Properties props = new Properties();

	/* Where httpListeners threads stand idle */
	protected Vector threads = new Vector();

	protected Vector httpListener = new Vector();

	/* the web server's virtual root */
	protected File rootpath;

	/* timeout on client connections */
	protected int timeout = 0;

	/* max # worker threads */
	protected int httpHandlersNum = 5;

	protected String httpVersion = "1.1";

	public static String wanAddress;

	public static String lanAddress;

	private ServerSocket _myServerSocket;

	private int _myPort;

	private Thread _myThread;

	private Object _myParent;

	private Class _myParentClass;

	private Method _myHttpEventMethod;

	private String _myHttpEventMethodName = "httpEvent";

	private boolean isHttpEventMethod = false;

	private String _myPath = System.getProperty("user.dir") + File.separator + "www";

	public static boolean DEBUG = false;

	/**
	 * @invisible start an http server.
	 * @param thePort
	 *            int
	 */
	public HTTPServer(int thePort) {
		_myPort = thePort;
		loadProps();
		init();
	}

	/**
	 * start an http server.
	 * 
	 * @param thePort
	 *            int
	 */
	public HTTPServer(final Object theObject, final int thePort) {
		_myParent = theObject;
		_myPort = thePort;
		loadProps();
		init();
	}
	
	
	private void welcome() {
		System.out.println("sojamo.http 0.1.0 experimental.");
	}

	/**
	 * 
	 * @param thePath
	 *            String
	 */
	public void setRootPath(String thePath) {
		_myPath = thePath;
		rootpath = new File(_myPath);
	}

	public String rootPath() {
		return _myPath;
	}

	private void init() {
		welcome();
		/* start httpHandlerThreads threads */

		for (int i = 0; i < httpHandlersNum; ++i) {
			HTTPHandler myHttpHandler = new HTTPHandler(this);
			(new Thread(myHttpHandler, "HttpHandler #" + i)).start();
			threads.addElement(myHttpHandler);
		}

		_myServerSocket = null;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException iex) {
			// Logger.printError("HttpServer.start()",
			// "HttpServer sleep interuption " + iex);
			return;
		}
		try {
			_myServerSocket = new ServerSocket(_myPort);
		} catch (IOException e) {
			System.out.println("HttpServer.start(), HttpServer io Exception" + e);
			return;
		}

		isHttpEventMethod = checkHttpMethod();

		_myThread = new Thread(this);
		_myThread.start();
		// Logger.printProcess("HttpServer", "ServerSocket started @ " +
		// _myPort);
		if (DEBUG) {
			System.out.println("### path " + _myPath);
		}
	}

	public void run() {
		if (DEBUG) {
			System.out.println("sojamo.http.HttpServer is running.");
		}
		threadLoop: while (Thread.currentThread() == _myThread) {
			try {
				Socket mySocket = _myServerSocket.accept();
				HTTPHandler w = null;
				if(DEBUG) {
					System.out.println("#### starting  Server ."+mySocket);
				}
				synchronized (threads) {
					if (threads.isEmpty()) {
						HTTPHandler myHttpHandler = new HTTPHandler(this);
						myHttpHandler.setSocket(mySocket);
						Thread t = (new Thread(myHttpHandler, "new HttpHandler"));
						System.out.println("#### socket:"+mySocket+" / thread:"+t);
						t.start();
						
					} else {
						if(DEBUG) {
							System.out.println("#### reusing ...");
						}
						w = (HTTPHandler) threads.elementAt(0);
						threads.removeElementAt(0);
						w.setSocket(mySocket);
					}
				}

			} catch (IOException e) {
				System.out.println("HttpServer, IOException. Stopping server.");
				break threadLoop;
			}
		}
		close();
	}

	/**
	 * stop the http server.
	 */
	public void stop() {
		close();
	}

	public void close() {
		try {
			_myThread = null;

			if (_myServerSocket != null) {
				_myServerSocket.close();
				_myServerSocket = null;
			}
		} catch (IOException e) {
			// Logger.printError("HttpServer.dispose", "IOException " + e);
		}
	}

	public int port() {
		return _myPort;
	}

	public void process(HTTPRequest theHttpRequest) {
		if (isHttpEventMethod) {
			invoke(_myParent, _myHttpEventMethod, new Object[] { theHttpRequest });
		}
		if (httpListener.size() > 0) {
			synchronized (httpListener) {
				for (int i = 0; i < httpListener.size(); i++) {
					((HTTPListener) httpListener.elementAt(i)).httpEvent(theHttpRequest);
				}
			}
		} else {
			// send an empty HttpReply
		}
	}

	public void addListener(HTTPListener theListener) {
		synchronized (httpListener) {
			httpListener.add(theListener);
		}
	}

	public void removeListener(HTTPListener theListener) {
		synchronized (httpListener) {
			httpListener.remove(theListener);
		}
	}

	public File getFile(String theName) {
		if (theName.charAt(0) != '/') {
			theName = "/" + theName;
		}
		if (DEBUG) {
			System.out.println("HTTPServer.getFile : " + rootpath + " " + theName);
		}
		File myTargetFile = new File(rootpath, theName);
		if (myTargetFile.isDirectory()) {
			File myIndexFile = new File(myTargetFile, "index.html");
			if (myIndexFile.exists()) {
				myTargetFile = myIndexFile;
			}
		}

		return myTargetFile;
	}

	public static String replace(String theString, char theSourceChar, String theDestChars) {
		String myString = "";
		for (int i = 0; i < theString.length(); i++) {
			if (theString.charAt(i) == theSourceChar) {
				myString += theDestChars;
			} else {
				myString += theString.charAt(i);
			}
		}
		return myString;
	}

	public static String[] split(String theString, char theDelimiter) {
		// do this so that the exception occurs inside the user's
		// program, rather than appearing to be a bug inside split()
		if (theString == null) {
			return null;
		}
		// return split(what, String.valueOf(delim)); // huh

		char myChars[] = theString.toCharArray();
		int mySplitCount = 0; // 1;
		for (int i = 0; i < myChars.length; i++) {
			if (myChars[i] == theDelimiter) {
				mySplitCount++;
			}
		}

		if (mySplitCount == 0) {
			String mySplits[] = new String[1];
			mySplits[0] = new String(theString);
			return mySplits;
		}
		// int pieceCount = splitCount + 1;
		String mySplits[] = new String[mySplitCount + 1];
		int mySplitIndex = 0;
		int myStartIndex = 0;
		for (int i = 0; i < myChars.length; i++) {
			if (myChars[i] == theDelimiter) {
				mySplits[mySplitIndex++] = new String(myChars, myStartIndex, i - myStartIndex);
				myStartIndex = i + 1;
			}
		}
		// if (startIndex != chars.length) {
		mySplits[mySplitIndex] = new String(myChars, myStartIndex, myChars.length - myStartIndex);
		// }
		return mySplits;
	}

	/* load www-server.properties from java.home */
	protected void loadProps() {
		File f;
		try {
			f = new File(System.getProperty("user.dir") + File.separator + "lib" + File.separator
					+ "httpServer.properties");
			if (f.exists()) {
				InputStream is = new BufferedInputStream(new FileInputStream(f));
				props.load(is);
				is.close();
				String r = props.getProperty("root");
				if (r != null) {
					rootpath = new File(r);
					if (!rootpath.exists()) {
						throw new Error(rootpath + " doesn't exist as server root");
					}
				}
				r = props.getProperty("timeout");
				if (r != null) {
					timeout = Integer.parseInt(r);
				}
				r = props.getProperty("workers");
				if (r != null) {
					httpHandlersNum = Integer.parseInt(r);
				}
				r = props.getProperty("log");
				if (r != null) {
					p("opening log file: " + r);
					log = new PrintStream(new BufferedOutputStream(new FileOutputStream(r)));
				}
			}
		} catch (IOException e) {

		}

		/* if no properties were specified, choose defaults */
		if (rootpath == null) {
			setRootPath(_myPath);
		}
		if (timeout <= 1000) {
			timeout = 5000;
		}
		if (httpHandlersNum < 25) {
			httpHandlersNum = 5;
		}
		if (log == null) {
			log = System.out;
		}
	}

	/* print to stdout */
	protected static void p(String s) {
		if (DEBUG) {
			System.out.println(s);
		}
	}

	/* print to the log file */
	protected static void log(String s) {
		if (DEBUG) {
			synchronized (log) {
				log.println(s);
				log.flush();
			}
		}
	}

	private boolean checkHttpMethod() {
		if (_myParent != null) {
			_myParentClass = _myParent.getClass();
			try {
				Class[] myClass = { HTTPRequest.class };
				_myHttpEventMethod = _myParentClass.getDeclaredMethod(_myHttpEventMethodName,
						myClass);
				_myHttpEventMethod.setAccessible(true);
				return true;
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				// System.out.println("### NOTE. no geoEvent(GeoEvent theEvent)
				// method available.");
			}
		}
		return false;
	}

	private void invoke(final Object theObject, final Method theMethod, final Object[] theArgs) {
		try {
			theMethod.invoke(theObject, theArgs);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println("HTTPServer InvocationTargetException. " + e);
		}
	}

	public static void main(String[] a) throws Exception {
		int port = 8080;
		if (a.length > 0) {
			port = Integer.parseInt(a[0]);
		}
		new HTTPServer(port);
	}
}
