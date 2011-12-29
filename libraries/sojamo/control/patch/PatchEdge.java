package sojamo.control.patch;

import processing.core.PApplet;
import processing.core.PVector;

class PatchEdge implements PatchInputListener, PDrawable, PatchInterface {

    protected PatchNode _myA;

    protected PatchNode _myB;

    protected PVector myCurrentBPosition;

    protected PVector myCurrentAPosition;

    private PVector myCenterPosition;

    protected float myLength = 0;

    private boolean isInside = false;

    protected boolean isRemove = false;

    public final static int IDLE = 0;

    public final static int ACTIVE = 1;

    public final static int IMPULSE = 2;

    private int myMode = IDLE;

    private PatchGroup _myGroup;

    public final static int ABBA = 0;

    public final static int AB = 1;

    public final static int BA = 2;

    public final static int PAUSE = 3;

    protected int _myDirection = AB;

    protected int moveNode;

    protected final static int A = 0;

    protected final static int B = 1;

    protected float _myAngle;

    protected float _myLength_half;

    protected boolean isMouseDown;

    protected float insideDistance;

    protected int id = -1;

    public int colorBackground = 0xff003652;

    public int colorForeground = 0xffffffff; // 0xff00698c;

    public int colorActive = 0xffffffff; // 0xff0699C4;

    public int colorLabel = 0xffffffff;

    protected PatchEdge(final int theId, final PatchNode theStart,
	    final PatchGroup theGroup) {
	_myGroup = theGroup;
	id = theId;
	_myA = theStart;
	myCurrentAPosition = new PVector(_myA.position.x,_myA.position.y,_myA.position.z);
	myCurrentBPosition = new PVector(_myA.position.x,_myA.position.y,_myA.position.z);
	myCenterPosition = new PVector(_myA.position.x,_myA.position.y,_myA.position.z);
	_myGroup.inputhandler.add(this);
    }

    public int category() {
	return PatchGroup.NETWORK;
    }

    public boolean isType(int theType) {
	return theType == NODE;
    }

    public boolean mousePressed() {
	if (isInside
		&& _myGroup.inputhandler.keysPressed[PatchInputListener.CTRL]) {
	    isMouseDown = true;
	    moveNode = (insideDistance > 0.5) ? B : A;
	    return true;
	}
	return false;
    }

    public void mouseReleased() {
	if (isMouseDown) {
	    isMouseDown = false;
	    if (moveNode == A) {
		if (_myA.inside(_myGroup.mouseX, _myGroup.mouseY)) {
		    updatePosition();
		    return;
		}
	    } else {
		if (_myB.inside(_myGroup.mouseX, _myGroup.mouseY)) {
		    updatePosition();
		    return;
		}
	    }
	    for (int i = 0; i < _myGroup.nodes.size(); i++) {
		if (((PatchNode) _myGroup.nodes.get(i)).inside(_myGroup.mouseX,
			_myGroup.mouseY)) {
		    if (moveNode == A) {
			_myA.removeEdge(this);
			_myA = (PatchNode) _myGroup.nodes.get(i);
			_myA.addEdge(this);
		    } else {
			_myB.removeEdge(this);
			_myB = (PatchNode) _myGroup.nodes.get(i);
			_myB.addEdge(this);
		    }
		    updatePosition();
		    return;
		}
	    }
	    remove();
	}

    }

    private void updatePosition() {
	myCurrentAPosition = new PVector(_myA.position.x,_myA.position.y,_myA.position.z);
	myCurrentBPosition = new PVector(_myB.position.x,_myB.position.y,_myB.position.z);
    }

    public void keyPressed(int theKeyCode) {
	if (isInside) {
	    if (theKeyCode == 39) {
		_myDirection++;
		if (_myDirection > PAUSE) {
		    _myDirection = ABBA;
		}
	    }
	    if (theKeyCode == 37) {
		_myDirection--;
		if (_myDirection < ABBA) {
		    _myDirection = PAUSE;
		}
	    }

	}
    }

    public void keyReleased(int theKeyCode) {
    }

    public void update() {

	if (isMouseDown) {
	    if (moveNode == B) {
		myCurrentBPosition.set(_myGroup.mouseX,_myGroup.mouseY,0);
		myCurrentAPosition.set(_myA.position.x,_myA.position.y,0);
	    } else {
		myCurrentAPosition.set(_myGroup.mouseX,_myGroup.mouseY,0);
		myCurrentBPosition.set(_myB.position.x,_myB.position.y,0);
		
	    }
	} else {
	    if (connected()) {
		myCurrentBPosition.set(_myB.position.x,_myB.position.y,0);
	    }
	    myCurrentAPosition.set(_myA.position.x, _myA.position.y,0);
	}

	myMode = IDLE;

	isInside = false;

	if (_myGroup.checkEdge(this)) {
	    myCurrentBPosition.set(_myGroup.mouseX, _myGroup.mouseY,0);
	} else {
	    isInside = inside();
	    if (isInside) {
		myMode = ACTIVE;
		return;
	    }
	}
    }

