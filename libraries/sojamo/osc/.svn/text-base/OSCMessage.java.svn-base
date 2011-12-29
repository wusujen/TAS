/**
 * sojamo.osc is a processing and java library for the
 * open sound control protocol, OSC.
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
 *make
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA
 *
 * @author Andreas Schlegel (http://www.sojamo.de)
 *
 */

package sojamo.osc;



/**
 * An OSC message consists of an OSC Address Pattern, an OSC Type Tag String
 * and the OSC arguments.
 *
 * @related OscBundle
 * @example oscSendReceive
 */
public class OSCMessage extends OSCPacket {

    protected final OSCArgument _myOscArgument = new OSCArgument();

    protected boolean isPlugged = false;

    protected OSCMessage(final byte[] theBytes) {
        parseMessage(theBytes);
        _myType = MESSAGE;
    }
    
    
    protected OSCMessage(final byte[] theBytes, long theTimetag) {
        parseMessage(theBytes);
        timetag = theTimetag;
        _myType = MESSAGE;
    }



    /**
     *
     * @param theOscMessage OscMessage
     * @invisible
     */

    public OSCMessage(final OSCMessage theOscMessage) {
        _myAddrPattern = theOscMessage._myAddrPattern;
        _myTypetag = theOscMessage._myTypetag;
        _myData = theOscMessage._myData;
        _myArguments = theOscMessage._myArguments;
        isValid = true;
    }


    /**
     *
     * @param theAddrPattern
     * String
     */
    public OSCMessage(final String theAddrPattern) {
        this(theAddrPattern, new Object[0]);
    }


    /**
     *
     * @param theAddrInt
     * int
     */
    public OSCMessage(final int theAddrInt) {
        this(theAddrInt, new Object[0]);
    }


    /**
     *
     * @param theAddrPattern String
     * @param theArguments
     * Object[]
     */
    public OSCMessage(final String theAddrPattern,
                      final Object[] theArguments) {
        init();
        setAddrPattern(theAddrPattern);
        setArguments(theArguments);
    }


    /**
     *
     * @param theAddrPattern int
     * @param theArguments Object[]
     */
    public OSCMessage(final int theAddrPattern,
                      final Object[] theArguments) {
        init();
        setAddrPattern(theAddrPattern);
        setArguments(theArguments);
    }


// dont forget to implement a timetag constructor!

//    protected OSCMessage(final byte[] theBytes,
//                         final InetAddress theInetAddress,
//                         final int thePort,
//                         final long theTimetag,
//                         final TCPClient theClient
//            ) {
//        this(theBytes,theInetAddress,thePort,theClient);
//        timetag = theTimetag;
//    }



    protected void init() {
        _myTypetag = new byte[0];
        _myData = new byte[0];
    }


    /**
     * clear and reset an OscMessage for reuse.
     */
    public void clear() {
        init();
        setAddrPattern("");
        setArguments(new Object[0]);
    }

    /**
     * @todo
     * set should enable the programmer to set values
     * of an existing osc message.
     */
    void set(int theIndex, Object theObject) {

    }


//    public void
    /**
     *
     * @param theTypeTag
     * String
     * @return boolean
     * @example oscParsing
     */
    public boolean checkTypetag(final String theTypeTag) {
        return theTypeTag.equals(typetag());
    }


    /**
     * check if an address pattern equals a specific address pattern
     * you are looking for. this is usually used when parsing an osc message.
     * e.g. if(theOscMessage.checkAddrPattern("/test")==true) {...}
     * @param theAddrPattern
     * String
     * @return boolean
     * @example oscParsing
     */
    public boolean checkAddrPattern(final String theAddrPattern) {
        return theAddrPattern.equals(addrPattern());
    }


    /**
     * set the address pattern of an osc message. you can set
     * a string or an int as address pattern.tnt might be useful for
     * supercollider users. sojamo.osc does support ints and strings as
     * address patterns when sending and receiving messages.
     * @param theAddrPattern
     * String
     */
    public void setAddrPattern(final String theAddrPattern) {
        _myAddrPattern = theAddrPattern.getBytes();
    }


    /**
     *
     * @param theAddrPattern
     * int
     */
    public void setAddrPattern(final int theAddrPattern) {
        _myAddrPattern = OSCBytes.toBytes(theAddrPattern);
    }


    /**
     * set the arguments of the osc message using an object array.
     * @param theArguments
     * Object[]
     */
    public void setArguments(final Object[] theArguments) {
        _myArguments = theArguments;
        add(_myArguments);
    }


    public String addrPattern() {
        return OSCBytes.getAsString(_myAddrPattern);
    }


    /**
     * returns the address pattern of the osc message as int.
     * @return int
     */
    public int addrInt() {
        return _myAddrInt;
    }


    /**
     * returns the typetag of the osc message. e.g. the message contains
     * 3 floats then the typetag would be "fff"
     * @return String
     */
    public String typetag() {
        return OSCBytes.getAsString(_myTypetag);
    }

