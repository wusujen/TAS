import processing.core.*; 
import processing.xml.*; 

import controlP5.*; 
import proxml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class interface_controls extends PApplet {




int appWidth = 1200;            // processing applet measurements
int appHeight = 800;            

int canvasX = 280;              // drop canvas measurements
int canvasY = 100;
int canvasWidth = 600;
int canvasHeight = 600;

int propertyPanelX=canvasX+canvasWidth+25;
int propertyPanelY=canvasY;

int libX = 50;                  // Media Library dimensions
int libY = 50;              
int libW = 200;
int libH = 300;

ControlP5 controlP5;
ListBox mediaList;
//Button saveBtn;

ArrayList sceneArray;          // holds an array of scenes--looped through when saving to XML
String activeScene;           // stores the name of the activeScene;

ArrayList sceneElementArray;
String activeSceneElement;     //stores the name of activeSceneElement
SceneElement activeElement;    //stores the actual activeSceneElement

ArrayList allFiles;            // global array of files loaded from media folder...
ArrayList imageFiles;          // ...broken down by filetype
ArrayList audioFiles;
ArrayList movieFiles;
ArrayList otherFiles;

CColor defaultColor;
DropCanvas canvas;

proxml.XMLElement media;        // xml element to store and load the media
XMLInOut xmlIO;                

String[] itemNames;             // stores names of items from media folder for later use
File[] itemInfo;                // stores File information of items from media folder
int dropListID=0;               // stores how many items have been added to dropCanvas
int dragListID=0;               // stores how many items have been added to drag list
boolean mouseDragging=false;    // check to see if mouse is dragging
boolean clickOnController;      // check to see if the mouse is over the controller

String clickToDelete;           //stores the name of the object to be removed from the scene  
String clickedItemName;         // stores the name of the item clicked from mediaList
int val;                        // stores the id of the item clicked from mediaList

boolean doneLoading=false;      //is set to true when XML is done loading


public void setup() {
  // path to media folder
  String path = sketchPath + "/data";
  // draw the applet
  size(appWidth, appHeight);

  // declare a global instance of ControlP5;
  controlP5 = new ControlP5(this);
  
   //initialize sceneElementArray
  sceneElementArray=new ArrayList();
  
  // initialize arrays to hold filenames from media folder
  // these will populate the MediaLists created with the 
  // MediaLibrary class, and the values of allFiles will be checked against the 
  // xml file loaded in to see if any node should be removed.
  allFiles = new ArrayList();
  imageFiles = new ArrayList();
  audioFiles = new ArrayList();
  movieFiles = new ArrayList();
  otherFiles = new ArrayList();
  //check all files types in media folder
  // and save them to separate global arrays
  checkFileTypes();
  // create the initial media list from media folder
  MediaLibrary imageLib = new MediaLibrary(imageFiles, "Images", 1);
  MediaLibrary audioLib = new MediaLibrary(audioFiles, "Audio", 2);
  MediaLibrary movieLib = new MediaLibrary(movieFiles, "Movies", 3);


  // initialize array to hold scene names
  sceneArray = new ArrayList();
  sceneArray.add("scene1");
  sceneArray.add("scene2");
  sceneArray.add("scene3");
  sceneArray.add("scene4");
  
  activeScene=(String) sceneArray.get(0);
  controlP5.tab("default").setLabel(activeScene);
  controlP5.tab("default").activateEvent(true);
  for(int i=1; i<sceneArray.size(); i++){
    Tab myTab=controlP5.tab((String) sceneArray.get(i));
    myTab.activateEvent(true);
  }  
    // load save Button, which will trigger writing to XML
  Button saveBtn=controlP5.addButton("save", 1 , appWidth - 100, appHeight - 50, 50, 20);
  saveBtn.activateBy(ControlP5.PRESSED);

  // TODO: preload image files from imageLib;

  itemNames=new String[allFiles.size()];
  for (int i=0;i<imageFiles.size();i++) {
    itemNames[i]=(String) imageFiles.get(i);
  }
  for (int i=0;i<audioFiles.size();i++) {
    itemNames[imageFiles.size()+i]=(String) audioFiles.get(i);
  }
  for (int i=0;i<movieFiles.size();i++) {
    itemNames[imageFiles.size()+audioFiles.size()+i]=(String) movieFiles.get(i);
  }

  //initialize dropCanvas
  canvas=new DropCanvas(255, 180, canvasX, canvasY, canvasWidth, canvasHeight);


  // load XML file
  loadXMLFile();

  //loads controlp5 parts of propertyPanel
  setupPropertyPanel();

}

public void draw() {
  background(200);
  
  // draws the canvas
  canvas.drawDropCanvas();
  if (doneLoading) {
    drawSceneElements();
  }

  // if an element has been selected, then change
  // the visual properties of that element
  if (activeElement!=null) {
    activeElement.hasBeenSelected();
    drawPropertyPanel();
  }

}
/*==============  checkFileTypes =============*
 On setup();
 Store all files from the media folder in 
a global array by type, to access later.
*=========================================*/
public void checkFileTypes() {
  String path = sketchPath + "/data"; // path to media folder
  // store the files to loop in a local array
  String[] filesToLoop = listFileNames(path);
 
  // loop through all files and list only the name
  for(int i =0; i<filesToLoop.length; i++) {
      
    // store the filename in a local var
    String mediaFileName = filesToLoop[i];
    // add all media to global var
    allFiles.add(mediaFileName);

    // check for acceptable image formats
    if( (mediaFileName.endsWith("png")) || (mediaFileName.endsWith("jpg")) || (mediaFileName.endsWith("jpeg")) || mediaFileName.endsWith("gif") ){ 
      imageFiles.add(mediaFileName);
    }
    // check for accetable audio formats
    else if( (mediaFileName.endsWith("aif")) || (mediaFileName.endsWith("mp3")) ){  
      audioFiles.add(mediaFileName);
    }
    // check for acceptable movie formats
    else if( (mediaFileName.endsWith("mov")) || (mediaFileName.endsWith("mp4")) ){  
      movieFiles.add(mediaFileName);
    }
    // finally check for all other formats
    else {  
      otherFiles.add(mediaFileName);
    }

  }
  
}


/*============== listFileNames =============*
 Returns all the files in a directory as 
 an array of Strings
*=========================================*/
public String[] listFileNames(String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    String names[] = file.list();
    return names;
  } else {
    // If it's not a directory
    return null;
  }
}


