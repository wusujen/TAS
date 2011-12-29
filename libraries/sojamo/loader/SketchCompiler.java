package sojamo.loader;

import org.codehaus.janino.CompileException;
import org.codehaus.janino.SimpleCompiler;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;
import processing.core.*;
import processing.opengl.PGraphicsOpenGL;

public class SketchCompiler {

	SLoader _mySLoader;

	public SketchCompiler(SLoader theSLoader) {
		_mySLoader = theSLoader;
	}

	protected SketchObject compile(PApplet theApplet, String theCode,
			SketchProperties theProperties) {
		SimpleCompiler c = new SimpleCompiler();
		SketchObject mySketch = null;
		try {
			c.cook(theCode);
			ClassLoader cl = c.getClassLoader();
			Class myclass = cl.loadClass(theProperties.className);
			mySketch = (SketchObject) myclass.newInstance();
			
			mySketch.loader = _mySLoader;
			mySketch.papplet = theApplet;
			mySketch.graphics = theApplet.g;
			mySketch.properties = theProperties;
			mySketch.setAutoDraw(theProperties.isAutoDraw);
			theProperties.isLoaded = true;
			if ((mySketch.graphics instanceof PGraphics2D)
					|| (mySketch.graphics instanceof PGraphicsJava2D)) {
				mySketch.is3D = false;
			} else {
				mySketch.is3D = true;
			}

			SLoader.debug("P3D:" + (mySketch.graphics instanceof PGraphics3D) + " P2D:"
					+ (mySketch.graphics instanceof PGraphics2D) + " OPENGL:"
					+ (mySketch.graphics instanceof PGraphicsOpenGL) + " JAVA2D:"
					+ (mySketch.graphics instanceof PGraphicsJava2D) + " PGraphics:"
					+ (mySketch.graphics instanceof PGraphicsOpenGL));

			mySketch.init();
			return mySketch;
			
		} catch (CompileException e) {
			// TODO Auto-generated catch block
			if (e.getMessage().indexOf("could not be") > -1) {
				System.out.println("[ERROR] sLoader: compiling '" + theProperties.className
						+ "' failed");
				System.out.println("please import the following libraries into your sketch\n");
				for (String element : theProperties.libraries) {
					System.out.println(element);
				}
				System.out.println("");
			} else {
				 System.err.println(e);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
		} catch (ScanException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
		}
		theProperties.isLoaded = false;
		return null;
	}
}