    /**
     * get the timetag of an osc message. timetags are only sent by
     * osc bundles.
     * @return long
     */
    public long timetag() {
      return timetag;
    }

    /**
     *
     * @return Object[]
     */
    public Object[] arguments() {
        return _myArguments;
    }


    /**
     * supported arrays see OscPlug.getArgs
     * @return Object[]
     */
    protected Object[] argsAsArray() {
        switch (_myTypetag[0]) {
        case (0x66): // float f
            float[] myFloatArray = new float[_myArguments.length];
            for (int i = 0; i < myFloatArray.length; i++) {
                myFloatArray[i] = ((Float) _myArguments[i]).floatValue();
            }
            return new Object[] {myFloatArray};
        case (0x69): // int i
            int[] myIntArray = new int[_myArguments.length];
            for (int i = 0; i < myIntArray.length; i++) {
                myIntArray[i] = ((Integer) _myArguments[i]).intValue();
            }
            return new Object[] {myIntArray};
        case (0x53): // Symbol S
        case (0x73): // String s
            String[] myStringArray = new String[_myArguments.length];
            for (int i = 0; i < myStringArray.length; i++) {
                myStringArray[i] = ((String) _myArguments[i]);
            }
            return new Object[] {myStringArray};
        default:
            break;
        }
        return new Object[] {};
    }

    /**
     *
     * @return byte[]
     * @invisible
     */
    public byte[] getAddrPatternAsBytes() {
        return OSCBytes.append(_myAddrPattern,
                            new byte[align(_myAddrPattern.length)]);
    }


    /**
     *
     * @return byte[]
     * @invisible
     */
    public byte[] getTypetagAsBytes() {
        return _myTypetag;
    }

    /**
     *
     * @return byte[]
     * @invisible
     */
    public byte[] getBytes() {
        byte[] myBytes = new byte[0];
        byte[] myTypeTag = OSCBytes.copy(_myTypetag, 0);
        myBytes = OSCBytes.append(myBytes, _myAddrPattern,
                               new byte[align(_myAddrPattern.length)]);
        if (myTypeTag.length == 0) {
            myTypeTag = new byte[] {KOMMA};
        } else if (myTypeTag[0] != KOMMA) {
            myTypeTag = OSCBytes.append(new byte[] {KOMMA}, myTypeTag);
        }
        myBytes = OSCBytes.append(myBytes, myTypeTag,
                               new byte[align(myTypeTag.length)]);
        myBytes = OSCBytes.append(myBytes, _myData,
                               new byte[align(_myData.length) % 4]);
        return myBytes;
    }


    /**
     * add values to an osc message. please check the
     * add documentation for specific information.
     * @example oscMessage
     */
    public void add() {
        _myTypetag = OSCBytes.append(_myTypetag, new byte[] {0x4e});
    }


    /**
     * @param theValue int
     */
    public void add(final int theValue) {
        _myTypetag = OSCBytes.append(_myTypetag, new byte[] {0x69});
        _myData = OSCBytes.append(_myData, OSCBytes.toBytes(theValue));
    }


    /**
     *
     * @param theValue String
     */
    public void add(final String theValue) {
        _myTypetag = OSCBytes.append(_myTypetag, new byte[] {0x73});
        byte[] myString = theValue.getBytes();
        _myData = OSCBytes.append(_myData, myString,
                               new byte[align(myString.length)]);
    }


    /**
     *
     * @param theValue float
     */
    public void add(final float theValue) {
        _myTypetag = OSCBytes.append(_myTypetag, new byte[] {0x66});
        _myData = OSCBytes.append(_myData, OSCBytes.toBytes(Float
                .floatToIntBits(theValue)));
    }


    /**
     *
     * @param theValue double
     */
    public void add(final double theValue) {
        _myTypetag = OSCBytes.append(_myTypetag, new byte[] {0x64});
        _myData = OSCBytes.append(_myData, OSCBytes.toBytes(Double
                .doubleToLongBits(theValue)));
    }


    /**
     *
     * @param theValue boolean
     */
    public void add(final boolean theValue) {
        if (theValue) {
            _myTypetag = OSCBytes.append(_myTypetag, new byte[] {0x54});
        } else {
            _myTypetag = OSCBytes.append(_myTypetag, new byte[] {0x46});
        }
    }


    /**
     *
     * @param theValue Boolean
     */
    public void add(final Boolean theValue) {
        add((theValue).booleanValue());
    }


    /**
     *
     * @param theValue Integer
     */
    public void add(final Integer theValue) {
        add(theValue.intValue());
    }


    /**
     *
     * @param theValue
     * Float
     */
    public void add(final Float theValue) {
        add(theValue.floatValue());
    }


    /**
     *
     * @param theValue
     * Double
     */
    public void add(final Double theValue) {
        add(theValue.doubleValue());
    }


    /**
     *
     * @param theValue
     * Character
     */
    public void add(final Character theValue) {
        add(theValue.charValue());
    }