/*============== saveProgram =============*
 calls the function to save the information 
 in SceneElementArray to the XML file.
*=========================================*/
public void save(int theValue) { // what is going on with this button.
  println("button pushed to save the sketch");
  saveXML();
}
class MediaLibrary {
  ArrayList mediaArray;
  String title;
  int id;
  int x;
  int y;
  int w;
  int h;

  MediaLibrary(ArrayList objArray, String objTitle, int objId) {
    mediaArray=objArray;
    title=objTitle;
    id=objId;
    
    drawLibrary();
    
  }
  
  public void drawLibrary(){
    mediaList = controlP5.addListBox(title, libX, libY , libW, libH);
    mediaList.setId(id);
    mediaList.setItemHeight(15);
    mediaList.setBarHeight(15);
    mediaList.captionLabel().toUpperCase(true);
    mediaList.captionLabel().set(title);
    mediaList.moveTo("global");
    
    mediaList.captionLabel().style().marginTop = 3;
    mediaList.valueLabel().style().marginTop = 3; // the +/- sign
    println("media array id: " + id + " size: "+ mediaArray.size());
    
    for(int i=0;i<mediaArray.size();i++) {
      String raisin =(String) mediaArray.get(i);
      mediaList.addItem(raisin,i);
    }
    
    // save the height of the list as a global var
    // to ensure proper vertical spacing for the lists
    mediaList.getBackgroundHeight();
    libY += mediaList.getBackgroundHeight() + 30;
  }

}
public void drawLibrary(ArrayList media, String title,  int id, int libY){
  controlP5 = new ControlP5(this);
  mediaList = controlP5.addListBox("mediaList", libX , libY , libW, libH);
  mediaList.setId(id);
  mediaList.setItemHeight(15);
  mediaList.setBarHeight(15);
  mediaList.captionLabel().toUpperCase(true);
  mediaList.captionLabel().set(title);
  
  mediaList.captionLabel().style().marginTop = 3;
  mediaList.valueLabel().style().marginTop = 3; // the +/- sign
  
  for(int i=0;i<media.size();i++) {
    String raisin =(String) media.get(i);
    mediaList.addItem(raisin,i);
  }
}


/*==============  dragCursor =============*
 this is the cursor that follows the mouse
 when the item is clicked. Ideally, it would
 capture the name of the clicked item...but
 because of the way the control events 
 function, that is currently not possible.
*=========================================*/
public void dragCursor(int x, int y, int w, int h){
  //fill(255);
  //textSize(9);
  //text(name,x+2,y+12);
  fill(150);
  noStroke();
  rect(x,y,w,h);
}


/*======  controlEvent(mediaList)  =======*
 this controlEvent applies to mediaList only.
 if an item is clicked in mediaList, then the
 item will be removed from mediaList.
*=========================================*/
public void controlEvent(ControlEvent theEvent) {
  if(theEvent.isTab()){
    String tabName=theEvent.tab().name();
    if(tabName=="default"){
      tabName=(String) sceneArray.get(0);
    }
    if(activeScene!=tabName){
      activeScene=tabName;
      println("changed to:" + activeScene);
      activeElement=null;
      resetPropertyPanel();
    }
    return;
  }
  // checks to see if the group id matches the list we intend for it to.
  if (theEvent.isGroup() && ((theEvent.group().id()==1)||(theEvent.group().id()==2)||(theEvent.group().id()==3))) {
    // check the value of the group
    println(theEvent.group().value()+" from "+theEvent.group());
    
    // set the val for future use as an item id &
    // retrieve the respective item name from the itemName array
    val=PApplet.parseInt(theEvent.group().value());
    clickedItemName=itemNames[val];
    
    // up the dragList by 1, to keep track of what 
    // has been dragged & add an Item to the dragToList
    dragListID=dragListID+1;
    //dragToList.addItem(clickedItemName,dragListID);
    return;
  }  
  //println(theEvent.controller().name());
  if(theEvent.isController()){
    Controller controller=theEvent.controller();
    for(int i=0; i<9; i++){
      if(theEvent.controller().name().equals("bang"+i)){
        println("clicked bang"+i);
        ArrayList tList=activeElement.getMyTriggerList();
        if(tList.contains(i)){
          println("removed bang"+i);
          tList.remove(tList.indexOf(i));
          controller.setColorForeground(color(0));
        }
        else{
          println("added bang"+i);
          tList.add(i);
          controller.setColorForeground(color(255,0,0));
        }
        return;
      }
    }
  }
}

