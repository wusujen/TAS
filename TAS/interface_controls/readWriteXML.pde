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

<media>
 <scene number="1">
   <element>
			<file name="" type="" />
			<trigger>
                          <triggerNumber="" />
                          <triggerNumber="" />
                        </trigger>
			<pos x="" y="" />
			<dimensions w="" h= "" />
    </element>
  </scene>
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
 //proxml.XMLElement lastElement = media.lastChild();
 loadXMLNodes(); 
}

/*==============  loadXML =============*
 Traverse the XML file and redraw media files 
 as they were placed. Save each element in the XML
 file as a SceneElement in SceneElementArray();
*=========================================*/ 
void loadXMLNodes() {
  
  println("loadXMLNodes() called");
  
  media.printElementTree(" ");
 
  proxml.XMLElement scene;
  proxml.XMLElement element;
  
  proxml.XMLElement file;
  proxml.XMLElement pos;
  proxml.XMLElement dimensions;
  proxml.XMLElement trigger;
    
  String filename;
  //String type;
  int triggerNumber;
  int w;
  int h;
  int x;
  int y;
  int number;
 
  
  for(int i = 0; i < media.countChildren();i++){ // the number of scenes
    
    scene = media.getChild(i);
    number = scene.getIntAttribute("number");
    
    for(int j = 0; j < scene.countChildren(); j++) { // the number of elements in each scene
      
      element = scene.getChild(i);
      file = element.getChild(0); 
      filename = file.getAttribute("filename");
      pos = file.getChild(1);
      x = pos.getIntAttribute("x");
      y = pos.getIntAttribute("y");
      dimensions = file.getChild(2);
      w = dimensions.getIntAttribute("w"); 
      h = dimensions.getIntAttribute("h");
      trigger = element.getChild(3);
      /* TODO: add triggers to the array and load them into sceneElement Array
      for (trigger.countChildren()) {  
        ArrayList myTriggers = new ArrayList();
        myTriggers.add(trigger.getIntAttribute("triggerNumber");
      }
      
      */
      // add a new SceneElement to the array.
      sceneElementArray.add(new SceneElement(filename, w, h, x, y, number)); //TODO: add trigger to constructor  
    }
    
  }
  
  println("new file object array size: " + sceneElementArray.size());
  doneLoading=true;
  
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



/*============== saveXML =============*
 Creates the XML file using the info
 from the SceneElement passed to it.
*=========================================*/ 
void saveXML(ArrayList elements) {
  
  boolean xmlDeleted = false;
  //erase the existing file before rewriting.
  for (int i = 0; i < media.countChildren(); i++) {
    media.removeChild(i);
    if (media.hasChildren() == false) {
      xmlDeleted = true;
    } 
  }
  
  // if the file is clean, commense with repopulating it
  if(xmlDeleted) {
    
    // iterate through sceneElementArray
  println("all clear!");
  
  }
  
  
  /*
  int sceneNum = node.scene;

  proxml.XMLElement scene = new proxml.XMLElement("scene"); 
    
  media.getChild[sceneNum+1]; // TODO: find the scene that this node should be added to
  
  proxml.XMLElement file = new proxml.XMLElement("file");
  proxml.XMLElement size = new proxml.XMLElement("dimensions");
  proxml.XMLElement position = new proxml.XMLElement("pos");
// check for the number of items in the triggerList (ArrayList)
  proxml.XMLElement controls = new proxml.XMLElement("trigger");

  
    // record scene
  scene.addAttribute("number", node.scene);
  
  // record filename and hash
  file.addAttribute("filename", node.name);

  // record size of media
  size.addAttribute("w", node.w); 
  size.addAttribute("h", node.h);  
  // record position
  position.addAttribute("x", node.xPos);
  position.addAttribute("y", node.yPos);

  // record trigger
  controls.addAttribute("trigger", node.trigger);
  
   // assemble the node
   media.addChild(scene);
   scene.addChild(file);
   file.addChild(trigger);
   file.addChild(dimensions);
   file.addChild(pos);
    
   
  println(node.name + " XML node created");
  */
  xmlIO.saveElement(media, "mediaoutput.xml");
}

