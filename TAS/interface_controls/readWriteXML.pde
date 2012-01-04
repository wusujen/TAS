/* 
XML structure
saved to data/mediaoutput.xml

<media>  
  <file name"sitin.jpg">
    <position xPos="25" yPos="25" />
    <controls trigger="1" />
    <scene number="1" /> 
    <size width="433" height="230" />
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
    //if the xml file could not be loaded it has to be created
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
void xmlAddToCanvas(){
  // build the new XML node
  proxml.XMLElement file = new proxml.XMLElement("file");
  file.addAttribute("filename", clickedItemName);
  proxml.XMLElement position = new proxml.XMLElement("position");
  position.addAttribute("xPos", mouseX);
  position.addAttribute("yPos", mouseY);
  proxml.XMLElement scene = new proxml.XMLElement("scene"); 
  scene.addAttribute("number", "SCENE NUM");//TODO: capture this var
  proxml.XMLElement size = new proxml.XMLElement("size"); 
  size.addAttribute("width", "TODO");//TODO: capture this var
  size.addAttribute("height", "TODO");//TODO: capture this var
  proxml.XMLElement transition = new proxml.XMLElement("transition");
  transition.addAttribute("type", "none");//TODO: capture this var. default to none.
  proxml.XMLElement controls = new proxml.XMLElement("trigger");
  controls.addAttribute("trigger", "-1"); //TODO: capture this var  
  
  // add the node to the file
  media.addChild(file);
  file.addChild(position);
  file.addChild(scene);
  file.addChild(size);
  file.addChild(transition);
  file.addChild(controls);
  xmlIO.saveElement(media, "mediaoutput.xml");
  
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
