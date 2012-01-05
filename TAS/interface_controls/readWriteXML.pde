/* 
XML structure
saved to data/mediaoutput.xml

<media>  
  <file name"sitin.jpg">
    <controls trigger="1" />
    <size width="433" height="230" />
    <position xPos="25" yPos="25" />
    <size width="433" height="230" />
    <scene number="1" />   
    <transition type="none" />
  <file>
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
 initCanvas(); 
}

/*==============  initCanvas =============*
 Traverse the XML file and redraw media files 
 as they were placed.
*=========================================*/ 
void initCanvas() {
  media.printElementTree(" ");
  proxml.XMLElement media;

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
  proxml.XMLElement file = new proxml.XMLElement("file");
 
 // record filename
  file.addAttribute("filename", node.name);
  // record trigger
  proxml.XMLElement controls = new proxml.XMLElement("controls");
  controls.addAttribute("trigger", node.trigger);
  // record size of media
  proxml.XMLElement size = new proxml.XMLElement("size"); 
  size.addAttribute("width", node.w); 
  size.addAttribute("height", node.h);  
  // record position
  proxml.XMLElement position = new proxml.XMLElement("position");
  position.addAttribute("xPos", node.xPos);
  position.addAttribute("yPos", node.yPos);
  // record scene
  proxml.XMLElement scene = new proxml.XMLElement("scene"); 
  scene.addAttribute("number", node.scene);
  // record transition
  proxml.XMLElement transition = new proxml.XMLElement("transition");
  transition.addAttribute("type", node.transition);
  
  // assemble the node
  media.addChild(file);
  file.addChild(controls);
  file.addChild(size);
  file.addChild(position);
  file.addChild(scene);
  file.addChild(transition);
  
  // save the node to the file
  xmlIO.saveElement(media, "mediaoutput.xml");
 
 // test
  println("XML: Media added to canvas");  
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

void xmlRemoveItem(){ 
  // capture the filename of the removed item
  // find the corresponding file item with the filename
  // and remove it.
 // String[] attributes = element.getAttribute();
  println("XML: Media removed from canvas");
}
