package sojamo.control.patch;

import java.util.Vector;
import processing.core.PApplet;

class PatchGroup {

	protected float mouseX = 0;

	protected float mouseY = 0;

	Vector<PatchNode> nodes;

	Vector<PatchEdge> edges;

	boolean isVisible;

	boolean isMousePressed;

	boolean isMouseReleased;

	PatchNode lineNode;

	PatchNode currentNode;

	PatchEdge currentEdge;

	public final static int DRAWEDGE = 1;

	public final static int MOVENODE = 2;

	public final static int IMPULSE = 3;

	public final static int REMOVE = 4;

	public final static int SELECT = 5;

	protected int currentTool = MOVENODE;

	private boolean isActive = false;

	private boolean isOver = false;

	public boolean isLineVisible = true;

	protected AbstractPatchInputHandler inputhandler;

	protected SPatch patch;

	protected boolean isMouseBusy;

	public final static int NETWORK = 0;

	public final static int NODEMENU = 1;

	int isBlocked = NODEMENU;

	protected Vector<PatchActionObject> patchActionObjects;

	protected PatchSelector patchselector;

	protected final int id;

	public PatchGroup(final SPatch thePatch, int theId) {
		patch = thePatch;
		id = theId; // needs to be defined.
		setPatchInputHandler(new PatchInputHandler(this));
		patchselector = new PatchSelector(this);
		init();
	}

	public void init() {
		isVisible = true;
		patchActionObjects = new Vector<PatchActionObject>();
		nodes = new Vector<PatchNode>();
		edges = new Vector<PatchEdge>();
	}

	public int id() {
		return id;
	}

	public void setPatchInputHandler(AbstractPatchInputHandler theInputHandler) {
		inputhandler = theInputHandler;
	}

	public void update() {

		mouseX = inputhandler.mouseX;
		mouseY = inputhandler.mouseY;
		isOver = false;

		isMouseReleased = false;

		patchselector.update();

		if (isMousePressed && isMousePressed != inputhandler.mousePressed) {
			isMouseReleased = true;
		}

		isMousePressed = inputhandler.mousePressed;

		for (int i = nodes.size() - 1; i >= 0; i--) {
			if (((PatchNode) nodes.elementAt(i)).isRemove) {
				nodes.remove(i);
			}
		}

		for (int i = edges.size() - 1; i >= 0; i--) {
			if (((PatchEdge) edges.elementAt(i)).isRemove) {
				removeEdge((PatchEdge) edges.elementAt(i));
			}
		}

		for (PatchNode o : nodes) {

			if (patchselector.selectionMode != patchselector.MOVEITEMS) {
				if (o.insideRect(patchselector.x1, patchselector.y1, patchselector.x2,
						patchselector.y2)) {
					o.isSelected = true;
				} else {
					o.isSelected = false;
				}
			}
			if (o.isSelected) {
				if (patchselector.selectionMode == patchselector.MOVEITEMS) {
					o.updatePosition(o.position.x + patchselector.diffMousePosition.x, o.position.y
							+ patchselector.diffMousePosition.y);
				}
				if (inputhandler.keysPressed[PatchInputListener.BACKSPACE] == true) {
					o.remove();
				}
			}
		}

	}

	protected void removeNodes() {
		for (PatchNode o : nodes) {
			o.remove();
		}
	}

	protected void removeEdges() {
		for (int i = edges.size() - 1; i >= 0; i--) {
			if (((PatchEdge) edges.elementAt(i)).isRemove) {
				removeEdge((PatchEdge) edges.elementAt(i));
			}
		}
	}