// Set to true if mouse is dragging
public void mouseDragged(){
  mouseDragging=true;
  if(activeElement!=null && activeElement.bActivate){
    activeElement.xPos = mouseX - activeElement.mStartX;
    activeElement.yPos = mouseY - activeElement.mStartY;   
    
    //keep drag inside the bound
    if (activeElement.xPos < canvas.xPos){
      activeElement.xPos = canvas.xPos;
    }
    else if(activeElement.xPos+PApplet.parseInt(activeElement.w) > canvas.xPos+canvas.w){
      activeElement.xPos = canvas.xPos + canvas.w - PApplet.parseInt(activeElement.w);
    }
    if (activeElement.yPos < canvas.yPos){
      activeElement.yPos = canvas.yPos;
    }
    else if(activeElement.yPos+PApplet.parseInt(activeElement.h) > canvas.yPos+canvas.h){
      activeElement.yPos = canvas.yPos + canvas.h - PApplet.parseInt(activeElement.h);
    }
    
    //include the closeButton with its sceneElement
    activeElement.c.xPos = activeElement.xPos + PApplet.parseInt(activeElement.w);
    activeElement.c.yPos = activeElement.yPos;
  }
  //print("mPos = (" + activeElement.xPos + "," + activeElement.yPos + ")\n");
}

// If mouse is released within the bounds of the dropCanvas
// then set the position of the item to the location of the
// mouse
public void mouseReleased(){
  canvas.detectDroppedItem();
  //select the activeElement to move 
   if(activeElement!=null){
   activeElement.bActivate = false;
   activeElement.c.pressedState = false;
   if (activeElement.isHoveringOverCloseButton(mouseX,mouseY)){
     //print ("remove the obj\n");
     canvas.removeDroppedItem();
   }
   
   activeElement.mEndX = mouseX;
   activeElement.mEndY = mouseY;
    //print("mEnd = (" + activeElement.mEndX + "," + activeElement.mEndY + ")\n");
   populatePropertyPanel();
  }
}

public void mousePressed(){
  if (activeElement!=null){
    if(activeElement.isMouseOver(mouseX, mouseY)){
      activeElement.bActivate = true;   
      //if click on the close button
      if (activeElement.isHoveringOverCloseButton(mouseX,mouseY)){
        activeElement.c.overState = false;
        activeElement.c.pressedState = true;
        clickToDelete = activeElement.objName();
        println("activeElement.objName() ==> " + activeElement.objName()); 
      }
      activeElement.mStartX = mouseX - activeElement.xPos;
      activeElement.mStartY = mouseY - activeElement.yPos;
      //print("mStart = (" + activeElement.mStartX + "," + activeElement.mStartY + ")\n");
    }
  }
}

public void mouseMoved(){
  if (activeElement!=null){
    if (activeElement.isHoveringOverCloseButton(mouseX,mouseY)){
      activeElement.c.overState = true;
    }else
      activeElement.c.overState = false;
  }
}

public void mouseClicked(){
  // check if SceneElement has been selected, as long
  // as the mouse is within the boundaries of the canvas
  if(canvas.mouseIsWithinDropCanvas()){
    selectSceneElement();
  }
}

/*==============  dropCanvas =============*
 the canvas that will hold all of the media
 files that the user drops onto it.
 *=========================================*/
class DropCanvas {
  int cs;
  int cf;
  int xPos;
  int yPos;
  int w;
  int h;

  String firstClicked;            // stores the name of the first item that was clicked
  String labelName;               // stores the name of the last created label
  int fileNumber;                 // keeps track of what files a loop has gone through
  int numberOfDroppedFiles=0;     // stores the total number of files in dropCanvas 

  DropCanvas(int dropCanvasStroke, int dropCanvasFill, int dropCanvasXPos, int dropCanvasYPos, int dropCanvasWidth, int dropCanvasHeight) {
    cs=dropCanvasStroke;
    cf=dropCanvasFill;
    xPos=dropCanvasXPos;
    yPos=dropCanvasYPos;
    w=dropCanvasWidth;
    h=dropCanvasHeight;
  }

