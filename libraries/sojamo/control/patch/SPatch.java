package sojamo.control.patch;

import processing.core.PApplet;

import java.util.List;
import java.util.Vector;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class SPatch {

	// edge decay
	// node and edge lifetime
	// weighting
	// node capacitor

	// decision trees
	// http://www.onlamp.com/pub/a/python/2006/02/09/ai_decision_trees.html
	// http://en.wikipedia.org/wiki/Decision_Trees
	//
	// 3d mouse
	// http://processing.org/discourse/yabb_beta/YaBB.cgi?board=OpenGL;action=display;num=1176483247

	public final Vector<PatchGroup> groups;

	public final static String VERSION = "0.1.1";

	protected final PApplet papplet;

	protected boolean isMouseDown;

	protected boolean isEventMethod;

	protected Method _myEventMethod;

	protected int patchActionId = 14112;

	protected Vector<PatchActionHandler> autoActionHandlers;

	protected float intervalSpeed = 1;

	protected static int strokeWeight = 2;

	protected int _myLastNodeId = -1;

	protected int _myCurrentNodeId = -1;

	protected int idCounter = 0;

	public SPatch(PApplet thePApplet) {
		papplet = thePApplet;

		groups = new Vector<PatchGroup>();
		groups.add(new PatchGroup(this, 0));

		autoActionHandlers = new Vector<PatchActionHandler>();
		Class myClass = papplet.getClass();
		Method[] myMethods = myClass.getDeclaredMethods();
		for (int i = 0; i < myMethods.length; i++) {
			if ((myMethods[i].getName()).equals("patchEvent")) {
				if (myMethods[i].getParameterTypes().length == 1) {
					if (myMethods[i].getParameterTypes()[0] == PatchEvent.class) {
						isEventMethod = true;
						_myEventMethod = myMethods[i];
						return;
					}
				}
			}
		}
		welcome();
	}

	private void reset() {
		_myLastNodeId = -1;
		_myCurrentNodeId = -1;
		idCounter = 0;
	}

	private void welcome() {
		System.out.println("sojamo.patch " + VERSION);
	}

	protected void invokeMethod(final Object[] theParam) {
		try {
			_myEventMethod.invoke(papplet, theParam);
		} catch (IllegalArgumentException e) {
			// System.out.println("### ERROR @ ControlP5.invokeMethod " +
			// theMethod.getName() +" "+theParam.length+ " " + e);
			/**
			 * @todo thrown when plugging a String method/field.
			 */
		} catch (IllegalAccessException e) {
			// printMethodError(theMethod, e);
		} catch (InvocationTargetException e) {
			// printMethodError(theMethod, e);
		} catch (NullPointerException e) {
			// printMethodError(theMethod, e);
		}

	}

	protected int getPatchActionId() {
		patchActionId++;
		return patchActionId;
	}

	public PApplet papplet() {
		return papplet;
	}

	public void draw() {
		int myRectMode = papplet.g.rectMode;
		int myEllipseMode = papplet.g.ellipseMode;
		papplet.pushStyle();
		papplet.rectMode(PApplet.CENTER);
		papplet.pushMatrix();
		papplet.rectMode(papplet.CORNER);
		papplet.ellipseMode(papplet.CORNER);

		isMouseDown = papplet.mousePressed;

		for (PatchGroup o : groups) {
			o.update();
			o.draw(papplet);
		}

		papplet.popMatrix();
		papplet.rectMode(myRectMode);
		papplet.ellipseMode(myEllipseMode);
		papplet.popStyle();

	}

	public void autoAdd(PatchActionHandler thePatchActionHandler) {
		for (PatchActionHandler o : autoActionHandlers) {
			if (o.equals(thePatchActionHandler)) {
				return;
			}
		}
		autoActionHandlers.add(thePatchActionHandler);
	}

	public void add(PatchActionObject thePatchActionObject) {
		groups.get(0).add(thePatchActionObject);
	}

	/**
	 * ?
	 * 
	 * @param theId
	 * @param theX
	 * @param theY
	 * @return PatchNode SPatch
	 */
	// public void add(PatchActionObject thePatchActionObject, PatchGroup
	// theGroup) {
	// for (int i = 0; i < groups.size(); i++) {
	// if (theGroup.id() == ((PatchGroup) groups.get(i)).id) {
	// theGroup.add(thePatchActionObject);
	// return;
	// }
	// }
	// int n = groups.size();
	// groups.add(new PatchGroup(this));
	// ((PatchGroup) groups.get(n)).add(thePatchActionObject);
	// }
	
	PatchNode addNode(int theId, float theX, float theY) {
		return addNode(theId, theX, theY, 0);
	}
	
	
	PatchNode addNode(int theId, float theX, float theY, float theZ) {
		PatchNode n = addNode(groups.get(0), theId, theX, theY, theZ);
		return n;
	}
	
	public PatchNode addNode(float theX, float theY) {
		return addNode(groups.get(0), -1, theX, theY, 0);
	}

	public PatchNode addNode(float theX, float theY, float theZ) {
		return addNode(groups.get(0), -1, theX, theY,theZ);
	}

	public PatchNode addNode(PatchGroup theGroup, int theId, float theX, float theY, float theZ) {
		// check of group exists.
		PatchNode myNode = null;
		if (theId < 0) {
			myNode = theGroup.addNode(theX, theY, theZ);
		} else {
			myNode = theGroup.addNode(theId, theX, theY, theZ);
		}
		for (int i = 0; i < autoActionHandlers.size(); i++) {
			myNode.addPatchAction(((PatchActionHandler) autoActionHandlers.get(i)));
		}
		_myLastNodeId = _myCurrentNodeId;
		_myCurrentNodeId = myNode.id;
		return myNode;
	}

	public PatchEdge connect(PatchNode theNodeA, PatchNode theNodeB) {
		return connect(-1, theNodeA, theNodeB);
	}

	public PatchEdge connect(int theId, PatchNode theNodeA, PatchNode theNodeB) {
		try {
			if (theNodeA.id() == -1) {
				addNode(theNodeA.position.x, theNodeA.position.y, theNodeA.position.z);
			}
			if (theNodeB.id() == -1) {
				// !!! shouldnt that be theNodeB, test.
				addNode(theNodeA.position.x, theNodeA.position.y,theNodeA.position.z);
			}
			PatchEdge myPatchEdge = new PatchEdge(theId, theNodeA, groups.get(0));
			myPatchEdge.setB(theNodeB);
			myPatchEdge.updateVector();
			groups.get(0).addEdge(myPatchEdge);
			return myPatchEdge;
		} catch (Exception e) {

		}
		return null;
	}

	public PatchNode getNodeById(int theId) {
		for (int i = 0; i < groups.get(0).nodes.size(); i++) {
			if (((PatchNode) groups.get(0).nodes.elementAt(i)).id() == theId) {
				return ((PatchNode) groups.get(0).nodes.elementAt(i));
			}
		}
		return null;
	}

	public void setIntervalSpeed(float theValue) {
		intervalSpeed = theValue;
		for (int i = 0; i < groups.get(0).nodes.size(); i++) {
			PatchNode myPatchNode = ((PatchNode) groups.get(0).nodes.get(i));
			for (int j = 0; j < myPatchNode.signals().size(); j++) {
				((PatchSignal) myPatchNode.signals().get(j)).setTimeIntervalScalar(theValue);
			}
		}
	}

	public Vector nodes() {
		return groups.get(0).nodes;
	}

	public void print() {
		for (int i = 0; i < groups.get(0).nodes.size(); i++) {
			System.out.println((PatchNode) groups.get(0).nodes.get(i));
		}

		for (int i = 0; i < groups.get(0).edges.size(); i++) {
			System.out.println((PatchEdge) groups.get(0).edges.get(i));
		}
	}

	public void clear() {
		for (PatchGroup o : groups) {
			o.removeNodes();
			o.removeEdges();
			o.init();
			o.reset();
		}
		reset();
	}

	/**
	 * save a patch. void SPatch
	 */
	public void save() {
		int cnt = groups.size();

		for (PatchGroup o : groups) {
			cnt += o.nodes.size();
			cnt += o.edges.size();
		}

		String[] s = new String[cnt];

		int n = 0;

		for (PatchGroup o : groups) {
			s[n] = o.toString();
			n++;

			for (PatchNode o1 : o.nodes) {
				s[n] = o1.toString();
				n++;
			}

			for (PatchEdge o2 : o.edges) {
				s[n] = o2.toString();
				n++;
			}
		}
		papplet.saveStrings("data/test.patch", s);
		System.out.println("test.patch");
	}

	/**
	 * load a patch. void SPatch
	 */
	public void load() {
		// group: g,id
		// node: n,id,x,y
		// edge: e,id,nodeA,nodeB,direction
		int cnt = -1;
		clear();
		String[] s = papplet.loadStrings(papplet.dataPath("test.patch"));
		for (int i = 0; i < s.length; i++) {
			String[] item = papplet.split(s[i], ',');
			if (item[0].charAt(0) == 'g') {

			}

			if (item[0].charAt(0) == 'n') {
				addNode((new Integer(item[1])).intValue(), (new Integer(item[2])).intValue(),
						(new Integer(item[3])).intValue());
			}

			if (item[0].charAt(0) == 'e') {
				PatchEdge e = connect(getNodeById((new Integer(item[2])).intValue()),
						getNodeById((new Integer(item[3])).intValue()));
				e.setId((new Integer(item[1])).intValue());
				e.setDirection((new Integer(item[4])).intValue());
			}

			if ((new Integer(item[1])).intValue() > cnt) {
				cnt = (new Integer(item[1])).intValue();
			}

			idCounter = cnt;
		}
	}
}

// nice synthesizer by pyramind. the proximity mouse
// gesture recording functions are nice
// http://www.vimeo.com/938314
// 
