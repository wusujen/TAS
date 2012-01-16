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

ListBox mediaList;   // TODO necessary?
ListBox dragToList;   // testing

Textlabel label;

ArrayList sceneElementArray;


ArrayList allFiles;            // global array of files loaded from media folder...
ArrayList imageFiles;          // ...broken down by filetype
ArrayList audioFiles;
ArrayList movieFiles;
ArrayList otherFiles;


CColor defaultColor;
DropCanvas canvas;

proxml.XMLElement media;        // xml element to store and load the media (must preface with library name)
XMLInOut xmlIO;                

int hash;                       // unique ID added to every file dropped into the canvas, and written to XML

String[] itemNames;             // stores names of items from media folder for later use
File[] itemInfo;                // sotres File information of items from media folder
int dropListID=0;               // stores how many items have been added to dropCanvas
int dragListID=0;               // stores how many items have been added to drag list
boolean mouseDragging=false;    // check to see if mouse is dragging
boolean clickOnController;      // check to see if the mouse is over the controller

String clickedItemName;         // stores the name of the item clicked from mediaList
int val;                        // stores the id of the item clicked from mediaList

String activeSceneElement;      //stores the name of activeSceneElement
SceneElement activeElement;     //stores the actual activeSceneElement

boolean doneLoading=false;      //is set to true when XML is done loading

void setup() {
    // path to media folder
  String path = sketchPath + "/data";
  // draw the applet
  size(appWidth, appHeight);
  
  // declare a global instance of ControlP5;
  controlP5 = new ControlP5(this);
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
  
  //initialize sceneElementArray
  sceneElementArray=new ArrayList();


  // create the initial media list from media folder
  //createMediaList(itemNames);
  MediaLibrary imageLib = new MediaLibrary(imageFiles, "Images", 1);
  MediaLibrary audioLib = new MediaLibrary(audioFiles, "Audio", 2);
  MediaLibrary movieLib = new MediaLibrary(movieFiles, "Movies", 3);
  
  
  // then take the image files and preload them
  // do something else with audio and image files
  

  
  // test to see if sketch is picking up the files 
  //println("Listing all filenames in a directory: ");
  itemNames=new String[allFiles.size()];
  for(int i=0;i<imageFiles.size();i++){
    itemNames[i]=(String) imageFiles.get(i);
  }
  for(int i=0;i<audioFiles.size();i++){
    itemNames[imageFiles.size()+i]=(String) audioFiles.get(i);
  }
  for(int i=0;i<movieFiles.size();i++){
    itemNames[imageFiles.size()+audioFiles.size()+i]=(String) movieFiles.get(i);
  }
  //println(filenames);

  //initialize dropCanvas
  canvas=new DropCanvas(255, 180, canvasX, canvasY, canvasWidth, canvasHeight);
  
  //initialize fileObjectArray
  //fileObjectArray=new ArrayList();


  
  // create the empty drag to List
  // createDragToList();
  // let dropListItems know what the max number of items can be
  // load XML file
  loadXMLFile();
  
  
  //loads controlp5 parts of propertyPanel
  setupPropertyPanel();
  
  doneLoading=true;
}

void draw() {
  background(200);
  
  // draws the canvas
  canvas.drawDropCanvas();
  if(doneLoading){
    drawSceneElements();
  }
  
  // if an element has been selected, then change
  // the visual properties of that element
  if(activeElement!=null){
    activeElement.hasBeenSelected();
    drawPropertyPanel();
  }
}
