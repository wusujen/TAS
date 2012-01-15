/*==============  sceneElement =============*
 when dropped onto the canvas, the Drop
 Canvas creates this object and puts it
 into an Arraylist of these objects.
 *=========================================*/
class SceneElement {
  int hash;
  String name;
  int trigger; //this int is no longer in use. Please refer to triggerList below
  int w;
  int h;
  int xPos;
  int yPos;
  int scene;
  String transition;
  CloseButton c;

  ArrayList triggerList; //this arraylist holds a set of integers, each corresponding to a trigger that has been turned "on".
                         //the max number is 9.
  
  SceneElement(int objHash, String objFilename, int objTrigger, int objWidth, int objHeight, int objX, int objY, int objScene, String objTransition) {
    hash=objHash;
    name=objFilename;
    trigger=objTrigger;
    w=objWidth;
    h=objHeight;
    xPos=objX;
    yPos=objY;
    scene=objScene;
    transition=objTransition;
    c=new CloseButton(w+xPos,yPos,1,1,12);
    triggerList= new ArrayList();
  }
  
  // this draws an actual rectangle instead of a button
  // later we can pass in PImages, and other parameters
  void drawSceneElement(){
    // the button code, just in case we need it again
    // Button b;
    // controlP5.addButton(name,val,xPos,yPos,w,h);
    stroke(0);
    fill(0);
    rect(xPos,yPos,w,h);
    
    fill(0);
    textSize(10);
    textAlign(LEFT,BOTTOM);
    text(name,xPos,yPos);
    c.drawCloseButton();
  }
  
  
  // this changes the visual appearance of the object
  void hasBeenSelected(){
    stroke(255,0,0);
    fill(0);
    rect(xPos,yPos,w,h);
    fill(255,0,0);
    textSize(10);
    textAlign(LEFT,BOTTOM);
    text(name,xPos,yPos);
    c.drawCloseButton();
  }
  
  // this function is namely for testing purposes to see
  // the stats of each object
  void displayProperties(){
    println("success");
    println("hash: "+hash);
    println("name: "+name);
    println("trigger: "+trigger);
    println("width: "+w);
    println("height: "+h);
    println("x-position: "+xPos);
    println("y-position: "+yPos);
    println("scene: "+scene);
    println("transition: "+transition);
  }
  
  // this function returns the name of the given object
  String objName(){
    String nameOfObject=name;
    return nameOfObject;
  }
  
  // basic functions for updating the object
  void updateTrigger(int newTrigger){
    trigger=newTrigger;
  }
  void updateWidth(int newWidth){
    w=newWidth;
  }
  void updateHeight(int newHeight){
    h=newHeight;
  }
  void updateX(int newX){
    xPos=newX;
  }
  void updateY(int newY) {
    yPos=newY;
  }
  void updateTransition(String newTransition){
    transition=newTransition;
  }
  
  
  // this boolean returns true of mouse is over the sceneElement
  boolean isMouseOver(int mX, int mY){
    if((mX>xPos) && (mX<xPos+w) && (mY>yPos) && (mY<yPos+h)){
    return true;
    }
    return false;
  }
  
  boolean isHoveringOverCloseButton(int mX, int mY){
    if(c.isMouseOver(mX,mY)){
      return true;
    }
    return false;
  }
  
  String getMyName(){
    return name;
  }
  int getMyX(){
    return xPos;
  }
  int getMyY(){
    return yPos;
  }
  int getMyWidth(){
    return w;
  }
  int getMyHeight(){
    return h;
  }
  ArrayList getMyTriggerList(){
    return triggerList;
  }
}

class CloseButton{
  int xPos;
  int yPos; 
  int wh; //width & height of the square closebutton
  int marginTop;
  int marginRight;
  
  
  CloseButton(int xPosition, int yPosition, int mT, int mR,int widthAndHeight){
    xPos=xPosition;
    yPos=yPosition;
    marginTop=mT;
    marginRight=mR;
    wh=widthAndHeight;
  }
  
  void drawCloseButton(){
    stroke(0);
    fill(0);
    rect(xPos-(wh+marginTop),yPos+marginRight,wh,wh);
    
    fill(255);
    textAlign(CENTER,CENTER);
    textSize(wh-(wh/5));
    text("X",xPos-(wh/2),yPos+(wh/2));
  }
  
  void drawHoverState(){
    stroke(0);
    fill(255,0,0);
    rect(xPos-(wh+marginTop),yPos+marginRight,wh,wh);
    
    fill(255);
    textAlign(CENTER,CENTER);
    textSize(wh-(wh/5));
    text("X",xPos-(wh/2),yPos+(wh/2));
  }
  
  void drawPressedState(){
    stroke(0);
    fill(50);
    rect(xPos-(wh+marginTop),yPos+marginRight,wh,wh);
    
    fill(255);
    textAlign(CENTER,CENTER);
    textSize(wh-(wh/5));
    text("X",xPos-(wh/2),yPos+(wh/2));
  }
  
  // this boolean returns true of mouse is over the sceneElement
  boolean isMouseOver(int mX, int mY){
    if((mX>xPos) && (mX<xPos) && (mY>yPos) && (mY<yPos)){
    return true;
    }
    return false;
  }
}

