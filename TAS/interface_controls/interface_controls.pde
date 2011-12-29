import controlP5.*;


ControlP5 control;          // Declare the ControlP5
ListBox mediaLib;           // Declare the controls that will be used
int currentMediaIndex;      // stores the id of the active listbox item.


void setup(){
  // Path to media files
  String rawMediaDir = sketchPath + "/media";
  // save all media files in an array
  // to access later
  File[] files = listFiles(rawMediaDir);
  
  smooth();
  size(1400, 900);
  
  control = new ControlP5(this);
  // add the media library using ListBox object
  mediaLib = control.addListBox("media",30,100,250,400);
  mediaLib.setItemHeight(20);
  mediaLib.setBarHeight(22);
  mediaLib.captionLabel().set("Media Library");
  mediaLib.captionLabel().style().marginTop = 3;
  mediaLib.valueLabel().style().marginTop = 3; // the +/- sign
  mediaLib.setId(1);
  
  // populate the listbox with items from the raw media folder
  for(int i=0;i<files.length;i++) {
    String fileName = files[i].getName();
    // TODO: ignore hidden files
    mediaLib.addItem(fileName,i);
  }


}

void draw() {

}

void controlEvent(ControlEvent event){
  if (event.isGroup()) {
    // an event from a group e.g. ListBox
      println(event.group().value() +" from "+event.group());
      // controlEvent is called when a listbox-item
      // has been activated, hence update the value
      // of myCurrentIndex accordingly
      currentMediaIndex = int(event.group().value());
      println(currentMediaIndex);
    }
}



// This function returns all the files in a directory as an array of File objects
// This is useful if you want more info about the file
File[] listFiles(String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    File[] files = file.listFiles();
    return files;
  } else {
    // If it's not a directory
    return null;
  }
}