    public void remove() {
	isRemove = true;
	// _mymaingroup.removeEdge(this);
    }

    public void setDirection(int theDirection) {
	if (theDirection >= ABBA && theDirection <= PAUSE) {
	    _myDirection = theDirection;
	}
    }

    protected PVector get_NOT_Position(PatchNode theNode) {
	if (theNode == _myA) {
	    return myCurrentBPosition;
	} else {
	    return myCurrentAPosition;
	}
    }

    public final float lengthSquared(PVector theV) {
	return theV.x * theV.x + theV.y * theV.y + theV.z * theV.z;
    }

    /**
     * Use this method to calculate the length of a vector, the length of a
     * vector is also known as its magnitude. Vectors have a magnitude and a
     * direction. These values are not explicitly expressed in the vector so
     * they have to be computed.
     * 
     * @return float: the length of the vector
     * @shortdesc Calculates the length of the vector.
     */
    public final float length(PVector theV) {
	return (float) Math.sqrt(lengthSquared(theV));
    }

    public final void scale(PVector thePVector, final float theScalar) {
	thePVector.x *= theScalar;
	thePVector.y *= theScalar;
	thePVector.z *= theScalar;
    }

    protected void updateVector() {
	PVector myVector = new PVector(myCurrentBPosition.x,myCurrentBPosition.y,myCurrentBPosition.z);
	myVector.sub(myCurrentAPosition);
	myLength = length(myVector);
	myVector.mult(0.5f);
	
	myVector.add(myCurrentAPosition);
	myCenterPosition.set(myVector);
	_myAngle = (float) Math.atan2(myCurrentBPosition.y
		- myCurrentAPosition.y, myCurrentBPosition.x
		- myCurrentAPosition.x);
	_myLength_half = myLength * 0.5f;
    }

    public void draw(PApplet theApplet) {
	switch (myMode) {
	case (IDLE):
	    theApplet.stroke(colorForeground, 128);
	    break;
	case (ACTIVE):
	    theApplet.stroke(colorActive);
	    break;
	case (IMPULSE):
	    theApplet.stroke(colorActive);
	    break;
	}
	if (_myGroup.isLineVisible) {
	    updateVector();
	    theApplet.pushMatrix();
	    theApplet.translate(myCurrentAPosition.x, myCurrentAPosition.y);
	    theApplet.rotate(_myAngle);
	    theApplet.strokeWeight(1);// _myGroup.patch.strokeWeight);
	    theApplet.line(0, 0, myLength, 0);
	    theApplet.strokeWeight(2);
	    theApplet.stroke(colorActive);

	    if (_myDirection == BA) {
		theApplet.line(_myLength_half, 0, _myLength_half + 4, 3);
		theApplet.line(_myLength_half, 0, _myLength_half + 4, -3);
	    } else if (_myDirection == AB) {
		theApplet.line(_myLength_half, 0, _myLength_half - 4, -3);
		theApplet.line(_myLength_half, 0, _myLength_half - 4, 3);
	    } else if (_myDirection == PAUSE) {
		theApplet.line(_myLength_half - 3, 7, _myLength_half + 3, 7);
	    }

	    theApplet.popMatrix();
	}
    }

    public boolean connected() {
	return (_myB != null);
    }

    public PatchNode getNextNode(PatchNode theNode) {
	if (theNode.id() == _myA.id()) {
	    if (_myDirection == AB || _myDirection == ABBA) {
		return _myB;
	    }
	}

	if (theNode.id() == _myB.id()) {
	    if (_myDirection == BA || _myDirection == ABBA) {
		return _myA;
	    }
	}
	return null;
    }

    public void setB(PatchNode theNode) {
	_myB = theNode;
	_myA.addEdge(this);
	_myB.addEdge(this);
	myCurrentBPosition.set(_myB.position.x,_myB.position.y,_myB.position.z);
    }

    public void die() {
	_myA.removeEdge(this);
	_myB.removeEdge(this);
    }

    private boolean inside() {
	PVector dir = new PVector(myCurrentBPosition.x,myCurrentBPosition.y,myCurrentBPosition.z);
	dir.sub(myCurrentAPosition);
	PVector diff = new PVector(_myGroup.mouseX, _myGroup.mouseY, 0);
	diff.sub(myCurrentAPosition);
	insideDistance = diff.dot(dir) / dir.dot(dir);
	if (insideDistance < .2f) {
	    insideDistance = 0.2f;
	}
	if (insideDistance > 0.8f) {
	    insideDistance = 0.8f;
	}

	PVector closest = new PVector(myCurrentAPosition.x,myCurrentAPosition.y,myCurrentAPosition.z);
	dir.mult(insideDistance);
	closest.add(dir);
	PVector d = new PVector(_myGroup.mouseX, _myGroup.mouseY, 0);
	d.sub(closest);
	float distsqr = d.dot(d);
	return (distsqr < 12);
    }

    public int id() {
	return id;
    }

    public void setId(int theId) {
	id = theId;
    }

    public String toString() {
	return ("e" + "," + id() + "," + _myA.id() + "," + _myB.id() + "," + _myDirection);
    }
}
