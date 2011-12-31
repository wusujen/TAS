import controlP5.*;

ControlP5 controlP5;
ListBox mediaList;
ListBox dragToList;
String[] itemNames; //stores names of items from media folder for later use
int dragListID=0; //stores how many items have been added to drag list

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

// Nothing is drawn in this program and the draw() doesn't loop because
// of the noLoop() in setup()
void draw() {
background(0);
}

