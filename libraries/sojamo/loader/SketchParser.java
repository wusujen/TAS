package sojamo.loader;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import processing.core.PApplet;

public class SketchParser {
	// regex essentials
	// http://java.sun.com/docs/books/tutorial/essential/regex/char_classes.html
	SLoader _mySLoader;

	/**
	 * 
	 * @param theSLoader
	 */
	SketchParser(SLoader theSLoader) {
		_mySLoader = theSLoader;
	}

	/**
	 * 
	 * @param theProps
	 * @return String SketchParser
	 */
	String parse(SketchProperties theProps) {
		
		// TODO check the pde folder for .pde files. if more than 1, join files together.
		checkSketchFolder(theProps);
		String[] myCodeArray = _mySLoader.papplet.loadStrings(theProps.sourceFolder+theProps.sourceFile);
		String codeParsed = "";

		// myImports store the imports found in a sketch
		ArrayList<String> myImports = new ArrayList<String>();

		// myClasses store the classes found in a sketch
		ArrayList<String> myClasses = new ArrayList<String>();

		// mySetup store the setup method found in a sketch
		ArrayList<String> mySetup = new ArrayList<String>(1);
		ArrayList<String> myDraw = new ArrayList<String>(1);
		ArrayList<String> myFields = new ArrayList<String>();
		ArrayList<String> myConstructor = new ArrayList<String>();

		// format int() and float() from the PApplet code.
		for (int i = 0; i < myCodeArray.length; i++) {
			if (checkPattern("[^a-zA-Z](int)[(]", myCodeArray[i])) {
				myCodeArray[i] = myCodeArray[i].replaceAll("int[(]", "(int)(");
			}

			if (checkPattern("([^a-zA-Z]|\\b)(float)[(]", myCodeArray[i])) {
				myCodeArray[i] = myCodeArray[i].replaceAll("float[(]", "(float)(");
			}
			
		}

		// convert theCodeArray to a string to execute regex operations
		String myCodeString = arrayToString(myCodeArray, "\n");

		myCodeString = PApplet.trim(myCodeString);
		myCodeString = extractBlocks("void setup", mySetup, myCodeString, 1);
		myCodeString = extractBlocks("void draw", myDraw, myCodeString, 1);
		myCodeString = extractImports(myImports, myCodeString);
		myCodeString = extractBlocks("class", myClasses, myCodeString, -1);
		
		theProps.libraries = (ArrayList<String>) myImports.clone();
		
		// convert myCodeString back to an array to parse
		// methods and fields of the sketch.
		myCodeArray = PApplet.split(myCodeString, "\n");

		// parse the methods and fields found in main PApplet.
		// methods and fields of internal classes are parsed later.
		codeParsed = parseMethodAndFieldCalls(myCodeArray);

		parseSetup(mySetup, myConstructor, myFields);
		mySetup.set(0, parseMethodAndFieldCalls(PApplet.split(mySetup.get(0), "\n")));
		
		parseDraw(myDraw);
		myDraw.set(0, parseMethodAndFieldCalls(PApplet.split(myDraw.get(0), "\n")));
		
		String myImportsString = "";
		for (String element : myImports) {
			myImportsString += element + "\n";
		}

		String myClassesString = "";
		for (String element : myClasses) {
			element = parseMethodAndFieldCalls(PApplet.split(element, "\n"));
			element = handleSpecialCases(element);
			myClassesString += element;
		}

		String myFieldsString = "";
		for (String element : myFields) {
			myFieldsString += element + "\n";
		}

		String myConstructorString = "";
		for (String element : myConstructor) {
			myConstructorString += element + "\n";
		}
		codeParsed = "import processing.core.*;\n" + myImportsString + "import sojamo.loader.*;\n"
				+ "public class " + theProps.className + " extends SketchObject "
				+ handleInterfaces() + "{\n" + myFieldsString + "\n" + "public "
				+ theProps.className + "() {\n" + "super();\n" + myConstructorString + "}\n"
				+ mySetup.get(0) + "\n" + myDraw.get(0) + "\n" + codeParsed + "\n";

		codeParsed = handleSpecialCases(codeParsed);

		// add extracted classes to the code
		codeParsed += myClassesString;

		// close the SketchObject class.
		codeParsed += "\n}";

		if (SLoader.DEBUG) {
			System.out.println(codeParsed);
		}
		return codeParsed;
	}
	
	private void checkSketchFolder(SketchProperties theProps) {
		
		String dash = "/";
		// String dash = System.getProperty("file.separator");
		if(theProps.sourcePath.endsWith(".pde")) {
		  int n = theProps.sourcePath.lastIndexOf(dash) + 1;
		  theProps.sourceFolder = theProps.sourcePath.substring(0,n); 
		  theProps.sourceFile = theProps.sourcePath.substring(n);
		} else {
			theProps.sourceFolder = theProps.sourcePath;
			theProps.sourceFolder = (theProps.sourceFolder.endsWith(dash)) ? theProps.sourceFolder:theProps.sourceFolder+dash;
		}
	}
	
