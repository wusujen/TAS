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

/*==============  loadXMLNodes =============*
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
      
      // add new SceneElement to the array.
      sceneElementArray.add(new SceneElement(filename, w, h, x, y, number, myTriggers));
    }
  }
  doneLoading=true;
}


/*============== saveXML =============*
 Creates the XML file using the info
 from the SceneElement that's passed to it.
*=========================================*/ 
void saveXML(ArrayList elements) {
  println("saveXML called");
  boolean xmlDeleted = false;
  if(media.countChildren() > 0) {
    println("there are childre nodes in the XML and they must be removed");
    for(int i = media.countChildren()-1; i > -1; i--) { // remove each scene
      println(i);
      media.removeChild(i);
      if (i == 0) {
        println("Old XML deleted. All clear to repopulate!");
        xmlDeleted = true;
      }
     }
   } else {
     println("There never were any children!");
     xmlDeleted = true;
   }
   
  if(xmlDeleted) {
  
   println("xml is deleted, work magic now."); 
   
    // Once scenes are dynamic, iterate through sceneArray.size()
    // and construct a new scene element for each item, with a String 
    // attribute for the name. NOT AN INTEGER! (this changes a lot of things).
    // For now, we assume 1 scene and construct it manually
    // outside of any loops.
    proxml.XMLElement scene = new proxml.XMLElement("scene"); 
    media.addChild(scene);
    scene.addAttribute("number", 1);
   
   
    xmlIO.saveElement(media, "mediaoutput.xml");
  }
}

