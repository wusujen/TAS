package sojamo.loader;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

public class SketchObject implements Cloneable, PConstants {

	protected PApplet papplet;
	
	public SLoader loader;

	protected PGraphics graphics;

	public SketchProperties properties;

	public int width;

	public int height;

	private boolean isAutoDraw;

	private boolean isPushStyle;

	public PVector position;

	public PVector rotation;

	boolean is3D;
	
	protected boolean graphicsIsPApplet;

	public SketchObject() {
		width = 100;
		height = 100;
		isAutoDraw = true;
		isPushStyle = true;
		position = new PVector();
		rotation = new PVector();
		graphicsIsPApplet = true;
	}

	public void init() {
		loader.register(this);
		setup();
	}

	public boolean setAutoDraw(boolean theFlag) {
		isAutoDraw = theFlag;
		return isAutoDraw;
	}

	public boolean isAutoDraw() {
		return isAutoDraw;
	}

	public boolean setPushStyle(final boolean theFlag) {
		isPushStyle = theFlag;
		return isPushStyle;
	}

	public boolean isPushStyle() {
		return isAutoDraw;
	}

	public void draw() {
	}

	public void setup() {
	}
	
	public PApplet papplet() {
		return papplet;
	}
	
	public PGraphics graphics() {
		return graphics;
	}
	
	public void setGraphics(PGraphics theGraphics) {
		graphicsIsPApplet = false;
		graphics = theGraphics;
	}
	
	public void setGraphics(PApplet theApplet) {
		graphicsIsPApplet = true;
		graphics = theApplet.g;
	}
	

	public void mousePressed() {
	}

	public void mouseReleased() {
	}

	public void mouseClicked() {
	}

	public void mouseDragged() {
	}

	public void mouseMoved() {
	}

	public void keyPressed() {
	}

	public void keyReleased() {
	}

	public void keyTyped() {
	}

	public void preDraw() {
		if (isPushStyle) {
			papplet.pushStyle();
		}
		papplet.pushMatrix();
		if (is3D) {
			papplet.translate(position.x, position.y, position.z);
			papplet.rotateX(rotation.x);
			papplet.rotateY(rotation.y);
			papplet.rotateZ(rotation.z);
		} else {
			papplet.translate(position.x, position.y);
		}
	}

	public void postDraw() {
		papplet.popMatrix();
		if (isPushStyle) {
			papplet.popStyle();
		}
	}

	/**
	 * 
	 */
	public SketchObject clone() {
		try {
			final SketchObject sketch = (SketchObject) super.clone();
			sketch.init();
			return sketch;
		} catch (final CloneNotSupportedException e) { // Dire trouble!!!
			throw new InternalError("But we are Cloneable!!!");
		}
	}

	/**
	 * overwritten from PApplet to adjust the file path.
	 * 
	 * @param theFileName
	 * @return PImage SketchObject
	 */
	public PImage loadImage(final String theFileName) {
		return papplet.loadImage(adjustFilePath(theFileName));
	}

	/**
	 * overwritten from PApplet to adjust the file path.
	 * 
	 * @param theFileName
	 * @param theExtension
	 * @return PImage SketchObject
	 */
	public PImage loadImage(final String theFileName, final String theExtension) {
		return papplet.loadImage(adjustFilePath(theFileName), theExtension);
	}

	/**
	 * overwritten from PApplet to adjust the file path.
	 * 
	 * @param theFileName
	 * @return PImage SketchObject
	 */
	public PImage requestImage(final String theFileName) {
		return papplet.requestImage(adjustFilePath(theFileName));
	}

	/**
	 * overwritten from PApplet to adjust the file path.
	 * 
	 * @param theFileName
	 * @param theExtension
	 * @return PImage SketchObject
	 */
	public PImage requestImage(final String theFileName, final String theExtension) {
		return papplet.requestImage(adjustFilePath(theFileName), theExtension);
	}

	/**
	 * overwritten from PApplet to adjust the file path.
	 * 
	 * @param theFileName
	 * @return PShape SketchObject
	 */
	public PShape loadShape(final String theFileName) {
		return papplet.loadShape(adjustFilePath(theFileName));
	}

	/**
	 * overwritten from PApplet to adjust the file path.
	 * 
	 * @param theFileName
	 * @return PFont SketchObject
	 */
	public PFont loadFont(String theFileName) {
		return papplet.loadFont(adjustFilePath(theFileName));
	}

	/**
	 * copied from PApplet, currently saves frames in the master-sketch. TODO
	 * make the savePath optional, relative to the sketchObject's save path.
	 * 
	 * @param theFilename
	 *            void SketchObject
	 */
	public void save(String theFilename) {
		papplet.g.save(papplet.savePath(theFilename));
	}