	/**
	 * borrowed from
	 * http://leepoint.net/notes-java/data/strings/96string_examples/example_arrayToString.html
	 * 
	 * @param theArray
	 * @param theDelimiter
	 * @return String SketchParser
	 */
	public static String arrayToString(String[] theArray, String theDelimiter) {
		StringBuffer result = new StringBuffer();
		if (theArray.length > 0) {
			result.append(theArray[0]);
			for (int i = 1; i < theArray.length; i++) {
				result.append(theDelimiter);
				result.append(theArray[i]);
			}
		}
		return result.toString();
	}

	/**
	 * 
	 * @param theCode
	 * @return String SketchParser
	 */
	private String handleSpecialCases(String theCode) {
		theCode = theCode.replaceAll("void papplet.mouse", "void mouse");
		theCode = theCode.replaceAll("void papplet.key", "void key");

		if (!checkPattern("(public void setup)[(]", theCode)) {
			theCode = theCode.replaceAll("void setup", "public void setup");
		}

		if (!checkPattern("(public void draw)[(]", theCode)) {
			theCode = theCode.replaceAll("void draw", "public void draw");
		}

		return theCode;
	}

	/**
	 * 
	 * @return String SketchParser
	 */
	private String handleInterfaces() {
		return "";
	}

	/**
	 * 
	 * @param theImports
	 * @param theCode
	 * @return String SketchParser
	 */
	private String extractImports(ArrayList<String> theImports, String theCode) {
		Pattern pattern = Pattern.compile("(\\bimport ).*[;]");
		Matcher matcher = pattern.matcher(theCode);
		String myCode = "" + theCode;
		while (matcher.find()) {
			theImports.add(theCode.substring(matcher.start(), matcher.end()));
			myCode = myCode.replace(theCode.substring(matcher.start(), matcher.end()), "");
		}
		return myCode;
	}

	/**
	 * 
	 * @param thePattern
	 * @param theBlocks
	 * @param theCode
	 * @param theNum
	 * @return String SketchParser
	 */
	private String extractBlocks(String thePattern, ArrayList<String> theBlocks, String theCode,
			int theNum) {
		Pattern pattern = Pattern.compile("(" + thePattern + ")|[{]|[}]");
		Matcher matcher = pattern.matcher(theCode);
		boolean found = false;
		boolean isBlock = false;
		int countBrackets = 0;
		int classStart = -1;
		int classEnd = -1;
		String myClass = "";
		String myCode = "" + theCode;
		int n = 0;
		while (matcher.find()) {
			if (isBlock) {
				if (matcher.group().equals("{")) {
					countBrackets++;
				} else if (matcher.group().equals("}")) {
					countBrackets--;
				}

				if (countBrackets == 0) {
					classEnd = matcher.end();
					theBlocks.add(theCode.substring(classStart, classEnd));
					myCode = myCode.replace(theCode.substring(classStart, classEnd), "");
					isBlock = false;
					n++;
					if (theNum != -1 && n == theNum) {
						break;
					}
				}
			}
			if (matcher.group().equals(thePattern)) {
				isBlock = true;
				countBrackets = 0;
				classStart = matcher.start();
			}
			found = true;
		}
		if (!found) {
			System.out.println("No match found.");
		}
		return myCode;
	}

	/**
	 * 
	 * @param thePattern
	 * @param theCode
	 * @return boolean SketchParser
	 */
	public boolean checkPattern(String thePattern, String theCode) {
		Pattern myPattern = Pattern.compile(thePattern);
		Matcher myMatcher = myPattern.matcher(theCode);

		boolean isFound = false;
		while (myMatcher.find()) {
			isFound = true;
		}
		return isFound;
	}

	private String parseMethodAndFieldCalls(String[] theCodeArray) {
		String[] s2 = new String[0];
		String myLine = "";
		String myCodeParsed = "";
		// start parsing methods and fields
		for (String element : theCodeArray) {
			if (element.indexOf("==") > -1) {
				s2 = PApplet.split(element, "==");
				for (int j = 0; j < s2.length - 1; j++) {
					myLine = extractMethodCalls(PApplet.trim(s2[j]));
					myLine = extractFieldCalls(myLine);
					myCodeParsed += myLine;
					myCodeParsed += "==";
				}

				myLine = extractMethodCalls(PApplet.trim(s2[s2.length - 1]));
				myLine = extractFieldCalls(myLine);
				myCodeParsed += myLine;

			} else if (element.indexOf("=") > -1) {
				s2 = PApplet.split(element, "=");
				for (int j = 0; j < s2.length - 1; j++) {
					myLine = extractMethodCalls(PApplet.trim(s2[j]));
					myLine = extractFieldCalls(myLine);
					myCodeParsed += myLine;
					myCodeParsed += "=";
				}

				myLine = extractMethodCalls(PApplet.trim(s2[s2.length - 1]));
				myLine = extractFieldCalls(myLine);
				myCodeParsed += myLine;

			} else {
				myLine = extractMethodCalls(PApplet.trim(element));
				myLine = extractFieldCalls(myLine);
				myCodeParsed += myLine;
			}
			myCodeParsed += "\n";
		}
		return myCodeParsed;
	}

