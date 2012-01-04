/*==============  dropCanvas =============*
 the canvas that will hold all of the media
 files that the user drops onto it.
*=========================================*/
class DropCanvas{
  int cs;
  int cf;
  int xPos;
  int yPos;
  int w;
  int h;
  
  int numberOfFileObjects=0;
  
  DropCanvas(int dropCanvasStroke, int dropCanvasFill, int dropCanvasXPos, int dropCanvasYPos, int dropCanvasWidth, int dropCanvasHeight){
    cs=dropCanvasStroke;
    cf=dropCanvasFill;
    xPos=dropCanvasXPos;
    yPos=dropCanvasYPos;
    w=dropCanvasWidth;
    h=dropCanvasHeight;
  }
  
  void drawDropCanvas(){
    stroke(cs);
    fill(cf);
    rect(xPos,yPos,w,h);
  }
  
  void detectDroppedItem(){
    if(mouseX>50 && mouseX<350 && mouseY>150 && mouseY<300 && clickedItemName!=null){
 
      numberOfFileObjects=numberOfFileObjects+2;
      println("This is the number of file objects: " + numberOfFileObjects);
      
      resetMediaListItemColor(color(230),color(230),color(240),color(180));
      
      int labelXpos=mouseX;
      int labelYpos=mouseY;
  
      labelName="label"+val;
      label = controlP5.addTextlabel(labelName, clickedItemName, labelXpos, labelYpos);
      
      println("X: "+ labelXpos + " , " + mouseX);
      println("Y: "+ labelYpos + " , " + mouseY);
      
      fileObjectArray.add(new FileObject(clickedItemName, null, 120, 15, labelXpos, labelYpos, 1, null));
      FileObject file=(FileObject) fileObjectArray.get(numberOfFileObjects-1);
      file.displayProperties();
      // store the name of the first item that was clicked!
      if(itemClicked==1){
        firstClicked=clickedItemName;
        println("I was first Clicked!: " + firstClicked);
        itemClicked=itemClicked+1;
      }
    }
    mouseDragging=false;
    clickedItemName=null;
    val=0;
  }
  
  void removeDroppedItem(){
    // check to see if the default color matches the current color of the item
   // if not, use as an indicator that the item has been clicked before
   // the first item usually slips through the cracks, so also check the
   // clicked name!
   if((mediaList.item(clickedItemName).getColor()!=defaultColor) || (firstClicked==clickedItemName)){
       resetClickedItemToDefault(clickedItemName);
       numberOfFileObjects=numberOfFileObjects-1;
       println("Number of File Objects decreased :" + numberOfFileObjects);
   }
   
    // get the default color the first time the item is clicked
    // increment the itemClicked to signify that the first item has been clicked
    if(itemClicked==0){
      defaultColor=mediaList.item(clickedItemName).getColor();
      itemClicked=itemClicked+1;
      println("defaultColor: " + defaultColor);
    }
  }
}

