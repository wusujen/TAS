/*==============  sceneElement =============*
 when dropped onto the canvas, the Drop
 Canvas creates this object and puts it
 into an Arraylist of these objects.
 *=========================================*/
class SceneElement {
  int hash;
  String name;
  int trigger;
  int w;
  int h;
  int xPos;
  int yPos;
  int scene;
  String transition;

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
  }
  
  // this draws an actual rectangle instead of a button
  // later we can pass in PImages, and other parameters
  void drawSceneElement(){
    // the button code, just in case we need it again
    // Button b;
    // controlP5.addButton(name,val,xPos,yPos,w,h);
    
    fill(0);
    rect(xPos,yPos,w,h);
    
    fill(255);
    text(name,xPos,yPos);
  }
  
  
  // this changes the visual appearance of the object
  void hasBeenSelected(){
    stroke(255,0,0);
    fill(0);
    rect(xPos,yPos,w,h);
    fill(255,0,0);
    text(name,xPos,yPos);
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
  void updateSize(int newWidth, int newHeight){
    w=newWidth;
    h=newHeight;
  }
  void updatePosition(int newX, int newY){
    xPos=newX;
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
}

