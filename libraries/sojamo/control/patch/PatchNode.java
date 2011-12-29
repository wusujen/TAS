package sojamo.control.patch;

import java.util.Vector;
import processing.core.PApplet;
import processing.core.PVector;

public class PatchNode implements PatchInputListener, Cloneable, PDrawable,
	PatchInterface {

    public PVector position;

    public PVector ghostposition;

    private PVector dimension;

    private boolean isActive = false;

    private boolean isPreviousActive = false;

    protected PatchGroup _myGroup;

    protected boolean isRemove;

    private Vector<PatchSignal> _mySignals;

    private Vector<PatchEdge> edges;

    protected Vector<PatchActionHandler> patchActionHandlers;

    protected boolean isSelected;

    protected boolean isMousePressed;

    protected boolean isDragged;

    protected int id = -1;

    protected boolean isKeyReleased;

    public int colorBackground = 0xff003652;

    public int colorForeground = 0xffffffff;// 0xff00698c;

    public int colorActive = 0xffffffff; // 0xff0699C4;

    public int colorLabel = 0xffffffff;

    public PatchNode(final PatchGroup theGroup, final float theX,
	    final float theY, float theZ) {
	_myGroup = theGroup;
	edges = new Vector<PatchEdge>();
	position = new PVector(theX, theY, theZ);
	ghostposition = new PVector(theX, theY, theZ);
	dimension = new PVector(20, 20, 0);
	_mySignals = new Vector<PatchSignal>();
	patchActionHandlers = new Vector<PatchActionHandler>();
	_myGroup.inputhandler.add(this);
    }

    public int category() {
	return PatchGroup.NETWORK;
    }

    public boolean isType(int theType) {
	return theType == NODE;
    }

    public boolean mousePressed() {
	if (inside(_myGroup.mouseX, _myGroup.mouseY)) {
	    if (isDragged == false
		    && _myGroup.inputhandler.keysPressed[SHIFT] == true) {
		impulse(new PatchSignal());
		return true;
	    }
	    _myGroup.isMouseBusy = true;
	    isMousePressed = true;
	    isActive = true;
	    if (_myGroup.inputhandler.keysPressed[ALT] == true) {
		_myGroup.activateDrawEdge(this);
		return true;
	    }
	    if (!_myGroup.inputhandler.isKeyPressed()) {
		for (PatchActionHandler o : patchActionHandlers) {
		    o.mousePressed();
		}
	    }
	    return true;
	}

	return false;
    }

    public void mouseReleased() {
	if (isMousePressed) {
	    for (PatchActionHandler o : patchActionHandlers) {
		o.mouseReleased();
	    }
	}
	isMousePressed = false;
	_myGroup.isMouseBusy = false;
	isActive = false;
    }

    public void keyPressed(int theKeyCode) {
	if (isActive) {
	    if (_myGroup.inputhandler.keysPressed[CTRL]
		    && _myGroup.inputhandler.keysPressed[79]) { // 65 = o
		System.out.println("activate menu for node.");
		_myGroup.isBlocked = PatchGroup.NETWORK;
		return;
	    }
	}
	if (theKeyCode == CTRL) {
	    isDragged = true;
	}
	isKeyReleased = false;
    }

    public void keyReleased(int theKeyCode) {
	if (theKeyCode == CTRL) {
	    isDragged = false;
	}
	isKeyReleased = true;
    }

    public boolean isKeyPressed() {
	return !isKeyReleased;
    }

    protected void update(final float theX, final float theY, float theZ) {
	
	for(int i=0;i<_mySignals.size();i++) {
	    ((PatchSignal)_mySignals.get(i)).update(this);
	}

	isActive = inside(theX, theY);
	if (isPreviousActive != isActive) {
	    ghostposition.set(position.x, position.y, position.z);
	    if (isDragged == false) {
		if (isActive) {
		    for (PatchActionHandler o : patchActionHandlers) {
			o.onEnter();
		    }
		} else {
		    for (PatchActionHandler o : patchActionHandlers) {
			o.onLeave();
		    }
		}
	    }
	}
	if (isActive) {
	    if (_myGroup.currentTool == PatchGroup.DRAWEDGE) {
		_myGroup.setCurrentNode(this);
	    }
	    if (_myGroup.inputhandler.keysPressed[BACKSPACE] == true) {
		_myGroup.inputhandler.keysPressed[BACKSPACE] = false;
		remove();
	    }
	}

	if (isMousePressed && isDragged == true) {
	    updatePosition(
		    position.x
			    + (_myGroup.patch.papplet.mouseX - _myGroup.patch.papplet.pmouseX),
		    position.y
			    + (_myGroup.patch.papplet.mouseY - _myGroup.patch.papplet.pmouseY));
	}
	isPreviousActive = isActive;
    }

    public void addEdge(final PatchEdge theEdge) {
	edges.add(theEdge);
    }

    public void removeEdge(final PatchEdge theEdge) {
	edges.remove(theEdge);
    }

    public void updatePosition(final float theX, final float theY) {
	if (!isActive) {
	    position.set((int) theX,(int) theY,0);
	    for (int i = 0; i < edges.size(); i++) {
		((PatchEdge) edges.elementAt(i)).updateVector();
	    }
	}
	ghostposition.set((int) theX, (int) theY,0);
    }

    protected void impulse(PatchSignal theSignal) {
	triggerControlActions();
	_myGroup.patch.invokeMethod(new Object[] { new PatchEvent(this,
		theSignal) });
	for (PatchEdge o : edges) {
	    PatchSignal mySignal = (PatchSignal) theSignal.clone();
	    mySignal.reset();
	    mySignal.setPreviousNode(mySignal.getCurrentNode());
	    mySignal.setCurrentNode(this);
	    mySignal.setNextNode(o.getNextNode(this));
	    if (mySignal.mode() == PatchSignal.DYNAMIC) {
		mySignal.setTimeIntervalScalar(_myGroup.patch.intervalSpeed);
		mySignal.setTimeInterval((long) (o.myLength * 10));
	    }
	    if (mySignal.getNextNode() != null) {
		_mySignals.add(mySignal);
	    }
	    dimension.set(20,20,0);
	}
    }

    public void forwardSignal(PatchSignal theSignal) {
	if (theSignal.getNextNode() != null) {
	    theSignal.getNextNode().impulse(theSignal);
	}
	removeSignal(theSignal);
    }

    public void removeSignal(PatchSignal theSignal) {
	_mySignals.remove(theSignal);
    }

    public Vector signals() {
	return _mySignals;
    }

    /**
     * remove the node. void PatchNode
     */
    public void remove() {
	isRemove = true;
	_mySignals.clear();
	for (int i = edges.size() - 1; i >= 0; i--) {
	    ((PatchEdge) edges.get(i)).remove();
	}
	edges.clear();

	for (PatchActionHandler o : patchActionHandlers) {
	    o.setRemove(true);
	}
	patchActionHandlers.clear();
    }

    public void update() {
	checkControlActionRemove();
	updateControlActions();
    }

    public void draw(PApplet theApplet) {
	if (isActive && !isDragged) {
	    theApplet.fill(colorActive);
	    theApplet.noStroke();
	    theApplet.rect((int) (ghostposition.x ),
		    (int) (ghostposition.y), 16, 16);
	    theApplet.stroke(colorActive);
	    for (PatchEdge o : edges) {
		theApplet.line(o.get_NOT_Position(this).x, o
			.get_NOT_Position(this).y, ghostposition.x,
			ghostposition.y);
	    }
	}

	theApplet.noStroke();
	if (isActive || isSelected) {
	    theApplet.fill(colorActive);
	} else {
	    theApplet.fill(colorForeground);
	}
	theApplet.rect((int) (position.x), (int) (position.y), 6, 6);
	dimension.mult(0.9f);
	theApplet.fill(colorActive, dimension.x * 10);
	theApplet.rect((int) (position.x),
		(int) (position.y), (int) (dimension.x),
		(int) (dimension.y));

	drawControlActions(theApplet);
    }

    boolean inside(float theX, float theY) {
	return (theX > position.x - 10 && theX < (position.x + 10)
		&& theY > position.y - 10 && theY < (position.y + 10));
    }

    public boolean insideRect(float theX1, float theY1, float theX2, float theY2) {
	return (position.x > theX1 && position.x < theX2 && position.y > theY1 && position.y < theY2);
    }

    public boolean addPatchAction(PatchActionObject theContainer) {
	if (inside(theContainer.position.x, theContainer.position.y)) {
	    for (int i = 0; i < theContainer.get().size(); i++) {
		addPatchAction(((PatchActionHandler) theContainer.get().get(i)));
	    }
	    return true;
	}
	return false;
    }

    public boolean addPatchAction(PatchActionHandler thePatchActionHandler) {
	PatchActionHandler myPatchActionHandler = (PatchActionHandler) thePatchActionHandler
		.clone();
	myPatchActionHandler.setNode(this);
	myPatchActionHandler.setId(_myGroup.patch.getPatchActionId());
	myPatchActionHandler.init();
	patchActionHandlers.add(myPatchActionHandler);
	return true;
    }

    public void removePatchAction(PatchActionHandler thePatchActionHandler) {
	thePatchActionHandler.setRemove(true);
    }

    public void removePatchAction(int theId) {
	for (PatchActionHandler o : patchActionHandlers) {
	    if (o.id() == theId) {
		removePatchAction(o);
		return;
	    }
	}
    }

    void drawControlActions(PApplet theApplet) {
	int n = 0;
	for (PatchActionHandler o : patchActionHandlers) {
	    theApplet.noStroke();
	    theApplet.fill(o.getColor());
	    theApplet.rect((int) (position.x + 6 + n * 4),
		    (int) (position.y - 3), 2, 6);
	    o.draw(theApplet,position.x, position.y, position.z);
	    o.draw(theApplet,position.x, position.y);
	    o.draw(position.x, position.y);
	    n++;
	}
    }

    void updateControlActions() {
	for (PatchActionHandler o : patchActionHandlers) {
	    o.update();
	}
    }

    void checkControlActionRemove() {
	for (int i = patchActionHandlers.size() - 1; i >= 0; i--) {
	    if (((PatchActionHandler) patchActionHandlers.get(i)).isRemove()) {
		patchActionHandlers.remove(i);
	    }
	}
    }

    void triggerControlActions() {
	for (PatchActionHandler o : patchActionHandlers) {
	    o.trigger();
	}
    }

    public Vector getActions() {
	return patchActionHandlers;
    }

    public void setX(float theX) {
	if (isMousePressed == false) {
	    updatePosition(theX, position.y);
	}
    }

    public void setY(float theY) {
	if (isMousePressed == false) {
	    updatePosition(position.x, theY);
	}
    }

    protected void setId(int theId) {
	id = theId;
    }

    public int id() {
	return id;
    }

    public PVector position() {
	return position;
    }

    public PatchNode copy() {
	return (PatchNode) this.clone();
    }

    public Object clone() {
	try {
	    return super.clone();
	} catch (CloneNotSupportedException e) { // Dire trouble!!!
	    throw new InternalError("But we are Cloneable!!!");
	}
    }

    public String toString() {
	return ("n," + id() + "," + (int) position.x + "," + (int) position.y);
    }

}
