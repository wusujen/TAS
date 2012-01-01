/*========  createMediaList  =============*
 creates the initial media list from the
 media folder contents and stores the names
 into an array for further use
*=========================================*/
void createMediaList(String[] itemNameArray){
  controlP5 = new ControlP5(this);
  mediaList = controlP5.addListBox("mediaList",50,50,120,300);
  mediaList.setId(1);
  mediaList.setItemHeight(15);
  mediaList.setBarHeight(15);
  mediaList.captionLabel().toUpperCase(true);
  mediaList.captionLabel().set("Media Files");
  mediaList.captionLabel().style().marginTop = 3;
  mediaList.valueLabel().style().marginTop = 3; // the +/- sign
  for(int i=0;i<itemNameArray.length;i++) {
    mediaList.addItem(itemNameArray[i],i);
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

/*======  controlEvent(mediaList)  =======*
 this controlEvent applies to mediaList only.
 if an item is clicked in mediaList, then the
 item will be removed from mediaList.
*=========================================*/
void controlEvent(ControlEvent theEvent) {
  //checks to see if the group id matches the list we
  //intend for it to
  if (theEvent.isGroup() && theEvent.group().id()==1) {
    clicked=true;
    
    dragListID=dragListID+1;
    println(theEvent.group().value()+" from "+theEvent.group());
    val=int(theEvent.group().value());
    
    //Changes the original color of the clicked item
    //Adds the item to dragToList
    clickedItemName=itemNames[val];
    mediaList.item(clickedItemName).setColorBackground(color(255,0,0));
    mediaList.item(clickedItemName).setColorForeground(color(255,0,0));
    mediaList.item(clickedItemName).setColorActive(color(255,0,0));
    mediaList.item(clickedItemName).setColorLabel(color(255));
    
    println(clickedItemName);
    dragToList.addItem(clickedItemName,dragListID);
    
    tryButton = controlP5.addButton(clickedItemName,val,mouseX,mouseY,120,15);
  }
}

void mousePressed(ControlEvent theEvent,String dragItemName){
  if(clicked){
    tryButton.setPosition(mouseX,mouseY);
  }
}

void mouseReleased(){
  clicked=false;
}
