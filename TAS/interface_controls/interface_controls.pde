import controlP5.*;

ControlP5 controlP5;
ListBox mediaList;
ListBox dragToList;
Textlabel label;
CColor defaultColor;
DropCanvas canvas;
ArrayList fileObjectArray;

String[] itemNames;             // stores names of items from media folder for later use
int dropListID=0;               // stores how many items have been added to dropCanvas
int dragListID=0;               // stores how many items have been added to drag list
boolean clickOnController;      // check to see if the mouse is over the controller
int itemClicked=0;              // stores the first time that a listBoxItem is clicked
boolean mouseDragging=false;    // check to see if mouse is dragging

String listTitle="Media Files"; // stores the title of the media List
String clickedItemName;         // stores the name of the item clicked from mediaList
String labelName;               // stores the name of the last created label
String firstClicked;            // stores the name of the first item that was clicked
int val;                        // stores the id of the item clicked from mediaList


void setup() {
  size(400,400);
  // path to media folder
  String path = sketchPath + "/media";
  
  // test to see if sketch is picking up the files
  println("Listing all filenames in a directory: ");
  String[] filenames = listFileNames(path);
  itemNames=filenames;
  println(filenames);
  
  //initialize dropCanvas
  canvas=new DropCanvas(255,180,50,150,300,150);
  //initialize fileObjectArray
  fileObjectArray=new ArrayList();
  // create the initial media list from media folder
  createMediaList(itemNames);
  // create the empty drag to List
  createDragToList();
  // let dropListItems know what the max number of items can be
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

