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
  String number;
  float w;
  float h;
  int x;
  int y;
  int TriggerNum;

  for(int i = 0; i < media.countChildren();i++){ // the number of scenes
    
    scene = media.getChild(i);
    number = scene.getAttribute("number");
    
    for(int j = 0; j < scene.countChildren(); j++) { // the number of elements in each scene
      
      element = scene.getChild(j);
      file = element.getChild(0); 
      filename = file.getAttribute("filename");
      println("filenames loaded " + filename);
      pos = element.getChild(1);
      x = pos.getIntAttribute("x");
      y = pos.getIntAttribute("y");
      dimensions = element.getChild(2);
      w = dimensions.getFloatAttribute("w"); 
      h = dimensions.getFloatAttribute("h");
      triggers = element.getChild(3);
      
      ArrayList myTriggers = new ArrayList();
      for (int p=0; p < triggers.countChildren(); p++) {  
        trigger = triggers.getChild(p);
        myTriggers.add(trigger.getIntAttribute("number"));
      }
      
      // add new SceneElement to the array.
      // TODO: activeScene is INCORRECT. This must be loaded from the XML/sceneElement
      sceneElementArray.add(new SceneElement(filename, w, h, x, y, number, myTriggers));
    }
  }
  doneLoading=true;
  // testing
  for (int i=0; i<sceneElementArray.size(); i++) {
    SceneElement thisElement =(SceneElement) sceneElementArray.get(i);
    println("Scene Element Loaded: " + thisElement.name + " for scene: " + thisElement.scene);
  }
}


/*============== saveXML =============*
 Creates the XML file using the info
 from the SceneElement that's passed to it.
*=========================================*/ 
void saveXML() {
  println("saveXML called");
  ArrayList elements = sceneElementArray;

  boolean xmlDeleted = false;
  if(media.countChildren() > 0) {
    println("there are childre nodes in the XML and they must be removed");
    for(int i = media.countChildren()-1; i > -1; i--) { // remove each scene
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
    for(int i=0; i<sceneArray.size(); i++){
      proxml.XMLElement scene = new proxml.XMLElement("scene"); 
      media.addChild(scene);
      String sceneName =(String) sceneArray.get(i);
      scene.addAttribute("number", sceneName); // this is a string
      println("scenes saved: " + sceneName);
    }  


   for(int i = 0; i<elements.size(); i++) {
      // get the SceneElement info for each item in the Array
     SceneElement thisElement=(SceneElement) elements.get(i);
     
     //reconstruct the node
     proxml.XMLElement element = new proxml.XMLElement("element");
     proxml.XMLElement file = new proxml.XMLElement("file");
     file.addAttribute("filename", thisElement.name);
     proxml.XMLElement pos = new proxml.XMLElement("pos");
     pos.addAttribute("x", thisElement.xPos);
     pos.addAttribute("y", thisElement.yPos);
     proxml.XMLElement dimensions = new proxml.XMLElement("dimensions");
     dimensions.addAttribute("w", thisElement.w); 
     dimensions.addAttribute("h", thisElement.h);  
     proxml.XMLElement triggers = new proxml.XMLElement("triggers");
    
    // print the node
     element.addChild(file);
     element.addChild(pos);
     element.addChild(dimensions);
     element.addChild(triggers);
     
     // Triggers are special, since the number of them vary.   
     // check for the number of items in the triggerList (ArrayList) and add elements accordingly.
     ArrayList thisTriggerList = thisElement.triggerList;
     int numTriggers = thisTriggerList.size();
     println("Number of Triggers: " + numTriggers);
     for(int k = 0; k<numTriggers; k++) {
       println(numTriggers + " triggers added for item in loop: " + k + " filename: " + thisElement.name);
       int triggerNum =(Integer) thisTriggerList.get(k);
       proxml.XMLElement trigger = new proxml.XMLElement("trigger");
       trigger.addAttribute("number", triggerNum);
       triggers.addChild(trigger);
     }

      
      // find the assigned scene from SceneElement.scene attribute;
      // TODO: I set this to 1 because thisElement.scene is now a STRING instead of an INT
      //int assignedScene = 1;
      String assignedScene = thisElement.scene;
      for (int j=0; j < media.countChildren(); j++) {  // go through all scene elements
        // store the looped scene in a temporary variable loopedScene
        proxml.XMLElement loopedScene = media.getChild(j);
         //println("media child: " + loopedScene);
         // save the number attribute of the scene
         String sceneNum = loopedScene.getAttribute("number"); 
          // if the looped Scene has the same attribute as the assignedScene from the SceneElement array... 
          if (sceneNum == assignedScene) {
            //.. then add this XML element within that scene
             loopedScene.addChild(element);
          }
      }    
      //println(thisElement.name + " XML element created");
   } 

  } 
  xmlIO.saveElement(media, "mediaoutput.xml");
}

