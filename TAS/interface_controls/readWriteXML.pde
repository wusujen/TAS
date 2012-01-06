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
  media.printElementTree(" ");
  proxml.XMLElement media;
  //TODO: recreate canvas from XML data
  //println("intial file object array size: " + fileObjectArray.size());
  //for(int i = 0; i < media.countChildren();i++){
   // thisFile = media.getChild(i);
   // position = thisFile.getChild(0);
 // }
}


/*==============  Write to XML =============*
 perform specific actions to the XML file depending
 on how the user has interacted with the media
 Call these xml functions in your UI functions.
*=========================================*/ 
void xmlAddToCanvas(FileObject node){
 // create the file node in XML
 // TODO: Only create new IF This is a new node that has been dropped on the canvas
 // otherwise, update all attribtues based on the filename (id?)
 
 int activeHash = node.hash; // hash of the selected object in sketch
  
  // loop through all XML nodes
   for (int i=0; i<media.countChildren(); i++) {
     
     proxml.XMLElement loopFile = media.getChild(i);
     int loopHash = loopFile.getIntAttribute("hash");
     
      // if the active hash is unique, create a new node
      if(activeHash != loopHash) {
        proxml.XMLElement file = new proxml.XMLElement("file");
        proxml.XMLElement controls = new proxml.XMLElement("controls");
        proxml.XMLElement size = new proxml.XMLElement("size");
        proxml.XMLElement position = new proxml.XMLElement("position");
        proxml.XMLElement scene = new proxml.XMLElement("scene"); 
        proxml.XMLElement transition = new proxml.XMLElement("transition");
                   // Do all this regardless, and do it once.
     
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
  
        // save the XML file
        xmlIO.saveElement(media, "mediaoutput.xml");
        println("this is a new node. loopHash: " + loopHash);
      } else if (activeHash == loopHash) {
        // assign the changes to matching node
        proxml.XMLElement file = loopFile;
        proxml.XMLElement controls = file.getChild(0);
        proxml.XMLElement size = file.getChild(1);
        proxml.XMLElement position = file.getChild(2);
        proxml.XMLElement scene = file.getChild(3);
        proxml.XMLElement transition = file.getChild(4);
        
        println("THis is not a new node");
        
                   // Do all this regardless, and do it once.
     
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
  
  // save the XML file
  xmlIO.saveElement(media, "mediaoutput.xml");
  
      }
      
      
  }
    
}

void xmlMoveItem(){
  println("XML: Media moved on canvas");
}

void xmlResize(){
  println("XML: Media resized.");
}

void xmlZIndex(){
  println("XML: Item's Z-Index changed on canvas");
}

void xmlRemoveItem(FileObject node){ 
 // remove the node with fileObject hash = file.getAttribute("hash");
 int hashToRemove = node.hash;
 for (int i=0; i<media.countChildren(); i++) {
     proxml.XMLElement file = media.getChild(i);
     int hash = file.getIntAttribute("hash");
     if (hash == hashToRemove) {
        media.removeChild(i);
      }
      // save the XML file
      xmlIO.saveElement(media, "mediaoutput.xml");
    }
}