    /**
     *
     * @param theValue
     * char
     */
    public void add(final char theValue) {
        _myTypetag = OSCBytes.append(_myTypetag, new byte[] {0x63});
        _myData = OSCBytes.append(_myData, OSCBytes.toBytes(theValue));
    }


    /**
     *
     * @param channel int
     * @param status int
     * @param value1 int
     * @param value2 int
     */

    public void add(final int channel,
                    final int status,
                    final int value1,
                    final int value2) {
        _myTypetag = OSCBytes.append(_myTypetag, new byte[] {0x6d}); // m
        byte[] theBytes = new byte[4];
        theBytes[0] = (byte) channel;
        theBytes[1] = (byte) status;
        theBytes[2] = (byte) value1;
        theBytes[3] = (byte) value2;
        _myData = OSCBytes.append(_myData, theBytes);
    }


    /**
     *
     * @param theArray
     * int[]
     */
    public void add(final int[] theArray) {
        for (int i = 0; i < theArray.length; i++) {
            add(theArray[i]);
        }
    }


    /**
     *
     * @param theArray
     * char[]
     */
    public void add(final char[] theArray) {
        for (int i = 0; i < theArray.length; i++) {
            add(theArray[i]);
        }
    }


    /**
     *
     * @param theArray
     * float[]
     */
    public void add(final float[] theArray) {
        for (int i = 0; i < theArray.length; i++) {
            add(theArray[i]);
        }
    }


    /**
     *
     * @param theArray
     * String[]
     */
    public void add(final String[] theArray) {
        for (int i = 0; i < theArray.length; i++) {
            add(theArray[i]);
        }
    }


    /**
     *
     * @param theArray
     * byte[]
     */
    public void add(final byte[] theArray) {
        _myTypetag = OSCBytes.append(_myTypetag, new byte[] {0x62});
        _myData = OSCBytes.append(_myData, makeBlob(theArray));
    }


    /**
     *
     * @param theArray
     * Object[]
     */
    public void add(final Object[] theArray) {
        for (int i = 0; i < theArray.length; i++) {
            if (!add(theArray[i])) {
                System.out.println("type of Argument not defined in osc specs.");
            }
        }
    }


    private boolean add(final Object theObject) {
        if (theObject instanceof Number) {
            if (theObject instanceof Integer) {
                add((Integer) theObject);
            } else if (theObject instanceof Float) {
                add((Float) theObject);
            } else if (theObject instanceof Double) {
                add((Double) theObject);
            } else if (theObject instanceof Long) {
                add((Long) theObject);
            }
        } else if (theObject instanceof String) {
            add((String) theObject);
        } else if (theObject instanceof Boolean) {
            add((Boolean) theObject);
        } else if (theObject instanceof Character) {
            add((Character) theObject);
        }

        else {
            if (theObject instanceof int[]) {
                add((int[]) theObject);
                return true;
            } else if (theObject instanceof float[]) {
                add((float[]) theObject);
                return true;
            } else if (theObject instanceof byte[]) {
                add((byte[]) theObject);
                return true;
            }

            else if (theObject instanceof String[]) {
                add((String[]) theObject);
                return true;
            } else if (theObject instanceof char[]) {
                add((char[]) theObject);
                return true;
            } else if (theObject instanceof double[]) {
                add((float[]) theObject);
                return true;
            }
            return false;
        }
        return true;
    }


    /**
     *
     * @param b byte[]
     * @return byte[]
     * @invisible
     */
    public static byte[] makeBlob(final byte[] b) {
        int tLength = b.length;
        byte[] b1 = OSCBytes.toBytes(tLength);
        b1 = OSCBytes.append(b1, b);
        int t = tLength % 4;
        if (t != 0) {
            b1 = OSCBytes.append(b1, new byte[4 - t]);
        }
        return b1;
    }


    /**
     * get a value at a specific position in the osc message. the get method
     * returns an OscArgument from which the value can be parsed into the right
     * format. e.g. to parse an int from the first argument in the osc message,
     * use theOscMessage.get(0).intValue();
     * @param theIndex int
     * @return OscArgument
     */
    public OSCArgument get(final int theIndex) {
        if (theIndex < arguments().length) {
            _myOscArgument.value = arguments()[theIndex];
            return _myOscArgument;
        }
        return null;
    }


    /**
     *
     * @return String
     * @invisible
     */
    public final String toString() {
        return hostAddress + ":" + port + " | " +
                addrPattern() + " " + typetag();
    }


    public boolean isPlugged() {
        return isPlugged;
    }


    public void print() {
//        Logger.println("-OscMessage----------");
//        Logger.println("received from\t" + hostAddress + ":" + port);
//        Logger.println("addrpattern\t" + SBytes.getAsString(_myAddrPattern));
//        Logger.println("typetag\t" + SBytes.getAsString(_myTypetag));
//        Logger.println(SBytes.getAsString(_myArguments));
//        Logger.println("---------------------");
    }

}
