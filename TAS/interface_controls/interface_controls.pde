import controlP5.*;

ControlP5 controlP5;
ListBox l;
String[] itemNames; //stores all of the item names for later use

void setup() {
  size(400,400);
  // Path
  String path = sketchPath + "/media";
  
  // Test to see if sketch is picking up the files
  println("Listing all filenames in a directory: ");
  String[] filenames = listFileNames(path);
  itemNames=filenames;
  println(filenames);
  
  // Create settings for the list element
  controlP5 = new ControlP5(this);
  l = controlP5.addListBox("myList",50,50,120,300);
  l.setItemHeight(15);
  l.setBarHeight(15);

  l.captionLabel().toUpperCase(true);
  l.captionLabel().set("Media Files");
  l.captionLabel().style().marginTop = 3;
  l.valueLabel().style().marginTop = 3; // the +/- sign
  for(int i=0;i<filenames.length;i++) {
    l.addItem(filenames[i],i);
  }
  l.setColorBackground(color(164,170,183));
  l.setColorActive(color(140,140,140));
}

// Nothing is drawn in this program and the draw() doesn't loop because
// of the noLoop() in setup()
void draw() {
background(0);
}