  public void drawDropCanvas() {
    stroke(cs);
    fill(cf);
    rect(xPos, yPos, w, h);
  }
  
  /*====== mouseIsWithinDropCanvas  ======*
  // this returns true if mouse is within
  // the bounds of dropCanvas
  ========================================*/
  public boolean mouseIsWithinDropCanvas(){
    if (mouseX>xPos && mouseX<xPos+w && mouseY>yPos && mouseY<yPos+h){
      return true;
    }
    return false;
  }

  /*=============   detectDroppedItem   ===============*
   // this function detects whether or not the item has
   // been dropped into the area of the dropCanvas, and if
   // it has, create a sceneElement and put it into the 
   // sceneElementArray
   ====================================================*/
  public void detectDroppedItem() {
    if (mouseX>xPos && mouseX<xPos+w && mouseY>yPos && mouseY<yPos+h && clickedItemName!=null) {
      // create a new label name for each label
      labelName="label"+val;

      // TODO: remove hardcoded variables for obj height, width, scene number, transition.

      //sceneElementArray.add(new SceneElement(clickedItemName, 0, 120, 15, mouseX, mouseY, 1, "none"));
      //sceneElementArray.add(new SceneElement(clickedItemName, 120, 15, mouseX, mouseY, 1, new ArrayList()));

      PImage myPImage=loadImage(clickedItemName);
      println(myPImage.width +" , "+ myPImage.height);
      float futureWidth;
      float futureHeight;
      float cWidth=PApplet.parseFloat(myPImage.width);
      float cHeight=PApplet.parseFloat(myPImage.height);
      if (cWidth>cHeight){
        futureWidth=300;
        futureHeight=300*(cHeight/cWidth);
        println(futureHeight);
      }
      else{
        futureHeight=300;
        futureWidth=300*(cWidth/cHeight);
        println(futureWidth);
      }
      //sceneElement now automatically sets to the currentScene
      sceneElementArray.add(new SceneElement(clickedItemName, futureWidth, futureHeight, mouseX, mouseY, activeScene, new ArrayList()));

      SceneElement newSceneElement=(SceneElement) sceneElementArray.get(numberOfDroppedFiles);
      //newSceneElement.drawSceneElement();
      numberOfDroppedFiles=sceneElementArray.size();
      
    }
    mouseDragging=false;
    clickedItemName=null;
    val=0;
  }
  
  
/*========   removeDroppedItem   =========*
 removes a fileObject that has been put into
 dropCanvas and changes the color of the
 listBoxItem
*=========================================*/
  public void removeDroppedItem(){
   // loop through the SceneElement array and check to see if the
   // name of the currently clicked object matches any of those names
   // if it does, then remove it and reset the listBoxItem color
   for(int i=0; i<sceneElementArray.size(); i++){
     SceneElement file = (SceneElement) sceneElementArray.get(i);
     String droppedObjectName = file.objName();
     //println("droppedObjectName ==> "+ droppedObjectName);
     //println("clickToDelete ==> " + clickToDelete);
     if(droppedObjectName==clickToDelete){
       controlP5.remove(clickToDelete);
       sceneElementArray.remove(i);
       numberOfDroppedFiles = sceneElementArray.size();
       break;
      }
   }
  }
/*========End of removeDroppedItem==============================*/


  /*========   getsceneElementById   =========*
   returns a sceneElement when given an id.
   *=========================================*/
  public SceneElement getSceneElementById(int id) {
    SceneElement file=(SceneElement) sceneElementArray.get(id);
    return file;
  }


  /*=======   getsceneElementByName   =========*
   returns a sceneElement when given a name.
   currently does not perform a check for 
   null.
   *=========================================*/
  public SceneElement getSceneElementByName(String name) {
    SceneElement sampleFile;
    for (int i=0; i<sceneElementArray.size(); i++) {
      sampleFile=(SceneElement) sceneElementArray.get(i);
      if (name==sampleFile.objName()) {
        fileNumber=i;
      }
    }
    SceneElement file=(SceneElement) sceneElementArray.get(fileNumber);
    return file;
  }
}

ArrayList bangList=new ArrayList();
Textlabel titleLabel;
Textlabel nameLabel;
Textlabel triggerLabel;
Numberbox widthBox;
Numberbox heightBox;
Numberbox xBox;
Numberbox yBox;

int innerMarginX=propertyPanelX+15;

