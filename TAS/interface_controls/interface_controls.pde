import controlP5.*;
import proxml.*;

ControlP5 controlP5;
ListBox mediaList;
ListBox dragToList;
Textlabel label;
CColor defaultColor;
DropCanvas canvas;
ArrayList fileObjectArray;

proxml.XMLElement media;        // xml element to store and load the media (must preface with library name)
XMLInOut xmlIO;                

int hash;                       // unique ID added to every file dropped into the canvas, and written to XML

String[] itemNames;             // stores names of items from media folder for later use
int dropListID=0;               // stores how many items have been added to dropCanvas
int dragListID=0;               // stores how many items have been added to drag list
int itemClicked=0;              // stores the first time that a listBoxItem is clicked
boolean mouseDragging=false;    // check to see if mouse is dragging
boolean clickOnController;      // check to see if the mouse is over the controller

String listTitle="Media Files"; // stores the title of the media List
String clickedItemName;         // stores the name of the item clicked from mediaList
int val;                        // stores the id of the item clicked from mediaList

int appWidth = 1200;            // processing applet measurements
int appHeight = 800;            

int canvasX = 200;              // drop canvas measurements
int canvasY = 100;
int canvasWidth = 600;
int canvasHeight = 600;

boolean onCanvas;               // check that t 


void setup() {
  size(appWidth, appHeight);
  String path = sketchPath + "/media"; // path to media folder

  // test to see if sketch is picking up the files
  println("Listing all filenames in a directory: ");
  String[] filenames = listFileNames(path);
  itemNames=filenames;
  println(filenames);
  
  //initialize dropCanvas
  canvas=new DropCanvas(255,180,canvasX, canvasY, canvasWidth, canvasHeight);
  //initialize fileObjectArray
  fileObjectArray=new ArrayList();
  // create the initial media list from media folder
  createMediaList(itemNames);
  // create the empty drag to List
  createDragToList();
  // let dropListItems know what the max number of items can be
  
  // load XML file
  loadXMLFile();
}

void draw() {
  background(200);
  
  // draws the canvas
  canvas.drawDropCanvas();
  
  // checks to see if the mouse is over the controller
  // if the mouse is not dragging an item, the cursor
  // does not appear
  clickOnController=controlP5.window(this).isMouseOver();
  boolean isOpen=mediaList.isOpen();
  if(clickOnController && mouseDragging && isOpen){
      dragCursor(int(mouseX),int(mouseY),120,15);
  }
}

