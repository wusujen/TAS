import controlP5.*;
import proxml.*;

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
Button saveBtn;

ArrayList sceneArray;          // holds an array of scenes--looped through when saving to XML
String activeScene;            // stores the name of the activeScene;

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

String clickedItemName;         // stores the name of the item clicked from mediaList
int val;                        // stores the id of the item clicked from mediaList

boolean doneLoading=false;      //is set to true when XML is done loading

void setup() {
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
  // load save Button, which will trigger writing to XML
  saveBtn = controlP5.addButton("save", 1 , appWidth - 100, appHeight - 50, 50, 20);

  // load XML file
  loadXMLFile();

  //loads controlp5 parts of propertyPanel
  setupPropertyPanel();
  
  //testing
  println("---------------");
  println("number of Scene Elements on load: " + sceneElementArray.size());
  for(int i=0;i<sceneElementArray.size();i++){
      SceneElement element=(SceneElement) sceneElementArray.get(i);
      println(element.objName());
  }
  println("---------------");
  // end testing
}

void draw() {
  background(200);
  
  // draws the canvas
  canvas.drawDropCanvas();
  if (doneLoading) {
    drawSceneElements();
  }

  // check if SceneElement has been selected, as long
  // as the mouse is within the boundaries of the canvas
  if (canvas.mouseIsWithinDropCanvas() && mousePressed) {
    selectSceneElement();
  }

  // if an element has been selected, then change
  // the visual properties of that element
  if (activeElement!=null) {
    activeElement.hasBeenSelected();
    drawPropertyPanel();
  }

}