	/**
	 * 
	 * @param theString
	 * @return String SLoader
	 */
	private String extractFieldCalls(String theString) {
		StringBuilder result = new StringBuilder(theString.length());

		String delimiters = " ([,/-+=<>*"; // pre
		delimiters += ")];"; // post
		StringTokenizer st = new StringTokenizer(theString, delimiters, true);
		while (st.hasMoreTokens()) {
			String w = st.nextToken();
			boolean needsToAppend = true;
			for (int i = 0; i < _mySLoader.pappletFields.size(); i++) {
				String find = _mySLoader.pappletFields.get(i);
				String replacement = "papplet." + _mySLoader.pappletFields.get(i);
				if (w.equals(find)) {
					result.append(replacement);
					needsToAppend = false;
				}
			}
			if (needsToAppend) {
				result.append(w);
			}
		}
		return result.toString();
	}

	/**
	 * 
	 * 
	 * @param theString
	 * @return String SLoader
	 */
	private String extractMethodCalls(String theString) {
		String myString = "";
		StringTokenizer st = new StringTokenizer(theString, "(", true);
		if(st.countTokens()>1) {
		while (st.hasMoreTokens()) {
			String w = st.nextToken();
			if(_mySLoader.graphicsMethods.contains(w)) {
				w = "graphics." + w;
			} else if (_mySLoader.pappletMethods.contains(w)) {
				w = "papplet." + w;
			}
			myString += w;
		}
		} else {
			myString = theString;
		}
		
		return myString;
	}

	private void parseDraw(ArrayList<String> theCode) {
		String myString = theCode.get(0);
		// Compile regular expression
		if (myString.indexOf("preDraw()") < 0) {
			Pattern pattern = Pattern.compile("(draw)[(].*[{]");

			// Replace all occurrences of pattern in input
			Matcher matcher = pattern.matcher(myString);
			myString = matcher.replaceAll("draw() {\npreDraw();\nif(!graphicsIsPApplet) {\ngraphics.beginDraw();\n}");
			myString = myString.substring(0, myString.length() - 1);
		}
		if (myString.indexOf("postDraw()") < 0) {
			myString += "if(!graphicsIsPApplet) {\ngraphics.endDraw();\n}\npostDraw();\n}";
		}
		theCode.set(0, myString);
	}

	/**
	 * 
	 * @param theCode
	 *            void SketchParser
	 */
	private void parseSetup(ArrayList<String> theCode, ArrayList<String> theConstructor,
			ArrayList<String> theFields) {
		Pattern pattern = Pattern.compile("(size)[(].*[;]");
		Matcher matcher = pattern.matcher(theCode.get(0));
		String mySetup = "";
		while (matcher.find()) {
			String[] mySizeParams = PApplet.split(theCode.get(0).substring(matcher.start() + 5,
					matcher.end() - 2), ",");
			theConstructor.add("width = " + mySizeParams[0] + ";");
			theConstructor.add("height = " + mySizeParams[1] + ";");
			mySetup = theCode.get(0).substring(0, matcher.start() - 1)
					+ theCode.get(0).substring(matcher.end() + 1, theCode.get(0).length());
		}
		theCode.set(0, mySetup);
	}
	
	/**
	 * TODO parse processing methods that load files from disk.
	 * TODO loadImage, loadShape, loadBytes, loadStrings, loadImage, loadFont
	 * TODO createReader, createWriter, requestImage
	 * TODO save, saveBytes, saveFrame, saveStream, saveStrings
	 * TODO check if path starts with / which would be an absolute path
	 * TODO check if the file is inside the data folder or on the same level as 
	 * the .pde file.
	 *  void SketchParser
	 */
	private void adjustPath() {
		
	}

	/**
	 * 
	 * @param theCode
	 * @return String[] SketchParser
	 */
	private String[] removeComments(String[] theCode) {
		// currently only removes comments made at the
		// beginning of a line.
		ArrayList<String> myStrings = new ArrayList<String>();
		for (String element : theCode) {
			String myString = PApplet.trim(element);
			if (myString.length() > 1) {
				if (!myString.substring(0, 2).equals("//")) {
					myStrings.add(myString);
				}
			} else {
				myStrings.add(myString);
			}
		}
		String[] myArray = new String[myStrings.size()];
		myStrings.toArray(myArray);
		return myArray;
	}
}
