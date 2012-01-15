/* 
XML structure
saved to data/mediaoutput.xml
<media>
 <file filename="swannhome-etch.jpeg" hash="3">
  <controls trigger="0"/>
  <size height="15" width="120"/>
  <position yPos="307" xPos="298"/>
  <scene number="1"/>
  <transition type="none"/>
 </file>
</media>

*/

/*==============  loadXMLFile =============*
 On setup();
 load media from XML file if it exists
*=========================================*/ 
void loadXMLFile() {
  xmlIO = new XMLInOut(this);
  try{
    xmlIO.loadElement("mediaoutput.xml"); 
  }catch(Exception e){
    //if the xml file could not be loaded, it has to be created
    xmlEvent(new proxml.XMLElement("media"));
  }
}


/*==============  xmlEvent =============*
 register the xmlEvent if there is no XMLfile
 and pass the main element to initCanvas().
 Any scene reconstruction should be done here.
*=========================================*/ 
void xmlEvent(proxml.XMLElement element) {
 media = element;
 proxml.XMLElement lastElement = media.lastChild();
 if(media.hasChildren()) {
   int lastHash = lastElement.getIntAttribute("hash");
   // set the global hash variable
   hash = lastHash;
 }
 initCanvas(); 
}

/*==============  initCanvas =============*
 Traverse the XML file and redraw media files 
 as they were placed.
*=========================================*/ 
void initCanvas() {
  
  println("initCanvas() called");
  //media.printElementTree(" ");
  proxml.XMLElement file;
  proxml.XMLElement controls;
  proxml.XMLElement size;
  proxml.XMLElement position;
  proxml.XMLElement scene;
  proxml.XMLElement transition;
  
  String filename;
  int hash;
  int trigger;
  int w;
  int h;
  int xPos;
  int yPos;
  int sceneNum;
  String type;
  
  println("intial file object array size: " + sceneElementArray.size());
  
  for(int i = 0; i < media.countChildren();i++){
    file = media.getChild(i);
    controls = file.getChild(0);
    size = file.getChild(1);
    position = file.getChild(2);
    scene = file.getChild(3);
    transition = file.getChild(4);
    
    // get filename and hash
    filename = file.getAttribute("filename");
    hash = file.getIntAttribute("hash");
    // get trigger
    trigger = controls.getIntAttribute("trigger");
    // get size of media
    w = size.getIntAttribute("width"); 
    h = size.getIntAttribute("height");  
    // get position
    xPos = position.getIntAttribute("xPos");
    yPos = position.getIntAttribute("yPos");
    // get scene
    sceneNum = scene.getIntAttribute("number");
    // get transition
    type = transition.getAttribute("type");
    
    // add a new SceneElement to the array.
    //SceneElement(int objHash, String objFilename, int objTrigger, int objWidth, int objHeight, int objX, int objY, int objScene, String objTransition)
    sceneElementArray.add(new SceneElement(hash,filename, trigger, w, h, xPos, yPos, sceneNum, type));
    // should this be the same SceneElementArray or a different one?
  }
  
  println("new file object array size: " + sceneElementArray.size());
  
  // this function is not necessary as I am drawing the sceneElements
  // already within draw.
  // drawFromXML(sceneElementArray);
}

// this function is not necessary as I am drawing the sceneElements
// already within draw.
/*void drawFromXML(ArrayList sceneElementArray) {

  ArrayList placedItems = new ArrayList(); 
  
  for(int i = 0; i < sceneElementArray.size(); i++) {
    SceneElement pie=(SceneElement) sceneElementArray.get(i);
    pie.drawSceneElement();
    println(pie);
  }
  println("Media drawn to canvas");
}*/


/*==============  writeToXML =============*
 Called whenever the program should alter XML.
 Checks whether a hash already exists in XML,
 and therefore whether to create the new node
 before updating it.
*=========================================*/ 

