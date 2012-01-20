void drawLibrary(ArrayList media, String title,  int id, int libY){
  controlP5 = new ControlP5(this);
  mediaList = controlP5.addListBox("mediaList", libX , libY , libW, libH);
  mediaList.setId(id);
  mediaList.setItemHeight(15);
  mediaList.setBarHeight(15);
  mediaList.captionLabel().toUpperCase(true);
  mediaList.captionLabel().set(title);
  mediaList.captionLabel().style().marginTop = 3;
  mediaList.valueLabel().style().marginTop = 3; // the +/- sign
  
  for(int i=0;i<media.size();i++) {
    String raisin =(String) media.get(i);
    mediaList.addItem(raisin,i);
  }
}


/*==============  dragCursor =============*
 this is the cursor that follows the mouse
 when the item is clicked. Ideally, it would
 capture the name of the clicked item...but
 because of the way the control events 
 function, that is currently not possible.
*=========================================*/
void dragCursor(int x, int y, int w, int h){
  //fill(255);
  //textSize(9);
  //text(name,x+2,y+12);
  fill(150);
  noStroke();
  rect(x,y,w,h);
}


/*======  controlEvent(mediaList)  =======*
 this controlEvent applies to mediaList only.
 if an item is clicked in mediaList, then the
 item will be removed from mediaList.
*=========================================*/
void controlEvent(ControlEvent theEvent) {
  if(theEvent.isTab()){
    String tabName=theEvent.tab().name();
    if(tabName=="default"){
      tabName=(String) sceneArray.get(0);
    }
    if(activeScene!=tabName){
      activeScene=tabName;
      println("changed to:" + activeScene);
      activeElement=null;
      resetPropertyPanel();
    }
    return;
  }
  // checks to see if the group id matches the list we intend for it to.
  if (theEvent.isGroup() && ((theEvent.group().id()==1)||(theEvent.group().id()==2)||(theEvent.group().id()==3))) {
    // check the value of the group
    println(theEvent.group().value()+" from "+theEvent.group());
    
    // set the val for future use as an item id &
    // retrieve the respective item name from the correct mediaList array 
    // based upon which ListBox the item is being selected from!
    val=int(theEvent.group().value());
    if(theEvent.group().id()==1){
      clickedItemName=(String) imageFiles.get(val);
      println("imageFiles was clicked, and clicked Item Name is" + clickedItemName);
    }
    if(theEvent.group().id()==2){
      clickedItemName=(String) audioFiles.get(val);
      println("audioFiles was clicked, and clicked Item Name is" + clickedItemName);
    }
    if(theEvent.group().id()==3){
      clickedItemName=(String) movieFiles.get(val);
      println("movieFiles was clicked, and clicked Item Name is" + clickedItemName);
    }
    
    // up the dragList by 1, to keep track of what 
    // has been dragged & add an Item to the dragToList
    dragListID=dragListID+1;
    //dragToList.addItem(clickedItemName,dragListID);
    return;
  }  
  //println(theEvent.controller().name());
  if(theEvent.isController()){
    Controller controller=theEvent.controller();
    for(int i=0; i<9; i++){
      if(theEvent.controller().name().equals("bang"+i)){
        println("clicked bang"+i);
        ArrayList tList=activeElement.getMyTriggerList();
        if(tList.contains(i)){
          println("removed bang"+i);
          tList.remove(tList.indexOf(i));
          controller.setColorForeground(color(0));
        }
        else{
          println("added bang"+i);
          tList.add(i);
          controller.setColorForeground(color(255,0,0));
        }
        return;
      }
    }
  }
}

// Set to true if mouse is dragging
void mouseDragged(){
  mouseDragging=true;
  if(activeElement!=null && activeElement.bActivate){
    activeElement.xPos = mouseX - activeElement.mStartX;
    activeElement.yPos = mouseY - activeElement.mStartY;   
    
    //keep drag inside the bound
    if (activeElement.xPos < canvas.xPos){
      activeElement.xPos = canvas.xPos;
    }
    else if(activeElement.xPos+int(activeElement.w) > canvas.xPos+canvas.w){
      activeElement.xPos = canvas.xPos + canvas.w - int(activeElement.w);
    }
    if (activeElement.yPos < canvas.yPos){
      activeElement.yPos = canvas.yPos;
    }
    else if(activeElement.yPos+int(activeElement.h) > canvas.yPos+canvas.h){
      activeElement.yPos = canvas.yPos + canvas.h - int(activeElement.h);
    }
    
    //include the closeButton with its sceneElement
    activeElement.c.xPos = activeElement.xPos + int(activeElement.w);
    activeElement.c.yPos = activeElement.yPos;
  }
  //print("mPos = (" + activeElement.xPos + "," + activeElement.yPos + ")\n");
}

// If mouse is released within the bounds of the dropCanvas
// then set the position of the item to the location of the
// mouse
void mouseReleased(){
  canvas.detectDroppedItem();
  //select the activeElement to move 
   if(activeElement!=null){
   activeElement.bActivate = false;
   activeElement.c.pressedState = false;
   if (activeElement.isHoveringOverCloseButton(mouseX,mouseY)){
     //print ("remove the obj\n");
     canvas.removeDroppedItem();
   }
   
   activeElement.mEndX = mouseX;
   activeElement.mEndY = mouseY;
    //print("mEnd = (" + activeElement.mEndX + "," + activeElement.mEndY + ")\n");
   populatePropertyPanel();
  }
}

void mousePressed(){
  if (activeElement!=null){
    if(activeElement.isMouseOver(mouseX, mouseY)){
      activeElement.bActivate = true;   
      //if click on the close button
      if (activeElement.isHoveringOverCloseButton(mouseX,mouseY)){
        activeElement.c.overState = false;
        activeElement.c.pressedState = true;
        indexOfItemToRemove = sceneElementArray.indexOf(activeElement);
        println("activeElement.objName() ==> " + activeElement.objName());
        saveXML();
      }
      activeElement.mStartX = mouseX - activeElement.xPos;
      activeElement.mStartY = mouseY - activeElement.yPos;
      //print("mStart = (" + activeElement.mStartX + "," + activeElement.mStartY + ")\n");
    }
  }
}

void mouseMoved(){
  if (activeElement!=null){
    if (activeElement.isHoveringOverCloseButton(mouseX,mouseY)){
      activeElement.c.overState = true;
    }else
      activeElement.c.overState = false;
  }
}

void mouseClicked(){
  // check if SceneElement has been selected, as long
  // as the mouse is within the boundaries of the canvas
  if(canvas.mouseIsWithinDropCanvas()){
    selectSceneElement();
  }
}

