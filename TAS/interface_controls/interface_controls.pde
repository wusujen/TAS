import controlP5.*;
import proxml.*;

ControlP5 controlP5;
ListBox mediaList;
ListBox dragToList;
Textlabel label;
CColor defaultColor;
DropCanvas canvas;
ArrayList sceneElementArray;

// array of file types from /media folder
ArrayList imageFiles;
ArrayList audioFiles;
ArrayList movieFiles;
ArrayList otherFiles;

proxml.XMLElement media;        // xml element to store and load the media (must preface with library name)
XMLInOut xmlIO;                

int hash;                       // unique ID added to every file dropped into the canvas, and written to XML

String[] itemNames;             // stores names of items from media folder for later use
File[] itemInfo;                // sotres File information of items from media folder
int dropListID=0;               // stores how many items have been added to dropCanvas
int dragListID=0;               // stores how many items have been added to drag list
int itemClicked=0;              // stores the first time that a listBoxItem is clicked
boolean mouseDragging=false;    // check to see if mouse is dragging
boolean clickOnController;      // check to see if the mouse is over the controller


String listTitle="Media Files"; // stores the title of the media List
ArrayList placedItems;          // stores all clickedItems/ items placed on the canvas. (not yet implemented)
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
  String path = sketchPath + "/data"; // path to media folder
  size(appWidth, appHeight);
  println(path);
  
  
  //check all files types in /data folder (save them to separate arrays?)
  checkFileTypes();
  // then take the image files and preload them
  // do something else with audio and image files
  
  
  // test to see if sketch is picking up the files
  println("Listing all filenames in a directory: ");
  String[] filenames = listFileNames(path);
  itemNames=filenames;
  println(filenames);

  //initialize dropCanvas
  canvas=new DropCanvas(255, 180, canvasX, canvasY, canvasWidth, canvasHeight);
  //initialize sceneElementArray
  sceneElementArray=new ArrayList();
  // initialize file types arrays
  imageFiles = new ArrayList();
  audioFiles = new ArrayList();
  movieFiles = new ArrayList();
  otherFiles = new ArrayList();
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
  if (clickOnController && mouseDragging && isOpen) {
    dragCursor(int(mouseX), int(mouseY), 120, 15);
  }
}


// use this function to categorize everything in the /data folder before dong anything else with it
void checkFileTypes() {
  String path = sketchPath + "/data"; // path to media folder
  File[] fileInfo = listFiles(path);
  itemInfo = fileInfo;
  
  // loop through all files and list only the name
  for(int i =0; i<itemInfo.length; i++) {
    String mediaFileName = itemInfo[i].getName();
    
    // check for acceptable image formats
    if( (mediaFileName.endsWith("png")) || (mediaFileName.endsWith("jpg")) || (mediaFileName.endsWith("jpeg")) || mediaFileName.endsWith("gif") ){  
      //println("image file: " + mediaFileName);
      File imageFiles =(File) itemInfo[i];
      println("Image Files: " + imageFiles);
    }
    
    // check for accetable audio formats
    if( (mediaFileName.endsWith("aif")) || (mediaFileName.endsWith("mp3")) ){  
      //println("audio file: " + mediaFileName);
      File audioFiles =(File) itemInfo[i];
      println("Audio Files: " + audioFiles);
    }
    
    // check for acceptable movie formats
    if( (mediaFileName.endsWith("mov")) || (mediaFileName.endsWith("mp4")) ){  
      //println("movie file: " + mediaFileName);
      File movieFiles =(File) itemInfo[i];
      println("Movie Files: " + movieFiles);
    }
    
    // finally check for all other formats
    else if (mediaFileName.endsWith("xml")){  
      //println("other media: " + mediaFileName);
      File otherFiles =(File) itemInfo[i];
      println("Other Files: " + otherFiles);
    }
  }
  
}