void writeToXML(SceneElement node){

  int numFileNodes = media.countChildren();    // number of files saved to XML
  int[] savedHashes = new int[numFileNodes];   // array of all XML hashes saved so far
  int activeHash = node.hash;                  // hash of the selected object in sketch
  Boolean nodeExists = false;                  // assume the node is new
  int fileIndex = 0;                           // the index of the file to alter 
  
 // loop through all nodes in XML
 // to find a possible matching hash
  for (int i=0; i<numFileNodes; i++) {
    proxml.XMLElement loopFile = media.getChild(i);   // the current file
    int loopHash = loopFile.getIntAttribute("hash");  // value of the current file hash
    savedHashes[i] = loopHash;                        // add the file hash to the array  
    // if the array contains the active hash
    // nodeExists is actually true! 
    if(savedHashes[i] == activeHash) {
      nodeExists = true;
      fileIndex = i;
      println(node.name + "Node exists and must be updated"); 
    }  
  }

 // So logic tells us...
 // if the node is new, first create it.
  if(nodeExists == false) {
     println("This node needs to be created");
     xmlCreate(node);
  } else {
    // otherwise, just update it with new values
    // from the SceneElement we're passing in
    xmlUpdate(node, fileIndex);
  }
}


/*============== xmlCreate =============*
 Creates a new XML file using the info
 from the node SceneElement passed to it.
*=========================================*/ 
void xmlCreate(SceneElement node) {
  proxml.XMLElement file = new proxml.XMLElement("file");
  proxml.XMLElement controls = new proxml.XMLElement("controls");
  proxml.XMLElement size = new proxml.XMLElement("size");
  proxml.XMLElement position = new proxml.XMLElement("position");
  proxml.XMLElement scene = new proxml.XMLElement("scene"); 
  proxml.XMLElement transition = new proxml.XMLElement("transition");
  
  // record filename and hash
  file.addAttribute("filename", node.name);
  file.addAttribute("hash", node.hash);
  // record trigger
  controls.addAttribute("trigger", node.trigger);
  // record size of media
  size.addAttribute("width", node.w); 
  size.addAttribute("height", node.h);  
  // record position
  position.addAttribute("xPos", node.xPos);
  position.addAttribute("yPos", node.yPos);
  // record scene
  scene.addAttribute("number", node.scene);
  // record transition
  transition.addAttribute("type", node.transition);
  
   // assemble the node
   media.addChild(file);
   file.addChild(controls);
   file.addChild(size);
   file.addChild(position);
   file.addChild(scene);
   file.addChild(transition); 
    
   xmlIO.saveElement(media, "mediaoutput.xml");
  
  println(node.name + " XML node created");
}


/*============== xmlUpdate =============*
 Updates an XML file using the info
 from the node SceneElement passed to it and
 and the index of that XML file from the loop
 performed in writeToXML().
*=========================================*/ 
void xmlUpdate(SceneElement node, int index) {
  
  // indicate which file you want to update
  proxml.XMLElement file = media.getChild(index);
  proxml.XMLElement controls = file.getChild(0);
  proxml.XMLElement size = file.getChild(1);
  proxml.XMLElement position = file.getChild(2);
  proxml.XMLElement scene = file.getChild(3);
  proxml.XMLElement transition = file.getChild(4);
  
  // record filename and hash
  file.addAttribute("filename", node.name);
  file.addAttribute("hash", node.hash);
  // record trigger
  controls.addAttribute("trigger", node.trigger);
  // record size of media
  size.addAttribute("width", node.w); 
  size.addAttribute("height", node.h);  
  // record position
  position.addAttribute("xPos", node.xPos);
  position.addAttribute("yPos", node.yPos);
  // record scene
  scene.addAttribute("number", node.scene);
  // record transition
  transition.addAttribute("type", node.transition);
      
  // assemble the node (this may not be neccessary in the update function
 //media.addChild(file);
 //file.addChild(controls);
 //file.addChild(size);
 //file.addChild(position);
 //file.addChild(scene);
 //file.addChild(transition);  

 
 xmlIO.saveElement(media, "mediaoutput.xml");
 
 println(node.name + " XML file updated");
}
  

/*============== xmlRemoveItem =============*
 Deletes an item from the XML file.
*=========================================*/ 
void xmlRemoveItem(SceneElement node){ 
 // remove the node with sceneElement hash = file.getAttribute("hash");
 int hashToRemove = node.hash;
 println("hash to remove: " + hashToRemove);
 for (int i=0; i<media.countChildren(); i++) {
     proxml.XMLElement file = media.getChild(i);
     int hash = file.getIntAttribute("hash");
     if (hash == hashToRemove) {
        media.removeChild(i);
      }
      // save the XML file
      xmlIO.saveElement(media, "mediaoutput.xml");
    }
   println(node.hash + " " + node.name + " XML: object removed from canvas"); 
}
