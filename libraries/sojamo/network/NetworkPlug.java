package sojamo.network;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.util.Vector;

/**
 * @invisible
 * @todo replace System.out.println with Logger.
 */
class NetworkPlug {

    protected boolean isEventMethod = false;

    protected Method _myEventMethod;

    protected String _myEventMethodName = "networkEvent";
    
    protected boolean isBytesEventMethod = false;
    
    protected Method _myBytesEventMethod;
    
    protected String _myBytesEventMethodName = "networkEvent";

    protected boolean isStatusMethod = false;

    protected Method _myStatusMethod;

    protected String _myStatusMethodName = "networkStatus";

    protected Class _myParentClass;

    protected Object _myParent;

    protected Vector _myNetListeners;

    protected boolean isNetListener;

    protected NetworkPlug() {
	_myNetListeners = new Vector();
    }

    protected void invoke(final Object theObject, final Method theMethod,
	    final Object[] theArgs) {
	try {
	    theMethod.invoke(theObject, theArgs);
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    System.out.println("sojamo.network ClassCastException. "
		    + "parsing failed for NetworkMessage " + e);
	}
    }

    protected void checkMethod(Object theObject) {
	_myParent = theObject;
	try {
	    checkEventMethod();
	    checkBytesEventMethod();
	    checkStatusMethod();
	} catch (Exception e) {
	}
    }
    

    private boolean checkBytesEventMethod() {
	_myParentClass = _myParent.getClass();
	if (_myBytesEventMethodName != null) {
	    try {
		_myBytesEventMethod = _myParentClass.getDeclaredMethod(
			_myBytesEventMethodName,
			new Class[] { byte[].class });
		isBytesEventMethod = true;
		_myBytesEventMethod.setAccessible(true);
		return true;
	    } catch (SecurityException e1) {
		e1.printStackTrace();
	    } catch (NoSuchMethodException e1) {
//		System.out.println("### NOTE. "
//			+ "no networkEvent(byte[] theBytes) "
//			+ "method available.");
	    }
	}
	if (_myBytesEventMethod != null) {
	    return true;
	}
	return false;
    }
    
    
    private boolean checkEventMethod() {
	_myParentClass = _myParent.getClass();
	if (_myEventMethodName != null) {
	    try {
		_myEventMethod = _myParentClass.getDeclaredMethod(
			_myBytesEventMethodName,
			new Class[] { NetworkMessage.class });
		isEventMethod = true;
		_myEventMethod.setAccessible(true);
		return true;
	    } catch (SecurityException e1) {
		e1.printStackTrace();
	    } catch (NoSuchMethodException e1) {
//		System.out.println("### NOTE. "
//			+ "no netEvent(NetworkMessage theMessage) "
//			+ "method available.");
	    }
	}
	if (_myEventMethod != null) {
	    return true;
	}
	return false;
    }

    private boolean checkStatusMethod() {
	_myParentClass = _myParent.getClass();
	if (_myStatusMethodName != null) {
	    try {
		_myStatusMethod = _myParentClass.getDeclaredMethod(
			_myStatusMethodName,
			new Class[] { NetworkStatus.class });
		isStatusMethod = true;
		_myStatusMethod.setAccessible(true);
		return true;
	    } catch (SecurityException e1) {
		e1.printStackTrace();
	    } catch (NoSuchMethodException e1) {
		// System.out.println("### NOTE. no netStatus(NetStatus
		// theMessage) method available.");
	    }
	}
	if (_myStatusMethod != null) {
	    return true;
	}
	return false;
    }

    /**
     * 
     * @param theDatagramPacket
     *                DatagramPacket
     * @param thePort
     *                int
     * @invisible
     */
    public void process(final DatagramPacket theDatagramPacket,
	    final int thePort) {
	if (isNetListener || isEventMethod || isBytesEventMethod) {
	    NetworkMessage n = new NetworkMessage(theDatagramPacket);
	    for (int i = 0; i < _myNetListeners.size(); i++) {
		getListener(i).netEvent(n);
	    }
	    if (isEventMethod) {
		try {
		    invoke(_myParent, _myEventMethod, new Object[] { n });
		} catch (ClassCastException e) {
		    System.out.println(
			    "NetworkPlug.callMessage " +
			    "ClassCastException. failed to forward NetworkMessage.");
		}
	    }
	    if (isBytesEventMethod) {
		try {
		    invoke(_myParent, _myBytesEventMethod, new Object[] { n.getBytes() });
		} catch (ClassCastException e) {
		    System.out.println(
			    "NetworkPlug.callMessage " +
			    "ClassCastException. failed to forward NetworkMessage.");
		}
	    }
	}
    }

    /**
     * @invisible
     * @param theIndex
     */
    public void status(int theIndex) {
	if (isNetListener || isEventMethod) {
	    NetworkStatus n = new NetworkStatus(theIndex);
	    for (int i = 0; i < _myNetListeners.size(); i++) {
		getListener(i).netStatus(n);
	    }
	    if (isStatusMethod) {
		try {
		    invoke(_myParent, _myStatusMethod, new Object[] { n });
		} catch (ClassCastException e) {
		    System.out.println(
			    "NetworkPlug.callMessage " +
			    "ClassCastException. failed to forward NetworkStatus.");
		}
	    }
	    
	}
    }

    /**
     * 
     * @param theTcpPacket
     *                TcpPacket
     * @param thePort
     *                int
     * @invisible
     */
    public void process(final TCPPacket theTcpPacket, final int thePort) {

	if (isNetListener || isEventMethod  || isBytesEventMethod) {
	    NetworkMessage n = new NetworkMessage(theTcpPacket);
	    for (int i = 0; i < _myNetListeners.size(); i++) {
		getListener(i).netEvent(n);
	    }

	    if (isEventMethod) {
		try {
		    invoke(_myParent, _myEventMethod, new Object[] { n });
		} catch (ClassCastException e) {
		    System.out.println(
			    "NetworkPlug.callMessage " +
			    "ClassCastException. failed to forward NetworkMessage.");
		}
	    }
	    if (isBytesEventMethod) {
		try {
		    invoke(_myParent, _myBytesEventMethod, new Object[] { n.getBytes() });
		} catch (ClassCastException e) {
		    System.out.println(
			    "NetworkPlug.callMessage " +
			    "ClassCastException. failed to forward NetworkMessage.");
		}
	    }
	}
    }

    protected void addListener(NetworkListener theListener) {
	_myNetListeners.add(theListener);
	isNetListener = true;
    }

    protected void removeListener(NetworkListener theListener) {
	_myNetListeners.remove(theListener);
	isNetListener = (_myNetListeners.size() > 0) ? true : false;
    }

    protected NetworkListener getListener(int theIndex) {
	return ((NetworkListener) _myNetListeners.get(theIndex));
    }

    protected Vector getListeners() {
	return _myNetListeners;
    }

}
