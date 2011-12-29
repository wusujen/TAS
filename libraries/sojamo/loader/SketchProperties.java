package sojamo.loader;

import java.io.File;
import java.util.ArrayList;

import processing.core.PGraphics;

public class SketchProperties {

	String[] codeArray;

	PGraphics graphics;

	String className;
	
	boolean isAutoDraw;
	
	String sourcePath = "";
	
	String sourceFile = "";
	
	String sourceFolder = "";
	
	String sourceCode = "";
	
	boolean isLoaded;
	
	String fileSeperator;
	ArrayList<String> libraries;

	public SketchProperties(String theClassName) {
		className = theClassName;
		isAutoDraw = true;
	}
	
	/**
	 * 
	 * @param theSourcePath void SketchProperties
	 */
	public void setSourcePath(String theSourcePath) {
		if(theSourcePath.indexOf(".pde")>-1) {
			String c = "";
			int n = theSourcePath.length();
			while(!c.equals(File.separator) && n>0) {
			  n--;
			  c = "" + theSourcePath.charAt(n);
			}
			sourceFolder = theSourcePath.substring(0,n+1);
			sourceFile = theSourcePath.substring(n+1);
		} else {
			sourceFolder = theSourcePath;
		}
		sourcePath = theSourcePath;
	}
	
	/**
	 * 
	 * @param theGraphics void SketchProperties
	 */
	public void setGraphics(PGraphics theGraphics) {
		graphics = theGraphics;
	}
	
	/**
	 * 
	 * @param theFlag void SketchProperties
	 */
	public void setAutoDraw(boolean theFlag) {
		isAutoDraw = theFlag;
	}
	
	public boolean isLoaded() {
		return isLoaded;
	}
	
}
