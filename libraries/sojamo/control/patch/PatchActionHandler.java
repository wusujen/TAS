package sojamo.control.patch;

import processing.core.PApplet;
import processing.core.PVector;

public class PatchActionHandler implements Cloneable {

    protected int color = 0xff00698c;

    boolean isRemove;

    private int _myId;

    private boolean isCloned = false;

    private PatchActionObject _myParent;

    public PatchNode _myNode;

    public PVector position = new PVector(0, 0, 0);

    protected boolean isProtected = false;

    public void init() {
    }

    public void trigger() {
    }

    public void update() {
    }

    public void draw(PApplet theApplet, float theX, float theY, float theZ) {
    }

    public void draw(PApplet theApplet, float theX, float theY) {
    }

    public void draw(float theX, float theY) {
    }

    public void mousePressed() {
    }

    public void mouseReleased() {
    }

    public void onEnter() {
    }

    public void onLeave() {
    }

    protected void setParent(PatchActionObject theObject) {
	_myParent = theObject;
    }

    public PatchActionObject parent() {
	return _myParent;
    }

    public void setProtected(boolean theFlag) {
	isProtected = theFlag;
    }

    public boolean isProtected() {
	return isProtected;
    }

    public void setRemove(boolean theFlag) {
	if (!isProtected) {
	    isRemove = theFlag;
	    remove();
	}
    }

    public void remove() {
	isRemove = true;
    }

    protected boolean isRemove() {
	return isRemove;
    }

    protected final void setNode(PatchNode theNode) {
	_myNode = theNode;
    }

    public final PatchNode node() {
	return _myNode;
    }

    public final float x() {
	return _myParent.position.x;
    }

    public final float y() {
	return _myParent.position.y;
    }

    public final float z() {
	return _myParent.position.z;
    }

    protected final void setId(int theId) {
	_myId = theId;
    }

    public final int id() {
	return _myId;
    }

    public void cloned() {
    }

    public boolean isCloned() {
	return isCloned;
    }

    public void setColor(int theColor) {
	color = theColor;
    }

    public int getColor() {
	return color;
    }

    public Object clone() {
	try {
	    PatchActionHandler myClone = (PatchActionHandler) super.clone();
	    myClone.cloned();
	    myClone.isCloned = true;
	    return myClone;
	} catch (CloneNotSupportedException e) {
	    throw new InternalError("But we are Cloneable!!!");
	}
    }
}
