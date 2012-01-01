import controlP5.*;

ControlP5 controlP5;
ListBox mediaList;
ListBox dragToList;
Textlabel label;

String[] itemNames;             // stores names of items from media folder for later use
int dragListID=0;               // stores how many items have been added to drag list
boolean clickOnController;      // check to see if the mouse is over the controller
boolean mouseDragging=false;    // check to see if mouse is dragging

String listTitle="Media Files"; // stores the title of the media List
String clickedItemName;         // stores the name of the item clicked from mediaList
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
  
  // create the initial media list from media folder
  createMediaList(itemNames);
  // create the empty drag to List
  createDragToList();
}

void draw() {
  background(200);
  
  // creates the canvas
  dropCanvas(50,150,300,150);
  
  // checks to see if the mouse is over the controller
  // if the mouse is not dragging an item, the cursor
  // does not appear
  clickOnController=controlP5.window(this).isMouseOver();
  boolean isOpen=mediaList.isOpen();
  if(clickOnController && mouseDragging && isOpen){
      dragCursor(int(mouseX),int(mouseY),120,15);
  }
}