	/**
	 * copied from PApplet, currently saves frames in the master-sketch. TODO
	 * make the savePath optional, relative to the sketchObject's save path.
	 */
	public void saveFrame() {
		try {
			papplet.g.save(papplet.savePath("screen-" + PApplet.nf(papplet.frameCount, 4) + ".tif"));
		} catch (SecurityException se) {
			System.err.println("Can't use saveFrame() when running in a browser, "
					+ "unless using a signed applet.");
		}
	}

	/**
	 * * copied from PApplet, currently saves frames in the master-sketch. TODO
	 * make the savePath optional, relative to the sketchObject's save path.
	 * 
	 * @param what
	 *            void SketchObject
	 */
	public void saveFrame(String what) {
		try {
			papplet.g.save(papplet.savePath(insertFrame(what)));
		} catch (SecurityException se) {
			System.err.println("Can't use saveFrame() when running in a browser, "
					+ "unless using a signed applet.");
		}
	}

	/**
	 * copied from PApplet since insertFrame is protected.
	 * 
	 * @param what
	 * @return String SketchObject
	 */
	protected String insertFrame(String what) {
		int first = what.indexOf('#');
		int last = what.lastIndexOf('#');

		if ((first != -1) && (last - first > 0)) {
			String prefix = what.substring(0, first);
			int count = last - first + 1;
			String suffix = what.substring(last + 1);
			return prefix + PApplet.nf(papplet.frameCount, count) + suffix;
		}
		return what; // no change
	}

	/**
	 * overwritten from PApplet to adjust the file path. TODO add
	 * createReader(File theFile)
	 * 
	 * @param theFileName
	 * @return BufferedReader SketchObject
	 */
	public BufferedReader createReader(String theFileName) {
		return papplet.createReader(adjustFilePath(theFileName));
	}

	/**
	 * overwritten from PApplet to adjust the file path. TODO add
	 * createWriter(File theFile)
	 * 
	 * @param theFileName
	 * @return PrintWriter SketchObject
	 */
	public PrintWriter createWriter(String theFileName) {
		return papplet.createWriter(adjustFilePath(theFileName));
	}

	/**
	 * overwritten from PApplet to adjust the file path. TODO add
	 * createInput(File theFile)
	 * 
	 * @param theFileName
	 * @return InputStream SketchObject
	 */
	public InputStream createInput(String theFileName) {
		return papplet.createInput(adjustFilePath(theFileName));
	}

	/**
	 * overwritten from PApplet to adjust the file path.
	 * 
	 * @param theFileName
	 * @return InputStream SketchObject
	 */
	public InputStream createInputRaw(String theFileName) {
		return papplet.createInputRaw(adjustFilePath(theFileName));
	}

	/**
	 * overwritten from PApplet to adjust the file path. TODO add loadBytes(File
	 * theFile)
	 * 
	 * @param theFileName
	 * @return byte[] SketchObject
	 */
	public byte[] loadBytes(String theFileName) {
		return papplet.loadBytes(adjustFilePath(theFileName));
	}

	/**
	 * overwritten from PApplet to adjust the file path. TODO add
	 * loadStrings(File theFile)
	 * 
	 * @param theFileName
	 * @return String[] SketchObject
	 */
	public String[] loadStrings(String theFileName) {
		return papplet.loadStrings(adjustFilePath(theFileName));
	}

	/**
	 * overwritten from PApplet to adjust the file path. TODO add
	 * createOutput(File theFile)
	 * 
	 * @param theFileName
	 * @return OutputStream SketchObject
	 */
	public OutputStream createOutput(String theFileName) {
		return papplet.createOutput(adjustFilePath(theFileName));
	}

	// continue with saveStream line 4192

	/**
	 * adjust the file path for loading procedures absoluted to the
	 * SketchObject's source file/folder.
	 * 
	 * @param theFileName
	 * @return String SketchObject
	 */
	private String adjustFilePath(final String theFileName) {
		try {
			if (new File(theFileName).isAbsolute())
				return theFileName;
		} catch (Exception e) {

		}
		String myPath = "";
		if (theFileName.toLowerCase().startsWith("http://")
				|| theFileName.toLowerCase().startsWith("https://")
				|| theFileName.toLowerCase().startsWith("file://")) {
			myPath = theFileName;
		} else {
			File file = new File(properties.sourceFolder + File.separator + "data"
					+ File.separator + theFileName);
			if (!file.exists()) {
				// next see if it's just in the sketch folder
				file = new File(properties.sourceFolder, theFileName);
			}
			myPath = file.getAbsolutePath();
		}
		SLoader.debug("creating absolutePath of " + theFileName + " / " + myPath);
		return myPath;
	}

	// saveBytes
	// saveStream
	// saveStrings

}