public void setupPropertyPanel(){
  titleLabel=controlP5.addTextlabel("textLabel","PROPERTIES PANEL", innerMarginX, propertyPanelY+15);
  titleLabel.setWidth(100);
  titleLabel.moveTo("global");
  nameLabel=controlP5.addTextlabel("nameLabel","empty",innerMarginX,propertyPanelY+45);
  nameLabel.moveTo("global");
  widthBox=controlP5.addNumberbox("widthBox",0,innerMarginX,propertyPanelY+70,130,18);
  widthBox.setMin(1);
  widthBox.moveTo("global");
  heightBox=controlP5.addNumberbox("heightBox",0,innerMarginX,propertyPanelY+110,130,18);
  heightBox.setMin(1);
  heightBox.moveTo("global");
  xBox=controlP5.addNumberbox("xBox",0,innerMarginX,propertyPanelY+150,130,18);
  xBox.setMin(canvasX);
  xBox.moveTo("global");
  yBox=controlP5.addNumberbox("yBox",0,innerMarginX,propertyPanelY+190,130,18);
  yBox.setMin(canvasY);
  yBox.moveTo("global");
  triggerLabel=controlP5.addTextlabel("triggerLabel","TRIGGERS", innerMarginX, propertyPanelY+250);
  triggerLabel.setWidth(100);
  triggerLabel.moveTo("global");
  for(int i=0; i<9; i++){
    Bang b=controlP5.addBang("bang"+i, innerMarginX+(60*(i%3)), propertyPanelY+275+(60*floor(i/3)), 50, 50);
    bangList.add(b);
    b.moveTo("global");
    b.setLabelVisible(false);
  }
  resetPropertyPanel();
}

public void drawPropertyPanel(){
   stroke(255);
   fill(180);
   rect(propertyPanelX,propertyPanelY,200,600);
}

public void calibrateMaxValues(){
  float xDiff = canvasX+canvasWidth-activeElement.getMyX();
  float xRatio = xDiff/activeElement.getMyWidth();
  
  float yDiff = canvasY+canvasHeight-activeElement.getMyY();
  float yRatio = yDiff/activeElement.getMyHeight();
  if (xRatio < yRatio) {
    widthBox.setMax(xDiff);
    heightBox.setMax(activeElement.getMyHeight()*xRatio);
  } else {
    heightBox.setMax(yDiff);
    widthBox.setMax(activeElement.getMyWidth()*yRatio);
  }
  
  xBox.setMax(canvasX+canvasWidth-activeElement.getMyWidth());
  yBox.setMax(canvasY+canvasHeight-activeElement.getMyHeight());
}

public void populatePropertyPanel(){
  titleLabel.show();
  triggerLabel.show();
  nameLabel.show();
  nameLabel.setValue(activeElement.getMyName());
  widthBox.show();
  widthBox.setValue(activeElement.getMyWidth());
  heightBox.show();
  heightBox.setValue(activeElement.getMyHeight());
  calibrateMaxValues();
  xBox.show();
  xBox.setValue(activeElement.getMyX());
  yBox.show();
  yBox.setValue(activeElement.getMyY());
  ArrayList tList=activeElement.getMyTriggerList();
  for(int i=0; i<9; i++){
    Bang b=(Bang) bangList.get(i);
    b.show();
    b.setColorForeground(color(0,0,0));
  }
  for(int i=0; i<tList.size();i++){
    Bang b=(Bang) bangList.get((Integer) tList.get(i));
    b.setColorForeground(color(255,0,0));
  }
  
}

public void resetPropertyPanel(){
  titleLabel.hide();
  nameLabel.hide();
  triggerLabel.hide();
  widthBox.hide();
  heightBox.hide();
  xBox.hide();
  yBox.hide();
  for(int i=0; i<9; i++){
    Bang b=(Bang) bangList.get(i);
    b.hide();
  }
}

public void widthBox(){
  println("widthboth: " + widthBox.value());
  activeElement.updateWidth(widthBox.value());
      //include the closeButton with its sceneElement
    activeElement.c.xPos = activeElement.xPos + PApplet.parseInt(activeElement.w);
    activeElement.c.yPos = activeElement.yPos;
}
public void heightBox(){

  //if (abs(activeElement.getMyHeight() - heightBox.value()) > 1) {
    activeElement.updateHeight(heightBox.value());
        //include the closeButton with its sceneElement
    activeElement.c.xPos = activeElement.xPos + PApplet.parseInt(activeElement.w);
    activeElement.c.yPos = activeElement.yPos;
  //}
}
public void xBox(){
  activeElement.updateX(PApplet.parseInt(xBox.value()));
}
public void yBox(){
  activeElement.updateY(PApplet.parseInt(yBox.value()));
}

/* 
XML structure
saved to data/mediaoutput.xml
<media>
  <scene number="1">
    <element>
      <file filename="test.jpg" />
      <pos x="420" y="310" />
      <dimensions w="230" h="15" />
      <triggers>
        <trigger number="6" />
        <trigger number="7" />
      </triggers>
    </element>
  </scene>
</media>
*/

/*==============  loadXMLFile =============*
 On setup();
 load media from XML file if it exists
*=========================================*/ 
public void loadXMLFile() {
  xmlIO = new XMLInOut(this);
  try{
    xmlIO.loadElement("mediaoutput.xml"); 
  }catch(Exception e){
    //if the xml file could not be loaded, it has to be created
    xmlEvent(new proxml.XMLElement("media"));
  }
}

/*==============  xmlEvent =============*
 register the xmlEvent if there is no XMLfile
 and pass the main element to initCanvas().
 Any scene reconstruction should be done here.
*=========================================*/ 
public void xmlEvent(proxml.XMLElement element) {
 media = element;
 loadXMLNodes(); 
}