	public void draw(PApplet theApplet) {
		theApplet.pushMatrix();

		if (isMousePressed && inputhandler.keysPressed[PatchInputListener.ALT] == true
				&& inputhandler.keysPressed[PatchInputListener.CTRL] == true
				&& isMouseBusy == false) {

			for (PatchActionObject o : patchActionObjects) {
				if (o.isMoveable()) {
					o.position.x += patch.papplet.mouseX - patch.papplet.pmouseX;
					o.position.y += patch.papplet.mouseY - patch.papplet.pmouseY;
				}
			}

			for (PatchNode o : nodes) {
				o.position.x += patch.papplet.mouseX - patch.papplet.pmouseX;
				o.position.y += patch.papplet.mouseY - patch.papplet.pmouseY;
			}
		}

		for (PatchActionObject o : patchActionObjects) {
			if (patchselector.selectionMode != patchselector.MOVEITEMS) {
				if (o.insideRect(patchselector.x1, patchselector.y1, patchselector.x2,
						patchselector.y2)) {
					o.isSelected = true;
				} else {
					o.isSelected = false;
				}
			}
			if (o.isSelected && patchselector.selectionMode == patchselector.MOVEITEMS) {
				o.position.x += patchselector.diffMousePosition.x;
				o.position.y += patchselector.diffMousePosition.y;
			}

			o.draw(patch.papplet);

		}

		patchselector.draw(patch.papplet);

		for (int i = 0; i < nodes.size(); i++) {
			((PatchNode) nodes.elementAt(i)).update(mouseX, mouseY,0);
			((PatchNode) nodes.elementAt(i)).update();
		}

		for (int i = 0; i < edges.size(); i++) {
			((PatchEdge) edges.elementAt(i)).update();
			((PatchEdge) edges.elementAt(i)).draw(theApplet);
		}

		for (int i = 0; i < nodes.size(); i++) {
			((PatchNode) nodes.elementAt(i)).draw(theApplet);
		}

		if (isMouseReleased) {
			deactivate();
		}
		theApplet.popMatrix();
	}

	protected boolean controlActionDropped(PatchActionObject theContainer) {
		for (int i = 0; i < nodes.size(); i++) {
			if (((PatchNode) nodes.elementAt(i)).addPatchAction(theContainer) == true) {
				return true;
			}
		}
		return false;
	}

	public void activateDrawEdge(PatchNode theNode) {
		currentTool = DRAWEDGE;
		if (!isActive) {
			lineNode = theNode;
			currentEdge = new PatchEdge(-1, theNode, this);
			addEdge(currentEdge);
			isActive = true;
		}
	}

	public void deactivate() {
		if (isActive) {
			switch (currentTool) {
			case (DRAWEDGE):
				if (lineNode != null && lineNode != currentNode && isOver) {
					currentEdge.setB(currentNode);
					currentEdge.updateVector();
				}
				break;
			}
			reset();
		}
		checkEdges();
	}

	public void add(PatchActionObject thePatchActionObject) {
		thePatchActionObject.init(patch);
		patchActionObjects.add(thePatchActionObject);
	}

	protected PatchEdge addEdge(PatchEdge theEdge) {
		theEdge.setId(patch.idCounter);
		edges.add(theEdge);
		patch.idCounter++;
		return theEdge;
	}

	protected boolean checkEdge(PatchEdge theEdge) {
		return currentEdge == theEdge;
	}

	protected Vector getEdges() {
		return edges;
	}

	public void checkEdges() {
		for (int i = 0; i < edges.size(); i++) {
			if (!((PatchEdge) edges.elementAt(i)).connected()) {
				edges.remove(i);
			}
		}
	}

	public void removeEdge(PatchEdge theEdge) {
		theEdge.die();
		edges.remove(theEdge);
	}

	protected PatchNode addNode(int theId, float theX, float theY, float theZ) {
		PatchNode myNode = new PatchNode(this, theX, theY, theZ);
		myNode.setId(theId);
		nodes.add(myNode);
		if (theId >= patch.idCounter) {
			patch.idCounter = theId + 1;
		}
		return myNode;
	}

	public PatchNode addNode(float theX, float theY, float theZ) {
		PatchNode myNode = new PatchNode(this, theX, theY, theZ);
		myNode.setId(patch.idCounter);
		nodes.add(myNode);
		patch.idCounter++;
		System.out.println("adding node " + patch.idCounter);
		return myNode;
	}

	protected void reset() {
		isActive = false;
		isMouseBusy = false;
		currentEdge = null;
		lineNode = null;
		currentNode = null;
	}

	public void setCurrentNode(PatchNode theNode) {
		if (!isOver) {
			isOver = true;
			currentNode = theNode;
		}
	}

	public void setTool(int theTool) {
		currentTool = theTool;
	}

	public String toString() {
		return ("g" + "," + id() + "," + (isVisible ? 1 : 0));
	}

	public void setVisible(boolean theFlag) {
		isVisible = theFlag;
	}
}
