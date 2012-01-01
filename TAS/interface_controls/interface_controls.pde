import controlP5.*;

ControlP5 controlP5;
ListBox mediaList;
ListBox dragToList;
Button tryButton;
String[] itemNames; //stores names of items from media folder for later use
int dragListID=0; //stores how many items have been added to drag list
boolean dragging=false;
String clickedItemName;
int val;

void setup() {
  size(400,400);
  // Path
  String path = sketchPath + "/media";
  
  // Test to see if sketch is picking up the files
  println("Listing all filenames in a directory: ");
  String[] filenames = listFileNames(path);
  itemNames=filenames;
  println(filenames);
  
  //create the initial media list from media folder
  createMediaList(itemNames);
  //create the empty drag to List
  createDragToList();
}

void draw() {
  background(200);
  //creates the canvas
  dropCanvas(50,150,300,150);
  if(dragging){
    dragCursor(int(mouseX),int(mouseY),120,15);
  }
}