/*==============  loadXMLNodes =============*
 Traverse the XML file and redraw media files 
 as they were placed. Save each element in the XML
 file as a SceneElement in SceneElementArray();
*=========================================*/ 
public void loadXMLNodes() {
  
  //media.printElementTree(" ");
  proxml.XMLElement scene;
  proxml.XMLElement element;
  proxml.XMLElement file;
  proxml.XMLElement pos;
  proxml.XMLElement dimensions;
  proxml.XMLElement triggers;
  proxml.XMLElement trigger;
    
  String filename;
  String number;
  float w;
  float h;
  int x;
  int y;
  int TriggerNum;

  for(int i = 0; i < media.countChildren();i++){ // the number of scenes
    
    scene = media.getChild(i);
    number = scene.getAttribute("number");
    
    for(int j = 0; j < scene.countChildren(); j++) { // the number of elements in each scene
      
      element = scene.getChild(j);
      file = element.getChild(0); 
      filename = file.getAttribute("filename");
      println("filenames loaded " + filename);
      pos = element.getChild(1);
      x = pos.getIntAttribute("x");
      y = pos.getIntAttribute("y");
      dimensions = element.getChild(2);
      w = dimensions.getFloatAttribute("w"); 
      h = dimensions.getFloatAttribute("h");
      triggers = element.getChild(3);
      
      ArrayList myTriggers = new ArrayList();
      for (int p=0; p < triggers.countChildren(); p++) {  
        trigger = triggers.getChild(p);
        myTriggers.add(trigger.getIntAttribute("number"));
      }
      
      // add new SceneElement to the array.
      // TODO: activeScene is INCORRECT. This must be loaded from the XML/sceneElement
      sceneElementArray.add(new SceneElement(filename, w, h, x, y, number, myTriggers));
    }
  }
  doneLoading=true;
  // testing
  for (int i=0; i<sceneElementArray.size(); i++) {
    SceneElement thisElement =(SceneElement) sceneElementArray.get(i);
    println("Scene Element Loaded: " + thisElement.name + " for scene: " + thisElement.scene);
  }
}


/*============== saveXML =============*
 Creates the XML file using the info
 from the SceneElement that's passed to it.
*=========================================*/ 
public void saveXML() {
  println("saveXML called");
  ArrayList elements = sceneElementArray;
    

  boolean xmlDeleted = false;
  if(media.countChildren() > 0) {
    println("there are childre nodes in the XML and they must be removed");
    for(int i = media.countChildren()-1; i > -1; i--) {  //remove each scene
      media.removeChild(i);
      if (i == 0) {
        println("Old XML deleted. All clear to repopulate!");
        xmlDeleted = true;
      }
     }
   } else {
     println("There never were any children!");
     xmlDeleted = true;
   }

 
  if(xmlDeleted) {
  
   println("xml is deleted, work magic now."); 
   println("--------------------");
   // test print the scene array
   for (int i=0;i<sceneElementArray.size();i++) {
    SceneElement element=(SceneElement) sceneElementArray.get(i);
    println("sceneElement:" + element.scene + " " + element.name);
    }
   println("Number of elements in SceneArray " + elements.size());
   println("--------------------");
   
    // Once scenes are dynamic, iterate through sceneArray.size()
    // and construct a new scene element for each item, with a String 
    // attribute for the name. NOT AN INTEGER! (this changes a lot of things).   
    for(int i=0; i<sceneArray.size(); i++){
      proxml.XMLElement scene = new proxml.XMLElement("scene"); 
      String sceneName =(String) sceneArray.get(i);
      scene.addAttribute("number", sceneName); // this is a string
      media.addChild(scene);
      println("<scene> node created: " + sceneName);
    }

   
   for(int i = 0; i<elements.size(); i++) {
      // get the SceneElement info for each item in the Array
     SceneElement thisElement=(SceneElement) elements.get(i);
    
     println("Scene Element name: " + thisElement.name + " in scene: " + thisElement.scene);
    
     //reconstruct the node
     proxml.XMLElement element = new proxml.XMLElement("element");
     
     proxml.XMLElement file = new proxml.XMLElement("file");
     file.addAttribute("filename", thisElement.name);
     proxml.XMLElement pos = new proxml.XMLElement("pos");
     pos.addAttribute("x", thisElement.xPos);
     pos.addAttribute("y", thisElement.yPos);
     proxml.XMLElement dimensions = new proxml.XMLElement("dimensions");
     dimensions.addAttribute("w", thisElement.w); 
     dimensions.addAttribute("h", thisElement.h);  
     proxml.XMLElement triggers = new proxml.XMLElement("triggers");
    
    // print the node
     element.addChild(file);
     element.addChild(pos);
     element.addChild(dimensions);
     element.addChild(triggers);

     
     // Triggers are special, since the number of them vary.   
     // check for the number of items in the triggerList (ArrayList) and add elements accordingly.
     ArrayList thisTriggerList = thisElement.triggerList;
     int numTriggers = thisTriggerList.size();
     for(int k = 0; k<numTriggers; k++) {
       println(numTriggers + " triggers added for item in loop: " + k + " filename: " + thisElement.name);
       int triggerNum =(Integer) thisTriggerList.get(k);
       proxml.XMLElement trigger = new proxml.XMLElement("trigger");
       trigger.addAttribute("number", triggerNum);
       triggers.addChild(trigger);
     }

      
      // find the assigned scene from SceneElement.scene attribute;
      // TODO: I set this to 1 because thisElement.scene is now a STRING instead of an INT
      //int assignedScene = 1;
      String assignedScene = thisElement.scene;
      println("assigned scene: " + assignedScene);
      
      for (int j=0; j < media.countChildren(); j++) {  // go through all scene elements
        // store the looped scene in a temporary variable loopedScene
        proxml.XMLElement loopedScene = media.getChild(j);
         //println("media child: " + loopedScene);
         // save the number attribute of the scene
         String sceneNum = loopedScene.getAttribute("number"); 
          // if the looped Scene has the same attribute as the assignedScene from the SceneElement array... 
          //if (sceneNum == assignedScene) {
           if (sceneNum.equals(assignedScene)) { 
            //.. then add this XML element within that scene
             loopedScene.addChild(element);
             println(j + " XML element created");
        }
      }    
      
   } 

  } 
  xmlIO.saveElement(media, "mediaoutput.xml");
}

