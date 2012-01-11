import controlP5.*;
import proxml.*;

int appWidth = 1200;            // processing applet measurements
int appHeight = 800;            

int canvasX = 280;              // drop canvas measurements
int canvasY = 100;
int canvasWidth = 600;
int canvasHeight = 600;

int libX = 50;                  // Media Library dimensions
int libY = 50;              
int libW = 200;
int libH = 300;

ControlP5 controlP5;

ListBox mediaList;   // TODO necessary?
ListBox dragToList;   // testing

ListBox mediaList;
ListBox dragToList;
Textlabel label;
CColor defaultColor;
DropCanvas canvas;
ArrayList sceneElementArray;


ArrayList allFiles;            // global array of files loaded from media folder...
ArrayList imageFiles;          // ...broken down by filetype
ArrayList audioFiles;
ArrayList movieFiles;
ArrayList otherFiles;



CColor defaultColor;
DropCanvas canvas;
ArrayList fileObjectArray;


proxml.XMLElement media;        // xml element to store and load the media (must preface with library name)
XMLInOut xmlIO;                

int hash;                       // unique ID added to every file dropped into the canvas, and written to XML

String[] itemNames;             // stores names of items from media folder for later use
File[] itemInfo;                // sotres File information of items from media folder
int dropListID=0;               // stores how many items have been added to dropCanvas
int dragListID=0;               // stores how many items have been added to drag list
boolean mouseDragging=false;    // check to see if mouse is dragging
boolean clickOnController;      // check to see if the mouse is over the controller



String listTitle="Media Files"; // stores the title of the media List
ArrayList placedItems;          // stores all clickedItems/ items placed on the canvas. (not yet implemented)

String clickedItemName;         // stores the name of the item clicked from mediaList
int val;                        // stores the id of the item clicked from mediaList





String activeSceneElement;      //stores the name of activeSceneElement
SceneElement activeElement;     //stores the actual activeSceneElement

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
  
  // then take the image files and preload them
  // do something else with audio and image files
  

  
  // test to see if sketch is picking up the files  TODO: check if this is necessary
  //println("Listing all filenames in a directory: ");
  String[] filenames = listFileNames(path);
  itemNames=filenames;
  //println(filenames);

  //initialize dropCanvas
  canvas=new DropCanvas(255, 180, canvasX, canvasY, canvasWidth, canvasHeight);
  
  //initialize fileObjectArray
  // hummmmm check this
  fileObjectArray=new ArrayList();

  
  //initialize sceneElementArray
  sceneElementArray=new ArrayList();
  // initialize file types arrays
  imageFiles = new ArrayList();
  audioFiles = new ArrayList();
  movieFiles = new ArrayList();
  otherFiles = new ArrayList();

  // create the initial media list from media folder
  //createMediaList(itemNames);
  MediaLibrary imageLib = new MediaLibrary(imageFiles, "Images", 1);
  MediaLibrary audioLib = new MediaLibrary(audioFiles, "Audio", 2);
  MediaLibrary movieLib = new MediaLibrary(movieFiles, "Movies", 3);
  
  
  // create the empty drag to List
  // createDragToList();
  // let dropListItems know what the max number of items can be
  // load XML file
  loadXMLFile();

}

void draw() {
  background(200);

  // draws the canvas
  canvas.drawDropCanvas();
  drawSceneElements();
  
  // check if SceneElement has been selected, as long
  // as the mouse is within the boundaries of the canvas
  if(canvas.mouseIsWithinDropCanvas() && mousePressed){
    activeElement=selectSceneElement();
  }
  
  // if an element has been selected, then change
  // the visual properties of that element
  if(activeElement!=null){
    activeElement.hasBeenSelected();
  }

  // checks to see if the mouse is over the controller
  // if the mouse is not dragging an item, the cursor
  // does not appear
  /*clickOnController=controlP5.window(this).isMouseOver();
  boolean isOpen=mediaList.isOpen();
  if (clickOnController && mouseDragging && isOpen) {
    dragCursor(int(mouseX), int(mouseY), 120, 15);
  }*/
}
