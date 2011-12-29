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

import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

public class HTTPRequest {

	protected String[] _myPath = {};

	protected HashMap _myQueryMap = new HashMap();

	protected final HashMap _myHeaderFieldMap;

	protected String _myRequest;

	protected PrintStream _myPrintStream;

	protected File _myRootPath;

	protected int _myMethod;

	public final static int ERROR = -1;

	public final static int GET = 0;

	public final static int POST = 1;

	public final static int HEAD = 2;

	public final static int OPTIONS = 3;

	public final static int PUT = 4;

	public final static int DELETE = 5;

	public final static int TRACE = 6;

	public final static int CONNECT = 7;

	protected HTTPServer _myServer;

	public HTTPRequest(final HTTPServer theServer, final String theRequest,
			final PrintStream thePrintStream, final HashMap theHeaderFields) {
		_myServer = theServer;
		_myRequest = theRequest;
		_myPrintStream = thePrintStream;
		_myHeaderFieldMap = theHeaderFields;
		parse();
	}

	/**
	 * returns the instance of the current HTTPServer.
	 * 
	 * @see HTTPServer
	 * @return HTTPServer HTTPRequest
	 */
	public HTTPServer server() {
		return _myServer;
	}

	public PrintStream printstream() {
		return _myPrintStream;
	}

	public String[] pathAsArray() {
		return _myPath;
	}
	
	public String report() {
		String myReport = "";
		myReport += "request:\t"+toString()+"\n";
		myReport += "decoded:\t"+decode()+"\n";
		myReport += "rootPath:\t"+rootPath()+"\n"; // the path to the file directory of this server
		myReport += "path:\t"+path()+"\n";
		myReport += "file:\t"+file()+"\n";
		return myReport;
	}
	
	/**
	 * returns the http request as sent from
	 * a browser/http-client as none decoded string.
	 * use decode() for a decoded version of the
	 * request query.
	 */
	public String toString() {
		return _myRequest;
	}

	/**
	 * returns a decoded query string. by default "UTF-8" decoding isused, but
	 * can be changed to one of the following charsets. US-ASCII, ISO-8859-1,
	 * UTF-8, UTF-16BE, UTF-16LE, UTF-16
	 * 
	 * http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html
	 * 
	 * @return String HTTPRequest
	 */
	public String decode() {
		return decode("UTF-8");
	}
	
	public String decode(String theCharset) {
		try {
			return URLDecoder.decode(_myRequest, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("### ERROR @ HTTPRequest.decode() " + e);
		}
		return _myRequest;
	}

	public HashMap queryMap() {
		return _myQueryMap;
	}

	public String query(String theQuery) {
		if (_myQueryMap.containsKey(theQuery)) {
			return (String) _myQueryMap.get(theQuery);
		}
		return "";
	}

	public String headerField(String theHeaderField) {
		if (_myHeaderFieldMap.containsKey(theHeaderField.toLowerCase())) {
			return (String) _myHeaderFieldMap.get(theHeaderField);
		}
		return "";
	}

	public void setRootPath(File theRootPath) {
		_myRootPath = theRootPath;
	}

	public File rootPath() {
		return _myRootPath;
	}

	private void parse() {
		if (_myRequest.charAt(0) == '/') {
			_myRequest = _myRequest.substring(1);
		}
		String s[] = HTTPServer.split(_myRequest, '?');
		if (s.length > 0) {
			parsePath(s[0]);
		}
		if (s.length == 2) {
			parseQuery(s[1]);
		}
	}

	private void parseQuery(String theString) {
		String myString = theString.replaceAll("&amp;", "&");
		String s[] = HTTPServer.split(myString, '&');
		for (int i = 0; i < s.length; i++) {
			String[] myQuery = HTTPServer.split(s[i], '=');
			if (myQuery.length == 2) {
				setQuery(myQuery[0], myQuery[1]);
			}
		}
	}

	private void parsePath(String theString) {
		_myPath = HTTPServer.split(theString, '/');
	}

	public void setQuery(String theKey, String theValue) {
		_myQueryMap.put(theKey, theValue);
	}

	public String[] queryKeys() {
		String s[] = {};
		_myQueryMap.keySet().toArray(s);
		return s;
	}

	public String[] queryValues() {
		String[] s = {};
		_myQueryMap.values().toArray();
		return s;
	}

	public String[] pathlist() {
		if (_myPath.length - 1 > 0) {
			String[] s = new String[_myPath.length - 1];
			System.arraycopy(_myPath, 0, s, 0, _myPath.length - 1);
			return s;
		}
		return new String[] {};
	}

	public String path() {
		String myPath = "/";
		String[] p = pathlist();
		for (int i = 0; i < p.length; i++) {
			myPath += (p[i] + File.separator);
		}
		return myPath;
	}

	public String file() {
		if (_myPath.length - 1 > -1) {
			return _myPath[_myPath.length - 1];
		} else {
			return "";
		}
	}

	public int method() {
		return _myMethod;
	}

	public String methodName() {
		return "";
	}
}
