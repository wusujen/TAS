/**
 * sojamo.osc is a processing and java library for the open sound control protocol,
 * OSC. 2006 by Andreas Schlegel This library is free software; you can
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

/**
 * @invisible
 */
public abstract class OSCPacket extends OSCPatcher {

    protected static final int MESSAGE = 0;

    protected static final int BUNDLE = 1;

    protected String hostAddress;

    protected int _myType;

    protected int port;

    /**
     * @invisible
     */
    public OSCPacket() {
    }

    protected static OSCPacket parse(byte[] theBytes) {
	if (evaluatePacket(theBytes) == MESSAGE) {
	    return new OSCMessage(theBytes);
	} else {
	    return new OSCBundle(theBytes);
	}
    }

    private static int evaluatePacket(byte[] theBytes) {
	return (OSCBytes.areEqual(OSCBundle.BUNDLE_AS_BYTES, OSCBytes.copy(
		theBytes, 0, OSCBundle.BUNDLE_AS_BYTES.length))) ? BUNDLE
		: MESSAGE;
    }

    
    protected boolean isValid() {
	return isValid;
    }

    protected int type() {
	return _myType;
    }

    /**
     * @return byte[]
     * @invisible
     */
    public abstract byte[] getBytes();

}
