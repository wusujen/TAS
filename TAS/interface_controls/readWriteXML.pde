/* 
XML structure
saved to data/mediaoutput.xml
<media>
  <scene number="1">
    <element>
      <file filename="test.jpg" />
      <pos x="420" y="310" />
      <dimensions w="230" h="15" />
      <triggers>
        <trigger number="6" />
        <trigger number="7" />
      </triggers>
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
 loadXMLNodes(); 
}

/*==============  loadXML =============*
 Traverse the XML file and redraw media files 
 as they were placed. Save each element in the XML
 file as a SceneElement in SceneElementArray();
*=========================================*/ 
void loadXMLNodes() {
  
  //media.printElementTree(" ");
 
  proxml.XMLElement scene;
  proxml.XMLElement element;
  
  proxml.XMLElement file;
  proxml.XMLElement pos;
  proxml.XMLElement dimensions;
  proxml.XMLElement triggers;
  proxml.XMLElement trigger;
    
  String filename;
  //String type;
  int number;
  int w;
  int h;
  int x;
  int y;
  int TriggerNum;

  for(int i = 0; i < media.countChildren();i++){ // the number of scenes
    
    scene = media.getChild(i);
    number = scene.getIntAttribute("number");
    
    for(int j = 0; j < scene.countChildren(); j++) { // the number of elements in each scene
      
      element = scene.getChild(i);
      file = element.getChild(0); 
      filename = file.getAttribute("filename");
      pos = element.getChild(1);
      x = pos.getIntAttribute("x");
      y = pos.getIntAttribute("y");
      dimensions = element.getChild(2);
      w = dimensions.getIntAttribute("w"); 
      h = dimensions.getIntAttribute("h");
      triggers = element.getChild(3);
      
      ArrayList myTriggers = new ArrayList();
      for (int p=0; p < triggers.countChildren(); p++) {  
        trigger = triggers.getChild(p);
        myTriggers.add(trigger.getIntAttribute("number"));
      }
      
      // add a new SceneElement to the array.
      sceneElementArray.add(new SceneElement(filename, w, h, x, y, number, myTriggers));
 
    }
    
  }

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
 from the SceneElement that's passed to it.
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
    
    println("All clear! XML deleted.");
  
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

