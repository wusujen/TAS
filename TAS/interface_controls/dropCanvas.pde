/*==============  dropCanvas =============*
 the canvas that will hold all of the media
 files that the user drops onto it.
 *=========================================*/
class DropCanvas {
  int cs;
  int cf;
  int xPos;
  int yPos;
  int w;
  int h;

  String firstClicked;            // stores the name of the first item that was clicked
  String labelName;               // stores the name of the last created label
  int fileNumber;                 // keeps track of what files a loop has gone through
  int numberOfDroppedFiles=0;     // stores the total number of files in dropCanvas 

  DropCanvas(int dropCanvasStroke, int dropCanvasFill, int dropCanvasXPos, int dropCanvasYPos, int dropCanvasWidth, int dropCanvasHeight) {
    cs=dropCanvasStroke;
    cf=dropCanvasFill;
    xPos=dropCanvasXPos;
    yPos=dropCanvasYPos;
    w=dropCanvasWidth;
    h=dropCanvasHeight;
  }

  void drawDropCanvas() {
    stroke(cs);
    fill(cf);
    rect(xPos, yPos, w, h);
  }

  /*=============   detectDroppedItem   ===============*
   // this function detects whether or not the item has
   // been dropped into the area of the dropCanvas, and if
   // it has, create a sceneElement and put it into the sceneElementArray
   ====================================================*/
  void detectDroppedItem() {
    if (mouseX>xPos && mouseX<xPos+w && mouseY>yPos && mouseY<yPos+h && clickedItemName!=null) {
      // change the color of the mediaList item
      resetMediaListItemColor(color(230), color(230), color(240), color(180));

      // create a new label name for each label
      // this should be changed later into drawSceneElement() 
      labelName="label"+val;
      //label = controlP5.addTextlabel(labelName, clickedItemName, mouseX, mouseY);
      // increment the global hash before it's applied to the new sceneElement
       hash++;
      // TODO: remove hardcoded variables for obj height, width, scene number, transition.
      sceneElementArray.add(new SceneElement(hash,clickedItemName, 0, 120, 15, mouseX, mouseY, 1, "none"));
      SceneElement cake=(SceneElement) sceneElementArray.get(numberOfDroppedFiles);
      cake.drawSceneElement();
      numberOfDroppedFiles=sceneElementArray.size();

      // store the name of the first item that was clicked!
      if (itemClicked==1) {
        firstClicked=clickedItemName;
        itemClicked=itemClicked+2;
      }
      
      //Pass the new SceneElement named cake to update XML
      writeToXML(cake);
      //println("after drop: file object array size: " + sceneElementArray.size());
    }
    mouseDragging=false;
    clickedItemName=null;
    val=0;
  }

  /*========   removeDroppedItem   =========*
   removes a sceneElement that has been put into
   dropCanvas and changes the color of the
   listBoxItem
   *=========================================*/
  void removeDroppedItem() {
    
    // ISSUES WITH THIS FUNCTION.
    // it's being called multiple times because controlEvent is registering false positives.
    // I ignore them with the assumption that we'll
    // write a different removal function for each
    // piece of media
    
    // loop through the sceneElement array and check to see if the
    // name of the currently clicked object matches any of those names
    // if it does, then remove it and reset the listBoxItem color
    for (int i=0; i<sceneElementArray.size(); i++) {
      SceneElement file=(SceneElement) sceneElementArray.get(i);
      String droppedObjectName=file.objName();
      
      println("dropped Object Name: " + droppedObjectName);
      
      if ((clickedItemName==droppedObjectName) || (firstClicked==clickedItemName)) {
        resetClickedItemToDefault(clickedItemName);
        controlP5.remove(droppedObjectName);
        sceneElementArray.remove(i);
        numberOfDroppedFiles=sceneElementArray.size();
        println("This file must be removed");
        xmlRemoveItem(file);
      }
    }
    // get the default color the first time the item is clicked
    // increment the itemClicked to signify that the first item has been clicked
    if (itemClicked==0) {
      defaultColor=mediaList.item(clickedItemName).getColor();
      itemClicked=itemClicked+1;
    }
  }


  /*========   getsceneElementById   =========*
   returns a sceneElement when given an id.
   *=========================================*/
  SceneElement getSceneElementById(int id) {
    SceneElement file=(SceneElement) sceneElementArray.get(id);
    return file;
  }


  /*=======   getsceneElementByName   =========*
   returns a sceneElement when given a name.
   currently does not perform a check for 
   null.
   *=========================================*/
  SceneElement getSceneElementByName(String name) {
    SceneElement sampleFile;
    for (int i=0; i<sceneElementArray.size(); i++) {
      sampleFile=(SceneElement) sceneElementArray.get(i);
      if (name==sampleFile.objName()) {
        fileNumber=i;
      }
    }
    SceneElement file=(SceneElement) sceneElementArray.get(fileNumber);
    return file;
  }
}

