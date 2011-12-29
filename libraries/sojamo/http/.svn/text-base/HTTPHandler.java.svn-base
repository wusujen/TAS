package sojamo.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.HashMap;
import sojamo.http.HTTPConstants;

public class HTTPHandler implements Runnable, HTTPConstants {

	public final static int BUF_SIZE = 4096;

	/* buffer used for requests */
	byte[] _myBuffer;

	/* Socket to client we're handling */
	private Socket _mySocket;

	private final HTTPServer _myHttpServer;

	public HTTPHandler(HTTPServer theHttpServer) {
		_myHttpServer = theHttpServer;
		_myBuffer = new byte[BUF_SIZE];
		_mySocket = null;
	}

	synchronized void setSocket(Socket s) {
		_mySocket = s;
		notify();
	}

	public synchronized void run() {
		while (true) {
			if (_mySocket == null) {
				/* nothing to do */
				try {
					wait();
				} catch (InterruptedException e) {
					continue; /* should not happen */
				}
			}
			try {
				handleRequest();
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*
			 * go back in wait queue if there's fewer than numHandler
			 * connections.
			 */
			_mySocket = null;
			Vector myPool = _myHttpServer.threads;
			if (HTTPServer.DEBUG) {
				System.out.println("### _myHttpServer.threads " + _myHttpServer.threads.size());
			}
			synchronized (myPool) {
				if (myPool.size() >= _myHttpServer.httpHandlersNum) {
					/* too many threads, exit this one */
					return;
				} else {
					myPool.addElement(this);
				}
			}
		}
	}

	private void handleRequest() throws IOException {
		InputStream is = new BufferedInputStream(_mySocket.getInputStream());
		PrintStream ps = new PrintStream(_mySocket.getOutputStream());
		/*
		 * we will only block in read for this many milliseconds before we fail
		 * with java.io.InterruptedIOException, at which point we will abandon
		 * the connection.
		 */
		_mySocket.setSoTimeout(_myHttpServer.timeout);
		_mySocket.setTcpNoDelay(true);
		/* zero out the buffer from last time */
		for (int i = 0; i < BUF_SIZE; i++) {
			_myBuffer[i] = 0;
		}
		try {
			/*
			 * We only support HTTP GET/HEAD, and don't support any fancy HTTP
			 * options, so we're only interested really in the first line.
			 */
			int myRead = 0, r = 0;
			int myBuf_size = BUF_SIZE;
			outerloop: while (true) {
				r = is.read(_myBuffer, myRead, myBuf_size - myRead);
				if (r == -1) {
					/* EOF */
					return;
				}
				int i = myRead;
				myRead += r;
				if(HTTPServer.DEBUG) {
				System.out.print("bytes: "+myRead + ", ");
				}
				if (_myBuffer.length < myRead - 10) {
					if (HTTPServer.DEBUG) {
						System.out.println("### increasing buffersize to " + _myBuffer.length
								+ BUF_SIZE);
					}
					byte[] myBuffer = new byte[_myBuffer.length + BUF_SIZE];
					System.arraycopy(_myBuffer, 0, myBuffer, 0, _myBuffer.length);
					_myBuffer = myBuffer;
					myBuf_size = myBuffer.length;
				}
				for (; i < myRead; i++) {
					if (_myBuffer[i] == (byte) '\n' || _myBuffer[i] == (byte) '\r') {
						if (HTTPServer.DEBUG) {
							System.out.println("### length: " + myRead);
						}
						break outerloop;
					}
				}
			}

			/* are we doing a GET or just a HEAD */
			boolean isGET = false;
			/* beginning of file name */
			int myIndex;
			if (_myBuffer[0] == (byte) 'G' && _myBuffer[1] == (byte) 'E'
					&& _myBuffer[2] == (byte) 'T' && _myBuffer[3] == (byte) ' ') {
				isGET = true;
				myIndex = 4;
			} else if (_myBuffer[0] == (byte) 'H' && _myBuffer[1] == (byte) 'E'
					&& _myBuffer[2] == (byte) 'A' && _myBuffer[3] == (byte) 'D'
					&& _myBuffer[4] == (byte) ' ') {
				isGET = false;
				myIndex = 5;
			} else if (_myBuffer[0] == (byte) 'P' && _myBuffer[1] == (byte) 'O'
					&& _myBuffer[2] == (byte) 'S' && _myBuffer[3] == (byte) 'T'
					&& _myBuffer[4] == (byte) ' ') {
				isGET = false;
				myIndex = 5;
			}

			else {
				/* we don't support this method */
				ps.print("HTTP/" + _myHttpServer.httpVersion + " " + HTTP_BAD_METHOD
						+ " unsupported method type: ");
				ps.write(_myBuffer, 0, 5);
				ps.write(EOL.getBytes());
				ps.flush();
				_mySocket.close();
				return;
			}

			if (isGET) {
			}

			int i = 0;

			/*
			 * find the file name, from: GET /foo/bar.html HTTP/1.0 extract
			 * "/foo/bar.html"
			 */

			for (i = myIndex; i < myRead; i++) {
				if (_myBuffer[i] == (byte) ' ') {
					break;
				}
			}

			String s = new String(_myBuffer);

			String myUrl = s.substring(myIndex, i);

			if (HTTPServer.DEBUG) {
				System.out.println(s);
			}

			if (myUrl.equals("/favicon.ico")) {
				_myBuffer = new byte[BUF_SIZE];
				return;
			}

			String[] myRequest = s.split("\r");
			HashMap myHeaderFields = new HashMap();
			if (myRequest.length > 1) {
				for (int n = 1; n < myRequest.length; n++) {
					String[] myLine = myRequest[n].split(": ");
					if (myLine.length > 1) {
						myLine[0] = HTTPServer.replace(myLine[0], (char) (10), "");
						myLine[1] = HTTPServer.replace(myLine[1], (char) (10), "");
						myHeaderFields.put(myLine[0].toLowerCase(), myLine[1]);
					}
				}
			} else {
				return;
			}
			HTTPRequest myHttpRequest = new HTTPRequest(_myHttpServer, myUrl, ps, myHeaderFields);
			myHttpRequest.setRootPath(_myHttpServer.rootpath);
			_myHttpServer.process(myHttpRequest);
			_myBuffer = new byte[BUF_SIZE];
		} finally {
			_mySocket.close();
		}
	}

}
