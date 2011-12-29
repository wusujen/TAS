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


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;


public class HTTPReply
    implements HTTPConstants {

  private HTTPHeader _myHttpHeader;

  private final PrintStream _myPrintStream;

  /* map of file extensions to content-types */
  static java.util.Hashtable map = new java.util.Hashtable();

  static {
    fillMap();
  }



  static void setSuffix(String k, String v) {
    map.put(k, v);
  }



  public static void fillMap() {
    setSuffix("", "content/unknown");
    setSuffix(".uu", "application/octet-stream");
    setSuffix(".exe", "application/octet-stream");
    setSuffix(".ps", "application/postscript");
    setSuffix(".zip", "application/zip");
    setSuffix(".sh", "application/x-shar");
    setSuffix(".tar", "application/x-tar");
    setSuffix(".js", "application/x-javascript");
    setSuffix(".kml", "application/vnd.google-earth.kml+xml");
    setSuffix(".swf", "application/x-shockwave-flash");
    setSuffix(".snd", "audio/basic");
    setSuffix(".au", "audio/basic");
    setSuffix(".wav", "audio/x-wav");
    setSuffix(".gif", "image/gif");
    setSuffix(".jpg", "image/jpeg");
    setSuffix(".jpeg", "image/jpeg");
    setSuffix(".png", "image/png");
    setSuffix(".bmp", "image/bmp");
    setSuffix(".tif", "image/tiff");
    setSuffix(".htm", "text/html");
    setSuffix(".html", "text/html");
    setSuffix(".text", "text/plain");
    setSuffix(".c", "text/plain");
    setSuffix(".cc", "text/plain");
    setSuffix(".c++", "text/plain");
    setSuffix(".h", "text/plain");
    setSuffix(".pl", "text/plain");
    setSuffix(".txt", "text/plain");
    setSuffix(".java", "text/plain");
    setSuffix(".pde", "text/plain");
    setSuffix(".xml", "text/xml");
    setSuffix(".css", "text/css");
    setSuffix(".mov", "video/quicktime");
    setSuffix(".mpeg", "video/mpeg");
    setSuffix(".mpg", "video/mpeg");
    setSuffix(".mp3", "video/quicktime");
  }


  protected HTTPRequest _myRequest;
  
  public HTTPReply(HTTPRequest theRequest) {
	  _myRequest = theRequest;
    _myPrintStream = _myRequest.printstream();
    _myHttpHeader = new HTTPHeader();
  }



  public HTTPReply(PrintStream thePrintStream) {
    _myPrintStream = thePrintStream;
    _myHttpHeader = new HTTPHeader();
  }



  static public byte[] loadBytes(InputStream input) {
    try {
      BufferedInputStream bis = new BufferedInputStream(input);
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      int c = bis.read();
      while (c != -1) {
        out.write(c);
        c = bis.read();
      }
      return out.toByteArray();

    }
    catch (IOException e) {
      e.printStackTrace();
      // throw new RuntimeException("Couldn't load bytes from stream");
    }
    return null;
  }



  public PrintStream printstream() {
    return _myPrintStream;
  }



  public HTTPHeader header() {
    return _myHttpHeader;
  }



  public void send(String theString, String theSuffix) {
    try {
      String myContentType = null;
      myContentType = (String) map.get(theSuffix);

      if (myContentType == null) {
        myContentType = "unknown/unknown";
      }

      printstream().print("HTTP/1.0 " + HTTP_OK + " OK");
      printstream().write(EOL.getBytes());
      printstream().print("Server: sHTTP");
      printstream().write(EOL.getBytes());
      printstream().print("Date: " + (new Date()));
      printstream().write(EOL.getBytes());
      printstream().print("Content-type: " + myContentType);
      printstream().write(EOL.getBytes());
      printstream().write(EOL.getBytes());
      printstream().write(theString.getBytes());
    }
    catch (IOException e) {

    }
  }



  public void send() {
	  send(_myRequest.server().getFile(_myRequest.path() + _myRequest.file()));
  }

  
  public void send(String theString) {
    send(theString, ".html");
  }

  
  public void send(HTTPRequest theRequest) {
	  send(theRequest.server().getFile(theRequest.path() + theRequest.file()));
  }
  
  
  public void send(File theFile) {
    try {
      boolean isOk = printHeaders(theFile);
      if (isOk) {
        sendFile(theFile);
      }
      else {
        sendError();
      }
    }
    catch (IOException e) {

    }
  }



  public void send(byte[] theBytes, String theSuffix) {
    if (theSuffix.charAt(0) != '.') {
      theSuffix = "." + theSuffix;
    }
    try {
      printstream().print("HTTP/1.0 " + HTTP_OK + " OK");
      printstream().write(EOL.getBytes());
      printstream().print("Server: sHTTP");
      printstream().write(EOL.getBytes());
      printstream().print("Date: " + (new Date()));
      printstream().write(EOL.getBytes());
      printstream().print("Content-length: " + theBytes.length);
      printstream().write(EOL.getBytes());
      printstream().print("Last Modified: " + (new Date()));
      printstream().write(EOL.getBytes());
      String myContentType = (String) map.get(theSuffix);
      printstream().print("Content-type: " + myContentType);
      printstream().write(EOL.getBytes());
      printstream().write(EOL.getBytes());
      printstream().write(theBytes);
    }
    catch (IOException e) {

    }
  }



  boolean printHeaders(File theFile) throws IOException {
    boolean ret = false;
    int rCode = 0;
    if (!theFile.exists()) {
      rCode = HTTP_NOT_FOUND;
      printstream().print("HTTP/1.0 " + HTTP_NOT_FOUND + " not found");
      printstream().write(EOL.getBytes());
      ret = false;
    }
    else {
      rCode = HTTP_OK;
      printstream().print("HTTP/1.0 " + HTTP_OK + " OK");
      printstream().write(EOL.getBytes());
      ret = true;
    }
    // log("From " +s.getInetAddress().getHostAddress()+": GET " +
    // targ.getAbsolutePath()+"-->"+rCode);
    printstream().print("Server: sHTTP");
    printstream().write(EOL.getBytes());
    printstream().print("Date: " + (new Date()));
    printstream().write(EOL.getBytes());
    if (ret) {
      if (!theFile.isDirectory()) {
        printstream().print("Content-length: " + theFile.length());
        printstream().write(EOL.getBytes());
        printstream().print(
            "Last Modified: " + (new Date(theFile.lastModified())));
        printstream().write(EOL.getBytes());
        String name = theFile.getName();
        int ind = name.lastIndexOf('.');
        String ct = null;
        if (ind > 0) {
          ct = (String) map.get(name.substring(ind));
        }
        if (ct == null) {
          ct = "unknown/unknown";
        }
        printstream().print("Content-type: " + ct);
        printstream().write(EOL.getBytes());
      }
      else {
        printstream().print("Content-type: text/html");
        printstream().write(EOL.getBytes());
      }
    }
    if(rCode==0) {}
    return ret;
  }



  public void sendError() throws IOException {
    printstream().write(EOL.getBytes());
    printstream().write(EOL.getBytes());
    printstream().println(
        "Not Found\n\n" + "The requested resource was not found.\n");
  }



  public void sendFile() {
    System.out.println("not yet supported.");
  }


  public void sendFile(File theFile) throws IOException {
    InputStream is = null;
    printstream().write(EOL.getBytes());
    if (theFile.isDirectory()) {
      listDirectory(theFile);
      return;
    }
    else {
      is = new FileInputStream(theFile.getAbsolutePath());
    }

    try {
      byte[] b = loadBytes(is);
      printstream().write(b);
    }
    finally {
      is.close();
    }
  }



  void listDirectory(File dir) throws IOException {
    printstream().println("<TITLE>Directory listing</TITLE><P>\n");
    printstream().println("<A HREF=\"..\">Parent Directory</A><BR>\n");
    String[] list = dir.list();
    for (int i = 0; list != null && i < list.length; i++) {
      File f = new File(dir, list[i]);
      if (f.isDirectory()) {
        printstream().println("<A HREF=\"" + list[i] + "/\">" + list[i]+ "/</A><BR>");
      }
      else {
        printstream().println("<A HREF=\"" + list[i] + "\">" + list[i] + "</A><BR");
      }
    }
    printstream().println("<P><HR><BR><I>" + (new Date()) + "</I>");
  }

}
