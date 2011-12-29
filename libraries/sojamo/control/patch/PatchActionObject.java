package sojamo.control.patch;

import java.util.Vector;
import processing.core.PApplet;
import processing.core.PVector;

public class PatchActionObject implements PatchInputListener, PDrawable {
    
    /**
     * a visual representation of a patch action.
     */
    
    boolean isDragged;

    float w, h;

    float prevX, prevY;

    Vector<PatchActionHandler> patchActionHandlers;

    public PVector position;

    protected boolean isSelected;

    protected boolean isInside;

    protected boolean isMouseDown;

    protected SPatch _myPatch;

    protected boolean isVisible = true;

    protected boolean isMoveable = false;

    protected int color = 0xffffffff;

    public PatchActionObject(float theX, float theY) {
	patchActionHandlers = new Vector<PatchActionHandler>();
	position = new PVector(theX, theY, 0);
	w = 10;
	h = 10;
    }

    protected void init(SPatch theSPatch) {
	_myPatch = theSPatch;
	_myPatch.groups.get(0).inputhandler.add(this);
    }

    public int category() {
	return PatchGroup.NETWORK;
    }

    public boolean mousePressed() {
	if (inside(_myPatch.groups.get(0).mouseX, _myPatch.groups.get(0).mouseY)) {
	    _myPatch.groups.get(0).isMouseBusy = true;
	    isMouseDown = true;
	    prevX = position.x;
	    prevY = position.y;
	    return true;
	}
	return false;
    }

    public void mouseReleased() {
	if (isMouseDown) {
	    _myPatch.groups.get(0).isMouseBusy = false;
	    isMouseDown = false;
	    dropped(_myPatch.groups.get(0));
	    if (isDragged == false) {
		position.x = prevX;
		position.y = prevY;
	    }
	}
    }

    public void keyPressed(int theKeyCode) {
	if (theKeyCode == CTRL) {
	    isDragged = true;
	}
    }

    public void keyReleased(int theKeyCode) {
	if (theKeyCode == CTRL) {
	    isDragged = false;
	}
    }

    public void setColor(int theColor) {
	color = theColor;
	for (PatchActionHandler o : patchActionHandlers) {
	    o.setColor(color);
	}

    }

    public int getColor() {
	return color;
    }

    public void add(PatchActionHandler theActionContainer) {
	theActionContainer.setParent(this);
	theActionContainer.setColor(color);
	patchActionHandlers.add(theActionContainer);
    }

    Vector get() {
	return patchActionHandlers;
    }

    void dropped(PatchGroup themaingroup) {
	if (themaingroup.controlActionDropped(this)) {
	    position.x = prevX;
	    position.y = prevY;
	}
    }

    public void update() {
    }

    public void draw(PApplet theApplet) {
	if (isVisible) {

	    theApplet.noStroke();

	    if (isMouseDown) {
		position.x = _myPatch.groups.get(0).mouseX;
		position.y = _myPatch.groups.get(0).mouseY;
	    }

	    for (PatchActionHandler o : patchActionHandlers) {
		o.update();
		o.draw(theApplet, position.x, position.y);
		theApplet.fill(o.getColor());
	    }
	    if (isMouseDown) {
		theApplet.fill(255);
	    }
	    theApplet.rect(position.x, position.y, w, h);
	}
    }

    public void show() {
	isVisible = true;
    }

    public void hide() {
	isVisible = false;
    }

    public boolean isVisible() {
	return isVisible;
    }

    public void setMoveable(boolean theFlag) {
	isMoveable = theFlag;
    }

    public boolean isMoveable() {
	return isMoveable;
    }

    public boolean insideRect(float theX1, float theY1, float theX2, float theY2) {
	return ((position.x + 2) > theX1 && (position.x + 2) < theX2
		&& (position.y + 2) > theY1 && (position.y + 2) < theY2);
    }

    boolean inside(float theX, float theY) {
	return (theX > position.x && theX < (position.x + w)
		&& theY > position.y && theY < (position.y + h));
    }
}
