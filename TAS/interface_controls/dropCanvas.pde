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
  
  /*====== mouseIsWithinDropCanvas  ======*
  // this returns true if mouse is within
  // the bounds of dropCanvas
  ========================================*/
  boolean mouseIsWithinDropCanvas(){
    if (mouseX>xPos && mouseX<xPos+w && mouseY>yPos && mouseY<yPos+h){
      return true;
    }
    return false;
  }

  /*=============   detectDroppedItem   ===============*
   // this function detects whether or not the item has
   // been dropped into the area of the dropCanvas, and if
   // it has, create a sceneElement and put it into the 
   // sceneElementArray
   ====================================================*/
  void detectDroppedItem() {
    if (mouseX>xPos && mouseX<xPos+w && mouseY>yPos && mouseY<yPos+h && clickedItemName!=null) {
      // create a new label name for each label
      labelName="label"+val;

      // TODO: remove hardcoded variables for obj height, width, scene number, transition.
      //sceneElementArray.add(new SceneElement(clickedItemName, 0, 120, 15, mouseX, mouseY, 1, "none"));
      //sceneElementArray.add(new SceneElement(clickedItemName, 120, 15, mouseX, mouseY, 1, new ArrayList()));

       // to select which image should be displayed, figure out
      // which type of file it is, and then based upon that get
      // the correct image check for acceptable image formats
      PImage myPImage=new PImage();
      if((clickedItemName.endsWith("png")) || (clickedItemName.endsWith("jpg")) || (clickedItemName.endsWith("jpeg")) || clickedItemName.endsWith("gif") ){ 
        println("image");
        myPImage=loadImage(sketchPath+"/media/"+clickedItemName);
      }
      // check for accetable audio formats
      else if( (clickedItemName.endsWith("aif")) || (clickedItemName.endsWith("mp3")) ){  
        println("audio");
        myPImage=loadImage(sketchPath+"/media/audio_icon.png");
      }
      // check for acceptable movie formats
      else if( (clickedItemName.endsWith("mov")) || (clickedItemName.endsWith("mp4")) ){  
        myPImage=loadImage(sketchPath+"/media/movie_icon.png");
      }
      else{
        println("this is an unacceptable file");
      }
      
      float futureWidth;
      float futureHeight;
      float cWidth=float(myPImage.width);
      float cHeight=float(myPImage.height);
      if (cWidth>cHeight){
        futureWidth=300;
        futureHeight=300*(cHeight/cWidth);
        println(futureHeight);
      }
      else{
        futureHeight=300;
        futureWidth=300*(cWidth/cHeight);
        println(futureWidth);
      }
      //sceneElement now automatically sets to the currentScene
      sceneElementArray.add(new SceneElement(clickedItemName, futureWidth, futureHeight, mouseX, mouseY, activeScene, new ArrayList()));

      SceneElement newSceneElement=(SceneElement) sceneElementArray.get(numberOfDroppedFiles);
      //newSceneElement.drawSceneElement();
      numberOfDroppedFiles=sceneElementArray.size();
      
    }
    mouseDragging=false;
    clickedItemName=null;
    val=0;
  }
  
  
/*========   removeDroppedItem   =========*
 removes a fileObject that has been put into
 dropCanvas and changes the color of the
 listBoxItem
*=========================================*/
  void removeDroppedItem(){
   // loop through the SceneElement array and check to see if the
   // name of the currently clicked object matches any of those names
   // if it does, then remove it and reset the listBoxItem color
   for(int i=0; i<sceneElementArray.size(); i++){
     SceneElement file = (SceneElement) sceneElementArray.get(i);
     String droppedObjectName = file.objName();
     //println("droppedObjectName ==> "+ droppedObjectName);
     //println("clickToDelete ==> " + clickToDelete);
     if(droppedObjectName==clickToDelete){
       controlP5.remove(clickToDelete);
       sceneElementArray.remove(i);
       numberOfDroppedFiles = sceneElementArray.size();
       break;
      }
   }
  }
/*========End of removeDroppedItem==============================*/


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

