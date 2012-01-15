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

/*========  createDragToList  =============*
 creates the list which represents the canvas
 the media files will eventually rest on.
*=========================================*/
void createDragToList(){
  //controlP5 = new ControlP5(this);
  dragToList = controlP5.addListBox("dragToList",200,50,120,300);
  dragToList.setId(2);
  dragToList.setItemHeight(15);
  dragToList.setBarHeight(15);

  dragToList.captionLabel().toUpperCase(true);
  dragToList.captionLabel().set("Drag To List");
  dragToList.captionLabel().style().marginTop = 3;
  dragToList.valueLabel().style().marginTop = 3; // the +/- sig
  dragToList.setColorBackground(color(164,170,183));
  dragToList.setColorActive(color(140,140,140));
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
  // checks to see if the group id matches the list we intend for it to.
  if (theEvent.isGroup() && theEvent.group().id()==1) {
    // check the value of the group
    // println(theEvent.group().value()+" from "+theEvent.group());
    
    // set the val for future use as an item id &
    // retrieve the respective item name from the itemName array
    val=int(theEvent.group().value());
    clickedItemName=itemNames[val];
    
    // up the dragList by 1, to keep track of what 
    // has been dragged & add an Item to the dragToList
    dragListID=dragListID+1;
    dragToList.addItem(clickedItemName,dragListID);
    return;
  }
  //println(theEvent.controller().name());
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

// Set to true if mouse is dragging
void mouseDragged(){
  mouseDragging=true;
}

// If mouse is released within the bounds of the dropCanvas
// then set the position of the item to the location of the
// mouse
void mouseReleased(){
  canvas.detectDroppedItem();
}