/*==============  sceneElement =============*
 when dropped onto the canvas, the Drop
 Canvas creates this object and puts it
 into an Arraylist of these objects.
 *=========================================*/
class SceneElement {
  String name;
  //String transition;
  //int trigger; this int is no longer in use. Please refer to triggerList below
  float w;
  float h;
  int xPos;
  int yPos;
  String scene;

  boolean bActivate;
  int mStartX, mStartY;  //store the position of the mouse when mouse has been pressed
  int mEndX, mEndY;      //store the position of the mouse when mouse has been released
  

  CloseButton c;
  ArrayList triggerList; //this arraylist holds a set of integers, each corresponding to a trigger that has been turned "on".
                         //the max number is 9.
                         
  public PImage myPImage;  //this will hold the image associated with the sceneElement
  

  //SceneElement(String objFilename, int objWidth, int objHeight, int objX, int objY, int objScene, ArrayList objTriggers) {
  //SceneElement(int objHash, String objFilename, int objTrigger, float objWidth, float objHeight, int objX, int objY, int objScene, String objTransition) {
    SceneElement(String objFilename, float objWidth, float objHeight, int objX, int objY, String objScene,  ArrayList objTriggers) {
    //hash=objHash;
    name=objFilename;
    w=objWidth;
    h=objHeight;
    xPos=objX;
    yPos=objY;
    scene=objScene;
    //transition=objTransition;
    //c=new CloseButton(w+xPos,yPos,1,1,12);
    //triggerList=new ArrayList();
    triggerList = objTriggers;

    mEndX = 0;
    mEndY = 0;
    bActivate = false;
    mStartX = 0;
    mStartY = 0;
    c=new CloseButton(PApplet.parseInt(w)+xPos,yPos,1,1,18);
    //triggerList= new ArrayList();
    myPImage=loadImage(objFilename);
  }
  
  public float scaleFactor(){
     return(h/PApplet.parseFloat(myPImage.height));
  }
  
  // this draws an actual rectangle instead of a button
  // later we can pass in PImages, and other parameters
  public void drawSceneElement(){
    // the button code, just in case we need it again
    // Button b;
    // controlP5.addButton(name,val,xPos,yPos,w,h);
   /* stroke(0);
    fill(0);
    rect(xPos,yPos,w,h);
  
    c.drawCloseButton();*/
    //println(name + "drawn to " + scene);    
    fill(0);
    textSize(10);
    textAlign(LEFT,BOTTOM);
    text(name,xPos,yPos);
    
    pushMatrix();
    translate(xPos,yPos);
    scale(scaleFactor());
    image(myPImage,0,0);
    popMatrix();

  }
  
  
  // this changes the visual appearance of the object
  public void hasBeenSelected(){
    /*stroke(255,0,0);
    fill(0);
    rect(xPos,yPos,w,h);*/
    
    fill(255,0,0);
    textSize(10);
    textAlign(LEFT,BOTTOM);
    text(name,xPos,yPos);
    c.drawCloseButton();
  }
  
  // this function is namely for testing purposes to see
  // the stats of each object
  public void displayProperties(){
    println("success");
    println("name: "+name);
    println("width: "+w);
    println("height: "+h);
    println("x-position: "+xPos);
    println("y-position: "+yPos);
    println("triggers: " + triggerList);
  }
  
  // this function returns the name of the given object
  public String objName(){
    String nameOfObject=name;
    return nameOfObject;
  }
  
  // basic functions for updating the object
  /*void updateTrigger(int newTrigger){
    trigger=newTrigger;
  }
  */

