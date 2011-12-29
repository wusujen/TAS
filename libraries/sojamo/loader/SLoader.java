package sojamo.loader;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.*;
import java.util.LinkedList;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PGraphics;

public class SLoader {

	protected LinkedList<String> pappletMethods;

	protected LinkedList<String> pappletFields;

	protected LinkedList<String> graphicsMethods;

	protected LinkedList<String> graphicsFields;

	protected String[] overwrittenMethods;

	PApplet papplet;

	PGraphics renderer;

	protected Vector<SketchObject> _mySketchObjects;

	public static boolean DEBUG = false;

	SketchParser _mySketchParser;

	protected SketchCompiler _myCompiler;

	public SLoader(PApplet theApplet) {
		papplet = theApplet;
		renderer = theApplet.g;
		_mySketchParser = new SketchParser(this);
		_myCompiler = new SketchCompiler(this);
		init();
	}

	/**
	 * 
	 * void SLoader
	 */
	private void init() {

		_mySketchObjects = new Vector<SketchObject>();
		// modifiers and ids
		// 25 public static final
		// 17 public final
		// 9 public static
		// 1 public

		overwrittenMethods = new String[] { "loadImage", "loadShape", "loadBytes", "loadStrings",
				"loadFont", "createReader", "createWriter", "requestImage", "save", "saveBytes",
				"saveFrame", "saveStream", "saveStrings" };

		pappletMethods = new LinkedList<String>();
		pappletFields = new LinkedList<String>();
		graphicsMethods = new LinkedList<String>();
		graphicsFields = new LinkedList<String>();
		getMethodsAndFields("processing.core.PApplet", pappletMethods, pappletFields);
		getMethodsAndFields("processing.core.PGraphics", graphicsMethods, graphicsFields);

		papplet.registerDraw(this);
		papplet.registerMouseEvent(this);
		papplet.registerKeyEvent(this);
		papplet.registerDispose(this);
	}

	public void dispose() {
		papplet.unregisterDraw(this);
		papplet.unregisterMouseEvent(this);
		papplet.unregisterKeyEvent(this);
		papplet.unregisterDispose(this);
	}

	private void getMethodsAndFields(String theClassName, LinkedList<String> theMethodList,
			LinkedList<String> theFieldList) {
		try {
			Class c = Class.forName(theClassName);
			Method m[] = c.getDeclaredMethods();

			for (Method element : m) {
				int mdf = element.getModifiers();
				if (mdf == 25 || mdf == 17 || mdf == 9 || mdf == 1) {
					if (!theMethodList.contains(element.getName())) {
						theMethodList.add((element.getName()));
					}
				}
			}
			for (int i = 0; i < overwrittenMethods.length; i++) {
				theMethodList.remove(overwrittenMethods[i]);
			}

		} catch (Throwable e) {
			System.err.println(e);
		}

		try {
			Class c = Class.forName("processing.core.PApplet");
			Field f[] = c.getDeclaredFields();

			for (Field element : f) {
				int mdf = element.getModifiers();
				if (mdf == 25 || mdf == 17 || mdf == 9 || mdf == 1) {
					theFieldList.add(element.getName());
				}
			}
		} catch (Throwable e) {
			System.err.println(e);
		}
	}

	protected void register(SketchObject theObject) {
		for (SketchObject o : _mySketchObjects) {
			if (o.properties.className.equals(theObject.properties.className)) {
				_mySketchObjects.remove(o);
				System.out.println("removing " + theObject.properties.className);
				break;
			}
		}
		_mySketchObjects.add(theObject);
		System.out.println(_mySketchObjects.size());
	}

	public void remove(SketchObject theObject) {
		if (theObject != null) {
			_mySketchObjects.remove(theObject);
		}
	}

	public SketchObject load(SketchProperties theProps) {
		return _myCompiler.compile(papplet, _mySketchParser.parse(theProps), theProps);
	}

	public void draw() {
		for (SketchObject o : _mySketchObjects) {
			if (o.isAutoDraw()) {
				o.draw();
			}
		}
	}

	public void keyEvent(final KeyEvent theKeyEvent) {
		switch (theKeyEvent.getID()) {
		case KeyEvent.KEY_PRESSED:
			keyPressed();
			break;
		case KeyEvent.KEY_RELEASED:
			keyReleased();
			break;
		case KeyEvent.KEY_TYPED:
			keyTyped();
			break;
		}
	}

	/**
	 * 
	 * @param theMouseEvent
	 *            void Sketch
	 */
	public void mouseEvent(final MouseEvent theMouseEvent) {
		if (theMouseEvent.getID() == MouseEvent.MOUSE_PRESSED) {
			mousePressed();
		}
		if (theMouseEvent.getID() == MouseEvent.MOUSE_RELEASED) {
			mouseReleased();
		}
		if (theMouseEvent.getID() == MouseEvent.MOUSE_DRAGGED) {
			mouseDragged();
		}
		if (theMouseEvent.getID() == MouseEvent.MOUSE_MOVED) {
			mouseMoved();
		}
		if (theMouseEvent.getID() == MouseEvent.MOUSE_CLICKED) {
			mouseClicked();
		}
	}

	public void mousePressed() {
		for (SketchObject o : _mySketchObjects) {
			o.mousePressed();
		}
	}

	public void mouseReleased() {
		for (SketchObject o : _mySketchObjects) {
			o.mouseReleased();
		}
	}

	public void mouseClicked() {
		for (SketchObject o : _mySketchObjects) {
			o.mouseClicked();
		}
	}

	public void mouseDragged() {
		for (SketchObject o : _mySketchObjects) {
			o.mouseDragged();
		}
	}

	public void mouseMoved() {
		for (SketchObject o : _mySketchObjects) {
			o.mouseMoved();
		}
	}

	public void keyPressed() {
		for (SketchObject o : _mySketchObjects) {
			o.keyPressed();
		}
	}

	public void keyReleased() {
		for (SketchObject o : _mySketchObjects) {
			o.keyReleased();
		}
	}

	public void keyTyped() {
		for (SketchObject o : _mySketchObjects) {
			o.keyTyped();
		}
	}

	public static void debug(String theString) {
		if (DEBUG) {
			System.out.println(theString);
		}
	}
}