  public void updateWidth(float newWidth){
    w=newWidth;
    h=PApplet.parseFloat(myPImage.height)*newWidth/PApplet.parseFloat(myPImage.width);
  }
  public void updateHeight(float newHeight){
    h=newHeight;
    w=PApplet.parseFloat(myPImage.width)*newHeight/PApplet.parseFloat(myPImage.height);
  }
  public void updateX(int newX){
    xPos=newX;
  }
  public void updateY(int newY) {
    yPos=newY;
  }
 /* void updateTransition(String newTransition){
    transition=newTransition;
  }
*/  
  
  // this boolean returns true if mouse is over the sceneElement
  public boolean isMouseOver(int mX, int mY){
    if((mX>xPos) && (mX<xPos+w) && (mY>yPos) && (mY<yPos+h)){
      return true;
    }
    return false;
  }
  
  public boolean isHoveringOverCloseButton(int mX, int mY){
    if(c.isMouseOver(mX,mY)){
      return true;
    }
    return false;
  }
  
  public String getMyName(){
    return name;
  }
  public int getMyX(){
    return xPos;
  }
  public int getMyY(){
    return yPos;
  }
  public float getMyWidth(){
    return w;
  }
  public float getMyHeight(){
    return h;
  }
  public ArrayList getMyTriggerList(){
    return triggerList;
  }
}

/*======================================
begin of CloseButton Class
========================================*/
class CloseButton{
  int xPos;
  int yPos; 
  int wh; //width & height of the square closebutton
  int marginTop;
  int marginRight;
  boolean overState;
  boolean pressedState;
  
  CloseButton(int xPosition, int yPosition, int mT, int mR,int widthAndHeight){
    xPos = xPosition;
    yPos = yPosition;
    marginTop = mT;
    marginRight = mR;
    wh = widthAndHeight;
  }
  
  public void drawCloseButton(){
    if(overState){
      drawHoverState();
    }else if(pressedState){
      drawPressedState();
    }else
    drawNormalState();
  }
  
  public void drawNormalState(){
    overState = false;
    pressedState =false;
    
    stroke(0);
    fill(0);
    rect(xPos-(wh+marginTop),yPos+marginRight,wh,wh);
    
    fill(255);
    textAlign(CENTER,CENTER);
    textSize(wh-(wh/5));
    text("X",xPos-(wh/2),yPos+(wh/2));
  }
  
  public void drawHoverState(){
    overState = true;
    pressedState = false;
    
    stroke(0);
    fill(255,0,0);
    rect(xPos-(wh+marginTop),yPos+marginRight,wh,wh);
    
    fill(255);
    textAlign(CENTER,CENTER);
    textSize(wh-(wh/5));
    text("X",xPos-(wh/2),yPos+(wh/2));
  }
  
  public void drawPressedState(){
    overState = false;
    pressedState = true;
    
    stroke(0);
    fill(50);
    rect(xPos-(wh+marginTop),yPos+marginRight,wh,wh);
    
    fill(255);
    textAlign(CENTER,CENTER);
    textSize(wh-(wh/5));
    text("X",xPos-(wh/2),yPos+(wh/2));
  }
  
  // this boolean returns true of mouse is over the sceneElement
  public boolean isMouseOver(int mX, int mY){
    if((mX>xPos-(wh+marginTop)) && (mX<xPos) && (mY>yPos+marginRight) && (mY<yPos+wh)){
    return true;
    }
    return false;
  }
}

/*======================================
end of CloseButton Class
========================================*/

// this draws all of the scene elements that
// currently exist in the sceneElementArray
public void drawSceneElements() {
  SceneElement drawIt;
  for (int i=0;i<sceneElementArray.size();i++) {
    drawIt=(SceneElement) sceneElementArray.get(i);
    // if the current sceneElement's scene property matches the
    // active scene, then draw the sceneElement.
    //if (drawIt.scene==activeScene) { 
    if (drawIt.scene.equals(activeScene)) { 
      drawIt.drawSceneElement();
    }
  }
}


/*=============   selectSceneElement   ==============*
 Check to see if mouse is over a sceneElement. If
 it is, then change the color properties of the
 element.
 ====================================================*/
public SceneElement selectSceneElement() {
  boolean mouseIsOver;
  for (int i=0;i<sceneElementArray.size();i++) {
    SceneElement element=(SceneElement) sceneElementArray.get(i);
    String elementScene = element.scene; 
    //if (elementScene!=activeScene) {
    if (elementScene.compareTo(activeScene) != 0) {
      println("SKIPPED: element scene: " + element.scene + " vs active Scene: " + activeScene);
      continue;
    }
    println("check if on Element:" + element.scene);
    mouseIsOver=element.isMouseOver(mouseX, mouseY);
    if (mouseIsOver) {
      if (activeElement != element) {
        println(element.objName());
        activeElement=element;
        populatePropertyPanel();
      }
      return element;
    }
  }
  if (activeElement!=null) {
    println("deleselected"+activeElement.objName());
    activeElement=null;
    resetPropertyPanel();
  }
  return null;
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "interface_controls" });
  }
}
